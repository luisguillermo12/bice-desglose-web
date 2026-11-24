package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.repository.SQLCIFRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.DesgloseImpresion;
import cl.tutorial.desglose.domain.pk.DesgloseImpresionPK;
import cl.tutorial.desglose.repository.DesgloseImpresionRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.DesgloseImpresionDTO;
import cl.tutorial.desglose.service.mapper.DesgloseImpresionMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cl.tutorial.desglose.config.Constants.ESQUEMA_CIF;
import static cl.tutorial.desglose.config.Constants.ESQUEMA_DGL;

/**
 * Service Implementation for managing DesgloseImpresion.
 */
@Service
@Transactional
public class DesgloseImpresionService {

    private static final String SP_CIF_SP_INS_DESIMP = "DGL_SP_INS_DESIMP_WK";
    private static final String SP_CIF_SP_INS_DESIMPCIF = "CIF_SP_INS_DESIMP_PLT";
    private static final String SP_CIF_SP_INS_DESIMP_01_PI_COD_CLIENTE = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_DESIMP_02_PI_COD_PLANTILLA_DES = "P_COD_PLANTILLA_DES";
    private static final String SP_CIF_SP_INS_DESIMP_02_PI_COD_PLANTILLA_DES_CIF = "P_COD_PLANTILLA";
    private static final String SP_CIF_SP_INS_DESIMP_03_PI_COD_TIPO_DES = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_DESIMP_04_PI_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_DESIMP_05_PI_COD_MONEDA = "p_COD_MONEDA";
    private static final String SP_CIF_SP_INS_DESIMP_06_PI_COD_MONEDA_IMP = "P_COD_MONEDA_IMP";
    private static final String SP_CIF_SP_INS_DESIMP_07_PI_VAL_DEFLAC_BASE = "P_VAL_DEFLAC_BASE";
    private static final String SP_CIF_SP_INS_DESIMP_08_PI_FEC_DEFLAC_BASE = "P_FEC_DEFLAC_BASE";
    private static final String SP_CIF_SP_INS_DESIMP_09_PO_COR_IMPRESO = "P_COR_IMPRESO";
    private static final String SP_CIF_SP_INS_DESIMP_10_PO_RETORNO = "P_RETORNO";
    private static final String SP_CIF_SP_INS_DESIMP_11_PO_GLOSA = "P_GLOSA";

    private final Logger log = LogManager.getLogger(DesgloseImpresionService.class);

    private final DesgloseImpresionRepository desgloseImpresionRepository;

    private final DesgloseImpresionMapper desgloseImpresionMapper;

    private final SQLDGLRepository SQLDGLRepository;
    private final SQLCIFRepository sqlcifRepository;

    public DesgloseImpresionService(DesgloseImpresionRepository desgloseImpresionRepository, DesgloseImpresionMapper desgloseImpresionMapper,
        SQLDGLRepository SQLDGLRepository,SQLCIFRepository sqlcifRepository) {
        this.desgloseImpresionRepository = desgloseImpresionRepository;
        this.desgloseImpresionMapper = desgloseImpresionMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.sqlcifRepository = sqlcifRepository;
    }

