package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.repository.SQLCIFRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.EmpresaConsolidada;
import cl.tutorial.desglose.domain.pk.EmpresaConsolidadaPK;
import cl.tutorial.desglose.repository.EmpresaConsolidadaRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.service.dto.EmpConsolPeriodoDesgConsolDTO;
import cl.tutorial.desglose.service.dto.EmpresaConsolidadaDTO;
import cl.tutorial.desglose.service.mapper.EmpresaConsolidadaMapper;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureBusinessError;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureExecutionException;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing EmpresaConsolidada.
 */
@Service
@Transactional
public class EmpresaConsolidadaService {

    private final Logger log = LogManager.getLogger(EmpresaConsolidadaService.class);

    private final EmpresaConsolidadaRepository empresaConsolidadaRepository;

    private final SQLDGLRepository SQLDGLRepository;

    private final SQLCIFRepository sqlcifRepository;

    private final EmpresaConsolidadaMapper empresaConsolidadaMapper;

    private static final String SP_CIF_SP_DEL_CONSOL                      = "DGL_SP_DEL_CONSOL_WK";
    private static final String SP_CIF_SP_DEL_CONSOL_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_DEL_CONSOL_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_DEL_CONSOL_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_DEL_CONSOL_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_DEL_CONSOL_05_P_COR_EMPRESA     = "P_COR_EMPRESA";
    private static final String SP_CIF_SP_DEL_CONSOL_06_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_DEL_CONSOL_07_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_DEL_CONSOL_08_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    private static final String SP_CIF_SP_INS_CONSOL                      = "DGL_SP_INS_CONSOL_WK";
    private static final String SP_CIF_SP_INS_CONSOL_01_P_COD_CLIENTE     = "P_COD_CLIENTE";
    private static final String SP_CIF_SP_INS_CONSOL_02_P_COD_TIPO_DES    = "P_COD_TIPO_DES";
    private static final String SP_CIF_SP_INS_CONSOL_03_P_COD_SUBTIPO_DES = "P_COD_SUBTIPO_DES";
    private static final String SP_CIF_SP_INS_CONSOL_04_P_COD_MONEDA      = "P_COD_MONEDA";
    private static final String SP_CIF_SP_INS_CONSOL_05_P_COR_EMPRESA     = "P_COR_EMPRESA";
    private static final String SP_CIF_SP_INS_CONSOL_06_P_NOM_EMPRESA     = "P_NOM_EMPRESA";
    private static final String SP_CIF_SP_INS_CONSOL_07_P_RETORNO         = "P_RETORNO";
    private static final String SP_CIF_SP_INS_CONSOL_08_P_GLOSA           = "P_GLOSA";
    private static final String SP_CIF_SP_INS_CONSOL_09_P_COD_PLANTILLA   = "P_COD_PLANTILLA";

    private static final String SQL_GET_CONSOL_Y_PRCCNS =
        "SELECT A.NOM_EMPRESA, TO_CHAR(B.FEC_PERIODO,'DD-MM-YYYY') FECPRD, B.VAL_PRC_CONSOL PRC_CNS,A.COR_EMPRESA\n" + "FROM CONSOL_WK A, PRCCNS_WK B\n"
            + "WHERE A.COD_CLIENTE = ?1 AND A.COD_TIPO_DES = ?2 AND A.COD_SUBTIPO_DES = ?3 AND A.COD_MONEDA = ?4 AND A.COD_PLANTILLA = ?5 \n"
            + "      AND B.COD_CLIENTE (+)= A.COD_CLIENTE AND B.COD_TIPO_DES (+)= A.COD_TIPO_DES\n"
            + "      AND B.COD_SUBTIPO_DES (+)= A.COD_SUBTIPO_DES AND B.COD_MONEDA (+)= A.COD_MONEDA AND B.COD_PLANTILLA (+)= A.COD_PLANTILLA \n"
            + "      AND B.COR_EMPRESA (+)= A.COR_EMPRESA\n" + "ORDER BY A.NOM_EMPRESA, TO_CHAR(B.FEC_PERIODO,'DD-MM-YYYY')";

