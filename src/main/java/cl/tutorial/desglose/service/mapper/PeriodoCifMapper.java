package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.PeriodoCif;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import cl.tutorial.desglose.service.dto.PeriodoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Periodo and its DTO PeriodoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PeriodoCifMapper extends EntityMapper<PeriodoDTO, PeriodoCif> {

    default PeriodoCif fromId(PeriodoPK id) {
        if (id == null) {
            return null;
        }
        PeriodoCif periodo = new PeriodoCif();
        return periodo;
    }
}
