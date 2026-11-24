create or replace PROCEDURE "CIF_SP_INS_CONSOL"
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_SP_INS_CONSOL
Fecha             :05/12/2005
Autor             :Rodrigo González K.
Descripcion       :Inserta o Actualiza una Empresa que Consolida en el sistema de Desglose
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
   p_nom_empresa       IN CONSOL.NOM_EMPRESA%TYPE,
   p_retorno           OUT NUMBER,
   p_glosa             OUT VARCHAR2)
IS
   -- Variables Generales
   EXCP_NO_EXISTE_CODCLI    EXCEPTION;
   EXCP_NO_EXISTE_TIPDES    EXCEPTION;
   EXCP_NO_EXISTE_STPDES    EXCEPTION;
   EXCP_NO_EXISTE_MONEDA    EXCEPTION;
   EXCP_NO_EXISTE_NOMBRE    EXCEPTION;
   v_contador               NUMBER(9):=-1;
   v_cor_empresa            CONSOL.COR_EMPRESA%TYPE;
BEGIN
   p_retorno := 0;
   p_glosa := '';
   v_cor_empresa := p_cor_empresa;

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
      v_cor_empresa := 0;
   END IF;
   IF ltrim(rtrim(p_nom_empresa)) IS NULL THEN
      RAISE EXCP_NO_EXISTE_NOMBRE;
   END IF;

   /** SELECCIONA REGISTRO PARA VALIDAR SI EXISTE **/
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   CONSOL
   WHERE  COD_CLIENTE     = p_cod_cliente
   AND    COD_TIPO_DES    = p_cod_tipo_des
   AND    COD_SUBTIPO_DES = p_cod_subtipo_des
   AND    COD_MONEDA      = p_cod_moneda
   AND    COR_EMPRESA     = v_cor_empresa
   AND    cod_plantilla   = p_cod_plantilla;

   /** SI EXISTE LO ACTUALIZA **/
   IF v_contador = 1 THEN
      UPDATE CONSOL
      SET    NOM_EMPRESA     = p_nom_empresa
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    COR_EMPRESA     = v_cor_empresa
      AND    cod_plantilla   = p_cod_plantilla;
   ELSE
   /** SINO, LO CREA **/
      INSERT INTO CONSOL (
             COD_CLIENTE
            ,COD_TIPO_DES
            ,COD_SUBTIPO_DES
            ,COD_MONEDA
            ,COR_EMPRESA
            ,NOM_EMPRESA
            ,cod_plantilla)
      VALUES (
             p_cod_cliente
            ,p_cod_tipo_des
            ,p_cod_subtipo_des
            ,p_cod_moneda
            ,v_cor_empresa
            ,p_nom_empresa,
            p_cod_plantilla);

      SELECT NVL(MAX(COR_EMPRESA),0) INTO v_contador
      FROM   CONSOL
      WHERE  COD_CLIENTE     = p_cod_cliente
      AND    COD_TIPO_DES    = p_cod_tipo_des
      AND    COD_SUBTIPO_DES = p_cod_subtipo_des
      AND    COD_MONEDA      = p_cod_moneda
      AND    cod_plantilla   = p_cod_plantilla;

      p_glosa := v_contador;
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
      WHEN EXCP_NO_EXISTE_NOMBRE THEN
         p_retorno := -5;
         p_glosa := 'Se requiere un nombre para la empresa consolidadora';
      WHEN others then
         p_retorno := -99;
         p_glosa := SQLERRM;
END CIF_SP_INS_CONSOL;
