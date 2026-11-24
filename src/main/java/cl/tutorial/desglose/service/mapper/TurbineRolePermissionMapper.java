package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRolePermission;
import cl.tutorial.desglose.service.dto.TurbineRolePermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity TurbineRolePermission and its DTO TurbineRolePermissionDTO.
 */
@Mapper(componentModel = "spring", uses = { TurbineRoleMapper.class, TurbinePermissionMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TurbineRolePermissionMapper extends EntityMapper<TurbineRolePermissionDTO, TurbineRolePermission> {

    TurbineRolePermissionDTO toDto(TurbineRolePermission turbineRolePermission);

    TurbineRolePermission toEntity(TurbineRolePermissionDTO turbineRolePermissionDTO);

    default TurbineRolePermission fromId(Long id) {
        if (id == null) {
            return null;
        }
        TurbineRolePermission turbineRolePermission = new TurbineRolePermission();
        turbineRolePermission.setPermissionId(id);
        return turbineRolePermission;
    }
}
