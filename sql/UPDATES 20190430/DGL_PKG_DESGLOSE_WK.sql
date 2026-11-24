--------------------------------------------------------
--  File created - Tuesday-April-30-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Package DGL_PKG_DESGLOSE_WK
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "DGL_PKG_DESGLOSE_WK" IS
    PROCEDURE LOG (
        PI_COD_TIPO_PROCESO   IN VARCHAR2,
        PI_NUM_IDEN           IN VARCHAR2,
        PI_COD_TIPO_DES       IN VARCHAR2,
        PI_COD_SUBTIPO_DES    IN VARCHAR2,
        PI_COD_MONEDA         IN VARCHAR2,
        PI_MSG                IN VARCHAR2,
        PI_COD_PLANTILLA      IN VARCHAR2 DEFAULT '0'
    );

    PROCEDURE DGL_SP_GET_LINK_PRINT_WK (
        PI_NUM_IDEN          IN VARCHAR2,
        PI_COD_TIPO_DES      IN VARCHAR2,
        PI_COD_SUBTIPO_DES   IN VARCHAR2,
        PI_COD_MONEDA        IN VARCHAR2,
        PO_GLS_LINK_PRINT    OUT VARCHAR2,
        PI_COD_PLANTILLA     IN VARCHAR2 DEFAULT '0'
    );

    PROCEDURE DGL_SP_GEN_ENV_MAIL_WK (
        PI_NUM_IDEN          IN VARCHAR2,
        PI_COD_TIPO_DES      IN VARCHAR2,
        PI_COD_SUBTIPO_DES   IN VARCHAR2,
        PI_COD_MONEDA        IN VARCHAR2,
        PI_COD_EST_NVO       IN VARCHAR2,
        PI_COD_EST_ANT       IN VARCHAR2,
        PI_COD_PLANTILLA     IN VARCHAR2 DEFAULT '0'
    );

    PROCEDURE DGL_SP_GEN_CHK_INFORMES_WK (
        PI_NUM_IDEN          IN VARCHAR,
        PI_COD_TIPO_DES      IN VARCHAR,
        PI_COD_SUBTIPO_DES   IN VARCHAR,
        PI_COD_MONEDA        IN VARCHAR,
        PI_COD_PLANTILLA     IN VARCHAR2 DEFAULT '0'
    );

    PROCEDURE DGL_SP_GEN_CHK_INFORMES2_WK (
        PI_NUM_IDEN          IN VARCHAR,
        PI_COD_TIPO_DES      IN VARCHAR,
        PI_COD_SUBTIPO_DES   IN VARCHAR,
        PI_COD_MONEDA        IN VARCHAR,
        PI_COD_PLANTILLA     IN VARCHAR2 DEFAULT '0'
    );

    PROCEDURE DGL_SP_CLONAR_DESGLOSE_WK (
        PI_NUM_IDEN              IN VARCHAR2,
        PI_COD_TIPO_DES_ORI      IN VARCHAR2,
        PI_COD_SUBTIPO_DES_ORI   IN VARCHAR2,
        PI_COD_MONEDA_ORI        IN VARCHAR2,
        PI_COD_TIPO_DES_DES      IN VARCHAR2,
        PI_COD_SUBTIPO_DES_DES   IN VARCHAR2,
        PI_COD_MONEDA_DES        IN VARCHAR2,
        PI_LOGIN_NAME            IN VARCHAR2,
        PO_RETORNO               OUT NUMBER,
        PO_GLOSA                 OUT VARCHAR2,
        PI_COD_PLANTILLA         IN VARCHAR2 DEFAULT '0'
    );

    C_COD_PROC_MAIL CONSTANT VARCHAR2(10) := 'DesgloseVB';
    V_NUM_LINEA NUMBER(10) := 0;
END DGL_PKG_DESGLOSE_WK;

/
