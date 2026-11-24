package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseMensajePK;
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
 * Representa la tabla \"DESGLO_MSG_WK\" que guarda mensajería entre digitador y revisor para un ciclo o flujo de desglose. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"DESGLO_MSG_WK\" que guarda mensajería entre digitador y revisor para un ciclo o flujo de desglose. Pertenece al esquema CIF_ADMIN. "
        + "@author "
        + "Antonio Marrero Palomino")
@IdClass(DesgloseMensajePK.class)
@Entity
@Table(name = "DESGLO_MSG")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseMensajeCif implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Fecha y hora de ocurrido el evento.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha y hora de ocurrido el evento.", required = true)
    @Column(name = "fec_event", nullable = false)
    private ZonedDateTime fecEvent;

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
     * Rol empleado asociado.
     */
    @NotNull
    @ApiModelProperty(value = "Rol empleado asociado.", required = true)
    @Column(name = "num_rol_empleado", nullable = false)
    private String numRolEmpleado;

    /**
     * Mensaje enviado.
     */
    @ApiModelProperty(value = "Mensaje enviado.")
    @Column(name = "gls_comentarios")
    private String glsComentarios;

    @Id
    @ApiModelProperty(value = "cod tipo de plantilla")
    @Column(name = "cod_plantilla")
    private Integer codPlantilla;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public ZonedDateTime getFecEvent() {
        return fecEvent;
    }

    public DesgloseMensajeCif fecEvent(ZonedDateTime fecEvent) {
        this.fecEvent = fecEvent;
        return this;
    }

    public void setFecEvent(ZonedDateTime fecEvent) {
        this.fecEvent = fecEvent;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseMensajeCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseMensajeCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseMensajeCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseMensajeCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public DesgloseMensajeCif numRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
        return this;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public String getGlsComentarios() {
        return glsComentarios;
    }

    public DesgloseMensajeCif glsComentarios(String glsComentarios) {
        this.glsComentarios = glsComentarios;
        return this;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public void setGlsComentarios(String glsComentarios) {
        this.glsComentarios = glsComentarios;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseMensajeCif))
            return false;
        DesgloseMensajeCif that = (DesgloseMensajeCif) o;
        return getCodCliente().equals(that.getCodCliente()) && getCodTipoDes().equals(that.getCodTipoDes()) && getCodSubtipoDes().equals(that.getCodSubtipoDes()) && getCodMoneda()
            .equals(that.getCodMoneda()) && getNumRolEmpleado().equals(that.getNumRolEmpleado()) && getGlsComentarios().equals(that.getGlsComentarios()) && codPlantilla
            .equals(that.codPlantilla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodCliente(), getCodTipoDes(), getCodSubtipoDes(), getCodMoneda(), getNumRolEmpleado(), getGlsComentarios(), codPlantilla);
    }

    @Override
    public String toString() {
        return "DesgloseMensaje{" + "fecEvent=" + fecEvent + ", codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes
            + ", codMoneda='" + codMoneda + '\'' + ", numRolEmpleado='" + numRolEmpleado + '\'' + ", glsComentarios='" + glsComentarios + '\'' + ", codPlantilla='" + codPlantilla
            + '\'' + '}';
    }
}
