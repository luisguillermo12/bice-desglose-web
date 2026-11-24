package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.TablaValor;
import cl.tutorial.desglose.domain.pk.TablaValorPK;
import cl.tutorial.desglose.service.dto.TablaValorDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TablaValor and its DTO TablaValorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TablaValorMapper extends EntityMapper<TablaValorDTO, TablaValor> {

    default TablaValor fromId(TablaValorPK id) {
        if (id == null) {
            return null;
        }
        TablaValor tablaValor = new TablaValor();
        tablaValor.setCodTabla(id.getCodTabla());
        tablaValor.setCodGenerico(id.getCodGenerico());
        tablaValor.setFecValidez(id.getFecValidez());
        tablaValor.setCodRegStatus(id.getCodRegStatus());
        return tablaValor;
    }
}
