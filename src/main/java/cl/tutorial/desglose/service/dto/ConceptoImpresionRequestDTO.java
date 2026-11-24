package cl.tutorial.desglose.service.dto;

import java.io.Serializable;

/**
 * A DTO for the ConceptoImpresion filter.
 */
public class ConceptoImpresionRequestDTO extends CommonRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static ConceptoImpresionRequestDTO newInstance(BreakdownSearchRequestDTO searchRequestDTO) {
        ConceptoImpresionRequestDTO conceptoImpresionRequestDTO = new ConceptoImpresionRequestDTO();
        conceptoImpresionRequestDTO.codCliente = searchRequestDTO.codCliente;
        conceptoImpresionRequestDTO.tipoDesglose = searchRequestDTO.tipoDesglose;
        conceptoImpresionRequestDTO.subTipoDesglose = searchRequestDTO.subTipoDesglose;
        conceptoImpresionRequestDTO.moneda = searchRequestDTO.moneda;
        return conceptoImpresionRequestDTO;
    }

    @Override
    public String toString() {
        return "ConceptoImpresionRequestDTO{" + "codCliente='" + codCliente + '\'' + ", tipoDesglose=" + tipoDesglose + ", subTipoDesglose=" + subTipoDesglose + ", moneda='"
            + moneda + '\'' + '}';
    }
}
