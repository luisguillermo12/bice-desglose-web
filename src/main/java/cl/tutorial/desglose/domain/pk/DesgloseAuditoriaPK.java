package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class DesgloseAuditoriaPK extends NewCommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime fecEvento;
    private Integer       numLinea;

    public LocalDateTime getFecEvento() {
        return fecEvento;
    }

    public void setFecEvento(LocalDateTime fecEvento) {
        this.fecEvento = fecEvento;
    }

    public Integer getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(Integer numLinea) {
        this.numLinea = numLinea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseAuditoriaPK))
            return false;
        if (!super.equals(o))
            return false;
        DesgloseAuditoriaPK that = (DesgloseAuditoriaPK) o;
        return Objects.equals(fecEvento, that.fecEvento) && Objects.equals(numLinea, that.numLinea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fecEvento, numLinea);
    }

    @Override
    public String toString() {
        return "DesgloseAuditoriaPK{" + "fecEvento=" + fecEvento + ", numLinea=" + numLinea + ", codPlantilla=" + codPlantilla + ", codCliente='" + codCliente + '\''
            + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + '}';
    }

}
