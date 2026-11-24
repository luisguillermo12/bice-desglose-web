package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * A DTO for the DesgloseChequeoInfo entity.
 */
public class DesgloseChequeoInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ZonedDateTime fecCheck;

    private String codTipoInforme;

    private String glsFactorInforme;

    private String codCliente;

    private Integer codTipoDes;

    private Integer codSubtipoDes;

    private String codMoneda;

    private LocalDate fecPeriodo;

    private Integer numRank;

    private LocalDate fecUserModif;

    private String glsUserModif;

    private BigDecimal valDeflacBase;

    private BigDecimal valDeflacPeriodo;

    private BigDecimal valDeflacCalc;

    private BigDecimal valConceptoBd;

    private BigDecimal valConceptoOnline;

    private String glsConceptoFormula;

    private BigDecimal valDif;

    private BigDecimal mtoActivo;

    private BigDecimal mtoPasivoPatr;

    private Integer corConcepto;

    private Integer corValor;

    private String codTipoConcep;

    private String codEstado;

    private String glsObservacion;

    public ZonedDateTime getFecCheck() {
        return fecCheck;
    }

    public void setFecCheck(ZonedDateTime fecCheck) {
        this.fecCheck = fecCheck;
    }

    public String getCodTipoInforme() {
        return codTipoInforme;
    }

    public void setCodTipoInforme(String codTipoInforme) {
        this.codTipoInforme = codTipoInforme;
    }

    public String getGlsFactorInforme() {
        return glsFactorInforme;
    }

    public void setGlsFactorInforme(String glsFactorInforme) {
        this.glsFactorInforme = glsFactorInforme;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public Integer getNumRank() {
        return numRank;
    }

    public void setNumRank(Integer numRank) {
        this.numRank = numRank;
    }

    public LocalDate getFecUserModif() {
        return fecUserModif;
    }

    public void setFecUserModif(LocalDate fecUserModif) {
        this.fecUserModif = fecUserModif;
    }

    public String getGlsUserModif() {
        return glsUserModif;
    }

    public void setGlsUserModif(String glsUserModif) {
        this.glsUserModif = glsUserModif;
    }

    public BigDecimal getValDeflacBase() {
        return valDeflacBase;
    }

    public void setValDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
    }

    public BigDecimal getValDeflacPeriodo() {
        return valDeflacPeriodo;
    }

    public void setValDeflacPeriodo(BigDecimal valDeflacPeriodo) {
        this.valDeflacPeriodo = valDeflacPeriodo;
    }

    public BigDecimal getValDeflacCalc() {
        return valDeflacCalc;
    }

    public void setValDeflacCalc(BigDecimal valDeflacCalc) {
        this.valDeflacCalc = valDeflacCalc;
    }

    public BigDecimal getValConceptoBd() {
        return valConceptoBd;
    }

    public void setValConceptoBd(BigDecimal valConceptoBd) {
        this.valConceptoBd = valConceptoBd;
    }

    public BigDecimal getValConceptoOnline() {
        return valConceptoOnline;
    }

    public void setValConceptoOnline(BigDecimal valConceptoOnline) {
        this.valConceptoOnline = valConceptoOnline;
    }

    public String getGlsConceptoFormula() {
        return glsConceptoFormula;
    }

    public void setGlsConceptoFormula(String glsConceptoFormula) {
        this.glsConceptoFormula = glsConceptoFormula;
    }

    public BigDecimal getValDif() {
        return valDif;
    }

    public void setValDif(BigDecimal valDif) {
        this.valDif = valDif;
    }

    public BigDecimal getMtoActivo() {
        return mtoActivo;
    }

    public void setMtoActivo(BigDecimal mtoActivo) {
        this.mtoActivo = mtoActivo;
    }

    public BigDecimal getMtoPasivoPatr() {
        return mtoPasivoPatr;
    }

    public void setMtoPasivoPatr(BigDecimal mtoPasivoPatr) {
        this.mtoPasivoPatr = mtoPasivoPatr;
    }

    public Integer getCorConcepto() {
        return corConcepto;
    }

    public void setCorConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
    }

    public Integer getCorValor() {
        return corValor;
    }

    public void setCorValor(Integer corValor) {
        this.corValor = corValor;
    }

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getGlsObservacion() {
        return glsObservacion;
    }

    public void setGlsObservacion(String glsObservacion) {
        this.glsObservacion = glsObservacion;
    }

    @Override
    public String toString() {
        return "DesgloseChequeoInfoDTO{" + "fecCheck='" + getFecCheck() + "'" + ", codTipoInforme='" + getCodTipoInforme() + "'" + ", glsFactorInforme='" + getGlsFactorInforme()
            + "'" + ", codCliente='" + getCodCliente() + "'" + ", codTipoDes=" + getCodTipoDes() + ", codSubtipoDes=" + getCodSubtipoDes() + ", codMoneda='" + getCodMoneda() + "'"
            + ", fecPeriodo='" + getFecPeriodo() + "'" + ", numRank=" + getNumRank() + ", fecUserModif='" + getFecUserModif() + "'" + ", glsUserModif='" + getGlsUserModif() + "'"
            + ", valDeflacBase=" + getValDeflacBase() + ", valDeflacPeriodo=" + getValDeflacPeriodo() + ", valDeflacCalc=" + getValDeflacCalc() + ", valConceptoBd="
            + getValConceptoBd() + ", valConceptoOnline=" + getValConceptoOnline() + ", glsConceptoFormula='" + getGlsConceptoFormula() + "'" + ", valDif=" + getValDif()
            + ", mtoActivo=" + getMtoActivo() + ", mtoPasivoPatr=" + getMtoPasivoPatr() + ", corConcepto=" + getCorConcepto() + ", corValor=" + getCorValor() + ", codTipoConcep='"
            + getCodTipoConcep() + "'" + ", codEstado='" + getCodEstado() + "'" + ", glsObservacion='" + getGlsObservacion() + "'" + "}";
    }
}
