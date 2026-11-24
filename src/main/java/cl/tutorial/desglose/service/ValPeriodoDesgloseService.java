package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.PeriodoCif;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.SQLCIFRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.Periodo;
import cl.tutorial.desglose.domain.ValPeriodoDesglose;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesglosePK;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.repository.ValPeriodoDesgImprRepository;
import cl.tutorial.desglose.repository.ValPeriodoDesgloseRepository;
import cl.tutorial.desglose.service.dto.ConceptoDTO;
import cl.tutorial.desglose.service.dto.ValPeriodoDesgloseDTO;
import cl.tutorial.desglose.service.mapper.ValPeriodoDesgloseMapper;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureBusinessError;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureExecutionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing ValPeriodoDesglose.
 */
@Service
@Transactional
public class ValPeriodoDesgloseService {

    private final Logger log = LogManager.getLogger(ValPeriodoDesgloseService.class);

    private final ValPeriodoDesgloseRepository valPeriodoDesgloseRepository;
    private final ValPeriodoDesgImprRepository valPeriodoDesgImprRepository;

    private final SQLDGLRepository SQLDGLRepository;
    private final SQLCIFRepository sqlcifRepository;

    private final ValPeriodoDesgloseMapper valPeriodoDesgloseMapper;

    private static final String SP_CIF_SP_INS_VALPRD                      = "DGL_SP_INS_VALPRD_WK";
    private static final String SP_CIF_SP_INS_VALPRD_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_VALPRD_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_VALPRD_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_VALPRD_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_INS_VALPRD_05_P_FEC_PERIODO     = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_INS_VALPRD_06_P_COD_TIPO_CONCEP = "P_COD_TIPO_CONCEP";
    private static final String SP_CIF_SP_INS_VALPRD_07_P_COR_CONCEPTO    = "P_COR_CONCEPTO";
    private static final String SP_CIF_SP_INS_VALPRD_08_P_COR_VALOR       = "P_COR_VALOR";
    private static final String SP_CIF_SP_INS_VALPRD_09_P_VAL_CONCEPTO    = "P_VAL_CONCEPTO";
    private static final String SP_CIF_SP_INS_VALPRD_10_P_GLS_FRML_CONCEP = "P_GLS_FRML_CONCEP";
    private static final String SP_CIF_SP_INS_VALPRD_11_P_GLS_CONCEPTO    = "P_GLS_CONCEPTO";
    private static final String SP_CIF_SP_INS_VALPRD_12_P_VAL_PRC_CONCEP  = "P_VAL_PRC_CONCEP";
    private static final String SP_CIF_SP_INS_VALPRD_13_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_INS_VALPRD_14_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_INS_VALPRD_15_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    private static final String SP_CIF_SP_DEL_VALPRD                      = "DGL_SP_DEL_VALPRD_WK";
    private static final String SP_CIF_SP_DEL_VALPRD_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_DEL_VALPRD_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_DEL_VALPRD_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_DEL_VALPRD_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_DEL_VALPRD_05_P_FEC_PERIODO     = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_DEL_VALPRD_06_P_COD_TIPO_CONCEP = "P_COD_TIPO_CONCEP";
    private static final String SP_CIF_SP_DEL_VALPRD_07_P_COR_CONCEPTO    = "P_COR_CONCEPTO";
    private static final String SP_CIF_SP_DEL_VALPRD_08_P_COR_VALOR       = "P_COR_VALOR";
    private static final String SP_CIF_SP_DEL_VALPRD_09_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_DEL_VALPRD_10_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_DEL_VALPRD_11_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    private static final String SQL_GET_VALPRD_VALUES =
        "SELECT  V.COD_TIPO_CONCEP, V.COR_CONCEPTO, C.COD_CONCEPTO, V.COR_VALOR, \n" + "        V.VAL_CONCEPTO, V" + ".GLS_FRML_CONCEP, V.GLS_CONCEPTO, V.VAL_PRC_CONCEP\n"
            + " FROM VALPRD_WK V, CONCEP C\n" + " WHERE  V.COD_CLIENTE = ?1 AND V.COD_TIPO_DES =  ?2 AND V.COD_SUBTIPO_DES = ?3\n"
            + "        AND V.COD_MONEDA = ?4 AND V.FEC_PERIODO = TO_DATE(?5, 'DD-MM-YYYY') AND V.COD_PLANTILLA =?6 AND C.COD_PLANTILLA = V.COD_PLANTILLA \n"
            + "        AND C.COD_TIPO_CONCEP = V.COD_TIPO_CONCEP AND C.COR_CONCEPTO = V.COR_CONCEPTO\n" + "ORDER BY C.COD_TIPO_CONCEP, C.NUM_SECUENCIA, V.COR_VALOR ASC";

