package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRole;
import cl.tutorial.desglose.service.dto.TurbineRoleDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TurbineRole and its DTO TurbineRoleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TurbineRoleMapper extends EntityMapper<TurbineRoleDTO, TurbineRole> {

    default TurbineRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        TurbineRole turbineRole = new TurbineRole();
        turbineRole.setRoleId(id);
        return turbineRole;
    }
}
