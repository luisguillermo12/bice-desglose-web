/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla CONCEP
*
* ALTER TABLE CONCEP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
* COMMENT ON COLUMN CONCEP.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';
*/
/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla CONSOL del esquema CIF_ADMIN
*/
ALTER TABLE CONSOL ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN CONSOL.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla PERIOD del esquema CIF_ADMIN
*/
ALTER TABLE PERIOD ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN PERIOD.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla PRCCNS del esquema CIF_ADMIN
*/
ALTER TABLE PRCCNS ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN PRCCNS.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla VALPRD del esquema CIF_ADMIN
*/
ALTER TABLE VALPRD ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN VALPRD.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla VALIMP del esquema CIF_ADMIN
*/
ALTER TABLE VALIMP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN VALIMP.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla PRDIMP del esquema CIF_ADMIN
*/
ALTER TABLE PRDIMP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN PRDIMP.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla DESGLO del esquema CIF_ADMIN
*/
ALTER TABLE DESGLO ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';


