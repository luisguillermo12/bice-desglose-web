package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the Concepto filters.
 */
public class ConceptoRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public Integer      templateCode;
    public Integer      templateCodeRelacionada;
    public Integer      flgConActivo;
    public String       conceptCode;
    public List<String> conceptTypeCodesIn;
    public List<String> orderByFields;
    /**
     * ASC or DESC. Default "ASC"
     */
    public String       orderByDirection = "ASC";
    public String       columnLetterGlsFormulaVal1;
    public String       columnLetterGlsFormulaVal2;
    public String       columnLetterGlsFormulaVal3;
    public String       columnLetterGlsFormulaVal4;
    public String       columnLetterGlsFormulaPrc1;
    public String       columnLetterGlsFormulaPrc2;
    public String       columnLetterGlsFormulaPrc3;
    public String       columnLetterGlsFormulaPrc4;
    public String       columnLetterGlsFormulaDef1;
    public String       columnLetterGlsFormulaDef2;
    public String       columnLetterGlsFormulaDef3;
    public String       columnLetterGlsFormulaDef4;
    public String       cellNumMeses1;
    public String       cellNumMeses2;
    public String       cellNumMeses3;
    public String       cellNumMeses4;
    public String       cellIva;
    public String       cellDeflacBase;
    public String       cellDeflacPeriod1;
    public String       cellDeflacPeriod2;
    public String       cellDeflacPeriod3;
    public String       cellDeflacPeriod4;

    @Override
    public String toString() {
        return "ConceptoRequestDTO{" + "templateCode=" + templateCode + ", flgConActivo=" + flgConActivo + ", conceptCode='" + conceptCode + '\'' + ", conceptTypeCodesIn="
            + conceptTypeCodesIn + ", orderByFields=" + orderByFields + ", orderByDirection='" + orderByDirection + '\'' + ", columnLetterGlsFormulaVal1='"
            + columnLetterGlsFormulaVal1 + '\'' + ", columnLetterGlsFormulaVal2='" + columnLetterGlsFormulaVal2 + '\'' + ", columnLetterGlsFormulaVal3='"
            + columnLetterGlsFormulaVal3 + '\'' + ", columnLetterGlsFormulaVal4='" + columnLetterGlsFormulaVal4 + '\'' + ", columnLetterGlsFormulaPrc1='"
            + columnLetterGlsFormulaPrc1 + '\'' + ", columnLetterGlsFormulaPrc2='" + columnLetterGlsFormulaPrc2 + '\'' + ", columnLetterGlsFormulaPrc3='"
            + columnLetterGlsFormulaPrc3 + '\'' + ", columnLetterGlsFormulaPrc4='" + columnLetterGlsFormulaPrc4 + '\'' + ", columnLetterGlsFormulaDef1='"
            + columnLetterGlsFormulaDef1 + '\'' + ", columnLetterGlsFormulaDef2='" + columnLetterGlsFormulaDef2 + '\'' + ", columnLetterGlsFormulaDef3='"
            + columnLetterGlsFormulaDef3 + '\'' + ", columnLetterGlsFormulaDef4='" + columnLetterGlsFormulaDef4 + '\'' + ", cellNumMeses1='" + cellNumMeses1 + '\''
            + ", cellNumMeses2='" + cellNumMeses2 + '\'' + ", cellNumMeses3='" + cellNumMeses3 + '\'' + ", cellNumMeses4='" + cellNumMeses4 + '\'' + ", cellIva='" + cellIva + '\''
            + ", cellDeflacBase='" + cellDeflacBase + '\'' + ", cellDeflacPeriod1='" + cellDeflacPeriod1 + '\'' + ", cellDeflacPeriod2='" + cellDeflacPeriod2 + '\''
            + ", cellDeflacPeriod3='" + cellDeflacPeriod3 + '\'' + ", cellDeflacPeriod4='" + cellDeflacPeriod4 + '\'' + '}';
    }
}
