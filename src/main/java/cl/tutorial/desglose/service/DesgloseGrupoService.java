package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseGrupo;
import cl.tutorial.desglose.domain.SfStfOperator;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUser;
import cl.tutorial.desglose.domain.pk.DesgloseGrupoPK;
import cl.tutorial.desglose.repository.DesgloseGrupoRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.repository.SfStfOperatorRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserRepository;
import cl.tutorial.desglose.service.dto.DesgloseGrupoDTO;
import cl.tutorial.desglose.service.mapper.DesgloseGrupoMapper;
import cl.tutorial.desglose.web.rest.errors.InternalServerErrorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing DesgloseGrupo.
 */
@Service
@Transactional
public class DesgloseGrupoService {

    private final Logger log = LogManager.getLogger(DesgloseGrupoService.class);

    private final DesgloseGrupoRepository desgloseGrupoRepository;
    private final TurbineUserRepository   turbineUserRepository;
    private final SfStfOperatorRepository sfStfOperatorRepository;
    private final DesgloseGrupoMapper     desgloseGrupoMapper;
    private final SQLDGLRepository        SQLDGLRepository;

    private final String SQL_Delete_UserbyIdEmpleado =
        "DELETE FROM DESGLO_GRUPO_WK WHERE DES_COD_CARGO = 'CARGO-003'\n" + "          AND DES_NUM_ROL_EMPLEADO \n" + "          IN (SELECT OPERATOR_CODE  FROM TURBINE_USER USR "
            + " \n"
            + "              INNER JOIN SF_STF_OPERATOR STF ON (STF.LOGIN_NAME = USR.LOGIN_NAME)\n" + "              WHERE USR.USER_ID =:userId )";

    public DesgloseGrupoService(DesgloseGrupoRepository desgloseGrupoRepository, TurbineUserRepository turbineUserRepository, SfStfOperatorRepository sfStfOperatorRepository,
        DesgloseGrupoMapper desgloseGrupoMapper, SQLDGLRepository SQLDGLRepository) {
        this.desgloseGrupoRepository = desgloseGrupoRepository;
        this.turbineUserRepository = turbineUserRepository;
        this.sfStfOperatorRepository = sfStfOperatorRepository;
        this.desgloseGrupoMapper = desgloseGrupoMapper;
        this.SQLDGLRepository = SQLDGLRepository;
    }

    /**
     * Save a desgloseGrupo.
     *
     * @param desgloseGrupoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseGrupoDTO save(DesgloseGrupoDTO desgloseGrupoDTO) {
        log.info("INICIO - Request to save DesgloseGrupo : {}", desgloseGrupoDTO);

        DesgloseGrupo desgloseGrupo = desgloseGrupoMapper.toEntity(desgloseGrupoDTO);
        desgloseGrupo = desgloseGrupoRepository.save(desgloseGrupo);
        log.info("FIN - Request to save DesgloseGrupo : {}", desgloseGrupoDTO);
        return desgloseGrupoMapper.toDto(desgloseGrupo);
    }

    /**
     * Get all the desgloseGrupos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseGrupoDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseGrupos");
        log.info("FIN - Request to get all DesgloseGrupos");
        return desgloseGrupoRepository.findAll(pageable).map(desgloseGrupoMapper::toDto);
    }

    /**
     * Get one desgloseGrupo by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseGrupoDTO> findOne(DesgloseGrupoPK id) {
        log.info("INICIO - Request to get DesgloseGrupo : {}", id);
        log.info("FIN - Request to get DesgloseGrupo : {}", id);
        return desgloseGrupoRepository.findById(id).map(desgloseGrupoMapper::toDto);
    }

    /**
     * @param numRolEmpleado
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<DesgloseGrupoDTO> findAllByNumRolEmpleado(String numRolEmpleado) {
        return desgloseGrupoMapper.toDto(desgloseGrupoRepository.findAllByNumRolEmpleado(numRolEmpleado));
    }

    /**
     * Delete the desgloseGrupo by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseGrupoPK id) {
        log.info("INICIO - Request to delete DesgloseGrupo : {}", id);
        log.info("FIN - Request to delete DesgloseGrupo : {}", id);
        desgloseGrupoRepository.deleteById(id);
    }

    /**
     * Delete the degloseGrupo By Entidad
     *
     * @param desgloseGrupoDTO
     *
     * @author carlos eduardo rodriguez perez
     */
    public void deleteByDesgloseGrupo(DesgloseGrupoDTO desgloseGrupoDTO) {
        log.info("INICIO - deleteByDesgloseGrupo ");
        DesgloseGrupo desgloseGrupo = desgloseGrupoMapper.toEntity(desgloseGrupoDTO);
        desgloseGrupoRepository.delete(desgloseGrupo);
        log.info("FIN - deleteByDesgloseGrupo");
    }

    /**
     * Elimina datos de la tabla DESGLO_GRUPO_WK enviandole como parametro el rol_empleado author Carlos Eduardo Rodriguez Perez
     *
     * @param idUser
     */
    public void deleteByUserIdEmpleado(Long idUser) {
        log.info("INICIO - deleteByUserIdEmpleado ");
        Map<String, Object> listaParametros = new HashMap<String, Object>();
        listaParametros.put("userId", idUser);
        log.debug("QUERY : " +SQL_Delete_UserbyIdEmpleado );
        log.debug("PARAMETROS : " +listaParametros.toString() );
        SQLDGLRepository.executeQuery(SQL_Delete_UserbyIdEmpleado, listaParametros);
        log.info("FIN - deleteByUserIdEmpleado");
    }

    @Transactional(readOnly = true)
    public DesgloseGrupoDTO findFirstByDesNumRolEmpleado(String rolEmpleado) {
        log.info("INICIO - Busqueda de jefe de grupo de un ejecutor");
        DesgloseGrupoDTO desgloseGrupoDTO = null;
        DesgloseGrupo desgloseGrupo = desgloseGrupoRepository.findFirstByDesNumRolEmpleado(rolEmpleado);
        if(desgloseGrupo!= null) {
            final SfStfOperator stfOperator = this.sfStfOperatorRepository.findOneByOperatorCode(Integer.valueOf(desgloseGrupo.getNumRolEmpleado()))
                .orElseThrow(() -> new InternalServerErrorException("The user with code " + desgloseGrupo.getNumRolEmpleado() + " can not be found un the SF_STF scheme"));
            final TurbineUser turbineUser = this.turbineUserRepository.findOneByLoginName(stfOperator.getLoginName())
                .orElseThrow(() -> new InternalServerErrorException("The user with login name " + stfOperator.getLoginName() + "can not be found un the TURBINE scheme"));
            desgloseGrupoDTO = desgloseGrupoMapper.toDto(desgloseGrupo);
            desgloseGrupoDTO.setGlsNumRolEmpleado(turbineUser.getFullName());
        }
        log.info("FIN - Busqueda de jefe de grupo de un ejecutor");
        return desgloseGrupoDTO;
    }
}
