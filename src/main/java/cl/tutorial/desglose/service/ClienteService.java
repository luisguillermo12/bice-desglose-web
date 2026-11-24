package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.repository.SQLCIFRepository;
import cl.tutorial.desglose.config.ApplicationProperties;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.SfStfOperator;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.ClienteNaturalRepository;
import cl.tutorial.desglose.repository.SQLDGLRepository;
import cl.tutorial.desglose.repository.SfStfOperatorRepository;
import cl.tutorial.desglose.service.dto.BreakdownSearchRequestDTO;
import cl.tutorial.desglose.service.dto.BreakdownSearchResponseDTO;
import cl.tutorial.desglose.service.dto.ClienteDTO;
import cl.tutorial.desglose.service.dto.TurbineUserDTO;
import cl.tutorial.desglose.service.mapper.ClienteNaturalMapper;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Cliente business logic.
 */
@Service
@Transactional
public class ClienteService {

    private final Logger log = LogManager.getLogger(ClienteService.class);

    /**
     * Se deberá concatenar al final del string el valor del rut entre comillas simples.
     */
    private static final String SQL_SEARCH_NATURAL_CLIENT_BY_RUT = "SELECT\n" + "      A.NUM_IDEN RUT,\n"
        + "      TRIM(DECODE(B.NOM_NOMBRES || B.NOM_PATERNO || B.NOM_MATERNO,NULL,'SIN NOMBRE',B.NOM_NOMBRES || ' ' || B.NOM_PATERNO || ' ' || B.NOM_MATERNO) ) NOMBRE\n"
        + "  FROM\n" + "      IDECLI A, CLINAT B\n" + "  WHERE\n" + "      A.NUM_CLIENTE = B.NUM_CLIENTE AND A.COD_REG_STATUS = 0 AND B.COD_REG_STATUS = 0 AND A.NUM_IDEN = ";

    /**
     * Se deberá concatenar al final del string el valor del rut entre comillas simples.
     */
    private static final String SQL_SEARCH_JURIDIC_CLIENT_BY_RUT =
        "SELECT A.NUM_IDEN   RUT, NVL(B.GLS_RAZON_SOCI,'SIN NOMBRE') NOMBRE\n" + " FROM\n" + "     IDECLI A, CLIJUR B\n" + " WHERE\n"
            + "     A.NUM_CLIENTE = B.NUM_CLIENTE AND A.COD_REG_STATUS = 0 AND B.COD_REG_STATUS = 0 AND A.NUM_IDEN = ";

    private static  final String SQL_SEARCH_SIN_RUT_CLIENT = "SELECT b.cod_cliente,b.nom_cliente,b.cod_actividad FROM  SINRUT B\n" + "WHERE   b.cod_cliente = ";

    /**
     * Se deberá concatenar al final del string el valor del rut entre comillas simples.
     */
    private static final String SQL_SEARCH_ECONOMIC_ACTIVITY_JURIDIC_CLIENT_BY_RUT = "SELECT B.COD_SECTOR_ECONOM   ACTIVIDAD\n" + " FROM IDECLI A, BASCLI B\n"
        + " WHERE A.COD_REG_STATUS = 0 AND B.COD_REG_STATUS = 0 AND B.NUM_CLIENTE = A.NUM_CLIENTE AND A.NUM_IDEN = ";

