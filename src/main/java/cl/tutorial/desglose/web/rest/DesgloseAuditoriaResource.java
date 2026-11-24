package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseAuditoriaPK;
import cl.tutorial.desglose.service.DesgloseAuditoriaService;
import cl.tutorial.desglose.service.dto.DesgloseAuditoriaDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.ObjectUtils;
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
 * REST controller for managing DesgloseAuditoria.
 */
@RestController
@RequestMapping("/api")
public class DesgloseAuditoriaResource {

    private final Logger log = LogManager.getLogger(DesgloseAuditoriaResource.class);

    private static final String ENTITY_NAME = "desgloseAuditoria";

    private final DesgloseAuditoriaService desgloseAuditoriaService;

    public DesgloseAuditoriaResource(DesgloseAuditoriaService desgloseAuditoriaService) {
        this.desgloseAuditoriaService = desgloseAuditoriaService;
    }

    /**
     * POST  /desglose-auditorias : Create a new desgloseAuditoria.
     *
     * @param desgloseAuditoriaDTO
     * 		the desgloseAuditoriaDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseAuditoriaDTO, or with status 400 (Bad Request) if the desgloseAuditoria has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-auditorias")
    @Timed
    public ResponseEntity<DesgloseAuditoriaDTO> createDesgloseAuditoria(@Valid @RequestBody DesgloseAuditoriaDTO desgloseAuditoriaDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseAuditoria : {}", desgloseAuditoriaDTO);
        if (!ObjectUtils
            .allNotNull(desgloseAuditoriaDTO.getCodCliente(), desgloseAuditoriaDTO.getCodTipoDes(), desgloseAuditoriaDTO.getCodSubtipoDes(), desgloseAuditoriaDTO.getCodMoneda(),
                desgloseAuditoriaDTO.getFecEvento(), desgloseAuditoriaDTO.getNumLinea())) {
            throw new BadRequestAlertException("A new cuadre must have a valid ID", ENTITY_NAME, "notValidId");
        }

        DesgloseAuditoriaPK id = new DesgloseAuditoriaPK();
        id.setCodCliente(desgloseAuditoriaDTO.getCodCliente());
        id.setCodTipoDes(desgloseAuditoriaDTO.getCodTipoDes());
        id.setCodSubtipoDes(desgloseAuditoriaDTO.getCodSubtipoDes());
        id.setCodMoneda(desgloseAuditoriaDTO.getCodMoneda());
        id.setFecEvento(desgloseAuditoriaDTO.getFecEvento());
        id.setNumLinea(desgloseAuditoriaDTO.getNumLinea());
        if (desgloseAuditoriaService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("A new DesgloseAuditoria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseAuditoriaDTO result = desgloseAuditoriaService.save(desgloseAuditoriaDTO);
        final String entityId = HeaderUtil
            .createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes().toString(), result.getCodSubtipoDes().toString(), result.getCodMoneda(),
                result.getFecEvento().toString(), result.getNumLinea().toString());
        return ResponseEntity.created(new URI(
            "/api/desglose-auditorias/" + result.getCodCliente() + "/" + result.getCodTipoDes().toString() + "/" + result.getCodSubtipoDes().toString() + "/" + result
                .getCodMoneda() + result.getFecEvento() + "/" + result.getNumLinea().toString())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * PUT  /desglose-auditorias : Updates an existing desgloseAuditoria.
     *
     * @param desgloseAuditoriaDTO
     * 		the desgloseAuditoriaDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseAuditoriaDTO, or with status 400 (Bad Request) if the desgloseAuditoriaDTO is not valid, or
     * with status 500 (Internal Server Error) if the desgloseAuditoriaDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-auditorias")
    @Timed
    public ResponseEntity<DesgloseAuditoriaDTO> updateDesgloseAuditoria(@Valid @RequestBody DesgloseAuditoriaDTO desgloseAuditoriaDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseAuditoria : {}", desgloseAuditoriaDTO);
        if (!ObjectUtils
            .allNotNull(desgloseAuditoriaDTO.getCodCliente(), desgloseAuditoriaDTO.getCodTipoDes(), desgloseAuditoriaDTO.getCodSubtipoDes(), desgloseAuditoriaDTO.getCodMoneda(),
                desgloseAuditoriaDTO.getFecEvento(), desgloseAuditoriaDTO.getNumLinea())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        DesgloseAuditoriaPK id = new DesgloseAuditoriaPK();
        id.setCodCliente(desgloseAuditoriaDTO.getCodCliente());
        id.setCodTipoDes(desgloseAuditoriaDTO.getCodTipoDes());
        id.setCodSubtipoDes(desgloseAuditoriaDTO.getCodSubtipoDes());
        id.setCodMoneda(desgloseAuditoriaDTO.getCodMoneda());
        id.setFecEvento(desgloseAuditoriaDTO.getFecEvento());
        id.setNumLinea(desgloseAuditoriaDTO.getNumLinea());
        if (!desgloseAuditoriaService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Invalid id. Entity not found", ENTITY_NAME, "idNotFound");
        }
        DesgloseAuditoriaDTO result = desgloseAuditoriaService.save(desgloseAuditoriaDTO);
        final String entityId = HeaderUtil
            .createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes().toString(), result.getCodSubtipoDes().toString(), result.getCodMoneda(),
                result.getFecEvento().toString(), result.getNumLinea().toString());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * GET  /desglose-auditorias : get all the desgloseAuditorias.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseAuditorias in body
     */
    @GetMapping("/desglose-auditorias")
    @Timed
    public ResponseEntity<List<DesgloseAuditoriaDTO>> getAllDesgloseAuditorias(Pageable pageable) {
        log.info("REST request to get a page of DesgloseAuditorias");
        Page<DesgloseAuditoriaDTO> page = desgloseAuditoriaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-auditorias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-auditorias/:id : get the "id" desgloseAuditoria.
     *
     * @param id
     * 		the id of the desgloseAuditoriaDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseAuditoriaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-auditorias/{id}")
    @Timed
    public ResponseEntity<DesgloseAuditoriaDTO> getDesgloseAuditoria(@PathVariable Long id) {
        // TODO: CORREGIR
        DesgloseAuditoriaPK id1 = new DesgloseAuditoriaPK();
        log.info("REST request to get DesgloseAuditoria : {}", id);
        Optional<DesgloseAuditoriaDTO> desgloseAuditoriaDTO = desgloseAuditoriaService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseAuditoriaDTO);
    }

    /**
     * DELETE  /desglose-auditorias/:id : delete the "id" desgloseAuditoria.
     *
     * @param id
     * 		the id of the desgloseAuditoriaDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-auditorias/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseAuditoria(@PathVariable Long id) {
        // TODO: CORREGIR
        DesgloseAuditoriaPK id1 = new DesgloseAuditoriaPK();
        log.info("REST request to delete DesgloseAuditoria : {}", id);
        desgloseAuditoriaService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
