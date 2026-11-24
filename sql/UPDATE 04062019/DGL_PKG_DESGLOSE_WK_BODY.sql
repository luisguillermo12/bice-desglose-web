create or replace PACKAGE BODY "DGL_PKG_DESGLOSE_WK" 
IS
  PROCEDURE LOG 
  (
    pi_cod_tipo_proceso     IN  VARCHAR2,
    pi_num_iden             IN  VARCHAR2,
    pi_cod_tipo_des         IN  VARCHAR2,
    pi_cod_subtipo_des      IN  VARCHAR2,
    pi_cod_moneda           IN  VARCHAR2,
    pi_msg                  IN  VARCHAR2,
    PI_COD_PLANTILLA        IN  VARCHAR2 DEFAULT '0'
  )
  IS
    -- Aislamiento de transaccion
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    INSERT INTO DGL_CHK_LOG_WK (COD_TIPO_PROCESO, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_LOG, GLS_LOG, cod_plantilla)
                     VALUES (pi_cod_tipo_proceso, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, SYSTIMESTAMP, pi_msg, PI_COD_PLANTILLA );
    COMMIT;
  END LOG;

  PROCEDURE DGL_SP_GET_LINK_PRINT_WK
  (
    pi_num_iden         IN  VARCHAR2,
    pi_cod_tipo_des     IN  VARCHAR2,
    pi_cod_subtipo_des  IN  VARCHAR2,
    pi_cod_moneda       IN  VARCHAR2,
    po_gls_link_print   OUT VARCHAR2,
    pi_cod_plantilla    IN  VARCHAR2 DEFAULT '0'
  )  
  IS
    v_val_param_rep VARCHAR2(100);  
  BEGIN
    FOR c_cfg_pdf IN (  SELECT 1 AS PRIO, R.COD_GENERICO AS ORDEN, R.GLS_GENERICACORTA AS COD_CAMPO, R.GLS_GENERICALARGA AS PARAM
                          FROM BCT_VIEW_DESCOD R
                         WHERE R.COD_TABLA = 'DGLCONFG'
                           AND R.COD_GENERICO LIKE 'URL_PDF_BASE%'
                        ORDER BY PRIO, ORDEN
                     )
    LOOP
      IF c_cfg_pdf.COD_CAMPO = 'SERVIDOR' THEN
        po_gls_link_print := c_cfg_pdf.PARAM;
      ELSE 
        v_val_param_rep := CASE WHEN c_cfg_pdf.COD_CAMPO = 'COD_CLIENTE' THEN pi_num_iden ELSE 
                           CASE WHEN c_cfg_pdf.COD_CAMPO = 'COD_TIPO_DES' THEN pi_cod_tipo_des ELSE 
                           CASE WHEN c_cfg_pdf.COD_CAMPO = 'COD_SUBTIPO_DES' THEN pi_cod_subtipo_des ELSE 
                           CASE WHEN c_cfg_pdf.COD_CAMPO = 'COD_MONEDA' THEN pi_cod_moneda ELSE 
                           CASE WHEN c_cfg_pdf.COD_CAMPO = 'COD_PLANTILLA' THEN pi_cod_plantilla ELSE '' END END END END END;        
        po_gls_link_print := po_gls_link_print || '&' || c_cfg_pdf.PARAM || v_val_param_rep;
      END IF;
    END LOOP;    
  END DGL_SP_GET_LINK_PRINT_WK;
  
  PROCEDURE DGL_SP_GEN_ENV_MAIL_WK
  (
    PI_NUM_IDEN          IN VARCHAR2,
    PI_COD_TIPO_DES      IN VARCHAR2,
    PI_COD_SUBTIPO_DES   IN VARCHAR2,
    PI_COD_MONEDA        IN VARCHAR2,
    PI_COD_EST_NVO       IN VARCHAR2,
    PI_COD_EST_ANT       IN VARCHAR2,
    PI_COD_PLANTILLA     IN VARCHAR2 DEFAULT '0'
  )
  IS
    -- Aislamiento de transaccion
    PRAGMA AUTONOMOUS_TRANSACTION;

    C_COD_EST_CUADRADO   CONSTANT VARCHAR2(5) := 'BV';
    C_COD_EST_ENV_A_REV  CONSTANT VARCHAR2(5) := 'BT';
    C_COD_EST_CON_OBSERV CONSTANT VARCHAR2(5) := 'OBS';
    C_COD_EST_VALIDADO   CONSTANT VARCHAR2(5) := 'DEF';
    C_COD_EST_CORREGIDO  CONSTANT VARCHAR2(5) := 'DEFM';
    C_LARGO_ASUNTO       CONSTANT NUMBER:= 150;
    C_LARGO_PREFIJO_MAIL CONSTANT NUMBER:= 10;

    v_count              NUMBER := 0;
    v_num_rol_ejecutor   VARCHAR2(7);
    v_gls_usr_ejecutor   VARCHAR2(30);

    v_smtp_host          VARCHAR2(100);
    v_smtp_port          VARCHAR2(100);
    v_num_max_reintento  NUMBER;

    v_mail_from_name     VARCHAR2(80);
    v_mail_from_mail     VARCHAR2(80);

    v_gls_header_html    VARCHAR2(200);
    v_gls_footer_html    VARCHAR2(200);

    v_gls_text_subject   VARCHAR2(200);
    v_gls_text_subj_full VARCHAR2(200);
    v_gls_text_header    VARCHAR2(200);
    v_gls_text_body      VARCHAR2(500);
    v_gls_text_body_full LONG;
    v_gls_text_footer    VARCHAR2(200);
    v_gls_link_printer   VARCHAR2(200);

    v_cod_estado         VARCHAR2(3);
    v_gls_respuesta      VARCHAR2(4000);
    v_cod_retorno        VARCHAR2(3);

    v_cur_info_dest      SYS_REFCURSOR;    
    v_cur_info_clon      SYS_REFCURSOR;    
    v_gls_tipo_desglose  VARCHAR2(20);
    v_num_solicitud      NUMBER;
    v_num_trx_sistema    NUMBER;
    v_nom_cliente        VARCHAR2(140) := 'SIN NOMBRE';
    v_num_envio          NUMBER;
    v_gls_text_reenvio   VARCHAR2(20);
    v_largo_nombre       NUMBER;
    v_largo              NUMBER;
    v_flg_mail           NUMBER;
    v_rol_reem           VARCHAR2(7);
    v_flg_reem           NUMBER;
    v_nom                VARCHAR2(140);
    v_mai                VARCHAR2(100);
    v_tip                VARCHAR2(5);

  BEGIN

    v_gls_text_subj_full := '';
    v_gls_header_html := '<html><head>' || chr(10);
    v_gls_header_html := v_gls_header_html || '<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"></head><body>' || chr(10);
    v_gls_header_html := v_gls_header_html || '<font color="#000000" size="2" face="Tahoma">' || chr(10);    
    v_gls_footer_html := '</font>' || chr(10);
    v_gls_footer_html := v_gls_footer_html || '</body>' || chr(10);
    v_gls_footer_html := v_gls_footer_html || '</html>' || chr(10);

    -- Se obtiene nombre de cliente
    -- Se comenta este bloque porque no se logra otorgar permisos correctos sobre la vista "CIF_VIEW_CLIENTES_ONLINE".
    /*BEGIN
      SELECT COMPLETO AS GLS_NOMBRE_CLIENTE 
        INTO v_nom_cliente
        FROM CIF_VIEW_CLIENTES_ONLINE WHERE NUM_IDEN = pi_num_iden;
      EXCEPTION 
        WHEN OTHERS THEN
          v_nom_cliente:= 'SIN NOMBRE';
    END;*/

    -- Determinamos la glosa del tipo desglose
    SELECT DECODE(pi_cod_tipo_des, 0, 'Individual', 'Consolidado') INTO v_gls_tipo_desglose FROM DUAL;

    -- Obtenemos la informacion del ejecutor del desglose
    SELECT NVL(D.NOM_USER_MODIF, D.NOM_USER_CREA) AS LOGIN, SF.OPERATOR_CODE AS ROL, NVL(NUM_DEVOL_EJECUTOR, 0) AS NUM_DEVOL_EJECUTOR
      INTO v_gls_usr_ejecutor, v_num_rol_ejecutor, v_num_envio
      FROM DESGLO_WK D
     INNER JOIN SF_STF_OPERATOR SF ON ( NVL(D.NOM_USER_MODIF, D.NOM_USER_CREA) = SF.LOGIN_NAME ) 
     WHERE D.COD_CLIENTE = pi_num_iden
       AND D.COD_TIPO_DES = pi_cod_tipo_des
       AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
       AND D.COD_MONEDA = pi_cod_moneda
       AND D.COD_PLANTILLA = PI_COD_PLANTILLA
       AND ROWNUM < 2;   

    IF v_num_envio > 0 THEN
      v_gls_text_reenvio:= '- Desglose Corregido';
    END IF;

    LOG (C_COD_PROC_MAIL, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, 'CIF_SP_GEN_ENV_MAIL: pi_cod_est_nvo['||pi_cod_est_nvo||'] pi_cod_est_ant['||pi_cod_est_ant||'] v_gls_usr_ejecutor['||v_gls_usr_ejecutor||'] v_num_rol_ejecutor['||v_num_rol_ejecutor||']', PI_COD_PLANTILLA);      

    --------------------------------------------------------------------------------------------------------------------------------------------
    -- Envio de mail al momento de enviar a revision
    IF pi_cod_est_nvo = C_COD_EST_ENV_A_REV AND pi_cod_est_ant = C_COD_EST_CUADRADO THEN

      -- Obtenemos la informacion del remitente 
      -- EJECUTOR
      BEGIN
        SELECT TUE.FIRST_NAME || ' ' || TUE.LAST_NAME AS NOMBRE, NVL(NVL(TUE.EMAIL, EE.GLS_EMAIL), CFR.GLS_GENERICALARGA) AS MAIL
          INTO v_mail_from_name, v_mail_from_mail
          FROM SF_STF_OPERATOR SFE 
         INNER JOIN TURBINE_USER TUE ON ( TUE.LOGIN_NAME = SFE.LOGIN_NAME )  
          LEFT JOIN DESCOD CFR ON ( CFR.COD_REG_STATUS = 0 AND CFR.COD_TABLA = 'DGLCONFG' AND CFR.COD_GENERICO = 'DGL_MAIL_FROM'  )
          LEFT JOIN EJECTA EE ON (EE.COD_REG_STATUS = 0 AND EE.NUM_ROLEMPLEADO = SFE.OPERATOR_CODE )  
         WHERE SFE.OPERATOR_CODE = v_num_rol_ejecutor
           AND ROWNUM < 2;
      EXCEPTION 
        WHEN OTHERS THEN
        LOG (C_COD_PROC_MAIL, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, 'CIF_SP_GEN_ENV_MAIL: Problemas para determinar el remitente segun v_num_rol_ejecutor['||v_num_rol_ejecutor||'] ', PI_COD_PLANTILLA);
        RETURN;
      END;

      -- Obtenemos la informacion de los destinatarios
      -- REVISOR
      OPEN v_cur_info_dest 
        FOR       
          SELECT DECODE(SFT.OPERATOR_CODE, NULL, TUR.FIRST_NAME || ' ' || TUR.LAST_NAME, TUT.FIRST_NAME || ' ' || TUT.LAST_NAME) AS NOMBRE, 
                 DECODE(SFT.OPERATOR_CODE, NULL, NVL(NVL(TUR.EMAIL, ER.GLS_EMAIL), CTO.GLS_GENERICALARGA ), NVL(NVL(TUT.EMAIL, ET.GLS_EMAIL), CTO.GLS_GENERICALARGA )) AS MAIL, 'TO' AS TIPO  
            FROM SF_STF_OPERATOR SFR 
           INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFR.LOGIN_NAME )
            LEFT JOIN desglo_grupo_wk G ON ( G.DES_NUM_ROL_EMPLEADO = SFR.OPERATOR_CODE )
            LEFT JOIN SF_STF_OPERATOR SFT ON (G.NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
            LEFT JOIN EJECTA ER ON (ER.COD_REG_STATUS = 0 AND ER.NUM_ROLEMPLEADO = G.NUM_ROL_EMPLEADO )  
            LEFT JOIN TURBINE_USER TUT ON ( TUT.LOGIN_NAME = SFT.LOGIN_NAME )  
            LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )
            LEFT JOIN EJECTA ET ON (ET.COD_REG_STATUS = 0 AND ET.NUM_ROLEMPLEADO = G.DES_NUM_ROL_EMPLEADO )  
           WHERE SFR.OPERATOR_CODE = v_num_rol_ejecutor
             AND ROWNUM < 2     
          UNION
          SELECT UPPER(TUR.FIRST_NAME || ' ' || TUR.LAST_NAME) AS NOMBRE, 
                 NVL(TUR.EMAIL, CTO.GLS_GENERICALARGA ) AS MAIL,
                 'TO' AS TIPO  
            FROM (SELECT G.NUM_ROL_EMPLEADO, 
                         DECODE(SFR.OPERATOR_CODE, NULL, TUR.FIRST_NAME || ' ' || TUR.LAST_NAME, TUT.FIRST_NAME || ' ' || TUT.LAST_NAME) AS NOMBRE, 
                         DECODE(SFR.OPERATOR_CODE, NULL, NVL(NVL(TUR.EMAIL, ER.GLS_EMAIL), CTO.GLS_GENERICALARGA ), NVL(NVL(TUT.EMAIL, ET.GLS_EMAIL), CTO.GLS_GENERICALARGA )) AS MAIL,
                         'TO' AS TIPO  
                    FROM SF_STF_OPERATOR SFR 
                   INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFR.LOGIN_NAME )
                    LEFT JOIN desglo_grupo_wk G ON ( G.DES_NUM_ROL_EMPLEADO = SFR.OPERATOR_CODE )
                    LEFT JOIN SF_STF_OPERATOR SFT ON (G.NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
                    LEFT JOIN EJECTA ER ON (ER.COD_REG_STATUS = 0 AND ER.NUM_ROLEMPLEADO = G.NUM_ROL_EMPLEADO )  
                    LEFT JOIN TURBINE_USER TUT ON ( TUT.LOGIN_NAME = SFT.LOGIN_NAME )  
                    LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )
                    LEFT JOIN EJECTA ET ON (ET.COD_REG_STATUS = 0 AND ET.NUM_ROLEMPLEADO = G.DES_NUM_ROL_EMPLEADO )  
                   WHERE SFR.OPERATOR_CODE = v_num_rol_ejecutor
                     AND ROWNUM < 2
                     ) D
            LEFT JOIN desglo_ree_wk REE ON (REE.NUM_ROL_EMPLEADO = D.NUM_ROL_EMPLEADO AND REE.COD_REG_STATUS = 0)
            LEFT JOIN SF_STF_OPERATOR SFT ON (REE.DES_NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
           INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFT.LOGIN_NAME )
            LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )           
           UNION
           SELECT CC.GLS_GENERICACORTA AS NOMBRE, CC.GLS_GENERICALARGA AS EMAIL, 'CC' AS TIPO 
             FROM DESCOD CC
            WHERE CC.COD_REG_STATUS = 0 
              AND CC.COD_TABLA = 'DGLCONFG' 
              AND CC.COD_GENERICO = 'DGL_MAIL_CC' 
           UNION
           SELECT BCC.GLS_GENERICACORTA AS NOMBRE, BCC.GLS_GENERICALARGA AS EMAIL, 'BCC' AS TIPO 
             FROM DESCOD BCC
            WHERE BCC.COD_REG_STATUS = 0 
              AND BCC.COD_TABLA = 'DGLCONFG' 
              AND BCC.COD_GENERICO = 'DGL_MAIL_BCC';

      OPEN v_cur_info_clon 
        FOR       
          SELECT DECODE(SFT.OPERATOR_CODE, NULL, TUR.FIRST_NAME || ' ' || TUR.LAST_NAME, TUT.FIRST_NAME || ' ' || TUT.LAST_NAME) AS NOMBRE, 
                 DECODE(SFT.OPERATOR_CODE, NULL, NVL(NVL(TUR.EMAIL, ER.GLS_EMAIL), CTO.GLS_GENERICALARGA ), NVL(NVL(TUT.EMAIL, ET.GLS_EMAIL), CTO.GLS_GENERICALARGA )) AS MAIL, 'TO' AS TIPO  
            FROM SF_STF_OPERATOR SFR 
           INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFR.LOGIN_NAME )
            LEFT JOIN desglo_grupo_wk G ON ( G.DES_NUM_ROL_EMPLEADO = SFR.OPERATOR_CODE )
            LEFT JOIN SF_STF_OPERATOR SFT ON (G.NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
            LEFT JOIN EJECTA ER ON (ER.COD_REG_STATUS = 0 AND ER.NUM_ROLEMPLEADO = G.NUM_ROL_EMPLEADO )  
            LEFT JOIN TURBINE_USER TUT ON ( TUT.LOGIN_NAME = SFT.LOGIN_NAME )  
            LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )
            LEFT JOIN EJECTA ET ON (ET.COD_REG_STATUS = 0 AND ET.NUM_ROLEMPLEADO = G.DES_NUM_ROL_EMPLEADO )  
           WHERE SFR.OPERATOR_CODE = v_num_rol_ejecutor
             AND ROWNUM < 2     
          UNION
          SELECT UPPER(TUR.FIRST_NAME || ' ' || TUR.LAST_NAME) AS NOMBRE, 
                 NVL(TUR.EMAIL, CTO.GLS_GENERICALARGA ) AS MAIL,
                 'TO' AS TIPO  
            FROM (SELECT G.NUM_ROL_EMPLEADO, 
                         DECODE(SFR.OPERATOR_CODE, NULL, TUR.FIRST_NAME || ' ' || TUR.LAST_NAME, TUT.FIRST_NAME || ' ' || TUT.LAST_NAME) AS NOMBRE, 
                         DECODE(SFR.OPERATOR_CODE, NULL, NVL(NVL(TUR.EMAIL, ER.GLS_EMAIL), CTO.GLS_GENERICALARGA ), NVL(NVL(TUT.EMAIL, ET.GLS_EMAIL), CTO.GLS_GENERICALARGA )) AS MAIL,
                         'TO' AS TIPO  
                    FROM SF_STF_OPERATOR SFR 
                   INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFR.LOGIN_NAME )
                    LEFT JOIN desglo_grupo_wk G ON ( G.DES_NUM_ROL_EMPLEADO = SFR.OPERATOR_CODE )
                    LEFT JOIN SF_STF_OPERATOR SFT ON (G.NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
                    LEFT JOIN EJECTA ER ON (ER.COD_REG_STATUS = 0 AND ER.NUM_ROLEMPLEADO = G.NUM_ROL_EMPLEADO )  
                    LEFT JOIN TURBINE_USER TUT ON ( TUT.LOGIN_NAME = SFT.LOGIN_NAME )  
                    LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )
                    LEFT JOIN EJECTA ET ON (ET.COD_REG_STATUS = 0 AND ET.NUM_ROLEMPLEADO = G.DES_NUM_ROL_EMPLEADO )  
                   WHERE SFR.OPERATOR_CODE = v_num_rol_ejecutor
                     AND ROWNUM < 2
                     ) D
            LEFT JOIN desglo_ree_wk REE ON (REE.NUM_ROL_EMPLEADO = D.NUM_ROL_EMPLEADO AND REE.COD_REG_STATUS = 0)
            LEFT JOIN SF_STF_OPERATOR SFT ON (REE.DES_NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
           INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFT.LOGIN_NAME )
            LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )           
           UNION
           SELECT CC.GLS_GENERICACORTA AS NOMBRE, CC.GLS_GENERICALARGA AS EMAIL, 'CC' AS TIPO 
             FROM DESCOD CC
            WHERE CC.COD_REG_STATUS = 0 
              AND CC.COD_TABLA = 'DGLCONFG' 
              AND CC.COD_GENERICO = 'DGL_MAIL_CC' 
           UNION
           SELECT BCC.GLS_GENERICACORTA AS NOMBRE, BCC.GLS_GENERICALARGA AS EMAIL, 'BCC' AS TIPO 
             FROM DESCOD BCC
            WHERE BCC.COD_REG_STATUS = 0 
              AND BCC.COD_TABLA = 'DGLCONFG' 
              AND BCC.COD_GENERICO = 'DGL_MAIL_BCC';

      -- Obtenemos el link de impresion del desglose
      DGL_SP_GET_LINK_PRINT_WK(pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_link_printer, PI_COD_PLANTILLA);

      v_gls_text_subject := 'Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ') - Revisar' || ' ' || v_gls_text_reenvio;
      v_largo:= LENGTH(v_gls_text_subject);
      v_largo_nombre:= C_LARGO_ASUNTO - (C_LARGO_PREFIJO_MAIL + v_largo + 4);
      v_gls_text_subject := 'Rut '|| pi_num_iden || ' -' || SUBSTR(v_nom_cliente,1,v_largo_nombre) || ' ('|| v_gls_tipo_desglose || ') - Revisar' || ' ' || v_gls_text_reenvio;      


      v_gls_text_body := '<p>Revisar Desglose Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ')' || '<BR><BR>' || chr(10) || chr(10);    
      v_gls_text_body := v_gls_text_body || '<a href=''' || v_gls_link_printer || '''>Visualizar Desglose</a>' || chr(10);

      v_count := 1;
    END IF;

    --------------------------------------------------------------------------------------------------------------------------------------------
    IF pi_cod_est_nvo IN ( C_COD_EST_CON_OBSERV, C_COD_EST_CORREGIDO, C_COD_EST_VALIDADO) AND pi_cod_est_ant <> pi_cod_est_nvo THEN

      v_count := 1;

      -- Obtenemos la informacion del remitente 
      -- EJECUTOR
      SELECT TUE.FIRST_NAME || ' ' || TUE.LAST_NAME AS NOMBRE, NVL(NVL(TUE.EMAIL, EE.GLS_EMAIL), CFR.GLS_GENERICALARGA) AS MAIL
        INTO v_mail_from_name, v_mail_from_mail
        FROM SF_STF_OPERATOR SFE 
       INNER JOIN TURBINE_USER TUE ON ( TUE.LOGIN_NAME = SFE.LOGIN_NAME )  
        LEFT JOIN DESCOD CFR ON ( CFR.COD_REG_STATUS = 0 AND CFR.COD_TABLA = 'DGLCONFG' AND CFR.COD_GENERICO = 'DGL_MAIL_FROM'  )
        LEFT JOIN EJECTA EE ON (EE.COD_REG_STATUS = 0 AND EE.NUM_ROLEMPLEADO = SFE.OPERATOR_CODE )  
       WHERE SFE.OPERATOR_CODE = v_num_rol_ejecutor
         AND ROWNUM < 2;

      OPEN v_cur_info_dest 
        FOR       
          SELECT DECODE(SFT.OPERATOR_CODE, NULL, TUR.FIRST_NAME || ' ' || TUR.LAST_NAME, TUT.FIRST_NAME || ' ' || TUT.LAST_NAME) AS NOMBRE, 
                 DECODE(SFT.OPERATOR_CODE, NULL, NVL(NVL(TUR.EMAIL, ER.GLS_EMAIL), CTO.GLS_GENERICALARGA ), NVL(NVL(TUT.EMAIL, ET.GLS_EMAIL), CTO.GLS_GENERICALARGA )) AS MAIL, 'TO' AS TIPO  
            FROM SF_STF_OPERATOR SFR 
           INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFR.LOGIN_NAME )
            LEFT JOIN desglo_grupo_wk G ON ( G.DES_NUM_ROL_EMPLEADO = SFR.OPERATOR_CODE )
            LEFT JOIN SF_STF_OPERATOR SFT ON (G.NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
            LEFT JOIN EJECTA ER ON (ER.COD_REG_STATUS = 0 AND ER.NUM_ROLEMPLEADO = G.NUM_ROL_EMPLEADO )  
            LEFT JOIN TURBINE_USER TUT ON ( TUT.LOGIN_NAME = SFT.LOGIN_NAME )  
            LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )
            LEFT JOIN EJECTA ET ON (ET.COD_REG_STATUS = 0 AND ET.NUM_ROLEMPLEADO = G.DES_NUM_ROL_EMPLEADO )  
           WHERE SFR.OPERATOR_CODE = v_num_rol_ejecutor
             AND ROWNUM < 2     
          UNION
          SELECT UPPER(TUR.FIRST_NAME || ' ' || TUR.LAST_NAME) AS NOMBRE, 
                 NVL(TUR.EMAIL, CTO.GLS_GENERICALARGA ) AS MAIL,
                 'TO' AS TIPO  
            FROM (SELECT G.NUM_ROL_EMPLEADO, 
                         DECODE(SFR.OPERATOR_CODE, NULL, TUR.FIRST_NAME || ' ' || TUR.LAST_NAME, TUT.FIRST_NAME || ' ' || TUT.LAST_NAME) AS NOMBRE, 
                         DECODE(SFR.OPERATOR_CODE, NULL, NVL(NVL(TUR.EMAIL, ER.GLS_EMAIL), CTO.GLS_GENERICALARGA ), NVL(NVL(TUT.EMAIL, ET.GLS_EMAIL), CTO.GLS_GENERICALARGA )) AS MAIL,
                         'TO' AS TIPO  
                    FROM SF_STF_OPERATOR SFR 
                   INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFR.LOGIN_NAME )
                    LEFT JOIN desglo_grupo_wk G ON ( G.DES_NUM_ROL_EMPLEADO = SFR.OPERATOR_CODE )
                    LEFT JOIN SF_STF_OPERATOR SFT ON (G.NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
                    LEFT JOIN EJECTA ER ON (ER.COD_REG_STATUS = 0 AND ER.NUM_ROLEMPLEADO = G.NUM_ROL_EMPLEADO )  
                    LEFT JOIN TURBINE_USER TUT ON ( TUT.LOGIN_NAME = SFT.LOGIN_NAME )  
                    LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )
                    LEFT JOIN EJECTA ET ON (ET.COD_REG_STATUS = 0 AND ET.NUM_ROLEMPLEADO = G.DES_NUM_ROL_EMPLEADO )  
                   WHERE SFR.OPERATOR_CODE = v_num_rol_ejecutor
                     AND ROWNUM < 2
                     ) D
            LEFT JOIN desglo_ree_wk REE ON (REE.NUM_ROL_EMPLEADO = D.NUM_ROL_EMPLEADO AND REE.COD_REG_STATUS = 0)
            LEFT JOIN SF_STF_OPERATOR SFT ON (REE.DES_NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
           INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFT.LOGIN_NAME )
            LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )           
          UNION
          SELECT CC.GLS_GENERICACORTA AS NOMBRE, CC.GLS_GENERICALARGA AS EMAIL, 'CC' AS TIPO 
            FROM DESCOD CC
           WHERE CC.COD_REG_STATUS = 0 
             AND CC.COD_TABLA = 'DGLCONFG' 
             AND CC.COD_GENERICO = 'DGL_MAIL_CC' 
          UNION
          SELECT BCC.GLS_GENERICACORTA AS NOMBRE, BCC.GLS_GENERICALARGA AS EMAIL, 'BCC' AS TIPO 
            FROM DESCOD BCC
           WHERE BCC.COD_REG_STATUS = 0 
             AND BCC.COD_TABLA = 'DGLCONFG' 
             AND BCC.COD_GENERICO = 'DGL_MAIL_BCC';

      OPEN v_cur_info_clon 
        FOR       
          SELECT DECODE(SFT.OPERATOR_CODE, NULL, TUR.FIRST_NAME || ' ' || TUR.LAST_NAME, TUT.FIRST_NAME || ' ' || TUT.LAST_NAME) AS NOMBRE, 
                 DECODE(SFT.OPERATOR_CODE, NULL, NVL(NVL(TUR.EMAIL, ER.GLS_EMAIL), CTO.GLS_GENERICALARGA ), NVL(NVL(TUT.EMAIL, ET.GLS_EMAIL), CTO.GLS_GENERICALARGA )) AS MAIL, 'TO' AS TIPO  
            FROM SF_STF_OPERATOR SFR 
           INNER JOIN TURBINE_USER TUR ON ( TUR.LOGIN_NAME = SFR.LOGIN_NAME )
            LEFT JOIN desglo_grupo_wk G ON ( G.DES_NUM_ROL_EMPLEADO = SFR.OPERATOR_CODE )
            LEFT JOIN SF_STF_OPERATOR SFT ON (G.NUM_ROL_EMPLEADO = SFT.OPERATOR_CODE)
            LEFT JOIN EJECTA ER ON (ER.COD_REG_STATUS = 0 AND ER.NUM_ROLEMPLEADO = G.NUM_ROL_EMPLEADO )  
            LEFT JOIN TURBINE_USER TUT ON ( TUT.LOGIN_NAME = SFT.LOGIN_NAME )  
            LEFT JOIN DESCOD CTO ON ( CTO.COD_REG_STATUS = 0 AND CTO.COD_TABLA = 'DGLCONFG' AND CTO.COD_GENERICO = 'DGL_MAIL_TO'  )
            LEFT JOIN EJECTA ET ON (ET.COD_REG_STATUS = 0 AND ET.NUM_ROLEMPLEADO = G.DES_NUM_ROL_EMPLEADO )  
           WHERE SFR.OPERATOR_CODE = v_num_rol_ejecutor
             AND ROWNUM < 2     
          UNION
          SELECT CC.GLS_GENERICACORTA AS NOMBRE, CC.GLS_GENERICALARGA AS EMAIL, 'CC' AS TIPO 
            FROM DESCOD CC
           WHERE CC.COD_REG_STATUS = 0 
             AND CC.COD_TABLA = 'DGLCONFG' 
             AND CC.COD_GENERICO = 'DGL_MAIL_CC' 
          UNION
          SELECT BCC.GLS_GENERICACORTA AS NOMBRE, BCC.GLS_GENERICALARGA AS EMAIL, 'BCC' AS TIPO 
            FROM DESCOD BCC
           WHERE BCC.COD_REG_STATUS = 0 
             AND BCC.COD_TABLA = 'DGLCONFG' 
             AND BCC.COD_GENERICO = 'DGL_MAIL_BCC';

      -- Envio de mail al momento de devolver desglose 
      IF ( pi_cod_est_nvo = C_COD_EST_CON_OBSERV  ) AND pi_cod_est_ant <> C_COD_EST_CON_OBSERV THEN
        v_gls_text_subject := 'Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ') - Con Observaciones' || ' ' || v_gls_text_reenvio;
        v_largo:= LENGTH(v_gls_text_subject);
        v_largo_nombre:= C_LARGO_ASUNTO - (C_LARGO_PREFIJO_MAIL + v_largo + 4);
        v_gls_text_subject := 'Rut '|| pi_num_iden || ' -' || SUBSTR(v_nom_cliente,1,v_largo_nombre) || ' ('|| v_gls_tipo_desglose || ') - Con Observaciones' || ' ' || v_gls_text_reenvio;

        v_gls_text_body := '<p>Revisar Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ') con observaciones, favor corregir y enviar nuevamente a validacion.</p>' || chr(10);
      END IF;

      --------------------------------------------------------------------------------------------------------------------------------------------
      -- Envio de mail al momento de devolver corregir desglose
      IF pi_cod_est_nvo = C_COD_EST_CORREGIDO AND pi_cod_est_ant <> C_COD_EST_CORREGIDO THEN
        v_gls_text_subject := 'Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ') - Corregido' || ' ' || v_gls_text_reenvio;        
        v_largo:= LENGTH(v_gls_text_subject);
        v_largo_nombre:= C_LARGO_ASUNTO - (C_LARGO_PREFIJO_MAIL + v_largo + 4);
        v_gls_text_subject := 'Rut '|| pi_num_iden || ' -' || SUBSTR(v_nom_cliente,1,v_largo_nombre) || ' ('|| v_gls_tipo_desglose || ') - Corregido' || ' ' || v_gls_text_reenvio;        
        v_gls_text_body := '<p>Desglose Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ') corregido por revisor, favor imprimir y enviar por mail a ejecutivo.</p>' || chr(10);
      END IF;

      --------------------------------------------------------------------------------------------------------------------------------------------
      -- Envio de mail al momento de devolver corregir desglose
      IF pi_cod_est_nvo = C_COD_EST_VALIDADO AND pi_cod_est_ant <> C_COD_EST_VALIDADO THEN
        v_gls_text_subject := 'Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ') - Validado' || ' ' || v_gls_text_reenvio;        
        v_largo:= LENGTH(v_gls_text_subject);
        v_largo_nombre:= C_LARGO_ASUNTO - (C_LARGO_PREFIJO_MAIL + v_largo + 4);
        v_gls_text_subject := 'Rut '|| pi_num_iden || ' -' || SUBSTR(v_nom_cliente,1,v_largo_nombre) || ' ('|| v_gls_tipo_desglose || ') - Validado' || ' ' || v_gls_text_reenvio;        
        v_gls_text_body := '<p>Desglose Rut '|| pi_num_iden || ' ('|| v_gls_tipo_desglose || ') validado, favor imprimir y enviar por mail a ejecutivo.</p>' || chr(10);
      END IF;
    END IF;

    --------------------------------------------------------------------------------------------------------------------------------------------
    -- Si hay configuracion enviamos mail
    IF v_count > 0 THEN

      v_num_solicitud   := TO_CHAR(SYSDATE, 'YYMMDDHHMISS');
      v_num_trx_sistema := v_num_solicitud;

      -- Obtenemos el subject
      FOR v_cur IN (SELECT GLS_GENERICALARGA FROM DESCOD WHERE COD_REG_STATUS = 0 AND COD_TABLA = 'DGLCONFG' AND GLS_GENERICACORTA = 'DGL_MAIL_SUBJECT' ORDER BY COD_GENERICO)
      LOOP
        v_gls_text_subj_full := v_gls_text_subj_full || v_cur.GLS_GENERICALARGA;
      END LOOP;

      -- Obtenemos el header
      v_gls_text_header := '';
      FOR v_cur IN (SELECT GLS_GENERICALARGA FROM DESCOD WHERE COD_REG_STATUS = 0 AND COD_TABLA = 'DGLCONFG' AND GLS_GENERICACORTA = 'DGL_MAIL_HEADER_TOP' ORDER BY COD_GENERICO)
      LOOP
        v_gls_text_header := v_gls_text_header || v_cur.GLS_GENERICALARGA;
      END LOOP;

      -- Obtenemos el footer
      v_gls_text_footer := '';
      FOR v_cur IN (SELECT GLS_GENERICALARGA FROM DESCOD WHERE COD_REG_STATUS = 0 AND COD_TABLA = 'DGLCONFG' AND GLS_GENERICACORTA = 'DGL_MAIL_FOOTER_TOP' ORDER BY COD_GENERICO)
      LOOP
        v_gls_text_footer := v_gls_text_footer || v_cur.GLS_GENERICALARGA || '<BR>';
      END LOOP;
      FOR v_cur IN (SELECT GLS_GENERICALARGA FROM DESCOD WHERE COD_REG_STATUS = 0 AND COD_TABLA = 'DGLCONFG' AND GLS_GENERICACORTA = 'DGL_MAIL_FOOTER_BOTT' ORDER BY COD_GENERICO)
      LOOP
        v_gls_text_footer := v_gls_text_footer || v_cur.GLS_GENERICALARGA;
      END LOOP;

      v_gls_text_subj_full:= v_gls_text_subj_full || v_gls_text_subject;

      v_gls_text_body_full := v_gls_header_html;
      v_gls_text_body_full := v_gls_text_body_full || v_gls_text_header || chr(10) || chr(10);
      v_gls_text_body_full := v_gls_text_body_full || v_gls_text_body || '<BR><BR>' || chr(10) || chr(10);
      v_gls_text_body_full := v_gls_text_body_full || v_gls_text_footer || '<BR><BR>' || chr(10) || chr(10);
      v_gls_text_body_full := v_gls_text_body_full || v_gls_footer_html;

      --------------------------------------------------------------------------------------------------------------------------------------------
      -- Obtenemos los parametros del sistema
      FOR v_cur IN (
                    SELECT SRV.GLS_GENERICALARGA AS GLS_HOST, 
                           SRV.GLS_GENERICACORTA AS NUM_PORT, 
                           TO_NUMBER(SRV2.GLS_GENERICACORTA) AS NUM_MAX_REINTENTO
                      FROM DESCOD SRV
                     INNER JOIN DESCOD SRV2 ON (SRV2.COD_REG_STATUS = 0 AND SRV2.COD_TABLA = 'SRV_MAIL' AND SRV2.COD_GENERICO = 'FLG_ACT_SRV')
                     WHERE SRV.COD_REG_STATUS = 0
                       AND SRV.COD_GENERICO = 'HOST'
                       AND SRV.COD_TABLA = 'SRV_MAIL'      
                   )
      LOOP
        v_smtp_host := v_cur.GLS_HOST;
        v_smtp_port := v_cur.NUM_PORT;
        v_num_max_reintento := v_cur.NUM_MAX_REINTENTO;
      END LOOP;  

      -- Determina si Rol de quien reviso y modifico el Desglose está en blanco, lo que siginica que Desglose fue elaborado por REVISOR
      v_flg_mail:= 0;
      FETCH v_cur_info_clon INTO v_nom,v_mai,v_tip;
      WHILE v_cur_info_clon%found
      LOOP 
        IF v_mai = v_mail_from_mail THEN
          v_flg_mail:= 1;
        END IF;  
        FETCH v_cur_info_clon INTO v_nom,v_mai,v_tip;
      END LOOP; 

      IF v_flg_mail = 0 THEN  
        LOG (C_COD_PROC_MAIL, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, 'CIF_SP_GEN_ENV_MAIL: solicitando generacion mail...', PI_COD_PLANTILLA);      
         BCT_SP_GEN_ENV_MAIL(v_smtp_host, v_smtp_port, C_COD_PROC_MAIL, v_num_solicitud, SYSDATE, v_num_trx_sistema, v_mail_from_name, v_mail_from_mail, v_cur_info_dest, v_gls_text_subj_full, v_gls_text_body_full, v_num_max_reintento, 'HTML',  v_cod_estado, v_gls_respuesta, v_cod_retorno);
        LOG (C_COD_PROC_MAIL, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, 'CIF_SP_GEN_ENV_MAIL: Generado OK, v_cod_estado['||v_cod_estado||'] v_cod_retorno['||v_cod_retorno||']', PI_COD_PLANTILLA);
      ELSE  
        LOG (C_COD_PROC_MAIL, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, 'CIF_SP_GEN_ENV_MAIL: No se genera envío de correo por ser Ejecutor el mismo al Revisor', PI_COD_PLANTILLA);      
      END IF;  
    ELSE
      LOG (C_COD_PROC_MAIL, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, 'CIF_SP_GEN_ENV_MAIL: No aplica generacion, pi_cod_est_nvo['||pi_cod_est_nvo||'] pi_cod_est_ant['||pi_cod_est_ant||'] v_gls_usr_ejecutor['||v_gls_usr_ejecutor||'] v_num_rol_ejecutor['||v_num_rol_ejecutor||']', PI_COD_PLANTILLA);      
    END IF;    
    --------------------------------------------------------------------------------------------------------------------------------------------

  EXCEPTION
    WHEN OTHERS THEN
      LOG (C_COD_PROC_MAIL, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, 'ERROR CIF_SP_GEN_ENV_MAIL: ' || SQLCODE || ' TRACE['||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE||']',PI_COD_PLANTILLA);

  END DGL_SP_GEN_ENV_MAIL_WK;

  PROCEDURE DGL_SP_GEN_CHK_INFORMES_WK
  (
    pi_num_iden        IN  VARCHAR,
    pi_cod_tipo_des    IN  VARCHAR,
    pi_cod_subtipo_des IN  VARCHAR,
    pi_cod_moneda      IN  VARCHAR,
    PI_COD_PLANTILLA   IN  VARCHAR2 DEFAULT '0'
  ) 
  IS
    v_count_casos          NUMBER;
    v_count_total          NUMBER;
    v_num_toleran_chk      NUMBER:= 13;
    v_gls_log              VARCHAR2(200);
    C_COD_TIPO_PROCESO     CONSTANT VARCHAR2(10) := 'CHECK_IMP';
  BEGIN

    -- Obtenemos el parametro para la tolerancia en el calculo que se usa como ROUND en las fórmulas.  
    BEGIN
      SELECT R.GLS_GENERICACORTA
        INTO v_num_toleran_chk
        FROM DESCOD R
       WHERE R.COD_REG_STATUS = 0
         AND R.COD_TABLA = 'DGLCONFG'
         AND R.COD_GENERICO = 'NUM_ROUND_TOLERANCIA';
    EXCEPTION 
      WHEN OTHERS THEN
        v_num_toleran_chk := 13;
    END;

    DELETE FROM DGL_CHK_INFO_WK
     WHERE COD_CLIENTE = pi_num_iden 
       AND COD_TIPO_DES = pi_cod_tipo_des 
       AND COD_SUBTIPO_DES = pi_cod_subtipo_des 
       AND COD_MONEDA = pi_cod_moneda
       AND COD_PLANTILLA = PI_COD_PLANTILLA
       AND COD_TIPO_INFORME IN ('INF1', 'INF2', 'INF3', 'INF4');    
    COMMIT;

    -- Informe 1 - Indicadores Financieros y Generacion de Fondos
    ------------------------------------------------------------------------------------------------------------------------------------------
    -- Informe 1.01 => Resultado Operacional - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'DGL_PKG_DESGLOSE.DGL_SP_GEN_CHK_INFORMES_WK -- Informe 1.01 => Resultado Operacional';
      INSERT INTO DGL_CHK_INFO_WK(fec_check,cod_tipo_informe,gls_factor_informe,cod_cliente,cod_tipo_des,cod_subtipo_des,cod_moneda,
      fec_periodo,num_rank,fec_user_modif,gls_user_modif,val_deflac_base,val_deflac_periodo,val_deflac_calc,val_concepto_bd,val_dif,val_concepto_online,
      gls_concepto_formula,mto_activo,mto_pasivo_patr,cor_concepto,cor_valor,cod_tipo_concep,cod_estado,gls_observacion,COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF01' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( GF_1_5, 3 )
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( GF_1_5, 3 ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                                FROM VALPRD_WK V 
                                WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES 
                                AND V.COD_MONEDA = P.COD_MONEDA AND V.COD_PLANTILLA = P.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO 
                                AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk) AS GF_1_5,
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK 
                             ,R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                        INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (1))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )       
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);     
    COMMIT;
    
    -- Informe 02 => + Depreciación del ejercicio - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'DGL_SP_DES_CHECK_DESGLO_STOCK -- Informe 02 => + Depreciación del ejercicio';
      INSERT INTO DGL_CHK_INFO_WK(fec_check,cod_tipo_informe,gls_factor_informe,cod_cliente,cod_tipo_des,cod_subtipo_des,cod_moneda,
      fec_periodo,num_rank,fec_user_modif,gls_user_modif,val_deflac_base,val_deflac_periodo,val_deflac_calc,val_concepto_bd,val_dif,val_concepto_online,
      gls_concepto_formula,mto_activo,mto_pasivo_patr,cor_concepto,cor_valor,cod_tipo_concep,cod_estado,gls_observacion, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF02' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( GF_2_2, 3 )
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( GF_2_2, 3 ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             ABS(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                                 FROM VALPRD_WK V 
                                 WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (9) ) )),v_num_toleran_chk)) AS GF_2_2,  
                             ABS(ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK 
                             ,R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (2))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 03 => + Castigos /provisiones/amortización - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 03 => + Castigos /provisiones/amortización';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF03' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( GF_3_4_5, 3 ) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( GF_3_4_5, 3 ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             ABS(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('16') ) )),v_num_toleran_chk)) AS GF_3_4_5,  
                             ABS(ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK 
                             ,R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (3))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 04 => Generación Operacional  (Ebitda) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 04 => Generación Operacional  (Ebitda)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF04' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( GF_4_5 + GF_4_22 + GF_4_45, 3 ) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( GF_4_5 + GF_4_22 + GF_4_45, 3 ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             - ABS(ROUND(VR.VAL_CONCEPTO,3)) AS VAL_DIF,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                                FROM VALPRD_WK V 
                                WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                                AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk)),0) AS GF_4_5,  
                             NVL(ABS(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                                FROM VALPRD_WK V 
                                WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                                AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (9) ) )),v_num_toleran_chk)),0) AS GF_4_22,  
                             NVL(ABS(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE 
                             AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (16) ) )),v_num_toleran_chk)),0) AS GF_4_45,  
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK 
                             ,R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (4))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);     
    COMMIT;


    -- Informe 05 => + Otros Ingresos - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 05 => + Otros Ingresos';
      INSERT INTO DGL_CHK_INFO_WK(FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF05' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( VAL_DEFL_CON, 3 )
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3 ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND 
                             V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (20, 21, 22, 24, 26) ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK 
                             ,R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (5))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )      
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;


    -- Informe 06 => - Otros Egresos - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 06 => - Otros Egresos';
      INSERT INTO DGL_CHK_INFO_WK(FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF06' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3 ) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3 ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA 
                             AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (42) ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK 
                             ,R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND 
                        VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (6))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;


    -- Informe 07 => Generación Bruta - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 07 => Generación Bruta';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF07' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND ( ( ( GF_07_04_01 + GF_07_04_02 + GF_07_04_03 ) + GF_07_05 + GF_07_06 ), 3)
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ( ROUND ( ( ( GF_07_04_01 + GF_07_04_02 + GF_07_04_03 ) + GF_07_05 + GF_07_06 ), 3) ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES,     
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk)),0) AS GF_07_04_01,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA 
                             AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (9) ) )),v_num_toleran_chk)),0)) AS GF_07_04_02,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA 
                             AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (16) ) )),v_num_toleran_chk)),0)) AS GF_07_04_03,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA 
                             AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (20, 21, 22, 24, 26) ) )),v_num_toleran_chk)),0)) AS GF_07_05,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA 
                             AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (42) ) )),v_num_toleran_chk)),0)) AS GF_07_06,  
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (7))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 08 => - Gastos Financieros - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 08 => - Gastos Financieros';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF08' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3 ) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3 ) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (31, 32, 33, 34) ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA 
                             AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA 
                             AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (8))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 09 => - Impuestos - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 09 => - Impuestos';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF09' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) 
                             FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (52) ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA 
                       AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES 
                        AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (9))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;


    -- Informe 10 => Generación Neta - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 10 => Generación Neta';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF10' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( ( ( ( GF_10_07_04_01 + GF_10_07_04_02 + GF_10_07_04_03 ) + GF_10_07_05 + GF_10_07_06 ) + GF_10_08 + GF_10_09 ), 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( ( ( GF_10_07_04_01 + GF_10_07_04_02 + GF_10_07_04_03 ) + GF_10_07_05 + GF_10_07_06 ) + GF_10_08 + GF_10_09 ), 3) AS VAL_ONLINE,
                     GF_10_07_04_01, GF_10_07_04_02, GF_10_07_04_03, GF_10_07_05, GF_10_07_06, GF_10_08, GF_10_09,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,  
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk)),0) AS GF_10_07_04_01,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (9) ) )),v_num_toleran_chk)),0)) AS GF_10_07_04_02,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (16) ) )),v_num_toleran_chk)),0)) AS GF_10_07_04_03,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (20, 21, 22, 24, 26) ) )),v_num_toleran_chk)),0)) AS GF_10_07_05,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (42) ) )),v_num_toleran_chk)),0)) AS GF_10_07_06,  
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (31, 32, 33, 34) ) )),v_num_toleran_chk),0) AS GF_10_08,  
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (51) ) )),v_num_toleran_chk),0) AS GF_10_09,  
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (10))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 11 => Cobertura de Gastos Financieros (veces) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 11 => Cobertura de Gastos Financieros (veces)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF11' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( ( -( ( GF_10_07_04_01 + GF_10_07_04_02 + GF_10_07_04_03 ) + GF_10_07_05 + GF_10_07_06 ) / GF_10_08 ), 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( -( ( GF_10_07_04_01 + GF_10_07_04_02 + GF_10_07_04_03 ) + GF_10_07_05 + GF_10_07_06 ) / GF_10_08 ), 3) AS VAL_ONLINE,
                     GF_10_07_04_01, GF_10_07_04_02, GF_10_07_04_03, GF_10_07_05, GF_10_07_06, GF_10_08,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,      
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk)),0) AS GF_10_07_04_01,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (9) ) )),v_num_toleran_chk)),0)) AS GF_10_07_04_02,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (16) ) )),v_num_toleran_chk)),0)) AS GF_10_07_04_03,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (20, 21, 22, 24, 26) ) )),v_num_toleran_chk)),0)) AS GF_10_07_05,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (42) ) )),v_num_toleran_chk)),0)) AS GF_10_07_06,  
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (31, 32, 33, 34) ) )),v_num_toleran_chk) AS GF_10_08,  
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA 
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (11))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
               WHERE GF_10_08 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 12 => Obligaciones LP con Venc. en el Año - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 12 => Obligaciones LP con Venc. en el Año';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF12' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RANK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( ( GF_12_20 + GF_12_23 + GF_12_24 ), 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( GF_12_20 + GF_12_23 + GF_12_24 ), 3) AS VAL_ONLINE,
                     GF_12_20, GF_12_23, GF_12_24,  
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO,       
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (106) ) )),v_num_toleran_chk)),0) AS GF_12_20,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (121) ) )),v_num_toleran_chk)),0) AS GF_12_23,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (123, 124) ) )),v_num_toleran_chk)),0) AS GF_12_24,
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA 
                        AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (12))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;  

    -- Informe 13 => Gen.Neta/Oblig. Venc.en el Año (veces) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 13 => Gen.Neta/Oblig. Venc.en el Año (veces)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF13' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( ( ( ( ( GF_13_10_07_04_01 + GF_13_10_07_04_02 + GF_13_10_07_04_03 + GF_13_10_07_05 + GF_13_10_07_06 + GF_13_10_08 + GF_13_10_09 ) / NUM_MESES ) * 12 ) / ( GF_13_12_20 + GF_13_12_23 + GF_13_12_24 ) ), 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( ( ( ( GF_13_10_07_04_01 + GF_13_10_07_04_02 + GF_13_10_07_04_03 + GF_13_10_07_05 + GF_13_10_07_06 + GF_13_10_08 + GF_13_10_09 ) / NUM_MESES ) * 12 ) / ( GF_13_12_20 + GF_13_12_23 + GF_13_12_24 ) ), 3) AS VAL_ONLINE,
                     NUM_MESES, GF_13_10_07_04_01, GF_13_10_07_04_02, GF_13_10_07_04_03, GF_13_10_07_05, GF_13_10_07_06, GF_13_10_08, GF_13_10_09, GF_13_12_20, GF_13_12_23, GF_13_12_24, 
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES,    
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk)),0) AS GF_13_10_07_04_01,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (9) ) )),v_num_toleran_chk)),0)) AS GF_13_10_07_04_02,
                             ABS(NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (16) ) )),v_num_toleran_chk)),0)) AS GF_13_10_07_04_03,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (20, 21, 22, 24, 26) ) )),v_num_toleran_chk)),0)) AS GF_13_10_07_05,
                             (NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (42) ) )),v_num_toleran_chk)),0)) AS GF_13_10_07_06,  
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (31, 32, 33, 34) ) )),v_num_toleran_chk),0) AS GF_13_10_08,  
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (51) ) )),v_num_toleran_chk),0) AS GF_13_10_09,                            
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (106) ) )),v_num_toleran_chk)),0) AS GF_13_12_20,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (121) ) )),v_num_toleran_chk)),0) AS GF_13_12_23,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V 
                             WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO 
                             AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (123, 124) ) )),v_num_toleran_chk)),0) AS GF_13_12_24,
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA 
                        AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (13))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                         AND P.NUM_MESES <> 0                       
                     ) 
               WHERE ( GF_13_12_20 + GF_13_12_23 + GF_13_12_24 ) <> 0 
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 14 => Deuda Fin/ Ebitda (años) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 14 => Deuda Fin/ Ebitda (años)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF14' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( ( GF_14_19 + GF_14_20 + GF_14_23 + GF_14_24 + GF_14_29 + GF_14_31 + GF_14_32 ) / ( ( ( GF_4_5 + GF_4_22 + GF_4_45)  / NUM_MESES ) * 12 ), 3)
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( GF_14_19 + GF_14_20 + GF_14_23 + GF_14_24 + GF_14_29 + GF_14_31 + GF_14_32 ) / ( ( ( GF_4_5 + GF_4_22 + GF_4_45)  / NUM_MESES ) * 12 ), 3) AS VAL_ONLINE,
                     NUM_MESES, GF_14_19, GF_14_20, GF_14_23, GF_14_24, GF_14_29, GF_14_31, GF_14_32, GF_4_5, GF_4_22, GF_4_45,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES,     
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (99, 100, 101, 102, 103, 104) ) )),v_num_toleran_chk)),0) AS GF_14_19,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (106) ) )),v_num_toleran_chk)),0) AS GF_14_20,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (121) ) )),v_num_toleran_chk)),0) AS GF_14_23,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (123) ) )),v_num_toleran_chk)),0) AS GF_14_24,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (144) ) )),v_num_toleran_chk)),0) AS GF_14_29,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (149) ) )),v_num_toleran_chk)),0) AS GF_14_31,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (153) ) )),v_num_toleran_chk)),0) AS GF_14_32,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk)),0) AS GF_4_5,  
                             NVL(ABS(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('9') ) )),v_num_toleran_chk)),0) AS GF_4_22,  
                             NVL(ABS(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('16') ) )),v_num_toleran_chk)),0) AS GF_4_45,                         
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (14))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                         AND P.NUM_MESES <> 0
                     )
               WHERE ( GF_4_5 + GF_4_22 + GF_4_45) <> 0                      
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 15 => Flujo de la Operación - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 15 => Flujo de la Operación';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF15' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF,  NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,      
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('56') ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             P.FEC_PERIODO, RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (16))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;


    -- Informe 16 => Flujo de Inversiones - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 16 => Flujo de Inversiones';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF16' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,      
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('57') ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             --ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VD.VAL_CONCEPTO),v_num_toleran_chk) AS VAL_DET_DEFL_CON, -- CAMPOS DE DETALLE 
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             P.FEC_PERIODO, RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (17))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 17 => Flujo de Financiamiento - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 17 => Flujo de Financiamiento';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF17' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,      
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('58') ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             --ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VD.VAL_CONCEPTO),v_num_toleran_chk) AS VAL_DET_DEFL_CON, -- CAMPOS DE DETALLE 
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             P.FEC_PERIODO, RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (18))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 18 => Efecto Var Tasa Cambio sobre Efectivo - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 18 => Efecto Var Tasa Cambio sobre Efectivo';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF18' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,      
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('59') ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             P.FEC_PERIODO, RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (19))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 19 => Var. Neta Efectivo y Equiv Efectivo - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 19 => Var. Neta Efectivo y Equiv Efectivo';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF19' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,      
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('56', '57', '58', '59') ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             P.FEC_PERIODO, RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (20))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 20 => Saldo Inicial Efectivo y Equiv. Efectivo - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 20 => Saldo Inicial Efectivo y Equiv. Efectivo';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF20' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( VAL_DEFL_CON, 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( VAL_DEFL_CON, 3) AS VAL_ONLINE,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,      
                             ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN ('61') ) )),v_num_toleran_chk) AS VAL_DEFL_CON,  
                             --ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VD.VAL_CONCEPTO),v_num_toleran_chk) AS VAL_DET_DEFL_CON, -- CAMPOS DE DETALLE 
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             P.FEC_PERIODO, RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (21))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 21 => Saldo Final Efectivo y Equiv. a Efectivo - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 21 => Saldo Final Efectivo y Equiv. a Efectivo';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF21' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO,
             NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( (GF_22_21 + GF_22_22 ), 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( (GF_22_21 + GF_22_22 ), 3) AS VAL_ONLINE,
                     GF_22_21, GF_22_22,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (  
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,      
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (56, 57, 58, 59) ) )),v_num_toleran_chk),0) AS GF_22_21,  
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (61) ) )),v_num_toleran_chk),0) AS GF_22_22,  
                             ROUND(VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_CON_BD, 
                             P.FEC_PERIODO, RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'GEN_FONDOS' AND VR.COR_CONCEPTO IN (22))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 22 => Capital de trabajo - OK COD_PLANTILLA
     BEGIN
       v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 22 => Capital de trabajo';
       INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                 NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                 VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
       SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF22' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
              FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
              VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
              COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
         FROM (  
               SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                      (
                        ROUND( ( GF_22_8 - GF_22_28 ), 3) 
                      ) - VAL_CON_BD AS VAL_DIF, 
                      ROUND( ( GF_22_8 - GF_22_28 ), 3) AS VAL_ONLINE,
                      NUM_MESES, GF_22_8, GF_22_28,
                      NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                 FROM (
                       SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                              ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                              R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES,     
                              NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (2,3,4,5,6,7,8,10,11,12,13,15,16,17,18,191,20,21,22,23,24,25,26,27,28,29,30,32,33,34,35,36,38,39,40,41,43,44,45,46,47,48) ) )),v_num_toleran_chk)),0) AS GF_22_8,
                              NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (99, 100, 101, 102, 103, 104, 106, 108, 109, 110, 111, 113, 114, 115, 116, 117, 118, 119, 121, 123, 124, 126, 127, 128, 130, 131, 132, 134, 135, 194, 136, 137, 138, 139, 140, 141) ) )),v_num_toleran_chk)),0) AS GF_22_28,
                              --
                              (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                              RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                              R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                         FROM DESGLO_WK R
                        INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                         LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (2))
                         LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                        WHERE R.COD_CLIENTE = pi_num_iden
                          AND R.COD_TIPO_DES = pi_cod_tipo_des
                          AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                          AND R.COD_MONEDA = pi_cod_moneda
                          AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                      )    
              ) R       
        INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
        WHERE NUM_RNK <= D.GLS_GENERICACORTA     
          AND VAL_CON_BD IS NOT NULL       
          AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
       v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
     EXCEPTION 
       WHEN OTHERS THEN
       v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
     END;
     LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
     COMMIT;

    -- Informe 23 => Razón Corriente (veces) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 23 => Razón Corriente (veces)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF23' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
             COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( ( GF_23_8 / GF_23_28 ), 3) 
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( GF_23_8 / GF_23_28 ), 3) AS VAL_ONLINE,
                     NUM_MESES, GF_23_8, GF_23_28,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES,     
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (2,3,4,5,6,7,8,10,11,12,13,15,16,17,18,191,20,21,22,23,24,25,26,27,28,29,30,32,33,34,35,36,38,39,40,41,43,44,45,46,47,48) ) )),v_num_toleran_chk)),0) AS GF_23_8,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (99, 100, 101, 102, 103, 104, 106, 108, 109, 110, 111, 113, 114, 115, 116, 117, 118, 119, 121, 123, 124, 126, 127, 128, 130, 131, 132, 134, 135, 194, 136, 137, 138, 139, 140, 141) ) )),v_num_toleran_chk)),0) AS GF_23_28,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (3))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
               WHERE GF_23_28 <> 0 
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;


    -- Informe 24 => Test Acido (veces) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 24 => Test Acido (veces)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF24' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
             COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,   
                     (
                       ROUND( ( ( GF_24_8 - GF_24_4) / GF_24_28 ), 3) 
                     ) - VAL_CON_BD AS VAL_DIF,
                     ROUND( ( ( GF_24_8 - GF_24_4) / GF_24_28 ), 3) AS VAL_ONLINE, 
                     NUM_MESES, GF_24_4, GF_24_8, GF_24_28,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES,     
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30) ) )),v_num_toleran_chk)),0) AS GF_24_4,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (2,3,4,5,6,7,8,10,11,12,13,15,16,17,18,191,20,21,22,23,24,25,26,27,28,29,30,32,33,34,35,36,38,39,40,41,43,44,45,46,47,48) ) )),v_num_toleran_chk)),0) AS GF_24_8,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (99, 100, 101, 102, 103, 104, 106, 108, 109, 110, 111, 113, 114, 115, 116, 117, 118, 119, 121, 123, 124, 126, 127, 128, 130, 131, 132, 134, 135, 194, 136, 137, 138, 139, 140, 141) ) )),v_num_toleran_chk)),0) AS GF_24_28,
                             --
                             ABS(ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (4))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
               WHERE GF_24_28 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 25 => Permanencia Cuentas por Cobrar (días) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 25 => Permanencia Cuentas por Cobrar (días)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF25' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
             COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD, 
                     (
                       ROUND( ( ( GF_25_2 + GF_25_3) / ER_25_1 * (365 / 12) * NUM_MESES / ( 1 + VAL_IVA ) ), 3)
                     ) - VAL_CON_BD AS VAL_DIF,
                     ROUND( ( ( GF_25_2 + GF_25_3) / ER_25_1 * (365 / 12) * NUM_MESES / ( 1 + VAL_IVA ) ), 3) AS VAL_ONLINE, 
                     NUM_MESES, VAL_IVA, GF_25_2, GF_25_3, ER_25_1,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (10, 11, 12, 13) ) )),v_num_toleran_chk)),0) AS GF_25_2,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (15, 16, 17, 18, 191) ) )),v_num_toleran_chk)),0) AS GF_25_3,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6) ) )),v_num_toleran_chk)),0) AS ER_25_1,
                             --
                             ABS(ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (5))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                         AND P.NUM_MESES <> 0                       
                     )        
               WHERE ER_25_1 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 26 => Permanencia Inventario  (días) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 26 => Permanencia Inventario  (días)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF26' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
             COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,  
                     (
                       ROUND( ( ( -GF_26_B4 / GF_26_ER2 ) * ( (365 / 12) * NUM_MESES ) ), 3)
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( ( -GF_26_B4 / GF_26_ER2 ) * ( (365 / 12) * NUM_MESES ) ), 3) AS VAL_ONLINE,
                     NUM_MESES, VAL_IVA, GF_26_B4, GF_26_ER2,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP,
                     VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30) ) )),v_num_toleran_chk)),0) AS GF_26_B4,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (8, 9) ) )),v_num_toleran_chk)),0) AS GF_26_ER2,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (6))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                         AND P.NUM_MESES <> 0                       
                     )        
               WHERE GF_26_ER2 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 27 => Permanencia Cuentas por Pagar (días) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 27 => Permanencia Cuentas por Pagar (días)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF27' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO,
             COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,  
                     (
                       ROUND( (-GF_27_B21/ GF_27_ER2 * (365 / 12) * NUM_MESES / ( 1 + VAL_IVA ) ), 3)
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( (-GF_27_B21/ GF_27_ER2 * (365 / 12) * NUM_MESES / ( 1 + VAL_IVA ) ), 3) AS VAL_ONLINE,
                     NUM_MESES, VAL_IVA, GF_27_B21, GF_27_ER2,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP,
                     VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (108, 109, 110, 111) ) )),v_num_toleran_chk)),0) AS GF_27_B21,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (8, 9) ) )),v_num_toleran_chk)),0) AS GF_27_ER2,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (7))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                         AND P.NUM_MESES <> 0                       
                     ) 
               WHERE GF_27_ER2 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 28 => Leverage - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 28 => Leverage';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF28' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD, 
                     (
                       ROUND( ( GF_28_B34 / GF_28_B44 ), 3)
                     ) - VAL_CON_BD AS VAL_DIF,
                     ROUND( ( GF_28_B34 / GF_28_B44 ), 3) AS VAL_ONLINE, 
                     NUM_MESES, VAL_IVA, GF_28_B34, GF_28_B44,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP,
                     VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (99, 100, 101, 102, 103, 104, 106, 108, 109, 110, 111, 113, 114, 115, 116, 117, 118, 119, 121, 123, 124, 126, 127, 128, 130, 131, 132, 134, 135, 194, 136, 137, 138, 139, 140, 141, 144, 146, 147, 149, 151, 152, 153, 154, 155) ) )),v_num_toleran_chk)),0) AS GF_28_B34,
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (161, 162, 164, 166, 168, 169, 170, 171, 173, 174, 175, 177, 178, 179, 180, 182, 183, 184, 185, 187, 196) ) )),v_num_toleran_chk)),0) AS GF_28_B44,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (9))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
               WHERE GF_28_B44 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 29 => Result.Oper./Gastos Finan. (veces) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 29 => Result.Oper./Gastos Finan. (veces)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF29' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO,
             NULL AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA      
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD, 
                     (
                       ROUND( ( -GF_29_ER5 / GF_29_ER8 ), 3)
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( -GF_29_ER5 / GF_29_ER8 ), 3) AS VAL_ONLINE,
                     NUM_MESES, VAL_IVA, GF_29_ER5, GF_29_ER8,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP,
                     VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16) ) )),v_num_toleran_chk),0) AS GF_29_ER5,                       
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (31, 32, 33, 34) ) )),v_num_toleran_chk)),0) AS GF_29_ER8,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (10))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )    
               WHERE GF_29_ER8 <> 0 
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 1.30 => Rentabilidad sobre ingresos (%) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 1.30 => Rentabilidad sobre ingresos (%)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF30' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
             COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD, 
                     (
                       ROUND( ( ( GF_30_ER14 / GF_30_ER1 ) * 100 ), 3)
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( ( GF_30_ER14 / GF_30_ER1 ) * 100 ), 3) AS VAL_ONLINE,
                     NUM_MESES, VAL_IVA, GF_30_ER1, GF_30_ER14,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP,
                     VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42, 44, 45, 47, 48, 49, 52) ) )),v_num_toleran_chk),0) AS GF_30_ER14,                       
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6) ) )),v_num_toleran_chk)),0) AS GF_30_ER1,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (13))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )      
               WHERE GF_30_ER1 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 1.31 => Rentabilidad sobre patrimonio (%) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK -- Informe 1.31 => Rentabilidad sobre patrimonio (%)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF31' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO,
             COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD, 
                     (
                       ROUND( ( ( GF_31_ER14 / ( GF_30_B44 - GF_31_ER14 ) ) * 100 ), 3)
                     ) - VAL_CON_BD AS VAL_DIF, 
                     ROUND( ( ( GF_31_ER14 / ( GF_30_B44 - GF_31_ER14 ) ) * 100 ), 3) AS VAL_ONLINE,
                     NUM_MESES, VAL_IVA, GF_30_B44, GF_31_ER14,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE,
                     VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42, 44, 45, 47, 48, 49, 52) ) )),v_num_toleran_chk),0) AS GF_31_ER14,                                              
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (161, 162, 164, 166, 168, 169, 170, 171, 173, 174, 175, 177, 178, 179, 180, 182, 183, 184, 185, 187, 196) ) )),v_num_toleran_chk)),0) AS GF_30_B44,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK
                             ,R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (14))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
               WHERE ( GF_30_B44 - GF_31_ER14 ) <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 1.32 => Rentabilidad sobre activos (%) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK - Informe 1.32 => Rentabilidad sobre activos (%)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF32' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, VAL_ONLINE AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
             COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( ( ( GF_32_ER14 / GF_32_B18 ) * 100 ), 3)
                     ) - VAL_CON_BD AS VAL_DIF,
                     ROUND( ( ( GF_32_ER14 / GF_32_B18 ) * 100 ), 3) AS VAL_ONLINE, 
                     NUM_MESES, VAL_IVA, GF_32_B18, GF_32_ER14,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, COR_VALOR, GLS_FRML_CONCEP, VAL_DEFLAC_BASE,
                     VAL_DEFLAC_PERIODO, VAL_DEF AS VAL_DEFLAC_CALC, COD_PLANTILLA
                FROM (
                      SELECT R.VAL_DEFLAC_BASE, P.VAL_DEFLAC_PRD AS VAL_DEFLAC_PERIODO, 
                             ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42, 44, 45, 47, 48, 49, 52) ) )),v_num_toleran_chk),0) AS GF_32_ER14,                                              
                             NVL((ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (2,3,4,5,6,7,8,10,11,12,13,15,16,17,18,191,20,21,22,23,24,25,26,27,28,29,30,32,33,34,35,36,38,39,40,41,43,44,45,46,47,48,51, 192, 52, 53, 54, 193, 55, 56, 57, 58, 59, 60, 61,63,65,68, 69, 70, 71, 72, 73,75, 76, 77, 78, 79,81, 82, 83, 84, 85, 86, 87,89, 90, 91, 92, 93, 94, 95) ) )),v_num_toleran_chk)),0) AS GF_32_B18,
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (15))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )        
               WHERE GF_32_B18 <> 0
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL       
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 1.7 => Ciclo de Caja (dia) - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK - Informe 1.32 => Rentabilidad sobre activos (%)';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT  DISTINCT SYSTIMESTAMP AS FEC_CHECK, 'INF1' AS COD_TIPO_INFORME, 'GF33' AS GLS_FACTOR_INFORME, 
             A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, FEC_PERIODO,
             0 AS NUM_RNK, D.FEC_USER_MODIF, D.NOM_USER_MODIF AS GLS_USER_MODIF, NULL AS VAL_DEFLAC_BASE, NULL AS VAL_DEFLAC_PERIODO, NULL AS VAL_DEFLAC_CALC, 
             NULL AS VAL_CONCEPTO_BD, NULL AS VAL_DIF, NULL AS VAL_CONCEPTO_ONLINE, NULL AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, NULL AS COR_CONCEPTO, NULL AS COR_VALOR, 'IND_FINAN' AS COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 'Inconsistencia datos Ind.Finan. Ciclo de Caja' AS GLS_OBSERVACION, A.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO--, D.FEC_USER_MODIF, D.NOM_USER_MODIF AS GLS_USER_MODIF      
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_IF5, VAL_IF6, VAL_IF7,
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, 
                                             R.COD_TIPO_DES, 
                                             R.COD_SUBTIPO_DES, 
                                             R.COD_MONEDA,
                                             R.COD_PLANTILLA,
                                             R.FEC_PERIODO,
                                             VAL_IF5,
                                             VAL_IF6,
                                             VAL_IF7
                                        FROM (
                                              SELECT COD_CLIENTE, 
                                                     COD_TIPO_DES, 
                                                     COD_SUBTIPO_DES, 
                                                     COD_MONEDA,
                                                     COD_PLANTILLA,
                                                     FEC_PERIODO,
                                                     NUM_MESES, 
                                                     VAL_DEFLAC_PRD,
                                                     VAL_DEFLAC_BASE,
                                                     VAL_IVA,
                                                     DECODE(VAL_ER1,0,0,(VAL_B2+VAL_B3)/VAL_ER1*(365/12)*NUM_MESES/(1+VAL_IVA)) VAL_IF5,
                                                     DECODE(VAL_ER2,0,0,(-VAL_B4)/VAL_ER2*(365/12)*NUM_MESES) VAL_IF6,
                                                     DECODE(VAL_ER2,0,0,(-VAL_B21)/VAL_ER2*(365/12)*NUM_MESES/(1+VAL_IVA)) VAL_IF7
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, 
                                                             COD_TIPO_DES, 
                                                             COD_SUBTIPO_DES, 
                                                             COD_MONEDA,
                                                             COD_PLANTILLA,
                                                             FEC_PERIODO,
                                                             NUM_MESES, 
                                                             VAL_DEFLAC_PRD,
                                                             VAL_DEFLAC_BASE,
                                                             VAL_IVA,
                                                             SUM(VAL_B2) VAL_B2,
                                                             SUM(VAL_B3) VAL_B3,
                                                             SUM(VAL_B4) VAL_B4,
                                                             SUM(VAL_B21) VAL_B21,
                                                             SUM(VAL_ER1) VAL_ER1,
                                                             SUM(VAL_ER2) VAL_ER2
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, 
                                                                     D.COD_TIPO_DES, 
                                                                     D.COD_SUBTIPO_DES, 
                                                                     D.COD_MONEDA,
                                                                     D.COD_PLANTILLA,
                                                                     P.FEC_PERIODO, 
                                                                     P.NUM_MESES, 
                                                                     P.VAL_DEFLAC_PRD, 
                                                                     D.VAL_DEFLAC_BASE, 
                                                                     D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 9 AND V.VAL_CONCEPTO IS NOT NULL THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B2,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 14 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B3,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 19 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B4,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 107 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B21,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 1 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER1,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 7 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER2
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND ((C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (9,14,19,107)) 
                                                                  OR (C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO IN (1,7)))
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       WHERE NUM_RNK <= 4 AND ROUND((VAL_IF5+VAL_IF6-VAL_IF7),0) <> 0
                     ) A
                LEFT JOIN (SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'IND_FINAN' AND VR.COR_CONCEPTO IN (16))
                                            WHERE VR.COD_TIPO_CONCEP = 'IND_FINAN'
                                              AND VR.COR_CONCEPTO IN (16)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA   
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               WHERE B.COD_CLIENTE IS NULL
             ) A
       INNER JOIN DESGLO_WK D ON (D.COD_CLIENTE = A.COD_CLIENTE AND D.COD_TIPO_DES = A.COD_TIPO_DES AND D.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND D.COD_MONEDA = A.COD_MONEDA AND D.COD_PLANTILLA = A.COD_PLANTILLA);

        v_count_casos := SQL%ROWCOUNT;
        v_count_total := v_count_total + v_count_casos;
        v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
      EXCEPTION 
        WHEN OTHERS THEN
        v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 2 - Concepto Utilidad del ejercicio - OK COD_PLANTILLA
    ------------------------------------------------------------------------------------------------------------------------------------------
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK - Informe 2 - Concepto Utilidad del ejercicio';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF2' AS COD_TIPO_INFORME, 'IF02' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, NULL AS VAL_DEFLAC_BASE, NULL AS VAL_DEFLAC_PERIODO, NULL AS VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, CAL_CONCEPTO AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR,
             COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     (
                       ROUND( GF_GRAL_ER12 + GF_GRAL_ER13, 3)
                     ) - VAL_CON_BD AS VAL_DIF,
                     ROUND( GF_GRAL_ER12 + GF_GRAL_ER13, 3) AS CAL_CONCEPTO, 
                     NUM_MESES, VAL_IVA, GF_GRAL_ER12, GF_GRAL_ER13,
                     NUM_RNK, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, GLS_CONCEPTO, COD_TIPO_CONCEP, COR_CONCEPTO, GLS_FRML_CONCEP, COR_VALOR, COD_PLANTILLA
                FROM (          
                      SELECT ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (2, 3, 4, 5, 6, 8, 9, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42, 44, 45, 47, 48, 49) ) )),v_num_toleran_chk),0) AS GF_GRAL_ER12,                                              
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'EST_RESULT' AND V.COR_CONCEPTO IN (52) ) )),v_num_toleran_chk),0) AS GF_GRAL_ER13,                                              
                             --
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD_ORIG, 
                             (ROUND(VR.VAL_CONCEPTO*(R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),3)) AS VAL_CON_BD, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, VR.GLS_FRML_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                       INNER JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'BALANCE' AND VR.COR_CONCEPTO IN (187))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                   )               
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL     
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 3 - Consistencia de signo - OK COD_PLANTILLA
    ------------------------------------------------------------------------------------------------------------------------------------------
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK - Informe 3 - Consistencia de signo';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, 
                                NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, 
                                VAL_DIF, VAL_CONCEPTO_ONLINE, GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF3' AS COD_TIPO_INFORME, 'IF03' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, 0 AS NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, NULL AS VAL_DEFLAC_BASE, NULL AS VAL_DEFLAC_PERIODO, NULL AS VAL_DEFLAC_CALC, 
             VAL_CON_BD AS VAL_CONCEPTO_BD, VAL_DIF, CAL_CONCEPTO AS VAL_CONCEPTO_ONLINE, GLS_FRML_CONCEP AS GLS_CONCEPTO_FORMULA, NULL AS MTO_ACTIVO, NULL AS MTO_PASIVO_PATR, COR_CONCEPTO,
             COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT COD_CLIENTE, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, VAL_CONCEPTO AS VAL_CON_BD, CAL_CONCEPTO,
              (VAL_CONCEPTO-CAL_CONCEPTO) AS VAL_DIF, GLS_FRML_CONCEP, GLS_CONCEPTO, GLS_CONCEPTO_VAL, COR_CONCEPTO, COD_CONCEPTO, 1 AS NUM_RNK, COD_TIPO_CONCEP, COR_VALOR, COD_PLANTILLA
                FROM (
                      SELECT R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF,
                      P.FEC_PERIODO, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, VR.VAL_CONCEPTO, VR.GLS_FRML_CONCEP, ROUND(CIF_FUN_EVAL_NUMBER_STRING(VR.GLS_FRML_CONCEP,
                      NVL(VR.VAL_CONCEPTO,0)),3) AS CAL_CONCEPTO, CR.GLS_CONCEPTO, VR.GLS_CONCEPTO AS GLS_CONCEPTO_VAL, CR.COD_CONCEPTO, VR.COD_TIPO_CONCEP, VR.COR_CONCEPTO, VR.COR_VALOR, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       --INNER JOIN PERIOD P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA)
                       INNER JOIN (SELECT * 
                                     FROM (SELECT P.*, DS.GLS_GENERICACORTA  
                                             FROM PERIOD_WK P   
                                            INNER JOIN DESCOD DS ON (DS.COD_TABLA = 'DGLCONFG' AND COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND COD_REG_STATUS = 0)      
                                            WHERE COD_CLIENTE = pi_num_iden
                                              AND COD_TIPO_DES = pi_cod_tipo_des
                                              AND COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND COD_MONEDA = pi_cod_moneda
                                              AND COD_PLANTILLA = PI_COD_PLANTILLA
                                            ORDER BY FEC_PERIODO DESC) 
                                    WHERE ROWNUM <= GLS_GENERICACORTA) P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                        LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO ) 
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                     )         
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL     
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;


    -- Informe 4 - Activo = Pasivo + Patrimonio - OK COD_PLANTILLA
    ------------------------------------------------------------------------------------------------------------------------------------------
    BEGIN
      v_gls_log := 'CIF_SP_DES_CHECK_DESGLO_STOCK - Informe 4 - Activo = Pasivo + Patrimonio';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_CONCEPTO_ONLINE, VAL_DIF, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 'INF4' AS COD_TIPO_INFORME, 'IF04' AS GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, 
             FEC_PERIODO, NUM_RNK, FEC_USER_MODIF, NOM_USER_MODIF AS GLS_USER_MODIF, NULL AS VAL_DEFLAC_BASE, NULL AS VAL_DEFLAC_PERIODO, NULL AS VAL_DEFLAC_CALC, 
             NULL AS VAL_CONCEPTO_BD, NULL AS VAL_CONCEPTO_ONLINE, VAL_DIF, MTO_ACT AS MTO_ACTIVO, MTO_PAS_PAT AS MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, 'GN' AS COD_ESTADO, GLS_CONCEPTO AS GLS_OBSERVACION, COD_PLANTILLA
        FROM (  
              SELECT VAL_DEF, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_USER_MODIF, NOM_USER_MODIF, FEC_PERIODO, VAL_CON_BD,
                     ROUND( ( MTO_PATRIM + MTO_PASIVO ) - MTO_ACT, 3) AS VAL_DIF,
                     NUM_MESES, VAL_IVA, ( MTO_PATRIM + MTO_PASIVO ) AS MTO_PAS_PAT, MTO_ACT,
                     NUM_RNK, GLS_CONCEPTO, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_PLANTILLA
                FROM (          
                      SELECT ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),v_num_toleran_chk) AS VAL_DEF,
                             R.COD_CLIENTE, NVL(P.FEC_USER_MODIF, P.FEC_USER_CREA) AS FEC_USER_MODIF, NVL(P.NOM_USER_MODIF, P.NOM_USER_CREA) AS NOM_USER_MODIF, P.FEC_PERIODO, P.NUM_MESES, R.VAL_IVA,   
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (160,163,165,167,172,176,181,186,195) ) )),3),0) AS MTO_PATRIM,                                              
                             NVL(ROUND(((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*(SELECT SUM(CIF_FUN_EVAL_NUMBER_STRING(V.GLS_FRML_CONCEP, NVL(V.VAL_CONCEPTO,0))) FROM VALPRD_WK V WHERE (V.COD_CLIENTE = P.COD_CLIENTE AND V.COD_TIPO_DES = P.COD_TIPO_DES AND V.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND V.COD_MONEDA = P.COD_MONEDA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP = 'BALANCE' AND V.COR_CONCEPTO IN (159) ) )),3),0) AS MTO_PASIVO,                                              
                             (ROUND(VR.VAL_CONCEPTO,3)) AS VAL_CON_BD, 
                             (ROUND(VR.VAL_CONCEPTO*(R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD),3)) AS MTO_ACT, 
                             RANK() OVER (PARTITION BY R.COD_CLIENTE ORDER BY P.FEC_PERIODO DESC) AS NUM_RNK,
                             R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, CR.GLS_CONCEPTO, VR.COR_CONCEPTO, VR.COR_VALOR, VR.COD_TIPO_CONCEP, R.COD_PLANTILLA
                        FROM DESGLO_WK R
                       INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                       INNER JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO AND VR.COD_TIPO_CONCEP = 'BALANCE' AND VR.COR_CONCEPTO IN (97))
                        LEFT JOIN CONCEP CR ON (CR.COD_TIPO_CONCEP = VR.COD_TIPO_CONCEP AND CR.COR_CONCEPTO = VR.COR_CONCEPTO )
                       WHERE R.COD_CLIENTE = pi_num_iden
                         AND R.COD_TIPO_DES = pi_cod_tipo_des
                         AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                         AND R.COD_MONEDA = pi_cod_moneda
                         AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                   )               
             ) R       
       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
         AND VAL_CON_BD IS NOT NULL  
         AND ABS(VAL_DIF) > ( D.GLS_GENERICALARGA / 1000);

      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;  
  END DGL_SP_GEN_CHK_INFORMES_WK;
  
  
  PROCEDURE DGL_SP_GEN_CHK_INFORMES2_WK
  (
    pi_num_iden        IN  VARCHAR,
    pi_cod_tipo_des    IN  VARCHAR,
    pi_cod_subtipo_des IN  VARCHAR,
    pi_cod_moneda      IN  VARCHAR,
    PI_COD_PLANTILLA   IN  VARCHAR2 DEFAULT '0'
  ) 
  IS
    v_count_casos          NUMBER;
    v_count_total          NUMBER;
    v_num_toleran_chk      NUMBER:= 13;
    v_gls_log              VARCHAR2(200);
    C_COD_TIPO_PROCESO     CONSTANT VARCHAR2(10) := 'CHECK_IMP';
  BEGIN

    -- Obtenemos el parámetro para la tolerancia en el calculo que se usa como ROUND en las fórmulas.  
    BEGIN
      SELECT R.GLS_GENERICACORTA
        INTO v_num_toleran_chk
        FROM DESCOD R
       WHERE R.COD_REG_STATUS = 0
         AND R.COD_TABLA = 'DGLCONFG'
         AND R.COD_GENERICO = 'NUM_ROUND_TOLERANCIA';
    EXCEPTION 
      WHEN OTHERS THEN
        v_num_toleran_chk := 13;
    END;

    DELETE FROM DGL_CHK_INFO_WK 
     WHERE COD_CLIENTE = pi_num_iden 
       AND COD_TIPO_DES = pi_cod_tipo_des 
       AND COD_SUBTIPO_DES = pi_cod_subtipo_des 
       AND COD_MONEDA = pi_cod_moneda 
       AND COD_TIPO_INFORME IN ('INF5', 'INF6');
    COMMIT;

    -- Informe 5 - Totales de Balance
    ------------------------------------------------------------------------------------------------------------------------------------------
    -- Informe 1 => Total Act.Circulantes - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.1 => 8 Total Act.Circulantes';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA01' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B8_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B8_CALC AS VAL_CONCEPTO_ONLINE, 
             '[1]+[2]+[3]+[4]+[5]+[6]+[7]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             49 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '8 Total Act.Circulantes' AS GLS_OBSERVACION,
             DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B8 AS VAL_B8_CALC, B.VAL_B8 AS VAL_B8_BD, A.VAL_B8-B.VAL_B8 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B8 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B1, VAL_B2, VAL_B3, VAL_B4, VAL_B5, VAL_B6, VAL_B7, ROUND(VAL_B1+VAL_B2+VAL_B3+VAL_B4+VAL_B5+VAL_B6+VAL_B7,v_num_toleran_chk) AS VAL_B8, 
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B1, VAL_B2, VAL_B3, VAL_B4, VAL_B5, VAL_B6, VAL_B7
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B1, VAL_B2, VAL_B3, VAL_B4, VAL_B5, VAL_B6, VAL_B7
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B1) VAL_B1, SUM(VAL_B2) VAL_B2, SUM(VAL_B3) VAL_B3, SUM(VAL_B4) VAL_B4, SUM(VAL_B5) VAL_B5, SUM(VAL_B6) VAL_B6, SUM(VAL_B7) VAL_B7
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 1 AND V.VAL_CONCEPTO IS NOT NULL THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B1,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 9 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B2,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 14 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B3,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 19 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B4,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 31 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B5,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 37 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B6,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 42 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B7
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (1,9,14,19,31,37,42)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND((VAL_B1+VAL_B2+VAL_B3+VAL_B4+VAL_B5+VAL_B6+VAL_B7),0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B8
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B8,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B8
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (49)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B8 <> B.VAL_B8)
                 AND ABS(A.VAL_B8-B.VAL_B8) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO);
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 2 => Retasación Técnica del AF - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.2 => 10 Retasación Técnica del AF';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA02' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B10_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B10_CALC AS VAL_CONCEPTO_ONLINE, 
             '[10.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             62 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '10 Retasación Técnica del AF' AS GLS_OBSERVACION,
             DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B10 AS VAL_B10_CALC, B.VAL_B10 AS VAL_B10_BD, A.VAL_B10-B.VAL_B10 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B10 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, ROUND(VAL_B10,v_num_toleran_chk) AS VAL_B10, 
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B10
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA,VAL_B10
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B10) VAL_B10
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 63 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B10
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (63)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B10,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B10
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B10,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B10
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (62)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B10 <> B.VAL_B10)
                 AND ABS(A.VAL_B10-B.VAL_B10) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO);
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 3 => Depreciación Acumulada - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.3 => 11 Depreciación Acumulada';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA03' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B11_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B11_CALC AS VAL_CONCEPTO_ONLINE, 
             '[11.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             64 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '11 Depreciación Acumulada' AS GLS_OBSERVACION,
             DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B11 AS VAL_B11_CALC, B.VAL_B11 AS VAL_B11_BD, A.VAL_B11-B.VAL_B11 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B11 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, ROUND(VAL_B11,v_num_toleran_chk) AS VAL_B11, 
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B11
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B11
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B11) VAL_B11
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 65 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B11
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden   
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (65)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B11,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B11
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B11,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B11
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden   
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (64)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B11 <> B.VAL_B11)
                 AND ABS(A.VAL_B11-B.VAL_B11) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO);
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 4 => 12 Activo Fijo Neto - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.4 => 12 Activo Fijo Neto';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA04' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B12_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B12_CALC AS VAL_CONCEPTO_ONLINE, 
             '[9]+[10]+[11]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             66 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '12 Activo Fijo Neto' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B12 AS VAL_B12_CALC, B.VAL_B12 AS VAL_B12_BD, A.VAL_B12-B.VAL_B12 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B12 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B9, VAL_B10, VAL_B11, ROUND(VAL_B9+VAL_B10+VAL_B11,v_num_toleran_chk) AS VAL_B12,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B9, VAL_B10, VAL_B11
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B9, VAL_B10, VAL_B11
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B9) VAL_B9, SUM(VAL_B10) VAL_B10, SUM(VAL_B11) VAL_B11
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 50 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B9,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 62 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B10,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 64 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B11
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (50,62,64)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B9+VAL_B10+VAL_B11,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B12
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B12,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B12
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (66)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B12 <> B.VAL_B12)
                 AND ABS(A.VAL_B12-B.VAL_B12) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO);
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 5 => 17 Total Activos LP - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.5 => 17 Total Activos LP';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA05' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B17_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B17_CALC AS VAL_CONCEPTO_ONLINE, 
             '[12]+[13]+[14]+[15]+[16]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             96 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '17 Total Activos LP' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B17 AS VAL_B17_CALC, B.VAL_B17 AS VAL_B17_BD, A.VAL_B17-B.VAL_B17 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B17 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B12, VAL_B13, VAL_B14, VAL_B15, VAL_B16, ROUND(VAL_B12+VAL_B13+VAL_B14+VAL_B15+VAL_B16,v_num_toleran_chk) AS VAL_B17,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B12, VAL_B13, VAL_B14, VAL_B15, VAL_B16
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B12, VAL_B13, VAL_B14, VAL_B15, VAL_B16
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B12) VAL_B12, SUM(VAL_B13) VAL_B13, SUM(VAL_B14) VAL_B14, SUM(VAL_B15) VAL_B15, SUM(VAL_B16) VAL_B16
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 66 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B12,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 67 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B13,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 74 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B14,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 80 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B15,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 88 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B16
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (66,67,74,80,88)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B12+VAL_B13+VAL_B14+VAL_B15+VAL_B16,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B17
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B17,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B17
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (96)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B17 <> B.VAL_B17)
                 AND ABS(A.VAL_B17-B.VAL_B17) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO);
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 6 => 18 TOTAL ACTIVOS - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.6 => 18 TOTAL ACTIVOS';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA06' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B18_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B18_CALC AS VAL_CONCEPTO_ONLINE, 
             '[8]+[17]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             97 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '18 TOTAL ACTIVOS' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B18 AS VAL_B18_CALC, B.VAL_B18 AS VAL_B18_BD, A.VAL_B18-B.VAL_B18 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B18 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B8, VAL_B17, ROUND(VAL_B8+VAL_B17,v_num_toleran_chk) AS VAL_B18,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B8, VAL_B17
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B8, VAL_B17
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B8) VAL_B8, SUM(VAL_B17) VAL_B17
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 49 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B8,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 96 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B17
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (49,96)   
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B8+VAL_B17,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B18
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B18,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B18
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (97)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B18 <> B.VAL_B18)
                 AND ABS(A.VAL_B18-B.VAL_B18) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 7 => 20 Porción CP Deuda Bancos LP - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.7 => 20 Porción CP Deuda Bancos LP ';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA07' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B20_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B20_CALC AS VAL_CONCEPTO_ONLINE, 
             '[20.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             105 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '20 Porción CP Deuda Bancos LP ' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B20 AS VAL_B20_CALC, B.VAL_B20 AS VAL_B20_BD, A.VAL_B20-B.VAL_B20 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B20 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B20_1, ROUND(VAL_B20_1,v_num_toleran_chk) AS VAL_B20,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B20_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B20_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA,SUM(VAL_B20_1) VAL_B20_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 106 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B20_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (106)   
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B20,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B20
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B20,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B20
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (105)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B20 <> B.VAL_B20)
                 AND ABS(A.VAL_B20-B.VAL_B20) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 8 => 23 Obligaciones Leasing CP - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.8 => 23 Obligaciones Leasing CP';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA08' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B23_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B23_CALC AS VAL_CONCEPTO_ONLINE, 
             '[23.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             120 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '23 Obligaciones Leasing CP ' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B23 AS VAL_B23_CALC, B.VAL_B23 AS VAL_B23_BD, A.VAL_B23-B.VAL_B23 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B23 
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B23_1, ROUND(VAL_B23_1,v_num_toleran_chk) AS VAL_B23,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B23_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B23_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B23_1) VAL_B23_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 121 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B23_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (121)   
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B23,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B23
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B23,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B23
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (120)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B23 <> B.VAL_B23)
                 AND ABS(A.VAL_B23-B.VAL_B23) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 9 => 28 Total Pasivos Circulantes - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.9 => 28 Total Pasivos Circulantes';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA09' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B28_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B28_CALC AS VAL_CONCEPTO_ONLINE, 
             '[19]+[20]+[21]+[22]+[23]+[24]+[25]+[26]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             142 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '28 Total Pasivos Circulantes' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B28 AS VAL_B28_CALC, B.VAL_B28 AS VAL_B28_BD, A.VAL_B28-B.VAL_B28 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B28
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B19, VAL_B20, VAL_B21, VAL_B22, VAL_B23, VAL_B24, VAL_B25, VAL_B26, VAL_B27, ROUND(VAL_B19+VAL_B20+VAL_B21+VAL_B22+VAL_B23+VAL_B24+VAL_B25+VAL_B26+VAL_B27,v_num_toleran_chk) AS VAL_B28,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B19, VAL_B20, VAL_B21, VAL_B22, VAL_B23, VAL_B24, VAL_B25, VAL_B26, VAL_B27
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B19, VAL_B20, VAL_B21, VAL_B22, VAL_B23, VAL_B24, VAL_B25, VAL_B26, VAL_B27
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B19) VAL_B19, SUM(VAL_B20) VAL_B20, SUM(VAL_B21) VAL_B21, SUM(VAL_B22) VAL_B22, SUM(VAL_B23) VAL_B23, SUM(VAL_B24) VAL_B24, SUM(VAL_B25) VAL_B25, SUM(VAL_B26) VAL_B26, SUM(VAL_B27) VAL_B27
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 98 AND V.VAL_CONCEPTO IS NOT NULL THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B19,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 105 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B20,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 107 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B21,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 112 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B22,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 120 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B23,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 122 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B24,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 125 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B25,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 129 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B26,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 133 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B27
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (98,105,107,112,120,122,125,129,133)   
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B19+VAL_B20+VAL_B21+VAL_B22+VAL_B23+VAL_B24+VAL_B25+VAL_B26+VAL_B27,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B28
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B28,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B28
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (142)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B28 <> B.VAL_B28)
                 AND ABS(A.VAL_B28-B.VAL_B28) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 10 => 29 Deuda Bancos LP - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.10 => 29 Deuda Bancos LP';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA10' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B29_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B29_CALC AS VAL_CONCEPTO_ONLINE, 
             '[29.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             143 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '29 Deuda Bancos LP' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B29 AS VAL_B29_CALC, B.VAL_B29 AS VAL_B29_BD, A.VAL_B29-B.VAL_B29 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B29
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B29_1, ROUND(VAL_B29_1,v_num_toleran_chk) AS VAL_B29,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B29_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B29_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B29_1) VAL_B29_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 144 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B29_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (144)   
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B29,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B29
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B29,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B29
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (143)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B29 <> B.VAL_B29)
                 AND ABS(A.VAL_B29-B.VAL_B29) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 11 => 31 Obligaciones Leasing LP - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.11 => 31 Obligaciones Leasing LP';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA11' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B31_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B31_CALC AS VAL_CONCEPTO_ONLINE, 
             '[31.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             148 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '31 Obligaciones Leasing LP' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B31 AS VAL_B31_CALC, B.VAL_B31 AS VAL_B31_BD, A.VAL_B31-B.VAL_B31 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B31
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B31_1, ROUND(VAL_B31_1,v_num_toleran_chk) AS VAL_B31,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B31_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B31_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B31_1) VAL_B31_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 149 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B31_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (149)   
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B31,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B31
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B31,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B31
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (148)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B31 <> B.VAL_B31)
                 AND ABS(A.VAL_B31-B.VAL_B31) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 12 => 33 Total Pasivos LP - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.12 => 31 Obligaciones Leasing LP';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA12' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B33_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B33_CALC AS VAL_CONCEPTO_ONLINE, 
             '[29]+[30]+[31]+[32]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             158 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '33 Total Pasivos LP' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B33 AS VAL_B33_CALC, B.VAL_B33 AS VAL_B33_BD, A.VAL_B33-B.VAL_B33 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B33
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B29, VAL_B30, VAL_B31, VAL_B32, ROUND(VAL_B29+VAL_B30+VAL_B31+VAL_B32,v_num_toleran_chk) AS VAL_B33,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B29, VAL_B30, VAL_B31, VAL_B32
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B29, VAL_B30, VAL_B31, VAL_B32
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B29) VAL_B29, SUM(VAL_B30) VAL_B30, SUM(VAL_B31) VAL_B31, SUM(VAL_B32) VAL_B32
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 143 AND V.VAL_CONCEPTO IS NOT NULL THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B29,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 145 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B30,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 148 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B31,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 150 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B32
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (143,145,148,150)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B29+VAL_B30+VAL_B31+VAL_B32,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B33
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B33,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B33
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (158)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B33 <> B.VAL_B33)
                 AND ABS(A.VAL_B33-B.VAL_B33) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 13 => 34 TOTAL PASIVOS - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.13 => 34 TOTAL PASIVOS';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA13' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B34_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B34_CALC AS VAL_CONCEPTO_ONLINE, 
             '[28]+[33]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             159 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '34 TOTAL PASIVOS' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B34 AS VAL_B34_CALC, B.VAL_B34 AS VAL_B34_BD, A.VAL_B34-B.VAL_B34 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B34
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B28, VAL_B33, ROUND(VAL_B28+VAL_B33,v_num_toleran_chk) AS VAL_B34,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B28, VAL_B33
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B28, VAL_B33
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B28) VAL_B28, SUM(VAL_B33) VAL_B33
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 142 AND V.VAL_CONCEPTO IS NOT NULL THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B28,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 158 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B33
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (142,158)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B28+VAL_B33,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B34
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B34,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B34
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (159)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B34 <> B.VAL_B34)
                 AND ABS(A.VAL_B34-B.VAL_B34) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 14 => 36 Revalorización Capital Propio - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.14 => 36 Revalorización Capital Propio';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA14' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B36_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B36_CALC AS VAL_CONCEPTO_ONLINE, 
             '[36.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             163 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '36 Revalorización Capital Propio' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B36 AS VAL_B36_CALC, B.VAL_B36 AS VAL_B36_BD, A.VAL_B36-B.VAL_B36 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B36
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B36_1, ROUND(VAL_B36_1,v_num_toleran_chk) AS VAL_B36,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B36_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B36_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B36_1) VAL_B36_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 164 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B36_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (164)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B36,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B36
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B36,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B36
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (163)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B36 <> B.VAL_B36)
                 AND ABS(A.VAL_B36-B.VAL_B36) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 15 => 37 Reserva Retasación Técnica AF - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.15 => 37 Reserva Retasación Técnica AF';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA15' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B37_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B37_CALC AS VAL_CONCEPTO_ONLINE, 
             '[37.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             165 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '37 Reserva Retasación Técnica AF' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B37 AS VAL_B37_CALC, B.VAL_B37 AS VAL_B37_BD, A.VAL_B37-B.VAL_B37 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B37
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B37_1, ROUND(VAL_B37_1,v_num_toleran_chk) AS VAL_B37,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B37_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B37_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B37_1) VAL_B37_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 166 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B37_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (166)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B37,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B37
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B37,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B37
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (165)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B37 <> B.VAL_B37)
                 AND ABS(A.VAL_B37-B.VAL_B37) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 16 => 42 Utilidad (Pérdida) del Ejercicio - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.16 => 42 Utilidad (Pérdida) del Ejercicio';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA16' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B42_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B42_CALC AS VAL_CONCEPTO_ONLINE, 
             '[42.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             186 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '42 Utilidad (Pérdida) del Ejercicio' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B42 AS VAL_B42_CALC, B.VAL_B42 AS VAL_B42_BD, A.VAL_B42-B.VAL_B42 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B42
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B42_1, ROUND(VAL_B42_1,v_num_toleran_chk) AS VAL_B42,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B42_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B42_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B42_1) VAL_B42_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 187 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B42_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (187)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B42,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B42
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B42,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B42
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (186)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B42 <> B.VAL_B42)
                 AND ABS(A.VAL_B42-B.VAL_B42) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 17 => 44 TOTAL PATRIMONIO - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.17 => 44 TOTAL PATRIMONIO';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA17' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B44_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B44_CALC AS VAL_CONCEPTO_ONLINE, 
             '[35]+[36]+[37]+[38]+[39]+[40]+[41]+[42]+[43]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             188 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '44 TOTAL PATRIMONIO' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B44 AS VAL_B44_CALC, B.VAL_B44 AS VAL_B44_BD, A.VAL_B44-B.VAL_B44 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B44
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B35, VAL_B36, VAL_B37, VAL_B38, VAL_B39, VAL_B40, VAL_B41, VAL_B42, VAL_B43, ROUND(VAL_B35+VAL_B36+VAL_B37+VAL_B38+VAL_B39+VAL_B40+VAL_B41+VAL_B42+VAL_B43,v_num_toleran_chk) AS VAL_B44,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B35, VAL_B36, VAL_B37, VAL_B38, VAL_B39, VAL_B40, VAL_B41, VAL_B42, VAL_B43
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B35, VAL_B36, VAL_B37, VAL_B38, VAL_B39, VAL_B40, VAL_B41, VAL_B42, VAL_B43
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B35) VAL_B35, SUM(VAL_B36) VAL_B36, SUM(VAL_B37) VAL_B37, SUM(VAL_B38) VAL_B38, SUM(VAL_B39) VAL_B39, SUM(VAL_B40) VAL_B40, SUM(VAL_B41) VAL_B41, SUM(VAL_B42) VAL_B42, SUM(VAL_B43) VAL_B43
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 160 AND V.VAL_CONCEPTO IS NOT NULL THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B35,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 163 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B36,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 165 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B37,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 167 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B38,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 172 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B39,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 176 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B40,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 181 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B41,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 186 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B42,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 195 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B43
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (160,163,165,167,172,176,181,186,195)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B35+VAL_B36+VAL_B37+VAL_B38+VAL_B39+VAL_B40+VAL_B41+VAL_B42+VAL_B43,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B44
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B44,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B44
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (188)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B44 <> B.VAL_B44)
                 AND ABS(A.VAL_B44-B.VAL_B44) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 18 => 45 TOTAL PASIVOS+PATRIMONIO - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.18 => 45 TOTAL PASIVOS+PATRIMONIO';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA18' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B45_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B45_CALC AS VAL_CONCEPTO_ONLINE, 
             '[34]+[44]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             189 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '45 TOTAL PASIVOS+PATRIMONIO' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B45 AS VAL_B45_CALC, B.VAL_B45 AS VAL_B45_BD, A.VAL_B45-B.VAL_B45 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B45
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B34, VAL_B44, ROUND(VAL_B34+VAL_B44,v_num_toleran_chk) AS VAL_B45,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B34, VAL_B44
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B34, VAL_B44
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B34) VAL_B34, SUM(VAL_B44) VAL_B44
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 159 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B34,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 188 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B44
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (159,188)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B34+VAL_B44,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B45
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B45,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B45
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (189)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B45 <> B.VAL_B45)
                 AND ABS(A.VAL_B45-B.VAL_B45) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 19 => 46 TOTAL ACTIVOS - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.19 => 46 TOTAL ACTIVOS';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA19' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B46_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B46_CALC AS VAL_CONCEPTO_ONLINE, 
             '[18]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             190 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '46 TOTAL ACTIVOS' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B46 AS VAL_B46_CALC, B.VAL_B46 AS VAL_B46_BD, A.VAL_B46-B.VAL_B46 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B46
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B18, ROUND(VAL_B18,v_num_toleran_chk) AS VAL_B46,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B18
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B18
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B18) VAL_B18
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 97 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B18
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (97)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B46,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B46
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B46,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B46
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (190)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B46 <> B.VAL_B46)
                 AND ABS(A.VAL_B46-B.VAL_B46) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 20 => 43 Participaciones no Controladoras - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 5.20 => 46 TOTAL ACTIVOS';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF5' AS COD_TIPO_INFORME, 
             'BA20' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_B43_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_B43_CALC AS VAL_CONCEPTO_ONLINE, 
             '[18]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             195 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'BALANCE' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '46 TOTAL ACTIVOS' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_B43 AS VAL_B43_CALC, B.VAL_B43 AS VAL_B43_BD, A.VAL_B43-B.VAL_B43 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B43
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B43_1, ROUND(VAL_B43_1,v_num_toleran_chk) AS VAL_B43,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_B43_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_B43_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_B43_1) VAL_B43_1
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 196 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_B43_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (196)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_B43,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B43
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_B43,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_B43
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'BALANCE'
                                              AND VR.COR_CONCEPTO IN (195)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_B43 <> B.VAL_B43)
                 AND ABS(A.VAL_B43-B.VAL_B43) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 6 - Totales de Estado de Resultado
    ------------------------------------------------------------------------------------------------------------------------------------------
    -- Informe 1 => 3 RESULTADO BRUTO - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 6.1  => 3 RESULTADO BRUTO';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF6' AS COD_TIPO_INFORME, 
             'ER1' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_ER3_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_ER3_CALC AS VAL_CONCEPTO_ONLINE, 
             '[1]+[2]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             10 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'EST_RESULT' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '3 RESULTADO BRUTO' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_ER3 AS VAL_ER3_CALC, B.VAL_ER3 AS VAL_ER3_BD, A.VAL_ER3-B.VAL_ER3 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER3
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO,  VAL_ER1, VAL_ER2, ROUND(VAL_ER1+VAL_ER2,v_num_toleran_chk) AS VAL_ER3,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_ER1, VAL_ER2
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_ER1, VAL_ER2
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_ER1) VAL_ER1, SUM(VAL_ER2) VAL_ER2
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 1 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER1,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 7 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER2
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO IN (1,7)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_ER1+VAL_ER2,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER3
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER3,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_ER3
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'EST_RESULT'
                                              AND VR.COR_CONCEPTO IN (10)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_ER3 <> B.VAL_ER3)
                 AND ABS(A.VAL_ER3-B.VAL_ER3) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 2 => 5 RESULTADO OPERACIONAL - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 6.2 => 5 RESULTADO OPERACIONAL';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF6' AS COD_TIPO_INFORME, 
             'ER2' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_ER5_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_ER5_CALC AS VAL_CONCEPTO_ONLINE, 
             '[3]+[4]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             17 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'EST_RESULT' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '5 RESULTADO OPERACIONAL' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_ER5 AS VAL_ER5_CALC, B.VAL_ER5 AS VAL_ER5_BD, A.VAL_ER5-B.VAL_ER5 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER5
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO,  VAL_ER3, VAL_ER4, ROUND(VAL_ER3+VAL_ER4,v_num_toleran_chk) AS VAL_ER5,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_ER3, VAL_ER4
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_ER3, VAL_ER4
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_ER3) VAL_ER3, SUM(VAL_ER4) VAL_ER4
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 10 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER3,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 11 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER4
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO IN (10,11)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_ER3+VAL_ER4,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER5
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER5,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_ER5
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'EST_RESULT'
                                              AND VR.COR_CONCEPTO IN (17)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_ER5 <> B.VAL_ER5)
                 AND ABS(A.VAL_ER5-B.VAL_ER5) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 3 => 12 RESULTADO ANTES DE IMPUESTO - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 6.3 => 12 RESULTADO ANTES DE IMPUESTO';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF6' AS COD_TIPO_INFORME, 
             'ER3' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_ER12_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_ER12_CALC AS VAL_CONCEPTO_ONLINE, 
             '[5]+[6]+[7]+[8]+[9]+[10]+[11]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             50 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'EST_RESULT' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '12 RESULTADO ANTES DE IMPUESTO' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_ER12 AS VAL_ER12_CALC, B.VAL_ER12 AS VAL_ER12_BD, A.VAL_ER12-B.VAL_ER12 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER12
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER5, VAL_ER6, VAL_ER7, VAL_ER8, VAL_ER9, VAL_ER10, VAL_ER11, ROUND(VAL_ER5+VAL_ER6+VAL_ER7+VAL_ER8+VAL_ER9+VAL_ER10+VAL_ER11,v_num_toleran_chk) AS VAL_ER12,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_ER5, VAL_ER6, VAL_ER7, VAL_ER8, VAL_ER9, VAL_ER10, VAL_ER11
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_ER5, VAL_ER6, VAL_ER7, VAL_ER8, VAL_ER9, VAL_ER10, VAL_ER11
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_ER5) VAL_ER5, SUM(VAL_ER6) VAL_ER6, SUM(VAL_ER7) VAL_ER7, SUM(VAL_ER8) VAL_ER8, SUM(VAL_ER9) VAL_ER9, SUM(VAL_ER10) VAL_ER10, SUM(VAL_ER11) VAL_ER11 
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 17 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER5,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 18 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER6,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 27 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER7,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 30 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER8,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 35 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER9,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 43 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER10,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 46 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER11
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO IN (17,18,27,30,35,43,46)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_ER5+VAL_ER6+VAL_ER7+VAL_ER8+VAL_ER9+VAL_ER10+VAL_ER11,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER12
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER12,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_ER12
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'EST_RESULT'
                                              AND VR.COR_CONCEPTO IN (50)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_ER12 <> B.VAL_ER12)
                 AND ABS(A.VAL_ER12-B.VAL_ER12) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 4 => 13 Impuesto - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 6.4   => 13 Impuesto';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF6' AS COD_TIPO_INFORME, 
             'ER4' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_ER13_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_ER13_CALC AS VAL_CONCEPTO_ONLINE, 
             '[13.1]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             51 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'EST_RESULT' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '13 Impuesto' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_ER13 AS VAL_ER13_CALC, B.VAL_ER13 AS VAL_ER13_BD, A.VAL_ER13-B.VAL_ER13 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER13
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER13_1, ROUND(VAL_ER13_1,v_num_toleran_chk) AS VAL_ER13,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_ER13_1
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_ER13_1
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_ER13_1) VAL_ER13_1 
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 52 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER13_1
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO IN (52)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_ER13,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER13
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER13,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_ER13
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'EST_RESULT'
                                              AND VR.COR_CONCEPTO IN (51)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_ER13 <> B.VAL_ER13)
                 AND ABS(A.VAL_ER13-B.VAL_ER13) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 5 => 14 UTIL.(PERD.) DEL EJERCICIO - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 6.5 => 14 UTIL.(PERD.) DEL EJERCICIO';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF6' AS COD_TIPO_INFORME, 
             'ER5' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_ER14_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_ER14_CALC AS VAL_CONCEPTO_ONLINE, 
             '[12]+[13]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             53 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'EST_RESULT' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '14 UTIL.(PERD.) DEL EJERCICIO' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_ER14 AS VAL_ER14_CALC, B.VAL_ER14 AS VAL_ER14_BD, A.VAL_ER14-B.VAL_ER14 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER14
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER12, VAL_ER13, ROUND(VAL_ER12+VAL_ER13,v_num_toleran_chk) AS VAL_ER14,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_ER12, VAL_ER13
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_ER12, VAL_ER13
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_ER12) VAL_ER12, SUM(VAL_ER13) VAL_ER13 
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 50 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER12,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 51 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER13
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO IN (50,51)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_ER12+VAL_ER13,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER14
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER14,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_ER14
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'EST_RESULT'
                                              AND VR.COR_CONCEPTO IN (53)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_ER14 <> B.VAL_ER14)
                 AND ABS(A.VAL_ER14-B.VAL_ER14) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 6 => 15 UTIL.(PERD.) DEL EJERCICIO BCE. - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 6.6 => 15 UTIL.(PERD.) DEL EJERCICIO BCE.';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF6' AS COD_TIPO_INFORME, 
             'ER6' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_ER15_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_ER15_CALC AS VAL_CONCEPTO_ONLINE, 
             '[BALANCE.42]' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             54 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'EST_RESULT' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '15 UTIL.(PERD.) DEL EJERCICIO BCE.' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_ER15 AS VAL_ER15_CALC, B.VAL_ER15 AS VAL_ER15_BD, A.VAL_ER15-B.VAL_ER15 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER15
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_BA42, ROUND(VAL_BA42,v_num_toleran_chk) AS VAL_ER15,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_BA42
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_BA42
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_BA42) VAL_BA42
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO = 186 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_BA42
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'BALANCE' AND C.COR_CONCEPTO IN (186)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_ER15,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER15
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER15,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_ER15
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'EST_RESULT'
                                              AND VR.COR_CONCEPTO IN (54)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_ER15 <> B.VAL_ER15)
                 AND ABS(A.VAL_ER15-B.VAL_ER15) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 7 => 23 Saldo Final Efectivo y Equiv. A Efectivo - OK COD_PLANTILLA
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_GEN_CHK_INFORMES2 -- Informe 6.7 => 23 Saldo Final Efectivo y Equiv. A Efectivo';
      INSERT INTO DGL_CHK_INFO_WK (FEC_CHECK, COD_TIPO_INFORME, GLS_FACTOR_INFORME, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, NUM_RANK, 
                                FEC_USER_MODIF, GLS_USER_MODIF, VAL_DEFLAC_BASE, VAL_DEFLAC_PERIODO, VAL_DEFLAC_CALC, VAL_CONCEPTO_BD, VAL_DIF, VAL_CONCEPTO_ONLINE, 
                                GLS_CONCEPTO_FORMULA, MTO_ACTIVO, MTO_PASIVO_PATR, COR_CONCEPTO, COR_VALOR, COD_TIPO_CONCEP, COD_ESTADO, GLS_OBSERVACION, COD_PLANTILLA)
      SELECT SYSTIMESTAMP AS FEC_CHECK, 
             'INF6' AS COD_TIPO_INFORME, 
             'ER6' AS GLS_FACTOR_INFORME, 
             DL.COD_CLIENTE, 
             DL.COD_TIPO_DES, 
             DL.COD_SUBTIPO_DES, 
             DL.COD_MONEDA, 
             P.FEC_PERIODO, 
             0 AS NUM_RNK, 
             DL.FEC_USER_MODIF, 
             DL.NOM_USER_MODIF AS GLS_USER_MODIF, 
             DL.VAL_DEFLAC_BASE, 
             P.VAL_DEFLAC_PRD, 
             DL.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD VAL_DEFLAC_CALC, 
             VAL_ER23_BD AS VAL_CONCEPTO_BD, 
             VAL_DIF, 
             VAL_ER23_CALC AS VAL_CONCEPTO_ONLINE, 
             '([21]+[22])' AS GLS_CONCEPTO_FORMULA, 
             NULL AS MTO_ACTIVO, 
             NULL AS MTO_PASIVO_PATR, 
             62 COR_CONCEPTO, 
             NULL COR_VALOR, 
             'EST_RESULT' COD_TIPO_CONCEP, 
             'GN' AS COD_ESTADO, 
             '23 Saldo Final Efectivo y Equiv. A Efectivo' AS GLS_OBSERVACION, DL.COD_PLANTILLA
        FROM (
              SELECT DISTINCT A.COD_CLIENTE, A.COD_TIPO_DES, A.COD_SUBTIPO_DES, A.COD_MONEDA, A.COD_PLANTILLA, A.FEC_PERIODO, A.VAL_ER23 AS VAL_ER23_CALC, B.VAL_ER23 AS VAL_ER23_BD, A.VAL_ER23-B.VAL_ER23 AS VAL_DIF       
                FROM (
                      SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER23
                        FROM (
                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER21, VAL_ER22,  ROUND(VAL_ER21+VAL_ER22,v_num_toleran_chk) AS VAL_ER23,  
                                     RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                FROM (                               
                                      SELECT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, R.FEC_PERIODO, VAL_ER21, VAL_ER22
                                        FROM (
                                              SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, VAL_ER21, VAL_ER22
                                                FROM (
                                                      SELECT DISTINCT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA, SUM(VAL_ER21) VAL_ER21, SUM(VAL_ER22) VAL_ER22
                                                        FROM (      
                                                              SELECT D.COD_CLIENTE, D.COD_TIPO_DES, D.COD_SUBTIPO_DES, D.COD_MONEDA, D.COD_PLANTILLA, P.FEC_PERIODO, P.NUM_MESES, P.VAL_DEFLAC_PRD, D.VAL_DEFLAC_BASE, D.VAL_IVA,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 60 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER21,
                                                                     CASE
                                                                       WHEN C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO = 61 AND V.VAL_CONCEPTO IS NOT NULL  THEN VAL_CONCEPTO*(D.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)
                                                                       ELSE 0
                                                                     END VAL_ER22
                                                                FROM DESGLO_WK D
                                                               INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = D.COD_CLIENTE AND P.COD_TIPO_DES = D.COD_TIPO_DES AND P.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND P.COD_MONEDA = D.COD_MONEDA AND P.COD_PLANTILLA = D.COD_PLANTILLA)
                                                               INNER JOIN CONCEP C ON (C.COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN')) 
                                                                LEFT JOIN VALPRD_WK V ON (V.COD_CLIENTE = D.COD_CLIENTE AND V.COD_TIPO_DES = D.COD_TIPO_DES AND V.COD_SUBTIPO_DES = D.COD_SUBTIPO_DES AND V.COD_MONEDA = D.COD_MONEDA AND V.COD_PLANTILLA = D.COD_PLANTILLA AND V.FEC_PERIODO = P.FEC_PERIODO AND V.COD_TIPO_CONCEP=C.COD_TIPO_CONCEP AND V.COR_CONCEPTO=C.COR_CONCEPTO)  
                                                               WHERE D.COD_CLIENTE = pi_num_iden
                                                                 AND D.COD_TIPO_DES = pi_cod_tipo_des
                                                                 AND D.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                                                 AND D.COD_MONEDA = pi_cod_moneda
                                                                 AND D.COD_PLANTILLA = PI_COD_PLANTILLA
                                                                 AND C.COD_TIPO_CONCEP = 'EST_RESULT' AND C.COR_CONCEPTO IN (60, 61)  
                                                             )
                                                       GROUP BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, NUM_MESES, VAL_DEFLAC_PRD, VAL_DEFLAC_BASE, VAL_IVA    
                                                     )
                                             ) R
                                       RIGHT JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA AND R.FEC_PERIODO = P.FEC_PERIODO)
                                     )
                               ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                             )
                       INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                       WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                         AND ROUND(VAL_ER21+VAL_ER22,0) <> 0
                     ) A
                LEFT JOIN (
                           SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER23
                             FROM (
                                   SELECT COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA, FEC_PERIODO, VAL_ER23,
                                          RANK() OVER ( PARTITION BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA ORDER BY FEC_PERIODO DESC ) AS NUM_RNK
                                     FROM (
                                           SELECT DISTINCT R.COD_CLIENTE, R.COD_TIPO_DES, R.COD_SUBTIPO_DES, R.COD_MONEDA, R.COD_PLANTILLA, P.FEC_PERIODO, ROUND((R.VAL_DEFLAC_BASE/P.VAL_DEFLAC_PRD)*VR.VAL_CONCEPTO,v_num_toleran_chk) AS VAL_ER23
                                             FROM DESGLO_WK R
                                            INNER JOIN PERIOD_WK P ON (R.COD_CLIENTE = P.COD_CLIENTE AND R.COD_TIPO_DES = P.COD_TIPO_DES AND R.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND R.COD_MONEDA = P.COD_MONEDA AND R.COD_PLANTILLA = P.COD_PLANTILLA)
                                             LEFT JOIN VALPRD_WK VR ON (VR.COD_CLIENTE = P.COD_CLIENTE AND VR.COD_TIPO_DES = P.COD_TIPO_DES AND VR.COD_SUBTIPO_DES = P.COD_SUBTIPO_DES AND VR.COD_MONEDA = P.COD_MONEDA AND VR.COD_PLANTILLA = P.COD_PLANTILLA AND VR.FEC_PERIODO = P.FEC_PERIODO)
                                            WHERE R.COD_CLIENTE = pi_num_iden
                                              AND R.COD_TIPO_DES = pi_cod_tipo_des
                                              AND R.COD_SUBTIPO_DES = pi_cod_subtipo_des
                                              AND R.COD_MONEDA = pi_cod_moneda
                                              AND R.COD_PLANTILLA = PI_COD_PLANTILLA
                                              AND VR.COD_TIPO_CONCEP = 'EST_RESULT'
                                              AND VR.COR_CONCEPTO IN (62)
                                          )
                                    ORDER BY COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA DESC
                                  )
                            INNER JOIN DESCOD D ON ( D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
                            WHERE NUM_RNK <= D.GLS_GENERICACORTA     
                          ) B ON (B.COD_CLIENTE = A.COD_CLIENTE AND B.COD_TIPO_DES = A.COD_TIPO_DES AND B.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND B.COD_MONEDA = A.COD_MONEDA AND B.COD_PLANTILLA = A.COD_PLANTILLA AND B.FEC_PERIODO = A.FEC_PERIODO)
               INNER JOIN DESCOD D ON (D.COD_GENERICO = 'NUM_PER_CHK_GLOBAL' AND D.COD_REG_STATUS = 0 AND D.COD_TABLA = 'DGLCONFG')
               WHERE (B.COD_CLIENTE IS NULL
                  OR A.VAL_ER23 <> B.VAL_ER23)
                 AND ABS(A.VAL_ER23-B.VAL_ER23) > ( D.GLS_GENERICALARGA / 1000)
             ) A
       INNER JOIN DESGLO_WK DL ON (A.COD_CLIENTE = DL.COD_CLIENTE AND A.COD_TIPO_DES = DL.COD_TIPO_DES AND A.COD_SUBTIPO_DES = DL.COD_SUBTIPO_DES AND A.COD_MONEDA = DL.COD_MONEDA AND A.COD_PLANTILLA = DL.COD_PLANTILLA)
       INNER JOIN PERIOD_WK P ON (P.COD_CLIENTE = A.COD_CLIENTE AND P.COD_TIPO_DES = A.COD_TIPO_DES AND P.COD_SUBTIPO_DES = A.COD_SUBTIPO_DES AND P.COD_MONEDA = A.COD_MONEDA AND P.COD_PLANTILLA = A.COD_PLANTILLA AND P.FEC_PERIODO = A.FEC_PERIODO); 
      v_count_casos := SQL%ROWCOUNT;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des, pi_cod_subtipo_des, pi_cod_moneda, v_gls_log, PI_COD_PLANTILLA);  
    COMMIT;

    -- Informe 8 => 24 Saldo Efectivo Balance
    -- NO SE IMPLEMENTA ESTA VALIDACION POR NO SE ESTA CUENTA IMPRIMEBLE
  END DGL_SP_GEN_CHK_INFORMES2_WK;    

  PROCEDURE DGL_SP_CLONAR_DESGLOSE_WK
  (
    pi_num_iden             IN  VARCHAR2,
    pi_cod_tipo_des_ori     IN  VARCHAR2,
    pi_cod_subtipo_des_ori  IN  VARCHAR2,
    pi_cod_moneda_ori       IN  VARCHAR2,
    pi_cod_tipo_des_des     IN  VARCHAR2,
    pi_cod_subtipo_des_des  IN  VARCHAR2,
    pi_cod_moneda_des       IN  VARCHAR2,
    pi_login_name           IN  VARCHAR2,
    po_retorno              OUT  NUMBER,
    po_glosa                OUT  VARCHAR2,
    PI_COD_PLANTILLA        IN  VARCHAR2 DEFAULT '0'
  )

  IS
    v_cod                         NUMBER;
    v_gls                         VARCHAR2(200);
    v_count_casos                 NUMBER;
    v_count_total                 NUMBER;
    v_num_toleran_chk             NUMBER:= 13;
    v_gls_log                     VARCHAR2(300);
    C_COD_TIPO_PROCESO            CONSTANT VARCHAR2(10) := 'CLONAR_DES';
    C_ESTADO_BORRADOR_EN_PROCESO  CONSTANT VARCHAR2(3) := 'BEP';
    V_OPERATOR_CODE               NUMBER;
  BEGIN
    po_retorno:= 0;
    v_gls_log := 'DGL_PKG_DESGLOSE.DGL_SP_CLONAR_DESGLOSE -- Inicio proceso de Clonacion de Desglose. pi_num_iden['||pi_num_iden||'] pi_cod_tipo_des_ori['||pi_cod_tipo_des_ori||'] pi_cod_subtipo_des_ori['||pi_cod_subtipo_des_ori||'] pi_cod_moneda_ori['||pi_cod_moneda_ori||'] En -->pi_cod_tipo_des_des['||pi_cod_tipo_des_des||'] pi_cod_subtipo_des_des['||pi_cod_subtipo_des_des||'] pi_cod_moneda_des['||pi_cod_moneda_des||'] PI_COD_PLANTILLA['||PI_COD_PLANTILLA||'] con USR['||pi_login_name||']'; 
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log, PI_COD_PLANTILLA);
    -- Clonar tabla DESGLO
    BEGIN
      v_gls_log := 'DGL_PKG_DESGLOSE.DGL_SP_CLONAR_DESGLOSE -- Clonando tabla DESGLO';
      v_count_casos:= 0;
      FOR c_desglo IN (SELECT COD_CLIENTE,
                              COD_UNID_MONEDA,
                              GLS_SUBTIPO_DES,
                              VAL_DEFLAC_BASE,
                              FEC_DEFLAC_BASE,      
                              VAL_IVA,
                              GLS_OBSERVACION,
                              COD_MONEDA_IMP,
                              NOM_USER,
                              FLG_VALIDACION,
                              COD_ESTADO,
                              ROL_EMPLEADO_REV_MOD,
                              COD_PLANTILLA
                         FROM DESGLO_WK   
                        WHERE COD_CLIENTE = pi_num_iden
                          AND COD_TIPO_DES = pi_cod_tipo_des_ori
                          AND COD_SUBTIPO_DES = pi_cod_subtipo_des_ori
                          AND COD_MONEDA = pi_cod_moneda_ori
                          AND COD_PLANTILLA = PI_COD_PLANTILLA
                      )
      LOOP
        LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, '->Insertando en DESGLO: codCliente['||c_desglo.COD_CLIENTE||'] PI_COD_PLANTILLA['||c_desglo.cod_plantilla||'] pi_cod_tipo_des_des['||pi_cod_tipo_des_des||'] pi_cod_subtipo_des_des['||pi_cod_subtipo_des_des||'] pi_cod_moneda_des['||pi_cod_moneda_des||']', PI_COD_PLANTILLA);
        -- SE SELECCIONA EL CÓDIGO DE OPERADOR DADO EL USUARIO LOGUEADO.
        SELECT OPERATOR_CODE INTO V_OPERATOR_CODE FROM SF_STF_OPERATOR WHERE LOGIN_NAME = pi_login_name;
        DGL_SP_INSUPD_DESGLO(c_desglo.COD_CLIENTE, pi_cod_tipo_des_des, pi_cod_subtipo_des_des, pi_cod_moneda_des, 
        c_desglo.COD_UNID_MONEDA, c_desglo.GLS_SUBTIPO_DES, c_desglo.VAL_DEFLAC_BASE, c_desglo.FEC_DEFLAC_BASE, c_desglo.VAL_IVA, 
        c_desglo.GLS_OBSERVACION, pi_login_name, c_desglo.COD_MONEDA_IMP, pi_login_name, 'NOK', C_ESTADO_BORRADOR_EN_PROCESO, NULL, 
        V_OPERATOR_CODE, v_cod, v_gls, PI_COD_PLANTILLA);
        v_count_casos:= v_count_casos + 1;
      END LOOP;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
      po_retorno:= -1;
      po_glosa:= v_gls_log;
      RETURN;
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log, PI_COD_PLANTILLA);
    --  Clonar tabla CONIMP
    BEGIN
      v_gls_log := 'DGL_PKG_DESGLOSE.DGL_SP_CLONAR_DESGLOSE -- Clonando tabla CONIMP';
      v_count_casos:= 0;
      FOR c_conimp IN (SELECT COD_CLIENTE,
                              COD_TIPO_CONCEP,
                              COR_CONCEPTO
                         FROM CONIMP_WK    
                        WHERE COD_CLIENTE = pi_num_iden
                          AND COD_TIPO_DES = pi_cod_tipo_des_ori
                          AND COD_SUBTIPO_DES = pi_cod_subtipo_des_ori
                          AND COD_MONEDA = pi_cod_moneda_ori
                          AND COD_PLANTILLA = PI_COD_PLANTILLA
                      )
      LOOP
        LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, '->Insertando en CONIMP: codCliente['||c_conimp.COD_CLIENTE||'] pi_cod_tipo_des_des['||pi_cod_tipo_des_des||'] pi_cod_subtipo_des_des['||pi_cod_subtipo_des_des||'] pi_cod_moneda_des['||pi_cod_moneda_des||'] codTipoConcep['||c_conimp.COD_TIPO_CONCEP||'] corConcepto['||c_conimp.COR_CONCEPTO||'] PI_COD_PLANTILLA['||PI_COD_PLANTILLA||']');
        DGL_SP_INS_CONIMP_WK (c_conimp.COD_CLIENTE, pi_cod_tipo_des_des, pi_cod_subtipo_des_des, pi_cod_moneda_des, c_conimp.COD_TIPO_CONCEP, c_conimp.COR_CONCEPTO, v_cod, v_gls, PI_COD_PLANTILLA);
        v_count_casos:= v_count_casos + 1;
      END LOOP;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
      po_retorno:= -2;
      po_glosa:= v_gls_log;
      RETURN;
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log);
    --  Clonar tabla PERIOD
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_CLONAR_DESGLOSE -- Clonando tabla PERIOD';
      v_count_casos:= 0;
      FOR c_period IN (SELECT COD_CLIENTE,
                              FEC_PERIODO,
                              NUM_MESES,
                              VAL_DEFLAC_PRD,
                              COD_AUDITOR,
                              GLS_OTROS_AUDIT,
                              COD_ESTADO_PRD,
                              COD_ESTADO_AUDI,
                              FEC_INFORM_SBIF,
                              MTO_SBIF_DD_VIG,
                              MTO_SBIF_DD_MOR,
                              MTO_SBIF_DD_VEN,
                              MTO_SBIF_DD_CAS,
                              MTO_SBIF_DI_VIG,
                              MTO_SBIF_DI_VEN,
                              MTO_SBIF_DI_CAS,
                              MTO_SBIF_IMP_90_179,
                              NOM_USER_CREA,
                              FEC_USER_CREA,
                              VAL_TIPO_CAMBIO
                         FROM PERIOD_WK    
                        WHERE COD_CLIENTE = pi_num_iden
                          AND COD_TIPO_DES = pi_cod_tipo_des_ori
                          AND COD_SUBTIPO_DES = pi_cod_subtipo_des_ori
                          AND COD_MONEDA = pi_cod_moneda_ori
                          AND COD_PLANTILLA = PI_COD_PLANTILLA
                      )
      LOOP
        LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, '->Insertando en CONIMP: codCliente['||c_period.COD_CLIENTE||'] pi_cod_tipo_des_des['||pi_cod_tipo_des_des||'] pi_cod_subtipo_des_des['||pi_cod_subtipo_des_des||'] pi_cod_moneda_des['||pi_cod_moneda_des||'] fecPeriodo['||c_period.FEC_PERIODO||'] PI_COD_PLANTILLA['||PI_COD_PLANTILLA||']');
        DGL_SP_INS_PERIOD_WK (c_period.COD_CLIENTE, pi_cod_tipo_des_des, pi_cod_subtipo_des_des, pi_cod_moneda_des, c_period.FEC_PERIODO, c_period.NUM_MESES, c_period.VAL_DEFLAC_PRD, c_period.COD_AUDITOR, c_period.GLS_OTROS_AUDIT, c_period.COD_ESTADO_PRD, c_period.COD_ESTADO_AUDI, c_period.FEC_INFORM_SBIF, c_period.MTO_SBIF_DD_VIG, c_period.MTO_SBIF_DD_MOR, c_period.MTO_SBIF_DD_VEN, c_period.MTO_SBIF_DD_CAS, c_period.MTO_SBIF_DI_VIG, c_period.MTO_SBIF_DI_VEN, c_period.MTO_SBIF_DI_CAS, c_period.MTO_SBIF_IMP_90_179, c_period.NOM_USER_CREA, c_period.VAL_TIPO_CAMBIO, v_cod, v_gls, PI_COD_PLANTILLA);
        v_count_casos:= v_count_casos + 1;
      END LOOP;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';      
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
      po_retorno:= -1;
      po_glosa:= v_gls_log;
      RETURN;
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log);
    --  Clonar tabla VALPRD_WK
    BEGIN
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_CLONAR_DESGLOSE -- Clonando tabla VALPRD_WK';
      v_count_casos:= 0;
      FOR c_valprd IN (SELECT COD_CLIENTE,
                              FEC_PERIODO,
                              COD_TIPO_CONCEP,
                              COR_CONCEPTO,
                              COR_VALOR,
                              VAL_CONCEPTO,
                              GLS_FRML_CONCEP, 
                              GLS_CONCEPTO,
                              VAL_PRC_CONCEP
                         FROM VALPRD_WK    
                        WHERE COD_CLIENTE = pi_num_iden
                          AND COD_TIPO_DES = pi_cod_tipo_des_ori
                          AND COD_SUBTIPO_DES = pi_cod_subtipo_des_ori
                          AND COD_MONEDA = pi_cod_moneda_ori
                          AND COD_PLANTILLA = PI_COD_PLANTILLA
                      )
      LOOP
        LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, '->Insertando en VALPRD_WK: codCliente['||c_valprd.COD_CLIENTE||'] pi_cod_tipo_des_des['||pi_cod_tipo_des_des||'] pi_cod_subtipo_des_des['||pi_cod_subtipo_des_des||'] pi_cod_moneda_des['||pi_cod_moneda_des||'] fecPeriodo['||c_valprd.FEC_PERIODO||'] codTipoConcep['||c_valprd.COD_TIPO_CONCEP||'] corConcepto['||c_valprd.COR_CONCEPTO||'] corValor['||c_valprd.COR_VALOR||'] valConcepto['||c_valprd.VAL_CONCEPTO||'] PI_COD_PLANTILLA['||PI_COD_PLANTILLA||']');
        DGL_SP_INS_VALPRD_WK (c_valprd.COD_CLIENTE, pi_cod_tipo_des_des, pi_cod_subtipo_des_des, pi_cod_moneda_des, c_valprd.FEC_PERIODO, c_valprd.COD_TIPO_CONCEP, c_valprd.COR_CONCEPTO, c_valprd.COR_VALOR, c_valprd.VAL_CONCEPTO, c_valprd.GLS_FRML_CONCEP, c_valprd.GLS_CONCEPTO, c_valprd.VAL_PRC_CONCEP, v_cod, v_gls, PI_COD_PLANTILLA);
        v_count_casos:= v_count_casos + 1;
      END LOOP;
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';      
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
      po_retorno:= -1;
      po_glosa:= v_gls_log;
      RETURN;
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log);
    --  Clonar tabla CONSOL_WK
    BEGIN
      v_count_casos:= 0;
      v_gls_log := 'CIF_PKG_DESGLOSE.CIF_SP_CLONAR_DESGLOSE -- Clonando tabla CONSOL_WK';
      FOR c_consol IN (SELECT COD_CLIENTE,
                              COR_EMPRESA,      
                              NOM_EMPRESA
                         FROM CONSOL_WK    
                        WHERE COD_CLIENTE = pi_num_iden
                          AND COD_TIPO_DES = pi_cod_tipo_des_ori
                          AND COD_SUBTIPO_DES = pi_cod_subtipo_des_ori
                          AND COD_MONEDA = pi_cod_moneda_ori
                          AND COD_PLANTILLA = PI_COD_PLANTILLA
                      )
      LOOP
        LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, '->Insertando en CONSOL_WK: codCliente['||c_consol.COD_CLIENTE||'] pi_cod_tipo_des_des['||pi_cod_tipo_des_des||'] pi_cod_subtipo_des_des['||pi_cod_subtipo_des_des||'] pi_cod_moneda_des['||pi_cod_moneda_des||'] corEmpresa['||c_consol.COR_EMPRESA||'] nomEmpresa['||c_consol.NOM_EMPRESA||'] PI_COD_PLANTILLA['||PI_COD_PLANTILLA||']');
        DGL_SP_INS_CONSOL_WK (c_consol.COD_CLIENTE, pi_cod_tipo_des_des, pi_cod_subtipo_des_des, pi_cod_moneda_des, c_consol.COR_EMPRESA, c_consol.NOM_EMPRESA, v_cod, v_gls, PI_COD_PLANTILLA);
        v_count_casos:= v_count_casos + 1;
      END LOOP;       
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
      po_retorno:= -1;
      po_glosa:= v_gls_log;
      RETURN;
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log);
    --  Clonar tabla PRCCNS
    BEGIN
      v_gls_log := 'DGL_PKG_DESGLOSE.DGL_SP_CLONAR_DESGLOSE -- Clonando tabla PRCCNS';
      FOR c_prccns IN (SELECT COD_CLIENTE,
                              COR_EMPRESA,      
                              FEC_PERIODO,
                              VAL_PRC_CONSOL
                         FROM PRCCNS_WK    
                        WHERE COD_CLIENTE = pi_num_iden
                          AND COD_TIPO_DES = pi_cod_tipo_des_ori
                          AND COD_SUBTIPO_DES = pi_cod_subtipo_des_ori
                          AND COD_MONEDA = pi_cod_moneda_ori
                          AND COD_PLANTILLA = PI_COD_PLANTILLA
                      )
      LOOP
        LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, '->Insertando en PRCCNS: codCliente['||c_prccns.COD_CLIENTE||'] pi_cod_tipo_des_des['||pi_cod_tipo_des_des||'] pi_cod_subtipo_des_des['||pi_cod_subtipo_des_des||'] pi_cod_moneda_des['||pi_cod_moneda_des||'] corEmpresa['||c_prccns.COR_EMPRESA||'] fecPeriodo['||c_prccns.FEC_PERIODO||'] valPrcConsol['||c_prccns.VAL_PRC_CONSOL||'] PI_COD_PLANTILLA['||PI_COD_PLANTILLA||']');
        DGL_SP_INS_PRCCNS_WK (c_prccns.COD_CLIENTE, pi_cod_tipo_des_des, pi_cod_subtipo_des_des, pi_cod_moneda_des, c_prccns.COR_EMPRESA, c_prccns.FEC_PERIODO, c_prccns.VAL_PRC_CONSOL, v_cod, v_gls, PI_COD_PLANTILLA);
        v_count_casos:= v_count_casos + 1;
      END LOOP;       
      v_count_total := v_count_total + v_count_casos;
      v_gls_log := v_gls_log || ' - rowcount['||v_count_casos||']';      
    EXCEPTION 
      WHEN OTHERS THEN
      v_gls_log := v_gls_log || ' Error ['||(SQLCODE || '-' || SQLERRM)||']';
      po_retorno:= -1;
      po_glosa:= v_gls_log;
      RETURN;
    END;
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log);
    --
    v_gls_log := 'DGL_PKG_DESGLOSE.DGL_SP_CLONAR_DESGLOSE -- Termino proceso de Clonacion de Desglose.';
    LOG(C_COD_TIPO_PROCESO, pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, v_gls_log);
  END DGL_SP_CLONAR_DESGLOSE_WK; 
END DGL_PKG_DESGLOSE_WK;