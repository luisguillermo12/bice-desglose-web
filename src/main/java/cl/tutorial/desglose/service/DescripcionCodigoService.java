package cl.tutorial.desglose.service;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.DescripcionCodigo;
import cl.tutorial.desglose.domain.pk.DescripcionCodigoPK;
import cl.tutorial.desglose.repository.DescripcionCodigoRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.DescripcionCodigoDTO;
import cl.tutorial.desglose.service.mapper.DescripcionCodigoMapper;
import org.jetbrains.annotations.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing DescripcionCodigo.
 */
@Service
@Transactional
public class DescripcionCodigoService {

    private final Logger log = LogManager.getLogger(DescripcionCodigoService.class);

    private final DescripcionCodigoRepository descripcionCodigoRepository;
    private final DescripcionCodigoMapper     descripcionCodigoMapper;
    private final SQLDGLRepository            SQLDGLRepository;

    /**
     * Buscar el valor de configuración CANT_REG_CONS_AGENDA
     */
    private static final String SQL_SEARCH_BREAKDOWN_CANT_REG_CONS_AGENDA =
        "SELECT GLS_GENERICACORTA, GLS_GENERICALARGA\n" + "FROM DESCOD\n" + "WHERE COD_TABLA = 'DGLCONFG'\n" + "    AND COD_GENERICO = 'CANT_REG_CONS_AGENDA'\n"
            + "    AND COD_REG_STATUS = 0";

    /**
     * Buscar el valor de configuración TOLERANCIA_CUADRATURA
     */
    private static final String SQL_SEARCH_QUADRATURE_TOLERANCE =
        "SELECT GLS_GENERICACORTA TOLERANCE, GLS_GENERICALARGA\n" + "FROM DESCOD\n" + "WHERE COD_TABLA = 'DESGLOCF'\n" + "    AND COD_REG_STATUS = 0\n"
            + "    AND COD_GENERICO = '002'";

    /**
     * Busca estados de los desgloses y los devuelve en el orden deseado por el cliente.
     */
    private static final String SQL_SEARCH_BREAKDOWN_STATUS =
        " SELECT DISTINCT\n" + "      D.GLS_GENERICACORTA,\n" + "      D.GLS_GENERICALARGA,\n" + "      O.GLS_GENERICALARGA   ORDEN\n" + "  FROM\n" + "      DESCOD D\n"
            + "      INNER JOIN DESCOD O ON ( O.COD_REG_STATUS = 0\n" + "                               AND O.COD_TABLA = D.COD_TABLA\n"
            + "                               AND TRIM(O.GLS_GENERICACORTA) = TRIM(D.GLS_GENERICACORTA) )\n" + "  WHERE\n" + "      D.COD_REG_STATUS = 0\n"
            + "      AND D.COD_TABLA = '" + Constants.DGLCONFG + "'      AND D.COD_GENERICO LIKE '" + Constants.COD_GENERICO_DESGLOSE + "%' AND O.COD_GENERICO LIKE " + "'"
            + Constants.COD_GENERICO_ORDEN_ESTADO_DESGLOSE + "%' AND D.GLS_GENERICACORTA <> '" + Constants.ESTADO_EN_REVISION + "'  ORDER BY\n" + "      ORDEN ASC ";

    public DescripcionCodigoService(DescripcionCodigoRepository descripcionCodigoRepository, DescripcionCodigoMapper descripcionCodigoMapper, SQLDGLRepository SQLDGLRepository) {
        this.descripcionCodigoRepository = descripcionCodigoRepository;
        this.descripcionCodigoMapper = descripcionCodigoMapper;
        this.SQLDGLRepository = SQLDGLRepository;
    }

