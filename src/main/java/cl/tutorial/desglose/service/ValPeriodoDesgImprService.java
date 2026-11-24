package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.repository.SQLCIFRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.ValPeriodoDesgImpr;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesgImprPK;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.repository.ValPeriodoDesgImprRepository;
import cl.tutorial.desglose.service.dto.ValPeriodoDesgImprDTO;
import cl.tutorial.desglose.service.mapper.ValPeriodoDesgImprMapper;
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
 * Service Implementation for managing ValPeriodoDesgImpr.
 */
@Service
@Transactional
public class ValPeriodoDesgImprService {

    private static final String SP_CIF_SP_INS_VALIMP                         = "DGL_SP_INS_VALIMP_WK";
    private static final String SP_CIF_SP_INS_VALIMPCIF                      = "CIF_SP_INS_VALIMP_PLT";
    private static final String SP_CIF_SP_INS_VALIMP_01_PI_COD_CLIENTE       = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_VALIMP_02_PI_COD_PLANTILLA_DES = "P_COD_PLANTILLA_DES";
    private static final String SP_CIF_SP_INS_VALIMP_02_PI_COD_PLANTILLA_DES_CIF = "P_COD_PLANTILLA";
    private static final String SP_CIF_SP_INS_VALIMP_03_PI_COD_TIPO_DES      = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_VALIMP_04_PI_COD_SUBTIPO_DES   = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_VALIMP_05_PI_COD_MONEDA        = "p_COD_MONEDA";
    private static final String SP_CIF_SP_INS_VALIMP_06_PI_COD_MONEDA_IMP    = "P_COD_MONEDA_IMP";
    private static final String SP_CIF_SP_INS_VALIMP_07_PI_FEC_PERIODO       = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_INS_VALIMP_08_PI_COD_TIPO_CONCEP   = "P_COD_TIPO_CONCEP";
    private static final String SP_CIF_SP_INS_VALIMP_09_PI_COR_CONCEPTO      = "P_COR_CONCEPTO";
    private static final String SP_CIF_SP_INS_VALIMP_10_PI_COR_VALOR         = "P_COR_VALOR";
    private static final String SP_CIF_SP_INS_VALIMP_11_PI_COR_IMPRESO       = "P_COR_IMPRESO";
    private static final String SP_CIF_SP_INS_VALIMP_12_PI_VAL_CONCEPTO      = "P_VAL_CONCEPTO";
    private static final String SP_CIF_SP_INS_VALIMP_13_PO_RETORNO           = "P_RETORNO";
    private static final String SP_CIF_SP_INS_VALIMP_14_PO_GLOSA             = "P_GLOSA";

    private final Logger log = LogManager.getLogger(ValPeriodoDesgImprService.class);

    private final ValPeriodoDesgImprRepository valPeriodoDesgImprRepository;

    private final ValPeriodoDesgImprMapper valPeriodoDesgImprMapper;

    private final SQLDGLRepository SQLDGLRepository;
    private final SQLCIFRepository sqlcifRepository;

    public ValPeriodoDesgImprService(ValPeriodoDesgImprRepository valPeriodoDesgImprRepository,
                                     ValPeriodoDesgImprMapper valPeriodoDesgImprMapper,
                                     SQLDGLRepository SQLDGLRepository,SQLCIFRepository sqlcifRepository) {
        this.valPeriodoDesgImprRepository = valPeriodoDesgImprRepository;
        this.valPeriodoDesgImprMapper = valPeriodoDesgImprMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.sqlcifRepository = sqlcifRepository;
    }

