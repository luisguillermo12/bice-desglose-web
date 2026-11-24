--------------------------------------------------------
--  File created - Friday-January-25-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure CIF_SP_INSUPD_DESGLO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "DGL_ADMIN"."CIF_SP_INSUPD_DESGLO" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INSUPD_DESGLO
Fecha             :05/12/2005
Autor             :
Descripcion       :Inserta o Actualiza un Desglose en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 21/12/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se agrega el parámetro de entrada p_COD_PLANTILLA
***********************************************************************************/
(
   p_cod_cliente            IN DESGLO.COD_CLIENTE%TYPE,
   p_cod_tipo_des           IN DESGLO.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des        IN DESGLO.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda             IN DESGLO.COD_MONEDA%TYPE,
   p_cod_unid_moneda        IN DESGLO.COD_UNID_MONEDA%TYPE,
   p_gls_subtipo_des        IN DESGLO.GLS_SUBTIPO_DES%TYPE,
   p_val_deflac_base        IN DESGLO.VAL_DEFLAC_BASE%TYPE,
   p_fec_deflac_base        IN DESGLO.FEC_DEFLAC_BASE%TYPE,
   p_val_iva                IN DESGLO.VAL_IVA%TYPE,
   p_gls_observacion        IN DESGLO.GLS_OBSERVACION%TYPE,
   p_nom_user               IN DESGLO.NOM_USER_CREA%TYPE,
   p_cod_moneda_imp         IN DESGLO.COD_MONEDA_IMP%TYPE,
   p_user                   IN DESGLO.NOM_USER%TYPE,
   p_flg_validacion         IN DESGLO.FLG_VALIDACION%TYPE,
   p_cod_estado             IN DESGLO.COD_ESTADO%TYPE,
   p_rol_empleado_rev_mod   IN DESGLO.ROL_EMPLEADO_REV_MOD%TYPE,
   p_rol_empleado           IN DESGLO.NUM_ROL_CHG_ESTADO%TYPE,
   p_retorno                OUT NUMBER,
   p_glosa                  OUT VARCHAR2,
   p_COD_PLANTILLA     IN DESGLO.COD_PLANTILLA%TYPE DEFAULT 0
   )
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI        EXCEPTION;
   EXCP_NO_EXISTE_TIPDES        EXCEPTION;
   EXCP_NO_EXISTE_STPDES        EXCEPTION;
   EXCP_NO_EXISTE_CODMON        EXCEPTION;
   EXCP_NO_EXISTE_UNIMON        EXCEPTION;
   EXCP_NO_EXISTE_GLSSTP        EXCEPTION;
   EXCP_NO_EXISTE_DEFBAS        EXCEPTION;
   EXCP_NO_EXISTE_VALIVA        EXCEPTION;
   EXCP_NO_EXISTE_USER          EXCEPTION;
   EXCP_NO_EXISTE_ROL           EXCEPTION;
   EXCP_NO_AUTHORIZED_USER      EXCEPTION;
   EXCP_NO_EXISTE_MONIMP        EXCEPTION;
   EXCP_NO_EXISTE_FLG_VAL       EXCEPTION;
   EXCP_NO_EXISTE_ESTADO        EXCEPTION;
   EXCP_NO_EXISTE_CODTIPOPLANT  EXCEPTION;
   v_contador                   NUMBER(9):=-1;
   v_fec_today                  DESGLO.FEC_USER_CREA%TYPE;
   v_nom_user_open_db           DESGLO.NOM_USER_OPEN%TYPE;
