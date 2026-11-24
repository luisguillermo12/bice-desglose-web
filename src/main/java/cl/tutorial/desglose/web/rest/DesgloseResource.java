package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import cl.tutorial.desglose.repository.ValPeriodoProyeccionDesgloseRepository;
import cl.tutorial.desglose.service.DesgloseService;
import cl.tutorial.desglose.service.dto.*;
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

import static cl.tutorial.desglose.config.Constants.*;

/**
 * REST controller for managing Desglose.
 */
@RestController
@RequestMapping("/api")
public class DesgloseResource {

    private final Logger log = LogManager.getLogger(DesgloseResource.class);

    private static final String ENTITY_NAME = "desglose";

    private final DesgloseService                        desgloseService;
    private final    ValPeriodoProyeccionDesgloseRepository valPeriodoProyeccionDesgloseRepository;
    public DesgloseResource(ValPeriodoProyeccionDesgloseRepository valPeriodoProyeccionDesgloseRepository,DesgloseService desgloseService) {
        this.desgloseService = desgloseService;
        this.valPeriodoProyeccionDesgloseRepository = valPeriodoProyeccionDesgloseRepository;
    }

    /**
     * POST  /desgloses : Create a new desglose.
     *
     * @param desgloseDTO
     * 		the desgloseDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseDTO, or with status 400 (Bad Request) if the desglose has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desgloses")
    @Timed
    public ResponseEntity<DesgloseDTO> createDesglose(@Valid @RequestBody DesgloseDTO desgloseDTO) throws URISyntaxException {
        log.info("REST request to createOrUpdate Desglose : {}", desgloseDTO);
        if (!ObjectUtils.allNotNull(desgloseDTO.getCodCliente(), desgloseDTO.getCodTipoDes(), desgloseDTO.getCodSubtipoDes(), desgloseDTO.getCodMoneda())) {
            throw new BadRequestAlertException("A new cuadre must have a valid ID", ENTITY_NAME, "notValidId");
        }

        NewCommonPK id = NewCommonPK.newInstance(desgloseDTO);
        if (desgloseService.findOne(id,null).isPresent()) {
            throw new BadRequestAlertException("A new desglose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesgloseDTO result = desgloseService.createOrUpdate(desgloseDTO);
        final String entityId = HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes(), result.getCodSubtipoDes(), result.getCodMoneda());
        return ResponseEntity
            .created(new URI("/api/desgloses/" + result.getCodCliente() + "/" + result.getCodTipoDes() + "/" + result.getCodSubtipoDes() + "/" + result.getCodMoneda()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * POST  /desgloses-save : Save all information about a new desglose.
     *
     * @param breakdownSaveRequestDTO
     * 		the desgloseDTO to save
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new desgloseDTO, or with status 400 (Bad Request) if the desglose has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desgloses-save")
    @Timed
    public ResponseEntity<DesgloseDTO> saveDesglose(@Valid @RequestBody BreakdownSaveRequestDTO breakdownSaveRequestDTO) throws URISyntaxException {
        log.info("REST request to save BreakdownSaveRequestDTO : {}", breakdownSaveRequestDTO);
        DesgloseDTO result;
        BreakdownConceptValidationRequestDTO requestDTO = new BreakdownConceptValidationRequestDTO();
        requestDTO.extraDataDTO = breakdownSaveRequestDTO.extraDataDTO;
        requestDTO.activePeriodsIndexes = breakdownSaveRequestDTO.isIndicePeriodos;
        requestDTO.skipNotRequiredValidations = breakdownSaveRequestDTO.skipNotRequiredValidations;
        // Si la variable requestDTO.extraDataDTO.canValidate es falsa no se realizan validaciones en validateBreakdown().
        desgloseService.checkEmptyFieldsInBreakdown(breakdownSaveRequestDTO.extraDataDTO);
        List<BreakdownValidationErrorDTO> validationErrorDTOS = desgloseService.validateBreakdown(requestDTO);
        Integer isRequiredCount = 0;
        for(BreakdownValidationErrorDTO item :validationErrorDTOS){
            if(item.getIsRequired()) {
                isRequiredCount ++;
            }
        }
        if (validationErrorDTOS.size() == 0 || isRequiredCount == 0 ) {
            result = desgloseService.save(breakdownSaveRequestDTO);
            if (!result.getCodEstado().equals(ESTADO_DEFINITIVO) && !result.getCodEstado().equals(Constants.ESTADO_CORREGIDO)) {
                if(!result.getCodEstado().equals(ESTADO_BORRADOR_EN_PROCESO)) {
                    desgloseService.createOrUpdate(result);
                } else {
                    result.setNomUserOpen(PROCESS_USER_OPEN + breakdownSaveRequestDTO.desgloseDTO.getNomUserOpen());
                    desgloseService.convertDtoToEntityAndUpdate(result, false);
                }
            }
            desgloseService.updateRevisorModificaDesglose(breakdownSaveRequestDTO.desgloseDTO);
        } else {
            NewCommonPK id = NewCommonPK.newInstance(breakdownSaveRequestDTO.desgloseDTO);
            DesgloseDTO desgloseDTO = desgloseService.findOne(id,null).get();
            desgloseDTO.setCodEstado(ESTADO_BORRADOR_EN_PROCESO);
            desgloseService.createOrUpdate(desgloseDTO);
            throw new BadRequestAlertException("Invalid id. Validaciones no correctas", ENTITY_NAME, "validaciones no correctas");

        }
        if (result == null) {
            return ResponseEntity.badRequest().body(result);
        }
        // TODO: Corregir esto porque desde el save debería venir bien el objeto. No viene con el estado actualizado por esto se hace esta variante de guardar y luego buscar en
        //  el Controller.
        result = desgloseService.updateBreakdownGlsStatus(result);
        final String entityId =
            HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes(), result.getCodSubtipoDes(), result.getCodMoneda(), result.getCodPlantilla());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }
    @PostMapping("/desgloses-save-pry")
    @Timed
    public ResponseEntity<DesgloseDTO> saveDesglosePry(@RequestBody BreakdownSaveRequestDTO breakdownSaveRequestDTO) throws URISyntaxException {
        log.info("REST request to save BreakdownSaveRequestDTO pry : {}", breakdownSaveRequestDTO);
        DesgloseDTO result;
        desgloseService.deleteProyecciones(breakdownSaveRequestDTO);
        result = desgloseService.savePry(breakdownSaveRequestDTO);
        final String entityId =
            HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes(), result.getCodSubtipoDes(), result.getCodMoneda(), result.getCodPlantilla());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * @param requestDTO
     *
     * @return
     *
     * @throws URISyntaxException
     */
    @PostMapping("/desgloses-validations")
    @Timed
    public ResponseEntity<List<BreakdownValidationErrorDTO>> validateBreakdown(@Valid @RequestBody BreakdownConceptValidationRequestDTO requestDTO) throws URISyntaxException {
        log.info("REST request to validaciones extraDataDTO : {}", requestDTO);
        // Si la variable requestDTO.extraDataDTO.canValidate es falsa no se realizan validaciones en validateBreakdown().
        desgloseService.checkEmptyFieldsInBreakdown(requestDTO.extraDataDTO);
        List<BreakdownValidationErrorDTO> result = desgloseService.validateBreakdown(requestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/desgloses-approve")
    @Timed
    public ResponseEntity<DesgloseDTO> approveBreakdown(@RequestBody BreakdownSendRequestDTO breakdownSendRequestDTO) throws URISyntaxException {
        log.info("REST request to approveBreakdown");
        DesgloseDTO desgloseDTO;
        // Se quitan las validaciones de aquí porque no es necesario. Ya está cuadrado este desglose al grabar.
        // List<BreakdownValidationErrorDTO> listaValidaciones = desgloseService.validateBreakdown(breakdownSendRequestDTO.extraDataDesglose);
        // if (listaValidaciones.size() == 0) {
        desgloseDTO = desgloseService.aprove(breakdownSendRequestDTO);
        desgloseService.createOrUpdate(desgloseDTO);
        /*} else {
            throw new BadRequestAlertException("Invalid id. Validaciones no correctas", ENTITY_NAME, "validaciones no correctas");
        }*/
        return new ResponseEntity<>(desgloseDTO, HttpStatus.OK);
    }

    @PostMapping("/desgloses-review")
    @Timed
    public ResponseEntity<DesgloseDTO> sendReview(@RequestBody BreakdownSendRequestDTO breakdownSendRequestDTO) throws URISyntaxException {
        log.info("REST request to reviewDesglose");
        DesgloseDTO desgloseDTO;
        // List<BreakdownValidationErrorDTO> listaValidaciones = desgloseService.validateBreakdown(breakdownSendRequestDTO.extraDataDesglose);
        // if (listaValidaciones.size() == 0) {
        desgloseDTO = desgloseService.sendToReview(breakdownSendRequestDTO);
        desgloseService.createOrUpdate(desgloseDTO);
        /*} else {
            throw new BadRequestAlertException("Invalid id. Validaciones no correctas", ENTITY_NAME, "validaciones no correctas");
        }*/
        return new ResponseEntity<>(desgloseDTO, HttpStatus.OK);
    }

    @PostMapping("/desgloses-executor")
    @Timed
    public ResponseEntity<DesgloseDTO> sendExecutor(@RequestBody BreakdownSendRequestDTO breakdownSendRequestDTO) throws URISyntaxException {
        log.info("REST request to executorDesglose");
        DesgloseDTO desgloseDTO;
        // List<BreakdownValidationErrorDTO> listaValidaciones = desgloseService.validateBreakdown(breakdownSendRequestDTO.extraDataDesglose);
        // if (listaValidaciones.size() == 0) {
        desgloseDTO = desgloseService.sendExecutor(breakdownSendRequestDTO);
        desgloseService.createOrUpdate(desgloseDTO);
        /*} else {
            throw new BadRequestAlertException("Invalid id. Validaciones no correctas", ENTITY_NAME, "validaciones no correctas");
        }*/
        return new ResponseEntity<>(desgloseDTO, HttpStatus.OK);
    }

    /**
     * POST  /desgloses-clone : Clone all information about a new desglose.
     *
     * @param cloneRequestDTO
     * 		the BreakdownCloneRequestDTO to clone
     *
     * @return the ResponseEntity with status 200 (OK) and the list of BreakdownRecoverResponseDTO in body
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/desgloses-clone")
    @Timed
    public ResponseEntity<BreakdownRecoverResponseDTO> cloneDesglose(@Valid @RequestBody BreakdownCloneRequestDTO cloneRequestDTO) throws URISyntaxException {
        log.info("REST request to clone BreakdownCloneRequestDTO : {}", cloneRequestDTO);
        BreakdownRecoverResponseDTO responseDTO = desgloseService.clone(cloneRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        // return ResponseEntity.ok().headers(HeaderUtil.createEntityClonedAlert(ENTITY_NAME, entityId)).build();
        // return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * @param desgloseDTO
     *
     * @return
     *
     * @throws URISyntaxException
     */
    @PostMapping("/desgloses-delete")
    @Timed
    public ResponseEntity<Void> deleteDesglose(@RequestBody DesgloseDTO desgloseDTO) throws URISyntaxException {
        log.info("REST request to delete desgloseDTO : {}", desgloseDTO);
        desgloseService.deleteDesglose(desgloseDTO);
        final String entityId = HeaderUtil
            .createHeaderEntityId(desgloseDTO.getCodCliente(), desgloseDTO.getCodPlantilla(), desgloseDTO.getCodTipoDes(), desgloseDTO.getCodSubtipoDes(),
                desgloseDTO.getCodMoneda());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, entityId)).build();
        // return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * PUT  /desgloses : Updates an existing desglose.
     *
     * @param desgloseDTO
     * 		the desgloseDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseDTO, or with status 400 (Bad Request) if the desgloseDTO is not valid, or with status 500
     * (Internal Server Error) if the desgloseDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desgloses")
    @Timed
    public ResponseEntity<DesgloseDTO> updateDesglose(@RequestBody DesgloseDTO desgloseDTO) throws URISyntaxException {
        log.info("REST request to update Desglose : {}", desgloseDTO);
        if (!ObjectUtils.allNotNull(desgloseDTO.getCodCliente(), desgloseDTO.getCodTipoDes(), desgloseDTO.getCodSubtipoDes(), desgloseDTO.getCodMoneda())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        NewCommonPK id = NewCommonPK.newInstance(desgloseDTO);
        if (!desgloseService.findOne(id,null).isPresent()) {
            throw new BadRequestAlertException("Invalid id. Entity not found", ENTITY_NAME, "idNotFound");
        }
        DesgloseDTO result = desgloseService.createOrUpdateMarcar(desgloseDTO);
        final String entityId = HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes(), result.getCodSubtipoDes(), result.getCodMoneda());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }

    @PostMapping("/desgloses-free")
    @Timed
    public ResponseEntity<DesgloseDTO> clearBreakdownUserOpenAndSetStatusInProcess(@RequestBody DesgloseDTO desgloseDTO) throws URISyntaxException {
        log.info("REST request to update Desglose User Open: {}", desgloseDTO);
        NewCommonPK id = NewCommonPK.newInstance(desgloseDTO);
        final Optional<DesgloseDTO> desgloseServiceOne = desgloseService.findOne(id,null);
        DesgloseDTO result = null;
        if (desgloseServiceOne.isPresent()) {
            // throw new BadRequestAlertException("Invalid id. Entity not found", ENTITY_NAME, "idNotFound");
            final TurbineUserDTO user = desgloseService.getTurbineUserDTOAndSecurityCheck(null);
            if (ESTADO_EN_EDICION.equalsIgnoreCase(desgloseDTO.getCodEstado())) {
                if (desgloseDTO.getNomUserOpen() != null && desgloseDTO.getNomUserOpen().equalsIgnoreCase(user.getLoginName())) {
                    if (!desgloseDTO.isFlgIsTemporal()) {
                        result = desgloseService.clearBreakdownUserOpenAndSetStatusInProcess(desgloseServiceOne.get());
                    }
                }
            } else {
                result = desgloseService.convertDtoToEntityAndUpdate(desgloseServiceOne.get(), true);
            }
        }
        desgloseService.deleteIsTemporal();
        return new ResponseEntity<>(result, HttpStatus.OK);
        // final String entityId = HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes(), result.getCodSubtipoDes(), result.getCodMoneda());
        // return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }
    @PostMapping("/desgloses-free-temporal")
    @Timed
    public ResponseEntity<Void> learBreakdownUserOpenAndSetStatusInProcessTemporal() throws URISyntaxException {
        log.info("REST request to update Desglose  delete temporales User Open: {}");
        desgloseService.deleteIsTemporal();
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    /**
     * PUT  /desgloses-edit : Updates an existing desglose according to the business logic.
     *
     * @param breakdownEditRequestDTO
     * 		the dreakdownEditRequestDTO param
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated desgloseDTO, or with status 400 (Bad Request) if the desgloseDTO is not valid, or with status 500
     * (Internal Server Error) if the desgloseDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/desgloses-edit")
    @Timed
    public ResponseEntity<DesgloseDTO> editDesglose(@Valid @RequestBody BreakdownEditRequestDTO breakdownEditRequestDTO) throws URISyntaxException {
        log.info("REST request to edit Desglose : {}", breakdownEditRequestDTO);
        if (!ObjectUtils
            .allNotNull(breakdownEditRequestDTO.desglose.getCodCliente(), breakdownEditRequestDTO.desglose.getCodTipoDes(), breakdownEditRequestDTO.desglose.getCodSubtipoDes(),
                breakdownEditRequestDTO.desglose.getCodMoneda())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesgloseDTO result = desgloseService.edit(breakdownEditRequestDTO.desglose, breakdownEditRequestDTO.req2Firma, breakdownEditRequestDTO.isNewApprobationProcess);
        desgloseService.updateRevisorModificaDesglose(result);
        final String entityId =
            HeaderUtil.createHeaderEntityId(result.getCodCliente(), result.getCodTipoDes(), result.getCodSubtipoDes(), result.getCodMoneda(), result.getCodPlantilla());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityId)).body(result);
    }

    /**
     * GET  /desgloses : get all the desgloses.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of desgloses in body
     */
    @GetMapping("/desgloses")
    @Timed
    public ResponseEntity<List<DesgloseDTO>> getAllDesgloses(Pageable pageable) {
        log.info("REST request to get a page of Desgloses");
        Page<DesgloseDTO> page = desgloseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desgloses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desgloses/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda : get the "id" desglose.
     *
     * @param codCliente
     * 		the id of the desgloseDTO to retrieve
     * @param codTipoDes
     * 		the id of the desgloseDTO to retrieve
     * @param codSubtipoDes
     * 		the id of the desgloseDTO to retrieve
     * @param codMoneda
     * 		the id of the desgloseDTO to retrieve
     * @param codPlantilla
     * 		the id of the desgloseDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the desgloseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desgloses/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{codPlantilla}")
    @Timed
    public ResponseEntity<DesgloseDTO> getDesglose(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable Integer codPlantilla) {

        NewCommonPK id = NewCommonPK.newInstance(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla);

        log.info("REST request to get Desglose : {}", id);
        Optional<DesgloseDTO> desgloseDTO = desgloseService.findOne(id,null);
        return ResponseUtil.wrapOrNotFound(desgloseDTO);
    }

    /**
     * DELETE  /desgloses/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda : delete the "id" desglose.
     *
     * @param codCliente
     * 		the id of the desgloseDTO to retrieve
     * @param codTipoDes
     * 		the id of the desgloseDTO to retrieve
     * @param codSubtipoDes
     * 		the id of the desgloseDTO to retrieve
     * @param codMoneda
     * 		the id of the desgloseDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desgloses/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{codPlantilla}")
    @Timed
    public ResponseEntity<Void> deleteDesglose(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable Integer codPlantilla) {

        NewCommonPK id = NewCommonPK.newInstance(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla);

        log.info("REST request to delete Desglose : {}", id);
        desgloseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /desgloses-filter : get all the BreakdownFilterResponseDTO filtered by the param.
     *
     * @param breakdownFilterRequestDTO
     * 		objeto con los valores del filtro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of BreakdownFilterResponseDTO in body
     */
    @PostMapping("/desgloses-filter")
    @Timed
    public ResponseEntity<List<BreakdownFilterResponseDTO>> filterBreakdown(@Valid @RequestBody BreakdownFilterRequestDTO breakdownFilterRequestDTO) {
        log.info("REST request to get filtered Desgloses with filter: {}", breakdownFilterRequestDTO.toString());
        List<BreakdownFilterResponseDTO> page = desgloseService.filterBreakdown(breakdownFilterRequestDTO);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * POST  /desgloses-search : get all the BreakdownSearchResponseDTO filtered by the param.
     *
     * @param breakdownSearchRequestDTO
     * 		objeto con los valores del filtro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of BreakdownSearchResponseDTO in body
     */
    @PostMapping("/desgloses-search")
    @Timed
    public ResponseEntity<List<BreakdownSearchResponseDTO>> searchBreakdown(@Valid @RequestBody BreakdownSearchRequestDTO breakdownSearchRequestDTO) {
        log.info("REST request to get filtered Desgloses with filter: {}", breakdownSearchRequestDTO);
        List<BreakdownSearchResponseDTO> page = desgloseService.searchBreakdown(breakdownSearchRequestDTO,null);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * POST  /desgloses-recover : recovers all the breakdown data .
     *
     * @param breakdownSearchRequestDTO
     * 		objeto con los valores del filtro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of BreakdownRecoverResponseDTO in body
     */
    @PostMapping("/desgloses-recover")
    @Timed
    public ResponseEntity<BreakdownRecoverResponseDTO> recoverBreakdown(@Valid @RequestBody BreakdownSearchRequestDTO breakdownSearchRequestDTO) {
        log.info("REST request to recover Desgloses with breakdownSearchRequestDTO: {}", breakdownSearchRequestDTO);
        BreakdownRecoverResponseDTO responseDTO = desgloseService.recoverBreakdown(breakdownSearchRequestDTO);
        if(responseDTO.extraDataDTO.nomUserOpen!= null && responseDTO.extraDataDTO.nomUserOpen.contains(PROCESS_USER_OPEN)) {
            responseDTO.extraDataDTO.nomUserOpen = responseDTO.extraDataDTO.nomUserOpen.replaceAll(PROCESS_USER_OPEN,"");
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
