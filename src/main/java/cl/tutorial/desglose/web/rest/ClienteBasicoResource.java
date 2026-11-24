package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.service.ClienteBasicoService;
import cl.tutorial.desglose.service.dto.ClienteBasicoDTO;
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
 * REST controller for managing ClienteBasico.
 */
@RestController
@RequestMapping("/api")
public class ClienteBasicoResource {

    private final Logger log = LogManager.getLogger(ClienteBasicoResource.class);

    private static final String ENTITY_NAME = "clienteBasico";

    private final ClienteBasicoService clienteBasicoService;

    public ClienteBasicoResource(ClienteBasicoService clienteBasicoService) {
        this.clienteBasicoService = clienteBasicoService;
    }

    /**
     * POST  /cliente-basicos : Create a new clienteBasico.
     *
     * @param clienteBasicoDTO
     * 		the clienteBasicoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new clienteBasicoDTO, or with status 400 (Bad Request) if the clienteBasico has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/cliente-basicos")
    @Timed
    public ResponseEntity<ClienteBasicoDTO> createClienteBasico(@Valid @RequestBody ClienteBasicoDTO clienteBasicoDTO) throws URISyntaxException {
        log.info("REST request to save ClienteBasico : {}", clienteBasicoDTO);
        if (clienteBasicoDTO.getNumCliente() != null) {
            throw new BadRequestAlertException("A new clienteBasico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClienteBasicoDTO result = clienteBasicoService.save(clienteBasicoDTO);
        return ResponseEntity.created(new URI("/api/cliente-basicos/" + result.getNumCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNumCliente().toString())).body(result);
    }

    /**
     * PUT  /cliente-basicos : Updates an existing clienteBasico.
     *
     * @param clienteBasicoDTO
     * 		the clienteBasicoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated clienteBasicoDTO, or with status 400 (Bad Request) if the clienteBasicoDTO is not valid, or with
     * status 500 (Internal Server Error) if the clienteBasicoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/cliente-basicos")
    @Timed
    public ResponseEntity<ClienteBasicoDTO> updateClienteBasico(@Valid @RequestBody ClienteBasicoDTO clienteBasicoDTO) throws URISyntaxException {
        log.info("REST request to update ClienteBasico : {}", clienteBasicoDTO);
        if (clienteBasicoDTO.getNumCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClienteBasicoDTO result = clienteBasicoService.save(clienteBasicoDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clienteBasicoDTO.getNumCliente().toString())).body(result);
    }

    /**
     * GET  /cliente-basicos : get all the clienteBasicos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clienteBasicos in body
     */
    @GetMapping("/cliente-basicos")
    @Timed
    public ResponseEntity<List<ClienteBasicoDTO>> getAllClienteBasicos(Pageable pageable) {
        log.info("REST request to get a page of ClienteBasicos");
        Page<ClienteBasicoDTO> page = clienteBasicoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cliente-basicos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cliente-basicos/:id : get the "id" clienteBasico.
     *
     * @param id
     * 		the id of the clienteBasicoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the clienteBasicoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cliente-basicos/{id}")
    @Timed
    public ResponseEntity<ClienteBasicoDTO> getClienteBasico(@PathVariable Long id) {
        CommonClientePK id1 = new CommonClientePK();
        log.info("REST request to get ClienteBasico : {}", id);
        Optional<ClienteBasicoDTO> clienteBasicoDTO = clienteBasicoService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(clienteBasicoDTO);
    }

    /**
     * DELETE  /cliente-basicos/:id : delete the "id" clienteBasico.
     *
     * @param id
     * 		the id of the clienteBasicoDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cliente-basicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteClienteBasico(@PathVariable Long id) {
        CommonClientePK id1 = new CommonClientePK();
        log.info("REST request to delete ClienteBasico : {}", id);
        clienteBasicoService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
