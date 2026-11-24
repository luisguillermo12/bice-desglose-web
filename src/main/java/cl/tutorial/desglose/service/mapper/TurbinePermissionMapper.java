package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbinePermission;
import cl.tutorial.desglose.service.dto.TurbinePermissionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TurbinePermission and its DTO TurbinePermissionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TurbinePermissionMapper extends EntityMapper<TurbinePermissionDTO, TurbinePermission> {

    default TurbinePermission fromId(Long id) {
        if (id == null) {
            return null;
        }
        TurbinePermission turbinePermission = new TurbinePermission();
        turbinePermission.setPermissionId(id);
        return turbinePermission;
    }
}
