package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class RutDoesNotExistException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public RutDoesNotExistException() {
        super(ErrorConstants.RUT_DOES_NOT_EXIST_TYPE, "The rut you provide does not exist.", Status.BAD_REQUEST);
    }
}
