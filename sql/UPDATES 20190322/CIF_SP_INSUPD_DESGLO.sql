--------------------------------------------------------
--  DDL for Procedure CIF_SP_INSUPD_DESGLO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_INSUPD_DESGLO" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INSUPD_DESGLO
Fecha             :05/12/2005
Autor             :
Descripcion       :Inserta o Actualiza un Desglose en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 22/03/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se actualiza la firma, incorporando el parámetro "COD_PLANTILLA"
***********************************************************************************/ (
    P_COD_CLIENTE            IN DESGLO.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES           IN DESGLO.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES        IN DESGLO.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA             IN DESGLO.COD_MONEDA%TYPE,
    P_COD_UNID_MONEDA        IN DESGLO.COD_UNID_MONEDA%TYPE,
    P_GLS_SUBTIPO_DES        IN DESGLO.GLS_SUBTIPO_DES%TYPE,
    P_VAL_DEFLAC_BASE        IN DESGLO.VAL_DEFLAC_BASE%TYPE,
    P_FEC_DEFLAC_BASE        IN DESGLO.FEC_DEFLAC_BASE%TYPE,
    P_VAL_IVA                IN DESGLO.VAL_IVA%TYPE,
    P_GLS_OBSERVACION        IN DESGLO.GLS_OBSERVACION%TYPE,
    P_NOM_USER               IN DESGLO.NOM_USER_CREA%TYPE,
    P_COD_MONEDA_IMP         IN DESGLO.COD_MONEDA_IMP%TYPE,
    P_USER                   IN DESGLO.NOM_USER%TYPE,
    P_FLG_VALIDACION         IN DESGLO.FLG_VALIDACION%TYPE,
    P_COD_ESTADO             IN DESGLO.COD_ESTADO%TYPE,
    P_ROL_EMPLEADO_REV_MOD   IN DESGLO.ROL_EMPLEADO_REV_MOD%TYPE,
    P_ROL_EMPLEADO           IN DESGLO.NUM_ROL_CHG_ESTADO%TYPE,
    P_RETORNO                OUT NUMBER,
    P_GLOSA                  OUT VARCHAR2,
    P_COD_PLANTILLA          IN DESGLO.COD_PLANTILLA%TYPE DEFAULT 0
) IS
   -- Variables Generales

    EXCP_NO_EXISTE_CODCLI EXCEPTION;
    EXCP_NO_EXISTE_TIPDES EXCEPTION;
    EXCP_NO_EXISTE_STPDES EXCEPTION;
    EXCP_NO_EXISTE_CODMON EXCEPTION;
    EXCP_NO_EXISTE_UNIMON EXCEPTION;
    EXCP_NO_EXISTE_GLSSTP EXCEPTION;
    EXCP_NO_EXISTE_DEFBAS EXCEPTION;
    EXCP_NO_EXISTE_VALIVA EXCEPTION;
    EXCP_NO_EXISTE_USER EXCEPTION;
    EXCP_NO_EXISTE_ROL EXCEPTION;
    EXCP_NO_AUTHORIZED_USER EXCEPTION;
    EXCP_NO_EXISTE_MONIMP EXCEPTION;
    EXCP_NO_EXISTE_FLG_VAL EXCEPTION;
    EXCP_NO_EXISTE_ESTADO EXCEPTION;
    EXCP_NO_EXISTE_CODTIPOPLANT EXCEPTION;
    V_CONTADOR           NUMBER(9) :=-1;
    V_FEC_TODAY          DESGLO.FEC_USER_CREA%TYPE;
    V_NOM_USER_OPEN_DB   DESGLO.NOM_USER_OPEN%TYPE;
