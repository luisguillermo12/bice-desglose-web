package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the DesgloseRevisionLog entity.
 */
public class DesgloseRevisionLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private ZonedDateTime fecEvent;

    @NotNull
    private Integer numLineaLog;

    @NotNull
    private String codCliente;

    @NotNull
    private Integer codTipoDes;

    @NotNull
    private Integer codSubtipoDes;

    @NotNull
    private String codMoneda;

    @NotNull
    private String numRolEmpleado;

    private String codEstado;

    private String codEstadoOld;

    private String numRolEmpReem;

    private String glsComentarios;

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

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getCodEstadoOld() {
        return codEstadoOld;
    }

    public void setCodEstadoOld(String codEstadoOld) {
        this.codEstadoOld = codEstadoOld;
    }

    public String getNumRolEmpReem() {
        return numRolEmpReem;
    }

    public void setNumRolEmpReem(String numRolEmpReem) {
        this.numRolEmpReem = numRolEmpReem;
    }

    public String getGlsComentarios() {
        return glsComentarios;
    }

    public void setGlsComentarios(String glsComentarios) {
        this.glsComentarios = glsComentarios;
    }

    @Override
    public String toString() {
        return "DesgloseRevisionLogDTO{" + "fecEvent='" + getFecEvent() + "'" + ", numLineaLog=" + getNumLineaLog() + ", codCliente='" + getCodCliente() + "'" + ", codTipoDes="
            + getCodTipoDes() + ", codSubtipoDes=" + getCodSubtipoDes() + ", codMoneda='" + getCodMoneda() + "'" + ", numRolEmpleado='" + getNumRolEmpleado() + "'"
            + ", codEstado='" + getCodEstado() + "'" + ", codEstadoOld='" + getCodEstadoOld() + "'" + ", numRolEmpReem='" + getNumRolEmpReem() + "'" + ", glsComentarios='"
            + getGlsComentarios() + "'" + "}";
    }
}
