package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the ValPeriodoDesgImpr entity.
 */
public class ValPeriodoDesgImprDTO implements Serializable {

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
    private String codTipoConcep;

    @NotNull
    private Integer corConcepto;

    @NotNull
    private Integer corValor;

    @NotNull
    private BigDecimal valConcepto;

    @NotNull
    private Integer corImpreso;

    @NotNull
    private Integer cod_tipo_plantilla_des;

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

    public Integer getCorImpreso() {
        return corImpreso;
    }

    public void setCorImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
    }

    public Integer getCod_tipo_plantilla_des() {
        return cod_tipo_plantilla_des;
    }

    public void setCod_tipo_plantilla_des(Integer cod_tipo_plantilla_des) {
        this.cod_tipo_plantilla_des = cod_tipo_plantilla_des;
    }

    @Override
    public String toString() {
        return "ValPeriodoDesgImprDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda
            + '\'' + ", codMonedaImp='" + codMonedaImp + '\'' + ", fecPeriodo=" + fecPeriodo + ", codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto
            + ", corValor=" + corValor + ", valConcepto=" + valConcepto + ", corImpreso=" + corImpreso + ", cod_tipo_plantilla_des=" + cod_tipo_plantilla_des + '}';
    }
}
