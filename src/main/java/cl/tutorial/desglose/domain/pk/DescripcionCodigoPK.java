package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class DescripcionCodigoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  codTabla;
    private String  codGenerico;
    private String glsGenericacorta;
    private String glsGenericalarga;

    public String getCodTabla() {
        return codTabla;
    }

    public void setCodTabla(String codTabla) {
        this.codTabla = codTabla;
    }

    public String getCodGenerico() {
        return codGenerico;
    }

    public void setCodGenerico(String codGenerico) {
        this.codGenerico = codGenerico;
    }


    public String getGlsGenericacorta() {
        return glsGenericacorta;
    }

    public void setGlsGenericacorta(String glsGenericacorta) {
        this.glsGenericacorta = glsGenericacorta;
    }

    public String getGlsGenericalarga() {
        return glsGenericalarga;
    }

    public void setGlsGenericalarga(String glsGenericalarga) {
        this.glsGenericalarga = glsGenericalarga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DescripcionCodigoPK that = (DescripcionCodigoPK) o;
        return Objects.equals(codTabla, that.codTabla) && Objects.equals(codGenerico, that.codGenerico)  && Objects
            .equals(glsGenericacorta, that.glsGenericacorta) && Objects.equals(glsGenericalarga, that.glsGenericalarga);
    }

    @Override
    public String toString() {
        return "DescripcionCodigoPK{" + "codTabla='" + codTabla + '\'' + ", codGenerico='" + codGenerico + '\'' + ", glsGenericacorta='" + glsGenericacorta + '\''
            + ", glsGenericalarga='" + glsGenericalarga + '\'' + '}';
    }
}