    /**
     * Save a descripcionCodigo.
     *
     * @param descripcionCodigoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DescripcionCodigoDTO save(DescripcionCodigoDTO descripcionCodigoDTO) {
        log.info("INICIO - Request to save DescripcionCodigo : {}", descripcionCodigoDTO);
        DescripcionCodigo descripcionCodigo = descripcionCodigoMapper.toEntity(descripcionCodigoDTO);
        descripcionCodigo = descripcionCodigoRepository.save(descripcionCodigo);
        log.info("FIN - Request to save DescripcionCodigo : {}", descripcionCodigoDTO);
        return descripcionCodigoMapper.toDto(descripcionCodigo);
    }

    /**
     * Get all the descripcionCodigos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DescripcionCodigoDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DescripcionCodigos");
        List<String> codTablas = new LinkedList<>();
        codTablas.add(CODTABLA_PLANTILLAS_DESGLOSE);
        codTablas.add(CODTABLA_PROYECIONES_DESGLOSE);
        log.info("FIN - Request to get all DescripcionCodigos");
        return descripcionCodigoRepository.findAllByCodTablaInOrderByCodTablaAscCodGenericoAsc(codTablas,pageable).map(descripcionCodigoMapper::toDto);
    }

    /**
     * Get one descripcionCodigo by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DescripcionCodigoDTO> findOne(DescripcionCodigoPK id) {
        log.info("INICIO - Request to get DescripcionCodigo : {}", id);
        log.info("FIN - Request to get DescripcionCodigo ");
        return descripcionCodigoRepository.findById(id).map(descripcionCodigoMapper::toDto);
    }

    /**
     * Delete the descripcionCodigo by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DescripcionCodigoPK id) {
        log.info("INICIO - Request to delete DescripcionCodigo : {}", id);
        log.info("FIN - Request to delete DescripcionCodigo");
        descripcionCodigoRepository.deleteById(id);
    }

    public List<DescripcionCodigoDTO> findAllByCodTabla(String codTabla, Integer codRegStatus, String sort) {
        log.info("INICIO - Request to findAllByCodTabla DescripcionCodigo with codTabla : {} and sorted by {}", codTabla, sort);
        Sort orderBy = initOrderByObject(sort, ",");
        final List<DescripcionCodigo> allByCodTabla = descripcionCodigoRepository.findDistinctByCodTablaAndCodRegStatus(codTabla, codRegStatus, orderBy);
        log.info("FIN - Request to findAllByCodTabla DescripcionCodigo with codTabla : {} and sorted by {}", codTabla, sort);
        return descripcionCodigoMapper.toDto(allByCodTabla);
    }

    public List<DescripcionCodigoDTO> findAllByCodTablaAndCodGenericoLikeAndCodGenericoNot(String codTabla, String codGenerico, String codGenerico1, Integer codRegStatus) {
        log.info("INICIO - Request to findAllByCodTabla DescripcionCodigo with codTabla : {} {}{}", codTabla, codGenerico, codGenerico1);
        Sort.Direction direction = Sort.Direction.ASC;
        final List<DescripcionCodigo> allByCodTabla =
            descripcionCodigoRepository.findDistinctByCodTablaAndCodGenericoStartingWithAndCodGenericoNotInAndCodRegStatus(codTabla, codGenerico, codGenerico1, codRegStatus);
        log.info("FIN - Request to findAllByCodTabla DescripcionCodigo with codTabla : {} {}{}", codTabla, codGenerico, codGenerico1);
        return descripcionCodigoMapper.toDto(allByCodTabla);
    }

    public List<DescripcionCodigoDTO> findAllByCodTablasIn(String codTablas, String sort) {
        log.info("INICIO - Request to findAllByCodTabla DescripcionCodigo with codTablas : {} and sorted by {}", codTablas, sort);
        Sort orderBy = initOrderByObject(sort, "\\s*,\\s*");
        List<String> codtablaList = splitTableCodes(codTablas);
        final List<DescripcionCodigo> allByCodTabla = descripcionCodigoRepository.findDistinctByCodTablaInAndCodRegStatus(codtablaList, COD_REG_STATUS_ZERO, orderBy);
        log.info("FIN - Request to findAllByCodTabla DescripcionCodigo with codTablas : {} and sorted by {}", codTablas, sort);
        return descripcionCodigoMapper.toDto(allByCodTabla);
    }

    @NotNull
    /**
     * Separa los códigos de tabla en una lista de String.
     */ private List<String> splitTableCodes(String codTablas) {
        // Separar los códigos de tabla
        List<String> codtablaList = new ArrayList<>();
        if (codTablas != null && !codTablas.isEmpty()) {
            codtablaList = Arrays.asList(codTablas.split("\\s*,\\s*"));
        }
        return codtablaList;
    }

    @NotNull
    private Sort initOrderByObject(String sort, String splitPattern) {
        String values[] = sort.split(splitPattern);
        Sort.Direction direction = Sort.Direction.ASC;
        if (values.length > 1) {
            direction = values[1].equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }
        return Sort.by(direction, values[0]);
    }

    public List<DescripcionCodigoDTO> getAllDesCodByCodTablasInAndCodGenericoLike(String codTablas, String codGenerico, Integer codRegStatus, String sort) {
        log.info("INICIO - Request to findAllByCodTabla DescripcionCodigo with codTablas : {}, codGenerico : {} and sorted by {}", codTablas, codGenerico, sort);
        Sort orderBy = initOrderByObject(sort, "\\s*,\\s*");
        List<String> codtablaList = splitTableCodes(codTablas);
        final List<DescripcionCodigo> allByCodTabla =
            descripcionCodigoRepository.findDistinctByCodTablaInAndCodGenericoStartsWithIgnoreCaseAndCodRegStatus(codtablaList, codGenerico, codRegStatus, orderBy);
        log.info("FIN - Request to findAllByCodTabla DescripcionCodigo with codTablas : {}, codGenerico : {} and sorted by {}", codTablas, codGenerico, sort);
        return descripcionCodigoMapper.toDto(allByCodTabla);
    }