    private static final String SQL_GETCIF_CONSOL_Y_PRCCNS =
        "SELECT A.NOM_EMPRESA, TO_CHAR(B.FEC_PERIODO,'DD-MM-YYYY') FECPRD, B.VAL_PRC_CONSOL PRC_CNS,A.COR_EMPRESA\n" + "FROM CONSOL A, PRCCNS B\n"
            + "WHERE A.COD_CLIENTE = ?1 AND A.COD_TIPO_DES = ?2 AND A.COD_SUBTIPO_DES = ?3 AND A.COD_MONEDA = ?4 AND A.COD_PLANTILLA = ?5 \n"
            + "      AND B.COD_CLIENTE (+)= A.COD_CLIENTE AND B.COD_TIPO_DES (+)= A.COD_TIPO_DES\n"
            + "      AND B.COD_SUBTIPO_DES (+)= A.COD_SUBTIPO_DES AND B.COD_MONEDA (+)= A.COD_MONEDA AND B.COD_PLANTILLA (+)= A.COD_PLANTILLA \n"
            + "      AND B.COR_EMPRESA (+)= A.COR_EMPRESA\n" + "ORDER BY A.NOM_EMPRESA, TO_CHAR(B.FEC_PERIODO,'DD-MM-YYYY')";

    public EmpresaConsolidadaService(EmpresaConsolidadaRepository empresaConsolidadaRepository, SQLCIFRepository sqlcifRepository ,SQLDGLRepository SQLDGLRepository, EmpresaConsolidadaMapper empresaConsolidadaMapper) {
        this.empresaConsolidadaRepository = empresaConsolidadaRepository;
        this.SQLDGLRepository = SQLDGLRepository;
        this.empresaConsolidadaMapper = empresaConsolidadaMapper;
        this.sqlcifRepository = sqlcifRepository;
    }

