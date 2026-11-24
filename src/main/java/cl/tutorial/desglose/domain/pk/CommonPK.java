package cl.tutorial.desglose.domain.pk;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public class CommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String  codCliente;
    protected Integer codTipoDes;
    protected Integer codSubtipoDes;
    protected String  codMoneda;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CommonPK))
            return false;
        CommonPK commonPK = (CommonPK) o;
        return Objects.equals(codCliente, commonPK.codCliente) && Objects.equals(codTipoDes, commonPK.codTipoDes) && Objects.equals(codSubtipoDes, commonPK.codSubtipoDes)
            && Objects.equals(codMoneda, commonPK.codMoneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda);
    }

    @Override
    public String toString() {
        return "CommonPK{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + '}';
    }
}
