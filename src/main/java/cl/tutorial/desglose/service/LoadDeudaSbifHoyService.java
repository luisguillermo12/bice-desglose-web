package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.sqldesglose.domain.ConsultaRut;
import cl.tutorial.desglose.conexiones.sqldesglose.domain.ObtieneDatosDeudaSbif;
import cl.tutorial.desglose.conexiones.sqldesglose.repository.SQLRepository;
import javax.persistence.ParameterMode;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.service.dto.*;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.InvalidRutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cl.tutorial.desglose.config.Constants.FORMATTER_DATE_SHORT;
import static cl.tutorial.desglose.config.Constants.FORMATTER_DATE_YYYYmm_SHORT;

/**
 * Service Implementation for managing LoadDeudaSbifHoy.
 */
@Service
@Transactional
public class LoadDeudaSbifHoyService {

    private final Logger log = LogManager.getLogger(LoadDeudaSbifHoyService.class);

    private static final String SCHEMA = "INFOANYWHERE.";
    private static final String BD = "iawdsf.";
    private static final String DBO = "dbo.";
    private static final String TABLA = "DSF_";
    private static final String SQL_GET_ALL_DEUDASBIFHOY =
        " SELECT rut" +
            ",deuda_direct_vig" +
            ",deuda_morosa_30_90" +
            ",deuda_direct_venc" +
            ",deuda_inv_financieras" +
            ",deuda_ope_pacto" +
            ",deuda_indirec_vig" +
            ",deuda_indirec_vencida" +
            ",deuda_comercial" +
            ",deuda_credito_consumo" +
            ",nro_inst_cred_consumo" +
            ",deuda_cred_hipotecario" +
            ",saldo_deud_cast_direct" +
            ",saldo_deud_cast_indirect" +
            ",mto_linea_disponible" +
            ",deuda_com_vig_mex" +
            ",deuda_com_venc_mex" +
            ",nro_inst_cred_com" +
            ",deuda_leasing" +
            ",deuda_morosa_leasing" +
            ",deuda_dir_venc_180d_3anos"+
            " FROM " + BD + DBO + TABLA + "%s D (NOLOCK)" +
            " WHERE rut = '%s'";
    private static final String SPE_SRV_CONSULTA_RUT  = SCHEMA + DBO + "spe_srv_consulta_rut";

    private static final String PIN_001_AV_CUENTA  = "av_cuenta";
    private static final String PIN_002_AV_CLAVE   = "av_clave";
    private static final String PIN_003_AC_RUT     = "ac_rut";
    private static final String PIN_004_AC_MODREQ  = "ac_modreq";
    private static final String PIN_005_AI_P01     = "ai_p01";

    private static final String SPE_SRV_OBTIENE_DATOS = SCHEMA + DBO + "spe_srv_obtiene_datos";
    private static final String PIN_001_AN_CODPRO  = "an_codPro";
    private static final String PIN_002_AN_IDCNS   = "an_idcns";
    private static final String PIN_003_AC_TIPOREG = "ac_tiporeg";

    private static  final  String SQL_CONSULTA_MAX_PER = "SELECT max(periodo) per ,count(*) number  FROM iawdsf.dbo.publicacion D (NOLOCK)";

    private final SQLRepository sqlRepository;

    public LoadDeudaSbifHoyService(SQLRepository sqlRepository) {
        this.sqlRepository = sqlRepository;
    }

