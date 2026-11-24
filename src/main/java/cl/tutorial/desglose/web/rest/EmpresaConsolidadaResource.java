package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.EmpresaConsolidadaPK;
import cl.tutorial.desglose.service.EmpresaConsolidadaService;
import cl.tutorial.desglose.service.dto.EmpresaConsolidadaDTO;
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
 * REST controller for managing EmpresaConsolidada.
 */
@RestController
@RequestMapping("/api")
public class EmpresaConsolidadaResource {

    private final Logger log = LogManager.getLogger(EmpresaConsolidadaResource.class);

    private static final String ENTITY_NAME = "empresaConsolidada";

    private final EmpresaConsolidadaService empresaConsolidadaService;

    public EmpresaConsolidadaResource(EmpresaConsolidadaService empresaConsolidadaService) {
        this.empresaConsolidadaService = empresaConsolidadaService;
    }

    /**
     * POST  /empresa-consolidadas : Create a new empresaConsolidada.
     *
     * @param empresaConsolidadaDTO
     * 		the empresaConsolidadaDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new empresaConsolidadaDTO, or with status 400 (Bad Request) if the empresaConsolidada has already an
     * ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/empresa-consolidadas")
    @Timed
    public ResponseEntity<EmpresaConsolidadaDTO> createEmpresaConsolidada(@Valid @RequestBody EmpresaConsolidadaDTO empresaConsolidadaDTO) throws URISyntaxException {
        log.info("REST request to save EmpresaConsolidada : {}", empresaConsolidadaDTO);
        if (empresaConsolidadaDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new empresaConsolidada cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmpresaConsolidadaDTO result = empresaConsolidadaService.save(empresaConsolidadaDTO);
        return ResponseEntity.created(new URI("/api/empresa-consolidadas/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /empresa-consolidadas : Updates an existing empresaConsolidada.
     *
     * @param empresaConsolidadaDTO
     * 		the empresaConsolidadaDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated empresaConsolidadaDTO, or with status 400 (Bad Request) if the empresaConsolidadaDTO is not valid,
     * or with status 500 (Internal Server Error) if the empresaConsolidadaDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/empresa-consolidadas")
    @Timed
    public ResponseEntity<EmpresaConsolidadaDTO> updateEmpresaConsolidada(@Valid @RequestBody EmpresaConsolidadaDTO empresaConsolidadaDTO) throws URISyntaxException {
        log.info("REST request to update EmpresaConsolidada : {}", empresaConsolidadaDTO);
        if (empresaConsolidadaDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmpresaConsolidadaDTO result = empresaConsolidadaService.save(empresaConsolidadaDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, empresaConsolidadaDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /empresa-consolidadas : get all the empresaConsolidadas.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of empresaConsolidadas in body
     */
    @GetMapping("/empresa-consolidadas")
    @Timed
    public ResponseEntity<List<EmpresaConsolidadaDTO>> getAllEmpresaConsolidadas(Pageable pageable) {
        log.info("REST request to get a page of EmpresaConsolidadas");
        Page<EmpresaConsolidadaDTO> page = empresaConsolidadaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/empresa-consolidadas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /empresa-consolidadas/:id : get the "id" empresaConsolidada.
     *
     * @param id
     * 		the id of the empresaConsolidadaDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the empresaConsolidadaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/empresa-consolidadas/{id}")
    @Timed
    public ResponseEntity<EmpresaConsolidadaDTO> getEmpresaConsolidada(@PathVariable Long id) {
        EmpresaConsolidadaPK id1 = new EmpresaConsolidadaPK();
        log.info("REST request to get EmpresaConsolidada : {}", id);
        Optional<EmpresaConsolidadaDTO> empresaConsolidadaDTO = empresaConsolidadaService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(empresaConsolidadaDTO);
    }

    /**
     * DELETE  /empresa-consolidadas/:id : delete the "id" empresaConsolidada.
     *
     * @param id
     * 		the id of the empresaConsolidadaDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/empresa-consolidadas/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmpresaConsolidada(@PathVariable Long id) {
        EmpresaConsolidadaPK id1 = new EmpresaConsolidadaPK();
        log.info("REST request to delete EmpresaConsolidada : {}", id);
        empresaConsolidadaService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
