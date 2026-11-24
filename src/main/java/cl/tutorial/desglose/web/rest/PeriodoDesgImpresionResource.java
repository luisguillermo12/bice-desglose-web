package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.PeriodoDesgImpresionPK;
import cl.tutorial.desglose.service.PeriodoDesgImpresionService;
import cl.tutorial.desglose.service.dto.PeriodoDesgImpresionDTO;
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
 * REST controller for managing PeriodoDesgImpresion.
 */
@RestController
@RequestMapping("/api")
public class PeriodoDesgImpresionResource {

    private final Logger log = LogManager.getLogger(PeriodoDesgImpresionResource.class);

    private static final String ENTITY_NAME = "periodoDesgImpresion";

    private final PeriodoDesgImpresionService periodoDesgImpresionService;

    public PeriodoDesgImpresionResource(PeriodoDesgImpresionService periodoDesgImpresionService) {
        this.periodoDesgImpresionService = periodoDesgImpresionService;
    }

    /**
     * POST  /periodo-desg-impresions : Create a new periodoDesgImpresion.
     *
     * @param periodoDesgImpresionDTO
     * 		the periodoDesgImpresionDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new periodoDesgImpresionDTO, or with status 400 (Bad Request) if the periodoDesgImpresion has already
     * an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/periodo-desg-impresions")
    @Timed
    public ResponseEntity<PeriodoDesgImpresionDTO> createPeriodoDesgImpresion(@Valid @RequestBody PeriodoDesgImpresionDTO periodoDesgImpresionDTO) throws URISyntaxException {
        log.info("REST request to save PeriodoDesgImpresion : {}", periodoDesgImpresionDTO);
        if (periodoDesgImpresionDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new periodoDesgImpresion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodoDesgImpresionDTO result = periodoDesgImpresionService.save(periodoDesgImpresionDTO);
        return ResponseEntity.created(new URI("/api/periodo-desg-impresions/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /periodo-desg-impresions : Updates an existing periodoDesgImpresion.
     *
     * @param periodoDesgImpresionDTO
     * 		the periodoDesgImpresionDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated periodoDesgImpresionDTO, or with status 400 (Bad Request) if the periodoDesgImpresionDTO is not
     * valid, or with status 500 (Internal Server Error) if the periodoDesgImpresionDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/periodo-desg-impresions")
    @Timed
    public ResponseEntity<PeriodoDesgImpresionDTO> updatePeriodoDesgImpresion(@Valid @RequestBody PeriodoDesgImpresionDTO periodoDesgImpresionDTO) throws URISyntaxException {
        log.info("REST request to update PeriodoDesgImpresion : {}", periodoDesgImpresionDTO);
        if (periodoDesgImpresionDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeriodoDesgImpresionDTO result = periodoDesgImpresionService.save(periodoDesgImpresionDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodoDesgImpresionDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /periodo-desg-impresions : get all the periodoDesgImpresions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of periodoDesgImpresions in body
     */
    @GetMapping("/periodo-desg-impresions")
    @Timed
    public ResponseEntity<List<PeriodoDesgImpresionDTO>> getAllPeriodoDesgImpresions(Pageable pageable) {
        log.info("REST request to get a page of PeriodoDesgImpresions");
        Page<PeriodoDesgImpresionDTO> page = periodoDesgImpresionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/periodo-desg-impresions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /periodo-desg-impresions/:id : get the "id" periodoDesgImpresion.
     *
     * @param id
     * 		the id of the periodoDesgImpresionDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the periodoDesgImpresionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/periodo-desg-impresions/{id}")
    @Timed
    public ResponseEntity<PeriodoDesgImpresionDTO> getPeriodoDesgImpresion(@PathVariable Long id) {
        PeriodoDesgImpresionPK id1 = new PeriodoDesgImpresionPK();
        log.info("REST request to get PeriodoDesgImpresion : {}", id);
        Optional<PeriodoDesgImpresionDTO> periodoDesgImpresionDTO = periodoDesgImpresionService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(periodoDesgImpresionDTO);
    }

    /**
     * DELETE  /periodo-desg-impresions/:id : delete the "id" periodoDesgImpresion.
     *
     * @param id
     * 		the id of the periodoDesgImpresionDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periodo-desg-impresions/{id}")
    @Timed
    public ResponseEntity<Void> deletePeriodoDesgImpresion(@PathVariable Long id) {
        PeriodoDesgImpresionPK id1 = new PeriodoDesgImpresionPK();
        log.info("REST request to delete PeriodoDesgImpresion : {}", id);
        periodoDesgImpresionService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @PostMapping("/periodo-desg-impresions/executeCifSpInsPrdImp")
    @Timed
    public ResponseEntity<Void> executeCifSpInsPrdImp(@Valid @RequestBody List<PeriodoDesgImpresionDTO> listPeriodoDesgImpresionDTO) throws URISyntaxException {
        log.info("REST request to save PeriodoDesgImpresion : {}", listPeriodoDesgImpresionDTO);
        for (PeriodoDesgImpresionDTO item : listPeriodoDesgImpresionDTO ) {
            Integer retorno = periodoDesgImpresionService.executeCifSpInsPrdImp(item,null);
            if(retorno!= 0){
                break;
            }
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert(ENTITY_NAME,"")).build();
    }
}
