package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.Periodo;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import cl.tutorial.desglose.repository.*;
import cl.tutorial.desglose.service.dto.*;
import cl.tutorial.desglose.service.mapper.DesgloseMapper;
import cl.tutorial.desglose.service.mapper.DesgloseMensajeMapper;
import cl.tutorial.desglose.service.mapper.DesgloseRevisionMapper;
import cl.tutorial.desglose.service.mapper.PeriodoMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static cl.tutorial.desglose.config.Constants.ESTADO_DEFINITIVO;
import static cl.tutorial.desglose.config.Constants.ESTADO_EN_EDICION;

/**
 * Service Implementation for managing Proyecciones.
 */
@Service
@Transactional
public class ProjectionService {

    private static final String ENTITY_NAME = "projection";

    private final Logger log = LogManager.getLogger(ProjectionService.class);

    private final DesgloseRepository          desgloseRepository;
    private final DesgloseRevisionRepository  desgloseRevisionRepository;
    private final DesgloseMensajeRepository   desgloseMensajeRepository;
    private final SQLDGLRepository            SQLDGLRepository;
    private final DescripcionCodigoRepository descripcionCodigoRepository;
    private final DesgloseReemplazoRepository desgloseReemplazoRepository;
    private final DesgloseAuditoriaRepository desgloseAuditoriaRepository;
    private final PeriodoRepository           periodoRepository;
    private final DesgloseService             desgloseService;

    private final DesgloseMapper         desgloseMapper;
    private final DesgloseMensajeMapper  desgloseMensajeMapper;
    private final DesgloseRevisionMapper desgloseRevisionMapper;

    private final DescripcionCodigoService  descripcionCodigoService;
    private final TurbineUserService        turbineUserService;
    private final ValPeriodoDesgloseService valPeriodoDesgloseService;
    private final PeriodoService            periodoService;
    private final ConceptoImpresionService  conceptoImpresionService;
    private final EmpresaConsolidadaService empresaConsolidadaService;
    private final PeriodoDesgConsolService  periodoDesgConsolService;
    private final SfStfOperatorService      sfStfOperatorService;

    private final TablaServicioMailService tablaServicioMailService;
    private final PeriodoMapper periodoMapper;

    public ProjectionService(DesgloseRepository desgloseRepository, DesgloseRevisionRepository desgloseRevisionRepository, DesgloseMensajeRepository desgloseMensajeRepository,
        DesgloseReemplazoRepository desgloseReemplazoRepository, PeriodoRepository periodoRepository, DesgloseService desgloseService, DesgloseMapper desgloseMapper,
        SQLDGLRepository SQLDGLRepository, DescripcionCodigoRepository descripcionCodigoRepository, DescripcionCodigoService descripcionCodigoService,
        TurbineUserService turbineUserService, ValPeriodoDesgloseService valPeriodoDesgloseService, PeriodoService periodoService,
        ConceptoImpresionService conceptoImpresionService, EmpresaConsolidadaService empresaConsolidadaService, PeriodoDesgConsolService periodoDesgConsolService,
        DesgloseAuditoriaRepository desgloseAuditoriaRepository, SfStfOperatorService sfStfOperatorService, DesgloseRevisionMapper desgloseRevisionMapper,
        DesgloseMensajeMapper desgloseMensajeMapper, TablaServicioMailService tablaServicioMailService,PeriodoMapper periodoMapper) {
        this.desgloseRepository = desgloseRepository;
        this.desgloseRevisionRepository = desgloseRevisionRepository;
        this.desgloseMensajeRepository = desgloseMensajeRepository;
        this.desgloseReemplazoRepository = desgloseReemplazoRepository;
        this.periodoRepository = periodoRepository;
        this.desgloseService = desgloseService;
        this.desgloseMapper = desgloseMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.descripcionCodigoRepository = descripcionCodigoRepository;
        this.descripcionCodigoService = descripcionCodigoService;
        this.turbineUserService = turbineUserService;
        this.valPeriodoDesgloseService = valPeriodoDesgloseService;
        this.periodoService = periodoService;
        this.conceptoImpresionService = conceptoImpresionService;
        this.empresaConsolidadaService = empresaConsolidadaService;
        this.periodoDesgConsolService = periodoDesgConsolService;
        this.desgloseAuditoriaRepository = desgloseAuditoriaRepository;
        this.sfStfOperatorService = sfStfOperatorService;
        this.desgloseMensajeMapper = desgloseMensajeMapper;
        this.desgloseRevisionMapper = desgloseRevisionMapper;
        this.tablaServicioMailService = tablaServicioMailService;
        this.periodoMapper = periodoMapper;
    }

    public BreakdownRecoverResponseDTO getHistoricData(ProjectionHistoricDataRequestDTO projectionHistoricDataRequestDTO) {
        // Cargar el desglose con los datos.
        BreakdownSearchRequestDTO searchRequestDTO = BreakdownSearchRequestDTO.newInstance(projectionHistoricDataRequestDTO);
        return desgloseService.getBreakdownHistoricData(searchRequestDTO);
    }

    public PeriodoDTO updateEstado(PeriodoPK id,String codEstado) {
        final TurbineUserDTO user = desgloseService.getTurbineUserDTOAndSecurityCheck(log.getName());
        Periodo period =  periodoRepository.findById(id).get();
        period.setCodEstadoPry(codEstado);
        if(codEstado.equals(ESTADO_EN_EDICION)) {
            period.setUserEdit(user.getLoginName());
            period.setFecUserEditPry(LocalDate.now());
            period.setUserClosePry(null);
            period.setFecUserClosePry(null);
        } else if (codEstado.equals(ESTADO_DEFINITIVO)) {
            period.setUserClosePry(user.getLoginName());
            period.setFecUserClosePry(LocalDate.now());
        }
        periodoRepository.save(period);
        return  periodoRepository.findById(id).map(periodoMapper::toDto).get();
    }
}
