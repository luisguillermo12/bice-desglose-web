package cl.tutorial.desglose.config;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SCRIPT_ENGINE_BY_NAME                         = "ECMAScript";
    public static final String SQUARE_BRACKETS_CONCEPT_CHARACTER_LEFT        = "[";
    public static final String SQUARE_BRACKETS_CONCEPT_CHARACTER_RIGHT       = "]";
    public static final String ACTUAL_CONCEPT_EXPRESSION                     = "VALOR";
    public static final String QUADRATURE_TOLERANCE_EXPRESSION               = "TOLERANCIA";
    public static final String PROPERTY_NAME_FECH_PERIODO                    = "fecPeriodo";
    public static final String PROPERTY_NAME_FECH_PERIODO_PRY                = "fecPeriodoPry";
    public static final String QUADRATURE_VALIDATION_NOT_REQUIRED_EXPRESSION = "{INFO}";
    public static final String EQUALS_SIGN                                   = "=";

    public static final String SYSTEM_ACCOUNT                  = "system";
    public static final String ANONYMOUS_USER                  = "anonymoususer";
    public static final String DEFAULT_LANGUAGE                = "es";
    public static final String ZERO_AS_STRING                  = "0";
    public static final Long   ZERO_AS_LONG                    = Long.valueOf("0");
    public static  final  Integer ONE_AS_INTEGER                  = Integer.valueOf("1");
    public static final String VALUE_AS_STRING                 = "#VALUE!";
    public static final String ERROR_AS_STRING                 = "#ERROR!";
    public static final String PLAT_DESGLOSE                   = "Plat_Desglose";
    public static final String PLAT_JEFE                       = "Plat_Desglose_Jefe";
    public static final String PLAT_REVISOR                    = "Plat_Desglose_Revisor";
    public static final String PLAT_EJECUTOR                   = "Plat_Desglose_Ejecutor";
    public static final String PLAT_DESGLOSE_ADMINISTRADOR     = "Plat_Desglose_Administrador";
    public static final String PLAT_DESGLOSE_CUSTOM_CLONE_ROLE = "Plat_Desglose_Custom_Clone";

    public static final String INTERNAL_ERROR = "*** ERROR INTERNO ***: ";
    public static final String INFORM         = "Informe: ";

    public static final String ORACLE_KNOWN_ERROR_MESSAGE_1             = "value larger than specified precision allowed for this column";
    public static final String ORACLE_KNOWN_ERROR_MESSAGE_1_TRANSLATION = "Para la fila %s, el valor es mayor a lo permitido. Tipo de concepto: %s, código: %s, período: %s.";

    public static final String ROLE_PART_NAME_PLAT_DESGLOSE = "PLAT_DESGLOSE";

    public static final String  CODTABLA_IVA        = "IVA";
    public static final Integer COD_REG_STATUS_ZERO = 0;
    public static final Integer COD_REG_STATUS_ONE  = 1;
    public static final Integer MAX_PERIODS_SHOWED  = 4;

    public static final String ACTION_NAME_CLONE  = "Clonar";
    public static final String ACTION_NAME_DELETE = "Eliminar";

    public static final String CODTABLA_ESTADO_DESGLOSE            = "DGLCONFG";
    public static final String CODTABLA_AUDITORES                  = "AUDITBAL";
    public static final String COD_GENERICO_DESGLOSE               = "ESTADOS-";
    public static final String COD_GENERICO_ORDEN_ESTADO_DESGLOSE  = "ORDEN_ESTADO-";
    public static final String COD_ACTIVITY_WITHOUT_CLASSIFICATION = "0";

    // Constantes de columnas de valores de las tablas.
    public static final String COLUMN_NUMBER_GLS_FORMULA__1 = "F";
    public static final String COLUMN_NUMBER_GLS_FORMULA__2 = "I";
    public static final String COLUMN_NUMBER_GLS_FORMULA__3 = "L";
    public static final String COLUMN_NUMBER_GLS_FORMULA__4 = "O";

    public static final Integer   COLUMN_NUMBER_COD_TIPO_CONCEP = 0;
    public static final Integer   COLUMN_NUMBER_COD_TIPO_REGIS  = 1;
    public static final Integer   COLUMN_NUMBER_FLG_IS_TOTAL    = 2;
    public static final Integer   COLUMN_NUMBER_COD_CONCEPT     = 3;
    public static final Integer   COLUMN_NUMBER_CCC             = 4;
    public static final Integer[] COLUMN_NUMBER_GLS_FORMULA_VAL = { 5,  8, 11, 14 };
    public static final Integer[] COLUMN_NUMBER_GLS_FORMULA_PRC = { 6,  9, 12, 15 };
    public static final Integer[] COLUMN_NUMBER_GLS_FORMULA_DEF = { 7, 10, 13, 16 };

    public static final String  FORMULA_NUM_MESES_STRING             = "NUM_MESES";
    public static final Integer FORMULA_NUM_MESES_STRING_INDEX       = -50;
    public static final String  FORMULA_IVA_STRING                   = "IVA";
    public static final Integer FORMULA_IVA_STRING_INDEX             = -51;
    public static final String  FORMULA_P_ANT_NUM_MESES_STRING       = "P_ANT.NUM_MESES";
    public static final Integer FORMULA_P_ANT_NUM_MESES_STRING_INDEX = -52;
    public static final String  FORMULA_DEF_BASE                     = "DEF_BASE";
    public static final Integer FORMULA_DEF_BASE_INDEX               = -53;
    public static final String  FORMULA_DEF_PERI                     = "DEF_PERI";
    public static final Integer FORMULA_DEF_PERI_INDEX               = -54;
    public static final String  FORMULA_CNPT                         = "CNPT";
    public static final String  COD_MINUS_1_STRING_VALUE             = "-1";
    public static final String  FORMULA_P_ANT                        = "P_ANT.";
    public static final String  FORMULA_P_SIG                        = "P_SIG.";

    public static final String DEFLACT_BASE_IVA = "BASE_VA";
    public static final String DEFLACT_PERIODS  = "PERIODS_DE";
    public static final String CANT_MESES_PER0  = "MESES_PER0";
    public static final String CANT_MESES_PER1  = "MESES_PER1";
    public static final String CANT_MESES_PER2  = "MESES_PER2";
    public static final String CANT_MESES_PER3  = "MESES_PER3";
    public static final String CANT_MESES_PER4  = "MESES_PER4";
    public static final String DEF_PER1         = "DE_PER1";
    public static final String DEF_PER2         = "DE_PER2";
    public static final String DEF_PER3         = "DE_PER3";
    public static final String DEF_PER4         = "DE_PER4";

    public static final Integer NUM_LINEA_AUDITORIA                = -1;
    public static final String  EVENTO_AUDITORIA_DESHABILITA_NOTIF = "DESHABILITA_NOTIF";
    public static final String  EVENTO_AUDITORIA_CAMBIO_ESTADO     = "CAMBIO_ESTADO";

    public static final String DESGLO_AUDIT_COMENTARIO_EN_EDICION        = "Se pone en edición el desglose.";
    public static final String DESGLO_AUDIT_COMENTARIO_DESHABILITA_NOTIF = "Se deshabilita notificación enviada por nueva edición de desglose.";

    // Códigos de flag de validación
    public static final String COD_OK  = "OK";
    public static final String COD_NOK = "NOK";

    // Códigos de flag req2Firma
    public static final String COD_REQ_2_FIRMA_SI = "1";
    public static final String COD_REQ_2_FIRMA_NO = "0";

    // Estados de los desgloses
    public static final String ESTADO_EN_EDICION           = "ED";
    public static final String ESTADO_BORRADOR_EN_PROCESO  = "BEP";
    public static final String ESTADO_BORRADOR_VALIDADO    = "BV";
    public static final String ESTADO_BORRADOR_TERMINADO   = "BT";
    public static final String ESTADO_BORRADOR_OBSERVACION = "OBS";
    public static final String ESTADO_EN_REVISION          = "REV";
    public static final String ESTADO_DEFINITIVO           = "DEF";
    public static final String ESTADO_CORREGIDO            = "DEFM";

    public static  final String PROCESS_USER_OPEN          = "T-";

    // Formato de las fechas en el sistema.
    public static final String            PATTERN_DATE_YYYYmm_SHORT      = "yyyyMM";
    public static final String            PATTERN_DATE_SHORT             = "dd-MM-yyyy";
    public static final String            PATTERN_DATE_SHORT_LATINO      = "dd/MM/yyyy";
    public static final String             PATTERN_DATE_SHORT_LATINO_CORTO = "dd/MM/yy";
    public static final String            PATTERN_DATE_SHORT_CORTO       = "dd-MM-yy";
    public static final String            PATTERN_DATE_SHORT_LETRAS_MES  = "MMM-yyyy";
    public static final DateTimeFormatter FORMATTER_DATE_SHORT           = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_SHORT,new Locale ( "es" , "ES" ));
    public static final DateTimeFormatter FORMATTER_DATE_SHORT_LATINO    = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_SHORT_LATINO,new Locale ( "es" , "ES" ));
    public static final DateTimeFormatter FORMATTER_DATE_SHORT_LATINO_CORTO    = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_SHORT_LATINO_CORTO,new Locale ( "es" , "ES" ));

    public static final DateTimeFormatter FORMATTER_DATE_SHORT_CORTO     = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_SHORT_CORTO,new Locale ( "es" , "ES" ));
    public static final DateTimeFormatter FORMATTER_DATE_YYYYmm_SHORT    = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_YYYYmm_SHORT,new Locale ( "es" , "ES" ));
    public static final String            PATTERN_DATE_TIME              = "dd-MM-yyyy HH:mm:ss";
    public static final DateTimeFormatter FORMATTER_DATE_TIME            = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_TIME,new Locale ( "es" , "ES" ));
    public static final String            PATTERN_DATE_NO_SEPARATORS     = "yyyyMMdd";
    public static final DateTimeFormatter FORMATTER_DATE_NO_SEPARATORS   = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_NO_SEPARATORS,new Locale ( "es" , "ES" ));
    public static final DateTimeFormatter FORMATTER_DATE_SHORT_MES_LETRA = DateTimeFormatter.ofPattern(Constants.PATTERN_DATE_SHORT_LETRAS_MES, new Locale ( "es" , "ES" ));

    public static final String INTERNAL_SERVER_ERROR     = "*** ERROR INTERNO ***: ";
    public static final String CLIENT_IDENTITY_TYPE_RUT  = "1";
    public static final String USER_OPEN_T_MINUS_VALUE   = "T-";
    public static final String UNKNOWN_TURBINE_USER_NAME = "Desconocido...";

    // Códigos para los tipos de conceptos
    public static final String COD_TIPO_CONCEP_BALANCE    = "BALANCE";
    public static final String COD_TIPO_CONCEP_EST_RESULT = "EST_RESULT";
    public static final String COD_TIPO_CONCEP_GEN_FONDOS = "GEN_FONDOS";
    public static final String COD_TIPO_CONCEP_IND_FINAN  = "IND_FINAN";
    public static final String COD_TIPO_CONCEP_PROY_DM    = "PROY_DM";
    public static final String COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL = "PROY_DM_BALAN_GRAL";
    public static final String COD_TIPO_CONCEP_PROY_DM_EST_RESULT  = "PROY_DM_EST_RESULT";
    public static final String COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA  = "PROY_DM_FLUJO_CAJA";
    public static final String ESTADO_DE_FLUJO_DE_EFECTIVO  = "ESTADO DE FLUJO DE EFECTIVO";



    // Codigo de tipo de destino de mail
    public static final String COD_TIPO_DESTINO = "TO";

    public static final String CARGO_1 = "CARGO-001";
    public static final String CARGO_2 = "CARGO-002";

    public static final Integer COD_REG_STATUS_ENVIA_REE      = 0;
    public static final Integer COD_REG_STATUS_VIGENTE_REE    = 1;
    public static final Integer COD_REG_STATUS_DEVOLUCION_REE = 2;
    public static final Integer COD_REG_STATUS_APROBADO_REE   = 3;

    public static final String MDB1 = "MDB_elimPropDesglose";
    public static final String MDB2 = "MDB_creacionDesglose";
    public static final String MDB3 = "MDB_eliminaDesglose";
    public static final String MDB4 = "MDB_consultaDesglose";
    public static final String MDB5 = "MDB_clonarDesglose";
    public static final String MDB6 = "MDB_elimTodoDesglose";

    public static final String MULTI_REGISTRY = "MULTIREGISTRO";

    public static final String DGLCONFG               = "DGLCONFG";
    public static final String DESGLOCF               = "DESGLOCF";
    public static final String CANT_REG_CONS_AGENDA   = "CANT_REG_CONS_AGENDA";
    public static final String GENERIC_CODE_TOLERANCE = "002";

    public static final String DEFAULT_START_DATE_FORMATTED = "19000101";
    public static final String DEFAULT_END_DATE_FORMATTED   = "30001231";

    public static final String BREAKDOWN_QUERY_UI_ORIGIN_REVIEW_PAGE = "REVIEW_PAGE";
    public static final String BREAKDOWN_QUERY_UI_ORIGIN_QUERY_PAGE  = "QUERY_PAGE";

    public static final BigDecimal BIG_DECIMAL_VALUE_100 = new BigDecimal("100");

    public static final String FIELD_NAME_COD_TIPO_CONCEP = "codTipoConcep";
    public static final String FIELD_NAME_NUM_SECUENCIA   = "numSecuencia";

    public  static final String CODTABLA_TIPO_DESGLOSE          = "TIPODESG";
    public  static final String CODTABLA_SUBTIPO_DESGLOSE       = "SBTDES";
    public  static final String CODTABLA_UNIDAD_MONETARIA       = "UNIDMON";
    public  static final String CODTABLA_TIPO_MONEDAS           = "MONEDAS";
    public  static final String CODTABLA_PLANTILLAS_DESGLOSE    = "TIPPLANT";
    public  static final String CODTABLA_PROYECIONES_DESGLOSE   = "PLANPROY";
    public  static final String CODTABLA_TIPO_RESPUESTA_AUDITOR = "TIPREAUD";

    public static final String DATABASE_ORACLE = "ORACLE";
    public static final String DATABASE_SQL    = "SQL";

    public static final String COD_TIPO_DESG_CONSOLIDADO = "1";

    public static  final  String CLIENTE_SOLICITA_WEB = "WEB";
    public static  final  String CLIENTE_SOLICITA_VB = "VB";
    public static  final  String CLIENTE_SOLICITA_CORREO = "CORREO";

    public static  final String ESQUEMA_DGL = "DGL_EXP";
    public static  final String ESQUEMA_CIF = "CIF_EXP";

}
