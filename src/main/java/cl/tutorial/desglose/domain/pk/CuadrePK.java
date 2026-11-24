package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class CuadrePK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  codTpoConcep1;
    private Integer corConcepto1;
    private String  codTpoConcep2;
    private Integer corConcepto2;

    public String getCodTpoConcep1() {
        return codTpoConcep1;
    }

    public void setCodTpoConcep1(String codTpoConcep1) {
        this.codTpoConcep1 = codTpoConcep1;
    }

    public Integer getCorConcepto1() {
        return corConcepto1;
    }

    public void setCorConcepto1(Integer corConcepto1) {
        this.corConcepto1 = corConcepto1;
    }

    public String getCodTpoConcep2() {
        return codTpoConcep2;
    }

    public void setCodTpoConcep2(String codTpoConcep2) {
        this.codTpoConcep2 = codTpoConcep2;
    }

    public Integer getCorConcepto2() {
        return corConcepto2;
    }

    public void setCorConcepto2(Integer corConcepto2) {
        this.corConcepto2 = corConcepto2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CuadrePK))
            return false;
        CuadrePK cuadrePK = (CuadrePK) o;
        return Objects.equals(codTpoConcep1, cuadrePK.codTpoConcep1) && Objects.equals(corConcepto1, cuadrePK.corConcepto1) && Objects.equals(codTpoConcep2, cuadrePK.codTpoConcep2)
            && Objects.equals(corConcepto2, cuadrePK.corConcepto2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTpoConcep1, corConcepto1, codTpoConcep2, corConcepto2);
    }

    @Override
    public String toString() {
        return "CuadrePK{" + "codTpoConcep1='" + codTpoConcep1 + '\'' + ", corConcepto1=" + corConcepto1 + ", codTpoConcep2='" + codTpoConcep2 + '\'' + ", corConcepto2="
            + corConcepto2 + '}';
    }
}
