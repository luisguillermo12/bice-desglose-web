package cl.tutorial.desglose.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the /download/informe_resumen_desglose_balance_A4 service.
 */
public class NoteValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String     corConcept;
    private String     conceptName;
    private BigDecimal period1Value;
    private BigDecimal period2Value;
    private Boolean    isTotal;
    private Boolean    isHeader;
    private String     tipoConcepto;

    public Boolean getTotal() {
        return isTotal;
    }

    public void setTotal(Boolean total) {
        isTotal = total;
    }

    public Boolean getHeader() {
        return isHeader;
    }

    public void setHeader(Boolean header) {
        isHeader = header;
    }

    public String getTipoConcepto() {
        return tipoConcepto;
    }

    public void setTipoConcepto(String tipoConcepto) {
        this.tipoConcepto = tipoConcepto;
    }

    public String getCorConcept() {
        return corConcept;
    }

    public void setCorConcept(String corConcept) {
        this.corConcept = corConcept;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public BigDecimal getPeriod1Value() {
        return period1Value;
    }

    public void setPeriod1Value(BigDecimal period1Value) {
        this.period1Value = period1Value;
    }

    public BigDecimal getPeriod2Value() {
        return period2Value;
    }

    public void setPeriod2Value(BigDecimal period2Value) {
        this.period2Value = period2Value;
    }

    public Boolean getIsTotal() {
        return isTotal;
    }

    public void setIsTotal(Boolean total) {
        isTotal = total;
    }

    public Boolean getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(Boolean header) {
        isHeader = header;
    }

    @Override
    public String toString() {
        return "NoteValueDTO{" + "corConcept='" + corConcept + '\'' + ", conceptName='" + conceptName + '\'' + ", period1Value=" + period1Value + ", period2Value=" + period2Value
            + ", isTotal=" + isTotal + ", isHeader=" + isHeader + ", tipoConcepto='" + tipoConcepto + '\'' + '}';
    }
}
