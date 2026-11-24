--------------------------------------------------------
--  File created - Monday-March-11-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table CUADRE
--------------------------------------------------------

  CREATE TABLE "DGL_ADMIN"."CUADRE" 
   (	"COD_TPO_CONCEP1" VARCHAR2(20 BYTE), 
	"COR_CONCEPTO1" NUMBER(6,0), 
	"COD_TPO_CONCEP2" VARCHAR2(20 BYTE), 
	"COR_CONCEPTO2" NUMBER(6,0), 
	"GLS_MENSAJE" VARCHAR2(255 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 0 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;
--------------------------------------------------------
--  DDL for Index PK_CUADRE
--------------------------------------------------------

  CREATE UNIQUE INDEX "DGL_ADMIN"."PK_CUADRE" ON "DGL_ADMIN"."CUADRE" ("COD_TPO_CONCEP1", "COR_CONCEPTO1", "COD_TPO_CONCEP2", "COR_CONCEPTO2") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Trigger BCT_TRG_INS_CUADRE
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DGL_ADMIN"."BCT_TRG_INS_CUADRE" BEFORE INSERT ON DGL_ADMIN.CUADRE REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
/***********************************************************************************
------ DATOS DE CREACION ---
Nombre Logico     :CIF_TRG_INS_CUADRE
Fecha             :03/12/2005
Autor             :Rodrigo González Kirkman
Descripcion       :Realiza diversas validaciones sobre los campos de la tabla CUADRE
Tablas Involucradas..
     CUADRE

------ DATOS ULTIMA MODIFICACION
Fecha       :
Responsable :
***********************************************************************************/
DECLARE
  e_invalid_concep  EXCEPTION;
  v_contador1       NUMBER(9):=0;
  v_contador2       NUMBER(9):=0;
BEGIN
   IF INSERTING THEN
      SELECT NVL(COUNT(1),0) INTO v_contador1
      FROM   CONCEP
      WHERE  COD_TIPO_CONCEP = :NEW.COD_TPO_CONCEP1
      AND    COR_CONCEPTO = :NEW.COR_CONCEPTO1
      AND    FLG_CON_ACTIVO = 1;

      SELECT NVL(COUNT(1),0) INTO v_contador2
      FROM   CONCEP
      WHERE  COD_TIPO_CONCEP = :NEW.COD_TPO_CONCEP2
      AND    COR_CONCEPTO = :NEW.COR_CONCEPTO2
      AND    FLG_CON_ACTIVO = 1;

      IF v_contador1 = 0 OR v_contador2 = 0 THEN
         RAISE e_invalid_concep;
      END IF;
   END IF;
--------------
EXCEPTION
WHEN e_invalid_concep THEN
     raise_application_error(-20100,'Error: Los conceptos deben estar Activos.');
WHEN others THEN
     raise_application_error(-1,'Error: No se puede Insertar la Empresa Consolidadora.');
END BCT_TRG_INS_CUADRE;

/
ALTER TRIGGER "DGL_ADMIN"."BCT_TRG_INS_CUADRE" ENABLE;
--------------------------------------------------------
--  Constraints for Table CUADRE
--------------------------------------------------------

  ALTER TABLE "DGL_ADMIN"."CUADRE" ADD CONSTRAINT "PK_CUADRE" PRIMARY KEY ("COD_TPO_CONCEP1", "COR_CONCEPTO1", "COD_TPO_CONCEP2", "COR_CONCEPTO2")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
  ALTER TABLE "DGL_ADMIN"."CUADRE" MODIFY ("COR_CONCEPTO2" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."CUADRE" MODIFY ("COD_TPO_CONCEP2" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."CUADRE" MODIFY ("COR_CONCEPTO1" NOT NULL ENABLE);
  ALTER TABLE "DGL_ADMIN"."CUADRE" MODIFY ("COD_TPO_CONCEP1" NOT NULL ENABLE);
