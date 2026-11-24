package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseImpresion;
import cl.tutorial.desglose.domain.pk.DesgloseImpresionPK;
import cl.tutorial.desglose.service.dto.DesgloseImpresionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseImpresion and its DTO DesgloseImpresionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseImpresionMapper extends EntityMapper<DesgloseImpresionDTO, DesgloseImpresion> {

    default DesgloseImpresion fromId(DesgloseImpresionPK id) {
        if (id == null) {
            return null;
        }
        DesgloseImpresion desgloseImpresion = new DesgloseImpresion();
        return desgloseImpresion;
    }
}
