package cl.tutorial.desglose.service;

import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.SectorDTO;
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
public class SectorService {

    private final Logger log = LogManager.getLogger(SectorService.class);

    private static final String SQL_GET_ALL_SECTORS =
        "SELECT DISTINCT NVL(A.COD_GENERICO,'-1') COD_SECTOR, NVL(A.GLS_GENERICALARGA,' ') GLS_SECTOR\n" + "FROM \n" + "    DESCOD A, DESCOD B, DESCOD C, RELTAB AB, RELTAB BC\n"
            + "WHERE\n" + "    C.COD_TABLA = 'CLAECONO' AND C.COD_REG_STATUS = 0 AND B.COD_TABLA (+) = 'SUBSECON'\n"
            + "    AND B.COD_REG_STATUS (+) = 0 AND A.COD_TABLA (+) = 'SECECONO' AND A.COD_REG_STATUS (+) = 0\n"
            + "    AND BC.COD_TABLA1 (+) = 'SUBSECON' AND BC.COD_TABLA2 (+) = 'CLAECONO' AND BC.COD_REG_STATUS (+) = 0\n"
            + "    AND BC.COD_GENERICO2 (+) = C.COD_GENERICO AND AB.COD_TABLA1 (+) = 'SECECONO'\n" + "    AND AB.COD_TABLA2 (+) = 'SUBSECON' AND AB.COD_REG_STATUS (+) = 0\n"
            + "    AND AB.COD_GENERICO2 (+) = BC.COD_GENERICO1 AND B.COD_GENERICO (+) = BC.COD_GENERICO1\n" + "    AND A.COD_GENERICO (+) = AB.COD_GENERICO1\n" + "ORDER BY\n"
            + "    GLS_SECTOR ASC";

    private final SQLDGLRepository SQLDGLRepository;

    public SectorService(SQLDGLRepository SQLDGLRepository) {
        this.SQLDGLRepository = SQLDGLRepository;
    }

    @Transactional(readOnly = true)
    public List<SectorDTO> getAllSectors() {
        log.info("INICIO - getAllSectors()");
        log.debug("QUERY :" + SectorService.SQL_GET_ALL_SECTORS);
        final List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(SectorService.SQL_GET_ALL_SECTORS);
        List<SectorDTO> list = new ArrayList<>();
        for (Object[] elem : objects) {
            list.add(new SectorDTO(elem[0].toString(), elem[1].toString()));
        }
        log.info("FIN - getAllSectors()");
        return list;
    }
}
