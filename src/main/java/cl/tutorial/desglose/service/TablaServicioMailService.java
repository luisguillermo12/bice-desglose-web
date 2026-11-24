package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.bctdesglose.repository.SQLBCTRepository;
import cl.tutorial.desglose.domain.TablaServicioMail;
import cl.tutorial.desglose.domain.pk.TablaServicioMailPK;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.repository.TablaServicioMailRepository;
import cl.tutorial.desglose.service.dto.CorreoDTO;
import cl.tutorial.desglose.service.dto.TablaServicioMailDTO;
import cl.tutorial.desglose.service.mapper.TablaServicioMailMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.COD_TIPO_DESTINO;

/**
 * Service Implementation for managing TablaServicioMail.
 */
@Service
@Transactional
public class TablaServicioMailService {

    private final Logger log = LogManager.getLogger(TablaServicioMailService.class);

    private static final String SQL_QUERY_CORREO =
        "SELECT\n" + "    tm.fec_solicitud,\n" + "    tm.num_solicitud,\n" + "    tm.gls_from_nombre,\n" + "    tm.gls_from_mail,\n" + "    td.gls_to_nombre,\n"
            + "    td.gls_to_mail,\n" + "    tm.gls_asunto \n" + "FROM\n" + "    tbl_srv_mail tm\n"
            + "           INNER JOIN TBL_SRV_MAIL_DEST TD ON (TM.NUM_SOLICITUD = TD.NUM_SOLICITUD AND TD.COD_TIPO_DEST = 'TO' ) \n"
            + "           INNER JOIN TURBINE_USER UE ON (UE.EMAIL = TM.GLS_FROM_MAIL ) \n"
            + "           INNER JOIN TURBINE_ROLE LE ON ( LE.ROLE_NAME = 'Plat_Desglose_Ejecutor' ) \n"
            + "           INNER JOIN TURBINE_USER_GROUP_ROLE RE ON (UE.USER_ID = RE.USER_ID AND RE.ROLE_ID = LE.ROLE_ID )\n"
            + "           INNER JOIN TURBINE_USER UR ON (UR.EMAIL = TD.GLS_TO_MAIL ) \n"
            + "           INNER JOIN TURBINE_ROLE LR ON ( LR.ROLE_NAME = 'Plat_Desglose_Revisor' OR LR.ROLE_NAME = 'Plat_Desglose_Jefe' ) \n"
            + "           INNER JOIN TURBINE_USER_GROUP_ROLE RR ON (UR.USER_ID = RR.USER_ID AND RR.ROLE_ID = LR.ROLE_ID ) \n"
            + "           INNER JOIN DESCOD DC ON (DC.COD_REG_STATUS = 0 AND DC.COD_TABLA = 'DGLCONFG' AND DC.COD_GENERICO = 'FLG_FIX_MAIL_REVISOR' AND DC.GLS_GENERICACORTA = "
            + "'S' ) \n"
            + "           WHERE TM.COD_SISTEMA IS NOT NULL \n" + "            AND TM.COD_SISTEMA = 'DesgloseVB'\n" + "            AND TM.COD_ESTADO <> 'OK' \n"
            + "            AND TM.GLS_FROM_MAIL <> TD.GLS_TO_MAIL\n" + "            AND ( tm.gls_asunto LIKE '%Validado%' \n"
            + "                OR tm.gls_asunto LIKE '%Corregido%'\n" + "                OR tm.gls_asunto LIKE '%Con Observaciones%' ) \n"
            + "           order by tm.fec_solicitud desc ";

    private static final String SQL_QUERY_UPDATE_MAIL = "UPDATE TBL_SRV_MAIL SET GLS_FROM_NOMBRE =:GLSFROM_NOMBRE , GLS_FROM_MAIL =:GLSFROM_MAIL  \n"
        + "                WHERE NUM_SOLICITUD =:N_SOLICITUD ";

    private static final String SQL_QUERY_UPDATE_MAIL_DEST = "UPDATE TBL_SRV_MAIL_DEST SET GLS_TO_NOMBRE =:GLSTO_NOMBRE , GLS_TO_MAIL =:GLSTO_MAIL \n"
        + "                WHERE NUM_SOLICITUD =:N_SOLICITUD AND COD_TIPO_DEST =:COD_DEST ";


    private final TablaServicioMailRepository tablaServicioMailRepository;

    private final TablaServicioMailMapper tablaServicioMailMapper;

    private final SQLDGLRepository SQLDGLRepository;

