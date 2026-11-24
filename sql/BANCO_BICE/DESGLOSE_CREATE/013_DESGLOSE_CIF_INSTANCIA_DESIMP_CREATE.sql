--------------------------------------------------------
--  File created - Monday-March-11-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table DESIMP
--------------------------------------------------------

  CREATE TABLE "DGL_ADMIN"."DESIMP" 
   (	"COD_CLIENTE" VARCHAR2(20 BYTE), 
	"COD_TIPO_DES" NUMBER(1,0), 
	"COD_SUBTIPO_DES" NUMBER(2,0), 
	"COD_MONEDA" VARCHAR2(20 BYTE), 
	"COD_MONEDA_IMP" VARCHAR2(20 BYTE), 
	"VAL_DEFLAC_BASE" NUMBER(6,3), 
	"FEC_CREACION" DATE, 
	"FEC_DEFLAC_BASE" DATE, 
	"COR_IMPRESO" NUMBER(3,0), 
	"COD_PLANTILLA" NUMBER(5,0) DEFAULT 0
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;
--------------------------------------------------------
--  DDL for Index PK_DESIMP
--------------------------------------------------------

  CREATE UNIQUE INDEX "DGL_ADMIN"."PK_DESIMP" ON "DGL_ADMIN"."DESIMP" ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "COD_MONEDA_IMP", "COR_IMPRESO") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_UPD_DESIMP_DEFLAC
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DESIMP_DEFLAC" BEFORE UPDATE OF VAL_DEFLAC_BASE ON DESIMP
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_UPD_DESIMP_DEFLAC
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza validaciones sobre el campo val_deflac_base de la tabla DESIMP
Tablas Involucradas..
	   DESIMP

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE

BEGIN
    -- Ajuste de Deflactores = 1 si Moneda_Impreso <> 0 (Pesos)
    IF :OLD.COD_MONEDA_IMP <> 0 THEN
       :NEW.VAL_DEFLAC_BASE := 1;
    END IF;
EXCEPTION
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Actualizar el Desglose.');
END CIF_TRG_UPD_DESIMP_DEFLAC;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_UPD_DESIMP_DEFLAC" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIF_TRG_INS_DESIMP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."CIF_TRG_INS_DESIMP" BEFORE INSERT ON DESIMP
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_INS_DESIMP
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza diversas validaciones sobre los campos de la tabla DESIMP
Tablas Involucradas..
	   DESIMP

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_moneda   EXCEPTION;
  v_contador         NUMBER(9):=0;
BEGIN
   IF INSERTING THEN
      -- Busco la moneda en el tabla MONEDAS de la tabla DESCOD, en BCT
      v_contador := 0;
      SELECT NVL(COUNT(1),0) INTO v_contador
	  FROM   DESCOD
	  WHERE  COD_TABLA = 'MONEDAS'
	  AND    COD_GENERICO = :NEW.COD_MONEDA_IMP
	  AND    COD_REG_STATUS = 0;
      -- Si NO existe la MONEDA en la tabla, esta es invalida. ERROR
      IF v_contador = 0 THEN
         RAISE e_invalid_moneda;
      END IF;

   	  -- Ajuste de Deflactores = 1 si Moneda_Impreso <> 0 (Pesos)
      IF :NEW.COD_MONEDA_IMP <> 0 THEN
         :NEW.VAL_DEFLAC_BASE := 1;
      END IF;

   END IF;
--------------
EXCEPTION
WHEN e_invalid_moneda   THEN
     raise_application_error(-20100,'Error: La moneda ingresada no es válida en el sistema.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Insertar el Desglose Impresion.');
END CIF_TRG_INS_DESIMP;



/
ALTER TRIGGER "DGL_ADMIN"."CIF_TRG_INS_DESIMP" ENABLE;
--------------------------------------------------------
--  Constraints for Table DESIMP
--------------------------------------------------------

  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("COD_PLANTILLA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("COR_IMPRESO" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("FEC_DEFLAC_BASE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("FEC_CREACION" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("VAL_DEFLAC_BASE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("COD_MONEDA_IMP" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("COD_MONEDA" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("COD_SUBTIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("COD_TIPO_DES" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" MODIFY ("COD_CLIENTE" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."DESIMP" ADD CONSTRAINT "PK_DESIMP" PRIMARY KEY ("COD_CLIENTE", "COD_TIPO_DES", "COD_SUBTIPO_DES", "COD_MONEDA", "COD_MONEDA_IMP", "COR_IMPRESO")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
