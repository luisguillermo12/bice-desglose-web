package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import cl.tutorial.desglose.service.PeriodoService;
import cl.tutorial.desglose.service.dto.PeriodDeleteRequestDTO;
import cl.tutorial.desglose.service.dto.PeriodoDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Periodo.
 */
@RestController
@RequestMapping("/api")
public class PeriodoResource {

    private final Logger log = LogManager.getLogger(PeriodoResource.class);

    private static final String ENTITY_NAME = "periodo";

    private final PeriodoService periodoService;

    public PeriodoResource(PeriodoService periodoService) {
        this.periodoService = periodoService;
    }

    /**
     * POST  /periodos : Create a new periodo.
     *
     * @param periodoDTO
     * 		the periodoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new periodoDTO, or with status 400 (Bad Request) if the periodo has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/periodos")
    @Timed
    public ResponseEntity<PeriodoDTO> createPeriodo(@Valid @RequestBody PeriodoDTO periodoDTO) throws URISyntaxException {
        log.info("REST request to save Periodo : {}", periodoDTO);
        if (!ObjectUtils.allNotNull(periodoDTO.getCodCliente(), periodoDTO.getCodTipoDes(), periodoDTO.getCodSubtipoDes(), periodoDTO.getCodMoneda())) {
            throw new BadRequestAlertException("A new periodo must have a valid ID", ENTITY_NAME, "notValidId");
        }

        PeriodoPK id = new PeriodoPK();
        id.setCodCliente(periodoDTO.getCodCliente());
        id.setCodTipoDes(periodoDTO.getCodTipoDes());
        id.setCodSubtipoDes(periodoDTO.getCodSubtipoDes());
        id.setCodMoneda(periodoDTO.getCodMoneda());
        if (periodoService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("A new periodo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodoDTO result = periodoService.save(periodoDTO);
        final String entityId =
            HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes().toString(), result.getCodSubtipoDes().toString(), result.getCodMoneda());
        return ResponseEntity.created(
            new URI("/api/periodos/" + result.getCodCliente() + "/" + result.getCodTipoDes().toString() + "/" + result.getCodSubtipoDes().toString() + "/" + result.getCodMoneda()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * PUT  /periodos : Updates an existing periodo.
     *
     * @param periodoDTO
     * 		the periodoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated periodoDTO, or with status 400 (Bad Request) if the periodoDTO is not valid, or with status 500
     * (Internal Server Error) if the periodoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/periodos")
    @Timed
    public ResponseEntity<PeriodoDTO> updatePeriodo(@Valid @RequestBody PeriodoDTO periodoDTO) throws URISyntaxException {
        log.info("REST request to update Periodo : {}", periodoDTO);
        if (!ObjectUtils.allNotNull(periodoDTO.getCodCliente(), periodoDTO.getCodTipoDes(), periodoDTO.getCodSubtipoDes(), periodoDTO.getCodMoneda())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        PeriodoPK id = new PeriodoPK();
        id.setCodCliente(periodoDTO.getCodCliente());
        id.setCodTipoDes(periodoDTO.getCodTipoDes());
        id.setCodSubtipoDes(periodoDTO.getCodSubtipoDes());
        id.setCodMoneda(periodoDTO.getCodMoneda());
        if (!periodoService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Invalid id. Entity not found", ENTITY_NAME, "idNotFound");
        }
        PeriodoDTO result = periodoService.save(periodoDTO);
        final String entityId =
            HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes().toString(), result.getCodSubtipoDes().toString(), result.getCodMoneda());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * GET  /periodos : get all the periodos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of periodos in body
     */
    @GetMapping("/periodos")
    @Timed
    public ResponseEntity<List<PeriodoDTO>> getAllPeriodos(Pageable pageable) {
        log.info("REST request to get a page of Periodos");
        Page<PeriodoDTO> page = periodoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/periodos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /periodos/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda : get the "id" periodo.
     *
     * @param codCliente
     * 		the id of the periodoDTO to retrieve
     * @param codTipoDes
     * 		the id of the periodoDTO to retrieve
     * @param codSubtipoDes
     * 		the id of the periodoDTO to retrieve
     * @param codMoneda
     * 		the id of the periodoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the periodoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/periodos/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}")
    @Timed
    public ResponseEntity<PeriodoDTO> getPeriodo(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda) {

        PeriodoPK id = new PeriodoPK();
        id.setCodCliente(codCliente);
        id.setCodTipoDes(codTipoDes);
        id.setCodSubtipoDes(codSubtipoDes);
        id.setCodMoneda(codMoneda);

        log.info("REST request to get Periodo : {}", id);
        Optional<PeriodoDTO> periodoDTO = periodoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodoDTO);
    }

    /**
     * DELETE  /periodos/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:fecPeriodo : delete the "id" periodo.
     *
     * @param codCliente
     * 		the id of the periodoDTO to retrieve
     * @param codTipoDes
     * 		the id of the periodoDTO to retrieve
     * @param codSubtipoDes
     * 		the id of the periodoDTO to retrieve
     * @param codMoneda
     * 		the id of the periodoDTO to retrieve
     * @param fecPeriodo
     * 		the id of the periodoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periodos/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{fecPeriodo}/{codPlantilla}")
    @Timed
    public ResponseEntity<Void> deletePeriodo(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable LocalDate fecPeriodo,@PathVariable Integer codPlantilla) {

        PeriodDeleteRequestDTO requestDTO = new PeriodDeleteRequestDTO();
        requestDTO.codCliente = codCliente;
        requestDTO.codTipoDes = codTipoDes;
        requestDTO.codSubtipoDes = codSubtipoDes;
        requestDTO.codMoneda = codMoneda;
        requestDTO.fecPeriodo = fecPeriodo;
        requestDTO.codPlantilla = codPlantilla;
        log.info("REST request to delete Periodo : {}", requestDTO);
        periodoService.delete(requestDTO);
        final String entityId = HeaderUtil.createHeaderEntityId(requestDTO.codCliente, requestDTO.codTipoDes.toString(), requestDTO.codSubtipoDes.toString(), requestDTO.codMoneda,
            requestDTO.fecPeriodo.format(Constants.FORMATTER_DATE_SHORT));
        // return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, entityId)).build();
    }

    /**
     * DELETE  /periodos/delete : delete a periodo.
     *
     * @param requestDTO
     * 		the requestDTO param
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periodos/delete")
    @Timed
    public ResponseEntity<Void> deletePeriodo(@Valid @RequestBody PeriodDeleteRequestDTO requestDTO) {
        log.info("REST request to delete Periodo : {}", requestDTO);
        periodoService.delete(requestDTO);
        final String entityId = HeaderUtil.createHeaderEntityId(requestDTO.codCliente, requestDTO.codTipoDes.toString(), requestDTO.codSubtipoDes.toString(), requestDTO.codMoneda,
            requestDTO.fecPeriodo.format(Constants.FORMATTER_DATE_SHORT));
        // return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, entityId)).build();
    }
}
