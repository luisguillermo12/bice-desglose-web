package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class StorageProcedureBusinessError extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public StorageProcedureBusinessError(String detail) {
        super(ErrorConstants.STORAGE_PROCEDURE_BUSINESS_ERROR, "A business error has occurred in a stored procedure.", Status.EXPECTATION_FAILED, detail);
    }
}
