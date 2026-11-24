--------------------------------------------------------
--  DDL for Procedure CIF_SP_INS_CONIMP
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "CIF_SP_INS_CONIMP" 
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_CONIMP
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza los Conceptos asociados a la Impresion de Resumen
                   de Desglose para la aplicación de Desglose.
Sub_procesos asociados.
------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
Descripcion :
***********************************************************************************/
(
   p_cod_cliente       IN CONIMP.COD_CLIENTE%TYPE,
   p_cod_tipo_des      IN CONIMP.COD_TIPO_DES%TYPE,
   p_cod_subtipo_des   IN CONIMP.COD_SUBTIPO_DES%TYPE,
   p_cod_moneda        IN CONIMP.COD_MONEDA%TYPE,
   p_cod_tipo_concep   IN CONIMP.COD_TIPO_CONCEP%TYPE,
   p_cor_concepto      IN CONIMP.COR_CONCEPTO%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_NO_EXISTE_TIPCON    EXCEPTION;
   EXCP_NO_EXISTE_CORCON    EXCEPTION;
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
   IF ltrim(rtrim(p_cod_tipo_concep)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_TIPCON;
   END IF;
   IF p_cor_concepto IS NULL THEN
      RAISE EXCP_NO_EXISTE_CORCON;
   END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   CONIMP
   WHERE  COD_CLIENTE     = p_cod_cliente
   AND    COD_TIPO_DES    = p_cod_tipo_des
   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
   AND    COD_MONEDA      = p_cod_moneda
   AND    COD_TIPO_CONCEP = p_cod_tipo_concep
   AND    COR_CONCEPTO    = p_cor_concepto;

   /** SI EXISTE NO HACE NADA, DE LO CONTRARIO LO CREA **/
   IF v_contador <> 1 THEN
      INSERT INTO CONIMP (
             COD_CLIENTE
            ,COD_TIPO_DES
            ,COD_SUBTIPO_DES
            ,COD_MONEDA
            ,COD_TIPO_CONCEP
            ,COR_CONCEPTO)
      VALUES (
             p_cod_cliente
            ,p_cod_tipo_des
            ,p_cod_subtipo_des
            ,p_cod_moneda
            ,p_cod_tipo_concep
            ,p_cor_concepto);
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
      WHEN EXCP_NO_EXISTE_TIPCON THEN
         p_retorno := -5;
         p_glosa := 'Debe especificar el Tipo de Concepto';
      WHEN EXCP_NO_EXISTE_CORCON THEN
         p_retorno := -6;
         p_glosa := 'Debe especificar el Correlativo de Concepto';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_INS_CONIMP;

/
