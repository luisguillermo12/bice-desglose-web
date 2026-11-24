package cl.tutorial.desglose.conexiones.cifdesglose.domain.pk;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public class CommonClientePK implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Long    numCliente;
    protected Integer codRegLog;

    public Long getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public void setCodRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CommonClientePK))
            return false;
        CommonClientePK that = (CommonClientePK) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codRegLog);
    }

    @Override
    public String toString() {
        return "CommonClientePK{" + "numCliente=" + numCliente + ", codRegLog=" + codRegLog + '}';
    }
}
