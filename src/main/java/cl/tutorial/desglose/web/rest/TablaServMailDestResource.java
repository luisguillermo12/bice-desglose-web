package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.TablaServicioMailDestPK;
import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.TablaServMailDestService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.TablaServMailDestDTO;
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
 * REST controller for managing TablaServMailDest.
 */
@RestController
@RequestMapping("/api")
public class TablaServMailDestResource {

    private final Logger log = LogManager.getLogger(TablaServMailDestResource.class);

    private static final String ENTITY_NAME = "tablaServMailDest";

    private final TablaServMailDestService tablaServMailDestService;

    public TablaServMailDestResource(TablaServMailDestService tablaServMailDestService) {
        this.tablaServMailDestService = tablaServMailDestService;
    }

    /**
     * POST  /tabla-serv-mail-dests : Create a new tablaServMailDest.
     *
     * @param tablaServMailDestDTO the tablaServMailDestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tablaServMailDestDTO, or with status 400 (Bad Request) if the tablaServMailDest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tabla-serv-mail-dests")
    @Timed
    public ResponseEntity<TablaServMailDestDTO> createTablaServMailDest(@Valid @RequestBody TablaServMailDestDTO tablaServMailDestDTO) throws URISyntaxException {
        log.info("REST request to save TablaServMailDest : {}", tablaServMailDestDTO);
        if (tablaServMailDestDTO.getNumSolicitud() != null) {
            throw new BadRequestAlertException("A new tablaServMailDest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TablaServMailDestDTO result = tablaServMailDestService.save(tablaServMailDestDTO);
        return ResponseEntity.created(new URI("/api/tabla-serv-mail-dests/" + result.getNumSolicitud()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNumSolicitud().toString()))
            .body(result);
    }

    /**
     * PUT  /tabla-serv-mail-dests : Updates an existing tablaServMailDest.
     *
     * @param tablaServMailDestDTO the tablaServMailDestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tablaServMailDestDTO,
     * or with status 400 (Bad Request) if the tablaServMailDestDTO is not valid,
     * or with status 500 (Internal Server Error) if the tablaServMailDestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tabla-serv-mail-dests")
    @Timed
    public ResponseEntity<TablaServMailDestDTO> updateTablaServMailDest(@Valid @RequestBody TablaServMailDestDTO tablaServMailDestDTO) throws URISyntaxException {
        log.info("REST request to update TablaServMailDest : {}", tablaServMailDestDTO);
        if (tablaServMailDestDTO.getNumSolicitud() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TablaServMailDestDTO result = tablaServMailDestService.save(tablaServMailDestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tablaServMailDestDTO.getNumSolicitud().toString()))
            .body(result);
    }

    /**
     * GET  /tabla-serv-mail-dests : get all the tablaServMailDests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tablaServMailDests in body
     */
    @GetMapping("/tabla-serv-mail-dests")
    @Timed
    public ResponseEntity<List<TablaServMailDestDTO>> getAllTablaServMailDests(Pageable pageable) {
        log.info("REST request to get a page of TablaServMailDests");
        Page<TablaServMailDestDTO> page = tablaServMailDestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tabla-serv-mail-dests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tabla-serv-mail-dests/:id : get the "id" tablaServMailDest.
     *
     * @param id the id of the tablaServMailDestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tablaServMailDestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tabla-serv-mail-dests/{id}")
    @Timed
    public ResponseEntity<TablaServMailDestDTO> getTablaServMailDest(@PathVariable Long id) {
        log.info("REST request to get TablaServMailDest : {}", id);
        TablaServicioMailDestPK tablaServicioMailDestPK = new TablaServicioMailDestPK();
        Optional<TablaServMailDestDTO> tablaServMailDestDTO = tablaServMailDestService.findOne(tablaServicioMailDestPK);
        return ResponseUtil.wrapOrNotFound(tablaServMailDestDTO);
    }

    /**
     * DELETE  /tabla-serv-mail-dests/:id : delete the "id" tablaServMailDest.
     *
     * @param id the id of the tablaServMailDestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tabla-serv-mail-dests/{id}")
    @Timed
    public ResponseEntity<Void> deleteTablaServMailDest(@PathVariable Long id) {
        log.info("REST request to delete TablaServMailDest : {}", id);
        TablaServicioMailDestPK tablaServicioMailDestPK = new TablaServicioMailDestPK();
        tablaServMailDestService.delete(tablaServicioMailDestPK);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
