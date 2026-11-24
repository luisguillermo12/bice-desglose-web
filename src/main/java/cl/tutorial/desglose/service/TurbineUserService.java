package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.sfdesglose.repository.SQLSFRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUser;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserRepository;
import cl.tutorial.desglose.service.dto.TurbineUserDTO;
import cl.tutorial.desglose.service.dto.TurbineUsuarioDTO;
import cl.tutorial.desglose.service.mapper.TurbineUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.PLAT_JEFE;
import static cl.tutorial.desglose.config.Constants.PLAT_REVISOR;

/**
 * Service Implementation for managing TurbineUser.
 */
@Service
@Transactional
public class TurbineUserService {

    private final Logger log = LoggerFactory.getLogger(TurbineUserService.class);

    private final TurbineUserRepository turbineUserRepository;
    private final TurbineUserMapper     turbineUserMapper;
    private final SQLDGLRepository       sqldglRepository;
    private final SQLSFRepository       sqlsfRepository;

    private final String SQL_GET_USUARIOS =
        "SELECT NVL(COUNT(*),0) CONT \n" + "          FROM TURBINE_USER USR \n" + "         INNER JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
            + "         INNER JOIN DESGLO_GRUPO_WK GRP ON (GRP.COD_CARGO = 'CARGO-002' AND GRP.NUM_ROL_EMPLEADO = STF.OPERATOR_CODE) \n" + "         WHERE USR.USER_ID=:userId";

    private final String SQL_GET_USUARIO_BY_ROLE = "SELECT USR.LOGIN_NAME,usr.first_name || '  ' || usr.last_name,stf.operator_code,usr.user_id  FROM TURBINE_USER USR    \n"
        + "INNER JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME)\n" + "WHERE stf.operator_code=:role";

    private final String SQL_GET_MIEMBROREEMPLAZAR =
        "SELECT OPERATOR_CODE, GLS_NOMBRE\n" + "            FROM (SELECT 'CARGO-002' COD_CARGO, STF.OPERATOR_CODE, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE \n"
            + "                   FROM TURBINE_USER_GROUP_ROLE ROLE \n" + "                   LEFT JOIN TURBINE_USER USR ON (USR.USER_ID = ROLE.USER_ID) \n"
            + "                    LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
            + "                   WHERE ROLE.ROLE_ID =  (SELECT ROLE_ID FROM TURBINE_ROLE WHERE ROLE_NAME LIKE '%" + PLAT_REVISOR + "%')\n" + "                  UNION \n"
            + "                  SELECT 'CARGO-001' COD_CARGO, STF.OPERATOR_CODE, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE \n"
            + "                   FROM TURBINE_USER_GROUP_ROLE ROLE \n" + "                    LEFT JOIN TURBINE_USER USR ON (USR.USER_ID = ROLE.USER_ID)\n"
            + "                    LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
            + "                   WHERE ROLE.ROLE_ID = (SELECT ROLE_ID FROM TURBINE_ROLE WHERE ROLE_NAME LIKE '%" + PLAT_JEFE + "%') ) \n"
            + "           WHERE COD_CARGO =:idCargo\n" + "             AND OPERATOR_CODE NOT IN (SELECT NUM_ROL_EMPLEADO FROM DESGLO_REE_WK WHERE COD_REG_STATUS = 0) \n"
            + "            AND OPERATOR_CODE NOT IN (SELECT DES_NUM_ROL_EMPLEADO FROM DESGLO_REE_WK WHERE COD_REG_STATUS = 0) \n" + "  ORDER BY GLS_NOMBRE";

