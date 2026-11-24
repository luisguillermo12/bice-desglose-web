--------------------------------------------------------
--  File created - Friday-January-25-2019   
--------------------------------------------------------
-- Updates:
--		* Adicionando el campo tipo de plantilla de desglose a las tablas DGL_CHK_LOG
--------------------------------------------------------
ALTER TABLE DGL_CHK_LOG ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DGL_CHK_LOG.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo base banco.';


