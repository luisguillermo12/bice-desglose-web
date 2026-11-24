package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.bctdesglose.domain.Concepto;
import cl.tutorial.desglose.conexiones.bctdesglose.repository.ConceptoRepository;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.DesgloseCifRepository;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.SQLCIFRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.*;
import cl.tutorial.desglose.domain.pk.DesgloseMensajePK;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import cl.tutorial.desglose.repository.*;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.dto.*;
import cl.tutorial.desglose.service.mapper.DesgloseCifMapper;
import cl.tutorial.desglose.service.mapper.DesgloseMapper;
import cl.tutorial.desglose.service.mapper.DesgloseMensajeMapper;
import cl.tutorial.desglose.service.mapper.DesgloseRevisionMapper;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.service.util.ScriptEngineParser;
import cl.tutorial.desglose.service.util.Utils;
import cl.tutorial.desglose.web.rest.errors.*;
import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing Desglose.
 */
@Service
@Transactional
public class DesgloseService {

    private static final String ENTITY_NAME = "desglose";

    private final Logger log = LogManager.getLogger(DesgloseService.class);

    private static final String SQL_CONSULTA_COD_CLIENTE_START      = " AND UPPER(DESGLO.COD_CLIENTE) = UPPER('";
    private static final String SQL_CONSULTA_COD_CLIENTE_END        = "') ";
    private static final String SQL_CONSULTA_FECHA_CREACION_1       = " AND TRUNC(NVL(DESGLO.FEC_USER_CREA,TO_DATE('19000101','YYYYMMDD'))) BETWEEN TO_DATE('";
    private static final String SQL_CONSULTA_FECHA_CREACION_2       = "','YYYYMMDD') AND TO_DATE('";
    private static final String SQL_CONSULTA_FECHA_CREACION_3       = "','YYYYMMDD') ";
    private static final String SQL_CONSULTA_FECHA_MODIFICACION_1   = " AND TRUNC(NVL(DESGLO.FEC_USER_MODIF,TO_DATE('19000101','YYYYMMDD'))) BETWEEN TRUNC(TO_DATE('";
    private static final String SQL_CONSULTA_FECHA_MODIFICACION_2   = "','YYYYMMDD')) AND TRUNC(TO_DATE('";
    private static final String SQL_CONSULTA_FECHA_MODIFICACION_3   = "','YYYYMMDD')) ";
    private static final String SQL_CONSULTA_COD_PLANTILLA          = " AND DESGLO.COD_PLANTILLA = ";
    private static final String SQL_CONSULTA_COD_TIPO_DES_START     = " AND DESGLO.COD_TIPO_DES = '";
    private static final String SQL_CONSULTA_COD_TIPO_DES_END       = "' ";
    private static final String SQL_CONSULTA_COD_SUB_TIPO_DES_START = " AND DESGLO.COD_SUBTIPO_DES = '";
    private static final String SQL_CONSULTA_COD_SUB_TIPO_DES_END   = "' ";
    private static final String SQL_CONSULTA_COD_MONEDA_START       = " AND DESGLO.COD_MONEDA = '";
    private static final String SQL_CONSULTA_COD_MONEDA_END         = "' ";
    private static final String SQL_CONSULTA_COD_ESTADO_START       = " AND DESGLO.COD_ESTADO = '";
    private static final String SQL_CONSULTA_COD_ESTADO_END         = "' ";
    private static final String SQL_CONSULTA_NOM_USER_CREA_START    = " AND UPPER(TRIM(DESGLO.NOM_USER_ANALIS)) = UPPER('";
    private static final String SQL_CONSULTA_NOM_USER_CREA_END      = "') ";
    private static final String SQL_CONSULTA_NOM_USER_MODIF_START   = " AND UPPER(TRIM(DESGLO.NOM_USER_MODIF)) = UPPER('";
    private static final String SQL_CONSULTA_NOM_USER_MODIF_END     = "') ";
    private static final String SQL_CONSULTA_NOMBRE_START           = "  AND UPPER(GLS_COMPLETO) LIKE UPPER('%";
    private static final String SQL_CONSULTA_NOMBRE_END             = "%') ";
    private static final String SQL_CONSULTA_ROWNUM                 = " AND ROWNUM <= ";
    private static final String SQL_CONSULTA_ORDER_BY               = " ORDER BY FEC_USER_MODIF DESC NULLS LAST, FEC_DESGLO_DEF DESC ";

    private static final String SQL_QUERY_FILTRO_CONSULTA_START_3 = "     LEFT JOIN TURBINE_USER USR_ANALIS ON (USR_ANALIS.LOGIN_NAME = NOM_USER_ANALIS) \n"
        + "            LEFT JOIN TURBINE_USER USR_CREA ON (USR_CREA.LOGIN_NAME = DATOS.NOM_USER_CREA) \n"
        + "            LEFT JOIN TURBINE_USER USR_OPEN ON (USR_OPEN.LOGIN_NAME = DATOS.USER_OPEN) \n"
        + "            LEFT JOIN TURBINE_USER USR_MODIF ON (USR_MODIF.LOGIN_NAME = DATOS.NOM_USER_MODIF) \n"
        + "            LEFT JOIN SF_STF_OPERATOR ON (SF_STF_OPERATOR.OPERATOR_CODE = TRIM(DATOS.ROL_EMPLEADO_REV_MOD)) \n"
        + "            LEFT JOIN TURBINE_USER USR_REV_MOD ON (USR_REV_MOD.LOGIN_NAME = SF_STF_OPERATOR.LOGIN_NAME) \n"
        + "           INNER JOIN DESCOD SUBTIPO ON (SUBTIPO.COD_REG_STATUS = 0 AND SUBTIPO.COD_TABLA = 'SBTDES' AND SUBTIPO.COD_GENERICO = DATOS.COD_SUBTIPO_DES) \n"
        + "           INNER JOIN DESCOD MONEDAS ON (MONEDAS.COD_REG_STATUS = 0 AND MONEDAS.COD_TABLA = 'MONEDAS' AND MONEDAS.COD_GENERICO = DATOS.COD_MONEDA) \n"
        + "           INNER JOIN DESCOD PLANTILLA ON (PLANTILLA.COD_REG_STATUS = 0 AND PLANTILLA.COD_TABLA = 'TIPPLANT' AND PLANTILLA.COD_GENERICO = DATOS.COD_PLANTILLA) \n"
        + "            LEFT JOIN DESCOD ESTADOS ON (ESTADOS.COD_REG_STATUS = 0 AND ESTADOS.COD_TABLA = 'DGLCONFG' AND ESTADOS.COD_GENERICO LIKE 'ESTADOS-%' AND TRIM(ESTADOS"
        + ".GLS_GENERICACORTA) = TRIM(DATOS.COD_ESTADO)) \n" + "            LEFT JOIN IDECLI CLI ON (CLI.NUM_IDEN = DATOS.COD_CLIENTE AND CLI.COD_REG_STATUS = 0) \n"
        + "            LEFT JOIN BASCLI BAS ON (BAS.NUM_CLIENTE = CLI.NUM_CLIENTE AND BAS.COD_REG_STATUS = 0) \n"
        + "            LEFT JOIN CLINAT NAT ON (NAT.NUM_CLIENTE = CLI.NUM_CLIENTE AND NAT.COD_REG_STATUS = 0) \n"
        + "            LEFT JOIN CLIJUR JUR ON (JUR.NUM_CLIENTE = CLI.NUM_CLIENTE AND JUR.COD_REG_STATUS = 0) \n"
        + "            LEFT JOIN SINRUT SRUT ON (SRUT.COD_CLIENTE = DATOS.COD_CLIENTE) \n" + "           WHERE 1 = 1 \n"
        + "           ORDER BY FEC_USER_MODIF DESC NULLS LAST, FEC_DESGLO_DEF DESC ) \n" + "           WHERE 1 = 1  ";

    private static final String SQL_QUERY_FILTRO_CONSULTA_START_2 = "   ) DATOS ";

    private static final String SQL_QUERY_FILTRO_CONSULTA_START_1 =
        "SELECT GLS_NOMBRE_ANALIS, COD_CLIENTE_FMT, GLS_COMPLETO, GLS_PLANTILLA, GLS_TIPO_DESGLOSE, GLS_SUBTIPO_DESGLOSE, GLS_MONEDA_DESGLOSE,\n"
            + "    GLS_ESTADO_DESGLOSE, FEC_USER_CREA, NOM_USER_CREA, GLS_NOMBRE_CREA, FEC_USER_MODIF, NOM_USER_MODIF, GLS_NOMBRE_MODIF,\n"
            + "    FEC_DESGLO_DEF, NOM_REVISOR_DEF, GLS_NOMBRE_REV_MODIF, COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_ESTADO,\n"
            + "    COD_ACTIVIDAD, FLG_TIP_CLIENTE, COD_PLANTILLA, COD_UNID_MONEDA, NOM_USER_ANALIS, USER_OPEN, GLS_NOMBRE_USER_OPEN, GLS_OBSERVACION, \n"
            + "( SELECT GLS_GENERICALARGA FROM DESCOD\n" + "WHERE COD_TABLA = 'SUBSECON' AND COD_REG_STATUS = 0 AND COD_GENERICO = ( SELECT COD_GENERICO1 FROM RELTAB\n"
            + " WHERE COD_TABLA1 = 'SUBSECON' AND COD_TABLA2 = 'CLAECONO'\n" + "AND COD_GENERICO2 = COD_ACTIVIDAD AND COD_REG_STATUS = 0)) GLS_SECTOR\n"
            + " FROM ( SELECT\n"
            + "            DECODE(USR_ANALIS.FIRST_NAME, NULL, NVL(NOM_USER_MODIF, NOM_USER_CREA), NLS_INITCAP(USR_ANALIS.FIRST_NAME\n"
            + "                || ' ' || USR_ANALIS.LAST_NAME) ) GLS_NOMBRE_ANALIS, DECODE(CLI.NUM_IDEN, NULL, DATOS.COD_CLIENTE, \n"
            + "                DATOS.COD_CLIENTE_FMT) COD_CLIENTE_FMT,\n"
            + "            DECODE(CLI.NUM_IDEN, NULL, NLS_INITCAP(SRUT.NOM_CLIENTE) || ' ' || NLS_INITCAP(SRUT.NOM_PATERNO) || ' '\n"
            + "                || NLS_INITCAP(SRUT.NOM_MATERNO),INITCAP(TRIM(NAT.NOM_PATERNO) || DECODE(TRIM(NAT.NOM_PATERNO),'','',' ')\n"
            + "                || TRIM(NAT.NOM_MATERNO) || DECODE(TRIM(NAT.NOM_MATERNO), '', '', ' ') || TRIM(JUR.GLS_RAZON_SOCI)\n"
            + "                || DECODE(TRIM(JUR.GLS_RAZON_SOCI), '', '', ' ') || TRIM(NAT.NOM_NOMBRES))) GLS_COMPLETO,\n"
            + "            DECODE(COD_TIPO_DES, 0, 'Individual', 'Consolidado') GLS_TIPO_DESGLOSE,\n"
            + "            NVL(DECODE(DATOS.COD_SUBTIPO_DES, 6, DATOS.GLS_SUBTIPO_DES, 7, DATOS.GLS_SUBTIPO_DES, SUBTIPO.GLS_GENERICALARGA), \n"
            + "                '---') GLS_SUBTIPO_DESGLOSE,\n"
            + "            MONEDAS.GLS_GENERICALARGA GLS_MONEDA_DESGLOSE, NLS_INITCAP(ESTADOS.GLS_GENERICALARGA) GLS_ESTADO_DESGLOSE,\n"
            + "            DATOS.FEC_USER_CREA, DATOS.NOM_USER_CREA, NLS_INITCAP(USR_CREA.FIRST_NAME || ' ' || USR_CREA.LAST_NAME) GLS_NOMBRE_CREA,\n"
            + "            DATOS.FEC_USER_MODIF, DATOS.NOM_USER_MODIF, NLS_INITCAP(USR_MODIF.FIRST_NAME || ' ' || USR_MODIF.LAST_NAME) GLS_NOMBRE_MODIF,\n"
            + "            DATOS.FEC_DESGLO_DEF, NLS_INITCAP(DATOS.NOM_REVISOR_DEF) NOM_REVISOR_DEF, NLS_INITCAP(USR_REV_MOD.FIRST_NAME || ' ' || USR_REV_MOD.LAST_NAME) "
            + "            GLS_NOMBRE_REV_MODIF, DATOS.COD_CLIENTE, DATOS.COD_TIPO_DES, DATOS.COD_SUBTIPO_DES, DATOS.COD_MONEDA, DATOS.COD_ESTADO,\n"
            + "            NVL(DECODE(BAS.COD_SECTOR_ECONOM,NULL,SRUT.COD_ACTIVIDAD,BAS.COD_SECTOR_ECONOM),0) COD_ACTIVIDAD,\n"
            + "            DECODE(CLI.NUM_IDEN, NULL, 'SR', 'CR') FLG_TIP_CLIENTE, DATOS.COD_PLANTILLA, PLANTILLA.GLS_GENERICALARGA GLS_PLANTILLA, DATOS.COD_UNID_MONEDA,"
            + "            DATOS.NOM_USER_ANALIS, DATOS.USER_OPEN, NLS_INITCAP(USR_OPEN.FIRST_NAME || ' ' || USR_OPEN.LAST_NAME) GLS_NOMBRE_USER_OPEN, GLS_OBSERVACION\n"
            + "        FROM ( SELECT\n"
            + "                     TRIM(REPLACE(TO_CHAR(SUBSTR(DESGLO.COD_CLIENTE, 1, LENGTH(TRIM(DESGLO.COD_CLIENTE) ) - 1), '999,999,999,999,999'),\n"
            + "                        ',', '.') || '-' || SUBSTR(DESGLO.COD_CLIENTE,LENGTH(DESGLO.COD_CLIENTE) )) COD_CLIENTE_FMT,\n"
            + "                     DESGLO.FEC_USER_CREA, DESGLO.NOM_USER_CREA, DESGLO.FEC_USER_MODIF, DESGLO.NOM_USER_MODIF,\n"
            + "                     DESGLO.FEC_DESGLO_DEF, NLS_INITCAP(DESGLO.NOM_REVISOR_DEF) NOM_REVISOR_DEF, DESGLO.COD_CLIENTE,\n"
            + "                     DESGLO.COD_TIPO_DES, DESGLO.COD_SUBTIPO_DES,\n" + "                     DESGLO.COD_MONEDA, DESGLO.COD_ESTADO, DESGLO.COD_PLANTILLA, \n"
            + "                     DESGLO.NOM_USER_ANALIS, DESGLO.ROL_EMPLEADO_REV_MOD, DESGLO.GLS_SUBTIPO_DES, DESGLO.COD_UNID_MONEDA,"
            + "                     DESGLO.NOM_USER_OPEN USER_OPEN, GLS_OBSERVACION\n" + "                 FROM\n" + "                     DESGLO_WK DESGLO \n"
            + "                 WHERE\n" + "                     1 = 1  AND flg_is_temporal = 0 ";

    private static final String SQL_QUERY_DETERMINA_QUIENES_REEMPLAZO_START =
        "SELECT NUM_ROL_EMPLEADO" + " FROM DESGLO_REE_WK REE" + " WHERE COD_REG_STATUS = 0" + " AND REE.DES_NUM_ROL_EMPLEADO IN ('";

    private static final String SQL_QUERY_DETERMINA_QUIENES_REEMPLAZO_END = "')";

    private static final String SQL_QUERY_OBTIENE_2FIRMA = "SELECT NUM_ROL_EMPLEADO_2FIR" + " FROM DESGLO_FIR_WK" + " WHERE NUM_ROL_EMPLEADO =:numRolEmpleado";

    private static final String SQL_QUERY_OBTIENE_PRIMERA_FIRMA =
        " SELECT NUM_ROL_EMPLEADO_2FIR, LOGIN_NAME" + " FROM DESGLO_FIR_WK " + " INNER JOIN SF_STF_OPERATOR ON (OPERATOR_CODE = NUM_ROL_EMPLEADO_2FIR) "
            + " WHERE NUM_ROL_EMPLEADO =:numRolEmpleado ";

    private static final String SQL_QUERY_VALIDA_REEMPLAZO_2FIRMA =
        " SELECT NVL(COUNT(*),0) CNT FROM DESGLO_REE_WK REE \n" + " INNER JOIN DESGLO_FIR_WK FIR ON (REE.NUM_ROL_EMPLEADO = FIR.NUM_ROL_EMPLEADO_2FIR)\n"
            + " WHERE REE.COD_REG_STATUS = 0 AND REE.DES_COD_CARGO = 'CARGO-002' AND REE.DES_NUM_ROL_EMPLEADO =:CODEOPERATOR";

    private static final String SQL_QUERY_FILTRO_REVISION_START_1 =
        "SELECT GLS_NOMBRE_ANALISTA," + " COD_CLIENTE," + " COD_CLIENTE_FMT," + " COD_PLANTILLA," + " GLS_COD_PLANTILLA," + " COD_TIPO_DES," + " GLS_TIPO_DES,"
            + " COD_SUBTIPO_DES," + " GLS_SUBTIPO_DES," + " COD_MONEDA," + " GLS_MONEDA," + " GLS_NOMBRE_CLIENTE," + " GLS_ESTADO," + " FEC_ENVIO_REVISION, " + " FEC_DESGLO_DEF,"
            + " COD_ESTADO," + " GLS_NOTIFICACION," + " NOM_REVISOR_DEF," + " ROL_EMPLEADO_DEF_ENV_PDF," + " FEC_USER_MODIF," + " FLG_TIP_CLIENTE," + " COD_ACTIVIDAD,"
            + " NUM_DEVOL_EJECUTOR,COD_UNID_MONEDA,VAL_IVA" + " FROM (SELECT DISTINCT DATOS.GLS_NOMBRE_ANALISTA," + " DATOS.COD_CLIENTE," + " DATOS.COD_CLIENTE_FMT," + " DATOS"
            + ".COD_PLANTILLA," + " DATOS.GLS_COD_PLANTILLA," + " DATOS.COD_TIPO_DES," + " DATOS.GLS_TIPO_DES," + " DATOS.COD_SUBTIPO_DES," + " DATOS.GLS_SUBTIPO_DES,"
            + " DATOS.GLS_MONEDA," + " DATOS.GLS_NOMBRE_CLIENTE," + " DATOS.COD_MONEDA," + " DATOS.GLS_ESTADO,"
            + " NVL(DATOS.FEC_ENVIO_REVISION,(SELECT MAX(FEC_ENVIO_REVISION) FROM DESGLO_REV_WK X WHERE X.COD_CLIENTE = DATOS.COD_CLIENTE AND X.COD_TIPO_DES = DATOS.COD_TIPO_DES "
            + "AND X.COD_SUBTIPO_DES = DATOS.COD_SUBTIPO_DES AND X.COD_MONEDA = DATOS.COD_MONEDA)) FEC_ENVIO_REVISION," + " DATOS.FEC_DESGLO_DEF," + " DATOS.COD_ESTADO,"
            + " DATOS.GLS_NOTIFICACION," + " DATOS.NOM_REVISOR_DEF," + " DATOS.ROL_EMPLEADO_DEF_ENV_PDF," + " DATOS.FEC_USER_MODIF," + " FLG_TIP_CLIENTE," + " COD_ACTIVIDAD,"
            + " NUM_DEVOL_EJECUTOR,DATOS.COD_UNID_MONEDA,DATOS.VAL_IVA " + " FROM (SELECT DATOS.GLS_NOMBRE_ANALISTA," + "  DATOS.COD_CLIENTE,"
            + "  DECODE(CLI.NUM_IDEN,NULL,DATOS.COD_CLIENTE,DATOS.COD_CLIENTE_FMT) COD_CLIENTE_FMT," + "  DATOS.cod_plantilla," + "  datos.gls_cod_plantilla,"
            + "  DATOS.COD_TIPO_DES," + "  DATOS.GLS_TIPO_DES," + "  DATOS.COD_SUBTIPO_DES," + "  DATOS.GLS_SUBTIPO_DES," + "  DATOS.COD_MONEDA," + "  DATOS.GLS_MONEDA,"
            + "  DECODE(CLI.NUM_IDEN,NULL," + "  NLS_INITCAP(SRUT.NOM_CLIENTE)," + "  INITCAP(TRIM (NAT.NOM_PATERNO)" + "  || DECODE (TRIM (NAT.NOM_PATERNO), '', '', ' ')"
            + "  || TRIM (NAT.NOM_MATERNO)" + "  || DECODE (TRIM (NAT.NOM_MATERNO), '', '', ' ')" + "  || TRIM (JUR.GLS_RAZON_SOCI)"
            + "  || DECODE (TRIM (JUR.GLS_RAZON_SOCI), '', '', ' ') " + "  || TRIM (NAT.NOM_NOMBRES))) GLS_NOMBRE_CLIENTE," + "  DATOS.GLS_ESTADO, "
            + "  DATOS.FEC_ENVIO_REVISION, " + "  DATOS.FEC_DESGLO_DEF, " + "  DATOS.COD_ESTADO, " + "  DATOS.GLS_NOTIFICACION, " + "  DATOS.NOM_REVISOR_DEF, "
            + "  DATOS.ROL_EMPLEADO_DEF_ENV_PDF," + "  DATOS.FEC_USER_MODIF, " + "  DECODE(CLI.NUM_IDEN,NULL,'SR','CR') FLG_TIP_CLIENTE, "
            + "  NVL(DECODE(BAS.COD_SECTOR_ECONOM, NULL, SRUT.COD_ACTIVIDAD, BAS.COD_SECTOR_ECONOM), 0) COD_ACTIVIDAD," + "  NUM_DEVOL_EJECUTOR,DATOS.COD_UNID_MONEDA,"
            + "DATOS.VAL_IVA " + "FROM (SELECT DECODE(TURBINE_USER.FIRST_NAME,NULL,NVL(DESGLO_WK.NOM_USER_MODIF,NOM_USER_CREA),"
            + "  NLS_INITCAP(TURBINE_USER.FIRST_NAME || ' ' || TURBINE_USER.LAST_NAME)) GLS_NOMBRE_ANALISTA," + "  DESGLO_WK.COD_CLIENTE,"
            + "  TRIM(REPLACE(TO_CHAR(SUBSTR(DESGLO_WK.COD_CLIENTE,1,LENGTH(TRIM(DESGLO_WK.COD_CLIENTE))-1),'999,999,999,999,999'),',','.') || '-' || SUBSTR(DESGLO_WK"
            + ".COD_CLIENTE,LENGTH"
            + "(DESGLO_WK" + ".COD_CLIENTE))) COD_CLIENTE_FMT," + "  DESGLO_WK.cod_plantilla cod_plantilla," + "  descod_codPlantilla.gls_genericalarga gls_cod_plantilla,"
            + "  DESGLO_WK" + ".COD_TIPO_DES," + "  NVL(DECODE(DESGLO_WK.COD_TIPO_DES,0,'Individual',1,'Consolidado'),'---') GLS_TIPO_DES," + "  DESGLO_WK.COD_SUBTIPO_DES,"
            + "  NVL(DECODE(DESGLO_WK.COD_SUBTIPO_DES,6,DESGLO_WK.GLS_SUBTIPO_DES,7,DESGLO_WK.GLS_SUBTIPO_DES,DESCOD_SUBTIPODES.GLS_GENERICALARGA),'---') GLS_SUBTIPO_DES, \n"
            + "  DESGLO_WK.COD_MONEDA," + "  NVL(DESCOD_MONEDAS.GLS_GENERICALARGA,'---') GLS_MONEDA," + "  DESCOD_ESTADOS.GLS_GENERICALARGA GLS_ESTADO," + "  FEC_ENVIO_REVISION,"
            + "  FEC_DESGLO_DEF," + "  COD_ESTADO," + "  NULL GLS_NOTIFICACION," + "  NOM_REVISOR_DEF," + "  ROL_EMPLEADO_DEF_ENV_PDF," + "  FEC_USER_MODIF,"
            + "  NVL(NUM_DEVOL_EJECUTOR,0) NUM_DEVOL_EJECUTOR," + " COD_UNID_MONEDA,VAL_IVA " + "  FROM DESGLO_WK"
            + " LEFT JOIN DESGLO_REV_WK ON (DESGLO_REV_WK.COD_CLIENTE = DESGLO_WK.COD_CLIENTE AND DESGLO_REV_WK.cod_plantilla = DESGLO_WK.cod_plantilla AND DESGLO_REV_WK"
            + ".COD_TIPO_DES = " + "DESGLO_WK.COD_TIPO_DES AND "
            + " DESGLO_REV_WK.COD_SUBTIPO_DES = DESGLO_WK.COD_SUBTIPO_DES AND DESGLO_REV_WK.COD_MONEDA = DESGLO_WK.COD_MONEDA AND DESGLO_REV_WK" + ".COD_REG_STATUS = ";

    private static final String SQL_QUERY_FILTRO_REVISION_START_2 = "  ) LEFT JOIN TURBINE_USER ON (TURBINE_USER.LOGIN_NAME = NVL(DESGLO_WK" + ".NOM_USER_MODIF,NOM_USER_CREA)) \n"
        + "                                   LEFT JOIN SF_STF_OPERATOR ON (SF_STF_OPERATOR.LOGIN_NAME = TURBINE_USER.LOGIN_NAME) INNER JOIN descod descod_codPlantilla ON ( "
        + "descod_codPlantilla.cod_reg_status = 0\n" + "                                                                     AND descod_codPlantilla.cod_tabla = 'TIPPLANT'\n"
        + "                                                                     AND TRIM(descod_codPlantilla.cod_generico) = TRIM(desglo_wk.cod_plantilla\n"
        + "                                                                     ) )\n"
        + "                                   INNER JOIN DESCOD DESCOD_SUBTIPODES ON (DESCOD_SUBTIPODES.COD_REG_STATUS= 0 AND DESCOD_SUBTIPODES.COD_TABLA = 'SBTDES' AND TRIM"
        + "(DESCOD_SUBTIPODES.COD_GENERICO)=TRIM(DESGLO_WK.COD_SUBTIPO_DES))\n"
        + "                                   INNER JOIN DESCOD DESCOD_MONEDAS ON (DESCOD_MONEDAS.COD_REG_STATUS = 0 AND DESCOD_MONEDAS.COD_TABLA = 'MONEDAS' AND TRIM"
        + "(DESCOD_MONEDAS.COD_GENERICO) = TRIM(DESGLO_WK.COD_MONEDA)) \n"
        + "                                   INNER JOIN DESCOD DESCOD_ESTADOS ON (DESCOD_ESTADOS.COD_REG_STATUS = 0 AND DESCOD_ESTADOS.COD_TABLA = 'DGLCONFG' AND DESCOD_ESTADOS"
        + ".COD_GENERICO LIKE 'ESTADOS-%' AND DESCOD_ESTADOS.GLS_GENERICACORTA = DESGLO_WK.COD_ESTADO) \n" + "                                   WHERE 1 = 1 ";

