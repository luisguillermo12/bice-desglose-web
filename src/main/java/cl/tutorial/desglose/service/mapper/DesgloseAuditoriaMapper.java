package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.DesgloseAuditoria;
import cl.tutorial.desglose.domain.pk.DesgloseAuditoriaPK;
import cl.tutorial.desglose.service.dto.DesgloseAuditoriaDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DesgloseAuditoria and its DTO DesgloseAuditoriaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseAuditoriaMapper extends EntityMapper<DesgloseAuditoriaDTO, DesgloseAuditoria> {

    default DesgloseAuditoria fromId(DesgloseAuditoriaPK id) {
        if (id == null) {
            return null;
        }
        DesgloseAuditoria desgloseAuditoria = new DesgloseAuditoria();
        desgloseAuditoria.setCodCliente(id.getCodCliente());
        desgloseAuditoria.setCodTipoDes(id.getCodTipoDes());
        desgloseAuditoria.setCodSubtipoDes(id.getCodSubtipoDes());
        desgloseAuditoria.setCodMoneda(id.getCodMoneda());
        desgloseAuditoria.setFecEvento(id.getFecEvento());
        desgloseAuditoria.setNumLinea(id.getNumLinea());
        return desgloseAuditoria;
    }
}
