package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the DesgloseRevision entity.
 */
public class DesgloseRevisionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codCliente;

    private Integer codTipoDes;

    private Integer codSubtipoDes;

    private String codMoneda;

    private Integer codPlantilla;

    private String rolDigitador;

    private String rolRevisor;

    private String rolRevisorReem;

    private String glsNotificacion;

    private Boolean flgNotificado;

    private ZonedDateTime fecEnvioRevision;

    private LocalDate fecEnRevision;

    private LocalDate fecTerminoRevision;

    private Integer codRegStatus;

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getRolDigitador() {
        return rolDigitador;
    }

    public void setRolDigitador(String rolDigitador) {
        this.rolDigitador = rolDigitador;
    }

    public String getRolRevisor() {
        return rolRevisor;
    }

    public void setRolRevisor(String rolRevisor) {
        this.rolRevisor = rolRevisor;
    }

    public String getRolRevisorReem() {
        return rolRevisorReem;
    }

    public void setRolRevisorReem(String rolRevisorReem) {
        this.rolRevisorReem = rolRevisorReem;
    }

    public String getGlsNotificacion() {
        return glsNotificacion;
    }

    public void setGlsNotificacion(String glsNotificacion) {
        this.glsNotificacion = glsNotificacion;
    }

    public Boolean isFlgNotificado() {
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

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public Boolean getFlgNotificado() {
        return flgNotificado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseRevisionDTO))
            return false;
        DesgloseRevisionDTO that = (DesgloseRevisionDTO) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codPlantilla, that.codPlantilla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla);
    }

    @Override
    public String toString() {
        return "DesgloseRevisionDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", rolDigitador='" + rolDigitador + '\'' + ", rolRevisor='" + rolRevisor + '\'' + ", rolRevisorReem='" + rolRevisorReem + '\''
            + ", glsNotificacion='" + glsNotificacion + '\'' + ", flgNotificado=" + flgNotificado + ", fecEnvioRevision=" + fecEnvioRevision + ", fecEnRevision=" + fecEnRevision
            + ", fecTerminoRevision=" + fecTerminoRevision + ", codRegStatus=" + codRegStatus + '}';
    }
}
