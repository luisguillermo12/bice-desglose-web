package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class ConceptoImpresionPK extends CommonPK implements Serializable{

    private static final long serialVersionUID = 1L;
    private String  codTipoConcep;
    private Integer corConcepto;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConceptoImpresionPK))
            return false;
        ConceptoImpresionPK that = (ConceptoImpresionPK) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codTipoConcep, that.codTipoConcep) && Objects.equals(corConcepto, that.corConcepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codTipoConcep, corConcepto);
    }

    @Override
    public String toString() {
        return "ConceptoImpresionPK{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + '}';
    }
}
