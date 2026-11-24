--------------------------------------------------------
--  DDL for Procedure CIF_SP_INS_VALPRD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_INS_VALPRD" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_VALPRD
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza un Valor de Detalle de Periodo en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 22/03/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se actualiza la firma, incorporando el parámetro "COD_PLANTILLA"
***********************************************************************************/ (
    P_COD_CLIENTE       IN VALPRD.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES      IN VALPRD.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES   IN VALPRD.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA        IN VALPRD.COD_MONEDA%TYPE,
    P_FEC_PERIODO       IN VALPRD.FEC_PERIODO%TYPE,
    P_COD_TIPO_CONCEP   IN VALPRD.COD_TIPO_CONCEP%TYPE,
    P_COR_CONCEPTO      IN VALPRD.COR_CONCEPTO%TYPE,
    P_COR_VALOR         IN VALPRD.COR_VALOR%TYPE,
    P_VAL_CONCEPTO      IN VALPRD.VAL_CONCEPTO%TYPE,
    P_GLS_FRML_CONCEP   IN VALPRD.GLS_FRML_CONCEP%TYPE,
    P_GLS_CONCEPTO      IN VALPRD.GLS_CONCEPTO%TYPE,
    P_VAL_PRC_CONCEP    IN VALPRD.VAL_PRC_CONCEP%TYPE,
    P_RETORNO           OUT NUMBER,
    P_GLOSA             OUT VARCHAR2,
    P_COD_PLANTILLA     IN VALPRD.COD_PLANTILLA%TYPE
) IS
   -- Variables Generales

    EXCP_NO_EXISTE_CODCLI EXCEPTION;
    EXCP_NO_EXISTE_TIPDES EXCEPTION;
    EXCP_NO_EXISTE_STPDES EXCEPTION;
    EXCP_NO_EXISTE_MONEDA EXCEPTION;
    EXCP_NO_EXISTE_FECPRD EXCEPTION;
    EXCP_NO_EXISTE_TIPCON EXCEPTION;
    EXCP_NO_EXISTE_CORCON EXCEPTION;
    EXCP_NO_EXISTE_GLSCON EXCEPTION;
    EXCP_NO_EXISTE_DATOS EXCEPTION;
    EXCP_NO_EXISTE_PLANTILLA EXCEPTION;
    V_CONTADOR    NUMBER(9) :=-1;
    V_COR_VALOR   VALPRD.COR_VALOR%TYPE;
BEGIN
    P_RETORNO := 0;
    P_GLOSA := '';
    V_COR_VALOR := P_COR_VALOR;

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
    IF LTRIM(RTRIM(P_COD_TIPO_CONCEP) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_TIPCON;
    END IF;
    IF P_COR_CONCEPTO IS NULL THEN
        RAISE EXCP_NO_EXISTE_CORCON;
    END IF;
    IF LTRIM(RTRIM(P_GLS_CONCEPTO) ) IS NULL AND LTRIM(RTRIM(P_COD_TIPO_CONCEP) ) = 'MULTIREGISTRO' THEN
        RAISE EXCP_NO_EXISTE_GLSCON;
    END IF;

    IF P_COR_VALOR IS NULL AND LTRIM(RTRIM(P_COD_TIPO_CONCEP) ) <> 'MULTIREGISTRO' THEN
        V_COR_VALOR := 0;
    END IF;

    IF P_VAL_CONCEPTO IS NULL AND LTRIM(RTRIM(P_GLS_FRML_CONCEP) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_DATOS;
    END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/

    SELECT
        NVL(COUNT(1),0)
    INTO V_CONTADOR
    FROM
        VALPRD
    WHERE
        COD_CLIENTE = P_COD_CLIENTE
        AND COD_TIPO_DES = P_COD_TIPO_DES
        AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
        AND COD_MONEDA = P_COD_MONEDA
        AND FEC_PERIODO = P_FEC_PERIODO
        AND COD_TIPO_CONCEP = P_COD_TIPO_CONCEP
        AND COR_CONCEPTO = P_COR_CONCEPTO
        AND COR_VALOR = V_COR_VALOR
        AND COD_PLANTILLA = P_COD_PLANTILLA;

   /** SI EXISTE LO ACTUALIZA **/

    IF V_CONTADOR = 1 THEN
        UPDATE VALPRD
        SET
            VAL_CONCEPTO = P_VAL_CONCEPTO,
            GLS_FRML_CONCEP = P_GLS_FRML_CONCEP,
            GLS_CONCEPTO = P_GLS_CONCEPTO,
            VAL_PRC_CONCEP = P_VAL_PRC_CONCEP
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_TIPO_CONCEP = P_COD_TIPO_CONCEP
            AND COR_CONCEPTO = P_COR_CONCEPTO
            AND COR_VALOR = V_COR_VALOR
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
   /** SINO, LO CREA **/
        INSERT INTO VALPRD (
            COD_CLIENTE,
            COD_TIPO_DES,
            COD_SUBTIPO_DES,
            COD_MONEDA,
            FEC_PERIODO,
            COD_TIPO_CONCEP,
            COR_CONCEPTO,
            COR_VALOR,
            VAL_CONCEPTO,
            GLS_FRML_CONCEP,
            GLS_CONCEPTO,
            VAL_PRC_CONCEP,
            COD_PLANTILLA
        ) VALUES (
            P_COD_CLIENTE,
            P_COD_TIPO_DES,
            P_COD_SUBTIPO_DES,
            P_COD_MONEDA,
            P_FEC_PERIODO,
            P_COD_TIPO_CONCEP,
            P_COR_CONCEPTO,
            V_COR_VALOR,
            P_VAL_CONCEPTO,
            P_GLS_FRML_CONCEP,
            P_GLS_CONCEPTO,
            P_VAL_PRC_CONCEP,
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
    WHEN EXCP_NO_EXISTE_TIPCON THEN
        P_RETORNO :=-6;
        P_GLOSA := 'El tipo de concepto es un parametro requerido';
    WHEN EXCP_NO_EXISTE_CORCON THEN
        P_RETORNO :=-7;
        P_GLOSA := 'El correlativo de concepto es un parametro requerido';
    WHEN EXCP_NO_EXISTE_GLSCON THEN
        P_RETORNO :=-8;
        P_GLOSA := 'La glosa del concepto es requerida cuando el tipo re registro es MULTIREGISTRO';
    WHEN EXCP_NO_EXISTE_DATOS THEN
        P_RETORNO :=-9;
        P_GLOSA := 'No hay informacion para grabar; debe indicar un valor numérico o una formula a grabar.';
    WHEN EXCP_NO_EXISTE_PLANTILLA THEN
        P_RETORNO :=-10;
        P_GLOSA := 'Debe especificar el Codigo de Plantilla';
    WHEN OTHERS THEN
        P_RETORNO :=-99;
        P_GLOSA := SQLERRM;
END CIF_SP_INS_VALPRD;

/
