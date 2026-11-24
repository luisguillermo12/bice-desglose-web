INSERT INTO "TURBINE_ROLE" (ROLE_ID, ROLE_NAME) VALUES ('27005', 'Plat_Desglose_Administrador');
-- de pruebas para un usuario
INSERT INTO "TURBINE_USER_GROUP_ROLE" (USER_ID, GROUP_ID, ROLE_ID) VALUES ('2202', '1', '27005');

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

-- ALTER TABLE DESGLO_REV RENAME COLUMN COD_TIPO_PLANTILLA_DES TO COD_PLANTILLA;

-----------------------------------------------------------------------------------
-- Adicionando el campo tipo de plantilla de desglose a la tabla DESGLO_AUDIT
-----------------------------------------------------------------------------------
ALTER TABLE DESGLO_AUDIT
ADD (COD_PLANTILLA NUMBER(5) DEFAULT 0 NOT NULL);
COMMENT ON COLUMN DESGLO_AUDIT.COD_PLANTILLA IS 'Código del tipo de planilla. Por defecto 0 que es tipo banco.';
