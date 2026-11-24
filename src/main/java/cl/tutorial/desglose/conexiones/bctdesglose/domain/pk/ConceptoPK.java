package cl.tutorial.desglose.conexiones.bctdesglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class ConceptoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  codTipoConcep;
    private Integer corConcepto;
    private Integer codTipoPlantilla;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConceptoPK that = (ConceptoPK) o;
        return Objects.equals(codTipoConcep, that.codTipoConcep) && Objects.equals(corConcepto, that.corConcepto) && Objects.equals(codTipoPlantilla, that.codTipoPlantilla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTipoConcep, corConcepto, codTipoPlantilla);
    }

    @Override
    public String toString() {
        return "ConceptoPK{" + "codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + ", codTipoPlantilla=" + codTipoPlantilla + '}';
    }
}