    /**
     * Se deberá concatenar al final del string el valor del rut entre comillas simples.
     */
    private static final String SQL_SEARCH_CLIENT_BY_NAME_LIKE =
        "SELECT * from (\n" + "SELECT I.NUM_IDEN NUM_IDEN, NULL as CODIGO_CLIENTE, C.GLS_RAZON_SOCI NOMBRE, B.COD_SECTOR_ECONOM COD_SECTOR_ECONOM, 'RUT_COMPANY' AS CLIENT_TYPE\n"
            + "    FROM IDECLI I, CLIJUR C, BASCLI B\n" + "    WHERE UPPER(C.GLS_RAZON_SOCI) LIKE UPPER(?1)\n" + "        AND I.COD_REG_STATUS = 0\n"
            + "        AND C.COD_REG_STATUS = 0\n" + "        AND C.NUM_CLIENTE = I.NUM_CLIENTE\n" + "        AND B.COD_REG_STATUS (+)= 0\n"
            + "        AND B.NUM_CLIENTE (+)= I.NUM_CLIENTE\n" + "    union\n" + "\n"
            + "SELECT I.NUM_IDEN NUM_IDEN, NULL as CODIGO_CLIENTE, C.NOM_NOMBRES || ' ' || C.NOM_PATERNO || ' ' || C.NOM_MATERNO NOMBRE, NULL COD_SECTOR_ECONOM, 'RUT_PERSON' AS "
            + "CLIENT_TYPE\n" + "    FROM IDECLI I, CLINAT C\n" + "        WHERE I.COD_REG_STATUS = 0\n"
            + "            AND UPPER(C.NOM_NOMBRES || ' ' || C.NOM_PATERNO || ' ' || C.NOM_MATERNO) LIKE UPPER(?1)\n" + "            AND C.NUM_CLIENTE = I.NUM_CLIENTE\n"
            + "            AND C.COD_REG_STATUS = 0\n" + "    union \n"
            + "SELECT COD_CLIENTE CODIGO_CLIENTE,NULL as NUM_IDEN, NOM_CLIENTE || DECODE(' ' || NOM_PATERNO,' ','',' ' || NOM_PATERNO) || DECODE(' ' "
            + "|| NOM_MATERNO,' ','',' ' "
            + "|| NOM_MATERNO) NOMBRE, TO_NUMBER(COD_ACTIVIDAD) COD_SECTOR_ECONOM, 'NO_RUT' AS CLIENT_TYPE\n" + "    FROM SINRUT\n"
            + "        WHERE UPPER(NOM_CLIENTE || ' ' || NOM_PATERNO || ' ' || NOM_MATERNO) LIKE UPPER(?1)\n" + ") ORDER BY NOMBRE ASC";

    private static final String SP_CIF_SP_INS_CLIENTE               = "CIF_SP_INS_CLIENTE";
    private static final String SP_INS_CLIENTE_01_P_COD_CANAL       = "P_COD_CANAL";
    private static final String SP_INS_CLIENTE_02_P_GLS_APLICACION  = "P_GLS_APLICACION";
    private static final String SP_INS_CLIENTE_03_P_COD_SUCURSAL    = "P_COD_SUCURSAL";
    private static final String SP_INS_CLIENTE_04_P_COD_OPERADOR    = "P_COD_OPERADOR";
    private static final String SP_INS_CLIENTE_05_P_NUM_IDEN        = "P_NUM_IDEN";
    private static final String SP_INS_CLIENTE_06_P_COD_TIPO_IDEN   = "P_COD_TIPO_IDEN";
    private static final String SP_INS_CLIENTE_07_P_COD_TIPO_JURIDI = "P_COD_TIPO_JURIDI";
    private static final String SP_INS_CLIENTE_08_P_GLS_NOMBRE      = "P_GLS_NOMBRE";
    private static final String SP_INS_CLIENTE_09_P_GLS_PATERNO     = "P_GLS_PATERNO";
    private static final String SP_INS_CLIENTE_10_P_GLS_MATERNO     = "P_GLS_MATERNO";
    private static final String SP_INS_CLIENTE_11_P_COD_ACTIVIDAD   = "P_COD_ACTIVIDAD";
    private static final String SP_INS_CLIENTE_12_P_RETORNO         = "P_RETORNO";
    private static final String SP_INS_CLIENTE_13_P_GLOSA           = "P_GLOSA";

    private final ClienteNaturalRepository clienteNaturalRepository;
    private final SQLDGLRepository         SQLDGLRepository;
    private final SQLCIFRepository         sqlcifRepository;
    private final ClienteNaturalMapper     clienteNaturalMapper;
    private final ApplicationProperties    applicationProperties;
    private final DesgloseService          desgloseService;
    private final UserService              userService;
    private final TurbineUserService       turbineUserService;
    private final SfStfOperatorRepository  sfStfOperatorRepository;

