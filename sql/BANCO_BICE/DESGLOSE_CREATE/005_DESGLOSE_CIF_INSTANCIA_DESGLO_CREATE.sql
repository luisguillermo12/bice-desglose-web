--------------------------------------------------------
--  File created - Monday-March-11-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table DESGLO
--------------------------------------------------------

  CREATE TABLE "DGL_ADMIN"."DESGLO" 
   (	"COD_CLIENTE" VARCHAR2(20 BYTE), 
	"COD_TIPO_DES" NUMBER(1,0), 
	"COD_SUBTIPO_DES" NUMBER(2,0), 
	"COD_MONEDA" VARCHAR2(20 BYTE), 
	"COD_UNID_MONEDA" VARCHAR2(20 BYTE), 
	"GLS_SUBTIPO_DES" VARCHAR2(50 BYTE), 
	"VAL_DEFLAC_BASE" NUMBER(6,3), 
	"VAL_IVA" NUMBER(4,3), 
	"NOM_USER_ANALIS" VARCHAR2(80 BYTE), 
	"GLS_OBSERVACION" VARCHAR2(500 BYTE), 
	"NOM_USER_CREA" VARCHAR2(80 BYTE), 
	"FEC_USER_CREA" DATE, 
	"NOM_USER_MODIF" VARCHAR2(80 BYTE), 
	"FEC_USER_MODIF" DATE, 
	"FEC_DEFLAC_BASE" DATE, 
	"NOM_USER_OPEN" VARCHAR2(80 BYTE), 
	"FLG_IS_TEMPORAL" NUMBER(1,0), 
	"COD_MONEDA_IMP" VARCHAR2(20 BYTE), 
	"FEC_HRA_OPEN" DATE, 
	"NOM_USER" VARCHAR2(80 BYTE), 
	"FLG_VALIDACION" VARCHAR2(5 BYTE), 
	"FLG_REVISION" VARCHAR2(5 BYTE), 
	"GLS_COMENT_VALID" VARCHAR2(500 BYTE), 
	"COD_ESTADO" VARCHAR2(10 BYTE), 
	"ROL_EMPLEADO_DES_DEF" VARCHAR2(7 BYTE), 
	"NOM_REVISOR_DEF" VARCHAR2(80 BYTE), 
	"FEC_DESGLO_DEF" DATE, 
	"ROL_EMPLEADO_REV_MOD" VARCHAR2(7 BYTE), 
	"ROL_EMPLEADO_DEF_ENV_PDF" VARCHAR2(7 BYTE), 
	"FEC_DESGLO_ENV_PDF" DATE, 
	"COD_MONEDA_RPT" NUMBER(5,0), 
	"COR_IMPRESION_RPT" NUMBER(5,0), 
	"FLG_EJE_FACTORING_RPT" VARCHAR2(1 BYTE), 
	"FEC_PERIODO1_RPT" DATE, 
	"FEC_PERIODO2_RPT" DATE, 
	"FEC_PERIODO3_RPT" DATE, 
	"FEC_PERIODO4_RPT" DATE, 
	"FEC_PER_CONSOL1_RPT" DATE, 
	"FEC_PER_CONSOL2_RPT" DATE, 
	"FEC_ULT1_RPT" DATE, 
	"FEC_ULT2_RPT" DATE, 
	"FEC_USER_MODIF_RPT" DATE, 
	"NOM_USER_MODIF_RPT" VARCHAR2(80 BYTE), 
	"NUM_REPORTE_RPT" NUMBER(2,0), 
	"FEC_GENERACION_RPT" DATE, 
	"NUM_DEVOL_EJECUTOR" NUMBER(2,0), 
	"NUM_ROL_CHG_ESTADO" VARCHAR2(20 BYTE), 
	"FLG_REQ_2FIRMA" VARCHAR2(1 BYTE), 
	"COD_PLANTILLA" NUMBER(5,0) DEFAULT 0
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);

   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."FLG_VALIDACION" IS 'Flag que señala resultado de validación. OK o NOK';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."FLG_REVISION" IS 'Flag que señala condicion de revision. AREV o ENREV';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."GLS_COMENT_VALID" IS 'Comentario producto de la validaion';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."COD_ESTADO" IS 'Estado del Desglose';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."ROL_EMPLEADO_DES_DEF" IS 'Rol empleado que lleva Desglose a estado Definitivo';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."NOM_REVISOR_DEF" IS 'Nombre de Revisor que lleva Desglose a estado Definitivo';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."FEC_DESGLO_DEF" IS 'Fecha en que Desglose se lleva a estado Definitivo';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."ROL_EMPLEADO_REV_MOD" IS 'Rol empleado de Revisor que reviso y modifico desglose';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."NUM_ROL_CHG_ESTADO" IS 'Nombre del rol del usuario.';
   COMMENT ON COLUMN "DGL_ADMIN"."DESGLO"."COD_PLANTILLA" IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Index PK_DESGLO
--------------------------------------------------------

  CREATE UNIQUE INDEX "DGL_ADMIN"."PK_DESGLO" ON "DGL_ADMIN"."DESGLO" ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "COD_PLANTILLA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_DES_MONIMP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_MONIMP" BEFORE UPDATE OF COD_MONEDA_IMP ON DESGLO
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_DES_MONIMP
Fecha             :15/07/2006
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo cod_moneda_imp de la tabla DESGLO
Tablas Involucradas..
	   DESGLO

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_moneda   EXCEPTION;
  v_contador         NUMBER(9):=0;
