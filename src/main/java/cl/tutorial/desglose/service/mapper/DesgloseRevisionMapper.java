package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseRevision;
import cl.tutorial.desglose.domain.pk.DesgloseRevisionPK;
import cl.tutorial.desglose.service.dto.DesgloseRevisionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseRevision and its DTO DesgloseRevisionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseRevisionMapper extends EntityMapper<DesgloseRevisionDTO, DesgloseRevision> {

    default DesgloseRevision fromId(DesgloseRevisionPK id) {
        if (id == null) {
            return null;
        }
        DesgloseRevision desgloseRevision = new DesgloseRevision();
        return desgloseRevision;
    }
}
