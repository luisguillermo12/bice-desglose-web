package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * A DTO for the datos históricos de proyección.
 */
public class ProjectionHistoricDataRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public String     codCliente;
    @NotNull
    public Integer    codTipoDes;
    @NotNull
    public Integer    codSubtipoDes;
    @NotNull
    public String     codMoneda;
    @NotNull
    public Integer    codPlantilla;
    @NotNull
    public LocalDate  fecPeriodo;

    @Override
    public String toString() {
        return "ProjectionHistoricDataRequestDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='"
            + codMoneda + '\'' + ", codPlantilla=" + codPlantilla + ", fecPeriodo=" + fecPeriodo + '}';
    }
}
