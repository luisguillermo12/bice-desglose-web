package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class TablaServicioMailPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codSistema;

    private Long numSolicitud;

    public String getCodSistema() {
        return codSistema;
    }

    public void setCodSistema(String codSistema) {
        this.codSistema = codSistema;
    }

    public Long getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(Long numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TablaServicioMailPK))
            return false;
        TablaServicioMailPK that = (TablaServicioMailPK) o;
        return Objects.equals(getCodSistema(), that.getCodSistema()) && Objects.equals(getNumSolicitud(), that.getNumSolicitud());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodSistema(), getNumSolicitud());
    }

    @Override
    public String toString() {
        return "TablaServicioMailPK{" + "codSistema='" + codSistema + '\'' + ", numSolicitud=" + numSolicitud + '}';
    }
}
