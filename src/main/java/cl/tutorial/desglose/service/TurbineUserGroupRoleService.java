package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUserGroupRole;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.SQLSFRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserGroupRoleRepository;
import cl.tutorial.desglose.service.dto.TurbineUserGroupRoleDTO;
import cl.tutorial.desglose.service.dto.TurbineUsuarioDTO;
import cl.tutorial.desglose.service.mapper.TurbineUserGroupRoleMapper;
import org.apache.commons.lang.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cl.tutorial.desglose.config.Constants.PLAT_EJECUTOR;
import static cl.tutorial.desglose.config.Constants.PLAT_REVISOR;

/**
 * Service Implementation for managing TurbineUserGroupRole.
 */
@Service
@Transactional
public class TurbineUserGroupRoleService {

    private final Logger log = LogManager.getLogger(TurbineUserGroupRoleService.class);

    private final TurbineUserGroupRoleRepository turbineUserGroupRoleRepository;

    private final TurbineUserGroupRoleMapper turbineUserGroupRoleMapper;

    private final SQLDGLRepository SQLDGLRepository;

    private final SQLSFRepository sqlsfRepository;

    private final String SQL_GET_LISTA_USUARIOS_REVISION =
        "SELECT\n" + "    num_rol_empleado,\n" + "    turbine_user.login_name,\n" + "    nls_initcap(turbine_user.first_name\n" + "                || ' '\n"
            + "                || turbine_user.last_name)\n"
            + "    || DECODE(flg, '1', '', DECODE(flg, 2, ' (POR REEMPLAZO)', DECODE(flg, 3, ' (POR REEMPLAZO)', ''))) gls_nombre\n" + "FROM ( SELECT\n"
            + "    des_num_rol_empleado   num_rol_empleado,\n" + "    0 flg\n" + "FROM\n" + "    desglo_grupo_wk\n" + "WHERE\n" + "\n"
            + "    num_rol_empleado =:ROL_EMPLEADO\n" + "UNION\n" + "SELECT\n" + "    num_rol_empleado_2fir,\n" + "    1 flg\n" + "FROM\n" + "    desglo_fir_wk\n" + "WHERE\n"
            + "    num_rol_empleado =:ROL_EMPLEADO\n" + "UNION\n" + "SELECT\n" + "    grp.des_num_rol_empleado   num_rol_empleado,\n" + "    2 flg\n" + "FROM\n"
            + "    desglo_ree_wk ree\n" + "    INNER JOIN desglo_grupo_wk grp ON ( grp.cod_cargo = ree.cod_cargo\n"
            + "                                     AND grp.num_rol_empleado = ree.num_rol_empleado )\n" + "WHERE\n" + "    ree.cod_reg_status = 0\n"
            + "         AND ree.des_num_rol_empleado =:ROL_EMPLEADO\n" + "UNION\n" + "SELECT\n" + "    fir.num_rol_empleado_2fir,\n"
            + "    3 flg\n" + "FROM\n" + "    desglo_ree_wk ree\n" + "    INNER JOIN desglo_fir_wk fir ON ( ree.cod_cargo = fir.cod_cargo\n"
            + "                                   AND ree.num_rol_empleado = fir.num_rol_empleado )\n" + "WHERE\n" + "    cod_reg_status = 0\n"
            + "   AND DES_NUM_ROL_EMPLEADO =:ROL_EMPLEADO\n" + "                ) ROLES\n"
            + "          INNER JOIN SF_STF_OPERATOR ON (SF_STF_OPERATOR.OPERATOR_CODE = ROLES.NUM_ROL_EMPLEADO) \n"
            + "          INNER JOIN TURBINE_USER ON ( turbine_user.login_name = sf_stf_operator.login_name ) \n" + "          order by gls_nombre";
    private final String SQL_GET_LISTA_USUARIOSBYROL     =
        "SELECT USR.LOGIN_NAME as loginName, NLS_INITCAP(USR.FIRST_NAME || ' ' || USR.LAST_NAME)  nombre, STF.OPERATOR_CODE operatorCode, USR.USER_ID user_id\n"
            + " FROM TURBINE_USER_GROUP_ROLE ROLE \n" + " LEFT JOIN TURBINE_USER USR ON (USR.USER_ID = ROLE.USER_ID)\n"
            + " INNER JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n" + " WHERE ROLE.ROLE_ID =:roleId \n" + "ORDER BY nombre";

