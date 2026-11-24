package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class DesgloseReemplazoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codCargo;

    public String getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseReemplazoPK))
            return false;
        DesgloseReemplazoPK that = (DesgloseReemplazoPK) o;
        return Objects.equals(codCargo, that.codCargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCargo);
    }

    @Override
    public String toString() {
        return "DesgloseReemplazoPK{" + "codCargo='" + codCargo + '\'' + '}';
    }
}
