package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseRevisionPK;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import cl.tutorial.desglose.service.DesgloseRevisionService;
import cl.tutorial.desglose.service.dto.DesgloseRevisionDTO;
import cl.tutorial.desglose.service.dto.ReemplazosVistaDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
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

/**
 * REST controller for managing DesgloseRevision.
 */
@RestController
@RequestMapping("/api")
public class DesgloseRevisionResource {

    private final Logger log = LogManager.getLogger(DesgloseRevisionResource.class);

    private static final String ENTITY_NAME = "desgloseRevision";

    private final DesgloseRevisionService desgloseRevisionService;

    public DesgloseRevisionResource(DesgloseRevisionService desgloseRevisionService) {
        this.desgloseRevisionService = desgloseRevisionService;
    }

    /**
     * POST  /desglose-revisions : Create a new desgloseRevision.
     *
     * @param desgloseRevisionDTO
     * 		the desgloseRevisionDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseRevisionDTO, or with status 400 (Bad Request) if the desgloseRevision has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-revisions")
    @Timed
    public ResponseEntity<DesgloseRevisionDTO> createDesgloseRevision(@RequestBody DesgloseRevisionDTO desgloseRevisionDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseRevision : {}", desgloseRevisionDTO);
        if (desgloseRevisionDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new desgloseRevision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseRevisionDTO result = desgloseRevisionService.save(desgloseRevisionDTO);
        return ResponseEntity.created(new URI("/api/desglose-revisions/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /desglose-revisions : Updates an existing desgloseRevision.
     *
     * @param desgloseRevisionDTO
     * 		the desgloseRevisionDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseRevisionDTO, or with status 400 (Bad Request) if the desgloseRevisionDTO is not valid, or
     * with status 500 (Internal Server Error) if the desgloseRevisionDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-revisions")
    @Timed
    public ResponseEntity<DesgloseRevisionDTO> updateDesgloseRevision(@RequestBody DesgloseRevisionDTO desgloseRevisionDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseRevision : {}", desgloseRevisionDTO);
        if (desgloseRevisionDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseRevisionDTO result = desgloseRevisionService.save(desgloseRevisionDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseRevisionDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /desglose-revisions : get all the desgloseRevisions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseRevisions in body
     */
    @GetMapping("/desglose-revisions")
    @Timed
    public ResponseEntity<List<DesgloseRevisionDTO>> getAllDesgloseRevisions(Pageable pageable) {
        log.info("REST request to get a page of DesgloseRevisions");
        Page<DesgloseRevisionDTO> page = desgloseRevisionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-revisions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-revisions/:id : get the "id" desgloseRevision.
     *
     * 		the id of the desgloseRevisionDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseRevisionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-revisions/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{codPlantilla}")
    @Timed
    public ResponseEntity<List<DesgloseRevisionDTO>> getDesgloseRevision(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable Integer codPlantilla) {

        NewCommonPK newCommonPK = NewCommonPK.newInstance(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla);
        DesgloseRevisionPK id = DesgloseRevisionPK.newInstance(newCommonPK);

        log.info("REST request to get DesgloseRevision : {}",id);
        List<DesgloseRevisionDTO> objeto = desgloseRevisionService.findOneTotal(id);
        return new ResponseEntity<>(objeto,HttpStatus.OK);
    }
    @GetMapping("/desglose-revisions-indicador-notificaciones")
    @Timed
    public ResponseEntity<Integer> getIndicadorNotifiaciones(){
        log.debug("REST request to get indicador de notificaciones");
        Integer count = desgloseRevisionService.indicadorNotificaciones();
        return  new ResponseEntity<>(count,HttpStatus.OK);
    }
    /**
     * DELETE  /desglose-revisions/:id : delete the "id" desgloseRevision.
     *
     * @param id
     * 		the id of the desgloseRevisionDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-revisions/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseRevision(@PathVariable Long id) {
        DesgloseRevisionPK id1 = new DesgloseRevisionPK();
        log.info("REST request to delete DesgloseRevision : {}", id);
        desgloseRevisionService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/desglose-revisions/vistaReemplazos")
    @Timed
    public ResponseEntity<List<ReemplazosVistaDTO>> VistaReemplazos() {
        log.info("Listamos Los Reemplazos");
        List<ReemplazosVistaDTO> lista = desgloseRevisionService.findAllReemplazosVista();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
}
