package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class CantEditBreakdownException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public CantEditBreakdownException(String detail) {
        super(ErrorConstants.CANT_EDIT_BREAKDOWN_TYPE, "It's not possible to edit the breakdown.", Status.INTERNAL_SERVER_ERROR, detail);
    }
}
