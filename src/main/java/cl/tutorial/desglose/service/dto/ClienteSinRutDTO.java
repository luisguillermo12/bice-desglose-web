package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ClienteSinRut entity.
 */
public class ClienteSinRutDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCliente;

    @NotNull
    @Size(max = 40)
    private String nomCliente;

    @Size(max = 20)
    private String nomPaterno;

    @Size(max = 20)
    private String nomMaterno;

    private String codActividad;

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getNomCliente() {
        return nomCliente;
    }

    public void setNomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
    }

    public String getNomPaterno() {
        return nomPaterno;
    }

    public void setNomPaterno(String nomPaterno) {
        this.nomPaterno = nomPaterno;
    }

    public String getNomMaterno() {
        return nomMaterno;
    }

    public void setNomMaterno(String nomMaterno) {
        this.nomMaterno = nomMaterno;
    }

    public String getCodActividad() {
        return codActividad;
    }

    public void setCodActividad(String codActividad) {
        this.codActividad = codActividad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClienteSinRutDTO clienteSinRutDTO = (ClienteSinRutDTO) o;
        if (clienteSinRutDTO.getCodCliente() == null || getCodCliente() == null) {
            return false;
        }
        return Objects.equals(getCodCliente(), clienteSinRutDTO.getCodCliente());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCodCliente());
    }

    @Override
    public String toString() {
        return "ClienteSinRutDTO{" + "codCliente='" + getCodCliente() + "'" + ", nomCliente='" + getNomCliente() + "'" + ", nomPaterno='" + getNomPaterno() + "'" + ", nomMaterno='"
            + getNomMaterno() + "'" + ", codActividad='" + getCodActividad() + "'" + "}";
    }
}
