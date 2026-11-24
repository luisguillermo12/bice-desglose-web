package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.ValPeriodoDesglose;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesglosePK;
import cl.tutorial.desglose.service.dto.ValPeriodoDesgloseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ValPeriodoDesglose and its DTO ValPeriodoDesgloseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ValPeriodoDesgloseMapper extends EntityMapper<ValPeriodoDesgloseDTO, ValPeriodoDesglose> {

    default ValPeriodoDesglose fromId(ValPeriodoDesglosePK id) {
        if (id == null) {
            return null;
        }
        ValPeriodoDesglose valPeriodoDesglose = new ValPeriodoDesglose();
        return valPeriodoDesglose;
    }
}
