package cl.tutorial.desglose.web.rest.errors;

import cl.tutorial.desglose.security.UserAlreadyLoggedInException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.zalando.problem.Status.FORBIDDEN;
import static org.zalando.problem.Status.UNAUTHORIZED;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
            .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                .with("violations", ((ConstraintViolationProblem) problem).getViolations())
                .with("message", ErrorConstants.ERR_VALIDATION);
        } else {
            builder
                .withCause(((DefaultProblem) problem).getCause())
                .withDetail(problem.getDetail())
                .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
                builder.with("message", "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
            .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode()))
            .collect(Collectors.toList());

        Problem problem = Problem.builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Method argument not valid")
            .withStatus(defaultConstraintViolationStatus())
            .with("message", ErrorConstants.ERR_VALIDATION)
            .with("fieldErrors", fieldErrors)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.NOT_FOUND)
            .with("message", ErrorConstants.ENTITY_NOT_FOUND_TYPE)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.CONFLICT)
            .with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomInvalidRutException(InvalidRutException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_INVALID_RUT)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomNoEconomicActivityException(NoEconomicActivityException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_NO_ECONOMIC_ACTIVITY)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomRutDoesNotExistException(RutDoesNotExistException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_RUT_DOES_NOT_EXIST)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomConceptNotExistException(ConceptNotExistException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_CONCEPT_NOT_EXIST)
            .build();
        return create(ex, problem, request);
    }
    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomConceptNotExistException(ConceptExistException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_CONCEPT_EXIST)
            .build();
        return create(ex, problem, request);
    }
    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomImpresionException(ImpresionNotExistException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_IMPRESION)
            .build();
        return create(ex, problem, request);
    }
    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomEmptyNameException(EmptyNameException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_EMPTY_NAME)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomStorageProcedureExecutionException(StorageProcedureExecutionException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with("message", ErrorConstants.ERR_STORAGE_PROCEDURE_EXECUTION_EXCEPTION)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomTestCaseOnPurposeException(TestCaseOnPurposeException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with("message", ErrorConstants.ERR_TEST_CASE_ON_PURPOSE_EXCEPTION)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomStorageProcedureBusinessError(StorageProcedureBusinessError ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with("message", ErrorConstants.ERR_STORAGE_PROCEDURE_BUSINESS_ERROR)
            .with("params","{\"errorDetail\": \"" + ex.getDetail() + "\"}" ) // JSON con el detalle desde BD del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomSaveDesgloseException(SaveDesgloseValidationException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_SAVE_BREAKDOWN_VALIDATION_ERROR)
            .with("params","{\"errorDetail\": \"" + ex.getDetail() + "\"}" ) // JSON con el detalle del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCantEditBreakdownException(CantEditBreakdownException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with("message", ErrorConstants.ERR_EDIT_BREAKDOWN_ERROR)
            .with("params","{\"errorDetail\": \"" + ex.getDetail() + "\"}" ) // JSON con el detalle del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleRecoverDesgloseException(RecoverDesgloseValidationException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with("message", ErrorConstants.ERR_RECOVER_BREAKDOWN_ERROR)
            .with("params","{\"errorDetail\": \"" + ex.getDetail() + "\"}" ) // JSON con el detalle del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomTestCaseOnPurposeException(NoPermissionBusinessError ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            // .withStatus(Status.UNAUTHORIZED)
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with("message", ErrorConstants.ERR_INSUFFICIENT_PERMISSION_BUSINESS_ERROR)
            .with("params","{\"errorDetail\": \"" + ex.getDetail() + "\"}" ) // JSON con el detalle del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleJpaSystemException(JpaSystemException ex, NativeWebRequest request) {
        String errorMsg = ex.getMessage();
        try {
            errorMsg = ex.getCause().getCause().getMessage().replaceAll("\"", "").replace("\n", "");
        } catch (Exception ex1 ) {
        }
        Problem problem = Problem.builder()
            .withStatus(Status.EXPECTATION_FAILED)
            .with("message", ErrorConstants.ERR_SQL_ERROR)
            .with("params","{\"errorDetail\": \"" + errorMsg + "\"}" ) // JSON con el detalle del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomLoginReemplazoException(LoginReplacementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with("message", ErrorConstants.ERR_LOGIN_REPLACEMENT)
            .with("params", ex.getReplacementUser())
            .build();
        return create(ex, problem, request);
    }

    /*
    @ExceptionHandler
    public ResponseEntity<Problem> handleAccessDenied(final AccessDeniedException e, final NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.FORBIDDEN)
            .with("message", ErrorConstants.ERR_FORBIDDEN_USER_ALREADY_LOGGED)
            .with("params", e.getMessage())
            .build();
        return create(e, problem, request);
    }*/

    @ExceptionHandler
    public ResponseEntity<Problem> handleCustomUserAlreadyLoggedInException(UserAlreadyLoggedInException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.UNAUTHORIZED)
            .with("message", ErrorConstants.ERR_FORBIDDEN_USER_ALREADY_LOGGED)
            .with("params","{\"loginDate\": \"" + ex.getLoginDate() + "\", \"errorDetail\": \"" + ex.getMessage() + "\""  + " }" ) // JSON con el detalle del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleAuthentication(final AuthenticationException ex, final NativeWebRequest request) {
        String loginDate = "";
        String errForbiddenUserAlreadyLogged = ErrorConstants.ERR_FORBIDDEN_AUTHENTICATION;
        if (ex.getCause() instanceof UserAlreadyLoggedInException) {
            loginDate = ((UserAlreadyLoggedInException)ex.getCause()).getLoginDate();
            errForbiddenUserAlreadyLogged = ErrorConstants.ERR_FORBIDDEN_USER_ALREADY_LOGGED;
        }
        Problem problem = Problem.builder()
            .withStatus(Status.UNAUTHORIZED)
            .with("message", errForbiddenUserAlreadyLogged)
            .with("params","{\"loginDate\": \"" + loginDate + "\", \"errorDetail\": \"" + ex.getMessage() + "\""  + " }" ) // JSON con el detalle del error.
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInsufficientAuthenticationException(final InsufficientAuthenticationException ex, final NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.UNAUTHORIZED)
            .with("message", ErrorConstants.ERR_FORBIDDEN_USER_NOT_LOGGED_IN)
            .build();
        return create(ex, problem, request);
    }
}
