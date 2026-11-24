package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A DTO for the BreakdownConceptValidationRequestDTO.
 */
public class BreakdownConceptValidationRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public ExtraDataDTO extraDataDTO;
    @NotNull
    public Integer      activePeriodsIndexes[];
    @NotNull
    public Boolean      skipNotRequiredValidations;

    @Override
    public String toString() {
        return "BreakdownConceptValidationRequestDTO{" + "extraDataDTO=" + extraDataDTO + ", activePeriodsIndexes=" + Arrays.toString(activePeriodsIndexes)
            + ", skipNotRequiredValidations=" + skipNotRequiredValidations + '}';
    }
}
