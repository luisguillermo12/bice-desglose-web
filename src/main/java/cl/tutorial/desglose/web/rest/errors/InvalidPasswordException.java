package cl.tutorial.desglose.web.rest.errors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidPasswordException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private final Logger log = LogManager.getLogger(TestCaseOnPurposeException.class);
    public InvalidPasswordException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
        log.error(ErrorConstants.INVALID_PASSWORD_TYPE);
    }
}
