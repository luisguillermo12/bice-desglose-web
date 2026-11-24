package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseMensajePK;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import cl.tutorial.desglose.service.DesgloseMensajeService;
import cl.tutorial.desglose.service.dto.DesgloseMensajeDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing DesgloseMensaje.
 */
@RestController
@RequestMapping("/api")
public class DesgloseMensajeResource {

    private final Logger log = LogManager.getLogger(DesgloseMensajeResource.class);

    private static final String ENTITY_NAME = "desgloseMensaje";

    private final DesgloseMensajeService desgloseMensajeService;

    public DesgloseMensajeResource(DesgloseMensajeService desgloseMensajeService) {
        this.desgloseMensajeService = desgloseMensajeService;
    }

    /**
     * POST  /desglose-mensajes : Create a new desgloseMensaje.
     *
     * @param desgloseMensajeDTO
     * 		the desgloseMensajeDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseMensajeDTO, or with status 400 (Bad Request) if the desgloseMensaje has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-mensajes")
    @Timed
    public ResponseEntity<DesgloseMensajeDTO> createDesgloseMensaje(@Valid @RequestBody DesgloseMensajeDTO desgloseMensajeDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseMensaje : {}", desgloseMensajeDTO);
        if (desgloseMensajeDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new desgloseMensaje cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseMensajeDTO result = desgloseMensajeService.save(desgloseMensajeDTO);
        return ResponseEntity.created(new URI("/api/desglose-mensajes/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /desglose-mensajes : Updates an existing desgloseMensaje.
     *
     * @param desgloseMensajeDTO
     * 		the desgloseMensajeDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseMensajeDTO, or with status 400 (Bad Request) if the desgloseMensajeDTO is not valid, or
     * with status 500 (Internal Server Error) if the desgloseMensajeDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-mensajes")
    @Timed
    public ResponseEntity<DesgloseMensajeDTO> updateDesgloseMensaje(@Valid @RequestBody DesgloseMensajeDTO desgloseMensajeDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseMensaje : {}", desgloseMensajeDTO);
        if (desgloseMensajeDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseMensajeDTO result = desgloseMensajeService.save(desgloseMensajeDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseMensajeDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /desglose-mensajes : get all the desgloseMensajes.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseMensajes in body
     */
    @GetMapping("/desglose-mensajes")
    @Timed
    public ResponseEntity<List<DesgloseMensajeDTO>> getAllDesgloseMensajes(Pageable pageable) {
        log.info("REST request to get a page of DesgloseMensajes");
        Page<DesgloseMensajeDTO> page = desgloseMensajeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-mensajes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @param codCliente
     * @param codTipoDes
     * @param codSubtipoDes
     * @param codMoneda
     * @param codPlantilla
     *
     * @return
     */
    @GetMapping("/desglose-mensajes/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{codPlantilla}")
    @Timed
    public ResponseEntity<List<DesgloseMensajeDTO>> getDesgloseMensaje(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable Integer codPlantilla) {
        DesgloseMensajePK id = new DesgloseMensajePK();
        id.setCodPlantilla(codPlantilla);
        id.setCodCliente(codCliente);
        id.setCodMoneda(codMoneda);
        id.setCodSubtipoDes(codSubtipoDes);
        id.setCodTipoDes(codTipoDes);
        log.info("REST request to get DesgloseMensaje : {}", id);
        List<DesgloseMensajeDTO> desgloseMensajeDTO = desgloseMensajeService.findAllById(id);
        return new ResponseEntity<>(desgloseMensajeDTO, HttpStatus.OK);
    }

    /**
     * DELETE  /desglose-mensajes/:id : delete the "id" desgloseMensaje.
     *
     * @param id
     * 		the id of the desgloseMensajeDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-mensajes/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseMensaje(@PathVariable Long id) {
        NewCommonPK id1 = new NewCommonPK();
        log.info("REST request to delete DesgloseMensaje : {}", id);
        desgloseMensajeService.delete(DesgloseMensajePK.newInstance(id1));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
