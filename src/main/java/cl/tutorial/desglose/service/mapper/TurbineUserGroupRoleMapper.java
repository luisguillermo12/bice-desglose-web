package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.pk.TurbineUserGroupRolePK;
import cl.tutorial.desglose.service.dto.TurbineUserGroupRoleDTO;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUserGroupRole;
import org.mapstruct.*;

/**
 * Mapper for the entity TurbineUserGroupRole and its DTO TurbineUserGroupRoleDTO.
 */
@Mapper(componentModel = "spring", uses = { TurbineUserMapper.class, TurbineGroupMapper.class, TurbineRoleMapper.class })
public interface TurbineUserGroupRoleMapper extends EntityMapper<TurbineUserGroupRoleDTO, TurbineUserGroupRole> {

    TurbineUserGroupRoleDTO toDto(TurbineUserGroupRole turbineUserGroupRole);

    TurbineUserGroupRole toEntity(TurbineUserGroupRoleDTO turbineUserGroupRoleDTO);

    default TurbineUserGroupRole fromId(TurbineUserGroupRolePK id) {
        if (id == null) {
            return null;
        }
        TurbineUserGroupRole turbineUserGroupRole = new TurbineUserGroupRole();
        turbineUserGroupRole.setUserId(id.getUserId());
        turbineUserGroupRole.setGroupId(id.getGroupId());
        turbineUserGroupRole.setRoleId(id.getRoleId());
        return turbineUserGroupRole;
    }
}
