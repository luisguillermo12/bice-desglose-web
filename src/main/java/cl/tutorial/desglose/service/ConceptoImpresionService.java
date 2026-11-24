package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ConceptoImpresionCif;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.ConceptoImpresionCifRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.ConceptoImpresion;
import cl.tutorial.desglose.domain.pk.ConceptoImpresionPK;
import cl.tutorial.desglose.repository.ConceptoImpresionRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.BreakdownSaveRequestDTO;
import cl.tutorial.desglose.service.dto.ConceptoImpresionDTO;
import cl.tutorial.desglose.service.dto.ConceptoImpresionRequestDTO;
import cl.tutorial.desglose.service.mapper.ConceptoImpresionMapper;
import cl.tutorial.desglose.service.mapper.ConceptoImpresionCifMapper;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureBusinessError;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.ESQUEMA_CIF;
import static cl.tutorial.desglose.config.Constants.ESQUEMA_DGL;

/**
 * Service Implementation for managing ConceptoImpresion.
 */
@Service
@Transactional
public class ConceptoImpresionService {

    private final Logger log = LogManager.getLogger(ConceptoImpresionService.class);

    private final ConceptoImpresionRepository conceptoImpresionRepository;

    private final ConceptoImpresionCifRepository conceptoImpresionCifRepository;

    private final SQLDGLRepository SQLDGLRepository;

    private final ConceptoImpresionMapper conceptoImpresionMapper;
    private final ConceptoImpresionCifMapper conceptoImpresionCifMapper;
    private static final String SP_CIF_SP_INS_CONIMP                      = "DGL_SP_INS_CONIMP_WK";
    private static final String SP_CIF_SP_INS_CONIMP_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_CONIMP_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_CONIMP_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_CONIMP_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_INS_CONIMP_05_P_COD_TIPO_CONCEP = "P_COD_TIPO_CONCEP";
    private static final String SP_CIF_SP_INS_CONIMP_06_P_COR_CONCEPTO    = "P_COR_CONCEPTO";
    private static final String SP_CIF_SP_INS_CONIMP_07_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_INS_CONIMP_08_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_INS_CONIMP_09_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    private static final String SP_CIF_SP_DEL_CONIMP                      = "DGL_SP_DEL_CONIMP_WK";
    private static final String SP_CIF_SP_DEL_CONIMP_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_DEL_CONIMP_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_DEL_CONIMP_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_DEL_CONIMP_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_DEL_CONIMP_05_P_COD_TIPO_CONCEP = "P_COD_TIPO_CONCEP";
    private static final String SP_CIF_SP_DEL_CONIMP_06_P_COR_CONCEPTO    = "P_COR_CONCEPTO";
    private static final String SP_CIF_SP_DEL_CONIMP_07_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_DEL_CONIMP_08_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_DEL_CONIMP_09_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    public ConceptoImpresionService(ConceptoImpresionRepository conceptoImpresionRepository, SQLDGLRepository SQLDGLRepository,
                                    ConceptoImpresionMapper conceptoImpresionMapper,ConceptoImpresionCifMapper conceptoImpresionCifMapper,ConceptoImpresionCifRepository conceptoImpresionCifRepository) {
        this.conceptoImpresionRepository = conceptoImpresionRepository;
        this.SQLDGLRepository = SQLDGLRepository;
        this.conceptoImpresionMapper = conceptoImpresionMapper;
        this.conceptoImpresionCifRepository = conceptoImpresionCifRepository;
        this.conceptoImpresionCifMapper = conceptoImpresionCifMapper;
    }

