package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseRevision;
import cl.tutorial.desglose.domain.pk.DesgloseRevisionPK;
import cl.tutorial.desglose.repository.DesgloseRevisionRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.dto.DesgloseRevisionDTO;
import cl.tutorial.desglose.service.dto.ReemplazosVistaDTO;
import cl.tutorial.desglose.service.dto.SfStfOperatorDTO;
import cl.tutorial.desglose.service.mapper.DesgloseRevisionMapper;
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
 * Service Implementation for managing DesgloseRevision.
 */
@Service
@Transactional
public class DesgloseRevisionService {

    private final Logger log = LogManager.getLogger(DesgloseRevisionService.class);

    private final DesgloseRevisionRepository desgloseRevisionRepository;

    private final DesgloseRevisionMapper desgloseRevisionMapper;

    private final SQLDGLRepository SQLDGLRepository;

    private final SfStfOperatorService sfStfOperatorService;

    private final String SQL_GET_REEMPLAZOSVISTA =
        "SELECT  R.COD_CARGO CGO1,\n" + "        R.NUM_ROL_EMPLEADO ROL1,\n" + "        NLS_INITCAP(USR1.FIRST_NAME || ' ' || USR1.LAST_NAME) NOM1,\n"
            + "        R.DES_COD_CARGO CGO2,\n" + "        R.DES_NUM_ROL_EMPLEADO ROL2,\n" + "        NLS_INITCAP(USR2.FIRST_NAME || ' ' || USR2.LAST_NAME) NOM2\n"
            + "        FROM DESGLO_REE_WK R\n" + "        LEFT JOIN SF_STF_OPERATOR SF1 ON (SF1.OPERATOR_CODE = R.NUM_ROL_EMPLEADO)\n"
            + "        LEFT JOIN TURBINE_USER USR1 ON (USR1.LOGIN_NAME = SF1.LOGIN_NAME)\n"
            + "        LEFT JOIN SF_STF_OPERATOR SF2 ON (SF2.OPERATOR_CODE = R.DES_NUM_ROL_EMPLEADO)\n"
            + "        LEFT JOIN TURBINE_USER USR2 ON (USR2.LOGIN_NAME = SF2.LOGIN_NAME)\n" + "    WHERE COD_REG_STATUS = 0\n" + "    ORDER BY NOM1, NOM2";

    private final String SQL_UPDATE_RECUPERAR = "UPDATE DESGLO_REV_WK SET fec_en_revision = SYSDATE, flg_notificado = 1"
        + " WHERE cod_cliente =:codCliente AND cod_moneda=:codMoneda AND cod_plantilla =:codPlantilla AND cod_tipo_des =:codTipoDes AND cod_subtipo_des =:codSubTipo";

    private final String SQL_UPDATE_DESHABILITA_DESGLOSE = "UPDATE DESGLO_REV_WK SET cod_reg_status = 1"
        + " WHERE cod_cliente =:codCliente AND cod_moneda=:codMoneda AND cod_plantilla =:codPlantilla AND cod_tipo_des =:codTipoDes AND cod_subtipo_des =:codSubTipo";

    private final String SQL_UPDATE_DEVOLVER_DESGLOSE =
        "UPDATE DESGLO_REV_WK SET cod_reg_status = 2, flg_notificado = 0,rol_digitador =:rolDigitador,gls_notificacion=:glsNotificacion"
            + " WHERE cod_cliente =:codCliente AND cod_moneda=:codMoneda AND cod_plantilla =:codPlantilla AND cod_tipo_des =:codTipoDes AND cod_subtipo_des =:codSubTipo";

    private final String SQL_UPDATE_APROBAR_DESGLOSE = "UPDATE DESGLO_REV_WK SET cod_reg_status = 3,fec_termino_revision = SYSDATE,rol_revisor_reem=:rolRevisorReem"
        + " WHERE cod_cliente =:codCliente AND cod_moneda=:codMoneda AND cod_plantilla =:codPlantilla AND cod_tipo_des =:codTipoDes AND cod_subtipo_des =:codSubTipo AND "
        + "cod_reg_status = 0";

    private final String SQL_GET_BUSQUEDA = "select cod_cliente,cod_tipo_des,cod_subtipo_des,cod_moneda,rol_digitador,rol_revisor,rol_revisor_reem,gls_notificacion,"
        + "flg_notificado,fec_envio_revision,fec_en_revision,fec_termino_revision,cod_reg_status,cod_plantilla "
        + " from desglo_rev_WK where cod_cliente=:codCliente and cod_tipo_des=:codTipoDes and cod_subtipo_des=:codSubTipo and cod_moneda=:codMoneda and "
        + " cod_plantilla=:codPlantilla and cod_reg_status=:codRegStatus";

