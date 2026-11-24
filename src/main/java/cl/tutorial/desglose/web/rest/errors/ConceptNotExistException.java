package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ConceptNotExistException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ConceptNotExistException() {
        super(ErrorConstants.CONCEPT_NOT_EXIST_TYPE, "The concept Padre you provide does not exist.", Status.BAD_REQUEST);
    }
}
