package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteJuridico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.service.dto.ClienteJuridicoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ClienteJuridico and its DTO ClienteJuridicoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClienteJuridicoMapper extends EntityMapper<ClienteJuridicoDTO, ClienteJuridico> {

    default ClienteJuridico fromId(CommonClientePK id) {
        if (id == null) {
            return null;
        }
        ClienteJuridico clienteJuridico = new ClienteJuridico();
        clienteJuridico.setNumCliente(id.getNumCliente());
        clienteJuridico.setCodRegLog(id.getCodRegLog());
        return clienteJuridico;
    }
}
