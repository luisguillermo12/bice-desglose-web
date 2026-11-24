package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.*;
import cl.tutorial.desglose.domain.pk.TablaServicioMailDestPK;
import cl.tutorial.desglose.service.dto.TablaServMailDestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TablaServMailDest and its DTO TablaServMailDestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TablaServMailDestMapper extends EntityMapper<TablaServMailDestDTO, TablaServMailDest> {



    default TablaServMailDest fromId(TablaServicioMailDestPK id) {
        if (id == null) {
            return null;
        }
        TablaServMailDest tablaServMailDest = new TablaServMailDest();
        tablaServMailDest.setCodSistema(id.getCodSistema());
        tablaServMailDest.setNumSolicitud(id.getNumSolicitud());
        tablaServMailDest.setGlsToMail(id.getGlsToMail());
        tablaServMailDest.setCodTipoDest(id.getCodTipoDest());
        return tablaServMailDest;
    }
}
