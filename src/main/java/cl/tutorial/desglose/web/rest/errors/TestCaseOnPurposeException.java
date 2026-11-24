package cl.tutorial.desglose.web.rest.errors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class TestCaseOnPurposeException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private final Logger log = LogManager.getLogger(TestCaseOnPurposeException.class);
    public TestCaseOnPurposeException() {
        super(ErrorConstants.TEST_CASE_ON_PURPOSE_EXCEPTION, "This is not an error, is a test case for debugging.", Status.INTERNAL_SERVER_ERROR);
        log.error("This is not an error, is a test case for debugging.");
    }
}