    private static final String SQL_GET_VALPRDCIF_VALUES =
        "SELECT  V.COD_TIPO_CONCEP, V.COR_CONCEPTO, C.COD_CONCEPTO, V.COR_VALOR, \n" + "        V.VAL_CONCEPTO, V" + ".GLS_FRML_CONCEP, V.GLS_CONCEPTO, V.VAL_PRC_CONCEP\n"
            + " FROM VALPRD V, CONCEP C\n" + " WHERE  V.COD_CLIENTE = ?1 AND V.COD_TIPO_DES =  ?2 AND V.COD_SUBTIPO_DES = ?3\n"
            + "        AND V.COD_MONEDA = ?4 AND V.FEC_PERIODO = TO_DATE(?5, 'DD-MM-YYYY') AND V.COD_PLANTILLA =?6 AND C.COD_PLANTILLA = V.COD_PLANTILLA \n"
            + "        AND C.COD_TIPO_CONCEP = V.COD_TIPO_CONCEP AND C.COR_CONCEPTO = V.COR_CONCEPTO\n" + "ORDER BY C.COD_TIPO_CONCEP, C.NUM_SECUENCIA, V.COR_VALOR ASC";
    public ValPeriodoDesgloseService(ValPeriodoDesgloseRepository valPeriodoDesgloseRepository,
        SQLDGLRepository SQLDGLRepository,
        ValPeriodoDesgloseMapper valPeriodoDesgloseMapper,
        ValPeriodoDesgImprRepository valPeriodoDesgImprRepository, SQLCIFRepository sqlcifRepository) {
        this.valPeriodoDesgloseRepository = valPeriodoDesgloseRepository;
        this.SQLDGLRepository = SQLDGLRepository;
        this.valPeriodoDesgloseMapper = valPeriodoDesgloseMapper;
        this.valPeriodoDesgImprRepository = valPeriodoDesgImprRepository;
        this.sqlcifRepository = sqlcifRepository;
    }

