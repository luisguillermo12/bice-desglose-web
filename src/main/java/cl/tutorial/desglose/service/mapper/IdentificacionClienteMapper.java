package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.IdentificacionCliente;
import cl.tutorial.desglose.domain.pk.IdentificacionClientePK;
import cl.tutorial.desglose.service.dto.IdentificacionClienteDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity IdentificacionCliente and its DTO IdentificacionClienteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IdentificacionClienteMapper extends EntityMapper<IdentificacionClienteDTO, IdentificacionCliente> {

    default IdentificacionCliente fromId(IdentificacionClientePK id) {
        if (id == null) {
            return null;
        }
        IdentificacionCliente identificacionCliente = new IdentificacionCliente();
        ;
        identificacionCliente.setNumCliente(id.getNumCliente());
        identificacionCliente.setCodRegLog(id.getCodRegLog());
        identificacionCliente.setCodTipoIden(id.getCodTipoIden());
        return identificacionCliente;
    }
}
