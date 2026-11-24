package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.PeriodoCif;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.PeriodoCifRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.Periodo;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import cl.tutorial.desglose.repository.PeriodoRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.dto.PeriodDeleteRequestDTO;
import cl.tutorial.desglose.service.dto.PeriodoDTO;
import cl.tutorial.desglose.service.dto.PeriodoRequestDTO;
import cl.tutorial.desglose.service.mapper.PeriodoCifMapper;
import cl.tutorial.desglose.service.mapper.PeriodoMapper;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.InternalServerErrorException;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureBusinessError;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.ESQUEMA_CIF;
import static cl.tutorial.desglose.config.Constants.ESQUEMA_DGL;

/**
 * Service Implementation for managing Periodo.
 */
@Service
@Transactional
public class PeriodoService {

    private final Logger log = LogManager.getLogger(PeriodoService.class);

    private final PeriodoRepository           periodoRepository;
    private final PeriodoCifRepository        periodoCifRepository;
    private final ValPeriodoDesgImprService   valPeriodoDesgImprService;
    private final ValPeriodoDesgloseService   valPeriodoDesgloseService;
    private final PeriodoDesgConsolService    periodoDesgConsolService;
    private final PeriodoDesgImpresionService periodoDesgImpresionService;
    private final SQLDGLRepository            SQLDGLRepository;

    private final PeriodoMapper periodoMapper;
    private final PeriodoCifMapper periodoCifMapper;

    private static final String SP_CIF_SP_INS_PERIOD                          = "DGL_SP_INS_PERIOD_WK";
    private static final String SP_CIF_SP_INS_PERIOD_01_P_COD_CLIENTE         = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_PERIOD_02_P_COD_TIPO_DES        = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_PERIOD_03_P_COD_SUBTIPO_DES     = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_PERIOD_04_P_COD_MONEDA          = "P_COD_MONEDA";
    private static final String SP_CIF_SP_INS_PERIOD_05_P_FEC_PERIODO         = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_INS_PERIOD_06_P_NUM_MESES           = "P_NUM_MESES";
    private static final String SP_CIF_SP_INS_PERIOD_07_P_VAL_DEFLAC_PRD      = "P_VAL_DEFLAC_PRD";
    private static final String SP_CIF_SP_INS_PERIOD_08_P_COD_AUDITOR         = "P_COD_AUDITOR";
    private static final String SP_CIF_SP_INS_PERIOD_09_P_GLS_OTROS_AUDIT     = "P_GLS_OTROS_AUDIT";
    private static final String SP_CIF_SP_INS_PERIOD_10_P_COD_ESTADO_PRD      = "P_COD_ESTADO_PRD";
    private static final String SP_CIF_SP_INS_PERIOD_11_P_COD_ESTADO_AUDI     = "P_COD_ESTADO_AUDI";
    private static final String SP_CIF_SP_INS_PERIOD_12_P_FEC_INFORM_SBIF     = "P_FEC_INFORM_SBIF";
    private static final String SP_CIF_SP_INS_PERIOD_13_P_MTO_SBIF_DD_VIG     = "P_MTO_SBIF_DD_VIG";
    private static final String SP_CIF_SP_INS_PERIOD_14_P_MTO_SBIF_DD_MOR     = "P_MTO_SBIF_DD_MOR";
    private static final String SP_CIF_SP_INS_PERIOD_15_P_MTO_SBIF_DD_VEN     = "P_MTO_SBIF_DD_VEN";
    private static final String SP_CIF_SP_INS_PERIOD_16_P_MTO_SBIF_DD_CAS     = "P_MTO_SBIF_DD_CAS";
    private static final String SP_CIF_SP_INS_PERIOD_17_P_MTO_SBIF_DI_VIG     = "P_MTO_SBIF_DI_VIG";
    private static final String SP_CIF_SP_INS_PERIOD_18_P_MTO_SBIF_DI_VEN     = "P_MTO_SBIF_DI_VEN";
    private static final String SP_CIF_SP_INS_PERIOD_19_P_MTO_SBIF_DI_CAS     = "P_MTO_SBIF_DI_CAS";
    private static final String SP_CIF_SP_INS_PERIOD_20_P_MTO_SBIF_IMP_90_179 = "P_MTO_SBIF_IMP_90_179";
    private static final String SP_CIF_SP_INS_PERIOD_21_P_NOM_USER            = "P_NOM_USER";
    private static final String SP_CIF_SP_INS_PERIOD_22_P_VAL_TIPO_CAMBIO     = "P_VAL_TIPO_CAMBIO";
    private static final String SP_CIF_SP_INS_PERIOD_23_P_RETORNO             = "P_RETORNO";
    private static final String SP_CIF_SP_INS_PERIOD_24_P_GLOSA               = "P_GLOSA";
    private static final String SP_CIF_SP_INS_PERIOD_25_P_COD_PLANTILLA       = "P_COD_PLANTILLA";

