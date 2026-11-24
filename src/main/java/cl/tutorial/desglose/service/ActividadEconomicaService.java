package cl.tutorial.desglose.service;

import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.ActividadEconomicaDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing ActividadEconomica.
 */
@Service
@Transactional
public class ActividadEconomicaService {

    private final Logger log = LogManager.getLogger(ActividadEconomicaService.class);

    private static final String SQL_GET_ALL_ECONOMIC_ACTIVITIES = "SELECT DISTINCT NVL(A.COD_GENERICO,'-1') COD_SECTOR, NVL(B.COD_GENERICO,'-1') COD_SUBSECTOR, NVL(C"
        + ".COD_GENERICO,'-1') COD_ACTIVIDAD, NVL(C.GLS_GENERICALARGA,' ') GLS_ACTIVIDAD\n" + "FROM\n" + "    DESCOD A, DESCOD B, DESCOD C, RELTAB AB, RELTAB BC\n" + "WHERE\n"
        + "    C.COD_TABLA = 'CLAECONO' AND C.COD_REG_STATUS = 0 AND B.COD_TABLA (+) = 'SUBSECON'\n"
        + "    AND B.COD_REG_STATUS (+) = 0 AND A.COD_TABLA (+) = 'SECECONO' AND A.COD_REG_STATUS (+) = 0\n"
        + "    AND BC.COD_TABLA1 (+) = 'SUBSECON' AND BC.COD_TABLA2 (+) = 'CLAECONO' AND BC.COD_REG_STATUS (+) = 0\n"
        + "    AND BC.COD_GENERICO2 (+) = C.COD_GENERICO AND AB.COD_TABLA1 (+) = 'SECECONO' AND AB.COD_TABLA2 (+) = 'SUBSECON'\n"
        + "    AND AB.COD_REG_STATUS (+) = 0 AND AB.COD_GENERICO2 (+) = BC.COD_GENERICO1 AND B.COD_GENERICO (+) = BC.COD_GENERICO1\n"
        + "    AND A.COD_GENERICO (+) = AB.COD_GENERICO1\n" + "ORDER BY\n" + "    GLS_ACTIVIDAD ASC";

    private static final String SQL_GET_ECONOMIC_ACTIVITIES_BY_SECTOR = "SELECT DISTINCT NVL(A.COD_GENERICO,'-1') COD_SECTOR, NVL(B.COD_GENERICO,'-1') COD_SUBSECTOR, NVL(C"
        + ".COD_GENERICO,'-1') COD_ACTIVIDAD, NVL(C.GLS_GENERICALARGA,' ') GLS_ACTIVIDAD\n" + "FROM\n" + "    DESCOD A, DESCOD B, DESCOD C, RELTAB AB, RELTAB BC\n" + "WHERE\n"
        + "    C.COD_TABLA = 'CLAECONO' AND C.COD_REG_STATUS = 0 AND B.COD_TABLA (+) = 'SUBSECON'\n"
        + "    AND B.COD_REG_STATUS (+) = 0 AND A.COD_TABLA (+) = 'SECECONO' AND A.COD_REG_STATUS (+) = 0\n"
        + "    AND BC.COD_TABLA1 (+) = 'SUBSECON' AND BC.COD_TABLA2 (+) = 'CLAECONO' AND BC.COD_REG_STATUS (+) = 0\n"
        + "    AND BC.COD_GENERICO2 (+) = C.COD_GENERICO AND AB.COD_TABLA1 (+) = 'SECECONO' AND AB.COD_TABLA2 (+) = 'SUBSECON'\n"
        + "    AND AB.COD_REG_STATUS (+) = 0 AND AB.COD_GENERICO2 (+) = BC.COD_GENERICO1 AND B.COD_GENERICO (+) = BC.COD_GENERICO1\n"
        + "    AND A.COD_GENERICO (+) = AB.COD_GENERICO1\n"
        + "    AND  A.COD_GENERICO = \n";

    private static final String SQL_ORDERBY_GLS_ACTIVIDAD = " ORDER BY GLS_ACTIVIDAD ASC";

    private final SQLDGLRepository SQLDGLRepository;

    public ActividadEconomicaService(SQLDGLRepository SQLDGLRepository) {
        this.SQLDGLRepository = SQLDGLRepository;
    }

    public List<ActividadEconomicaDTO> executeNativeQueryByName(String queryName) {
        log.info("INICIO -  executeNativeQueryByName");
        log.debug("QUERY :" +queryName);
        log.info("FIN - executeNativeQueryByName");
        return (List<ActividadEconomicaDTO>) this.SQLDGLRepository.executeNativeSelectQueryByName(queryName);
    }

    @Transactional(readOnly = true)
    public List<ActividadEconomicaDTO> getAllEconomicActivities() {
        log.info("INICIO - getAllEconomicActivities()");
        log.debug("QUERY :" + ActividadEconomicaService.SQL_GET_ALL_ECONOMIC_ACTIVITIES);
        final List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(ActividadEconomicaService.SQL_GET_ALL_ECONOMIC_ACTIVITIES);
        List<ActividadEconomicaDTO> list = new ArrayList<>();
        for (Object[] elem : objects) {
            list.add(new ActividadEconomicaDTO(elem[0].toString(), elem[1].toString(), elem[2].toString(), elem[3].toString()));
        }
        log.info("FIN - getAllEconomicActivities()");
        return list;
    }

    @Transactional(readOnly = true)
    public List<ActividadEconomicaDTO> getEconomicActivitiesBySector(String codSector) {
        log.info("INICIO - getEconomicActivitiesBySector():{}",codSector);
        log.debug("QUERY: " + ActividadEconomicaService.SQL_GET_ECONOMIC_ACTIVITIES_BY_SECTOR + codSector + ActividadEconomicaService.SQL_ORDERBY_GLS_ACTIVIDAD);
        final List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(ActividadEconomicaService.SQL_GET_ECONOMIC_ACTIVITIES_BY_SECTOR + codSector + ActividadEconomicaService.SQL_ORDERBY_GLS_ACTIVIDAD);
        List<ActividadEconomicaDTO> list = new ArrayList<>();
        for (Object[] elem : objects) {
            list.add(new ActividadEconomicaDTO(elem[0].toString(), elem[1].toString(), elem[2].toString(), elem[3].toString()));
        }
        log.info("FIN - getEconomicActivitiesBySector()");
        return list;
    }
}