    private final String SQL_GET_LISTA_USUARIOS =
        "SELECT USR.LOGIN_NAME as loginName,UPPER(TRIM(FIRST_NAME) || ' ' || TRIM(LAST_NAME)) nombre ,STF.OPERATOR_CODE operatorCode,USR.USER_ID user_id\n"
            + "FROM TURBINE_ROLE ROLE \n"
            + "INNER JOIN DESCOD DES ON (DES.COD_TABLA = 'DGLCONFG' AND DES.COD_GENERICO = 'ROL_APLICACION' AND DES.COD_REG_STATUS = 0 AND DES.GLS_GENERICACORTA = ROLE"
            + ".ROLE_NAME) \n" + "INNER JOIN TURBINE_USER_GROUP_ROLE USR_ROLE ON (USR_ROLE.ROLE_ID = ROLE.ROLE_ID) \n"
            + "LEFT JOIN TURBINE_USER USR ON (USR.USER_ID = USR_ROLE.USER_ID) \n" + "LEFT JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
            + "WHERE DES.GLS_GENERICACORTA = 'Plat_Desglose' \n"
            + "AND USR.USER_ID NOT IN (SELECT USER_ID FROM TURBINE_USER_GROUP_ROLE WHERE ROLE_ID IN (SELECT ROLE_ID FROM TURBINE_ROLE WHERE ROLE_NAME IN ('Plat_Desglose_Jefe',"
            + "'Plat_Desglose_Revisor','Plat_Desglose_Ejecutor')))\n" + "ORDER BY nombre";

    private final String SQL_GET_LISTA_USUARIOS_ALL =
        "SELECT\n" + "    usr.login_name      AS loginname,\n" + "    upper(TRIM(first_name)\n" + "          || ' '\n" + "          || TRIM(last_name)) nombre,\n"
            + "    stf.operator_code   operatorcode,\n" + "    usr.user_id         user_id\n" + "FROM\n" + "    turbine_role role\n"
            + "    INNER JOIN descod des ON ( des.cod_tabla = 'DGLCONFG'\n" + "                               AND des.cod_generico = 'ROL_APLICACION'\n"
            + "                               AND des.cod_reg_status = 0\n" + "                               AND des.gls_genericacorta = role.role_name )\n"
            + "    INNER JOIN turbine_user_group_role usr_role ON ( usr_role.role_id = role.role_id )\n" + "    LEFT JOIN turbine_user usr ON ( usr.user_id = usr_role.user_id )\n"
            + "    LEFT JOIN sf_stf_operator stf ON ( stf.login_name = usr.login_name )\n" + "WHERE\n"
            + "    des.gls_genericacorta = 'Plat_Desglose' and usr.login_name is not null and stf.operator_code is not null\n" + "ORDER BY\n" + "    nombre";

    private final String SQL_SET_TURBINEUSERGROUPROLE = "INSERT INTO TURBINE_USER_GROUP_ROLE (USER_ID, GROUP_ID, ROLE_ID)" + " VALUES(:user_Id,:group_Id,:role_Id)";

    private final String SQL_GET_ALL_GRUPOS = "SELECT STF.OPERATOR_CODE, NLS_INITCAP(USR.FIRST_NAME || ' ' || USR.LAST_NAME) GLS_NOMBRE, \n"
        + "  REV.DES_NUM_ROL_EMPLEADO ROL_INTEGRANTE, NLS_INITCAP(USR2.FIRST_NAME || ' ' || USR2.LAST_NAME) NOMBRE_INTEGRANTE, \n"
        + "  ROLE.USER_ID USER_ID_REVISOR, USR2.USER_ID USER_ID_EJECUTOR FROM TURBINE_USER_GROUP_ROLE ROLE \n" + "  INNER JOIN TURBINE_USER USR ON (USR.USER_ID = ROLE.USER_ID) \n"
        + "  INNER JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
        + "  LEFT JOIN DESGLO_GRUPO_WK REV ON (REV.COD_CARGO ='CARGO-002' AND REV.NUM_ROL_EMPLEADO = STF.OPERATOR_CODE) \n"
        + "  LEFT JOIN SF_STF_OPERATOR STF2 ON (STF2.OPERATOR_CODE = REV.DES_NUM_ROL_EMPLEADO) LEFT JOIN TURBINE_USER USR2 ON (USR2.LOGIN_NAME = STF2.LOGIN_NAME) \n"
        + "  WHERE ROLE_ID = (SELECT ROLE_ID FROM TURBINE_ROLE WHERE ROLE_NAME LIKE '%" + PLAT_REVISOR + "%')"
        + " ORDER BY USR.FIRST_NAME, USR.LAST_NAME, USR2.FIRST_NAME, USR2.LAST_NAME";