    private final String SQL_GET_BUSQUEDATOTAL = "select cod_cliente,cod_tipo_des,cod_subtipo_des,cod_moneda,rol_digitador,rol_revisor,rol_revisor_reem,gls_notificacion,"
        + "flg_notificado,fec_envio_revision,fec_en_revision,fec_termino_revision,cod_reg_status,cod_plantilla "
        + " from desglo_rev_WK where cod_cliente=:codCliente and cod_tipo_des=:codTipoDes and cod_subtipo_des=:codSubTipo and cod_moneda=:codMoneda and "
        + " cod_plantilla=:codPlantilla";

    public DesgloseRevisionService(DesgloseRevisionRepository desgloseRevisionRepository, DesgloseRevisionMapper desgloseRevisionMapper, SQLDGLRepository SQLDGLRepository,
        SfStfOperatorService sfStfOperatorService) {
        this.desgloseRevisionRepository = desgloseRevisionRepository;
        this.desgloseRevisionMapper = desgloseRevisionMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.sfStfOperatorService = sfStfOperatorService;
    }

    /**
     * Save a desgloseRevision.
     *
     * @param desgloseRevisionDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseRevisionDTO save(DesgloseRevisionDTO desgloseRevisionDTO) {
        log.info("INICIO -Request to save DesgloseRevision : {}", desgloseRevisionDTO);
        DesgloseRevision desgloseRevision = desgloseRevisionMapper.toEntity(desgloseRevisionDTO);
        desgloseRevision = desgloseRevisionRepository.save(desgloseRevision);
        log.info("FIN - Request to save DesgloseRevision : {}", desgloseRevisionDTO);
        return desgloseRevisionMapper.toDto(desgloseRevision);
    }

    /**
     * Get all the desgloseRevisions.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseRevisionDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseRevisions");
        log.info("FIN - Request to get all DesgloseRevisions");
        return desgloseRevisionRepository.findAll(pageable).map(desgloseRevisionMapper::toDto);
    }

    /**
     * @param desgloseRevisionPK
     *
     * @return
     */
    @Transactional(readOnly = true)
    public DesgloseRevisionDTO findOne(DesgloseRevisionPK desgloseRevisionPK) {
        log.info("INICIO - Request to get DesgloseRevision : {}", desgloseRevisionPK);
        log.info("FIN - Request to get DesgloseRevision : {}", desgloseRevisionPK);
        return desgloseRevisionMapper.toDto(
            this.busquedaRevision(desgloseRevisionPK.getCodCliente(), desgloseRevisionPK.getCodTipoDes(), desgloseRevisionPK.getCodSubtipoDes(), desgloseRevisionPK.getCodMoneda(),
                desgloseRevisionPK.getCodPlantilla(), COD_REG_STATUS_ENVIA_REE));
    }
    @Transactional(readOnly = true)
    public List<DesgloseRevisionDTO> findOneTotal(DesgloseRevisionPK desgloseRevisionPK) {
        log.info("Request to get DesgloseRevision : {}", desgloseRevisionPK);
        return desgloseRevisionMapper.toDto(
            this.busquedaRevisionTotal(desgloseRevisionPK.getCodCliente(), desgloseRevisionPK.getCodTipoDes(), desgloseRevisionPK.getCodSubtipoDes(), desgloseRevisionPK.getCodMoneda(),
                desgloseRevisionPK.getCodPlantilla()));
    }
    @Transactional(readOnly = true)
    public Integer indicadorNotificaciones() {
        Integer count = 0;
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        String code = getCurrentUserOperatorCode(userLogin);
        Collection<?> roles = SecurityUtils.getCurrentUserAuthorities().get();
        for (Object el : roles.toArray()) {
            String rol = ((Object) el).toString();
            if (rol.equals(PLAT_REVISOR) || rol.equals(PLAT_JEFE)) {
                count = desgloseRevisionRepository.countAllByNotificacionesRevisorOrJefe(code);
            } else if (rol.equals(PLAT_EJECUTOR)) {
                count = desgloseRevisionRepository.countAllByNotificacionesDigitador(code);
            }
        }
        return count;
    }

