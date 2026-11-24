--------------------------------------------------------
--  File created - Tuesday-March-12-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for View BCT_VIEW_DESCOD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "BCT_VIEW_DESCOD" ("COD_GENERICO", "COD_TABLA", "GLS_DESCRIPCION", "GLS_GENERICACORTA", "GLS_GENERICALARGA") AS 
  SELECT DESCOD.COD_GENERICO,
            DESCOD.COD_TABLA,
            NVL (TABLAS.GLS_DESCRIPCION, ' ') GLS_DESCRIPCION,
            NVL (DESCOD.GLS_GENERICACORTA, ' ') GLS_GENERICACORTA,
            NVL (DESCOD.GLS_GENERICALARGA, ' ') GLS_GENERICALARGA
       FROM DESCOD, TABLAS
      WHERE     DESCOD.COD_REG_STATUS = 0
            AND TABLAS.COD_TABLA(+) = DESCOD.COD_TABLA
            AND TABLAS.COD_REG_STATUS(+) = 0
   ORDER BY GLS_GENERICACORTA
 
;

