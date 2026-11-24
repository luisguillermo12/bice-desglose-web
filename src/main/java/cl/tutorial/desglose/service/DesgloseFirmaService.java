package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseFirma;
import cl.tutorial.desglose.domain.pk.DesgloseFirmaPK;
import cl.tutorial.desglose.repository.DesgloseFirmaRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.DesgloseFirmaDTO;
import cl.tutorial.desglose.service.mapper.DesgloseFirmaMapper;
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
 * Service Implementation for managing DesgloseFirma.
 */
@Service
@Transactional
public class DesgloseFirmaService {

    private final Logger log = LogManager.getLogger(DesgloseFirmaService.class);

    private final DesgloseFirmaRepository desgloseFirmaRepository;

    private final DesgloseFirmaMapper desgloseFirmaMapper;

    private final SQLDGLRepository SQLDGLRepository;

    private final String  SQL_GET_FIRMAS = "SELECT NUM_ROL_EMPLEADO, NUM_ROL_EMPLEADO_2FIR, NLS_INITCAP(FIRST_NAME || ' ' || LAST_NAME) GLS_NOMBRE \n"+
        "            FROM DESGLO_FIR_WK FIR \n"+
        "            INNER JOIN SF_STF_OPERATOR STF ON (STF.OPERATOR_CODE = FIR.NUM_ROL_EMPLEADO_2FIR) \n"+
        "            LEFT JOIN TURBINE_USER USR ON (USR.LOGIN_NAME = STF.LOGIN_NAME) \n"+
        "          ORDER BY GLS_NOMBRE";
    public DesgloseFirmaService(DesgloseFirmaRepository desgloseFirmaRepository, DesgloseFirmaMapper desgloseFirmaMapper, SQLDGLRepository SQLDGLRepository) {
        this.desgloseFirmaRepository = desgloseFirmaRepository;
        this.desgloseFirmaMapper = desgloseFirmaMapper;
        this.SQLDGLRepository = SQLDGLRepository;
    }

    /**
     * Save a desgloseFirma.
     *
     * @param desgloseFirmaDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseFirmaDTO save(DesgloseFirmaDTO desgloseFirmaDTO) {
        log.info("INICIO - Request to save DesgloseFirma : {}", desgloseFirmaDTO);

        DesgloseFirma desgloseFirma = desgloseFirmaMapper.toEntity(desgloseFirmaDTO);
        desgloseFirma = desgloseFirmaRepository.save(desgloseFirma);
        log.info("FIN - Request to save DesgloseFirma : {}", desgloseFirmaDTO);
        return desgloseFirmaMapper.toDto(desgloseFirma);
    }

    /**
     * Get all the desgloseFirmas.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseFirmaDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseFirmas");
        log.info("FIN - Request to get all DesgloseFirmas");
        return desgloseFirmaRepository.findAll(pageable).map(desgloseFirmaMapper::toDto);
    }

    /** get lista de firmas asociadas
     * @author carlos eduardo rodriguez perez
     * @return
     */
    @Transactional(readOnly = true)
    public List<?> getAllFirmas(){
        log.info("INICIO - getAllFirmas() ");
        Map<String,Object> listaParametros = new HashMap<String, Object>();
        log.debug("QUERY : " +SQL_GET_FIRMAS );
        log.debug("PARAMETROS : " +listaParametros.toString() );
        List<?> listaGrupos = SQLDGLRepository.execute(SQL_GET_FIRMAS,listaParametros);
        log.info("FIN - getAllFirmas()");
        return listaGrupos;
    }
    /**
     * Get one desgloseFirma by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseFirmaDTO> findOne(DesgloseFirmaPK id) {
        log.info("INICIO - Request to get DesgloseFirma : {}", id);
        log.info("FIN - Request to get DesgloseFirma : {}", id);
        return desgloseFirmaRepository.findById(id).map(desgloseFirmaMapper::toDto);
    }

    /**
     * @author carlos eduardo rodriguez perez
     * @param numRolEmpleado
     * @param numRolEmpleado2Fir
     * @return
     */
    @Transactional(readOnly = true)
    public Integer validasegundafirma(String numRolEmpleado,String numRolEmpleado2Fir){
        log.info("INICIO - Valida segunda firma");
        log.info("FIN - Valida segunda firma");
        return desgloseFirmaRepository.findAllByNumRolEmpleadoAndNumRolEmpleado2fir(numRolEmpleado,numRolEmpleado2Fir).size();
    }

    /**
     *
     * @param numRolEmpleado2Fir
     * @return
     */
    @Transactional(readOnly = true)
    public List<DesgloseFirmaDTO>  findAllByNumRolEmpleado2fir(String numRolEmpleado2Fir){
        return desgloseFirmaMapper.toDto(desgloseFirmaRepository.findAllByNumRolEmpleado2fir(numRolEmpleado2Fir));
    }

    /**
     *
     * @param numRolEmpleado
     * @return
     */
    @Transactional(readOnly = true)
    public List<DesgloseFirmaDTO>  findAllByNumRolEmpleado(String numRolEmpleado){
        return desgloseFirmaMapper.toDto(desgloseFirmaRepository.findAllByNumRolEmpleado(numRolEmpleado));
    }
    /**
     * Delete the desgloseFirma by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseFirmaPK id) {
        log.info("INICIO - Request to delete DesgloseFirma : {}", id);
        log.info("FIN - Request to delete DesgloseFirma : {}", id);
        desgloseFirmaRepository.deleteByCodCargoAndNumRolEmpleadoAndNumRolEmpleado2fir(id.getCodCargo(),id.getNumRolEmpleado(),id.getNumRolEmpleado2fir());
    }
}
