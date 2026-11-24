package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class DesgloseRevisionLogPK extends CommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private ZonedDateTime fecEvent;
    private Integer       numLineaLog;

    public ZonedDateTime getFecEvent() {
        return fecEvent;
    }

    public void setFecEvent(ZonedDateTime fecEvent) {
        this.fecEvent = fecEvent;
    }

    public Integer getNumLineaLog() {
        return numLineaLog;
    }

    public void setNumLineaLog(Integer numLineaLog) {
        this.numLineaLog = numLineaLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseRevisionLogPK))
            return false;
        if (!super.equals(o))
            return false;
        DesgloseRevisionLogPK that = (DesgloseRevisionLogPK) o;
        return Objects.equals(fecEvent, that.fecEvent) && Objects.equals(numLineaLog, that.numLineaLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fecEvent, numLineaLog);
    }

    @Override
    public String toString() {
        return "DesgloseAuditoriaPK{" + "fecEvent=" + fecEvent + ", numLineaLog=" + numLineaLog + ", codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes
            + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + '}';
    }

}
