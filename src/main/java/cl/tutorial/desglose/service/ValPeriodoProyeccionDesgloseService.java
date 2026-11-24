package cl.tutorial.desglose.service;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.ValPeriodoProyeccionDesglose;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.repository.ValPeriodoDesgImprRepository;
import cl.tutorial.desglose.repository.ValPeriodoProyeccionDesgloseRepository;
import cl.tutorial.desglose.service.dto.ConceptoDTO;
import cl.tutorial.desglose.service.dto.DesgloseDTO;
import cl.tutorial.desglose.service.dto.ValPeriodoProyeccionDesgloseDTO;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureBusinessError;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureExecutionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing
 */
@Service
@Transactional
public class ValPeriodoProyeccionDesgloseService {

    private final Logger log = LogManager.getLogger(ValPeriodoProyeccionDesgloseService.class);

    private final ValPeriodoProyeccionDesgloseRepository valPeriodoProyeccionDesgloseRepository;

    private final SQLDGLRepository SQLDGLRepository;

    private static final String SP_CIF_SP_INS_VALPRD                            = "DGL_SP_INS_VALPRDPRY_WK";
    private static final String SP_CIF_SP_INS_VALPRD_01_P_COD_CLIENTE           = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_VALPRD_02_P_COD_TIPO_DES          = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_VALPRD_03_P_COD_SUBTIPO_DES       = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_VALPRD_04_P_COD_MONEDA            = "P_COD_MONEDA";
    private static final String SP_CIF_SP_INS_VALPRD_05_P_COD_PLANTILLA         = "P_COD_PLANTILLA";
    private static final String SP_CIF_SP_INS_VALPRD_06_P_FEC_PERIODO           = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_INS_VALPRD_07_P_COD_TIPO_CONCEP_PRY   = "P_COD_TIPO_CONCEP_PRY";
    private static final String SP_CIF_SP_INS_VALPRD_08_P_COR_CONCEPTO_PRY      = "P_COR_CONCEPTO_PRY";
    private static final String SP_CIF_SP_INS_VALPRD_09_P_VAL_CONCEPTO_PRY      = "P_VAL_CONCEPTO_PRY";
    private static final String SP_CIF_SP_INS_VALPRD_10_P_FRML_CONCEP_PRY       = "P_GLS_FRML_CONCEP_PRY";
    private static final String SP_CIF_SP_INS_VALPRD_11_P_FEC_PERIODO_PRY       = "P_FEC_PERIODO_PRY";
    private static final String SP_CIF_SP_INS_VALPRD_12_P_COD_PLANTILLA_PRY     = "P_COD_PLANTILLA_PRY";
    private static final String SP_CIF_SP_INS_VALPRD_13_P_RETORNO               = "P_RETORNO";
    private static final String SP_CIF_SP_INS_VALPRD_14_P_GLOSA                 = "P_GLOSA";

    private static final String SQL_GET_VALPRD_VALUES =
        "SELECT\n" + "    V.COD_TIPO_CONCEP_PRY,\n" + "    V.COR_CONCEPTO_PRY,\n" + "    C.COD_CONCEPTO,\n" + "    V.VAL_CONCEPTO_PRY, \n" + "    V.GLS_FRML_CONCEP_PRY, V.FEC_PERIODO_PRY\n"
            + "    FROM VALPRD_PRY_WK V, CONCEP C WHERE  V.COD_CLIENTE = ?1 AND V.COD_TIPO_DES =  ?2 AND V.COD_SUBTIPO_DES = ?3\n"
            + "    AND V.COD_MONEDA = ?4 AND V.FEC_PERIODO = TO_DATE(?5, 'DD-MM-YYYY') AND V.COD_PLANTILLA =?6  and V.COD_PLANTILLA_PRY = ?7\n"
            + "    AND C.COD_PLANTILLA = V.COD_PLANTILLA_PRY AND C.COD_TIPO_CONCEP = V.COD_TIPO_CONCEP_PRY AND C.COR_CONCEPTO = V.COR_CONCEPTO_PRY\n" + "ORDER BY\n"
            + "    V.FEC_PERIODO_PRY ASC";