    private static final String SQL_QUERY_FILTRO_REVISION_END =
        " ) datos left\n" + "JOIN idecli cli ON ( cli.num_iden = datos.cod_cliente\n" + "                     AND cli.cod_reg_status = 0 )\n"
            + "LEFT JOIN bascli bas ON ( bas.num_cliente = cli.num_cliente\n" + "                          AND bas.cod_reg_status = 0 )\n"
            + "LEFT JOIN clinat nat ON ( nat.num_cliente = cli.num_cliente\n" + "                          AND nat.cod_reg_status = 0 )\n"
            + "LEFT JOIN clijur jur ON ( jur.num_cliente = cli.num_cliente\n" + "                          AND jur.cod_reg_status = 0 )\n"
            + "LEFT JOIN sinrut srut ON ( srut.cod_cliente = datos.cod_cliente ) ) datos\n" + "                          ORDER BY fec_user_modif DESC NULLS LAST,\n"
            + "fec_desglo_def DESC ) WHERE 1=1";

    private static final String SQL_FILTRO_ESTADODESGLO_START = " AND COD_ESTADO = '";
    private static final String SQL_FILTRO_ESTADODESGLO_END   = "'";

    private static final String SQL_FILTRO_ANALISTADESGLO_START = " AND UPPER(GLS_NOMBRE_ANALISTA) LIKE UPPER('%";
    private static final String SQL_FILTRO_ANALISTADESGLO_END   = "%')";

    private static final String SQL_FILTRO_TIPO_PLANTILLADESGLO            = " AND cod_plantilla = ";
    private static final String SQL_FILTRO_TIPODESGLO                      = " AND COD_TIPO_DES = ";
    private static final String SQL_FILTRO_SUBTIPODESGLO                   = " AND COD_SUBTIPO_DES = ";
    private static final String SQL_FILTRO_MONEDADESGLO                    = " AND COD_MONEDA = ";
    private static final String SQL_FILTRO_FECHAENVIOREVISIONDESGLOSTART   = " AND TRUNC(NVL(FEC_ENVIO_REVISION,TO_DATE('19000101','YYYY-MM-DD'))) BETWEEN TO_DATE('";
    private static final String SQL_FILTRO_FECHAENVIOREVISIONDESGLOSTARTM  = "','YYYY-MM-DD') AND TO_DATE('";
    private static final String SQL_FILTRO_FECHAENVIOREVISIONDESGLOSEND    = "','YYYY-MM-DD')";
    private static final String SQL_FILTRO_FECHAINICIOREVISIONDESGLOSTART  = " AND TRUNC(NVL(FEC_DESGLO_DEF,TO_DATE('19000101','YYYY-MM-DD'))) BETWEEN TO_DATE('";
    private static final String SQL_FILTRO_FECHAINICIOREVISIONDESGLOSTARTM = "','YYYY-MM-DD') AND TO_DATE('";
    private static final String SQL_FILTRO_FECHAINICIOREVISIONDESGLOEND    = "','YYYY-MM-DD')";
    private static final String SQL_FILTRO_RUTDESGLO_START                 = " AND COD_CLIENTE = '";
    private static final String SQL_FILTRO_RUTDESGLO_END                   = "'";
    private static final String SQL_FILTRO_NOMBREDESGLO_START              = " AND UPPER(GLS_NOMBRE_CLIENTE) LIKE UPPER('%";
    private static final String SQL_FILTRO_NOMBREDESGLO_END                = "%')";
    private static final String SQL_QUERY_FILTRO_DESGLOSE                  =
        "SELECT DISTINCT CLIDES.COD_CLIENTE, CLIDES.NOM_CLIENTE, CLIDES.IS_EMPRESA, CLIDES.HAS_RUT, CLIDES.COD_ACTIVIDAD,\n" + "     ( SELECT COD_GENERICO1\n" + "         FROM "
            + "RELTAB\n" + "         WHERE COD_TABLA1 = 'SUBSECON' AND COD_TABLA2 = 'CLAECONO'\n" + "             AND COD_GENERICO2 = CLIDES.COD_ACTIVIDAD AND COD_REG_STATUS = 0\n"
            + "     ) COD_SECTOR,\n" + "     ( SELECT GLS_GENERICALARGA\n" + "         FROM DESCOD\n"
            + "         WHERE COD_TABLA = 'SUBSECON' AND COD_REG_STATUS = 0 AND COD_GENERICO = \n" + "            ( SELECT COD_GENERICO1\n" + "                 FROM RELTAB\n"
            + "                 WHERE COD_TABLA1 = 'SUBSECON' AND COD_TABLA2 = 'CLAECONO'\n"
            + "                     AND COD_GENERICO2 = CLIDES.COD_ACTIVIDAD AND COD_REG_STATUS = 0\n" + "             )\n" + "     ) GLS_SECTOR, CLIDES.COD_TIPO_DES,\n"
            + "     NVL(DECODE(CLIDES.COD_TIPO_DES,0,'Individual',1,'Consolidado'),'-----') GLS_TIPO_DES,\n" + "     CLIDES.COD_SUBTIPO_DES,\n"
            + "     NVL(DECODE(CLIDES.COD_SUBTIPO_DES,6,CLIDES.GLS_SUBTIPO_DES,7,CLIDES.GLS_SUBTIPO_DES, \n" + "        ( SELECT GLS_GENERICALARGA\n" + "            FROM DESCOD\n"
            + "            WHERE COD_TABLA = 'SBTDES' AND COD_REG_STATUS = 0 AND COD_GENERICO = CLIDES.COD_SUBTIPO_DES\n" + "        ) ),'-----') GLS_SUBTIPO_DES,\n"
            + "     CLIDES.COD_MONEDA,\n" + "     NVL( ( SELECT GLS_GENERICALARGA FROM DESCOD\n"
            + "         WHERE COD_TABLA = 'MONEDAS' AND COD_REG_STATUS = 0 AND COD_GENERICO = CLIDES.COD_MONEDA),'-----') GLS_MONEDA,\n" + "     CLIDES.COD_MONEDA_IMP,\n"
            + "     NVL( ( SELECT  GLS_GENERICALARGA FROM DESCOD WHERE COD_TABLA = 'MONEDAS' \n"
            + "        AND COD_REG_STATUS = 0 AND COD_GENERICO = CLIDES.COD_MONEDA_IMP),'-----') GLS_MONEDA_IMP,\n" + "     CLIDES.COD_UNIDMON,\n"
            + "     NVL( ( SELECT GLS_GENERICALARGA FROM DESCOD WHERE COD_TABLA = 'UNIDMON'\n"
            + "             AND COD_REG_STATUS = 0 AND COD_GENERICO = CLIDES.COD_UNIDMON),'-----') GLS_UNIDMON,\n"
            + "     CLIDES.FEC_DEFLAC_BASE   FEC_DESGLO, CLIDES.FEC_LAST_UPDATE,\n" + "     CLIDES.USR_USER_ANALIS   USR_ANALISTA,\n"
            + "     NLS_INITCAP(NVL(DECODE(USR.FIRST_NAME || ' ' || USR.LAST_NAME,' ',NULL,UPPER(USR.FIRST_NAME || ' ' || USR.LAST_NAME) ),'-----') ) NOM_ANALISTA,\n"
            + "     CLIDES.COD_ESTADO, CLIDES.GLS_ESTADO, CLIDES.COD_PLANTILLA, CLIDES.GLS_TIPO_PLANTILLA_DES \n" + " FROM\n DGL_VIEW_CLIMDB_WK CLIDES, TURBINE_USER USR \n"
            + " WHERE USR.LOGIN_NAME (+) = CLIDES.USR_USER_ANALIS ";

    private static final String SQL_FILTRO_RUT_START                 = " AND CLIDES.COD_CLIENTE = '";
    private static final String SQL_FILTRO_RUT_END                   = "'";
    private static final String SQL_FILTRO_NOMBRE_START              = " AND UPPER(CLIDES.NOM_CLIENTE) LIKE UPPER('%";
    private static final String SQL_FILTRO_NOMBRE_END                = "%')";
    private static final String SQL_FILTRO_TIPO_PLANTILLA_DESGLOSE   = "AND CLIDES.COD_PLANTILLA = ";
    private static final String SQL_FILTRO_TIPO_DESGLOSE             = " AND    CLIDES.COD_TIPO_DES = ";
    private static final String SQL_FILTRO_SUB_TIPO_DESGLOSE_START   = " AND   (CLIDES.COD_SUBTIPO_DES = ";
    private static final String SQL_FILTRO_SUB_TIPO_DESGLOSE_MIDDLE  = " OR UPPER(CLIDES.GLS_SUBTIPO_DES) LIKE UPPER('%";
    private static final String SQL_FILTRO_SUB_TIPO_DESGLOSE_END     = "%'))";
    private static final String SQL_FILTRO_MONEDA                    = " AND CLIDES.COD_MONEDA = ";
    private static final String SQL_FILTRO_FECHA_MODIFICACION1_START = " AND TO_DATE(TO_CHAR(CLIDES.FEC_LAST_UPDATE,'DD-MM-YYYY'),'DD-MM-YYYY') >= TO_DATE('";
    private static final String SQL_FILTRO_FECHA_MODIFICACION1_END   = "','DD-MM-YYYY')";
    private static final String SQL_FILTRO_FECHA_MODIFICACION2_START = " AND TO_DATE(TO_CHAR(CLIDES.FEC_LAST_UPDATE,'DD-MM-YYYY'),'DD-MM-YYYY') <= TO_DATE('";
    private static final String SQL_FILTRO_FECHA_MODIFICACION2_END   = "','DD-MM-YYYY')";
    private static final String SQL_FILTRO_FECHA_DESGLOSE1_START     = " AND TO_DATE(TO_CHAR(CLIDES.FEC_DEFLAC_BASE,'DD-MM-YYYY'),'DD-MM-YYYY') >= TO_DATE('";
    private static final String SQL_FILTRO_FECHA_DESGLOSE1_END       = "','DD-MM-YYYY')";
    private static final String SQL_FILTRO_FECHA_DESGLOSE2_START     = " AND TO_DATE(TO_CHAR(CLIDES.FEC_DEFLAC_BASE,'DD-MM-YYYY'),'DD-MM-YYYY') <= TO_DATE('";
    private static final String SQL_FILTRO_FECHA_DESGLOSE2_END       = "','DD-MM-YYYY')";
    private static final String SQL_FILTRO_ANALISTA_START            =
        " AND (DECODE(USR.FIRST_NAME || ' ' || USR.LAST_NAME,' ','',UPPER(USR.FIRST_NAME || ' ' || USR.LAST_NAME)) LIKE UPPER('%";
    private static final String SQL_FILTRO_ANALISTA_MIDDLE           = "%') OR UPPER(CLIDES.USR_USER_ANALIS) LIKE UPPER('%";
    private static final String SQL_FILTRO_ANALISTA_END              = "%'))";
    private static final String SQL_FILTRO_SECTOR_START              = " AND NVL(SEC.COD_GENERICO,'0') = '";
    private static final String SQL_FILTRO_SECTOR_END                = "'";
    private static final String SQL_FILTRO_ESTADO_START              = " AND UPPER(CLIDES.COD_ESTADO) = UPPER('";
    private static final String SQL_FILTRO_ESTADO_END                = "')";
    private static final String SQL_FILTRO_ORDER                     = " AND ROWNUM <= 100 ORDER BY FEC_LAST_UPDATE DESC";

    private static final String SQL_QUERY_FILTRO_USUARIO_MODIFICACION_START = "AND CLIDES.USR_USER_MODIF = '";
    private static final String SQL_QUERY_FILTRO_USUARIO_MODIFICACION_END   = "'";

    private static final String SQL_QUERY_BUSQUEDA_DESGLOSE_WK =
        "SELECT COD_UNID_MONEDA, GLS_SUBTIPO_DES, VAL_DEFLAC_BASE, FEC_DEFLAC_BASE, VAL_IVA, GLS_OBSERVACION, NOM_USER_CREA, NOM_USER_MODIF USER_MODIF, "
            + " NVL(FEC_USER_MODIF,FEC_USER_CREA) FECHA_MODIF, NOM_USER_OPEN USER_OPEN, FLG_IS_TEMPORAL, COD_MONEDA_IMP, NOM_USER, "
            + " FIRST_NAME || ' ' || LAST_NAME GLS_EJECUTOR, NVL(FLG_REQ_2FIRMA,0) FLG_REQ_2FIRMA, NVL(NOM_USER_MODIF,NOM_USER_CREA) USR_EJECUTOR, NOM_USER_OPEN, "
            + " COD_ESTADO, DES.GLS_GENERICALARGA GLS_ESTADO " + "     FROM DESGLO_WK "
            + "         LEFT JOIN DESCOD DES ON (DES.COD_TABLA = 'DGLCONFG' AND DES.COD_GENERICO LIKE 'ESTADOS-%' AND DESGLO_WK.COD_ESTADO = DES.GLS_GENERICACORTA AND "
            + "             DES.COD_REG_STATUS = 0) "
            + "         LEFT JOIN TURBINE_USER ON (LOGIN_NAME = NVL(NOM_USER_MODIF, NOM_USER_CREA)) WHERE COD_CLIENTE = UPPER(?1) AND COD_PLANTILLA = ?2 AND "
            + "         COD_TIPO_DES = ?3 AND COD_SUBTIPO_DES = ?4 AND COD_MONEDA = ?5";
    private static final String SQL_QUERY_BUSQUEDA_DESGLOSE =
        "SELECT COD_UNID_MONEDA, GLS_SUBTIPO_DES, VAL_DEFLAC_BASE, FEC_DEFLAC_BASE, VAL_IVA, GLS_OBSERVACION, NOM_USER_CREA, NOM_USER_MODIF USER_MODIF, "
            + " NVL(FEC_USER_MODIF,FEC_USER_CREA) FECHA_MODIF, NOM_USER_OPEN USER_OPEN, FLG_IS_TEMPORAL, COD_MONEDA_IMP, NOM_USER, "
            + " FIRST_NAME || ' ' || LAST_NAME GLS_EJECUTOR, NVL(FLG_REQ_2FIRMA,0) FLG_REQ_2FIRMA, NVL(NOM_USER_MODIF,NOM_USER_CREA) USR_EJECUTOR, NOM_USER_OPEN, "
            + " COD_ESTADO, DES.GLS_GENERICALARGA GLS_ESTADO " + "     FROM DESGLO "
            + "         LEFT JOIN DESCOD DES ON (DES.COD_TABLA = 'DGLCONFG' AND DES.COD_GENERICO LIKE 'ESTADOS-%' AND DESGLO.COD_ESTADO = DES.GLS_GENERICACORTA AND "
            + "             DES.COD_REG_STATUS = 0) "
            + "         LEFT JOIN TURBINE_USER ON (LOGIN_NAME = NVL(NOM_USER_MODIF, NOM_USER_CREA)) WHERE COD_CLIENTE = UPPER(?1) AND COD_PLANTILLA = ?2 AND "
            + "         COD_TIPO_DES = ?3 AND COD_SUBTIPO_DES = ?4 AND COD_MONEDA = ?5";

    private static final String SQL_QUERY_BUSQUEDA_DESGLOSE_CREADO_SP =
        "SELECT USER_OPEN, FLG_IS_TEMPORAL, COD_ESTADO,  GLS_ESTADO,  NUM_ROL_1FIR, NUM_ROL_2FIR, DES_NUM_ROL_EMPLEADO NUM_ROL_REE, VAL_DEFLAC_BASE, FEC_DEFLAC_BASE,"
            + "flg_req_2firma, GLS_OBSERVACION,NOM_USER_OPEN,VAL_IVA,NOM_USER_ANALIS "
            + " FROM ( select tu.user_id  USER_OPEN, DGL.FLG_IS_TEMPORAL, DGL.COD_ESTADO, DES.GLS_GENERICALARGA GLS_ESTADO, STF.OPERATOR_CODE NUM_ROL_1FIR, "
            + " NVL(FIR.NUM_ROL_EMPLEADO,NVL(GRP.NUM_ROL_EMPLEADO,'-1')) NUM_ROL_2FIR, DGL.VAL_DEFLAC_BASE, DGL.FEC_DEFLAC_BASE,DGL.flg_req_2firma, GLS_OBSERVACION,"
            + "NOM_USER_OPEN,VAL_IVA,NOM_USER_ANALIS"
            + " FROM "
            + "DESGLO_WK DGL \n" + "     LEFT JOIN DESCOD DES ON (DES.COD_TABLA = 'DGLCONFG' AND DES.COD_GENERICO LIKE 'ESTADOS-%' AND DGL.COD_ESTADO = DES.GLS_GENERICACORTA AND "
            + "         DES.COD_REG_STATUS = 0) " + "     LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = NVL(DGL.NOM_USER_MODIF,'ameza')) \n"
            + "     LEFT JOIN DESGLO_FIR_WK FIR ON (FIR.NUM_ROL_EMPLEADO_2FIR = STF.OPERATOR_CODE) \n"
            + "     LEFT JOIN DESGLO_GRUPO_WK GRP ON (GRP.DES_NUM_ROL_EMPLEADO = STF.OPERATOR_CODE) " + "     LEFT JOIN turbine_user tu ON (stf.login_name = tu.login_name) "
            + "    WHERE COD_CLIENTE = UPPER(?1)  AND COD_PLANTILLA = ?2  AND COD_TIPO_DES = ?3 AND COD_SUBTIPO_DES = ?4 AND COD_MONEDA = ?5) INFO "
            + " LEFT JOIN DESGLO_REE_WK ON (INFO.NUM_ROL_2FIR = DESGLO_REE_WK.NUM_ROL_EMPLEADO AND DESGLO_REE_WK.COD_REG_STATUS = 0)";

    private static final String SQL_QUERY_BUSQUEDA_DESGLOSECIF_CREADO_SP =
        "SELECT USER_OPEN, FLG_IS_TEMPORAL, COD_ESTADO,  GLS_ESTADO,  NUM_ROL_1FIR, NUM_ROL_2FIR, DES_NUM_ROL_EMPLEADO NUM_ROL_REE, VAL_DEFLAC_BASE, FEC_DEFLAC_BASE,"
            + "flg_req_2firma, GLS_OBSERVACION,NOM_USER_OPEN,VAL_IVA,NOM_USER_ANALIS "
            + " FROM ( select tu.user_id  USER_OPEN, DGL.FLG_IS_TEMPORAL, DGL.COD_ESTADO, DES.GLS_GENERICALARGA GLS_ESTADO, STF.OPERATOR_CODE NUM_ROL_1FIR, "
            + " NVL(FIR.NUM_ROL_EMPLEADO,NVL(GRP.NUM_ROL_EMPLEADO,'-1')) NUM_ROL_2FIR, DGL.VAL_DEFLAC_BASE, DGL.FEC_DEFLAC_BASE,DGL.flg_req_2firma, GLS_OBSERVACION,"
            + "NOM_USER_OPEN,VAL_IVA,NOM_USER_ANALIS"
            + " FROM "
            + "DESGLO DGL \n" + "     LEFT JOIN DESCOD DES ON (DES.COD_TABLA = 'DGLCONFG' AND DES.COD_GENERICO LIKE 'ESTADOS-%' AND DGL.COD_ESTADO = DES.GLS_GENERICACORTA AND "
            + "         DES.COD_REG_STATUS = 0) " + "     LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = NVL(DGL.NOM_USER_MODIF,'ameza')) \n"
            + "     LEFT JOIN DESGLO_FIR FIR ON (FIR.NUM_ROL_EMPLEADO_2FIR = STF.OPERATOR_CODE) \n"
            + "     LEFT JOIN DESGLO_GRUPO GRP ON (GRP.DES_NUM_ROL_EMPLEADO = STF.OPERATOR_CODE) " + "     LEFT JOIN turbine_user tu ON (stf.login_name = tu.login_name) "
            + "    WHERE COD_CLIENTE = UPPER(?1)  AND COD_PLANTILLA = ?2  AND COD_TIPO_DES = ?3 AND COD_SUBTIPO_DES = ?4 AND COD_MONEDA = ?5) INFO "
            + " LEFT JOIN DESGLO_REE ON (INFO.NUM_ROL_2FIR = DESGLO_REE.NUM_ROL_EMPLEADO AND DESGLO_REE.COD_REG_STATUS = 0)";

    private static final String SQL_QUERY_FILTRO_FECHA_REVISION       = "";
    private static final String SQL_QUERY_FILTRO_FECHA_ENVIO_REVISION = "";

    private static final String SP_CIF_SP_TAKE_DESGLO_ROL                      = "DGL_SP_TAKE_DESGLO_ROL_WK";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_05_P_NOM_USER        = "P_NOM_USER";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_06_P_NUM_ROL         = "P_NUM_ROL";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_07_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_08_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_TAKE_DESGLO_ROL_09_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    private static final String SP_RESECUENCIAR_MULTIREGISTROS                      = "DGL_SP_RESET_MULTIREGISTROS_WK";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_01_P_COD_CLIENTE     = "PI_COD_CLIENTE";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_02_P_COD_TIPO_DES    = "PI_COD_TIPO_DES";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_03_P_COD_SUBTIPO_DES = "PI_COD_SUBTIPO_DES";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_04_P_COD_MONEDA      = "PI_COD_MONEDA";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_05_P_COD_PLANTILLA   = "PI_COD_PLANTILLA";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_06_P_COR_CONCEPTO    = "PI_COR_CONCEPTO";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_07_P_COD_TIPO_CONCEP = "PI_COD_TIPO_CONCEP";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_08_P_RETORNO         = "PI_RETORNO";
    private static final String SP_RESECUENCIAR_MULTIREGISTROS_09_P_GLOSA           = "PI_GLOSA";

    private static final String SQL_LOAD_LISTA_GRAB = "SELECT COD_TIPO_CONCEP, COR_CONCEPTO, 0 COR_VALOR  FROM CONCEP\n"
        + "    WHERE COD_TIPO_CONCEP IN (SELECT GLS_GENERICACORTA FROM DESCOD WHERE COD_TABLA = 'TIPODES' AND COD_REG_STATUS = 0) \n"
        + "        AND cod_tipo_regis <> 'MULTIREGISTRO'\n" + "UNION ALL \n" + "    SELECT A.COD_TIPO_CONCEP, A.COR_CONCEPTO, COR_VALOR  FROM CONCEP A, VALPRD_WK B \n"
        + "        WHERE A.COD_TIPO_CONCEP IN (SELECT GLS_GENERICACORTA FROM DESCOD WHERE COD_TABLA = 'TIPODES' AND COD_REG_STATUS = 0) \n"
        + "             AND A.cod_tipo_regis = 'MULTIREGISTRO' AND A.COD_TIPO_CONCEP = B.COD_TIPO_CONCEP AND A.COR_CONCEPTO = B.COR_CONCEPTO\n"
        + "             AND B.COD_CLIENTE = ?1\n" + "             AND COD_TIPO_DES =  ?2\n" + "             AND COD_SUBTIPO_DES =  ?3\n" + "             AND COD_MONEDA = ?4\n"
        + "             AND to_char(FEC_PERIODO,'YYYYMMDD') = ?5\n";

    private static final String SQL_GET_DESGLOSE_STATUS =
        "SELECT USER_OPEN, FLG_IS_TEMPORAL, COD_ESTADO, GLS_ESTADO, NUM_ROL_1FIR, NUM_ROL_2FIR, " + "DES_NUM_ROL_EMPLEADO NUM_ROL_REE \n" + "    FROM ( \n"
            + "        SELECT DGL.NOM_USER_OPEN USER_OPEN, DGL.FLG_IS_TEMPORAL, DGL.COD_ESTADO, DES.GLS_GENERICALARGA GLS_ESTADO, STF.OPERATOR_CODE NUM_ROL_1FIR, NVL"
            + "(FIR" + ".NUM_ROL_EMPLEADO,NVL(GRP.NUM_ROL_EMPLEADO,'-1')) NUM_ROL_2FIR\n" + "            FROM DESGLO_WK DGL\n"
            + "                INNER JOIN DESCOD DES ON (DES.COD_TABLA = 'DGLCONFG' AND DES.COD_GENERICO LIKE 'ESTADOS-%' AND DGL.COD_ESTADO = DES.GLS_GENERICACORTA AND DES"
            + ".COD_REG_STATUS = 0) \n" + "                LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = NVL(DGL.NOM_USER_MODIF,'ameza')) \n"
            + "                LEFT JOIN DESGLO_FIR_WK FIR ON (FIR.NUM_ROL_EMPLEADO_2FIR = STF.OPERATOR_CODE) \n"
            + "                LEFT JOIN DESGLO_GRUPO_WK GRP ON (GRP.DES_NUM_ROL_EMPLEADO = STF.OPERATOR_CODE) \n"
            + "                WHERE COD_CLIENTE = UPPER(?1) AND COD_TIPO_DES = ?2 AND COD_SUBTIPO_DES = ?3 AND COD_MONEDA = ?4 ) INFO \n"
            + "    LEFT JOIN DESGLO_REE_WK ON (INFO.NUM_ROL_2FIR = DESGLO_REE_WK.NUM_ROL_EMPLEADO AND DESGLO_REE_WK.COD_REG_STATUS = 0) ";


    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL                           = "DGL_SP_INSUPD_DESGLO_ROL_WK";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_01_P_COD_CLIENTE          = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_02_P_COD_TIPO_DES         = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_03_P_COD_SUBTIPO_DES      = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_04_P_COD_MONEDA           = "P_COD_MONEDA";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_05_P_COD_UNID_MONEDA      = "P_COD_UNID_MONEDA";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_06_P_GLS_SUBTIPO_DES      = "P_GLS_SUBTIPO_DES";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_07_P_VAL_DEFLAC_BASE      = "P_VAL_DEFLAC_BASE";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_08_P_FEC_DEFLAC_BASE      = "P_FEC_DEFLAC_BASE";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_09_P_VAL_IVA              = "P_VAL_IVA";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_10_P_GLS_OBSERVACION      = "P_GLS_OBSERVACION";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_11_P_NOM_USER             = "P_NOM_USER";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_12_P_COD_MONEDA_IMP       = "P_COD_MONEDA_IMP";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_13_P_USER                 = "P_USER";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_14_P_FLG_VALIDACION       = "P_FLG_VALIDACION";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_15_P_COD_ESTADO           = "P_COD_ESTADO";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_16_P_ROL_EMPLEADO_REV_MOD = "P_ROL_EMPLEADO_REV_MOD";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_17_P_ROL_EMPLEADO         = "P_ROL_EMPLEADO";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_18_P_RETORNO              = "P_RETORNO";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_19_P_GLOSA                = "P_GLOSA";
    private static final String SP_CIF_SP_INSUPD_DESGLO_ROL_20_P_COD_PLANTILLA        = "P_COD_PLANTILLA";

    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE                           = "DGL_SP_DES_CLONAR_DESGLOSE_WK";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_01_PI_NUM_IDEN            = "PI_NUM_IDEN";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_02_PI_COD_TIPO_DES_ORI    = "PI_COD_TIPO_DES_ORI";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_03_PI_COD_SUBTIPO_DES_ORI = "PI_COD_SUBTIPO_DES_ORI";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_04_PI_COD_MONEDA_ORI      = "PI_COD_MONEDA_ORI";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_05_PI_COD_TIPO_DES_DES    = "PI_COD_TIPO_DES_DES";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_06_PI_COD_SUBTIPO_DES_DES = "PI_COD_SUBTIPO_DES_DES";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_07_PI_COD_MONEDA_DES      = "PI_COD_MONEDA_DES";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_08_PI_LOGIN_NAME          = "PI_LOGIN_NAME";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_09_PI_COD_PLANTILLA       = "PI_COD_PLANTILLA";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_10_PO_RETORNO             = "PO_RETORNO";
    private static final String SP_CIF_SP_DES_CLONAR_DESGLOSE_11_PO_GLOSA               = "PO_GLOSA";