BEGIN
   -- Busco la moneda_imp en el tabla MONEDAS de la tabla DESCOD, en BCT
   v_contador := 0;
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   DESCOD
   WHERE  COD_TABLA = 'MONEDAS'
   AND    COD_GENERICO = :NEW.COD_MONEDA_IMP
   AND    COD_REG_STATUS = 0;
   -- Si NO existe la MONEDA_IMP en la tabla, esta es invalida. ERROR
   IF v_contador = 0 THEN
      RAISE e_invalid_moneda;
   END IF;
EXCEPTION
WHEN e_invalid_moneda   THEN
     raise_application_error(-20100,'Error: La moneda ingresada no es válida en el sistema.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Desglose.');
END CIF_TRG_UPD_DES_MONIMP;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_MONIMP" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_DLG_TRACK_STATUS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_DLG_TRACK_STATUS" 
AFTER UPDATE OF COD_ESTADO ON DESGLO REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
DECLARE
  ROL_REVISOR_SF    VARCHAR2(30):= 'Plat_Desglose_Revisor';
  ROL_DIGITADOR_SF  VARCHAR2(30):= 'Plat_Desglose_Ejecutor';
  ROL_GLOBAL_SF     VARCHAR2(30):= 'Plat_Desglose_Jefe';
  v_count           NUMBER;
  v_rol             VARCHAR2(7);
  v_rol_acc         VARCHAR2(7);
  v_ree             VARCHAR2(7);
  v_gls             VARCHAR2(255);
  v_gls_aux1        VARCHAR2(30);
  v_gls_aux2        VARCHAR2(30);
  v_gls_rol         VARCHAR2(10);
  v_num_rev         NUMBER;
  
  PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN
  v_rol_acc:= :NEW.NUM_ROL_CHG_ESTADO;
  v_num_rev:= :NEW.NUM_DEVOL_EJECUTOR;

  BEGIN
    SELECT NVL(COUNT(*),0)
      INTO v_count
      FROM TURBINE_ROLE R
     INNER JOIN TURBINE_USER_GROUP_ROLE RG ON (RG.ROLE_ID = R.ROLE_ID)
     INNER JOIN TURBINE_USER TU ON (TU.USER_ID = RG.USER_ID)
     INNER JOIN SF_STF_OPERATOR SO ON (SO.LOGIN_NAME = TU.LOGIN_NAME)
     WHERE R.ROLE_NAME = ROL_GLOBAL_SF
       AND SO.OPERATOR_CODE = :NEW.NUM_ROL_CHG_ESTADO;
    EXCEPTION 
      WHEN OTHERS THEN
        v_count:= 0;
  END;  
  IF v_count > 0 THEN
    v_gls_rol:= 'Jefe Area';
  ELSE
    BEGIN
      SELECT NVL(COUNT(*),0)
        INTO v_count
        FROM TURBINE_ROLE R
       INNER JOIN TURBINE_USER_GROUP_ROLE RG ON (RG.ROLE_ID = R.ROLE_ID)
       INNER JOIN TURBINE_USER TU ON (TU.USER_ID = RG.USER_ID)
       INNER JOIN SF_STF_OPERATOR SO ON (SO.LOGIN_NAME = TU.LOGIN_NAME)
       WHERE R.ROLE_NAME = ROL_REVISOR_SF
         AND SO.OPERATOR_CODE = :NEW.NUM_ROL_CHG_ESTADO;
      EXCEPTION 
        WHEN OTHERS THEN
          v_count:= 0;
    END;    
    IF v_count > 0 THEN
      v_gls_rol:= 'Revisor';
    ELSE
      BEGIN
        SELECT NVL(COUNT(*),0)
          INTO v_count
          FROM TURBINE_ROLE R
         INNER JOIN TURBINE_USER_GROUP_ROLE RG ON (RG.ROLE_ID = R.ROLE_ID)
         INNER JOIN TURBINE_USER TU ON (TU.USER_ID = RG.USER_ID)
         INNER JOIN SF_STF_OPERATOR SO ON (SO.LOGIN_NAME = TU.LOGIN_NAME)
         WHERE R.ROLE_NAME = ROL_DIGITADOR_SF
           AND SO.OPERATOR_CODE = :NEW.NUM_ROL_CHG_ESTADO;
        EXCEPTION 
          WHEN OTHERS THEN
            v_count:= 0;
      END;    
      IF v_count > 0 THEN
        v_gls_rol:= 'Analista';
      ELSE
        v_gls_rol:= '';
      END IF;
    END IF;
  END IF; 
  
  BEGIN
    SELECT NVL(COUNT(*),0) + 1
      INTO v_count
      FROM DESGLO_REV_LOG 
     WHERE COD_CLIENTE = :NEW.COD_CLIENTE
       AND COD_TIPO_DES = :NEW.COD_TIPO_DES
       AND COD_SUBTIPO_DES = :NEW.COD_SUBTIPO_DES
       AND COD_MONEDA = :NEW.COD_MONEDA;
  END;
  
  BEGIN       
    SELECT NVL(DES_NUM_ROL_EMPLEADO, '')
      INTO v_ree
      FROM DESGLO_REE
     WHERE NUM_ROL_EMPLEADO = :NEW.NUM_ROL_CHG_ESTADO
       AND COD_REG_STATUS = 0;
    EXCEPTION 
      WHEN OTHERS THEN
        v_ree:= '';
  END;
  
  IF v_num_rev IS NOT NULL AND v_num_rev > 0 THEN
    v_gls_aux1:= ' - Desglose Corregido (' || TRIM(TO_CHAR(v_num_rev)) || ')';
  END IF;  
  IF v_num_rev IS NOT NULL AND v_num_rev > 0 THEN
    v_gls_aux2:= ' (' || TRIM(TO_CHAR(v_num_rev)) || ')';
  END IF;  
  IF :NEW.COD_ESTADO = 'ED' THEN 
    v_gls:= v_gls_rol || ' -> ' || 'Edita Desglose';
  END IF;
  IF :NEW.COD_ESTADO = 'BEP' THEN 
    v_gls:= v_gls_rol || ' -> ' || 'Desglose a Borrador en Proceso';
  END IF;
  IF :NEW.COD_ESTADO = 'BV'THEN 
    v_gls:= v_gls_rol || ' -> ' || 'Desglose Cuadrado';
  END IF;
  IF :OLD.COD_ESTADO = 'BV' AND :NEW.COD_ESTADO = 'BT' THEN 
    v_gls:= v_gls_rol || ' -> Revisor: ' || 'Desglose a Revisión' || v_gls_aux1;
  END IF;
  IF :OLD.COD_ESTADO = 'BT' AND :NEW.COD_ESTADO = 'OBS' THEN 
    v_gls:= v_gls_rol || ' -> Analista: ' || 'Desglose con Observacion' || v_gls_aux2;
  END IF;
  IF :NEW.COD_ESTADO = 'DEF' THEN 
    v_gls:= v_gls_rol || ' -> ' || 'Desglose Aprobado';
  END IF;
  IF :NEW.COD_ESTADO = 'DEFM' THEN 
    v_gls:= v_gls_rol || ' -> ' || 'Desglose Aprobado con Observaciones';
  END IF;
  IF v_gls IS NULL THEN
    v_gls:= v_gls_rol || ' -> OLD.COD_ESTADO: [' || :OLD.COD_ESTADO || '] NEW.COD_ESTADO: [' || :NEW.COD_ESTADO || '] OLD.NUM_DEVOL_EJECUTOR ['||:OLD.NUM_DEVOL_EJECUTOR||'] NEW.NUM_DEVOL_EJECUTOR ['||:NEW.NUM_DEVOL_EJECUTOR||']';
  END IF;
    
  INSERT INTO DESGLO_REV_LOG (FEC_EVENT, NUM_LINEA_LOG, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, NUM_ROL_EMPLEADO, COD_ESTADO, COD_ESTADO_OLD, NUM_ROL_EMP_REEM, GLS_COMENTARIOS)
                      VALUES (SYSTIMESTAMP, v_count, :NEW.COD_CLIENTE, :NEW.COD_TIPO_DES, :NEW.COD_SUBTIPO_DES, :NEW.COD_MONEDA, :NEW.NUM_ROL_CHG_ESTADO, :NEW.COD_ESTADO, :OLD.COD_ESTADO, v_ree, v_gls);
  COMMIT;                        
