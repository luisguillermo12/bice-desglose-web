package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.bctdesglose.repository.SQLBCTRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.conexiones.bctdesglose.domain.Concepto;
import cl.tutorial.desglose.conexiones.bctdesglose.domain.pk.ConceptoPK;
import cl.tutorial.desglose.conexiones.bctdesglose.repository.ConceptoRepository;
import cl.tutorial.desglose.service.dto.BreakdownFilterConceptDTO;
import cl.tutorial.desglose.service.dto.BreakdownValidationErrorDTO;
import cl.tutorial.desglose.service.dto.ConceptoDTO;
import cl.tutorial.desglose.service.dto.ConceptoRequestDTO;
import cl.tutorial.desglose.service.mapper.ConceptoMapper;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.service.util.Utils;
import cl.tutorial.desglose.web.rest.errors.*;
import org.jetbrains.annotations.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing Concepto.
 */
@Service
@Transactional
public class ConceptoService {

    private final Logger log = LogManager.getLogger(ConceptoService.class);

    private final ConceptoRepository conceptoRepository;

    private final ConceptoMapper conceptoMapper;

    private final SQLBCTRepository sqlbctRepository;

    private  static final String BCT_SP_RESECUENCIAR = "BCT_SP_RESECUENCIAR";

    private static final String BCT_SP_RESECUENCIAR_PI_COD_PLANTILLA = "PI_COD_PLANTILLA";

    private static final String BCT_SP_RESECUENCIAR_PI_RETORNO = "PI_RETORNO";

    private static final String BCT_SP_RESECUENCIAR_PI_GLOSA = "PI_GLOSA";

    private final String SQL_GET_MAX_COR_CONCEPTO = "select nvl(max(cor_concepto),0)" + " from concep where cod_tipo_concep =:codTipoConcepto";

    public ConceptoService(ConceptoRepository conceptoRepository, ConceptoMapper conceptoMapper, SQLBCTRepository sqlbctRepository) {
        this.conceptoRepository = conceptoRepository;
        this.conceptoMapper = conceptoMapper;
        this.sqlbctRepository = sqlbctRepository;
    }