    @Transactional(readOnly = true)
    public DesgloseRevision busquedaRevision(String codCliente, Integer codTipoDes, Integer codSubTipo, String codMoneda, Integer codPlantilla, Integer codRegStatus) {
        log.info("INICIO - busquedaRevision ");
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("codCliente", codCliente);
        listaParametros.put("codTipoDes", codTipoDes);
        listaParametros.put("codSubTipo", codSubTipo);
        listaParametros.put("codMoneda", codMoneda);
        listaParametros.put("codPlantilla", codPlantilla);
        listaParametros.put("codRegStatus", codRegStatus);
        List<Object[]> lista = SQLDGLRepository.execute(SQL_GET_BUSQUEDA, listaParametros);

        log.debug("QUERY : " +SQL_GET_BUSQUEDA );
        log.debug("PARAMETROS : " +listaParametros.toString() );
        DesgloseRevision desgloseRevision = null;
        for (Object[] item : lista) {
            desgloseRevision = new DesgloseRevision();
            desgloseRevision.setCodCliente(item[0]!=null?item[0].toString():null);
            desgloseRevision.setCodTipoDes(Integer.parseInt(item[1].toString()));
            desgloseRevision.setCodSubtipoDes(Integer.parseInt(item[2].toString()));
            desgloseRevision.setCodMoneda(item[3]!=null?item[3].toString():null);
            desgloseRevision.setRolDigitador(item[4] != null ? item[4].toString() : null);
            desgloseRevision.setRolRevisor(item[5] != null ? item[5].toString() : null);
            desgloseRevision.setRolRevisorReem(item[6] != null ? item[6].toString() : null);
            desgloseRevision.setGlsNotificacion(item[7] != null ?item[7].toString():null);
            desgloseRevision.setFlgNotificado(Boolean.parseBoolean(item[8].toString()));
           /* LocalDate dateValue = LocalDate.parse(item[9].toString());
            desgloseRevision.setFecEnvioRevision(dateValue != null ? dateValue : null);
            dateValue = LocalDate.parse(item[10].toString());
            desgloseRevision.setFecEnRevision(dateValue != null ? dateValue : null);
            dateValue = LocalDate.parse(item[11].toString());
            desgloseRevision.setFecTerminoRevision(dateValue != null ? dateValue : null);*/
            desgloseRevision.setCodRegStatus(Integer.parseInt(item[12].toString()));
            desgloseRevision.setCodPlantilla(Integer.parseInt(item[13].toString()));
        }
        log.info("FIN - busquedaRevision");
        return desgloseRevision;
    }
    @Transactional(readOnly = true)
    public List<DesgloseRevision> busquedaRevisionTotal(String codCliente, Integer codTipoDes, Integer codSubTipo, String codMoneda, Integer codPlantilla) {
        log.info("INICIO - busquedaRevisionTotal");
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("codCliente", codCliente);
        listaParametros.put("codTipoDes", codTipoDes);
        listaParametros.put("codSubTipo", codSubTipo);
        listaParametros.put("codMoneda", codMoneda);
        listaParametros.put("codPlantilla", codPlantilla);
        log.debug("QUERY : " + SQL_GET_BUSQUEDATOTAL);
        log.debug("PARAMETROS : " + listaParametros.toString());
        List<Object[]> lista = SQLDGLRepository.execute(SQL_GET_BUSQUEDATOTAL, listaParametros);
        List<DesgloseRevision> listaReev = new LinkedList<>();
        for (Object[] item : lista) {
            DesgloseRevision desgloseRevision = new DesgloseRevision();
            desgloseRevision.setCodCliente(item[0]!=null?item[0].toString():null);
            desgloseRevision.setCodTipoDes(Integer.parseInt(item[1].toString()));
            desgloseRevision.setCodSubtipoDes(Integer.parseInt(item[2].toString()));
            desgloseRevision.setCodMoneda(item[3]!=null?item[3].toString():null);
            desgloseRevision.setRolDigitador(item[4] != null ? item[4].toString() : null);
            desgloseRevision.setRolRevisor(item[5] != null ? item[5].toString() : null);
            desgloseRevision.setRolRevisorReem(item[6] != null ? item[6].toString() : null);
            desgloseRevision.setGlsNotificacion(item[7] != null ?item[7].toString():null);
            desgloseRevision.setFlgNotificado(Boolean.parseBoolean(item[8].toString()));
           /* LocalDate dateValue = LocalDate.parse(item[9].toString());
            desgloseRevision.setFecEnvioRevision(dateValue != null ? dateValue : null);
            dateValue = LocalDate.parse(item[10].toString());
            desgloseRevision.setFecEnRevision(dateValue != null ? dateValue : null);
            dateValue = LocalDate.parse(item[11].toString());
            desgloseRevision.setFecTerminoRevision(dateValue != null ? dateValue : null);*/
            desgloseRevision.setCodRegStatus(Integer.parseInt(item[12].toString()));
            desgloseRevision.setCodPlantilla(Integer.parseInt(item[13].toString()));
            listaReev.add(desgloseRevision);
        }
        log.info("FIN - busquedaRevisionTotal");
        return listaReev;
    }
    public void updateRecuperar(DesgloseRevision desgloseRevision) {
        log.info("INICIO - updateRecuperar ");
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("codCliente", desgloseRevision.getCodCliente());
        listaParametros.put("codTipoDes", desgloseRevision.getCodTipoDes());
        listaParametros.put("codSubTipo", desgloseRevision.getCodSubtipoDes());
        listaParametros.put("codMoneda", desgloseRevision.getCodMoneda());
        listaParametros.put("codPlantilla", desgloseRevision.getCodPlantilla());
        SQLDGLRepository.executeQuery(SQL_UPDATE_RECUPERAR, listaParametros);

        log.debug("QUERY : " + SQL_UPDATE_RECUPERAR);
        log.debug("PARAMETROS : " + listaParametros.toString());
        log.info("FIN - updateRecuperar ");
    }

