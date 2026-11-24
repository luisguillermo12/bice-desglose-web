SPOOL 1102_BCT_ADMIN_ORAPLA_ALTER_TABLE_CONCEP.LOG;

--------------------------------------------------------
--  File created - Wednesday-June-12-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table BCT_ADMIN.CONCEP
--------------------------------------------------------

-- Se adiciona las columnas para fórmulas a utilizar en el sistema web.
ALTER TABLE BCT_ADMIN.CONCEP ADD (GLS_FORMULA_VAL_WEB VARCHAR2(255) );
ALTER TABLE BCT_ADMIN.CONCEP ADD (GLS_FORMULA_PRC_WEB VARCHAR2(255) );
ALTER TABLE BCT_ADMIN.CONCEP ADD (GLS_FORMULA_DEF_WEB VARCHAR2(255) );
COMMENT ON COLUMN BCT_ADMIN.CONCEP.GLS_FORMULA_VAL_WEB IS 'Fórmula de evaluación de concepto para el sistema web.';
COMMENT ON COLUMN BCT_ADMIN.CONCEP.GLS_FORMULA_PRC_WEB IS 'Fórmula de evaluación totales porcentuales para el sistema web.';
COMMENT ON COLUMN BCT_ADMIN.CONCEP.GLS_FORMULA_DEF_WEB IS 'Fórmuala conceptual de deflactación para el sistema web.';

SPOOL OFF;  