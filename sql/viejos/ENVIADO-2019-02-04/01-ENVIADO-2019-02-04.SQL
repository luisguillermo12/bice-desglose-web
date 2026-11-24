/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla DESGLO
*/
ALTER TABLE DESGLO_REV ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE CONIMP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE PRCCNS ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE VALIMP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE VALPRD ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE PRDIMP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE PERIOD ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE DESIMP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

ALTER TABLE CONSOL ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';