    /**
     * Save a valPeriodoDesgImpr.
     *
     * @param valPeriodoDesgImprDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public ValPeriodoDesgImprDTO save(ValPeriodoDesgImprDTO valPeriodoDesgImprDTO) {
        log.info("INICIO - Request to save ValPeriodoDesgImpr");
        log.debug(valPeriodoDesgImprDTO.toString());
        ValPeriodoDesgImpr valPeriodoDesgImpr = valPeriodoDesgImprMapper.toEntity(valPeriodoDesgImprDTO);
        valPeriodoDesgImpr = valPeriodoDesgImprRepository.save(valPeriodoDesgImpr);
        log.info("FIN - Request to save ValPeriodoDesgImpr");
        return valPeriodoDesgImprMapper.toDto(valPeriodoDesgImpr);
    }

    /**
     * Get all the valPeriodoDesgImprs.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ValPeriodoDesgImprDTO> findAll(Pageable pageable) {
        log.info("Request to get all ValPeriodoDesgImprs");
        return valPeriodoDesgImprRepository.findAll(pageable).map(valPeriodoDesgImprMapper::toDto);
    }

    /**
     * Get one valPeriodoDesgImpr by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ValPeriodoDesgImprDTO> findOne(ValPeriodoDesgImprPK id) {
        log.info("Request to get ValPeriodoDesgImpr : {}", id);
        return valPeriodoDesgImprRepository.findById(id).map(valPeriodoDesgImprMapper::toDto);
    }

    /**
     * Delete the valPeriodoDesgImpr by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(ValPeriodoDesgImprPK id) {
        log.info("INICIO - Request to delete ValPeriodoDesgImpr : {} [" + Calendar.getInstance().getTime() + "]", id);
        valPeriodoDesgImprRepository.deleteById(id);
        log.debug("FIN - Request to delete ValPeriodoDesgImpr: [" + Calendar.getInstance().getTime() + "]");
    }

    public List<ValPeriodoDesgImpr> deleteAllBy(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla) {
        log.info(
            "INICIO - Request to deleteBy ValPeriodoDesgImpr with codCliente: {}, codTipoDes: {}, " + "codSubtipoDes: {}, codMoneda: {}, fecPeriodo: {} and codPlantilla: {}- ["
                + Calendar.getInstance().getTime() + "]", codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);
        List<ValPeriodoDesgImpr> deletedItems = this.valPeriodoDesgImprRepository
            .removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);
        log.info("FIN - Request to deleteBy ValPeriodoDesgImpr - [" + Calendar.getInstance().getTime() + "]");
        return deletedItems;
    }

    /**
     * @param valPeriodoDesgImprDTO
     *
     * @author carlos eduardo rodriguez
     */
    @Transactional
    public void executeCifSpInsValImp(ValPeriodoDesgImprDTO valPeriodoDesgImprDTO,String esquema) {
        this.log.info("INICIO - executeCifSpInsValImp");
        // Llenar los parámetros
        if (valPeriodoDesgImprDTO.getCodCliente() == null && valPeriodoDesgImprDTO.getCodTipoDes() == null && valPeriodoDesgImprDTO.getCodSubtipoDes() == null
            && valPeriodoDesgImprDTO.getCodMoneda() == null && valPeriodoDesgImprDTO.getCodMonedaImp() == null && valPeriodoDesgImprDTO.getValConcepto() == null
            && valPeriodoDesgImprDTO.getCod_tipo_plantilla_des() == null && valPeriodoDesgImprDTO.getFecPeriodo() == null) {
            // Error de parámetros inválidos.
            throw new StorageProcedureBusinessError("Error llamando al DGL_SP_INS_VALIMP_WK. Parámetros inválidos.");
        }
        Param<String> p1 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_01_PI_COD_CLIENTE, String.class, valPeriodoDesgImprDTO.getCodCliente(), ParameterMode.IN);
        Param<Integer> p2 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_02_PI_COD_PLANTILLA_DES, Integer.class, valPeriodoDesgImprDTO.getCod_tipo_plantilla_des(), ParameterMode.IN);
        Param<Integer> p2Cif = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_02_PI_COD_PLANTILLA_DES_CIF, Integer.class, valPeriodoDesgImprDTO.getCod_tipo_plantilla_des(), ParameterMode.IN);
        Param<Integer> p3 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_03_PI_COD_TIPO_DES, Integer.class, valPeriodoDesgImprDTO.getCodTipoDes(), ParameterMode.IN);
        Param<Integer> p4 =
            new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_04_PI_COD_SUBTIPO_DES, Integer.class, valPeriodoDesgImprDTO.getCodSubtipoDes(), ParameterMode.IN);
        Param<String> p5 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_05_PI_COD_MONEDA, String.class, valPeriodoDesgImprDTO.getCodMoneda(), ParameterMode.IN);
        Param<String> p6 =
            new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_06_PI_COD_MONEDA_IMP, String.class, valPeriodoDesgImprDTO.getCodMonedaImp(), ParameterMode.IN);
        Param<LocalDate> p7 =
            new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_07_PI_FEC_PERIODO, LocalDate.class, valPeriodoDesgImprDTO.getFecPeriodo(), ParameterMode.IN);
        Param<String> p8 =
            new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_08_PI_COD_TIPO_CONCEP, String.class, valPeriodoDesgImprDTO.getCodTipoConcep(), ParameterMode.IN);
        Param<Integer> p9 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_09_PI_COR_CONCEPTO, Integer.class, valPeriodoDesgImprDTO.getCorConcepto(), ParameterMode.IN);
        Param<Integer> p10 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_10_PI_COR_VALOR, Integer.class, valPeriodoDesgImprDTO.getCorValor(), ParameterMode.IN);
        Param<Integer> p11 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_11_PI_COR_IMPRESO, Integer.class, valPeriodoDesgImprDTO.getCorImpreso(), ParameterMode.IN);
        Param<BigDecimal> p12 =
            new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_12_PI_VAL_CONCEPTO, BigDecimal.class, valPeriodoDesgImprDTO.getValConcepto(), ParameterMode.IN);
        Param<Integer> p13 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_13_PO_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p14 = new Param<>(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_14_PO_GLOSA, String.class, null, ParameterMode.OUT);

        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14);
        List<Param<?>> parametersCif = Arrays.asList(p2Cif,p1, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14);
        // Interpretar los resultados
        Map<String, Object> objects = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            log.debug("QUERY: " + ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP);
            log.debug("PARAMETROS: " + parameters);
            objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP, parameters);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            log.debug("QUERY: " + ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP);
            log.debug("PARAMETROS: " + parametersCif);
            objects = this.sqlcifRepository.executeNativeStoredProcedureOutputNoSelect(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMPCIF, parametersCif);
        }
        Integer returnParam = (Integer) objects.get(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_13_PO_RETORNO);
        if (returnParam == null) {
            if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP);
            }
            if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
                log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMPCIF);
            }
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(ValPeriodoDesgImprService.SP_CIF_SP_INS_VALIMP_14_PO_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }
        log.info("FIN - executeCifSpInsValImp");
    }
}