    public ValPeriodoProyeccionDesgloseService(ValPeriodoProyeccionDesgloseRepository valPeriodoProyeccionDesgloseRepository,
        SQLDGLRepository SQLDGLRepository,
        ValPeriodoDesgImprRepository valPeriodoDesgImprRepository) {
        this.valPeriodoProyeccionDesgloseRepository = valPeriodoProyeccionDesgloseRepository;
        this.SQLDGLRepository = SQLDGLRepository;
    }
    public void deleteValPrdPry(DesgloseDTO desgloseDTO) {
        log.info("INICIO - Request to delete ValPrdPry");
        final String codCliente = desgloseDTO.getCodCliente();
        final Integer codTipoDes = Integer.valueOf(desgloseDTO.getCodTipoDes());
        final Integer codSubtipoDes = Integer.valueOf(desgloseDTO.getCodSubtipoDes());
        final String codMoneda = desgloseDTO.getCodMoneda();
        final Integer codPlantilla = Integer.valueOf(desgloseDTO.getCodPlantilla());
        final Integer codPlantillaPry = Integer.valueOf(desgloseDTO.getCodPlantillaProyeccion());
        this.valPeriodoProyeccionDesgloseRepository.deleteByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaAndCodPlantillaPry(codCliente, codTipoDes,
            codSubtipoDes, codMoneda,codPlantilla,codPlantillaPry);
        log.info("FIN - Request to delete ValPrdPry");
    }
    public Integer executeCifSpInsValPrdPry(final String codCliente, final Integer codPlantilla, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        final ConceptoDTO conceptoDTO, LocalDate fecPeriodo, String[] data, String[] visualData,Integer columnNumberGlsFormulaVal, Integer rowNumber, LocalDate fecPeriodoPry) {

        this.log.info("INICIO - executeCifSpInsValPrdPry");

        // Llenar los parámetros
        Param<String> p1 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_03_P_COD_SUBTIPO_DES, Integer.class, codSubtipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<Integer> p5 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_05_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        Param<LocalDate> p6 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_06_P_FEC_PERIODO, LocalDate.class, fecPeriodo, ParameterMode.IN);
        Param<String> p7 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_07_P_COD_TIPO_CONCEP_PRY, String.class, conceptoDTO.getCodTipoConcep(),
            ParameterMode.IN);
        Param<Integer> p8 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_08_P_COR_CONCEPTO_PRY, Integer.class, conceptoDTO.getCorConcepto(), ParameterMode.IN);
        // TODO: ¿Este valor es el que corresponde? -> conceptoDTO.getNewValue()
        BigDecimal valConcept = null;
        try {
            valConcept = NumberUtils.createBigDecimal(visualData[columnNumberGlsFormulaVal]).setScale(3,BigDecimal.ROUND_HALF_DOWN);
        } catch (Exception e) {
            // Esta excepción se ignora si vienen valores malos desde la UI, simplemente se guarda null en la base de datos.
            log.warn("OCURRIÓ UN ERROR CONVIRTIENDO EL VALOR DE visualData[columnNumberGlsFormulaVal] = {} -> " + e.getMessage(), visualData[columnNumberGlsFormulaVal]);
        }
        String glsFormulaConcept = data[columnNumberGlsFormulaVal];
        if(glsFormulaConcept!=null  && glsFormulaConcept.indexOf("=")>-1){
            if(data[COLUMN_NUMBER_COD_TIPO_CONCEP].equals(COD_TIPO_CONCEP_BALANCE) || data[COLUMN_NUMBER_COD_TIPO_CONCEP].equals(COD_TIPO_CONCEP_EST_RESULT)){
                if(data[COLUMN_NUMBER_COD_CONCEPT].contains(".")) {
                    try {
                        glsFormulaConcept = NumberUtils.createBigDecimal(data[columnNumberGlsFormulaVal]).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP).toString();
                        glsFormulaConcept = null;
                    } catch (Exception e) {
                        glsFormulaConcept = data[columnNumberGlsFormulaVal].replace("=", "");
                    }
                }
                else{
                    glsFormulaConcept = null;
                }
            }
            else {
                try {
                    glsFormulaConcept = NumberUtils.createBigDecimal(visualData[columnNumberGlsFormulaVal]).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP).toString();
                    glsFormulaConcept = null;
                } catch (Exception e) {
                    glsFormulaConcept = null;
                }
            }
        } else {
            glsFormulaConcept = null;
        }
        if (valConcept == null && (glsFormulaConcept == null || glsFormulaConcept.trim().isEmpty())) {
            valConcept = BigDecimal.ZERO; // Se inicializa en cero si no tiene valor en la tabla ni fórmula.
        }
        Float valorQuiebrePos = Float.valueOf("100000000000000");
        Float valorQuiebreNeg = Float.valueOf("-100000000000000");
        if(valConcept.floatValue()>=valorQuiebrePos) {
            valConcept = new BigDecimal("99999999999999.000");
        }
        if(valConcept.floatValue()<=valorQuiebreNeg) {
            valConcept = new BigDecimal("-99999999999999.000");
        }
        Param<BigDecimal> p9 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_09_P_VAL_CONCEPTO_PRY, BigDecimal.class, valConcept, ParameterMode.IN);
        // TODO: ¿Este valor es el que corresponde? -> conceptoDTO.getNewFormula()
        Param<String> p10 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_10_P_FRML_CONCEP_PRY, String.class, glsFormulaConcept, ParameterMode.IN);
        // TODO: ¿Este valor es el que corresponde? -> conceptoDTO.getNewGlosa()            ---         glsFormulaConcept.replace("=", "")
        Param<LocalDate> p11 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_11_P_FEC_PERIODO_PRY, LocalDate.class, fecPeriodoPry, ParameterMode.IN);
        Param<Integer> p12 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_12_P_COD_PLANTILLA_PRY, Integer.class, conceptoDTO.getCodTipoPlantilla(),
            ParameterMode.IN);
        Param<Integer> p13 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_13_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p14 = new Param<>(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_14_P_GLOSA, String.class, null, ParameterMode.OUT);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14);

        // Interpretar los resultados
        if(valConcept.compareTo(BigDecimal.ZERO)!=0) {
            log.debug("QUERY: " + ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD);
            log.debug("PARAMETROS: " + parameters);
            Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD, parameters);
            log.debug(String.valueOf(objects));
            Integer returnParam = (Integer) objects.get(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_13_P_RETORNO);
            if (returnParam == null) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD);
                log.info("parameters -> {}", parameters.toString());
                throw new StorageProcedureExecutionException();
            }
            if (!returnParam.equals(0)) { // El SP devolvió un error.
                String returnMessage = (String) objects.get(ValPeriodoProyeccionDesgloseService.SP_CIF_SP_INS_VALPRD_14_P_GLOSA);
                log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
                log.info("fila {} --- parameters -> {}", (rowNumber + 1), parameters.toString());
                if (returnMessage != null && !returnMessage.isEmpty() && StringUtils.containsIgnoreCase(returnMessage, Constants.ORACLE_KNOWN_ERROR_MESSAGE_1)) {
                    String formattedErrorMessage = String
                        .format(Constants.ORACLE_KNOWN_ERROR_MESSAGE_1_TRANSLATION, (rowNumber + 1), conceptoDTO.getCodTipoConcep(), conceptoDTO.getCodConcepto(), fecPeriodo.format(Constants.FORMATTER_DATE_SHORT));
                    throw new StorageProcedureBusinessError(formattedErrorMessage);
                }
                throw new StorageProcedureBusinessError(returnMessage);
            }
            return 0;
        }
        log.info("FIN - executeCifSpInsValPrdPry");
        return -1;
    }
    @Transactional(readOnly = true)
    public List<ValPeriodoProyeccionDesgloseDTO> findValPeriodoProyeccion(final String codCliente, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        LocalDate fecPeriodo, Integer codPlantilla, Integer codPlantillaProyeccion) {
        log.info("INICIO - Request to findValPeriodoDesglosesProyeccion codCliente: {}, codCliente: {}, codTipoDes: {}, codSubtipoDes: {}, codMoneda: {}, fecPeriodo: {}, "
                + "codPlantilla: {}",
            codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);

        List<ValPeriodoProyeccionDesgloseDTO> list = new ArrayList<>();
        // COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, COD_PLANTILLA
        List<Object> params = Arrays.asList(codCliente.toUpperCase(), codTipoDes, codSubtipoDes, codMoneda, fecPeriodo.format(Constants.FORMATTER_DATE_SHORT),
            codPlantilla,codPlantillaProyeccion);
        log.debug("QUERY: " + this.SQL_GET_VALPRD_VALUES);
        log.debug("PARAMETROS: " + params.toString());
        List<Object[]> result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_GET_VALPRD_VALUES, params);
        if (result != null && result.size() > 0) {
            // Agrego el encabezado de grupo sin valores porque no se va a utilizar...
            String lastGroup = "";
            ValPeriodoProyeccionDesgloseDTO groupEmptyHeader = new ValPeriodoProyeccionDesgloseDTO();
            for (Object[] elem : result) {
                ValPeriodoProyeccionDesgloseDTO dto = new ValPeriodoProyeccionDesgloseDTO();
                // Valores fijos.
                dto.setCodCliente(codCliente);
                dto.setCodTipoDes(codTipoDes);
                dto.setCodSubtipoDes(codSubtipoDes);
                dto.setCodMoneda(codMoneda);
                dto.setFecPeriodo(fecPeriodo);
                dto.setCodPlantilla(codPlantilla);
                dto.setCodPlantillaPry(codPlantillaProyeccion);
                dto.setCodTipoConcepPry(elem[0] != null ? elem[0].toString() : null);
                dto.setCorConceptoPry(elem[1] != null ? ((BigDecimal) elem[1]).intValue() : null);
                dto.codConcepto = elem[2] != null ? elem[2].toString() : null;
                final BigDecimal value = elem[3] != null ? (BigDecimal) elem[3] : null;
                dto.setValConceptoPry(value);
                dto.setGlsFrmlConcepPry(elem[3] != null ? elem[3].toString() : null);
                Timestamp dateValue = elem[5] != null ? (Timestamp) elem[5] : null;
                if(dateValue!= null) {
                    dto.setFecPeriodoPry(dateValue.toLocalDateTime().toLocalDate());
                }
                list.add(dto);
            }
        }
        log.info("FIN - Request to findValPeriodoDesgloses");
        return list;
    }

}
