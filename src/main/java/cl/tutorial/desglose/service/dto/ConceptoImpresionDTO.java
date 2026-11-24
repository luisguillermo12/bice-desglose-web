package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ConceptoImpresion entity.
 */
public class ConceptoImpresionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCliente;

    @NotNull
    private Integer codPlantilla;

    @NotNull
    private Integer codTipoDes;

    @NotNull
    private Integer codSubtipoDes;

    @NotNull
    private String codMoneda;

    @NotNull
    private String codTipoConcep;

    @NotNull
    private String corConcepto;

    private String tipoConcep;

    private String codConcepto;

    private Float prc;

    private Float prc1;

    public String getCodConcepto() {
        return codConcepto;
    }

    public void setCodConcepto(String codConcepto) {
        this.codConcepto = codConcepto;
    }

    public String getTipoConcep() {
        return tipoConcep;
    }

    public void setTipoConcep(String tipoConcep) {
        this.tipoConcep = tipoConcep;
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

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public String getCorConcepto() {
        return corConcepto;
    }

    public void setCorConcepto(String corConcepto) {
        this.corConcepto = corConcepto;
    }

    public Float getPrc() {
        return prc;
    }

    public void setPrc(Float prc) {
        this.prc = prc;
    }

    public Float getPrc1() {
        return prc1;
    }

    public void setPrc1(Float prc1) {
        this.prc1 = prc1;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConceptoImpresionDTO))
            return false;
        ConceptoImpresionDTO that = (ConceptoImpresionDTO) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codTipoConcep, that.codTipoConcep) && Objects.equals(corConcepto, that.corConcepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codTipoConcep, corConcepto);
    }

    @Override
    public String toString() {
        return "ConceptoImpresionDTO{" + "codCliente='" + codCliente + '\'' + ", codPlantilla=" + codPlantilla + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes
            + ", codMoneda='" + codMoneda + '\'' + ", codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto='" + corConcepto + '\'' + ", tipoConcep='" + tipoConcep + '\''
            + ", codConcepto='" + codConcepto + '\'' + ", prc=" + prc + ", prc1=" + prc1 + '}';
    }
}
