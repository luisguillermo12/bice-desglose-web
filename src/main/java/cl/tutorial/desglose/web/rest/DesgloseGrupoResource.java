package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.DesgloseGrupoPK;
import cl.tutorial.desglose.service.DesgloseGrupoService;
import cl.tutorial.desglose.service.dto.DesgloseGrupoDTO;
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
 * REST controller for managing DesgloseGrupo.
 */
@RestController
@RequestMapping("/api")
public class DesgloseGrupoResource {

    private final Logger log = LogManager.getLogger(DesgloseGrupoResource.class);

    private static final String ENTITY_NAME = "desgloseGrupo";

    private final DesgloseGrupoService desgloseGrupoService;

    public DesgloseGrupoResource(DesgloseGrupoService desgloseGrupoService) {
        this.desgloseGrupoService = desgloseGrupoService;
    }

    /**
     * POST  /desglose-grupos : Create a new desgloseGrupo.
     *
     * @param desgloseGrupoDTO
     * 		the desgloseGrupoDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseGrupoDTO, or with status 400 (Bad Request) if the desgloseGrupo has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desglose-grupos")
    @Timed
    public ResponseEntity<DesgloseGrupoDTO> createDesgloseGrupo(@Valid @RequestBody DesgloseGrupoDTO desgloseGrupoDTO) throws URISyntaxException {
        log.info("REST request to save DesgloseGrupo : {}", desgloseGrupoDTO);
        DesgloseGrupoDTO result = desgloseGrupoService.save(desgloseGrupoDTO);
        return ResponseEntity.created(new URI("/api/desglose-grupos/" + result.getCodCargo())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCargo()))
            .body(result);
    }

    /**
     * PUT  /desglose-grupos : Updates an existing desgloseGrupo.
     *
     * @param desgloseGrupoDTO
     * 		the desgloseGrupoDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseGrupoDTO, or with status 400 (Bad Request) if the desgloseGrupoDTO is not valid, or with
     * status 500 (Internal Server Error) if the desgloseGrupoDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desglose-grupos")
    @Timed
    public ResponseEntity<DesgloseGrupoDTO> updateDesgloseGrupo(@Valid @RequestBody DesgloseGrupoDTO desgloseGrupoDTO) throws URISyntaxException {
        log.info("REST request to update DesgloseGrupo : {}", desgloseGrupoDTO);
        if (desgloseGrupoDTO.getCodCargo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseGrupoDTO result = desgloseGrupoService.save(desgloseGrupoDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desgloseGrupoDTO.getCodCargo())).body(result);
    }

    /**
     * GET  /desglose-grupos : get all the desgloseGrupos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloseGrupos in body
     */
    @GetMapping("/desglose-grupos")
    @Timed
    public ResponseEntity<List<DesgloseGrupoDTO>> getAllDesgloseGrupos(Pageable pageable) {
        log.info("REST request to get a page of DesgloseGrupos");
        Page<DesgloseGrupoDTO> page = desgloseGrupoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desglose-grupos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desglose-grupos/:id : get the "id" desgloseGrupo.
     *
     * @param id
     * 		the id of the desgloseGrupoDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseGrupoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desglose-grupos/{id}")
    @Timed
    public ResponseEntity<DesgloseGrupoDTO> getDesgloseGrupo(@PathVariable Long id) {
        DesgloseGrupoPK id1 = new DesgloseGrupoPK();
        log.info("REST request to get DesgloseGrupo : {}", id);
        Optional<DesgloseGrupoDTO> desgloseGrupoDTO = desgloseGrupoService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(desgloseGrupoDTO);
    }

    @GetMapping("/desglose-grupos-byRolEmpleado/{rolEmpleado}")
    public ResponseEntity<List<DesgloseGrupoDTO>> findAllByNumRolEmpleado(@PathVariable String rolEmpleado) {
        List<DesgloseGrupoDTO> desgloseGrupoDTOList = desgloseGrupoService.findAllByNumRolEmpleado(rolEmpleado);
        return new ResponseEntity<>(desgloseGrupoDTOList, HttpStatus.OK);
    }

    /**
     * DELETE  /desglose-grupos/:id : delete the "id" desgloseGrupo.
     *
     * @param id
     * 		the id of the desgloseGrupoDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desglose-grupos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesgloseGrupo(@PathVariable Long id) {
        DesgloseGrupoPK id1 = new DesgloseGrupoPK();
        log.info("REST request to delete DesgloseGrupo : {}", id);
        desgloseGrupoService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @param codCargo
     * @param rolEmpleado
     * @param desCodCargo
     * @param numRolEmpleado
     *
     * @return
     *
     * @author carlos eduardo rodriguez perez
     */
    @DeleteMapping("/desglose-grupos/eliminaEntidad/{codCargo}/{rolEmpleado}/{desCodCargo}/{numRolEmpleado}")
    @Timed
    public ResponseEntity<Void> deleteDesglose(@PathVariable String codCargo, @PathVariable String rolEmpleado, @PathVariable String desCodCargo,
        @PathVariable String numRolEmpleado) {
        log.info("Eliminamos la entidad DesgloseGrupos");
        DesgloseGrupoDTO desgloseGrupoDto = new DesgloseGrupoDTO();
        desgloseGrupoDto.setCodCargo(codCargo);
        desgloseGrupoDto.setDesCodCargo(desCodCargo);
        desgloseGrupoDto.setNumRolEmpleado(rolEmpleado);
        desgloseGrupoDto.setDesNumRolEmpleado(numRolEmpleado);
        desgloseGrupoService.deleteByDesgloseGrupo(desgloseGrupoDto);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }

    @DeleteMapping("/desglose-grupos/deleteByUser/{idUser}")
    @Timed
    public ResponseEntity<Void> deleteByUserIdEmpleado(@PathVariable Long idUser) {
        desgloseGrupoService.deleteByUserIdEmpleado(idUser);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idUser.toString())).build();
    }

    @GetMapping("/desglose-grupos-findFirtByRolEjecutor/{rolEmpleado}")
    public ResponseEntity<DesgloseGrupoDTO> findFirstByDesNumRolEmpleado(@PathVariable String rolEmpleado) {
        DesgloseGrupoDTO desgloseGrupoDTO = desgloseGrupoService.findFirstByDesNumRolEmpleado(rolEmpleado);
        return new ResponseEntity<>(desgloseGrupoDTO, HttpStatus.OK);
    }
}
