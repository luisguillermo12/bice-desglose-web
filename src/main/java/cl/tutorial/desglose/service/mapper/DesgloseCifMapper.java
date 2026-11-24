package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseCif;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.Desglose;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import cl.tutorial.desglose.service.dto.DesgloseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

/**
 * Mapper for the entity Desglose and its DTO DesgloseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DesgloseCifMapper extends EntityMapper<DesgloseDTO, DesgloseCif> {

    default DesgloseCif fromId(NewCommonPK id) {
        if (id == null) {
            return null;
        }
        DesgloseCif desglose = new DesgloseCif();
        desglose.setCodPlantilla(id.getCodPlantilla());
        desglose.setCodCliente(id.getCodCliente());
        desglose.setCodTipoDes(id.getCodTipoDes());
        desglose.setCodSubtipoDes(id.getCodSubtipoDes());
        desglose.setCodMoneda(id.getCodMoneda());
        return desglose;
    }

    @Mapping(source = "entity", target = "valIva", qualifiedByName = "valIvaToDTO")
    DesgloseDTO toDto(Desglose entity);

    @Mapping(source = "dto", target = "valIva", qualifiedByName = "valIvaToEntity")
    DesgloseCif toEntity(DesgloseDTO dto);

    @Named("valIvaToDTO")
    default BigDecimal transformValIvaToDTO(Desglose entity) {
        BigDecimal newValue = entity.getValIva();
        if (newValue != null) {
            newValue = newValue.multiply(Constants.BIG_DECIMAL_VALUE_100);
        }
        return newValue;
    }

    @Named("valIvaToEntity")
    default BigDecimal transformValIvaToEntity(DesgloseDTO dto) {
        BigDecimal newValue = dto.getValIva();
        if (newValue != null) {
            newValue = newValue.divide(Constants.BIG_DECIMAL_VALUE_100);
        }
        return newValue;
    }
}
