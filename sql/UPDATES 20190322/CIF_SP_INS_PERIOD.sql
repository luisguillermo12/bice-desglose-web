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
Fecha       : 22/03/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se actualiza la firma, incorporando el parámetro "COD_PLANTILLA"
***********************************************************************************/ (
    P_COD_CLIENTE           IN PERIOD.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES          IN PERIOD.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES       IN PERIOD.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA            IN PERIOD.COD_MONEDA%TYPE,
    P_FEC_PERIODO           IN PERIOD.FEC_PERIODO%TYPE,
    P_NUM_MESES             IN PERIOD.NUM_MESES%TYPE,
    P_VAL_DEFLAC_PRD        IN PERIOD.VAL_DEFLAC_PRD%TYPE,
    P_COD_AUDITOR           IN PERIOD.COD_AUDITOR%TYPE,
    P_GLS_OTROS_AUDIT       IN PERIOD.GLS_OTROS_AUDIT%TYPE,
    P_COD_ESTADO_PRD        IN PERIOD.COD_ESTADO_PRD%TYPE,
    P_COD_ESTADO_AUDI       IN PERIOD.COD_ESTADO_AUDI%TYPE,
    P_FEC_INFORM_SBIF       IN PERIOD.FEC_INFORM_SBIF%TYPE,
    P_MTO_SBIF_DD_VIG       IN PERIOD.MTO_SBIF_DD_VIG%TYPE,
    P_MTO_SBIF_DD_MOR       IN PERIOD.MTO_SBIF_DD_MOR%TYPE,
    P_MTO_SBIF_DD_VEN       IN PERIOD.MTO_SBIF_DD_VEN%TYPE,
    P_MTO_SBIF_DD_CAS       IN PERIOD.MTO_SBIF_DD_CAS%TYPE,
    P_MTO_SBIF_DI_VIG       IN PERIOD.MTO_SBIF_DI_VIG%TYPE,
    P_MTO_SBIF_DI_VEN       IN PERIOD.MTO_SBIF_DI_VEN%TYPE,
    P_MTO_SBIF_DI_CAS       IN PERIOD.MTO_SBIF_DI_CAS%TYPE,
    P_MTO_SBIF_IMP_90_179   IN PERIOD.MTO_SBIF_IMP_90_179%TYPE,
    P_NOM_USER              IN PERIOD.NOM_USER_CREA%TYPE,
    P_VAL_TIPO_CAMBIO       IN PERIOD.VAL_TIPO_CAMBIO%TYPE,
    P_RETORNO               OUT NUMBER,
    P_GLOSA                 OUT VARCHAR2,
    P_COD_PLANTILLA         IN PERIOD.COD_PLANTILLA%TYPE DEFAULT 0
) IS
   -- Variables Generales

    EXCP_NO_EXISTE_CODCLI EXCEPTION;
    EXCP_NO_EXISTE_TIPDES EXCEPTION;
    EXCP_NO_EXISTE_STPDES EXCEPTION;
    EXCP_NO_EXISTE_MONEDA EXCEPTION;
    EXCP_NO_EXISTE_FECPRD EXCEPTION;
    EXCP_NO_EXISTE_NUMMES EXCEPTION;
    EXCP_NO_EXISTE_DEFPRD EXCEPTION;
    EXCP_NO_EXISTE_GLSAUD EXCEPTION;
    EXCP_NO_EXISTE_USER EXCEPTION;
    EXCP_NO_EXISTE_PLANTILLA EXCEPTION;
    V_CONTADOR          NUMBER(9) :=-1;
    V_FEC_TODAY         PERIOD.FEC_USER_CREA%TYPE;
    V_COD_ESTADO_PRD    PERIOD.COD_ESTADO_PRD%TYPE;
    V_COD_ESTADO_AUDI   PERIOD.COD_ESTADO_AUDI%TYPE;
    V_VAL_TIPO_CAMBIO   PERIOD.VAL_TIPO_CAMBIO%TYPE;
