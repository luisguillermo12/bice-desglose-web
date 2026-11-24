package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the BreakdownEditRequest.
 */
public class BreakdownEditRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public DesgloseDTO desglose;
    @NotNull
    public Boolean     req2Firma;
    @NotNull
    public Boolean     isNewApprobationProcess;

    @Override
    public String toString() {
        return "BreakdownEditRequestDTO{" + "desglose=" + desglose + ", req2Firma=" + req2Firma + ", isNewApprobationProcess=" + isNewApprobationProcess + '}';
    }
}
