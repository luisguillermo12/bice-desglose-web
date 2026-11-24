package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class LoginReplacementException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private String replacementUser;

    public LoginReplacementException(String replacementUser) {
        super(ErrorConstants.LOGIN_REPLACEMENT_EXCEPTION, "You are being replaced by " + replacementUser + ", therefore can not access the platform.",
            Status.INTERNAL_SERVER_ERROR);
        this.replacementUser = replacementUser;
    }

    public String getReplacementUser() {
        return replacementUser;
    }

    public void setReplacementUser(String replacementUser) {
        this.replacementUser = replacementUser;
    }
}
