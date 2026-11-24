package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class SaveDesgloseValidationException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public SaveDesgloseValidationException(String detail) {
        super(ErrorConstants.SAVE_BREAKDOWN_ERROR_TYPE, "A validation error has occurred saving the desglose.", Status.BAD_REQUEST, detail);
    }
}
