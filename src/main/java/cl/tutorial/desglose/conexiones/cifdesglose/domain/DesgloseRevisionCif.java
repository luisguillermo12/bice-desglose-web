package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseRevisionPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Representa la tabla \"DESGLO_REV_WK\" que contiene las notificaciones de desgloses producto de los envíos a revisión. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"DESGLO_REV_WK\" que contiene las notificaciones de desgloses producto de los envíos a revisión. Pertenece al esquema CIF_ADMIN. @author "
        + "Antonio Marrero Palomino")
@IdClass(DesgloseRevisionPK.class)
@Entity
@Table(name = "DESGLO_REV")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseRevisionCif implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Rut de cliente.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Rut de cliente.")
    @Column(name = "cod_cliente")
    private String codCliente;

    /**
     * Tipo de desglose.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de desglose.")
    @Column(name = "cod_tipo_des")
    private Integer codTipoDes;

    /**
     * Sub Tipo de desglose.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Sub Tipo de desglose.")
    @Column(name = "cod_subtipo_des")
    private Integer codSubtipoDes;

    /**
     * Código de moneda.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de moneda.")
    @Column(name = "cod_moneda")
    private String codMoneda;

    /**
     * Código del tipo de planilla. Por defecto 0 que es tipo banco.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código del tipo de planilla. Por defecto 0 que es tipo banco.", required = true)
    @Column(name = "cod_plantilla", nullable = false)
    private Integer codPlantilla;

    /**
     * Rol empleado digitador de quien envia a revisión.
     */
    @ApiModelProperty(value = "Rol empleado digitador de quien envia a revisión.")
    @Column(name = "rol_digitador")
    private String rolDigitador;

    /**
     * Rol empleado revisor que está asignado para revisión segun RELTAB.
     */
    @ApiModelProperty(value = "Rol empleado revisor que está asignado para revisión segun RELTAB.")
    @Column(name = "rol_revisor")
    private String rolRevisor;

    /**
     * Rol empleado revisor reemplazado.
     */
    @ApiModelProperty(value = "Rol empleado revisor reemplazado.")
    @Column(name = "rol_revisor_reem")
    private String rolRevisorReem;

    /**
     * Mensaje de notificación.
     */
    @Id
    @ApiModelProperty(value = "Mensaje de notificación.")
    @Column(name = "gls_notificacion")
    private String glsNotificacion;

    /**
     * Señala si se notificó al revisor.
     */
    @ApiModelProperty(value = "Señala si se notificó al revisor.")
    @Column(name = "flg_notificado")
    private Boolean flgNotificado;

    /**
     * Fecha de envío a revisión.
     */
    @Id
    @ApiModelProperty(value = "Fecha de envío a revisión.")
    @Column(name = "fec_envio_revision")
    private ZonedDateTime fecEnvioRevision;

    /**
     * Fecha cuando es tomada para revisión.
     */
    @Id
    @ApiModelProperty(value = "Fecha cuando es tomada para revisión.")
    @Column(name = "fec_en_revision")
    private LocalDate fecEnRevision;

    /**
     * Fecha cuando se termina revisión.
     */
    @Id
    @ApiModelProperty(value = "Fecha cuando se termina revisión.")
    @Column(name = "fec_termino_revision")
    private LocalDate fecTerminoRevision;

    /**
     * Código de vigencia del registro.
     */
    @Id
    @ApiModelProperty(value = "Código de vigencia del registro.")
    @Column(name = "cod_reg_status")
    private Integer codRegStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseRevisionCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseRevisionCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseRevisionCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseRevisionCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getRolDigitador() {
        return rolDigitador;
    }

    public DesgloseRevisionCif rolDigitador(String rolDigitador) {
        this.rolDigitador = rolDigitador;
        return this;
    }

    public void setRolDigitador(String rolDigitador) {
        this.rolDigitador = rolDigitador;
    }

    public String getRolRevisor() {
        return rolRevisor;
    }

    public DesgloseRevisionCif rolRevisor(String rolRevisor) {
        this.rolRevisor = rolRevisor;
        return this;
    }

    public void setRolRevisor(String rolRevisor) {
        this.rolRevisor = rolRevisor;
    }

    public String getRolRevisorReem() {
        return rolRevisorReem;
    }

    public DesgloseRevisionCif rolRevisorReem(String rolRevisorReem) {
        this.rolRevisorReem = rolRevisorReem;
        return this;
    }

    public void setRolRevisorReem(String rolRevisorReem) {
        this.rolRevisorReem = rolRevisorReem;
    }

    public String getGlsNotificacion() {
        return glsNotificacion;
    }

    public DesgloseRevisionCif glsNotificacion(String glsNotificacion) {
        this.glsNotificacion = glsNotificacion;
        return this;
    }

    public void setGlsNotificacion(String glsNotificacion) {
        this.glsNotificacion = glsNotificacion;
    }

    public Boolean isFlgNotificado() {
        return flgNotificado;
    }

    public DesgloseRevisionCif flgNotificado(Boolean flgNotificado) {
        this.flgNotificado = flgNotificado;
        return this;
    }

    public void setFlgNotificado(Boolean flgNotificado) {
        this.flgNotificado = flgNotificado;
    }

    public ZonedDateTime getFecEnvioRevision() {
        return fecEnvioRevision;
    }

    public DesgloseRevisionCif fecEnvioRevision(ZonedDateTime fecEnvioRevision) {
        this.fecEnvioRevision = fecEnvioRevision;
        return this;
    }

    public void setFecEnvioRevision(ZonedDateTime fecEnvioRevision) {
        this.fecEnvioRevision = fecEnvioRevision;
    }

    public LocalDate getFecEnRevision() {
        return fecEnRevision;
    }

    public DesgloseRevisionCif fecEnRevision(LocalDate fecEnRevision) {
        this.fecEnRevision = fecEnRevision;
        return this;
    }

    public void setFecEnRevision(LocalDate fecEnRevision) {
        this.fecEnRevision = fecEnRevision;
    }

    public LocalDate getFecTerminoRevision() {
        return fecTerminoRevision;
    }

    public DesgloseRevisionCif fecTerminoRevision(LocalDate fecTerminoRevision) {
        this.fecTerminoRevision = fecTerminoRevision;
        return this;
    }

    public void setFecTerminoRevision(LocalDate fecTerminoRevision) {
        this.fecTerminoRevision = fecTerminoRevision;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public DesgloseRevisionCif codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public Boolean getFlgNotificado() {
        return flgNotificado;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseRevisionCif))
            return false;
        DesgloseRevisionCif that = (DesgloseRevisionCif) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codPlantilla, that.codPlantilla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla);
    }

    @Override
    public String toString() {
        return "DesgloseRevision{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", rolDigitador='" + rolDigitador + '\'' + ", rolRevisor='" + rolRevisor + '\'' + ", rolRevisorReem='" + rolRevisorReem + '\''
            + ", glsNotificacion='" + glsNotificacion + '\'' + ", flgNotificado=" + flgNotificado + ", fecEnvioRevision=" + fecEnvioRevision + ", fecEnRevision=" + fecEnRevision
            + ", fecTerminoRevision=" + fecTerminoRevision + ", codRegStatus=" + codRegStatus + '}';
    }
}
