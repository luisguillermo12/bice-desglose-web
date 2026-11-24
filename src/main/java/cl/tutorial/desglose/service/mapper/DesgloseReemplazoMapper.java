package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseReemplazo;
import cl.tutorial.desglose.domain.pk.DesgloseReemplazoPK;
import cl.tutorial.desglose.service.dto.DesgloseReemplazoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseReemplazo and its DTO DesgloseReemplazoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseReemplazoMapper extends EntityMapper<DesgloseReemplazoDTO, DesgloseReemplazo> {

    default DesgloseReemplazo fromId(DesgloseReemplazoPK id) {
        if (id == null) {
            return null;
        }
        DesgloseReemplazo desgloseReemplazo = new DesgloseReemplazo();
        return desgloseReemplazo;
    }
}
