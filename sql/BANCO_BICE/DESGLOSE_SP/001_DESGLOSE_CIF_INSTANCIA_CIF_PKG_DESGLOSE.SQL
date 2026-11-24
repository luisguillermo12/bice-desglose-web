--------------------------------------------------------
--  File created - Tuesday-March-12-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Package CIF_PKG_DESGLOSE
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "DGL_ADMIN"."CIF_PKG_DESGLOSE" 
IS
  PROCEDURE LOG
  (
    pi_cod_tipo_proceso     IN  VARCHAR2,
    pi_num_iden             IN  VARCHAR2,
    pi_cod_tipo_des         IN  VARCHAR2,
    pi_cod_subtipo_des      IN  VARCHAR2,
    pi_cod_moneda           IN  VARCHAR2,
    pi_msg                  IN  VARCHAR2,
    pi_cod_plantilla        IN  VARCHAR2 DEFAULT '0'
  );

  PROCEDURE CIF_SP_GET_LINK_PRINT
  (
    pi_num_iden         IN  VARCHAR2,
    pi_cod_tipo_des     IN  VARCHAR2,
    pi_cod_subtipo_des  IN  VARCHAR2,
    pi_cod_moneda       IN  VARCHAR2,
    po_gls_link_print   OUT VARCHAR2
  );

  PROCEDURE CIF_SP_GEN_ENV_MAIL
  (
    pi_num_iden         IN  VARCHAR2,
    pi_cod_tipo_des     IN  VARCHAR2,
    pi_cod_subtipo_des  IN  VARCHAR2,
    pi_cod_moneda       IN  VARCHAR2,
    pi_cod_est_nvo      IN  VARCHAR2,
    pi_cod_est_ant      IN  VARCHAR2
  );

  PROCEDURE CIF_SP_GEN_CHK_INFORMES
  (
    pi_num_iden        IN  VARCHAR,
    pi_cod_tipo_des    IN  VARCHAR,
    pi_cod_subtipo_des IN  VARCHAR,
    pi_cod_moneda      IN  VARCHAR
  );
  PROCEDURE CIF_SP_GEN_CHK_INFORMES2
  (
    pi_num_iden        IN  VARCHAR,
    pi_cod_tipo_des    IN  VARCHAR,
    pi_cod_subtipo_des IN  VARCHAR,
    pi_cod_moneda      IN  VARCHAR
  );
  PROCEDURE CIF_SP_CLONAR_DESGLOSE
  (
    pi_num_iden             IN  VARCHAR2,
    pi_cod_tipo_des_ori     IN  VARCHAR2,
    pi_cod_subtipo_des_ori  IN  VARCHAR2,
    pi_cod_moneda_ori       IN  VARCHAR2,
    pi_cod_tipo_des_des     IN  VARCHAR2,
    pi_cod_subtipo_des_des  IN  VARCHAR2,
    pi_cod_moneda_des       IN  VARCHAR2,
    pi_login_name           IN  VARCHAR2,
    po_retorno              OUT  NUMBER,
    po_glosa                OUT  VARCHAR2,
    pi_cod_plantilla        IN  VARCHAR2 DEFAULT '0'
  );

  C_COD_PROC_MAIL      CONSTANT VARCHAR2(10) := 'DesgloseVB';

  v_num_linea                 NUMBER (10)   := 0; 

END CIF_PKG_DESGLOSE;

/
