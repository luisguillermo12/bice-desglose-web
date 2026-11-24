package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseChequeoInfo;
import cl.tutorial.desglose.domain.pk.DesgloseChequeoInfoPK;
import cl.tutorial.desglose.service.dto.DesgloseChequeoInfoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseChequeoInfo and its DTO DesgloseChequeoInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseChequeoInfoMapper extends EntityMapper<DesgloseChequeoInfoDTO, DesgloseChequeoInfo> {

    default DesgloseChequeoInfo fromId(DesgloseChequeoInfoPK id) {
        if (id == null) {
            return null;
        }
        DesgloseChequeoInfo desgloseChequeoInfo = new DesgloseChequeoInfo();
        desgloseChequeoInfo.setCodCliente(id.getCodCliente());
        desgloseChequeoInfo.setCodTipoDes(id.getCodTipoDes());
        desgloseChequeoInfo.setCodSubtipoDes(id.getCodSubtipoDes());
        desgloseChequeoInfo.setCodMoneda(id.getCodMoneda());
        return desgloseChequeoInfo;
    }
}
