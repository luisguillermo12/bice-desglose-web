package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.dto.TurbineUsuarioDTO;
import com.codahale.metrics.annotation.Timed;
import cl.tutorial.desglose.service.TurbineUserGroupRoleService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import cl.tutorial.desglose.service.dto.TurbineUserGroupRoleDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.checkerframework.checker.units.qual.Time;
import org.hibernate.exception.SQLGrammarException;
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
 * REST controller for managing TurbineUserGroupRole.
 */
@RestController
@RequestMapping("/api")
public class TurbineUserGroupRoleResource {

    private final Logger log = LogManager.getLogger(TurbineUserGroupRoleResource.class);

    private static final String ENTITY_NAME = "turbineUserGroupRole";

    private final TurbineUserGroupRoleService turbineUserGroupRoleService;

    public TurbineUserGroupRoleResource(TurbineUserGroupRoleService turbineUserGroupRoleService) {
        this.turbineUserGroupRoleService = turbineUserGroupRoleService;
    }

    /**
     * POST  /turbine-user-group-roles : Create a new turbineUserGroupRole.
     *
     * @param turbineUserGroupRoleDTO
     * 		the turbineUserGroupRoleDTO to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new turbineUserGroupRoleDTO, or with status 400 (Bad Request) if the turbineUserGroupRole has already
     * an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/turbine-user-group-roles")
    @Timed
    public ResponseEntity<TurbineUserGroupRoleDTO> createTurbineUserGroupRole(@RequestBody TurbineUserGroupRoleDTO turbineUserGroupRoleDTO) throws URISyntaxException {
        log.info("REST request to save TurbineUserGroupRole : {}", turbineUserGroupRoleDTO);
        if (turbineUserGroupRoleDTO.getUserId() == null && turbineUserGroupRoleDTO.getGroupId() == null && turbineUserGroupRoleDTO.getRoleId() == null) {
            throw new BadRequestAlertException("A new turbineUserGroupRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurbineUserGroupRoleDTO result = turbineUserGroupRoleService.save(turbineUserGroupRoleDTO);
        return ResponseEntity.created(new URI("/api/turbine-user-group-roles/" + result.getUserId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getUserId().toString())).body(result);
    }

    @PostMapping("/turbine-user-group-roles/save")
    @Timed
    public ResponseEntity<TurbineUserGroupRoleDTO> createTurbineUserGroupRoleNative(@RequestBody TurbineUserGroupRoleDTO turbineUserGroupRoleDTO) throws URISyntaxException {
        log.info("REST request to save TurbineUserGroupRole : {}", turbineUserGroupRoleDTO);
        if (turbineUserGroupRoleDTO.getUserId() == null && turbineUserGroupRoleDTO.getGroupId() == null && turbineUserGroupRoleDTO.getRoleId() == null) {
            throw new BadRequestAlertException("A new turbineUserGroupRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurbineUserGroupRoleDTO result = turbineUserGroupRoleService.saveNative(turbineUserGroupRoleDTO);
        return ResponseEntity.created(new URI("/api/turbine-user-group-roles/" + result.getUserId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getUserId().toString())).body(result);
    }

    /**
     * PUT  /turbine-user-group-roles : Updates an existing turbineUserGroupRole.
     *
     * @param turbineUserGroupRoleDTO
     * 		the turbineUserGroupRoleDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated turbineUserGroupRoleDTO, or with status 400 (Bad Request) if the turbineUserGroupRoleDTO is not
     * valid, or with status 500 (Internal Server Error) if the turbineUserGroupRoleDTO couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/turbine-user-group-roles")
    @Timed
    public ResponseEntity<TurbineUserGroupRoleDTO> updateTurbineUserGroupRole(@RequestBody TurbineUserGroupRoleDTO turbineUserGroupRoleDTO) throws URISyntaxException {
        log.info("REST request to update TurbineUserGroupRole : {}", turbineUserGroupRoleDTO);
        if (turbineUserGroupRoleDTO.getUserId() == null && turbineUserGroupRoleDTO.getGroupId() == null && turbineUserGroupRoleDTO.getRoleId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurbineUserGroupRoleDTO result = turbineUserGroupRoleService.save(turbineUserGroupRoleDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turbineUserGroupRoleDTO.getUserId().toString())).body(result);
    }

    /**
     * GET  /turbine-user-group-roles : get all the turbineUserGroupRoles.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of turbineUserGroupRoles in body
     */
    @GetMapping("/turbine-user-group-roles")
    @Timed
    public ResponseEntity<List<TurbineUserGroupRoleDTO>> getAllTurbineUserGroupRoles(Pageable pageable) {
        log.info("REST request to get a page of TurbineUserGroupRoles");
        Page<TurbineUserGroupRoleDTO> page = turbineUserGroupRoleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turbine-user-group-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /turbine-user-group-roles/:id : get the "id" turbineUserGroupRole.
     *
     * @param id
     * 		the id of the turbineUserGroupRoleDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the turbineUserGroupRoleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turbine-user-group-roles/{id}")
    @Timed
    public ResponseEntity<TurbineUserGroupRoleDTO> getTurbineUserGroupRole(@PathVariable Long id) {
        log.info("REST request to get TurbineUserGroupRole : {}", id);
        Optional<TurbineUserGroupRoleDTO> turbineUserGroupRoleDTO = turbineUserGroupRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turbineUserGroupRoleDTO);
    }

