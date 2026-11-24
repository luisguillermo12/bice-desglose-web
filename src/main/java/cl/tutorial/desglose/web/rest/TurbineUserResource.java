package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.DesgloseReemplazo;
import cl.tutorial.desglose.domain.SfStfOperator;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUser;
import cl.tutorial.desglose.repository.DesgloseReemplazoRepository;
import cl.tutorial.desglose.repository.SfStfOperatorRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserRepository;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.TurbineUserService;
import cl.tutorial.desglose.service.dto.TurbineUserDTO;
import cl.tutorial.desglose.service.dto.TurbineUsuarioDTO;
import cl.tutorial.desglose.service.dto.UserDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.errors.InternalServerErrorException;
import cl.tutorial.desglose.web.rest.errors.LoginReplacementException;
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
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static cl.tutorial.desglose.config.Constants.CARGO_2;
import static cl.tutorial.desglose.config.Constants.COD_REG_STATUS_ZERO;

/**
 * REST controller for managing TurbineUser.
 */
@RestController
@RequestMapping("/api")
public class TurbineUserResource {

    private final Logger log = LogManager.getLogger(TurbineUserResource.class);

    private static final String ENTITY_NAME = "turbineUser";

    private final TurbineUserService turbineUserService;

    private final DesgloseReemplazoRepository desgloseReemplazoRepository;
    private final SfStfOperatorRepository     sfStfOperatorRepository;
    private final TurbineUserRepository       turbineUserRepository;

    public TurbineUserResource(TurbineUserService turbineUserService, TurbineUserRepository turbineUserRepository, DesgloseReemplazoRepository desgloseReemplazoRepository,
        SfStfOperatorRepository sfStfOperatorRepository) {
        this.turbineUserService = turbineUserService;
        this.desgloseReemplazoRepository = desgloseReemplazoRepository;
        this.sfStfOperatorRepository = sfStfOperatorRepository;
        this.turbineUserRepository = turbineUserRepository;
    }

    /**
     * POST  /turbine-users : Create a new turbineUser.
     *
     * @param turbineUserDTO
     * 		the turbineUserDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new turbineUserDTO, or with status 400 (Bad Request) if the turbineUser has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/turbine-users")
    @Timed
    public ResponseEntity<TurbineUserDTO> createTurbineUser(@Valid @RequestBody TurbineUserDTO turbineUserDTO) throws URISyntaxException {
        log.info("REST request to save TurbineUser : {}", turbineUserDTO);
        if (turbineUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new turbineUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurbineUserDTO result = turbineUserService.save(turbineUserDTO);
        return ResponseEntity.created(new URI("/api/turbine-users/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turbine-users : Updates an existing turbineUser.
     *
     * @param turbineUserDTO
     * 		the turbineUserDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated turbineUserDTO, or with status 400 (Bad Request) if the turbineUserDTO is not valid, or with status
     * 500 (Internal Server Error) if the turbineUserDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/turbine-users")
    @Timed
    public ResponseEntity<TurbineUserDTO> updateTurbineUser(@Valid @RequestBody TurbineUserDTO turbineUserDTO) throws URISyntaxException {
        log.info("REST request to update TurbineUser : {}", turbineUserDTO);
        if (turbineUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurbineUserDTO result = turbineUserService.save(turbineUserDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turbineUserDTO.getId().toString())).body(result);
    }

    /**
     * GET  /turbine-users : get all the turbineUsers.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of turbineUsers in body
     */
    @GetMapping("/turbine-users")
    @Timed
    public ResponseEntity<List<TurbineUserDTO>> getAllTurbineUsers(Pageable pageable) {
        log.info("REST request to get a page of TurbineUsers");
        Page<TurbineUserDTO> page = turbineUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turbine-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /turbine-users/:id : get the "id" turbineUser.
     *
     * @param id
     * 		the id of the turbineUserDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the turbineUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turbine-users/{id}")
    @Timed
    public ResponseEntity<TurbineUserDTO> getTurbineUser(@PathVariable Long id) {
        log.info("REST request to get TurbineUser : {}", id);
        Optional<TurbineUserDTO> turbineUserDTO = turbineUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turbineUserDTO);
    }