    /**
     * Save a concepto.
     *
     * @param conceptoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public void save(ConceptoDTO conceptoDTO, boolean update) {
        log.info("INICIO to save Concepto : {}", conceptoDTO);
        if (conceptoDTO.getCodConcepto().contains(".")) {
            String fatherCodConcept = conceptoDTO.getCodConcepto().split("\\.")[0];
            List<Concepto> listOptional;
            if (conceptoDTO.getFlgConActivo() == null || conceptoDTO.getFlgConActivo()== 0) {
                listOptional =
                    conceptoRepository.findAllByCodTipoPlantillaAndCodTipoConcepAndCodConcepto(conceptoDTO.getCodTipoPlantilla(), conceptoDTO.getCodTipoConcep(), fatherCodConcept);
            } else {
                listOptional = conceptoRepository
                    .findAllByCodTipoPlantillaAndCodTipoConcepAndCodConceptoAndFlgConActivo(conceptoDTO.getCodTipoPlantilla(), conceptoDTO.getCodTipoConcep(), fatherCodConcept,
                        conceptoDTO.getFlgConActivo());
            }
            if (listOptional.size() == 0) {
                throw new ConceptNotExistException();
            }
        }
        if(!update) {
            List<Concepto> conceptoList = conceptoRepository.findAllByCodTipoPlantillaAndCodTipoConcepAndCodConcepto(conceptoDTO.getCodTipoPlantilla(), conceptoDTO.getCodTipoConcep(), conceptoDTO.getCodConcepto());
            if (conceptoList.size() > 0) {
                throw new ConceptExistException();
            }
        }
        Concepto concepto = conceptoMapper.toEntity(conceptoDTO);
        conceptoRepository.save(concepto);
        log.info("FIN to save Concepto");
    }

    public  List<BreakdownValidationErrorDTO> validateConcepto(ConceptoDTO conceptoDTO) {
        log.info("INICIO to validate Concepto : {}", conceptoDTO);
        ConceptoRequestDTO conceptoRequestDTO = new ConceptoRequestDTO();
        conceptoRequestDTO.templateCode = conceptoDTO.getCodTipoPlantilla();
        List<String> conceptosTypesCodes = new LinkedList<>();
        conceptosTypesCodes.add(COD_TIPO_CONCEP_BALANCE);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_GEN_FONDOS);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_IND_FINAN);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA);
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
        List<ConceptoDTO> dtoList = this.findByTemplateAndCodConceptsIn(conceptoRequestDTO,null).conceptoDTOList;
        List<BreakdownValidationErrorDTO> validationErrorDTOS =  new LinkedList<>();
        if (dtoList != null && !dtoList.isEmpty()) {
            updateFormulaValues(dtoList);
            for (int i = 0; i < dtoList.size(); i++) {
                ConceptoDTO rowData = dtoList.get(i);
                if (conceptoDTO.getCodTipoConcep().equals(rowData.getCodTipoConcep()) && conceptoDTO.getCorConcepto()== rowData.getCorConcepto()) {
                    rowData = conceptoDTO;
                    // GLS_FORMULA_VAL
                    String glsFormulaVal = "";
                    String glsFormulaValOriginal = "";
                    if (rowData.getGlsFormulaValWeb() != null && !rowData.getGlsFormulaValWeb().trim().isEmpty()) {
                        glsFormulaVal = rowData.getGlsFormulaValWeb();
                        glsFormulaValOriginal = rowData.getGlsFormulaValWeb();
                        rowData.setGlsFormulaVal(glsFormulaVal);
                    } else {
                        glsFormulaVal = rowData.getGlsFormulaVal();
                        glsFormulaValOriginal = rowData.getGlsFormulaVal();
                    }
                    try {
                        if (glsFormulaVal != null && !glsFormulaVal.trim().isEmpty() &&
                            (rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_BALANCE) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_EST_RESULT) ||
                                rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_EST_RESULT))) {
                            boolean indexOfEquals = glsFormulaVal.contains(Constants.EQUALS_SIGN);
                            if (indexOfEquals) {
                                Integer[] indexesOfOpenSquareBrackets = Utils.indexesOf(glsFormulaVal, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT);
                                Integer[] indexesOfCloseSquareBrackets =
                                    indexesOfOpenSquareBrackets.length > 0 ? Utils.indexesOf(glsFormulaVal, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT) : new Integer[0];
                                for (int k = 0; k < indexesOfOpenSquareBrackets.length; k++) { // La fórmula no está traducida.
                                    String value = glsFormulaVal.substring(indexesOfOpenSquareBrackets[k] + 1, indexesOfCloseSquareBrackets[k]);
                                    Integer pos = ConceptoService.getValueMatrixPosition(dtoList, value, rowData);
                                    if (pos >= 0) {

                                    } else if (Constants.FORMULA_NUM_MESES_STRING_INDEX.equals(pos)) {

                                    } else if (Constants.FORMULA_IVA_STRING_INDEX.equals(pos)) {

                                    } else if (FORMULA_DEF_BASE_INDEX.equals(pos)) {

                                    } else if (FORMULA_DEF_PERI_INDEX.equals(pos)) {

                                    } else {
                                        if (!glsFormulaVal.contains(CANT_MESES_PER1) && !glsFormulaVal.contains(DEF_PER1)) {
                                            BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                                            errorDTO.setPeriod(null);
                                            errorDTO.setTableColumn(null);
                                            errorDTO.setTableRow(null);
                                            errorDTO.setErrorMessage("Error en Traduccion de Formula de valor de concepto :" + rowData.getCodTipoConcep() + "  " + rowData.getCodConcepto() + " --> la Referencia no valida es :" + value);
                                            errorDTO.setIsRequired(true);
                                            validationErrorDTOS.add(errorDTO);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        if (ex.getMessage() != null) {
                            log.debug(ex.getMessage());
                        }
                    }
                    // GLS_FORMULA_PRC
                    String glsFormulaPrc = "";
                    String glsFormulaPrcOriginal = "";
                    if (rowData.getGlsFormulaPrcWeb() != null && !rowData.getGlsFormulaPrcWeb().trim().isEmpty()) {
                        glsFormulaPrc = rowData.getGlsFormulaPrcWeb();
                        glsFormulaPrcOriginal = rowData.getGlsFormulaPrcWeb();
                        rowData.setGlsFormulaPrc(glsFormulaPrc);
                    } else {
                        glsFormulaPrc = rowData.getGlsFormulaPrc();
                        glsFormulaPrcOriginal = rowData.getGlsFormulaPrc();
                    }
                    try {
                        if (glsFormulaVal != null && !glsFormulaVal.trim().isEmpty() &&
                            (rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_BALANCE) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_EST_RESULT) ||
                                rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_EST_RESULT) ||
                                rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM))) {
                            boolean indexOfEquals = glsFormulaPrc.contains("=");
                            if (indexOfEquals) {
                                Integer[] indexesOfOpenSquareBrackets = Utils.indexesOf(glsFormulaPrc, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT);
                                Integer[] indexesOfCloseSquareBrackets =
                                    indexesOfOpenSquareBrackets.length > 0 ? Utils.indexesOf(glsFormulaPrc, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT) : new Integer[0];
                                for (int k = 0; k < indexesOfOpenSquareBrackets.length; k++) { // La fórmula no está traducida.
                                    String value = glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k] + 1, indexesOfCloseSquareBrackets[k]);
                                    Integer periodo_anterior = 0;
                                    Integer periodo_siguiente = 0;
                                    if (value.contains(FORMULA_P_ANT)) {
                                        if (!value.contains(FORMULA_P_ANT_NUM_MESES_STRING)) {
                                            value = value.replaceAll(FORMULA_P_ANT, "");
                                            periodo_anterior = 1;
                                        } else {
                                            rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                                .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                    (FORMULA_P_ANT_NUM_MESES_STRING)));
                                            rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal()
                                                .replace(glsFormulaPrcOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                    (FORMULA_P_ANT_NUM_MESES_STRING)));
                                        }
                                    }
                                    if (value.contains(FORMULA_P_SIG)) {
                                        value = value.replaceAll(FORMULA_P_SIG, "");
                                        periodo_siguiente = 1;
                                    }
                                    Integer pos = ConceptoService.getValueMatrixPosition(dtoList, value, rowData);
                                    // Utils.convertToNumberingScheme(1)
                                    if (pos >= 0) {
                                        if (periodo_anterior == 1) {

                                        }
                                        if (periodo_siguiente == 1) {

                                        } else {

                                        }
                                    } else if (pos == -1 && value.equals(FORMULA_CNPT)) {

                                    } else if (Constants.FORMULA_NUM_MESES_STRING_INDEX.equals(pos)) {

                                    } else if (Constants.FORMULA_IVA_STRING_INDEX.equals(pos)) {

                                    } else if (FORMULA_DEF_BASE_INDEX.equals(pos)) {

                                    } else if (FORMULA_DEF_PERI_INDEX.equals(pos)) {

                                    } else {
                                        if (!glsFormulaPrc.contains(CANT_MESES_PER1) && !glsFormulaPrc.contains(DEF_PER1)) {
                                            BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                                            errorDTO.setPeriod(null);
                                            errorDTO.setTableColumn(null);
                                            errorDTO.setTableRow(null);
                                            errorDTO.setErrorMessage("Error en Traduccion de Formula de porcentaje de concepto :" + rowData.getCodTipoConcep() + "  " + rowData.getCodConcepto() + " --> la Referencia no valida es :" + value);
                                            errorDTO.setIsRequired(true);
                                            validationErrorDTOS.add(errorDTO);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        if (ex.getMessage() != null) {
                            log.debug(ex.getMessage());
                        }
                    }
                    // GLS_FORMULA_DEF
                    String glsFormulaDef = "";
                    String glsFormulaDefOriginal = "";
                    if (rowData.getGlsFormulaDefWeb() != null && !rowData.getGlsFormulaDefWeb().trim().isEmpty()) {
                        glsFormulaDef = rowData.getGlsFormulaDefWeb();
                        glsFormulaDefOriginal = rowData.getGlsFormulaDefWeb();
                        rowData.setGlsFormulaDef(glsFormulaDef);
                    } else {
                        glsFormulaDef = rowData.getGlsFormulaDef();
                        glsFormulaDefOriginal = rowData.getGlsFormulaDef();
                    }
                    try {
                        if (glsFormulaDef != null && !glsFormulaDef.trim().isEmpty()) {
                            boolean indexOfEquals = glsFormulaDef.contains("=");
                            if (indexOfEquals) {
                                Integer[] indexesOfOpenSquareBrackets = Utils.indexesOf(glsFormulaDef, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT);
                                Integer[] indexesOfCloseSquareBrackets =
                                    indexesOfOpenSquareBrackets.length > 0 ? Utils.indexesOf(glsFormulaDef, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT) : new Integer[0];
                                for (int k = 0; k < indexesOfOpenSquareBrackets.length; k++) { // La fórmula no está traducida.
                                    String value = glsFormulaDef.substring(indexesOfOpenSquareBrackets[k] + 1, indexesOfCloseSquareBrackets[k]);
                                    Integer periodo_anterior = 0;
                                    Integer periodo_siguiente = 0;
                                    if (value.contains(FORMULA_P_ANT)) {
                                        if (!value.contains(FORMULA_P_ANT_NUM_MESES_STRING)) {
                                            value = value.replaceAll(FORMULA_P_ANT, "");
                                            periodo_anterior = 1;
                                        } else {
                                            rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                                .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                    (FORMULA_P_ANT_NUM_MESES_STRING)));
                                            rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal()
                                                .replace(glsFormulaDefOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                    (FORMULA_P_ANT_NUM_MESES_STRING)));
                                        }
                                    }
                                    if (value.contains(FORMULA_P_SIG)) {
                                        value = value.replaceAll(FORMULA_P_SIG, "");
                                        periodo_siguiente = 1;
                                    }
                                    Integer pos = ConceptoService.getValueMatrixPosition(dtoList, value, rowData);
                                    if (pos >= 0) {
                                        if (periodo_anterior == 1) {

                                        }
                                        if (periodo_siguiente == 1) {

                                        } else {

                                        }
                                    } else if (pos == -1 && value.equals(FORMULA_CNPT)) {

                                    } else if (Constants.FORMULA_NUM_MESES_STRING_INDEX.equals(pos)) {

                                    } else if (Constants.FORMULA_IVA_STRING_INDEX.equals(pos)) {

                                    } else if (FORMULA_DEF_BASE_INDEX.equals(pos)) {

                                    } else if (FORMULA_DEF_PERI_INDEX.equals(pos)) {

                                    } else {
                                        if (!glsFormulaDef.contains(FORMULA_CNPT) && !glsFormulaDef.contains(CANT_MESES_PER1) &&
                                            !glsFormulaDef.contains(DEF_PER1) && !glsFormulaDef.contains(FORMULA_P_ANT_NUM_MESES_STRING)) {
                                            BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                                            errorDTO.setPeriod(null);
                                            errorDTO.setTableColumn(null);
                                            errorDTO.setTableRow(null);
                                            errorDTO.setErrorMessage("Error en Traduccion de Formula de deflactor de concepto :" + rowData.getCodTipoConcep() + "  " + rowData.getCodConcepto() + " -->  la Referencia no valida es :" + value);
                                            errorDTO.setIsRequired(true);
                                            validationErrorDTOS.add(errorDTO);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        if (ex.getMessage() != null) {
                            log.debug(ex.getMessage());
                        }
                    }
                }
            }
        }
        String conceptoMax = "";
        String conceptoMin = "";
        for (int i = 0; i < dtoList.size(); i++) {
            ConceptoDTO rowData = dtoList.get(i);
            if (conceptoDTO.getCodTipoConcep().equals(rowData.getCodTipoConcep()) && rowData.getCodConcepto() != null) {
                if (rowData.getCodConcepto().startsWith(conceptoDTO.getCodConcepto() + ".")) {
                    conceptoMax = rowData.getCodConcepto();
                    conceptoMin = conceptoDTO.getCodConcepto() + ".1";
                }
            }
        }
        if(conceptoDTO.getGlsFormulaVal()!= null && conceptoDTO.getGlsFormulaVal().contains("SUM(") && !conceptoMax.isEmpty() && !conceptoMin.isEmpty()) {
            if(!conceptoDTO.getGlsFormulaVal().contains(conceptoMax)) {
                BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                errorDTO.setPeriod(null);
                errorDTO.setTableColumn(null);
                errorDTO.setTableRow(null);
                errorDTO.setErrorMessage("Formula de Valor de concepto :" + conceptoDTO.getCodTipoConcep() + "  " + conceptoDTO.getCodConcepto() + " -->  El Concepto Maximo no es referenciado :" + conceptoMax);
                errorDTO.setIsRequired(true);
                validationErrorDTOS.add(errorDTO);
            }
            if(!conceptoDTO.getGlsFormulaVal().contains(conceptoMin)) {
                BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                errorDTO.setPeriod(null);
                errorDTO.setTableColumn(null);
                errorDTO.setTableRow(null);
                errorDTO.setErrorMessage("Formula de Valor de concepto :" + conceptoDTO.getCodTipoConcep() + "  " + conceptoDTO.getCodConcepto() + " -->  El Concepto Minimo no es referenciado :" + conceptoMin);
                errorDTO.setIsRequired(true);
                validationErrorDTOS.add(errorDTO);
            }
        }
        if(conceptoDTO.getGlsFormulaValWeb() != null &&  conceptoDTO.getGlsFormulaValWeb().contains("SUM(") && !conceptoMax.isEmpty() && !conceptoMin.isEmpty()) {
            if(!conceptoDTO.getGlsFormulaValWeb().contains(conceptoMax)) {
                BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                errorDTO.setPeriod(null);
                errorDTO.setTableColumn(null);
                errorDTO.setTableRow(null);
                errorDTO.setErrorMessage("Formula de Valor de concepto :" + conceptoDTO.getCodTipoConcep() + "  " + conceptoDTO.getCodConcepto() + " -->  El Concepto Maximo no es referenciado :" + conceptoMax);
                errorDTO.setIsRequired(true);
                validationErrorDTOS.add(errorDTO);
            }
            if(!conceptoDTO.getGlsFormulaValWeb().contains(conceptoMin)) {
                BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                errorDTO.setPeriod(null);
                errorDTO.setTableColumn(null);
                errorDTO.setTableRow(null);
                errorDTO.setErrorMessage("Formula de Valor de concepto :" + conceptoDTO.getCodTipoConcep() + "  " + conceptoDTO.getCodConcepto() + " -->  El Concepto Minimo no es referenciado :" + conceptoMin);
                errorDTO.setIsRequired(true);
                validationErrorDTOS.add(errorDTO);
            }
        }
        log.info("FIN to validate Concepto");
        return  validationErrorDTOS;
    }
    public  List<BreakdownValidationErrorDTO> validateConceptoSumatoriasValores(ConceptoDTO conceptoDTO) {
        log.info("INICIO to validate Concepto All: {}", conceptoDTO);
        ConceptoRequestDTO conceptoRequestDTO = new ConceptoRequestDTO();
        conceptoRequestDTO.templateCode = conceptoDTO.getCodTipoPlantilla();
        List<String> conceptosTypesCodes = new LinkedList<>();
        conceptosTypesCodes.add(COD_TIPO_CONCEP_BALANCE);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_GEN_FONDOS);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_IND_FINAN);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA);
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
        List<ConceptoDTO> dtoList = this.findByTemplateAndCodConceptsIn(conceptoRequestDTO,null).conceptoDTOList;
        List<BreakdownValidationErrorDTO> validationErrorDTOS =  new LinkedList<>();
        for (ConceptoDTO item : dtoList) {
            String conceptoMax = "";
            String conceptoMin = "";
            if((item.getGlsFormulaValOriginal()!= null && item.getGlsFormulaValOriginal().contains("SUM("))) {
                for (int i = 0; i < dtoList.size(); i++) {
                    ConceptoDTO rowData = dtoList.get(i);
                    if (item.getCodTipoConcep().equals(rowData.getCodTipoConcep()) && rowData.getCodConcepto() != null) {
                        if (rowData.getCodConcepto().startsWith(item.getCodConcepto() + ".")) {
                            conceptoMax = rowData.getCodConcepto();
                            conceptoMin = item.getCodConcepto() + ".1";
                        }
                    }
                }
            }
            if (item.getGlsFormulaValOriginal() != null && item.getGlsFormulaValOriginal().contains("SUM(") && !conceptoMax.isEmpty() && !conceptoMin.isEmpty()) {
                if (!item.getGlsFormulaValOriginal().contains(conceptoMax)) {
                    BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                    errorDTO.setPeriod(null);
                    errorDTO.setTableColumn(null);
                    errorDTO.setTableRow(null);
                    errorDTO.setErrorMessage("Formula de Valor de concepto :" + item.getCodTipoConcep() + "  " + item.getCodConcepto() + " -->  El Concepto Maximo no es referenciado :" + conceptoMax);
                    errorDTO.setIsRequired(true);
                    validationErrorDTOS.add(errorDTO);
                }
                if (!item.getGlsFormulaValOriginal().contains(conceptoMin)) {
                    BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                    errorDTO.setPeriod(null);
                    errorDTO.setTableColumn(null);
                    errorDTO.setTableRow(null);
                    errorDTO.setErrorMessage("Formula de Valor de concepto :" + item.getCodTipoConcep() + "  " + item.getCodConcepto() + " -->  El Concepto Minimo no es referenciado :" + conceptoMin);
                    errorDTO.setIsRequired(true);
                    validationErrorDTOS.add(errorDTO);
                }
            }
        }
        log.info("FIN to validate Concepto All");
        return  validationErrorDTOS;
    }

    /**
     * Get all the conceptos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ConceptoDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all Conceptos");
        log.info("FIN - Request to get all Conceptos");
        return conceptoRepository.findAll(pageable).map(conceptoMapper::toDto);
    }

    /**
     * Obtiene todos los conceptos para un tipo de plantilla y un código de concepto no parecido al dado.
     *
     * @param templateCode
     * 		- Código del tipo de plantilla.
     * @param conceptCode
     * 		- Cadena de caracteres con el formato que no debe cumplir el código del concepto. En caso que no se pase este parámetro se obtendrán todos por el tipo de plantilla.
     *
     * @return Lista de ConceptoDTO que cumplan con el filtro.
     */
    @Transactional(readOnly = true)
    public List<ConceptoDTO> findByTemplateConceptNotLike(Integer templateCode, String conceptCode, Integer flgConActivo) {
        log.info("INICIO - Request to get all Conceptos filtered by templateCode: {} and conceptCode: {} and flgConActivo: {}", templateCode, conceptCode, flgConActivo);
        List<Concepto> listaConceptos;
        if (conceptCode != null && !conceptCode.trim().isEmpty()) {
            if (flgConActivo == null) {
                listaConceptos = conceptoRepository.findAllByCodTipoPlantillaAndCodConceptoNotLike(templateCode, conceptCode);
            } else {
                listaConceptos = conceptoRepository.findAllByCodTipoPlantillaAndCodConceptoNotLikeAndFlgConActivo(templateCode, conceptCode, flgConActivo);
            }
        } else {
            if (flgConActivo == null) {
                listaConceptos = conceptoRepository.findAllByCodTipoPlantilla(templateCode);
            } else {
                listaConceptos = conceptoRepository.findAllByCodTipoPlantillaAndFlgConActivo(templateCode, flgConActivo);
            }
        }
        log.info("FIN - Request to get all Conceptos filtered by templateCode");
        return this.conceptoMapper.toDto(listaConceptos);
    }

