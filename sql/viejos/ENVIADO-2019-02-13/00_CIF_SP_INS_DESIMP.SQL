create or replace PROCEDURE "CIF_SP_INS_DESIMP"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_DESIMP
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza un Desglose en el sistema de Desglose Impreso
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       : 08/02/2007
Responsable : Rodrigo González K.
Descripcion : Se ha incorporado el campo COR_IMPRESO para diferenciar entre impresos
              del Analista e Impresos del Ejecutivo que tengan la misma llave.
***********************************************************************************/
(
   p_cod_cliente       IN DESIMP.COD_CLIENTE%TYPE,
   p_cod_plantilla IN VALIMP.COD_PLANTILLA%TYPE,
   p_cod_tipo_des      IN DESIMP.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN DESIMP.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN DESIMP.COD_MONEDA%TYPE,
   p_cod_moneda_imp    IN DESIMP.COD_MONEDA_IMP%TYPE,
   p_val_deflac_base   IN DESIMP.VAL_DEFLAC_BASE%TYPE,
   p_fec_deflac_base   IN DESIMP.FEC_DEFLAC_BASE%TYPE,
-- MODIFICACION 08/02/2007 COR_IMPRESO --
   p_cor_impreso       OUT DESIMP.COR_IMPRESO%TYPE,
-- ------------------------------FIN COR_IMPRESO
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_PLADES    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_CODMON    EXCEPTION;
   EXCP_NO_EXISTE_MONIMP    EXCEPTION;
   EXCP_NO_EXISTE_DEFBAS    EXCEPTION;
   v_contador               NUMBER(9):=-1;
   v_fec_today              DESIMP.FEC_CREACION%TYPE;
BEGIN
   p_cor_impreso := -1;
   p_retorno := 0;
   p_glosa := '';
   v_fec_today := SYSDATE;

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
      RAISE EXCP_NO_EXISTE_CODMON;
   END IF;
   IF LTRIM(RTRIM(p_cod_moneda_imp)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_MONIMP;
   END IF;
   IF p_val_deflac_base IS NULL OR p_fec_deflac_base IS NULL THEN
      RAISE EXCP_NO_EXISTE_DEFBAS;
   END IF;

-- MODIFICACION 08/02/2007 COR_IMPRESO --
--   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
--   SELECT NVL(COUNT(1),0) INTO v_contador
--   FROM   DESIMP
--   WHERE  COD_CLIENTE     = p_cod_cliente
--   AND    COD_TIPO_DES    = p_cod_tipo_des
--   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
--   AND    COD_MONEDA      = p_cod_moneda
--   AND    COD_MONEDA_IMP  = p_cod_moneda_imp;
--
--   /** SI EXISTE LO ACTUALIZA **/
--   IF v_contador = 1 THEN
--      UPDATE DESIMP
--      SET    VAL_DEFLAC_BASE = p_val_deflac_base,
--             FEC_DEFLAC_BASE = p_fec_deflac_base,
--             FEC_CREACION    = v_fec_today
--      WHERE  COD_CLIENTE     = p_cod_cliente
--      AND    COD_TIPO_DES    = p_cod_tipo_des
--      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
--      AND    COD_MONEDA      = p_cod_moneda
--      AND    COD_MONEDA_IMP  = p_cod_moneda_imp;
--   ELSE
--   /** SINO, LO CREA **/
--      INSERT INTO DESIMP (
--             COD_CLIENTE
--            ,COD_TIPO_DES
--            ,COD_SUBTIPO_DES
--            ,COD_MONEDA
--            ,COD_MONEDA_IMP
--            ,VAL_DEFLAC_BASE
--            ,FEC_DEFLAC_BASE
--            ,FEC_CREACION)
--      VALUES (
--             p_cod_cliente
--            ,p_cod_tipo_des
--            ,p_cod_subtipo_des
--            ,p_cod_moneda
--            ,p_cod_moneda_imp
--            ,p_val_deflac_base
--            ,p_fec_deflac_base
--            ,v_fec_today);
--   END IF;
-- ------------------------------FIN COR_IMPRESO

-- MODIFICACION 08/02/2007 COR_IMPRESO --
   /** SELECCIONA REGISTROS PARA OBTENER EL CORRELATIVO **/
   SELECT NVL(MAX(COR_IMPRESO),0)+1 INTO p_cor_impreso
   FROM   DESIMP
   WHERE  COD_CLIENTE     = p_cod_cliente
   AND    COD_TIPO_DES    = p_cod_tipo_des
   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
   AND    COD_MONEDA      = p_cod_moneda
   AND    COD_MONEDA_IMP  = p_cod_moneda_imp
   AND    COD_PLANTILLA     = p_cod_plantilla;

   INSERT INTO DESIMP (
        COD_CLIENTE
       ,COD_PLANTILLA
       ,COD_TIPO_DES
       ,COD_SUBTIPO_DES
       ,COD_MONEDA
       ,COD_MONEDA_IMP
       ,COR_IMPRESO
       ,VAL_DEFLAC_BASE
       ,FEC_DEFLAC_BASE
       ,FEC_CREACION)
   VALUES (
        p_cod_cliente
       ,p_cod_plantilla
       ,p_cod_tipo_des
       ,p_cod_subtipo_des
       ,p_cod_moneda
       ,p_cod_moneda_imp
       ,p_cor_impreso
       ,p_val_deflac_base
       ,p_fec_deflac_base
       ,v_fec_today);
-- ------------------------------FIN COR_IMPRESO

   EXCEPTION
      WHEN EXCP_NO_EXISTE_CODCLI THEN
         p_cor_impreso := -1;
         p_retorno := -1;
         p_glosa := 'El cliente es un parametro obligatorio';
     WHEN EXCP_NO_EXISTE_PLADES THEN
         p_retorno := -10;
         p_glosa := 'Debe especificar la plantilla de desglose';
      WHEN EXCP_NO_EXISTE_TIPDES THEN
         p_cor_impreso := -1;
         p_retorno := -2;
         p_glosa := 'Debe especificar el Tipo de Desglose';
      WHEN EXCP_NO_EXISTE_STPDES THEN
         p_cor_impreso := -1;
         p_retorno := -3;
         p_glosa := 'Debe especificar el SubTipo de Desglose';
      WHEN EXCP_NO_EXISTE_CODMON THEN
         p_cor_impreso := -1;
         p_retorno := -4;
         p_glosa := 'La Moneda es un parametro obligatorio';
      WHEN EXCP_NO_EXISTE_MONIMP THEN
         p_cor_impreso := -1;
         p_retorno := -5;
         p_glosa := 'La Moneda de Destino del Impreso es un parametro obligatorio';
      WHEN EXCP_NO_EXISTE_DEFBAS THEN
         p_cor_impreso := -1;
         p_retorno := -6;
         p_glosa := 'El Deflactor Base es Obligatorio';
      WHEN OTHERS THEN
         p_cor_impreso := -1;
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_INS_DESIMP;