    private final String SQL_GET_MIEMBROREEMPLAZO =
        "SELECT OPERATOR_CODE, GLS_NOMBRE \n" + "            FROM (SELECT STF.OPERATOR_CODE, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE \n"
            + "                    FROM TURBINE_USER_GROUP_ROLE ROLE \n" + "                   LEFT JOIN TURBINE_USER USR ON (USR.USER_ID = ROLE.USER_ID) \n"
            + "                   LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
            + "                   WHERE ROLE.ROLE_ID = (SELECT ROLE_ID FROM TURBINE_ROLE WHERE ROLE_NAME LIKE '%" + PLAT_REVISOR + "%') \n" + "                  UNION \n"
            + "                  SELECT STF.OPERATOR_CODE, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE \n" + "                FROM TURBINE_USER_GROUP_ROLE ROLE \n"
            + "                    LEFT JOIN TURBINE_USER USR ON (USR.USER_ID = ROLE.USER_ID) \n"
            + "                    LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
            + "                   WHERE ROLE.ROLE_ID = (SELECT ROLE_ID FROM TURBINE_ROLE WHERE ROLE_NAME LIKE '%" + PLAT_JEFE + "%')) \n" + "       "
            + "                   WHERE OPERATOR_CODE <>:operatorCode\n" + "        AND OPERATOR_CODE NOT IN (SELECT NUM_ROL_EMPLEADO FROM DESGLO_REE_WK WHERE COD_REG_STATUS = 0)"
            + "     ORDER BY GLS_NOMBRE";

    private final String SQL_GET_ISMIENBROREEMPLAZO = "SELECT NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE \n" + "           FROM DESGLO_REE_WK REE \n"
        + "          INNER JOIN SF_STF_OPERATOR SF ON (REE.DES_NUM_ROL_EMPLEADO = SF.OPERATOR_CODE) \n"
        + "          INNER JOIN TURBINE_USER TU ON (SF.LOGIN_NAME = TU.LOGIN_NAME)\n" + "          WHERE COD_REG_STATUS = 0 \n" + "          AND NUM_ROL_EMPLEADO =:codOperador";

    private final String SQL_GET_ROLREEMPLAZO = "SELECT DES_NUM_ROL_EMPLEADO, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE \n" + "           FROM DESGLO_REE_WK REE \n"
        + "           LEFT JOIN SF_STF_OPERATOR OPE ON (OPE.OPERATOR_CODE = REE.DES_NUM_ROL_EMPLEADO AND COD_REG_STATUS = 0) \n"
        + "           LEFT JOIN TURBINE_USER USR ON (OPE.LOGIN_NAME = USR.LOGIN_NAME)\n" + "           WHERE NUM_ROL_EMPLEADO =:codOperador\n"
        + "            AND REE.COD_REG_STATUS = 0\n" + "            AND ROWNUM < 2";

    public TurbineUserService(TurbineUserRepository turbineUserRepository, TurbineUserMapper turbineUserMapper,
        SQLDGLRepository sqldglRepository,SQLSFRepository sqlsfRepository) {
        this.turbineUserRepository = turbineUserRepository;
        this.turbineUserMapper = turbineUserMapper;
        this.sqldglRepository = sqldglRepository;
        this.sqlsfRepository = sqlsfRepository;
    }

