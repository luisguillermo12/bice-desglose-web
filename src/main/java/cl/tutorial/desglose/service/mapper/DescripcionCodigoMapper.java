package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DescripcionCodigo;
import cl.tutorial.desglose.domain.pk.DescripcionCodigoPK;
import cl.tutorial.desglose.service.dto.DescripcionCodigoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DescripcionCodigo and its DTO DescripcionCodigoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DescripcionCodigoMapper extends EntityMapper<DescripcionCodigoDTO, DescripcionCodigo> {

    default DescripcionCodigo fromId(DescripcionCodigoPK id) {
        if (id == null) {
            return null;
        }
        DescripcionCodigo descripcionCodigo = new DescripcionCodigo();
        descripcionCodigo.setCodTabla(id.getCodTabla());
        descripcionCodigo.setCodGenerico(id.getCodGenerico());
        return descripcionCodigo;
    }
}
