package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseReemplazo;
import cl.tutorial.desglose.domain.pk.DesgloseReemplazoPK;
import cl.tutorial.desglose.repository.DesgloseReemplazoRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.DesgloseReemplazoDTO;
import cl.tutorial.desglose.service.mapper.DesgloseReemplazoMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing DesgloseReemplazo.
 */
@Service
@Transactional
public class DesgloseReemplazoService {

    private final Logger log = LogManager.getLogger(DesgloseReemplazoService.class);

    private final DesgloseReemplazoRepository desgloseReemplazoRepository;

    private final DesgloseReemplazoMapper desgloseReemplazoMapper;

    private final SQLDGLRepository SQLDGLRepository;

    private final String SQL_INSERT_REEMPLAZOS =
        " INSERT INTO DESGLO_REE_WK (COD_CARGO, NUM_ROL_EMPLEADO, DES_COD_CARGO, DES_NUM_ROL_EMPLEADO, FEC_REEMPLAZO, NUM_ROL_EMPLEADO_CREA, COD_REG_STATUS) \n"
            + " SELECT :cargo,:roleReemplazar, COD_CARGO, :roleReemplazo, SYSDATE,:operatorCode, 0 \n"
            + " FROM (SELECT 'CARGO-002' COD_CARGO, SF_STF_OPERATOR.OPERATOR_CODE, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE_\n" + " FROM TURBINE_USER\n"
            + " INNER JOIN TURBINE_USER_GROUP_ROLE ON (TURBINE_USER_GROUP_ROLE.USER_ID = TURBINE_USER.USER_ID)\n"
            + " INNER JOIN TURBINE_ROLE ON (TURBINE_ROLE.ROLE_ID = TURBINE_USER_GROUP_ROLE.ROLE_ID)\n"
            + " INNER JOIN SF_STF_OPERATOR ON (SF_STF_OPERATOR.LOGIN_NAME = TURBINE_USER.LOGIN_NAME)\n" + " WHERE ROLE_NAME IN (SELECT GLS_GENERICALARGA FROM RELTAB\n"
            + " INNER JOIN DESCOD ON (DESCOD.COD_REG_STATUS = 0 AND RELTAB.COD_TABLA2 = DESCOD.COD_TABLA AND RELTAB.COD_GENERICO2 = DESCOD.COD_GENERICO)\n"
            + " WHERE COD_TABLA1 = 'DGLCONFG' AND COD_GENERICO1 = 'ROLES-DESGLOSE-02')\n" + " AND TURBINE_USER.LOGIN_NAME NOT IN (SELECT LOGIN_NAME\n" + " FROM TURBINE_USER\n"
            + " WHERE LOGIN_NAME IN (SELECT GLS_GENERICACORTA FROM DESCOD WHERE COD_REG_STATUS = 0 AND COD_TABLA = 'DGLCONFG' AND COD_GENERICO LIKE 'JEFE_AREA_%'))\n" + " UNION\n"
            + " SELECT 'CARGO-001' COD_CARGO, SF_STF_OPERATOR.OPERATOR_CODE, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE\n" + " FROM TURBINE_USER\n"
            + " INNER JOIN SF_STF_OPERATOR ON (SF_STF_OPERATOR.LOGIN_NAME = TURBINE_USER.LOGIN_NAME)\n"
            + " WHERE TURBINE_USER.LOGIN_NAME IN (SELECT GLS_GENERICACORTA FROM DESCOD WHERE COD_REG_STATUS = 0 AND COD_TABLA = 'DGLCONFG' AND COD_GENERICO LIKE 'JEFE_AREA_%'))\n"
            + " WHERE OPERATOR_CODE =:roleReemplazo ";

    private final String SQL_UPDATE = "UPDATE DESGLO_REE_WK SET COD_REG_STATUS = 1, FEC_ELIM_REEMPLAZO = SYSDATE, NUM_ROL_EMPLEADO_ELIM = :operadorCode   WHERE COD_CARGO "
        + "=:codCargo"
        + "         AND NUM_ROL_EMPLEADO =:numRolEmpleado AND DES_COD_CARGO =:desCodCargo AND DES_NUM_ROL_EMPLEADO =:desNumRol";