    /**
     * GET
     *
     * @param id
     * 		rol del usuario
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    @GetMapping("/turbine-user-group-roles/find/{id}")
    @Timed
    public ResponseEntity<List<TurbineUsuarioDTO>> getTurbineUsuarios(@PathVariable int id) {
        log.info("REST request to get TurbineUsuarios : {}", id);
        try {
            List<TurbineUsuarioDTO> turbineUsuarioDTO = turbineUserGroupRoleService.getAllListaUsuarios(id);
            return new ResponseEntity<>(turbineUsuarioDTO, HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }

    /**
     * GET
     *
     * @param
     *
     * @author carlos.rodriguez
     */
    @GetMapping("/turbine-user-group-roles/findAll")
    @Time
    public ResponseEntity<List<TurbineUsuarioDTO>> getTurbineUsuariosRol() {
        log.info("REST request to get TurbineUsuariosRol");
        try {
            List<TurbineUsuarioDTO> turbineUsuarioDTO = turbineUserGroupRoleService.getAllListaUsuariosRol();
            return new ResponseEntity<>(turbineUsuarioDTO, HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }

    /**
     *
     * @return
     */
    @GetMapping("/turbine-user-group-roles/findAllTotal")
    @Time
    public ResponseEntity<List<TurbineUsuarioDTO>> getTurbineUsuariosRolAll() {
        log.info("REST request to get TurbineUsuariosRol");
        try {
            List<TurbineUsuarioDTO> turbineUsuarioDTO = turbineUserGroupRoleService.getAllListaUsuariosRolAll();
            return new ResponseEntity<>(turbineUsuarioDTO, HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }
    @GetMapping("/turbine-user-group-roles-revision/{codeOperator}")
    @Time
    public ResponseEntity<List<TurbineUsuarioDTO>> getTurbineUsuariosRevision(@PathVariable String codeOperator) {
        log.info("REST request to get getTurbineUsuariosRevision");
        try {
            List<TurbineUsuarioDTO> turbineUsuarioDTO = turbineUserGroupRoleService.getAllListaUsuariosRevision(codeOperator);
            return new ResponseEntity<>(turbineUsuarioDTO, HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }

    @GetMapping("/turbine-user-group-roles/findAllGrupos")
    @Time
    public ResponseEntity<List<?>> getAllGrupos() {
        log.info("Rest request to get Grupos");
        try {
            List<?> listaGrupos = turbineUserGroupRoleService.getAllGrupos();
            return new ResponseEntity<>(listaGrupos, HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }

    @GetMapping("/turbine-user-group-roles/findAllEjecutoresSinGrupo")
    @Timed
    public ResponseEntity<List<TurbineUsuarioDTO>> getEjecutoresSinGrupo() {
        log.info("REST request ejecutores que aun no tienen asignado grupo");
        try {
            List<TurbineUsuarioDTO> turbineUsuarioDTO = turbineUserGroupRoleService.getAllListaEjecutoresNoAsignadosGrupo();
            return new ResponseEntity<>(turbineUsuarioDTO, HttpStatus.OK);
        } catch (SQLGrammarException ex) {
            throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
        } catch (Exception ex) {
            if (ex.getCause() instanceof SQLGrammarException) {
                throw new BadRequestAlertException("The SQL Grammar isn't ok", ENTITY_NAME, "sqlGrammarWrong");
            }
            throw new BadRequestAlertException("Error executing SQL string", ENTITY_NAME, "sqlExecuteGeneralError");
        }
    }

    /**
     * DELETE  /turbine-user-group-roles/:id : delete the "id" turbineUserGroupRole.
     *
     * @param id
     * 		the id of the turbineUserGroupRoleDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turbine-user-group-roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurbineUserGroupRole(@PathVariable Long id) {
        log.info("REST request to delete TurbineUserGroupRole : {}", id);
        turbineUserGroupRoleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * Elimina Asignacion en la tabla TurbineUserGroupRole
     *
     * @param userId
     * @param groupId
     * @param roleId
     *
     * @return the ResponseEntity with status 200 (OK)
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    @DeleteMapping("/turbine-user-group-roles/deleteByUserByGroupByRole/{userId}/{groupId}/{roleId}")
    @Time
    public ResponseEntity<Void> deleteByUserIdAndGroupIdAndRoleId(@PathVariable Long userId, @PathVariable Long groupId, @PathVariable Long roleId) {
        log.info("userId:" + userId + "/groupId:" + groupId + "/roleId:" + roleId);
        turbineUserGroupRoleService.deleteByUserIdAndGroupIdAndRoleId(userId, groupId, roleId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "Eliminación Exitosa")).build();
    }
}