    public DescripcionCodigoDTO findFirstByCodTablaAndCodGenericoStartingWithAndGlsGenericacortaAndCodRegStatus(String codTabla, String codGenerico, String glsGenericacorta,
        Integer codRegStatus) {

        log.info("INICIO - Request to findFirstByCodTablaAndCodGenericoStartingWithAndGlsGenericacortaAndCodRegStatus codTabla : {}, codGenerico : {}, glsGenericacorta: {}, codRegStatus"
            + " : {}", codTabla, codGenerico, glsGenericacorta, codRegStatus);
        log.info("FIN - Request to findFirstByCodTablaAndCodGenericoStartingWithAndGlsGenericacortaAndCodRegStatus codTabla : {}, codGenerico : {}, glsGenericacorta: {}, codRegStatus"
            + " : {}", codTabla, codGenerico, glsGenericacorta, codRegStatus);
        return descripcionCodigoMapper.toDto(
            descripcionCodigoRepository.findFirstByCodTablaAndCodGenericoStartingWithAndGlsGenericacortaAndCodRegStatus(codTabla, codGenerico, glsGenericacorta, codRegStatus));
    }

    public DescripcionCodigoDTO findFirstByCodTablaAndCodGenericoAndCodRegStatus(String codTabla, String codGenerico, Integer codRegStatus) {
        log.info("INICIO - Request to findFirstByCodTablaAndCodGenericoAndCodRegStatus codTabla : {}, codGenerico : {}, codRegStatus : {}", codTabla, codGenerico, codRegStatus);
        log.info("FIN - Request to findFirstByCodTablaAndCodGenericoAndCodRegStatus codTabla : {}, codGenerico : {}, codRegStatus : {}", codTabla, codGenerico, codRegStatus);
        return descripcionCodigoMapper.toDto(descripcionCodigoRepository.findFirstByCodTablaAndCodGenericoAndCodRegStatus(codTabla, codGenerico, codRegStatus));
    }

    public DescripcionCodigoDTO findCantRegConsAgenda() {
        log.info("INICIO - Request to findCantRegConsAgenda");
        List<Object> params = new ArrayList<>();
        log.debug("QUERY : " + DescripcionCodigoService.SQL_SEARCH_BREAKDOWN_CANT_REG_CONS_AGENDA);
        log.debug("PARAMETROS : "+ params.toString());
        List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(DescripcionCodigoService.SQL_SEARCH_BREAKDOWN_CANT_REG_CONS_AGENDA, params);
        DescripcionCodigoDTO descripcionCodigoDTO = new DescripcionCodigoDTO();
        for (Object[] elem : objects) {
            descripcionCodigoDTO.setGlsGenericacorta(elem[0] != null ? elem[0].toString() : null);
            descripcionCodigoDTO.setGlsGenericalarga(elem[1] != null ? elem[1].toString() : null);
            descripcionCodigoDTO.setCodRegStatus(0);
            descripcionCodigoDTO.setCodTabla(Constants.DGLCONFG);
            descripcionCodigoDTO.setCodGenerico(Constants.CANT_REG_CONS_AGENDA);
        }
        log.info("FIN - Request to findCantRegConsAgenda");
        return descripcionCodigoDTO;
    }

    public List<DescripcionCodigoDTO> findBreakdownStatuses() {
        log.info("INICIO  - List<DescripcionCodigoDTO> findBreakdownStatuses");
        List<DescripcionCodigoDTO> list = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        log.debug("QUERY: " + DescripcionCodigoService.SQL_SEARCH_BREAKDOWN_STATUS);
        log.debug("PARAMETROS: " + params.toString());
        List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(DescripcionCodigoService.SQL_SEARCH_BREAKDOWN_STATUS, params);
        for (Object[] elem : objects) {
            DescripcionCodigoDTO descripcionCodigoDTO = new DescripcionCodigoDTO();
            descripcionCodigoDTO.setGlsGenericacorta(elem[0] != null ? elem[0].toString() : null);
            descripcionCodigoDTO.setGlsGenericalarga(elem[1] != null ? elem[1].toString() : null);
            descripcionCodigoDTO.setCodRegStatus(0);
            descripcionCodigoDTO.setCodTabla(Constants.DGLCONFG);
            list.add(descripcionCodigoDTO);
        }
        log.info("FIN - List<DescripcionCodigoDTO> findBreakdownStatuses");
        return list;
    }

    public DescripcionCodigoDTO findQuadratureTolerance() {
        log.info("INICIO - Request to findQuadratureTolerance");
        List<Object> params = new ArrayList<>();
        log.debug("QUERY: " + DescripcionCodigoService.SQL_SEARCH_QUADRATURE_TOLERANCE);
        log.debug("PARAMETROS:  " + params.toString());
        List<Object[]> objects = this.SQLDGLRepository.executeNativeSelectQuery(DescripcionCodigoService.SQL_SEARCH_QUADRATURE_TOLERANCE, params);
        DescripcionCodigoDTO descripcionCodigoDTO = new DescripcionCodigoDTO();
        for (Object[] elem : objects) {
            descripcionCodigoDTO.setGlsGenericacorta(elem[0] != null ? elem[0].toString() : null);
            descripcionCodigoDTO.setGlsGenericalarga(elem[1] != null ? elem[1].toString() : null);
            descripcionCodigoDTO.setCodRegStatus(0);
            descripcionCodigoDTO.setCodTabla(Constants.DESGLOCF);
            descripcionCodigoDTO.setCodGenerico(Constants.GENERIC_CODE_TOLERANCE);
        }
        log.info("FIN - Request to findQuadratureTolerance");
        return descripcionCodigoDTO;
    }
}
