package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseChequeoInfoPK;
import cl.tutorial.desglose.service.DesgloseChequeoInfoService;
import cl.tutorial.desglose.service.dto.DesgloseChequeoInfoDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DesgloseChequeoInfo.
 */
@RestController
@RequestMapping("/api")
public class DesgloseChequeoInfoResource {

    private final Logger log = LogManager.getLogger(DesgloseChequeoInfoResource.class);

    private static final String ENTITY_NAME = "desgloseChequeoInfo";

    private final DesgloseChequeoInfoService desgloseChequeoInfoService;

    public DesgloseChequeoInfoResource(DesgloseChequeoInfoService desgloseChequeoInfoService) {
        this.desgloseChequeoInfoService = desgloseChequeoInfoService;
    }

    /**
     * POST  /desglose-chequeo-infos : Create a new desgloseChequeoInfo.
     *
     * @param desgloseChequeoInfoDTO
     * 		the desgloseChequeoInfoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseChequeoInfoDTO, or with status 400 (Bad Request) if the desgloseChequeoInfo has already an
     * ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-chequeo-infos")
    @Timed
    public ResponseEntity<DesgloseChequeoInfoDTO> createDesgloseChequeoInfo(@RequestBody DesgloseChequeoInfoDTO desgloseChequeoInfoDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseChequeoInfo : {}", desgloseChequeoInfoDTO);
        if (desgloseChequeoInfoDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new desgloseChequeoInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseChequeoInfoDTO result = desgloseChequeoInfoService.save(desgloseChequeoInfoDTO);
        return ResponseEntity.created(new URI("/api/desglose-chequeo-infos/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /desglose-chequeo-infos : Updates an existing desgloseChequeoInfo.
     *
     * @param desgloseChequeoInfoDTO
     * 		the desgloseChequeoInfoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseChequeoInfoDTO, or with status 400 (Bad Request) if the desgloseChequeoInfoDTO is not
     * valid, or with status 500 (Internal Server Error) if the desgloseChequeoInfoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-chequeo-infos")
    @Timed
    public ResponseEntity<DesgloseChequeoInfoDTO> updateDesgloseChequeoInfo(@RequestBody DesgloseChequeoInfoDTO desgloseChequeoInfoDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseChequeoInfo : {}", desgloseChequeoInfoDTO);
        if (desgloseChequeoInfoDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseChequeoInfoDTO result = desgloseChequeoInfoService.save(desgloseChequeoInfoDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseChequeoInfoDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /desglose-chequeo-infos : get all the desgloseChequeoInfos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseChequeoInfos in body
     */
    @GetMapping("/desglose-chequeo-infos")
    @Timed
    public ResponseEntity<List<DesgloseChequeoInfoDTO>> getAllDesgloseChequeoInfos(Pageable pageable) {
        log.info("REST request to get a page of DesgloseChequeoInfos");
        Page<DesgloseChequeoInfoDTO> page = desgloseChequeoInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-chequeo-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-chequeo-infos/:id : get the "id" desgloseChequeoInfo.
     *
     * @param id
     * 		the id of the desgloseChequeoInfoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseChequeoInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-chequeo-infos/{id}")
    @Timed
    public ResponseEntity<DesgloseChequeoInfoDTO> getDesgloseChequeoInfo(@PathVariable Long id) {
        DesgloseChequeoInfoPK id1 = new DesgloseChequeoInfoPK();
        log.info("REST request to get DesgloseChequeoInfo : {}", id);
        Optional<DesgloseChequeoInfoDTO> desgloseChequeoInfoDTO = desgloseChequeoInfoService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseChequeoInfoDTO);
    }

    /**
     * DELETE  /desglose-chequeo-infos/:id : delete the "id" desgloseChequeoInfo.
     *
     * @param id
     * 		the id of the desgloseChequeoInfoDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-chequeo-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseChequeoInfo(@PathVariable Long id) {
        DesgloseChequeoInfoPK id1 = new DesgloseChequeoInfoPK();
        log.info("REST request to delete DesgloseChequeoInfo : {}", id);
        desgloseChequeoInfoService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
