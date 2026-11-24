package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.repository.SQLCIFRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.PeriodoDesgImpresion;
import cl.tutorial.desglose.domain.pk.PeriodoDesgImpresionPK;
import cl.tutorial.desglose.repository.PeriodoDesgImpresionRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.PeriodoDesgImpresionDTO;
import cl.tutorial.desglose.service.mapper.PeriodoDesgImpresionMapper;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.ESQUEMA_CIF;
import static cl.tutorial.desglose.config.Constants.ESQUEMA_DGL;

/**
 * Service Implementation for managing PeriodoDesgImpresion.
 */
@Service
@Transactional
public class PeriodoDesgImpresionService {

    private static final String SP_CIF_SP_INS_PRDIMP                         = "DGL_SP_INS_PRDIMP_WK";
    private static final String SP_CIF_SP_INS_PRDIMPCIF                      = "CIF_SP_INS_PRDIMP_PLT";
    private static final String SP_CIF_SP_INS_PRDIMP_01_PI_COD_CLIENTE       = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_PRDIMP_02_PI_COD_PLANTILLA_DES = "P_COD_PLANTILLA_DES";
    private static final String SP_CIF_SP_INS_PRDIMP_02_PI_COD_PLANTILLA_DES_CIF = "P_COD_PLANTILLA";
    private static final String SP_CIF_SP_INS_PRDIMP_03_PI_COD_TIPO_DES      = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_PRDIMP_04_PI_COD_SUBTIPO_DES   = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_PRDIMP_05_PI_COD_MONEDA        = "p_COD_MONEDA";
    private static final String SP_CIF_SP_INS_PRDIMP_06_PI_COD_MONEDA_IMP    = "P_COD_MONEDA_IMP";
    private static final String SP_CIF_SP_INS_PRDIMP_07_PI_FEC_PERIODO       = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_INS_PRDIMP_08_PI_COR_IMPRESO       = "P_COR_IMPRESO";
    private static final String SP_CIF_SP_INS_PRDIMP_09_PI_VAL_DEFLAC_PRD    = "P_VAL_DEFLAC_PRD";
    private static final String SP_CIF_SP_INS_PRDIMP_10_PI_VAL_TIPO_CAMBIO   = "P_VAL_TIPO_CAMBIO";
    private static final String SP_CIF_SP_INS_PRDIMP_11_PO_RETORNO           = "P_RETORNO";
    private static final String SP_CIF_SP_INS_PRDIMP_12_PO_GLOSA             = "P_GLOSA";

    private final Logger log = LogManager.getLogger(PeriodoDesgImpresionService.class);

    private final PeriodoDesgImpresionRepository periodoDesgImpresionRepository;

    private final PeriodoDesgImpresionMapper periodoDesgImpresionMapper;

    private final SQLDGLRepository SQLDGLRepository;
    private final SQLCIFRepository sqlcifRepository;

    public PeriodoDesgImpresionService(PeriodoDesgImpresionRepository periodoDesgImpresionRepository, PeriodoDesgImpresionMapper periodoDesgImpresionMapper,
        SQLDGLRepository SQLDGLRepository,SQLCIFRepository sqlcifRepository) {
        this.periodoDesgImpresionRepository = periodoDesgImpresionRepository;
        this.periodoDesgImpresionMapper = periodoDesgImpresionMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.sqlcifRepository = sqlcifRepository;
    }

