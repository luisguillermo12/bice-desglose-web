package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.pk.ValPeriodoDesglosePK;
import cl.tutorial.desglose.service.DesgloseService;
import cl.tutorial.desglose.service.ValPeriodoDesgloseService;
import cl.tutorial.desglose.service.dto.DesgloseDTO;
import cl.tutorial.desglose.service.dto.ValPeriodoDesgloseDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.logging.log4j.*;
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
 * REST controller for managing ValPeriodoDesglose.
 */
@RestController
@RequestMapping("/api")
public class ValPeriodoDesgloseResource {
    private final Logger log = LogManager.getLogger(ValPeriodoDesgloseResource.class);

    private static final String ENTITY_NAME = "valPeriodoDesglose";

    private final ValPeriodoDesgloseService valPeriodoDesgloseService;
    private final DesgloseService desgloseService;

    public ValPeriodoDesgloseResource(ValPeriodoDesgloseService valPeriodoDesgloseService, DesgloseService desgloseService) {

        this.valPeriodoDesgloseService = valPeriodoDesgloseService;
        this.desgloseService = desgloseService;
    }

    /**
     * POST  /val-periodo-desgloses : Create a new valPeriodoDesglose.
     *
     * @param valPeriodoDesgloseDTO
     * 		the valPeriodoDesgloseDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new valPeriodoDesgloseDTO, or with status 400 (Bad Request) if the valPeriodoDesglose has already an
     * ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/val-periodo-desgloses")
    @Timed
    public ResponseEntity<ValPeriodoDesgloseDTO> createValPeriodoDesglose(@Valid @RequestBody ValPeriodoDesgloseDTO valPeriodoDesgloseDTO) throws URISyntaxException {
        log.info("REST request to save ValPeriodoDesglose : {}", valPeriodoDesgloseDTO);
        if (valPeriodoDesgloseDTO.getCodCliente() != null) {
            throw new BadRequestAlertException("A new valPeriodoDesglose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValPeriodoDesgloseDTO result = valPeriodoDesgloseService.save(valPeriodoDesgloseDTO);
        return ResponseEntity.created(new URI("/api/val-periodo-desgloses/" + result.getCodCliente()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodCliente())).body(result);
    }

    /**
     * PUT  /val-periodo-desgloses : Updates an existing valPeriodoDesglose.
     *
     * @param valPeriodoDesgloseDTO
     * 		the valPeriodoDesgloseDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated valPeriodoDesgloseDTO, or with status 400 (Bad Request) if the valPeriodoDesgloseDTO is not valid,
     * or with status 500 (Internal Server Error) if the valPeriodoDesgloseDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/val-periodo-desgloses")
    @Timed
    public ResponseEntity<ValPeriodoDesgloseDTO> updateValPeriodoDesglose(@Valid @RequestBody ValPeriodoDesgloseDTO valPeriodoDesgloseDTO) throws URISyntaxException {
        log.info("REST request to update ValPeriodoDesglose : {}", valPeriodoDesgloseDTO);
        if (valPeriodoDesgloseDTO.getCodCliente() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValPeriodoDesgloseDTO result = valPeriodoDesgloseService.save(valPeriodoDesgloseDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, valPeriodoDesgloseDTO.getCodCliente())).body(result);
    }

    /**
     * GET  /val-periodo-desgloses : get all the valPeriodoDesgloses.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of valPeriodoDesgloses in body
     */
    @GetMapping("/val-periodo-desgloses")
    @Timed
    public ResponseEntity<List<ValPeriodoDesgloseDTO>> getAllValPeriodoDesgloses(Pageable pageable) {
        log.info("REST request to get a page of ValPeriodoDesgloses");
        Page<ValPeriodoDesgloseDTO> page = valPeriodoDesgloseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/val-periodo-desgloses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/val-periodo-desgloses/busquedaValores/{codCliente}/{codTipoDesglose}/{codSubtipoDesglose}/{codMoneda}/{codPlantilla}")
    @Timed
    public ResponseEntity<List<ValPeriodoDesgloseDTO>> getAllValPeriodoDesgloses(@PathVariable String codCliente, @PathVariable Integer codTipoDesglose,
        @PathVariable Integer codSubtipoDesglose, @PathVariable String codMoneda, @PathVariable Integer codPlantilla) {
        log.info("busqueda de valores desglose");
        List<ValPeriodoDesgloseDTO> lista = valPeriodoDesgloseService.findAllBy(codCliente, codTipoDesglose, codSubtipoDesglose, codMoneda, codPlantilla);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * GET  /val-periodo-desgloses/:id : get the "id" valPeriodoDesglose.
     *
     * @param id
     * 		the id of the valPeriodoDesgloseDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the valPeriodoDesgloseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/val-periodo-desgloses/{id}")
    @Timed
    public ResponseEntity<ValPeriodoDesgloseDTO> getValPeriodoDesglose(@PathVariable Long id) {
        ValPeriodoDesglosePK id1 = new ValPeriodoDesglosePK();
        log.info("REST request to get ValPeriodoDesglose : {}", id);
        Optional<ValPeriodoDesgloseDTO> valPeriodoDesgloseDTO = valPeriodoDesgloseService.findOne(id1);
        return ResponseUtil.wrapOrNotFound(valPeriodoDesgloseDTO);
    }

    /**
     * DELETE  /val-periodo-desgloses/:id : delete the "id" valPeriodoDesglose.
     *
     * @param id
     * 		the id of the valPeriodoDesgloseDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/val-periodo-desgloses/{id}")
    @Timed
    public ResponseEntity<Void> deleteValPeriodoDesglose(@PathVariable Long id) {
        ValPeriodoDesglosePK id1 = new ValPeriodoDesglosePK();
        log.info("REST request to delete ValPeriodoDesglose : {}", id);
        valPeriodoDesgloseService.delete(id1);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @DeleteMapping("/val-periodo-desgloses/delete/{codCliente}/{codTipoDesglose}/{codSubtipoDesglose}/{codMoneda}/{codPlantilla}/{corConcepto}/{corValor}")
    @Timed
    public ResponseEntity<Void> deleteValPeriodoDesglose(@PathVariable String codCliente, @PathVariable Integer codTipoDesglose,
        @PathVariable Integer codSubtipoDesglose, @PathVariable String codMoneda, @PathVariable Integer codPlantilla,@PathVariable Integer corConcepto,
        @PathVariable Integer corValor) {

        log.info("REST request to delete ValPeriodoDesglose : {}");

        ValPeriodoDesgloseDTO valPeriodoDesgloseDTO = new ValPeriodoDesgloseDTO();
        valPeriodoDesgloseDTO.setCodCliente(codCliente);
        valPeriodoDesgloseDTO.setCodTipoDes(codTipoDesglose);
        valPeriodoDesgloseDTO.setCodSubtipoDes(codSubtipoDesglose);
        valPeriodoDesgloseDTO.setCodMoneda(codMoneda);
        valPeriodoDesgloseDTO.setCodPlantilla(codPlantilla);
        valPeriodoDesgloseDTO.setCorConcepto(corConcepto);
        valPeriodoDesgloseDTO.setCorValor(corValor);
        valPeriodoDesgloseService.deleteMultiregistro(valPeriodoDesgloseDTO);

        DesgloseDTO dto = new DesgloseDTO();
        dto.setCodCliente(codCliente);
        dto.setCodTipoDes(codTipoDesglose.toString());
        dto.setCodSubtipoDes(codSubtipoDesglose.toString());
        dto.setCodMoneda(codMoneda);
        dto.setCodPlantilla(codPlantilla.toString());
        // desgloseService.executeSpResecuenciarMultiregistros(dto,corConcepto,);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, codPlantilla.toString())).build();
    }
}
