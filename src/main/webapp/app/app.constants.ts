// These constants are injected via webpack environment variables.
// You can add more variables in webpack.common.js or in profile specific webpack.<dev|prod>.js files.
// If you change the values in the webpack config files, you need to re run webpack to update the application

import { ExpandOperator } from 'rxjs/internal/operators/expand';

export const VERSION = process.env.VERSION;
export const DEBUG_INFO_ENABLED: boolean = !!process.env.DEBUG_INFO_ENABLED;
export const SERVER_API_URL = process.env.SERVER_API_URL;
export const BUILD_TIMESTAMP = process.env.BUILD_TIMESTAMP;
export const MAX_PROJECTION_YEARS = Number(process.env.MAX_PROJECTION_YEARS);
export const USER_SESSION_IDLE_TIME = Number(process.env.USER_SESSION_IDLE_TIME);
export const USER_SESSION_TIMEOUT_TIME = Number(process.env.USER_SESSION_TIMEOUT_TIME);
export const USER_SESSION_PING_TIME = Number(process.env.USER_SESSION_PING_TIME);
export const USER_SESSION_AUTO_LOGOUT = process.env.USER_SESSION_AUTO_LOGOUT;

/**
 * Estas constantes son definidas en el componente IDLE, pero se pasan acá porque no estaba tomando los valores.
 * @type {string}
 */
export const USER_SESSION_DEFAULT_INTERRUPT_SOURCES = 'mousemove keydown DOMMouseScroll mousewheel mousedown touchstart touchmove scroll';
export const USER_SESSION_IDLE_TIMEOUT_IN_SECONDS = 5; /*1 minuto de inactividad de la sesión.*/
export const USER_ACTIVITY_IDLE_TIMEOUT_IN_SECONDS = 5; /*5 segundos cuando culmine el período de inactividad del usuario.*/
export const USER_SESSION_KEEP_ALIVE_PING_INTERVAL_IN_SECONDS = 5; /*15 segundos para hacer ping a ver si el usuario realizó alguna acción.*/

// Tiempo que está visible el mensaje de error. 5000 por defecto.
export const ALERT_ERROR_TOAST_TIMEOUT = 4000;

export const COLUMN_SIZES_SEARCH_BREAKDOWN_TABLE = [ 120, 80, 120, 100, 110, 200, 180, 70, 90, 140, 80 ];
export const COLUMN_SIZES_SEARCH_BREAKDOWN_TABLE_DESGLO = [ 100, 120, 200, 120, 100, 70, 120, 140, 80, 90, 80 ];
export const HANDS_ON_TABLE_LICENSE_KEY = 'non-commercial-and-evaluation';

export const TEXTO_MONEDA_DMP = 'MONEDA';
export const URL_SEGMENT_NEW_ELEMENT = 'new';

// Códigos para los tipos de conceptos
export const COD_TIPO_CONCEP_BALANCE = 'BALANCE';
export const COD_TIPO_CONCEP_EST_RESULT = 'EST_RESULT';
export const COD_TIPO_CONCEP_GEN_FONDOS = 'GEN_FONDOS';
export const COD_TIPO_CONCEP_IND_FINAN = 'IND_FINAN';
export const COD_TIPO_CONCEP_PROY_DM = 'PROY_DM';
export const COD_TIPO_CONCEP_FLUJO_EF_SVS = 'FLUJO_EF_SVS';
export const COD_TIPO_CONCEP_FLUJO_EF = 'FLUJO_EF';
export const COD_TIPO_CONCEP_PROY_DM_EST_RESULT = 'PROY_DM_EST_RESULT';
export const COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA = 'PROY_DM_FLUJO_CAJA';
export const COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL = 'PROY_DM_BALAN_GRAL';
// Nombre de los campos para los order by
export const FIELD_NAME_COD_TIPO_CONCEP = 'codTipoConcep';
export const FIELD_NAME_NUM_SECUENCIA = 'numSecuencia';

export const ORDER_BY_ASC = 'ASC';
export const ORDER_BY_DESC = 'DESC';