EXCEPTION 
  WHEN OTHERS THEN
    v_count := 0;
      
END CIF_TRG_DLG_TRACK_STATUS;

/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_DLG_TRACK_STATUS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_DES_MONEDA
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_MONEDA" BEFORE UPDATE OF COD_MONEDA ON DESGLO
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_DES_MONEDA
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo cod_moneda de la tabla DESGLO
Tablas Involucradas..
	   DESGLO

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_moneda   EXCEPTION;
  v_contador         NUMBER(9):=0;
BEGIN
   -- Busco la moneda en el tabla MONEDAS de la tabla DESCOD, en BCT
   v_contador := 0;
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   DESCOD
   WHERE  COD_TABLA = 'MONEDAS'
   AND    COD_GENERICO = :NEW.COD_MONEDA
   AND    COD_REG_STATUS = 0;
   -- Si NO existe la MONEDA en la tabla, esta es invalida. ERROR
   IF v_contador = 0 THEN
      RAISE e_invalid_moneda;
   END IF;
EXCEPTION
WHEN e_invalid_moneda   THEN
     raise_application_error(-20100,'Error: La moneda ingresada no es válida en el sistema.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Desglose.');
END CIF_TRG_UPD_DES_MONEDA;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_MONEDA" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_DES_GLSTIP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_GLSTIP" BEFORE UPDATE OF GLS_SUBTIPO_DES ON DESGLO
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_DES_GLSTIP
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo gls_subtipo_des de la tabla DESGLO
Tablas Involucradas..
	   DESGLO

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_gls_cant_be_null EXCEPTION;
BEGIN
   -- Si el subtipo de desglose es 6 o 7, la glosa de subtipo es requerida
   IF :OLD.COD_SUBTIPO_DES IN (6,7) AND (:NEW.GLS_SUBTIPO_DES = '' OR :NEW.GLS_SUBTIPO_DES IS NULL) THEN
      RAISE e_gls_cant_be_null;
   END IF;
EXCEPTION
WHEN e_gls_cant_be_null THEN
     raise_application_error(-20100,'Error: La glosa del subtipo es REQUERIDA para este subtipo de desglose.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Desglose.');
END CIF_TRG_UPD_DES_GLSTIP;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_GLSTIP" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_INS_DESGLO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_INS_DESGLO" BEFORE INSERT ON DESGLO
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_INS_DESGLO
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza diversas validaciones sobre los campos de la tabla DESGLO
Tablas Involucradas..
	   DESGLO

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_rut      EXCEPTION;
  e_invalid_subtipo  EXCEPTION;
  e_gls_cant_be_null EXCEPTION;
  e_invalid_moneda   EXCEPTION;
  e_invalid_unidmon  EXCEPTION;
  v_contador         NUMBER(9):=0;