    /**
     * Save a valPeriodoDesglose.
     *
     * @param valPeriodoDesgloseDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public ValPeriodoDesgloseDTO save(ValPeriodoDesgloseDTO valPeriodoDesgloseDTO) {
        log.info("INICIO - Request to save ValPeriodoDesglose");
        log.debug(valPeriodoDesgloseDTO.toString());
        ValPeriodoDesglose valPeriodoDesglose = valPeriodoDesgloseMapper.toEntity(valPeriodoDesgloseDTO);
        valPeriodoDesglose = valPeriodoDesgloseRepository.save(valPeriodoDesglose);
        log.info("FIN - Request to save ValPeriodoDesglose");
        return valPeriodoDesgloseMapper.toDto(valPeriodoDesglose);
    }

    /**
     * Get all the valPeriodoDesgloses.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ValPeriodoDesgloseDTO> findAll(Pageable pageable) {
        log.info("Request to get all ValPeriodoDesgloses");
        return valPeriodoDesgloseRepository.findAll(pageable).map(valPeriodoDesgloseMapper::toDto);
    }

    /**
     * busqueda de valores de desglose
     *
     * @param codCliente
     * @param codTipoDesglose
     * @param codSubtipoDesglose
     * @param codMoneda
     *
     * @return
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    @Transactional(readOnly = true)
    public List<ValPeriodoDesgloseDTO> findAllBy(String codCliente, Integer codTipoDesglose, Integer codSubtipoDesglose, String codMoneda, Integer codPlantilla) {
        log.info("INICIO - BÚsqueda de desglose");
        List<ValPeriodoDesglose> lista = valPeriodoDesgloseRepository
            .findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(codCliente, codTipoDesglose, codSubtipoDesglose, codMoneda, codPlantilla);
        log.info("FIN - BÚsqueda de desglose");
        return this.valPeriodoDesgloseMapper.toDto(lista);
    }

    /**
     * Get one valPeriodoDesglose by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ValPeriodoDesgloseDTO> findOne(ValPeriodoDesglosePK id) {
        log.info("Request to get ValPeriodoDesglose : {}", id);
        return valPeriodoDesgloseRepository.findById(id).map(valPeriodoDesgloseMapper::toDto);
    }

    /**
     * Delete the valPeriodoDesglose by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(ValPeriodoDesglosePK id) {
        log.info("Request to delete ValPeriodoDesglose : {}", id);
        valPeriodoDesgloseRepository.deleteById(id);
    }

    public void deleteMultiregistro(ValPeriodoDesgloseDTO dto){
        log.info("Eliminando Multiregistro");
        valPeriodoDesgloseRepository.deleteByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaAndCorConceptoAndCorValor(dto.getCodCliente(),
            dto.getCodTipoDes(),dto.getCodSubtipoDes(),dto.getCodMoneda(),dto.getCodPlantilla(),dto.getCorConcepto(),dto.getCorValor());

        valPeriodoDesgImprRepository.deleteByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaAndCorConceptoAndCorValor(dto.getCodCliente(),
            dto.getCodTipoDes(),dto.getCodSubtipoDes(),dto.getCodMoneda(),dto.getCodPlantilla(),dto.getCorConcepto(),dto.getCorValor());


    }

    /**
     * Ejecuta el procedimiento almacenado DGL_SP_INS_VALPRD_WK
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param conceptoDTO
     * @param fecPeriodo
     */
    public Integer executeCifSpInsValPrd(final String codCliente, final Integer codPlantilla, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        final ConceptoDTO conceptoDTO, LocalDate fecPeriodo, String[] data, String[] visualData, Integer columnNumberCc,Integer columnNumberGlsFormulaVal,
        Integer columnNumberGlsFormulaPrc,
        Integer rowNumber) {

        this.log.info("INICIO - executeCifSpInsValPrd");

        // Llenar los parámetros
        Param<String> p1 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_03_P_COD_SUBTIPO_DES, Integer.class, codSubtipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<LocalDate> p5 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_05_P_FEC_PERIODO, LocalDate.class, fecPeriodo, ParameterMode.IN);
        Param<String> p6 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_06_P_COD_TIPO_CONCEP, String.class, conceptoDTO.getCodTipoConcep(), ParameterMode.IN);
        Param<Integer> p7 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_07_P_COR_CONCEPTO, Integer.class, conceptoDTO.getCorConcepto(), ParameterMode.IN);
        Param<Integer> p8 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_08_P_COR_VALOR, Integer.class, conceptoDTO.getCorValor(), ParameterMode.IN);
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
        String glsConcept = null;
        if(conceptoDTO.getCodTipoRegis().equals(MULTI_REGISTRY)) {
            glsConcept = data[columnNumberCc];
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
        Param<BigDecimal> p9 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_09_P_VAL_CONCEPTO, BigDecimal.class, valConcept, ParameterMode.IN);
        // TODO: ¿Este valor es el que corresponde? -> conceptoDTO.getNewFormula()
        Param<String> p10 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_10_P_GLS_FRML_CONCEP, String.class, glsFormulaConcept, ParameterMode.IN);
        // TODO: ¿Este valor es el que corresponde? -> conceptoDTO.getNewGlosa()            ---         glsFormulaConcept.replace("=", "")
        Param<String> p11 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_11_P_GLS_CONCEPTO, String.class, glsConcept, ParameterMode.IN);
        /*If arrPERIODOS(x).VALPRD(Y).newPorcent <> Empty Then
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_val_prc_concep", adSingle, adParamInput, 4, arrPERIODOS(x).VALPRD(Y).newPorcent)
        Else
            cmdTrx.Parameters.Append cmdTrx.CreateParameter("p_val_prc_concep", adSingle, adParamInput, 4, Null)
        End If*/
        // LocalDate fecInformSbif;
        // TODO: ¿Este valor es el que corresponde? -> conceptoDTO.getNewPorcent()
        BigDecimal valPrcConcep = null; // TODO: Corregir esto... enviar desde la UI o calcular el valor real de este elemento.
        try {
            valPrcConcep = NumberUtils.createBigDecimal(visualData[columnNumberGlsFormulaPrc]).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
            if(valPrcConcep.floatValue() >= 10) {
                valPrcConcep = new BigDecimal("9.999");
            }
            if(valPrcConcep.floatValue() <= -10) {
                valPrcConcep = new BigDecimal("-9.999");
            }
        } catch (Exception e) {
            // Esta excepción se ignora si vienen valores malos desde la UI, simplemente se guarda null en la base de datos.
            log.warn("OCURRIÓ UN ERROR CONVIRTIENDO EL VALOR DE visualData[columnNumberGlsFormulaPrc] = {} -> " + e.getMessage(), visualData[columnNumberGlsFormulaPrc]);
        }
        Param<BigDecimal> p12 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_12_P_VAL_PRC_CONCEP, BigDecimal.class, valPrcConcep, ParameterMode.IN);
        Param<Integer> p13 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_13_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p14 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_14_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p15 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_15_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15);

        // Interpretar los resultados
        if(valConcept.compareTo(BigDecimal.ZERO)!=0) {
            log.debug("QUERY : " + ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD);
            log.debug("PARAMETROS : " + parameters.toString());
            Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD, parameters);
            log.debug(String.valueOf(objects));
            Integer returnParam = (Integer) objects.get(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_13_P_RETORNO);
            if (returnParam == null) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD);
                log.info("parameters -> {}", parameters.toString());
                throw new StorageProcedureExecutionException();
            }
            if (!returnParam.equals(0)) { // El SP devolvió un error.
                String returnMessage = (String) objects.get(ValPeriodoDesgloseService.SP_CIF_SP_INS_VALPRD_14_P_GLOSA);
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
        log.info("FIN - executeCifSpInsValPrd");
        return -1;
    }

    /**
     * Ejecuta el procedimiento almacenado DGL_SP_DEL_VALPRD_WK
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param conceptoDTO
     * @param fecPeriodo
     */
    public void executeCifSpDelValPrd(final String codCliente, final Integer codPlantilla, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        final ConceptoDTO conceptoDTO, LocalDate fecPeriodo) {
        this.log.info("INICIO - executeCifSpInsValPrd");

        // Llenar los parámetros
        Param<String> p1 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_03_P_COD_SUBTIPO_DES, Integer.class, codSubtipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<LocalDate> p5 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_05_P_FEC_PERIODO, LocalDate.class, fecPeriodo, ParameterMode.IN);
        Param<String> p6 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_06_P_COD_TIPO_CONCEP, String.class, conceptoDTO.getCodTipoConcep(), ParameterMode.IN);
        Param<Integer> p7 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_07_P_COR_CONCEPTO, Integer.class, conceptoDTO.getCorConcepto(), ParameterMode.IN);
        Param<Integer> p8 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_08_P_COR_VALOR, Integer.class, conceptoDTO.getCorValor(), ParameterMode.IN);
        Param<Integer> p9 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_09_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p10 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_10_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p11 = new Param<>(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_11_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
        log.debug("QUERY: " + ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD);
        log.debug("PARAMETROS: " + parameters);
        // Interpretar los resultados
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD, parameters);
        log.debug(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_09_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(ValPeriodoDesgloseService.SP_CIF_SP_DEL_VALPRD_10_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpInsValPrd");
    }

    @Transactional(readOnly = true)
    public List<ValPeriodoDesgloseDTO> findValPeriodoDesgloses(final String codCliente, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        LocalDate fecPeriodo, Integer codPlantilla,String esquema) {

        log.info("INICIO - Request to findValPeriodoDesgloses codCliente: {}, codCliente: {}, codTipoDes: {}, codSubtipoDes: {}, codMoneda: {}, fecPeriodo: {}, codPlantilla: {}",
            codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);

        List<ValPeriodoDesgloseDTO> list = new ArrayList<>();
        // COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, FEC_PERIODO, COD_PLANTILLA
        List<Object> params = Arrays.asList(codCliente.toUpperCase(), codTipoDes, codSubtipoDes, codMoneda, fecPeriodo.format(Constants.FORMATTER_DATE_SHORT), codPlantilla);
        List<Object[]> result = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            log.debug("findValPeriodoDesgloses: " +this.SQL_GET_VALPRD_VALUES);
            log.debug("findValPeriodoDesgloses: " + params);
            result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_GET_VALPRD_VALUES, params);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            log.debug("findValPeriodoDesgloses: " +this.SQL_GET_VALPRDCIF_VALUES);
            log.debug("findValPeriodoDesgloses: " + params);
             result = this.sqlcifRepository.executeNativeSelectQuery(this.SQL_GET_VALPRDCIF_VALUES, params);
        }

        if (result != null && result.size() > 0) {
            // Agrego el encabezado de grupo sin valores porque no se va a utilizar...
            String lastGroup = "";
            ValPeriodoDesgloseDTO groupEmptyHeader = new ValPeriodoDesgloseDTO();
            for (Object[] elem : result) {
                ValPeriodoDesgloseDTO dto = new ValPeriodoDesgloseDTO();
                // Valores fijos.
                dto.setCodCliente(codCliente);
                dto.setCodTipoDes(codTipoDes);
                dto.setCodSubtipoDes(codSubtipoDes);
                dto.setCodMoneda(codMoneda);
                dto.setFecPeriodo(fecPeriodo);
                dto.setCodPlantilla(codPlantilla);

                dto.setCodTipoConcep(elem[0] != null ? elem[0].toString() : null);
                if (lastGroup.isEmpty() || !lastGroup.equals(dto.getCodTipoConcep())) { // Agrego el encabezado de grupo sin valores porque no se va a utilizar...
                    lastGroup = dto.getCodTipoConcep();
                    list.add(groupEmptyHeader);
                }
                dto.setCorConcepto(elem[1] != null ? ((BigDecimal) elem[1]).intValue() : null);
                dto.codConcepto = elem[2] != null ? elem[2].toString() : null;
                dto.setCorValor(elem[3] != null ? ((BigDecimal) elem[3]).intValue() : null);
                final BigDecimal value = elem[4] != null ? (BigDecimal) elem[4] : null;
                dto.setValConcepto(value);
                dto.setGlsFrmlConcep(elem[5] != null ? elem[5].toString() : null);
                dto.setGlsConcepto(elem[6] != null ? elem[6].toString() : null);
                dto.setValPrcConcep(elem[7] != null ? ((BigDecimal) elem[7]).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_VALUE_100) : null);

                // Valores trabajados en el sistema antiguo
                dto.hasChange = false;
                dto.isModif = false;
                dto.oldValue = dto.getValConcepto();
                dto.newValue = dto.getValConcepto();
                dto.oldFormula = dto.getGlsFrmlConcep();
                dto.newFormula = dto.getGlsFrmlConcep();
                dto.newPorcent = dto.getValPrcConcep() != null ? dto.getValPrcConcep().intValue() : null;

                if (dto.getCorValor() > 0) {
                    dto.numItem = getNumItem(dto.getCodTipoConcep(), dto.codConcepto, dto.getGlsConcepto());
                    // arrPERIODOS(x).VALPRD(Y).numItem = getNumItem(arrPERIODOS(x).VALPRD(Y).tipoConcepto, arrPERIODOS(x).VALPRD(Y).codConcepto, rsTrx("GLS_CONCEPTO"))
                } else {
                    dto.numItem = dto.getCorValor();
                }
                list.add(dto);
            }
        }

        log.info("FIN - Request to findValPeriodoDesgloses");
        return list;
    }

    private Integer getNumItem(String codTipoConcep, String codConcepto, String glsConcepto) {
        return null;
    }

    public List<ValPeriodoDesglose> deleteAllBy(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codTipoPlantilla) {
        log.info(
            "INICIO - Request to deleteBy ValPeriodoDesglose with codCliente: {}, codTipoDes: {}, codSubtipoDes: {}, codMoneda: {} and fecPeriodo: {} and codTipoPlantilla: {} - ["
                + Calendar.getInstance().getTime() + "]", codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codTipoPlantilla);
        List<ValPeriodoDesglose> deletedItems = this.valPeriodoDesgloseRepository
            .removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo,
                codTipoPlantilla);
        log.info("FIN - Request to deleteBy ValPeriodoDesglose - [" + Calendar.getInstance().getTime() + "]");
        return deletedItems;
    }
}
