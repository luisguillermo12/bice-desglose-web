package cl.tutorial.desglose.web.rest;

import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.TablaValorService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.TablaValorDTO;
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
 * REST controller for managing TablaValor.
 */
@RestController
@RequestMapping("/api")
public class TablaValorResource {

    private final Logger log = LogManager.getLogger(TablaValorResource.class);

    private static final String ENTITY_NAME = "tablaValor";

    private final TablaValorService tablaValorService;

    public TablaValorResource(TablaValorService tablaValorService) {
        this.tablaValorService = tablaValorService;
    }

    /**
     * POST  /tabla-valors : Create a new tablaValor.
     *
     * @param tablaValorDTO
     * 		the tablaValorDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new tablaValorDTO, or with status 400 (Bad Request) if the tablaValor has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/tabla-valors")
    @Timed
    public ResponseEntity<TablaValorDTO> createTablaValor(@Valid @RequestBody TablaValorDTO tablaValorDTO) throws URISyntaxException {
        log.info("REST request to save TablaValor : {}", tablaValorDTO);
        if (tablaValorDTO.getId() != null) {
            throw new BadRequestAlertException("A new tablaValor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TablaValorDTO result = tablaValorService.save(tablaValorDTO);
        return ResponseEntity.created(new URI("/api/tabla-valors/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tabla-valors : Updates an existing tablaValor.
     *
     * @param tablaValorDTO
     * 		the tablaValorDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated tablaValorDTO, or with status 400 (Bad Request) if the tablaValorDTO is not valid, or with status
     * 500 (Internal Server Error) if the tablaValorDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/tabla-valors")
    @Timed
    public ResponseEntity<TablaValorDTO> updateTablaValor(@Valid @RequestBody TablaValorDTO tablaValorDTO) throws URISyntaxException {
        log.info("REST request to update TablaValor : {}", tablaValorDTO);
        if (tablaValorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TablaValorDTO result = tablaValorService.save(tablaValorDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tablaValorDTO.getId().toString())).body(result);
    }

    /**
     * GET  /tabla-valors : get all the tablaValors.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tablaValors in body
     */
    @GetMapping("/tabla-valors")
    @Timed
    public ResponseEntity<List<TablaValorDTO>> getAllTablaValors(Pageable pageable) {
        log.info("REST request to get a page of TablaValors");
        Page<TablaValorDTO> page = tablaValorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tabla-valors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tabla-valors/:id : get the "id" tablaValor.
     *
     * @param id
     * 		the id of the tablaValorDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the tablaValorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tabla-valors/{id}")
    @Timed
    public ResponseEntity<TablaValorDTO> getTablaValor(@PathVariable Long id) {
        log.info("REST request to get TablaValor : {}", id);
        Optional<TablaValorDTO> tablaValorDTO = tablaValorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tablaValorDTO);
    }

    /**
     * DELETE  /tabla-valors/:id : delete the "id" tablaValor.
     *
     * @param id
     * 		the id of the tablaValorDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tabla-valors/{id}")
    @Timed
    public ResponseEntity<Void> deleteTablaValor(@PathVariable Long id) {
        log.info("REST request to delete TablaValor : {}", id);
        tablaValorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @param codTabla
     * @param codRegStatus
     *
     * @return
     *
     * @author carlos eduardo rodriguez perez
     */
    @GetMapping("/tabla-valors/findByCodTablaAndCodRegStatus/{codTabla}/{codRegStatus}")
    @Timed
    public ResponseEntity<List<TablaValorDTO>> findByCodTablaAndCodRegStatus(@PathVariable String codTabla, @PathVariable Integer codRegStatus) {
        log.info("Listamos TablaValor buscando por codigo de tabla y status");
        List<TablaValorDTO> lista = tablaValorService.findByCodTablaAndCodRegStatus(codTabla, codRegStatus);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/tabla-valors/get-iva-value")
    @Timed
    public ResponseEntity<TablaValorDTO> getIvaValue() {
        log.info("REST request to get a TablaValorDTO with the last IVA value");
        TablaValorDTO page = tablaValorService.getIvaValue();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