    public DesgloseReemplazoService(DesgloseReemplazoRepository desgloseReemplazoRepository, DesgloseReemplazoMapper desgloseReemplazoMapper, SQLDGLRepository SQLDGLRepository) {
        this.desgloseReemplazoRepository = desgloseReemplazoRepository;
        this.desgloseReemplazoMapper = desgloseReemplazoMapper;
        this.SQLDGLRepository = SQLDGLRepository;
    }

    /**
     * Save a desgloseReemplazo.
     *
     * @param desgloseReemplazoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseReemplazoDTO save(DesgloseReemplazoDTO desgloseReemplazoDTO) {
        log.info("INICIO - Request to save DesgloseReemplazo : {}", desgloseReemplazoDTO);
        DesgloseReemplazo desgloseReemplazo = desgloseReemplazoMapper.toEntity(desgloseReemplazoDTO);
        desgloseReemplazo = desgloseReemplazoRepository.save(desgloseReemplazo);
        log.info("FIN - Request to save DesgloseReemplazo : {}", desgloseReemplazoDTO);
        return desgloseReemplazoMapper.toDto(desgloseReemplazo);
    }

    /**
     * Get all the desgloseReemplazos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseReemplazoDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseReemplazos");
        log.info("FIN - Request to get all DesgloseReemplazos");
        return desgloseReemplazoRepository.findAll(pageable).map(desgloseReemplazoMapper::toDto);
    }

    /**
     * Get one desgloseReemplazo by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseReemplazoDTO> findOne(DesgloseReemplazoPK id) {
        log.info("INICIO - Request to get DesgloseReemplazo : {}", id);
        log.info("FIN - Request to get DesgloseReemplazo : {}", id);
        return desgloseReemplazoRepository.findById(id).map(desgloseReemplazoMapper::toDto);
    }
    @Transactional(readOnly = true)
    public DesgloseReemplazoDTO findFirstByDesCodCargoAndDesNumRolEmpleado(String desNumRolEmpleado){
        DesgloseReemplazo desgloseReemplazo = desgloseReemplazoRepository.findFirstByDesNumRolEmpleado(desNumRolEmpleado);
        return  desgloseReemplazoMapper.toDto(desgloseReemplazo);
    }
    /**
     * Delete the desgloseReemplazo by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseReemplazoPK id) {
        log.info("INICIO - Request to delete DesgloseReemplazo : {}", id);
        log.info("FIN - Request to delete DesgloseReemplazo : {}", id);
        desgloseReemplazoRepository.deleteById(id);
    }

    public void InsertarReemplazo(DesgloseReemplazoDTO desgloseReemplazoDTO) {
        log.info("INICIO - InsertarReemplazo");
        Map<String, Object> listaParametros = new HashMap<String, Object>();
        listaParametros.put("cargo", desgloseReemplazoDTO.getCodCargo());
        listaParametros.put("roleReemplazar", desgloseReemplazoDTO.getNumRolEmpleado());
        listaParametros.put("roleReemplazo", desgloseReemplazoDTO.getDesNumRolEmpleado());
        listaParametros.put("operatorCode", desgloseReemplazoDTO.getNumRolEmpleadoCrea());
        this.SQLDGLRepository.executeQuery(SQL_INSERT_REEMPLAZOS, listaParametros);

        log.debug("QUERY : " + SQL_INSERT_REEMPLAZOS);
        log.debug("PARAMETROS : " + listaParametros);
        log.info("FIN - InsertarReemplazo");
    }

    /**
     * @param desgloseReemplazoDTO
     *
     * @author carlos eduardo rodriguez perez
     */
    public void UpdateReemplazo(DesgloseReemplazoDTO desgloseReemplazoDTO) {
        log.info("INICIO - UpdateReemplazo");
        Map<String, Object> listaParametros = new HashMap<String, Object>();
        listaParametros.put("operadorCode", desgloseReemplazoDTO.getNumRolEmpleadoElim());
        listaParametros.put("codCargo", desgloseReemplazoDTO.getCodCargo());
        listaParametros.put("numRolEmpleado", desgloseReemplazoDTO.getNumRolEmpleado());
        listaParametros.put("desCodCargo", desgloseReemplazoDTO.getDesCodCargo());
        listaParametros.put("desNumRol", desgloseReemplazoDTO.getDesNumRolEmpleado());
        this.SQLDGLRepository.executeQuery(SQL_UPDATE, listaParametros);

        log.debug("UpdateReemplazo : " + SQL_UPDATE );
        log.debug("UpdateReemplazo : " + listaParametros );
        log.info("FIN - UpdateReemplazo");
    }
}
