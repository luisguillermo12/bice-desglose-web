package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the BreakdownFilterResponseDTO.
 */
public class BreakdownFilterConceptDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<ConceptoDTO>     conceptoDTOList   ;
    public  List<BreakdownValidationErrorDTO> validationErrorDTOS;

    public List<ConceptoDTO> getConceptoDTOList() {
        return conceptoDTOList;
    }

    public void setConceptoDTOList(List<ConceptoDTO> conceptoDTOList) {
        this.conceptoDTOList = conceptoDTOList;
    }

    public List<BreakdownValidationErrorDTO> getValidationErrorDTOS() {
        return validationErrorDTOS;
    }

    public void setValidationErrorDTOS(List<BreakdownValidationErrorDTO> validationErrorDTOS) {
        this.validationErrorDTOS = validationErrorDTOS;
    }

    @Override
    public String toString() {
        return "BreakdownFilterConceptDTO{" + "conceptoDTOList=" + conceptoDTOList + ", validationErrorDTOS=" + validationErrorDTOS + '}';
    }
}
