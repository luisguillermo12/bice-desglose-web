package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseGrupo;
import cl.tutorial.desglose.domain.pk.DesgloseGrupoPK;
import cl.tutorial.desglose.service.dto.DesgloseGrupoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseGrupo and its DTO DesgloseGrupoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseGrupoMapper extends EntityMapper<DesgloseGrupoDTO, DesgloseGrupo> {

    default DesgloseGrupo fromId(DesgloseGrupoPK id) {
        if (id == null) {
            return null;
        }
        DesgloseGrupo desgloseGrupo = new DesgloseGrupo();
        return desgloseGrupo;
    }
}
