package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the ValPeriodoDesglose entity.
 */
public class ValPeriodoProyeccionDesgloseDTO implements Serializable {

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
    private String codTipoConcepPry;

    @NotNull
    private Integer corConceptoPry;

    @NotNull
    private Integer codPlantillaPry;

    @NotNull
    private LocalDate fecPeriodoPry;

    private BigDecimal valConceptoPry;

    private String glsFrmlConcepPry;

    public String codConcepto;


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


    public Integer getCodPlantillaPry() {
        return codPlantillaPry;
    }

    public void setCodPlantillaPry(Integer codPlantillaPry) {
        this.codPlantillaPry = codPlantillaPry;
    }

    public LocalDate getFecPeriodoPry() {
        return fecPeriodoPry;
    }

    public void setFecPeriodoPry(LocalDate fecPeriodoPry) {
        this.fecPeriodoPry = fecPeriodoPry;
    }

    public String getCodTipoConcepPry() {
        return codTipoConcepPry;
    }

    public void setCodTipoConcepPry(String codTipoConcepPry) {
        this.codTipoConcepPry = codTipoConcepPry;
    }

    public Integer getCorConceptoPry() {
        return corConceptoPry;
    }

    public void setCorConceptoPry(Integer corConceptoPry) {
        this.corConceptoPry = corConceptoPry;
    }

    public BigDecimal getValConceptoPry() {
        return valConceptoPry;
    }

    public void setValConceptoPry(BigDecimal valConceptoPry) {
        this.valConceptoPry = valConceptoPry;
    }

    public String getGlsFrmlConcepPry() {
        return glsFrmlConcepPry;
    }

    public void setGlsFrmlConcepPry(String glsFrmlConcepPry) {
        this.glsFrmlConcepPry = glsFrmlConcepPry;
    }

    @Override
    public String toString() {
        return "ValPeriodoProyeccionDesgloseDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='"
            + codMoneda + '\'' + ", codPlantilla=" + codPlantilla + ", fecPeriodo=" + fecPeriodo + ", codTipoConcepPry='" + codTipoConcepPry + '\'' + ", corConceptoPry="
            + corConceptoPry + ", codPlantillaPry=" + codPlantillaPry + ", fecPeriodoPry=" + fecPeriodoPry + ", valConceptoPry=" + valConceptoPry + ", glsFrmlConcepPry='"
            + glsFrmlConcepPry + '\'' + '}';
    }
}
