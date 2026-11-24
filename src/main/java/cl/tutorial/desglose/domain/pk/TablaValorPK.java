package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class TablaValorPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String    codTabla;
    private String    codGenerico;
    private LocalDate fecValidez;
    private Integer   codRegStatus;

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

    public LocalDate getFecValidez() {
        return fecValidez;
    }

    public void setFecValidez(LocalDate fecValidez) {
        this.fecValidez = fecValidez;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TablaValorPK))
            return false;
        TablaValorPK that = (TablaValorPK) o;
        return Objects.equals(codTabla, that.codTabla) && Objects.equals(codGenerico, that.codGenerico) && Objects.equals(fecValidez, that.fecValidez) && Objects
            .equals(codRegStatus, that.codRegStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTabla, codGenerico, fecValidez, codRegStatus);
    }

    @Override
    public String toString() {
        return "TablaValorPK{" + "codTabla='" + codTabla + '\'' + ", codGenerico='" + codGenerico + '\'' + ", fecValidez=" + fecValidez + ", codRegStatus=" + codRegStatus + '}';
    }
}