    /**
     * Save a turbineUser.
     *
     * @param turbineUserDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public TurbineUserDTO save(TurbineUserDTO turbineUserDTO) {
        log.info("Request to save TurbineUser : {}", turbineUserDTO);

        TurbineUser turbineUser = turbineUserMapper.toEntity(turbineUserDTO);
        turbineUser = turbineUserRepository.save(turbineUser);
        return turbineUserMapper.toDto(turbineUser);
    }

    /**
     * Get all the turbineUsers.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TurbineUserDTO> findAll(Pageable pageable) {
        log.info("Request to get all TurbineUsers");
        return turbineUserRepository.findAll(pageable).map(turbineUserMapper::toDto);
    }

    /**
     * Get one turbineUser by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurbineUserDTO> findOne(Long id) {
        log.info("Request to get TurbineUser : {}", id);
        return turbineUserRepository.findById(id).map(turbineUserMapper::toDto);
    }

    /**
     * Delete the turbineUser by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete TurbineUser : {}", id);
        turbineUserRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal isCanDeleteReviewer(Long userId) {
        log.info("Request to can delete reviewer by userId: {}", userId);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        log.debug("QUERY: "+SQL_GET_USUARIOS);
        log.debug("PARAMETROS: "+params.toString());
        BigDecimal cont = sqldglRepository.countSql(SQL_GET_USUARIOS, params);
        return cont;
    }

    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> finAllToReplace(String cargo) {
        log.info("Request to get a list of TurbineUsuarioDTO to replace an operator by code: {}", cargo);
        List<TurbineUsuarioDTO> listToReplace = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("idCargo", cargo);
        log.debug("QUERY: " + SQL_GET_MIEMBROREEMPLAZAR);
        log.debug("PARAMETROS: " + params.toString());
        return getTurbineUsuarioDTOS(listToReplace, params, SQL_GET_MIEMBROREEMPLAZAR);
    }

    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> findByRole(String role) {
        log.info("Request to find TurbineUsuarioDTO by role : {}", role);
        List<TurbineUsuarioDTO> usersList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("role", role);
        log.debug("QUERY: " + SQL_GET_USUARIO_BY_ROLE);
        log.debug("PARAMETROS: " + params.toString());
        List<Object[]> objects = sqldglRepository.execute(SQL_GET_USUARIO_BY_ROLE, params);
        for (Object[] item : objects) {
            usersList.add(new TurbineUsuarioDTO(item[0].toString(), item[1].toString(), item[2].toString(), item[3].toString()));
        }
        return usersList;
    }

    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> finAllReplacements(Long operatorCode) {
        log.info("Request to get a list of TurbineUsuarioDTO that can replace an operator by code: {}", operatorCode);
        List<TurbineUsuarioDTO> replacementsList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("operatorCode", operatorCode);
        log.debug("QUERY : " +SQL_GET_MIEMBROREEMPLAZO);
        log.debug("PARAMETROS : " + params.toString());
        return getTurbineUsuarioDTOS(replacementsList, params, SQL_GET_MIEMBROREEMPLAZO);
    }

    private List<TurbineUsuarioDTO> getTurbineUsuarioDTOS(List<TurbineUsuarioDTO> replacementsList, Map<String, Object> params, String sql_get_miembroreemplazo) {
        List<Object[]> objects = sqldglRepository.execute(sql_get_miembroreemplazo, params);
        log.debug("QUERY: " + sql_get_miembroreemplazo);
        log.debug("PARAMETROS: " + params.toString());
        for (Object[] item : objects) {
            replacementsList.add(new TurbineUsuarioDTO(item[1].toString(), item[0].toString()));
        }
        return replacementsList;
    }

    @Transactional(readOnly = true)
    public TurbineUserDTO findByLoginName(String loginName) {
        TurbineUser turbineUser = turbineUserRepository.findByLoginName(loginName);
        return turbineUserMapper.toDto(turbineUser);
    }

    @Transactional(readOnly = true)
    public TurbineUsuarioDTO getReplacementRole(String operatorCode) {
        log.info("Request to get TurbineUsuarioDTO member to replace by operator code: {} ", operatorCode);
        TurbineUsuarioDTO turbineUsuarioDTO = null;
        Map<String, Object> params = new HashMap<>();
        params.put("codOperador", operatorCode);
        log.debug("QUERY: " +SQL_GET_ROLREEMPLAZO );
        log.debug("PARAMETROS: " +params.toString() );
        List<Object[]> objects = sqldglRepository.execute(SQL_GET_ROLREEMPLAZO, params);
        for (Object[] item : objects) {
            turbineUsuarioDTO = new TurbineUsuarioDTO(item[1].toString(), item[0].toString());
        }
        return turbineUsuarioDTO;
    }

    @Transactional(readOnly = true)
    public TurbineUsuarioDTO isReplacementMember(String operatorCode) {
        log.info("Request to check if is member to replace by operator code: {} ", operatorCode);
        TurbineUsuarioDTO turbineUsuarioDTO = null;
        Map<String, Object> params = new HashMap<>();
        params.put("codOperador", operatorCode);
        log.debug("QUERY: " +SQL_GET_ISMIENBROREEMPLAZO );
        log.debug("PARAMETROS: " +params.toString() );
        List<Object[]> objects = sqldglRepository.execute(SQL_GET_ISMIENBROREEMPLAZO, params);
        for (Object item : objects) {
            turbineUsuarioDTO = new TurbineUsuarioDTO(item.toString());
        }
        return turbineUsuarioDTO;
    }
}