// Códigos de las tablas de tipos de datos generales del banco
export const CODTABLA_TIPO_DESGLOSE = 'TIPODESG';
export const CODTABLA_SUBTIPO_DESGLOSE = 'SBTDES';
export const CODTABLA_TIPO_RESPUESTA_AUDITOR = 'TIPREAUD';
export const CODTABLA_UNIDAD_MONETARIA = 'UNIDMON';
export const CODTABLA_TIPO_MONEDAS = 'MONEDAS';
export const CODTABLA_TIPO_CLIENTE = 'TIPOCLIE';
export const CODTABLA_ESTADO_DESGLOSE = 'DGLCONFG';
export const CODTABLA_AUDITORES = 'AUDITBAL';
export const COD_GENERICO_DESGLOSE = 'ESTADOS-';
export const COD_GENERICO_PROYECTAR = 'PROYECTAR';

export const HANDS_ON_TABLE_CUSTOM_CULTURE = 'es-CL';
export const HANDS_ON_TABLE_NUMERIC_PATTERN = '0,0.00';
export const HANDS_ON_TABLE_NUMERIC_PATTERN_3 = '0,0.000';
export const HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN = '0,0';

// Estados de los desgloses
export const ESTADO_EN_EDICION = 'ED'; // al editar desglose
export const ESTADO_BORRADOR_EN_PROCESO = 'BEP'; // al presionar boton en proceso desglose en proceso
export const ESTADO_BORRADOR_VALIDADO = 'BV'; // esto es el estado cuadrado
export const ESTADO_BORRADOR_TERMINADO = 'BT'; // esto al enviar  a revision ENVIADO A REVISION
export const ESTADO_BORRADOR_OBSERVACION = 'OBS'; // esto al devolver desglose a ejecutor
export const ESTADO_EN_REVISION = 'REV';  // actualmente no en uso.(segun omar)
export const ESTADO_DEFINITIVO = 'DEF'; // al momento de presionar el boton de aprobar desglose, que solo lo hace el revisor de segunda firma
export const ESTADO_CORREGIDO = 'DEFM'; // cuando el ejecutor devuelve el desglose con observaciones

// Códigos de los tipos de desglose
export const COD_TIPO_DESG_CONSOLIDADO = '1';

export const COD_TIPO_DESG_BASE_NUEVA = '1';

// Códigos de los auditores
export const COD_AUDITOR_OTROS_9999 = '9999';

// Códigos de los sub tipos de desglose
export const COD_SUBTIPO_DESG_OTHER1 = '6';
export const COD_SUBTIPO_DESG_OTHER2 = '7';
export const COD_SUBTIPO_DESG_PESOS = '1';

// Códigos de datos.
export const COD_TIPO_MONEDA_PESOS = '0';
export const DEFLACTOR_BASE_DEFAULT_VALUE = 1;

export const ID_GROUP_STF = 1;

export const PLAT_CODCARGOJEFE = 'CARGO-001';
export const PLAT_CODCARGOREVISOR = 'CARGO-002';
export const PLAT_CODEJECUTOR = 'CARGO-003';
export const NOT_LIKE_EXPRESSION_ONLY_FATHERS = '%.%';

export const CODTABLA_TIPO_PLANTILLAS_DESGLOSE = 'TIPPLANT';
export const CODTABLA_TIPO_PLANTILLAS_PROYECCION = 'PLANPROY';
export const CODTABLA_TIPO_ACTIVIDADES = 'TIPPLANT';
export const CODTABLA_TIPO_REGISTRO = 'TIPREGIS';

export const CODTABLA_TIPO_CARGOS = 'DGLCONFG';

export const CODTABLA_TIPO_ACTIVIDADES_PERSONAS_SIN_RUT = 'CLAECONO';
export const SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES = 'SIMPLE_COMBOBOX_TYPES';
export const SESSION_VARIABLE_ECONOMIC_ACTIVITIES = 'ECONOMIC_ACTIVITIES';
export const SESSION_VARIABLE_SECTORS = 'SECTORS';
export const SESSION_VARIABLE_BREAKDOWN_STATES = 'ESTADOS_DESGLOSE';
export const SESSION_VARIABLE_SUB_SECTORS = 'SUB_SECTORS';
export const SESSION_VARIABLE_CARGOS = 'SIMPLE_CARGOS';
export const SESSION_VARIABLE_USUARIO = 'USER_SYSTEM';
export const SESSION_VARIABLE_USUARIO_NAME = 'USER_SYSTEM';
export const SESSION_REEMPLAZO_AUTENTICATE = 'REEMPLAZO_AUTENTICATE';
export const SESSION_REEMPLAZO_MENSAJE = 'REEMPLAZO_MENSAJE';
export const SESSION_REEMPLAZO_PARAMS = 'REEMPLAZO_PARAMS';
export const SESSION_VARIABLE_BUSQUEDA_DESGLO = 'BUSQUEDA_DESGLO';
export const SESSION_VARIABLE_EN_DESGLOSE = 'DESGLOSE';
export const SESSION_VARIABLE_CLOSE = 'CLOSE';

