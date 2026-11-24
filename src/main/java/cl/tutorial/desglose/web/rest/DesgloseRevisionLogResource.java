package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseRevisionLogPK;
import cl.tutorial.desglose.service.DesgloseRevisionLogService;
import cl.tutorial.desglose.service.dto.DesgloseRevisionLogDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DesgloseRevisionLog.
 */
@RestController
@RequestMapping("/api")
public class DesgloseRevisionLogResource {

    private final Logger log = LogManager.getLogger(DesgloseRevisionLogResource.class);

    private static final String ENTITY_NAME = "desgloseRevisionLog";

    private final DesgloseRevisionLogService desgloseRevisionLogService;

    public DesgloseRevisionLogResource(DesgloseRevisionLogService desgloseRevisionLogService) {
        this.desgloseRevisionLogService = desgloseRevisionLogService;
    }

    /**
     * POST  /desglose-revision-logs : Create a new desgloseRevisionLog.
     *
     * @param desgloseRevisionLogDTO
     * 		the desgloseRevisionLogDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseRevisionLogDTO, or with status 400 (Bad Request) if the desgloseRevisionLog has already an
     * ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-revision-logs")
    @Timed
    public ResponseEntity<DesgloseRevisionLogDTO> createDesgloseRevisionLog(@Valid @RequestBody DesgloseRevisionLogDTO desgloseRevisionLogDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseRevisionLog : {}", desgloseRevisionLogDTO);
        if (desgloseRevisionLogDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new desgloseRevisionLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseRevisionLogDTO result = desgloseRevisionLogService.save(desgloseRevisionLogDTO);
        return ResponseEntity.created(new URI("/api/desglose-revision-logs/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /desglose-revision-logs : Updates an existing desgloseRevisionLog.
     *
     * @param desgloseRevisionLogDTO
     * 		the desgloseRevisionLogDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseRevisionLogDTO, or with status 400 (Bad Request) if the desgloseRevisionLogDTO is not
     * valid, or with status 500 (Internal Server Error) if the desgloseRevisionLogDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-revision-logs")
    @Timed
    public ResponseEntity<DesgloseRevisionLogDTO> updateDesgloseRevisionLog(@Valid @RequestBody DesgloseRevisionLogDTO desgloseRevisionLogDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseRevisionLog : {}", desgloseRevisionLogDTO);
        if (desgloseRevisionLogDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseRevisionLogDTO result = desgloseRevisionLogService.save(desgloseRevisionLogDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseRevisionLogDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /desglose-revision-logs : get all the desgloseRevisionLogs.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseRevisionLogs in body
     */
    @GetMapping("/desglose-revision-logs")
    @Timed
    public ResponseEntity<List<DesgloseRevisionLogDTO>> getAllDesgloseRevisionLogs(Pageable pageable) {
        log.info("REST request to get a page of DesgloseRevisionLogs");
        Page<DesgloseRevisionLogDTO> page = desgloseRevisionLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-revision-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-revision-logs/:id : get the "id" desgloseRevisionLog.
     *
     * @param id
     * 		the id of the desgloseRevisionLogDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseRevisionLogDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-revision-logs/{id}")
    @Timed
    public ResponseEntity<DesgloseRevisionLogDTO> getDesgloseRevisionLog(@PathVariable Long id) {
        DesgloseRevisionLogPK id1 = new DesgloseRevisionLogPK();
        log.info("REST request to get DesgloseRevisionLog : {}", id);
        Optional<DesgloseRevisionLogDTO> desgloseRevisionLogDTO = desgloseRevisionLogService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseRevisionLogDTO);
    }

    /**
     * DELETE  /desglose-revision-logs/:id : delete the "id" desgloseRevisionLog.
     *
     * @param id
     * 		the id of the desgloseRevisionLogDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-revision-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseRevisionLog(@PathVariable Long id) {
        DesgloseRevisionLogPK id1 = new DesgloseRevisionLogPK();
        log.info("REST request to delete DesgloseRevisionLog : {}", id);
        desgloseRevisionLogService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
