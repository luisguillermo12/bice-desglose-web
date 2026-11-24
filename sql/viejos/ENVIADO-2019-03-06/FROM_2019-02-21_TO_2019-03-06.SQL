/**
 * Aumentando el tamaño de la columna COD_TIPO_PROCESO en la tabla DGL_CHK_LOG.
 */
ALTER TABLE DGL_CHK_LOG MODIFY (COD_TIPO_PROCESO VARCHAR2(30 BYTE) );

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

/**
 * Se aumenta el tamaño de la columna.
 */
ALTER TABLE DESGLO_AUDIT MODIFY (GLS_ROL VARCHAR2(30 BYTE) );

/**
 * Se adiciona la columna COD_PLANTILLA a la tabla.
 */
ALTER TABLE DESGLO_MSG ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO_MSG.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';
