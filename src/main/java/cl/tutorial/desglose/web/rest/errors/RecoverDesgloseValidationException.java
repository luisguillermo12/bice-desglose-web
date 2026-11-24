package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class RecoverDesgloseValidationException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public RecoverDesgloseValidationException(String detail) {
        super(ErrorConstants.RECOVER_BREAKDOWN_ERROR_TYPE, "A validation error has occurred recovering the desglose.", Status.BAD_REQUEST, detail);
    }
}
