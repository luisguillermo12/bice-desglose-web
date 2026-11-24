package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.TablaServicioMailPK;
import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.TablaServicioMailService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.TablaServicioMailDTO;
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
 * REST controller for managing TablaServicioMail.
 */
@RestController
@RequestMapping("/api")
public class TablaServicioMailResource {

    private final Logger log = LogManager.getLogger(TablaServicioMailResource.class);

    private static final String ENTITY_NAME = "tablaServicioMail";

    private final TablaServicioMailService tablaServicioMailService;

    public TablaServicioMailResource(TablaServicioMailService tablaServicioMailService) {
        this.tablaServicioMailService = tablaServicioMailService;
    }

    /**
     * POST  /tabla-servicio-mails : Create a new tablaServicioMail.
     *
     * @param tablaServicioMailDTO the tablaServicioMailDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tablaServicioMailDTO, or with status 400 (Bad Request) if the tablaServicioMail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tabla-servicio-mails")
    @Timed
    public ResponseEntity<TablaServicioMailDTO> createTablaServicioMail(@Valid @RequestBody TablaServicioMailDTO tablaServicioMailDTO) throws URISyntaxException {
        log.info("REST request to save TablaServicioMail : {}", tablaServicioMailDTO);
        if (tablaServicioMailDTO.getNumSolicitud() != null) {
            throw new BadRequestAlertException("A new tablaServicioMail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TablaServicioMailDTO result = tablaServicioMailService.save(tablaServicioMailDTO);
        return ResponseEntity.created(new URI("/api/tabla-servicio-mails/" + result.getNumSolicitud()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNumSolicitud().toString()))
            .body(result);
    }

    @PostMapping("/tabla-servicio-mails-send-correos")
    @Timed
    public ResponseEntity<Boolean> sendCorreos(){
        log.info("Envio de correos inicio");
        tablaServicioMailService.enviarCorreos();
        return  new ResponseEntity<>(true,HttpStatus.OK);
    }

    /**
     * PUT  /tabla-servicio-mails : Updates an existing tablaServicioMail.
     *
     * @param tablaServicioMailDTO the tablaServicioMailDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tablaServicioMailDTO,
     * or with status 400 (Bad Request) if the tablaServicioMailDTO is not valid,
     * or with status 500 (Internal Server Error) if the tablaServicioMailDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tabla-servicio-mails")
    @Timed
    public ResponseEntity<TablaServicioMailDTO> updateTablaServicioMail(@Valid @RequestBody TablaServicioMailDTO tablaServicioMailDTO) throws URISyntaxException {
        log.info("REST request to update TablaServicioMail : {}", tablaServicioMailDTO);
        if (tablaServicioMailDTO.getNumSolicitud() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TablaServicioMailDTO result = tablaServicioMailService.save(tablaServicioMailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tablaServicioMailDTO.getNumSolicitud().toString()))
            .body(result);
    }

    /**
     * GET  /tabla-servicio-mails : get all the tablaServicioMails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tablaServicioMails in body
     */
    @GetMapping("/tabla-servicio-mails")
    @Timed
    public ResponseEntity<List<TablaServicioMailDTO>> getAllTablaServicioMails(Pageable pageable) {
        log.info("REST request to get a page of TablaServicioMails");
        Page<TablaServicioMailDTO> page = tablaServicioMailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tabla-servicio-mails");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tabla-servicio-mails/:id : get the "id" tablaServicioMail.
     *
     * @param id the id of the tablaServicioMailDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tablaServicioMailDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tabla-servicio-mails/{id}")
    @Timed
    public ResponseEntity<TablaServicioMailDTO> getTablaServicioMail(@PathVariable Long id) {
        log.info("REST request to get TablaServicioMail : {}", id);
        TablaServicioMailPK tablaServicioMailPK = new TablaServicioMailPK();
        Optional<TablaServicioMailDTO> tablaServicioMailDTO = tablaServicioMailService.findOne(tablaServicioMailPK);
        return ResponseUtil.wrapOrNotFound(tablaServicioMailDTO);
    }

    /**
     * DELETE  /tabla-servicio-mails/:id : delete the "id" tablaServicioMail.
     *
     * @param id the id of the tablaServicioMailDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tabla-servicio-mails/{id}")
    @Timed
    public ResponseEntity<Void> deleteTablaServicioMail(@PathVariable Long id) {
        log.info("REST request to delete TablaServicioMail : {}", id);
        TablaServicioMailPK tablaServicioMailPK = new TablaServicioMailPK();
        tablaServicioMailService.delete(tablaServicioMailPK);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
