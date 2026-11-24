package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseFirmaPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"DESGLO_FIR_WK\" que contiene estructura de la segunda firma. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"DESGLO_FIR_WK\" que contiene estructura de la segunda firma. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(DesgloseFirmaPK.class)
@Entity
@Table(name = "DESGLO_FIR_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseFirma implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Código cargo. Posibles permitidos (001-Subgte, 002-Jefe Grupo y 003-Analista).
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código cargo. Posibles permitidos (CARGO-001, CARGO-002 y CARGO-003).", required = true)
    @Column(name = "cod_cargo", nullable = false)
    private String codCargo;

    /**
     * Rol empleado de primera firma.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Rol empleado de primera firma.", required = true)
    @Column(name = "num_rol_empleado", nullable = false)
    private String numRolEmpleado;

    /**
     * Rol empleado de segunda firma.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Rol empleado de segunda firma.", required = true)
    @Column(name = "num_rol_empleado_2fir", nullable = false)
    private String numRolEmpleado2fir;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCargo() {
        return codCargo;
    }

    public DesgloseFirma codCargo(String codCargo) {
        this.codCargo = codCargo;
        return this;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public DesgloseFirma numRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
        return this;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public String getNumRolEmpleado2fir() {
        return numRolEmpleado2fir;
    }

    public DesgloseFirma numRolEmpleado2fir(String numRolEmpleado2fir) {
        this.numRolEmpleado2fir = numRolEmpleado2fir;
        return this;
    }

    public void setNumRolEmpleado2fir(String numRolEmpleado2fir) {
        this.numRolEmpleado2fir = numRolEmpleado2fir;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseFirma))
            return false;
        DesgloseFirma that = (DesgloseFirma) o;
        return Objects.equals(codCargo, that.codCargo) && Objects.equals(numRolEmpleado, that.numRolEmpleado) && Objects.equals(numRolEmpleado2fir, that.numRolEmpleado2fir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCargo, numRolEmpleado, numRolEmpleado2fir);
    }

    @Override
    public String toString() {
        return "DesgloseFirma{" + "codCargo='" + codCargo + '\'' + ", numRolEmpleado='" + numRolEmpleado + '\'' + ", numRolEmpleado2fir='" + numRolEmpleado2fir + '\'' + '}';
    }
}
