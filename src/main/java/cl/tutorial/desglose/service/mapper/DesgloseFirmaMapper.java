package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseFirma;
import cl.tutorial.desglose.domain.pk.DesgloseFirmaPK;
import cl.tutorial.desglose.service.dto.DesgloseFirmaDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseFirma and its DTO DesgloseFirmaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseFirmaMapper extends EntityMapper<DesgloseFirmaDTO, DesgloseFirma> {

    default DesgloseFirma fromId(DesgloseFirmaPK id) {
        if (id == null) {
            return null;
        }
        DesgloseFirma desgloseFirma = new DesgloseFirma();
        desgloseFirma.setCodCargo(id.getCodCargo());
        desgloseFirma.setNumRolEmpleado(id.getNumRolEmpleado());
        desgloseFirma.setNumRolEmpleado2fir(id.getNumRolEmpleado2fir());
        return desgloseFirma;
    }
}