    private static final String SP_CIF_SP_DEL_PERIOD                      = "DGL_SP_DEL_PERIOD_WK";
    private static final String SP_CIF_SP_DEL_PERIOD_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_DEL_PERIOD_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_DEL_PERIOD_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_DEL_PERIOD_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_DEL_PERIOD_05_P_FEC_PERIODO     = "P_FEC_PERIODO";
    private static final String SP_CIF_SP_DEL_PERIOD_06_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_DEL_PERIOD_07_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_DEL_PERIOD_08_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    public PeriodoService(PeriodoRepository periodoRepository,
                          ValPeriodoDesgImprService valPeriodoDesgImprService,
                          ValPeriodoDesgloseService valPeriodoDesgloseService,
                          PeriodoDesgConsolService periodoDesgConsolService,
                          PeriodoDesgImpresionService periodoDesgImpresionService,
                          SQLDGLRepository SQLDGLRepository,
                          PeriodoMapper periodoMapper,
                          PeriodoCifMapper periodoCifMapper,PeriodoCifRepository periodoCifRepository) {
        this.periodoRepository = periodoRepository;
        this.valPeriodoDesgImprService = valPeriodoDesgImprService;
        this.valPeriodoDesgloseService = valPeriodoDesgloseService;
        this.periodoDesgConsolService = periodoDesgConsolService;
        this.periodoDesgImpresionService = periodoDesgImpresionService;
        this.SQLDGLRepository = SQLDGLRepository;
        this.periodoMapper = periodoMapper;
        this.periodoCifMapper = periodoCifMapper;
        this.periodoCifRepository = periodoCifRepository;
    }