// Roles de usuario
export const USER_ROLE_PLAT_DESGLOSE = 'Plat_Desglose';
export const USER_ROLE_PLAT_DESGLOSE_JEFE = 'Plat_Desglose_Jefe';
export const USER_ROLE_PLAT_DESGLOSE_REVISOR = 'Plat_Desglose_Revisor';
export const USER_ROLE_PLAT_DESGLOSE_EJECUTOR = 'Plat_Desglose_Ejecutor';
export const USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR = 'Plat_Desglose_Administrador';
export const USER_ROLE_PLAT_DESGLOSE_CUSTOM_CLONE_ROLE = 'Plat_Desglose_Custom_Clone';

export const NAME_VALIDATION_PATTERN = '[0-9a-zA-ZñÑ &\'-]+';
export const RUT_VALIDATION_PATTERN = '[0-9]+';
export const RUT_VERIFICATION_NUMBER_VALIDATION_PATTERN = '[0-9kK]+';
export const DEFLACTOR_VALIDATION_PATTERN = '(\\d{1,3})([,](\\d{1,3}))?';

export const CSS_CLASS_HEADER_GROUP_RENDERER = 'header-group-renderer';
export const CSS_CLASS_HEADER_GROUP_RENDERER_ZEROS = 'header-group-renderer-zeros';
export const CSS_CLASS_HEADER_TOTAL_RENDERER = 'header-total-renderer';
export const CSS_CLASS_HEADER_TOTAL_RENDERER_NUMERIC = 'header-total-renderer-numeric';
export const CSS_CLASS_HEADER_TOTAL_RENDERER_ZERO = 'header-total-renderer_zeros';
export const CSS_CLASS_HEADER_TOTAL_NUMBER_RENDERER = 'header-total-number-renderer';
export const CSS_CLASS_HEADER_TOTAL_NUMBER_RENDERER_ERROR = 'header-total-number-renderer-error';
export const CSS_CLASS_HEADER_INFO_RENDERER = 'header-info-renderer';
export const CSS_CLASS_ZERO = 'make-me-white';
export const CSS_CLASS_HEREDO = 'make-me-heredado';
export const CSS_CLASS_ERROR = 'make-me-error';

export const ICON_CARD_OPEN = 'plus-square';
export const ICON_CARD_CLOSE = 'minus-square';
export const COLOR_BLUE_BACKGROUND = '#3367d6';
export const COLOR_WHITE_FOREGROUND = '#ffffff';

export const PRIMARY_WHITE = '#ffffff';
export const SECONDARY_GREY = '#ccc';

export const MULTI_REGISTRY = 'MULTIREGISTRO';

export const COLUMN_LETTER_GLS_FORMULA_VAL1 = 'F';
export const COLUMN_LETTER_GLS_FORMULA_VAL2 = 'I';
export const COLUMN_LETTER_GLS_FORMULA_VAL3 = 'L';
export const COLUMN_LETTER_GLS_FORMULA_VAL4 = 'O';
export const COLUMN_LETTER_GLS_FORMULA_DEF1 = 'H';
export const COLUMN_LETTER_GLS_FORMULA_DEF2 = 'K';
export const COLUMN_LETTER_GLS_FORMULA_DEF3 = 'N';
export const COLUMN_LETTER_GLS_FORMULA_DEF4 = 'Q';
export const REGEX_NUMBER = '[FILO][0-9]+';

export const COLUMN_NUMBER_COD_TIPO_CONCEP = 0;
export const COLUMN_NUMBER_COD_TIPO_REGIS = 1;
export const COLUMN_NUMBER_FLG_IS_TOTAL = 2;
export const COLUMN_NUMBER_COD_CONCEPT = 3;
export const COLUMN_NUMBER_CCC = 4;
export const COLUMN_NUMBER_GLS_FORMULA_VAL = [ 5, 8, 11, 14 ];
export const COLUMN_NUMBER_GLS_FORMULA_VAL_LETTERS = [ 'F', 'I', 'L', 'O' ];
export const COLUMN_NUMBER_GLS_FORMULA_PRC = [ 6, 9, 12, 15 ];
export const COLUMN_NUMBER_GLS_FORMULA_DEF = [ 7, 10, 13, 16 ];
export const COLUMN_NUMBER_GLS_FORMULA_DEF_LETTERS = [ 'H', 'K', 'N', 'Q'];

