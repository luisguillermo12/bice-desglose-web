package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseChequeoLog;
import cl.tutorial.desglose.domain.pk.DesgloseChequeoLogPK;
import cl.tutorial.desglose.service.dto.DesgloseChequeoLogDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseChequeoLog and its DTO DesgloseChequeoLogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseChequeoLogMapper extends EntityMapper<DesgloseChequeoLogDTO, DesgloseChequeoLog> {

    default DesgloseChequeoLog fromId(DesgloseChequeoLogPK id) {
        if (id == null) {
            return null;
        }
        DesgloseChequeoLog desgloseChequeoLog = new DesgloseChequeoLog();
        desgloseChequeoLog.setCodCliente(id.getCodCliente());
        desgloseChequeoLog.setCodTipoDes(id.getCodTipoDes());
        desgloseChequeoLog.setCodSubtipoDes(id.getCodSubtipoDes());
        desgloseChequeoLog.setCodMoneda(id.getCodMoneda());
        return desgloseChequeoLog;
    }
}
