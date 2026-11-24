package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NoEconomicActivityException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NoEconomicActivityException() {
        super(ErrorConstants.NO_ECONOMIC_ACTIVITY_TYPE, "Client without defined commercial activity.", Status.INTERNAL_SERVER_ERROR);
    }
}