BEGIN
    INSERT INTO DGL_CHK_LOG (
        COD_TIPO_PROCESO,
        COD_CLIENTE,
        COD_TIPO_DES,
        COD_SUBTIPO_DES,
        COD_MONEDA,
        FEC_LOG,
        GLS_LOG,
        COD_PLANTILLA
    ) VALUES (
        'INSUPDDESG',
        P_COD_CLIENTE,
        P_COD_TIPO_DES,
        P_COD_SUBTIPO_DES,
        P_COD_MONEDA,
        SYSTIMESTAMP,
        'Se ejectua CIF_SP_INSUPD_DESGLO por '
        || P_NOM_USER
        || ' Rol '
        || P_ROL_EMPLEADO,
        P_COD_PLANTILLA
    );

    COMMIT;
    P_RETORNO := 0;
    P_GLOSA := '';
    V_FEC_TODAY := SYSDATE;
    V_NOM_USER_OPEN_DB := '';
  
  /*** VALIDA DATOS NOT NULL***/
    IF LTRIM(RTRIM(P_COD_CLIENTE) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODCLI;
    END IF;
    IF P_COD_TIPO_DES IS NULL THEN
        RAISE EXCP_NO_EXISTE_TIPDES;
    END IF;
    IF P_COD_SUBTIPO_DES IS NULL THEN
        RAISE EXCP_NO_EXISTE_STPDES;
    END IF;
    IF LTRIM(RTRIM(P_COD_MONEDA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODMON;
    END IF;
    IF LTRIM(RTRIM(P_COD_UNID_MONEDA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_UNIMON;
    END IF;
    IF LTRIM(RTRIM(P_GLS_SUBTIPO_DES) ) IS NULL AND P_COD_SUBTIPO_DES IN (
        6,
        7
    ) THEN
        RAISE EXCP_NO_EXISTE_GLSSTP;
    END IF;

    IF P_VAL_DEFLAC_BASE IS NULL OR P_FEC_DEFLAC_BASE IS NULL THEN
        RAISE EXCP_NO_EXISTE_DEFBAS;
    END IF;
    IF P_VAL_IVA IS NULL THEN
        RAISE EXCP_NO_EXISTE_VALIVA;
    END IF;
    IF LTRIM(RTRIM(P_NOM_USER) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_USER;
    END IF;
    IF LTRIM(RTRIM(P_COD_MONEDA_IMP) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_MONIMP;
    END IF;
    IF LTRIM(RTRIM(P_ROL_EMPLEADO) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_ROL;
    END IF;
    IF LTRIM(RTRIM(P_COD_PLANTILLA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODTIPOPLANT;
    END IF;
  
  /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
    SELECT
        NVL(COUNT(1),0)
    INTO V_CONTADOR
    FROM
        DESGLO
    WHERE
        COD_PLANTILLA = P_COD_PLANTILLA
        AND COD_CLIENTE = P_COD_CLIENTE
        AND COD_TIPO_DES = P_COD_TIPO_DES
        AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
        AND COD_MONEDA = P_COD_MONEDA;

    IF V_CONTADOR > 0 THEN
        SELECT
            NVL(NOM_USER_OPEN,'')
        INTO V_NOM_USER_OPEN_DB
        FROM
            DESGLO
        WHERE
            COD_PLANTILLA = P_COD_PLANTILLA
            AND COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA;

    END IF;

    IF V_NOM_USER_OPEN_DB <> '' AND TRIM(UPPER(V_NOM_USER_OPEN_DB) ) <> TRIM(UPPER(P_NOM_USER) ) THEN
        RAISE EXCP_NO_AUTHORIZED_USER;
    END IF;
   
  /** SI EXISTE LO ACTUALIZA **/

    IF V_CONTADOR = 1 THEN
        UPDATE DESGLO
        SET
            COD_UNID_MONEDA = P_COD_UNID_MONEDA,
            GLS_SUBTIPO_DES = P_GLS_SUBTIPO_DES,
            VAL_DEFLAC_BASE = P_VAL_DEFLAC_BASE,
            FEC_DEFLAC_BASE = P_FEC_DEFLAC_BASE,
            VAL_IVA = P_VAL_IVA,
            NOM_USER_ANALIS = P_NOM_USER,
            GLS_OBSERVACION = P_GLS_OBSERVACION,
            NOM_USER_MODIF = P_NOM_USER,
            FEC_USER_MODIF = V_FEC_TODAY,
            NOM_USER_OPEN = P_NOM_USER,
            FLG_IS_TEMPORAL = 0,
            COD_MONEDA_IMP = P_COD_MONEDA_IMP,
            NOM_USER = P_USER,
            FLG_VALIDACION = P_FLG_VALIDACION,
            COD_ESTADO = P_COD_ESTADO,
            ROL_EMPLEADO_REV_MOD = P_ROL_EMPLEADO_REV_MOD
        WHERE
            COD_PLANTILLA = P_COD_PLANTILLA
            AND COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA;

    ELSE
  /** SINO, LO CREA **/
        INSERT INTO DESGLO (
            COD_CLIENTE,
            COD_PLANTILLA,
            COD_TIPO_DES,
            COD_SUBTIPO_DES,
            COD_MONEDA,
            COD_UNID_MONEDA,
            GLS_SUBTIPO_DES,
            VAL_DEFLAC_BASE,
            FEC_DEFLAC_BASE,
            VAL_IVA,
            NOM_USER_ANALIS,
            GLS_OBSERVACION,
            NOM_USER_CREA,
            FEC_USER_CREA,
            NOM_USER_OPEN,
            FLG_IS_TEMPORAL,
            COD_MONEDA_IMP,
            NOM_USER,
            FLG_VALIDACION,
            COD_ESTADO,
            ROL_EMPLEADO_REV_MOD
        ) VALUES (
            P_COD_CLIENTE,
            P_COD_PLANTILLA,
            P_COD_TIPO_DES,
            P_COD_SUBTIPO_DES,
            P_COD_MONEDA,
            P_COD_UNID_MONEDA,
            P_GLS_SUBTIPO_DES,
            P_VAL_DEFLAC_BASE,
            P_FEC_DEFLAC_BASE,
            P_VAL_IVA,
            P_NOM_USER,
            P_GLS_OBSERVACION,
            P_NOM_USER,
            V_FEC_TODAY,
            P_NOM_USER,
            0,
            P_COD_MONEDA_IMP,
            P_USER,
            P_FLG_VALIDACION,
            P_COD_ESTADO,
            P_ROL_EMPLEADO_REV_MOD
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
    WHEN EXCP_NO_EXISTE_CODMON THEN
        P_RETORNO :=-4;
        P_GLOSA := 'La Moneda es un parametro obligatorio';
    WHEN EXCP_NO_EXISTE_UNIMON THEN
        P_RETORNO :=-5;
        P_GLOSA := 'La Unidad Monetaria es un parametro obligatorio';
    WHEN EXCP_NO_EXISTE_GLSSTP THEN
        P_RETORNO :=-6;
        P_GLOSA := 'Si el SubTipo es "Otros", entonces la descripcion del subtipo es Obligatoria';
    WHEN EXCP_NO_EXISTE_DEFBAS THEN
        P_RETORNO :=-7;
        P_GLOSA := 'El Deflactor Base es Obligatorio';
    WHEN EXCP_NO_EXISTE_VALIVA THEN
        P_RETORNO :=-8;
        P_GLOSA := 'El IVA es un parametro obligatorio';
    WHEN EXCP_NO_EXISTE_USER THEN
        P_RETORNO :=-9;
        P_GLOSA := 'Se debe identificar el usuario conectado';
    WHEN EXCP_NO_AUTHORIZED_USER THEN
        P_RETORNO :=-10;
        P_GLOSA := 'El desglose se encuentra en uso por otro usuario. No está autorizado para grabar sobre él.';
    WHEN EXCP_NO_EXISTE_MONIMP THEN
        P_RETORNO :=-11;
        P_GLOSA := 'La Moneda de Impresion es un parametro obligatorio';
    WHEN EXCP_NO_EXISTE_FLG_VAL THEN
        P_RETORNO :=-12;
        P_GLOSA := 'Se debe señalar si Desglose está validado o no. (OK, NOK)';
    WHEN EXCP_NO_EXISTE_ESTADO THEN
        P_RETORNO :=-13;
        P_GLOSA := 'Se debe señalar el estado del Desglose. (BEP, BT, DEF)';
    WHEN EXCP_NO_EXISTE_ROL THEN
        P_RETORNO :=-14;
        P_GLOSA := 'Se debe identificar Rol de Empleado el usuario conectado';
    WHEN EXCP_NO_EXISTE_CODTIPOPLANT THEN
        P_RETORNO :=-15;
        P_GLOSA := 'Debe especificar el tipo de plantilla de desglose';
    WHEN OTHERS THEN
        P_RETORNO :=-99;
        P_GLOSA := SQLERRM;
END CIF_SP_INSUPD_DESGLO;

/
