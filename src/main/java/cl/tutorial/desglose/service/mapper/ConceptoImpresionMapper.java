package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.ConceptoImpresion;
import cl.tutorial.desglose.domain.pk.ConceptoImpresionPK;
import cl.tutorial.desglose.service.dto.ConceptoImpresionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ConceptoImpresion and its DTO ConceptoImpresionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConceptoImpresionMapper extends EntityMapper<ConceptoImpresionDTO, ConceptoImpresion> {

    default ConceptoImpresion fromId(ConceptoImpresionPK id) {
        if (id == null) {
            return null;
        }
        ConceptoImpresion conceptoImpresion = new ConceptoImpresion();
        conceptoImpresion.setCodCliente(id.getCodCliente());
        conceptoImpresion.setCodMoneda(id.getCodMoneda());
        conceptoImpresion.setCodSubtipoDes(id.getCodSubtipoDes());
        conceptoImpresion.setCodTipoConcep(id.getCodTipoConcep());
        conceptoImpresion.setCodTipoDes(id.getCodTipoDes());
        conceptoImpresion.setCorConcepto(id.getCorConcepto());
        return conceptoImpresion;
    }
}
