package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 */
public class ValPeriodoDesgloseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCliente;

    @NotNull
    private Integer codTipoDes;

    @NotNull
    private Integer codSubtipoDes;

    @NotNull
    private String codMoneda;

    @NotNull
    private Integer codPlantilla;

    @NotNull
    private LocalDate fecPeriodo;

    @NotNull
    private String codTipoConcep;

    @NotNull
    private Integer corConcepto;

    @NotNull
    private Integer corValor;

    private BigDecimal valConcepto;

    private String glsFrmlConcep;

    private String glsConcepto;

    private BigDecimal valPrcConcep;

    // TODO: Campos extras del sistema legado, ver eliminarlos si no se utilizan
    public String     codConcepto;
    public Integer    numItem;
    public Boolean    hasChange;
    public Boolean    isModif;
    public BigDecimal oldValue;
    public BigDecimal newValue;
    public String     oldFormula;
    public String     newFormula;
    public Integer    newPorcent;

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
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

    public Integer getCorValor() {
        return corValor;
    }

    public void setCorValor(Integer corValor) {
        this.corValor = corValor;
    }

    public BigDecimal getValConcepto() {
        return valConcepto;
    }

    public void setValConcepto(BigDecimal valConcepto) {
        this.valConcepto = valConcepto;
    }

    public String getGlsFrmlConcep() {
        return glsFrmlConcep;
    }

    public void setGlsFrmlConcep(String glsFrmlConcep) {
        this.glsFrmlConcep = glsFrmlConcep;
    }

    public String getGlsConcepto() {
        return glsConcepto;
    }

    public void setGlsConcepto(String glsConcepto) {
        this.glsConcepto = glsConcepto;
    }

    public BigDecimal getValPrcConcep() {
        return valPrcConcep;
    }

    public void setValPrcConcep(BigDecimal valPrcConcep) {
        this.valPrcConcep = valPrcConcep;
    }

    @Override
    public String toString() {
        return "ValPeriodoDesgloseDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda
            + '\'' + ", codPlantilla=" + codPlantilla + ", fecPeriodo=" + fecPeriodo + ", codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + ", corValor="
            + corValor + ", valConcepto=" + valConcepto + ", glsFrmlConcep='" + glsFrmlConcep + '\'' + ", glsConcepto='" + glsConcepto + '\'' + ", valPrcConcep=" + valPrcConcep
            + ", codConcepto='" + codConcepto + '\'' + ", numItem=" + numItem + ", hasChange=" + hasChange + ", isModif=" + isModif + ", oldValue=" + oldValue + ", newValue="
            + newValue + ", oldFormula='" + oldFormula + '\'' + ", newFormula='" + newFormula + '\'' + ", newPorcent=" + newPorcent + '}';
    }
}
