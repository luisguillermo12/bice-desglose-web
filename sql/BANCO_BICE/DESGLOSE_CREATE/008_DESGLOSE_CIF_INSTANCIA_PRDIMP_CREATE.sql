--------------------------------------------------------
--  File created - Monday-March-11-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table PRDIMP
--------------------------------------------------------

  CREATE TABLE "DGL_ADMIN"."PRDIMP" 
   (	"COD_CLIENTE" VARCHAR2(20 BYTE), 
	"COD_TIPO_DES" NUMBER(1,0), 
	"COD_SUBTIPO_DES" NUMBER(2,0), 
	"COD_MONEDA" VARCHAR2(20 BYTE), 
	"COD_MONEDA_IMP" VARCHAR2(20 BYTE), 
	"FEC_PERIODO" DATE, 
	"VAL_DEFLAC_PRD" NUMBER(6,3), 
	"VAL_TIPO_CAMBIO" NUMBER(12,6), 
	"COR_IMPRESO" NUMBER(3,0), 
	"COD_TIPO_PLANTILLA_DES" NUMBER(5,0) DEFAULT 0
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index PK_PRDIMP
--------------------------------------------------------

  CREATE UNIQUE INDEX "DGL_ADMIN"."PK_PRDIMP" ON "DGL_ADMIN"."PRDIMP" ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "COD_MONEDA_IMP", "FEC_PERIODO", "COR_IMPRESO") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_INS_PRDIMP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_INS_PRDIMP" BEFORE INSERT ON PRDIMP
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_INS_PRDIMP
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza diversas validaciones sobre los campos de la tabla PRDIMP
Tablas Involucradas..
	   PRDIMP

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE

BEGIN
   IF INSERTING THEN
   	  -- Ajuste de Deflactores = 1 si Moneda_Impreso <> 0 (Pesos)
      IF :NEW.COD_MONEDA_IMP <> 0 THEN
         :NEW.VAL_DEFLAC_PRD := 1;
      END IF;

   	  -- Ajuste de Tipo de Cambio = 1 si Moneda_Desglose = Moneda_Impreso
      IF :NEW.COD_MONEDA = :NEW.COD_MONEDA_IMP THEN
         :NEW.VAL_TIPO_CAMBIO := 1;
      END IF;
   END IF;
--------------
EXCEPTION
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Insertar el Desglose Impresion.');
END CIF_TRG_INS_PRDIMP;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_INS_PRDIMP" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_PRDIMP_DEFLAC
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_PRDIMP_DEFLAC" BEFORE UPDATE OF VAL_DEFLAC_PRD ON PRDIMP
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_PRDIMP_DEFLAC
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo val_deflac_prd de la tabla PRDIMP
Tablas Involucradas..
	   PRDIMP

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE

BEGIN
    -- Ajuste de Deflactores = 1 si Moneda_Impreso <> 0 (Pesos)
    IF :OLD.COD_MONEDA_IMP <> 0 THEN
       :NEW.VAL_DEFLAC_PRD := 1;
    END IF;
EXCEPTION
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Desglose.');
END CIF_TRG_UPD_PRDIMP_DEFLAC;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_PRDIMP_DEFLAC" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_PRDIMP_TPOCBO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_PRDIMP_TPOCBO" BEFORE UPDATE OF VAL_TIPO_CAMBIO ON PRDIMP
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_PRDIMP_TPOCBO
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo val_tipo_cambio de la tabla PRDIMP
Tablas Involucradas..
	   PRDIMP

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE

BEGIN
    -- Ajuste de Tipo de Cambio = 1 si Moneda_Desglose = Moneda_Impreso
    IF :OLD.COD_MONEDA = :OLD.COD_MONEDA_IMP THEN
       :NEW.VAL_TIPO_CAMBIO := 1;
    END IF;
EXCEPTION
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Desglose.');
END CIF_TRG_UPD_PRDIMP_TPOCBO;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_PRDIMP_TPOCBO" ENABLE;
--------------------------------------------------------
--  Constraints for Table PRDIMP
--------------------------------------------------------

  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("COD_TIPO_PLANTILLA_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("COR_IMPRESO" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("VAL_TIPO_CAMBIO" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("VAL_DEFLAC_PRD" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("FEC_PERIODO" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("COD_MONEDA_IMP" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("COD_MONEDA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("COD_SUBTIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("COD_TIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" MODIFY ("COD_CLIENTE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."PRDIMP" ADD CONSTRAINT "PK_PRDIMP" PRIMARY KEY ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "COD_MONEDA_IMP", "FEC_PERIODO", "COR_IMPRESO")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
