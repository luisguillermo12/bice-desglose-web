package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the DesgloseChequeoLog entity.
 */
public class DesgloseChequeoLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codTipoProceso;

    private String codCliente;

    private Integer codTipoDes;

    private Integer codSubtipoDes;

    private String codMoneda;

    private ZonedDateTime fecLog;

    private String glsLog;

    public String getCodTipoProceso() {
        return codTipoProceso;
    }

    public void setCodTipoProceso(String codTipoProceso) {
        this.codTipoProceso = codTipoProceso;
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

    public ZonedDateTime getFecLog() {
        return fecLog;
    }

    public void setFecLog(ZonedDateTime fecLog) {
        this.fecLog = fecLog;
    }

    public String getGlsLog() {
        return glsLog;
    }

    public void setGlsLog(String glsLog) {
        this.glsLog = glsLog;
    }

    @Override
    public String toString() {
        return "DesgloseChequeoLogDTO{" + "codTipoProceso='" + getCodTipoProceso() + "'" + ", codCliente='" + getCodCliente() + "'" + ", codTipoDes=" + getCodTipoDes()
            + ", codSubtipoDes=" + getCodSubtipoDes() + ", codMoneda='" + getCodMoneda() + "'" + ", fecLog='" + getFecLog() + "'" + ", glsLog='" + getGlsLog() + "'" + "}";
    }
}
