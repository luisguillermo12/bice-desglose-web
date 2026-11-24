package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"SINRUT\" que contiene detalle de cliente sin rut. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"SINRUT\" que contiene detalle de cliente sin rut. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@Entity
@Table(name = "SINRUT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClienteSinRut implements Serializable {

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
     * Nombre de cliente o razón social.
     */
    @NotNull
    @ApiModelProperty(value = "Nombre de cliente o razón social.", required = true)
    @Column(name = "nom_cliente", nullable = false)
    private String nomCliente;

    /**
     * Apellido paterno del cliente.
     */
    @ApiModelProperty(value = "Apellido paterno del cliente.")
    @Column(name = "nom_paterno")
    private String nomPaterno;

    /**
     * Apellido materno del cliente.
     */
    @ApiModelProperty(value = "Apellido materno del cliente.")
    @Column(name = "nom_materno")
    private String nomMaterno;

    /**
     * Código de actividad asociada al cliente.
     */
    @ApiModelProperty(value = "Código de actividad asociada al cliente.")
    @Column(name = "cod_actividad")
    private String codActividad;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getCodCliente() {
        return codCliente;
    }

    public ClienteSinRut codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getNomCliente() {
        return nomCliente;
    }

    public ClienteSinRut nomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
        return this;
    }

    public void setNomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
    }

    public String getNomPaterno() {
        return nomPaterno;
    }

    public ClienteSinRut nomPaterno(String nomPaterno) {
        this.nomPaterno = nomPaterno;
        return this;
    }

    public void setNomPaterno(String nomPaterno) {
        this.nomPaterno = nomPaterno;
    }

    public String getNomMaterno() {
        return nomMaterno;
    }

    public ClienteSinRut nomMaterno(String nomMaterno) {
        this.nomMaterno = nomMaterno;
        return this;
    }

    public void setNomMaterno(String nomMaterno) {
        this.nomMaterno = nomMaterno;
    }

    public String getCodActividad() {
        return codActividad;
    }

    public ClienteSinRut codActividad(String codActividad) {
        this.codActividad = codActividad;
        return this;
    }

    public void setCodActividad(String codActividad) {
        this.codActividad = codActividad;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClienteSinRut clienteSinRut = (ClienteSinRut) o;
        if (clienteSinRut.getCodCliente() == null || getCodCliente() == null) {
            return false;
        }
        return Objects.equals(getCodCliente(), clienteSinRut.getCodCliente());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCodCliente());
    }

    @Override
    public String toString() {
        return "ClienteSinRut{" + " codCliente='" + getCodCliente() + "'" + ", nomCliente='" + getNomCliente() + "'" + ", nomPaterno='" + getNomPaterno() + "'" + ", nomMaterno='"
            + getNomMaterno() + "'" + ", codActividad='" + getCodActividad() + "'" + "}";
    }
}
