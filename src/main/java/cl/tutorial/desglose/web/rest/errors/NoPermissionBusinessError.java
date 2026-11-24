package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NoPermissionBusinessError extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NoPermissionBusinessError(String detail) {
        super(ErrorConstants.INSUFFICIENT_PERMISSION_BUSINESS_ERROR, "The user has no permission to execute this action", Status.INTERNAL_SERVER_ERROR, /*Status.UNAUTHORIZED,*/
            detail);
    }
}
