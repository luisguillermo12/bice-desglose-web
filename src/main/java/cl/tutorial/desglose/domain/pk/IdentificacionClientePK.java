package cl.tutorial.desglose.domain.pk;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;

import java.io.Serializable;
import java.util.Objects;

public class IdentificacionClientePK extends CommonClientePK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer codTipoIden;

    public Integer getCodTipoIden() {
        return codTipoIden;
    }

    public void setCodTipoIden(Integer codTipoIden) {
        this.codTipoIden = codTipoIden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof IdentificacionClientePK))
            return false;
        if (!super.equals(o))
            return false;
        IdentificacionClientePK that = (IdentificacionClientePK) o;
        return Objects.equals(codTipoIden, that.codTipoIden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), codTipoIden);
    }

    @Override
    public String toString() {
        return "IdentificacionClientePK{" + "codTipoIden=" + codTipoIden + ", numCliente=" + numCliente + ", codRegLog=" + codRegLog + '}';
    }
}
