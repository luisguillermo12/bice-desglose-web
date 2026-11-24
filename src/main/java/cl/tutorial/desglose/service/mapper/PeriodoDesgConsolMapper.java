package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.PeriodoDesgConsol;
import cl.tutorial.desglose.domain.pk.PeriodoDesgConsolPK;
import cl.tutorial.desglose.service.dto.PeriodoDesgConsolDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PeriodoDesgConsol and its DTO PeriodoDesgConsolDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PeriodoDesgConsolMapper extends EntityMapper<PeriodoDesgConsolDTO, PeriodoDesgConsol> {

    default PeriodoDesgConsol fromId(PeriodoDesgConsolPK id) {
        if (id == null) {
            return null;
        }
        PeriodoDesgConsol periodoDesgConsol = new PeriodoDesgConsol();
        return periodoDesgConsol;
    }
}
