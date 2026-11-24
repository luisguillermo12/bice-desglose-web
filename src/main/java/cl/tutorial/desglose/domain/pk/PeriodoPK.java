package cl.tutorial.desglose.domain.pk;

import cl.tutorial.desglose.service.dto.PeriodDeleteRequestDTO;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
public class PeriodoPK extends NewCommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    protected LocalDate fecPeriodo;

    public static PeriodoPK newInstance(PeriodDeleteRequestDTO requestDTO) {
        PeriodoPK pk = new PeriodoPK();
        pk.setCodCliente(requestDTO.codCliente);
        pk.setCodTipoDes(requestDTO.codTipoDes);
        pk.setCodSubtipoDes(requestDTO.codSubtipoDes);
        pk.setCodMoneda(requestDTO.codMoneda);
        pk.setCodPlantilla(requestDTO.codPlantilla);
        pk.setFecPeriodo(requestDTO.fecPeriodo);
        return pk;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PeriodoPK))
            return false;
        if (!super.equals(o))
            return false;
        PeriodoPK periodoPK = (PeriodoPK) o;
        return Objects.equals(fecPeriodo, periodoPK.fecPeriodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fecPeriodo);
    }

    @Override
    public String toString() {
        return "PeriodoPK{" + "fecPeriodo=" + fecPeriodo + ", codPlantilla=" + codPlantilla + ", codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes
            + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + '}';
    }

}
