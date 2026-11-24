package cl.tutorial.desglose.web.rest.errors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class StorageProcedureExecutionException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private final Logger log = LogManager.getLogger(StorageProcedureExecutionException.class);
    public StorageProcedureExecutionException() {
        super(ErrorConstants.STORAGE_PROCEDURE_EXECUTION_EXCEPTION, "An error has occurred executing an stored procedure.", Status.INTERNAL_SERVER_ERROR);
        log.error(ErrorConstants.STORAGE_PROCEDURE_EXECUTION_EXCEPTION);
    }
}
