package cl.tutorial.desglose.service.dto;

import java.time.LocalDateTime;

public class CorreoDTO {

    private LocalDateTime fecSolicitud;

    private Long numSolicitud;

    private String glsFromNombre;

    private String glsFromMail;

    private String glsToNombre;

    private String glsToMail;

    private String glsAsunto;

    public CorreoDTO(LocalDateTime fecSolicitud, Long numSolicitud, String glsFromNombre, String glsFromMail, String glsToNombre, String glsToMail, String glsAsunto) {
        this.fecSolicitud = fecSolicitud;
        this.numSolicitud = numSolicitud;
        this.glsFromNombre = glsFromNombre;
        this.glsFromMail = glsFromMail;
        this.glsToNombre = glsToNombre;
        this.glsToMail = glsToMail;
        this.glsAsunto = glsAsunto;
    }

    public LocalDateTime getFecSolicitud() {
        return fecSolicitud;
    }

    public void setFecSolicitud(LocalDateTime fecSolicitud) {
        this.fecSolicitud = fecSolicitud;
    }

    public Long getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(Long numSolicitud) {
        this.numSolicitud = numSolicitud;
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

    public String getGlsAsunto() {
        return glsAsunto;
    }

    public void setGlsAsunto(String glsAsunto) {
        this.glsAsunto = glsAsunto;
    }

    @Override
    public String toString() {
        return "CorreoDTO{" + "fecSolicitud=" + fecSolicitud + ", numSolicitud=" + numSolicitud + ", glsFromNombre='" + glsFromNombre + '\'' + ", glsFromMail='" + glsFromMail
            + '\'' + ", glsToNombre='" + glsToNombre + '\'' + ", glsToMail='" + glsToMail + '\'' + ", glsAsunto='" + glsAsunto + '\'' + '}';
    }
}