    /**
     * Save a conceptoImpresion.
     *
     * @param conceptoImpresionDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public ConceptoImpresionDTO save(ConceptoImpresionDTO conceptoImpresionDTO) {
        log.info("INICIO - Request to save ConceptoImpresion : {}", conceptoImpresionDTO);
        ConceptoImpresion conceptoImpresion = conceptoImpresionMapper.toEntity(conceptoImpresionDTO);
        conceptoImpresion = conceptoImpresionRepository.save(conceptoImpresion);
        log.info("FIN - Request to save ConceptoImpresion");
        return conceptoImpresionMapper.toDto(conceptoImpresion);
    }

    /**
     * Get all the conceptoImpresions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ConceptoImpresionDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all ConceptoImpresions");
        log.info("FIN - Request to get all ConceptoImpresions");
        return conceptoImpresionRepository.findAll(pageable).map(conceptoImpresionMapper::toDto);
    }

    /**
     * Get one conceptoImpresion by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ConceptoImpresionDTO> findOne(ConceptoImpresionPK id) {
        log.info("INICIO - Request to get ConceptoImpresion : {}", id);
        log.info("FIN - Request to get ConceptoImpresion");
        return conceptoImpresionRepository.findById(id).map(conceptoImpresionMapper::toDto);
    }

    /**
     * Delete the conceptoImpresion by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(ConceptoImpresionPK id) {
        log.info("INICIO - Request to delete ConceptoImpresion : {}", id);
        log.info("FIN - Request to delete ConceptoImpresion");
        conceptoImpresionRepository.deleteById(id);
    }

    public List<ConceptoImpresionDTO> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(ConceptoImpresionRequestDTO conceptoImpresionRequestDTO,Integer codPlantilla,String esquema) {
        log.info("INICIO - Request to findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda");
        log.info("FIN - Request to findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda");
        return this.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(conceptoImpresionRequestDTO.codCliente, conceptoImpresionRequestDTO.tipoDesglose,
            conceptoImpresionRequestDTO.subTipoDesglose, conceptoImpresionRequestDTO.moneda,codPlantilla,esquema);
    }

    public List<ConceptoImpresionDTO> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda,Integer codPlantilla,String esquema) {
        log.info("INICIO - Request to findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda");
        log.info("FIN - Request to findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda");
         List<ConceptoImpresion> list = null;
         List<ConceptoImpresionCif> listCif = null;
         List<ConceptoImpresionDTO> conceptoImpresionDTOList = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            list = conceptoImpresionRepository.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaOrderByCodTipoConcepAscCorConceptoAsc(codCliente, codTipoDes, codSubtipoDes, codMoneda,codPlantilla);
            conceptoImpresionDTOList =  conceptoImpresionMapper.toDto(list);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            listCif = conceptoImpresionCifRepository.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaOrderByCodTipoConcepAscCorConceptoAsc(codCliente, codTipoDes, codSubtipoDes, codMoneda,codPlantilla);
            conceptoImpresionDTOList =   conceptoImpresionCifMapper.toDto(listCif);
        }

        return conceptoImpresionDTOList;
    }

    public void executeCifSpInsConImp(final String codCliente, final Integer codPlantilla, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        final String codTipoConcepto, final Integer corConcepto) {
        this.log.info("INICIO - executeCifSpInsConImpwk");

        // Llenar los parámetros
        Param<String> p1 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_03_P_COD_SUBTIPO_DES, Integer.class, codSubtipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<String> p5 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_05_P_COD_TIPO_CONCEP, String.class, codTipoConcepto, ParameterMode.IN);
        Param<Integer> p6 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_06_P_COR_CONCEPTO, Integer.class, corConcepto, ParameterMode.IN);
        Param<Integer> p7 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_07_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p8 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_08_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p9 = new Param<>(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_09_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);

        // Interpretar los resultados
        log.debug("QUERY: " +ConceptoImpresionService.SP_CIF_SP_INS_CONIMP );
        log.debug("PARAMETROS: " + parameters.toString());
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP, parameters);
        Integer returnParam = (Integer) objects.get(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_07_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ConceptoImpresionService.SP_CIF_SP_INS_CONIMP);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(ConceptoImpresionService.SP_CIF_SP_INS_CONIMP_08_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpInsConImpwk");
    }

    public void executeCifSpDelConImp(BreakdownSaveRequestDTO requestDTO) {
        this.log.info("INICIO - executeCifSpDelConImpwk");
        // Llenar los parámetros
        Param<String> p1 = new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_01_P_COD_CLIENTE, String.class, requestDTO.desgloseDTO.getCodCliente(), ParameterMode.IN);
        Param<Integer> p2 =
            new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_02_P_COD_TIPO_DES, Integer.class, Integer.valueOf(requestDTO.desgloseDTO.getCodTipoDes()), ParameterMode.IN);
        Param<Integer> p3 =
            new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_03_P_COD_SUBTIPO_DES, Integer.class, Integer.valueOf(requestDTO.desgloseDTO.getCodSubtipoDes()),
                ParameterMode.IN);
        Param<String> p4 = new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_04_P_COD_MONEDA, String.class, requestDTO.desgloseDTO.getCodMoneda(), ParameterMode.IN);
        Param<String> p5 = new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_05_P_COD_TIPO_CONCEP, String.class, null, ParameterMode.IN);
        Param<String> p6 = new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_06_P_COR_CONCEPTO, String.class, null, ParameterMode.IN);
        Param<Integer> p7 = new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_07_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p8 = new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_08_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p9 = new Param<>(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_09_P_COD_PLANTILLA, Integer.class, Integer.valueOf(requestDTO.desgloseDTO.getCodPlantilla()),
            ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);

        // Interpretar los resultados
        log.debug("QUERY: " + ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP);
        log.debug("PARAMETROS : " + parameters.toString());
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP, parameters);
        log.info(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_07_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(ConceptoImpresionService.SP_CIF_SP_DEL_CONIMP_08_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpDelConImp_wk");
    }
}