    private static final String SP_CIF_SP_DEL_DESGLOSE                          = "DGL_SP_DEL_DESGLO_WK";
    private static final String SP_CIF_SP_DEL_DESGLOSE_01_PI_COD_CLIENTE        = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_DEL_DESGLOSE_02_PI_COD_TIPO_PLANTILLA = "P_COD_PLANTILLA";
    private static final String SP_CIF_SP_DEL_DESGLOSE_03_PI_COD_TIPO_DES       = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_DEL_DESGLOSE_04_PI_COD_SUBTIPO_DES    = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_DEL_DESGLOSE_05_PI_COD_MONEDA         = "P_COD_MONEDA";
    private static final String SP_CIF_SP_DEL_DESGLOSE_06_PO_RETORNO            = "P_RETORNO";
    private static final String SP_CIF_SP_DEL_DESGLOSE_07_P_GLOSA               = "P_GLOSA";


    private  static  final String SQL_QUERY_REPORTE_IMPRESION  = "SELECT\n" + "    '('\n"
        + "    || UPPER(DECODE(D.COD_SUBTIPO_DES,'6',D.GLS_SUBTIPO_DES,'7',D.GLS_SUBTIPO_DES,ST.GLS_GENERICALARGA) )\n" + "    || ')'\n" + "FROM\n" + "    DESIMP_WK I,\n"
        + "    DESGLO_WK D,\n" + "    DESCOD ST\n" + "WHERE\n" + "    I.COD_PLANTILLA =:codPlantilla\n" + "    AND I.COD_CLIENTE =:codCliente\n"
        + "    AND I.COD_TIPO_DES =:codTipoDes\n" + "    AND I.COD_SUBTIPO_DES =:codSubtipoDes\n" + "    AND I.COD_MONEDA =:codMoneda\n"
        + "    AND I.COD_MONEDA_IMP =:codMonedaImp\n" + "    AND I.COR_IMPRESO =:corImpreso\n" + "    AND D.COD_PLANTILLA = I.COD_PLANTILLA\n"
        + "    AND D.COD_CLIENTE = I.COD_CLIENTE\n" + "    AND D.COD_TIPO_DES = I.COD_TIPO_DES\n" + "    AND D.COD_SUBTIPO_DES = I.COD_SUBTIPO_DES\n"
        + "    AND D.COD_MONEDA = I.COD_MONEDA\n" + "    AND ST.COD_TABLA = 'SBTDES'\n" + "    AND ST.COD_GENERICO = D.COD_SUBTIPO_DES\n" + "    AND ST.COD_REG_STATUS = 0";

    private final DesgloseRepository          desgloseRepository;
    private final DesgloseCifRepository       desgloseCifRepository;
    private final DesgloseRevisionRepository  desgloseRevisionRepository;
    private final DesgloseMensajeRepository   desgloseMensajeRepository;
    private final SQLDGLRepository            SQLDGLRepository;
    private final SQLCIFRepository            sqlcifRepository;
    private final DescripcionCodigoRepository descripcionCodigoRepository;
    private final DesgloseReemplazoRepository desgloseReemplazoRepository;
    private final DesgloseAuditoriaRepository desgloseAuditoriaRepository;
    private final ConceptoRepository          conceptoRepository;
    private final ValPeriodoDesgloseRepository valPeriodoDesgloseRepository;

    private final DesgloseMapper         desgloseMapper;
    private final DesgloseCifMapper      desgloseCifMapper;
    private final DesgloseMensajeMapper  desgloseMensajeMapper;
    private final DesgloseRevisionMapper desgloseRevisionMapper;

    private final DescripcionCodigoService  descripcionCodigoService;
    private final TurbineUserService        turbineUserService;
    private final ValPeriodoDesgloseService valPeriodoDesgloseService;
    private final ValPeriodoProyeccionDesgloseService valPeriodoProyeccionDesgloseService;
    private final PeriodoService            periodoService;
    private final ConceptoImpresionService  conceptoImpresionService;
    private final EmpresaConsolidadaService empresaConsolidadaService;
    private final PeriodoDesgConsolService  periodoDesgConsolService;
    private final SfStfOperatorService      sfStfOperatorService;
    private final DesgloseRevisionService   desgloseRevisionService;
    private final TablaServicioMailService  tablaServicioMailService;
    private final TurbinePermissionService  turbinePermissionService;
    private final LoadDeudaSbifHoyService   loadDeudaSbifHoyService;
    private final ConceptoService           conceptoService;
    private final JHipsterProperties jHipsterProperties;
    public DesgloseService(DesgloseRepository desgloseRepository,
        DesgloseRevisionRepository desgloseRevisionRepository, DesgloseMensajeRepository desgloseMensajeRepository,
        DesgloseReemplazoRepository desgloseReemplazoRepository, DesgloseMapper desgloseMapper, SQLDGLRepository SQLDGLRepository,
        DescripcionCodigoRepository descripcionCodigoRepository, DescripcionCodigoService descripcionCodigoService, TurbineUserService turbineUserService,
        ValPeriodoDesgloseService valPeriodoDesgloseService, PeriodoService periodoService, ConceptoImpresionService conceptoImpresionService,
        EmpresaConsolidadaService empresaConsolidadaService, PeriodoDesgConsolService periodoDesgConsolService, DesgloseAuditoriaRepository desgloseAuditoriaRepository,
        SfStfOperatorService sfStfOperatorService, DesgloseRevisionMapper desgloseRevisionMapper, DesgloseMensajeMapper desgloseMensajeMapper,
        TablaServicioMailService tablaServicioMailService, DesgloseRevisionService desgloseRevisionService, TurbinePermissionService turbinePermissionService,
        ConceptoRepository conceptoRepository,ValPeriodoProyeccionDesgloseService valPeriodoProyeccionDesgloseService,ConceptoService conceptoService,
        LoadDeudaSbifHoyService loadDeudaSbifHoyService,DesgloseCifMapper desgloseCifMapper,JHipsterProperties jHipsterProperties,
                           ValPeriodoDesgloseRepository valPeriodoDesgloseRepository,SQLCIFRepository sqlcifRepository, DesgloseCifRepository desgloseCifRepository) {
        this.desgloseRepository = desgloseRepository;
        this.desgloseCifMapper = desgloseCifMapper;
        this.desgloseRevisionRepository = desgloseRevisionRepository;
        this.desgloseMensajeRepository = desgloseMensajeRepository;
        this.desgloseReemplazoRepository = desgloseReemplazoRepository;
        this.desgloseMapper = desgloseMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.descripcionCodigoRepository = descripcionCodigoRepository;
        this.descripcionCodigoService = descripcionCodigoService;
        this.turbineUserService = turbineUserService;
        this.valPeriodoDesgloseService = valPeriodoDesgloseService;
        this.periodoService = periodoService;
        this.conceptoImpresionService = conceptoImpresionService;
        this.empresaConsolidadaService = empresaConsolidadaService;
        this.periodoDesgConsolService = periodoDesgConsolService;
        this.desgloseAuditoriaRepository = desgloseAuditoriaRepository;
        this.sfStfOperatorService = sfStfOperatorService;
        this.desgloseMensajeMapper = desgloseMensajeMapper;
        this.desgloseRevisionMapper = desgloseRevisionMapper;
        this.tablaServicioMailService = tablaServicioMailService;
        this.desgloseRevisionService = desgloseRevisionService;
        this.turbinePermissionService = turbinePermissionService;
        this.conceptoRepository = conceptoRepository;
        this.loadDeudaSbifHoyService = loadDeudaSbifHoyService;
        this.valPeriodoDesgloseRepository = valPeriodoDesgloseRepository;
        this.conceptoService = conceptoService;
        this.valPeriodoProyeccionDesgloseService =  valPeriodoProyeccionDesgloseService;
        this.desgloseCifRepository = desgloseCifRepository;
        this.jHipsterProperties = jHipsterProperties;
        this.sqlcifRepository = sqlcifRepository;
    }

