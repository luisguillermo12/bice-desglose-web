package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Cuadre entity.
 */
public class CuadreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codTpoConcep1;

    @NotNull
    private Integer corConcepto1;

    @NotNull
    private String codTpoConcep2;

    @NotNull
    private Integer corConcepto2;

    private String glsMensaje;

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

    public String getGlsMensaje() {
        return glsMensaje;
    }

    public void setGlsMensaje(String glsMensaje) {
        this.glsMensaje = glsMensaje;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CuadreDTO))
            return false;
        CuadreDTO cuadreDTO = (CuadreDTO) o;
        return Objects.equals(codTpoConcep1, cuadreDTO.codTpoConcep1) && Objects.equals(corConcepto1, cuadreDTO.corConcepto1) && Objects
            .equals(codTpoConcep2, cuadreDTO.codTpoConcep2) && Objects.equals(corConcepto2, cuadreDTO.corConcepto2) && Objects.equals(glsMensaje, cuadreDTO.glsMensaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTpoConcep1, corConcepto1, codTpoConcep2, corConcepto2, glsMensaje);
    }

    @Override
    public String toString() {
        return "CuadreDTO{" + "codTpoConcep1='" + getCodTpoConcep1() + "'" + ", corConcepto1=" + getCorConcepto1() + ", codTpoConcep2='" + getCodTpoConcep2() + "'"
            + ", corConcepto2=" + getCorConcepto2() + ", glsMensaje='" + getGlsMensaje() + "'" + "}";
    }
}
