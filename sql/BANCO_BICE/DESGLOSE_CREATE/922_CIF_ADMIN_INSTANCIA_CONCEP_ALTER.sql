/**
*  RollBack Adicionando el campo tipo de plantilla de desglose a la tabla CONCEP
*/
ALTER TABLE CONCEP DROP COLUMN COD_PLANTILLA;
/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla CONSOL del esquema CIF_ADMIN
*/
ALTER TABLE CONSOL DROP COLUMN COD_PLANTILLA;
/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla PERIOD del esquema CIF_ADMIN
*/
ALTER TABLE PERIOD DROP COLUMN COD_PLANTILLA;
/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla PRCCNS del esquema CIF_ADMIN
*/
ALTER TABLE PRCCNS DROP COLUMN COD_PLANTILLA;
/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla VALPRD del esquema CIF_ADMIN
*/
ALTER TABLE VALPRD DROP COLUMN COD_PLANTILLA;
/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla VALIMP del esquema CIF_ADMIN
*/
ALTER TABLE VALIMP DROP COLUMN COD_PLANTILLA;
/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla PRDIMP del esquema CIF_ADMIN
*/
ALTER TABLE PRDIMP DROP COLUMN COD_PLANTILLA;

/**
*   Adicionando el campo tipo de plantilla de desglose a la tabla DESGLO del esquema CIF_ADMIN
*/
ALTER TABLE DESGLO DROP COLUMN COD_PLANTILLA;


/**
 * Agregando los tipos de concepto PROY_DM, PROY_DM_EST_RESULT, PROY_DM_FLUJO_CAJA y PROY_DM_BALAN_GRAL.
 */
ALTER TABLE CONCEP DROP CONSTRAINT TIPCON_00_CHK;
ALTER TABLE CONCEP ADD CONSTRAINT TIPCON_00_CHK
    CHECK (COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN','DEUDA_SBIF')) ENABLE;


/**
 * Se adiciona la columna UNIDAD para la entrada de datos en proyecciones.
 */
ALTER TABLE CONCEP DROP COLUMN UNIDAD; 


-- Nuevas columnas para las validaciones de conceptos en los desgloses
ALTER TABLE CONCEP DROP COLUMN EXP_VALIDACION_VAL ;
ALTER TABLE CONCEP DROP COLUMN EXP_VALIDACION_PRC ;
ALTER TABLE CONCEP DROP COLUMN EXP_VALIDACION_DEF ;
ALTER TABLE CONCEP DROP COLUMN MSG_ERR_VALIDACION_VAL;
ALTER TABLE CONCEP DROP COLUMN MSG_ERR_VALIDACION_PRC ;
ALTER TABLE CONCEP DROP COLUMN MSG_ERR_VALIDACION_DEF ;