    /**
     * Save a empresaConsolidada.
     *
     * @param empresaConsolidadaDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public EmpresaConsolidadaDTO save(EmpresaConsolidadaDTO empresaConsolidadaDTO) {
        log.info("Request to save EmpresaConsolidada : {}", empresaConsolidadaDTO);

        EmpresaConsolidada empresaConsolidada = empresaConsolidadaMapper.toEntity(empresaConsolidadaDTO);
        empresaConsolidada = empresaConsolidadaRepository.save(empresaConsolidada);
        return empresaConsolidadaMapper.toDto(empresaConsolidada);
    }

    /**
     * Get all the empresaConsolidadas.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmpresaConsolidadaDTO> findAll(Pageable pageable) {
        log.info("Request to get all EmpresaConsolidadas");
        return empresaConsolidadaRepository.findAll(pageable).map(empresaConsolidadaMapper::toDto);
    }

    /**
     * Get one empresaConsolidada by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<EmpresaConsolidadaDTO> findOne(EmpresaConsolidadaPK id) {
        log.debug("Request to get EmpresaConsolidada : {}", id);
        return empresaConsolidadaRepository.findById(id).map(empresaConsolidadaMapper::toDto);
    }

    /**
     * Delete the empresaConsolidada by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(EmpresaConsolidadaPK id) {
        log.info("Request to delete EmpresaConsolidada : {}", id);
        empresaConsolidadaRepository.deleteById(id);
    }

    public void executeCifSpDelConsol(@NotNull String codCliente, @NotNull Integer codPlantilla, @NotNull Integer codTipoDes, @NotNull Integer codSubTipoDes,
        @NotNull String codMoneda, @NotNull Integer corEmpresa) {

        this.log.info("INICIO - executeCifSpDelConsol");
        // Llenar los parámetros
        Param<String> p1 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_03_P_COD_SUBTIPO_DES, Integer.class, codSubTipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<Integer> p5 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_05_P_COR_EMPRESA, Integer.class, corEmpresa, ParameterMode.IN);
        Param<Integer> p6 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_06_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p7 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_07_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p8 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_08_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8);

        // Interpretar los resultados
        log.debug("QUERY : " + EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL );
        log.debug("PARAMETROS : " +  parameters );
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL, parameters);
        log.info(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_06_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL);
            throw new StorageProcedureExecutionException();
        }
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            String returnMessage = (String) objects.get(EmpresaConsolidadaService.SP_CIF_SP_DEL_CONSOL_07_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpDelConsol");
    }

    /**
     * Ejecuta el SP "DGL_SP_INS_CONSOL_WK"
     *
     * @param codCliente
     * @param codTipoDes
     * @param codSubTipoDes
     * @param codMoneda
     * @param corEmpresa
     * @param nomEmpresa
     *
     * @return El correlativo de la empresa creada o actualizada.
     */
    public Integer executeCifSpInsConsol(@NotNull String codCliente, @NotNull Integer codPlantilla, @NotNull Integer codTipoDes, @NotNull Integer codSubTipoDes,
        @NotNull String codMoneda, @NotNull Integer corEmpresa, @NotNull String nomEmpresa) {

        this.log.info("INICIO - executeCifSpInsConsol");
        // Llenar los parámetros
        Param<String> p1 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_01_P_COD_CLIENTE, String.class, codCliente, ParameterMode.IN);
        Param<Integer> p2 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_02_P_COD_TIPO_DES, Integer.class, codTipoDes, ParameterMode.IN);
        Param<Integer> p3 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_03_P_COD_SUBTIPO_DES, Integer.class, codSubTipoDes, ParameterMode.IN);
        Param<String> p4 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_04_P_COD_MONEDA, String.class, codMoneda, ParameterMode.IN);
        Param<Integer> p5 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_05_P_COR_EMPRESA, Integer.class, corEmpresa, ParameterMode.IN);
        Param<String> p6 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_06_P_NOM_EMPRESA, String.class, nomEmpresa, ParameterMode.IN);
        Param<Integer> p7 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_07_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p8 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_08_P_GLOSA, String.class, null, ParameterMode.OUT);
        Param<Integer> p9 = new Param<>(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_09_P_COD_PLANTILLA, Integer.class, codPlantilla, ParameterMode.IN);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);

        // Interpretar los resultados
        log.debug("QUERY : " +EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL );
        log.debug("PARAMETROS : " + parameters.toString() );
        Map<String, Object> objects = this.SQLDGLRepository.executeNativeStoredProcedureOutputNoSelect(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL, parameters);
        log.info(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_07_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL);
            throw new StorageProcedureExecutionException();
        }
        String returnMessage = (String) objects.get(EmpresaConsolidadaService.SP_CIF_SP_INS_CONSOL_08_P_GLOSA);
        if (!returnParam.equals(0)) { // El SP devolvió un error.
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN - executeCifSpInsConsol");
        return NumberUtils.createInteger(returnMessage); // Si no hay excepciones en este valor que retorna el SP viene el correlativo de la empresa creada.
    }

    @Transactional(readOnly = true)
    public List<EmpConsolPeriodoDesgConsolDTO> findEmpresaConsolidadas(final String codCliente, final Integer codTipoDes, final Integer codSubtipoDes, final String codMoneda,
        final Integer codPlantilla,String esquema) {
        log.info("INICIO - Request to findEmpresaConsolidadas codCliente: {}, codCliente: {}, codTipoDes: {}, codSubtipoDes: {}, codMoneda: {}, codPlantilla: {}", codCliente,
            codTipoDes, codSubtipoDes, codMoneda, codPlantilla);

        List<EmpConsolPeriodoDesgConsolDTO> list = new ArrayList<>();
        // COD_CLIENTE, COD_TIPO_DES, COD_SUBTIPO_DES, COD_MONEDA, COD_PLANTILLA
        List<Object> params = Arrays.asList(codCliente.toUpperCase(), codTipoDes, codSubtipoDes, codMoneda, codPlantilla);
        List<Object[]> result = null;
        if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
            log.debug("QUERY : " +this.SQL_GET_CONSOL_Y_PRCCNS );
            log.debug("PARAMETROS: " + params.toString());
            result = this.SQLDGLRepository.executeNativeSelectQuery(this.SQL_GET_CONSOL_Y_PRCCNS, params);
        }
        if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
            log.debug("QUERY : " +this.SQL_GET_CONSOL_Y_PRCCNS );
            log.debug("PARAMETROS: " + params.toString());
             result = this.sqlcifRepository.executeNativeSelectQuery(this.SQL_GETCIF_CONSOL_Y_PRCCNS, params);
        }
        if (result != null && result.size() > 0) {
            for (Object[] elem : result) {
                EmpConsolPeriodoDesgConsolDTO dto = new EmpConsolPeriodoDesgConsolDTO();
                // Valores fijos.
                dto.codCliente = codCliente;
                dto.codTipoDes = codTipoDes;
                dto.codSubtipoDes = codSubtipoDes;
                dto.codMoneda = codMoneda;
                dto.codPlantilla = codPlantilla;

                dto.nomEmpresa = elem[0] != null ? elem[0].toString() : null;
                dto.fecPeriodo = elem[1] != null ? LocalDate.parse((String) elem[1], FORMATTER_DATE_SHORT) : null;
                if (dto.fecPeriodo != null) {
                    dto.valPrcConsol = elem[2] != null ? (BigDecimal) elem[2] : null;
                    if(dto.valPrcConsol!= null) {
                        dto.valPrcConsol = dto.valPrcConsol.multiply(new BigDecimal("100"));
                    }
                    dto.valPrcConsolInt = dto.valPrcConsol != null ? dto.valPrcConsol.intValue() * 100 : null;
                    dto.corEmpresa = elem[3] != null ? Integer.parseInt(elem[3].toString()) : null;
                }
                list.add(dto);
            }
        }

        log.info("FIN - Request to findEmpresaConsolidadas");
        return list;
    }
}