    private final String SQL_GET_LISTA_EJECUTORES =
        "SELECT USR.LOGIN_NAME loginName, NLS_INITCAP(USR.FIRST_NAME || ' ' || USR.LAST_NAME) nombre, STF.OPERATOR_CODE operatorCode, ROLE.USER_ID user_id \n"
            + " FROM TURBINE_USER_GROUP_ROLE ROLE \n" + "           INNER JOIN TURBINE_USER USR ON (USR.USER_ID = ROLE.USER_ID)\n"
            + "           INNER JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME) \n"
            + "           WHERE ROLE_ID = (SELECT ROLE_ID FROM TURBINE_ROLE WHERE ROLE_NAME LIKE '%" + PLAT_EJECUTOR + "%')\n"
            + "          AND STF.OPERATOR_CODE NOT IN (SELECT DES_NUM_ROL_EMPLEADO FROM DESGLO_GRUPO_WK WHERE DES_COD_CARGO='CARGO-003') \n"
            + "           ORDER BY USR.FIRST_NAME, USR.LAST_NAME";

    public TurbineUserGroupRoleService(TurbineUserGroupRoleRepository turbineUserGroupRoleRepository, TurbineUserGroupRoleMapper turbineUserGroupRoleMapper,
        SQLDGLRepository SQLDGLRepository,SQLSFRepository sqlsfRepository) {
        this.turbineUserGroupRoleRepository = turbineUserGroupRoleRepository;
        this.turbineUserGroupRoleMapper = turbineUserGroupRoleMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.sqlsfRepository = sqlsfRepository;
    }

    /**
     * Save a turbineUserGroupRole.
     *
     * @param turbineUserGroupRoleDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public TurbineUserGroupRoleDTO save(TurbineUserGroupRoleDTO turbineUserGroupRoleDTO) {
        log.info("Request to save TurbineUserGroupRole : {}", turbineUserGroupRoleDTO);

        TurbineUserGroupRole turbineUserGroupRole = turbineUserGroupRoleMapper.toEntity(turbineUserGroupRoleDTO);
        turbineUserGroupRoleRepository.save(turbineUserGroupRole);

        return turbineUserGroupRoleMapper.toDto(turbineUserGroupRole);

    }

    /**
     * @param turbineUserGroupRoleDTO
     *
     * @return
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    public TurbineUserGroupRoleDTO saveNative(TurbineUserGroupRoleDTO turbineUserGroupRoleDTO) {
        log.info("INICIO - Request to save TurbineUserGroupRole : {}", turbineUserGroupRoleDTO);

        TurbineUserGroupRole turbineUserGroupRole = turbineUserGroupRoleMapper.toEntity(turbineUserGroupRoleDTO);
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("user_Id", turbineUserGroupRole.getUserId());
        listaParametros.put("group_Id", turbineUserGroupRole.getGroupId());
        listaParametros.put("role_Id", turbineUserGroupRole.getRoleId());
        log.debug("QUERY: " + SQL_SET_TURBINEUSERGROUPROLE);
        log.debug("PARAMETROS: " + listaParametros.toString());
        sqlsfRepository.executeQuery(SQL_SET_TURBINEUSERGROUPROLE, listaParametros);
        log.info("FIN - Request to save TurbineUserGroupRole");
        return turbineUserGroupRoleMapper.toDto(turbineUserGroupRole);
    }

    /**
     * Get all the turbineUserGroupRoles.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TurbineUserGroupRoleDTO> findAll(Pageable pageable) {
        log.info("Request to get all TurbineUserGroupRoles");
        return turbineUserGroupRoleRepository.findAll(pageable).map(turbineUserGroupRoleMapper::toDto);
    }

    /**
     * Get one turbineUserGroupRole by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurbineUserGroupRoleDTO> findOne(Long id) {
        log.info("Request to get TurbineUserGroupRole : {}", id);
        //return turbineUserGroupRoleRepository.findById(id)
        //   .map(turbineUserGroupRoleMapper::toDto);
        return null;
    }

    /**
     * Get  retorna lista de usuarios por id
     *
     * @param roleId
     * 		del rol del usuario
     *
     * @return lista de usuarios
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> getAllListaUsuarios(int roleId) {
        log.info("INICIO - getAllListaUsuarios");
        List<TurbineUsuarioDTO> usersList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        log.debug("QUERY: " + SQL_GET_LISTA_USUARIOSBYROL);
        log.debug("PARAMETROS: " + params.toString());
        log.info("FIN - getAllListaUsuarios");
        return getTurbineUsersDTOS(SQL_GET_LISTA_USUARIOSBYROL, params, true);
    }

    /**
     * Get retorna lista de usuarios por rol
     *
     * @return lista de usuarios
     */
    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> getAllListaUsuariosRol() {
        log.info("INICIO- getAllListaUsuariosRol");
        Map<String, Object> params = new HashMap<>();
        log.debug("QUERY: " + SQL_GET_LISTA_USUARIOS);
        log.info("FIN- getAllListaUsuariosRol");
        return getTurbineUsersDTOS(SQL_GET_LISTA_USUARIOS, params, true);
    }

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> getAllListaUsuariosRolAll() {
        log.info("Retorna la Lista De Usuarios Segun el Rol");
        Map<String, Object> params = new HashMap<>();
        log.debug("QUERY: " +SQL_GET_LISTA_USUARIOS_ALL );
        return getTurbineUsersDTOS(SQL_GET_LISTA_USUARIOS_ALL, params, true);
    }

    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> getAllListaUsuariosRevision(String operatorCode) {
        log.info("Retorna la Lista De Usuarios Segun el Rol");
        Map<String, Object> params = new HashMap<>();
        params.put("ROL_EMPLEADO", operatorCode);
        log.debug("QUERY: " + SQL_GET_LISTA_USUARIOS_REVISION);
        log.debug("PARAMETROS: " + params.toString());
        return getTurbineUsersDTOS(SQL_GET_LISTA_USUARIOS_REVISION, params, false);
    }