export const COL_NUMBER_COMPANY_NAME = 0;

export const VALUE_IS_ACTIVE_CONCEPT_TRUE = 1;
export const VALUE_IS_ACTIVE_CONCEPT_FALSE = 0;

export const COD_DEFAULT_TIPO_CAMBIO = 1;
export const SUB_TIPO_DESGLOSE_PESOS = '0: 1';
export const SUB_TIPO_DESGLOSE_OTRA_MONEDA = '1: 2';

export const EVENTO_AUDITORIA_MARCA_DEFINITIVO = 'MARCA_DEFINITIVO';
export const EVENTO_AUDITORIA_CAMBIO_ESTADO = 'CAMBIO_ESTADO';
export const EVENTO_AUDITORIA_VALIDACION_GRABAR = 'VALIDACION_AL_GRABAR';
export const EVENTO_AUDITORIA_DESHABILITA_NOTIF = 'DESHABILITA_NOTIF';
export const EVENTO_AUDITORIA_LIBERA_DESGLOSE = 'LIBERA_DESGLOSE';

export const COMENTARIO_AUDITORIA_CAMBIO_ESTADO = 'Se aprueba desglose por jefe de grupo';
export const COMENTARIO_AUDITORIA_CAMBIO_ESTADO_BORRADOR_TERMINADO = 'Se cambia estado por envio a revision para Revisor';
export const COMENTARIO_AUDITORIA_CAMBIO_ESTADO_BORRADOR_OBSERVACION = 'Se devuelve desglose a ejecutor';

export const COD_REG_STATUS_ENVIA_REE = 0;
export const COD_REG_STATUS_VIGENTE_REE = 1;
export const COD_REG_STATUS_DEVOLUCION_REE = 2;
export const COD_REG_STATUS_APROBADO_REE = 3;

export const DEFLACT_BASE_IVA = 'BASE_VA';
export const DEFLACT_PERIODS = 'PERIODS_DE';
export const CANT_MESES_PER = 'MESES_PER';
export const CANT_MESES_PER0 = 'MESES_PER0';
export const CANT_MESES_PER00 = 'MESES_PER00';
export const CANT_MESES_PER01 = 'MESES_PER01';
export const CANT_MESES_PER1 = 'MESES_PER1';
export const CANT_MESES_PER2 = 'MESES_PER2';
export const CANT_MESES_PER3 = 'MESES_PER3';
export const CANT_MESES_PER4 = 'MESES_PER4';
export const DEF_PER1 = 'DE_PER1';
export const DEF_PER2 = 'DE_PER2';
export const DEF_PER3 = 'DE_PER3';
export const DEF_PER4 = 'DE_PER4';
export const P_ANT = 'P_ANT.';
export const P_SIG = 'P_SIG.';
export const CNPT = 'CNPT';
export const INFINITY = -Infinity; // esto ocurre cuando se divide un numero negativo entre cero
export const INFINITY_STR = '-Infinity'; // esto ocurre cuando se divide un numero negativo entre cero

export const BREAKDOWN_QUERY_UI_ORIGIN_REVIEW_PAGE = 'REVIEW_PAGE';
export const BREAKDOWN_QUERY_UI_ORIGIN_QUERY_PAGE = 'QUERY_PAGE';

export const EVENT_NAME_AUTHENTICATION_SUCCESS = 'authenticationSuccess';

export const  SECTOR_DEFAULT = '00000000';
export const  SUBSECTOR_DEFAULT = '00000000';

// Sirve para inicializar los validadores de caracteres.
export const INICIALIZE_CONTADOR = 500;
export const INICIALIZE_CONTADOR_ENVREV = 1024;
export const MAX_CANT_CONTADOR = 500;
export const MAX_CANT_CONTADOR_ENVREV = 1024;
export const INICIALIZE_CHECKOTHER_CARACTER = true;
export const MAXCARACTERESVALIDATION = '*Ha superado el limite de caracteres permitidos.';
export  const UNIDADMONETARIADEFAULT = '3'; // MILLONES;
