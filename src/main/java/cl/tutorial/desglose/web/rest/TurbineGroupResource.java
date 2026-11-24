package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.TurbineGroupService;
import cl.tutorial.desglose.service.dto.TurbineGroupDTO;
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
 * REST controller for managing TurbineGroup.
 */
@RestController
@RequestMapping("/api")
public class TurbineGroupResource {

    private final Logger log = LogManager.getLogger(TurbineGroupResource.class);

    private static final String ENTITY_NAME = "turbineGroup";

    private final TurbineGroupService turbineGroupService;

    public TurbineGroupResource(TurbineGroupService turbineGroupService) {
        this.turbineGroupService = turbineGroupService;
    }

    /**
     * POST  /turbine-groups : Create a new turbineGroup.
     *
     * @param turbineGroupDTO the turbineGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new turbineGroupDTO, or with status 400 (Bad Request) if the turbineGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/turbine-groups")
    @Timed
    public ResponseEntity<TurbineGroupDTO> createTurbineGroup(@Valid @RequestBody TurbineGroupDTO turbineGroupDTO) throws URISyntaxException {
        log.info("REST request to save TurbineGroup : {}", turbineGroupDTO);
        if (turbineGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new turbineGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurbineGroupDTO result = turbineGroupService.save(turbineGroupDTO);
        return ResponseEntity.created(new URI("/api/turbine-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turbine-groups : Updates an existing turbineGroup.
     *
     * @param turbineGroupDTO the turbineGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated turbineGroupDTO,
     * or with status 400 (Bad Request) if the turbineGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the turbineGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/turbine-groups")
    @Timed
    public ResponseEntity<TurbineGroupDTO> updateTurbineGroup(@Valid @RequestBody TurbineGroupDTO turbineGroupDTO) throws URISyntaxException {
        log.info("REST request to update TurbineGroup : {}", turbineGroupDTO);
        if (turbineGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurbineGroupDTO result = turbineGroupService.save(turbineGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turbineGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /turbine-groups : get all the turbineGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of turbineGroups in body
     */
    @GetMapping("/turbine-groups")
    @Timed
    public ResponseEntity<List<TurbineGroupDTO>> getAllTurbineGroups(Pageable pageable) {
        log.info("REST request to get a page of TurbineGroups");
        Page<TurbineGroupDTO> page = turbineGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turbine-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /turbine-groups/:id : get the "id" turbineGroup.
     *
     * @param id the id of the turbineGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the turbineGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turbine-groups/{id}")
    @Timed
    public ResponseEntity<TurbineGroupDTO> getTurbineGroup(@PathVariable Long id) {
        log.info("REST request to get TurbineGroup : {}", id);
        Optional<TurbineGroupDTO> turbineGroupDTO = turbineGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turbineGroupDTO);
    }

    /**
     * DELETE  /turbine-groups/:id : delete the "id" turbineGroup.
     *
     * @param id the id of the turbineGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turbine-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurbineGroup(@PathVariable Long id) {
        log.info("REST request to delete TurbineGroup : {}", id);
        turbineGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
