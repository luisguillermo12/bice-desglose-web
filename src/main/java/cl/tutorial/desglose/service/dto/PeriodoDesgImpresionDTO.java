package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the PeriodoDesgImpresion entity.
 */
public class PeriodoDesgImpresionDTO implements Serializable {

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
    private String codMonedaImp;

    @NotNull
    private LocalDate fecPeriodo;

    @NotNull
    private BigDecimal valDeflacPrd;

    @NotNull
    private BigDecimal valTipoCambio;

    @NotNull
    private Integer corImpreso;

    @NotNull
    private Integer codPlantilla;

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

    public String getCodMonedaImp() {
        return codMonedaImp;
    }

    public void setCodMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public BigDecimal getValDeflacPrd() {
        return valDeflacPrd;
    }

    public void setValDeflacPrd(BigDecimal valDeflacPrd) {
        this.valDeflacPrd = valDeflacPrd;
    }

    public BigDecimal getValTipoCambio() {
        return valTipoCambio;
    }

    public void setValTipoCambio(BigDecimal valTipoCambio) {
        this.valTipoCambio = valTipoCambio;
    }

    public Integer getCorImpreso() {
        return corImpreso;
    }

    public void setCorImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    @Override
    public String toString() {
        return "PeriodoDesgImpresionDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda
            + '\'' + ", codMonedaImp='" + codMonedaImp + '\'' + ", fecPeriodo=" + fecPeriodo + ", valDeflacPrd=" + valDeflacPrd + ", valTipoCambio=" + valTipoCambio
            + ", corImpreso=" + corImpreso + ", codPlantilla=" + codPlantilla + '}';
    }
}
