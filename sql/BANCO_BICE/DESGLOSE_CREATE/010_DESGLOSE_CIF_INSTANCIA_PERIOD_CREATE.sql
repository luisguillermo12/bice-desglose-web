--------------------------------------------------------
--  File created - Monday-March-11-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table PERIOD
--------------------------------------------------------

  CREATE TABLE "DGL_ADMIN"."PERIOD" 
   (	"COD_CLIENTE" VARCHAR2(20 BYTE), 
	"COD_TIPO_DES" NUMBER(1,0), 
	"COD_SUBTIPO_DES" NUMBER(2,0), 
	"COD_MONEDA" VARCHAR2(20 BYTE), 
	"FEC_PERIODO" DATE, 
	"NUM_MESES" NUMBER(2,0), 
	"VAL_DEFLAC_PRD" NUMBER(6,3), 
	"COD_AUDITOR" VARCHAR2(20 BYTE), 
	"GLS_OTROS_AUDIT" VARCHAR2(50 BYTE), 
	"COD_ESTADO_PRD" NUMBER(1,0), 
	"COD_ESTADO_AUDI" NUMBER(1,0), 
	"MTO_SBIF_DD_VIG" NUMBER(17,2), 
	"MTO_SBIF_DD_MOR" NUMBER(17,2), 
	"MTO_SBIF_DD_VEN" NUMBER(17,2), 
	"MTO_SBIF_DD_CAS" NUMBER(17,2), 
	"MTO_SBIF_DI_VIG" NUMBER(17,2), 
	"MTO_SBIF_DI_VEN" NUMBER(17,2), 
	"MTO_SBIF_DI_CAS" NUMBER(17,2), 
	"NOM_USER_CREA" VARCHAR2(20 BYTE), 
	"FEC_USER_CREA" DATE, 
	"NOM_USER_MODIF" VARCHAR2(20 BYTE), 
	"FEC_USER_MODIF" DATE, 
	"FEC_INFORM_SBIF" DATE, 
	"VAL_TIPO_CAMBIO" NUMBER(12,6), 
	"MTO_SBIF_IMP_90_179" NUMBER(17,2), 
	"COD_PLANTILLA" NUMBER(5,0) DEFAULT 0
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index PK_PERIOD
--------------------------------------------------------

  CREATE UNIQUE INDEX "DGL_ADMIN"."PK_PERIOD" ON "DGL_ADMIN"."PERIOD" ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "FEC_PERIODO") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_PRD_AUDIT
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_PRD_AUDIT" BEFORE UPDATE OF COD_AUDITOR ON PERIOD
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_PRD_AUDIT
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo cod_auditor de la tabla PERIOD
Tablas Involucradas..
	   PERIOD

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_audit    EXCEPTION;
  v_contador         NUMBER(9):=0;
BEGIN
   -- Busco el auditor del periodo en el tabla AUDITOR de la tabla DESCOD, en BCT
   IF ltrim(rtrim(:NEW.COD_AUDITOR)) IS NOT NULL THEN
      v_contador := 0;
      SELECT NVL(COUNT(1),0) INTO v_contador
      FROM   DESCOD
      WHERE  COD_TABLA = 'AUDITBAL'
      AND    COD_GENERICO = :NEW.COD_AUDITOR
      AND    COD_REG_STATUS = 0;
      -- Si NO existe el AUDITOR en la tabla, este es invalido. ERROR
      IF v_contador = 0 THEN
         RAISE e_invalid_audit;
      END IF;
   END IF;
EXCEPTION
WHEN e_invalid_audit    THEN
     raise_application_error(-20100,'Error: El auditor seleccionado no corresponde a una opción válida.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Periodo.');
END CIF_TRG_UPD_PRD_AUDIT;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_PRD_AUDIT" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_INS_PERIOD
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_INS_PERIOD" BEFORE INSERT ON PERIOD
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_INS_PERIOD
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza diversas validaciones sobre los campos de la tabla PERIOD
Tablas Involucradas..
	   PERIOD

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_audit    EXCEPTION;
  e_gls_cant_be_null EXCEPTION;
  v_contador         NUMBER(9):=0;
BEGIN
   IF INSERTING THEN
      -- Busco el auditor del periodo en el tabla AUDITOR de la tabla DESCOD, en BCT
      IF ltrim(rtrim(:NEW.COD_AUDITOR)) IS NOT NULL THEN
         v_contador := 0;
	     SELECT NVL(COUNT(1),0) INTO v_contador
	     FROM   DESCOD
	     WHERE  COD_TABLA = 'AUDITBAL'
	     AND    COD_GENERICO = :NEW.COD_AUDITOR
	     AND    COD_REG_STATUS = 0;
         -- Si NO existe el AUDITOR en la tabla, este es invalido. ERROR
         IF v_contador = 0 THEN
            RAISE e_invalid_audit;
         END IF;
      END IF;

      -- Si el codigo de auditor es 9999, la glosa del auditor es requerida
      IF :NEW.COD_AUDITOR = '9999' AND (:NEW.GLS_OTROS_AUDIT = '' OR :NEW.GLS_OTROS_AUDIT IS NULL) THEN
         RAISE e_gls_cant_be_null;
      END IF;
   END IF;
--------------
EXCEPTION
WHEN e_invalid_audit    THEN
     raise_application_error(-20100,'Error: El auditor seleccionado no corresponde a una opción válida.');
WHEN e_gls_cant_be_null THEN
     raise_application_error(-20100,'Error: Debe indicar el nombre del auditor.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Insertar el Periodo.');
END CIF_TRG_INS_PERIOD;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_INS_PERIOD" ENABLE;
--------------------------------------------------------
--  Constraints for Table PERIOD
--------------------------------------------------------

  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("COD_PLANTILLA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("VAL_TIPO_CAMBIO" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("FEC_USER_CREA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("NOM_USER_CREA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("COD_ESTADO_AUDI" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("COD_ESTADO_PRD" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("VAL_DEFLAC_PRD" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("NUM_MESES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("FEC_PERIODO" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("COD_MONEDA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("COD_SUBTIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("COD_TIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" MODIFY ("COD_CLIENTE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PERIOD" ADD CONSTRAINT "DEFPRD_00_CHK" CHECK (VAL_DEFLAC_PRD > 0 AND VAL_DEFLAC_PRD <= 999.999) ENABLE;
  ALTER TABLE "DGL_ADMIN"."PERIOD" ADD CONSTRAINT "ESTAUD_00_CHK" CHECK (COD_ESTADO_AUDI IN (0,1,2)) ENABLE;
  ALTER TABLE "DGL_ADMIN"."PERIOD" ADD CONSTRAINT "NUMMES_00_CHK" CHECK (NUM_MESES >= 1 AND NUM_MESES <=12) ENABLE;
  ALTER TABLE "DGL_ADMIN"."PERIOD" ADD CONSTRAINT "PK_PERIOD" PRIMARY KEY ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "FEC_PERIODO")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
  ALTER TABLE "DGL_ADMIN"."PERIOD" ADD CONSTRAINT "ESTPRD_00_CHK" CHECK (COD_ESTADO_PRD IN (0,1,2)) ENABLE;