    public List<DatosSbifDTO> getLoadDeudaSbifHoy_By_Rut_FecConsulta(String rut, LocalDate fecConsulta, List<LocalDate> arrPERIODOS, List<Integer> activePeriodsIndexes) {
        log.info("INICIO - getLoadDeudaSbifHoy_By_Rut_FecConsulta(): Inicio Consulta Deuda SBIF. Rut:" + rut);
        if (rut == null || rut.trim().isEmpty() || Constants.ZERO_AS_STRING.equals(rut)) {
            throw new InvalidRutException();
        }
        String rutQuery = rut;
        List<DatosSbifDTO> list = new ArrayList<>();

        if ( (!rut.trim().isEmpty()) && fecConsulta != null) {
            rutQuery = rut.trim().substring(0, rut.trim().length() - 1).toUpperCase();
            String strYYYYMM = FORMATTER_DATE_YYYYmm_SHORT.format(fecConsulta);
            /*if (isLastPeriod(fecConsulta, arrPERIODOS, activePeriodsIndexes)) {
                final List<DatosSbifDTO> listUltimaDeuda = this.getConsultarUltimaDeuda(rut);
                // No hay registros
                if (listUltimaDeuda != null && listUltimaDeuda.size() == 0) {
                    log.info("getLoadDeudaSbifHoy_By_Rut_FecConsulta(). No se encontró Deuda SBIF para el cliente " + rutQuery + ", periodo " + strYYYYMM);
                } else {
                    log.info("getLoadDeudaSbifHoy_By_Rut_FecConsulta(). Deuda Sbif Consultada, para el cliente " + rutQuery + ", periodo " + strYYYYMM);
                }
                return listUltimaDeuda;
            }*/
            DatosSbifDTO datosSbif = new DatosSbifDTO();
            try {
                String strYYYYMMMax = null;
                String strYYYYMMVal = strYYYYMM;
                log.debug("getLoadDeudaSbifHoy_By_Rut_FecConsulta max periodo: " + SQL_CONSULTA_MAX_PER);
                final List<Object[]> MaxPer = this.sqlRepository.executeNativeSelectQuery2(SQL_CONSULTA_MAX_PER);
                for (Object[] elem : MaxPer) {
                    if (elem[0] != null) {
                        strYYYYMMMax = String.valueOf((Object) elem[0].toString());
                        strYYYYMMVal = strYYYYMMMax;
                        log.debug("Fecha Maxima Publicacion: " + strYYYYMMMax );
                    }
                }
                if(strYYYYMMMax!= null) {
                    if (Integer.valueOf(strYYYYMM) > Integer.valueOf(strYYYYMMMax)) {
                        strYYYYMMVal = strYYYYMMMax;
                        log.debug("fecha de consulta: " + strYYYYMMVal );
                    }
                }
                log.debug("getLoadDeudaSbifHoy_By_Rut_FecConsulta : " + SQL_GET_ALL_DEUDASBIFHOY);
                log.debug("Parametros de consulta fecha : " +strYYYYMMVal + " rut " + rutQuery );
                final List<Object[]> listObjects = this.sqlRepository.executeNativeSelectQuery2(String.format(SQL_GET_ALL_DEUDASBIFHOY, strYYYYMMVal, rutQuery));

                for (Object[] elem : listObjects) {

                    DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("01-MM-yyyy");
                    String fechaInforme = "01" + "-" + strYYYYMMVal.substring(4) + "-" + strYYYYMMVal.substring(0,4);
                    log.debug("FECHA DE INFORME SBIF : " + fechaInforme);
                    LocalDate fechaInformeDate = LocalDate.parse(fechaInforme, FORMATTER_DATE_SHORT);
                    datosSbif.setFecInforme(fechaInformeDate);
                    BigDecimal valor;
                    valor = convertirBigDecimal((Object) elem[1]); // Campo 2 del Select deuda_direct_vig
                    if (valor != null) {
                            BigDecimal deuda_direct_vig = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_DVig(deuda_direct_vig);
                    }
                    valor = convertirBigDecimal((Object) elem[2]); // Campo 3 del Select deuda_morosa_30_90
                    if (valor != null) {
                        BigDecimal deuda_morosa_30_90 = valor.divide(new BigDecimal(1000));
                        datosSbif.setMtoSbif_DMor(deuda_morosa_30_90);
                    }
                    valor = convertirBigDecimal((Object) elem[20]); // Campo 20
                    if (valor != null) {
                        BigDecimal deuda_direct_venc = valor.divide(new BigDecimal(1000));
                        datosSbif.setMtoSbif_DVen(deuda_direct_venc);
                    }
                    valor = convertirBigDecimal((Object) elem[12]); // Campo 13 del Select saldo_deud_cast_direct
                    if (valor != null) {
                        BigDecimal saldo_deud_cast_direct = valor.divide(new BigDecimal(1000));
                        datosSbif.setMtoSbif_DCas(saldo_deud_cast_direct);
                    }
                    valor = convertirBigDecimal((Object) elem[6]); // Campo 7 del Select saldo_deud_cast_direct
                    if (valor != null) {
                        BigDecimal deuda_indirec_vig = valor.divide(new BigDecimal(1000));
                        datosSbif.setMtoSbif_IVig(deuda_indirec_vig);
                    }
                    valor = convertirBigDecimal((Object) elem[7]); // Campo 8 del Select deuda_indirec_vencida
                    if (valor != null) {
                        BigDecimal deuda_indirec_vencida = valor.divide(new BigDecimal(1000));
                        datosSbif.setMtoSbif_IVen(deuda_indirec_vencida);
                    }
                    valor = convertirBigDecimal((Object) elem[13]); // Campo 14 del Select saldo_deud_cast_indirect
                    if (valor != null) {
                        BigDecimal saldo_deud_cast_indirect = valor.divide(new BigDecimal(1000));
                        datosSbif.setMtoSbif_ICas(saldo_deud_cast_indirect);
                    }
                    valor = convertirBigDecimal((Object) elem[3]); // Campo 3
                    if (valor != null) {
                        BigDecimal deuda_com_venc_mex = valor.divide(new BigDecimal(1000));
                        datosSbif.setMtoSbif_Imp90_179(deuda_com_venc_mex);
                    }
                    datosSbif.setHayDeudaSbif(true);
                    list.add(datosSbif);
                }
            } catch (Exception e1) {
                log.error(String.format("getLoadDeudaSbifHoy_By_Rut_FecConsulta(). No se encontró deuda Sbif para rut: %s y fecConsulta: %s", rut, FORMATTER_DATE_SHORT.format(fecConsulta)));
            }
            // No hay registros
           /* if (list != null && list.size() == 0) {
                log.info("getLoadDeudaSbifHoy_By_Rut_FecConsulta(). No se encontró Deuda SBIF para el cliente " + rutQuery + ", periodo " + strYYYYMM);
                if (fecConsulta.isAfter(LocalDate.now().minusMonths(3))) {
                    final List<DatosSbifDTO> listUltimaDeuda = this.getConsultarUltimaDeuda(rut);
                    // No hay registros
                    if (listUltimaDeuda != null && listUltimaDeuda.size() == 0) {
                        log.info("getLoadDeudaSbifHoy_By_Rut_FecConsulta(). No se encontró Deuda SBIF para el cliente " + rutQuery + ", periodo " + strYYYYMM);
                    } else {
                        log.info("getLoadDeudaSbifHoy_By_Rut_FecConsulta(). Deuda Sbif Consultada, para el cliente " + rutQuery + ", periodo " + strYYYYMM);
                    }
                    return listUltimaDeuda;
                }
            }*/
        }
        log.info("FIN - getLoadDeudaSbifHoy_By_Rut_FecConsulta()");
        return list;
    }

