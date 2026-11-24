--------------------------------------------------------
--  DDL for Procedure CIF_SP_DEL_PERIOD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_DEL_PERIOD" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_DEL_PERIOD
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Elimina un Periodo en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 26/03/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se actualiza la firma, incorporando el parámetro "COD_PLANTILLA"
***********************************************************************************/ (
    P_COD_CLIENTE       IN PERIOD.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES      IN PERIOD.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES   IN PERIOD.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA        IN PERIOD.COD_MONEDA%TYPE,
    P_FEC_PERIODO       IN PERIOD.FEC_PERIODO%TYPE,
    P_RETORNO           OUT NUMBER,
    P_GLOSA             OUT VARCHAR2,
    P_COD_PLANTILLA     IN PERIOD.COD_PLANTILLA%TYPE DEFAULT 0
) IS
   -- Variables Generales
    EXCP_NO_EXISTE_CODCLI EXCEPTION;
    EXCP_NO_EXISTE_TIPDES EXCEPTION;
    EXCP_NO_EXISTE_STPDES EXCEPTION;
    EXCP_NO_EXISTE_MONEDA EXCEPTION;
    EXCP_NO_EXISTE_PLANTILLA EXCEPTION;
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
    IF P_COD_PLANTILLA IS NULL THEN
        RAISE EXCP_NO_EXISTE_PLANTILLA;
    END IF;

   /** ELIMINA LOS VALORES DE DETALLE DEL PERIODO DE DESGLOSE (VALPRD) **/
    IF P_FEC_PERIODO IS NULL THEN
        DELETE VALIMP
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

        DELETE VALPRD
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
        DELETE VALIMP
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_PLANTILLA = P_COD_PLANTILLA;

        DELETE VALPRD
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    END IF;

   /** ELIMINA EL PORCENTAJE DE CONSOLIDACION DEL PERIODO **/

    IF P_FEC_PERIODO IS NULL THEN
        DELETE PRCCNS
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
        DELETE PRCCNS
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    END IF;

   /** ELIMINA EL PERIODO DE DESGLOSE **/

    IF P_FEC_PERIODO IS NULL THEN
        DELETE PRDIMP
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

        DELETE PERIOD
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
        DELETE PRDIMP
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_PLANTILLA = P_COD_PLANTILLA;

        DELETE PERIOD
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND FEC_PERIODO = P_FEC_PERIODO
            AND COD_PLANTILLA = P_COD_PLANTILLA;

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
        P_GLOSA := 'Debe especificar la Moneda de Desglose';
    WHEN EXCP_NO_EXISTE_PLANTILLA THEN
        P_RETORNO :=-5;
        P_GLOSA := 'Debe especificar el Codigo de Plantilla';
    WHEN OTHERS THEN
        P_RETORNO :=-99;
        P_GLOSA := SQLERRM;
END CIF_SP_DEL_PERIOD;

/
