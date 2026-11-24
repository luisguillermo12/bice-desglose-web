package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.service.ClienteNaturalService;
import cl.tutorial.desglose.service.dto.ClienteNaturalDTO;
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
 * REST controller for managing ClienteNatural.
 */
@RestController
@RequestMapping("/api")
public class ClienteNaturalResource {

    private final Logger log = LogManager.getLogger(ClienteNaturalResource.class);

    private static final String ENTITY_NAME = "clienteNatural";

    private final ClienteNaturalService clienteNaturalService;

    public ClienteNaturalResource(ClienteNaturalService clienteNaturalService) {
        this.clienteNaturalService = clienteNaturalService;
    }

    /**
     * POST  /cliente-naturals : Create a new clienteNatural.
     *
     * @param clienteNaturalDTO
     * 		the clienteNaturalDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new clienteNaturalDTO, or with status 400 (Bad Request) if the clienteNatural has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/cliente-naturals")
    @Timed
    public ResponseEntity<ClienteNaturalDTO> createClienteNatural(@Valid @RequestBody ClienteNaturalDTO clienteNaturalDTO) throws URISyntaxException {
        log.info("REST request to save ClienteNatural : {}", clienteNaturalDTO);
        if (clienteNaturalDTO.getNumCliente() != null) {
            throw new BadRequestAlertException("A new clienteNatural cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClienteNaturalDTO result = clienteNaturalService.save(clienteNaturalDTO);
        return ResponseEntity.created(new URI("/api/cliente-naturals/" + result.getNumCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNumCliente().toString())).body(result);
    }

    /**
     * PUT  /cliente-naturals : Updates an existing clienteNatural.
     *
     * @param clienteNaturalDTO
     * 		the clienteNaturalDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated clienteNaturalDTO, or with status 400 (Bad Request) if the clienteNaturalDTO is not valid, or with
     * status 500 (Internal Server Error) if the clienteNaturalDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/cliente-naturals")
    @Timed
    public ResponseEntity<ClienteNaturalDTO> updateClienteNatural(@Valid @RequestBody ClienteNaturalDTO clienteNaturalDTO) throws URISyntaxException {
        log.info("REST request to update ClienteNatural : {}", clienteNaturalDTO);
        if (clienteNaturalDTO.getNumCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClienteNaturalDTO result = clienteNaturalService.save(clienteNaturalDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clienteNaturalDTO.getNumCliente().toString())).body(result);
    }

    /**
     * GET  /cliente-naturals : get all the clienteNaturals.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clienteNaturals in body
     */
    @GetMapping("/cliente-naturals")
    @Timed
    public ResponseEntity<List<ClienteNaturalDTO>> getAllClienteNaturals(Pageable pageable) {
        log.info("REST request to get a page of ClienteNaturals");
        Page<ClienteNaturalDTO> page = clienteNaturalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cliente-naturals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cliente-naturals/:id : get the "id" clienteNatural.
     *
     * @param id
     * 		the id of the clienteNaturalDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the clienteNaturalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cliente-naturals/{id}")
    @Timed
    public ResponseEntity<ClienteNaturalDTO> getClienteNatural(@PathVariable Long id) {
        CommonClientePK id1 = new CommonClientePK();
        log.info("REST request to get ClienteNatural : {}", id);
        Optional<ClienteNaturalDTO> clienteNaturalDTO = clienteNaturalService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(clienteNaturalDTO);
    }

    /**
     * DELETE  /cliente-naturals/:id : delete the "id" clienteNatural.
     *
     * @param id
     * 		the id of the clienteNaturalDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cliente-naturals/{id}")
    @Timed
    public ResponseEntity<Void> deleteClienteNatural(@PathVariable Long id) {
        CommonClientePK id1 = new CommonClientePK();
        log.info("REST request to delete ClienteNatural : {}", id);
        clienteNaturalService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
