--------------------------------------------------------
--  DDL for Procedure CIF_SP_INS_PERIOD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_INS_PERIOD" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_PERIOD
Fecha             :05/12/2005
Autor             :Rodrigo Gonzalez K.
Descripcion       :Inserta o Actualiza un Periodo en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
   p_cod_cliente       IN PERIOD.COD_CLIENTE%TYPE,
   p_cod_tipo_des      IN PERIOD.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN PERIOD.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN PERIOD.COD_MONEDA%TYPE,
   p_fec_periodo       IN PERIOD.FEC_PERIODO%TYPE,
   p_num_meses         IN PERIOD.NUM_MESES%TYPE,
   p_val_deflac_prd    IN PERIOD.VAL_DEFLAC_PRD%TYPE,
   p_cod_auditor       IN PERIOD.COD_AUDITOR%TYPE,
   p_gls_otros_audit   IN PERIOD.GLS_OTROS_AUDIT%TYPE,
   p_cod_estado_prd    IN PERIOD.COD_ESTADO_PRD%TYPE,
   p_cod_estado_audi   IN PERIOD.COD_ESTADO_AUDI%TYPE,
   p_fec_inform_sbif   IN PERIOD.FEC_INFORM_SBIF%TYPE,
   p_mto_sbif_dd_vig   IN PERIOD.MTO_SBIF_DD_VIG%TYPE,
   p_mto_sbif_dd_mor   IN PERIOD.MTO_SBIF_DD_MOR%TYPE,
   p_mto_sbif_dd_ven   IN PERIOD.MTO_SBIF_DD_VEN%TYPE,
   p_mto_sbif_dd_cas   IN PERIOD.MTO_SBIF_DD_CAS%TYPE,
   p_mto_sbif_di_vig   IN PERIOD.MTO_SBIF_DI_VIG%TYPE,
   p_mto_sbif_di_ven   IN PERIOD.MTO_SBIF_DI_VEN%TYPE,
   p_mto_sbif_di_cas   IN PERIOD.MTO_SBIF_DI_CAS%TYPE,
   p_mto_sbif_imp_90_179 IN PERIOD.MTO_SBIF_IMP_90_179%TYPE,
   p_nom_user          IN PERIOD.NOM_USER_CREA%TYPE,
   p_val_tipo_cambio   IN PERIOD.VAL_TIPO_CAMBIO%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_NO_EXISTE_FECPRD    EXCEPTION;
   EXCP_NO_EXISTE_NUMMES    EXCEPTION;
   EXCP_NO_EXISTE_DEFPRD    EXCEPTION;
   EXCP_NO_EXISTE_GLSAUD    EXCEPTION;
   EXCP_NO_EXISTE_USER      EXCEPTION;
   v_contador               NUMBER(9):=-1;
   v_fec_today              PERIOD.FEC_USER_CREA%TYPE;
   v_cod_estado_prd         PERIOD.COD_ESTADO_PRD%TYPE;
   v_cod_estado_audi        PERIOD.COD_ESTADO_AUDI%TYPE;
   v_val_tipo_cambio        PERIOD.VAL_TIPO_CAMBIO%TYPE;
