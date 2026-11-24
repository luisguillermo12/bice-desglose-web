package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseFirmaPK;
import cl.tutorial.desglose.service.DesgloseFirmaService;
import cl.tutorial.desglose.service.dto.DesgloseFirmaDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.hibernate.exception.SQLGrammarException;
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
 * REST controller for managing DesgloseFirma.
 */
@RestController
@RequestMapping("/api")
public class DesgloseFirmaResource {

    private final Logger log = LogManager.getLogger(DesgloseFirmaResource.class);

    private static final String ENTITY_NAME = "desgloseFirma";

    private final DesgloseFirmaService desgloseFirmaService;

    public DesgloseFirmaResource(DesgloseFirmaService desgloseFirmaService) {
        this.desgloseFirmaService = desgloseFirmaService;
    }

    /**
     * POST  /desglose-firmas : Create a new desgloseFirma.
     *
     * @param desgloseFirmaDTO
     * 		the desgloseFirmaDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseFirmaDTO, or with status 400 (Bad Request) if the desgloseFirma has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-firmas")
    @Timed
    public ResponseEntity<DesgloseFirmaDTO> createDesgloseFirma(@Valid @RequestBody DesgloseFirmaDTO desgloseFirmaDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseFirma : {}", desgloseFirmaDTO);
        if (desgloseFirmaDTO.getCodCargo() == null) {
            throw new BadRequestAlertException("A new desgloseFirma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseFirmaDTO result = desgloseFirmaService.save(desgloseFirmaDTO);
        return ResponseEntity.created(new URI("/api/desglose-firmas/" + result.getCodCargo())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCargo()))
            .body(result);
    }

    /**
     * PUT  /desglose-firmas : Updates an existing desgloseFirma.
     *
     * @param desgloseFirmaDTO
     * 		the desgloseFirmaDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseFirmaDTO, or with status 400 (Bad Request) if the desgloseFirmaDTO is not valid, or with
     * status 500 (Internal Server Error) if the desgloseFirmaDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-firmas")
    @Timed
    public ResponseEntity<DesgloseFirmaDTO> updateDesgloseFirma(@Valid @RequestBody DesgloseFirmaDTO desgloseFirmaDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseFirma : {}", desgloseFirmaDTO);
        if (desgloseFirmaDTO.getCodCargo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseFirmaDTO result = desgloseFirmaService.save(desgloseFirmaDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseFirmaDTO.getCodCargo())).body(result);
    }

    /**
     * GET  /desglose-firmas : get all the desgloseFirmas.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseFirmas in body
     */
    @GetMapping("/desglose-firmas")
    @Timed
    public ResponseEntity<List<DesgloseFirmaDTO>> getAllDesgloseFirmas(Pageable pageable) {
        log.info("REST request to get a page of DesgloseFirmas");
        Page<DesgloseFirmaDTO> page = desgloseFirmaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-firmas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/desglose-firmas/findAllFirmas")
    public ResponseEntity<List<?>> getAllFirmas() {
        log.info("REST  listado de firmas");
        try {
            List<?> listaFirmas = desgloseFirmaService.getAllFirmas();
            return new ResponseEntity<>(listaFirmas, HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }

    /**
     * GET  /desglose-firmas/:id : get the "id" desgloseFirma.
     *
     * @param id
     * 		the id of the desgloseFirmaDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseFirmaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-firmas/{id}")
    @Timed
    public ResponseEntity<DesgloseFirmaDTO> getDesgloseFirma(@PathVariable Long id) {
        DesgloseFirmaPK id1 = new DesgloseFirmaPK();
        log.info("REST request to get DesgloseFirma : {}", id);
        Optional<DesgloseFirmaDTO> desgloseFirmaDTO = desgloseFirmaService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseFirmaDTO);
    }

    /**
     * @param numRolEmpleado
     * @param numRolEmpleado2Fir
     *
     * @return
     *
     * @author carlos eduardo rodriguez perez
     */
    @GetMapping("/desglose-firmas/validasegundafirma/{numRolEmpleado}/{numRolEmpleado2Fir}")
    @Timed
    public ResponseEntity<Integer> validasegundafirma(@PathVariable String numRolEmpleado, @PathVariable String numRolEmpleado2Fir) {
        log.info("validamos si es 2 firma");
        Integer count = desgloseFirmaService.validasegundafirma(numRolEmpleado, numRolEmpleado2Fir);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * Delete DesgloseFirma
     *
     * @param codCargo
     * @param numRolEmpleado
     * @param numRolEmpleado2Firm
     *
     * @return
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    @DeleteMapping("/desglose-firmas/{codCargo}/{numRolEmpleado}/{numRolEmpleado2Firm}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseFirma(@PathVariable String codCargo, @PathVariable String numRolEmpleado, @PathVariable String numRolEmpleado2Firm) {
        DesgloseFirmaPK id1 = new DesgloseFirmaPK();
        id1.setCodCargo(codCargo);
        id1.setNumRolEmpleado(numRolEmpleado);
        id1.setNumRolEmpleado2fir(numRolEmpleado2Firm);
        log.debug("REST request to delete DesgloseFirma : {}", id1);
        desgloseFirmaService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id1.toString())).build();
    }

    @GetMapping("/desglose-firmas-empleadoAsociado2Firma/{numRolEmpleado2Fir}")
    @Timed
    public ResponseEntity<List<DesgloseFirmaDTO>> findAllByNumRolEmpleado2fir(@PathVariable String numRolEmpleado2Fir) {
        log.info("REST request to findAllByNumRolEmpleado2fir with prams {" + numRolEmpleado2Fir + "}");
        List<DesgloseFirmaDTO> desgloseFirmaDTOList = desgloseFirmaService.findAllByNumRolEmpleado2fir(numRolEmpleado2Fir);
        return new ResponseEntity<>(desgloseFirmaDTOList, HttpStatus.OK);
    }

    @GetMapping("/desglose-firmas-empleadoAsociado/{numRolEmpleado}")
    @Timed
    public ResponseEntity<List<DesgloseFirmaDTO>> findAllByNumRolEmpleado(@PathVariable String numRolEmpleado) {
        log.info("REST request to findAllByNumRolEmpleado with prams {" + numRolEmpleado + "}");
        List<DesgloseFirmaDTO> desgloseFirmaDTOList = desgloseFirmaService.findAllByNumRolEmpleado(numRolEmpleado);
        return new ResponseEntity<>(desgloseFirmaDTOList, HttpStatus.OK);
    }
}