    /**
     * Obtiene todos los conceptos para un tipo de plantilla y los códigos de conceptos dados. El método retornará la matriz de conceptos para los 4 períodos que se mostrarán en
     * pantalla con ls fórmulas correctas para cada caso.
     *
     * @param requestDTO
     * 		- Filtro para los conceptos.
     *
     * @return Lista de ConceptoDTO que cumplan con el filtro.
     */
    @Transactional(readOnly = true)
    public BreakdownFilterConceptDTO findByTemplateAndCodConceptsIn(ConceptoRequestDTO requestDTO,ConceptoRequestDTO requestRelacionadaDTO) {
        log.info("INICIO - Request to get all Conceptos filtered by templateCode: {} and conceptTypeCodesIn: {}", requestDTO.templateCode, requestDTO.conceptTypeCodesIn);
        List<ConceptoDTO> dtoList =  getConceptListToTranslate(requestDTO);
        List<ConceptoDTO> dtoMatrix = null;
        List<BreakdownValidationErrorDTO> validationErrorDTOS =  new LinkedList<>();
        if(requestRelacionadaDTO == null) {
            dtoMatrix = dtoList;
        } else {
            dtoMatrix = getConceptListToTranslate(requestRelacionadaDTO);
            updateFormulaValues(dtoMatrix);
        }
        if (dtoList != null && !dtoList.isEmpty()) {
            updateFormulaValues(dtoList);
            for (int i = 0; i < dtoList.size(); i++) {
                ConceptoDTO rowData = dtoList.get(i);
                // GLS_FORMULA_VAL
                String glsFormulaVal = "";
                String glsFormulaValOriginal = "";
                if (rowData.getGlsFormulaValWeb() != null && !rowData.getGlsFormulaValWeb().trim().isEmpty()) {
                    glsFormulaVal = rowData.getGlsFormulaValWeb();
                    glsFormulaValOriginal = rowData.getGlsFormulaValWeb();
                    rowData.setGlsFormulaVal(glsFormulaVal);
                } else {
                    glsFormulaVal = rowData.getGlsFormulaVal();
                    glsFormulaValOriginal = rowData.getGlsFormulaVal();
                }
                try {
                    if (glsFormulaVal != null && !glsFormulaVal.trim().isEmpty() &&
                        (rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_BALANCE) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_EST_RESULT) ||
                            rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_EST_RESULT))) {
                        boolean indexOfEquals = glsFormulaVal.contains(Constants.EQUALS_SIGN);
                        if (indexOfEquals) {
                            Integer[] indexesOfOpenSquareBrackets = Utils.indexesOf(glsFormulaVal, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT);
                            Integer[] indexesOfCloseSquareBrackets =
                                indexesOfOpenSquareBrackets.length > 0 ? Utils.indexesOf(glsFormulaVal, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT) : new Integer[0];
                            for (int k = 0; k < indexesOfOpenSquareBrackets.length; k++) { // La fórmula no está traducida.
                                String value = glsFormulaVal.substring(indexesOfOpenSquareBrackets[k] + 1, indexesOfCloseSquareBrackets[k]);
                                Integer pos = ConceptoService.getValueMatrixPosition(dtoMatrix, value, rowData);
                                if (pos >= 0) {
                                    rowData.setGlsFormulaVal(rowData.getGlsFormulaVal()
                                        .replace(glsFormulaVal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                            (requestDTO.columnLetterGlsFormulaVal1 + (pos + 1 /*+ incrementRow*/))));
                                } else if (Constants.FORMULA_NUM_MESES_STRING_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaVal(rowData.getGlsFormulaVal()
                                        .replace(glsFormulaVal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), CANT_MESES_PER1));
                                    rowData.setGlsFormulaValOriginal(rowData.getGlsFormulaValOriginal()
                                        .replace(glsFormulaValOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), CANT_MESES_PER1));
                                } else if (Constants.FORMULA_IVA_STRING_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaVal(rowData.getGlsFormulaVal()
                                        .replace(glsFormulaVal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_BASE_IVA));
                                    rowData.setGlsFormulaValOriginal(rowData.getGlsFormulaValOriginal()
                                        .replace(glsFormulaValOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_BASE_IVA));
                                } else if (FORMULA_DEF_BASE_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaVal(rowData.getGlsFormulaVal()
                                        .replace(glsFormulaVal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_PERIODS));
                                    rowData.setGlsFormulaValOriginal(rowData.getGlsFormulaValOriginal()
                                        .replace(glsFormulaValOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_PERIODS));
                                } else if (FORMULA_DEF_PERI_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaVal(
                                        rowData.getGlsFormulaVal().replace(glsFormulaVal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEF_PER1));
                                    rowData.setGlsFormulaValOriginal(
                                        rowData.getGlsFormulaValOriginal().replace(glsFormulaValOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEF_PER1));
                                } else {
                                    if(!glsFormulaVal.contains(CANT_MESES_PER1) && !glsFormulaVal.contains(DEF_PER1)) {
                                        BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                                        errorDTO.setPeriod(null);
                                        errorDTO.setTableColumn(null);
                                        errorDTO.setTableRow(i+1);
                                        errorDTO.setErrorMessage("Error en Traduccion de Formula de Valor: " + glsFormulaVal + " Concepto :" + rowData.getCodTipoConcep() + "  " + rowData.getCodConcepto());
                                        errorDTO.setIsRequired(true);
                                        validationErrorDTOS.add(errorDTO);
                                    }
                                }
                            }

                            // Período 2
                            rowData.setGlsFormulaVal2(rowData.getGlsFormulaVal().replaceAll(requestDTO.columnLetterGlsFormulaVal1, requestDTO.columnLetterGlsFormulaVal2));
                            rowData.setGlsFormulaVal2(rowData.getGlsFormulaVal2().replaceAll(CANT_MESES_PER1, CANT_MESES_PER2));
                            rowData.setGlsFormulaVal2(rowData.getGlsFormulaVal2().replaceAll(DEF_PER1, DEF_PER2));
                            // Período 3
                            rowData.setGlsFormulaVal3(rowData.getGlsFormulaVal().replaceAll(requestDTO.columnLetterGlsFormulaVal1, requestDTO.columnLetterGlsFormulaVal3));
                            rowData.setGlsFormulaVal3(rowData.getGlsFormulaVal3().replaceAll(CANT_MESES_PER1, CANT_MESES_PER3));
                            rowData.setGlsFormulaVal3(rowData.getGlsFormulaVal3().replaceAll(DEF_PER1, DEF_PER3));
                            // Período 4
                            rowData.setGlsFormulaVal4(rowData.getGlsFormulaVal().replaceAll(requestDTO.columnLetterGlsFormulaVal1, requestDTO.columnLetterGlsFormulaVal4));
                            rowData.setGlsFormulaVal4(rowData.getGlsFormulaVal4().replaceAll(CANT_MESES_PER1, CANT_MESES_PER4));
                            rowData.setGlsFormulaVal4(rowData.getGlsFormulaVal4().replaceAll(DEF_PER1, DEF_PER4));

                        }
                    }
                } catch (Exception ex) {
                    if(ex.getMessage()!= null) {
                        log.debug(ex.getMessage());
                    }
                }

                // GLS_FORMULA_PRC
                String glsFormulaPrc = "";
                String glsFormulaPrcOriginal = "";
                if (rowData.getGlsFormulaPrcWeb() != null && !rowData.getGlsFormulaPrcWeb().trim().isEmpty()) {
                    glsFormulaPrc = rowData.getGlsFormulaPrcWeb();
                    glsFormulaPrcOriginal = rowData.getGlsFormulaPrcWeb();
                    rowData.setGlsFormulaPrc(glsFormulaPrc);
                } else {
                    glsFormulaPrc = rowData.getGlsFormulaPrc();
                    glsFormulaPrcOriginal = rowData.getGlsFormulaPrc();
                }
                try {
                    if (glsFormulaVal != null && !glsFormulaVal.trim().isEmpty() &&
                        (rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_BALANCE) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_EST_RESULT) ||
                            rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_EST_RESULT) ||
                            rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM))) {
                        boolean indexOfEquals = glsFormulaPrc.contains("=");
                        if (indexOfEquals) {
                            Integer[] indexesOfOpenSquareBrackets = Utils.indexesOf(glsFormulaPrc, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT);
                            Integer[] indexesOfCloseSquareBrackets =
                                indexesOfOpenSquareBrackets.length > 0 ? Utils.indexesOf(glsFormulaPrc, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT) : new Integer[0];
                            for (int k = 0; k < indexesOfOpenSquareBrackets.length; k++) { // La fórmula no está traducida.
                                String value = glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k] + 1, indexesOfCloseSquareBrackets[k]);
                                Integer periodo_anterior = 0;
                                Integer periodo_siguiente = 0;
                                if (value.contains(FORMULA_P_ANT)) {
                                    if(!value.contains(FORMULA_P_ANT_NUM_MESES_STRING)) {
                                        value = value.replaceAll(FORMULA_P_ANT, "");
                                        periodo_anterior = 1;
                                    } else {
                                        rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                            .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_ANT_NUM_MESES_STRING)));
                                        rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal()
                                            .replace(glsFormulaPrcOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_ANT_NUM_MESES_STRING)));
                                    }
                                }
                                if(value.contains(FORMULA_P_SIG))  {
                                    value = value.replaceAll(FORMULA_P_SIG, "");
                                    periodo_siguiente = 1;
                                }
                                Integer pos = ConceptoService.getValueMatrixPosition(dtoMatrix, value, rowData);
                                // Utils.convertToNumberingScheme(1)
                                if (pos >= 0) {
                                    if (periodo_anterior == 1) {
                                        rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc()
                                            .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_ANT + (pos + 1/*+ incrementRow*/))));
                                    }
                                    if(periodo_siguiente == 1) {
                                        rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc()
                                            .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_SIG + (pos + 1/*+ incrementRow*/))));
                                    }
                                    else {
                                        rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc()
                                            .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (requestDTO.columnLetterGlsFormulaPrc1 + (pos + 1 /*+ incrementRow*/))));
                                    }
                                } else if (pos == -1 && value.equals(FORMULA_CNPT)) {
                                    rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc()
                                        .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                            (FORMULA_CNPT + (i + 1/* + incrementRow*/))));
                                }
                                 else if (Constants.FORMULA_NUM_MESES_STRING_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc()
                                        .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), CANT_MESES_PER1));
                                    rowData.setGlsFormulaPrcOriginal(rowData.getGlsFormulaPrcOriginal()
                                        .replace(glsFormulaPrcOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), CANT_MESES_PER1));
                                } else if (Constants.FORMULA_IVA_STRING_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc()
                                        .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_BASE_IVA));
                                    rowData.setGlsFormulaPrcOriginal(rowData.getGlsFormulaPrcOriginal()
                                        .replace(glsFormulaPrcOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_BASE_IVA));
                                } else if (FORMULA_DEF_BASE_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc()
                                        .replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_PERIODS));
                                    rowData.setGlsFormulaPrcOriginal(rowData.getGlsFormulaPrcOriginal()
                                        .replace(glsFormulaPrcOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_PERIODS));
                                } else if (FORMULA_DEF_PERI_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaPrc(
                                        rowData.getGlsFormulaPrc().replace(glsFormulaPrc.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEF_PER1));
                                    rowData.setGlsFormulaPrcOriginal(
                                        rowData.getGlsFormulaPrcOriginal().replace(glsFormulaPrcOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEF_PER1));
                                } else {
                                    if(!glsFormulaPrc.contains(CANT_MESES_PER1) && !glsFormulaPrc.contains(DEF_PER1)) {
                                        BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                                        errorDTO.setPeriod(null);
                                        errorDTO.setTableColumn(null);
                                        errorDTO.setTableRow(i+1);
                                        errorDTO.setErrorMessage("Error en Traduccion de Formula de Porcentaje: " + glsFormulaPrc + " Concepto :" + rowData.getCodTipoConcep() + "  " + rowData.getCodConcepto());
                                        errorDTO.setIsRequired(true);
                                        validationErrorDTOS.add(errorDTO);
                                    }
                                }
                            }

                            // Período 2
                            rowData.setGlsFormulaPrc2(rowData.getGlsFormulaPrc().replaceAll(requestDTO.columnLetterGlsFormulaPrc1, requestDTO.columnLetterGlsFormulaPrc2));
                            rowData.setGlsFormulaPrc2(rowData.getGlsFormulaPrc2().replaceAll(CANT_MESES_PER1, CANT_MESES_PER2));
                            rowData.setGlsFormulaPrc2(rowData.getGlsFormulaPrc2().replaceAll(DEF_PER1, DEF_PER2));
                            // Período 3
                            rowData.setGlsFormulaPrc3(rowData.getGlsFormulaPrc().replaceAll(requestDTO.columnLetterGlsFormulaPrc1, requestDTO.columnLetterGlsFormulaPrc3));
                            rowData.setGlsFormulaPrc3(rowData.getGlsFormulaPrc3().replaceAll(CANT_MESES_PER1, CANT_MESES_PER3));
                            rowData.setGlsFormulaPrc3(rowData.getGlsFormulaPrc3().replaceAll(DEF_PER1, DEF_PER3));
                            // Período 4
                            rowData.setGlsFormulaPrc4(rowData.getGlsFormulaPrc().replaceAll(requestDTO.columnLetterGlsFormulaPrc1, requestDTO.columnLetterGlsFormulaPrc4));
                            rowData.setGlsFormulaPrc4(rowData.getGlsFormulaPrc4().replaceAll(CANT_MESES_PER1, CANT_MESES_PER4));
                            rowData.setGlsFormulaPrc4(rowData.getGlsFormulaPrc4().replaceAll(DEF_PER1, DEF_PER4));

                        }
                    }
                } catch (Exception ex) {
                    if(ex.getMessage()!= null) {
                        log.debug(ex.getMessage());
                    }
                }

                // GLS_FORMULA_DEF
                String glsFormulaDef = "";
                String glsFormulaDefOriginal = "";
                if (rowData.getGlsFormulaDefWeb() != null && !rowData.getGlsFormulaDefWeb().trim().isEmpty()) {
                    glsFormulaDef = rowData.getGlsFormulaDefWeb();
                    glsFormulaDefOriginal = rowData.getGlsFormulaDefWeb();
                    rowData.setGlsFormulaDef(glsFormulaDef);
                } else {
                    glsFormulaDef = rowData.getGlsFormulaDef();
                    glsFormulaDefOriginal = rowData.getGlsFormulaDef();
                }
                try {
                    if (glsFormulaDef != null && !glsFormulaDef.trim().isEmpty()) {
                        boolean indexOfEquals = glsFormulaDef.contains("=");
                        if (indexOfEquals) {
                            Integer[] indexesOfOpenSquareBrackets = Utils.indexesOf(glsFormulaDef, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT);
                            Integer[] indexesOfCloseSquareBrackets =
                                indexesOfOpenSquareBrackets.length > 0 ? Utils.indexesOf(glsFormulaDef, Constants.SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT) : new Integer[0];
                            for (int k = 0; k < indexesOfOpenSquareBrackets.length; k++) { // La fórmula no está traducida.
                                String value = glsFormulaDef.substring(indexesOfOpenSquareBrackets[k] + 1, indexesOfCloseSquareBrackets[k]);
                                Integer periodo_anterior = 0;
                                Integer periodo_siguiente = 0;
                                if (value.contains(FORMULA_P_ANT)) {
                                    if(!value.contains(FORMULA_P_ANT_NUM_MESES_STRING)) {
                                        value = value.replaceAll(FORMULA_P_ANT, "");
                                        periodo_anterior = 1;
                                    } else {
                                        rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                            .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_ANT_NUM_MESES_STRING)));
                                        rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal()
                                            .replace(glsFormulaDefOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_ANT_NUM_MESES_STRING)));
                                    }
                                }
                                if(value.contains(FORMULA_P_SIG))  {
                                    value = value.replaceAll(FORMULA_P_SIG, "");
                                    periodo_siguiente = 1;
                                }
                                Integer pos = ConceptoService.getValueMatrixPosition(dtoMatrix, value, rowData);
                                if (pos >= 0) {
                                    if (periodo_anterior == 1) {
                                        rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                            .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_ANT + (pos + 1/*+ incrementRow*/))));
                                    }
                                    if(periodo_siguiente == 1) {
                                        rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                            .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (FORMULA_P_SIG + (pos + 1/*+ incrementRow*/))));
                                    }
                                    else {
                                        rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                            .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                                (requestDTO.columnLetterGlsFormulaDef1 + (pos + 1 /*+ incrementRow*/))));
                                    }
                                } else if (pos == -1 && value.equals(FORMULA_CNPT)) {
                                    rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                        .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1),
                                            (FORMULA_CNPT + (i + 1/* + incrementRow*/))));
                                } else if (Constants.FORMULA_NUM_MESES_STRING_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                        .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), CANT_MESES_PER1));
                                    rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal()
                                        .replace(glsFormulaDefOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), CANT_MESES_PER1));
                                } else if (Constants.FORMULA_IVA_STRING_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                        .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_BASE_IVA));
                                    rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal()
                                        .replace(glsFormulaDefOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_BASE_IVA));
                                } else if (FORMULA_DEF_BASE_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaDef(rowData.getGlsFormulaDef()
                                        .replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_PERIODS));
                                    rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal()
                                        .replace(glsFormulaDefOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEFLACT_PERIODS));
                                } else if (FORMULA_DEF_PERI_INDEX.equals(pos)) {
                                    rowData.setGlsFormulaDef(
                                        rowData.getGlsFormulaDef().replace(glsFormulaDef.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEF_PER1));
                                    rowData.setGlsFormulaDefOriginal(
                                        rowData.getGlsFormulaDefOriginal().replace(glsFormulaDefOriginal.substring(indexesOfOpenSquareBrackets[k], indexesOfCloseSquareBrackets[k] + 1), DEF_PER1));
                                } else {
                                    if(!glsFormulaDef.contains(FORMULA_CNPT) && !glsFormulaDef.contains(CANT_MESES_PER1) &&
                                        !glsFormulaDef.contains(DEF_PER1) && !glsFormulaDef.contains(FORMULA_P_ANT_NUM_MESES_STRING)) {
                                        BreakdownValidationErrorDTO errorDTO = new BreakdownValidationErrorDTO();
                                        errorDTO.setPeriod(null);
                                        errorDTO.setTableColumn(null);
                                        errorDTO.setTableRow(i+1);
                                        errorDTO.setErrorMessage("Error en Traduccion de Formula de Deflactor: " + glsFormulaDef + " Concepto :" + rowData.getCodTipoConcep() + "  " + rowData.getCodConcepto());
                                        errorDTO.setIsRequired(true);
                                        validationErrorDTOS.add(errorDTO);
                                    }
                                }
                            }

                            // Período 2
                            rowData.setGlsFormulaDef2(rowData.getGlsFormulaDef().replaceAll(requestDTO.columnLetterGlsFormulaDef1, requestDTO.columnLetterGlsFormulaDef2));
                            rowData.setGlsFormulaDef2(rowData.getGlsFormulaDef2().replaceAll(FORMULA_CNPT, requestDTO.columnLetterGlsFormulaVal2));
                            rowData.setGlsFormulaDef2(rowData.getGlsFormulaDef2().replaceAll(CANT_MESES_PER1, CANT_MESES_PER2));
                            rowData.setGlsFormulaDef2(rowData.getGlsFormulaDef2().replaceAll(DEF_PER1, DEF_PER2));
                            rowData.setGlsFormulaDef2(rowData.getGlsFormulaDef2().replaceAll(FORMULA_P_ANT_NUM_MESES_STRING, CANT_MESES_PER1));
                            // Período 3
                            rowData.setGlsFormulaDef3(rowData.getGlsFormulaDef().replaceAll(requestDTO.columnLetterGlsFormulaDef1, requestDTO.columnLetterGlsFormulaDef3));
                            rowData.setGlsFormulaDef3(rowData.getGlsFormulaDef3().replaceAll(FORMULA_CNPT, requestDTO.columnLetterGlsFormulaVal3));
                            rowData.setGlsFormulaDef3(rowData.getGlsFormulaDef3().replaceAll(CANT_MESES_PER1, CANT_MESES_PER3));
                            rowData.setGlsFormulaDef3(rowData.getGlsFormulaDef3().replaceAll(DEF_PER1, DEF_PER3));
                            rowData.setGlsFormulaDef3(rowData.getGlsFormulaDef3().replaceAll(FORMULA_P_ANT_NUM_MESES_STRING, CANT_MESES_PER2));
                            // Período 4
                            rowData.setGlsFormulaDef4(rowData.getGlsFormulaDef().replaceAll(requestDTO.columnLetterGlsFormulaDef1, requestDTO.columnLetterGlsFormulaDef4));
                            rowData.setGlsFormulaDef4(rowData.getGlsFormulaDef4().replaceAll(FORMULA_CNPT, requestDTO.columnLetterGlsFormulaVal4));
                            rowData.setGlsFormulaDef4(rowData.getGlsFormulaDef4().replaceAll(CANT_MESES_PER1, CANT_MESES_PER4));
                            rowData.setGlsFormulaDef4(rowData.getGlsFormulaDef4().replaceAll(DEF_PER1, DEF_PER4));
                            rowData.setGlsFormulaDef4(rowData.getGlsFormulaDef4().replaceAll(FORMULA_P_ANT_NUM_MESES_STRING, CANT_MESES_PER3));

                            // periodo 1
                            rowData.setGlsFormulaDef(rowData.getGlsFormulaDef().replaceAll(FORMULA_P_ANT_NUM_MESES_STRING, CANT_MESES_PER0));
                            rowData.setGlsFormulaDef(rowData.getGlsFormulaDef().replaceAll(FORMULA_CNPT, requestDTO.columnLetterGlsFormulaVal1));
                            rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDefOriginal().replaceAll(FORMULA_P_ANT_NUM_MESES_STRING, CANT_MESES_PER0));

                        }
                    }
                } catch (Exception ex) {
                    if(ex.getMessage()!= null) {
                        log.debug(ex.getMessage());
                    }
                }
            }
        }
        log.info("FIN - Request to get all Conceptos filtered by templateCode");
        BreakdownFilterConceptDTO breakdownFilterConceptDTO = new BreakdownFilterConceptDTO();
        breakdownFilterConceptDTO.conceptoDTOList = dtoList;
        breakdownFilterConceptDTO.validationErrorDTOS = validationErrorDTOS;
        return breakdownFilterConceptDTO;
    }

    /**
     * Obtiene todos los conceptos para un tipo de plantilla y los códigos de conceptos dados. El método retornará la matriz de conceptos para los 4 períodos que se mostrarán en
     * pantalla con ls fórmulas correctas para cada caso.
     *
     * @param requestDTO
     * 		- Filtro para los conceptos.
     *
     * @return Lista de ConceptoDTO que cumplan con el filtro.
     */
    @Transactional(readOnly = true)
    public List<ConceptoDTO> findProjectionConceptsByTemplateAndCodConceptsIn(ConceptoRequestDTO requestDTO) {
        log.info("INICIO - Request to findProjectionConceptsByTemplateAndCodConceptsIn [templateCode={}, conceptTypeCodesIn={}", requestDTO.templateCode,
            requestDTO.conceptTypeCodesIn);

        // List<List<ConceptoDTO>> matrix = new ArrayList<>();
        List<ConceptoDTO> dtoList = getConceptListToTranslate(requestDTO);
        if (dtoList != null && !dtoList.isEmpty()) {
            updateFormulaValues(dtoList);
        }
        log.info("FIN - Request to findProjectionConceptsByTemplateAndCodConceptsIn");
        return dtoList;
    }

    /**
     * Reemplaza en todas las fórmulas las letras de la columna para la que se tradujeron por la columna referente al período que se esté representando.
     *
     * @param dtoList2
     * @param textToFindVal
     * @param textToReplaceVal
     * @param textToFindPrc
     * @param textToReplacePrc
     * @param textToFindDef
     * @param textToReplaceDef
     */
    private void replaceColumnLetterInPeriod(List<ConceptoDTO> dtoList2, String textToFindVal, String textToReplaceVal, String textToFindPrc, String textToReplacePrc,
        String textToFindDef, String textToReplaceDef) {
        for (int i = 0; i < dtoList2.size(); i++) {
            ConceptoDTO rowData = dtoList2.get(i);
            // GLS_FORMULA_VAL
            String glsFormulaVal = rowData.getGlsFormulaVal();
            if (glsFormulaVal != null && !glsFormulaVal.trim().isEmpty()) {
                boolean indexOfEquals = glsFormulaVal.contains("=");
                if (indexOfEquals) {
                    rowData.setGlsFormulaVal(rowData.getGlsFormulaVal().replaceAll(textToFindVal, textToReplaceVal));
                }
            }
            // GLS_FORMULA_PRC
            String glsFormulaPrc = rowData.getGlsFormulaPrc();
            if (glsFormulaPrc != null && !glsFormulaPrc.trim().isEmpty()) {
                boolean indexOfEquals = glsFormulaPrc.contains("=");
                if (indexOfEquals) {
                    rowData.setGlsFormulaPrc(rowData.getGlsFormulaPrc().replaceAll(textToFindPrc, textToReplacePrc));
                }
            }
            // GLS_FORMULA_DEF
            String glsFormulaDef = rowData.getGlsFormulaDef();
            if (glsFormulaDef != null && !glsFormulaDef.trim().isEmpty()) {
                boolean indexOfEquals = glsFormulaDef.contains("=");
                if (indexOfEquals) {
                    rowData.setGlsFormulaDef(rowData.getGlsFormulaDef().replaceAll(textToFindDef, textToReplaceDef));
                }
            }
        }
    }

    /**
     * Busca un concepto en la matriz y devuelve un número de fila.
     *
     * @param dtoList
     * @param i
     * @param value
     * @param conceptoDTO
     *
     * @return
     */
    public static Integer getValueMatrixPosition(List<ConceptoDTO> dtoList, String value, ConceptoDTO conceptoDTO) {
        Integer pos;
        if (Utils.isNumberAndDot(value)) { // Si es una referencia simple a un código de concepto
            pos = IntStream.range(0, dtoList.size())
                .filter(p -> value.equals(dtoList.get(p).getCodConcepto()) && conceptoDTO.getCodTipoConcep().equals(dtoList.get(p).getCodTipoConcep())).findFirst().orElse(-1);
        } else if (Constants.FORMULA_IVA_STRING.equals(value)) { // Es una referencia a un valor DE VARIABLE DE  HANSONTABLE [IVA]
            pos = Constants.FORMULA_IVA_STRING_INDEX;
        } else if (Constants.FORMULA_NUM_MESES_STRING.equals(value)) { // Es una referencia a un valor DE VARIABLE DE  HANSONTABLE [MESES]
            pos = Constants.FORMULA_NUM_MESES_STRING_INDEX;
        } else if (Constants.FORMULA_P_ANT_NUM_MESES_STRING.equals(value)) { // Es una referencia a un valor DE VARIABLE DE  HANSONTABLE [P_ANT.NUM_MESES]
            pos = Constants.FORMULA_P_ANT_NUM_MESES_STRING_INDEX;
        } else if (Constants.FORMULA_DEF_BASE.equals(value)) { // Es una referencia a un valor DE VARIABLE DE  HANSONTABLE []
            pos = Constants.FORMULA_DEF_BASE_INDEX;
        } else if (Constants.FORMULA_DEF_PERI.equals(value)) { // Es una referencia a un valor DE VARIABLE DE  HANSONTABLE []
            pos = Constants.FORMULA_DEF_PERI_INDEX;
        } else { // Es un valor de búsqueda compuesta, por código y tipo
            pos = findConceptPosition(dtoList, value);
        }
        return pos;
    }

    /**
     * @param codPlantilla
     * 		* @param codConcepto
     *
     * @return
     *
     * @author carlos eduardo rodriguez perez
     */
    @Transactional(readOnly = true)
    public List<ConceptoDTO> findAllByCodTipoPlantillaLike(Integer codPlantilla, String codTipoConcepto, String corConcepto, Integer flgConActivo) {
        log.info("INICIO - Request to get all Conceptos");
        List<Concepto> listaConceptos;
        if (flgConActivo == null) {
            listaConceptos = conceptoRepository.findAllByCodTipoPlantillaAndCodTipoConcepAndCodConceptoLikeOrderByCodConceptoAsc(codPlantilla, codTipoConcepto, corConcepto);
        } else {
            listaConceptos = conceptoRepository
                .findAllByCodTipoPlantillaAndCodTipoConcepAndCodConceptoLikeAndFlgConActivoOrderByNumSecuenciaAsc(codPlantilla, codTipoConcepto, corConcepto, flgConActivo);
        }
        log.info("FIN - Request to get all Conceptos");
        return this.conceptoMapper.toDto(listaConceptos);
    }

    /**
     * Get one concepto by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ConceptoDTO> findOne(ConceptoPK id) {
        log.info("INICIO - Request to get Concepto : {}", id);
        log.info("FIN - Request to get Concepto");
        return conceptoRepository.findById(id).map(conceptoMapper::toDto);
    }

    public Integer resecuenciar(Integer codPlantilla){
        log.info("INICIO Resecuenciar codigo de plantilla : {}", codPlantilla);
        Param<Integer> p1 = new Param<>(ConceptoService.BCT_SP_RESECUENCIAR_PI_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(ConceptoService.BCT_SP_RESECUENCIAR_PI_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String>  p3 = new Param<>(ConceptoService.BCT_SP_RESECUENCIAR_PI_GLOSA, String.class, null, ParameterMode.OUT);
        List<Param<?>> parameters = Arrays.asList(p1,p2,p3);
        log.debug("QUERY: " +ConceptoService.BCT_SP_RESECUENCIAR );
        log.debug("PARAMETROS: " + parameters.toString());
        Map<String, Object> objects = this.sqlbctRepository.executeNativeStoredProcedureOutputNoSelect(ConceptoService.BCT_SP_RESECUENCIAR,parameters);
        Integer returnParam = (Integer) objects.get(ConceptoService.BCT_SP_RESECUENCIAR_PI_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ConceptoService.BCT_SP_RESECUENCIAR);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(ConceptoService.BCT_SP_RESECUENCIAR_PI_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }
        log.info("FIN Resecuenciar codigo de plantilla" );
        return returnParam;
    }

    /**
     * Delete the concepto by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(ConceptoPK id) {
        log.info("INICIO - Request to delete Concepto : {}", id);
        log.info("FIN - Request to delete Concepto");
        conceptoRepository.deleteById(id);
    }

    public BigDecimal maxCorConcepto(String codTipoConcepto, Integer codTipoPlantilla) {
        log.info("INICIO - Extraemos el Concepto máximo");
        Map<String, Object> listaParametros = new HashMap<String, Object>();
        listaParametros.put("codTipoConcepto", codTipoConcepto);
        log.debug("QUERY: " + SQL_GET_MAX_COR_CONCEPTO);
        log.debug("PARAMETROS: " + listaParametros.toString());
        log.info("FIN - Extraemos el Concepto máximo");
        return sqlbctRepository.countSql(SQL_GET_MAX_COR_CONCEPTO, listaParametros);
    }

    /**
     * Buscar la posición del concepto.
     *
     * @param dtoList
     * @param value
     *
     * @return
     */
    @NotNull
    private static Integer findConceptPosition(List<ConceptoDTO> dtoList, String value) {
        String codeConcept = value.substring(value.indexOf(".") + 1);
        String codeConceptType = value.replaceAll("[^A-Z_].*", "");
        Integer pos =
            IntStream.range(0, dtoList.size()).filter(p -> codeConcept.equals(dtoList.get(p).getCodConcepto()) && codeConceptType.equals(dtoList.get(p).getCodTipoConcep()))
                .findFirst().orElse(-1);
        //      .reduce((first, second) -> second).orElse(-1); // .reduce((first, second) -> second) reduce dejando solamente el el último elemento
        return pos;
    }

    /**
     * Busca los conceptos.
     *
     * @param requestDTO
     *
     * @return
     */
    private List<ConceptoDTO> getConceptListToTranslate(ConceptoRequestDTO requestDTO) {
        Sort orderBy = null;
        if (requestDTO.orderByFields != null && !requestDTO.orderByFields.isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC;
            if (requestDTO.orderByDirection != null && !requestDTO.orderByDirection.trim().isEmpty()) {
                direction = requestDTO.orderByDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
            }
            orderBy = Sort.by(direction, requestDTO.orderByFields.toArray(new String[0]));
        }
        List<Concepto> list;
        if (orderBy != null) {
            if (requestDTO.flgConActivo == null) {
                list = conceptoRepository.findAllByCodTipoPlantillaAndCodTipoConcepIn(requestDTO.templateCode, requestDTO.conceptTypeCodesIn, orderBy);
            } else {
                list = conceptoRepository
                    .findAllByCodTipoPlantillaAndCodTipoConcepInAndFlgConActivo(requestDTO.templateCode, requestDTO.conceptTypeCodesIn, requestDTO.flgConActivo, orderBy);
            }
        } else {
            if (requestDTO.flgConActivo == null) {
                list = conceptoRepository.findAllByCodTipoPlantillaAndCodTipoConcepIn(requestDTO.templateCode, requestDTO.conceptTypeCodesIn);

            } else {
                list =
                    conceptoRepository.findAllByCodTipoPlantillaAndCodTipoConcepInAndFlgConActivo(requestDTO.templateCode, requestDTO.conceptTypeCodesIn, requestDTO.flgConActivo);
            }
        }
        return this.conceptoMapper.toDto(list);
    }

    /**
     * Actualiza valores en las fórmulas.
     *
     * @param dtoList
     */
    private void updateFormulaValues(List<ConceptoDTO> dtoList) {
        String lastGroup = "";
        // Integer incrementRow = 0;
        // Agregar las filas que son encabezados de grupos de conceptos.
        for (int i = 0; i < dtoList.size(); i++) {
            ConceptoDTO rowData = dtoList.get(i);
            if (lastGroup.isEmpty() || !lastGroup.equals(rowData.getCodTipoConcep())) {
                lastGroup = rowData.getCodTipoConcep();
                ConceptoDTO groupHeader = new ConceptoDTO();
                groupHeader.setCodTipoConcep(lastGroup); // Columna A 'COD_TIPO_CONCEP', BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                groupHeader.setFlgIsTotal(1);// Columna F 'FLG_IS_TOTAL', Si es un total (no es editable) 1 sino 0
                groupHeader.setGlsConcepto(lastGroup + rowData.getCodTipoPlantilla()); // Columna H
                groupHeader.setFlgIsPrinted(1);
                dtoList.add(i, groupHeader);
            }
        }
        // TODO: Pasar el contenido del ciclo for para el de arriba, dejarlo en 1 solo
        // Añadimos las fórmulas correctas al encabesado y detalle de fila
        for (int i = 0; i < dtoList.size(); i++) {
            ConceptoDTO rowData = dtoList.get(i);
            if (rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_GEN_FONDOS) || rowData.getCodTipoConcep().equals(COD_TIPO_CONCEP_IND_FINAN)) {
                if(rowData.getGlsFormulaValWeb()!=null && !rowData.getGlsFormulaValWeb().trim().isEmpty()) {
                    dtoList.get(i).setGlsFormulaDef(rowData.getGlsFormulaValWeb());
                } else {
                    dtoList.get(i).setGlsFormulaDef(rowData.getGlsFormulaVal());
                }
            }
        }
        for (int i = 0; i < dtoList.size(); i++) {
            ConceptoDTO rowData = dtoList.get(i);
            rowData.setGlsFormulaValOriginal(rowData.getGlsFormulaVal());
            rowData.setGlsFormulaDefOriginal(rowData.getGlsFormulaDef());
            rowData.setGlsFormulaPrcOriginal(rowData.getGlsFormulaPrc());
        }
    }
}
