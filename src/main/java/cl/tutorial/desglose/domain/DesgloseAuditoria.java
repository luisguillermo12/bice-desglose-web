package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseAuditoriaPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa la tabla \"DESGLO_AUDIT_WK\" que contiene información de auditoría de desgloses. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"DESGLO_AUDIT_WK\" que contiene información de auditoría de desgloses. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero "
    + "Palomino")
@IdClass(DesgloseAuditoriaPK.class)
@Entity
@Table(name = "DESGLO_AUDIT_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * Código del tipo de planilla. Por defecto 0 que es tipo banco.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código del tipo de planilla. Por defecto 0 que es tipo banco.", required = true)
    @Column(name = "cod_plantilla", nullable = false)
    private Integer codPlantilla;

    /**
     * Fecha de ocurrencia de evento.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha de ocurrencia de evento.", required = true)
    @Column(name = "fec_evento", nullable = false)
    private LocalDateTime fecEvento;

    /**
     * Numero de línea.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Numero de línea.", required = true)
    @Column(name = "num_linea", nullable = false)
    private Integer numLinea;

    /**
     * Cambio estado - Reemplazo - Validación - Desvinculación digitador.
     */
    @ApiModelProperty(value = "Cambio estado - Reemplazo - Validación - Desvinculación digitador.")
    @Column(name = "cod_evento")
    private String codEvento;

    /**
     * Rol empleado que realiza la acción.
     */
    @ApiModelProperty(value = "Rol empleado que realiza la acción.")
    @Column(name = "rol_empleado_accion")
    private String rolEmpleadoAccion;

    /**
     * Rol de quien realiza la acción (Digitador - Revisor).
     */
    @ApiModelProperty(value = "Rol de quien realiza la acción (Digitador - Revisor).")
    @Column(name = "gls_rol")
    private String glsRol;

    /**
     * Estado original o anterior.
     */
    @ApiModelProperty(value = "Estado original o anterior.")
    @Column(name = "cod_estado_ori")
    private String codEstadoOri;

    /**
     * Nuevo estado.
     */
    @ApiModelProperty(value = "Nuevo estado.")
    @Column(name = "cod_estado_nvo")
    private String codEstadoNvo;

    /**
     * Rol empleado original.
     */
    @ApiModelProperty(value = "Rol empleado original.")
    @Column(name = "rol_empleado_ori")
    private String rolEmpleadoOri;

    /**
     * Rol empleado nuevo.
     */
    @ApiModelProperty(value = "Rol empleado nuevo.")
    @Column(name = "rol_empleado_nvo")
    private String rolEmpleadoNvo;

    /**
     * OK - NOK.
     */
    @ApiModelProperty(value = "OK - NOK.")
    @Column(name = "flg_result_valid")
    private String flgResultValid;

    /**
     * Comentario asociado.
     */
    @ApiModelProperty(value = "Comentario asociado.")
    @Column(name = "gls_comentario")
    private String glsComentario;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseAuditoria codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseAuditoria codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseAuditoria codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseAuditoria codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public LocalDateTime getFecEvento() {
        return fecEvento;
    }

    public DesgloseAuditoria fecEvento(LocalDateTime fecEvento) {
        this.fecEvento = fecEvento;
        return this;
    }

    public void setFecEvento(LocalDateTime fecEvento) {
        this.fecEvento = fecEvento;
    }

    public Integer getNumLinea() {
        return numLinea;
    }

    public DesgloseAuditoria numLinea(Integer numLinea) {
        this.numLinea = numLinea;
        return this;
    }

    public void setNumLinea(Integer numLinea) {
        this.numLinea = numLinea;
    }

    public String getCodEvento() {
        return codEvento;
    }

    public DesgloseAuditoria codEvento(String codEvento) {
        this.codEvento = codEvento;
        return this;
    }

    public void setCodEvento(String codEvento) {
        this.codEvento = codEvento;
    }

    public String getRolEmpleadoAccion() {
        return rolEmpleadoAccion;
    }

    public DesgloseAuditoria rolEmpleadoAccion(String rolEmpleadoAccion) {
        this.rolEmpleadoAccion = rolEmpleadoAccion;
        return this;
    }

    public void setRolEmpleadoAccion(String rolEmpleadoAccion) {
        this.rolEmpleadoAccion = rolEmpleadoAccion;
    }

    public String getGlsRol() {
        return glsRol;
    }

    public DesgloseAuditoria glsRol(String glsRol) {
        this.glsRol = glsRol;
        return this;
    }

    public void setGlsRol(String glsRol) {
        this.glsRol = glsRol;
    }

    public String getCodEstadoOri() {
        return codEstadoOri;
    }

    public DesgloseAuditoria codEstadoOri(String codEstadoOri) {
        this.codEstadoOri = codEstadoOri;
        return this;
    }

    public void setCodEstadoOri(String codEstadoOri) {
        this.codEstadoOri = codEstadoOri;
    }

    public String getCodEstadoNvo() {
        return codEstadoNvo;
    }

    public DesgloseAuditoria codEstadoNvo(String codEstadoNvo) {
        this.codEstadoNvo = codEstadoNvo;
        return this;
    }

    public void setCodEstadoNvo(String codEstadoNvo) {
        this.codEstadoNvo = codEstadoNvo;
    }

    public String getRolEmpleadoOri() {
        return rolEmpleadoOri;
    }

    public DesgloseAuditoria rolEmpleadoOri(String rolEmpleadoOri) {
        this.rolEmpleadoOri = rolEmpleadoOri;
        return this;
    }

    public void setRolEmpleadoOri(String rolEmpleadoOri) {
        this.rolEmpleadoOri = rolEmpleadoOri;
    }

    public String getRolEmpleadoNvo() {
        return rolEmpleadoNvo;
    }

    public DesgloseAuditoria rolEmpleadoNvo(String rolEmpleadoNvo) {
        this.rolEmpleadoNvo = rolEmpleadoNvo;
        return this;
    }

    public void setRolEmpleadoNvo(String rolEmpleadoNvo) {
        this.rolEmpleadoNvo = rolEmpleadoNvo;
    }

    public String getFlgResultValid() {
        return flgResultValid;
    }

    public DesgloseAuditoria flgResultValid(String flgResultValid) {
        this.flgResultValid = flgResultValid;
        return this;
    }

    public void setFlgResultValid(String flgResultValid) {
        this.flgResultValid = flgResultValid;
    }

    public String getGlsComentario() {
        return glsComentario;
    }

    public DesgloseAuditoria glsComentario(String glsComentario) {
        this.glsComentario = glsComentario;
        return this;
    }

    public void setGlsComentario(String glsComentario) {
        this.glsComentario = glsComentario;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public DesgloseAuditoria codPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseAuditoria))
            return false;
        DesgloseAuditoria that = (DesgloseAuditoria) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codPlantilla, that.codPlantilla) && Objects.equals(fecEvento, that.fecEvento) && Objects
            .equals(numLinea, that.numLinea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, fecEvento, numLinea);
    }

    @Override
    public String toString() {
        return "DesgloseAuditoria{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", fecEvento=" + fecEvento + ", numLinea=" + numLinea + ", codEvento='" + codEvento + '\'' + ", rolEmpleadoAccion='"
            + rolEmpleadoAccion + '\'' + ", glsRol='" + glsRol + '\'' + ", codEstadoOri='" + codEstadoOri + '\'' + ", codEstadoNvo='" + codEstadoNvo + '\'' + ", rolEmpleadoOri='"
            + rolEmpleadoOri + '\'' + ", rolEmpleadoNvo='" + rolEmpleadoNvo + '\'' + ", flgResultValid='" + flgResultValid + '\'' + ", glsComentario='" + glsComentario + '\''
            + '}';
    }
}
