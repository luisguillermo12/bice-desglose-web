package cl.tutorial.desglose.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the /download/informe-ingreso service.
 */
public class TableValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String corConcept;
    private String conceptName;
    private BigDecimal period1Value;
    private BigDecimal period1Percent;
    private String  period1Fecha;
    private BigDecimal period2Value;
    private BigDecimal period2Percent;
    private String  period2Fecha;
    private BigDecimal period3Value;
    private BigDecimal period3Percent;
    private String  period3Fecha;
    private BigDecimal period4Value;
    private BigDecimal period4Percent;
    private String  period4Fecha;
    private Boolean isTotal;
    private Boolean isHeader;
    private String codTipoConcepto;
    private Integer corConcept1;
    private Integer flgIsBold;
    private Integer flgIsEnclosed;
    private Integer flgIsImpreso;
    private Integer numDecPrint;
    private Integer flgNonegativ;
    private Integer flgIsTotal;
    private String codTipoRegis;
    private Integer corValor;
    private Boolean hiddeRow;
    private String formatPathern;

    public Integer getFlgIsImpreso() {
        return flgIsImpreso;
    }

    public void setFlgIsImpreso(Integer flgIsImpreso) {
        this.flgIsImpreso = flgIsImpreso;
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

    public BigDecimal getPeriod1Percent() {
        return period1Percent;
    }

    public void setPeriod1Percent(BigDecimal period1Percent) {
        this.period1Percent = period1Percent;
    }

    public BigDecimal getPeriod2Value() {
        return period2Value;
    }

    public void setPeriod2Value(BigDecimal period2Value) {
        this.period2Value = period2Value;
    }

    public BigDecimal getPeriod2Percent() {
        return period2Percent;
    }

    public void setPeriod2Percent(BigDecimal period2Percent) {
        this.period2Percent = period2Percent;
    }

    public BigDecimal getPeriod3Value() {
        return period3Value;
    }

    public void setPeriod3Value(BigDecimal period3Value) {
        this.period3Value = period3Value;
    }

    public BigDecimal getPeriod3Percent() {
        return period3Percent;
    }

    public void setPeriod3Percent(BigDecimal period3Percent) {
        this.period3Percent = period3Percent;
    }

    public BigDecimal getPeriod4Value() {
        return period4Value;
    }

    public void setPeriod4Value(BigDecimal period4Value) {
        this.period4Value = period4Value;
    }

    public BigDecimal getPeriod4Percent() {
        return period4Percent;
    }

    public void setPeriod4Percent(BigDecimal period4Percent) {
        this.period4Percent = period4Percent;
    }

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

    public String getCodTipoConcepto() {
        return codTipoConcepto;
    }

    public void setCodTipoConcepto(String codTipoConcepto) {
        this.codTipoConcepto = codTipoConcepto;
    }

    public Integer getCorConcept1() {
        return corConcept1;
    }

    public void setCorConcept1(Integer corConcept1) {
        this.corConcept1 = corConcept1;
    }

    public Integer getFlgIsBold() {
        return flgIsBold;
    }

    public void setFlgIsBold(Integer flgIsBold) {
        this.flgIsBold = flgIsBold;
    }

    public Integer getFlgIsEnclosed() {
        return flgIsEnclosed;
    }

    public void setFlgIsEnclosed(Integer flgIsEnclosed) {
        this.flgIsEnclosed = flgIsEnclosed;
    }

    public String getPeriod1Fecha() {
        return period1Fecha;
    }

    public void setPeriod1Fecha(String period1Fecha) {
        this.period1Fecha = period1Fecha;
    }

    public String getPeriod2Fecha() {
        return period2Fecha;
    }

    public void setPeriod2Fecha(String period2Fecha) {
        this.period2Fecha = period2Fecha;
    }

    public String getPeriod3Fecha() {
        return period3Fecha;
    }

    public void setPeriod3Fecha(String period3Fecha) {
        this.period3Fecha = period3Fecha;
    }

    public String getPeriod4Fecha() {
        return period4Fecha;
    }

    public void setPeriod4Fecha(String period4Fecha) {
        this.period4Fecha = period4Fecha;
    }

    public Integer getNumDecPrint() {
        return numDecPrint;
    }

    public void setNumDecPrint(Integer numDecPrint) {
        this.numDecPrint = numDecPrint;
    }

    public Integer getFlgNonegativ() {
        return flgNonegativ;
    }

    public void setFlgNonegativ(Integer flgNonegativ) {
        this.flgNonegativ = flgNonegativ;
    }

    public void setFlgIsTotal(Integer flgIsTotal) {
        this.flgIsTotal = flgIsTotal;
    }

    public Integer getFlgIsTotal() {
        return flgIsTotal;
    }

    public String getCodTipoRegis() {
        return codTipoRegis;
    }

    public void setCodTipoRegis(String codTipoRegis) {
        this.codTipoRegis = codTipoRegis;
    }

    public Integer getCorValor() {
        return corValor;
    }

    public void setCorValor(Integer corValor) {
        this.corValor = corValor;
    }

    public Boolean getHiddeRow() {
        return hiddeRow;
    }

    public void setHiddeRow(Boolean hiddeRow) {
        this.hiddeRow = hiddeRow;
    }

    public String getFormatPathern() {
        return formatPathern;
    }

    public void setFormatPathern(String formatPathern) {
        this.formatPathern = formatPathern;
    }

    @Override
    public String toString() {
        return "TableValueDTO{" + "corConcept='" + corConcept + '\'' + ", conceptName='" + conceptName + '\'' + ", period1Value=" + period1Value + ", period1Percent="
            + period1Percent + ", period1Fecha='" + period1Fecha + '\'' + ", period2Value=" + period2Value + ", period2Percent=" + period2Percent + ", period2Fecha='"
            + period2Fecha + '\'' + ", period3Value=" + period3Value + ", period3Percent=" + period3Percent + ", period3Fecha='" + period3Fecha + '\'' + ", period4Value="
            + period4Value + ", period4Percent=" + period4Percent + ", period4Fecha='" + period4Fecha + '\'' + ", isTotal=" + isTotal + ", isHeader=" + isHeader
            + ", codTipoConcepto='" + codTipoConcepto + '\'' + ", corConcept1=" + corConcept1 + ", flgIsBold=" + flgIsBold + ", flgIsEnclosed=" + flgIsEnclosed + ", flgIsImpreso="
            + flgIsImpreso + ", numDecPrint=" + numDecPrint + ", flgNonegativ=" + flgNonegativ + ", flgIsTotal=" + flgIsTotal + ", codTipoRegis='" + codTipoRegis + '\''
            + ", corValor=" + corValor + ", hiddeRow=" + hiddeRow + ", formatPathern='" + formatPathern + '\'' + '}';
    }
}
