package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the SearchRequest.
 */
public class BreakdownSearchRequestDTO extends CommonRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public Integer   tipoPlantillaDesglose;
    public LocalDate fecPeriodo;
    public Boolean   isProjectionRecover = false;

    public static BreakdownSearchRequestDTO newInstance(BreakdownCloneRequestDTO cloneRequestDTO) {
        BreakdownSearchRequestDTO searchRequestDTO = new BreakdownSearchRequestDTO();
        searchRequestDTO.codCliente = cloneRequestDTO.codCliente;
        searchRequestDTO.tipoDesglose = Integer.valueOf(cloneRequestDTO.codTipoDes);
        searchRequestDTO.subTipoDesglose = Integer.valueOf(cloneRequestDTO.codSubtipoDes);
        searchRequestDTO.moneda = cloneRequestDTO.codMoneda;
        searchRequestDTO.tipoPlantillaDesglose = Integer.valueOf(cloneRequestDTO.codPlantillaDesgloseOriginal);
        return searchRequestDTO;
    }

    public static BreakdownSearchRequestDTO newInstance(ProjectionHistoricDataRequestDTO historicDataRequestDTO) {
        BreakdownSearchRequestDTO searchRequestDTO = new BreakdownSearchRequestDTO();
        searchRequestDTO.codCliente = historicDataRequestDTO.codCliente;
        searchRequestDTO.tipoDesglose = historicDataRequestDTO.codTipoDes;
        searchRequestDTO.subTipoDesglose = historicDataRequestDTO.codSubtipoDes;
        searchRequestDTO.moneda = historicDataRequestDTO.codMoneda;
        searchRequestDTO.fecPeriodo = historicDataRequestDTO.fecPeriodo;
        searchRequestDTO.tipoPlantillaDesglose = historicDataRequestDTO.codPlantilla;
        searchRequestDTO.isProjectionRecover = true;
        return searchRequestDTO;
    }

    @Override
    public String toString() {
        return "BreakdownSearchRequestDTO{" + "codCliente='" + codCliente + '\'' + ", tipoDesglose=" + tipoDesglose + ", tipoPlantillaDesglose='" + tipoPlantillaDesglose + '\''
            + ", subTipoDesglose=" + subTipoDesglose + ", moneda=" + moneda + '}';
    }
}
