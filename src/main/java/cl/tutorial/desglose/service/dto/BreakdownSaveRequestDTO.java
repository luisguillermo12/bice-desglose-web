package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A DTO for the BreakdownSaveRequestDTO.
 */
public class BreakdownSaveRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public DesgloseDTO  desgloseDTO;
    @NotNull
    public ExtraDataDTO extraDataDTO;
    @NotNull
    public Integer      isIndicePeriodos[];

    public Integer      activePeriodsEdit[];

    @NotNull
    public Boolean      skipNotRequiredValidations;

    public Boolean      isRapid;

    @Override
    public String toString() {
        return "BreakdownSaveRequestDTO{" + "desgloseDTO=" + desgloseDTO + ", extraDataDTO=" + extraDataDTO + ", isIndicePeriodos=" + Arrays.toString(isIndicePeriodos)
            + ", activePeriodsEdit=" + Arrays.toString(activePeriodsEdit) + ", skipNotRequiredValidations=" + skipNotRequiredValidations + ", isRapid=" + isRapid + '}';
    }
}
