package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUser;
import cl.tutorial.desglose.service.dto.TurbineUserDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TurbineUser and its DTO TurbineUserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TurbineUserMapper extends EntityMapper<TurbineUserDTO, TurbineUser> {

    default TurbineUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        TurbineUser turbineUser = new TurbineUser();
        turbineUser.setUserId(id);
        return turbineUser;
    }
}
