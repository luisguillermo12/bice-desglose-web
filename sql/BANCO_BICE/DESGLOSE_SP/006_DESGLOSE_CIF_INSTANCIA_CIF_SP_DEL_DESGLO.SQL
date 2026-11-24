--------------------------------------------------------
--  DDL for Procedure CIF_SP_DEL_DESGLO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "DGL_ADMIN"."CIF_SP_DEL_DESGLO"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_DEL_DESGLO
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Elimina un Desglose en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
  p_cod_cliente        IN DESGLO.COD_CLIENTE%TYPE,
  p_cod_tipo_plantilla IN DESGLO.COD_PLANTILLA%TYPE,
  p_cod_tipo_des       IN DESGLO.COD_TIPO_DES%TYPE,
  p_cod_subtipo_des    IN DESGLO.COD_SUBTIPO_DES%TYPE,
  p_cod_moneda         IN DESGLO.COD_MONEDA%TYPE,
  p_retorno            OUT NUMBER,
  p_glosa              OUT VARCHAR2)
IS
  -- Variables Generales
  EXCP_NO_EXISTE_CODCLI    EXCEPTION;
  EXCP_NO_EXISTE_TIPPLA    EXCEPTION;
  EXCP_NO_EXISTE_TIPDES    EXCEPTION;
  EXCP_NO_EXISTE_STPDES    EXCEPTION;
  EXCP_NO_EXISTE_MONEDA    EXCEPTION;

BEGIN
  p_retorno := 0;
  p_glosa := '';

  /*** VALIDA DATOS NOT NULL***/
  IF ltrim(rtrim(p_cod_cliente)) IS NULL THEN
    RAISE EXCP_NO_EXISTE_CODCLI;
  END IF;
  IF ltrim(rtrim(p_cod_tipo_plantilla)) IS NULL THEN
    RAISE EXCP_NO_EXISTE_TIPPLA;
  END IF;
  IF p_cod_tipo_des IS NULL THEN
    RAISE EXCP_NO_EXISTE_TIPDES;
  END IF;
  IF p_cod_subtipo_des IS NULL THEN
    RAISE EXCP_NO_EXISTE_STPDES;
  END IF;
  IF ltrim(rtrim(p_cod_moneda)) IS NULL THEN
    RAISE EXCP_NO_EXISTE_MONEDA;
  END IF;

  /** ELIMINA LOS EVENTOS DE REVISIONES EN TABLA **/
  DELETE DESGLO_REV
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA LOS CONCEPTOS DE IMPRESION RESUMEN (CONIMP) **/
  DELETE CONIMP
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA LOS PORCENTAJES DE CONSOLIDACION **/
  DELETE PRCCNS
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA LAS EMPRESAS QUE CONSOLIDAN **/
  DELETE CONSOL
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA LOS VALORES DE DETALLE DEL DESGLOSE IMPRESO (VALIMP) **/
  DELETE VALIMP
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA LOS VALORES DE DETALLE DEL DESGLOSE (VALPRD) **/
  DELETE VALPRD
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA LOS PERIODOS IMPRESOS **/
  DELETE PRDIMP
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA LOS PERIODOS **/
  DELETE PERIOD
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES    = p_cod_tipo_des
     AND COD_SUBTIPO_DES = p_cod_subtipo_des
     AND COD_MONEDA      = p_cod_moneda;

  /** ELIMINA EL DESGLOSE IMPRESO SEÑALADO **/
  DELETE DESIMP
   WHERE COD_CLIENTE     = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND  COD_TIPO_DES    = p_cod_tipo_des
     AND  COD_SUBTIPO_DES = p_cod_subtipo_des
     AND  COD_MONEDA      = p_cod_moneda;

  /** ELIMINA EL DESGLOSE SEÑALADO **/
  DELETE DESGLO
   WHERE COD_CLIENTE                = p_cod_cliente
     AND COD_PLANTILLA     = p_cod_tipo_plantilla
     AND COD_TIPO_DES               = p_cod_tipo_des
     AND COD_SUBTIPO_DES            = p_cod_subtipo_des
     AND COD_MONEDA                 = p_cod_moneda;

  EXCEPTION
    WHEN EXCP_NO_EXISTE_CODCLI THEN
      p_retorno := -1;
      p_glosa := 'El cliente es un parametro obligatorio';
    WHEN EXCP_NO_EXISTE_TIPPLA THEN
      p_retorno := -5;
      p_glosa := 'El tipo plantilla  es un parametro obligatorio';
    WHEN EXCP_NO_EXISTE_TIPDES THEN
      p_retorno := -2;
      p_glosa := 'Debe especificar el Tipo de Desglose';
    WHEN EXCP_NO_EXISTE_STPDES THEN
      p_retorno := -3;
      p_glosa := 'Debe especificar el SubTipo de Desglose';
    WHEN EXCP_NO_EXISTE_MONEDA THEN
      p_retorno := -4;
      p_glosa := 'Debe especificar la Moneda del Desglose';
    WHEN others THEN
      p_retorno := -99;
      p_glosa := SQLERRM;
END CIF_SP_DEL_DESGLO;

/

  GRANT EXECUTE ON "DGL_ADMIN"."CIF_SP_DEL_DESGLO" TO "R_CIF_CON";
  GRANT EXECUTE ON "DGL_ADMIN"."CIF_SP_DEL_DESGLO" TO "R_CIF_ACT";
