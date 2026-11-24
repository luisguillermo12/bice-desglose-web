package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the DesgloseGrupo entity.
 */
public class DesgloseGrupoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCargo;

    @NotNull
    private String numRolEmpleado;

    private String glsNumRolEmpleado;

    @NotNull
    private String desCodCargo;

    @NotNull
    private String desNumRolEmpleado;

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

    public String getDesCodCargo() {
        return desCodCargo;
    }

    public void setDesCodCargo(String desCodCargo) {
        this.desCodCargo = desCodCargo;
    }

    public String getDesNumRolEmpleado() {
        return desNumRolEmpleado;
    }

    public void setDesNumRolEmpleado(String desNumRolEmpleado) {
        this.desNumRolEmpleado = desNumRolEmpleado;
    }

    public String getGlsNumRolEmpleado() {
        return glsNumRolEmpleado;
    }

    public void setGlsNumRolEmpleado(String glsNumRolEmpleado) {
        this.glsNumRolEmpleado = glsNumRolEmpleado;
    }

    @Override
    public String toString() {
        return "DesgloseGrupoDTO{" + "codCargo='" + codCargo + '\'' + ", numRolEmpleado='" + numRolEmpleado + '\'' + ", glsNumRolEmpleado='" + glsNumRolEmpleado + '\''
            + ", desCodCargo='" + desCodCargo + '\'' + ", desNumRolEmpleado='" + desNumRolEmpleado + '\'' + '}';
    }
}
