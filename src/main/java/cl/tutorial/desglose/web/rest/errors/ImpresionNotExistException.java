package cl.tutorial.desglose.web.rest.errors;

import cl.tutorial.desglose.web.rest.errors.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ImpresionNotExistException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ImpresionNotExistException() {
        super(ErrorConstants.INVALID_IMPRESION_TYPE, "El desglose no existe para imprimir", Status.BAD_REQUEST);
    }
}