BEGIN
   p_retorno := 0;
   p_glosa := '';
   v_fec_today := SYSDATE;
   v_cod_estado_prd  := p_cod_estado_prd;
   v_cod_estado_audi := p_cod_estado_audi;
   v_val_tipo_cambio := p_val_tipo_cambio;

   /*** VALIDA DATOS NOT NULL***/
   IF ltrim(rtrim(p_cod_cliente)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_CODCLI;
   END IF;
   IF p_cod_tipo_des IS NULL THEN
      RAISE EXCP_NO_EXISTE_TIPDES;
   END IF;
   IF p_cod_subtipo_des IS NULL THEN
      RAISE EXCP_NO_EXISTE_STPDES;
   END IF;
   IF ltrim(rtrim(p_cod_moneda)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_MONEDA;
   END IF;
   IF p_fec_periodo IS NULL THEN
      RAISE EXCP_NO_EXISTE_FECPRD;
   END IF;
   IF p_num_meses IS NULL THEN
      RAISE EXCP_NO_EXISTE_NUMMES;
   END IF;
   IF p_val_deflac_prd IS NULL THEN
      RAISE EXCP_NO_EXISTE_DEFPRD;
   END IF;
   IF ltrim(rtrim(p_gls_otros_audit)) IS NULL AND ltrim(rtrim(p_cod_auditor)) = '9999' THEN
      RAISE EXCP_NO_EXISTE_GLSAUD;
   END IF;
   IF p_cod_estado_prd IS NULL THEN
      v_cod_estado_prd := 0;
   END IF;
   IF p_cod_estado_audi IS NULL THEN
      v_cod_estado_audi := 0;
   END IF;
   IF ltrim(rtrim(p_nom_user)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_USER;
   END IF;
   IF p_val_tipo_cambio IS NULL THEN
      v_val_tipo_cambio := 1;
   END IF;


   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   PERIOD
   WHERE  COD_CLIENTE     = p_cod_cliente
   AND    COD_TIPO_DES    = p_cod_tipo_des
   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
   AND    COD_MONEDA      = p_cod_moneda
   AND    FEC_PERIODO     = p_fec_periodo;

   /** SI EXISTE LO ACTUALIZA **/
   IF v_contador = 1 THEN
      UPDATE PERIOD
      SET    NUM_MESES       = p_num_meses,
             VAL_DEFLAC_PRD  = p_val_deflac_prd,
             COD_AUDITOR     = p_cod_auditor,
             GLS_OTROS_AUDIT = p_gls_otros_audit,
             COD_ESTADO_PRD  = v_cod_estado_prd,
             COD_ESTADO_AUDI = v_cod_estado_audi,
             FEC_INFORM_SBIF = p_fec_inform_sbif,
             MTO_SBIF_DD_VIG = p_mto_sbif_dd_vig,
             MTO_SBIF_DD_MOR = p_mto_sbif_dd_mor,
             MTO_SBIF_DD_VEN = p_mto_sbif_dd_ven,
             MTO_SBIF_DD_CAS = p_mto_sbif_dd_cas,
             MTO_SBIF_DI_VIG = p_mto_sbif_di_vig,
             MTO_SBIF_DI_VEN = p_mto_sbif_di_ven,
             MTO_SBIF_DI_CAS = p_mto_sbif_di_cas,
             MTO_SBIF_IMP_90_179 = p_mto_sbif_imp_90_179,
             NOM_USER_MODIF  = p_nom_user,
             FEC_USER_MODIF  = v_fec_today,
             VAL_TIPO_CAMBIO = v_val_tipo_cambio
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo;
   ELSE
   /** SINO, LO CREA **/
      INSERT INTO PERIOD (
             COD_CLIENTE
            ,COD_TIPO_DES
            ,COD_SUBTIPO_DES
            ,COD_MONEDA
            ,FEC_PERIODO
            ,NUM_MESES
            ,VAL_DEFLAC_PRD
            ,COD_AUDITOR
            ,GLS_OTROS_AUDIT
            ,COD_ESTADO_PRD
            ,COD_ESTADO_AUDI
            ,FEC_INFORM_SBIF
            ,MTO_SBIF_DD_VIG
            ,MTO_SBIF_DD_MOR
            ,MTO_SBIF_DD_VEN
            ,MTO_SBIF_DD_CAS
            ,MTO_SBIF_DI_VIG
            ,MTO_SBIF_DI_VEN
            ,MTO_SBIF_DI_CAS
            ,MTO_SBIF_IMP_90_179
            ,NOM_USER_CREA
            ,FEC_USER_CREA
            ,VAL_TIPO_CAMBIO)
      VALUES (
             p_cod_cliente
            ,p_cod_tipo_des
            ,p_cod_subtipo_des
            ,p_cod_moneda
            ,p_fec_periodo
            ,p_num_meses
            ,p_val_deflac_prd
            ,p_cod_auditor
            ,p_gls_otros_audit
            ,v_cod_estado_prd
            ,v_cod_estado_audi
            ,p_fec_inform_sbif
            ,p_mto_sbif_dd_vig
            ,p_mto_sbif_dd_mor
            ,p_mto_sbif_dd_ven
            ,p_mto_sbif_dd_cas
            ,p_mto_sbif_di_vig
            ,p_mto_sbif_di_ven
            ,p_mto_sbif_di_cas
            ,p_mto_sbif_imp_90_179
            ,p_nom_user
            ,v_fec_today
            ,v_val_tipo_cambio);
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
      WHEN EXCP_NO_EXISTE_MONEDA THEN
         p_retorno := -4;
         p_glosa := 'Debe especificar la Moneda del Desglose';
      WHEN EXCP_NO_EXISTE_FECPRD THEN
         p_retorno := -5;
         p_glosa := 'Se requiere una fecha para identificar el Periodo';
      WHEN EXCP_NO_EXISTE_NUMMES THEN
         p_retorno := -6;
         p_glosa := 'El numero de meses del Periodo es requerido';
      WHEN EXCP_NO_EXISTE_DEFPRD THEN
         p_retorno := -7;
         p_glosa := 'El Deflactor del periodo es un parametro requerido';
      WHEN EXCP_NO_EXISTE_GLSAUD THEN
         p_retorno := -8;
         p_glosa := 'Si el Auditor no se encuentra en la lista, entonces debe escribir el nombre del mismo';
      WHEN EXCP_NO_EXISTE_USER THEN
         p_retorno := -9;
         p_glosa := 'Se debe identificar el usuario conectado';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_INS_PERIOD;

/
