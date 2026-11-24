--------------------------------------------------------
--  DDL for Procedure CIF_SP_DES_CLONAR_DESGLOSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_DES_CLONAR_DESGLOSE" 
(
  pi_num_iden               IN  VARCHAR2,
  pi_cod_tipo_des_ori       IN  VARCHAR2,
  pi_cod_subtipo_des_ori    IN  VARCHAR2,
  pi_cod_moneda_ori         IN  VARCHAR2,
  pi_cod_tipo_des_des       IN  VARCHAR2,
  pi_cod_subtipo_des_des    IN  VARCHAR2,
  pi_cod_moneda_des         IN  VARCHAR2,
  pi_login_name             IN  VARCHAR2,
  po_retorno                OUT  NUMBER,
  po_glosa                  OUT  VARCHAR2,
  pi_cod_plantilla     IN  VARCHAR2 DEFAULT '0'  
)
 AS
  
BEGIN

  CIF_PKG_DESGLOSE.CIF_SP_CLONAR_DESGLOSE(pi_num_iden, pi_cod_tipo_des_ori, pi_cod_subtipo_des_ori, pi_cod_moneda_ori, pi_cod_tipo_des_des, pi_cod_subtipo_des_des, pi_cod_moneda_des, pi_login_name, po_retorno, po_glosa, pi_cod_plantilla);

END CIF_SP_DES_CLONAR_DESGLOSE;

/
