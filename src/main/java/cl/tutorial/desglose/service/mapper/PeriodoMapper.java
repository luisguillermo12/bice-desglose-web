package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.Periodo;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import cl.tutorial.desglose.service.dto.PeriodoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Periodo and its DTO PeriodoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PeriodoMapper extends EntityMapper<PeriodoDTO, Periodo> {

    default Periodo fromId(PeriodoPK id) {
        if (id == null) {
            return null;
        }
        Periodo periodo = new Periodo();
        return periodo;
    }
}
