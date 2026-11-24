package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DescripcionCodigoPK;
import cl.tutorial.desglose.service.DescripcionCodigoService;
import cl.tutorial.desglose.service.dto.DescripcionCodigoDTO;
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

import static cl.tutorial.desglose.config.Constants.CODTABLA_PLANTILLAS_DESGLOSE;
import static cl.tutorial.desglose.config.Constants.CODTABLA_PROYECIONES_DESGLOSE;

/**
 * REST controller for managing DescripcionCodigo.
 */
@RestController
@RequestMapping("/api")
public class DescripcionCodigoResource {

    private final Logger log = LogManager.getLogger(DescripcionCodigoResource.class);

    private static final String ENTITY_NAME = "descripcionCodigo";

    private final DescripcionCodigoService descripcionCodigoService;

    public DescripcionCodigoResource(DescripcionCodigoService descripcionCodigoService) {
        this.descripcionCodigoService = descripcionCodigoService;
    }

    /**
     * POST  /descripcion-codigos : Create a new descripcionCodigo.
     *
     * @param descripcionCodigoDTO
     * 		the descripcionCodigoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new descripcionCodigoDTO, or with status 400 (Bad Request) if the descripcionCodigo has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/descripcion-codigos")
    @Timed
    public ResponseEntity<DescripcionCodigoDTO> createDescripcionCodigo(@Valid @RequestBody DescripcionCodigoDTO descripcionCodigoDTO) throws URISyntaxException {
        log.info("REST request to save DescripcionCodigo : {}", descripcionCodigoDTO);
        if (!ObjectUtils.allNotNull(descripcionCodigoDTO.getCodTabla(), descripcionCodigoDTO.getCodGenerico(), descripcionCodigoDTO.getCodRegStatus())) {
            throw new BadRequestAlertException("A new descripcionCodigo must have a valid ID", ENTITY_NAME, "notValidId");
        }

        DescripcionCodigoPK id = new DescripcionCodigoPK();
        id.setCodTabla(descripcionCodigoDTO.getCodTabla());
        id.setCodGenerico(descripcionCodigoDTO.getCodGenerico());
        id.setGlsGenericacorta(descripcionCodigoDTO.getGlsGenericacorta());
        id.setGlsGenericalarga(descripcionCodigoDTO.getGlsGenericalarga());
        if(!descripcionCodigoDTO.getCodTabla().equalsIgnoreCase(CODTABLA_PLANTILLAS_DESGLOSE) && !descripcionCodigoDTO.getCodTabla().equalsIgnoreCase(CODTABLA_PROYECIONES_DESGLOSE)) {
            throw new BadRequestAlertException("El codigo de tabla no es valido", ENTITY_NAME, "idexists");
        }
        DescripcionCodigoDTO result = descripcionCodigoService.save(descripcionCodigoDTO);
        final String entityId = HeaderUtil.createHeaderEntityId(result.getCodTabla(), result.getCodGenerico(), result.getCodRegStatus().toString());
        return ResponseEntity.created(new URI("/api/descripcion-codigos/" + result.getCodTabla() + "/" + result.getCodGenerico() + "/" + result.getCodRegStatus().toString()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * PUT  /descripcion-codigos : Updates an existing descripcionCodigo.
     *
     * @param descripcionCodigoDTO
     * 		the descripcionCodigoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated descripcionCodigoDTO, or with status 400 (Bad Request) if the descripcionCodigoDTO is not valid, or
     * with status 500 (Internal Server Error) if the descripcionCodigoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/descripcion-codigos")
    @Timed
    public ResponseEntity<DescripcionCodigoDTO> updateDescripcionCodigo(@Valid @RequestBody DescripcionCodigoDTO descripcionCodigoDTO) throws URISyntaxException {
        log.info("REST request to update DescripcionCodigo : {}", descripcionCodigoDTO);
        if (descripcionCodigoDTO.getCodTabla() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DescripcionCodigoDTO result = descripcionCodigoService.save(descripcionCodigoDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, descripcionCodigoDTO.getCodTabla())).body(result);
    }

    /**
     * GET  /descripcion-codigos : get all the descripcionCodigos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of descripcionCodigos in body
     */
    @GetMapping("/descripcion-codigos")
    @Timed
    public ResponseEntity<List<DescripcionCodigoDTO>> getAllDescripcionCodigos(Pageable pageable) {
        log.info("REST request to get a page of DescripcionCodigos");
        Page<DescripcionCodigoDTO> page = descripcionCodigoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/descripcion-codigos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /descripcion-codigos/:codTabla/:codGenerico/:codRegStatus : get the "id" descripcionCodigo.
     *
     * @param codTabla
     * 		the id of the descripcionCodigoDTO to retrieve
     * @param codGenerico
     * 		the id of the descripcionCodigoDTO to retrieve
     * @param codRegStatus
     * 		the id of the descripcionCodigoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the descripcionCodigoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/descripcion-codigos/{codTabla}/{codGenerico}/{codRegStatus}/{glsGenericacorta}/{glsGenericalarga}")
    @Timed
    public ResponseEntity<DescripcionCodigoDTO> getDescripcionCodigo(@PathVariable String codTabla,@PathVariable String codGenerico, @PathVariable Integer codRegStatus,
        @PathVariable String glsGenericacorta,@PathVariable String glsGenericalarga) {
        DescripcionCodigoPK id = new DescripcionCodigoPK();
        id.setCodTabla(codTabla);
        id.setCodGenerico(codGenerico);
        id.setGlsGenericacorta(glsGenericacorta);
        id.setGlsGenericalarga(glsGenericalarga);
        log.info("REST request to get DescripcionCodigo : {}", id);
        Optional<DescripcionCodigoDTO> descripcionCodigoDTO = descripcionCodigoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descripcionCodigoDTO);
    }