    /**
     * Save a periodo.
     *
     * @param periodoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public PeriodoDTO save(PeriodoDTO periodoDTO) {
        log.info("Request to save Periodo : {}", periodoDTO);

        Periodo periodo = periodoMapper.toEntity(periodoDTO);
        periodo = periodoRepository.save(periodo);
        return periodoMapper.toDto(periodo);
    }

    /**
     * Get all the periodos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PeriodoDTO> findAll(Pageable pageable) {
        log.info("Request to get all Periodos");
        return periodoRepository.findAll(pageable).map(periodoMapper::toDto);
    }

    /**
     * Get one periodo by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PeriodoDTO> findOne(PeriodoPK id) {
        log.info("Request to get Periodo : {}", id);
        return periodoRepository.findById(id).map(periodoMapper::toDto);
    }

    /**
     * Delete the periodo by id. Also delets all data related with this period.
     *
     * @param requestDTO
     * 		the id of the entity
     */
    public void delete(PeriodDeleteRequestDTO requestDTO) {
        this.log.info("delete(): Inicio eliminación del período {} deuda SBIF [" + Calendar.getInstance().getTime() + "]", requestDTO.toString());

        // Se cambia la eliminación por JPA por la llamada al SP.
        this.executeCifSpDelPeriod(requestDTO.codCliente, requestDTO.codPlantilla, requestDTO.codTipoDes, requestDTO.codSubtipoDes, requestDTO.codMoneda, requestDTO.fecPeriodo);
        /*
        // Elimina VALIMP_WK
        this.valPeriodoDesgImprService
            .deleteAllBy(requestDTO.codCliente, requestDTO.codTipoDes, requestDTO.codSubtipoDes, requestDTO.codMoneda, requestDTO.fecPeriodo, requestDTO.codPlantilla);

        // Elimina VALPRD_WK
        this.valPeriodoDesgloseService
            .deleteAllBy(requestDTO.codCliente, requestDTO.codTipoDes, requestDTO.codSubtipoDes, requestDTO.codMoneda, requestDTO.fecPeriodo, requestDTO.codPlantilla);

        // Elimina PRCCNS
        this.periodoDesgConsolService
            .deleteAllBy(requestDTO.codCliente, requestDTO.codTipoDes, requestDTO.codSubtipoDes, requestDTO.codMoneda, requestDTO.fecPeriodo, requestDTO.codPlantilla);

        // Elimina PRDIMP_WK
        this.periodoDesgImpresionService
            .deleteAllBy(requestDTO.codCliente, requestDTO.codTipoDes, requestDTO.codSubtipoDes, requestDTO.codMoneda, requestDTO.fecPeriodo, requestDTO.codPlantilla);

        // Elimina PERIOD
        log.debug("INICIO - Request to deleteById Periodo : {} [" + Calendar.getInstance().getTime() + "]", requestDTO);
        PeriodoPK id = PeriodoPK.newInstance(requestDTO);
        this.periodoRepository.deleteById(id);
        log.debug("INICIO - Request to deleteById Periodo : {} [" + Calendar.getInstance().getTime() + "]", id);
        */
        this.log.info("delete(): Fin eliminación del período deuda SBIF [" + Calendar.getInstance().getTime() + "]");
        // throw new TestCaseOnPurposeException();
    }

    /**
     * Busca los períodos por los filtros dados.
     *
     * @param periodoRequestDTO
     * @param orderBy
     *
     * @return
     */
    public List<PeriodoDTO> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(PeriodoRequestDTO periodoRequestDTO, Sort orderBy,String esquema) {
        return this
            .findAllBy(periodoRequestDTO.codCliente, periodoRequestDTO.tipoDesglose, periodoRequestDTO.subTipoDesglose, periodoRequestDTO.moneda, periodoRequestDTO.codPlantilla,
                orderBy,esquema);
    }

    /**
     * Busca los períodos por los filtros dados.
     *
     * @param periodoRequestDTO
     * @param orderBy
     *
     * @return
     */
    public List<PeriodoDTO> findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodTipoPlantilla(PeriodoRequestDTO periodoRequestDTO, Sort orderBy) {
        return this
            .findAllBy(periodoRequestDTO.codCliente, periodoRequestDTO.tipoDesglose, periodoRequestDTO.subTipoDesglose, periodoRequestDTO.moneda, periodoRequestDTO.fecPeriodo,
                periodoRequestDTO.codPlantilla, orderBy);
    }

