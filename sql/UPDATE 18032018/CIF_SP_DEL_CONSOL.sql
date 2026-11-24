create or replace PROCEDURE "CIF_SP_DEL_CONSOL"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_DEL_CONSOL
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Elimina una Empresa que Consolida en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
   p_cod_cliente       IN CONSOL.COD_CLIENTE%TYPE,
   p_cod_plantilla     IN CONSOL.COD_PLANTILLA%TYPE,
   p_cod_tipo_des      IN CONSOL.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN CONSOL.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN CONSOL.COD_MONEDA%TYPE,
   p_cor_empresa       IN CONSOL.COR_EMPRESA%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales

   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_CODPLA    EXCEPTION;
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
   IF ltrim(rtrim(p_cod_plantilla)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_CODPLA;
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

   /** ELIMINA LOS PORCENTAJES DE CONSOLIDACION PARA LOS PERIODOS DEL DEGLOSE (PRCCNS) **/
   IF p_cor_empresa IS NULL THEN
      DELETE PRCCNS
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda;
   ELSE
      DELETE PRCCNS
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COR_EMPRESA     = p_cor_empresa;
   END IF;

   /** ELIMINA UNA DE LAS EMPRESA QUE CONSOLIDA EL DESGLOSE **/
   IF p_cor_empresa IS NULL THEN
      DELETE CONSOL
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COD_PLANTILLA   = p_cod_plantilla;
   ELSE
      DELETE CONSOL
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COR_EMPRESA     = p_cor_empresa
      AND    COD_PLANTILLA   = p_cod_plantilla;
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
      WHEN EXCP_NO_EXISTE_CODPLA THEN
         p_retorno := -5;
         p_glosa := 'Debe especificar tipo de plantilla';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_DEL_CONSOL;
