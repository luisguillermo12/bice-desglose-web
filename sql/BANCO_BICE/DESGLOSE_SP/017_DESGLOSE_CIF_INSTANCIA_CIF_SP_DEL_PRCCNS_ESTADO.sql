--------------------------------------------------------
--  DDL for Procedure CIF_SP_DEL_PRCCNS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_DEL_PRCCNS" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_DEL_PRCCNS
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Elimina el Porcentaje de Consolidacion de una Empresa para un Periodo
                   en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
   p_cod_cliente       IN PRCCNS.COD_CLIENTE%TYPE,
   p_cod_tipo_des      IN PRCCNS.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN PRCCNS.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN PRCCNS.COD_MONEDA%TYPE,
   p_cor_empresa       IN PRCCNS.COR_EMPRESA%TYPE,
   p_fec_periodo       IN PRCCNS.FEC_PERIODO%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_OPCION_NO_VALIDA    EXCEPTION;
BEGIN
   p_retorno := 0;
   p_glosa := '';

   /*** VALIDA DATOS NOT NULL***/
   IF ltrim(rtrim(p_cod_cliente)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_CODCLI;
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

   /** ELIMINA EL PORCENTAJE DE CONSOLIDACION PARA UNA EMPRESA, EN UN PERIODO DEL DESGLOSE (PRCCNS) **/
   IF p_cor_empresa IS NULL AND p_fec_periodo IS NULL THEN
      DELETE PRCCNS
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda;
   ELSIF p_cor_empresa IS NOT NULL AND p_fec_periodo IS NULL THEN
      DELETE PRCCNS
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COR_EMPRESA     = p_cor_empresa;
   ELSIF p_cor_empresa IS NOT NULL AND p_fec_periodo IS NOT NULL THEN
      DELETE PRCCNS
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COR_EMPRESA     = p_cor_empresa
      AND    FEC_PERIODO     = p_fec_periodo;
   ELSE
      RAISE EXCP_OPCION_NO_VALIDA;
   END IF;

   EXCEPTION
     WHEN EXCP_NO_EXISTE_CODCLI THEN
         p_retorno := -1;
         p_glosa := 'El cliente es un parametro obligatorio';
      WHEN EXCP_NO_EXISTE_TIPDES THEN
         p_retorno := -2;
         p_glosa := 'Debe especificar el Tipo de Desglose';
      WHEN EXCP_NO_EXISTE_STPDES THEN
         p_retorno := -3;
         p_glosa := 'Debe especificar el SubTipo de Desglose';
      WHEN EXCP_NO_EXISTE_MONEDA THEN
         p_retorno := -4;
         p_glosa := 'Debe especificar la Moneda del Desglose';
      WHEN EXCP_OPCION_NO_VALIDA THEN
         p_retorno := -5;
         p_glosa := 'Debe indicar una opcion de eliminacion valida.';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_DEL_PRCCNS;

/
