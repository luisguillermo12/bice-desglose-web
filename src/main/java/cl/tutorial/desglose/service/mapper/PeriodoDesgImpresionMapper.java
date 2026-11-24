package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.PeriodoDesgImpresion;
import cl.tutorial.desglose.domain.pk.PeriodoDesgImpresionPK;
import cl.tutorial.desglose.service.dto.PeriodoDesgImpresionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PeriodoDesgImpresion and its DTO PeriodoDesgImpresionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PeriodoDesgImpresionMapper extends EntityMapper<PeriodoDesgImpresionDTO, PeriodoDesgImpresion> {

    default PeriodoDesgImpresion fromId(PeriodoDesgImpresionPK id) {
        if (id == null) {
            return null;
        }
        PeriodoDesgImpresion periodoDesgImpresion = new PeriodoDesgImpresion();
        return periodoDesgImpresion;
    }
}
