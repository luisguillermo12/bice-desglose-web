package cl.tutorial.desglose.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE  = "error.concurrencyFailure";
    public static final String ERR_VALIDATION           = "error.validation";
    public static final String ERR_INVALID_RUT          = "error.invalidRut";
    public static final String ERR_NO_ECONOMIC_ACTIVITY = "error.noEconomicActivity";
    public static final String ERR_RUT_DOES_NOT_EXIST   = "error.rutDoesNotExist";
    public static final String ERR_CONCEPT_NOT_EXIST    = "error.conceptNotExist";
    public static final String ERR_CONCEPT_EXIST        = "error.conceptExist";
    public static final String ERR_EMPTY_NAME           = "error.emptyName";

    public static final String ERR_STORAGE_PROCEDURE_EXECUTION_EXCEPTION  = "error.storageProcedureExecutionException";
    public static final String ERR_TEST_CASE_ON_PURPOSE_EXCEPTION         = "error.testCaseOnPurposeException";
    public static final String ERR_STORAGE_PROCEDURE_BUSINESS_ERROR       = "error.storageProcedureBusinessError";
    public static final String ERR_SAVE_BREAKDOWN_VALIDATION_ERROR        = "error.saveBreakdownValidationError";
    public static final String ERR_EDIT_BREAKDOWN_ERROR                   = "error.editBreakdownError";
    public static final String ERR_RECOVER_BREAKDOWN_ERROR                = "error.recover";
    public static final String ERR_IMPRESION                              = "error.impresion";
    public static final String ERR_INSUFFICIENT_PERMISSION_BUSINESS_ERROR = "error.insufficientPermissionBusinessError";
    public static final String ERR_SQL_ERROR                              = "error.sqlError";
    public static final String ERR_LOGIN_REPLACEMENT                      = "error.userReplacement";
    public static final String ERR_FORBIDDEN_USER_ALREADY_LOGGED          = "login.messages.error.userAlreadyLogged";
    public static final String ERR_FORBIDDEN_AUTHENTICATION               = "login.messages.error.authentication";
    public static final String ERR_FORBIDDEN_USER_NOT_LOGGED_IN           = "error.forbiddenUserNotLoggedIn";

    public static final String PROBLEM_BASE_URL             = "https://www.tutorial.desglose/problem";
    public static final URI    DEFAULT_TYPE                 = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI    CONSTRAINT_VIOLATION_TYPE    = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI    PARAMETERIZED_TYPE           = URI.create(PROBLEM_BASE_URL + "/parameterized");
    public static final URI    ENTITY_NOT_FOUND_TYPE        = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
    public static final URI    INVALID_PASSWORD_TYPE        = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI    EMAIL_ALREADY_USED_TYPE      = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI    LOGIN_ALREADY_USED_TYPE      = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI    EMAIL_NOT_FOUND_TYPE         = URI.create(PROBLEM_BASE_URL + "/email-not-found");
    public static final URI    INVALID_RUT_TYPE             = URI.create(PROBLEM_BASE_URL + "/invalid-rut");
    public static final URI    INVALID_IMPRESION_TYPE             = URI.create(PROBLEM_BASE_URL + "/invalid-rut");
    public static final URI    LOGIN_REPLACEMENT_EXCEPTION  = URI.create(PROBLEM_BASE_URL + "/login-reemplazo-error");
    public static final URI    SAVE_BREAKDOWN_ERROR_TYPE    = URI.create(PROBLEM_BASE_URL + "/save-breakdown-error");
    public static final URI    RECOVER_BREAKDOWN_ERROR_TYPE = URI.create(PROBLEM_BASE_URL + "/recover-breakdown-error");
    public static final URI    EMPTY_NAME_TYPE              = URI.create(PROBLEM_BASE_URL + "/empty-name");
    public static final URI    RUT_DOES_NOT_EXIST_TYPE      = URI.create(PROBLEM_BASE_URL + "/invalid-rut");
    public static final URI    CONCEPT_NOT_EXIST_TYPE       = URI.create(PROBLEM_BASE_URL + "/invalid-concept");
    public static final URI    NO_ECONOMIC_ACTIVITY_TYPE    = URI.create(PROBLEM_BASE_URL + "/no-economic-activity");
    public static final URI    CANT_EDIT_BREAKDOWN_TYPE     = URI.create(PROBLEM_BASE_URL + "/cant-edit-breakdown");

    public static final URI STORAGE_PROCEDURE_EXECUTION_EXCEPTION  = URI.create(PROBLEM_BASE_URL + "/sp-execution-error");
    public static final URI TEST_CASE_ON_PURPOSE_EXCEPTION         = URI.create(PROBLEM_BASE_URL + "/no-error");
    public static final URI STORAGE_PROCEDURE_BUSINESS_ERROR       = URI.create(PROBLEM_BASE_URL + "/sp-business-error");
    public static final URI INSUFFICIENT_PERMISSION_BUSINESS_ERROR = URI.create(PROBLEM_BASE_URL + "/no-permission-business-error");

    private ErrorConstants() {
    }
}
