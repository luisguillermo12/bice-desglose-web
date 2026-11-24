package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.ValPeriodoDesgImpr;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesgImprPK;
import cl.tutorial.desglose.service.dto.ValPeriodoDesgImprDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ValPeriodoDesgImpr and its DTO ValPeriodoDesgImprDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ValPeriodoDesgImprMapper extends EntityMapper<ValPeriodoDesgImprDTO, ValPeriodoDesgImpr> {

    default ValPeriodoDesgImpr fromId(ValPeriodoDesgImprPK id) {
        if (id == null) {
            return null;
        }
        ValPeriodoDesgImpr valPeriodoDesgImpr = new ValPeriodoDesgImpr();
        return valPeriodoDesgImpr;
    }
}
