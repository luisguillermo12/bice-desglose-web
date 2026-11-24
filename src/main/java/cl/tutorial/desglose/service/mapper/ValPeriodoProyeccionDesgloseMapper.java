package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.ValPeriodoDesglose;
import cl.tutorial.desglose.domain.ValPeriodoProyeccionDesglose;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesglosePK;
import cl.tutorial.desglose.domain.pk.ValPeriodoProyeccionDesglosePK;
import cl.tutorial.desglose.service.dto.ValPeriodoProyeccionDesgloseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity
 */
@Mapper(componentModel = "spring", uses = {})
public interface ValPeriodoProyeccionDesgloseMapper extends EntityMapper<ValPeriodoProyeccionDesgloseDTO, ValPeriodoProyeccionDesglose> {

    default ValPeriodoProyeccionDesglose fromId(ValPeriodoProyeccionDesglosePK id) {
        if (id == null) {
            return null;
        }
        ValPeriodoProyeccionDesglose valPeriodoProyeccionDesglose = new ValPeriodoProyeccionDesglose();
        return valPeriodoProyeccionDesglose;
    }
}
