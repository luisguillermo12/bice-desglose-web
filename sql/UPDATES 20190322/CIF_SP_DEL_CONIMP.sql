--------------------------------------------------------
--  DDL for Procedure CIF_SP_DEL_CONIMP
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_DEL_CONIMP" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_DEL_CONIMP
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Elimina un Concepto asociado a la Impresion de Resumen de Desglose
                   para la aplicación de Desglose.
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 22/03/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se actualiza la firma, incorporando el parámetro "COD_PLANTILLA"
***********************************************************************************/ (
    P_COD_CLIENTE       IN CONIMP.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES      IN CONIMP.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES   IN CONIMP.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA        IN CONIMP.COD_MONEDA%TYPE,
    P_COD_TIPO_CONCEP   IN CONIMP.COD_TIPO_CONCEP%TYPE,
    P_COR_CONCEPTO      IN CONIMP.COR_CONCEPTO%TYPE,
    P_RETORNO           OUT NUMBER,
    P_GLOSA             OUT VARCHAR2,
    P_COD_PLANTILLA     IN CONIMP.COD_PLANTILLA%TYPE DEFAULT 0
) IS
   -- Variables Generales
    EXCP_NO_EXISTE_CODPLA EXCEPTION;
    EXCP_NO_EXISTE_CODCLI EXCEPTION;
    EXCP_NO_EXISTE_TIPDES EXCEPTION;
    EXCP_NO_EXISTE_STPDES EXCEPTION;
    EXCP_NO_EXISTE_MONEDA EXCEPTION;
BEGIN
    P_RETORNO := 0;
    P_GLOSA := '';

   /*** VALIDA DATOS NOT NULL***/
    IF LTRIM(RTRIM(P_COD_CLIENTE) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODCLI;
    END IF;
    IF LTRIM(RTRIM(P_COD_PLANTILLA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODPLA;
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

   /** ELIMINAR CONCEPTO DE IMPRESION DEL RESUMEN DE DESGLOSE (CONIMP) **/
    IF LTRIM(RTRIM(P_COD_TIPO_CONCEP) ) IS NULL OR P_COR_CONCEPTO IS NULL THEN
        DELETE CONIMP
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
        DELETE CONIMP
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_TIPO_CONCEP = P_COD_TIPO_CONCEP
            AND COR_CONCEPTO = P_COR_CONCEPTO
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
        P_GLOSA := 'Debe especificar la Moneda del Desglose';
    WHEN EXCP_NO_EXISTE_CODPLA THEN
        P_RETORNO :=-5;
        P_GLOSA := 'Debe especificar el Codigo de Plantilla';
    WHEN OTHERS THEN
        P_RETORNO :=-99;
        P_GLOSA := SQLERRM;
END CIF_SP_DEL_CONIMP;

/