    /**
     * DELETE  /descripcion-codigos/:codTabla/:codGenerico/:codRegStatus : delete the "id" descripcionCodigo.
     *
     * @param codTabla
     * 		the id of the descripcionCodigoDTO to retrieve
     * @param codGenerico
     * 		the id of the descripcionCodigoDTO to retrieve
     * @param codRegStatus
     * 		the id of the descripcionCodigoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/descripcion-codigos/{codTabla}/{codGenerico}/{codRegStatus}/{glsGenericacorta}/{glsGenericalarga}")
    @Timed
    public ResponseEntity<Void> deleteDescripcionCodigo(@PathVariable String codTabla, @PathVariable String codGenerico, @PathVariable Integer codRegStatus,
        @PathVariable String glsGenericacorta,@PathVariable String glsGenericalarga) {
        DescripcionCodigoPK id = new DescripcionCodigoPK();
        id.setCodTabla(codTabla);
        id.setCodGenerico(codGenerico);
        id.setGlsGenericacorta(glsGenericacorta);
        id.setGlsGenericalarga(glsGenericalarga);
        log.info("REST request to delete DescripcionCodigo : {}", id);
        descripcionCodigoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /descripcion-codigos/find-by-codtabla : get all the descripcionCodigos by codtabla.
     *
     * @param codTabla
     * 		El código de la codiguera o nomenclador
     * @param sort
     * 		Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are not supported.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of descripcionCodigos in body
     */
    @GetMapping("/descripcion-codigos/find-by-codtabla")
    @Timed
    public ResponseEntity<List<DescripcionCodigoDTO>> getAllDescripcionCodigosByCodTabla(@RequestParam(name = "codTabla") String codTabla,
        @RequestParam(name = "codRegStatus", required = false, defaultValue = "0") Integer codRegStatus, @RequestParam(name = "sort") String sort) {
        log.info("REST request to get a page of DescripcionCodigos");
        List<DescripcionCodigoDTO> page = descripcionCodigoService.findAllByCodTabla(codTabla, codRegStatus, sort);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /descripcion-codigos/find-by-codtabla-in : get all the descripcionCodigos by codtabla.
     *
     * @param codTablas
     * 		El código de la codiguera o nomenclador. Pueden ser valores separados por coma (,) si se quiere más de un tipo de elemento.
     * @param sort
     * 		Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are not supported.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of descripcionCodigos in body
     */
    @GetMapping("/descripcion-codigos/find-by-codtabla-in")
    @Timed
    public ResponseEntity<List<DescripcionCodigoDTO>> getAllDescripcionCodigosByCodTablasIn(@RequestParam(name = "codTablas") String codTablas, @RequestParam(name = "sort") String sort) {
        log.info("REST request to get a page of DescripcionCodigos");
        List<DescripcionCodigoDTO> page = descripcionCodigoService.findAllByCodTablasIn(codTablas, sort);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /descripcion-codigos/find-by-codtabla-in-and-codgenerico-like : get all the descripcionCodigos by codtabla.
     *
     * @param codTablas
     * 		El código de la codiguera o nomenclador. Pueden ser valores separados por coma (,) si se quiere más de un tipo de elemento.
     * @param codGenerico
     * 		Texto con el que comienza la glosa del código.
     * @param sort
     * 		Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are not supported.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of descripcionCodigos in body
     */
    @GetMapping("/descripcion-codigos/find-by-codtabla-in-and-codgenerico-like")
    @Timed
    public ResponseEntity<List<DescripcionCodigoDTO>> getAllDesCodByCodTablasInAndCodGenericoLike(@RequestParam(name = "codTablas") String codTablas,
        @RequestParam(name = "codRegStatus", required = false, defaultValue = "0") Integer codRegStatus, @RequestParam(name = "codGenerico") String codGenerico,
        @RequestParam(name = "sort") String sort) {
        log.info("REST request to get a page of DescripcionCodigos");
        List<DescripcionCodigoDTO> page = descripcionCodigoService.getAllDesCodByCodTablasInAndCodGenericoLike(codTablas, codGenerico, codRegStatus, sort);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/descripcion-codigos/findAllByCodTablaAndCodGenericoLikeAndCodGenericoNot/{codtablas}/{codGenerico}/{codGenerico1}")
    @Timed
    public ResponseEntity<List<DescripcionCodigoDTO>> getAllDescripcionCodigosByCodTablasIn(@PathVariable String codtablas, @PathVariable String codGenerico,
        @PathVariable String codGenerico1, @RequestParam(name = "codRegStatus", required = false, defaultValue = "0") Integer codRegStatus) {
        log.info("REST request to get a page of DescripcionCodigos");
        List<DescripcionCodigoDTO> page = descripcionCodigoService.findAllByCodTablaAndCodGenericoLikeAndCodGenericoNot(codtablas, codGenerico, codGenerico1, codRegStatus);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /descripcion-codigos/find-breakdown-statuses : get all the statuses order by custom pattern.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of descripcionCodigos in body
     */
    @GetMapping("/descripcion-codigos/find-breakdown-statuses")
    @Timed
    public ResponseEntity<List<DescripcionCodigoDTO>> findBreakdownStatuses() {
        log.info("REST request to get a page of findBreakdownStatuses");
        List<DescripcionCodigoDTO> page = descripcionCodigoService.findBreakdownStatuses();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
