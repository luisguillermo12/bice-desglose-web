package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for extra data for Desglose.
 */
public class ExtraDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer                             numPeriodosLoaded;
    public Integer                             numPeriodosConInfo;
    public List<Integer>                       monthsPeriod                   = new ArrayList<>();
    public List<BigDecimal>                    deflatorPeriod                 = new ArrayList<>();
    public List<Integer>                       periodRating                   = new ArrayList<>();
    public List<LocalDate>                     fecPeriodo                     = new ArrayList<>();
    public List<LocalDate>                     fecPeriodoSbif                 = new ArrayList<>();
    public List<BreakdownAuditorDTO>           auditors                       = new ArrayList<>();
    public Boolean                             enProceso;
    public Boolean                             validado;
    public Boolean                             isAutoriced;
    public Boolean                             isTemporal;
    public String                              userOpen;
    public String                              nomUserOpen;
    public Boolean                             requireValidation;
    public Boolean                             canValidate;
    public Boolean                             canLoadSBIF;
    /**
     * Conceptos de impresión en forma de lista de objetos en vez de una matriz.
     */
    public List<ConceptoImpresionDTO>          matrizConImp                   = new ArrayList<>();
    public List<PeriodoDTO>                    periodos                       = new ArrayList<>();
    public List<ConceptoDTO>                   conceptos                      = new ArrayList<>();
    public List<ConceptoDTO>                   conceptos1                      = new ArrayList<>();
    public List<ConceptoDTO>                   conceptos2                      = new ArrayList<>();
    public List<EmpresaConsolidadaDTO>         empresaConsolidadas            = new ArrayList<>();
    public List<PeriodoDesgConsolDTO>          periodoDesgConsols             = new ArrayList<>();
    /**
     * Unifica los datos de empresaConsolidadas y periodoDesgConsols
     */
    public List<EmpConsolPeriodoDesgConsolDTO> empConsolPeriodoDesgConsolList = new ArrayList<>();
    public String[][]                          data;
    public String[][]                          data1;
    public String[][]                          visualData;
    public String[][]                          visualData1;
}
