package cl.tutorial.desglose.service;

import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.SubSectorDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing SubSector Económico.
 */
@Service
@Transactional
public class SubSectorService {

    private final Logger log = LogManager.getLogger(SubSectorService.class);

    private static final String SQL_GET_ALL_SUB_SECTORS =
        "SELECT DISTINCT NVL(A.COD_GENERICO,'-1') COD_SECTOR, NVL(B.COD_GENERICO,'-1') COD_SUBSECTOR, NVL(B.GLS_GENERICALARGA,' ') GLS_SUBSECTOR\n"
            + "FROM DESCOD A, DESCOD B, DESCOD C, RELTAB AB, RELTAB BC\n"
            + "WHERE C.COD_TABLA = 'CLAECONO' AND C.COD_REG_STATUS = 0 AND B.COD_TABLA (+) = 'SUBSECON' AND B.COD_REG_STATUS (+) = 0\n"
            + "    AND A.COD_TABLA (+) = 'SECECONO' AND A.COD_REG_STATUS (+) = 0 AND BC.COD_TABLA1 (+) = 'SUBSECON' AND BC.COD_TABLA2 (+) = 'CLAECONO'\n"
            + "    AND BC.COD_REG_STATUS (+) = 0 AND BC.COD_GENERICO2 (+) = C.COD_GENERICO AND AB.COD_TABLA1 (+) = 'SECECONO' AND AB.COD_TABLA2 (+) = 'SUBSECON'\n"
            + "    AND AB.COD_REG_STATUS (+) = 0 AND AB.COD_GENERICO2 (+) = BC.COD_GENERICO1 AND B.COD_GENERICO (+) = BC.COD_GENERICO1 AND A.COD_GENERICO (+) = AB.COD_GENERICO1\n"
            + "ORDER BY GLS_SUBSECTOR ASC";

    private static final String SQL_GET_SUB_SECTORS =
        "SELECT DISTINCT NVL(A.COD_GENERICO,'-1') COD_SECTOR, NVL(B.COD_GENERICO,'-1') COD_SUBSECTOR, NVL(B.GLS_GENERICALARGA,' ') GLS_SUBSECTOR\n"
            + "FROM DESCOD A, DESCOD B, DESCOD C, RELTAB AB, RELTAB BC\n"
            + "WHERE C.COD_TABLA = 'CLAECONO' AND C.COD_REG_STATUS = 0 AND B.COD_TABLA (+) = 'SUBSECON' AND B.COD_REG_STATUS (+) = 0\n"
            + "    AND A.COD_TABLA (+) = 'SECECONO' AND A.COD_REG_STATUS (+) = 0 AND BC.COD_TABLA1 (+) = 'SUBSECON' AND BC.COD_TABLA2 (+) = 'CLAECONO'\n"
            + "    AND BC.COD_REG_STATUS (+) = 0 AND BC.COD_GENERICO2 (+) = C.COD_GENERICO AND AB.COD_TABLA1 (+) = 'SECECONO' AND AB.COD_TABLA2 (+) = 'SUBSECON'\n"
            + "    AND AB.COD_REG_STATUS (+) = 0 AND AB.COD_GENERICO2 (+) = BC.COD_GENERICO1 AND B.COD_GENERICO (+) = BC.COD_GENERICO1 AND A.COD_GENERICO (+) = AB.COD_GENERICO1\n"
            + "AND A.COD_GENERICO = ";

    private static final String SQL_ORDERBY_SUB_SECTORS = " ORDER BY GLS_SUBSECTOR ASC";

    private final SQLDGLRepository SQLDGLRepository;

    public SubSectorService(SQLDGLRepository SQLDGLRepository) {
        this.SQLDGLRepository = SQLDGLRepository;
    }

    @Transactional(readOnly = true)
    public List<SubSectorDTO> getAllSubSectors() {
        log.info("INICIO - getAllSubSectors()");
        log.debug("QUERY: "+ SubSectorService.SQL_GET_ALL_SUB_SECTORS );
        final List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(SubSectorService.SQL_GET_ALL_SUB_SECTORS);
        List<SubSectorDTO> list = new ArrayList<>();
        for (Object[] elem : objects) {
            list.add(new SubSectorDTO(elem[0].toString(), elem[1].toString(), elem[2] != null ? elem[2].toString() : null));
        }
        log.info("FIN - getAllSubSectors()");
        return list;
    }

    // GET SUBSECTOR BY SECTOR
    @Transactional(readOnly = true)
    public List<SubSectorDTO> getSubSectorsBySector(String codSector) {
        log.info("INICIO - getSubSectorsBySector()");
        log.debug("QUERY : " + SubSectorService.SQL_GET_SUB_SECTORS + codSector +  SubSectorService.SQL_ORDERBY_SUB_SECTORS);
        final List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(SubSectorService.SQL_GET_SUB_SECTORS + codSector +  SubSectorService.SQL_ORDERBY_SUB_SECTORS);
        List<SubSectorDTO> list = new ArrayList<>();
        for (Object[] elem : objects) {
            list.add(new SubSectorDTO(elem[0].toString(), elem[1].toString(), elem[2] != null ? elem[2].toString() : null));
        }
        log.info("FIN - getSubSectorsBySector()");
        return list;
    }
}