    /**
     * Llena los parámeteos del procedimiento almacenado para tomar un desglose.
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubTipoDes
     * @param codMoneda
     * @param codTipoPlantilla
     * @param userLogin
     * @param userAuthority
     *
     * @return
     */
    private List<Param<?>> fillExecuteCifSpTakeDesgloRolParams(@NotNull String codCliente, @NotNull Integer codTipoDes, @NotNull Integer codSubTipoDes, @NotNull String codMoneda,
        @NotNull Integer codTipoPlantilla, @NotNull String userLogin, @NotNull Long userAuthority) {
        Param<String> p1 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_03_P_COD_SUBTIPO_DES, Integer.class, codSubTipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<String> p5 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_05_P_NOM_USER, String.class, userLogin, ParameterMode.IN);
        Param<Long> p6 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_06_P_NUM_ROL, Long.class, userAuthority, ParameterMode.IN);
        Param<Integer> p7 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_07_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p8 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_08_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p9 = new Param<>(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_09_P_COD_PLANTILLA, Integer.class, codTipoPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
        return parameters;
    }
    public  String getGlosaImpresion(String codCliente,Integer codPlantilla,Integer codTipoDesglose,Integer codSubtipoDesglose,String codMoneda,String codMonedaImp,
        Integer corImpreso){
        String glosa = "";
        log.info("getGlosaImpresion() - INICIO");
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("codPlantilla",codPlantilla);
        params.put("codCliente",codCliente);
        params.put("codTipoDes",codTipoDesglose);
        params.put("codSubtipoDes",codSubtipoDesglose);
        params.put("codMoneda",codMoneda);
        params.put("codMonedaImp",codMonedaImp);
        params.put("corImpreso",corImpreso);
        List<Object[]> result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_QUERY_REPORTE_IMPRESION, params);
        log.debug("QUERY: " + this.SQL_QUERY_REPORTE_IMPRESION );
        log.debug("PARAMETROS: " + params.toString() );
        log.info("getGlosaImpresion() - FIN");
        if(result.size()> 0) {
            glosa = result.toString();
        }
        return  glosa;
    }
    private StatusDesgloDTO getStatusCheckDesglo(DesgloseDTO desgloseDTO) {
        log.info("getStatusCheckDesglo() - INICIO");
        StatusDesgloDTO varStatus = new StatusDesgloDTO();
        // String Process_back = "modulo_global";

        List<Object> params = new ArrayList<>();
        params.add(desgloseDTO.getCodCliente().toUpperCase()); // Filtro COD_CLIENTE
        params.add(desgloseDTO.getCodTipoDes()); // Filtro COD_TIPO_DES
        params.add(desgloseDTO.getCodSubtipoDes()); // Filtro COD_SUBTIPO_DES
        params.add(desgloseDTO.getCodMoneda()); // Filtro COD_MONEDA

        List<Object[]> result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_GET_DESGLOSE_STATUS, params);
        log.debug("QUERY: "+ this.SQL_GET_DESGLOSE_STATUS);
        log.debug("PARAMETROS: "+ params);
        if (result != null && result.size() > 0) {
            varStatus.isFoundOnDB = true;
            for (Object[] elem : result) {
                varStatus.userOpen = elem[0] != null ? elem[0].toString() : null; // USER_OPEN
                varStatus.isTemporal = elem[1] != null ? (BooleanUtils.toBoolean(Integer.valueOf(elem[1].toString()))) : false; // FLG_IS_TEMPORAL
                varStatus.codEstado = elem[2] != null ? elem[2].toString() : null; // COD_ESTADO
                varStatus.glsEstado = elem[3] != null ? elem[3].toString() : null; // GLS_ESTADO
                varStatus.rolEjecutor = elem[4] != null ? elem[4].toString() : null; // NUM_ROL_1FIR
                varStatus.rolEjecutor = elem[5] != null ? elem[5].toString() : null; // NUM_ROL_2FIR
                varStatus.rolReemplazo = elem[6] != null ? elem[6].toString() : null; // NUM_ROL_REE
            }
            varStatus.enProceso = varStatus.userOpen != null;
        }

        if (varStatus.userOpen != null) {
            final TurbineUserDTO turbineUserDTO = this.turbineUserService.findByLoginName(varStatus.userOpen);
            // .orElseThrow(() -> new InternalServerErrorException("Turbine user with ID " + varStatus.userOpen + " was not found"));
            varStatus.nomUserOpen =
                (turbineUserDTO != null && turbineUserDTO.getFirstName() != null && turbineUserDTO.getFirstName().trim().isEmpty()) ? turbineUserDTO.getFullName() :
                    Constants.UNKNOWN_TURBINE_USER_NAME;
        }
        log.info("getStatusCheckDesglo() - TERMINO");
        return varStatus;
    }

    private boolean tomarCheckDesglo(BreakdownSaveRequestDTO requestDTO) {
        // TODO: llamar esto executeCifSpTakeDesgloRol();
        return false;
    }

    private void loadListaGrab(BreakdownSaveRequestDTO requestDTO, List<String> lstModA, List<String> lstCleanA, List<String> lstPeriodoA, List<String> lstDataA,
        List<String> lstNombreA, List<String> lstConceptosA, List<String> lstConcepA, List<String> lstMultiA) {

        log.info("loadListaGrab() - INICIO");
        //        for (int i = 0; i < 3; i++) { // No hasta 3 porque puede ser que venga menos información de las fechas de los períodos.
        for (int i = 0; i < requestDTO.extraDataDTO.fecPeriodo.size(); i++) {
            if (requestDTO.extraDataDTO.fecPeriodo.get(i) != null) {
                // Inicialización de parámetros de la query.
                List<Object> params = new ArrayList<>();
                params.add(requestDTO.desgloseDTO.getCodCliente().toUpperCase()); // Filtro COD_CLIENTE
                params.add(requestDTO.desgloseDTO.getCodTipoDes()); // Filtro COD_TIPO_DES
                params.add(requestDTO.desgloseDTO.getCodSubtipoDes()); // Filtro COD_SUBTIPO_DES
                params.add(requestDTO.desgloseDTO.getCodMoneda()); // Filtro COD_MONEDA
                params.add(requestDTO.extraDataDTO.fecPeriodo.get(i).format(Constants.FORMATTER_DATE_NO_SEPARATORS)); // Filtro FEC_PERIODO yyyyMMdd

                List<Object[]> result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_LOAD_LISTA_GRAB, params);

                log.debug("loadListaGrab : " +this.SQL_LOAD_LISTA_GRAB );
                log.debug("loadListaGrab : " + params );
                if (result != null && result.size() > 0) {
                    for (Object[] elem : result) {
                        if (requestDTO.extraDataDTO.fecPeriodo.get(i) != null) {
                            // if (!existeConcep(requestDTO.extraDataDTO.fecPeriodo[i], new ArrayList<>()) {
                            //      this.agregaValorLista(Me.txtFecha(iCol).Text, lstPeriodoA);
                            // }
                            // Se obtiene correctamente el COR_VALOR del multiregistro en la base de datos, mediante rs.Fields("COR_VALOR")
                            // this.agregaValorLista(/*"COD_TIPO_CONCEP"*/
                            //     elem[0].toString() + ";" + requestDTO.extraDataDTO.fecPeriodo[i].toString() + ";" + /*"COR_CONCEPTO"*/elem[1] + ";E;" + /*"COR_VALOR"*/ elem[2],
                            //    lstCleanA);
                            lstCleanA.add(/*"COD_TIPO_CONCEP"*/
                                elem[0].toString() + ";" + requestDTO.extraDataDTO.fecPeriodo.get(i).toString() + ";" + /*"COR_CONCEPTO"*/elem[1] + ";E;" + /*"COR_VALOR
                                "*/ elem[2]);
                        }
                        // LOG de matriz cargada
                        log.info("loadListaGrab(): VALOR [lstCleanA]: DATOS");
                        log.debug(lstCleanA.toString());

                        /*BreakdownSearchResponseDTO dto = new BreakdownSearchResponseDTO();
                        dto.userOpen = elem[0] != null ? elem[0].toString() : null;
                        BigDecimal value = (BigDecimal) elem[1];
                        dto.flgIsTemporal = value != null && BooleanUtils.toBoolean(value.intValue());
                        dto.codEstado = elem[2] != null ? elem[2].toString() : null;
                        dto.glsEstado = elem[3] != null ? elem[3].toString() : null;
                        dto.numRol1Firm = elem[4] != null ? elem[4].toString() : null;
                        dto.numRol2Firm = elem[5] != null ? elem[5].toString() : null;
                        dto.numRolRee = elem[6] != null ? elem[6].toString() : null;
                        dto.valDeflacBase = elem[7] != null ? (BigDecimal) elem[7] : null;
                        Timestamp dateValue = (Timestamp) elem[8];
                        dto.fecDeflacBase = dateValue != null ? dateValue.toLocalDateTime() : null;*/
                        // list.add(dto);
                    }
                }
            }
        }
        log.info("loadListaGrab() - FIN");
    }

    private void agregaValorLista(String value, List<String> list) {
        list.add(value);
    }

    private Boolean existeConcep(@NotNull String concept, @NotNull List<Concepto> concepts) {
        for (int i = 0; i < concepts.size(); i++) {
            if (concept.equals(concepts.get(i).getCodConcepto())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create or update a desglose.
     *
     * @param desgloseDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseDTO createOrUpdate(DesgloseDTO desgloseDTO) {
        log.info("Request to create or update a Desglose : {}", desgloseDTO);
        String user = SecurityUtils.getCurrentUserLogin().get();
        NewCommonPK id = NewCommonPK.newInstance(desgloseDTO);
        desgloseDTO = this.findOne(id,null).get();
        return convertDtoToEntityAndUpdate(desgloseDTO, true);
    }

    /**
     * Actualiza el estado a en proceso y se limpia el user open para el desglose.
     *
     * @param desgloseDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseDTO clearBreakdownUserOpenAndSetStatusInProcess(DesgloseDTO desgloseDTO) {
        log.info("Request to clear user open and set status in process a Desglose : {}", desgloseDTO);
        desgloseDTO.setCodEstado(ESTADO_BORRADOR_EN_PROCESO);
        return convertDtoToEntityAndUpdate(desgloseDTO, true);
    }

    @org.jetbrains.annotations.NotNull
    public DesgloseDTO convertDtoToEntityAndUpdate(DesgloseDTO desgloseDTO, boolean clearUserOpen) {
        if (clearUserOpen) {
            // al liberar el desglose se liberan usuario y fecha que abrio.
            desgloseDTO.setNomUserOpen(null);
            desgloseDTO.setFecHraOpen(null);
        }
        Desglose desglose = desgloseMapper.toEntity(desgloseDTO);
        desglose = desgloseRepository.save(desglose);
        DesgloseDTO dto = this.desgloseMapper.toDto(desglose);
        return updateBreakdownGlsStatus(dto);
    }

    public DesgloseDTO createOrUpdateMarcar(DesgloseDTO desgloseDTO) {
        log.info("Request to create or update a Desglose : {}", desgloseDTO);
        String user = SecurityUtils.getCurrentUserLogin().get();
        return convertDtoToEntityAndUpdate(desgloseDTO, false);
    }

    public DesgloseDTO updateRevisorModificaDesglose(DesgloseDTO desgloseDTO) {
        log.info("INICIO - Request to create or update a Desglose : {}", desgloseDTO);
        String user = SecurityUtils.getCurrentUserLogin().get();
        Integer codeOperator = this.sfStfOperatorService.findFirstByLoginName(user).getOperatorCode();
        NewCommonPK id = NewCommonPK.newInstance(desgloseDTO);
        desgloseDTO = this.findOne(id,null).get();
        desgloseDTO.setNomUserModif(user);
        desgloseDTO.setFecUserModif(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Santiago")));
        // desgloseDTO.setRolEmpleadoRevMod(codeOperator.toString());
        log.info("FIN - Request to create or update a Desglose : {}", desgloseDTO);
        return convertDtoToEntityAndUpdate(desgloseDTO, false);
    }

    /**
     * Clone a desglose.
     *
     * @param cloneRequestDTO
     * 		the entity whit the data to be cloned
     *
     * @return the persisted entity
     */
    @Transactional
    public BreakdownRecoverResponseDTO clone(BreakdownCloneRequestDTO cloneRequestDTO) {
        log.info("INICIO - Request to clone Desglose: {}",cloneRequestDTO);
        if (SecurityUtils.isCurrentUserInRole(PLAT_DESGLOSE_CUSTOM_CLONE_ROLE)) {
            // Se llama al SP para clonar.
            this.executeCifSpDesClonarDesglose(cloneRequestDTO);
        } else {
            throw new NoPermissionBusinessError(ACTION_NAME_CLONE);
        }

        // Se llama al método para recuperar un desglose.
        final BreakdownRecoverResponseDTO responseDTO = this.recoverBreakdown(BreakdownSearchRequestDTO.newInstance(cloneRequestDTO));
        log.info("FIN - Request to clone Desglose : {}");
        return responseDTO;
    }

    /**
     * @param breakdownSendRequestDTO
     *
     * @return
     */
    @Transactional
    public DesgloseDTO aprove(BreakdownSendRequestDTO breakdownSendRequestDTO) {
        log.info("INICIO - request to aprove desglose: {}", breakdownSendRequestDTO);
        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(breakdownSendRequestDTO.toString());
        // final Integer codeOperator = this.sfStfOperatorService.findFirstByLoginName(user.getLoginName()).getOperatorCode();

        NewCommonPK id = NewCommonPK.newInstance(breakdownSendRequestDTO.desglose);
        String estado = ESTADO_DEFINITIVO;
        if (breakdownSendRequestDTO.desglose.getRolEmpleadoRevMod() != null) {
            estado = ESTADO_CORREGIDO;
        }
        Desglose desglose =
            this.findBreakdownByIdAndSetData(DesgloseMensajePK.newInstance(id), estado, breakdownSendRequestDTO.desglose.getNumRolChgEstado().toString(), true, true);
        DesgloseAuditoriaDTO desgloseAuditoriaDTO = breakdownSendRequestDTO.desgloseAuditoria;
        DesgloseMensaje desgloseMensaje = desgloseMensajeMapper.toEntity(breakdownSendRequestDTO.desgloseMensaje);
        // DesgloseRevision desgloseRevision = desgloseRevisionMapper.toEntity(breakdownSendRequestDTO.desgloseRevision);

        // Actualizar fecha y usuario que aprueba.
        desglose.setFecDesgloDef(ZonedDateTime.now());
        desglose.setNomRevisorDef(user.getLoginName());
        desglose.setRolEmpleadoDesDef(getCurrentUserOperatorCode(user.getLoginName()));

        desgloseRepository.save(desglose);
        saveDesgloseAudit(desglose.getCodCliente(), desglose.getCodTipoDes(), desglose.getCodSubtipoDes(), desglose.getCodMoneda(), desglose.getCodPlantilla(),
            Constants.NUM_LINEA_AUDITORIA, desgloseAuditoriaDTO.getCodEvento(), desgloseAuditoriaDTO.getCodEstadoNvo(), desgloseAuditoriaDTO.getCodEstadoOri(),
            desgloseAuditoriaDTO.getGlsRol(), desgloseAuditoriaDTO.getRolEmpleadoAccion(), desgloseAuditoriaDTO.getGlsComentario());
        /**
         * ACTUALIZAMOS EL COD_REG_STATUS
         */
        DesgloseRevision desgloseRevision1 = desgloseRevisionService
            .busquedaRevision(id.getCodCliente(), id.getCodTipoDes(), id.getCodSubtipoDes(), id.getCodMoneda(), id.getCodPlantilla(), COD_REG_STATUS_ENVIA_REE);
        if (desgloseRevision1 == null) {
            //throw new InternalServerErrorException("Desglose no se encuentra en revision");
        } else {
            desgloseRevision1.setCodRegStatus(COD_REG_STATUS_APROBADO_REE);
            desgloseRevision1.setFecTerminoRevision(LocalDate.now());
            desgloseRevisionService.updateAprobar(desgloseRevision1);
        }
        tablaServicioMailService.enviarCorreos();
        log.info("FIN - request to aprove desglose");
        return saveBreakdownMessage(desglose, desgloseMensaje);
    }

    @Transactional
	public DesgloseDTO sendToReview(BreakdownSendRequestDTO breakdownSendRequestDTO) {
		log.info("INICIO- request to send Review desglose: {}", breakdownSendRequestDTO);
		final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(breakdownSendRequestDTO.toString());

		TurbineUsuarioDTO turbineUsuarioDTO = turbineUserService
				.getReplacementRole(breakdownSendRequestDTO.desgloseRevision.getRolRevisor());
		NewCommonPK id = NewCommonPK.newInstance(breakdownSendRequestDTO.desglose);
		Desglose desglose = findBreakdownByIdAndSetData(id, breakdownSendRequestDTO.desglose.getCodEstado(),
				breakdownSendRequestDTO.desglose.getNumRolChgEstado().toString(), true, true);
		DesgloseAuditoriaDTO desgloseAuditoriaDTO = breakdownSendRequestDTO.desgloseAuditoria;
		if (breakdownSendRequestDTO.desgloseMensaje.getGlsComentarios() != null
				&& !breakdownSendRequestDTO.desgloseMensaje.getGlsComentarios().isEmpty()) {
			breakdownSendRequestDTO.desgloseMensaje.setGlsComentarios(user.getFullName().toString() + "["
					+ breakdownSendRequestDTO.desgloseMensaje.getFecEvent().format(FORMATTER_DATE_TIME) + "] \n"
					+ breakdownSendRequestDTO.desgloseMensaje.getGlsComentarios());
		}
		DesgloseMensaje desgloseMensaje = desgloseMensajeMapper.toEntity(breakdownSendRequestDTO.desgloseMensaje);
		DesgloseRevision desgloseRevision = desgloseRevisionMapper.toEntity(breakdownSendRequestDTO.desgloseRevision);
		if (turbineUsuarioDTO != null) {
			desgloseRevision.setRolRevisorReem(turbineUsuarioDTO.getOperatorCode());
		} else {
			desgloseRevision.setRolRevisorReem(null);
		}
        desgloseRepository.save(desglose);
        saveDesgloseAudit(desglose.getCodCliente(), desglose.getCodTipoDes(), desglose.getCodSubtipoDes(), desglose.getCodMoneda(), desglose.getCodPlantilla(),
            Constants.NUM_LINEA_AUDITORIA, desgloseAuditoriaDTO.getCodEvento(), desgloseAuditoriaDTO.getCodEstadoNvo(), desgloseAuditoriaDTO.getCodEstadoOri(),
            desgloseAuditoriaDTO.getGlsRol(), desgloseAuditoriaDTO.getRolEmpleadoAccion(), desgloseAuditoriaDTO.getGlsComentario());
        /**
         * ACTUALIZAMOS EL COD_REG_STATUS
         */
        desgloseRevision.setCodRegStatus(COD_REG_STATUS_ENVIA_REE);
        desgloseRevision.setFlgNotificado(false);
        desgloseRevision.setFecEnvioRevision(ZonedDateTime.now());
        desgloseRevisionRepository.save(desgloseRevision);
        log.info("FIN - request to send Review desglose");
        return saveBreakdownMessage(desglose, desgloseMensaje);
    }

    @org.jetbrains.annotations.NotNull
    private Desglose findBreakdownByIdAndSetData(NewCommonPK id, final String codEstado, final String numRolChgEstado, boolean setNomUserOpenToNull, boolean setFlgReq2firma) {
        Desglose desglose = desgloseRepository.findById(id).get();
        desglose.setCodEstado(codEstado);
        if (setNomUserOpenToNull) {
            desglose.setNomUserOpen(null);
        }
        if (setFlgReq2firma) {
            desglose.setFlgReq2firma(null);
        }
        if (numRolChgEstado != null && !numRolChgEstado.trim().isEmpty()) {
            desglose.setNumRolChgEstado(numRolChgEstado);
        }
        return desglose;
    }

    @Transactional
    public DesgloseDTO sendExecutor(BreakdownSendRequestDTO breakdownSendRequestDTO) {
        log.info("INICIO- request to send executor desglose: {}", breakdownSendRequestDTO);
        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(breakdownSendRequestDTO.toString());
        // final Integer codeOperator = this.sfStfOperatorService.findFirstByLoginName(user.getLoginName()).getOperatorCode();

        NewCommonPK id = NewCommonPK.newInstance(breakdownSendRequestDTO.desglose);
        Desglose desglose = this.findBreakdownByIdAndSetData(id, breakdownSendRequestDTO.desglose.getCodEstado(), null, false, false);
        DesgloseAuditoriaDTO desgloseAuditoriaDTO = breakdownSendRequestDTO.desgloseAuditoria;
        if (breakdownSendRequestDTO.desgloseMensaje.getGlsComentarios()!= null && !breakdownSendRequestDTO.desgloseMensaje.getGlsComentarios().isEmpty()) {
        	breakdownSendRequestDTO.desgloseMensaje.setGlsComentarios(user.getFullName().toString() + "[" + breakdownSendRequestDTO.desgloseMensaje.getFecEvent().format(FORMATTER_DATE_TIME) + "] \n" + breakdownSendRequestDTO.desgloseMensaje.getGlsComentarios());
        }
        DesgloseMensaje desgloseMensaje = desgloseMensajeMapper.toEntity(breakdownSendRequestDTO.desgloseMensaje);
        DesgloseRevision desgloseRevision = desgloseRevisionMapper.toEntity(breakdownSendRequestDTO.desgloseRevision);
        desglose.setCodEstado(ESTADO_BORRADOR_OBSERVACION);
        // TODO: Actualizar el dato de segunda firma según aplicación legada
        // desglose.setFlgReq2firma(?);
        desglose.setNumDevolEjecutor(desglose.getNumDevolEjecutor() != null ? desglose.getNumDevolEjecutor() + 1 : 1);
        desgloseRepository.save(desglose);
        saveDesgloseAudit(desglose.getCodCliente(), desglose.getCodTipoDes(), desglose.getCodSubtipoDes(), desglose.getCodMoneda(), desglose.getCodPlantilla(),
            Constants.NUM_LINEA_AUDITORIA, desgloseAuditoriaDTO.getCodEvento(), desgloseAuditoriaDTO.getCodEstadoNvo(), desgloseAuditoriaDTO.getCodEstadoOri(),
            desgloseAuditoriaDTO.getGlsRol(), desgloseAuditoriaDTO.getRolEmpleadoAccion(), desgloseAuditoriaDTO.getGlsComentario());
        /**
         * ACTUALIZAMOS EL COD_REG_STATUS
         */
        DesgloseRevision desgloseRevision1 = desgloseRevisionService
            .busquedaRevision(id.getCodCliente(), id.getCodTipoDes(), id.getCodSubtipoDes(), id.getCodMoneda(), id.getCodPlantilla(), COD_REG_STATUS_ENVIA_REE);
        if (desgloseRevision1 != null) {
            DesgloseRevision temporal = desgloseRevision;
            desgloseRevision = desgloseRevision1;
            desgloseRevision.setRolDigitador(temporal.getRolDigitador());
            desgloseRevision.setRolRevisor(temporal.getRolRevisor());
            desgloseRevision.setCodRegStatus(COD_REG_STATUS_DEVOLUCION_REE);
            desgloseRevision.setFecEnvioRevision(ZonedDateTime.now());
            this.desgloseRevisionService.updateDeshabilita(desgloseRevision);
            desgloseRevisionRepository.save(desgloseRevision);
        } else {
            desgloseRevision.setCodRegStatus(COD_REG_STATUS_DEVOLUCION_REE);
            desgloseRevision.setFecEnvioRevision(ZonedDateTime.now());
            desgloseRevisionRepository.save(desgloseRevision);
        }
        tablaServicioMailService.enviarCorreos();
        log.info("FIN - request to send executor desglose");
        return saveBreakdownMessage(desglose, desgloseMensaje);
    }

    /**
     * delete Desglose
     *
     * @param requestDTO
     */
    @Transactional
    public void deleteDesglose(DesgloseDTO requestDTO) {
        log.info("INICIO - Request to delete Desglose");
        log.info(requestDTO.toString());
        int contador = 0;
        if(requestDTO.getNomUserOpen() == null || requestDTO.getNomUserOpen().isEmpty()) {
            final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
            List<TurbinePermissionDTO> turbinePermissionList = this.turbinePermissionService.findAllPermisosByUser(userLogin);
            for (TurbinePermissionDTO item : turbinePermissionList) {
                if (item.getPermissionName().equals(MDB6)) {
                    contador++;
                }
            }
            if (contador == 0) {
                throw new NoPermissionBusinessError(ACTION_NAME_DELETE);
            }
            // Se llama al SP para eliminar
            this.executeCifSpDelDesglo(requestDTO);
        }

        // Se llama al método para recuperar un desglose.
        // final BreakdownRecoverResponseDTO responseDTO = this.recoverBreakdown(BreakdownSearchRequestDTO.newInstance(cloneRequestDTO));
        log.info("FIN - Request to delete Desglose");
        // return responseDTO;
    }

    @Transactional
    public List<BreakdownValidationErrorDTO> validateBreakdown(@NotNull BreakdownConceptValidationRequestDTO requestDTO) {
        /**
         * En caso que haya que realizar validación. Compruebo Períodos de pantalla están cuadrados.
         */
        log.info("validateBreakdown(): Inicio cuadratura. [" + Calendar.getInstance().getTime() + "]");
        List<BreakdownValidationErrorDTO> errorDTOS = new LinkedList<>();
        errorDTOS = this.executeConceptValidationsDesborde(requestDTO);
        List<BreakdownValidationErrorDTO> errorDTOS2 = new LinkedList<>();
        errorDTOS2 = this.executeConceptValidationsConcepts(requestDTO);
        for(BreakdownValidationErrorDTO item : errorDTOS2){
            errorDTOS.add(item);
        }
        if (requestDTO.extraDataDTO.canValidate) {
            // TODO: Validación frmAnalista.frm línea 10324 "If canValidate And Not objCheckDesglo.enProceso Then"
            List<BreakdownValidationErrorDTO> errorDTOS1  = this.executeConceptValidations(requestDTO);
            for(BreakdownValidationErrorDTO item :errorDTOS1){
                errorDTOS.add(item);
            }
        }
        log.info("validateBreakdown(): Fin cuadratura. [" + Calendar.getInstance().getTime() + "]");
        return errorDTOS;
    }

    /**
     * Save a desglose.
     *
     * @param requestDTO
     * 		the entity whit the data to be saved
     *
     * @return the persisted entity
     */
    @Transactional
    public DesgloseDTO save(BreakdownSaveRequestDTO requestDTO) {
        log.info("INICIO - Request to save Desglose");
        Boolean eliminaMultiregistro = false;
        ConceptoRequestDTO conceptoRequestDTO = new ConceptoRequestDTO();
        conceptoRequestDTO.templateCode = Integer.parseInt(requestDTO.desgloseDTO.getCodPlantilla());
        List<String> conceptosTypesCodes = new LinkedList<>();
        conceptosTypesCodes.add(COD_TIPO_CONCEP_BALANCE);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_GEN_FONDOS);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_IND_FINAN);
        List<String> orderByFiels = new LinkedList<>();
        orderByFiels.add(FIELD_NAME_COD_TIPO_CONCEP);
        orderByFiels.add(FIELD_NAME_NUM_SECUENCIA);
        conceptoRequestDTO.conceptTypeCodesIn = conceptosTypesCodes;
        conceptoRequestDTO.orderByFields = orderByFiels;
        conceptoRequestDTO.orderByDirection = "ASC";
        conceptoRequestDTO.flgConActivo = COD_REG_STATUS_ONE;
        conceptoRequestDTO.columnLetterGlsFormulaVal1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaVal2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaVal3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaVal4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaDef1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaDef2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaDef3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaDef4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaPrc1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaPrc2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaPrc3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaPrc4 = COLUMN_NUMBER_GLS_FORMULA__4;
        List<ConceptoDTO> conceptoDTOList = this.conceptoService.findByTemplateAndCodConceptsIn(conceptoRequestDTO,null).conceptoDTOList;
        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(requestDTO.toString());
        for(int i = 0; i< requestDTO.extraDataDTO.conceptos2.size();i++) {
            ValPeriodoDesgloseDTO valPeriodoDesgloseDTO = new ValPeriodoDesgloseDTO();
            valPeriodoDesgloseDTO.setCodCliente(requestDTO.desgloseDTO.getCodCliente());
            valPeriodoDesgloseDTO.setCodTipoDes(Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes()));
            valPeriodoDesgloseDTO.setCodSubtipoDes(Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes()));
            valPeriodoDesgloseDTO.setCodMoneda(requestDTO.desgloseDTO.getCodMoneda());
            valPeriodoDesgloseDTO.setCodPlantilla(Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla()));
            valPeriodoDesgloseDTO.setCorConcepto(requestDTO.extraDataDTO.conceptos2.get(i).getCorConcepto());
            valPeriodoDesgloseDTO.setCorValor(requestDTO.extraDataDTO.conceptos2.get(i).getCorValor());
            valPeriodoDesgloseService.deleteMultiregistro(valPeriodoDesgloseDTO);

            DesgloseDTO dto = new DesgloseDTO();
            dto.setCodCliente(requestDTO.desgloseDTO.getCodCliente());
            dto.setCodTipoDes(requestDTO.desgloseDTO.getCodTipoDes());
            dto.setCodSubtipoDes(requestDTO.desgloseDTO.getCodSubtipoDes());
            dto.setCodMoneda(requestDTO.desgloseDTO.getCodMoneda());
            dto.setCodPlantilla(requestDTO.desgloseDTO.getCodPlantilla());
            executeSpResecuenciarMultiregistros(dto,requestDTO.extraDataDTO.conceptos2.get(i).getCorConcepto(),requestDTO.extraDataDTO.conceptos2.get(i).getCodTipoConcep());
            eliminaMultiregistro = true;
        }
        checkEmptyFieldsInBreakdown(requestDTO.extraDataDTO);
        if (!requestDTO.extraDataDTO.canValidate) {
            requestDTO.desgloseDTO.setCodEstado(Constants.ESTADO_BORRADOR_EN_PROCESO);
            requestDTO.extraDataDTO.enProceso = true;
        } else {
            if (!requestDTO.desgloseDTO.getGlsEstado().equals(ESTADO_DEFINITIVO) && !requestDTO.desgloseDTO.getGlsEstado().equals(Constants.ESTADO_CORREGIDO)) {
                requestDTO.desgloseDTO.setCodEstado(Constants.ESTADO_BORRADOR_VALIDADO);
                // liberamos el usuario que edita el desglose.
                requestDTO.extraDataDTO.enProceso = false;
                requestDTO.extraDataDTO.validado = false;
            }
        }
        requestDTO.extraDataDTO.validado = !requestDTO.extraDataDTO.enProceso;
        requestDTO.extraDataDTO.isAutoriced = false;
        List<TurbinePermissionDTO> turbinePermissionList = this.turbinePermissionService.findAllPermisosByUser(user.getLoginName());
        for (TurbinePermissionDTO item : turbinePermissionList) {
            if (item.getPermissionName().equals(MDB2)) {
                requestDTO.extraDataDTO.isAutoriced = true;
            }
        }
        /**
         * SI NO ESTA AUTORIZADO, VOLVEMOS A VERIFICAR SU ESTADO EN LA BASE DE DATOS Y SI ESTÁ LIBRE O NO ESTÁ, LO TOMAMOS Y VOLVEMOS A VERIFICAR SU AUTORIZACIÓN
         */

        if (!requestDTO.extraDataDTO.isAutoriced) {
            StatusDesgloDTO varStatusDesglo = getStatusCheckDesglo(requestDTO.desgloseDTO);
            if (varStatusDesglo.isFoundOnDB) {
                requestDTO.extraDataDTO.isTemporal = varStatusDesglo.isTemporal;
                requestDTO.extraDataDTO.userOpen = varStatusDesglo.userOpen;
                requestDTO.extraDataDTO.nomUserOpen = varStatusDesglo.nomUserOpen;
                requestDTO.extraDataDTO.enProceso = varStatusDesglo.enProceso;
                if (requestDTO.extraDataDTO.userOpen == null) {
                    if (tomarCheckDesglo(requestDTO)) {
                        requestDTO.extraDataDTO.userOpen = user.getFullName();
                        requestDTO.extraDataDTO.nomUserOpen = user.getLoginName();
                    } else {
                        log.info("guardarDesglose(): CheckAutotizationError...");
                        throw new SaveDesgloseValidationException("No se ha podido Tomar ni GRABAR el desglose señalado por un Error desconocido."); // CheckAutotizationError
                    }
                }
            } else {
                if (tomarCheckDesglo(requestDTO)) {
                    requestDTO.extraDataDTO.isTemporal = true;
                    requestDTO.extraDataDTO.userOpen = user.getFullName();
                    requestDTO.extraDataDTO.nomUserOpen = user.getLoginName();
                } else {
                    log.info("guardarDesglose(): CheckAutotizationError...");
                    throw new SaveDesgloseValidationException("No se ha podido Tomar ni GRABAR el desglose señalado por un Error desconocido."); // CheckAutotizationError
                }
            }
            // Asignamos la autoridad para modificar el desglose
            requestDTO.extraDataDTO.isAutoriced = user.getLoginName().equals(user.getLoginName());
            if (!requestDTO.extraDataDTO.isAutoriced) {
                throw new SaveDesgloseValidationException(
                    "Usuario no autorizado para grabar sobre este Desglose, momentáneamente." + " El desglose está siendo utilizado por otro Ejecutor ("
                        + requestDTO.extraDataDTO.nomUserOpen + ")."); // OperacionNoAutirizada
            }
        }

        // Inicialización de variables de conexión que se utlizarán para transaformar fórmula en número
        final Date currentDateTime = Calendar.getInstance().getTime();

        // Se actualiza el valor del IVA
        if(requestDTO.desgloseDTO.getValIva()==null) {
            requestDTO.desgloseDTO.setValIva(new BigDecimal("0"));
        } else {
            requestDTO.desgloseDTO.setValIva(requestDTO.desgloseDTO.getValIva().divide(BIG_DECIMAL_VALUE_100));
        }


        /**
         * Creo el Desglose correspondiente
         */
        log.info("guardarDesglose(): Inicio Grabación Desglose, Cliente [" + requestDTO.desgloseDTO.getCodCliente() + "]. " + "[" + Calendar.getInstance().getTime() + "]");
        this.executeCifSpInsUpdDesgloRol(requestDTO);
        log.info("guardarDesglose(): Fin Grabación Desglose, Cliente [" + requestDTO.desgloseDTO.getCodCliente() + "]. " + "[" + Calendar.getInstance().getTime() + "]");
        /**
         * Grabo los Conceptos de Impresion CONIMP_WK
         */
        // Eliminación
        this.conceptoImpresionService.executeCifSpDelConImp(requestDTO);
        // Grabación
        final String codCliente = requestDTO.desgloseDTO.getCodCliente();
        final Integer codTipoDes = Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes());
        final Integer codSubtipoDes = Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes());
        final String codMoneda = requestDTO.desgloseDTO.getCodMoneda();
        final Integer codPlantilla = Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla());
        log.info("guardarDesglose(): Inicio Grabación CONIMP_WK [" + Calendar.getInstance().getTime() + "]");
        for (ConceptoImpresionDTO conImp : requestDTO.extraDataDTO.matrizConImp) {
            this.conceptoImpresionService
                .executeCifSpInsConImp(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, conImp.getCodTipoConcep(), Integer.parseInt(conImp.getCorConcepto()));
        }
        log.info("guardarDesglose(): Fina Grabación CONIMP_WK [" + Calendar.getInstance().getTime() + "]");
        /**
         * Recorro los períodos entre las posiciones 0 -> idxAnterior del arrPERIODOS y idxSiguiente -> numPeriodosConInfo - Grabo aquellos donde hasInfo = True
         */
        log.info("guardarDesglose(): Inicio Grabación Periodos  [" + Calendar.getInstance().getTime() + "]");
        // Se guardan solo los periodos visibles

        BreakdownSearchRequestDTO breakdownSearchRequestDTO = new BreakdownSearchRequestDTO();
        breakdownSearchRequestDTO.codCliente = requestDTO.desgloseDTO.getCodCliente();
        breakdownSearchRequestDTO.tipoDesglose = Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes());
        breakdownSearchRequestDTO.subTipoDesglose = Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes());
        breakdownSearchRequestDTO.moneda = requestDTO.desgloseDTO.getCodMoneda();
        breakdownSearchRequestDTO.tipoPlantillaDesglose = Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla());
        Sort orderBy = Sort.by(Sort.Direction.ASC, Constants.PROPERTY_NAME_FECH_PERIODO);
        List<PeriodoDTO> periodosCopy = this.periodoService.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(PeriodoRequestDTO.newInstance(breakdownSearchRequestDTO),
            orderBy,null);
        List<PeriodoDTO> periodos = requestDTO.extraDataDTO.periodos;

        for(int i = 0; i< periodos.size(); i++) {
            for(int j = 0; j< periodosCopy.size(); j++) {
                if(periodos.get(i).getFecPeriodo().isEqual(periodosCopy.get(j).getFecPeriodo())) {
                    periodos.get(i).setFecInformSbif(periodosCopy.get(j).getFecInformSbif());
                    periodos.get(i).setMtoSbifDdCas(periodosCopy.get(j).getMtoSbifDdCas());
                    periodos.get(i).setMtoSbifDdMor(periodosCopy.get(j).getMtoSbifDdMor());
                    periodos.get(i).setMtoSbifDdVen(periodosCopy.get(j).getMtoSbifDdVen());
                    periodos.get(i).setMtoSbifDdVig(periodosCopy.get(j).getMtoSbifDdVig());
                    periodos.get(i).setMtoSbifDiCas(periodosCopy.get(j).getMtoSbifDiCas());
                    periodos.get(i).setMtoSbifDiVen(periodosCopy.get(j).getMtoSbifDiVen());
                    periodos.get(i).setMtoSbifDiVig(periodosCopy.get(j).getMtoSbifDiVig());
                    periodos.get(i).setMtoSbifImp90179(periodosCopy.get(j).getMtoSbifImp90179());
                }
            }
        }

        // Se preparan los objetos para invocar loadDeudaSbifHoyService.getLoadDeudaSbifHoy_By_Rut_FecConsulta
        List<LocalDate> arrPERIODOS = new ArrayList<LocalDate>();
        for (int i = 0; i < periodos.size(); i++) {
            arrPERIODOS.add(periodos.get(i).getFecPeriodo());
        }
        List<Integer> activePeriodsIndexes = new ArrayList<Integer>();
        for (int i = 0; i < requestDTO.isIndicePeriodos.length; i++) {
            activePeriodsIndexes.add(requestDTO.isIndicePeriodos[i]);
        }
        for (int i = 0, periodosSize = periodos.size(); i < periodosSize; i++) {
            for (int l = 0; l < requestDTO.isIndicePeriodos.length; l++) {
                if (i == requestDTO.isIndicePeriodos[l]) {
                    Integer ejecutaProceso = 0;
                    for(Integer indice: requestDTO.activePeriodsEdit ){
                        if(l == indice){
                            ejecutaProceso = 1;
                        }
                    }
                    if (periodos.get(i) != null) {
                            requestDTO.extraDataDTO.canLoadSBIF = canLoadSBIF(requestDTO);
                        if (requestDTO.extraDataDTO.canLoadSBIF) {
                            List<DatosSbifDTO> listDatosSbifDTO = null;
                            if(periodos.get(i).getFecInformSbif()==null) {
                                listDatosSbifDTO = loadDeudaSbifHoyService.getLoadDeudaSbifHoy_By_Rut_FecConsulta(codCliente, periodos.get(i).getFecPeriodo(), arrPERIODOS,
                                    activePeriodsIndexes);
                            } else {
                                requestDTO.extraDataDTO.canLoadSBIF = true;
                            }
                            if (listDatosSbifDTO != null && listDatosSbifDTO.size() > 0) {
                                log.info("Datos de Sbif: " + listDatosSbifDTO.toString());
                                periodos.get(i).setFecInformSbif(listDatosSbifDTO.get(0).getFecInforme());
                                periodos.get(i).setMtoSbifDdCas(listDatosSbifDTO.get(0).getMtoSbif_DCas());
                                periodos.get(i).setMtoSbifDdMor(listDatosSbifDTO.get(0).getMtoSbif_DMor());
                                periodos.get(i).setMtoSbifDdVen(listDatosSbifDTO.get(0).getMtoSbif_DVen());
                                periodos.get(i).setMtoSbifDdVig(listDatosSbifDTO.get(0).getMtoSbif_DVig());
                                periodos.get(i).setMtoSbifDiCas(listDatosSbifDTO.get(0).getMtoSbif_ICas());
                                periodos.get(i).setMtoSbifDiVen(listDatosSbifDTO.get(0).getMtoSbif_IVen());
                                periodos.get(i).setMtoSbifDiVig(listDatosSbifDTO.get(0).getMtoSbif_IVig());
                                periodos.get(i).setMtoSbifImp90179(listDatosSbifDTO.get(0).getMtoSbif_Imp90_179());
                            }
                        }
                        PeriodoDTO periodoDTO = periodos.get(i);
                        // Grabar PERIODO

                        this.periodoService.executeCifSpInsPeriod(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, periodoDTO, requestDTO.extraDataDTO.canLoadSBIF);
                        // Grabar VALPRD_WK
                        Integer columnNumberGlsFormulaVal = Constants.COLUMN_NUMBER_GLS_FORMULA_VAL[l];
                        Integer columnNumberGlsFormulaDef = Constants.COLUMN_NUMBER_GLS_FORMULA_DEF[l];
                        Integer columnNumberGlsFormulaPrc = Constants.COLUMN_NUMBER_GLS_FORMULA_PRC[l];
                        if(ejecutaProceso == 1 || eliminaMultiregistro ) {
                            for (int j = 0, conceptosSize = conceptoDTOList.size(); j < conceptosSize; j++) {
                                ConceptoDTO conceptoDTO = conceptoDTOList.get(j);
                                if (conceptoDTO.getCodTipoRegis() != null && !conceptoDTO.getCodTipoRegis().isEmpty() && (conceptoDTO.getCodTipoConcep().equals(COD_TIPO_CONCEP_BALANCE) ||
                                    conceptoDTO.getCodTipoConcep().equals(COD_TIPO_CONCEP_EST_RESULT))) { // Si el
                                    // concepto
                                    // no es un encabezado de
                                    // grupo
                                    int corValor = 0;
                                    if (conceptoDTO.getCodTipoRegis().equals(MULTI_REGISTRY)) {
                                        corValor = 1;
                                        conceptoDTO.setCorValor(null);
                                    }
                                    if(conceptoDTO.getCorConcepto()!= null) {
                                        this.valPeriodoDesgloseService.executeCifSpDelValPrd(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, conceptoDTO, periodoDTO.getFecPeriodo());
                                    }
                                    for (int k = 0; k < requestDTO.extraDataDTO.visualData.length; k++) {
                                        if (requestDTO.extraDataDTO.visualData[k][COLUMN_NUMBER_COD_TIPO_CONCEP].equals(conceptoDTO.getCodTipoConcep()) && requestDTO.extraDataDTO.visualData[k][COLUMN_NUMBER_COD_CONCEPT].equals(conceptoDTO.getCodConcepto())) {
                                            conceptoDTO.setCorValor(corValor);
                                            if (requestDTO.extraDataDTO.visualData[k] != null) {
                                                if(eliminaMultiregistro) {
                                                    if(ejecutaProceso == 0) {

                                                    }
                                                }
                                               this.valPeriodoDesgloseService
                                                    .executeCifSpInsValPrd(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, conceptoDTO, periodoDTO.getFecPeriodo(),
                                                        requestDTO.extraDataDTO.data[k], requestDTO.extraDataDTO.visualData[k], COLUMN_NUMBER_CCC, columnNumberGlsFormulaVal,
                                                        columnNumberGlsFormulaPrc, k);
                                                corValor++;
                                            }
                                        }
                                    }
                                }
                            }
                         }
                        if(!requestDTO.isRapid) {
                            for (int j = requestDTO.extraDataDTO.data.length, conceptosSize = requestDTO.extraDataDTO.conceptos1.size(); j < conceptosSize; j++) {
                                ConceptoDTO conceptoDTO = requestDTO.extraDataDTO.conceptos1.get(j);
                                if (conceptoDTO.getCodTipoRegis() != null && !conceptoDTO.getCodTipoRegis().isEmpty()) {
                                    if(conceptoDTO.getCorConcepto()!= null) {
                                        this.valPeriodoDesgloseService.executeCifSpDelValPrd(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, conceptoDTO, periodoDTO.getFecPeriodo());
                                    }
                                    int corValor = 0;
                                    if (requestDTO.extraDataDTO.visualData1[j][COLUMN_NUMBER_COD_TIPO_CONCEP].equals(conceptoDTO.getCodTipoConcep()) && requestDTO.extraDataDTO.visualData1[j][COLUMN_NUMBER_COD_CONCEPT].equals(conceptoDTO.getCodConcepto())) {
                                        conceptoDTO.setCorValor(corValor);
                                        if (requestDTO.extraDataDTO.visualData1[j] != null) {
                                            this.valPeriodoDesgloseService
                                                .executeCifSpInsValPrd(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, conceptoDTO, periodoDTO.getFecPeriodo(),
                                                    requestDTO.extraDataDTO.data1[j], requestDTO.extraDataDTO.visualData1[j], COLUMN_NUMBER_CCC, columnNumberGlsFormulaDef,
                                                    columnNumberGlsFormulaPrc, j);
                                            corValor++;
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
            }
        }
        log.info("guardarDesglose(): Fin Grabación Periodos  [" + Calendar.getInstance().getTime() + "]");

        // TODO: Si no hay registros...
        /**
         * Grabo las Empresas que consolidan y sus porcentages (CONSOL y PRCCNS)
         */
        boolean hayEmpresas = requestDTO.extraDataDTO.empConsolPeriodoDesgConsolList != null && requestDTO.extraDataDTO.empConsolPeriodoDesgConsolList.size() > 0;
        if (hayEmpresas) {
            log.info("guardarDesglose(): Inicio empresas que consolidan [" + Calendar.getInstance().getTime() + "]");
            // Eliminación CONSOL y PRCCNS
            this.empresaConsolidadaService.executeCifSpDelConsol(codCliente, Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla()), codTipoDes, codSubtipoDes, codMoneda, null);
            List<String> listaConsolidados = new LinkedList<>();
            for (EmpConsolPeriodoDesgConsolDTO empresaConsolidadaDTO : requestDTO.extraDataDTO.empConsolPeriodoDesgConsolList) {
                Integer bandera = 0;
                for(String item : listaConsolidados) {
                    if(item.equals(empresaConsolidadaDTO.nomEmpresa)) {
                        bandera = 1;
                    }
                }
                if(bandera == 0) {
                    listaConsolidados.add(empresaConsolidadaDTO.nomEmpresa);
                }
            }
            for(String item : listaConsolidados) {
                // SP CREAR EMPRESA (CONSOL)
                final Integer corEmpresa = this.empresaConsolidadaService
                    .executeCifSpInsConsol(codCliente, Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla()), codTipoDes, codSubtipoDes, codMoneda,
                        null, item);
                for (EmpConsolPeriodoDesgConsolDTO empresaConsolidadaDTO : requestDTO.extraDataDTO.empConsolPeriodoDesgConsolList) { // TODO: Enviar esta estructura desde la UI
                    if (empresaConsolidadaDTO.fecPeriodo != null && empresaConsolidadaDTO.nomEmpresa.equals(item)) {
                        // SP CREAR PRCCNS
                        if (empresaConsolidadaDTO.valPrcConsol != null) {
                            this.periodoDesgConsolService
                                .executeCifSpInsPrcCns(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, empresaConsolidadaDTO.fecPeriodo, corEmpresa,
                                    empresaConsolidadaDTO.valPrcConsol);
                        }
                    }
                }
            }
            log.info("guardarDesglose(): Fin empresas que consolidan [" + Calendar.getInstance().getTime() + "]");
        }
        NewCommonPK id = new NewCommonPK();
        id.setCodCliente(requestDTO.desgloseDTO.getCodCliente());
        id.setCodTipoDes(Integer.parseInt(requestDTO.desgloseDTO.getCodTipoDes()));
        id.setCodSubtipoDes(Integer.parseInt(requestDTO.desgloseDTO.getCodSubtipoDes()));
        id.setCodMoneda(requestDTO.desgloseDTO.getCodMoneda());
        id.setCodPlantilla(Integer.parseInt(requestDTO.desgloseDTO.getCodPlantilla()));

        DesgloseDTO dto = this.findOne(id,null).get();
        // todo: esto no deberia ser asi conversar con tony, sobre transancionalidad
        dto.setCodEstado(requestDTO.desgloseDTO.getCodEstado());
        if (dto == null) {
            throw new SaveDesgloseValidationException("Error. El Desglose no existe.");
        }
        for (int j = 0, conceptosSize = conceptoDTOList.size(); j < conceptosSize; j++) {
            ConceptoDTO conceptoDTO = conceptoDTOList.get(j);
            if (conceptoDTO.getCodTipoRegis() != null && !conceptoDTO.getCodTipoRegis().isEmpty()) { // Si el concepto no es un encabezado de grupo
                int corValor = 0;
                if (conceptoDTO.getCodTipoRegis().equals(MULTI_REGISTRY)) {
                    this.executeSpResecuenciarMultiregistros(dto,conceptoDTO.getCorConcepto(),conceptoDTO.getCodTipoConcep());
                }
            }
        }
        log.info("FIN - Request to save Desglose");
        return updateBreakdownGlsStatus(dto);
    }
    @Transactional
    public void deleteProyecciones(BreakdownSaveRequestDTO requestDTO) {
        final String codCliente = requestDTO.desgloseDTO.getCodCliente();
        final Integer codTipoDes = Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes());
        final Integer codSubtipoDes = Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes());
        final String codMoneda = requestDTO.desgloseDTO.getCodMoneda();
        final Integer codPlantilla = Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla());
        final Integer codPlantillaPry = Integer.valueOf(requestDTO.desgloseDTO.getCodPlantillaProyeccion());
        this.valPeriodoProyeccionDesgloseService.deleteValPrdPry(requestDTO.desgloseDTO);
    }
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor = StorageProcedureBusinessError.class)
    public DesgloseDTO savePry(BreakdownSaveRequestDTO requestDTO) {
        log.info("INICIO - Request to save Desglose proyeccion");
        final String codCliente = requestDTO.desgloseDTO.getCodCliente();
        final Integer codTipoDes = Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes());
        final Integer codSubtipoDes = Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes());
        final String codMoneda = requestDTO.desgloseDTO.getCodMoneda();
        final Integer codPlantilla = Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla());
        final Integer codPlantillaPry = Integer.valueOf(requestDTO.desgloseDTO.getCodPlantillaProyeccion());
        for (int j = 0, conceptosSize = requestDTO.extraDataDTO.conceptos.size(); j < conceptosSize; j++) {
            ConceptoDTO conceptoDTO = requestDTO.extraDataDTO.conceptos.get(j);
            if (conceptoDTO.getCodTipoRegis() != null && !conceptoDTO.getCodTipoRegis().isEmpty()) { // Si el concepto no es un encabezado de grupo
                for (int k = 0; k < requestDTO.extraDataDTO.visualData.length; k++) {
                    if (requestDTO.extraDataDTO.visualData[k][COLUMN_NUMBER_COD_TIPO_CONCEP].equals(conceptoDTO.getCodTipoConcep())
                        && requestDTO.extraDataDTO.visualData[k][COLUMN_NUMBER_COD_CONCEPT].equals(conceptoDTO.getCodConcepto())) {
                        if (requestDTO.extraDataDTO.visualData[k] != null) {
                            LocalDate fechaAdd = requestDTO.desgloseDTO.getFecPeriodo1Rpt();
                            for(int l = 0; l < requestDTO.extraDataDTO.visualData[k].length; l ++) {
                                if(l >= 5) {
                                    this.valPeriodoProyeccionDesgloseService.executeCifSpInsValPrdPry(codCliente, codPlantilla, codTipoDes, codSubtipoDes, codMoneda, conceptoDTO,
                                        requestDTO.desgloseDTO.getFecPeriodo1Rpt(), requestDTO.extraDataDTO.data[k], requestDTO.extraDataDTO.visualData[k], l, k, fechaAdd);
                                    fechaAdd = fechaAdd.plusYears(1);
                                }
                            }
                        }
                    }
                }
            }
        }
        log.info("guardarDesglose(): Fin Grabación Periodos  [" + Calendar.getInstance().getTime() + "]");

        PeriodoPK periodoPK = new PeriodoPK();
        periodoPK.setFecPeriodo(requestDTO.desgloseDTO.getFecPeriodo1Rpt());
        periodoPK.setCodPlantilla(Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla()));
        periodoPK.setCodCliente(requestDTO.desgloseDTO.getCodCliente());
        periodoPK.setCodMoneda(requestDTO.desgloseDTO.getCodMoneda());
        periodoPK.setCodSubtipoDes(Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes()));
        periodoPK.setCodTipoDes(Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes()));
        PeriodoDTO period =  periodoService.findOne(periodoPK).get();
        period.setCodEstadoPry(ESTADO_BORRADOR_EN_PROCESO);
        periodoService.save(period);

        NewCommonPK id = new NewCommonPK();
        id.setCodCliente(requestDTO.desgloseDTO.getCodCliente());
        id.setCodTipoDes(Integer.parseInt(requestDTO.desgloseDTO.getCodTipoDes()));
        id.setCodSubtipoDes(Integer.parseInt(requestDTO.desgloseDTO.getCodSubtipoDes()));
        id.setCodMoneda(requestDTO.desgloseDTO.getCodMoneda());
        id.setCodPlantilla(Integer.parseInt(requestDTO.desgloseDTO.getCodPlantilla()));
        DesgloseDTO dto = this.findOne(id,null).get();
        dto.setCodEstado(requestDTO.desgloseDTO.getCodEstado());
        if (dto == null) {
            throw new SaveDesgloseValidationException("Error. El Desglose no existe.");
        }
        log.info("FIN - Request to save Desglose proyeccion");
        return updateBreakdownGlsStatus(dto);
    }
    public void executeSpResecuenciarMultiregistros(DesgloseDTO requestDTO,Integer corConcepto,String codTipoConcepto) {
        this.log.info("INICIO - executeSpResecuenciarMultiregistros");
        // Llenar los parámetros
        Param<String> p1 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_01_P_COD_CLIENTE, String.class, requestDTO.getCodCliente(), ParameterMode.IN);
        Param<Integer> p2 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_02_P_COD_TIPO_DES, Integer.class, Integer.valueOf(requestDTO.getCodTipoDes()), ParameterMode.IN);
        Param<Integer> p3 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_03_P_COD_SUBTIPO_DES, Integer.class, Integer.valueOf(requestDTO.getCodSubtipoDes()), ParameterMode.IN);
        Param<String> p4 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_04_P_COD_MONEDA, String.class, requestDTO.getCodMoneda(), ParameterMode.IN);
        Param<Integer> p5 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_05_P_COD_PLANTILLA, Integer.class, Integer.valueOf(requestDTO.getCodPlantilla()), ParameterMode.IN);
        Param<Integer> p6 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_06_P_COR_CONCEPTO, Integer.class, corConcepto, ParameterMode.IN);
        Param<String> p7 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_07_P_COD_TIPO_CONCEP, String.class,codTipoConcepto, ParameterMode.IN);
        Param<Integer> p8 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_08_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p9 = new Param<>(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_09_P_GLOSA, String.class, null, ParameterMode.OUT);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8,p9);

        // Interpretar los resultados
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS, parameters);
        log.debug("QUERY : " +DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS );
        log.debug("PARAMETROS : " + parameters );
        log.info(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_08_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(DesgloseService.SP_RESECUENCIAR_MULTIREGISTROS_09_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeSpResecuenciarMultiregistros");
    }
    /**
     * Método que chequea si existen campos vacíos que invalidan el salvar un desglose.
     *
     * @param extraDataDTO
     */
    public void checkEmptyFieldsInBreakdown(ExtraDataDTO extraDataDTO) {
        if (extraDataDTO.fecPeriodo == null || extraDataDTO.fecPeriodo.isEmpty() || extraDataDTO.deflatorPeriod == null
            || extraDataDTO.deflatorPeriod.isEmpty() || extraDataDTO.monthsPeriod == null || extraDataDTO.monthsPeriod.isEmpty()) {
            throw new SaveDesgloseValidationException("Error, las fechas de períodos, deflactores y meses no tienen valores.");
        }
        int periodIndex = 0;
        for (int index : COLUMN_NUMBER_GLS_FORMULA_VAL) {
            if (extraDataDTO.fecPeriodo.size() > periodIndex && extraDataDTO.fecPeriodo.get(periodIndex) != null) {
                LinkedList<String> toValidateZerosOrNullOrEmpty = new LinkedList<>();
                for (String[] value : extraDataDTO.visualData) {
                    toValidateZerosOrNullOrEmpty.add(value[index]);
                }
                final boolean allNullsOrZeros = Utils.isAllNullsOrZeros(toValidateZerosOrNullOrEmpty);
                if (allNullsOrZeros) {
                    throw new SaveDesgloseValidationException(
                        "Error, no existen datos para el período " + extraDataDTO.fecPeriodo.get(periodIndex).format(Constants.FORMATTER_DATE_SHORT));
                }
                periodIndex++;
            }
        }
    }

    /**
     * Edit a desglose.
     *
     * @param desgloseDTO
     * 		the entity to edit
     * @param isNewApprobationProcess
     *
     * @return the persisted entity
     */
    public DesgloseDTO edit(@NotNull DesgloseDTO desgloseDTO, @NotNull Boolean req2Firma, @NotNull Boolean isNewApprobationProcess) {
        log.info("INICIO - Request to edit Desglose");

        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(desgloseDTO.toString());
        final String userAuthority = SecurityUtils.getCurrentUserAuthority().orElseThrow(() -> new InternalServerErrorException("Current user has no authority"));
        final String currentUserOperatorCode = this.getCurrentUserOperatorCode(user.getLoginName());

        NewCommonPK id = NewCommonPK.newInstance(desgloseDTO);

        final Optional<DesgloseDTO> one = findOne(id,null);
        if (!one.isPresent()) {
            throw new BadRequestAlertException("Invalid id. Entity not found", ENTITY_NAME, "idNotFound");
        } else {
            // TODO: Private Function esPosibleEditar() As Boolean línea 1994
            DesgloseDTO desglose = one.get();
            // Si no lanza una excepción se puede editar el desglose por el usuario autenticado.
            this.esPosibleEditar(id.getCodCliente(), id.getCodTipoDes(), id.getCodSubtipoDes(), id.getCodMoneda(), id.getCodPlantilla(), desglose.getCodEstado(),
                currentUserOperatorCode, desglose.getNomUserOpen(), user.getLoginName(), userAuthority);
            if (isNewApprobationProcess) {
                desglose.setNomUserOpen(user.getLoginName());
                desglose.setFlgValidacion(null);
                desglose.setFlgRevision(null);
                desglose.setGlsComentValid(null);
                desglose.setRolEmpleadoDesDef(null);
                desglose.setNomRevisorDef(null);
                desglose.setRolEmpleadoDefEnvPdf(null);
                desglose.setRolEmpleadoRevMod(null);
                desglose.setFecDesgloEnvPdf(null);
                desglose.setNomUser(user.getFullName());
                desglose.setNomUserModif(null);
                desglose.setFecUserModif(null);
                desglose.setNumDevolEjecutor(null);
                desglose.setFecDesgloDef(null);
            }
            desglose.setNomUserModif(null);
            desglose.setFecUserModif(null);
            desglose.setNomUserOpen(user.getLoginName());
            desglose.setNomUser(user.getFullName());
            desglose.setCodEstado(desgloseDTO.getCodEstado());
            desglose.setNumRolChgEstado(user.getUserId());
            desglose.setFlgReq2firma(req2Firma ? Constants.COD_REQ_2_FIRMA_SI : Constants.COD_REQ_2_FIRMA_NO);

            Desglose desgloseToSave = this.desgloseMapper.toEntity(desglose);
            this.desgloseRepository.save(desgloseToSave);

            // Actualizar o eliminar estado en notificación desglose.
            final DesgloseRevision desgloseRevision1 = desgloseRevisionService
                .busquedaRevision(id.getCodCliente(), id.getCodTipoDes(), id.getCodSubtipoDes(), id.getCodMoneda(), id.getCodPlantilla(), COD_REG_STATUS_ENVIA_REE);
            final DesgloseRevision desgloseRevision = desgloseRevision1 != null ? desgloseRevision1 : null;
            if (desgloseRevision != null) {
                /*if (desgloseRevision.getRolDigitador().equals(currentUserOperatorCode)) {*/
                desgloseRevision.setCodRegStatus(COD_REG_STATUS_VIGENTE_REE);
                this.desgloseRevisionService.updateDeshabilita(desgloseRevision);
                /*}*/
            }

            if (isNewApprobationProcess) {
                this.desgloseMensajeRepository
                    .deleteAllByCodClienteAndCodPlantillaAndCodTipoDesAndCodSubtipoDesAndCodMoneda(id.getCodCliente(), id.getCodPlantilla(), id.getCodTipoDes(),
                        id.getCodSubtipoDes(), id.getCodMoneda());
            }

            DesgloseDTO dto = this.findOne(id,null).get();
            if (dto == null) {
                throw new SaveDesgloseValidationException("Error. El Desglose no existe.");
            }
            if (dto.getCodEstado() != null) { // Si tiene un código de estado.
                final DescripcionCodigo status = descripcionCodigoRepository
                    .findFirstByCodTablaAndCodGenericoStartingWithAndGlsGenericacortaAndCodRegStatus(Constants.CODTABLA_ESTADO_DESGLOSE, Constants.COD_GENERICO_DESGLOSE,
                        dto.getCodEstado(), Constants.COD_REG_STATUS_ZERO);
                dto.setGlsEstado(status.getGlsGenericalarga());
            }
            saveDesgloseAudit(id.getCodCliente(), id.getCodTipoDes(), id.getCodSubtipoDes(), id.getCodMoneda(), id.getCodPlantilla(), Constants.NUM_LINEA_AUDITORIA,
                Constants.EVENTO_AUDITORIA_CAMBIO_ESTADO, Constants.ESTADO_EN_EDICION, desglose.getCodEstado(), userAuthority, currentUserOperatorCode,
                Constants.DESGLO_AUDIT_COMENTARIO_EN_EDICION);

            dto.setFlgReq2firma(req2Firma.toString());
            dto.setNomUserAnalis(user.getFullName());
            log.info("FIN - Request to edit Desglose");
            return dto;
        }
    }

    /**
     * Get all the desgloses.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseDTO> findAll(Pageable pageable) {
        log.info("Request to get all Desgloses");
        return desgloseRepository.findAll(pageable).map(desgloseMapper::toDto);
    }

    /**
     * Get one desglose by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseDTO> findOne(NewCommonPK id,String esquema) {
        log.info("Request to get Desglose : {}", id);
        Optional<DesgloseDTO> desgloseDTOOptional = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            desgloseDTOOptional = desgloseRepository.findById(id).map(desgloseMapper::toDto);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            desgloseDTOOptional = desgloseCifRepository.findById(id).map(desgloseCifMapper::toDto);
        }
        return desgloseDTOOptional;
    }

    /**
     * Delete the desglose by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(NewCommonPK id) {
        log.info("Request to delete Desglose : {}", id);
        desgloseRepository.deleteById(id);
    }

    public void deleteIsTemporal() {
        log.info("Request to delete Desglose  temporales: {}");
        desgloseRepository.deleteByFlgIsTemporalAndNomUserOpen(true,getTurbineUserDTOAndSecurityCheck("").getLoginName());
    }

    @Transactional(readOnly = true)
    public List<BreakdownFilterResponseDTO> filterBreakdown(BreakdownFilterRequestDTO filtroDesgloseDTO) {
        log.info("INICIO - filterBreakdown: {}", filtroDesgloseDTO);
        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(filtroDesgloseDTO.toString());
        final String userAuthority = SecurityUtils.getCurrentUserAuthority().orElseThrow(() -> new InternalServerErrorException("Current user has no authority"));
        final String currentUserOperatorCode = this.getCurrentUserOperatorCode(user.getLoginName());
        final ArrayList<BreakdownFilterResponseDTO> list = new ArrayList<>();
        StringBuilder filter;
        final DescripcionCodigoDTO cantRegConsAgenda = descripcionCodigoService.findCantRegConsAgenda();
        if (Constants.BREAKDOWN_QUERY_UI_ORIGIN_REVIEW_PAGE.equals(filtroDesgloseDTO.tipoFiltro)) {
            List<String> rol1FirRee = new LinkedList<>();
            List<String> rol2FirRee = new LinkedList<>();
            int totRee = -1;
            String rol2Firma = "";
            String ln2Firma = "";
            String rolesEmpleado = currentUserOperatorCode;
            // determina a quienes reemplazo
            filter = new StringBuilder(this.SQL_QUERY_DETERMINA_QUIENES_REEMPLAZO_START).append(currentUserOperatorCode).append(SQL_QUERY_DETERMINA_QUIENES_REEMPLAZO_END);
            List<Object[]> result = this.SQLDGLRepository.executeNativeSelectQuery(filter.toString());
            log.debug("QUERY DETERMINA A QUIENES REEMPLAZO : " + filter.toString());
            // obtiene las primeras firmas para quien reemplazo y que es 2da firma
            for (Object elem : result) {
                totRee = totRee + 1;
                rolesEmpleado = rolesEmpleado + ", " + elem.toString();
                rol1FirRee.add(elem.toString());
                Map<String, Object> params1 = new HashMap<>();
                params1.put("numRolEmpleado", currentUserOperatorCode);
                log.debug("QUERY OBTIENE SEGUNDA FIRMA : " + this.SQL_QUERY_OBTIENE_2FIRMA);
                log.debug("PARAMETROS : " + params1);
                List<Object[]> result1 = this.SQLDGLRepository.execute(this.SQL_QUERY_OBTIENE_2FIRMA, params1);
                for (Object elem1 : result1) {
                    rol2FirRee.add(elem1.toString());
                }
            }
            // obtiene mi primera firma
            Map<String, Object> params = new HashMap<>();
            params.put("numRolEmpleado", currentUserOperatorCode);
            log.debug("QUERY OBTIENE PRIMERA FIRMA : " + this.SQL_QUERY_OBTIENE_PRIMERA_FIRMA);
            log.debug("PARAMETROS : " + params);
            List<Object[]> resultados = this.SQLDGLRepository.execute(this.SQL_QUERY_OBTIENE_PRIMERA_FIRMA, params);
            for (Object[] elem : resultados) {
                rol2Firma = elem[0].toString();
                ln2Firma = elem[1].toString();
            }
            // valida si segunda firma esta siendo reemplazado
            BigDecimal cont;
            params = new HashMap<>();
            params.put("CODEOPERATOR", currentUserOperatorCode);
            log.debug("QUERY VALIDA REEMPLAZO SEGUNDA FIRMA : " + this.SQL_QUERY_VALIDA_REEMPLAZO_2FIRMA);
            log.debug("PARAMETROS : " + params);
            cont = this.SQLDGLRepository.countSql(this.SQL_QUERY_VALIDA_REEMPLAZO_2FIRMA, params);

            int codStatus = 0;
            if (userAuthority.equals(PLAT_EJECUTOR) || (userAuthority.equals(PLAT_REVISOR) && filtroDesgloseDTO.codEstado.equals(ESTADO_BORRADOR_OBSERVACION))) {
                codStatus = 2;
            } else {
                if (filtroDesgloseDTO.codEstado.equals(ESTADO_DEFINITIVO) || filtroDesgloseDTO.codEstado.equals(ESTADO_CORREGIDO)) {
                    codStatus = 3;
                }
            }
            filter = new StringBuilder(this.SQL_QUERY_FILTRO_REVISION_START_1).append(codStatus).append(SQL_QUERY_FILTRO_REVISION_START_2);
            if (filtroDesgloseDTO.rut == null) {
                filtroDesgloseDTO.rut = "";
            }
            if (filtroDesgloseDTO.rutVerificationNumber == null) {
                filtroDesgloseDTO.rutVerificationNumber = "";
            }
            // filtro por analista
            boolean flg = false;
            if (userAuthority.equals(PLAT_JEFE) || userAuthority.equals(PLAT_REVISOR)) {
                if (filtroDesgloseDTO.nomAnalista != null && !filtroDesgloseDTO.nomAnalista.isEmpty()) {
                    flg = true;
                    filter.append(" AND (SF_STF_OPERATOR.OPERATOR_CODE = '" + filtroDesgloseDTO.nomAnalista + "'");
                } else {
                    if (userAuthority.equals(PLAT_REVISOR) && (filtroDesgloseDTO.rut == null || filtroDesgloseDTO.rut.isEmpty()) && (filtroDesgloseDTO.rutVerificationNumber == null
                        || filtroDesgloseDTO.rutVerificationNumber.isEmpty())) {
                        flg = true;
                        filter.append(" AND ( ");
                        filter.append(
                            " (DESGLO_REV_WK.ROL_REVISOR IN (" + rolesEmpleado + ") OR (DESGLO_REV_WK.ROL_REVISOR IS NULL))");
                        filter.append(" OR (SF_STF_OPERATOR.OPERATOR_CODE IN (SELECT DES_NUM_ROL_EMPLEADO FROM DESGLO_GRUPO_WK WHERE NUM_ROL_EMPLEADO " + " IN (" + rolesEmpleado
                            + ")) OR DESGLO_REV_WK.ROL_REVISOR IN ( " + currentUserOperatorCode + "))");
                        filter.append(" OR (DESGLO_REV_WK.ROL_REVISOR = '" + currentUserOperatorCode + "' AND DESGLO_REV_WK.ROL_DIGITADOR ='" + rol2Firma + "')");
                        // se agregan reemplazos de segunda firma
                        if (cont.intValue() > 0) {
                            for (int i = 0; i < totRee; i++) {
                                filter.append(
                                    " OR (DESGLO_REV_WK.ROL_REVISOR =" + rol1FirRee.get(i).toString() + "AND DESGLO_REV_WK.ROL_DIGITADOR ='" + rol2FirRee.get(i).toString() + "')");
                            }
                        }
                    }
                }

            } else {
                if (filtroDesgloseDTO.codEstado != null && !filtroDesgloseDTO.codEstado.isEmpty()) {
                    if (filtroDesgloseDTO.codEstado.equals(ESTADO_BORRADOR_VALIDADO)) {
                        filter.append(
                            "  AND (DESGLO_REV_WK.ROL_DIGITADOR ='" + currentUserOperatorCode + "' OR NVL(DESGLO_WK.NOM_USER_MODIF,NOM_USER_CREA) = '" + user.getLoginName()
                                + "')");
                    } else {
                        flg = true;
                        filter.append("  AND ((SF_STF_OPERATOR.OPERATOR_CODE = '" + currentUserOperatorCode + "' OR ROL_DIGITADOR = '" + currentUserOperatorCode + "')");
                    }
                }
            }
            // filtro segun estado
            if (filtroDesgloseDTO.codEstado != null && !filtroDesgloseDTO.codEstado.isEmpty()) {
                if (filtroDesgloseDTO.codEstado.equals(ESTADO_DEFINITIVO) || filtroDesgloseDTO.codEstado.equals(ESTADO_CORREGIDO)) {
                    // filter.append(" AND DESGLO_WK.ROL_EMPLEADO_DEF_ENV_PDF IS NULL AND ROWNUM <= 50");
                }
                if (filtroDesgloseDTO.codEstado.equals(ESTADO_BORRADOR_VALIDADO)) {
                    if (userAuthority.equals(PLAT_REVISOR) && filtroDesgloseDTO.rut == null && filtroDesgloseDTO.rutVerificationNumber == null
                        && filtroDesgloseDTO.nomAnalista == null) {
                        filter.append(" OR NVL(NOM_USER_MODIF,NOM_USER_CREA) IN (SELECT LOGIN_NAME");
                        filter.append(" FROM DESGLO_GRUPO_WK GRP ");
                        filter.append(
                            " INNER JOIN SF_STF_OPERATOR STF ON (GRP.NUM_ROL_EMPLEADO IN (" + currentUserOperatorCode + ") AND STF.OPERATOR_CODE = GRP.DES_NUM_ROL_EMPLEADO)");
                        filter.append(" UNION");
                        filter.append(" select " + ln2Firma + " FROM DUAL ");
                        filter.append(" UNION");
                        filter.append(" select " + user.getLoginName() + " FROM DUAL");
                        filter.append(" )");
                    }
                }
                if (flg) {
                    filter.append(" )");
                }
                filter.append(" AND DESGLO_WK.COD_ESTADO = '" + filtroDesgloseDTO.codEstado + "' ");
            }
            // filtro segun rut
            if (filtroDesgloseDTO.rut != null && !filtroDesgloseDTO.rut.isEmpty() && filtroDesgloseDTO.rutVerificationNumber != null && !filtroDesgloseDTO.rutVerificationNumber
                .isEmpty()) {
                filter.append(" AND DESGLO_WK.COD_CLIENTE = UPPER('" + filtroDesgloseDTO.rut + filtroDesgloseDTO.rutVerificationNumber + "')");
            }
            // fecha de envio a revision
            if (filtroDesgloseDTO.fecEnvRevi1 != null && filtroDesgloseDTO.fecEnvRevi2 != null) {
                filter.append(
                    " AND TRUNC(NVL(FEC_ENVIO_REVISION,TO_DATE('19000101','YYYYMMDD'))) BETWEEN TO_DATE('" + filtroDesgloseDTO.fecEnvRevi1.format(Constants.FORMATTER_DATE_SHORT)
                        + "','DD-MM-YYYY') " + "AND TO_DATE('" + filtroDesgloseDTO.fecEnvRevi2.format(Constants.FORMATTER_DATE_SHORT) + "','DD-MM-YYYY')");
            } else {
                filter.append(" AND TRUNC(NVL(FEC_ENVIO_REVISION,TO_DATE('19000101','YYYYMMDD'))) BETWEEN TO_DATE('19000101','YYYYMMDD') " + "AND TO_DATE('30001231','YYYYMMDD')");
            }
            // fecha de inicio a revision
            if (filtroDesgloseDTO.fecRevi1 != null && filtroDesgloseDTO.fecRevi2 != null) {
                filter.append(" AND TRUNC(NVL(FEC_DESGLO_DEF,TO_DATE('19000101','YYYYMMDD'))) BETWEEN TO_DATE('" + filtroDesgloseDTO.fecRevi1.format(Constants.FORMATTER_DATE_SHORT)
                    + "','DD-MM-YYYY') " + "AND TO_DATE('" + filtroDesgloseDTO.fecRevi2.format(Constants.FORMATTER_DATE_SHORT) + "','DD-MM-YYYY')");
            } else {
                filter.append(" AND TRUNC(NVL(FEC_DESGLO_DEF,TO_DATE('19000101','YYYYMMDD'))) BETWEEN TO_DATE('19000101','YYYYMMDD') " + "AND TO_DATE('30001231','YYYYMMDD')");
            }
            filter.append(this.SQL_QUERY_FILTRO_REVISION_END);
            if (filtroDesgloseDTO.tipoPlantillaDesglose != null && !filtroDesgloseDTO.tipoPlantillaDesglose.isEmpty()) {
                filter.append(" AND COD_PLANTILLA =" + filtroDesgloseDTO.tipoPlantillaDesglose);
            }
            // Filtro NOMBRE cliente;
            if (filtroDesgloseDTO.nombre != null && !filtroDesgloseDTO.nombre.isEmpty()) {
                filter.append(" AND UPPER(GLS_NOMBRE_CLIENTE) LIKE UPPER('" + filtroDesgloseDTO.nombre + "')");
            }
            if (filtroDesgloseDTO.codEstado != null && !filtroDesgloseDTO.codEstado.isEmpty()) {
                if (filtroDesgloseDTO.codEstado.equals(ESTADO_DEFINITIVO) || filtroDesgloseDTO.codEstado.equals(ESTADO_CORREGIDO)) {
                    filter.append(" AND ROL_EMPLEADO_DEF_ENV_PDF IS NULL ").append(SQL_CONSULTA_ROWNUM).append(cantRegConsAgenda.getGlsGenericacorta());
                }
            }

        } else { // Consulta
            filter = new StringBuilder(this.SQL_QUERY_FILTRO_CONSULTA_START_1);
            // Filtro RUT
            if (filtroDesgloseDTO.rut != null && !filtroDesgloseDTO.rut.isEmpty() && filtroDesgloseDTO.rutVerificationNumber != null && !filtroDesgloseDTO.rutVerificationNumber
                .isEmpty()) {

                filtroDesgloseDTO.rutVerificationNumber = filtroDesgloseDTO.rutVerificationNumber.toUpperCase();
                filter.append(SQL_CONSULTA_COD_CLIENTE_START).append(filtroDesgloseDTO.rut).append(filtroDesgloseDTO.rutVerificationNumber).append(SQL_CONSULTA_COD_CLIENTE_END);
            }
            // Filtro FECHA CREACION
            filter.append(SQL_CONSULTA_FECHA_CREACION_1)
                .append(filtroDesgloseDTO.fecDef1 != null ? filtroDesgloseDTO.fecDef1.format(Constants.FORMATTER_DATE_NO_SEPARATORS) : Constants.DEFAULT_START_DATE_FORMATTED)
                .append(SQL_CONSULTA_FECHA_CREACION_2)
                .append(filtroDesgloseDTO.fecDef2 != null ? filtroDesgloseDTO.fecDef2.format(Constants.FORMATTER_DATE_NO_SEPARATORS) : Constants.DEFAULT_END_DATE_FORMATTED)
                .append(SQL_CONSULTA_FECHA_CREACION_3);
            // Filtro FECHA MODIFICACIÓN
            filter.append(SQL_CONSULTA_FECHA_MODIFICACION_1)
                .append(filtroDesgloseDTO.fecCrea1 != null ? filtroDesgloseDTO.fecCrea1.format(Constants.FORMATTER_DATE_NO_SEPARATORS) : Constants.DEFAULT_START_DATE_FORMATTED)
                .append(SQL_CONSULTA_FECHA_MODIFICACION_2)
                .append(filtroDesgloseDTO.fecCrea2 != null ? filtroDesgloseDTO.fecCrea2.format(Constants.FORMATTER_DATE_NO_SEPARATORS) : Constants.DEFAULT_END_DATE_FORMATTED)
                .append(SQL_CONSULTA_FECHA_MODIFICACION_3);
            // Filtro Tipo Plantilla Desglose
            if (filtroDesgloseDTO.tipoPlantillaDesglose != null && !filtroDesgloseDTO.tipoPlantillaDesglose.trim().isEmpty()) {
                filter.append(SQL_CONSULTA_COD_PLANTILLA).append(filtroDesgloseDTO.tipoPlantillaDesglose);
            }
            // Filtro TIPO DESGLOSE
            if (filtroDesgloseDTO.tipoDesglose != null && filtroDesgloseDTO.tipoDesglose != -1) {
                filter.append(SQL_CONSULTA_COD_TIPO_DES_START).append(filtroDesgloseDTO.tipoDesglose).append(SQL_CONSULTA_COD_TIPO_DES_END);
            }
            // Filtro SUB-TIPO DESGLOSE y DESCRIPCIÓN
            if (filtroDesgloseDTO.subTipoDesglose != null && filtroDesgloseDTO.subTipoDesglose != -1) {
                /*if (filtroDesgloseDTO.subTipoDesgloseDesc == null || filtroDesgloseDTO.subTipoDesgloseDesc.trim().isEmpty()) {
                    filtroDesgloseDTO.subTipoDesgloseDesc = "";
                }*/
                filter.append(SQL_CONSULTA_COD_SUB_TIPO_DES_START).append(filtroDesgloseDTO.subTipoDesglose).append(SQL_CONSULTA_COD_SUB_TIPO_DES_END)
                /*.append(filtroDesgloseDTO.subTipoDesgloseDesc).append(SQL_FILTRO_SUB_TIPO_DESGLOSE_END)*/;
            }
            // Filtro MONEDA
            if (filtroDesgloseDTO.moneda != null && filtroDesgloseDTO.moneda != -1) {
                filter.append(SQL_CONSULTA_COD_MONEDA_START).append(filtroDesgloseDTO.moneda).append(SQL_CONSULTA_COD_MONEDA_END);
            }
            // Filtro ESTADO
            if (filtroDesgloseDTO.codEstado != null && !filtroDesgloseDTO.codEstado.isEmpty()) {
                filter.append(SQL_CONSULTA_COD_ESTADO_START).append(filtroDesgloseDTO.codEstado).append(SQL_CONSULTA_COD_ESTADO_END);
            }
            // Filtro ANALISTA
            if (filtroDesgloseDTO.nomAnalista != null && !filtroDesgloseDTO.nomAnalista.trim().isEmpty()) {
                filter.append(SQL_CONSULTA_NOM_USER_CREA_START).append(filtroDesgloseDTO.nomAnalista).append(SQL_CONSULTA_NOM_USER_CREA_END);
            }
            // Filtro Usuario Modificacion
            if (filtroDesgloseDTO.nomUserModification != null && !filtroDesgloseDTO.nomUserModification.trim().isEmpty()) {
                filter.append(SQL_CONSULTA_NOM_USER_MODIF_START).append(filtroDesgloseDTO.nomUserModification).append(SQL_CONSULTA_NOM_USER_MODIF_END);
            }

            filter.append(SQL_QUERY_FILTRO_CONSULTA_START_2);
            filter.append(SQL_QUERY_FILTRO_CONSULTA_START_3);

            // Filtro NOMBRE;
            if (filtroDesgloseDTO.nombre != null && !filtroDesgloseDTO.nombre.isEmpty()) {
                filter.append(SQL_CONSULTA_NOMBRE_START).append(filtroDesgloseDTO.nombre).append(SQL_CONSULTA_NOMBRE_END);
            }

            // TODO: Hacer esto como bean de sesión
            // CARGA CANTIDAD DE REGISTRO A CARGAR EN CONSULTA AGENDA
            filter.append(SQL_CONSULTA_ROWNUM).append(cantRegConsAgenda.getGlsGenericacorta());

            /*
            // Filtro FECHA MODIFICACIÓN
            if (filtroDesgloseDTO.fecCrea1 != null) {
                filter.append(SQL_FILTRO_FECHA_MODIFICACION1_START).append(filtroDesgloseDTO.fecCrea1.format(Constants.FORMATTER_DATE_SHORT))
                    .append(SQL_FILTRO_FECHA_MODIFICACION1_END);
            }
            if (filtroDesgloseDTO.fecCrea2 != null) {
                filter.append(SQL_FILTRO_FECHA_MODIFICACION2_START).append(filtroDesgloseDTO.fecCrea2.format(Constants.FORMATTER_DATE_SHORT))
                    .append(SQL_FILTRO_FECHA_MODIFICACION2_END);
            }
            // Filtro FECHA DESGLOSE (DEFLACTOR BASE)
            if (filtroDesgloseDTO.fecDef1 != null) {
                filter.append(SQL_FILTRO_FECHA_DESGLOSE1_START).append(filtroDesgloseDTO.fecDef1.format(Constants.FORMATTER_DATE_SHORT)).append(SQL_FILTRO_FECHA_DESGLOSE1_END);
            }
            if (filtroDesgloseDTO.fecDef2 != null) {
                filter.append(SQL_FILTRO_FECHA_DESGLOSE2_START).append(filtroDesgloseDTO.fecDef2.format(Constants.FORMATTER_DATE_SHORT)).append(SQL_FILTRO_FECHA_DESGLOSE2_END);
            }
            */
            /*// Filtro SECTOR
            if (filtroDesgloseDTO.sector != null && filtroDesgloseDTO.sector != -1) {
                // TODO: ANTONIO -> Comentando este filtro, ya que la query da error si se agrega.
                filter.append(SQL_FILTRO_SECTOR_START).append(filtroDesgloseDTO.sector).append(SQL_FILTRO_SECTOR_END);
            }*/
            // ORDER BY FEC_MODIF DESC
            filter.append(SQL_CONSULTA_ORDER_BY);
            // filter.append(" " + SQL_FILTRO_ORDER);
        }

        String query = filter.toString();
        log.debug("QUERY BUSQUEDA : " + query);
        List<Object[]> result = this.SQLDGLRepository.executeNativeSelectQuery(query);
        if (Constants.BREAKDOWN_QUERY_UI_ORIGIN_REVIEW_PAGE.equals(filtroDesgloseDTO.tipoFiltro)) {
            for (Object[] elem : result) {
                BreakdownFilterResponseDTO breakdownFilterResponseDTO = new BreakdownFilterResponseDTO();
                breakdownFilterResponseDTO.nombreAnalista = elem[0] != null ? elem[0].toString() : null;
                breakdownFilterResponseDTO.codCliente = elem[1] != null ? elem[1].toString() : null;
                breakdownFilterResponseDTO.codClienteFormatted = elem[2] != null ? elem[2].toString() : null;
                breakdownFilterResponseDTO.codPlantillaDesglose = elem[3] != null ? elem[3].toString() : null;
                breakdownFilterResponseDTO.glsPlantillaDesglose = elem[4] != null ? elem[4].toString() : null;
                breakdownFilterResponseDTO.codTipoDes = elem[5] != null ? elem[5].toString() : null;
                breakdownFilterResponseDTO.glsTipoDes = elem[6] != null ? elem[6].toString() : null;
                breakdownFilterResponseDTO.codSubTipoDes = elem[7] != null ? elem[7].toString() : null;
                breakdownFilterResponseDTO.glsSubTipoDes = elem[8] != null ? elem[8].toString() : null;
                breakdownFilterResponseDTO.codMoneda = elem[9] != null ? elem[9].toString() : null;
                breakdownFilterResponseDTO.glsMoneda = elem[10] != null ? elem[10].toString() : null;
                breakdownFilterResponseDTO.nomCliente = elem[11] != null ? elem[11].toString() : null;
                breakdownFilterResponseDTO.glsEstado = elem[12] != null ? elem[12].toString() : null;
                Timestamp dateValue = elem[13] != null ? (Timestamp) elem[13] : null;
                breakdownFilterResponseDTO.fechaEnvioRevision = dateValue != null ? dateValue.toLocalDateTime() : null;
                dateValue = elem[14] != null ? (Timestamp) elem[14] : null;
                breakdownFilterResponseDTO.fechaRevision = breakdownFilterResponseDTO.fechaRevision;
                breakdownFilterResponseDTO.fechaDesgloDef = breakdownFilterResponseDTO.fechaRevision;
                breakdownFilterResponseDTO.codEstado = elem[15] != null ? elem[15].toString() : null;
                breakdownFilterResponseDTO.nombreRevisor = elem[17] != null ? elem[17].toString() : null;
                breakdownFilterResponseDTO.rolEmpleadoDefEnvPdf = elem[18] != null ? elem[18].toString() : null;
                breakdownFilterResponseDTO.codActividad = elem[22] != null ? elem[22].toString() : null;
                breakdownFilterResponseDTO.codUnidadMon = elem[23] != null ? elem[23].toString() : null;
                // breakdownFilterResponseDTO.glsSector = elem[30] != null ? elem[30].toString() : null;
                // loadPeriodsBreakdownFilter(breakdownFilterResponseDTO);
                list.add(breakdownFilterResponseDTO);
            }
        } else {
            for (Object[] elem : result) {
                BreakdownFilterResponseDTO breakdownFilterResponseDTO = new BreakdownFilterResponseDTO();
                breakdownFilterResponseDTO.nombreAnalista = elem[0] != null ? elem[0].toString() : null;
                breakdownFilterResponseDTO.codClienteFormatted = elem[1] != null ? elem[1].toString() : null;
                breakdownFilterResponseDTO.nomCliente = elem[2] != null ? elem[2].toString() : null;
                breakdownFilterResponseDTO.glsPlantillaDesglose = elem[3] != null ? elem[3].toString() : null;
                breakdownFilterResponseDTO.glsTipoDes = elem[4] != null ? elem[4].toString() : null;
                breakdownFilterResponseDTO.glsSubTipoDes = elem[5] != null ? elem[5].toString() : null;
                breakdownFilterResponseDTO.glsMoneda = elem[6] != null ? elem[6].toString() : null;
                breakdownFilterResponseDTO.glsEstado = elem[7] != null ? elem[7].toString() : null;
                Timestamp dateValue = (Timestamp) elem[8];
                breakdownFilterResponseDTO.fechaDesglose = dateValue != null ? dateValue.toLocalDateTime() : null;
                breakdownFilterResponseDTO.userCreacion = elem[9] != null ? elem[9].toString() : null;
                breakdownFilterResponseDTO.nombreUserCreacion = elem[10] != null ? elem[10].toString() : null;
                dateValue = (Timestamp) elem[11];
                breakdownFilterResponseDTO.fechaLastUpdate = dateValue != null ? dateValue.toLocalDateTime() : null;
                breakdownFilterResponseDTO.userUpdate = elem[12] != null ? elem[12].toString() : null;
                breakdownFilterResponseDTO.nombreUserUpdate = elem[13] != null ? elem[13].toString() : null;
                dateValue = (Timestamp) elem[14];
                breakdownFilterResponseDTO.fechaDesgloDef = dateValue != null ? dateValue.toLocalDateTime() : null;
                breakdownFilterResponseDTO.nombreRevisorDef = elem[15] != null ? elem[15].toString() : null;
                breakdownFilterResponseDTO.nombreRevisorModif = elem[16] != null ? elem[16].toString() : null;
                breakdownFilterResponseDTO.codCliente = elem[17] != null ? elem[17].toString() : null;
                breakdownFilterResponseDTO.codTipoDes = elem[18] != null ? elem[18].toString() : null;
                breakdownFilterResponseDTO.codSubTipoDes = elem[19] != null ? elem[19].toString() : null;
                breakdownFilterResponseDTO.codMoneda = elem[20] != null ? elem[20].toString() : null;
                breakdownFilterResponseDTO.codEstado = elem[21] != null ? elem[21].toString() : null;
                breakdownFilterResponseDTO.codActividad = elem[22] != null ? elem[22].toString() : null;
                breakdownFilterResponseDTO.flgTipoCliente = elem[23] != null ? elem[23].toString() : null;
                breakdownFilterResponseDTO.codPlantillaDesglose = elem[24] != null ? elem[24].toString() : null;
                breakdownFilterResponseDTO.codUnidadMon = elem[25] != null ? elem[25].toString() : null;
                breakdownFilterResponseDTO.userAnalista = elem[26] != null ? elem[26].toString() : null;
                breakdownFilterResponseDTO.userOpen = elem[27] != null ? elem[27].toString() : null;
                breakdownFilterResponseDTO.nombreUserOpen = elem[28] != null ? elem[28].toString() : null;
                breakdownFilterResponseDTO.glsObservacion = elem[29] != null ? elem[29].toString() : null;
                /*BigDecimal value = (BigDecimal) elem[2];
                breakdownFilterResponseDTO.isEmpresa = value != null ? BooleanUtils.toBoolean(value.intValue()) : false;
                value = (BigDecimal) elem[3];
                breakdownFilterResponseDTO.hasRut = value != null ? BooleanUtils.toBoolean(value.intValue()) : false;
                breakdownFilterResponseDTO.codSector = elem[5] != null ? elem[5].toString() : null;*/
                breakdownFilterResponseDTO.glsSector = elem[30] != null ? elem[30].toString() : null;
                /*breakdownFilterResponseDTO.codMonedaImp = elem[13] != null ? elem[13].toString() : null;
                breakdownFilterResponseDTO.glsMonedaImp = elem[14] != null ? elem[14].toString() : null;
                breakdownFilterResponseDTO.glsUnidadMon = elem[16] != null ? elem[16].toString() : null;*/

                if (filtroDesgloseDTO.loadPeriodsDates) {
                    loadPeriodsBreakdownFilter(breakdownFilterResponseDTO);
                }

                list.add(breakdownFilterResponseDTO);
            }
        }
        log.info("FIN - filterBreakdown");
        return list;
    }

    /**
     * Carga las fechas de los períodos en la búqueda de desglose.
     *
     * @param breakdownFilterResponseDTO
     */
    private void loadPeriodsBreakdownFilter(BreakdownFilterResponseDTO breakdownFilterResponseDTO) {
        /**
         * Carga PERIODOS
         */
        log.info("INICIO - filterDesgloses - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");
        Sort orderBy = Sort.by(Sort.Direction.ASC, Constants.PROPERTY_NAME_FECH_PERIODO);
        PeriodoRequestDTO periodoRequestDTO = new PeriodoRequestDTO();
        periodoRequestDTO.codCliente = breakdownFilterResponseDTO.codCliente;
        periodoRequestDTO.tipoDesglose = Integer.valueOf(breakdownFilterResponseDTO.codTipoDes);
        periodoRequestDTO.subTipoDesglose = Integer.valueOf(breakdownFilterResponseDTO.codSubTipoDes);
        periodoRequestDTO.moneda = breakdownFilterResponseDTO.codMoneda;
        periodoRequestDTO.codPlantilla = Integer.valueOf(breakdownFilterResponseDTO.codPlantillaDesglose);
        final List<PeriodoDTO> periods = periodoService.findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodTipoPlantilla(periodoRequestDTO,orderBy);
        if (periods != null && periods.size() > 0) {
            breakdownFilterResponseDTO.fechaPeriodos = new ArrayList<>();
            for (PeriodoDTO period : periods) {
                breakdownFilterResponseDTO.fechaPeriodos.add(period.getFecPeriodo());
            }
        }
        log.info("FIN - filterDesgloses - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");
    }

    @Transactional(readOnly = true)
    public List<BreakdownSearchResponseDTO> searchBreakdown(BreakdownSearchRequestDTO searchRequestDTO,String esquema) {
        log.info("INICIO - searchBreakdown:{} ",searchRequestDTO);
        final ArrayList<BreakdownSearchResponseDTO> list = new ArrayList<>();
        List<Object> params = initializeBasicFilterQueryPrams(searchRequestDTO, true);
        List<Object[]> result = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            log.debug("QUERY : " + this.SQL_QUERY_BUSQUEDA_DESGLOSE_WK);
            log.debug("PARAMETROS : " + params.toString());
            result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_QUERY_BUSQUEDA_DESGLOSE_WK, params);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            log.debug("QUERY : " + this.SQL_QUERY_BUSQUEDA_DESGLOSE);
            log.debug("PARAMETROS : " + params.toString());
            result = this.sqlcifRepository.executeNativeSelectQuery(this.SQL_QUERY_BUSQUEDA_DESGLOSE, params);
        }
        if (result != null && result.size() > 0) {
            for (Object[] elem : result) {
                BreakdownSearchResponseDTO dto = new BreakdownSearchResponseDTO();
                dto.codUnidadMon = elem[0] != null ? elem[0].toString() : null;
                dto.glsTipoDes = elem[1] != null ? elem[1].toString() : null;
                dto.valDeflacBase = (BigDecimal) elem[2];
                Timestamp dateValue = (Timestamp) elem[3];
                dto.fecDeflacBase = dateValue != null ? dateValue.toLocalDateTime() : null;
                dto.valIva = (BigDecimal) elem[4];
                dto.glsObservacion = elem[5] != null ? elem[5].toString() : null;
                dto.nomUserCrea = elem[6] != null ? elem[6].toString() : null;
                dto.userModif = elem[7] != null ? elem[7].toString() : null;
                dateValue = (Timestamp) elem[8];
                dto.fechaModif = dateValue != null ? dateValue.toLocalDateTime() : null;
                dto.userOpen = elem[9] != null ? elem[9].toString() : null;
                BigDecimal value = (BigDecimal) elem[10];
                dto.flgIsTemporal = value != null && BooleanUtils.toBoolean(value.intValue());
                dto.codMonedaImp = elem[11] != null ? elem[11].toString() : null;
                dto.nomUser = elem[12] != null ? elem[12].toString() : null;
                dto.glsEjecutor = elem[13] != null ? elem[13].toString() : null;
                Integer strValue = elem[14] != null ? Integer.valueOf(elem[14].toString()) : null;
                dto.flgReq2Firma = value != null && BooleanUtils.toBoolean(strValue);
                dto.usrEjecutor = elem[15] != null ? elem[15].toString() : null;
                dto.nomUserOpen = elem[16] != null ? elem[16].toString() : null;
                dto.codEstado = elem[17] != null ? elem[17].toString() : null;
                dto.glsEstado = elem[18] != null ? elem[18].toString() : null;
                dto.glsSubtipoDes = elem[1] != null ? elem[1].toString() : null;
                list.add(dto);
            }
        }
        // llamar a CIF_SP_TAKE_DESGLO_ROL
        log.info("FIN - searchBreakdown ");
        return list;
    }

    /**
     * Inicialización de parámetros de la query.
     *
     * @param searchRequestDTO
     * @param addTemplateTypeParam
     *
     * @return
     */
    @org.jetbrains.annotations.NotNull
    private List<Object> initializeBasicFilterQueryPrams(BreakdownSearchRequestDTO searchRequestDTO, Boolean addTemplateTypeParam) {
        List<Object> params = new ArrayList<>();
        params.add(searchRequestDTO.codCliente.toUpperCase()); // Filtro COD_CLIENTE
        if (addTemplateTypeParam) {
            params.add(searchRequestDTO.tipoPlantillaDesglose);// Filtro COD_PLANTILLA
        }
        params.add(searchRequestDTO.tipoDesglose); // Filtro COD_TIPO_DES
        params.add(searchRequestDTO.subTipoDesglose); // Filtro COD_SUBTIPO_DES
        params.add(searchRequestDTO.moneda); // Filtro COD_MONEDA
        return params;
    }

    @Transactional(readOnly = true)
    public List<BreakdownSearchResponseDTO> searchDesglosesCreatedFromSp(BreakdownSearchRequestDTO searchRequestDTO,String esquema) {
        log.info("INICIO - searchDesglosesCreatedFromSp");
        final ArrayList<BreakdownSearchResponseDTO> list = new ArrayList<>();
        List<Object> params = initializeBasicFilterQueryPrams(searchRequestDTO, true);
        List<Object[]> result = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            log.debug("QUERY :" +this.SQL_QUERY_BUSQUEDA_DESGLOSE_CREADO_SP );
            log.debug("PARAMETROS :" + params.toString() );
            result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_QUERY_BUSQUEDA_DESGLOSE_CREADO_SP, params);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            log.debug("QUERY :" +this.SQL_QUERY_BUSQUEDA_DESGLOSECIF_CREADO_SP );
            log.debug("PARAMETROS :" + params.toString() );
             result = this.sqlcifRepository.executeNativeSelectQuery(this.SQL_QUERY_BUSQUEDA_DESGLOSECIF_CREADO_SP, params);
        }
        if (result != null && result.size() > 0) {
            for (Object[] elem : result) {
                BreakdownSearchResponseDTO dto = new BreakdownSearchResponseDTO();
                dto.userOpen = elem[0] != null ? elem[0].toString() : null;
                BigDecimal value = (BigDecimal) elem[1];
                dto.flgIsTemporal = value != null && BooleanUtils.toBoolean(value.intValue());
                dto.codEstado = elem[2] != null ? elem[2].toString() : null;
                dto.glsEstado = elem[3] != null ? elem[3].toString() : null;
                dto.numRol1Firm = elem[4] != null ? elem[4].toString() : null;
                dto.numRol2Firm = elem[5] != null ? elem[5].toString() : null;
                dto.numRolRee = elem[6] != null ? elem[6].toString() : null;
                dto.valDeflacBase = elem[7] != null ? (BigDecimal) elem[7] : null;
                Timestamp dateValue = (Timestamp) elem[8];
                dto.fecDeflacBase = dateValue != null ? dateValue.toLocalDateTime() : null;
                boolean estado = false;
                if (elem[9] != null && elem[9].toString().equals("1")) {
                    estado = true;
                } else {
                    estado = false;
                }
                dto.flgReq2Firma = estado;
                dto.glsObservacion = elem[10] != null ? elem[10].toString() : null;
                dto.nomUserOpen = elem[11] != null ? elem[11].toString() : null;
                dto.valIva = elem[12] != null ? (BigDecimal) elem[12] : null;
                if (dto.valIva != null) {
                    BigDecimal number = new BigDecimal(100);
                    dto.valIva = dto.valIva.multiply(number);
                }
                if(elem[13] != null ) {
                    TurbineUserDTO turbineUserDTO = turbineUserService.findByLoginName(elem[13].toString());
                    if(turbineUserDTO!=null) {
                        dto.userModif = turbineUserDTO.getFullName();
                    }
                }
                list.add(dto);
            }
        }
        log.info("FIN - searchDesglosesCreatedFromSp");
        return list;
    }

    public void executeCifSpTakeDesgloRol(String codCliente, Integer codTipoDes, Integer codSubTipoDes, String codMoneda, Integer codTipoPlantillaDes) {
        this.log.info("INICIO - executeCifSpTakeDesgloRol");
        // Llenar los parámetros
        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(null);
        final Long userAuthority = user.getUserId();
        if (!(codCliente != null && !codCliente.trim().isEmpty() && codTipoDes != null && codSubTipoDes != null && codMoneda != null && !codMoneda.trim().isEmpty()
            && codTipoPlantillaDes != null)) {
            // Error de parámetros inválidos.
            throw new StorageProcedureBusinessError("Error llamando al SP CIF_SP_TAKE_DESGLO_ROL. Parámetros inválidos.");
        }
        List<Param<?>> parameters = fillExecuteCifSpTakeDesgloRolParams(codCliente, codTipoDes, codSubTipoDes, codMoneda, codTipoPlantillaDes, user.getLoginName(), userAuthority);

        // Interpretar los resultados
        log.debug("QUERY : " + DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL);
        log.debug("PARAMETROS : " + parameters.toString());
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL, parameters);
        log.debug(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_07_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(DesgloseService.SP_CIF_SP_TAKE_DESGLO_ROL_08_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpTakeDesgloRol");
    }

    /**
     * Ejecuta el procedimiento almacenado "CIF_SP_INSUPD_DESGLO_ROL".
     *
     * @param requestDTO
     */
    public void executeCifSpInsUpdDesgloRol(BreakdownSaveRequestDTO requestDTO) {
        this.log.info("INICIO - executeCifSpInsUpdDesgloRol");
        // Llenar los parámetros
        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(null);
        final Long userAuthority = user.getUserId();
        Param<String> p1 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_01_P_COD_CLIENTE, String.class, requestDTO.desgloseDTO.getCodCliente(), ParameterMode.IN);
        Param<Integer> p2 =
            new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_02_P_COD_TIPO_DES, Integer.class, Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes()), ParameterMode.IN);
        Param<Integer> p3 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_03_P_COD_SUBTIPO_DES, Integer.class, Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes()),
            ParameterMode.IN);
        Param<String> p4 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_04_P_COD_MONEDA, String.class, requestDTO.desgloseDTO.getCodMoneda(), ParameterMode.IN);
        Param<String> p5 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_05_P_COD_UNID_MONEDA, String.class, requestDTO.desgloseDTO.getCodUnidMoneda(), ParameterMode.IN);
        Param<String> p6 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_06_P_GLS_SUBTIPO_DES, String.class, requestDTO.desgloseDTO.getGlsSubtipoDes(), ParameterMode.IN);
        Param<BigDecimal> p7 =
            new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_07_P_VAL_DEFLAC_BASE, BigDecimal.class, requestDTO.desgloseDTO.getValDeflacBase(), ParameterMode.IN); //adSingle
        Timestamp fecDeflacBase = Timestamp.valueOf(requestDTO.desgloseDTO.getFecDeflacBase().atStartOfDay());
        // Param<Timestamp> p8 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_08_P_FEC_DEFLAC_BASE, Timestamp.class, fecDeflacBase, ParameterMode.IN);
        Param<LocalDate> p8 =
            new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_08_P_FEC_DEFLAC_BASE, LocalDate.class, requestDTO.desgloseDTO.getFecDeflacBase(), ParameterMode.IN);
        Param<BigDecimal> p9 =
            new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_09_P_VAL_IVA, BigDecimal.class, requestDTO.desgloseDTO.getValIva(), ParameterMode.IN); //adSingle
        Param<String> p10 =
            new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_10_P_GLS_OBSERVACION, String.class, requestDTO.desgloseDTO.getGlsObservacion(), ParameterMode.IN);

        /*If gRolOperador <> ROL_DIGITADOR Then
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_nom_user", adVarChar, adParamInput, 20, loginAnalistaOrig)
        Else
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_nom_user", adVarChar, adParamInput, 20, Usuario)
        End If*/
        Param<String> p11 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_11_P_NOM_USER, String.class, user.getLoginName(), ParameterMode.IN);

        //Si no tiene el parámetro de moneda de impresión se toma la misma moneda del desglose.
        String codMonedaImp = (requestDTO.desgloseDTO.getCodMonedaImp() != null && !requestDTO.desgloseDTO.getCodMonedaImp().trim().isEmpty() && !Constants.COD_MINUS_1_STRING_VALUE
            .equals(requestDTO.desgloseDTO.getCodMonedaImp())) ? requestDTO.desgloseDTO.getCodMonedaImp() : requestDTO.desgloseDTO.getCodMoneda();
        Param<String> p12 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_12_P_COD_MONEDA_IMP, String.class, codMonedaImp, ParameterMode.IN);

        /*If gRolOperador <> ROL_DIGITADOR Then
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_user", adVarChar, adParamInput, 50, nombreAnalistaOrig)
        Else
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_user", adVarChar, adParamInput, 50, Trim(Me.txtUsuario))
        End If*/
        Param<String> p13 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_13_P_USER, String.class, user.getFullName(), ParameterMode.IN);

        String flgValidation = requestDTO.extraDataDTO.validado ? Constants.COD_OK : Constants.COD_NOK;
        Param<String> p14 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_14_P_FLG_VALIDACION, String.class, flgValidation, ParameterMode.IN);

        Param<String> p15 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_15_P_COD_ESTADO, String.class, requestDTO.desgloseDTO.getCodEstado(), ParameterMode.IN);

        /*If gRolOperador <> ROL_DIGITADOR And gLoginName <> loginAnalistaOrig Then
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_rol_emp_rev_mod", adVarChar, adParamInput, 10, Trim(codOperador))
        Else
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_rol_emp_rev_mod", adVarChar, adParamInput, 10, Null)
        End If*/
        Long rolEmpleadoRev = requestDTO.desgloseDTO.getRolEmpleadoRevMod() != null ? Long.parseLong(requestDTO.desgloseDTO.getRolEmpleadoRevMod()) : null;
        Param<Long> p16 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_16_P_ROL_EMPLEADO_REV_MOD, Long.class, rolEmpleadoRev, ParameterMode.IN);

        // cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_rol_empleado", adVarChar, adParamInput, 7, codOperador)
        Param<Long> p17 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_17_P_ROL_EMPLEADO, Long.class, userAuthority, ParameterMode.IN);
        Param<Integer> p18 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_18_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p19 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_19_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<String> p20 = new Param<>(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_20_P_COD_PLANTILLA, String.class, requestDTO.desgloseDTO.getCodPlantilla(), ParameterMode.IN);

        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20);
        log.debug("QUERY : " + DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL );
        log.debug("PARAMETROS : " +  parameters.toString() );
        // Interpretar los resultados
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL, parameters);
        log.debug(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_18_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(DesgloseService.SP_CIF_SP_INSUPD_DESGLO_ROL_19_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpInsUpdDesgloRol");
    }

    /**
     * Ejecuta el procedimiento almacenado "CIF_SP_DES_CLONAR_DESGLOSE".
     *
     * @param requestDTO
     */
    public void executeCifSpDesClonarDesglose(BreakdownCloneRequestDTO requestDTO) {
        this.log.info("INICIO - executeCifSpDesClonarDesglose with BreakdownCloneRequestDTO");
        this.log.debug(requestDTO.toString());
        // Llenar los parámetros
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Param<String> p1 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_01_PI_NUM_IDEN, String.class, requestDTO.codCliente, ParameterMode.IN);
        Param<String> p2 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_02_PI_COD_TIPO_DES_ORI, String.class, requestDTO.codTipoDesOriginal, ParameterMode.IN);
        Param<String> p3 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_03_PI_COD_SUBTIPO_DES_ORI, String.class, requestDTO.codSubtipoDesOriginal, ParameterMode.IN);
        Param<String> p4 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_04_PI_COD_MONEDA_ORI, String.class, requestDTO.codMonedaOriginal, ParameterMode.IN);
        Param<String> p5 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_05_PI_COD_TIPO_DES_DES, String.class, requestDTO.codTipoDes, ParameterMode.IN);
        Param<String> p6 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_06_PI_COD_SUBTIPO_DES_DES, String.class, requestDTO.codSubtipoDes, ParameterMode.IN);
        Param<String> p7 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_07_PI_COD_MONEDA_DES, String.class, requestDTO.codMoneda, ParameterMode.IN);
        Param<String> p8 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_08_PI_LOGIN_NAME, String.class, userLogin, ParameterMode.IN);
        Param<Integer> p9 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_10_PO_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p10 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_11_PO_GLOSA, String.class, null, ParameterMode.OUT);
        Param<String> p11 = new Param<>(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_09_PI_COD_PLANTILLA, String.class, requestDTO.codPlantillaDesgloseOriginal, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
        // Interpretar los resultados
        log.debug("QUERY : " + DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE);
        log.debug("PARAMETROS: " + parameters.toString());
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE, parameters);
        log.debug(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_10_PO_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(DesgloseService.SP_CIF_SP_DES_CLONAR_DESGLOSE_11_PO_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpDesClonarDesglose");
    }

    /**
     * ejecuta el procedimiento CIF_SP_DEL_DESGLO
     *
     * @param requestDTO
     */
    public void executeCifSpDelDesglo(DesgloseDTO requestDTO) {
        this.log.info("INICIO - executeCifSpDelDesglo with delete desglose");
        this.log.debug(requestDTO.toString());
        // Llenar los parámetros
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Param<String> p1 = new Param<>(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_01_PI_COD_CLIENTE, String.class, requestDTO.getCodCliente(), ParameterMode.IN);
        Param<String> p2 = new Param<>(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_02_PI_COD_TIPO_PLANTILLA, String.class, requestDTO.getCodPlantilla(), ParameterMode.IN);
        Param<String> p3 = new Param<>(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_03_PI_COD_TIPO_DES, String.class, requestDTO.getCodTipoDes(), ParameterMode.IN);
        Param<String> p4 = new Param<>(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_04_PI_COD_SUBTIPO_DES, String.class, requestDTO.getCodSubtipoDes(), ParameterMode.IN);
        Param<String> p5 = new Param<>(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_05_PI_COD_MONEDA, String.class, requestDTO.getCodMoneda(), ParameterMode.IN);
        Param<Integer> p6 = new Param<>(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_06_PO_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p7 = new Param<>(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_07_P_GLOSA, String.class, null, ParameterMode.OUT);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7);
        // Interpretar los resultados
        log.debug("QUERY : " + DesgloseService.SP_CIF_SP_DEL_DESGLOSE);
        log.debug("PARAMETROS : " + parameters);
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(DesgloseService.SP_CIF_SP_DEL_DESGLOSE, parameters);
        log.debug(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_06_PO_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + DesgloseService.SP_CIF_SP_DEL_DESGLOSE);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(DesgloseService.SP_CIF_SP_DEL_DESGLOSE_07_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpDelDesglo");
    }

    @SuppressWarnings("Duplicates")
    public BreakdownRecoverResponseDTO recoverBreakdown(BreakdownSearchRequestDTO searchRequestDTO) {
        log.info("INICIO - Request to recover Desglose");

        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        TurbineUserDTO user = null;
        if(userLogin!="anonymousUser") {
              user = getTurbineUserDTOAndSecurityCheck(searchRequestDTO.toString());
        }
        int contador = 0;
        if(user!=null) {
            List<TurbinePermissionDTO> turbinePermissionList = this.turbinePermissionService.findAllPermisosByUser(user.getLoginName());
            for (TurbinePermissionDTO item : turbinePermissionList) {
                if (item.getPermissionName().equals(MDB4)) {
                    contador++;
                }
            }
        }
        /*if(contador==0){
            throw new RecoverDesgloseValidationException("No dispone permisos de consulta de desglose");
        }*/
        // Se hacen validaciones...

        // busqueda del desglose si se encuentra en revision y si es asi se actualiza el flg_notificado a 1
        NewCommonPK id = new NewCommonPK();
        id.setCodTipoDes(searchRequestDTO.tipoDesglose);
        id.setCodSubtipoDes(searchRequestDTO.subTipoDesglose);
        id.setCodMoneda(searchRequestDTO.moneda);
        id.setCodCliente(searchRequestDTO.codCliente);
        id.setCodPlantilla(searchRequestDTO.tipoPlantillaDesglose);
        DesgloseRevision desgloseRevision = desgloseRevisionService
            .busquedaRevision(id.getCodCliente(), id.getCodTipoDes(), id.getCodSubtipoDes(), id.getCodMoneda(), id.getCodPlantilla(), COD_REG_STATUS_ENVIA_REE);
        if (desgloseRevision != null) {
            DesgloseRevision desgloseRevision1 = desgloseRevision;
            if (!desgloseRevision1.getFlgNotificado()) {
                desgloseRevision1.setFlgNotificado(true);
                desgloseRevision1.setFecEnRevision(LocalDate.now());
                desgloseRevisionService.updateRecuperar(desgloseRevision1);
            }
        }
        // ocupa el desglose por el usuario , indicando fecha y hora que es tomado.
        Optional<Desglose> desgloseOptional = desgloseRepository.findById(id);
        if (desgloseOptional.isPresent()) {
                Desglose desglose = desgloseOptional.get();
            if(desglose.getNomUserOpen() == null && user!=null ) {
                desglose.setNomUserOpen(user.getLoginName());
                desglose.setFecHraOpen(LocalDateTime.now());
                desgloseRepository.save(desglose);
            }
        }

        BreakdownRecoverResponseDTO responseDTO = searchBreakdownRecoverResponseDTO(searchRequestDTO,null);

        // Carga conceptos de impresión CONIMP_WK
        log.info("INICIO - recoverBreakdown - Carga conceptos de impresión (CONIMP_WK). [" + Calendar.getInstance().getTime() + "]");
        responseDTO.extraDataDTO.matrizConImp =
            this.conceptoImpresionService.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(ConceptoImpresionRequestDTO.newInstance(searchRequestDTO),searchRequestDTO.tipoPlantillaDesglose,null);
        log.info("FIN - recoverBreakdown - Carga conceptos de impresión (CONIMP_WK). [" + Calendar.getInstance().getTime() + "]");

        /**
         * Carga Formato en la pantalla
         * TODO: Hacer esto...
         */
        // Call LoadFormato

        // Carga PERIODOS
        log.info("INICIO - recoverBreakdown - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");
        Sort orderBy = Sort.by(Sort.Direction.ASC, Constants.PROPERTY_NAME_FECH_PERIODO);
        responseDTO.extraDataDTO.periodos =
            this.periodoService.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(PeriodoRequestDTO.newInstance(searchRequestDTO), orderBy,null);
        if (responseDTO.extraDataDTO.periodos != null && responseDTO.extraDataDTO.periodos.size() > 0) {
            for (PeriodoDTO period : responseDTO.extraDataDTO.periodos) {
                responseDTO.extraDataDTO.monthsPeriod.add(period.getNumMeses());
                responseDTO.extraDataDTO.deflatorPeriod.add(period.getValDeflacPrd());
                responseDTO.extraDataDTO.periodRating.add(period.getCodEstadoAudi());
                responseDTO.extraDataDTO.fecPeriodo.add(period.getFecPeriodo());
                final BreakdownAuditorDTO auditorDTO = new BreakdownAuditorDTO();
                auditorDTO.auditor = this.descripcionCodigoService
                    .findFirstByCodTablaAndCodGenericoAndCodRegStatus(Constants.CODTABLA_AUDITORES, period.getCodAuditor(), Constants.COD_REG_STATUS_ZERO);
                auditorDTO.otherAuditorName = period.getGlsOtrosAudit();
                responseDTO.extraDataDTO.auditors.add(auditorDTO);
            }
        }
        log.info("FIN - recoverBreakdown - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");

        responseDTO.extraDataDTO.numPeriodosLoaded = responseDTO.extraDataDTO.periodos != null ? responseDTO.extraDataDTO.periodos.size() : 0;
        responseDTO.extraDataDTO.numPeriodosConInfo =
            responseDTO.extraDataDTO.numPeriodosLoaded - Constants.MAX_PERIODS_SHOWED < 0 ? 0 : responseDTO.extraDataDTO.numPeriodosLoaded - Constants.MAX_PERIODS_SHOWED;

        // Carga valores VALPRD_WK
        log.info("INICIO - recoverBreakdown - Carga VALPRD_WK. [" + Calendar.getInstance().getTime() + "]");
        for (PeriodoDTO periodo : responseDTO.extraDataDTO.periodos) {
            periodo.valPeriodoDesgloseDTOList = this.valPeriodoDesgloseService
                .findValPeriodoDesgloses(searchRequestDTO.codCliente, searchRequestDTO.tipoDesglose, searchRequestDTO.subTipoDesglose, searchRequestDTO.moneda,
                    periodo.getFecPeriodo(), searchRequestDTO.tipoPlantillaDesglose,null);
        }
        log.info("FIN - recoverBreakdown - Carga VALPRD_WK. [" + Calendar.getInstance().getTime() + "]");

        // Carga CONSOL y PRCCNS (Empresas que consolidan)
        log.info("INICIO - recoverBreakdown - arga CONSOL y PRCCNS (Empresas que consolidan). [" + Calendar.getInstance().getTime() + "]");
        responseDTO.extraDataDTO.empConsolPeriodoDesgConsolList = this.empresaConsolidadaService
            .findEmpresaConsolidadas(searchRequestDTO.codCliente, searchRequestDTO.tipoDesglose, searchRequestDTO.subTipoDesglose, searchRequestDTO.moneda,
                searchRequestDTO.tipoPlantillaDesglose,null);
        log.info("FIN - recoverBreakdown - arga CONSOL y PRCCNS (Empresas que consolidan). [" + Calendar.getInstance().getTime() + "]");
        responseDTO.extraDataDTO.isAutoriced = true; // TODO: Consulta Omar -> Ver este campo cómo se trabaja a la hora de recuperar el desglose.

        responseDTO.codCliente = searchRequestDTO.codCliente;
        responseDTO.codPlantilla = String.valueOf(searchRequestDTO.tipoPlantillaDesglose);
        responseDTO.codTipoDes = String.valueOf(searchRequestDTO.tipoDesglose);
        responseDTO.codSubtipoDes = String.valueOf(searchRequestDTO.subTipoDesglose);
        responseDTO.codMoneda = searchRequestDTO.moneda;
        log.info("FIN - Request to recover Desglose");
        return responseDTO;
    }
    public void deleteAllDesglosesTemporal() {
        log.info("INICIO-- ELIMINA LOS DESGLOSES QUE SE QUEDARON CREADOS DE MANERA TEMPORAL");
        long tokenValidityInMinutes = jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds() / 60;
        List<Desglose> all = desgloseRepository.findAllByFlgIsTemporal(true);
        for(Desglose desglose: all) {
            desgloseRepository.delete(desglose);
        }
        log.info("FIN --  ELIMINA LOS DESGLOSES QUE SE QUEDARON CREADOS DE MANERA TEMPORAL");
    }
    public BreakdownRecoverResponseDTO recoverDatosImpresionBreakdown(BreakdownSearchRequestDTO searchRequestDTO,String esquema) {
        log.info("INICIO - Request to recover Desglose desde el esquema {}",esquema);

        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        TurbineUserDTO user = null;
        if(userLogin!="anonymousUser") {
              user = getTurbineUserDTOAndSecurityCheck(searchRequestDTO.toString());
        }

        BreakdownRecoverResponseDTO responseDTO = searchBreakdownRecoverResponseDTO(searchRequestDTO,esquema);

        // Carga conceptos de impresión CONIMP_WK
        log.info("INICIO - recoverBreakdown - Carga conceptos de impresión (CONIMP_WK). [" + Calendar.getInstance().getTime() + "]");
        responseDTO.extraDataDTO.matrizConImp =
            this.conceptoImpresionService.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(ConceptoImpresionRequestDTO.newInstance(searchRequestDTO),searchRequestDTO.tipoPlantillaDesglose,esquema);
        log.info("FIN - recoverBreakdown - Carga conceptos de impresión (CONIMP_WK). [" + Calendar.getInstance().getTime() + "]");

        // Carga PERIODOS
        log.info("INICIO - recoverBreakdown - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");
        Sort orderBy = Sort.by(Sort.Direction.ASC, Constants.PROPERTY_NAME_FECH_PERIODO);
        responseDTO.extraDataDTO.periodos =
            this.periodoService.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(PeriodoRequestDTO.newInstance(searchRequestDTO), orderBy,esquema);
        if (responseDTO.extraDataDTO.periodos != null && responseDTO.extraDataDTO.periodos.size() > 0) {
            for (PeriodoDTO period : responseDTO.extraDataDTO.periodos) {
                responseDTO.extraDataDTO.monthsPeriod.add(period.getNumMeses());
                responseDTO.extraDataDTO.deflatorPeriod.add(period.getValDeflacPrd());
                responseDTO.extraDataDTO.periodRating.add(period.getCodEstadoAudi());
                responseDTO.extraDataDTO.fecPeriodo.add(period.getFecPeriodo());
                if(period.getFecInformSbif()!= null) {
                    responseDTO.extraDataDTO.fecPeriodoSbif.add(period.getFecInformSbif());
                }
                final BreakdownAuditorDTO auditorDTO = new BreakdownAuditorDTO();
                auditorDTO.auditor = this.descripcionCodigoService
                    .findFirstByCodTablaAndCodGenericoAndCodRegStatus(Constants.CODTABLA_AUDITORES, period.getCodAuditor(), Constants.COD_REG_STATUS_ZERO);
                auditorDTO.otherAuditorName = period.getGlsOtrosAudit();
                responseDTO.extraDataDTO.auditors.add(auditorDTO);
            }
        } else {
            responseDTO.extraDataDTO.periodos = new ArrayList<>();
        }
        log.info("FIN - recoverBreakdown - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");

        responseDTO.extraDataDTO.numPeriodosLoaded = responseDTO.extraDataDTO.periodos != null ? responseDTO.extraDataDTO.periodos.size() : 0;
        responseDTO.extraDataDTO.numPeriodosConInfo =
            responseDTO.extraDataDTO.numPeriodosLoaded - Constants.MAX_PERIODS_SHOWED < 0 ? 0 : responseDTO.extraDataDTO.numPeriodosLoaded - Constants.MAX_PERIODS_SHOWED;

        // Carga valores VALPRD_WK
        if (responseDTO.extraDataDTO.periodos != null && responseDTO.extraDataDTO.periodos.size() > 0) {
            log.info("INICIO - recoverBreakdown - Carga VALPRD_WK. [" + Calendar.getInstance().getTime() + "]");

            for (PeriodoDTO periodo : responseDTO.extraDataDTO.periodos) {
                periodo.valPeriodoDesgloseDTOList = this.valPeriodoDesgloseService
                    .findValPeriodoDesgloses(searchRequestDTO.codCliente, searchRequestDTO.tipoDesglose, searchRequestDTO.subTipoDesglose, searchRequestDTO.moneda,
                        periodo.getFecPeriodo(), searchRequestDTO.tipoPlantillaDesglose, esquema);
            }
        }
        log.info("FIN - recoverBreakdown - Carga VALPRD_WK. [" + Calendar.getInstance().getTime() + "]");

        // Carga CONSOL y PRCCNS (Empresas que consolidan)
        log.info("INICIO - recoverBreakdown - arga CONSOL y PRCCNS (Empresas que consolidan). [" + Calendar.getInstance().getTime() + "]");
        responseDTO.extraDataDTO.empConsolPeriodoDesgConsolList = this.empresaConsolidadaService
            .findEmpresaConsolidadas(searchRequestDTO.codCliente, searchRequestDTO.tipoDesglose, searchRequestDTO.subTipoDesglose, searchRequestDTO.moneda,
                searchRequestDTO.tipoPlantillaDesglose,esquema);
        log.info("FIN - recoverBreakdown - arga CONSOL y PRCCNS (Empresas que consolidan). [" + Calendar.getInstance().getTime() + "]");
        responseDTO.extraDataDTO.isAutoriced = true;

        responseDTO.codCliente = searchRequestDTO.codCliente;
        responseDTO.codPlantilla = String.valueOf(searchRequestDTO.tipoPlantillaDesglose);
        responseDTO.codTipoDes = String.valueOf(searchRequestDTO.tipoDesglose);
        responseDTO.codSubtipoDes = String.valueOf(searchRequestDTO.subTipoDesglose);
        responseDTO.codMoneda = searchRequestDTO.moneda;
        log.info("FIN - Request to recover Desglose desde el esquema");
        return responseDTO;
    }

    @SuppressWarnings("Duplicates")
    public BreakdownRecoverResponseDTO getBreakdownHistoricData(BreakdownSearchRequestDTO searchRequestDTO) {
        log.info("INICIO - Request to getBreakdownHistoricData Projections");
        final TurbineUserDTO user = getTurbineUserDTOAndSecurityCheck(searchRequestDTO.toString());

        BreakdownRecoverResponseDTO responseDTO = searchBreakdownRecoverResponseDTO(searchRequestDTO,null);

        // Carga PERIODOS
        log.info("INICIO - getBreakdownHistoricData - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");
        Sort orderBy = Sort.by(Sort.Direction.ASC, Constants.PROPERTY_NAME_FECH_PERIODO);
        responseDTO.extraDataDTO.periodos = this.periodoService
            .findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodTipoPlantilla(PeriodoRequestDTO.newInstance(searchRequestDTO), orderBy);
        if (responseDTO.extraDataDTO.periodos != null && responseDTO.extraDataDTO.periodos.size() > 0) {
            for (PeriodoDTO period : responseDTO.extraDataDTO.periodos) {
                responseDTO.extraDataDTO.monthsPeriod.add(period.getNumMeses());
                responseDTO.extraDataDTO.deflatorPeriod.add(period.getValDeflacPrd());
                responseDTO.extraDataDTO.periodRating.add(period.getCodEstadoAudi());
                responseDTO.extraDataDTO.fecPeriodo.add(period.getFecPeriodo());
            }
        }
        log.info("FIN - getBreakdownHistoricData - Carga PERIODOS. [" + Calendar.getInstance().getTime() + "]");

        responseDTO.extraDataDTO.numPeriodosLoaded = responseDTO.extraDataDTO.periodos != null ? responseDTO.extraDataDTO.periodos.size() : 0;
        responseDTO.extraDataDTO.numPeriodosConInfo =
            responseDTO.extraDataDTO.numPeriodosLoaded - Constants.MAX_PERIODS_SHOWED < 0 ? 0 : responseDTO.extraDataDTO.numPeriodosLoaded - Constants.MAX_PERIODS_SHOWED;

        // Carga valores VALPRD_WK
        log.info("INICIO - getBreakdownHistoricData - Carga VALPRD_WK. [" + Calendar.getInstance().getTime() + "]");
        for (PeriodoDTO periodo : responseDTO.extraDataDTO.periodos) {
            periodo.valPeriodoDesgloseDTOList = this.valPeriodoDesgloseService
                .findValPeriodoDesgloses(searchRequestDTO.codCliente, searchRequestDTO.tipoDesglose, searchRequestDTO.subTipoDesglose, searchRequestDTO.moneda,
                    periodo.getFecPeriodo(), searchRequestDTO.tipoPlantillaDesglose,null);

            periodo.valPeriodoProyeccionDesgloseDTOList = this.valPeriodoProyeccionDesgloseService.findValPeriodoProyeccion(searchRequestDTO.codCliente, searchRequestDTO.tipoDesglose,
                searchRequestDTO.subTipoDesglose, searchRequestDTO.moneda,
                periodo.getFecPeriodo(), searchRequestDTO.tipoPlantillaDesglose,50000);
        }
        log.info("FIN - getBreakdownHistoricData - Carga VALPRD_WK. [" + Calendar.getInstance().getTime() + "]");

        // Cargar los conceptos.
        /*Sort orderByConcepts = null;
        if (requestDTO.orderByFields != null && !requestDTO.orderByFields.isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC;
            if (requestDTO.orderByDirection != null && !requestDTO.orderByDirection.trim().isEmpty()) {
                direction = requestDTO.orderByDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
            }
            orderByConcepts = Sort.by(direction, requestDTO.orderByFields.toArray(new String[0]));
        }
        conceptoRepository.findProjectionConceptsByTemplateAndCodConceptsIn(conceptoRequestDTO);*/

        log.info("FIN - Request to getBreakdownHistoricData Projections");
        return responseDTO;
    }
    /**
     * Método que ejecuta validaciones a los conceptos que contengan fórmulas de validación. Compatible solo con la versión web del sistema de desglose.
     *
     * @param requestDTO
     *
     * @return
     */
    public List<BreakdownValidationErrorDTO> executeConceptValidations(@NotNull BreakdownConceptValidationRequestDTO requestDTO) {
        BigDecimal tolerance = BigDecimal.ZERO;
        try {
            tolerance = new BigDecimal(this.descripcionCodigoService.findQuadratureTolerance().getGlsGenericacorta().replaceAll(",", "."));
        } catch (Exception ex) {
            log.debug("OCURRIÓ UN ERROR CONVIRTIENDO LA TOLERANCIA -> " + ex.getMessage());
        }
        final BigDecimal finalTolerance = tolerance;
        final List<BreakdownValidationErrorDTO> errors = new ArrayList<>();

        IntStream.range(0, requestDTO.extraDataDTO.conceptos.size())
            .filter(i -> requestDTO.extraDataDTO.conceptos.get(i).getExpValidacionVal() != null && !requestDTO.extraDataDTO.conceptos.get(i).getExpValidacionVal().trim().isEmpty())
            .forEach(i1 -> {
                final ConceptoDTO conceptoDTO = requestDTO.extraDataDTO.conceptos.get(i1);
                if (requestDTO.activePeriodsIndexes != null && requestDTO.activePeriodsIndexes.length > 0) {
                    for (int k = 0; k < requestDTO.activePeriodsIndexes.length; k++) {
                        analyseExpressionValidationForPeriod(requestDTO.extraDataDTO, errors, i1, conceptoDTO, k, 1, requestDTO.activePeriodsIndexes[k], finalTolerance,
                            requestDTO.skipNotRequiredValidations);
                    }
                }
            });
        IntStream.range(0, requestDTO.extraDataDTO.conceptos.size())
            .filter(i -> requestDTO.extraDataDTO.conceptos.get(i).getExpValidacionPrc() != null && !requestDTO.extraDataDTO.conceptos.get(i).getExpValidacionPrc().trim().isEmpty())
            .forEach(i1 -> {
                final ConceptoDTO conceptoDTO = requestDTO.extraDataDTO.conceptos.get(i1);
                if (requestDTO.activePeriodsIndexes != null && requestDTO.activePeriodsIndexes.length > 0) {
                    for (int k = 0; k < requestDTO.activePeriodsIndexes.length; k++) {
                        analyseExpressionValidationForPeriod(requestDTO.extraDataDTO, errors, i1, conceptoDTO, k, 2, requestDTO.activePeriodsIndexes[k], finalTolerance,
                            requestDTO.skipNotRequiredValidations);
                    }
                }
            });
        IntStream.range(0, requestDTO.extraDataDTO.conceptos.size())
            .filter(i -> requestDTO.extraDataDTO.conceptos.get(i).getExpValidacionDef() != null && !requestDTO.extraDataDTO.conceptos.get(i).getExpValidacionDef().trim().isEmpty())
            .forEach(i1 -> {
                final ConceptoDTO conceptoDTO = requestDTO.extraDataDTO.conceptos.get(i1);
                if (requestDTO.activePeriodsIndexes != null && requestDTO.activePeriodsIndexes.length > 0) {
                    for (int k = 0; k < requestDTO.activePeriodsIndexes.length; k++) {
                        analyseExpressionValidationForPeriod(requestDTO.extraDataDTO, errors, i1, conceptoDTO, k, 3, requestDTO.activePeriodsIndexes[k], finalTolerance,
                            requestDTO.skipNotRequiredValidations);
                    }
                }
            });

        return errors;
    }

    public List<BreakdownValidationErrorDTO> executeConceptValidationsDesborde(@NotNull BreakdownConceptValidationRequestDTO requestDTO) {
        final List<BreakdownValidationErrorDTO> errors = new ArrayList<>();
        IntStream.range(0, requestDTO.extraDataDTO.conceptos.size()) .forEach(i1 -> {
                final ConceptoDTO conceptoDTO = requestDTO.extraDataDTO.conceptos.get(i1);
                if (requestDTO.activePeriodsIndexes != null && requestDTO.activePeriodsIndexes.length > 0) {
                    for (int k = 0; k < requestDTO.activePeriodsIndexes.length; k++) {
                        analyseExpressionValidationForPeriodDesborde(requestDTO.extraDataDTO, errors, i1, conceptoDTO, k, 1, requestDTO.activePeriodsIndexes[k]);
                        analyseExpressionValidationForPeriodDesborde(requestDTO.extraDataDTO, errors, i1, conceptoDTO, k, 2, requestDTO.activePeriodsIndexes[k]);
                    }
                }
            });
        return errors;
    }
    public List<BreakdownValidationErrorDTO> executeConceptValidationsConcepts(@NotNull BreakdownConceptValidationRequestDTO requestDTO) {
        final List<BreakdownValidationErrorDTO> errors = new ArrayList<>();
        IntStream.range(0, requestDTO.extraDataDTO.conceptos.size()) .forEach(i1 -> {
                final ConceptoDTO conceptoDTO = requestDTO.extraDataDTO.conceptos.get(i1);
                if (requestDTO.activePeriodsIndexes != null && requestDTO.activePeriodsIndexes.length > 0) {
                    for (int k = 0; k < 1; k++) {
                        analyseExpressionValidationForConceptos(requestDTO.extraDataDTO, errors, i1, conceptoDTO, k, 1, requestDTO.activePeriodsIndexes[k]);
                    }
                }
            });
        return errors;
    }
    /**
     * Traduce y evalúa la expresión de validación de un concepto para un período. Agrega a la lista un error de validación en caso de existir.
     *
     * @param requestDTO
     * @param errors
     * 		objeto lista con los errores encontrados.
     * @param i1
     * 		- Índice de la fila del concepto
     * @param conceptoDTO
     * @param periodIndex
     * 		- Índice del 0-3 de los períodos visibles.
     * @param globalPeriodIndex
     * 		- Índice real del período en la lista de períodos. índice del período a analisar.
     * @param expressionToEvaluateIndex
     * 		puede tomar valores 1 (getExpValidacionVal), 2(getExpValidacionPrc) u otro (getExpValidacionDef). Dependiendo de este valor se analizarán las expresiones
     * 		correspondientes.
     */
    private void analyseExpressionValidationForPeriod(ExtraDataDTO requestDTO, List<BreakdownValidationErrorDTO> errors, int i1, ConceptoDTO conceptoDTO, final int periodIndex,
        final int expressionToEvaluateIndex, final int globalPeriodIndex, final BigDecimal tolerance, final Boolean skipNotRequiredValidations) {

        final Integer columnNumber = expressionToEvaluateIndex == 1 ? Constants.COLUMN_NUMBER_GLS_FORMULA_VAL[periodIndex] :
            (expressionToEvaluateIndex == 2 ? Constants.COLUMN_NUMBER_GLS_FORMULA_PRC[periodIndex] : Constants.COLUMN_NUMBER_GLS_FORMULA_DEF[periodIndex]);

        if (periodIndex < requestDTO.fecPeriodo.size() && requestDTO.visualData[i1] != null
            && columnNumber <= requestDTO.visualData[i1].length) { // Si existe un valor para el período.

            final String conceptValue = requestDTO.visualData[i1][columnNumber];
            if (conceptValue != null && !conceptValue.trim().isEmpty()) {
                String expValidation = expressionToEvaluateIndex == 1 ? conceptoDTO.getExpValidacionVal() :
                    (expressionToEvaluateIndex == 2 ? conceptoDTO.getExpValidacionPrc() : conceptoDTO.getExpValidacionDef());
                final boolean isRequired = !expValidation.contains(QUADRATURE_VALIDATION_NOT_REQUIRED_EXPRESSION);
                if (!(!isRequired && skipNotRequiredValidations)) {
                    String expValidationValTranslated = expValidation.replace(Constants.QUADRATURE_VALIDATION_NOT_REQUIRED_EXPRESSION, "");
                    if (expValidationValTranslated.contains(Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT)) {
                        Integer[] indexesOfOpenSquareBrackets = Utils.indexesOf(expValidationValTranslated, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT);
                        Integer[] indexesOfCloseSquareBrackets =
                            indexesOfOpenSquareBrackets.length > 0 ? Utils.indexesOf(expValidationValTranslated, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT) :
                                new Integer[0];
                        for (int k = 0; k < indexesOfOpenSquareBrackets.length; k++) { // Existen referencias a conceptos en la expresión de validación.
                            String value = expValidationValTranslated.substring(indexesOfOpenSquareBrackets[k] + 1, indexesOfCloseSquareBrackets[k]);
                            Integer pos = ConceptoService.getValueMatrixPosition(requestDTO.conceptos, value, conceptoDTO);
                            if (pos >= 0) {
                                BigDecimal valor = new BigDecimal(requestDTO.visualData[pos][columnNumber]);
                                expValidationValTranslated = expValidationValTranslated
                                    .replace(expValidationValTranslated.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                        valor.setScale(2,RoundingMode.HALF_UP).toString());
                            } else {
                                log.debug("ERROR EXPRESIÓN VALIDACIÓN -> La expresión de validación " + expValidation + " no es correcta");
                            }
                        }
                    }
                    // Reemplazamos el valor del concepto real en la expresión
                    BigDecimal valor = new BigDecimal(conceptValue);
                    expValidationValTranslated = expValidationValTranslated.replaceAll(Constants.ACTUAL_CONCEPT_EXPRESSION, valor.setScale(2, RoundingMode.HALF_UP).toString());
                    log.debug("TRADUCIENDO EL VALOR DEL CONCEPTO " + expValidation + " -> " + expValidationValTranslated);
                    expValidationValTranslated = expValidationValTranslated.replaceAll(QUADRATURE_TOLERANCE_EXPRESSION, tolerance.toString());
                    log.debug("TRADUCIENDO LA TOLERANCIA " + expValidation + " -> " + expValidationValTranslated);
                    // Evaluamos la expresión.
                    final Boolean validation = ScriptEngineParser.evaluateConceptValidation(expValidationValTranslated);
                    // TODO: Definir estrategia con el cliente para el caso que no tenga valor el concepto. Por javascript la expresión debe comenzar con "VALOR &&"
                    if (validation == null || !validation) { // Ocurrió un error evaluando la expresión o no valida
                        BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                        errorDTO.setPeriod(requestDTO.fecPeriodo.get(globalPeriodIndex));
                        errorDTO.setTableColumn(columnNumber);
                        errorDTO.setTableRow(i1 + 1); // Se le incrementa 1 para que tenga el valor visual y no el índice del array de columnas.
                        errorDTO.setErrorMessage(conceptoDTO.getMsgErrValidacionVal());
                        errorDTO.setIsRequired(isRequired);
                        errors.add(errorDTO);
                    }
                }
            }
        }
    }
    /**
     *
     * @param requestDTO
     * @param errors
     * @param i1
     * @param conceptoDTO
     * @param periodIndex
     * @param expressionToEvaluateIndex
     * @param globalPeriodIndex
     */
    private void analyseExpressionValidationForPeriodDesborde(ExtraDataDTO requestDTO, List<BreakdownValidationErrorDTO> errors, int i1, ConceptoDTO conceptoDTO,
        final int periodIndex, final int expressionToEvaluateIndex, final int globalPeriodIndex) {

        final Integer columnNumber = expressionToEvaluateIndex == 1 ? Constants.COLUMN_NUMBER_GLS_FORMULA_VAL[periodIndex] :
            (expressionToEvaluateIndex == 2 ? Constants.COLUMN_NUMBER_GLS_FORMULA_PRC[periodIndex] : Constants.COLUMN_NUMBER_GLS_FORMULA_DEF[periodIndex]);

        if (periodIndex < requestDTO.fecPeriodo.size() && requestDTO.visualData[i1] != null
            && columnNumber <= requestDTO.visualData[i1].length) { // Si existe un valor para el período.

            final String conceptValue = requestDTO.visualData[i1][columnNumber];
            Boolean validation = true;
            Boolean isRequerid = false;
            String mensaje = "";
            if (conceptValue != null && !conceptValue.trim().isEmpty()) {
                // Reemplazamos el valor del concepto real en la expresión
                BigDecimal valor = null;
                try {
                    valor = new BigDecimal(conceptValue);
                } catch (Exception ex) {
                    valor = new BigDecimal(0);
                }
                if (expressionToEvaluateIndex == 1) {
                    Float valorQuiebrePos = Float.valueOf("100000000000000");
                    Float valorQuiebreNeg = Float.valueOf("-100000000000000");
                    if (valor.floatValue() >= valorQuiebrePos) {
                        validation = false;
                        mensaje = "valor >= 100000000000000 se truncara a 99999999999999 por ser el valor demasiado grande";
                    }
                    if (valor.floatValue() <= valorQuiebreNeg) {
                        validation = false;
                        mensaje = "valor <= -100000000000000 se truncara a -99999999999999 por ser el valor demasiado grande";
                    }
                } else if (expressionToEvaluateIndex == 2) {
                    Float valorQuiebrePos = Float.valueOf("10");
                    Float valorQuiebreNeg = Float.valueOf("-10");
                    valor = valor.divide(new BigDecimal(100));
                    if (valor.floatValue() >= valorQuiebrePos) {
                        validation = false;
                        mensaje = "porcentaje >= 10 se truncara a 9,999 por ser el valor demasiado grande";
                    }
                    if (valor.floatValue() <= valorQuiebreNeg) {
                        validation = false;
                        mensaje = "porcentaje <= -10 se truncara a -9,999 por ser el valor demasiado grande";
                    }
                }
            }
            // Evaluamos la expresión.
            if (validation == null || !validation) { // Ocurrió un error evaluando la expresión o no valida
                BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                errorDTO.setPeriod(requestDTO.fecPeriodo.get(globalPeriodIndex));
                errorDTO.setTableColumn(columnNumber);
                errorDTO.setTableRow(i1 + 1); // Se le incrementa 1 para que tenga el valor visual y no el índice del array de columnas.
                errorDTO.setErrorMessage(mensaje);
                errorDTO.setIsRequired(isRequerid);
                errors.add(errorDTO);
            }
        }
    }
    private void analyseExpressionValidationForConceptos(ExtraDataDTO requestDTO, List<BreakdownValidationErrorDTO> errors, int i1, ConceptoDTO conceptoDTO,
        final int periodIndex, final int expressionToEvaluateIndex, final int globalPeriodIndex) {

        final Integer columnNumber = expressionToEvaluateIndex == 1 ? Constants.COLUMN_NUMBER_GLS_FORMULA_VAL[periodIndex] :
            (expressionToEvaluateIndex == 2 ? Constants.COLUMN_NUMBER_GLS_FORMULA_PRC[periodIndex] : Constants.COLUMN_NUMBER_GLS_FORMULA_DEF[periodIndex]);
            final String nameConcep = requestDTO.visualData[i1][COLUMN_NUMBER_CCC];
            Boolean validation = true;
            Boolean isRequerid = false;
            String mensaje = "";
            if (requestDTO.visualData[i1][COLUMN_NUMBER_COD_TIPO_REGIS].equals(MULTI_REGISTRY)) {
                if (nameConcep == null || nameConcep.isEmpty() || nameConcep.equals("0")) {
                    isRequerid = true;
                    validation = false;
                    mensaje = "nombre de concepto de multiregistro no puede ir en blanco";
                }
            }
            // Evaluamos la expresión.
            if (validation == null || !validation) { // Ocurrió un error evaluando la expresión o no valida
                BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                errorDTO.setPeriod(requestDTO.fecPeriodo.get(globalPeriodIndex));
                errorDTO.setTableColumn(columnNumber);
                errorDTO.setTableRow(i1 + 1); // Se le incrementa 1 para que tenga el valor visual y no el índice del array de columnas.
                errorDTO.setErrorMessage(mensaje);
                errorDTO.setIsRequired(isRequerid);
                errors.add(errorDTO);
            }
        }
    /**
     * Método que hace verificaciones de si es posible o no editar. En caso que no sea posible lanza excepciones ecplicando por qué.
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param codPlantilla
     * @param codEstadoDeglo
     * @param currentUserOperatorCode
     * @param nomUserOpen
     * @param currentUserLogin
     * @param userAuthority
     */
    private void esPosibleEditar(@NotNull String codCliente, @NotNull Integer codTipoDes, @NotNull Integer codSubtipoDes, @NotNull String codMoneda, @NotNull Integer codPlantilla,
        @NotNull String codEstadoDeglo, @NotNull String currentUserOperatorCode, String nomUserOpen, @NotNull String currentUserLogin,
        @NotNull String userAuthority) { // línea 2102

        if(nomUserOpen!= null && (nomUserOpen.contains(PROCESS_USER_OPEN))) {
            nomUserOpen = nomUserOpen.replaceAll(PROCESS_USER_OPEN,"");
        }
        log.info("esPosibleEditar() - INICIO");

        Integer codRegStatus = 0;
        Integer cont = 0;
        Boolean existeInfo = false;
        String numRolRevisor = "";
        Boolean flgReemplazado;

        final DesgloseRevision desgloseRevision = this.desgloseRevisionService.busquedaRevision(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, codRegStatus);
        if (desgloseRevision != null) {
            cont = desgloseRevision.isFlgNotificado() ? 0 : 1; // "FLG_NOTIFICADO"
            numRolRevisor = desgloseRevision.getRolRevisor(); // "ROL_REVISOR"
            existeInfo = true;
        }

        if (numRolRevisor == null || numRolRevisor.isEmpty()) { // If codOperador <> numRolRevisor
            numRolRevisor = "0";
        }
        if (!numRolRevisor.equals(currentUserOperatorCode)) {
            cont = this.desgloseReemplazoRepository.countByNumRolEmpleadoAndDesNumRolEmpleado(numRolRevisor, currentUserOperatorCode);
        }
        flgReemplazado = cont > 0;

        if (cont == 1 && !numRolRevisor.equals(currentUserOperatorCode) && !flgReemplazado) {
            log.error("Está en proceso de revisión por un jefe de grupo.");
            throw new CantEditBreakdownException("Está en proceso de revisión por un jefe de grupo.");
        }

        if (Constants.ESTADO_EN_EDICION.equals(codEstadoDeglo)) {
            if (nomUserOpen != null && !nomUserOpen.trim().isEmpty()) {
                if (nomUserOpen.startsWith(Constants.USER_OPEN_T_MINUS_VALUE)) {
                    nomUserOpen = nomUserOpen.replace(Constants.USER_OPEN_T_MINUS_VALUE, "");
                }
                existeInfo = true;
            }
        }

        if (nomUserOpen != null && !nomUserOpen.isEmpty() && !currentUserLogin.equalsIgnoreCase(nomUserOpen.trim())) {
            log.error("Desglose para cliente se encuentra en edición por otro usuario. USUARIO -> " + nomUserOpen);
            throw new CantEditBreakdownException("Desglose para cliente se encuentra en edición por otro usuario.");
        }

        if (existeInfo) {
            saveDesgloseAudit(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, Constants.NUM_LINEA_AUDITORIA, Constants.EVENTO_AUDITORIA_DESHABILITA_NOTIF,
                Constants.ESTADO_BORRADOR_TERMINADO, codEstadoDeglo, userAuthority, currentUserOperatorCode, Constants.DESGLO_AUDIT_COMENTARIO_DESHABILITA_NOTIF);
        }
        log.info("esPosibleEditar() - FIN");
    }

    /**
     * Método para guardar una auditoría de desglose.
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param numLinea
     * @param eventoAuditoriaCambioEstado
     * @param codEstadoNvo
     * @param userAuthority
     * @param codEstadoOri
     * @param glsComentario
     */
    private void saveDesgloseAudit(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda, Integer codPlantilla, Integer numLinea,
        String eventoAuditoriaCambioEstado, String codEstadoNvo, String codEstadoOri, String userAuthority, String currentUserOperatorCode, String glsComentario) {
        Integer line = 0;
        List<DesgloseAuditoria> breakdownAuditoryList;
        breakdownAuditoryList = this.desgloseAuditoriaRepository.
            findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla);
        if (breakdownAuditoryList != null) {
            line = breakdownAuditoryList.size() + 1;
        }
        log.debug("INICIO - guardar auditoría del desglose");
        DesgloseAuditoria desgloseAuditoria = new DesgloseAuditoria();
        desgloseAuditoria.setCodCliente(codCliente);
        desgloseAuditoria.setCodTipoDes(codTipoDes);
        desgloseAuditoria.setCodSubtipoDes(codSubtipoDes);
        desgloseAuditoria.setCodMoneda(codMoneda);
        desgloseAuditoria.setCodPlantilla(codPlantilla);
        desgloseAuditoria.setFecEvento(LocalDateTime.now());
        desgloseAuditoria.setNumLinea(line);
        desgloseAuditoria.setCodEvento(eventoAuditoriaCambioEstado);
        desgloseAuditoria.setRolEmpleadoAccion(currentUserOperatorCode);
        desgloseAuditoria.setGlsRol(userAuthority);
        desgloseAuditoria.setCodEstadoOri(codEstadoOri);
        desgloseAuditoria.setCodEstadoNvo(codEstadoNvo);
        desgloseAuditoria.setRolEmpleadoOri(null);
        desgloseAuditoria.setRolEmpleadoNvo(null);
        desgloseAuditoria.setFlgResultValid(null);
        desgloseAuditoria.setGlsComentario(glsComentario);
        this.desgloseAuditoriaRepository.save(desgloseAuditoria);
        log.debug("FIN - guardar auditoría del desglose");
    }

    /**
     * Get operator code of the current user.
     *
     * @return the operator code of the current user
     */
    public String getCurrentUserOperatorCode(String userLoginName) {
        final SfStfOperatorDTO firstByLoginName = sfStfOperatorService.findFirstByLoginName(userLoginName);
        return firstByLoginName != null ? firstByLoginName.getOperatorCode().toString() : null;
    }

    @org.jetbrains.annotations.NotNull
    public DesgloseDTO updateBreakdownGlsStatus(DesgloseDTO dto) {
        if (dto.getCodEstado() != null) { // Si tiene un código de estado.
            final DescripcionCodigo status = descripcionCodigoRepository
                .findFirstByCodTablaAndCodGenericoStartingWithAndGlsGenericacortaAndCodRegStatus(Constants.CODTABLA_ESTADO_DESGLOSE, Constants.COD_GENERICO_DESGLOSE,
                    dto.getCodEstado(), Constants.COD_REG_STATUS_ZERO);
            dto.setGlsEstado(status.getGlsGenericalarga());
        }
        return dto;
    }

    @org.jetbrains.annotations.NotNull
    public TurbineUserDTO getTurbineUserDTOAndSecurityCheck(String debugLog) {
        if (debugLog != null && !debugLog.trim().isEmpty()) {
            log.debug(debugLog);
        }
        log.info("INICIO - Chequeo de seguridad que el usuario esté autenticado y que exista en la base de datos.");
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        final TurbineUserDTO user = this.turbineUserService.findByLoginName(userLogin);
        if (user == null || user.getUserId() == null) {
            throw new InternalServerErrorException("Current user login not found");
        }
        log.info("FIN - Chequeo de seguridad que el usuario esté autenticado y que exista en la base de datos.");
        return user;
    }

    /**
     * Hace una búsqueda de desgloses.
     *
     * @param searchRequestDTO
     *
     * @return
     */
    @org.jetbrains.annotations.NotNull
    private BreakdownRecoverResponseDTO searchBreakdownRecoverResponseDTO(BreakdownSearchRequestDTO searchRequestDTO,String esquema) {
        BreakdownRecoverResponseDTO responseDTO = new BreakdownRecoverResponseDTO();
        responseDTO.extraDataDTO = new ExtraDataDTO();
        // Consulta desglose del cliente ingresado
        final List<BreakdownSearchResponseDTO> searchResponseDTOS = this.searchBreakdown(searchRequestDTO,esquema);
        if (searchResponseDTOS == null || searchResponseDTOS.isEmpty()) {
            log.info("FIN - Request to recover Desglose");
            throw new RecoverDesgloseValidationException("El desglose señalado no existe.");
        }

        for (BreakdownSearchResponseDTO item : searchResponseDTOS) {
            responseDTO.extraDataDTO.nomUserOpen = item.nomUserOpen;
        }

        if (searchResponseDTOS.size() == 1) {
            responseDTO.codUnidMoneda = searchResponseDTOS.get(0).codUnidadMon;
            responseDTO.codEstado = searchResponseDTOS.get(0).codEstado;
            responseDTO.glsEstado = searchResponseDTOS.get(0).glsEstado;
            responseDTO.glsSubtipoDes = searchResponseDTOS.get(0).glsSubtipoDes;
            responseDTO.fecUserModif = searchResponseDTOS.get(0).fechaModif;
            responseDTO.userModif = searchResponseDTOS.get(0).userModif;
        }
        return responseDTO;
    }

    @org.jetbrains.annotations.NotNull
    private DesgloseDTO saveBreakdownMessage(Desglose desglose, DesgloseMensaje desgloseMensaje) {
        if (desgloseMensaje.getGlsComentarios() != null) {
            desgloseMensajeRepository.save(desgloseMensaje);
        }
        DesgloseDTO dto = this.desgloseMapper.toDto(desglose);
        return updateBreakdownGlsStatus(dto);
    }

    private Boolean canLoadSBIF(BreakdownSaveRequestDTO requestDTO){
        requestDTO.desgloseDTO.getCodCliente().toUpperCase(); // Filtro COD_CLIENTE
        String codTipoDes = requestDTO.desgloseDTO.getCodTipoDes(); // Filtro COD_TIPO_DES
        String codSubtipoDes = requestDTO.desgloseDTO.getCodSubtipoDes(); // Filtro COD_SUBTIPO_DES
        requestDTO.desgloseDTO.getCodMoneda(); // Filtro COD_MONEDA
        Boolean result = true;
        // TIPO = Consolidado
        // SUBTIPO = {OtraMoneda | Matriz | Combinado | Otros1 | Otros2}
        if (codTipoDes.equals("1") || codSubtipoDes.equals("2") || codSubtipoDes.equals("4") || codSubtipoDes.equals("5")
            || codSubtipoDes.equals("6") || codSubtipoDes.equals("7")) {
            result = false;
        }
        return result;
    }
}
