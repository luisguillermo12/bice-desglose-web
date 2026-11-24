package cl.tutorial.desglose.web.rest;

import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.TurbineRoleService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.TurbineRoleDTO;
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
 * REST controller for managing TurbineRole.
 */
@RestController
@RequestMapping("/api")
public class TurbineRoleResource {

    private final Logger log = LogManager.getLogger(TurbineRoleResource.class);

    private static final String ENTITY_NAME = "turbineRole";

    private final TurbineRoleService turbineRoleService;

    public TurbineRoleResource(TurbineRoleService turbineRoleService) {
        this.turbineRoleService = turbineRoleService;
    }

    /**
     * POST  /turbine-roles : Create a new turbineRole.
     *
     * @param turbineRoleDTO the turbineRoleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new turbineRoleDTO, or with status 400 (Bad Request) if the turbineRole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/turbine-roles")
    @Timed
    public ResponseEntity<TurbineRoleDTO> createTurbineRole(@Valid @RequestBody TurbineRoleDTO turbineRoleDTO) throws URISyntaxException {
        log.info("REST request to save TurbineRole : {}", turbineRoleDTO);
        if (turbineRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new turbineRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurbineRoleDTO result = turbineRoleService.save(turbineRoleDTO);
        return ResponseEntity.created(new URI("/api/turbine-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turbine-roles : Updates an existing turbineRole.
     *
     * @param turbineRoleDTO the turbineRoleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated turbineRoleDTO,
     * or with status 400 (Bad Request) if the turbineRoleDTO is not valid,
     * or with status 500 (Internal Server Error) if the turbineRoleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/turbine-roles")
    @Timed
    public ResponseEntity<TurbineRoleDTO> updateTurbineRole(@Valid @RequestBody TurbineRoleDTO turbineRoleDTO) throws URISyntaxException {
        log.info("REST request to update TurbineRole : {}", turbineRoleDTO);
        if (turbineRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurbineRoleDTO result = turbineRoleService.save(turbineRoleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turbineRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /turbine-roles : get all the turbineRoles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of turbineRoles in body
     */
    @GetMapping("/turbine-roles")
    @Timed
    public ResponseEntity<List<TurbineRoleDTO>> getAllTurbineRoles(Pageable pageable) {
        log.info("REST request to get a page of TurbineRoles");
        Page<TurbineRoleDTO> page = turbineRoleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turbine-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /turbine-roles/:id : get the "id" turbineRole.
     *
     * @param id the id of the turbineRoleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the turbineRoleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turbine-roles/{id}")
    @Timed
    public ResponseEntity<TurbineRoleDTO> getTurbineRole(@PathVariable Long id) {
        log.info("REST request to get TurbineRole : {}", id);
        Optional<TurbineRoleDTO> turbineRoleDTO = turbineRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turbineRoleDTO);
    }

    /**
     * DELETE  /turbine-roles/:id : delete the "id" turbineRole.
     *
     * @param id the id of the turbineRoleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turbine-roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurbineRole(@PathVariable Long id) {
        log.info("REST request to delete TurbineRole : {}", id);
        turbineRoleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @author Carlos Eduardo Rodriguez Perez
     * @param roleName
     * @return
     */
    @GetMapping("/turbine-roles/findByRoleName/{roleName}")
    @Timed
    public ResponseEntity<TurbineRoleDTO> getTurbineRoleByName(@PathVariable String roleName){
        log.info("REST request to get TurbineROle: {}",roleName);
        Optional<TurbineRoleDTO> turbineRoleDTO = turbineRoleService.finRoleName(roleName);
        return  ResponseUtil.wrapOrNotFound(turbineRoleDTO);
    }
}
