package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.service.ClienteJuridicoService;
import cl.tutorial.desglose.service.dto.ClienteJuridicoDTO;
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
 * REST controller for managing ClienteJuridico.
 */
@RestController
@RequestMapping("/api")
public class ClienteJuridicoResource {

    private final Logger log = LogManager.getLogger(ClienteJuridicoResource.class);

    private static final String ENTITY_NAME = "clienteJuridico";

    private final ClienteJuridicoService clienteJuridicoService;

    public ClienteJuridicoResource(ClienteJuridicoService clienteJuridicoService) {
        this.clienteJuridicoService = clienteJuridicoService;
    }

    /**
     * POST  /cliente-juridicos : Create a new clienteJuridico.
     *
     * @param clienteJuridicoDTO
     * 		the clienteJuridicoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new clienteJuridicoDTO, or with status 400 (Bad Request) if the clienteJuridico has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/cliente-juridicos")
    @Timed
    public ResponseEntity<ClienteJuridicoDTO> createClienteJuridico(@Valid @RequestBody ClienteJuridicoDTO clienteJuridicoDTO) throws URISyntaxException {
        log.info("REST request to save ClienteJuridico : {}", clienteJuridicoDTO);
        if (clienteJuridicoDTO.getNumCliente() != null) {
            throw new BadRequestAlertException("A new clienteJuridico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClienteJuridicoDTO result = clienteJuridicoService.save(clienteJuridicoDTO);
        return ResponseEntity.created(new URI("/api/cliente-juridicos/" + result.getNumCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNumCliente().toString())).body(result);
    }

    /**
     * PUT  /cliente-juridicos : Updates an existing clienteJuridico.
     *
     * @param clienteJuridicoDTO
     * 		the clienteJuridicoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated clienteJuridicoDTO, or with status 400 (Bad Request) if the clienteJuridicoDTO is not valid, or
     * with status 500 (Internal Server Error) if the clienteJuridicoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/cliente-juridicos")
    @Timed
    public ResponseEntity<ClienteJuridicoDTO> updateClienteJuridico(@Valid @RequestBody ClienteJuridicoDTO clienteJuridicoDTO) throws URISyntaxException {
        log.info("REST request to update ClienteJuridico : {}", clienteJuridicoDTO);
        if (clienteJuridicoDTO.getNumCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClienteJuridicoDTO result = clienteJuridicoService.save(clienteJuridicoDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clienteJuridicoDTO.getNumCliente().toString())).body(result);
    }

    /**
     * GET  /cliente-juridicos : get all the clienteJuridicos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clienteJuridicos in body
     */
    @GetMapping("/cliente-juridicos")
    @Timed
    public ResponseEntity<List<ClienteJuridicoDTO>> getAllClienteJuridicos(Pageable pageable) {
        log.info("REST request to get a page of ClienteJuridicos");
        Page<ClienteJuridicoDTO> page = clienteJuridicoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cliente-juridicos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cliente-juridicos/:id : get the "id" clienteJuridico.
     *
     * @param id
     * 		the id of the clienteJuridicoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the clienteJuridicoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cliente-juridicos/{id}")
    @Timed
    public ResponseEntity<ClienteJuridicoDTO> getClienteJuridico(@PathVariable Long id) {
        CommonClientePK id1 = new CommonClientePK();
        log.info("REST request to get ClienteJuridico : {}", id);
        Optional<ClienteJuridicoDTO> clienteJuridicoDTO = clienteJuridicoService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(clienteJuridicoDTO);
    }

    /**
     * DELETE  /cliente-juridicos/:id : delete the "id" clienteJuridico.
     *
     * @param id
     * 		the id of the clienteJuridicoDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cliente-juridicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteClienteJuridico(@PathVariable Long id) {
        CommonClientePK id1 = new CommonClientePK();
        log.info("REST request to delete ClienteJuridico : {}", id);
        clienteJuridicoService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
