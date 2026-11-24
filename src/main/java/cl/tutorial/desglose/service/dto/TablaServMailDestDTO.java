package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TablaServMailDest entity.
 */
public class TablaServMailDestDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @NotNull
    private String codSistema;

    @NotNull
    private Long numSolicitud;

    @NotNull
    private String glsToNombre;

    @NotNull
    private String glsToMail;

    @NotNull
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

    public String getGlsToNombre() {
        return glsToNombre;
    }

    public void setGlsToNombre(String glsToNombre) {
        this.glsToNombre = glsToNombre;
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
        if (!(o instanceof TablaServMailDestDTO))
            return false;
        TablaServMailDestDTO that = (TablaServMailDestDTO) o;
        return Objects.equals(getCodSistema(), that.getCodSistema()) && Objects.equals(getNumSolicitud(), that.getNumSolicitud()) && Objects
            .equals(getGlsToNombre(), that.getGlsToNombre()) && Objects.equals(getGlsToMail(), that.getGlsToMail()) && Objects.equals(getCodTipoDest(), that.getCodTipoDest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodSistema(), getNumSolicitud(), getGlsToNombre(), getGlsToMail(), getCodTipoDest());
    }

    @Override
    public String toString() {
        return "TablaServMailDestDTO{" + "codSistema='" + codSistema + '\'' + ", numSolicitud=" + numSolicitud + ", glsToNombre='" + glsToNombre + '\'' + ", glsToMail='"
            + glsToMail + '\'' + ", codTipoDest='" + codTipoDest + '\'' + '}';
    }
}
