package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.IdentificacionClientePK;
import cl.tutorial.desglose.service.IdentificacionClienteService;
import cl.tutorial.desglose.service.dto.IdentificacionClienteDTO;
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
 * REST controller for managing IdentificacionCliente.
 */
@RestController
@RequestMapping("/api")
public class IdentificacionClienteResource {

    private final Logger log = LogManager.getLogger(IdentificacionClienteResource.class);

    private static final String ENTITY_NAME = "identificacionCliente";

    private final IdentificacionClienteService identificacionClienteService;

    public IdentificacionClienteResource(IdentificacionClienteService identificacionClienteService) {
        this.identificacionClienteService = identificacionClienteService;
    }

    /**
     * POST  /identificacion-clientes : Create a new identificacionCliente.
     *
     * @param identificacionClienteDTO
     * 		the identificacionClienteDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new identificacionClienteDTO, or with status 400 (Bad Request) if the identificacionCliente has
     * already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/identificacion-clientes")
    @Timed
    public ResponseEntity<IdentificacionClienteDTO> createIdentificacionCliente(@Valid @RequestBody IdentificacionClienteDTO identificacionClienteDTO) throws URISyntaxException {
        log.info("REST request to save IdentificacionCliente : {}", identificacionClienteDTO);
        if (identificacionClienteDTO.getNumCliente() != null) {
            throw new BadRequestAlertException("A new identificacionCliente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IdentificacionClienteDTO result = identificacionClienteService.save(identificacionClienteDTO);
        return ResponseEntity.created(new URI("/api/identificacion-clientes/" + result.getNumCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNumCliente().toString())).body(result);
    }

    /**
     * PUT  /identificacion-clientes : Updates an existing identificacionCliente.
     *
     * @param identificacionClienteDTO
     * 		the identificacionClienteDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated identificacionClienteDTO, or with status 400 (Bad Request) if the identificacionClienteDTO is not
     * valid, or with status 500 (Internal Server Error) if the identificacionClienteDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/identificacion-clientes")
    @Timed
    public ResponseEntity<IdentificacionClienteDTO> updateIdentificacionCliente(@Valid @RequestBody IdentificacionClienteDTO identificacionClienteDTO) throws URISyntaxException {
        log.info("REST request to update IdentificacionCliente : {}", identificacionClienteDTO);
        if (identificacionClienteDTO.getNumCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IdentificacionClienteDTO result = identificacionClienteService.save(identificacionClienteDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, identificacionClienteDTO.getNumCliente().toString())).body(result);
    }

    /**
     * GET  /identificacion-clientes : get all the identificacionClientes.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of identificacionClientes in body
     */
    @GetMapping("/identificacion-clientes")
    @Timed
    public ResponseEntity<List<IdentificacionClienteDTO>> getAllIdentificacionClientes(Pageable pageable) {
        log.info("REST request to get a page of IdentificacionClientes");
        Page<IdentificacionClienteDTO> page = identificacionClienteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/identificacion-clientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /identificacion-clientes/:id : get the "id" identificacionCliente.
     *
     * @param id
     * 		the id of the identificacionClienteDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the identificacionClienteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/identificacion-clientes/{id}")
    @Timed
    public ResponseEntity<IdentificacionClienteDTO> getIdentificacionCliente(@PathVariable Long id) {
        IdentificacionClientePK id1 = new IdentificacionClientePK();
        log.debug("REST request to get IdentificacionCliente : {}", id);
        Optional<IdentificacionClienteDTO> identificacionClienteDTO = identificacionClienteService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(identificacionClienteDTO);
    }

    /**
     * DELETE  /identificacion-clientes/:id : delete the "id" identificacionCliente.
     *
     * @param id
     * 		the id of the identificacionClienteDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/identificacion-clientes/{id}")
    @Timed
    public ResponseEntity<Void> deleteIdentificacionCliente(@PathVariable Long id) {
        IdentificacionClientePK id1 = new IdentificacionClientePK();
        log.info("REST request to delete IdentificacionCliente : {}", id);
        identificacionClienteService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
