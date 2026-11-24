package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.CuadrePK;
import cl.tutorial.desglose.service.CuadreService;
import cl.tutorial.desglose.service.dto.CuadreDTO;
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
 * REST controller for managing Cuadre.
 */
@RestController
@RequestMapping("/api")
public class CuadreResource {

    private final Logger log = LogManager.getLogger(CuadreResource.class);

    private static final String ENTITY_NAME = "cuadre";

    private final CuadreService cuadreService;

    public CuadreResource(CuadreService cuadreService) {
        this.cuadreService = cuadreService;
    }

    /**
     * POST  /cuadres : Create a new cuadre.
     *
     * @param cuadreDTO
     * 		the cuadreDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new cuadreDTO, or with status 400 (Bad Request) if the cuadre has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/cuadres")
    @Timed
    public ResponseEntity<CuadreDTO> createCuadre(@Valid @RequestBody CuadreDTO cuadreDTO) throws URISyntaxException {
        log.info("REST request to save Cuadre : {}", cuadreDTO);
        if (!ObjectUtils.allNotNull(cuadreDTO.getCodTpoConcep1(), cuadreDTO.getCodTpoConcep2(), cuadreDTO.getCorConcepto1(), cuadreDTO.getCorConcepto2())) {
            throw new BadRequestAlertException("A new cuadre must have a valid ID", ENTITY_NAME, "notValidId");
        }

        CuadrePK id = new CuadrePK();
        id.setCodTpoConcep1(cuadreDTO.getCodTpoConcep1());
        id.setCorConcepto1(cuadreDTO.getCorConcepto1());
        id.setCodTpoConcep2(cuadreDTO.getCodTpoConcep2());
        id.setCorConcepto2(cuadreDTO.getCorConcepto2());
        if (cuadreService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("A new cuadre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CuadreDTO result = cuadreService.save(cuadreDTO);
        final String entityId =
            HeaderUtil.createHeaderEntityId(result.getCodTpoConcep1(), result.getCodTpoConcep2(), result.getCorConcepto1().toString(), result.getCorConcepto2().toString());
        return ResponseEntity.created(new URI(
            "/api/cuadres/" + result.getCodTpoConcep1() + "/" + result.getCodTpoConcep2() + "/" + result.getCorConcepto1().toString() + "/" + result.getCorConcepto2().toString()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * PUT  /cuadres : Updates an existing cuadre.
     *
     * @param cuadreDTO
     * 		the cuadreDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated cuadreDTO, or with status 400 (Bad Request) if the cuadreDTO is not valid, or with status 500
     * (Internal Server Error) if the cuadreDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/cuadres")
    @Timed
    public ResponseEntity<CuadreDTO> updateCuadre(@Valid @RequestBody CuadreDTO cuadreDTO) throws URISyntaxException {
        log.info("REST request to update Cuadre : {}", cuadreDTO);
        if (!ObjectUtils
            .allNotNull(cuadreDTO.getCodTpoConcep1() != null, cuadreDTO.getCodTpoConcep2() != null, cuadreDTO.getCorConcepto1() != null, cuadreDTO.getCorConcepto2() != null)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CuadreDTO result = cuadreService.save(cuadreDTO);
        final String entityId =
            HeaderUtil.createHeaderEntityId(result.getCodTpoConcep1(), result.getCodTpoConcep2(), result.getCorConcepto1().toString(), result.getCorConcepto2().toString());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * GET  /cuadres : get all the cuadres.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cuadres in body
     */
    @GetMapping("/cuadres")
    @Timed
    public ResponseEntity<List<CuadreDTO>> getAllCuadres(Pageable pageable) {
        log.info("REST request to get a page of Cuadres");
        Page<CuadreDTO> page = cuadreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cuadres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cuadres/:codTpoConcep1/:corConcepto1/:codTpoConcep2/:corConcepto2 : get the "id" cuadre.
     *
     * @param codTpoConcep1
     * 		the id of the cuadreDTO to retrieve
     * @param corConcepto1
     * 		the id of the cuadreDTO to retrieve
     * @param codTpoConcep2
     * 		the id of the cuadreDTO to retrieve
     * @param corConcepto2
     * 		the id of the cuadreDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cuadreDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cuadres/{codTpoConcep1}/{corConcepto1}/{codTpoConcep2}/{corConcepto2}")
    @Timed
    public ResponseEntity<CuadreDTO> getCuadre(@PathVariable String codTpoConcep1, @PathVariable Integer corConcepto1, @PathVariable String codTpoConcep2,
        @PathVariable Integer corConcepto2) {

        CuadrePK id = new CuadrePK();
        id.setCodTpoConcep1(codTpoConcep1);
        id.setCorConcepto1(corConcepto1);
        id.setCodTpoConcep2(codTpoConcep2);
        id.setCorConcepto2(corConcepto2);

        log.info("REST request to get Cuadre : {}", id);
        Optional<CuadreDTO> cuadreDTO = cuadreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuadreDTO);
    }

    /**
     * DELETE  /cuadres/:codTpoConcep1/:corConcepto1/:codTpoConcep2/:corConcepto2 : delete the "id" cuadre.
     *
     * @param codTpoConcep1
     * 		the id of the cuadreDTO to retrieve
     * @param corConcepto1
     * 		the id of the cuadreDTO to retrieve
     * @param codTpoConcep2
     * 		the id of the cuadreDTO to retrieve
     * @param corConcepto2
     * 		the id of the cuadreDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cuadres/{codTpoConcep1}/{corConcepto1}/{codTpoConcep2}/{corConcepto2}")
    @Timed
    public ResponseEntity<Void> deleteCuadre(@PathVariable String codTpoConcep1, @PathVariable Integer corConcepto1, @PathVariable String codTpoConcep2,
        @PathVariable Integer corConcepto2) {

        CuadrePK id = new CuadrePK();
        id.setCodTpoConcep1(codTpoConcep1);
        id.setCorConcepto1(corConcepto1);
        id.setCodTpoConcep2(codTpoConcep2);
        id.setCorConcepto2(corConcepto2);

        log.info("REST request to delete Cuadre : {}", id);
        cuadreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
