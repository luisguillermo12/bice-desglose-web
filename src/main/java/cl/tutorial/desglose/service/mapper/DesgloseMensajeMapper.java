package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseMensaje;
import cl.tutorial.desglose.domain.pk.DesgloseMensajePK;
import cl.tutorial.desglose.service.dto.DesgloseMensajeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseMensaje and its DTO DesgloseMensajeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseMensajeMapper extends EntityMapper<DesgloseMensajeDTO, DesgloseMensaje> {

    default DesgloseMensaje fromId(DesgloseMensajePK id) {
        if (id == null) {
            return null;
        }
        DesgloseMensaje desgloseMensaje = new DesgloseMensaje();
        return desgloseMensaje;
    }
}
