package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ConceptoImpresionCif;
import cl.tutorial.desglose.domain.pk.ConceptoImpresionPK;
import cl.tutorial.desglose.service.dto.ConceptoImpresionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ConceptoImpresion and its DTO ConceptoImpresionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConceptoImpresionCifMapper extends EntityMapper<ConceptoImpresionDTO, ConceptoImpresionCif> {

    default ConceptoImpresionCif fromId(ConceptoImpresionPK id) {
        if (id == null) {
            return null;
        }
        ConceptoImpresionCif conceptoImpresion = new ConceptoImpresionCif();
        conceptoImpresion.setCodCliente(id.getCodCliente());
        conceptoImpresion.setCodMoneda(id.getCodMoneda());
        conceptoImpresion.setCodSubtipoDes(id.getCodSubtipoDes());
        conceptoImpresion.setCodTipoConcep(id.getCodTipoConcep());
        conceptoImpresion.setCodTipoDes(id.getCodTipoDes());
        conceptoImpresion.setCorConcepto(id.getCorConcepto());
        return conceptoImpresion;
    }
}
