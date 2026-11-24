package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class DesgloseGrupoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codCargo;
    private String numRolEmpleado;
    private String desCodCargo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseGrupoPK))
            return false;
        DesgloseGrupoPK that = (DesgloseGrupoPK) o;
        return Objects.equals(codCargo, that.codCargo) && Objects.equals(numRolEmpleado, that.numRolEmpleado) && Objects.equals(desCodCargo, that.desCodCargo) && Objects
            .equals(desNumRolEmpleado, that.desNumRolEmpleado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCargo, numRolEmpleado, desCodCargo, desNumRolEmpleado);
    }

    @Override
    public String toString() {
        return "DesgloseGrupoPK{" + "codCargo='" + codCargo + '\'' + ", numRolEmpleado='" + numRolEmpleado + '\'' + ", desCodCargo='" + desCodCargo + '\'' + ", desNumRolEmpleado='"
            + desNumRolEmpleado + '\'' + '}';
    }
}
