package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.*;
import cl.tutorial.desglose.service.dto.SfStfOperatorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SfStfOperator and its DTO SfStfOperatorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SfStfOperatorMapper extends EntityMapper<SfStfOperatorDTO, SfStfOperator> {



    default SfStfOperator fromId(Long id) {
        if (id == null) {
            return null;
        }
        SfStfOperator sfStfOperator = new SfStfOperator();
        return sfStfOperator;
    }
}