BEGIN
   IF INSERTING THEN
      -- Busco existencia del cliente (RUT) en la tabla IDECLI
      SELECT NVL(COUNT(1),0) INTO v_contador
	  FROM   IDECLI
	  WHERE  NUM_IDEN = :NEW.COD_CLIENTE
	  AND    COD_REG_STATUS = 0;
      -- Si no existe, busco existencia del cliente (RUT) en la tabla SINRUT
      IF v_contador = 0 THEN
         SELECT NVL(COUNT(1),0) INTO v_contador
         FROM   SINRUT
         WHERE  COD_CLIENTE = :NEW.COD_CLIENTE;
      END IF;
      -- Si NO existe el RUT en alguna de las tablas, este es invalido. ERROR
      IF v_contador = 0 THEN
         RAISE e_invalid_rut;
      END IF;

      -- Busco el subtipo de desglose en el tabla SBTDES de la tabla DESCOD, en BCT
      v_contador := 0;
	  SELECT NVL(COUNT(1),0) INTO v_contador
	  FROM   DESCOD
	  WHERE  COD_TABLA = 'SBTDES'
	  AND    COD_GENERICO = TO_CHAR(:NEW.COD_SUBTIPO_DES)
	  AND    COD_REG_STATUS = 0;
      -- Si NO existe el SUBTIPO_DESGLOSE en la tabla, este es invalido. ERROR
      IF v_contador = 0 THEN
         RAISE e_invalid_subtipo;
      END IF;

      -- Si el subtipo de desglose es 6 o 7, la glosa de subtipo es requerida
      IF :NEW.COD_SUBTIPO_DES IN (6,7) AND (:NEW.GLS_SUBTIPO_DES = '' OR :NEW.GLS_SUBTIPO_DES IS NULL) THEN
         RAISE e_gls_cant_be_null;
      END IF;

      -- Busco la moneda en el tabla MONEDAS de la tabla DESCOD, en BCT
      v_contador := 0;
      SELECT NVL(COUNT(1),0) INTO v_contador
	  FROM   DESCOD
	  WHERE  COD_TABLA = 'MONEDAS'
	  AND    COD_GENERICO = :NEW.COD_MONEDA
	  AND    COD_REG_STATUS = 0;
      -- Si NO existe la MONEDA en la tabla, esta es invalida. ERROR
      IF v_contador = 0 THEN
         RAISE e_invalid_moneda;
      END IF;

      -- Busco la moneda_imp en el tabla MONEDAS de la tabla DESCOD, en BCT
      v_contador := 0;
      SELECT NVL(COUNT(1),0) INTO v_contador
	  FROM   DESCOD
	  WHERE  COD_TABLA = 'MONEDAS'
	  AND    COD_GENERICO = :NEW.COD_MONEDA_IMP
	  AND    COD_REG_STATUS = 0;
      -- Si NO existe la MONEDA_IMP en la tabla, esta es invalida. ERROR
      IF v_contador = 0 THEN
         RAISE e_invalid_moneda;
      END IF;

      -- Busco la unidad monetaria en el tabla UNIDMON de la tabla DESCOD, en BCT
      v_contador := 0;
      SELECT NVL(COUNT(1),0) INTO v_contador
	  FROM   DESCOD
	  WHERE  COD_TABLA = 'UNIDMON'
	  AND    COD_GENERICO = :NEW.COD_UNID_MONEDA
	  AND    COD_REG_STATUS = 0;
      -- Si NO existe la UNIDAD MONETARIA en la tabla, esta es invalida. ERROR
      IF v_contador = 0 THEN
         RAISE e_invalid_unidmon;
      END IF;
   END IF;
--------------
EXCEPTION
WHEN e_invalid_rut      THEN
     raise_application_error(-20100,'Error: El rut ingresado no esta registrado en el sistema.');
WHEN e_invalid_subtipo  THEN
     raise_application_error(-20100,'Error: El subtipo de desglose no corresponde a una categoría válida.');
WHEN e_gls_cant_be_null THEN
     raise_application_error(-20100,'Error: La glosa del subtipo es REQUERIDA para este subtipo de desglose.');
WHEN e_invalid_moneda   THEN
     raise_application_error(-20100,'Error: La moneda ingresada no es válida en el sistema.');
WHEN e_invalid_unidmon  THEN
     raise_application_error(-20100,'Error: La unidad monetaria no es válida en el sistema.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Insertar el Desglose.');
END CIF_TRG_INS_DESGLO;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_INS_DESGLO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_DLG_EST_MAIL
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_DLG_EST_MAIL" 
AFTER UPDATE OF COD_ESTADO ON DESGLO REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
DECLARE
  v_count NUMBER;
BEGIN

  -- Gestion de envio de correo
  BEGIN
    CIF_PKG_DESGLOSE.CIF_SP_GEN_ENV_MAIL( :NEW.COD_CLIENTE, :NEW.COD_TIPO_DES, :NEW.COD_SUBTIPO_DES, :NEW.COD_MONEDA, :NEW.COD_ESTADO, :OLD.COD_ESTADO );
  EXCEPTION
    WHEN OTHERS THEN
      v_count := 0;
  END;

END CIF_TRG_DLG_EST_MAIL;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_DLG_EST_MAIL" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_DES_UNIMON
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_UNIMON" BEFORE UPDATE OF COD_UNID_MONEDA ON DESGLO
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_DES_UNIMON
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo cod_unid_moneda de la tabla DESGLO
Tablas Involucradas..
	   DESGLO

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_unidmon  EXCEPTION;
  v_contador         NUMBER(9):=0;