BEGIN
    P_RETORNO := 0;
    P_GLOSA := '';
    V_FEC_TODAY := SYSDATE;
    V_COD_ESTADO_PRD := P_COD_ESTADO_PRD;
    V_COD_ESTADO_AUDI := P_COD_ESTADO_AUDI;
    V_VAL_TIPO_CAMBIO := P_VAL_TIPO_CAMBIO;

   /*** VALIDA DATOS NOT NULL***/
    IF LTRIM(RTRIM(P_COD_CLIENTE) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODCLI;
    END IF;
    IF P_COD_PLANTILLA IS NULL THEN
        RAISE EXCP_NO_EXISTE_PLANTILLA;
    END IF;
    IF P_COD_TIPO_DES IS NULL THEN
        RAISE EXCP_NO_EXISTE_TIPDES;
    END IF;
    IF P_COD_SUBTIPO_DES IS NULL THEN
        RAISE EXCP_NO_EXISTE_STPDES;
    END IF;
    IF LTRIM(RTRIM(P_COD_MONEDA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_MONEDA;
    END IF;
    IF P_FEC_PERIODO IS NULL THEN
        RAISE EXCP_NO_EXISTE_FECPRD;
    END IF;
    IF P_NUM_MESES IS NULL THEN
        RAISE EXCP_NO_EXISTE_NUMMES;
    END IF;
    IF P_VAL_DEFLAC_PRD IS NULL THEN
        RAISE EXCP_NO_EXISTE_DEFPRD;
    END IF;
    IF LTRIM(RTRIM(P_GLS_OTROS_AUDIT) ) IS NULL AND LTRIM(RTRIM(P_COD_AUDITOR) ) = '9999' THEN
        RAISE EXCP_NO_EXISTE_GLSAUD;
    END IF;

    IF P_COD_ESTADO_PRD IS NULL THEN
        V_COD_ESTADO_PRD := 0;
    END IF;
    IF P_COD_ESTADO_AUDI IS NULL THEN
        V_COD_ESTADO_AUDI := 0;
    END IF;
    IF LTRIM(RTRIM(P_NOM_USER) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_USER;
    END IF;
    IF P_VAL_TIPO_CAMBIO IS NULL THEN
        V_VAL_TIPO_CAMBIO := 1;
    END IF;


   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
    SELECT
        NVL(COUNT(1),0)
    INTO V_CONTADOR
    FROM
        PERIOD
    WHERE
        COD_CLIENTE = P_COD_CLIENTE
        AND COD_TIPO_DES = P_COD_TIPO_DES
        AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
        AND COD_MONEDA = P_COD_MONEDA
        AND FEC_PERIODO = P_FEC_PERIODO
        AND COD_PLANTILLA = P_COD_PLANTILLA;

   /** SI EXISTE LO ACTUALIZA **/

    IF V_CONTADOR = 1 THEN
        UPDATE PERIOD
        SET
            NUM_MESES = P_NUM_MESES,
            VAL_DEFLAC_PRD = P_VAL_DEFLAC_PRD,
            COD_AUDITOR = P_COD_AUDITOR,
            GLS_OTROS_AUDIT = P_GLS_OTROS_AUDIT,
            COD_ESTADO_PRD = V_COD_ESTADO_PRD,
            COD_ESTADO_AUDI = V_COD_ESTADO_AUDI,
            FEC_INFORM_SBIF = P_FEC_INFORM_SBIF,
            MTO_SBIF_DD_VIG = P_MTO_SBIF_DD_VIG,
            MTO_SBIF_DD_MOR = P_MTO_SBIF_DD_MOR,
            MTO_SBIF_DD_VEN = P_MTO_SBIF_DD_VEN,
            MTO_SBIF_DD_CAS = P_MTO_SBIF_DD_CAS,
            MTO_SBIF_DI_VIG = P_MTO_SBIF_DI_VIG,
            MTO_SBIF_DI_VEN = P_MTO_SBIF_DI_VEN,
            MTO_SBIF_DI_CAS = P_MTO_SBIF_DI_CAS,
            MTO_SBIF_IMP_90_179 = P_MTO_SBIF_IMP_90_179,
            NOM_USER_MODIF = P_NOM_USER,
            FEC_USER_MODIF = V_FEC_TODAY,
            VAL_TIPO_CAMBIO = V_VAL_TIPO_CAMBIO
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
   /** SINO, LO CREA **/
        INSERT INTO PERIOD (
            COD_CLIENTE,
            COD_TIPO_DES,
            COD_SUBTIPO_DES,
            COD_MONEDA,
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
            VAL_TIPO_CAMBIO,
            COD_PLANTILLA
        ) VALUES (
            P_COD_CLIENTE,
            P_COD_TIPO_DES,
            P_COD_SUBTIPO_DES,
            P_COD_MONEDA,
            P_FEC_PERIODO,
            P_NUM_MESES,
            P_VAL_DEFLAC_PRD,
            P_COD_AUDITOR,
            P_GLS_OTROS_AUDIT,
            V_COD_ESTADO_PRD,
            V_COD_ESTADO_AUDI,
            P_FEC_INFORM_SBIF,
            P_MTO_SBIF_DD_VIG,
            P_MTO_SBIF_DD_MOR,
            P_MTO_SBIF_DD_VEN,
            P_MTO_SBIF_DD_CAS,
            P_MTO_SBIF_DI_VIG,
            P_MTO_SBIF_DI_VEN,
            P_MTO_SBIF_DI_CAS,
            P_MTO_SBIF_IMP_90_179,
            P_NOM_USER,
            V_FEC_TODAY,
            V_VAL_TIPO_CAMBIO,
            P_COD_PLANTILLA
        );

    END IF;

EXCEPTION
    WHEN EXCP_NO_EXISTE_CODCLI THEN
        P_RETORNO :=-1;
        P_GLOSA := 'El cliente es un parametro obligatorio';
    WHEN EXCP_NO_EXISTE_TIPDES THEN
        P_RETORNO :=-2;
        P_GLOSA := 'Debe especificar el Tipo de Desglose';
    WHEN EXCP_NO_EXISTE_STPDES THEN
        P_RETORNO :=-3;
        P_GLOSA := 'Debe especificar el SubTipo de Desglose';
    WHEN EXCP_NO_EXISTE_MONEDA THEN
        P_RETORNO :=-4;
        P_GLOSA := 'Debe especificar la Moneda del Desglose';
    WHEN EXCP_NO_EXISTE_FECPRD THEN
        P_RETORNO :=-5;
        P_GLOSA := 'Se requiere una fecha para identificar el Periodo';
    WHEN EXCP_NO_EXISTE_NUMMES THEN
        P_RETORNO :=-6;
        P_GLOSA := 'El numero de meses del Periodo es requerido';
    WHEN EXCP_NO_EXISTE_DEFPRD THEN
        P_RETORNO :=-7;
        P_GLOSA := 'El Deflactor del periodo es un parametro requerido';
    WHEN EXCP_NO_EXISTE_GLSAUD THEN
        P_RETORNO :=-8;
        P_GLOSA := 'Si el Auditor no se encuentra en la lista, entonces debe escribir el nombre del mismo';
    WHEN EXCP_NO_EXISTE_USER THEN
        P_RETORNO :=-9;
        P_GLOSA := 'Se debe identificar el usuario conectado';
    WHEN EXCP_NO_EXISTE_PLANTILLA THEN
        P_RETORNO :=-10;
        P_GLOSA := 'Debe especificar el Codigo de Plantilla';
    WHEN OTHERS THEN
        P_RETORNO :=-99;
        P_GLOSA := SQLERRM;
END CIF_SP_INS_PERIOD;

/
