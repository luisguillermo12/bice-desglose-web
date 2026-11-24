
CREATE OR REPLACE PROCEDURE CIF_ADMIN.CIF_SP_TAKE_DESGLO_ROL
(
  p_cod_cliente       IN DESGLO.COD_CLIENTE%TYPE,
  p_cod_tipo_des      IN DESGLO.COD_TIPO_DES%TYPE,
  p_cod_subtipo_des   IN DESGLO.COD_SUBTIPO_DES%TYPE,
  p_cod_moneda        IN DESGLO.COD_MONEDA%TYPE,
  p_nom_user          IN DESGLO.NOM_USER_OPEN%TYPE,
  p_num_rol           IN DESGLO.NUM_ROL_CHG_ESTADO%TYPE,
  p_retorno           OUT NUMBER,
  p_glosa             OUT VARCHAR2
)
IS
  -- Variables Generales
  EXCP_NO_EXISTE_CODCLI    EXCEPTION;
  EXCP_NO_EXISTE_TIPDES    EXCEPTION;
  EXCP_NO_EXISTE_STPDES    EXCEPTION;
  EXCP_NO_EXISTE_CODMON    EXCEPTION;
  EXCP_NO_EXISTE_USER      EXCEPTION;
  EXCP_NO_EXISTE_ROL       EXCEPTION;
  EXCP_NO_AUTHORIZED_USER EXCEPTION;
  v_contador               NUMBER(9):=-1;
  v_fec_today              DESGLO.FEC_USER_CREA%TYPE;
  v_nom_user_open_db       DESGLO.NOM_USER_OPEN%TYPE;
  v_gls_subtipo_des        DESGLO.GLS_SUBTIPO_DES%TYPE;

BEGIN

  INSERT INTO DGL_CHK_LOG (COD_TIPO_PROCESO, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_LOG, GLS_LOG)
                   VALUES ('TAKE_DESGL', p_cod_cliente, p_cod_tipo_des, p_cod_subtipo_des, p_cod_moneda, SYSTIMESTAMP, 'Se ejectua CIF_SP_TAKE_DESGLO por '||p_nom_user|| ' Rol '||p_num_rol);
  COMMIT; 
  
  p_retorno := 0;
  p_glosa := '';
  v_fec_today := SYSDATE;
  v_nom_user_open_db := '';
  v_gls_subtipo_des := '';
  
  -- VALIDA DATOS NOT NULL
  IF LTRIM(RTRIM(p_cod_cliente)) IS NULL THEN
    RAISE EXCP_NO_EXISTE_CODCLI;
  END IF;
  IF p_cod_tipo_des IS NULL THEN
    RAISE EXCP_NO_EXISTE_TIPDES;
  END IF;
  IF p_cod_subtipo_des IS NULL THEN
    RAISE EXCP_NO_EXISTE_STPDES;
  END IF;
  IF p_cod_subtipo_des = 6 OR p_cod_subtipo_des = 7 THEN
    v_gls_subtipo_des := 'DES_TEMPO';
  END IF;
  IF LTRIM(RTRIM(p_cod_moneda)) IS NULL THEN
    RAISE EXCP_NO_EXISTE_CODMON;
  END IF;
  IF LTRIM(RTRIM(p_nom_user)) IS NULL THEN
    RAISE EXCP_NO_EXISTE_USER;
  END IF;
