package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.SfStfOperatorService;
import cl.tutorial.desglose.service.dto.SfStfOperatorDTO;
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
 * REST controller for managing SfStfOperator.
 */
@RestController
@RequestMapping("/api")
public class SfStfOperatorResource {

    private final Logger log = LogManager.getLogger(SfStfOperatorResource.class);

    private static final String ENTITY_NAME = "sfStfOperator";

    private final SfStfOperatorService sfStfOperatorService;

    public SfStfOperatorResource(SfStfOperatorService sfStfOperatorService) {
        this.sfStfOperatorService = sfStfOperatorService;
    }

    /**
     * POST  /sf-stf-operators : Create a new sfStfOperator.
     *
     * @param sfStfOperatorDTO
     * 		the sfStfOperatorDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new sfStfOperatorDTO, or with status 400 (Bad Request) if the sfStfOperator has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/sf-stf-operators")
    @Timed
    public ResponseEntity<SfStfOperatorDTO> createSfStfOperator(@Valid @RequestBody SfStfOperatorDTO sfStfOperatorDTO) throws URISyntaxException {
        log.info("REST request to save SfStfOperator : {}", sfStfOperatorDTO);
        if (sfStfOperatorDTO.getBranchCode() != null) {
            throw new BadRequestAlertException("A new sfStfOperator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SfStfOperatorDTO result = sfStfOperatorService.save(sfStfOperatorDTO);
        return ResponseEntity.created(new URI("/api/sf-stf-operators/" + result.getBranchCode()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getBranchCode().toString())).body(result);
    }

    /**
     * PUT  /sf-stf-operators : Updates an existing sfStfOperator.
     *
     * @param sfStfOperatorDTO
     * 		the sfStfOperatorDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated sfStfOperatorDTO, or with status 400 (Bad Request) if the sfStfOperatorDTO is not valid, or with
     * status 500 (Internal Server Error) if the sfStfOperatorDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/sf-stf-operators")
    @Timed
    public ResponseEntity<SfStfOperatorDTO> updateSfStfOperator(@Valid @RequestBody SfStfOperatorDTO sfStfOperatorDTO) throws URISyntaxException {
        log.info("REST request to update SfStfOperator : {}", sfStfOperatorDTO);
        if (sfStfOperatorDTO.getBranchCode() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SfStfOperatorDTO result = sfStfOperatorService.save(sfStfOperatorDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sfStfOperatorDTO.getBranchCode().toString())).body(result);
    }

    /**
     * GET  /sf-stf-operators : get all the sfStfOperators.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sfStfOperators in body
     */
    @GetMapping("/sf-stf-operators")
    @Timed
    public ResponseEntity<List<SfStfOperatorDTO>> getAllSfStfOperators(Pageable pageable) {
        log.info("REST request to get a page of SfStfOperators");
        Page<SfStfOperatorDTO> page = sfStfOperatorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sf-stf-operators");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/sf-stf-operators-listar-analistas")
    @Timed
    public ResponseEntity<List<SfStfOperatorDTO>> listarAnalistas() {
        log.info("REST request to get a page of SfStfOperators");
        List<SfStfOperatorDTO> sfStfOperatorList = sfStfOperatorService.listarAnalistas();
        return new ResponseEntity<>(sfStfOperatorList, HttpStatus.OK);
    }

    /**
     * GET  /sf-stf-operators/:id : get the "id" sfStfOperator.
     *
     * @param id
     * 		the id of the sfStfOperatorDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the sfStfOperatorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sf-stf-operators/{id}")
    @Timed
    public ResponseEntity<SfStfOperatorDTO> getSfStfOperator(@PathVariable Long id) {
        log.info("REST request to get SfStfOperator : {}", id);
        Optional<SfStfOperatorDTO> sfStfOperatorDTO = sfStfOperatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sfStfOperatorDTO);
    }

    @GetMapping("/sf-stf-operators-byLoginName/{loginName}")
    @Timed
    public ResponseEntity<SfStfOperatorDTO> findAllByLoginName(@PathVariable String loginName) {
        log.info("REST request to get SfStfOperator : {}", loginName);
        SfStfOperatorDTO sfStfOperatorDTO = sfStfOperatorService.findFirstByLoginName(loginName);
        return new ResponseEntity<>(sfStfOperatorDTO, HttpStatus.OK);
    }

    /**
     * DELETE  /sf-stf-operators/:id : delete the "id" sfStfOperator.
     *
     * @param id
     * 		the id of the sfStfOperatorDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sf-stf-operators/{id}")
    @Timed
    public ResponseEntity<Void> deleteSfStfOperator(@PathVariable Long id) {
        log.info("REST request to delete SfStfOperator : {}", id);
        sfStfOperatorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
