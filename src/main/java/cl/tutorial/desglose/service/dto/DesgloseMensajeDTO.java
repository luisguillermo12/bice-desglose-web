package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the DesgloseMensaje entity.
 */
public class DesgloseMensajeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private ZonedDateTime fecEvent;

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

    private String glsComentarios;

    @NotNull
    private Integer codPlantilla;

    public ZonedDateTime getFecEvent() {
        return fecEvent;
    }

    public void setFecEvent(ZonedDateTime fecEvent) {
        this.fecEvent = fecEvent;
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

    public String getGlsComentarios() {
        return glsComentarios;
    }

    public void setGlsComentarios(String glsComentarios) {
        this.glsComentarios = glsComentarios;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    @Override
    public String toString() {
        return "DesgloseMensajeDTO{" + "fecEvent=" + fecEvent + ", codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes
            + ", codMoneda='" + codMoneda + '\'' + ", numRolEmpleado='" + numRolEmpleado + '\'' + ", glsComentarios='" + glsComentarios + '\'' + ", codPlantilla='" + codPlantilla
            + '\'' + '}';
    }
}
