package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Concepto entity.
 */
public class ConceptoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codTipoConcep;

    @NotNull
    private Integer corConcepto;

    @NotNull
    private Integer codTipoPlantilla;

    @NotNull
    private String codConcepto;

    @NotNull
    private String glsConcepto;

    @NotNull
    private Integer numSecuencia;

    private Integer numLvlIndent;

    private String codTipoRegis;

    private String glsFormulaValOriginal;
    private String glsFormulaVal;
    private String glsFormulaVal2;
    private String glsFormulaVal3;
    private String glsFormulaVal4;

    private String glsFormulaPrcOriginal;
    private String glsFormulaPrc;
    private String glsFormulaPrc2;
    private String glsFormulaPrc3;
    private String glsFormulaPrc4;

    private String glsFormulaDefOriginal;
    private String glsFormulaDef;
    private String glsFormulaDef2;
    private String glsFormulaDef3;
    private String glsFormulaDef4;

    @NotNull
    private Integer flgIsTotal;

    @NotNull
    private Integer flgUserModif;

    @NotNull
    private Integer flgConActivo;

    private Integer numDecimales;

    @NotNull
    private Integer flgHaceAjust;

    @NotNull
    private Integer flgIsBold;

    @NotNull
    private Integer flgIsUcase;

    @NotNull
    private Integer flgIsEnclosed;

    @NotNull
    private Integer flgIsPrinted;

    @NotNull
    private Integer numDecPrint;

    @NotNull
    private Integer flgNonegativ;

    @NotNull
    private Integer flgAfterspace;

    // Campos nuevos que vienen desde la UI para ser utilizados en los SP de guardar ValPrd
    private Integer    corValor;
    private BigDecimal newValue;
    private String     newFormula;
    private String     newGlosa;
    private BigDecimal newPorcent;

    private String expValidacionVal;
    private String expValidacionPrc;
    private String expValidacionDef;
    private String msgErrValidacionVal;
    private String msgErrValidacionPrc;
    private String msgErrValidacionDef;
    private String unidad;

    // nuevos campos para desglose nuevo
    private String glsFormulaValWeb;
    private String glsFormulaPrcWeb;
    private String glsFormulaDefWeb;

    public String getGlsFormulaVal2() {
        return glsFormulaVal2;
    }

    public void setGlsFormulaVal2(String glsFormulaVal2) {
        this.glsFormulaVal2 = glsFormulaVal2;
    }

    public String getGlsFormulaVal3() {
        return glsFormulaVal3;
    }

    public void setGlsFormulaVal3(String glsFormulaVal3) {
        this.glsFormulaVal3 = glsFormulaVal3;
    }

    public String getGlsFormulaVal4() {
        return glsFormulaVal4;
    }

    public void setGlsFormulaVal4(String glsFormulaVal4) {
        this.glsFormulaVal4 = glsFormulaVal4;
    }

    public String getGlsFormulaPrc2() {
        return glsFormulaPrc2;
    }

    public void setGlsFormulaPrc2(String glsFormulaPrc2) {
        this.glsFormulaPrc2 = glsFormulaPrc2;
    }

    public String getGlsFormulaPrc3() {
        return glsFormulaPrc3;
    }

    public void setGlsFormulaPrc3(String glsFormulaPrc3) {
        this.glsFormulaPrc3 = glsFormulaPrc3;
    }

    public String getGlsFormulaPrc4() {
        return glsFormulaPrc4;
    }

    public void setGlsFormulaPrc4(String glsFormulaPrc4) {
        this.glsFormulaPrc4 = glsFormulaPrc4;
    }

    public String getGlsFormulaDef2() {
        return glsFormulaDef2;
    }

    public void setGlsFormulaDef2(String glsFormulaDef2) {
        this.glsFormulaDef2 = glsFormulaDef2;
    }

    public String getGlsFormulaDef3() {
        return glsFormulaDef3;
    }

    public void setGlsFormulaDef3(String glsFormulaDef3) {
        this.glsFormulaDef3 = glsFormulaDef3;
    }

    public String getGlsFormulaDef4() {
        return glsFormulaDef4;
    }

    public void setGlsFormulaDef4(String glsFormulaDef4) {
        this.glsFormulaDef4 = glsFormulaDef4;
    }

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public Integer getCorConcepto() {
        return corConcepto;
    }

    public void setCorConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
    }

    public Integer getCodTipoPlantilla() {
        return codTipoPlantilla;
    }

    public void setCodTipoPlantilla(Integer codTipoPlantilla) {
        this.codTipoPlantilla = codTipoPlantilla;
    }

    public String getCodConcepto() {
        return codConcepto;
    }

    public void setCodConcepto(String codConcepto) {
        this.codConcepto = codConcepto;
    }

    public String getGlsConcepto() {
        return glsConcepto;
    }

    public void setGlsConcepto(String glsConcepto) {
        this.glsConcepto = glsConcepto;
    }

    public Integer getNumSecuencia() {
        return numSecuencia;
    }

    public void setNumSecuencia(Integer numSecuencia) {
        this.numSecuencia = numSecuencia;
    }

    public Integer getNumLvlIndent() {
        return numLvlIndent;
    }

    public void setNumLvlIndent(Integer numLvlIndent) {
        this.numLvlIndent = numLvlIndent;
    }

    public String getCodTipoRegis() {
        return codTipoRegis;
    }

    public void setCodTipoRegis(String codTipoRegis) {
        this.codTipoRegis = codTipoRegis;
    }

    public String getGlsFormulaVal() {
        return glsFormulaVal;
    }

    public void setGlsFormulaVal(String glsFormulaVal) {
        this.glsFormulaVal = glsFormulaVal;
    }

    public String getGlsFormulaPrc() {
        return glsFormulaPrc;
    }

    public void setGlsFormulaPrc(String glsFormulaPrc) {
        this.glsFormulaPrc = glsFormulaPrc;
    }

    public String getGlsFormulaDef() {
        return glsFormulaDef;
    }

    public void setGlsFormulaDef(String glsFormulaDef) {
        this.glsFormulaDef = glsFormulaDef;
    }

    public Integer getFlgIsTotal() {
        return flgIsTotal;
    }

    public void setFlgIsTotal(Integer flgIsTotal) {
        this.flgIsTotal = flgIsTotal;
    }

    public Integer getFlgUserModif() {
        return flgUserModif;
    }

    public void setFlgUserModif(Integer flgUserModif) {
        this.flgUserModif = flgUserModif;
    }

    public Integer getFlgConActivo() {
        return flgConActivo;
    }

    public void setFlgConActivo(Integer flgConActivo) {
        this.flgConActivo = flgConActivo;
    }

    public Integer getNumDecimales() {
        return numDecimales;
    }

    public void setNumDecimales(Integer numDecimales) {
        this.numDecimales = numDecimales;
    }

    public Integer getFlgHaceAjust() {
        return flgHaceAjust;
    }

    public void setFlgHaceAjust(Integer flgHaceAjust) {
        this.flgHaceAjust = flgHaceAjust;
    }

    public Integer getFlgIsBold() {
        return flgIsBold;
    }

    public void setFlgIsBold(Integer flgIsBold) {
        this.flgIsBold = flgIsBold;
    }

    public Integer getFlgIsUcase() {
        return flgIsUcase;
    }

    public void setFlgIsUcase(Integer flgIsUcase) {
        this.flgIsUcase = flgIsUcase;
    }

    public Integer getFlgIsEnclosed() {
        return flgIsEnclosed;
    }

    public void setFlgIsEnclosed(Integer flgIsEnclosed) {
        this.flgIsEnclosed = flgIsEnclosed;
    }

    public Integer getFlgIsPrinted() {
        return flgIsPrinted;
    }

    public void setFlgIsPrinted(Integer flgIsPrinted) {
        this.flgIsPrinted = flgIsPrinted;
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

    public Integer getFlgAfterspace() {
        return flgAfterspace;
    }

    public void setFlgAfterspace(Integer flgAfterspace) {
        this.flgAfterspace = flgAfterspace;
    }

    public Integer getCorValor() {
        return corValor;
    }

    public void setCorValor(Integer corValor) {
        this.corValor = corValor;
    }

    public BigDecimal getNewValue() {
        return newValue;
    }

    public void setNewValue(BigDecimal newValue) {
        this.newValue = newValue;
    }

    public String getNewFormula() {
        return newFormula;
    }

    public void setNewFormula(String newFormula) {
        this.newFormula = newFormula;
    }

    public String getNewGlosa() {
        return newGlosa;
    }

    public void setNewGlosa(String newGlosa) {
        this.newGlosa = newGlosa;
    }

    public BigDecimal getNewPorcent() {
        return newPorcent;
    }

    public void setNewPorcent(BigDecimal newPorcent) {
        this.newPorcent = newPorcent;
    }

    public String getExpValidacionVal() {
        return expValidacionVal;
    }

    public void setExpValidacionVal(String expValidacionVal) {
        this.expValidacionVal = expValidacionVal;
    }

    public String getExpValidacionPrc() {
        return expValidacionPrc;
    }

    public void setExpValidacionPrc(String expValidacionPrc) {
        this.expValidacionPrc = expValidacionPrc;
    }

    public String getExpValidacionDef() {
        return expValidacionDef;
    }

    public void setExpValidacionDef(String expValidacionDef) {
        this.expValidacionDef = expValidacionDef;
    }

    public String getMsgErrValidacionVal() {
        return msgErrValidacionVal;
    }

    public void setMsgErrValidacionVal(String msgErrValidacionVal) {
        this.msgErrValidacionVal = msgErrValidacionVal;
    }

    public String getMsgErrValidacionPrc() {
        return msgErrValidacionPrc;
    }

    public void setMsgErrValidacionPrc(String msgErrValidacionPrc) {
        this.msgErrValidacionPrc = msgErrValidacionPrc;
    }

    public String getMsgErrValidacionDef() {
        return msgErrValidacionDef;
    }

    public void setMsgErrValidacionDef(String msgErrValidacionDef) {
        this.msgErrValidacionDef = msgErrValidacionDef;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    // 1
    public String getGlsFormulaValWeb() {
        return glsFormulaValWeb;
    }

    public void setGlsFormulaValWeb(String glsFormulaValWeb) {
        this.glsFormulaValWeb = glsFormulaValWeb;
    }

    // 2
    public String getGlsFormulaPrcWeb() {
        return glsFormulaPrcWeb;
    }

    public void setGlsFormulaPrcWeb(String glsFormulaPrcWeb) {
        this.glsFormulaPrcWeb = glsFormulaPrcWeb;
    }

    // 3
    public String getGlsFormulaDefWeb() {
        return glsFormulaDefWeb;
    }

    public void setGlsFormulaDefWeb(String glsFormulaDefWeb) {
        this.glsFormulaDefWeb = glsFormulaDefWeb;
    }

    public String getGlsFormulaValOriginal() {
        return glsFormulaValOriginal;
    }

    public void setGlsFormulaValOriginal(String glsFormulaValOriginal) {
        this.glsFormulaValOriginal = glsFormulaValOriginal;
    }

    public String getGlsFormulaPrcOriginal() {
        return glsFormulaPrcOriginal;
    }

    public void setGlsFormulaPrcOriginal(String glsFormulaPrcOriginal) {
        this.glsFormulaPrcOriginal = glsFormulaPrcOriginal;
    }

    public String getGlsFormulaDefOriginal() {
        return glsFormulaDefOriginal;
    }

    public void setGlsFormulaDefOriginal(String glsFormulaDefOriginal) {
        this.glsFormulaDefOriginal = glsFormulaDefOriginal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConceptoDTO))
            return false;
        ConceptoDTO that = (ConceptoDTO) o;
        return Objects.equals(codTipoConcep, that.codTipoConcep) && Objects.equals(corConcepto, that.corConcepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTipoConcep, corConcepto);
    }

    @Override
    public String toString() {
        return "ConceptoDTO{" + "codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + ", codTipoPlantilla=" + codTipoPlantilla + ", codConcepto='"
            + codConcepto + '\'' + ", glsConcepto='" + glsConcepto + '\'' + ", numSecuencia=" + numSecuencia + ", numLvlIndent=" + numLvlIndent + ", codTipoRegis='" + codTipoRegis
            + '\'' + ", glsFormulaValOriginal='" + glsFormulaValOriginal + '\'' + ", glsFormulaVal='" + glsFormulaVal + '\'' + ", glsFormulaVal2='" + glsFormulaVal2 + '\''
            + ", glsFormulaVal3='" + glsFormulaVal3 + '\'' + ", glsFormulaVal4='" + glsFormulaVal4 + '\'' + ", glsFormulaPrcOriginal='" + glsFormulaPrcOriginal + '\''
            + ", glsFormulaPrc='" + glsFormulaPrc + '\'' + ", glsFormulaPrc2='" + glsFormulaPrc2 + '\'' + ", glsFormulaPrc3='" + glsFormulaPrc3 + '\'' + ", glsFormulaPrc4='"
            + glsFormulaPrc4 + '\'' + ", glsFormulaDefOriginal='" + glsFormulaDefOriginal + '\'' + ", glsFormulaDef='" + glsFormulaDef + '\'' + ", glsFormulaDef2='"
            + glsFormulaDef2 + '\'' + ", glsFormulaDef3='" + glsFormulaDef3 + '\'' + ", glsFormulaDef4='" + glsFormulaDef4 + '\'' + ", flgIsTotal=" + flgIsTotal + ", flgUserModif="
            + flgUserModif + ", flgConActivo=" + flgConActivo + ", numDecimales=" + numDecimales + ", flgHaceAjust=" + flgHaceAjust + ", flgIsBold=" + flgIsBold + ", flgIsUcase="
            + flgIsUcase + ", flgIsEnclosed=" + flgIsEnclosed + ", flgIsPrinted=" + flgIsPrinted + ", numDecPrint=" + numDecPrint + ", flgNonegativ=" + flgNonegativ
            + ", flgAfterspace=" + flgAfterspace + ", corValor=" + corValor + ", newValue=" + newValue + ", newFormula='" + newFormula + '\'' + ", newGlosa='" + newGlosa + '\''
            + ", newPorcent=" + newPorcent + ", expValidacionVal='" + expValidacionVal + '\'' + ", expValidacionPrc='" + expValidacionPrc + '\'' + ", expValidacionDef='"
            + expValidacionDef + '\'' + ", msgErrValidacionVal='" + msgErrValidacionVal + '\'' + ", msgErrValidacionPrc='" + msgErrValidacionPrc + '\'' + ", msgErrValidacionDef='"
            + msgErrValidacionDef + '\'' + ", unidad='" + unidad + '\'' + ", glsFormulaValWeb='" + glsFormulaValWeb + '\'' + ", glsFormulaPrcWeb='" + glsFormulaPrcWeb + '\''
            + ", glsFormulaDefWeb='" + glsFormulaDefWeb + '\'' + '}';
    }
}
