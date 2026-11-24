package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DesgloseRevisionPK extends NewCommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String        glsNotificacion;
    private Boolean       flgNotificado;
    private ZonedDateTime fecEnvioRevision;
    private LocalDate     fecEnRevision;
    private LocalDate     fecTerminoRevision;
    private Integer       codRegStatus;

    public DesgloseRevisionPK() {
    }

    private DesgloseRevisionPK(NewCommonPK commonPK) {
        this.codCliente = commonPK.codCliente;
        this.codTipoDes = commonPK.codTipoDes;
        this.codSubtipoDes = commonPK.codSubtipoDes;
        this.codMoneda = commonPK.codMoneda;
        this.codPlantilla = commonPK.codPlantilla;
    }

    public static DesgloseRevisionPK newInstance(NewCommonPK commonPK) {
        return new DesgloseRevisionPK(commonPK);
    }

    public String getGlsNotificacion() {
        return glsNotificacion;
    }

    public void setGlsNotificacion(String glsNotificacion) {
        this.glsNotificacion = glsNotificacion;
    }

    public Boolean getFlgNotificado() {
        return flgNotificado;
    }

    public void setFlgNotificado(Boolean flgNotificado) {
        this.flgNotificado = flgNotificado;
    }

    public ZonedDateTime getFecEnvioRevision() {
        return fecEnvioRevision;
    }

    public void setFecEnvioRevision(ZonedDateTime fecEnvioRevision) {
        this.fecEnvioRevision = fecEnvioRevision;
    }

    public LocalDate getFecEnRevision() {
        return fecEnRevision;
    }

    public void setFecEnRevision(LocalDate fecEnRevision) {
        this.fecEnRevision = fecEnRevision;
    }

    public LocalDate getFecTerminoRevision() {
        return fecTerminoRevision;
    }

    public void setFecTerminoRevision(LocalDate fecTerminoRevision) {
        this.fecTerminoRevision = fecTerminoRevision;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }
}
