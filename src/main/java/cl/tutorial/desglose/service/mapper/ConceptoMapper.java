package cl.tutorial.desglose.service.mapper;

import cl.tutorial.desglose.conexiones.bctdesglose.domain.Concepto;
import cl.tutorial.desglose.conexiones.bctdesglose.domain.pk.ConceptoPK;
import cl.tutorial.desglose.service.dto.ConceptoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity Concepto and its DTO ConceptoDTO.
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConceptoMapper extends EntityMapper<ConceptoDTO, Concepto> {

    default Concepto fromId(ConceptoPK id) {
        if (id == null) {
            return null;
        }
        Concepto concepto = new Concepto();
        concepto.setCodTipoConcep(id.getCodTipoConcep());
        concepto.setCorConcepto(id.getCorConcepto());
        concepto.setCodTipoPlantilla(id.getCodTipoPlantilla());
        return concepto;
    }

    @Mapping(source = "entity", target = "glsFormulaVal", qualifiedByName = "glsFormulaVal")
    @Mapping(source = "entity", target = "glsFormulaPrc", qualifiedByName = "glsFormulaPrc")
    @Mapping(source = "entity", target = "glsFormulaDef", qualifiedByName = "glsFormulaDef")
    @Mapping(source = "entity", target = "glsFormulaValWeb", qualifiedByName = "glsFormulaValWeb")
    @Mapping(source = "entity", target = "glsFormulaPrcWeb", qualifiedByName = "glsFormulaPrcWeb")
    @Mapping(source = "entity", target = "glsFormulaDefWeb", qualifiedByName = "glsFormulaDefWeb")
    ConceptoDTO toDto(Concepto entity);

    @Mapping(source = "dto", target = "glsFormulaVal", qualifiedByName = "glsFormulaValInverse")
    @Mapping(source = "dto", target = "glsFormulaPrc", qualifiedByName = "glsFormulaPrcInverse")
    @Mapping(source = "dto", target = "glsFormulaDef", qualifiedByName = "glsFormulaDefInverse")
    @Mapping(source = "dto", target = "glsFormulaValWeb", qualifiedByName = "glsFormulaValWebInverse")
    @Mapping(source = "dto", target = "glsFormulaPrcWeb", qualifiedByName = "glsFormulaPrcWebInverse")
    @Mapping(source = "dto", target = "glsFormulaDefWeb", qualifiedByName = "glsFormulaDefWebInverse")
    Concepto toEntity(ConceptoDTO dto);

    @Named("glsFormulaVal")
    default String glsFormulaValToExcelFormulaVal(Concepto entity) {
        String newValue = entity.getGlsFormulaVal();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = "=" + newValue;
        }
        return newValue;
    }

    @Named("glsFormulaPrc")
    default String glsFormulaPrcToExcelFormulaPrc(Concepto entity) {
        String newValue = entity.getGlsFormulaPrc();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = "=" + newValue;
        }
        return newValue;
    }

    @Named("glsFormulaDef")
    default String glsFormulaDefToExcelFormulaDef(Concepto entity) {
        String newValue = entity.getGlsFormulaDef();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = "=" + newValue;
        }
        return newValue;
    }

    @Named("glsFormulaValWeb")
    default String glsFormulaValToExcelFormulaValWeb(Concepto entity) {
        String newValue = entity.getGlsFormulaValWeb();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = "=" + newValue;
        }
        return newValue;
    }

    @Named("glsFormulaPrcWeb")
    default String glsFormulaPrcToExcelFormulaPrcWeb(Concepto entity) {
        String newValue = entity.getGlsFormulaPrcWeb();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = "=" + newValue;
        }
        return newValue;
    }

    @Named("glsFormulaDefWeb")
    default String glsFormulaDefToExcelFormulaDefWeb(Concepto entity) {
        String newValue = entity.getGlsFormulaDefWeb();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = "=" + newValue;
        }
        return newValue;
    }

    @Named("glsFormulaValInverse")
    default String glsFormulaValToExcelFormulaValInverse(ConceptoDTO dto) {
        String newValue = dto.getGlsFormulaVal();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = newValue.replace("=", "");
        }
        return newValue;
    }

    @Named("glsFormulaPrcInverse")
    default String glsFormulaPrcToExcelFormulaPrcInverse(ConceptoDTO dto) {
        String newValue = dto.getGlsFormulaPrc();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = newValue.replace("=", "");
        }
        return newValue;
    }

    @Named("glsFormulaDefInverse")
    default String glsFormulaDefToExcelFormulaDefInverse(ConceptoDTO dto) {
        String newValue = dto.getGlsFormulaDef();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = newValue.replace("=", "");
        }
        return newValue;
    }

    @Named("glsFormulaValWebInverse")
    default String glsFormulaValToExcelFormulaValWebInverse(ConceptoDTO dto) {
        String newValue = dto.getGlsFormulaValWeb();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = newValue.replace("=", "");
        }
        return newValue;
    }

    @Named("glsFormulaPrcWebInverse")
    default String glsFormulaPrcToExcelFormulaPrcWebInverse(ConceptoDTO dto) {
        String newValue = dto.getGlsFormulaPrcWeb();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = newValue.replace("=", "");
        }
        return newValue;
    }

    @Named("glsFormulaDefWebInverse")
    default String glsFormulaDefToExcelFormulaDefWebInverse(ConceptoDTO dto) {
        String newValue = dto.getGlsFormulaDefWeb();
        if (newValue != null && !newValue.trim().isEmpty()) {
            newValue = newValue.replace("=", "");
        }
        return newValue;
    }
}
