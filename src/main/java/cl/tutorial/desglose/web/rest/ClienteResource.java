package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.ClienteService;
import cl.tutorial.desglose.service.dto.ClienteDTO;
import cl.tutorial.desglose.web.rest.errors.InternalServerErrorException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Cliente.
 */
@RestController
@RequestMapping("/api")
public class ClienteResource {

    private final Logger log = LogManager.getLogger(ClienteResource.class);

    private static final String ENTITY_NAME = "newClient";

    private final ClienteService clienteService;

    public ClienteResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * GET  /cliente/findByRut : search for clients with the given rut.
     *
     * @param rut
     * 		- The rut of the cliente to retrieve
     * @param codTipoDes
     * 		- Código tipo de desglose
     * @param codSubTipoDes
     * 		- Código sub tipo de desglose
     * @param codMoneda
     * 		- Código de moneda
     * @param codTipoPlantillaDes
     * 		- Código de plantilla
     *
     * @return the ResponseEntity with status 200 (OK) and with body the list of clienteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cliente/findByRut")
    @Timed
    public ResponseEntity<List<ClienteDTO>> findByRut(@RequestParam(name = "rut") String rut, @RequestParam(name = "codTipoDes") Integer codTipoDes,
        @RequestParam(name = "codSubTipoDes") Integer codSubTipoDes, @RequestParam(name = "codMoneda") String codMoneda,
        @RequestParam(name = "codTipoPlantillaDes") Integer codTipoPlantillaDes) {
        log.info("REST request to get Cliente with rut: {}, codTipoDes: {}, codSubTipoDes: {}, codMoneda: {}, codTipoPlantillaDes: {}", rut, codTipoDes, codSubTipoDes, codMoneda,
            codTipoPlantillaDes);
        List<ClienteDTO> clienteDTO = clienteService.findByRut(rut, codTipoDes, codSubTipoDes, codMoneda, codTipoPlantillaDes,null);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    /**
     * GET  /cliente/findByName : search for clients with the given name.
     *
     * @param name
     * 		the name of the cliente to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the list of clienteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cliente/findByName")
    @Timed
    public ResponseEntity<List<ClienteDTO>> findByName(@RequestParam(name = "name") String name, @RequestParam(name = "codTipoDes") Integer codTipoDes,
        @RequestParam(name = "codSubTipoDes") Integer codSubTipoDes, @RequestParam(name = "codMoneda") String codMoneda,
        @RequestParam(name = "codTipoPlantillaDes") Integer codTipoPlantillaDes) {
        log.info("REST request to get Cliente with name like: {}", name);
        List<ClienteDTO> clienteDTO = clienteService.findByName(name, codTipoDes, codSubTipoDes, codMoneda, codTipoPlantillaDes);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    /**
     * POST  /cliente/save : Create a new cliente.
     *
     * @param clienteDTO
     * 		the clienteDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new clienteDTO, or with status 400 (Bad Request) if the cliente has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/cliente/save")
    @Timed
    public ResponseEntity<ClienteDTO> createClienteBasico(@Valid @RequestBody ClienteDTO clienteDTO) throws URISyntaxException {
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        log.info("REST request to save Cliente : {} and username: {}", clienteDTO, userLogin);
        /*if (clienteDTO.getNumCliente() != null) {
            throw new BadRequestAlertException("A new clienteBasico cannot already have an ID", ENTITY_NAME, "idexists");
        }*/
        ClienteDTO result = clienteService.saveClient(clienteDTO, userLogin);
        String clientId = "";
        return ResponseEntity.created(new URI("/api/cliente/save/" + clientId)).headers(HeaderUtil.createEntityCreationAlertCustom(ENTITY_NAME, clientId)).body(result);
    }
}
