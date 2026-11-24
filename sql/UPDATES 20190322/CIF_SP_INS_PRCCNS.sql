--------------------------------------------------------
--  DDL for Procedure CIF_SP_INS_PRCCNS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_INS_PRCCNS" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_PRCCNS
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza el Porcentaje de Consolidacion de una Empresa para un Periodo
                   en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 22/03/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se actualiza la firma, incorporando el parámetro "COD_PLANTILLA"
***********************************************************************************/ (
    P_COD_CLIENTE       IN PRCCNS.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES      IN PRCCNS.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES   IN PRCCNS.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA        IN PRCCNS.COD_MONEDA%TYPE,
    P_COR_EMPRESA       IN PRCCNS.COR_EMPRESA%TYPE,
    P_FEC_PERIODO       IN PRCCNS.FEC_PERIODO%TYPE,
    P_VAL_PRC_CONSOL    IN PRCCNS.VAL_PRC_CONSOL%TYPE,
    P_RETORNO           OUT NUMBER,
    P_GLOSA             OUT VARCHAR2,
    P_COD_PLANTILLA     IN PRCCNS.COD_PLANTILLA%TYPE
) IS
   -- Variables Generales

    EXCP_NO_EXISTE_CODCLI EXCEPTION;
    EXCP_NO_EXISTE_TIPDES EXCEPTION;
    EXCP_NO_EXISTE_STPDES EXCEPTION;
    EXCP_NO_EXISTE_MONEDA EXCEPTION;
    EXCP_NO_EXISTE_COREMP EXCEPTION;
    EXCP_NO_EXISTE_FECPRD EXCEPTION;
    EXCP_NO_EXISTE_PRCCNS EXCEPTION;
    EXCP_NO_EXISTE_PLANTILLA EXCEPTION;
    V_CONTADOR   NUMBER(9) :=-1;
BEGIN
    P_RETORNO := 0;
    P_GLOSA := '';

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
        RAISE EXCP_NO_EXISTE_MONEDA;
    END IF;
    IF P_COR_EMPRESA IS NULL THEN
        RAISE EXCP_NO_EXISTE_COREMP;
    END IF;
    IF P_FEC_PERIODO IS NULL THEN
        RAISE EXCP_NO_EXISTE_FECPRD;
    END IF;
    IF P_VAL_PRC_CONSOL IS NULL THEN
        RAISE EXCP_NO_EXISTE_PRCCNS;
    END IF;
    IF P_COD_PLANTILLA IS NULL THEN
        RAISE EXCP_NO_EXISTE_PLANTILLA;
    END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
    SELECT
        NVL(COUNT(1),0)
    INTO V_CONTADOR
    FROM
        PRCCNS
    WHERE
        COD_CLIENTE = P_COD_CLIENTE
        AND COD_TIPO_DES = P_COD_TIPO_DES
        AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
        AND COD_MONEDA = P_COD_MONEDA
        AND COR_EMPRESA = P_COR_EMPRESA
        AND FEC_PERIODO = P_FEC_PERIODO
        AND COD_PLANTILLA = P_COD_PLANTILLA;

   /** SI EXISTE LO ACTUALIZA **/

    IF V_CONTADOR = 1 THEN
        UPDATE PRCCNS
        SET
            VAL_PRC_CONSOL = P_VAL_PRC_CONSOL
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COR_EMPRESA = P_COR_EMPRESA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
   /** SINO, LO CREA **/
        INSERT INTO PRCCNS (
            COD_CLIENTE,
            COD_TIPO_DES,
            COD_SUBTIPO_DES,
            COD_MONEDA,
            COR_EMPRESA,
            FEC_PERIODO,
            VAL_PRC_CONSOL,
            COD_PLANTILLA
        ) VALUES (
            P_COD_CLIENTE,
            P_COD_TIPO_DES,
            P_COD_SUBTIPO_DES,
            P_COD_MONEDA,
            P_COR_EMPRESA,
            P_FEC_PERIODO,
            P_VAL_PRC_CONSOL,
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
    WHEN EXCP_NO_EXISTE_COREMP THEN
        P_RETORNO :=-5;
        P_GLOSA := 'Debe especificar el correlativo de empresa';
    WHEN EXCP_NO_EXISTE_FECPRD THEN
        P_RETORNO :=-6;
        P_GLOSA := 'Se requiere un la fecha de periodo a la que pertenece el porcentaje';
    WHEN EXCP_NO_EXISTE_PRCCNS THEN
        P_RETORNO :=-7;
        P_GLOSA := 'No hay porcentaje a asocial para el periodo. Este dato es requerido.';
    WHEN EXCP_NO_EXISTE_PLANTILLA THEN
        P_RETORNO :=-8;
        P_GLOSA := 'Debe especificar el Codigo de Plantilla';
    WHEN OTHERS THEN
        P_RETORNO :=-99;
        P_GLOSA := SQLERRM;
END CIF_SP_INS_PRCCNS;

/
