package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteSinRut;
import cl.tutorial.desglose.service.dto.ClienteSinRutDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ClienteSinRut and its DTO ClienteSinRutDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClienteSinRutMapper extends EntityMapper<ClienteSinRutDTO, ClienteSinRut> {

    default ClienteSinRut fromId(String codCliente) {
        if (codCliente == null) {
            return null;
        }
        ClienteSinRut clienteSinRut = new ClienteSinRut();
        clienteSinRut.setCodCliente(codCliente);
        return clienteSinRut;
    }
}
