package cl.tutorial.desglose.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ConceptExistException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ConceptExistException() {
        super(ErrorConstants.CONCEPT_NOT_EXIST_TYPE, "The concept  you provide does  exist.", Status.BAD_REQUEST);
    }
}
