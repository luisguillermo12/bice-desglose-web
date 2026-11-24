package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.SfStfOperator;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.repository.SfStfOperatorRepository;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.dto.SfStfOperatorDTO;
import cl.tutorial.desglose.service.dto.TurbineUserDTO;
import cl.tutorial.desglose.service.mapper.SfStfOperatorMapper;
import cl.tutorial.desglose.web.rest.errors.InternalServerErrorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing SfStfOperator.
 */
@Service
@Transactional
public class SfStfOperatorService {

    private final Logger log = LogManager.getLogger(SfStfOperatorService.class);

    private final String SQL_QUERY_LISTAR_ANALISTAS =
        "SELECT\n" + "    num_rol_empleado,\n" + "    nls_initcap(turbine_user.first_name\n" + "                || ' '\n" + "                || turbine_user.last_name)\n"
            + "    || DECODE(flg, '1', '', DECODE(flg, 2, ' (POR REEMPLAZO)', DECODE(flg, 3, ' (POR REEMPLAZO)', ''))) gls_nombre\n" + "FROM\n" + "    (\n" + "        SELECT\n"
            + "            des_num_rol_empleado   num_rol_empleado,\n" + "            0 flg\n" + "        FROM\n" + "            desglo_grupo_wk\n";

    private final String SQL_QUERY_LISTAR_ANALISTAS_1 = " WHERE NUM_ROL_EMPLEADO =:CodeUser";
    private final String SQL_QUERY_LISTAR_ANALISTAS_2 =
        " UNION" + " SELECT NUM_ROL_EMPLEADO_2FIR NUM_ROL_EMPLEADO, 1 FLG" + " FROM DESGLO_FIR_WK" + " WHERE   NUM_ROL_EMPLEADO =:CodeUser";
    private final String SQL_QUERY_LISTAR_ANALISTAS_3 = " UNION" + " SELECT GRP.DES_NUM_ROL_EMPLEADO, 2 FLG" + " FROM DESGLO_REE_WK REE"
        + " INNER JOIN DESGLO_GRUPO_WK GRP ON (GRP.COD_CARGO = REE.COD_CARGO AND GRP.NUM_ROL_EMPLEADO = REE.NUM_ROL_EMPLEADO)" + " WHERE COD_REG_STATUS = 0"
        + "  AND REE.DES_NUM_ROL_EMPLEADO IN (:CodeUser)" + " UNION" + " SELECT FIR.NUM_ROL_EMPLEADO_2FIR NUM_ROL_EMPLEADO, 3 FLG"
        + " FROM DESGLO_REE_WK REE" + " INNER JOIN DESGLO_FIR_WK FIR ON (REE.COD_CARGO = FIR.COD_CARGO AND REE.NUM_ROL_EMPLEADO = FIR.NUM_ROL_EMPLEADO)" + " WHERE COD_REG_STATUS "
        + "= 0"
        + "  AND DES_NUM_ROL_EMPLEADO =:CodeUser ";
    private final String SQL_QUERY_LISTAR_ANALISTAS_4 = " ) " + " INNER JOIN SF_STF_OPERATOR ON (SF_STF_OPERATOR.OPERATOR_CODE = NUM_ROL_EMPLEADO)"
        + " INNER JOIN TURBINE_USER ON (TURBINE_USER.LOGIN_NAME = SF_STF_OPERATOR.LOGIN_NAME)" + " ORDER BY TURBINE_USER.FIRST_NAME, TURBINE_USER.LAST_NAME";

    private final SfStfOperatorRepository  sfStfOperatorRepository;
    private final SfStfOperatorMapper      sfStfOperatorMapper;
    private final DesgloseReemplazoService desgloseReemplazoService;
    private final SQLDGLRepository         SQLDGLRepository;
    private final TurbineUserService       turbineUserService;

