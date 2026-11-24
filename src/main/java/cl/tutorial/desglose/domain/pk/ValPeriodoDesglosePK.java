package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ValPeriodoDesglosePK extends NewCommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Fecha para el período.
     */
    private LocalDate fecPeriodo;

    /**
     * Tipo de concepto.
     */
    private String codTipoConcep;

    /**
     * Correlativo para concepto.
     */
    private Integer corConcepto;

    /**
     * Correlativo que aplica para multiregistro, de lo contrario siempre es cero.
     */
    private Integer corValor;

    public static ValPeriodoDesglosePK newInstance(PeriodoPK periodoPK) {
        ValPeriodoDesglosePK pk = new ValPeriodoDesglosePK();
        pk.setCodCliente(periodoPK.codCliente);
        pk.setCodTipoDes(periodoPK.codTipoDes);
        pk.setCodSubtipoDes(periodoPK.codSubtipoDes);
        pk.setCodMoneda(periodoPK.codMoneda);
        pk.setFecPeriodo(periodoPK.fecPeriodo);
        return pk;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ValPeriodoDesglosePK))
            return false;
        if (!super.equals(o))
            return false;
        ValPeriodoDesglosePK that = (ValPeriodoDesglosePK) o;
        return Objects.equals(fecPeriodo, that.fecPeriodo) && Objects.equals(codTipoConcep, that.codTipoConcep) && Objects.equals(corConcepto, that.corConcepto) && Objects
            .equals(corValor, that.corValor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fecPeriodo, codTipoConcep, corConcepto, corValor);
    }

    @Override
    public String toString() {
        return "ValPeriodoDesglosePK{" + "fecPeriodo=" + fecPeriodo + ", codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + ", corValor=" + corValor
            + ", codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + '}';
    }
}
