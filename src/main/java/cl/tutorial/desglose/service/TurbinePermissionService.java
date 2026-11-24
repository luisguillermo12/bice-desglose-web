package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.sfdesglose.repository.SQLSFRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbinePermission;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbinePermissionRepository;
import cl.tutorial.desglose.service.dto.TurbinePermissionDTO;
import cl.tutorial.desglose.service.mapper.TurbinePermissionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing TurbinePermission.
 */
@Service
@Transactional
public class TurbinePermissionService {

    private final String SQL_CONSULTA_PERMISOS =
        "SELECT DISTINCT \n" + "       NVL(P.PERMISSION_NAME,'" + Constants.MDB4 + "') PERMISSION_NAME,R.ROLE_ID\n" + "       FROM " + "TURBINE_USER U \n"
            + "       INNER JOIN SF_STF_OPERATOR O ON (UPPER(O.LOGIN_NAME) = UPPER(U.LOGIN_NAME)) \n"
            + "       INNER JOIN TURBINE_USER_GROUP_ROLE UR ON (UR.USER_ID = U.USER_ID) \n" + "       INNER JOIN TURBINE_ROLE R ON (R.ROLE_ID = UR.ROLE_ID) \n"
            + "       INNER JOIN TURBINE_ROLE_PERMISSION RP ON (RP.ROLE_ID = R.ROLE_ID) \n" + "       INNER JOIN TURBINE_PERMISSION P  ON (P.PERMISSION_ID = RP.PERMISSION_ID) \n"
            + "  WHERE \n" + "  U.LOGIN_NAME=:usuario AND\n" + "  R.ROLE_NAME IN ('MDB_conDesglose','MDB_expDesglose','MDB_supDesglose','MDB_anaDesglose', 'Plat_Desglose')";

    private final Logger log = LogManager.getLogger(TurbinePermissionService.class);

    private final TurbinePermissionRepository turbinePermissionRepository;

    private final TurbinePermissionMapper turbinePermissionMapper;

    private final SQLDGLRepository SQLDGLRepository;

    private final SQLSFRepository sqlsfRepository;

    public TurbinePermissionService(TurbinePermissionRepository turbinePermissionRepository, TurbinePermissionMapper turbinePermissionMapper,
        SQLDGLRepository SQLDGLRepository,SQLSFRepository sqlsfRepository) {
        this.turbinePermissionRepository = turbinePermissionRepository;
        this.turbinePermissionMapper = turbinePermissionMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.sqlsfRepository = sqlsfRepository;
    }

    /**
     * Save a turbinePermission.
     *
     * @param turbinePermissionDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public TurbinePermissionDTO save(TurbinePermissionDTO turbinePermissionDTO) {
        log.info("Request to save TurbinePermission : {}", turbinePermissionDTO);

        TurbinePermission turbinePermission = turbinePermissionMapper.toEntity(turbinePermissionDTO);
        turbinePermission = turbinePermissionRepository.save(turbinePermission);
        return turbinePermissionMapper.toDto(turbinePermission);
    }

    /**
     * Get all the turbinePermissions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TurbinePermissionDTO> findAll(Pageable pageable) {
        log.info("Request to get all TurbinePermissions");
        return turbinePermissionRepository.findAll(pageable).map(turbinePermissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<TurbinePermissionDTO> findAllPermisosByUser(String user) {
        log.info("INICIO - findAllPermisosByUser");
        List<TurbinePermissionDTO> turbinePermissionList = new LinkedList<>();
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("usuario", user);
        log.debug("QUERY: " + SQL_CONSULTA_PERMISOS);
        log.debug("PARAMETROS: " + listaParametros);
        List<Object[]> lista = SQLDGLRepository.execute(SQL_CONSULTA_PERMISOS, listaParametros);
        for (Object[] item : lista) {
            TurbinePermissionDTO objeto = new TurbinePermissionDTO();
            objeto.setPermissionName(item[0].toString());
            turbinePermissionList.add(objeto);
        }
        log.info("FIN - findAllPermisosByUser");
        return turbinePermissionList;
    }

    /**
     * Get one turbinePermission by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurbinePermissionDTO> findOne(Long id) {
        log.info("Request to get TurbinePermission : {}", id);
        return turbinePermissionRepository.findById(id).map(turbinePermissionMapper::toDto);
    }

    /**
     * Delete the turbinePermission by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete TurbinePermission : {}", id);
        turbinePermissionRepository.deleteById(id);
    }
}