    /**
     * Busca los períodos por los filtros dados.
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param orderBy
     *
     * @return
     */
    public List<PeriodoDTO> findAllBy(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda, Integer codPlantilla, Sort orderBy,String esquema) {
        List<PeriodoDTO> lista  = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            final List<Periodo> list = periodoRepository
                .findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, orderBy);
            lista = periodoMapper.toDto(list);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            final List<PeriodoCif> list = periodoCifRepository
                .findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, orderBy);
            lista = periodoCifMapper.toDto(list);

        }

        return lista;
    }

    /**
     * Busca los períodos por los filtros dados.
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param orderBy
     *
     * @return
     */
    public List<PeriodoDTO> findAllBy(String codCliente, Integer codTipoDes, Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla, Sort orderBy) {
        final List<Periodo> list = periodoRepository
            .findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(codCliente, codTipoDes, codSubtipoDes, codMoneda, fecPeriodo,
                codPlantilla, orderBy);
        return periodoMapper.toDto(list);
    }

    /**
     * Ejecuta el procedimiento almacenado CIF_SP_INS_PERIOD.
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param periodoDTO
     * @param canLoadSBIF
     */
    public void executeCifSpInsPeriod(final String codCliente, final Integer codPlantilla, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        final PeriodoDTO periodoDTO, final Boolean canLoadSBIF) {
        this.log.info("INICIO - executeCifSpInsPeriod");

        // Llenar los parámetros
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        LocalDate fecInformSbif;
        BigDecimal mtoSbifDdVig = null;
        BigDecimal mtoSbifDdMor = null;
        BigDecimal mtoSbifDdVen = null;
        BigDecimal mtoSbifDdCas = null;
        BigDecimal mtoSbifDiVig = null;
        BigDecimal mtoSbifDiVen = null;
        BigDecimal mtoSbifDiCas = null;
        BigDecimal mtoSbifImp90179 = null;
        log.info("canLoadSBIF:" +canLoadSBIF);
        if (canLoadSBIF != null && canLoadSBIF) {
            fecInformSbif = periodoDTO.getFecInformSbif();
            log.info("fecInformSbif: "+fecInformSbif);
            if(periodoDTO.getMtoSbifDdVig()!=null) {
                mtoSbifDdVig = periodoDTO.getMtoSbifDdVig().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifDdVig: "+mtoSbifDdVig);
            }
            if(periodoDTO.getMtoSbifDdMor()!=null) {
                mtoSbifDdMor = periodoDTO.getMtoSbifDdMor().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifDdMor:" + mtoSbifDdMor);
            }
            if(periodoDTO.getMtoSbifDdVen()!=null) {
                mtoSbifDdVen = periodoDTO.getMtoSbifDdVen().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifDdVen: " + mtoSbifDdVen);
            }
            if(periodoDTO.getMtoSbifDdCas()!=null) {
                mtoSbifDdCas = periodoDTO.getMtoSbifDdCas().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifDdCas: " + mtoSbifDdCas);
            }
            if(periodoDTO.getMtoSbifDiVig()!=null) {
                mtoSbifDiVig = periodoDTO.getMtoSbifDiVig().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifDiVen: " + mtoSbifDiVig);
            }
            if(periodoDTO.getMtoSbifDiVen()!=null) {
                mtoSbifDiVen = periodoDTO.getMtoSbifDiVen().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifDiVen: " + mtoSbifDiVen);
            }
            if(periodoDTO.getMtoSbifDiCas()!=null) {
                mtoSbifDiCas = periodoDTO.getMtoSbifDiCas().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifImp90179: " + mtoSbifDiCas);
            }
            if(periodoDTO.getMtoSbifImp90179()!=null) {
                mtoSbifImp90179 = periodoDTO.getMtoSbifImp90179().setScale(2, RoundingMode.HALF_UP);
                log.info("mtoSbifImp90179: " + mtoSbifImp90179);
            }
        } else {
            fecInformSbif = periodoDTO.getFecPeriodo();
        }

        Param<String> p1 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_03_P_COD_SUBTIPO_DES, Integer.class, codSubtipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<LocalDate> p5 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_05_P_FEC_PERIODO, LocalDate.class, periodoDTO.getFecPeriodo(), ParameterMode.IN);
        Param<Integer> p6 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_06_P_NUM_MESES, Integer.class, periodoDTO.getNumMeses(), ParameterMode.IN);
        Param<BigDecimal> p7 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_07_P_VAL_DEFLAC_PRD, BigDecimal.class, periodoDTO.getValDeflacPrd(), ParameterMode.IN);
        Param<String> p8 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_08_P_COD_AUDITOR, String.class, periodoDTO.getCodAuditor(), ParameterMode.IN);
        Param<String> p9 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_09_P_GLS_OTROS_AUDIT, String.class, periodoDTO.getGlsOtrosAudit(), ParameterMode.IN);
        Param<Integer> p10 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_10_P_COD_ESTADO_PRD, Integer.class, periodoDTO.getCodEstadoPrd(), ParameterMode.IN);
        Param<Integer> p11 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_11_P_COD_ESTADO_AUDI, Integer.class, periodoDTO.getCodEstadoAudi(), ParameterMode.IN);
        Param<LocalDate> p12 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_12_P_FEC_INFORM_SBIF, LocalDate.class, fecInformSbif, ParameterMode.IN);
        Param<BigDecimal> p13 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_13_P_MTO_SBIF_DD_VIG, BigDecimal.class, mtoSbifDdVig, ParameterMode.IN);
        Param<BigDecimal> p14 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_14_P_MTO_SBIF_DD_MOR, BigDecimal.class, mtoSbifDdMor, ParameterMode.IN);
        Param<BigDecimal> p15 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_15_P_MTO_SBIF_DD_VEN, BigDecimal.class, mtoSbifDdVen, ParameterMode.IN);
        Param<BigDecimal> p16 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_16_P_MTO_SBIF_DD_CAS, BigDecimal.class, mtoSbifDdCas, ParameterMode.IN);
        Param<BigDecimal> p17 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_17_P_MTO_SBIF_DI_VIG, BigDecimal.class, mtoSbifDiVig, ParameterMode.IN);
        Param<BigDecimal> p18 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_18_P_MTO_SBIF_DI_VEN, BigDecimal.class, mtoSbifDiVen, ParameterMode.IN);
        Param<BigDecimal> p19 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_19_P_MTO_SBIF_DI_CAS, BigDecimal.class, mtoSbifDiCas, ParameterMode.IN);
        Param<BigDecimal> p20 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_20_P_MTO_SBIF_IMP_90_179, BigDecimal.class, mtoSbifImp90179, ParameterMode.IN);
        Param<String> p21 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_21_P_NOM_USER, String.class, userLogin, ParameterMode.IN);
        Param<BigDecimal> p22 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_22_P_VAL_TIPO_CAMBIO, BigDecimal.class, periodoDTO.getValTipoCambio(), ParameterMode.IN);
        Param<Integer> p23 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_23_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p24 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_24_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p25 = new Param<>(PeriodoService.SP_CIF_SP_INS_PERIOD_25_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23, p24, p25);
        // Interpretar los resultados

        log.debug("QUUERY : " +PeriodoService.SP_CIF_SP_INS_PERIOD );
        log.debug("PARAMETROS : "  + parameters.toString() );
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(PeriodoService.SP_CIF_SP_INS_PERIOD, parameters);
        Integer returnParam = (Integer) objects.get(PeriodoService.SP_CIF_SP_INS_PERIOD_23_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + PeriodoService.SP_CIF_SP_INS_PERIOD);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(PeriodoService.SP_CIF_SP_INS_PERIOD_24_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpInsPeriod");
    }

    public void executeCifSpDelPeriod(final String codCliente, final Integer codPlantilla, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        final LocalDate fecPeriod) {

        this.log.info("INICIO - executeCifSpDelPeriod");

        // Llenar los parámetros
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));

        Param<String> p1 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_03_P_COD_SUBTIPO_DES, Integer.class, codSubtipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<LocalDate> p5 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_05_P_FEC_PERIODO, LocalDate.class, fecPeriod, ParameterMode.IN);
        Param<Integer> p6 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_06_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p7 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_07_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p8 = new Param<>(PeriodoService.SP_CIF_SP_DEL_PERIOD_08_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8);
        log.debug("QUERY : " + PeriodoService.SP_CIF_SP_DEL_PERIOD);
        log.debug("PARAMETROS : " + parameters.toString());
        // Interpretar los resultados
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(PeriodoService.SP_CIF_SP_DEL_PERIOD, parameters);
        Integer returnParam = (Integer) objects.get(PeriodoService.SP_CIF_SP_DEL_PERIOD_06_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + PeriodoService.SP_CIF_SP_DEL_PERIOD);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(PeriodoService.SP_CIF_SP_DEL_PERIOD_07_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpDelPeriod");
    }
}
