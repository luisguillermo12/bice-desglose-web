create or replace PROCEDURE "CIF_SP_INS_PRCCNS"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_PRCCNS
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza el Porcentaje de Consolidacion de una Empresa para un Periodo
                   en el sistema de Desglose
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
   p_cod_cliente       IN PRCCNS.COD_CLIENTE%TYPE,
   p_cod_plantilla     IN PRCCNS.COD_PLANTILLA%TYPE,
   p_cod_tipo_des      IN PRCCNS.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN PRCCNS.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN PRCCNS.COD_MONEDA%TYPE,
   p_cor_empresa       IN PRCCNS.COR_EMPRESA%TYPE,
   p_fec_periodo       IN PRCCNS.FEC_PERIODO%TYPE,
   p_val_prc_consol    IN PRCCNS.VAL_PRC_CONSOL%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_NO_EXISTE_COREMP    EXCEPTION;
   EXCP_NO_EXISTE_FECPRD    EXCEPTION;
   EXCP_NO_EXISTE_PRCCNS    EXCEPTION;
   v_contador               NUMBER(9):=-1;
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
   IF p_cor_empresa IS NULL THEN
      RAISE EXCP_NO_EXISTE_COREMP;
   END IF;
   IF p_fec_periodo IS NULL THEN
      RAISE EXCP_NO_EXISTE_FECPRD;
   END IF;
   IF p_val_prc_consol IS NULL THEN
      RAISE EXCP_NO_EXISTE_PRCCNS;
   END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   PRCCNS
   WHERE  COD_CLIENTE     = p_cod_cliente
   AND    COD_TIPO_DES    = p_cod_tipo_des
   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
   AND    COD_MONEDA      = p_cod_moneda
   AND    COR_EMPRESA     = p_cor_empresa
   AND    FEC_PERIODO     = p_fec_periodo
   AND    COD_PLANTILLA   = p_cod_plantilla;

   /** SI EXISTE LO ACTUALIZA **/
   IF v_contador = 1 THEN
      UPDATE PRCCNS
      SET    VAL_PRC_CONSOL  = p_val_prc_consol
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COR_EMPRESA     = p_cor_empresa
      AND    FEC_PERIODO     = p_fec_periodo
      AND    COD_PLANTILLA   = p_cod_plantilla;
   ELSE
   /** SINO, LO CREA **/
      INSERT INTO PRCCNS (
             COD_CLIENTE
            ,COD_TIPO_DES
            ,COD_SUBTIPO_DES
            ,COD_MONEDA
            ,COR_EMPRESA
            ,FEC_PERIODO
            ,VAL_PRC_CONSOL
            ,cod_plantilla)
      VALUES (
             p_cod_cliente
            ,p_cod_tipo_des
            ,p_cod_subtipo_des
            ,p_cod_moneda
            ,p_cor_empresa
            ,p_fec_periodo
            ,p_val_prc_consol,
            p_cod_plantilla);
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
      WHEN EXCP_NO_EXISTE_COREMP THEN
         p_retorno := -5;
         p_glosa := 'Debe especificar el correlativo de empresa';
      WHEN EXCP_NO_EXISTE_FECPRD THEN
         p_retorno := -6;
         p_glosa := 'Se requiere un la fecha de periodo a la que pertenece el porcentaje';
      WHEN EXCP_NO_EXISTE_PRCCNS THEN
         p_retorno := -7;
         p_glosa := 'No hay porcentaje a asocial para el periodo. Este dato es requerido.';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_INS_PRCCNS;
