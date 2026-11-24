package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseImpresionPK;
import cl.tutorial.desglose.service.DesgloseImpresionService;
import cl.tutorial.desglose.service.dto.DesgloseImpresionDTO;
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
 * REST controller for managing DesgloseImpresion.
 */
@RestController
@RequestMapping("/api")
public class DesgloseImpresionResource {

    private final Logger log = LogManager.getLogger(DesgloseImpresionResource.class);

    private static final String ENTITY_NAME = "desgloseImpresion";

    private final DesgloseImpresionService desgloseImpresionService;

    public DesgloseImpresionResource(DesgloseImpresionService desgloseImpresionService) {
        this.desgloseImpresionService = desgloseImpresionService;
    }

    /**
     * POST  /desglose-impresions : Create a new desgloseImpresion.
     *
     * @param desgloseImpresionDTO
     * 		the desgloseImpresionDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseImpresionDTO, or with status 400 (Bad Request) if the desgloseImpresion has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-impresions")
    @Timed
    public ResponseEntity<DesgloseImpresionDTO> createDesgloseImpresion(@Valid @RequestBody DesgloseImpresionDTO desgloseImpresionDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseImpresion : {}", desgloseImpresionDTO);
        if (desgloseImpresionDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new desgloseImpresion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseImpresionDTO result = desgloseImpresionService.save(desgloseImpresionDTO);
        return ResponseEntity.created(new URI("/api/desglose-impresions/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /desglose-impresions : Updates an existing desgloseImpresion.
     *
     * @param desgloseImpresionDTO
     * 		the desgloseImpresionDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseImpresionDTO, or with status 400 (Bad Request) if the desgloseImpresionDTO is not valid, or
     * with status 500 (Internal Server Error) if the desgloseImpresionDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-impresions")
    @Timed
    public ResponseEntity<DesgloseImpresionDTO> updateDesgloseImpresion(@Valid @RequestBody DesgloseImpresionDTO desgloseImpresionDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseImpresion : {}", desgloseImpresionDTO);
        if (desgloseImpresionDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseImpresionDTO result = desgloseImpresionService.save(desgloseImpresionDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseImpresionDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /desglose-impresions : get all the desgloseImpresions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseImpresions in body
     */
    @GetMapping("/desglose-impresions")
    @Timed
    public ResponseEntity<List<DesgloseImpresionDTO>> getAllDesgloseImpresions(Pageable pageable) {
        log.info("REST request to get a page of DesgloseImpresions");
        Page<DesgloseImpresionDTO> page = desgloseImpresionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-impresions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-impresions/:id : get the "id" desgloseImpresion.
     *
     * @param id
     * 		the id of the desgloseImpresionDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseImpresionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-impresions/{id}")
    @Timed
    public ResponseEntity<DesgloseImpresionDTO> getDesgloseImpresion(@PathVariable Long id) {
        DesgloseImpresionPK id1 = new DesgloseImpresionPK();
        log.info("REST request to get DesgloseImpresion : {}", id);
        Optional<DesgloseImpresionDTO> desgloseImpresionDTO = desgloseImpresionService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseImpresionDTO);
    }

    /**
     * DELETE  /desglose-impresions/:id : delete the "id" desgloseImpresion.
     *
     * @param id
     * 		the id of the desgloseImpresionDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-impresions/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseImpresion(@PathVariable Long id) {
        DesgloseImpresionPK id1 = new DesgloseImpresionPK();
        log.info("REST request to delete DesgloseImpresion : {}", id);
        desgloseImpresionService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/desglose-impresions-executeCifSpInsDes")
    @Timed
    public ResponseEntity<Integer> executeCifSpInsDes(@Valid @RequestBody DesgloseImpresionDTO desgloseImpresionDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseImpresion : {}", desgloseImpresionDTO);
        Integer corImpreso = desgloseImpresionService.executeCifSpInsDes(desgloseImpresionDTO,null);
        return  new ResponseEntity<>(corImpreso,HttpStatus.OK);
    }
}