    public void updateDeshabilita(DesgloseRevision desgloseRevision) {
        log.info("INICIO - updateDeshabilita");
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("codCliente", desgloseRevision.getCodCliente());
        listaParametros.put("codTipoDes", desgloseRevision.getCodTipoDes());
        listaParametros.put("codSubTipo", desgloseRevision.getCodSubtipoDes());
        listaParametros.put("codMoneda", desgloseRevision.getCodMoneda());
        listaParametros.put("codPlantilla", desgloseRevision.getCodPlantilla());
        SQLDGLRepository.executeQuery(SQL_UPDATE_DESHABILITA_DESGLOSE, listaParametros);
        log.debug("QUERY: " + SQL_UPDATE_DESHABILITA_DESGLOSE);
        log.debug("PARAMETROS: " + listaParametros.toString());
        log.info("FIN - updateDeshabilita");
    }

    public void updateDevolver(DesgloseRevision desgloseRevision) {
        log.info("INICIO - updateDevolver ");
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("codCliente", desgloseRevision.getCodCliente());
        listaParametros.put("codTipoDes", desgloseRevision.getCodTipoDes());
        listaParametros.put("codSubTipo", desgloseRevision.getCodSubtipoDes());
        listaParametros.put("codMoneda", desgloseRevision.getCodMoneda());
        listaParametros.put("codPlantilla", desgloseRevision.getCodPlantilla());
        listaParametros.put("rolDigitador", desgloseRevision.getRolDigitador());
        listaParametros.put("glsNotificacion", desgloseRevision.getGlsNotificacion());
        SQLDGLRepository.executeQuery(SQL_UPDATE_DEVOLVER_DESGLOSE, listaParametros);

        log.debug("QUERY : " + SQL_UPDATE_DEVOLVER_DESGLOSE);
        log.debug("PARAMETROS : " + listaParametros.toString());

        log.info("FIN - updateDevolver");
    }

    public void updateAprobar(DesgloseRevision desgloseRevision) {
        log.info("INICIO - updateAprobar ");
        Map<String, Object> listaParametros = new HashMap<>();
        listaParametros.put("codCliente", desgloseRevision.getCodCliente());
        listaParametros.put("codTipoDes", desgloseRevision.getCodTipoDes());
        listaParametros.put("codSubTipo", desgloseRevision.getCodSubtipoDes());
        listaParametros.put("codMoneda", desgloseRevision.getCodMoneda());
        listaParametros.put("codPlantilla", desgloseRevision.getCodPlantilla());
        listaParametros.put("rolRevisorReem", desgloseRevision.getRolRevisorReem());
        SQLDGLRepository.executeQuery(SQL_UPDATE_APROBAR_DESGLOSE, listaParametros);

        log.debug("QUERY : " + SQL_UPDATE_APROBAR_DESGLOSE);
        log.debug("PARAMETROS : " + listaParametros.toString());

        log.info("FIN - updateAprobar ");
    }

    /**
     * Delete the desgloseRevision by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseRevisionPK id) {
        log.info("Request to delete DesgloseRevision : {}", id);
        desgloseRevisionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReemplazosVistaDTO> findAllReemplazosVista() {
        log.info("INICIO - findAllReemplazosVista");
        List<ReemplazosVistaDTO> listaReemplazos = new LinkedList<>();
        Map<String, Object> listaParametros = new HashMap<>();
        log.debug("QUERY : " +SQL_GET_REEMPLAZOSVISTA );
        log.debug("PARAMETROS : " +listaParametros.toString() );
        List<Object[]> lista = SQLDGLRepository.execute(SQL_GET_REEMPLAZOSVISTA, listaParametros);
        for (Object[] item : lista) {

            ReemplazosVistaDTO reemplazosVistaDTO =
                new ReemplazosVistaDTO(item[0].toString(), Long.parseLong(item[1].toString()), item[2].toString(), item[3].toString(), Long.parseLong(item[4].toString()),
                    item[5].toString());
            ((LinkedList<ReemplazosVistaDTO>) listaReemplazos).push(reemplazosVistaDTO);
        }
        log.info("FIN - findAllReemplazosVista");
        return listaReemplazos;
    }

    public String getCurrentUserOperatorCode(String userLoginName) {
        final SfStfOperatorDTO firstByLoginName = sfStfOperatorService.findFirstByLoginName(userLoginName);
        return firstByLoginName != null ? firstByLoginName.getOperatorCode().toString() : null;
    }
}
