package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class DesgloseFirmaPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codCargo;
    private String numRolEmpleado;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseFirmaPK))
            return false;
        DesgloseFirmaPK that = (DesgloseFirmaPK) o;
        return Objects.equals(codCargo, that.codCargo) && Objects.equals(numRolEmpleado, that.numRolEmpleado) && Objects.equals(numRolEmpleado2fir, that.numRolEmpleado2fir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCargo, numRolEmpleado, numRolEmpleado2fir);
    }

    @Override
    public String toString() {
        return "DesgloseFirmaPK{" + "codCargo='" + codCargo + '\'' + ", numRolEmpleado='" + numRolEmpleado + '\'' + ", numRolEmpleado2fir='" + numRolEmpleado2fir + '\'' + '}';
    }
}
