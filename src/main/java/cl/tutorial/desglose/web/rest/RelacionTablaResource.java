package cl.tutorial.desglose.web.rest;

import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.RelacionTablaService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.RelacionTablaDTO;
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
 * REST controller for managing RelacionTabla.
 */
@RestController
@RequestMapping("/api")
public class RelacionTablaResource {

    private final Logger log = LogManager.getLogger(RelacionTablaResource.class);

    private static final String ENTITY_NAME = "relacionTabla";

    private final RelacionTablaService relacionTablaService;

    public RelacionTablaResource(RelacionTablaService relacionTablaService) {
        this.relacionTablaService = relacionTablaService;
    }

    /**
     * POST  /relacion-tablas : Create a new relacionTabla.
     *
     * @param relacionTablaDTO the relacionTablaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relacionTablaDTO, or with status 400 (Bad Request) if the relacionTabla has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/relacion-tablas")
    @Timed
    public ResponseEntity<RelacionTablaDTO> createRelacionTabla(@Valid @RequestBody RelacionTablaDTO relacionTablaDTO) throws URISyntaxException {
        log.info("REST request to save RelacionTabla : {}", relacionTablaDTO);
        if (relacionTablaDTO.getId() != null) {
            throw new BadRequestAlertException("A new relacionTabla cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RelacionTablaDTO result = relacionTablaService.save(relacionTablaDTO);
        return ResponseEntity.created(new URI("/api/relacion-tablas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /relacion-tablas : Updates an existing relacionTabla.
     *
     * @param relacionTablaDTO the relacionTablaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relacionTablaDTO,
     * or with status 400 (Bad Request) if the relacionTablaDTO is not valid,
     * or with status 500 (Internal Server Error) if the relacionTablaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/relacion-tablas")
    @Timed
    public ResponseEntity<RelacionTablaDTO> updateRelacionTabla(@Valid @RequestBody RelacionTablaDTO relacionTablaDTO) throws URISyntaxException {
        log.info("REST request to update RelacionTabla : {}", relacionTablaDTO);
        if (relacionTablaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RelacionTablaDTO result = relacionTablaService.save(relacionTablaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, relacionTablaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /relacion-tablas : get all the relacionTablas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of relacionTablas in body
     */
    @GetMapping("/relacion-tablas")
    @Timed
    public ResponseEntity<List<RelacionTablaDTO>> getAllRelacionTablas(Pageable pageable) {
        log.info("REST request to get a page of RelacionTablas");
        Page<RelacionTablaDTO> page = relacionTablaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/relacion-tablas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /relacion-tablas/:id : get the "id" relacionTabla.
     *
     * @param id the id of the relacionTablaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relacionTablaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/relacion-tablas/{id}")
    @Timed
    public ResponseEntity<RelacionTablaDTO> getRelacionTabla(@PathVariable Long id) {
        log.info("REST request to get RelacionTabla : {}", id);
        Optional<RelacionTablaDTO> relacionTablaDTO = relacionTablaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relacionTablaDTO);
    }

    /**
     * DELETE  /relacion-tablas/:id : delete the "id" relacionTabla.
     *
     * @param id the id of the relacionTablaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/relacion-tablas/{id}")
    @Timed
    public ResponseEntity<Void> deleteRelacionTabla(@PathVariable Long id) {
        log.info("REST request to delete RelacionTabla : {}", id);
        relacionTablaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
