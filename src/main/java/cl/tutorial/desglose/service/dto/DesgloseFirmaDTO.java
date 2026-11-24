package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the DesgloseFirma entity.
 */
public class DesgloseFirmaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCargo;

    @NotNull
    private String numRolEmpleado;

    @NotNull
    private String numRolEmpleado2fir;

    public String getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public String getNumRolEmpleado2fir() {
        return numRolEmpleado2fir;
    }

    public void setNumRolEmpleado2fir(String numRolEmpleado2fir) {
        this.numRolEmpleado2fir = numRolEmpleado2fir;
    }

    @Override
    public String toString() {
        return "DesgloseFirmaDTO{" + "codCargo='" + getCodCargo() + "'" + ", numRolEmpleado='" + getNumRolEmpleado() + "'" + ", numRolEmpleado2fir='" + getNumRolEmpleado2fir()
            + "'" + "}";
    }
}
