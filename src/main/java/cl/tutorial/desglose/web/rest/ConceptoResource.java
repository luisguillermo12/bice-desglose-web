package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.conexiones.bctdesglose.domain.pk.ConceptoPK;
import cl.tutorial.desglose.service.ConceptoService;
import cl.tutorial.desglose.service.dto.BreakdownFilterConceptDTO;
import cl.tutorial.desglose.service.dto.BreakdownValidationErrorDTO;
import cl.tutorial.desglose.service.dto.ConceptoDTO;
import cl.tutorial.desglose.service.dto.ConceptoRequestDTO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * REST controller for managing Concepto.
 */
@RestController
@RequestMapping("/api")
public class ConceptoResource {

    private final Logger log = LogManager.getLogger(ConceptoResource.class);

    private static final String ENTITY_NAME = "concepto";

    private final ConceptoService conceptoService;

    public ConceptoResource(ConceptoService conceptoService) {
        this.conceptoService = conceptoService;
    }

    /**
     * POST  /conceptos : Create a new concepto.
     *
     * @param conceptoDTO
     * 		the conceptoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new conceptoDTO, or with status 400 (Bad Request) if the concepto has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/conceptos")
    @Timed
    public ResponseEntity<ConceptoDTO> createConcepto(@Valid @RequestBody ConceptoDTO conceptoDTO) throws URISyntaxException {
        log.info("REST request to save Concepto : {}", conceptoDTO);
        Integer corConcepto = conceptoService.maxCorConcepto(conceptoDTO.getCodTipoConcep(), conceptoDTO.getCodTipoPlantilla()).intValueExact();
        conceptoDTO.setCorConcepto(corConcepto + 1);
        conceptoService.save(conceptoDTO,false);
        final String entityId = HeaderUtil.createHeaderEntityId(conceptoDTO.getCodTipoConcep(), conceptoDTO.getCorConcepto().toString());
        return ResponseEntity.created(new URI("/api/conceptos/" + conceptoDTO.getCodTipoConcep() + "/" + conceptoDTO.getCorConcepto()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, entityId)).body(conceptoDTO);
    }

    /**
     * PUT  /conceptos : Updates an existing concepto.
     *
     * @param conceptoDTO
     * 		the conceptoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated conceptoDTO, or with status 400 (Bad Request) if the conceptoDTO is not valid, or with status 500
     * (Internal Server Error) if the conceptoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/conceptos")
    @Timed
    public ResponseEntity<ConceptoDTO> updateConcepto(@Valid @RequestBody ConceptoDTO conceptoDTO) throws URISyntaxException {
        log.info("REST request to update Concepto : {}", conceptoDTO);
        if (!ObjectUtils.allNotNull(conceptoDTO.getCodTipoConcep(), conceptoDTO.getCorConcepto())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        conceptoService.save(conceptoDTO,true);
        final String entityId = HeaderUtil.createHeaderEntityId(conceptoDTO.getCodTipoConcep(), conceptoDTO.getCorConcepto().toString());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(conceptoDTO);
    }
    @PostMapping("/conceptos-validations")
    @Timed
    public ResponseEntity<List<BreakdownValidationErrorDTO>> validateBreakdown(@Valid @RequestBody ConceptoDTO conceptoDTO) throws URISyntaxException {
        log.info("REST request to validaciones al momento de grabar conceptos : {}", conceptoDTO);
        List<BreakdownValidationErrorDTO> result = conceptoService.validateConcepto(conceptoDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/conceptos-validations-all")
    @Timed
    public ResponseEntity<List<BreakdownValidationErrorDTO>> validateAllBreakdown(@RequestBody ConceptoDTO conceptoDTO) throws URISyntaxException {
        log.info("REST request to validaciones ALl para ver conceptos descuadrados : {}", conceptoDTO);
        List<BreakdownValidationErrorDTO> result = conceptoService.validateConceptoSumatoriasValores(conceptoDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * GET  /conceptos : get all the conceptos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of conceptos in body
     */
    @GetMapping("/conceptos")
    @Timed
    public ResponseEntity<List<ConceptoDTO>> getAllConceptos(Pageable pageable) {
        log.info("REST request to get a page of Conceptos");
        Page<ConceptoDTO> page = conceptoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/conceptos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /conceptos/find-by-template-concept-not-like : Obtiene todos los conceptos para un tipo de plantilla y un código de concepto no parecido al dado.
     *
     * @param conceptoRequestDTO
     * 		Objeto que contiene el código del tipo de plantilla y la cadena de caracteres con el formato que no debe cumplir el código del concepto. En caso que no se pase este
     * 		parámetro se obtendrán todos por el tipo de plantilla.
     *
     * @return Lista de ConceptoDTO que cumplan con el filtro.
     */
    @PostMapping("/conceptos/find-by-template-concept-not-like")
    @Timed
    public ResponseEntity<List<ConceptoDTO>> findByTemplateConceptNotLike(@Valid @RequestBody ConceptoRequestDTO conceptoRequestDTO) {
        log.info("REST request to get all Conceptos by templateCode: {} and conceptCode: {} and flgConActivo: {}", conceptoRequestDTO.templateCode, conceptoRequestDTO.conceptCode,
            conceptoRequestDTO.flgConActivo);
        List<ConceptoDTO> conceptoDTOList =
            conceptoService.findByTemplateConceptNotLike(conceptoRequestDTO.templateCode, conceptoRequestDTO.conceptCode, conceptoRequestDTO.flgConActivo);
        return new ResponseEntity<>(conceptoDTOList, HttpStatus.OK);
    }

    /**
     * POST  /conceptos/find-by-template-cod-concepts-in : Obtiene todos los conceptos para un tipo de plantilla y los códigos de conceptos dados.
     *
     * @param conceptoRequestDTO
     * 		Objeto que contiene el código del tipo de plantilla y los códigos de conceptos para filtrar. El campo "conceptCode" no se utiliza.
     *
     * @return Lista de ConceptoDTO que cumplan con el filtro.
     */
    @PostMapping("/conceptos/find-by-template-cod-concepts-in")
    @Timed
    public ResponseEntity<BreakdownFilterConceptDTO> findByTemplateAndCodConceptsIn(@Valid @RequestBody ConceptoRequestDTO conceptoRequestDTO) {
        log.info("REST request to get all Conceptos by FILTER: {}", conceptoRequestDTO);
        ConceptoRequestDTO conceptoRequestDTO1 = null;
        if(conceptoRequestDTO.templateCodeRelacionada!= null) {
            conceptoRequestDTO1 =  new ConceptoRequestDTO();
            conceptoRequestDTO1.templateCode = conceptoRequestDTO.templateCodeRelacionada;
            List<String> conceptTypeCodesIn = new LinkedList<>();
            conceptTypeCodesIn.add(COD_TIPO_CONCEP_BALANCE);
            conceptTypeCodesIn.add(COD_TIPO_CONCEP_EST_RESULT);
            conceptTypeCodesIn.add(COD_TIPO_CONCEP_GEN_FONDOS);
            conceptTypeCodesIn.add(COD_TIPO_CONCEP_IND_FINAN);
            conceptoRequestDTO1.conceptTypeCodesIn = conceptTypeCodesIn;
            conceptoRequestDTO1.flgConActivo = conceptoRequestDTO.flgConActivo;
            conceptoRequestDTO1.columnLetterGlsFormulaVal1 = conceptoRequestDTO.columnLetterGlsFormulaVal1;
            conceptoRequestDTO1.orderByDirection = conceptoRequestDTO.orderByDirection;
            conceptoRequestDTO1.orderByFields = conceptoRequestDTO.orderByFields;
        }

        BreakdownFilterConceptDTO breakdownFilterConceptDTO = conceptoService.findByTemplateAndCodConceptsIn(conceptoRequestDTO, conceptoRequestDTO1);
        return new ResponseEntity<>(breakdownFilterConceptDTO, HttpStatus.OK);
    }

    @PutMapping("/conceptos/resecuenciar/{codPlantilla}")
    @Timed
    public ResponseEntity<Integer> resencuenciar(@PathVariable Integer codPlantilla){
        Integer estado = this.conceptoService.resecuenciar(codPlantilla);
        return new ResponseEntity<>(estado, HttpStatus.OK);
    }

    /**
     * @param codPlantilla
     * @param codTipoConcepto
     * @param corConcepto
     *
     * @return
     *
     * @author carlos eduardo rodriguez perez
     */
    @GetMapping("/conceptos/byplantillalike/{codPlantilla}/{codTipoConcepto}/{corConcepto}/{flgConActivo}")
    @Timed
    public ResponseEntity<List<ConceptoDTO>> getAllConceptosByPlantillalike(@PathVariable Integer codPlantilla, @PathVariable String codTipoConcepto,
        @PathVariable String corConcepto, @PathVariable Integer flgConActivo) {
        log.info("REST request to get a page of Conceptos");
        List<ConceptoDTO> lista = conceptoService.findAllByCodTipoPlantillaLike(codPlantilla, codTipoConcepto, corConcepto + ".%", null);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * GET  /conceptos/:codTipoConcep/:corConcepto : get the "id" concepto.
     *
     * @param codTipoConcep
     * 		the id of the conceptoDTO to retrieve
     * @param corConcepto
     * 		the id of the conceptoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the conceptoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/conceptos/{codTipoConcep}/{corConcepto}/{codTipoPlantilla}")
    @Timed
    public ResponseEntity<ConceptoDTO> getConcepto(@PathVariable String codTipoConcep, @PathVariable Integer corConcepto, @PathVariable Integer codTipoPlantilla) {
        ConceptoPK id = new ConceptoPK();
        id.setCodTipoConcep(codTipoConcep);
        id.setCorConcepto(corConcepto);
        id.setCodTipoPlantilla(codTipoPlantilla);

        log.info("REST request to get Concepto : {}", id);
        Optional<ConceptoDTO> conceptoDTO = conceptoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conceptoDTO);
    }

    /**
     * DELETE  /conceptos/:codTipoConcep/:corConcepto : get the "id" concepto.
     *
     * @param codTipoConcep
     * 		the id of the conceptoDTO to retrieve
     * @param corConcepto
     * 		the id of the conceptoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/conceptos/{codTipoConcep}/{corConcepto}/{codTipoPlantilla}")
    @Timed
    public ResponseEntity<Void> deleteConcepto(@PathVariable String codTipoConcep, @PathVariable Integer corConcepto, @PathVariable Integer codTipoPlantilla) {
        ConceptoPK id = new ConceptoPK();
        id.setCodTipoConcep(codTipoConcep);
        id.setCorConcepto(corConcepto);
        id.setCodTipoPlantilla(codTipoPlantilla);
        log.info("REST request to delete Concepto : {}", id);
        conceptoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