    private final SQLBCTRepository sqlbctRepository;
    public TablaServicioMailService(TablaServicioMailRepository tablaServicioMailRepository,
        TablaServicioMailMapper tablaServicioMailMapper, SQLDGLRepository SQLDGLRepository,SQLBCTRepository sqlbctRepository) {
        this.tablaServicioMailRepository = tablaServicioMailRepository;
        this.tablaServicioMailMapper = tablaServicioMailMapper;
        this.SQLDGLRepository = SQLDGLRepository;
        this.sqlbctRepository = sqlbctRepository;
    }

    /**
     * Save a tablaServicioMail.
     *
     * @param tablaServicioMailDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public TablaServicioMailDTO save(TablaServicioMailDTO tablaServicioMailDTO) {
        log.info("Request to save TablaServicioMail : {}", tablaServicioMailDTO);

        TablaServicioMail tablaServicioMail = tablaServicioMailMapper.toEntity(tablaServicioMailDTO);
        tablaServicioMail = tablaServicioMailRepository.save(tablaServicioMail);
        return tablaServicioMailMapper.toDto(tablaServicioMail);
    }

    /**
     * Get all the tablaServicioMails.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TablaServicioMailDTO> findAll(Pageable pageable) {
        log.info("Request to get all TablaServicioMails");
        return tablaServicioMailRepository.findAll(pageable).map(tablaServicioMailMapper::toDto);
    }

    /**
     * Get one tablaServicioMail by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TablaServicioMailDTO> findOne(TablaServicioMailPK id) {
        log.info("Request to get TablaServicioMail : {}", id);
        return tablaServicioMailRepository.findById(id).map(tablaServicioMailMapper::toDto);
    }

    /**
     * envia correos se ejecuta flujo de envio al momento de aprobar y devolver desglose
     */
    public void enviarCorreos(){
        log.info("INICIO- enviarCorreos");
        List<CorreoDTO> listaCorreos = findAllCorreos();
        for (CorreoDTO item: listaCorreos) {

            Map<String, Object> listaParametros = new HashMap<String, Object>();
            listaParametros.put("GLSFROM_NOMBRE", item.getGlsFromNombre());
            listaParametros.put("GLSFROM_MAIL", item.getGlsFromMail());
            listaParametros.put("N_SOLICITUD", item.getNumSolicitud());
            log.debug("QUERY: " + SQL_QUERY_UPDATE_MAIL);
            log.debug("PARAMETROS: " + listaParametros.toString());
            this.sqlbctRepository.executeQuery(SQL_QUERY_UPDATE_MAIL, listaParametros);

            listaParametros = new HashMap<String, Object>();
            listaParametros.put("GLSTO_NOMBRE", item.getGlsToNombre());
            listaParametros.put("GLSTO_MAIL", item.getGlsToMail());
            listaParametros.put("N_SOLICITUD", item.getNumSolicitud());
            listaParametros.put("COD_DEST", COD_TIPO_DESTINO);
            log.debug("QUERY: " + SQL_QUERY_UPDATE_MAIL_DEST);
            log.debug("PARAMETROS: " + listaParametros.toString());
            this.sqlbctRepository.executeQuery(SQL_QUERY_UPDATE_MAIL_DEST, listaParametros);
        }
        log.info("FIN- enviarCorreos");
    }

    /**
     *
     * @return
     */
    private List<CorreoDTO> findAllCorreos() {
        log.info("INICIO - findAllCorreos");
        List<CorreoDTO> listaCorreos = new LinkedList<>();
        log.debug("QUERY: " + SQL_QUERY_CORREO);
        List<Object[]> lista = this.SQLDGLRepository.executeNativeSelectQuery(SQL_QUERY_CORREO);
        log.debug("findAllCorreos : " + SQL_QUERY_CORREO);
        for (Object[] item : lista) {
            Timestamp dateValue = (Timestamp) item[0];
            CorreoDTO correoDTO =
                new CorreoDTO(dateValue.toLocalDateTime(), Long.parseLong(item[1].toString()), item[2].toString(), item[3].toString(), item[4].toString(), item[5].toString(),
                    item[6].toString());
            listaCorreos.add(correoDTO);
        }
        log.info("FIN - findAllCorreos");
        return  listaCorreos;
    }

    /**
     * Delete the tablaServicioMail by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(TablaServicioMailPK id) {
        log.info("Request to delete TablaServicioMail : {}", id);
        tablaServicioMailRepository.deleteById(id);
    }
}