BEGIN
   -- Busco la unidad monetaria en el tabla UNIDMON de la tabla DESCOD, en BCT
   v_contador := 0;
   SELECT NVL(COUNT(1),0) INTO v_contador
   FROM   DESCOD
   WHERE  COD_TABLA = 'UNIDMON'
   AND    COD_GENERICO = :NEW.COD_UNID_MONEDA
   AND    COD_REG_STATUS = 0;
   -- Si NO existe la UNIDAD MONETARIA en la tabla, esta es invalida. ERROR
   IF v_contador = 0 THEN
      RAISE e_invalid_unidmon;
   END IF;
EXCEPTION
WHEN e_invalid_unidmon  THEN
     raise_application_error(-20100,'Error: La unidad monetaria no es válida en el sistema.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Desglose.');
END CIF_TRG_UPD_DES_UNIMON;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DES_UNIMON" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_WEB_TRG_UPD_DES_ESTADO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_WEB_TRG_UPD_DES_ESTADO" AFTER UPDATE OF COD_ESTADO ON DESGLO
--REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW  WHEN ( NEW.COD_ESTADO = 'DEFweweeeeewas22222w' ) DECLARE
    -- Variables del desglose.
    V_COD_CLIENTE DESGLO_DGL_ADMIN.COD_CLIENTE%TYPE;
    V_COD_TIPO_DES DESGLO_DGL_ADMIN.COD_TIPO_DES%TYPE;
    V_COD_SUBTIPO_DES DESGLO_DGL_ADMIN.COD_SUBTIPO_DES%TYPE;
    V_COD_MONEDA DESGLO_DGL_ADMIN.COD_MONEDA%TYPE;
    V_COD_PLANTILLA DESGLO_DGL_ADMIN.COD_PLANTILLA%TYPE;
    V_COD_UNID_MONEDA DESGLO_DGL_ADMIN.COD_UNID_MONEDA%TYPE;
    V_GLS_SUBTIPO_DES DESGLO_DGL_ADMIN.GLS_SUBTIPO_DES%TYPE;
    V_VAL_DEFLAC_BASE DESGLO_DGL_ADMIN.VAL_DEFLAC_BASE%TYPE;
    V_FEC_DEFLAC_BASE DESGLO_DGL_ADMIN.FEC_DEFLAC_BASE%TYPE;
    V_VAL_IVA DESGLO_DGL_ADMIN.VAL_IVA%TYPE;
    V_GLS_OBSERVACION DESGLO_DGL_ADMIN.GLS_OBSERVACION%TYPE;
    V_NOM_USER DESGLO_DGL_ADMIN.NOM_USER_CREA%TYPE;
    V_COD_MONEDA_IMP DESGLO_DGL_ADMIN.COD_MONEDA_IMP%TYPE;
    V_USER DESGLO_DGL_ADMIN.NOM_USER%TYPE;
    V_FEC_TODAY DESGLO_DGL_ADMIN.FEC_USER_CREA%TYPE;
    V_NOM_USER_OPEN_DB DESGLO_DGL_ADMIN.NOM_USER_OPEN%TYPE;
    V_COD_ESTADO DESGLO_DGL_ADMIN.COD_ESTADO%TYPE;
    V_FLG_VALIDACION DESGLO_DGL_ADMIN.FLG_VALIDACION%TYPE;
    V_ROL_EMPLEADO_REV_MOD DESGLO_DGL_ADMIN.ROL_EMPLEADO_REV_MOD%TYPE;
    V_NUM_ROL_CHG_ESTADO DESGLO_DGL_ADMIN.NUM_ROL_CHG_ESTADO%TYPE;
    
    V_NUM_CLIENTE IDECLI_DGL_ADMIN.NUM_CLIENTE%TYPE;
    V_COD_TIPO_JURIDI BASCLI_DGL_ADMIN.COD_TIPO_JURIDI%TYPE;

    V_GLOSA     VARCHAR2(200);
    V_CONTADOR  NUMBER(9) := -1;
    V_GLS_LOG   VARCHAR2(200);
    V_PERSONA   NUMBER;
    PO_RETORNO  NUMBER;
    PO_GLOSA    VARCHAR2(200);

    -- VARIABLES DE EXCEPCIONES
    EXCP_NO_EXISTE_CODCLI   EXCEPTION;
    EXCP_NO_EXISTE_TIPDES   EXCEPTION;
    EXCP_NO_EXISTE_STPDES   EXCEPTION;
    EXCP_NO_EXISTE_CODMON   EXCEPTION;
    EXCP_NO_EXISTE_CODPLANT EXCEPTION;
    EXCP_NO_EXISTE_UNIMON   EXCEPTION;
    EXCP_NO_EXISTE_GLSSTP   EXCEPTION;
    EXCP_NO_EXISTE_DEFBAS   EXCEPTION;
    EXCP_NO_EXISTE_VALIVA   EXCEPTION;
    EXCP_NO_EXISTE_USER     EXCEPTION;
    EXCP_NO_AUTHORIZED_USER EXCEPTION;
    EXCP_NO_EXISTE_MONIMP   EXCEPTION;
