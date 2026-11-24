package cl.tutorial.desglose.conexiones.sqldesglose.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class ConsultaRut implements Serializable {

    @Id
    private BigDecimal codpro;
    private BigDecimal idcns;

    public ConsultaRut() {
    }

    public ConsultaRut(BigDecimal codpro, BigDecimal idcns) {
        this.codpro = codpro;
        this.idcns = idcns;
    }

    public BigDecimal getCodpro() {
        return codpro;
    }

    public void setCodpro(BigDecimal codpro) {
        this.codpro = codpro;
    }

    public BigDecimal getIdcns() {
        return idcns;
    }

    public void setIdcns(BigDecimal idcns) {
        this.idcns = idcns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsultaRut)) return false;
        ConsultaRut that = (ConsultaRut) o;
        return Objects.equals(codpro, that.codpro) &&
            Objects.equals(idcns, that.idcns);
    }

    @Override
    public int hashCode() {

        return Objects.hash(codpro, idcns);
    }

    @Override
    public String toString() {
        return "ConsultaRut{" +
            "codpro=" + codpro +
            ", idcns=" + idcns +
            '}';
    }
}
