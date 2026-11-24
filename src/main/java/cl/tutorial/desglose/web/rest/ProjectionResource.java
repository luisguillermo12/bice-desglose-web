package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.Periodo;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import cl.tutorial.desglose.service.DesgloseService;
import cl.tutorial.desglose.service.ProjectionService;
import cl.tutorial.desglose.service.dto.BreakdownRecoverResponseDTO;
import cl.tutorial.desglose.service.dto.PeriodoDTO;
import cl.tutorial.desglose.service.dto.ProjectionHistoricDataRequestDTO;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static cl.tutorial.desglose.config.Constants.ESTADO_DEFINITIVO;
import static cl.tutorial.desglose.config.Constants.ESTADO_EN_EDICION;

/**
 * REST controller for managing Proyecciones.
 */
@RestController
@RequestMapping("/api")
public class ProjectionResource {

    private final Logger log = LogManager.getLogger(ProjectionResource.class);

    private static final String ENTITY_NAME = "projections";

    private final DesgloseService   desgloseService;
    private final ProjectionService projectionService;

    public ProjectionResource(DesgloseService desgloseService, ProjectionService projectionService) {
        this.desgloseService = desgloseService;
        this.projectionService = projectionService;
    }

    /**
     * POST  /projection-historic-data : get the historic data to use in the projection.
     *
     * @param historicDataRequestDTO
     * 		objeto con los valores del filtro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ProjectionHistoricDataResponseDTO in body
     */
    @PostMapping("/projection-historic-data")
    @Timed
    public ResponseEntity<BreakdownRecoverResponseDTO> getHistoricData(@Valid @RequestBody ProjectionHistoricDataRequestDTO historicDataRequestDTO) {
        log.info("REST request to get Historic Data to project a Desglose with value: {}", historicDataRequestDTO);
        BreakdownRecoverResponseDTO page = projectionService.getHistoricData(historicDataRequestDTO);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/projection-cerrar")
    @Timed
    public ResponseEntity<PeriodoDTO> cerrarPeriodo(@Valid @RequestBody ProjectionHistoricDataRequestDTO historicDataRequestDTO) {
        log.info("cerrando periodo : {}", historicDataRequestDTO);
        PeriodoPK id = new PeriodoPK();
        id.setFecPeriodo(historicDataRequestDTO.fecPeriodo);
        id.setCodPlantilla(historicDataRequestDTO.codPlantilla);
        id.setCodCliente(historicDataRequestDTO.codCliente);
        id.setCodMoneda(historicDataRequestDTO.codMoneda);
        id.setCodSubtipoDes(historicDataRequestDTO.codSubtipoDes);
        id.setCodTipoDes(historicDataRequestDTO.codTipoDes);
        PeriodoDTO periodoDTO =  projectionService.updateEstado(id,ESTADO_DEFINITIVO);
        final String entityId = HeaderUtil.createHeaderEntityId(periodoDTO.getCodCliente(), periodoDTO.getCodMoneda());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(periodoDTO);
    }
    @PostMapping("/projection-editar")
    @Timed
    public ResponseEntity<PeriodoDTO> editarPeriodo(@Valid @RequestBody ProjectionHistoricDataRequestDTO historicDataRequestDTO) {
        log.info("cerrando periodo : {}", historicDataRequestDTO);
        PeriodoPK id = new PeriodoPK();
        id.setFecPeriodo(historicDataRequestDTO.fecPeriodo);
        id.setCodPlantilla(historicDataRequestDTO.codPlantilla);
        id.setCodCliente(historicDataRequestDTO.codCliente);
        id.setCodMoneda(historicDataRequestDTO.codMoneda);
        id.setCodSubtipoDes(historicDataRequestDTO.codSubtipoDes);
        id.setCodTipoDes(historicDataRequestDTO.codTipoDes);
        PeriodoDTO periodoDTO =  projectionService.updateEstado(id,ESTADO_EN_EDICION);
        final String entityId = HeaderUtil.createHeaderEntityId(periodoDTO.getCodCliente(), periodoDTO.getCodMoneda());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(periodoDTO);
    }
}
