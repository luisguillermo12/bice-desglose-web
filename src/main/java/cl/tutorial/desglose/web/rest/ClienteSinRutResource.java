package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.ClienteSinRutService;
import cl.tutorial.desglose.service.dto.ClienteSinRutDTO;
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
 * REST controller for managing ClienteSinRut.
 */
@RestController
@RequestMapping("/api")
public class ClienteSinRutResource {

    private final Logger log = LogManager.getLogger(ClienteSinRutResource.class);

    private static final String ENTITY_NAME = "clienteSinRut";

    private final ClienteSinRutService clienteSinRutService;

    public ClienteSinRutResource(ClienteSinRutService clienteSinRutService) {
        this.clienteSinRutService = clienteSinRutService;
    }

    /**
     * POST  /cliente-sin-ruts : Create a new clienteSinRut.
     *
     * @param clienteSinRutDTO
     * 		the clienteSinRutDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new clienteSinRutDTO, or with status 400 (Bad Request) if the clienteSinRut has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/cliente-sin-ruts")
    @Timed
    public ResponseEntity<ClienteSinRutDTO> createClienteSinRut(@Valid @RequestBody ClienteSinRutDTO clienteSinRutDTO) throws URISyntaxException {
        log.info("REST request to save ClienteSinRut : {}", clienteSinRutDTO);
        if (clienteSinRutDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new clienteSinRut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClienteSinRutDTO result = clienteSinRutService.save(clienteSinRutDTO);
        return ResponseEntity.created(new URI("/api/cliente-sin-ruts/" + result.getCodCliente())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente()))
            .body(result);
    }

    /**
     * PUT  /cliente-sin-ruts : Updates an existing clienteSinRut.
     *
     * @param clienteSinRutDTO
     * 		the clienteSinRutDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated clienteSinRutDTO, or with status 400 (Bad Request) if the clienteSinRutDTO is not valid, or with
     * status 500 (Internal Server Error) if the clienteSinRutDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/cliente-sin-ruts")
    @Timed
    public ResponseEntity<ClienteSinRutDTO> updateClienteSinRut(@Valid @RequestBody ClienteSinRutDTO clienteSinRutDTO) throws URISyntaxException {
        log.info("REST request to update ClienteSinRut : {}", clienteSinRutDTO);
        if (clienteSinRutDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClienteSinRutDTO result = clienteSinRutService.save(clienteSinRutDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clienteSinRutDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /cliente-sin-ruts : get all the clienteSinRuts.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clienteSinRuts in body
     */
    @GetMapping("/cliente-sin-ruts")
    @Timed
    public ResponseEntity<List<ClienteSinRutDTO>> getAllClienteSinRuts(Pageable pageable) {
        log.info("REST request to get a page of ClienteSinRuts");
        Page<ClienteSinRutDTO> page = clienteSinRutService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cliente-sin-ruts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return
     *
     * @author carlos eduardo rodriguez perez
     */
    @GetMapping("/cliente-sin-ruts/all")
    @Timed
    public ResponseEntity<List<ClienteSinRutDTO>> getAllClienteSinRuts() {
        log.info("REST request to get a page of ClienteSinRuts");
        List<ClienteSinRutDTO> all = clienteSinRutService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    /**
     * GET  /cliente-sin-ruts/:codCliente : get the "codCliente" clienteSinRut.
     *
     * @param codCliente
     * 		the codCliente of the clienteSinRutDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the clienteSinRutDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cliente-sin-ruts/{codCliente}")
    @Timed
    public ResponseEntity<ClienteSinRutDTO> getClienteSinRut(@PathVariable String codCliente) {
        log.info("REST request to get ClienteSinRut : {}", codCliente);
        Optional<ClienteSinRutDTO> clienteSinRutDTO = clienteSinRutService.findOneByCodCliente(codCliente);
        return ResponseUtil.wrapOrNotFound(clienteSinRutDTO);
    }

    /**
     * DELETE  /cliente-sin-ruts/:codCliente : delete the "codCliente" clienteSinRut.
     *
     * @param codCliente
     * 		the codCliente of the clienteSinRutDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cliente-sin-ruts/{codCliente}")
    @Timed
    public ResponseEntity<Void> deleteClienteSinRut(@PathVariable String codCliente) {
        log.info("REST request to delete ClienteSinRut : {}", codCliente);
        clienteSinRutService.deleteByCodCliente(codCliente);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, codCliente.toString())).build();
    }
}
