package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseGrupoPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"DESGLO_GRUPO_WK\" que contiene la definición de grupos. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"DESGLO_GRUPO_WK\" que contiene la definición de grupos. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(DesgloseGrupoPK.class)
@Entity
@Table(name = "DESGLO_GRUPO_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseGrupo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).", required = true)
    @Column(name = "cod_cargo", nullable = false)
    private String codCargo;

    /**
     * Rol de usuario asociado al cargo.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Rol de usuario asociado al cargo.", required = true)
    @Column(name = "num_rol_empleado", nullable = false)
    private String numRolEmpleado;

    /**
     * Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).", required = true)
    @Column(name = "des_cod_cargo", nullable = false)
    private String desCodCargo;

    /**
     * Rol de usuario asociado al cargo.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Rol de usuario asociado al cargo.", required = true)
    @Column(name = "des_num_rol_empleado", nullable = false)
    private String desNumRolEmpleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCargo() {
        return codCargo;
    }

    public DesgloseGrupo codCargo(String codCargo) {
        this.codCargo = codCargo;
        return this;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public DesgloseGrupo numRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
        return this;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public String getDesCodCargo() {
        return desCodCargo;
    }

    public DesgloseGrupo desCodCargo(String desCodCargo) {
        this.desCodCargo = desCodCargo;
        return this;
    }

    public void setDesCodCargo(String desCodCargo) {
        this.desCodCargo = desCodCargo;
    }

    public String getDesNumRolEmpleado() {
        return desNumRolEmpleado;
    }

    public DesgloseGrupo desNumRolEmpleado(String desNumRolEmpleado) {
        this.desNumRolEmpleado = desNumRolEmpleado;
        return this;
    }

    public void setDesNumRolEmpleado(String desNumRolEmpleado) {
        this.desNumRolEmpleado = desNumRolEmpleado;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseGrupo))
            return false;
        DesgloseGrupo that = (DesgloseGrupo) o;
        return Objects.equals(codCargo, that.codCargo) && Objects.equals(numRolEmpleado, that.numRolEmpleado) && Objects.equals(desCodCargo, that.desCodCargo) && Objects
            .equals(desNumRolEmpleado, that.desNumRolEmpleado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCargo, numRolEmpleado, desCodCargo, desNumRolEmpleado);
    }

    @Override
    public String toString() {
        return "DesgloseGrupo{" + "codCargo='" + codCargo + '\'' + ", numRolEmpleado='" + numRolEmpleado + '\'' + ", desCodCargo='" + desCodCargo + '\'' + ", desNumRolEmpleado='"
            + desNumRolEmpleado + '\'' + '}';
    }
}
