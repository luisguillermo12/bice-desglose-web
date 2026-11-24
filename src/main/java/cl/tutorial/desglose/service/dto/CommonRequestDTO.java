package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The CommonRequestDTO class.
 */
public class CommonRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public String  codCliente;
    @NotNull
    public Integer tipoDesglose;
    @NotNull
    public Integer subTipoDesglose;
    @NotNull
    public String  moneda;

    @Override
    public String toString() {
        return "CommonRequestDTO{" + "codCliente='" + codCliente + '\'' + ", tipoDesglose=" + tipoDesglose + ", subTipoDesglose=" + subTipoDesglose + ", moneda='" + moneda + '\''
            + '}';
    }
}
