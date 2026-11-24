package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.ConceptoImpresionPK;
import cl.tutorial.desglose.service.ConceptoImpresionService;
import cl.tutorial.desglose.service.dto.ConceptoImpresionDTO;
import cl.tutorial.desglose.service.dto.ConceptoImpresionRequestDTO;
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
 * REST controller for managing ConceptoImpresion.
 */
@RestController
@RequestMapping("/api")
public class ConceptoImpresionResource {

    private final Logger log = LogManager.getLogger(ConceptoImpresionResource.class);

    private static final String ENTITY_NAME = "conceptoImpresion";

    private final ConceptoImpresionService conceptoImpresionService;

    public ConceptoImpresionResource(ConceptoImpresionService conceptoImpresionService) {
        this.conceptoImpresionService = conceptoImpresionService;
    }

    /**
     * POST  /concepto-impresions : Create a new conceptoImpresion.
     *
     * @param conceptoImpresionDTO
     * 		the conceptoImpresionDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new conceptoImpresionDTO, or with status 400 (Bad Request) if the conceptoImpresion has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/concepto-impresions")
    @Timed
    public ResponseEntity<ConceptoImpresionDTO> createConceptoImpresion(@Valid @RequestBody ConceptoImpresionDTO conceptoImpresionDTO) throws URISyntaxException {
        log.info("REST request to save ConceptoImpresion : {}", conceptoImpresionDTO);
        if (!ObjectUtils
            .allNotNull(conceptoImpresionDTO.getCodCliente(), conceptoImpresionDTO.getCodMoneda(), conceptoImpresionDTO.getCodSubtipoDes(), conceptoImpresionDTO.getCodTipoConcep(),
                conceptoImpresionDTO.getCodTipoDes(), conceptoImpresionDTO.getCorConcepto())) {
            throw new BadRequestAlertException("A new cuadre must have a valid ID", ENTITY_NAME, "notValidId");
        }
        ConceptoImpresionDTO result = conceptoImpresionService.save(conceptoImpresionDTO);
        final String entityId = HeaderUtil
            .createHeaderEntityId(result.getCodCliente(), result.getCodMoneda(), result.getCodSubtipoDes().toString(), result.getCodTipoConcep(), result.getCodTipoDes().toString(),
                result.getCorConcepto().toString());
        return ResponseEntity.created(new URI(
            "/api/concepto-impresions/" + result.getCodCliente() + "/" + result.getCodMoneda() + "/" + result.getCodSubtipoDes() + "/" + result.getCodTipoConcep() + "/" + result
                .getCodTipoDes() + "/" + result.getCorConcepto().toString())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * PUT  /concepto-impresions : Updates an existing conceptoImpresion.
     *
     * @param conceptoImpresionDTO
     * 		the conceptoImpresionDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated conceptoImpresionDTO, or with status 400 (Bad Request) if the conceptoImpresionDTO is not valid, or
     * with status 500 (Internal Server Error) if the conceptoImpresionDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/concepto-impresions")
    @Timed
    public ResponseEntity<ConceptoImpresionDTO> updateConceptoImpresion(@Valid @RequestBody ConceptoImpresionDTO conceptoImpresionDTO) throws URISyntaxException {
        log.info("REST request to update ConceptoImpresion : {}", conceptoImpresionDTO);
        if (!ObjectUtils
            .allNotNull(conceptoImpresionDTO.getCodCliente(), conceptoImpresionDTO.getCodMoneda(), conceptoImpresionDTO.getCodSubtipoDes(), conceptoImpresionDTO.getCodTipoConcep(),
                conceptoImpresionDTO.getCodTipoDes(), conceptoImpresionDTO.getCorConcepto())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConceptoImpresionDTO result = conceptoImpresionService.save(conceptoImpresionDTO);
        final String entityId = HeaderUtil
            .createHeaderEntityId(result.getCodCliente(), result.getCodMoneda(), result.getCodSubtipoDes().toString(), result.getCodTipoConcep(), result.getCodTipoDes().toString(),
                result.getCorConcepto().toString());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * GET  /concepto-impresions : get all the conceptoImpresions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of conceptoImpresions in body
     */
    @GetMapping("/concepto-impresions")
    @Timed
    public ResponseEntity<List<ConceptoImpresionDTO>> getAllConceptoImpresions(Pageable pageable) {
        log.info("REST request to get a page of ConceptoImpresions");
        Page<ConceptoImpresionDTO> page = conceptoImpresionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/concepto-impresions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /concepto-impresions/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codTipoConcep/:corConcepto : get the "id" conceptoImpresion.
     *
     * @param codCliente
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codTipoDes
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codSubtipoDes
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codMoneda
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codTipoConcep
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param corConcepto
     * 		the id of the conceptoImpresionDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the conceptoImpresionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/concepto-impresions/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{codTipoConcep}/{corConcepto}")
    @Timed
    public ResponseEntity<ConceptoImpresionDTO> getConceptoImpresion(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable String codTipoConcep, @PathVariable Integer corConcepto) {

        ConceptoImpresionPK id = new ConceptoImpresionPK();
        id.setCodCliente(codCliente);
        id.setCodTipoDes(codTipoDes);
        id.setCodSubtipoDes(codSubtipoDes);
        id.setCodMoneda(codMoneda);
        id.setCodTipoConcep(codTipoConcep);
        id.setCorConcepto(corConcepto);
        log.info("REST request to get ConceptoImpresion : {}", id);
        Optional<ConceptoImpresionDTO> conceptoImpresionDTO = conceptoImpresionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conceptoImpresionDTO);
    }

    /**
     * DELETE  /concepto-impresions/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codTipoConcep/:corConcepto : delete the "id" conceptoImpresion.
     *
     * @param codCliente
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codTipoDes
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codSubtipoDes
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codMoneda
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param codTipoConcep
     * 		the id of the conceptoImpresionDTO to retrieve
     * @param corConcepto
     * 		the id of the conceptoImpresionDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/concepto-impresions/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{codTipoConcep}/{corConcepto}")
    @Timed
    public ResponseEntity<Void> deleteConceptoImpresion(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable String codTipoConcep, @PathVariable Integer corConcepto) {

        ConceptoImpresionPK id = new ConceptoImpresionPK();
        id.setCodCliente(codCliente);
        id.setCodTipoDes(codTipoDes);
        id.setCodSubtipoDes(codSubtipoDes);
        id.setCodMoneda(codMoneda);
        id.setCodTipoConcep(codTipoConcep);
        id.setCorConcepto(corConcepto);
        log.info("REST request to delete ConceptoImpresion : {}", id);
        conceptoImpresionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /concepto-impresions/filter-c-t-st-m : get all the ConceptoImpresionDTO filtered by the param.
     *
     * @param conceptoImpresionRequestDTO
     * 		objeto con los valores del filtro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ConceptoImpresionDTO in body
     */
    @PostMapping("/concepto-impresions/filter-c-t-st-m")
    @Timed
    public ResponseEntity<List<ConceptoImpresionDTO>> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(
        @Valid @RequestBody ConceptoImpresionRequestDTO conceptoImpresionRequestDTO) {
        log.info("REST request to get filtered ConceptoImpresion with filter: {}", conceptoImpresionRequestDTO);
        List<ConceptoImpresionDTO> page = conceptoImpresionService.findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMoneda(conceptoImpresionRequestDTO,null,null);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
