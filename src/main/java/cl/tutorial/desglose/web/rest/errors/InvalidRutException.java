package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidRutException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidRutException() {
        super(ErrorConstants.INVALID_RUT_TYPE, "The rut you provide is wrong.", Status.BAD_REQUEST);
    }
}
