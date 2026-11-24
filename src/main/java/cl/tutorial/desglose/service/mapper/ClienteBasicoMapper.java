package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteBasico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.service.dto.ClienteBasicoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ClienteBasico and its DTO ClienteBasicoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClienteBasicoMapper extends EntityMapper<ClienteBasicoDTO, ClienteBasico> {

    default ClienteBasico fromId(CommonClientePK id) {
        if (id == null) {
            return null;
        }
        ClienteBasico clienteBasico = new ClienteBasico();
        clienteBasico.setNumCliente(id.getNumCliente());
        clienteBasico.setCodRegLog(id.getCodRegLog());
        return clienteBasico;
    }
}
