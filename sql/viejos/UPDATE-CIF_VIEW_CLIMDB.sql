-- Se agregan los campos COD_PLANTILLA del desglose y su glosa GLS_TIPO_PLANTILLA_DES
CREATE OR REPLACE VIEW CIF_VIEW_CLIMDB
AS SELECT DES.COD_CLIENTE,
          NLS_INITCAP (
               NVL (
                  DECODE (
                     EMP.GLS_RAZON_SOCI,
                     NULL, DECODE (
                                 PER.NOM_NOMBRES
                              || ' '
                              || PER.NOM_PATERNO
                              || ' '
                              || PER.NOM_MATERNO,
                              '  ', UPPER (
                                       SIN.NOM_CLIENTE
                                       || DECODE (
                                                SIN.NOM_PATERNO
                                             || ' '
                                             || SIN.NOM_MATERNO,
                                             ' ', NULL,
                                                SIN.NOM_PATERNO
                                             || ' '
                                             || SIN.NOM_MATERNO)),
                              UPPER (
                                    PER.NOM_NOMBRES
                                 || ' '
                                 || PER.NOM_PATERNO
                                 || ' '
                                 || PER.NOM_MATERNO)),
                     UPPER (EMP.GLS_RAZON_SOCI)),
                  '-----')) NOM_CLIENTE,
          DECODE(EMP.NUM_CLIENTE, NULL, DECODE(SIN.COD_CLIENTE, NULL, 0, DECODE (SIN.NOM_PATERNO || SIN.NOM_MATERNO, NULL, 1, 0)), 1) IS_EMPRESA,
          DECODE (EMP.NUM_CLIENTE || PER.NUM_CLIENTE, NULL, 0, 1) HAS_RUT,
          NVL(DECODE(BAS.COD_SECTOR_ECONOM, NULL, SIN.COD_ACTIVIDAD, BAS.COD_SECTOR_ECONOM), 0) COD_ACTIVIDAD,
          DES.COD_TIPO_DES,
          DES.COD_SUBTIPO_DES,
          DES.GLS_SUBTIPO_DES,
          DES.COD_MONEDA,
          DES.COD_MONEDA_IMP,
          DES.COD_UNID_MONEDA COD_UNIDMON,
          DES.VAL_DEFLAC_BASE,
          DES.FEC_DEFLAC_BASE,
          DES.VAL_IVA,
          DES.GLS_OBSERVACION,
          DES.NOM_USER_CREA USR_USER_CREA,
          DES.FEC_USER_CREA,
          DES.NOM_USER_MODIF USR_USER_MODIF,
          DES.FEC_USER_MODIF,
          DES.NOM_USER_OPEN USR_USER_OPEN,
          --DECODE(DES.NOM_USER_MODIF,NULL,DES.NOM_USER_CREA,DES.NOM_USER_MODIF) USR_LAST_UPDATE,
          DES.NOM_USER USR_LAST_UPDATE,
          DECODE (DES.FEC_USER_MODIF, NULL, DES.FEC_USER_CREA, DES.FEC_USER_MODIF) FEC_LAST_UPDATE,
          DES.NOM_USER_ANALIS USR_USER_ANALIS,
          DES.ROL_EMPLEADO_DES_DEF,
          DES.NOM_REVISOR_DEF,
          DES.FEC_DESGLO_DEF,
          DES.COD_ESTADO,
          DSC.GLS_GENERICALARGA GLS_ESTADO,
          DES.COD_PLANTILLA,
          DSC1.GLS_GENERICALARGA GLS_TIPO_PLANTILLA_DES
     FROM DESGLO DES
     LEFT JOIN IDECLI IDE ON (IDE.NUM_IDEN = DES.COD_CLIENTE AND IDE.COD_REG_STATUS = 0)
     LEFT JOIN SINRUT SIN ON (SIN.COD_CLIENTE = DES.COD_CLIENTE)
     LEFT JOIN BASCLI BAS ON (BAS.NUM_CLIENTE = IDE.NUM_CLIENTE AND BAS.COD_REG_STATUS = 0)
     LEFT JOIN CLIJUR EMP ON (EMP.NUM_CLIENTE = IDE.NUM_CLIENTE AND EMP.COD_REG_STATUS = 0)
     LEFT JOIN CLINAT PER ON (PER.NUM_CLIENTE = IDE.NUM_CLIENTE AND PER.COD_REG_STATUS = 0)
     LEFT JOIN DESCOD DSC ON (DSC.COD_TABLA = 'DGLCONFG' AND DSC.COD_GENERICO LIKE 'ESTADOS-%' AND DSC.COD_REG_STATUS = 0 AND DSC.GLS_GENERICACORTA = DES.COD_ESTADO)
     LEFT JOIN DESCOD DSC1 ON (DSC1.COD_TABLA = 'TIPPLANT' AND DSC1.COD_REG_STATUS = 0 AND DSC1.COD_GENERICO = DES.COD_PLANTILLA)
    WHERE DES.FLG_IS_TEMPORAL = 0
    ORDER BY FEC_LAST_UPDATE DESC;
