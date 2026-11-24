package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseRevisionLog;
import cl.tutorial.desglose.domain.pk.DesgloseRevisionLogPK;
import cl.tutorial.desglose.service.dto.DesgloseRevisionLogDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseRevisionLog and its DTO DesgloseRevisionLogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseRevisionLogMapper extends EntityMapper<DesgloseRevisionLogDTO, DesgloseRevisionLog> {

    default DesgloseRevisionLog fromId(DesgloseRevisionLogPK id) {
        if (id == null) {
            return null;
        }
        DesgloseRevisionLog desgloseRevisionLog = new DesgloseRevisionLog();
        return desgloseRevisionLog;
    }
}