BEGIN

  INSERT INTO DGL_CHK_LOG (COD_TIPO_PROCESO, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_LOG, GLS_LOG, COD_PLANTILLA)
                   VALUES ('INSUPDDESG', p_cod_cliente, p_cod_tipo_des, p_cod_subtipo_des, p_cod_moneda, SYSTIMESTAMP, 'Se ejectua CIF_SP_INSUPD_DESGLO por '||p_nom_user|| ' Rol '||p_rol_empleado, p_COD_PLANTILLA);
  COMMIT; 
  
  p_retorno := 0;
  p_glosa := '';
  v_fec_today := SYSDATE;
  v_nom_user_open_db := '';
  
  /*** VALIDA DATOS NOT NULL***/
  IF LTRIM(RTRIM(p_cod_cliente)) IS NULL THEN
     RAISE EXCP_NO_EXISTE_CODCLI;
  END IF;
  IF p_cod_tipo_des IS NULL THEN
     RAISE EXCP_NO_EXISTE_TIPDES;
  END IF;
  IF p_cod_subtipo_des IS NULL THEN
     RAISE EXCP_NO_EXISTE_STPDES;
  END IF;
  IF LTRIM(RTRIM(p_cod_moneda)) IS NULL THEN
     RAISE EXCP_NO_EXISTE_CODMON;
  END IF;
  IF LTRIM(RTRIM(p_cod_unid_moneda)) IS NULL THEN
     RAISE EXCP_NO_EXISTE_UNIMON;
  END IF;
  IF LTRIM(RTRIM(p_gls_subtipo_des)) IS NULL AND p_cod_subtipo_des IN (6,7) THEN
     RAISE EXCP_NO_EXISTE_GLSSTP;
  END IF;
  IF p_val_deflac_base IS NULL OR p_fec_deflac_base IS NULL THEN
     RAISE EXCP_NO_EXISTE_DEFBAS;
  END IF;
  IF p_val_iva IS NULL THEN
     RAISE EXCP_NO_EXISTE_VALIVA;
  END IF;
  IF LTRIM(RTRIM(p_nom_user)) IS NULL THEN
     RAISE EXCP_NO_EXISTE_USER;
  END IF;
  IF LTRIM(RTRIM(p_cod_moneda_imp)) IS NULL THEN
     RAISE EXCP_NO_EXISTE_MONIMP;
  END IF;
  IF LTRIM(RTRIM(p_rol_empleado)) IS NULL THEN
     RAISE EXCP_NO_EXISTE_ROL;
  END IF;
  IF LTRIM(RTRIM(p_COD_PLANTILLA)) IS NULL THEN
     RAISE EXCP_NO_EXISTE_CODTIPOPLANT;
  END IF;
  
  /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
  SELECT NVL(COUNT(1),0) INTO v_contador
  FROM   DESGLO
  WHERE     COD_PLANTILLA  = p_COD_PLANTILLA
      AND   COD_CLIENTE             = p_cod_cliente
      AND   COD_TIPO_DES            = p_cod_tipo_des
      AND   COD_SUBTIPO_DES         = p_cod_subtipo_des
      AND   COD_MONEDA              = p_cod_moneda;
  
  IF v_contador > 0 THEN
     SELECT NVL(NOM_USER_OPEN,'') INTO v_nom_user_open_db
     FROM   DESGLO
     WHERE      COD_PLANTILLA  = p_COD_PLANTILLA
         AND    COD_CLIENTE             = p_cod_cliente
         AND    COD_TIPO_DES            = p_cod_tipo_des
         AND    COD_SUBTIPO_DES         = p_cod_subtipo_des
         AND    COD_MONEDA              = p_cod_moneda;
  END IF;

  IF v_nom_user_open_db <> '' AND TRIM(UPPER(v_nom_user_open_db)) <> TRIM(UPPER(p_nom_user)) THEN
    RAISE EXCP_NO_AUTHORIZED_USER;
  END IF;
   
  /** SI EXISTE LO ACTUALIZA **/
  IF v_contador = 1 THEN
     UPDATE DESGLO
     SET    COD_UNID_MONEDA      = p_cod_unid_moneda,
            GLS_SUBTIPO_DES      = p_gls_subtipo_des,
            VAL_DEFLAC_BASE      = p_val_deflac_base,
            FEC_DEFLAC_BASE      = p_fec_deflac_base,
            VAL_IVA              = p_val_iva,
            NOM_USER_ANALIS      = p_nom_user,
            GLS_OBSERVACION      = p_gls_observacion,
            NOM_USER_MODIF       = p_nom_user,
            FEC_USER_MODIF       = v_fec_today,
            NOM_USER_OPEN        = p_nom_user,
            FLG_IS_TEMPORAL      = 0,
            COD_MONEDA_IMP       = p_cod_moneda_imp,
            NOM_USER             = p_user,
            FLG_VALIDACION       = p_flg_validacion,
            COD_ESTADO           = p_cod_estado,
            ROL_EMPLEADO_REV_MOD = p_rol_empleado_rev_mod
     WHERE  COD_PLANTILLA  = p_COD_PLANTILLA
     AND    COD_CLIENTE             = p_cod_cliente
     AND    COD_TIPO_DES            = p_cod_tipo_des
     AND    COD_SUBTIPO_DES         = p_cod_subtipo_des
     AND    COD_MONEDA              = p_cod_moneda;
  ELSE
  /** SINO, LO CREA **/
     INSERT INTO DESGLO (
            COD_CLIENTE
           ,COD_PLANTILLA
           ,COD_TIPO_DES
           ,COD_SUBTIPO_DES
           ,COD_MONEDA
           ,COD_UNID_MONEDA
           ,GLS_SUBTIPO_DES
           ,VAL_DEFLAC_BASE
           ,FEC_DEFLAC_BASE
           ,VAL_IVA
           ,NOM_USER_ANALIS
           ,GLS_OBSERVACION
           ,NOM_USER_CREA
           ,FEC_USER_CREA
           ,NOM_USER_OPEN
           ,FLG_IS_TEMPORAL
           ,COD_MONEDA_IMP
           ,NOM_USER
           ,FLG_VALIDACION
           ,COD_ESTADO
           ,ROL_EMPLEADO_REV_MOD)
     VALUES (
            p_cod_cliente
           ,p_COD_PLANTILLA
           ,p_cod_tipo_des
           ,p_cod_subtipo_des
           ,p_cod_moneda
           ,p_cod_unid_moneda
           ,p_gls_subtipo_des
           ,p_val_deflac_base
           ,p_fec_deflac_base
           ,p_val_iva
           ,p_nom_user
           ,p_gls_observacion
           ,p_nom_user
           ,v_fec_today
           ,p_nom_user
           ,0
           ,p_cod_moneda_imp
           ,p_user
           ,p_flg_validacion
           ,p_cod_estado
           ,p_rol_empleado_rev_mod);
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
     WHEN EXCP_NO_EXISTE_UNIMON THEN
        p_retorno := -5;
        p_glosa := 'La Unidad Monetaria es un parametro obligatorio';
     WHEN EXCP_NO_EXISTE_GLSSTP THEN
        p_retorno := -6;
        p_glosa := 'Si el SubTipo es "Otros", entonces la descripcion del subtipo es Obligatoria';
     WHEN EXCP_NO_EXISTE_DEFBAS THEN
        p_retorno := -7;
        p_glosa := 'El Deflactor Base es Obligatorio';
     WHEN EXCP_NO_EXISTE_VALIVA THEN
        p_retorno := -8;
        p_glosa := 'El IVA es un parametro obligatorio';
     WHEN EXCP_NO_EXISTE_USER THEN
        p_retorno := -9;
        p_glosa := 'Se debe identificar el usuario conectado';
     WHEN EXCP_NO_AUTHORIZED_USER THEN
        p_retorno := -10;
        p_glosa := 'El desglose se encuentra en uso por otro usuario. No está autorizado para grabar sobre él.';
     WHEN EXCP_NO_EXISTE_MONIMP THEN
        p_retorno := -11;
        p_glosa := 'La Moneda de Impresion es un parametro obligatorio';
     WHEN EXCP_NO_EXISTE_FLG_VAL THEN
        p_retorno := -12;
        p_glosa := 'Se debe señalar si Desglose está validado o no. (OK, NOK)';
     WHEN EXCP_NO_EXISTE_ESTADO THEN
        p_retorno := -13;
        p_glosa := 'Se debe señalar el estado del Desglose. (BEP, BT, DEF)';
     WHEN EXCP_NO_EXISTE_ROL THEN
        p_retorno := -14;
        p_glosa := 'Se debe identificar Rol de Empleado el usuario conectado';
     WHEN EXCP_NO_EXISTE_CODTIPOPLANT THEN
        p_retorno := -15;
        p_glosa := 'Debe especificar el tipo de plantilla de desglose';
     WHEN OTHERS THEN
        p_retorno := -99;
        p_glosa := SQLERRM;
END CIF_SP_INSUPD_DESGLO;

/
