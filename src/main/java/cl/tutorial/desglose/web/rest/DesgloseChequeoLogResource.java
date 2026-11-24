package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseChequeoLogPK;
import cl.tutorial.desglose.service.DesgloseChequeoLogService;
import cl.tutorial.desglose.service.dto.DesgloseChequeoLogDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DesgloseChequeoLog.
 */
@RestController
@RequestMapping("/api")
public class DesgloseChequeoLogResource {

    private final Logger log = LogManager.getLogger(DesgloseChequeoLogResource.class);

    private static final String ENTITY_NAME = "desgloseChequeoLog";

    private final DesgloseChequeoLogService desgloseChequeoLogService;

    public DesgloseChequeoLogResource(DesgloseChequeoLogService desgloseChequeoLogService) {
        this.desgloseChequeoLogService = desgloseChequeoLogService;
    }

    /**
     * POST  /desglose-chequeo-logs : Create a new desgloseChequeoLog.
     *
     * @param desgloseChequeoLogDTO
     * 		the desgloseChequeoLogDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseChequeoLogDTO, or with status 400 (Bad Request) if the desgloseChequeoLog has already an
     * ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-chequeo-logs")
    @Timed
    public ResponseEntity<DesgloseChequeoLogDTO> createDesgloseChequeoLog(@RequestBody DesgloseChequeoLogDTO desgloseChequeoLogDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseChequeoLog : {}", desgloseChequeoLogDTO);
        if (desgloseChequeoLogDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new desgloseChequeoLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseChequeoLogDTO result = desgloseChequeoLogService.save(desgloseChequeoLogDTO);
        return ResponseEntity.created(new URI("/api/desglose-chequeo-logs/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /desglose-chequeo-logs : Updates an existing desgloseChequeoLog.
     *
     * @param desgloseChequeoLogDTO
     * 		the desgloseChequeoLogDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseChequeoLogDTO, or with status 400 (Bad Request) if the desgloseChequeoLogDTO is not valid,
     * or with status 500 (Internal Server Error) if the desgloseChequeoLogDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-chequeo-logs")
    @Timed
    public ResponseEntity<DesgloseChequeoLogDTO> updateDesgloseChequeoLog(@RequestBody DesgloseChequeoLogDTO desgloseChequeoLogDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseChequeoLog : {}", desgloseChequeoLogDTO);
        if (desgloseChequeoLogDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseChequeoLogDTO result = desgloseChequeoLogService.save(desgloseChequeoLogDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseChequeoLogDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /desglose-chequeo-logs : get all the desgloseChequeoLogs.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseChequeoLogs in body
     */
    @GetMapping("/desglose-chequeo-logs")
    @Timed
    public ResponseEntity<List<DesgloseChequeoLogDTO>> getAllDesgloseChequeoLogs(Pageable pageable) {
        log.info("REST request to get a page of DesgloseChequeoLogs");
        Page<DesgloseChequeoLogDTO> page = desgloseChequeoLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-chequeo-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-chequeo-logs/:id : get the "id" desgloseChequeoLog.
     *
     * @param id
     * 		the id of the desgloseChequeoLogDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseChequeoLogDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-chequeo-logs/{id}")
    @Timed
    public ResponseEntity<DesgloseChequeoLogDTO> getDesgloseChequeoLog(@PathVariable Long id) {
        DesgloseChequeoLogPK id1 = new DesgloseChequeoLogPK();
        log.info("REST request to get DesgloseChequeoLog : {}", id);
        Optional<DesgloseChequeoLogDTO> desgloseChequeoLogDTO = desgloseChequeoLogService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseChequeoLogDTO);
    }

    /**
     * DELETE  /desglose-chequeo-logs/:id : delete the "id" desgloseChequeoLog.
     *
     * @param id
     * 		the id of the desgloseChequeoLogDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-chequeo-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseChequeoLog(@PathVariable Long id) {
        DesgloseChequeoLogPK id1 = new DesgloseChequeoLogPK();
        log.info("REST request to delete DesgloseChequeoLog : {}", id);
        desgloseChequeoLogService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
