package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.Cuadre;
import cl.tutorial.desglose.domain.pk.CuadrePK;
import cl.tutorial.desglose.service.dto.CuadreDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Cuadre and its DTO CuadreDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CuadreMapper extends EntityMapper<CuadreDTO, Cuadre> {

    default Cuadre fromId(CuadrePK id) {
        if (id == null) {
            return null;
        }
        Cuadre cuadre = new Cuadre();
        cuadre.setCodTpoConcep1(id.getCodTpoConcep1());
        cuadre.setCodTpoConcep2(id.getCodTpoConcep2());
        cuadre.setCorConcepto1(id.getCorConcepto1());
        cuadre.setCorConcepto2(id.getCorConcepto2());
        return cuadre;
    }
}