    /**
     * DELETE  /turbine-users/:id : delete the "id" turbineUser.
     *
     * @param id
     * 		the id of the turbineUserDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turbine-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurbineUser(@PathVariable Long id) {
        log.info("REST request to delete TurbineUser : {}", id);
        turbineUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * isCanDeleteReviewer Carlos Eduardo Rodriguez Perez
     *
     * @param idUsuario
     *
     * @return
     */
    @GetMapping("/turbine-users/isCanDeleteRevisor/{idUsuario}")
    @Timed
    public ResponseEntity<BigDecimal> isCanDeleteRevisor(@PathVariable Long idUsuario) {
        log.info("REST request to get isCanDeleteReviewer : {}", idUsuario);
        BigDecimal Cont;
        Cont = turbineUserService.isCanDeleteReviewer(idUsuario);
        return new ResponseEntity<>(Cont, HttpStatus.OK);
    }

    @GetMapping("/turbine-users/finAllReemplazar/{codCargo}")
    @Timed
    public ResponseEntity<List<TurbineUsuarioDTO>> finAllReemplazar(@PathVariable String codCargo) {
        log.debug("Listando los Miembros a reemplazar");
        List<TurbineUsuarioDTO> lista = turbineUserService.finAllToReplace(codCargo);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/turbine-users/finAllReemplazos/{operatorCode}")
    @Timed
    public ResponseEntity<List<TurbineUsuarioDTO>> finAllReemplazos(@PathVariable Long operatorCode) {
        log.info("Listando los Reemplazos ");
        List<TurbineUsuarioDTO> list = turbineUserService.finAllReplacements(operatorCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/turbine-users/findByLoginName/{loginName}")
    @Timed
    public ResponseEntity<TurbineUserDTO> finByLoginName(@PathVariable String loginName) {
        log.info("Buscando un Usuario por su loginName");
        TurbineUserDTO turbineUserDTO = turbineUserService.findByLoginName(loginName);
        return new ResponseEntity<>(turbineUserDTO, HttpStatus.OK);
    }

    @SuppressWarnings("Duplicates")
    @GetMapping("/turbine-users/findByLoginNameSfStf/{loginName}")
    @Timed
    public ResponseEntity<TurbineUserDTO> finByLoginName1(@PathVariable String loginName) {
        log.info("Buscando un Usuario por su loginName");
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        final UserDTO userDTO =
            turbineUserRepository.findOneByLoginName(userLogin).map(UserDTO::new).orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        final SfStfOperator stfOperator = sfStfOperatorRepository.findFirstByLoginName(userLogin);
        if (stfOperator != null && stfOperator.getOperatorCode() != null) {
            userDTO.setStfOperatorCode(stfOperator.getOperatorCode());
        } else {
            throw new InternalServerErrorException("The user can not be found un the SF_STF scheme");
        }
        final DesgloseReemplazo desgloseReemplazo =
            desgloseReemplazoRepository.findFirstByCodCargoAndNumRolEmpleadoAndCodRegStatus(CARGO_2, stfOperator.getOperatorCode().toString(), COD_REG_STATUS_ZERO);
        if (desgloseReemplazo != null) {
            Integer operatorCodeReemplazo = Integer.parseInt(desgloseReemplazo.getDesNumRolEmpleado());
            Optional<SfStfOperator> stfOperatorReemplaso = sfStfOperatorRepository.findOneByOperatorCode(operatorCodeReemplazo);
            Optional<TurbineUser> turbineUser;
            String replacementUserName = "";
            if (stfOperatorReemplaso.isPresent()) {
                turbineUser = turbineUserRepository.findOneByLoginName(stfOperatorReemplaso.get().getLoginName());
                replacementUserName = turbineUser.get().getFullName();
            }
            throw new LoginReplacementException(replacementUserName);
        }
        TurbineUserDTO turbineUserDTO = turbineUserService.findByLoginName(loginName);
        return new ResponseEntity<>(turbineUserDTO, HttpStatus.OK);
    }

    @GetMapping("/turbine-users/findByRole/{role}")
    @Timed
    public ResponseEntity<List<TurbineUsuarioDTO>> finByRole(@PathVariable String role) {
        log.info("Buscando un Usuario role");
        List<TurbineUsuarioDTO> lista = turbineUserService.findByRole(role);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/turbine-users/findByRoleIsReemplazo/{role}")
    @Timed
    public ResponseEntity<TurbineUsuarioDTO> findByRoleIsReemplazo(@PathVariable String role) {
        log.info("Buscando un Usuario role");
        TurbineUsuarioDTO turbineUsuarioDTO = turbineUserService.isReplacementMember(role);
        return new ResponseEntity<>(turbineUsuarioDTO, HttpStatus.OK);
    }
}