    /**
     * Save a desgloseImpresion.
     *
     * @param desgloseImpresionDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseImpresionDTO save(DesgloseImpresionDTO desgloseImpresionDTO) {
        log.info("INICIO - Request to save DesgloseImpresion : {}", desgloseImpresionDTO);
        DesgloseImpresion desgloseImpresion = desgloseImpresionMapper.toEntity(desgloseImpresionDTO);
        desgloseImpresion = desgloseImpresionRepository.save(desgloseImpresion);
        log.info("FIN - Request to save DesgloseImpresion : {}", desgloseImpresionDTO);
        return desgloseImpresionMapper.toDto(desgloseImpresion);
    }

    /**
     * Get all the desgloseImpresions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseImpresionDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseImpresions");
        log.info("FIN - Request to get all DesgloseImpresions");
        return desgloseImpresionRepository.findAll(pageable).map(desgloseImpresionMapper::toDto);
    }

    /**
     * Get one desgloseImpresion by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseImpresionDTO> findOne(DesgloseImpresionPK id) {
        log.info("INICIO - Request to get DesgloseImpresion : {}", id);
        log.info("FIN - Request to get DesgloseImpresion : {}", id);
        return desgloseImpresionRepository.findById(id).map(desgloseImpresionMapper::toDto);
    }

    /**
     * Delete the desgloseImpresion by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseImpresionPK id) {
        log.info("INICIO - Request to delete DesgloseImpresion : {}", id);
        log.info("FIN - Request to delete DesgloseImpresion : {}", id);
        desgloseImpresionRepository.deleteById(id);
    }

    /**
     * @author carlos eduardo rodriguez
     * @param desgloseImpresionDTO
     * @return el valor de cor_impreso
     */
    @Transactional
    public Integer executeCifSpInsDes(DesgloseImpresionDTO desgloseImpresionDTO,String esquema) {
        this.log.info("INICIO - executeCifSpInsDes");
        // Llenar los parámetros
        if (desgloseImpresionDTO.getCodCliente() == null  && desgloseImpresionDTO.getCodTipoDes() == null && desgloseImpresionDTO.getCodSubtipoDes() == null &&
            desgloseImpresionDTO.getCodMoneda() == null && desgloseImpresionDTO.getCodMonedaImp() == null
            && desgloseImpresionDTO.getCodPlantillaDes() == null && desgloseImpresionDTO.getValDeflacBase() == null && desgloseImpresionDTO.getFecDeflacBase() == null) {
            // Error de parámetros inválidos.
            throw new StorageProcedureBusinessError("Error llamando al DGL_SP_INS_DESIMP_WK. Parámetros inválidos.");
        }
        List<Param<?>> parameters = new ArrayList<>();
        List<Param<?>> parametersCif = new ArrayList<>();
        Param<String> p1 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_01_PI_COD_CLIENTE, String.class, desgloseImpresionDTO.getCodCliente(), ParameterMode.IN);
        Param<Integer> p2 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_02_PI_COD_PLANTILLA_DES, Integer.class, desgloseImpresionDTO.getCodPlantillaDes(), ParameterMode.IN);
        Param<Integer> p2Cif = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_02_PI_COD_PLANTILLA_DES_CIF, Integer.class, desgloseImpresionDTO.getCodPlantillaDes(), ParameterMode.IN);
        Param<Integer> p3 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_03_PI_COD_TIPO_DES, Integer.class, desgloseImpresionDTO.getCodTipoDes(), ParameterMode.IN);
        Param<Integer> p4 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_04_PI_COD_SUBTIPO_DES, Integer.class,desgloseImpresionDTO.getCodSubtipoDes(), ParameterMode.IN);
        Param<String> p5 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_05_PI_COD_MONEDA, String.class,desgloseImpresionDTO.getCodMoneda(), ParameterMode.IN);
        Param<String> p6 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_06_PI_COD_MONEDA_IMP, String.class,desgloseImpresionDTO.getCodMonedaImp(), ParameterMode.IN);
        Param<BigDecimal> p7 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_07_PI_VAL_DEFLAC_BASE, BigDecimal.class,desgloseImpresionDTO.getValDeflacBase(), ParameterMode.IN);
        Param<LocalDate> p8 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_08_PI_FEC_DEFLAC_BASE, LocalDate.class,desgloseImpresionDTO.getFecDeflacBase(), ParameterMode.IN);
        Param<Integer> p9 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_09_PO_COR_IMPRESO, Integer.class, null, ParameterMode.OUT);
        Param<Integer> p10 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_10_PO_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p11 = new Param<>(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_11_PO_GLOSA, String.class, null, ParameterMode.OUT);
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

        Map<String, Object> objects = null;
        // Interpretar los resultados
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            log.debug("QUERY : " + DesgloseImpresionService.SP_CIF_SP_INS_DESIMP);
            log.debug("PARAMETROS : " + parameters.toString());
            objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP, parameters);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            log.debug("QUERY : " + DesgloseImpresionService.SP_CIF_SP_INS_DESIMPCIF);
            log.debug("PARAMETROS : " + parametersCif.toString());
            objects = this.sqlcifRepository.executeNativeStoredProcedureOutputNoSelect(DesgloseImpresionService.SP_CIF_SP_INS_DESIMPCIF, parametersCif);

        }
        Integer returnParam = (Integer) objects.get(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_10_PO_RETORNO);
            if (returnParam == null) {if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + DesgloseImpresionService.SP_CIF_SP_INS_DESIMP);
            }
            if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + DesgloseImpresionService.SP_CIF_SP_INS_DESIMPCIF);
            }
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_11_PO_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }
        log.info("FIN - executeCifSpInsDes");
        return (Integer) objects.get(DesgloseImpresionService.SP_CIF_SP_INS_DESIMP_09_PO_COR_IMPRESO);
    }
}