    /**
     * Get Retorna la lista de ejecutores que aun no tienen asignado grupo
     *
     * @return
     *
     * @author carlos eduardo rodriguez perez
     */
    @Transactional(readOnly = true)
    public List<TurbineUsuarioDTO> getAllListaEjecutoresNoAsignadosGrupo() {
        log.info("Retorna la Lista De Ejecutores que aun no han sido asignados a grupos");
        Map<String, Object> params = new HashMap<>();
        log.debug("QUERY: " + SQL_GET_LISTA_EJECUTORES);
        log.debug("PARAMETROS: " + params.toString());
        return getTurbineUsersDTOS(SQL_GET_LISTA_EJECUTORES, params, true);
    }

    /**
     * Get Retorna la consulta de grupos
     *
     * @return lista de grupos
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    @Transactional(readOnly = true)
    public List<?> getAllGrupos() {
        Map<String, Object> listaParametros = new HashMap<>();
        log.debug("QUERY: " + SQL_GET_ALL_GRUPOS);
        log.debug("PARAMETROS: " + listaParametros.toString());
        List<?> listaGrupos = SQLDGLRepository.execute(SQL_GET_ALL_GRUPOS, listaParametros);
        return listaGrupos;
    }

    /**
     * Delete the turbineUserGroupRole by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete TurbineUserGroupRole : {}", id);
        //turbineUserGroupRoleRepository.deleteById(id);
    }

    /**
     * Ejecuta delete en la tabla TurbineUserGroupRole Enviando los parametros
     *
     * @param userId
     * @param groupId
     * @param roleId
     *
     * @author Carlos Eduardo Rodriguez Perez
     */
    public void deleteByUserIdAndGroupIdAndRoleId(Long userId, Long groupId, Long roleId) {
        turbineUserGroupRoleRepository.eliminaUserIdAndGroupIdAndRoleId(userId, groupId, roleId);
    }

    /**
     * Método auxiliar para ejecutar una query y devolver una lista de TurbineUsuarioDTO.
     *
     * @param querySql
     * @param params
     * @param fullParams
     *
     * @return
     */
    private List<TurbineUsuarioDTO> getTurbineUsersDTOS(String querySql, Map<String, Object> params, Boolean fullParams) {
        log.info("INICIO - getTurbineUsersDTOS");
        List<TurbineUsuarioDTO> usersList = new LinkedList<>();
        List<Object[]> objects = SQLDGLRepository.execute(querySql, params);
        log.debug("QUERY : " + querySql);
        log.debug("PARAMETROS : " + params.toString());
        if (fullParams) {
            for (Object[] item : objects) {
                // item[1] es el nombre completo del usuario por lo que se capitalizan todas las palabras.
                usersList.add(new TurbineUsuarioDTO(item[0].toString(), WordUtils.capitalizeFully(item[1].toString()), item[2].toString(), item[3].toString()));
            }
        } else {
            // item[0] es el nombre completo del usuario por lo que se capitalizan todas las palabras.
            for (Object[] item : objects) {
                usersList.add(new TurbineUsuarioDTO(item[2].toString(), WordUtils.capitalizeFully(item[0].toString())));
            }
        }
        log.info("FIN - getTurbineUsersDTOS");
        return usersList;
    }

}
