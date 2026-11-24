package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseReemplazoPK;
import cl.tutorial.desglose.service.DesgloseReemplazoService;
import cl.tutorial.desglose.service.dto.DesgloseReemplazoDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DesgloseReemplazo.
 */
@RestController
@RequestMapping("/api")
public class DesgloseReemplazoResource {

    private final Logger log = LogManager.getLogger(DesgloseReemplazoResource.class);

    private static final String ENTITY_NAME = "desgloseReemplazo";

    private final DesgloseReemplazoService desgloseReemplazoService;

    public DesgloseReemplazoResource(DesgloseReemplazoService desgloseReemplazoService) {
        this.desgloseReemplazoService = desgloseReemplazoService;
    }

    /**
     * POST  /desglose-reemplazos : Create a new desgloseReemplazo.
     *
     * @param desgloseReemplazoDTO
     * 		the desgloseReemplazoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseReemplazoDTO, or with status 400 (Bad Request) if the desgloseReemplazo has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-reemplazos")
    @Timed
    public ResponseEntity<DesgloseReemplazoDTO> createDesgloseReemplazo(@RequestBody DesgloseReemplazoDTO desgloseReemplazoDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseReemplazo : {}", desgloseReemplazoDTO);
        if (desgloseReemplazoDTO.getCodCargo() != null) {
            throw new BadRequestAlertException("A new desgloseReemplazo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseReemplazoDTO result = desgloseReemplazoService.save(desgloseReemplazoDTO);
        return ResponseEntity.created(new URI("/api/desglose-reemplazos/" + result.getCodCargo())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCargo()))
            .body(result);
    }

    /**
     * PUT  /desglose-reemplazos : Updates an existing desgloseReemplazo.
     *
     * @param desgloseReemplazoDTO
     * 		the desgloseReemplazoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseReemplazoDTO, or with status 400 (Bad Request) if the desgloseReemplazoDTO is not valid, or
     * with status 500 (Internal Server Error) if the desgloseReemplazoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-reemplazos")
    @Timed
    public ResponseEntity<DesgloseReemplazoDTO> updateDesgloseReemplazo(@RequestBody DesgloseReemplazoDTO desgloseReemplazoDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseReemplazo : {}", desgloseReemplazoDTO);
        if (desgloseReemplazoDTO.getCodCargo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseReemplazoDTO result = desgloseReemplazoService.save(desgloseReemplazoDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseReemplazoDTO.getCodCargo())).body(result);
    }

    /**
     * GET  /desglose-reemplazos : get all the desgloseReemplazos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseReemplazos in body
     */
    @GetMapping("/desglose-reemplazos")
    @Timed
    public ResponseEntity<List<DesgloseReemplazoDTO>> getAllDesgloseReemplazos(Pageable pageable) {
        log.info("REST request to get a page of DesgloseReemplazos");
        Page<DesgloseReemplazoDTO> page = desgloseReemplazoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-reemplazos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-reemplazos/:id : get the "id" desgloseReemplazo.
     *
     * @param id
     * 		the id of the desgloseReemplazoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseReemplazoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-reemplazos/{id}")
    @Timed
    public ResponseEntity<DesgloseReemplazoDTO> getDesgloseReemplazo(@PathVariable Long id) {
        DesgloseReemplazoPK id1 = new DesgloseReemplazoPK();
        log.info("REST request to get DesgloseReemplazo : {}", id);
        Optional<DesgloseReemplazoDTO> desgloseReemplazoDTO = desgloseReemplazoService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseReemplazoDTO);
    }

    /**
     * DELETE  /desglose-reemplazos/:id : delete the "id" desgloseReemplazo.
     *
     * @param id
     * 		the id of the desgloseReemplazoDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-reemplazos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseReemplazo(@PathVariable Long id) {
        DesgloseReemplazoPK id1 = new DesgloseReemplazoPK();
        log.info("REST request to delete DesgloseReemplazo : {}", id);
        desgloseReemplazoService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @author carlos eduardo rodriguez perez
     * @param desgloseReemplazoDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/desglose-reemplazos/crear")
    @Timed
    public ResponseEntity<Void> createReemplazo(@RequestBody DesgloseReemplazoDTO desgloseReemplazoDTO){
        log.info("Grabamos un Reemplazo");
        try {
            desgloseReemplazoService.InsertarReemplazo(desgloseReemplazoDTO);
            return new ResponseEntity<>(HttpStatus.OK);
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
     * @author carlos eduardo rodriguez perez
     * @param listaDesgloseReemplazoDTO
     * @return
     */
    @PutMapping("/desglose-reemplazos/update")
    @Timed
    public ResponseEntity<Void> UpdateReemplazo(@RequestBody List<DesgloseReemplazoDTO> listaDesgloseReemplazoDTO){
        log.info("Grabamos un Reemplazo");
        try {
            for (DesgloseReemplazoDTO item:listaDesgloseReemplazoDTO) {
                desgloseReemplazoService.UpdateReemplazo(item);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }
}