    public SfStfOperatorService(SfStfOperatorRepository sfStfOperatorRepository, SfStfOperatorMapper sfStfOperatorMapper, DesgloseReemplazoService desgloseReemplazoService,
        SQLDGLRepository SQLDGLRepository, TurbineUserService turbineUserService) {
        this.sfStfOperatorRepository = sfStfOperatorRepository;
        this.sfStfOperatorMapper = sfStfOperatorMapper;
        this.desgloseReemplazoService = desgloseReemplazoService;
        this.SQLDGLRepository = SQLDGLRepository;
        this.turbineUserService = turbineUserService;
    }

    /**
     * Save a sfStfOperator.
     *
     * @param sfStfOperatorDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public SfStfOperatorDTO save(SfStfOperatorDTO sfStfOperatorDTO) {
        log.info("Request to save SfStfOperator : {}", sfStfOperatorDTO);

        SfStfOperator sfStfOperator = sfStfOperatorMapper.toEntity(sfStfOperatorDTO);
        sfStfOperator = sfStfOperatorRepository.save(sfStfOperator);
        return sfStfOperatorMapper.toDto(sfStfOperator);
    }

    /**
     * Get all the sfStfOperators.
     *
     * @param pageable
     * 		the pagination information;
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SfStfOperatorDTO> findAll(Pageable pageable) {
        log.info("Request to get all SfStfOperators");
        return sfStfOperatorRepository.findAll(pageable).map(sfStfOperatorMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<SfStfOperatorDTO> listarAnalistas() {
        List<SfStfOperatorDTO> listaAnalistas = new LinkedList<>();
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        // final User user = this.userService.findOneByLogin(userLogin);
        final TurbineUserDTO user = this.turbineUserService.findByLoginName(userLogin);
        final SfStfOperatorDTO sfStfOperatorDTO = this.findFirstByLoginName(userLogin);
        final String userAuthority = SecurityUtils.getCurrentUserAuthority().orElseThrow(() -> new InternalServerErrorException("Current user has no authority"));

        StringBuilder filtro = new StringBuilder(this.SQL_QUERY_LISTAR_ANALISTAS);

        if (!userAuthority.equals(PLAT_JEFE)) {
            filtro.append(this.SQL_QUERY_LISTAR_ANALISTAS_1);
        }
        if (userAuthority.equals(PLAT_REVISOR)) {
            filtro.append(this.SQL_QUERY_LISTAR_ANALISTAS_2);
        }

        if (this.desgloseReemplazoService.findFirstByDesCodCargoAndDesNumRolEmpleado(sfStfOperatorDTO.getOperatorCode().toString()) != null) {
            filtro.append(this.SQL_QUERY_LISTAR_ANALISTAS_3);
        }
        filtro.append(this.SQL_QUERY_LISTAR_ANALISTAS_4);
        Map<String, Object> listaParametros = new HashMap<String, Object>();
        listaParametros.put("CodeUser", sfStfOperatorDTO.getOperatorCode());
        log.debug("QUERY: " + filtro.toString());
        log.debug("PARAMETROS: " + listaParametros);
        List<Object[]> lista = this.SQLDGLRepository.execute(filtro.toString(), listaParametros);
        for (Object[] item : lista) {
            SfStfOperatorDTO sfStfOperator = new SfStfOperatorDTO();
            sfStfOperator.setOperatorCode(Integer.parseInt(item[0].toString()));
            sfStfOperator.setLoginName(item[1].toString());
            listaAnalistas.add(sfStfOperator);
        }
        return listaAnalistas;
    }

    /**
     * Get one sfStfOperator by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SfStfOperatorDTO> findOne(Long id) {
        log.info("Request to get SfStfOperator : {}", id);
        return sfStfOperatorRepository.findById(id).map(sfStfOperatorMapper::toDto);
    }

    /**
     * @param loginName
     *
     * @return
     */
    @Transactional(readOnly = true)
    public SfStfOperatorDTO findFirstByLoginName(String loginName) {
        return sfStfOperatorMapper.toDto(sfStfOperatorRepository.findFirstByLoginName(loginName));
    }

    /**
     * Delete the sfStfOperator by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete SfStfOperator : {}", id);
        sfStfOperatorRepository.deleteById(id);
    }
}