    /**
     * Save a periodoDesgImpresion.
     *
     * @param periodoDesgImpresionDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public PeriodoDesgImpresionDTO save(PeriodoDesgImpresionDTO periodoDesgImpresionDTO) {
        log.info("Request to save PeriodoDesgImpresion : {}", periodoDesgImpresionDTO);

        PeriodoDesgImpresion periodoDesgImpresion = periodoDesgImpresionMapper.toEntity(periodoDesgImpresionDTO);
        periodoDesgImpresion = periodoDesgImpresionRepository.save(periodoDesgImpresion);
        return periodoDesgImpresionMapper.toDto(periodoDesgImpresion);
    }

    /**
     * Get all the periodoDesgImpresions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PeriodoDesgImpresionDTO> findAll(Pageable pageable) {
        log.info("Request to get all PeriodoDesgImpresions");
        return periodoDesgImpresionRepository.findAll(pageable).map(periodoDesgImpresionMapper::toDto);
    }

    /**
     * Get one periodoDesgImpresion by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PeriodoDesgImpresionDTO> findOne(PeriodoDesgImpresionPK id) {
        log.info("Request to get PeriodoDesgImpresion : {}", id);
        return periodoDesgImpresionRepository.findById(id).map(periodoDesgImpresionMapper::toDto);
    }

    /**
     * Delete the periodoDesgImpresion by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(PeriodoDesgImpresionPK id) {
        log.info("INICIO - Request to delete PeriodoDesgImpresion : {} [" + Calendar.getInstance().getTime() + "]", id);
        periodoDesgImpresionRepository.deleteById(id);
        log.info("FIN - Request to delete PeriodoDesgImpresion: [" + Calendar.getInstance().getTime() + "]");
    }

    public List<PeriodoDesgImpresion> deleteAllBy(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla) {
        log.info(
            "INICIO - Request to deleteBy PeriodoDesgImpresion with codCliente: {}, codTipoDes: {}, " + "codSubtipoDes: {}, codMoneda: {}, fecPeriodo: {} and codPlantilla: {}- [" + Calendar
                .getInstance().getTime() + "]", codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);
        List<PeriodoDesgImpresion> deletedItems = this.periodoDesgImpresionRepository
            .removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);
        log.info("FIN - Request to deleteBy PeriodoDesgImpresion - [" + Calendar.getInstance().getTime() + "]");
        return deletedItems;
    }

    /**
     * @param periodoDesgImpresionDTO
     *
     * @return
     *
     * @author carlos eduardo rodriguez
     */
    @Transactional
    public Integer executeCifSpInsPrdImp(PeriodoDesgImpresionDTO periodoDesgImpresionDTO,String esquema) {
        Integer retorno = 0;
        this.log.info("INICIO - executeCifSpInsPrdImp");
        // Llenar los parámetros
        if (periodoDesgImpresionDTO.getCodCliente() == null && periodoDesgImpresionDTO.getCodTipoDes() == null && periodoDesgImpresionDTO.getCodSubtipoDes() == null
            && periodoDesgImpresionDTO.getCodMoneda() == null && periodoDesgImpresionDTO.getCodMonedaImp() == null && periodoDesgImpresionDTO.getValTipoCambio() == null
            && periodoDesgImpresionDTO.getCodPlantilla() == null && periodoDesgImpresionDTO.getValDeflacPrd() == null && periodoDesgImpresionDTO.getFecPeriodo() == null) {
            // Error de parámetros inválidos.
            throw new StorageProcedureBusinessError("Error llamando al DGL_SP_INS_PRDIMP_WK. Parámetros inválidos.");
        }
        List<Param<?>> parameters = new ArrayList<>();
        List<Param<?>> parametersCif = new ArrayList<>();
        Param<String> p1 = new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_01_PI_COD_CLIENTE, String.class, periodoDesgImpresionDTO.getCodCliente(), ParameterMode.IN);
        Param<Integer> p2 = new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_02_PI_COD_PLANTILLA_DES, Integer.class, periodoDesgImpresionDTO.getCodPlantilla(), ParameterMode.IN);
        Param<Integer> p2Cif = new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_02_PI_COD_PLANTILLA_DES_CIF, Integer.class, periodoDesgImpresionDTO.getCodPlantilla(), ParameterMode.IN);
        Param<Integer> p3 =
            new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_03_PI_COD_TIPO_DES, Integer.class, periodoDesgImpresionDTO.getCodTipoDes(), ParameterMode.IN);
        Param<Integer> p4 =
            new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_04_PI_COD_SUBTIPO_DES, Integer.class, periodoDesgImpresionDTO.getCodSubtipoDes(), ParameterMode.IN);
        Param<String> p5 = new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_05_PI_COD_MONEDA, String.class, periodoDesgImpresionDTO.getCodMoneda(), ParameterMode.IN);
        Param<String> p6 =
            new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_06_PI_COD_MONEDA_IMP, String.class, periodoDesgImpresionDTO.getCodMonedaImp(), ParameterMode.IN);
        Param<LocalDate> p7 =
            new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_07_PI_FEC_PERIODO, LocalDate.class, periodoDesgImpresionDTO.getFecPeriodo(), ParameterMode.IN);
        Param<Integer> p8 =
            new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_08_PI_COR_IMPRESO, Integer.class, periodoDesgImpresionDTO.getCorImpreso(), ParameterMode.IN);
        Param<BigDecimal> p9 =
            new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_09_PI_VAL_DEFLAC_PRD, BigDecimal.class, periodoDesgImpresionDTO.getValDeflacPrd(), ParameterMode.IN);
        Param<BigDecimal> p10 =
            new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_10_PI_VAL_TIPO_CAMBIO, BigDecimal.class, periodoDesgImpresionDTO.getValTipoCambio(), ParameterMode.IN);
        Param<Integer> p11 = new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_11_PO_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p12 = new Param<>(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_12_PO_GLOSA, String.class, null, ParameterMode.OUT);
        parameters.add(p1);
        parameters.add(p2);
        parameters.add(p3);
        parameters.add(p4);
        parameters.add(p5);
        parameters.add(p6);
        parameters.add(p7);
        parameters.add(p8);
        parameters.add(p9);
        parameters.add(p10);
        parameters.add(p11);
        parameters.add(p12);


        parametersCif.add(p2Cif);
        parametersCif.add(p1);
        parametersCif.add(p3);
        parametersCif.add(p4);
        parametersCif.add(p5);
        parametersCif.add(p6);
        parametersCif.add(p7);
        parametersCif.add(p8);
        parametersCif.add(p9);
        parametersCif.add(p10);
        parametersCif.add(p11);
        parametersCif.add(p12);
        // Interpretar los resultados
        Map<String, Object> objects = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            log.debug("QUERY  : "+PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP );
            log.debug("PARAMETROS : "+ parameters.toString() );
            objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP, parameters);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            log.debug("QUERY  : "+PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP );
            log.debug("PARAMETROS : "+ parametersCif.toString() );
            objects = this.sqlcifRepository.executeNativeStoredProcedureOutputNoSelect(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMPCIF, parametersCif);
        }
        Integer returnParam = (Integer) objects.get(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_11_PO_RETORNO);
        if (returnParam == null) {
            retorno = returnParam;
            if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP);
            }
            if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMPCIF);
            }
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            retorno = returnParam;
            String returnMessage = (String) objects.get(PeriodoDesgImpresionService.SP_CIF_SP_INS_PRDIMP_12_PO_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }
        log.info("FIN - executeCifSpInsPrdImp");
        return retorno;
    }
}
