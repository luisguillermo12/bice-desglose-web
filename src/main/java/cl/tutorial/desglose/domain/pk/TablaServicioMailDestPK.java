package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class TablaServicioMailDestPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codSistema;
    private Long numSolicitud;
    private String glsToMail;
    private String codTipoDest;

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

    public String getGlsToMail() {
        return glsToMail;
    }

    public void setGlsToMail(String glsToMail) {
        this.glsToMail = glsToMail;
    }

    public String getCodTipoDest() {
        return codTipoDest;
    }

    public void setCodTipoDest(String codTipoDest) {
        this.codTipoDest = codTipoDest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TablaServicioMailDestPK))
            return false;
        TablaServicioMailDestPK that = (TablaServicioMailDestPK) o;
        return Objects.equals(getCodSistema(), that.getCodSistema()) && Objects.equals(getNumSolicitud(), that.getNumSolicitud()) && Objects
            .equals(getGlsToMail(), that.getGlsToMail()) && Objects.equals(getCodTipoDest(), that.getCodTipoDest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodSistema(), getNumSolicitud(), getGlsToMail(), getCodTipoDest());
    }

    @Override
    public String toString() {
        return "TablaServicioMailDestPK{" + "codSistema='" + codSistema + '\'' + ", numSolicitud=" + numSolicitud + ", glsToMail='" + glsToMail + '\'' + ", codTipoDest='"
            + codTipoDest + '\'' + '}';
    }
}
