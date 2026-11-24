package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseReemplazoPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa la tabla \"DESGLO_REE_WK\" que contiene la definición de los reemplazos. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"DESGLO_REE_WK\" que contiene la definición de los reemplazos. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(DesgloseReemplazoPK.class)
@Entity
@Table(name = "DESGLO_REE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseReemplazoCif implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).
     */
    @Id
    @ApiModelProperty(value = "Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).")
    @Column(name = "cod_cargo")
    private String codCargo;

    /**
     * Rol de usuario asociado al cargo.
     */
    @ApiModelProperty(value = "Rol de usuario asociado al cargo.")
    @Column(name = "num_rol_empleado")
    private String numRolEmpleado;

    /**
     * Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).
     */
    @ApiModelProperty(value = "Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).")
    @Column(name = "des_cod_cargo")
    private String desCodCargo;

    /**
     * Rol de usuario asociado al cargo.
     */
    @ApiModelProperty(value = "Rol de usuario asociado al cargo.")
    @Column(name = "des_num_rol_empleado")
    private String desNumRolEmpleado;

    /**
     * Fecha del reemplazo.
     */
    @ApiModelProperty(value = "Fecha del reemplazo.")
    @Column(name = "fec_reemplazo")
    private LocalDate fecReemplazo;

    /**
     * Fecha de eliminación del reemplazo.
     */
    @ApiModelProperty(value = "Fecha de eliminación del reemplazo.")
    @Column(name = "fec_elim_reemplazo")
    private LocalDate fecElimReemplazo;

    /**
     * Rol de quien crea el reemplazo.
     */
    @ApiModelProperty(value = "Rol de quien crea el reemplazo.")
    @Column(name = "num_rol_empleado_crea")
    private String numRolEmpleadoCrea;

    /**
     * Rol de quien elimina el reemplazo.
     */
    @ApiModelProperty(value = "Rol de quien elimina el reemplazo.")
    @Column(name = "num_rol_empleado_elim")
    private String numRolEmpleadoElim;

    /**
     * Código de vigencia del registro.
     */
    @ApiModelProperty(value = "Código de vigencia del registro.")
    @Column(name = "cod_reg_status")
    private Integer codRegStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCargo() {
        return codCargo;
    }

    public DesgloseReemplazoCif codCargo(String codCargo) {
        this.codCargo = codCargo;
        return this;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public DesgloseReemplazoCif numRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
        return this;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public String getDesCodCargo() {
        return desCodCargo;
    }

    public DesgloseReemplazoCif desCodCargo(String desCodCargo) {
        this.desCodCargo = desCodCargo;
        return this;
    }

    public void setDesCodCargo(String desCodCargo) {
        this.desCodCargo = desCodCargo;
    }

    public String getDesNumRolEmpleado() {
        return desNumRolEmpleado;
    }

    public DesgloseReemplazoCif desNumRolEmpleado(String desNumRolEmpleado) {
        this.desNumRolEmpleado = desNumRolEmpleado;
        return this;
    }

    public void setDesNumRolEmpleado(String desNumRolEmpleado) {
        this.desNumRolEmpleado = desNumRolEmpleado;
    }

    public LocalDate getFecReemplazo() {
        return fecReemplazo;
    }

    public DesgloseReemplazoCif fecReemplazo(LocalDate fecReemplazo) {
        this.fecReemplazo = fecReemplazo;
        return this;
    }

    public void setFecReemplazo(LocalDate fecReemplazo) {
        this.fecReemplazo = fecReemplazo;
    }

    public LocalDate getFecElimReemplazo() {
        return fecElimReemplazo;
    }

    public DesgloseReemplazoCif fecElimReemplazo(LocalDate fecElimReemplazo) {
        this.fecElimReemplazo = fecElimReemplazo;
        return this;
    }

    public void setFecElimReemplazo(LocalDate fecElimReemplazo) {
        this.fecElimReemplazo = fecElimReemplazo;
    }

    public String getNumRolEmpleadoCrea() {
        return numRolEmpleadoCrea;
    }

    public DesgloseReemplazoCif numRolEmpleadoCrea(String numRolEmpleadoCrea) {
        this.numRolEmpleadoCrea = numRolEmpleadoCrea;
        return this;
    }

    public void setNumRolEmpleadoCrea(String numRolEmpleadoCrea) {
        this.numRolEmpleadoCrea = numRolEmpleadoCrea;
    }

    public String getNumRolEmpleadoElim() {
        return numRolEmpleadoElim;
    }

    public DesgloseReemplazoCif numRolEmpleadoElim(String numRolEmpleadoElim) {
        this.numRolEmpleadoElim = numRolEmpleadoElim;
        return this;
    }

    public void setNumRolEmpleadoElim(String numRolEmpleadoElim) {
        this.numRolEmpleadoElim = numRolEmpleadoElim;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public DesgloseReemplazoCif codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public String toString() {
        return "DesgloseReemplazo{" + "codCargo='" + getCodCargo() + "'" + ", numRolEmpleado='" + getNumRolEmpleado() + "'" + ", desCodCargo='" + getDesCodCargo() + "'"
            + ", desNumRolEmpleado='" + getDesNumRolEmpleado() + "'" + ", fecReemplazo='" + getFecReemplazo() + "'" + ", fecElimReemplazo='" + getFecElimReemplazo() + "'"
            + ", numRolEmpleadoCrea='" + getNumRolEmpleadoCrea() + "'" + ", numRolEmpleadoElim='" + getNumRolEmpleadoElim() + "'" + ", codRegStatus=" + getCodRegStatus() + "}";
    }
}