    @Transactional(readOnly = true)
    public List<ConsultaRut> getConsultaRut(String rut) throws Exception {
        log.info("INICIO - getConsultaRut()");
        log.debug(String.format("Entrada: rut: %s", (rut!=null?rut:"null")));
        String rutQuery = rut.trim().substring(0,rut.trim().length()-1).toUpperCase();
        Float rutF = Float.valueOf(rutQuery);
        String dv2 = rut.trim().substring(rut.trim().length()-1).toUpperCase();
        String rut0 = String.format("0000000000%s%s", rutQuery.trim(), dv2).trim();
        String rut0Fills = rut0.substring(rut0.length()-10, rut0.length());

        String  av_cuenta = "mdr_exp";
        String  av_clave  = "mdr_exp";
        String  ac_rut    = rut0Fills;
        String  ac_modreq = "V";
        String  ai_modreq = "16";

        Param<String> p1 = new Param<>(PIN_001_AV_CUENTA, String.class, av_cuenta, ParameterMode.IN);
        Param<String> p2 = new Param<>(PIN_002_AV_CLAVE, String.class, av_clave, ParameterMode.IN);
        Param<String> p3 = new Param<>(PIN_003_AC_RUT, String.class, ac_rut, ParameterMode.IN);
        Param<String> p4 = new Param<>(PIN_004_AC_MODREQ, String.class, ac_modreq, ParameterMode.IN);
        Param<String> p5 = new Param<>(PIN_005_AI_P01, String.class, ai_modreq, ParameterMode.IN);

        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5);
        log.debug(String.format("parameters IN:\n p1: %s,\n p2: %s,\n p3: %s,\n p4: %s \ny p5: %s", p1.toString(), p2.toString(), p3.toString(), p4.toString(), p5.toString()));
        log.debug("QUERY: " +SPE_SRV_CONSULTA_RUT );
        log.debug("PARAMETROS:  " + parameters);
        log.info("FIN - getConsultaRut()");
        return (List<ConsultaRut>) sqlRepository.executeNamedQuerySP(SPE_SRV_CONSULTA_RUT, parameters, ConsultaRut.class);

    }

    @Transactional(readOnly = true)
    public List<ObtieneDatosDeudaSbif> getObtieneDatos(BigDecimal codPro, BigDecimal idCns) throws Exception {
        log.info("INICIO - getObtieneDatos()");
        log.debug(String.format("Entrada: codPro: %s, idCns: %s", (codPro!=null?codPro.toString():"null"), (idCns!=null?idCns.toString():"null")));
        Param<BigDecimal> p1 = new Param<>(PIN_001_AN_CODPRO, BigDecimal.class, codPro, ParameterMode.IN);
        Param<BigDecimal> p2 = new Param<>(PIN_002_AN_IDCNS, BigDecimal.class, idCns, ParameterMode.IN);
        Param<String> p3 = new Param<>(PIN_003_AC_TIPOREG, String.class, "D01", ParameterMode.IN);

        List<Param<?>> parameters = Arrays.asList(p1, p2, p3);
        log.debug("QUERY: " + SPE_SRV_OBTIENE_DATOS);
        log.debug("PARAMETROS: " + parameters);
        log.info("FIN - getObtieneDatos()");

        return (List<ObtieneDatosDeudaSbif>) sqlRepository.executeNamedQuerySP(SPE_SRV_OBTIENE_DATOS, parameters, ObtieneDatosDeudaSbif.class);
    }

    private boolean isLastPeriod(LocalDate fecConsulta, List<LocalDate> arrPERIODOS, List<Integer> activePeriodsIndexes) {
        log.info("INICIO - isLastPeriod(LocalDate fecConsulta, List<LocalDate> arrPERIODOS)");
        log.debug(String.format("fecConsulta: %s\n", FORMATTER_DATE_SHORT.format(fecConsulta)));
        Boolean isLast = true;
        if (arrPERIODOS != null) {
            for (int i = 0; i < arrPERIODOS.size(); i++) {
                try {
                    if (arrPERIODOS.get(i) != null && (i != activePeriodsIndexes.get(0)
                        && i != activePeriodsIndexes.get(1)
                        && i != activePeriodsIndexes.get(2)
                        && i != activePeriodsIndexes.get(3)) ) {
                        String fecha = FORMATTER_DATE_SHORT.format(arrPERIODOS.get(i));
                        log.debug(String.format("arrPERIODOS[%s]: %s\n", Integer.toString(i), fecha));
                        if (arrPERIODOS.get(i).isAfter(fecConsulta) || arrPERIODOS.get(i).isEqual(fecConsulta)) {
                            isLast = false;
                            log.debug(String.format("isLast: false, ultima fecha: %s\n", FORMATTER_DATE_SHORT.format(arrPERIODOS.get(i))));
                            break;
                        }
                    }
                } catch (DateTimeParseException e) {
                    log.error(String.format("Error al convertir fecha de arrPERIODOS[%s]\n",Integer.toString(i)));
                }
            }
            log.debug(String.format("fecConsulta: %s\n", FORMATTER_DATE_SHORT.format(fecConsulta)));
            for (int i = 0; i < activePeriodsIndexes.size(); i++) {
                try {
                    if (arrPERIODOS.size()-1 >= activePeriodsIndexes.get(i)) {
                        if (arrPERIODOS.get(activePeriodsIndexes.get(i)) != null) {
                            String fecha = FORMATTER_DATE_SHORT.format(arrPERIODOS.get(activePeriodsIndexes.get(i)));
                            log.debug(String.format("fecha vizualizada en pantalla de arrPERIODOS[%s]: %s\n", Integer.toString(activePeriodsIndexes.get(i)), fecha));
                            if (isLast && arrPERIODOS.get(activePeriodsIndexes.get(i)) != null && (arrPERIODOS.get(activePeriodsIndexes.get(i)).isAfter(fecConsulta))) {
                                isLast = false;
                                log.debug(String.format("isLast: false, ultima fecha: %s\n", FORMATTER_DATE_SHORT.format(arrPERIODOS.get(i))));
                                break;
                            }
                        }
                    }
                } catch (DateTimeParseException e) {
                    log.error(String.format("Error al convertir fecha vizualizada en pantalla de arrPERIODOS[%s]\n",Integer.toString(i)));
                }

           }
        }
        log.debug(String.format("FIN - isLastPeriod. isLast: %s", isLast.toString()));
        return isLast;
    }

    List<DatosSbifDTO>  getConsultarUltimaDeuda(String rut) {
        log.info("INICIO - getConsultarUltimaDeuda()");
        log.debug(String.format("Entrada: rut: %s", rut));
        List<DatosSbifDTO> list = new ArrayList<>();
        try {
            final List<ConsultaRut> resultList = getConsultaRut(rut);
            BigDecimal valor = null;
            if (resultList != null && resultList.size() > 0) {
                BigDecimal codPro = null;
                BigDecimal idCns = null;
                for (ConsultaRut elem : resultList) {
                    valor = convertirBigDecimal((Object) elem.getCodpro()); // Campo 1 del Select codpro
                    if (valor != null) {
                        codPro = valor;
                    }
                    valor = convertirBigDecimal((Object) elem.getIdcns()); // Campo 2 del Select idcns
                    if (valor != null) {
                        idCns = valor;
                    }
                }
                log.debug(String.format("getConsultaRut(): Salida - resultList: codPro: %s, idCns: %s", (codPro != null ? codPro.toString().trim() : "null"), (idCns != null ? idCns.toString().trim() : "null")));

                final List<ObtieneDatosDeudaSbif> resultList2 = getObtieneDatos(codPro, idCns);
                if (resultList2 != null && resultList2.size() > 0) {
                    for (ObtieneDatosDeudaSbif elem : resultList2) {
                        DatosSbifDTO datosSbif = new DatosSbifDTO();
                        valor = convertirBigDecimal((Object) elem.getPERIODO01_1()); // Campo 22 del Select PERIODO01_1
                        if (valor != null) {
                            LocalDate fechaInformeDate = LocalDate.parse("01-"+valor.toString().substring(4,6)+ "-"+ valor.toString().substring(0,4),FORMATTER_DATE_SHORT);
                            datosSbif.setFecInforme(fechaInformeDate);
                        }
                        valor = convertirBigDecimal((Object) elem.getDEUDA_DIREC_VIG02_1()); // Campo 26 del Select DEUDA_DIREC_VIG02_1
                        if (valor != null) {
                            BigDecimal deuda_direct_vig = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_DVig(deuda_direct_vig);
                        }
                        valor = convertirBigDecimal((Object) elem.getDEUDA_MOROSA12_1()); // Campo 66 del Select DEUDA_MOROSA12_1
                        if (valor != null) {
                            BigDecimal deuda_morosa_30_90 = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_DMor(deuda_morosa_30_90);
                        }
                        valor = convertirBigDecimal((Object) elem.getDEUDA_VENCI_DIREC04_1()); // Campo 34 del Select DEUDA_VENCI_DIREC04_1
                        if (valor != null) {
                            BigDecimal deuda_direct_venc = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_DVen(deuda_direct_venc);
                        }
                        valor = convertirBigDecimal((Object) elem.getDEUDA_CAST_DIRECTA13_1()); // Campo 70 del Select DEUDA_CAST_DIRECTA13_1
                        if (valor != null) {
                            BigDecimal saldo_deud_cast_direct = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_DCas(saldo_deud_cast_direct);
                        }
                        valor = convertirBigDecimal((Object) elem.getDEUDA_INDIREC_VIG06_1()); // Campo 42 del Select DEUDA_INDIREC_VIG06_1
                        if (valor != null) {
                            BigDecimal deuda_indirec_vig = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_IVig(deuda_indirec_vig);
                        }
                        valor = convertirBigDecimal((Object) elem.getDEUDA_INDIREC_VENCI07_1()); // Campo 46 del Select DEUDA_INDIREC_VENCI07_1
                        if (valor != null) {
                            BigDecimal deuda_indirec_vencida = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_IVen(deuda_indirec_vencida);
                        }
                        valor = convertirBigDecimal((Object) elem.getDEUDA_CAST_INDIRECTA14_1()); // Campo 74 del Select DEUDA_CAST_INDIRECTA14_1
                        if (valor != null) {
                            BigDecimal saldo_deud_cast_indirect = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_ICas(saldo_deud_cast_indirect);
                        }
                        valor = convertirBigDecimal((Object) elem.getMTO_DEUDA_COM_VENCI_MEXT17_1()); // Campo 86 del Select MTO_DEUDA_COM_VENCI_MEXT17_1
                        if (valor != null) {
                            BigDecimal deuda_com_venc_mex = valor.divide(new BigDecimal(1000));
                            datosSbif.setMtoSbif_Imp90_179(deuda_com_venc_mex);
                        }
                        datosSbif.setHayDeudaSbif(true);
                        list.add(datosSbif);
                    }
                    for (DatosSbifDTO item : list) {
                        log.debug(String.format("getObtieneDatos() - Salida - list: item.toString(): %s\n", item.toString()));
                    }
                }
            }
        } catch(Exception e){
            log.error(String.format("getConsultarUltimaDeuda(). No se encontró deuda Sbif para rut: %s", rut));
        }
        log.info("FIN - getConsultarUltimaDeuda()");
        return list;
    }

    private BigDecimal convertirBigDecimal(Object value) {
        try {
            log.info("INICIO - convertirBigDecimal()");
            log.debug(String.format("Entrada: value:"+ value.toString()));
            BigDecimal ret = null;
            if (value != null) {
                ret = new BigDecimal(value.toString().trim());
            }
            log.debug(String.format("Salida: ret: "+ ret));
            log.info("FIN - convertirBigDecimal()");
            return ret;
        } catch (Exception ex) {
            log.error("Error - convertirBigDecimal()" + ex.getMessage());
            return null;
        }
    }

}

