package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the DesgloseReemplazo entity.
 */
public class DesgloseReemplazoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codCargo;

    private String numRolEmpleado;

    private String desCodCargo;

    private String desNumRolEmpleado;

    private LocalDate fecReemplazo;

    private LocalDate fecElimReemplazo;

    private String numRolEmpleadoCrea;

    private String numRolEmpleadoElim;

    private Integer codRegStatus;

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

    public LocalDate getFecReemplazo() {
        return fecReemplazo;
    }

    public void setFecReemplazo(LocalDate fecReemplazo) {
        this.fecReemplazo = fecReemplazo;
    }

    public LocalDate getFecElimReemplazo() {
        return fecElimReemplazo;
    }

    public void setFecElimReemplazo(LocalDate fecElimReemplazo) {
        this.fecElimReemplazo = fecElimReemplazo;
    }

    public String getNumRolEmpleadoCrea() {
        return numRolEmpleadoCrea;
    }

    public void setNumRolEmpleadoCrea(String numRolEmpleadoCrea) {
        this.numRolEmpleadoCrea = numRolEmpleadoCrea;
    }

    public String getNumRolEmpleadoElim() {
        return numRolEmpleadoElim;
    }

    public void setNumRolEmpleadoElim(String numRolEmpleadoElim) {
        this.numRolEmpleadoElim = numRolEmpleadoElim;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    @Override
    public String toString() {
        return "DesgloseReemplazoDTO{" + "codCargo='" + getCodCargo() + "'" + ", numRolEmpleado='" + getNumRolEmpleado() + "'" + ", desCodCargo='" + getDesCodCargo() + "'"
            + ", desNumRolEmpleado='" + getDesNumRolEmpleado() + "'" + ", fecReemplazo='" + getFecReemplazo() + "'" + ", fecElimReemplazo='" + getFecElimReemplazo() + "'"
            + ", numRolEmpleadoCrea='" + getNumRolEmpleadoCrea() + "'" + ", numRolEmpleadoElim='" + getNumRolEmpleadoElim() + "'" + ", codRegStatus=" + getCodRegStatus() + "}";
    }
}