BEGIN
    V_COD_CLIENTE := :NEW.COD_CLIENTE;
    V_COD_TIPO_DES := :NEW.COD_TIPO_DES;
    V_COD_SUBTIPO_DES := :NEW.COD_SUBTIPO_DES;
    V_COD_MONEDA := :NEW.COD_MONEDA;
    V_COD_PLANTILLA :=  :NEW.COD_PLANTILLA;
    V_COD_UNID_MONEDA := :NEW.COD_UNID_MONEDA;
    V_GLS_SUBTIPO_DES := :NEW.GLS_SUBTIPO_DES;
    V_VAL_DEFLAC_BASE := :NEW.VAL_DEFLAC_BASE;
    V_FEC_DEFLAC_BASE := :NEW.FEC_DEFLAC_BASE;
    V_VAL_IVA := :NEW.VAL_IVA;
    V_GLS_OBSERVACION := :NEW.GLS_OBSERVACION;
    V_NOM_USER := :NEW.NOM_USER_CREA;
    V_COD_MONEDA_IMP := :NEW.COD_MONEDA_IMP;
    V_USER := :NEW.NOM_USER;
    V_COD_ESTADO := :NEW.COD_ESTADO;
    V_FLG_VALIDACION := :NEW.FLG_VALIDACION;
    V_ROL_EMPLEADO_REV_MOD := :NEW.ROL_EMPLEADO_REV_MOD;
    V_NUM_ROL_CHG_ESTADO := :NEW.NUM_ROL_CHG_ESTADO;

    V_GLOSA := '';
    V_FEC_TODAY := SYSDATE;
    V_NOM_USER_OPEN_DB := '';
    V_GLS_LOG := 'Ejecutando el disparador CIF_TRG_UPD_DES_ESTADO por ' || V_USER || ' Rol ' || V_NUM_ROL_CHG_ESTADO;
    V_PERSONA := 1; -- V_EMPRESA := 0;
    PO_RETORNO := 0;
    PO_GLOSA := '';

    -- INICIO VALIDACIONES DE DATOS NOT NULL
    IF LTRIM(RTRIM(V_COD_CLIENTE) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODCLI;
    END IF;
    IF V_COD_TIPO_DES IS NULL THEN
        RAISE EXCP_NO_EXISTE_TIPDES;
    END IF;
    IF V_COD_SUBTIPO_DES IS NULL THEN
        RAISE EXCP_NO_EXISTE_STPDES;
    END IF;
    IF LTRIM(RTRIM(V_COD_MONEDA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODMON;
    END IF;
    IF LTRIM(RTRIM(V_COD_PLANTILLA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_CODPLANT;
    END IF;
    IF LTRIM(RTRIM(V_COD_UNID_MONEDA) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_UNIMON;
    END IF;
    IF LTRIM(RTRIM(V_GLS_SUBTIPO_DES) ) IS NULL AND V_COD_SUBTIPO_DES IN (6, 7) THEN
        RAISE EXCP_NO_EXISTE_GLSSTP;
    END IF;
    IF V_VAL_DEFLAC_BASE IS NULL OR V_FEC_DEFLAC_BASE IS NULL THEN
        RAISE EXCP_NO_EXISTE_DEFBAS;
    END IF;
    IF V_VAL_IVA IS NULL THEN
        RAISE EXCP_NO_EXISTE_VALIVA;
    END IF;
    IF LTRIM(RTRIM(V_NOM_USER) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_USER;
    END IF;
    IF LTRIM(RTRIM(V_COD_MONEDA_IMP) ) IS NULL THEN
        RAISE EXCP_NO_EXISTE_MONIMP;
    END IF;
    -- FIN VALIDACIONES DE DATOS NOT NULL

    -- INICIO COPIAR LOS DATOS DE CLIENTE
    -- Busco el cliente (rut) en la tabla IDECLI del esquema legado. Se omite la condición AND COD_REG_STATUS = 0 porque esto 
    -- debe estar validado en el sistema web
    SELECT NVL(COUNT(1),0) INTO V_CONTADOR FROM IDECLI_DGL_ADMIN WHERE NUM_IDEN = V_COD_CLIENTE;
    -- Si no existe, busco en la tabla SINRUT
    IF V_CONTADOR = 0 THEN
        SELECT NVL(COUNT(1),0) INTO V_CONTADOR FROM SINRUT_DGL_ADMIN WHERE COD_CLIENTE = V_COD_CLIENTE;
    END IF;
    -- Si no existe el rut en alguna de las tablas. Copiar los datos del cliente
    IF V_CONTADOR = 0 THEN
        -- Busco el cliente en las tablas del esquema web
        SELECT NVL(COUNT(1),0) INTO V_CONTADOR FROM IDECLI WHERE NUM_IDEN = V_COD_CLIENTE;
        -- SELECT NVL(COUNT(1),0) INTO V_CONTADOR FROM SINRUT WHERE COD_CLIENTE = V_COD_CLIENTE;
        IF V_CONTADOR = 0 THEN -- Es un cliente sin RUT.
            INSERT INTO SINRUT_DGL_ADMIN SELECT * FROM SINRUT WHERE COD_CLIENTE = V_COD_CLIENTE;
        ELSE -- Es un cliente con RUT.
            INSERT INTO IDECLI_DGL_ADMIN SELECT * FROM IDECLI WHERE NUM_IDEN = V_COD_CLIENTE;
            -- Guardamos en BASCLI porque sabemos que ya existe el cliente con RUT.
            SELECT NUM_CLIENTE INTO V_NUM_CLIENTE FROM IDECLI WHERE NUM_IDEN = V_COD_CLIENTE;
            INSERT INTO BASCLI_DGL_ADMIN SELECT * FROM BASCLI WHERE NUM_CLIENTE = V_NUM_CLIENTE;
            -- Copiar los datos de CLIJUR O CLINAT en dependencia del tipo de cliente.
            SELECT NUM_CLIENTE INTO V_COD_TIPO_JURIDI FROM BASCLI WHERE NUM_CLIENTE = V_NUM_CLIENTE;
            IF V_COD_TIPO_JURIDI = V_PERSONA THEN
                INSERT INTO CLINAT_DGL_ADMIN SELECT * FROM CLINAT WHERE NUM_CLIENTE = V_NUM_CLIENTE;
            ELSE
                INSERT INTO CLIJUR_DGL_ADMIN SELECT * FROM CLIJUR WHERE NUM_CLIENTE = V_NUM_CLIENTE;
            END IF;
        END IF;
    END IF;
    -- FIN COPIAR LOS DATOS DE CLIENTE

   -- SELECCIONA REGISTRO PARA VALIDAR SI EXISTE
    SELECT NVL(COUNT(1),0) INTO V_CONTADOR FROM DESGLO_DGL_ADMIN WHERE COD_CLIENTE = V_COD_CLIENTE 
        AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
        AND COD_PLANTILLA = V_COD_PLANTILLA;

    IF V_CONTADOR > 0 THEN
        SELECT NVL(NOM_USER_OPEN,'') INTO V_NOM_USER_OPEN_DB FROM DESGLO_DGL_ADMIN WHERE COD_CLIENTE = V_COD_CLIENTE
            AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
            AND COD_PLANTILLA = V_COD_PLANTILLA;
    END IF;

    -- Si existe lo actualiza
    IF V_CONTADOR = 1 THEN
        UPDATE DESGLO_DGL_ADMIN
        SET
            COD_UNID_MONEDA         = V_COD_UNID_MONEDA,
            GLS_SUBTIPO_DES         = V_GLS_SUBTIPO_DES,
            VAL_DEFLAC_BASE         = V_VAL_DEFLAC_BASE,
            FEC_DEFLAC_BASE         = V_FEC_DEFLAC_BASE,
            VAL_IVA                 = V_VAL_IVA,
            NOM_USER_ANALIS         = V_NOM_USER,
            GLS_OBSERVACION         = V_GLS_OBSERVACION,
            NOM_USER_MODIF          = V_NOM_USER,
            FEC_USER_MODIF          = V_FEC_TODAY,
            NOM_USER_OPEN           = V_NOM_USER,
            FLG_IS_TEMPORAL         = 0,
            COD_MONEDA_IMP          = V_COD_MONEDA_IMP,
            NOM_USER                = V_USER,
            FLG_VALIDACION          = V_FLG_VALIDACION,
            COD_ESTADO              = V_COD_ESTADO,
            ROL_EMPLEADO_REV_MOD    = V_ROL_EMPLEADO_REV_MOD
        WHERE
            COD_CLIENTE = V_COD_CLIENTE AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES
            AND COD_MONEDA = V_COD_MONEDA AND COD_PLANTILLA = V_COD_PLANTILLA;

    ELSE -- Sino, lo crea
        INSERT INTO DESGLO_DGL_ADMIN (COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA,
            COD_UNID_MONEDA, GLS_SUBTIPO_DES, VAL_DEFLAC_BASE, FEC_DEFLAC_BASE, VAL_IVA, NOM_USER_ANALIS, GLS_OBSERVACION,
            NOM_USER_CREA, FEC_USER_CREA, NOM_USER_OPEN, FLG_IS_TEMPORAL, COD_MONEDA_IMP, NOM_USER, FLG_VALIDACION, COD_ESTADO,
            ROL_EMPLEADO_REV_MOD)
        VALUES (V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA, V_COD_PLANTILLA, V_COD_UNID_MONEDA,
            V_GLS_SUBTIPO_DES, V_VAL_DEFLAC_BASE, V_FEC_DEFLAC_BASE, V_VAL_IVA, V_NOM_USER, V_GLS_OBSERVACION, V_NOM_USER,
            V_FEC_TODAY, V_NOM_USER, 0, V_COD_MONEDA_IMP, V_USER, V_FLG_VALIDACION, V_COD_ESTADO, V_ROL_EMPLEADO_REV_MOD);
    END IF;
    
    /*************** TODO: ¿Hay que eliminar los datos? ***************/
    -- INICIO ELIMINAR DATOS
    -- VALIMP, VALPRD, PRCCNS, PRDIMP, PERIOD
    CIF_SP_DEL_PERIOD_DGL_ADMIN(V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA, NULL, PO_RETORNO, PO_GLOSA);
    -- CONSOL
    DELETE CONSOL_DGL_ADMIN WHERE COD_CLIENTE = V_COD_CLIENTE AND COD_TIPO_DES = V_COD_TIPO_DES
        AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA AND COD_PLANTILLA = V_COD_PLANTILLA;
    /*-- PRCCNS Y CONSOL
    CIF_SP_DEL_CONSOL_DGL_ADMIN(V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA, NULL, PO_RETORNO, PO_GLOSA);*/
    /*-- VALIMP Y VALPRD
    CIF_SP_DEL_VALPRD_DGL_ADMIN(V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA, NULL, NULL, NULL, NULL,
        PO_RETORNO, PO_GLOSA);*/
    /*-- PRDIMP
    DELETE PRDIMP_DGL_ADMIN WHERE COD_CLIENTE = V_COD_CLIENTE AND COD_TIPO_DES = V_COD_TIPO_DES
        AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA AND COD_PLANTILLA = V_COD_PLANTILLA;*/
    -- FIN ELIMINAR DATOS
    
    -- INICIO COPIAR LOS DATOS DE PERIOD
    INSERT INTO PERIOD_DGL_ADMIN SELECT * FROM PERIOD WHERE COD_CLIENTE = V_COD_CLIENTE
        AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
        AND COD_PLANTILLA = V_COD_PLANTILLA;
    -- FIN COPIAR LOS DATOS DE PERIOD
    -- INICIO COPIAR LOS DATOS DE CONSOL
    INSERT INTO CONSOL_DGL_ADMIN SELECT * FROM CONSOL WHERE COD_CLIENTE = V_COD_CLIENTE
        AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
        AND COD_PLANTILLA = V_COD_PLANTILLA;
    -- FIN COPIAR LOS DATOS DE CONSOL
    -- INICIO COPIAR LOS DATOS DE PRCCNS
    INSERT INTO PRCCNS_DGL_ADMIN SELECT * FROM PRCCNS WHERE COD_CLIENTE = V_COD_CLIENTE
        AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
        AND COD_PLANTILLA = V_COD_PLANTILLA;
    -- FIN COPIAR LOS DATOS DE PRCCNS
    -- INICIO COPIAR LOS DATOS DE VALPRD
    INSERT INTO VALPRD_DGL_ADMIN SELECT * FROM VALPRD WHERE COD_CLIENTE = V_COD_CLIENTE
        AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
        AND COD_PLANTILLA = V_COD_PLANTILLA;
    -- FIN COPIAR LOS DATOS DE VALPRD
    -- INICIO COPIAR LOS DATOS DE VALIMP
    INSERT INTO VALIMP_DGL_ADMIN SELECT * FROM VALIMP WHERE COD_CLIENTE = V_COD_CLIENTE
        AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
        AND COD_PLANTILLA = V_COD_PLANTILLA;
    -- FIN COPIAR LOS DATOS DE VALIMP
    -- INICIO COPIAR LOS DATOS DE PRDIMP
    INSERT INTO PRDIMP_DGL_ADMIN SELECT * FROM PRDIMP WHERE COD_CLIENTE = V_COD_CLIENTE
        AND COD_TIPO_DES = V_COD_TIPO_DES AND COD_SUBTIPO_DES = V_COD_SUBTIPO_DES AND COD_MONEDA = V_COD_MONEDA
        AND COD_PLANTILLA = V_COD_PLANTILLA;
    -- FIN COPIAR LOS DATOS DE PRDIMP
    -- CREANDO LOG OK
    CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
        V_COD_PLANTILLA, 'OK -> ' || V_GLS_LOG);

EXCEPTION -- CREANDO LOG ERROR
    WHEN EXCP_NO_EXISTE_CODCLI THEN
        V_GLOSA := 'El cliente es un parametro obligatorio.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_TIPDES THEN
        V_GLOSA := 'Debe especificar el Tipo de desglose.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_STPDES THEN
        V_GLOSA := 'Debe especificar el SubTipo de desglose.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_CODMON THEN
        V_GLOSA := 'La Moneda es un parametro obligatorio.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_CODPLANT THEN
        V_GLOSA := 'El tipo de plantilla un parametro obligatorio.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_UNIMON THEN
        V_GLOSA := 'La Unidad Monetaria es un parametro obligatorio.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_GLSSTP THEN
        V_GLOSA := 'Si el SubTipo es "Otros", entonces la descripcion del subtipo es obligatoria.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_DEFBAS THEN
        V_GLOSA := 'El Deflactor Base es obligatorio.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_VALIVA THEN
        V_GLOSA := 'El IVA es un parametro obligatorio.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_USER THEN
        V_GLOSA := 'Se debe identificar el usuario conectado.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_AUTHORIZED_USER THEN
        V_GLOSA := 'El desglose se encuentra en uso por otro usuario. No está autorizado para grabar sobre él.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN EXCP_NO_EXISTE_MONIMP THEN
        V_GLOSA := 'La Moneda de Impresion es un parametro obligatorio.';
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
    WHEN OTHERS THEN
        V_GLOSA := SQLERRM;
        CIF_SP_INS_DGL_CHK_LOG('CIF_TRG_UPD_DES_ESTADO', V_COD_CLIENTE, V_COD_TIPO_DES, V_COD_SUBTIPO_DES, V_COD_MONEDA,
            V_COD_PLANTILLA, 'ERROR -> ' || V_GLS_LOG || '. ' || V_GLOSA);
END;
/
ALTER TRIGGER "DGL_ADMIN"."CIF_WEB_TRG_UPD_DES_ESTADO" ENABLE;
--------------------------------------------------------
--  Constraints for Table DESGLO
--------------------------------------------------------

  ALTER TABLE "DGL_ADMIN"."DESGLO" ADD CONSTRAINT "PK_DESGLO" PRIMARY KEY ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "COD_PLANTILLA")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TSPC_CIF_DAT_S"  ENABLE;
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("COD_PLANTILLA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("COD_MONEDA_IMP" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("FLG_IS_TEMPORAL" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("FEC_DEFLAC_BASE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("FEC_USER_CREA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("NOM_USER_CREA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("NOM_USER_ANALIS" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("VAL_IVA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("VAL_DEFLAC_BASE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("COD_UNID_MONEDA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("COD_MONEDA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("COD_SUBTIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("COD_TIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" MODIFY ("COD_CLIENTE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESGLO" ADD CONSTRAINT "DEFBAS_00_CHK" CHECK (VAL_DEFLAC_BASE > 0 AND VAL_DEFLAC_BASE <= 999.999) ENABLE;
  ALTER TABLE "DGL_ADMIN"."DESGLO" ADD CONSTRAINT "FLGTMP_00_CHK" CHECK (FLG_IS_TEMPORAL IN (0,1)) ENABLE;
  ALTER TABLE "DGL_ADMIN"."DESGLO" ADD CONSTRAINT "VALIVA_00_CHK" CHECK (VAL_IVA >= 0 AND VAL_IVA <=1) ENABLE;
  ALTER TABLE "DGL_ADMIN"."DESGLO" ADD CONSTRAINT "TPODES_00_CHK" CHECK (COD_TIPO_DES IN (0,1)) ENABLE;
