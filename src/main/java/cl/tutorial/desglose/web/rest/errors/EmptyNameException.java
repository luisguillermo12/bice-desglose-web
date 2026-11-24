package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EmptyNameException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public EmptyNameException() {
        super(ErrorConstants.EMPTY_NAME_TYPE, "The name you provide is empty.", Status.BAD_REQUEST);
    }
}
