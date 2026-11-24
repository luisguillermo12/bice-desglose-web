package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.*;
import cl.tutorial.desglose.service.dto.UniqueSessionControlDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UniqueSessionControl and its DTO UniqueSessionControlDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UniqueSessionControlMapper extends EntityMapper<UniqueSessionControlDTO, UniqueSessionControl> {

    default UniqueSessionControl fromId(Long id) {
        if (id == null) {
            return null;
        }
        UniqueSessionControl uniqueSessionControl = new UniqueSessionControl();
        uniqueSessionControl.setId(id);
        return uniqueSessionControl;
    }
}
