package cl.tutorial.desglose.service;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.PeriodoDesgConsol;
import cl.tutorial.desglose.domain.pk.PeriodoDesgConsolPK;
import cl.tutorial.desglose.repository.PeriodoDesgConsolRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.PeriodoDesgConsolDTO;
import cl.tutorial.desglose.service.mapper.PeriodoDesgConsolMapper;
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
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Service Implementation for managing PeriodoDesgConsol.
 */
@Service
@Transactional
public class PeriodoDesgConsolService {

    private final Logger log = LogManager.getLogger(PeriodoDesgConsolService.class);

    private final PeriodoDesgConsolRepository periodoDesgConsolRepository;

    private final SQLDGLRepository SQLDGLRepository;

    private final PeriodoDesgConsolMapper periodoDesgConsolMapper;

    private static final String SP_CIF_SP_INS_PRCCNS                      = "DGL_SP_INS_PRCCNS_WK";
    private static final String SP_CIF_SP_INS_PRCCNS_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_PRCCNS_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_PRCCNS_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_PRCCNS_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_INS_PRCCNS_05_P_COR_EMPRESA     = "P_COR_EMPRESA";
    private static final String SP_CIF_SP_INS_PRCCNS_06_P_FEC_PERIODO     = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_INS_PRCCNS_07_P_VAL_PRC_CONSOL  = "P_VAL_PRC_CONSOL";
    private static final String SP_CIF_SP_INS_PRCCNS_08_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_INS_PRCCNS_09_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_INS_PRCCNS_10_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    public PeriodoDesgConsolService(PeriodoDesgConsolRepository periodoDesgConsolRepository, SQLDGLRepository SQLDGLRepository, PeriodoDesgConsolMapper periodoDesgConsolMapper) {
        this.periodoDesgConsolRepository = periodoDesgConsolRepository;
        this.SQLDGLRepository = SQLDGLRepository;
        this.periodoDesgConsolMapper = periodoDesgConsolMapper;
    }

    /**
     * Save a periodoDesgConsol.
     *
     * @param periodoDesgConsolDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public PeriodoDesgConsolDTO save(PeriodoDesgConsolDTO periodoDesgConsolDTO) {
        log.info("Request to save PeriodoDesgConsol : {}", periodoDesgConsolDTO);

        PeriodoDesgConsol periodoDesgConsol = periodoDesgConsolMapper.toEntity(periodoDesgConsolDTO);
        periodoDesgConsol = periodoDesgConsolRepository.save(periodoDesgConsol);
        return periodoDesgConsolMapper.toDto(periodoDesgConsol);
    }

    /**
     * Get all the periodoDesgConsols.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PeriodoDesgConsolDTO> findAll(Pageable pageable) {
        log.info("Request to get all PeriodoDesgConsols");
        return periodoDesgConsolRepository.findAll(pageable).map(periodoDesgConsolMapper::toDto);
    }

    /**
     * Get one periodoDesgConsol by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PeriodoDesgConsolDTO> findOne(PeriodoDesgConsolPK id) {
        log.info("Request to get PeriodoDesgConsol : {}", id);
        return periodoDesgConsolRepository.findById(id).map(periodoDesgConsolMapper::toDto);
    }

    /**
     * Delete the periodoDesgConsol by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(PeriodoDesgConsolPK id) {
        log.info("INICIO - Request to delete PeriodoDesgConsol : {} [" + Calendar.getInstance().getTime() + "]", id);
        periodoDesgConsolRepository.deleteById(id);
        log.debug("FIN - Request to delete PeriodoDesgConsol: [" + Calendar.getInstance().getTime() + "]");
    }

    public List<PeriodoDesgConsol> deleteAllBy(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla) {
        log.debug(
            "INICIO - Request to deleteBy v with codCliente: {}, codTipoDes: {}, " + "codSubtipoDes: {}, codMoneda: {}, fecPeriodo: {} and codPlantilla:{} - ["
                + Calendar.getInstance().getTime() + "]", codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);
        List<PeriodoDesgConsol> deletedItems = this.periodoDesgConsolRepository
            .removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo, codPlantilla);
        log.info("FIN - Request to deleteBy PeriodoDesgConsol - [" + Calendar.getInstance().getTime() + "]");
        return deletedItems;
    }

    public void executeCifSpInsPrcCns(@NotNull final String codCliente, @NotNull final Integer cod_plantilla, @NotNull final Integer codTipoDes,
        @NotNull final Integer codSubtipoDes, @NotNull final String codMoneda, @NotNull final LocalDate fecPeriodo, @NotNull Integer corEmpresa, @NotNull BigDecimal valPrcConsol) {

        this.log.info("INICIO - executeCifSpInsValPrd");
        valPrcConsol = valPrcConsol.divide(new BigDecimal("100"));
        if(valPrcConsol != null && valPrcConsol.floatValue() >= 10) {
            valPrcConsol = new BigDecimal("9.999");
        }
        if(valPrcConsol!= null && valPrcConsol.floatValue() <= -10) {
            valPrcConsol = new BigDecimal("-9.999");
        }
        // Llenar los parámetros
        Param<String> p1 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_03_P_COD_SUBTIPO_DES, Integer.class, codSubtipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<Integer> p5 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_05_P_COR_EMPRESA, Integer.class, corEmpresa, ParameterMode.IN);
        Param<LocalDate> p6 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_06_P_FEC_PERIODO, LocalDate.class, fecPeriodo, ParameterMode.IN);
        Param<BigDecimal> p7 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_07_P_VAL_PRC_CONSOL, BigDecimal.class, valPrcConsol, ParameterMode.IN);
        Param<Integer> p8 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_08_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p9 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_09_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p10 = new Param<>(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_10_P_COD_PLANTILLA, Integer.class, cod_plantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);

        // Interpretar los resultados

        log.debug("QUERY : " + PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS );
        log.debug("PARAMETROS : " + parameters.toString() );
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS, parameters);
        log.info(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_08_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(PeriodoDesgConsolService.SP_CIF_SP_INS_PRCCNS_09_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage + ' ' + fecPeriodo);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpInsValPrd");
    }
}