    public ClienteService(ClienteNaturalRepository clienteNaturalRepository, SQLDGLRepository SQLDGLRepository, ClienteNaturalMapper clienteNaturalMapper,
        ApplicationProperties applicationProperties, DesgloseService desgloseService, UserService userService, TurbineUserService turbineUserService,
        SfStfOperatorRepository sfStfOperatorRepository,SQLCIFRepository sqlcifRepository) {
        this.clienteNaturalRepository = clienteNaturalRepository;
        this.SQLDGLRepository = SQLDGLRepository;
        this.clienteNaturalMapper = clienteNaturalMapper;
        this.applicationProperties = applicationProperties;
        this.desgloseService = desgloseService;
        this.userService = userService;
        this.turbineUserService = turbineUserService;
        this.sfStfOperatorRepository = sfStfOperatorRepository;
        this.sqlcifRepository = sqlcifRepository;
    }

    /**
     * Search for clients with the given rut.
     *
     * @param rut
     * 		Rut del cliente
     * @param codTipoDes
     * @param codSubTipoDes
     * @param codMoneda
     * @param codTipoPlantillaDes
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> findByRut(String rut, Integer codTipoDes, Integer codSubTipoDes, String codMoneda, Integer codTipoPlantillaDes,String esquema) {
        log.info("INICIO - Request to ClienteService findByRut");
        if (rut == null || rut.trim().isEmpty() || Constants.ZERO_AS_STRING.equals(rut)) {
            throw new InvalidRutException();
        }
        List<ClienteDTO> list = new ArrayList<>();

        // "txtRut_LostFocus(): Buscando Cliente [" & auxRut & "] en Maestro de Clientes por Rut..."
        // Buscar en clientes naturales
        StringBuilder sb = new StringBuilder(ClienteService.SQL_SEARCH_NATURAL_CLIENT_BY_RUT);
        sb.append("'").append(rut.toUpperCase()).append("'");
        log.debug("QUERY CLIENTES NATURALES: " + sb.toString());
        List<Object[]> objects = this.sqlcifRepository.executeNativeSelectQuery(sb.toString());
        boolean isCompany = false;
        boolean sinRut = false;

        if (objects == null || objects.isEmpty()) { // Buscar en clientes jurídicos
            sb.setLength(0);
            sb.append(ClienteService.SQL_SEARCH_JURIDIC_CLIENT_BY_RUT).append("'").append(rut).append("'");
            log.debug("QUERY CLIENTES JURIDICOS: " + sb.toString());
            objects = this.sqlcifRepository.executeNativeSelectQuery(sb.toString());
            isCompany = true;
        }
        if (objects == null || objects.isEmpty()) { // Buscar en clientes SIN RUT
            sb.setLength(0);
            sb.append(ClienteService.SQL_SEARCH_SIN_RUT_CLIENT).append("'").append(rut).append("'");
            log.debug("QUERY CLIENTES SIN RUT: " + sb.toString());
            objects = this.sqlcifRepository.executeNativeSelectQuery(sb.toString());
            isCompany = false;
            sinRut = true;
        }
        for (Object[] elem : objects) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setRut(elem[0].toString());
            clienteDTO.setClientCode(elem[0].toString());
            clienteDTO.setName(elem[1].toString());
            clienteDTO.setIsCompany(isCompany);
            sb.setLength(0);
            sb.append(ClienteService.SQL_SEARCH_ECONOMIC_ACTIVITY_JURIDIC_CLIENT_BY_RUT).append("'").append(rut).append("'");
            if(sinRut == false) {
                log.debug("QUERY ACTIVIDAD ECONOMICA: " + sb.toString());
                List<Object[]> economicActivities = this.sqlcifRepository.executeNativeSelectQuery(sb.toString());
                if (economicActivities.size() > 0) {
                    Object elem1 = economicActivities.get(0);
                    if (elem1 != null && !Constants.COD_ACTIVITY_WITHOUT_CLASSIFICATION.equals(elem1.toString())) {
                        clienteDTO.setActivity(elem1.toString());
                    }
                }
            } else {
                clienteDTO.setActivity(elem[2].toString());
            }
            // Se pone la actividad económica del cliente por defecto en sin clasificar...
            if (clienteDTO.getActivity() == null || clienteDTO.getActivity().trim().isEmpty()) {
                // showMessage "Cliente sin Actividad Comercial definida...", ErrorInterno
                // throw new NoEconomicActivityException();
                // clienteDTO.addWarning(ErrorConstants.ERR_NO_ECONOMIC_ACTIVITY);
                clienteDTO.setActivity(Constants.COD_ACTIVITY_WITHOUT_CLASSIFICATION);
            }
            list.add(clienteDTO);
        }
        /*if (list.isEmpty()) {
            throw new RutDoesNotExistException();
        }*/
        // Cargar los datos del desglose si el resutlado de la búsqueda de cliente es 1. En teoría esto es siempre.
        if (list.size() == 1) {
            BreakdownSearchRequestDTO searchRequestDTO = new BreakdownSearchRequestDTO();
            final ClienteDTO clienteDTO = list.get(0);
            searchRequestDTO.codCliente = clienteDTO.getClientCode();
            searchRequestDTO.tipoDesglose = codTipoDes;
            searchRequestDTO.subTipoDesglose = codSubTipoDes;
            searchRequestDTO.moneda = codMoneda;
            searchRequestDTO.tipoPlantillaDesglose = codTipoPlantillaDes;
            // Se busca el desglose
            final List<BreakdownSearchResponseDTO> searchResponseDTOS = this.desgloseService.searchDesglosesCreatedFromSp(searchRequestDTO,esquema);
            if (searchResponseDTOS != null && !searchResponseDTOS.isEmpty()) {
                final BreakdownSearchResponseDTO searchResponseDTO = searchResponseDTOS.get(0);
                if (searchResponseDTO.userOpen != null) {
                    // final User user = userService.findOneByLogin(searchResponseDTO.userOpen);
                    final TurbineUserDTO user =
                        searchResponseDTO.userOpen != null ? this.turbineUserService.findOne(Long.parseLong(searchResponseDTO.userOpen)).orElse(null) : null;
                    // final TurbineUserDTO user = this.turbineUserService.findOne(null).orElse(null);
                    searchResponseDTO.nomUserOpen = user != null && !user.getFullName().trim().isEmpty() ? user.getFullName() : Constants.UNKNOWN_TURBINE_USER_NAME;
                    searchResponseDTO.userOpen = user != null  ? user.getLoginName() : null;
                    // Query = " SELECT U.FIRST_NAME || ' ' || U.LAST_NAME NOMBRE" & _
                    // " FROM   TURBINE_USER U" & _
                    // " WHERE  UPPER(U.LOGIN_NAME) = UPPER('" & varStatus.userOpen & "')"
                }
                clienteDTO.setBreakdownSearchResponseDTO(searchResponseDTO);
            } else { // Si no se encuentra se llama al SP TakeDesgloRol que lo crea y lo toma.
                this.desgloseService.executeCifSpTakeDesgloRol(searchRequestDTO.codCliente, codTipoDes, codSubTipoDes, codMoneda, codTipoPlantillaDes);
            }
        }
        log.info("FIN - Request to ClienteService findByRut");
        return list;
    }

    /**
     * Search for clients with the given name.
     *
     * @param name
     * 		Nombre del cliente
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> findByName(String name,Integer codTipoDes, Integer codSubTipoDes, String codMoneda, Integer codTipoPlantillaDes) {
        log.info("INICIO - Buscando cliente con nombre {} ", name);
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyNameException();
        }
        name = name.replace("'", "''").toUpperCase(); // Se eliminan espacios y se convierte a mayúsculas.
        List<ClienteDTO> list = new ArrayList<>();

        // Buscar en clientes naturales, clientes jurídicos y clientes sin rut
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("%").append(name).append("%");
        params.add(sb.toString());
        log.debug("QUERY: " + ClienteService.SQL_SEARCH_CLIENT_BY_NAME_LIKE);
        log.debug("PARAMETROS: " + params.toString());
        List<Object[]> objects = this.sqlcifRepository.executeNativeSelectQuery(ClienteService.SQL_SEARCH_CLIENT_BY_NAME_LIKE, params);
        for (Object[] elem : objects) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setRut(elem[0] != null ? elem[0].toString() : null);
            clienteDTO.setClientCode(elem[0] != null ? elem[0].toString() : null);
            clienteDTO.setName(elem[2].toString());
            clienteDTO.setActivity(elem[3] != null ? elem[3].toString() : null);
            clienteDTO.setClientType(elem[4].toString());
            list.add(clienteDTO);
        }
        // Cargar los datos del desglose si el resutlado de la búsqueda de cliente es 1. En teoría esto es siempre.
        if (list.size() == 1) {
            BreakdownSearchRequestDTO searchRequestDTO = new BreakdownSearchRequestDTO();
            final ClienteDTO clienteDTO = list.get(0);
            searchRequestDTO.codCliente = clienteDTO.getClientCode();
            searchRequestDTO.tipoDesglose = codTipoDes;
            searchRequestDTO.subTipoDesglose = codSubTipoDes;
            searchRequestDTO.moneda = codMoneda;
            searchRequestDTO.tipoPlantillaDesglose = codTipoPlantillaDes;
            // Se busca el desglose
            final List<BreakdownSearchResponseDTO> searchResponseDTOS = this.desgloseService.searchDesglosesCreatedFromSp(searchRequestDTO,null);
            if (searchResponseDTOS != null && !searchResponseDTOS.isEmpty()) {
                final BreakdownSearchResponseDTO searchResponseDTO = searchResponseDTOS.get(0);
                if (searchResponseDTO.userOpen != null) {
                    // final User user = userService.findOneByLogin(searchResponseDTO.userOpen);
                    final TurbineUserDTO user =
                        searchResponseDTO.userOpen != null ? this.turbineUserService.findOne(Long.parseLong(searchResponseDTO.userOpen)).orElse(null) : null;
                    // final TurbineUserDTO user = this.turbineUserService.findOne(null).orElse(null);
                    searchResponseDTO.nomUserOpen = user != null && !user.getFullName().trim().isEmpty() ? user.getFullName() : Constants.UNKNOWN_TURBINE_USER_NAME;
                    searchResponseDTO.userOpen = user != null  ? user.getLoginName() : null;
                    // Query = " SELECT U.FIRST_NAME || ' ' || U.LAST_NAME NOMBRE" & _
                    // " FROM   TURBINE_USER U" & _
                    // " WHERE  UPPER(U.LOGIN_NAME) = UPPER('" & varStatus.userOpen & "')"
                }
                clienteDTO.setBreakdownSearchResponseDTO(searchResponseDTO);
            } else { // Si no se encuentra se llama al SP TakeDesgloRol que lo crea y lo toma.
                this.desgloseService.executeCifSpTakeDesgloRol(searchRequestDTO.codCliente, codTipoDes, codSubTipoDes, codMoneda, codTipoPlantillaDes);
            }
        }
        log.info("FIN - Buscando cliente con nombre {} ", name);
        return list;
    }

    /**
     * Save a client.
     *
     * @param client
     * 		Cliente
     *
     * @return the saved Cliente
     */
    public ClienteDTO saveClient(ClienteDTO client, String userLogin) throws StorageProcedureExecutionException {
        log.info("INICIO - Guardar cliente.");
        log.debug(client.toString());
        ClienteDTO clienteDTO = client;
        // Validations

        // Llenar los parámetros
        // Esta lógica está fija en el código...
        if (client.getRut() != null && !client.getRut().trim().isEmpty()) {
            client.setIdentityType(Constants.CLIENT_IDENTITY_TYPE_RUT);
            // Agregar al rut el dígito verificador.
            clienteDTO.setRut(client.getRut() + client.getRutVerificationNumber());
        }
        List<Param<?>> parameters = fillSaveClientParams(clienteDTO, userLogin);

        // Interpretar los resultados
        log.debug("QUERY: " + ClienteService.SP_CIF_SP_INS_CLIENTE);
        log.debug("PARAMETROS: " + parameters.toString());
        Map<String, Object> objects = this.sqlcifRepository.executeNativeStoredProcedureOutputNoSelect(ClienteService.SP_CIF_SP_INS_CLIENTE, parameters);
        log.debug(String.valueOf(objects));
        Integer returnParam = (Integer) objects.get(ClienteService.SP_INS_CLIENTE_12_P_RETORNO);
        if (returnParam == null) {
            log.error(Constants.INTERNAL_SERVER_ERROR + " Ocurrió un error llamando al procedimiento almacenado: " + ClienteService.SP_CIF_SP_INS_CLIENTE);
            throw new StorageProcedureExecutionException();
        }
        if (returnParam.equals(0)) {
            String identityNumber = (String) objects.get(ClienteService.SP_INS_CLIENTE_05_P_NUM_IDEN);
            client.setClientCode(identityNumber);
        } else { // El SP devolvió un error.
            String returnMessage = (String) objects.get(ClienteService.SP_INS_CLIENTE_13_P_GLOSA);
            log.error(Constants.INTERNAL_ERROR + returnParam + Constants.INFORM + returnMessage);
            throw new StorageProcedureBusinessError(returnMessage);
        }

        log.info("FIN  - Guardar cliente.");
        return client;
    }

    /**
     * Llena los parámeteos del procedimiento almacenado para guardar un cliente.
     *
     * @param client
     * @param userLogin
     *
     * @return
     */
    private List<Param<?>> fillSaveClientParams(@NotNull ClienteDTO client, @NotNull String userLogin) {
        Param<Integer> p1 = new Param<>(ClienteService.SP_INS_CLIENTE_01_P_COD_CANAL, Integer.class, applicationProperties.getChanelCode(), ParameterMode.IN);
        Param<String> p2 = new Param<>(ClienteService.SP_INS_CLIENTE_02_P_GLS_APLICACION, String.class, applicationProperties.getApplicationName(), ParameterMode.IN);
        Param<Integer> p3 = new Param<>(ClienteService.SP_INS_CLIENTE_03_P_COD_SUCURSAL, Integer.class, applicationProperties.getBranchOfficeCode(), ParameterMode.IN);

        // Asignar el código de operados de STF
        final SfStfOperator stfOperator = sfStfOperatorRepository.findFirstByLoginName(userLogin);
        if (stfOperator == null || stfOperator.getOperatorCode() == null) {
            throw new InternalServerErrorException("The user " + userLogin + " can not be found un the SF_STF scheme.");
        }
        Param<String> p4 = new Param<>(ClienteService.SP_INS_CLIENTE_04_P_COD_OPERADOR, String.class, stfOperator.getOperatorCode().toString(), ParameterMode.IN);
        Param<String> p5 = new Param<>(ClienteService.SP_INS_CLIENTE_05_P_NUM_IDEN, String.class, client.getRut(), ParameterMode.INOUT);
        Param<Integer> p6 =
            new Param<>(ClienteService.SP_INS_CLIENTE_06_P_COD_TIPO_IDEN, Integer.class, client.getIdentityType() != null ? Integer.valueOf(client.getIdentityType()) : null,
                ParameterMode.IN);
        Param<Integer> p7 =
            new Param<>(ClienteService.SP_INS_CLIENTE_07_P_COD_TIPO_JURIDI, Integer.class, client.getClientType() != null ? Integer.valueOf(client.getClientType()) : null,
                ParameterMode.IN);
        Param<String> p8 = new Param<>(ClienteService.SP_INS_CLIENTE_08_P_GLS_NOMBRE, String.class, client.getName().trim().toUpperCase(), ParameterMode.IN);
        Param<String> p9 =
            new Param<>(ClienteService.SP_INS_CLIENTE_09_P_GLS_PATERNO, String.class, client.getFatherLastName() != null ? client.getFatherLastName().trim().toUpperCase() : null,
                ParameterMode.IN);
        Param<String> p10 =
            new Param<>(ClienteService.SP_INS_CLIENTE_10_P_GLS_MATERNO, String.class, client.getMotherLastName() != null ? client.getMotherLastName().trim().toUpperCase() : null,
                ParameterMode.IN);
        Param<Integer> p11 =
            new Param<>(ClienteService.SP_INS_CLIENTE_11_P_COD_ACTIVIDAD, Integer.class, client.getActivity() != null ? Integer.valueOf(client.getActivity()) : null,
                ParameterMode.IN);
        Param<Integer> p12 = new Param<>(ClienteService.SP_INS_CLIENTE_12_P_RETORNO, Integer.class, null, ParameterMode.OUT);
        Param<String> p13 = new Param<>(ClienteService.SP_INS_CLIENTE_13_P_GLOSA, String.class, null, ParameterMode.OUT);
        List<Param<?>> parameters = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13);
        return parameters;
    }
}
