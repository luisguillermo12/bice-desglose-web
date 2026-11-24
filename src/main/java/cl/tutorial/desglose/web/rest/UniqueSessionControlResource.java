package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.UniqueSessionControlService;
import cl.tutorial.desglose.service.dto.UniqueSessionControlDTO;
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
 * REST controller for managing UniqueSessionControl.
 */
@RestController
@RequestMapping("/api")
public class UniqueSessionControlResource {

    private final Logger log = LogManager.getLogger(UniqueSessionControlResource.class);

    private static final String ENTITY_NAME = "uniqueSessionControl";

    private final UniqueSessionControlService uniqueSessionControlService;

    public UniqueSessionControlResource(UniqueSessionControlService uniqueSessionControlService) {
        this.uniqueSessionControlService = uniqueSessionControlService;
    }

    /**
     * POST  /unique-session-controls : Create a new uniqueSessionControl.
     *
     * @param uniqueSessionControlDTO
     * 		the uniqueSessionControlDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new uniqueSessionControlDTO, or with status 400 (Bad Request) if the uniqueSessionControl has already
     * an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/unique-session-controls")
    @Timed
    public ResponseEntity<UniqueSessionControlDTO> createUniqueSessionControl(@Valid @RequestBody UniqueSessionControlDTO uniqueSessionControlDTO) throws URISyntaxException {
        log.info("REST request to save UniqueSessionControl : {}", uniqueSessionControlDTO);
        if (uniqueSessionControlDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniqueSessionControl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UniqueSessionControlDTO result = uniqueSessionControlService.save(uniqueSessionControlDTO);
        return ResponseEntity.created(new URI("/api/unique-session-controls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT  /unique-session-controls : Updates an existing uniqueSessionControl.
     *
     * @param uniqueSessionControlDTO
     * 		the uniqueSessionControlDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated uniqueSessionControlDTO, or with status 400 (Bad Request) if the uniqueSessionControlDTO is not
     * valid, or with status 500 (Internal Server Error) if the uniqueSessionControlDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/unique-session-controls")
    @Timed
    public ResponseEntity<UniqueSessionControlDTO> updateUniqueSessionControl(@Valid @RequestBody UniqueSessionControlDTO uniqueSessionControlDTO) throws URISyntaxException {
        log.info("REST request to update UniqueSessionControl : {}", uniqueSessionControlDTO);
        if (uniqueSessionControlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UniqueSessionControlDTO result = uniqueSessionControlService.save(uniqueSessionControlDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, uniqueSessionControlDTO.getId().toString())).body(result);
    }

    /**
     * GET  /unique-session-controls : get all the uniqueSessionControls.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of uniqueSessionControls in body
     */
    @GetMapping("/unique-session-controls")
    @Timed
    public ResponseEntity<List<UniqueSessionControlDTO>> getAllUniqueSessionControls(Pageable pageable) {
        log.info("REST request to get a page of UniqueSessionControls");
        Page<UniqueSessionControlDTO> page = uniqueSessionControlService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unique-session-controls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /unique-session-controls/:id : get the "id" uniqueSessionControl.
     *
     * @param id
     * 		the id of the uniqueSessionControlDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uniqueSessionControlDTO, or with status 404 (Not Found)
     */
    @GetMapping("/unique-session-controls/{id}")
    @Timed
    public ResponseEntity<UniqueSessionControlDTO> getUniqueSessionControl(@PathVariable Long id) {
        log.info("REST request to get UniqueSessionControl : {}", id);
        Optional<UniqueSessionControlDTO> uniqueSessionControlDTO = uniqueSessionControlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniqueSessionControlDTO);
    }

    /**
     * DELETE  /unique-session-controls/:id : delete the "id" uniqueSessionControl.
     *
     * @param id
     * 		the id of the uniqueSessionControlDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/unique-session-controls/{id}")
    @Timed
    public ResponseEntity<Void> deleteUniqueSessionControl(@PathVariable Long id) {
        log.info("REST request to delete UniqueSessionControl : {}", id);
        uniqueSessionControlService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /unique-session-controls-delete-list : Elimina una lista de uniqueSessionControl.
     *
     * @param selectedItemsIds
     * 		the list of uniqueSessionControl´s id to delete
     *
     * @return he ResponseEntity with status 200 (OK)
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/unique-session-controls/delete-list")
    @Timed
    public ResponseEntity<Void> deleteListUniqueSessionControl(@Valid @RequestBody List<Long> selectedItemsIds) throws URISyntaxException {
        log.info("REST request to delete a list of UniqueSessionControl : {}", selectedItemsIds);
        /*if (selectedItemsIds == null || selectedItemsIds.size() <= 0) {
            throw new BadRequestAlertException("Se requiere al menos un elemento para eliminar");
        }*/
        uniqueSessionControlService.deleteByList(selectedItemsIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityCustomDeleteAlert(ENTITY_NAME)).build();
    }

    /**
     * GET  /unique-session-controls : get all the uniqueSessionControls.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of uniqueSessionControls in body
     */
    @GetMapping("/unique-session-controls/get-all")
    @Timed
    public ResponseEntity<List<UniqueSessionControlDTO>> getAllUniqueSessionControls() {
        log.info("REST request to get all of UniqueSessionControls");
        List<UniqueSessionControlDTO> list = uniqueSessionControlService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
