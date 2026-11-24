ALTER TABLE CONCEP ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN CONCEP.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';

/**
 * Agregando los tipos de concepto PROY_DM, PROY_DM_EST_RESULT, PROY_DM_FLUJO_CAJA y PROY_DM_BALAN_GRAL.
 */
ALTER TABLE CONCEP DROP CONSTRAINT TIPCON_00_CHK;
ALTER TABLE CONCEP ADD CONSTRAINT TIPCON_00_CHK
    CHECK (COD_TIPO_CONCEP IN ('BALANCE','EST_RESULT','GEN_FONDOS','IND_FINAN','DEUDA_SBIF','PROY_DM','PROY_DM_EST_RESULT','PROY_DM_FLUJO_CAJA','PROY_DM_BALAN_GRAL')) ENABLE;


/**
 * Se adiciona la columna UNIDAD para la entrada de datos en proyecciones.
 */
ALTER TABLE CONCEP ADD (UNIDAD VARCHAR2(80) );
COMMENT ON COLUMN CONCEP.UNIDAD IS 'Unidad para los datos del modelo de proyecciones.';



-- Nuevas columnas para las validaciones de conceptos en los desgloses
ALTER TABLE CONCEP ADD (EXP_VALIDACION_VAL VARCHAR2(255) );
ALTER TABLE CONCEP ADD (EXP_VALIDACION_PRC VARCHAR2(255) );
ALTER TABLE CONCEP ADD (EXP_VALIDACION_DEF VARCHAR2(255) );
ALTER TABLE CONCEP ADD (MSG_ERR_VALIDACION_VAL VARCHAR2(255) );
ALTER TABLE CONCEP ADD (MSG_ERR_VALIDACION_PRC VARCHAR2(255) );
ALTER TABLE CONCEP ADD (MSG_ERR_VALIDACION_DEF VARCHAR2(255) );

COMMENT ON COLUMN CONCEP.EXP_VALIDACION_VAL IS 'Expresión de validación para el valor del concepto.';
COMMENT ON COLUMN CONCEP.EXP_VALIDACION_PRC IS 'Expresión de validación para el valor de la fórmula del porciento del concepto.';
COMMENT ON COLUMN CONCEP.EXP_VALIDACION_DEF IS 'Expresión de validación para el valor de la fórmula del deflactor del concepto.';
COMMENT ON COLUMN CONCEP.MSG_ERR_VALIDACION_VAL IS 'Mensaje de validación en caso de error en la validación del valor del concepto.';
COMMENT ON COLUMN CONCEP.MSG_ERR_VALIDACION_PRC IS 'Mensaje de validación en caso de error en la validación del valor de la fórmula del porciento del concepto.';
COMMENT ON COLUMN CONCEP.MSG_ERR_VALIDACION_DEF IS 'Mensaje de validación en caso de error en la validación del valor de la fórmula del deflactor del concepto.';

