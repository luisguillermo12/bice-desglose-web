create or replace PROCEDURE "CIF_SP_INS_VALPRD"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_VALPRD
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza un Valor de Detalle de Periodo en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
   p_cod_cliente       IN VALPRD.COD_CLIENTE%TYPE,
   p_cod_plantilla     IN VALPRD.COD_PLANTILLA%TYPE,
   p_cod_tipo_des      IN VALPRD.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN VALPRD.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN VALPRD.COD_MONEDA%TYPE,
   p_fec_periodo       IN VALPRD.FEC_PERIODO%TYPE,
   p_cod_tipo_concep   IN VALPRD.COD_TIPO_CONCEP%TYPE,
   p_cor_concepto      IN VALPRD.COR_CONCEPTO%TYPE,
   p_cor_valor         IN VALPRD.COR_VALOR%TYPE,
   p_val_concepto      IN VALPRD.VAL_CONCEPTO%TYPE,
   p_gls_frml_concep   IN VALPRD.GLS_FRML_CONCEP%TYPE,
   p_gls_concepto      IN VALPRD.GLS_CONCEPTO%TYPE,
   p_val_prc_concep    IN VALPRD.VAL_PRC_CONCEP%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_NO_EXISTE_FECPRD    EXCEPTION;
   EXCP_NO_EXISTE_TIPCON    EXCEPTION;
   EXCP_NO_EXISTE_CORCON    EXCEPTION;
   EXCP_NO_EXISTE_GLSCON    EXCEPTION;
   EXCP_NO_EXISTE_DATOS     EXCEPTION;
   v_contador               NUMBER(9):=-1;
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
   IF p_fec_periodo IS NULL THEN
      RAISE EXCP_NO_EXISTE_FECPRD;
   END IF;
   IF ltrim(rtrim(p_cod_tipo_concep)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_TIPCON;
   END IF;
   IF p_cor_concepto IS NULL THEN
      RAISE EXCP_NO_EXISTE_CORCON;
   END IF;
   IF ltrim(rtrim(p_gls_concepto)) IS NULL AND ltrim(rtrim(p_cod_tipo_concep)) = 'MULTIREGISTRO' THEN
      RAISE EXCP_NO_EXISTE_GLSCON;
   END IF;
   IF p_cor_valor IS NULL AND ltrim(rtrim(p_cod_tipo_concep)) <> 'MULTIREGISTRO' THEN
	  v_cor_valor := 0;
   END IF;
   IF p_val_concepto IS NULL AND ltrim(rtrim(p_gls_frml_concep)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_DATOS;
   END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   VALPRD
   WHERE  COD_CLIENTE     = p_cod_cliente
   AND    COD_TIPO_DES    = p_cod_tipo_des
   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
   AND    COD_MONEDA      = p_cod_moneda
   AND    FEC_PERIODO     = p_fec_periodo
   AND    COD_TIPO_CONCEP = p_cod_tipo_concep
   AND    COR_CONCEPTO    = p_cor_concepto
   AND    COR_VALOR       = v_cor_valor
   AND    cod_plantilla   = p_cod_plantilla;

   /** SI EXISTE LO ACTUALIZA **/
   IF v_contador = 1 THEN
      UPDATE VALPRD
      SET    VAL_CONCEPTO    = p_val_concepto,
             GLS_FRML_CONCEP = p_gls_frml_concep,
			 GLS_CONCEPTO    = p_gls_concepto,
			 VAL_PRC_CONCEP  = p_val_prc_concep
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_TIPO_CONCEP = p_cod_tipo_concep
      AND    COR_CONCEPTO    = p_cor_concepto
      AND    COR_VALOR       = v_cor_valor
      AND    cod_plantilla   = p_cod_plantilla;
   ELSE
   /** SINO, LO CREA **/
      INSERT INTO VALPRD (
             COD_CLIENTE
            ,COD_TIPO_DES
            ,COD_SUBTIPO_DES
            ,COD_MONEDA
            ,FEC_PERIODO
            ,COD_TIPO_CONCEP
            ,COR_CONCEPTO
            ,COR_VALOR
            ,VAL_CONCEPTO
            ,GLS_FRML_CONCEP
			,GLS_CONCEPTO
			,VAL_PRC_CONCEP,
            cod_plantilla)
      VALUES (
             p_cod_cliente
            ,p_cod_tipo_des
            ,p_cod_subtipo_des
            ,p_cod_moneda
            ,p_fec_periodo
            ,p_cod_tipo_concep
            ,p_cor_concepto
            ,v_cor_valor
            ,p_val_concepto
            ,p_gls_frml_concep
			,p_gls_concepto
			,p_val_prc_concep
            ,p_cod_plantilla);
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
      WHEN EXCP_NO_EXISTE_FECPRD THEN
         p_retorno := -5;
         p_glosa := 'Se requiere una fecha para identificar el Periodo';
      WHEN EXCP_NO_EXISTE_TIPCON THEN
         p_retorno := -6;
         p_glosa := 'El tipo de concepto es un parametro requerido';
      WHEN EXCP_NO_EXISTE_CORCON THEN
         p_retorno := -7;
         p_glosa := 'El correlativo de concepto es un parametro requerido';
      WHEN EXCP_NO_EXISTE_GLSCON THEN
         p_retorno := -8;
         p_glosa := 'La glosa del concepto es requerida cuando el tipo re registro es MULTIREGISTRO';
      WHEN EXCP_NO_EXISTE_DATOS THEN
         p_retorno := -9;
         p_glosa := 'No hay informacion para grabar; debe indicar un valor numérico o una formula a grabar.';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_INS_VALPRD;
