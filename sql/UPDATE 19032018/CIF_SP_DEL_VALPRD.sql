create or replace PROCEDURE           "CIF_SP_DEL_VALPRD"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_DEL_VALPRD
Fecha             :05/12/2005
Autor             :Rodrigo Gonz lez K.
Descripcion       :Elimina un Valor de Detalle de Periodo en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
   p_cod_cliente       IN VALPRD.COD_CLIENTE%TYPE,
   P_cod_plantilla     IN VALPRD.COD_PLANTILLA%TYPE,
   p_cod_tipo_des      IN VALPRD.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN VALPRD.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN VALPRD.COD_MONEDA%TYPE,
   p_fec_periodo       IN VALPRD.FEC_PERIODO%TYPE,
   p_cod_tipo_concep   IN VALPRD.COD_TIPO_CONCEP%TYPE,
   p_cor_concepto      IN VALPRD.COR_CONCEPTO%TYPE,
   p_cor_valor         IN VALPRD.COR_VALOR%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_OPCION_NO_VALIDA    EXCEPTION;
   v_cor_valor              VALPRD.COR_VALOR%TYPE;
BEGIN
   p_retorno := 0;
   p_glosa := '';
   v_cor_valor := p_cor_valor;

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
  /*IF ltrim(rtrim(p_cod_tipo_concep)) <> 'MULTIREGISTRO' AND p_cor_valor IS NOT NULL THEN
      v_cor_valor := 0;
   END IF;*/

   /** ELIMINA EL VALOR DE DETALLE PARA EL PERIODO DEL DESGLOSE (VALPRD) **/
   IF p_fec_periodo IS NULL AND ltrim(rtrim(p_cod_tipo_concep)) IS NULL AND p_cor_concepto IS NULL AND p_cor_valor IS NULL THEN
      DELETE VALIMP
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    cod_plantilla   = p_cod_plantilla;

      DELETE VALPRD
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COD_PLANTILLA   = p_cod_plantilla;
   ELSIF p_fec_periodo IS NOT NULL AND ltrim(rtrim(p_cod_tipo_concep)) IS NULL AND p_cor_concepto IS NULL AND p_cor_valor IS NULL THEN
      DELETE VALIMP
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_PLANTILLA   = p_cod_plantilla;

      DELETE VALPRD
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_PLANTILLA   = p_cod_plantilla;
   ELSIF p_fec_periodo IS NOT NULL AND ltrim(rtrim(p_cod_tipo_concep)) IS NOT NULL AND p_cor_concepto IS NOT NULL AND p_cor_valor IS NULL THEN
      DELETE VALIMP
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_TIPO_CONCEP = p_cod_tipo_concep
      AND    COR_CONCEPTO    = p_cor_concepto
      AND    COD_PLANTILLA   = p_cod_plantilla;

      DELETE VALPRD
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_TIPO_CONCEP = p_cod_tipo_concep
      AND    COR_CONCEPTO    = p_cor_concepto
      AND    COD_PLANTILLA   = p_cod_plantilla;
   ELSIF p_fec_periodo IS NOT NULL AND ltrim(rtrim(p_cod_tipo_concep)) IS NOT NULL AND p_cor_concepto IS NOT NULL AND p_cor_valor IS NOT NULL THEN
      DELETE VALIMP
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_TIPO_CONCEP = p_cod_tipo_concep
      AND    COR_CONCEPTO    = p_cor_concepto
      AND    COR_VALOR       = v_cor_valor
      AND    COD_PLANTILLA   = p_cod_plantilla;

      DELETE VALPRD
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_TIPO_CONCEP = p_cod_tipo_concep
      AND    COR_CONCEPTO    = p_cor_concepto
      AND    COR_VALOR       = v_cor_valor
      AND    COD_PLANTILLA   = p_cod_plantilla;
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
         p_glosa := 'Debe especificar una opcion de eliminacion valida';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_DEL_VALPRD;
