package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.ValPeriodoDesgImprPK;
import cl.tutorial.desglose.service.ValPeriodoDesgImprService;
import cl.tutorial.desglose.service.dto.ValPeriodoDesgImprDTO;
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
 * REST controller for managing ValPeriodoDesgImpr.
 */
@RestController
@RequestMapping("/api")
public class ValPeriodoDesgImprResource {

    private final Logger log = LogManager.getLogger(ValPeriodoDesgImprResource.class);

    private static final String ENTITY_NAME = "valPeriodoDesgImpr";

    private final ValPeriodoDesgImprService valPeriodoDesgImprService;

    public ValPeriodoDesgImprResource(ValPeriodoDesgImprService valPeriodoDesgImprService) {
        this.valPeriodoDesgImprService = valPeriodoDesgImprService;
    }

    /**
     * POST  /val-periodo-desg-imprs : Create a new valPeriodoDesgImpr.
     *
     * @param valPeriodoDesgImprDTO
     * 		the valPeriodoDesgImprDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new valPeriodoDesgImprDTO, or with status 400 (Bad Request) if the valPeriodoDesgImpr has already an
     * ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/val-periodo-desg-imprs")
    @Timed
    public ResponseEntity<ValPeriodoDesgImprDTO> createValPeriodoDesgImpr(@Valid @RequestBody ValPeriodoDesgImprDTO valPeriodoDesgImprDTO) throws URISyntaxException {
        log.info("REST request to save ValPeriodoDesgImpr : {}", valPeriodoDesgImprDTO);
        ValPeriodoDesgImprDTO result = valPeriodoDesgImprService.save(valPeriodoDesgImprDTO);
        return ResponseEntity.created(new URI("/api/val-periodo-desg-imprs/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /val-periodo-desg-imprs : Updates an existing valPeriodoDesgImpr.
     *
     * @param valPeriodoDesgImprDTO
     * 		the valPeriodoDesgImprDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated valPeriodoDesgImprDTO, or with status 400 (Bad Request) if the valPeriodoDesgImprDTO is not valid,
     * or with status 500 (Internal Server Error) if the valPeriodoDesgImprDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/val-periodo-desg-imprs")
    @Timed
    public ResponseEntity<ValPeriodoDesgImprDTO> updateValPeriodoDesgImpr(@Valid @RequestBody ValPeriodoDesgImprDTO valPeriodoDesgImprDTO) throws URISyntaxException {
        log.info("REST request to update ValPeriodoDesgImpr : {}", valPeriodoDesgImprDTO);
        if (valPeriodoDesgImprDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValPeriodoDesgImprDTO result = valPeriodoDesgImprService.save(valPeriodoDesgImprDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, valPeriodoDesgImprDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /val-periodo-desg-imprs : get all the valPeriodoDesgImprs.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of valPeriodoDesgImprs in body
     */
    @GetMapping("/val-periodo-desg-imprs")
    @Timed
    public ResponseEntity<List<ValPeriodoDesgImprDTO>> getAllValPeriodoDesgImprs(Pageable pageable) {
        log.info("REST request to get a page of ValPeriodoDesgImprs");
        Page<ValPeriodoDesgImprDTO> page = valPeriodoDesgImprService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/val-periodo-desg-imprs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /val-periodo-desg-imprs/:id : get the "id" valPeriodoDesgImpr.
     *
     * @param id
     * 		the id of the valPeriodoDesgImprDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the valPeriodoDesgImprDTO, or with status 404 (Not Found)
     */
    @GetMapping("/val-periodo-desg-imprs/{id}")
    @Timed
    public ResponseEntity<ValPeriodoDesgImprDTO> getValPeriodoDesgImpr(@PathVariable Long id) {
        ValPeriodoDesgImprPK id1 = new ValPeriodoDesgImprPK();
        log.info("REST request to get ValPeriodoDesgImpr : {}", id);
        Optional<ValPeriodoDesgImprDTO> valPeriodoDesgImprDTO = valPeriodoDesgImprService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(valPeriodoDesgImprDTO);
    }

    /**
     * DELETE  /val-periodo-desg-imprs/:id : delete the "id" valPeriodoDesgImpr.
     *
     * @param id
     * 		the id of the valPeriodoDesgImprDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/val-periodo-desg-imprs/{id}")
    @Timed
    public ResponseEntity<Void> deleteValPeriodoDesgImpr(@PathVariable Long id) {
        ValPeriodoDesgImprPK id1 = new ValPeriodoDesgImprPK();
        log.info("REST request to delete ValPeriodoDesgImpr : {}", id);
        valPeriodoDesgImprService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * ejecuta procedimiento almacenado
     * @author carlos eduardo rodriguez
     * @param valPeriodoDesgImprDTO
     * @return
     */
    @PostMapping("/val-periodo-desg-imprs/executeCifSpInsValImp")
    @Timed
    public ResponseEntity<Void> executeCifSpInsValImp(@Valid @RequestBody ValPeriodoDesgImprDTO valPeriodoDesgImprDTO) throws URISyntaxException {
        log.info("REST request to save ValPeriodoDesgImpr : {}", valPeriodoDesgImprDTO);
        valPeriodoDesgImprService.executeCifSpInsValImp(valPeriodoDesgImprDTO,null);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert(ENTITY_NAME,"")).build();
    }
}
