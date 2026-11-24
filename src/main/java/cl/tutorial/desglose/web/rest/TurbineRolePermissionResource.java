package cl.tutorial.desglose.web.rest;

import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.TurbineRolePermissionService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.TurbineRolePermissionDTO;
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
 * REST controller for managing TurbineRolePermission.
 */
@RestController
@RequestMapping("/api")
public class TurbineRolePermissionResource {

    private final Logger log = LogManager.getLogger(TurbineRolePermissionResource.class);

    private static final String ENTITY_NAME = "turbineRolePermission";

    private final TurbineRolePermissionService turbineRolePermissionService;

    public TurbineRolePermissionResource(TurbineRolePermissionService turbineRolePermissionService) {
        this.turbineRolePermissionService = turbineRolePermissionService;
    }

    /**
     * POST  /turbine-role-permissions : Create a new turbineRolePermission.
     *
     * @param turbineRolePermissionDTO the turbineRolePermissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new turbineRolePermissionDTO, or with status 400 (Bad Request) if the turbineRolePermission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/turbine-role-permissions")
    @Timed
    public ResponseEntity<TurbineRolePermissionDTO> createTurbineRolePermission(@RequestBody TurbineRolePermissionDTO turbineRolePermissionDTO) throws URISyntaxException {
        log.info("REST request to save TurbineRolePermission : {}", turbineRolePermissionDTO);
        if (turbineRolePermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new turbineRolePermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurbineRolePermissionDTO result = turbineRolePermissionService.save(turbineRolePermissionDTO);
        return ResponseEntity.created(new URI("/api/turbine-role-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turbine-role-permissions : Updates an existing turbineRolePermission.
     *
     * @param turbineRolePermissionDTO the turbineRolePermissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated turbineRolePermissionDTO,
     * or with status 400 (Bad Request) if the turbineRolePermissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the turbineRolePermissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/turbine-role-permissions")
    @Timed
    public ResponseEntity<TurbineRolePermissionDTO> updateTurbineRolePermission(@RequestBody TurbineRolePermissionDTO turbineRolePermissionDTO) throws URISyntaxException {
        log.info("REST request to update TurbineRolePermission : {}", turbineRolePermissionDTO);
        if (turbineRolePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurbineRolePermissionDTO result = turbineRolePermissionService.save(turbineRolePermissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turbineRolePermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /turbine-role-permissions : get all the turbineRolePermissions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of turbineRolePermissions in body
     */
    @GetMapping("/turbine-role-permissions")
    @Timed
    public ResponseEntity<List<TurbineRolePermissionDTO>> getAllTurbineRolePermissions(Pageable pageable) {
        log.info("REST request to get a page of TurbineRolePermissions");
        Page<TurbineRolePermissionDTO> page = turbineRolePermissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turbine-role-permissions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /turbine-role-permissions/:id : get the "id" turbineRolePermission.
     *
     * @param id the id of the turbineRolePermissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the turbineRolePermissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turbine-role-permissions/{id}")
    @Timed
    public ResponseEntity<TurbineRolePermissionDTO> getTurbineRolePermission(@PathVariable Long id) {
        log.info("REST request to get TurbineRolePermission : {}", id);
        Optional<TurbineRolePermissionDTO> turbineRolePermissionDTO = turbineRolePermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turbineRolePermissionDTO);
    }

    /**
     * DELETE  /turbine-role-permissions/:id : delete the "id" turbineRolePermission.
     *
     * @param id the id of the turbineRolePermissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turbine-role-permissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurbineRolePermission(@PathVariable Long id) {
        log.info("REST request to delete TurbineRolePermission : {}", id);
        turbineRolePermissionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
