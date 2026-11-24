package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the BreakdownSaveRequestDTO.
 */
public class BreakdownCloneRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public String codCliente;

    @NotNull
    public String codTipoDesOriginal;

    @NotNull
    public String codSubtipoDesOriginal;

    @NotNull
    public String codMonedaOriginal;

    @NotNull
    public String codPlantillaDesgloseOriginal;

    @NotNull
    public String codTipoDes;

    @NotNull
    public String codSubtipoDes;

    @NotNull
    public String codMoneda;

    @Override
    public String toString() {
        return "BreakdownCloneRequestDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDesOriginal='" + codTipoDesOriginal + '\'' + ", codSubtipoDesOriginal='"
            + codSubtipoDesOriginal + '\'' + ", codMonedaOriginal='" + codMonedaOriginal + '\'' + ", codPlantillaDesgloseOriginal='" + codPlantillaDesgloseOriginal + '\''
            + ", codTipoDes='" + codTipoDes + '\'' + ", codSubtipoDes='" + codSubtipoDes + '\'' + ", codMoneda='" + codMoneda + '\'' + '}';
    }
}
