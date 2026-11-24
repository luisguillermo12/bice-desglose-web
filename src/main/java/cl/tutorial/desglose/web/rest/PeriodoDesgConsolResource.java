package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.PeriodoDesgConsolPK;
import cl.tutorial.desglose.service.PeriodoDesgConsolService;
import cl.tutorial.desglose.service.dto.PeriodoDesgConsolDTO;
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
 * REST controller for managing PeriodoDesgConsol.
 */
@RestController
@RequestMapping("/api")
public class PeriodoDesgConsolResource {

    private final Logger log = LogManager.getLogger(PeriodoDesgConsolResource.class);

    private static final String ENTITY_NAME = "periodoDesgConsol";

    private final PeriodoDesgConsolService periodoDesgConsolService;

    public PeriodoDesgConsolResource(PeriodoDesgConsolService periodoDesgConsolService) {
        this.periodoDesgConsolService = periodoDesgConsolService;
    }

    /**
     * POST  /periodo-desg-consols : Create a new periodoDesgConsol.
     *
     * @param periodoDesgConsolDTO
     * 		the periodoDesgConsolDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new periodoDesgConsolDTO, or with status 400 (Bad Request) if the periodoDesgConsol has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/periodo-desg-consols")
    @Timed
    public ResponseEntity<PeriodoDesgConsolDTO> createPeriodoDesgConsol(@Valid @RequestBody PeriodoDesgConsolDTO periodoDesgConsolDTO) throws URISyntaxException {
        log.info("REST request to save PeriodoDesgConsol : {}", periodoDesgConsolDTO);
        if (periodoDesgConsolDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new periodoDesgConsol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodoDesgConsolDTO result = periodoDesgConsolService.save(periodoDesgConsolDTO);
        return ResponseEntity.created(new URI("/api/periodo-desg-consols/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /periodo-desg-consols : Updates an existing periodoDesgConsol.
     *
     * @param periodoDesgConsolDTO
     * 		the periodoDesgConsolDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated periodoDesgConsolDTO, or with status 400 (Bad Request) if the periodoDesgConsolDTO is not valid, or
     * with status 500 (Internal Server Error) if the periodoDesgConsolDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/periodo-desg-consols")
    @Timed
    public ResponseEntity<PeriodoDesgConsolDTO> updatePeriodoDesgConsol(@Valid @RequestBody PeriodoDesgConsolDTO periodoDesgConsolDTO) throws URISyntaxException {
        log.info("REST request to update PeriodoDesgConsol : {}", periodoDesgConsolDTO);
        if (periodoDesgConsolDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeriodoDesgConsolDTO result = periodoDesgConsolService.save(periodoDesgConsolDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodoDesgConsolDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /periodo-desg-consols : get all the periodoDesgConsols.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of periodoDesgConsols in body
     */
    @GetMapping("/periodo-desg-consols")
    @Timed
    public ResponseEntity<List<PeriodoDesgConsolDTO>> getAllPeriodoDesgConsols(Pageable pageable) {
        log.info("REST request to get a page of PeriodoDesgConsols");
        Page<PeriodoDesgConsolDTO> page = periodoDesgConsolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/periodo-desg-consols");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /periodo-desg-consols/:id : get the "id" periodoDesgConsol.
     *
     * @param id
     * 		the id of the periodoDesgConsolDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the periodoDesgConsolDTO, or with status 404 (Not Found)
     */
    @GetMapping("/periodo-desg-consols/{id}")
    @Timed
    public ResponseEntity<PeriodoDesgConsolDTO> getPeriodoDesgConsol(@PathVariable Long id) {
        PeriodoDesgConsolPK id1 = new PeriodoDesgConsolPK();
        log.info("REST request to get PeriodoDesgConsol : {}", id);
        Optional<PeriodoDesgConsolDTO> periodoDesgConsolDTO = periodoDesgConsolService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(periodoDesgConsolDTO);
    }

    /**
     * DELETE  /periodo-desg-consols/:id : delete the "id" periodoDesgConsol.
     *
     * @param id
     * 		the id of the periodoDesgConsolDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periodo-desg-consols/{id}")
    @Timed
    public ResponseEntity<Void> deletePeriodoDesgConsol(@PathVariable Long id) {
        PeriodoDesgConsolPK id1 = new PeriodoDesgConsolPK();
        log.info("REST request to delete PeriodoDesgConsol : {}", id);
        periodoDesgConsolService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
