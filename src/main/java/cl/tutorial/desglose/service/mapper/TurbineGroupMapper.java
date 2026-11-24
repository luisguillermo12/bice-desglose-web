package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineGroup;
import cl.tutorial.desglose.service.dto.TurbineGroupDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TurbineGroup and its DTO TurbineGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TurbineGroupMapper extends EntityMapper<TurbineGroupDTO, TurbineGroup> {

    default TurbineGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        TurbineGroup turbineGroup = new TurbineGroup();
        turbineGroup.setGroupId(id);
        return turbineGroup;
    }
}
