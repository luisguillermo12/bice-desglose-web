package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.*;
import cl.tutorial.desglose.domain.pk.TablaServicioMailPK;
import cl.tutorial.desglose.service.dto.TablaServicioMailDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TablaServicioMail and its DTO TablaServicioMailDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TablaServicioMailMapper extends EntityMapper<TablaServicioMailDTO, TablaServicioMail> {



    default TablaServicioMail fromId(TablaServicioMailPK id) {
        if (id == null) {
            return null;
        }
        TablaServicioMail tablaServicioMail = new TablaServicioMail();
        tablaServicioMail.setCodSistema(id.getCodSistema());
        tablaServicioMail.setNumSolicitud(id.getNumSolicitud());
        return tablaServicioMail;
    }
}
