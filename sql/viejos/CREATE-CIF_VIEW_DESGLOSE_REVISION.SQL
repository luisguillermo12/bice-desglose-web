CREATE OR REPLACE VIEW CIF_VIEW_DESGLOSE_REVISION
AS
SELECT          GLS_NOMBRE_ANALISTA,
                 COD_CLIENTE,
                 COD_CLIENTE_FMT,
                 COD_TIPO_DES,
                 GLS_TIPO_DES,
                 COD_SUBTIPO_DES,
                 GLS_SUBTIPO_DES,
                 COD_MONEDA,
                 GLS_MONEDA,
                 GLS_NOMBRE_CLIENTE,
                 GLS_ESTADO,
                 FEC_ENVIO_REVISION,
                 FEC_DESGLO_DEF,
                 COD_ESTADO,
                 GLS_NOTIFICACION,
                 NOM_REVISOR_DEF,
                 ROL_EMPLEADO_DEF_ENV_PDF,
                 FEC_USER_MODIF,
                 FLG_TIP_CLIENTE,
                 COD_ACTIVIDAD,
                 NUM_DEVOL_EJECUTOR,
                 cod_plantilla,
                 COD_REG_STATUS,
                 GLS_GENERICALARGA
            FROM (SELECT DISTINCT DATOS.GLS_NOMBRE_ANALISTA,
                        DATOS.COD_CLIENTE,
                        DATOS.COD_CLIENTE_FMT,
                        DATOS.COD_TIPO_DES,
                        DATOS.GLS_TIPO_DES,
                        DATOS.COD_SUBTIPO_DES,
                        DATOS.GLS_SUBTIPO_DES,
                        DATOS.COD_MONEDA,
                        DATOS.GLS_MONEDA,
                        DATOS.GLS_NOMBRE_CLIENTE,
                        DATOS.GLS_ESTADO,
                        NVL(DATOS.FEC_ENVIO_REVISION,(SELECT MAX(FEC_ENVIO_REVISION) FROM DESGLO_REV X WHERE X.COD_CLIENTE = DATOS.COD_CLIENTE AND X.COD_TIPO_DES = DATOS.COD_TIPO_DES AND X.COD_SUBTIPO_DES = DATOS.COD_SUBTIPO_DES AND X.COD_MONEDA = DATOS.COD_MONEDA)) FEC_ENVIO_REVISION,
                        DATOS.FEC_DESGLO_DEF,
                        DATOS.COD_ESTADO,
                        DATOS.GLS_NOTIFICACION,
                        DATOS.NOM_REVISOR_DEF,
                        DATOS.ROL_EMPLEADO_DEF_ENV_PDF,
                        DATOS.FEC_USER_MODIF,
                        FLG_TIP_CLIENTE,
                        COD_ACTIVIDAD,
                        NUM_DEVOL_EJECUTOR,
                        DATOS.COD_PLANTILLA,
                        DATOS.COD_REG_STATUS,
                        GLS_GENERICALARGA
                   FROM (SELECT DATOS.GLS_NOMBRE_ANALISTA,
                                DATOS.COD_CLIENTE,
                                DECODE(CLI.NUM_IDEN,NULL,DATOS.COD_CLIENTE,DATOS.COD_CLIENTE_FMT) COD_CLIENTE_FMT,
                                DATOS.COD_TIPO_DES,
                                 DATOS.GLS_TIPO_DES,
                                DATOS.COD_SUBTIPO_DES,
                                 DATOS.GLS_SUBTIPO_DES,
                                 DATOS.COD_MONEDA,
                                 DATOS.GLS_MONEDA,
                                 DECODE(CLI.NUM_IDEN,NULL,
                                                     NLS_INITCAP(SRUT.NOM_CLIENTE),
                                                     INITCAP(TRIM (NAT.NOM_PATERNO)
                                                     || DECODE (TRIM (NAT.NOM_PATERNO), '', '', ' ')
                                                     || TRIM (NAT.NOM_MATERNO)
                                                     || DECODE (TRIM (NAT.NOM_MATERNO), '', '', ' ')
                                                     || TRIM (JUR.GLS_RAZON_SOCI)
                                                     || DECODE (TRIM (JUR.GLS_RAZON_SOCI), '', '', ' ')
                                                     || TRIM (NAT.NOM_NOMBRES))) GLS_NOMBRE_CLIENTE,
                                 DATOS.GLS_ESTADO,
                                 DATOS.FEC_ENVIO_REVISION,
                                 DATOS.FEC_DESGLO_DEF,
                                 DATOS.COD_ESTADO,
                                 DATOS.GLS_NOTIFICACION,
                                 DATOS.NOM_REVISOR_DEF,
                                 DATOS.ROL_EMPLEADO_DEF_ENV_PDF,
                                 DATOS.FEC_USER_MODIF,
                                 DECODE(CLI.NUM_IDEN,NULL,'SR','CR') FLG_TIP_CLIENTE,
                                 NVL(DECODE(BAS.COD_SECTOR_ECONOM, NULL, SRUT.COD_ACTIVIDAD, BAS.COD_SECTOR_ECONOM), 0) COD_ACTIVIDAD,
                                NUM_DEVOL_EJECUTOR ,
                                DATOS.COD_PLANTILLA,
                                DATOS.COD_REG_STATUS
                            FROM (SELECT DECODE(TURBINE_USER.FIRST_NAME,NULL,NVL(DESGLO.NOM_USER_MODIF,NOM_USER_CREA),NLS_INITCAP(TURBINE_USER.FIRST_NAME || ' ' || TURBINE_USER.LAST_NAME)) GLS_NOMBRE_ANALISTA,
                                         DESGLO.COD_CLIENTE,
                                         REPLACE(TO_CHAR(SUBSTR(DESGLO.COD_CLIENTE,1,LENGTH(TRIM(DESGLO.COD_CLIENTE))-1),'999,999,999,999'),',','.') || '-' || SUBSTR(DESGLO.COD_CLIENTE,LENGTH(DESGLO.COD_CLIENTE)) COD_CLIENTE_FMT,
                                         DESGLO.COD_TIPO_DES,
                                         NVL(DECODE(DESGLO.COD_TIPO_DES,0,'Individual',1,'Consolidado'),'---') GLS_TIPO_DES,
                                         DESGLO.COD_SUBTIPO_DES,
                                         NVL(DECODE(DESGLO.COD_SUBTIPO_DES,6,DESGLO.GLS_SUBTIPO_DES,7,DESGLO.GLS_SUBTIPO_DES,DESCOD_SUBTIPODES.GLS_GENERICALARGA),'---') GLS_SUBTIPO_DES,
                                         DESGLO.COD_MONEDA,
                                         NVL(DESCOD_MONEDAS.GLS_GENERICALARGA,'---') GLS_MONEDA,
                                         DESCOD_ESTADOS.GLS_GENERICALARGA GLS_ESTADO,
                                         FEC_ENVIO_REVISION,
                                         FEC_DESGLO_DEF,
                                         COD_ESTADO,
                                         NULL GLS_NOTIFICACION,
                                         NOM_REVISOR_DEF,
                                         ROL_EMPLEADO_DEF_ENV_PDF,
                                         FEC_USER_MODIF,
                                        NVL(NUM_DEVOL_EJECUTOR,0) NUM_DEVOL_EJECUTOR,
                                        DESGLO.COD_PLANTILLA,
                                        DESGLO_REV.COD_REG_STATUS
                                   FROM DESGLO
                                        LEFT JOIN DESGLO_REV ON (DESGLO_REV.COD_CLIENTE = DESGLO.COD_CLIENTE AND DESGLO_REV.COD_TIPO_DES = DESGLO.COD_TIPO_DES AND DESGLO_REV.COD_SUBTIPO_DES = DESGLO.COD_SUBTIPO_DES AND DESGLO_REV.COD_MONEDA = DESGLO.COD_MONEDA AND DESGLO_REV.COD_REG_STATUS = 0)
                                        LEFT JOIN TURBINE_USER ON (TURBINE_USER.LOGIN_NAME = NVL(DESGLO.NOM_USER_MODIF,NOM_USER_CREA))
                                        LEFT JOIN SF_STF_OPERATOR ON (SF_STF_OPERATOR.LOGIN_NAME = TURBINE_USER.LOGIN_NAME)
                                        INNER JOIN DESCOD DESCOD_SUBTIPODES ON (DESCOD_SUBTIPODES.COD_REG_STATUS= 0 AND DESCOD_SUBTIPODES.COD_TABLA = 'SBTDES' AND TRIM(DESCOD_SUBTIPODES.COD_GENERICO)=TRIM(DESGLO.COD_SUBTIPO_DES))
                                        INNER JOIN DESCOD DESCOD_MONEDAS ON (DESCOD_MONEDAS.COD_REG_STATUS = 0 AND DESCOD_MONEDAS.COD_TABLA = 'MONEDAS' AND TRIM(DESCOD_MONEDAS.COD_GENERICO) = TRIM(DESGLO.COD_MONEDA))
                                        INNER JOIN DESCOD DESCOD_ESTADOS ON (DESCOD_ESTADOS.COD_REG_STATUS = 0 AND DESCOD_ESTADOS.COD_TABLA = 'DGLCONFG' AND DESCOD_ESTADOS.COD_GENERICO LIKE 'ESTADOS-%' AND DESCOD_ESTADOS.GLS_GENERICACORTA = DESGLO.COD_ESTADO)
                                   WHERE 1 = 1
                                   ) DATOS
                                    LEFT JOIN IDECLI CLI ON (CLI.NUM_IDEN = DATOS.COD_CLIENTE AND CLI.COD_REG_STATUS = 0)
                                    LEFT JOIN BASCLI BAS ON (BAS.NUM_CLIENTE = CLI.NUM_CLIENTE AND BAS.COD_REG_STATUS = 0)
                                    LEFT JOIN CLINAT NAT ON (NAT.NUM_CLIENTE = CLI.NUM_CLIENTE AND NAT.COD_REG_STATUS = 0)
                                    LEFT JOIN CLIJUR JUR ON (JUR.NUM_CLIENTE = CLI.NUM_CLIENTE AND JUR.COD_REG_STATUS = 0)
                                    LEFT JOIN SINRUT SRUT ON (SRUT.COD_CLIENTE = DATOS.COD_CLIENTE)
                            ) DATOS
                            LEFT JOIN DESGLO_REV REV ON (REV.COD_CLIENTE = DATOS.COD_CLIENTE AND REV.COD_TIPO_DES = DATOS.COD_TIPO_DES AND REV.COD_SUBTIPO_DES = DATOS.COD_SUBTIPO_DES AND REV.COD_MONEDA = DATOS.COD_MONEDA AND REV.COD_REG_STATUS = 1)
                            INNER JOIN DESCOD DESCO ON (DATOS.COD_PLANTILLA = DESCO.COD_GENERICO AND DESCO.COD_TABLA = 'TIPPLANT')
                            ORDER BY FEC_USER_MODIF DESC NULLS LAST, FEC_DESGLO_DEF DESC )
                            WHERE 1=1;
