package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.*;
import cl.tutorial.desglose.service.dto.RelacionTablaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RelacionTabla and its DTO RelacionTablaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RelacionTablaMapper extends EntityMapper<RelacionTablaDTO, RelacionTabla> {



    default RelacionTabla fromId(Long id) {
        if (id == null) {
            return null;
        }
        RelacionTabla relacionTabla = new RelacionTabla();
        relacionTabla.setId(id);
        return relacionTabla;
    }
}
