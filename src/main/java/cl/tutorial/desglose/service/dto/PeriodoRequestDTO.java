package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the PeriodoRequestDTO.
 */
public class PeriodoRequestDTO extends CommonRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public LocalDate fecPeriodo;
    public Integer   codPlantilla;

    public static PeriodoRequestDTO newInstance(BreakdownSearchRequestDTO searchRequestDTO) {
        PeriodoRequestDTO periodoRequestDTO = new PeriodoRequestDTO();
        periodoRequestDTO.codCliente = searchRequestDTO.codCliente;
        periodoRequestDTO.tipoDesglose = searchRequestDTO.tipoDesglose;
        periodoRequestDTO.subTipoDesglose = searchRequestDTO.subTipoDesglose;
        periodoRequestDTO.moneda = searchRequestDTO.moneda;
        periodoRequestDTO.fecPeriodo = searchRequestDTO.fecPeriodo;
        periodoRequestDTO.codPlantilla = searchRequestDTO.tipoPlantillaDesglose;
        return periodoRequestDTO;
    }

    @Override
    public String toString() {
        return "PeriodoRequestDTO{" + "fecPeriodo=" + fecPeriodo + ", codPlantilla=" + codPlantilla + ", codCliente='" + codCliente + '\'' + ", tipoDesglose=" + tipoDesglose
            + ", subTipoDesglose=" + subTipoDesglose + ", moneda='" + moneda + '\'' + '}';
    }
}
