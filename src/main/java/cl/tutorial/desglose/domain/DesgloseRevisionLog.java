package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseRevisionLogPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Representa la tabla \"DESGLO_REV_LOG_WK\" que contiene los logs de flujo de desglose. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"DESGLO_REV_LOG_WK\" que contiene los logs de flujo de desglose. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(DesgloseRevisionLogPK.class)
@Entity
@Table(name = "DESGLO_REV_LOG_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseRevisionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Fecha y hora de evento.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha y hora de evento.", required = true)
    @Column(name = "fec_event", nullable = false)
    private ZonedDateTime fecEvent;

    /**
     * Número de linea.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Número de linea.", required = true)
    @Column(name = "num_linea_log", nullable = false)
    private Integer numLineaLog;

    /**
     * Rut de cliente.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Rut de cliente.", required = true)
    @Column(name = "cod_cliente", nullable = false)
    private String codCliente;

    /**
     * Tipo de desglose.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de desglose.", required = true)
    @Column(name = "cod_tipo_des", nullable = false)
    private Integer codTipoDes;

    /**
     * Subtipo de desglose.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Subtipo de desglose.", required = true)
    @Column(name = "cod_subtipo_des", nullable = false)
    private Integer codSubtipoDes;

    /**
     * Código de moneda.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de moneda.", required = true)
    @Column(name = "cod_moneda", nullable = false)
    private String codMoneda;

    /**
     * Rol empleado.
     */
    @NotNull
    @ApiModelProperty(value = "Rol empleado.", required = true)
    @Column(name = "num_rol_empleado", nullable = false)
    private String numRolEmpleado;

    /**
     * Estado nuevo.
     */
    @ApiModelProperty(value = "Estado nuevo.")
    @Column(name = "cod_estado")
    private String codEstado;

    /**
     * Estado antiguo.
     */
    @ApiModelProperty(value = "Estado antiguo.")
    @Column(name = "cod_estado_old")
    private String codEstadoOld;

    /**
     * Rol empleado reemplazado.
     */
    @ApiModelProperty(value = "Rol empleado reemplazado.")
    @Column(name = "num_rol_emp_reem")
    private String numRolEmpReem;

    /**
     * Comentarios.
     */
    @ApiModelProperty(value = "Comentarios.")
    @Column(name = "gls_comentarios")
    private String glsComentarios;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public ZonedDateTime getFecEvent() {
        return fecEvent;
    }

    public DesgloseRevisionLog fecEvent(ZonedDateTime fecEvent) {
        this.fecEvent = fecEvent;
        return this;
    }

    public void setFecEvent(ZonedDateTime fecEvent) {
        this.fecEvent = fecEvent;
    }

    public Integer getNumLineaLog() {
        return numLineaLog;
    }

    public DesgloseRevisionLog numLineaLog(Integer numLineaLog) {
        this.numLineaLog = numLineaLog;
        return this;
    }

    public void setNumLineaLog(Integer numLineaLog) {
        this.numLineaLog = numLineaLog;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseRevisionLog codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseRevisionLog codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseRevisionLog codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseRevisionLog codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public DesgloseRevisionLog numRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
        return this;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public DesgloseRevisionLog codEstado(String codEstado) {
        this.codEstado = codEstado;
        return this;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getCodEstadoOld() {
        return codEstadoOld;
    }

    public DesgloseRevisionLog codEstadoOld(String codEstadoOld) {
        this.codEstadoOld = codEstadoOld;
        return this;
    }

    public void setCodEstadoOld(String codEstadoOld) {
        this.codEstadoOld = codEstadoOld;
    }

    public String getNumRolEmpReem() {
        return numRolEmpReem;
    }

    public DesgloseRevisionLog numRolEmpReem(String numRolEmpReem) {
        this.numRolEmpReem = numRolEmpReem;
        return this;
    }

    public void setNumRolEmpReem(String numRolEmpReem) {
        this.numRolEmpReem = numRolEmpReem;
    }

    public String getGlsComentarios() {
        return glsComentarios;
    }

    public DesgloseRevisionLog glsComentarios(String glsComentarios) {
        this.glsComentarios = glsComentarios;
        return this;
    }

    public void setGlsComentarios(String glsComentarios) {
        this.glsComentarios = glsComentarios;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseRevisionLog))
            return false;
        DesgloseRevisionLog that = (DesgloseRevisionLog) o;
        return Objects.equals(fecEvent, that.fecEvent) && Objects.equals(numLineaLog, that.numLineaLog) && Objects.equals(codCliente, that.codCliente) && Objects
            .equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects.equals(codMoneda, that.codMoneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecEvent, numLineaLog, codCliente, codTipoDes, codSubtipoDes, codMoneda);
    }

    @Override
    public String toString() {
        return "DesgloseRevisionLog{" + "fecEvent='" + getFecEvent() + "'" + ", numLineaLog=" + getNumLineaLog() + ", codCliente='" + getCodCliente() + "'" + ", codTipoDes="
            + getCodTipoDes() + ", codSubtipoDes=" + getCodSubtipoDes() + ", codMoneda='" + getCodMoneda() + "'" + ", numRolEmpleado='" + getNumRolEmpleado() + "'"
            + ", codEstado='" + getCodEstado() + "'" + ", codEstadoOld='" + getCodEstadoOld() + "'" + ", numRolEmpReem='" + getNumRolEmpReem() + "'" + ", glsComentarios='"
            + getGlsComentarios() + "'" + "}";
    }
}
