--------------------------------------------------------
--  DDL for Procedure CIF_SP_INS_CONSOL
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_INS_CONSOL"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_CONSOL
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza una Empresa que Consolida en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 22/03/2019
Responsable : Antonio Marrero Palomino
Descripcion : Se actualiza la firma, incorporando el parámetro "COD_PLANTILLA"
***********************************************************************************/ (
    P_COD_CLIENTE       IN CONSOL.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES      IN CONSOL.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES   IN CONSOL.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA        IN CONSOL.COD_MONEDA%TYPE,
    P_COR_EMPRESA       IN CONSOL.COR_EMPRESA%TYPE,
    P_NOM_EMPRESA       IN CONSOL.NOM_EMPRESA%TYPE,
    P_RETORNO           OUT NUMBER,
    P_GLOSA             OUT VARCHAR2,
    P_COD_PLANTILLA     IN CONSOL.COD_PLANTILLA%TYPE DEFAULT 0
) IS
   -- Variables Generales

    EXCP_NO_EXISTE_CODCLI EXCEPTION;
    EXCP_NO_EXISTE_TIPDES EXCEPTION;
    EXCP_NO_EXISTE_STPDES EXCEPTION;
    EXCP_NO_EXISTE_MONEDA EXCEPTION;
    EXCP_NO_EXISTE_NOMBRE EXCEPTION;
    EXCP_NO_EXISTE_PLANTILLA EXCEPTION;
    V_CONTADOR      NUMBER(9) :=-1;
    V_COR_EMPRESA   CONSOL.COR_EMPRESA%TYPE;
BEGIN
    P_RETORNO := 0;
    P_GLOSA := '';
    V_COR_EMPRESA := P_COR_EMPRESA;

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
    IF P_COR_EMPRESA IS NULL THEN
        V_COR_EMPRESA := 0;
    END IF;
    IF LTRIM(RTRIM(P_NOM_EMPRESA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_NOMBRE;
    END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
    SELECT
        NVL(COUNT(1),0)
    INTO V_CONTADOR
    FROM
        CONSOL
    WHERE
        COD_CLIENTE = P_COD_CLIENTE
        AND COD_TIPO_DES = P_COD_TIPO_DES
        AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
        AND COD_MONEDA = P_COD_MONEDA
        AND COR_EMPRESA = V_COR_EMPRESA
        AND COD_PLANTILLA = P_COD_PLANTILLA;

   /** SI EXISTE LO ACTUALIZA **/

    IF V_CONTADOR = 1 THEN
        UPDATE CONSOL
        SET
            NOM_EMPRESA = P_NOM_EMPRESA
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COR_EMPRESA = V_COR_EMPRESA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

    ELSE
   /** SINO, LO CREA **/
        INSERT INTO CONSOL (
            COD_CLIENTE,
            COD_TIPO_DES,
            COD_SUBTIPO_DES,
            COD_MONEDA,
            COR_EMPRESA,
            NOM_EMPRESA,
            COD_PLANTILLA
        ) VALUES (
            P_COD_CLIENTE,
            P_COD_TIPO_DES,
            P_COD_SUBTIPO_DES,
            P_COD_MONEDA,
            V_COR_EMPRESA,
            P_NOM_EMPRESA,
            P_COD_PLANTILLA
        );

        SELECT
            NVL(MAX(COR_EMPRESA),0)
        INTO V_CONTADOR
        FROM
            CONSOL
        WHERE
            COD_CLIENTE = P_COD_CLIENTE
            AND COD_TIPO_DES = P_COD_TIPO_DES
            AND COD_SUBTIPO_DES = P_COD_SUBTIPO_DES
            AND COD_MONEDA = P_COD_MONEDA
            AND COD_PLANTILLA = P_COD_PLANTILLA;

        P_GLOSA := V_CONTADOR;
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
    WHEN EXCP_NO_EXISTE_NOMBRE THEN
        P_RETORNO :=-5;
        P_GLOSA := 'Se requiere un nombre para la empresa consolidadora';
    WHEN EXCP_NO_EXISTE_PLANTILLA THEN
        P_RETORNO :=-6;
        P_GLOSA := 'Debe especificar el Codigo de Plantilla';
    WHEN OTHERS THEN
        P_RETORNO :=-99;
        P_GLOSA := SQLERRM;
END CIF_SP_INS_CONSOL;

/
