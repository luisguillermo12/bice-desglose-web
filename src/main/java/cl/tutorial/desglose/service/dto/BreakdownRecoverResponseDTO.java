package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A DTO for the BreakdownRecoverResponseDTO.
 */
public class BreakdownRecoverResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public ExtraDataDTO extraDataDTO;

    @NotNull
    public String codCliente;

    @NotNull
    public String codTipoDes;

    @NotNull
    public String codSubtipoDes;

    @NotNull
    public String codMoneda;

    @NotNull
    public String codPlantilla;

    @NotNull
    public String codUnidMoneda;

    public String glsSubtipoDes;

    public String codEstado;

    public String glsEstado;

    public String userModif;

    public LocalDateTime fecUserModif;

    public String userDef;

    public LocalDateTime fecUserDef;
}
