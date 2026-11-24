--------------------------------------------------------
--  File created - Monday-February-25-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure CIF_SP_INS_DGL_CHK_LOG
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_INS_DGL_CHK_LOG" 
/***********************************************************************************
------------------  DATOS DE CREACIÓN ------------------
Nombre Lógico       : CIF_SP_INS_DGL_CHK_LOG
Fecha               : 25/02/2019
Autor               : Antonio Marrero Palomino
Descripción         : Inserta datos en la tabla de logs "DGL_CHK_LOG".
Tablas involucradas : DGL_CHK_LOG (esquema WEB)
------------------ DATOS ÚLTIMA MODIFICACIÓN ------------------
Fecha       :
Responsable :
***********************************************************************************/ (
    P_COD_TIPO_PROCESO   IN VARCHAR2,
    P_COD_CLIENTE        IN DGL_CHK_LOG.COD_CLIENTE%TYPE,
    P_COD_TIPO_DES       IN DGL_CHK_LOG.COD_TIPO_DES%TYPE,
    P_COD_SUBTIPO_DES    IN DGL_CHK_LOG.COD_SUBTIPO_DES%TYPE,
    P_COD_MONEDA         IN DGL_CHK_LOG.COD_MONEDA%TYPE,
    P_COD_PLANTILLA      IN DGL_CHK_LOG.COD_TIPO_PLANTILLA%TYPE,
    P_GLS_LOG            IN DGL_CHK_LOG.GLS_LOG%TYPE
) IS
BEGIN
    INSERT INTO 
    DGL_CHK_LOG (COD_TIPO_PROCESO, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_TIPO_PLANTILLA, FEC_LOG, GLS_LOG)
    VALUES (P_COD_TIPO_PROCESO, P_COD_CLIENTE, P_COD_TIPO_DES, P_COD_SUBTIPO_DES, P_COD_MONEDA, P_COD_PLANTILLA, SYSTIMESTAMP,
        P_GLS_LOG);
END CIF_SP_INS_DGL_CHK_LOG;

/
