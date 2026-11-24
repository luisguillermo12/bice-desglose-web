package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteNatural;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.service.dto.ClienteNaturalDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ClienteNatural and its DTO ClienteNaturalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClienteNaturalMapper extends EntityMapper<ClienteNaturalDTO, ClienteNatural> {

    default ClienteNatural fromId(CommonClientePK id) {
        if (id == null) {
            return null;
        }
        ClienteNatural clienteNatural = new ClienteNatural();
        clienteNatural.setNumCliente(id.getNumCliente());
        clienteNatural.setCodRegLog(id.getCodRegLog());
        return clienteNatural;
    }
}
