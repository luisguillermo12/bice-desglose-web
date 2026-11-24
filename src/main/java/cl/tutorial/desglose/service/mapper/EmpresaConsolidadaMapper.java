package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.domain.EmpresaConsolidada;
import cl.tutorial.desglose.domain.pk.EmpresaConsolidadaPK;
import cl.tutorial.desglose.service.dto.EmpresaConsolidadaDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity EmpresaConsolidada and its DTO EmpresaConsolidadaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmpresaConsolidadaMapper extends EntityMapper<EmpresaConsolidadaDTO, EmpresaConsolidada> {

    default EmpresaConsolidada fromId(EmpresaConsolidadaPK id) {
        if (id == null) {
            return null;
        }
        EmpresaConsolidada empresaConsolidada = new EmpresaConsolidada();
        return empresaConsolidada;
    }
}