--  IF LTRIM(RTRIM(p_num_rol)) IS NULL THEN
--    RAISE EXCP_NO_EXISTE_ROL;
--  END IF;
  
  --SELECCIONA REGISTRO PARA VALIDAR SI EXISTE
  SELECT NVL(COUNT(1),0) 
     INTO v_contador
     FROM DESGLO
    WHERE COD_CLIENTE     = p_cod_cliente
      AND COD_TIPO_DES    = p_cod_tipo_des
      AND COD_SUBTIPO_DES = p_cod_subtipo_des
      AND COD_MONEDA      = p_cod_moneda;
  
  IF v_contador > 0 THEN
    SELECT NVL(NOM_USER_OPEN,'') INTO v_nom_user_open_db
      FROM DESGLO
     WHERE COD_CLIENTE     = p_cod_cliente
       AND COD_TIPO_DES    = p_cod_tipo_des
       AND COD_SUBTIPO_DES = p_cod_subtipo_des
       AND COD_MONEDA      = p_cod_moneda;
  END IF;

  IF v_nom_user_open_db <> '' AND TRIM(UPPER(v_nom_user_open_db)) <> TRIM(UPPER(p_nom_user)) THEN
    RAISE EXCP_NO_AUTHORIZED_USER;
  END IF;
  -- 
  -- -- SI EXISTE LO ACTUALIZA **/
  IF v_contador = 1 THEN
    UPDATE DESGLO
       SET NOM_USER_OPEN   = p_nom_user,
           FEC_HRA_OPEN    = v_fec_today
     WHERE COD_CLIENTE     = p_cod_cliente
       AND COD_TIPO_DES    = p_cod_tipo_des
       AND COD_SUBTIPO_DES = p_cod_subtipo_des
       AND COD_MONEDA      = p_cod_moneda;
  ELSE
    INSERT INTO DESGLO (COD_CLIENTE,COD_TIPO_DES,COD_SUBTIPO_DES,COD_MONEDA,COD_UNID_MONEDA,GLS_SUBTIPO_DES,VAL_DEFLAC_BASE,FEC_DEFLAC_BASE,VAL_IVA,NOM_USER_ANALIS,GLS_OBSERVACION,NOM_USER_CREA,FEC_USER_CREA,NOM_USER_OPEN,FEC_HRA_OPEN,FLG_IS_TEMPORAL,COD_MONEDA_IMP,NUM_ROL_CHG_ESTADO)
                VALUES (p_cod_cliente,p_cod_tipo_des,p_cod_subtipo_des,p_cod_moneda,1,v_gls_subtipo_des,1,v_fec_today,0.19,p_nom_user,'',p_nom_user,v_fec_today,p_nom_user,v_fec_today,1,p_cod_moneda,p_num_rol);
  END IF;

EXCEPTION
  WHEN EXCP_NO_EXISTE_CODCLI THEN
    p_retorno := -1;
    p_glosa := 'El cliente es un parametro obligatorio';
  WHEN EXCP_NO_EXISTE_TIPDES THEN
    p_retorno := -2;
    p_glosa := 'Debe especificar el Tipo de Desglose';
  WHEN EXCP_NO_EXISTE_STPDES THEN
    p_retorno := -3;
    p_glosa := 'Debe especificar el SubTipo de Desglose';
  WHEN EXCP_NO_EXISTE_CODMON THEN
    p_retorno := -4;
    p_glosa := 'La Moneda es un parametro obligatorio';
  WHEN EXCP_NO_EXISTE_USER THEN
    p_retorno := -9;
    p_glosa := 'Se debe identificar el usuario conectado';
  WHEN EXCP_NO_EXISTE_ROL THEN
    p_retorno := -11;
    p_glosa := 'Se debe identificar Rol de Empleado el usuario conectado';
  WHEN EXCP_NO_AUTHORIZED_USER THEN
    p_retorno := -10;
    p_glosa := 'El desglose se encuentra en uso por otro usuario. No está autorizado para grabar sobre él.';
  WHEN OTHERS THEN
    p_retorno := -99;
    p_glosa := SUBSTR(SQLERRM,1,255);
END CIF_SP_TAKE_DESGLO_ROL;
/

GRANT EXECUTE ON CIF_ADMIN.CIF_SP_TAKE_DESGLO_ROL TO R_CIF_ACT;
GRANT EXECUTE ON CIF_ADMIN.CIF_SP_TAKE_DESGLO_ROL TO R_CIF_CON;


SPOOL OFF;