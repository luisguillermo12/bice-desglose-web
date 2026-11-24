package cl.tutorial.desglose.web.rest;

import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.TurbinePermissionService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.TurbinePermissionDTO;
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
 * REST controller for managing TurbinePermission.
 */
@RestController
@RequestMapping("/api")
public class TurbinePermissionResource {

    private final Logger log = LogManager.getLogger(TurbinePermissionResource.class);

    private static final String ENTITY_NAME = "turbinePermission";

    private final TurbinePermissionService turbinePermissionService;

    public TurbinePermissionResource(TurbinePermissionService turbinePermissionService) {
        this.turbinePermissionService = turbinePermissionService;
    }

    /**
     * POST  /turbine-permissions : Create a new turbinePermission.
     *
     * @param turbinePermissionDTO the turbinePermissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new turbinePermissionDTO, or with status 400 (Bad Request) if the turbinePermission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/turbine-permissions")
    @Timed
    public ResponseEntity<TurbinePermissionDTO> createTurbinePermission(@Valid @RequestBody TurbinePermissionDTO turbinePermissionDTO) throws URISyntaxException {
        log.info("REST request to save TurbinePermission : {}", turbinePermissionDTO);
        if (turbinePermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new turbinePermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurbinePermissionDTO result = turbinePermissionService.save(turbinePermissionDTO);
        return ResponseEntity.created(new URI("/api/turbine-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turbine-permissions : Updates an existing turbinePermission.
     *
     * @param turbinePermissionDTO the turbinePermissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated turbinePermissionDTO,
     * or with status 400 (Bad Request) if the turbinePermissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the turbinePermissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/turbine-permissions")
    @Timed
    public ResponseEntity<TurbinePermissionDTO> updateTurbinePermission(@Valid @RequestBody TurbinePermissionDTO turbinePermissionDTO) throws URISyntaxException {
        log.info("REST request to update TurbinePermission : {}", turbinePermissionDTO);
        if (turbinePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurbinePermissionDTO result = turbinePermissionService.save(turbinePermissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turbinePermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /turbine-permissions : get all the turbinePermissions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of turbinePermissions in body
     */
    @GetMapping("/turbine-permissions")
    @Timed
    public ResponseEntity<List<TurbinePermissionDTO>> getAllTurbinePermissions(Pageable pageable) {
        log.info("REST request to get a page of TurbinePermissions");
        Page<TurbinePermissionDTO> page = turbinePermissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turbine-permissions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /turbine-permissions/:id : get the "id" turbinePermission.
     *
     * @param id the id of the turbinePermissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the turbinePermissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turbine-permissions/{id}")
    @Timed
    public ResponseEntity<TurbinePermissionDTO> getTurbinePermission(@PathVariable Long id) {
        log.info("REST request to get TurbinePermission : {}", id);
        Optional<TurbinePermissionDTO> turbinePermissionDTO = turbinePermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turbinePermissionDTO);
    }

    /**
     * DELETE  /turbine-permissions/:id : delete the "id" turbinePermission.
     *
     * @param id the id of the turbinePermissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turbine-permissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurbinePermission(@PathVariable Long id) {
        log.info("REST request to delete TurbinePermission : {}", id);
        turbinePermissionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
