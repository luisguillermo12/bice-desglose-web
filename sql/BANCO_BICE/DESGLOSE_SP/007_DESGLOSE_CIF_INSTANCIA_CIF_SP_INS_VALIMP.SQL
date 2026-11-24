create or replace PROCEDURE           "CIF_SP_INS_VALIMP"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_VALIMP
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza un Valor de Detalle de Periodo Impreso en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 08/02/2007
Responsable : Rodrigo González K.
Descripcion : Se ha incorporado el campo COR_IMPRESO para diferenciar entre impresos
              del Analista e Impresos del Ejecutivo que tengan la misma llave.
***********************************************************************************/
(
   p_cod_cliente            IN VALIMP.COD_CLIENTE%TYPE,
   p_cod_plantilla_des      IN VALIMP.COD_PLANTILLA%TYPE,
   p_cod_tipo_des           IN VALIMP.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des        IN VALIMP.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda             IN VALIMP.COD_MONEDA%TYPE,
   p_cod_moneda_imp         IN VALIMP.COD_MONEDA_IMP%TYPE,
   p_fec_periodo            IN VALIMP.FEC_PERIODO%TYPE,
   p_cod_tipo_concep        IN VALIMP.COD_TIPO_CONCEP%TYPE,
   p_cor_concepto           IN VALIMP.COR_CONCEPTO%TYPE,
   p_cor_valor              IN VALIMP.COR_VALOR%TYPE,
-- ------ Modif. 08/07/2007 (COR_IMPRESO)------
   p_cor_impreso            IN PRDIMP.COR_IMPRESO%TYPE,
-- -----------------------------FIN COR_IMPRESO
   p_val_concepto           IN VALIMP.VAL_CONCEPTO%TYPE,
   p_retorno                OUT NUMBER,
   p_glosa                  OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_PLADES    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_NO_EXISTE_MONIMP    EXCEPTION;
   EXCP_NO_EXISTE_FECPRD    EXCEPTION;
   EXCP_NO_EXISTE_TIPCON    EXCEPTION;
   EXCP_NO_EXISTE_CORCON    EXCEPTION;
   EXCP_NO_EXISTE_CORVAL    EXCEPTION;
   EXCP_NO_EXISTE_DATOS     EXCEPTION;
   v_contador               NUMBER(9):=-1;
-- ------ Modif. 08/07/2007 (COR_IMPRESO)------
   EXCP_NO_EXISTE_CORIMP    EXCEPTION;
-- -----------------------------FIN COR_IMPRESO
BEGIN
   p_retorno := 0;
   p_glosa := '';

   /*** VALIDA DATOS NOT NULL***/
   IF LTRIM(RTRIM(p_cod_cliente)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_CODCLI;
   END IF;
    IF p_cod_plantilla_des IS NULL THEN
      RAISE EXCP_NO_EXISTE_PLADES;
   END IF;
   IF p_cod_tipo_des IS NULL THEN
      RAISE EXCP_NO_EXISTE_TIPDES;
   END IF;
   IF p_cod_subtipo_des IS NULL THEN
      RAISE EXCP_NO_EXISTE_STPDES;
   END IF;
   IF LTRIM(RTRIM(p_cod_moneda)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_MONEDA;
   END IF;
   IF LTRIM(RTRIM(p_cod_moneda_imp)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_MONIMP;
   END IF;
   IF p_fec_periodo IS NULL THEN
      RAISE EXCP_NO_EXISTE_FECPRD;
   END IF;
   IF LTRIM(RTRIM(p_cod_tipo_concep)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_TIPCON;
   END IF;
   IF p_cor_concepto IS NULL THEN
      RAISE EXCP_NO_EXISTE_CORCON;
   END IF;
   IF p_cor_valor IS NULL THEN
      RAISE EXCP_NO_EXISTE_CORVAL;
   END IF;
-- ------ Modif. 08/07/2007 (COR_IMPRESO)------
   IF p_cor_impreso IS NULL THEN
      RAISE EXCP_NO_EXISTE_CORIMP;
   END IF;
-- -----------------------------FIN COR_IMPRESO
   IF p_val_concepto IS NULL THEN
      RAISE EXCP_NO_EXISTE_DATOS;
   END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   VALIMP
   WHERE  COD_CLIENTE     = p_cod_cliente
   AND    COD_TIPO_DES    = p_cod_tipo_des
   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
   AND    COD_MONEDA      = p_cod_moneda
   AND    COD_MONEDA_IMP  = p_cod_moneda_imp
   AND    FEC_PERIODO     = p_fec_periodo
   AND    COD_TIPO_CONCEP = p_cod_tipo_concep
   AND    COR_CONCEPTO    = p_cor_concepto
   AND    COR_VALOR       = p_cor_valor
-- ------ Modif. 08/07/2007 (COR_IMPRESO)------
   AND    COR_IMPRESO     = p_cor_impreso
   AND    COD_PLANTILLA = p_cod_plantilla_des;
-- -----------------------------FIN COR_IMPRESO

   /** SI EXISTE LO ACTUALIZA **/
   IF v_contador = 1 THEN
      UPDATE VALIMP
      SET    VAL_CONCEPTO    = p_val_concepto
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COD_MONEDA_IMP  = p_cod_moneda_imp
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_TIPO_CONCEP = p_cod_tipo_concep
      AND    COR_CONCEPTO    = p_cor_concepto
      AND    COR_VALOR       = p_cor_valor
-- ------ Modif. 08/07/2007 (COR_IMPRESO)------
      AND    COR_IMPRESO     = p_cor_impreso
      AND    COD_PLANTILLA = p_cod_plantilla_des;
-- -----------------------------FIN COR_IMPRESO
   ELSE
   /** SINO, LO CREA **/
      INSERT INTO VALIMP (
             COD_CLIENTE
            ,COD_PLANTILLA
            ,COD_TIPO_DES
            ,COD_SUBTIPO_DES
            ,COD_MONEDA
            ,COD_MONEDA_IMP
            ,FEC_PERIODO
            ,COD_TIPO_CONCEP
            ,COR_CONCEPTO
            ,COR_VALOR
-- ------ Modif. 08/07/2007 (COR_IMPRESO)------
            ,COR_IMPRESO
-- -----------------------------FIN COR_IMPRESO
            ,VAL_CONCEPTO)
      VALUES (
             p_cod_cliente
            ,p_cod_plantilla_des
            ,p_cod_tipo_des
            ,p_cod_subtipo_des
            ,p_cod_moneda
            ,p_cod_moneda_imp
            ,p_fec_periodo
            ,p_cod_tipo_concep
            ,p_cor_concepto
            ,p_cor_valor
-- ------ Modif. 08/07/2007 (COR_IMPRESO)------
            ,p_cor_impreso
-- -----------------------------FIN COR_IMPRESO
            ,p_val_concepto);
   END IF;

   EXCEPTION
      WHEN EXCP_NO_EXISTE_CODCLI THEN
         p_retorno := -1;
         p_glosa := 'El cliente es un parametro obligatorio';
      WHEN EXCP_NO_EXISTE_TIPDES  THEN
         p_retorno := -2;
         p_glosa := 'Debe especificar el Tipo de Desglose';
      WHEN EXCP_NO_EXISTE_PLADES THEN
         p_retorno := -12;
         p_glosa := 'Debe especificar la plantilla de desglose';
      WHEN EXCP_NO_EXISTE_STPDES THEN
         p_retorno := -3;
         p_glosa := 'Debe especificar el SubTipo de Desglose';
      WHEN EXCP_NO_EXISTE_MONEDA THEN
         p_retorno := -4;
         p_glosa := 'Debe especificar la Moneda del Desglose';
      WHEN EXCP_NO_EXISTE_MONIMP THEN
         p_retorno := -5;
         p_glosa := 'Debe especificar la Moneda de Destino del Impreso del Desglose';
      WHEN EXCP_NO_EXISTE_FECPRD THEN
         p_retorno := -6;
         p_glosa := 'Se requiere una fecha para identificar el Periodo';
      WHEN EXCP_NO_EXISTE_TIPCON THEN
         p_retorno := -7;
         p_glosa := 'El tipo de concepto es un parametro requerido';
      WHEN EXCP_NO_EXISTE_CORCON THEN
         p_retorno := -8;
         p_glosa := 'El correlativo de concepto es un parametro requerido';
      WHEN EXCP_NO_EXISTE_CORVAL THEN
         p_retorno := -9;
         p_glosa := 'Debe indicar el correlativo del valor segun lo existente en la tabla VALPRD';
      WHEN EXCP_NO_EXISTE_CORIMP THEN
         p_retorno := -10;
         p_glosa := 'Debe indicar el correlativo del impreso';
      WHEN EXCP_NO_EXISTE_DATOS THEN
         p_retorno := -11;
         p_glosa := 'No hay informacion para grabar; debe indicar un valor numérico o una formula a grabar.';
      WHEN OTHERS THEN
        IF SQLCODE = -2291 THEN
            p_retorno := 0;
            p_glosa := '';
        ELSE
            p_retorno := -99;
            p_glosa := SQLERRM;
        END IF;
END CIF_SP_INS_VALIMP;
