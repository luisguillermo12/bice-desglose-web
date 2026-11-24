package cl.tutorial.desglose.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TablaServicioMail entity.
 */
public class TablaServicioMailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codSistema;

    @NotNull
    private Long numSolicitud;

    private LocalDate fecSolicitud;

    private Long numTrxSistema;

    private String glsFromNombre;

    private String glsFromMail;

    private String glsAsunto;

    private Long glsMensaje;

    private LocalDate fecEnvMail;

    private Long numIntento;

    private Long numMaxReintento;

    private String codEstado;

    private String glsDetRespuesta;

    private String glsContentType;


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

    public LocalDate getFecSolicitud() {
        return fecSolicitud;
    }

    public void setFecSolicitud(LocalDate fecSolicitud) {
        this.fecSolicitud = fecSolicitud;
    }

    public Long getNumTrxSistema() {
        return numTrxSistema;
    }

    public void setNumTrxSistema(Long numTrxSistema) {
        this.numTrxSistema = numTrxSistema;
    }

    public String getGlsFromNombre() {
        return glsFromNombre;
    }

    public void setGlsFromNombre(String glsFromNombre) {
        this.glsFromNombre = glsFromNombre;
    }

    public String getGlsFromMail() {
        return glsFromMail;
    }

    public void setGlsFromMail(String glsFromMail) {
        this.glsFromMail = glsFromMail;
    }

    public String getGlsAsunto() {
        return glsAsunto;
    }

    public void setGlsAsunto(String glsAsunto) {
        this.glsAsunto = glsAsunto;
    }

    public Long getGlsMensaje() {
        return glsMensaje;
    }

    public void setGlsMensaje(Long glsMensaje) {
        this.glsMensaje = glsMensaje;
    }

    public LocalDate getFecEnvMail() {
        return fecEnvMail;
    }

    public void setFecEnvMail(LocalDate fecEnvMail) {
        this.fecEnvMail = fecEnvMail;
    }

    public Long getNumIntento() {
        return numIntento;
    }

    public void setNumIntento(Long numIntento) {
        this.numIntento = numIntento;
    }

    public Long getNumMaxReintento() {
        return numMaxReintento;
    }

    public void setNumMaxReintento(Long numMaxReintento) {
        this.numMaxReintento = numMaxReintento;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getGlsDetRespuesta() {
        return glsDetRespuesta;
    }

    public void setGlsDetRespuesta(String glsDetRespuesta) {
        this.glsDetRespuesta = glsDetRespuesta;
    }

    public String getGlsContentType() {
        return glsContentType;
    }

    public void setGlsContentType(String glsContentType) {
        this.glsContentType = glsContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TablaServicioMailDTO))
            return false;
        TablaServicioMailDTO that = (TablaServicioMailDTO) o;
        return Objects.equals(getCodSistema(), that.getCodSistema()) && Objects.equals(getNumSolicitud(), that.getNumSolicitud()) && Objects
            .equals(getFecSolicitud(), that.getFecSolicitud()) && Objects.equals(getNumTrxSistema(), that.getNumTrxSistema()) && Objects
            .equals(getGlsFromNombre(), that.getGlsFromNombre()) && Objects.equals(getGlsFromMail(), that.getGlsFromMail()) && Objects.equals(getGlsAsunto(), that.getGlsAsunto())
            && Objects.equals(getGlsMensaje(), that.getGlsMensaje()) && Objects.equals(getFecEnvMail(), that.getFecEnvMail()) && Objects
            .equals(getNumIntento(), that.getNumIntento()) && Objects.equals(getNumMaxReintento(), that.getNumMaxReintento()) && Objects.equals(getCodEstado(), that.getCodEstado())
            && Objects.equals(getGlsDetRespuesta(), that.getGlsDetRespuesta()) && Objects.equals(getGlsContentType(), that.getGlsContentType());
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(getCodSistema(), getNumSolicitud(), getFecSolicitud(), getNumTrxSistema(), getGlsFromNombre(), getGlsFromMail(), getGlsAsunto(), getGlsMensaje(), getFecEnvMail(),
                getNumIntento(), getNumMaxReintento(), getCodEstado(), getGlsDetRespuesta(), getGlsContentType());
    }

    @Override
    public String toString() {
        return "TablaServicioMailDTO{" + "codSistema='" + codSistema + '\'' + ", numSolicitud=" + numSolicitud + ", fecSolicitud=" + fecSolicitud + ", numTrxSistema="
            + numTrxSistema + ", glsFromNombre='" + glsFromNombre + '\'' + ", glsFromMail='" + glsFromMail + '\'' + ", glsAsunto='" + glsAsunto + '\'' + ", glsMensaje="
            + glsMensaje + ", fecEnvMail=" + fecEnvMail + ", numIntento=" + numIntento + ", numMaxReintento=" + numMaxReintento + ", codEstado='" + codEstado + '\''
            + ", glsDetRespuesta='" + glsDetRespuesta + '\'' + ", glsContentType='" + glsContentType + '\'' + '}';
    }
}
