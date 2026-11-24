package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the DesgloseAuditoria entity.
 */
public class DesgloseAuditoriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCliente;

    @NotNull
    private Integer codTipoDes;

    @NotNull
    private Integer codSubtipoDes;

    @NotNull
    private String codMoneda;

    @NotNull
    private Integer codPlantilla;

    @NotNull
    private LocalDateTime fecEvento;

    @NotNull
    private Integer numLinea;

    private String codEvento;

    private String rolEmpleadoAccion;

    private String glsRol;

    private String codEstadoOri;

    private String codEstadoNvo;

    private String rolEmpleadoOri;

    private String rolEmpleadoNvo;

    private String flgResultValid;

    private String glsComentario;

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

    public LocalDateTime getFecEvento() {
        return fecEvento;
    }

    public void setFecEvento(LocalDateTime fecEvento) {
        this.fecEvento = fecEvento;
    }

    public Integer getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(Integer numLinea) {
        this.numLinea = numLinea;
    }

    public String getCodEvento() {
        return codEvento;
    }

    public void setCodEvento(String codEvento) {
        this.codEvento = codEvento;
    }

    public String getRolEmpleadoAccion() {
        return rolEmpleadoAccion;
    }

    public void setRolEmpleadoAccion(String rolEmpleadoAccion) {
        this.rolEmpleadoAccion = rolEmpleadoAccion;
    }

    public String getGlsRol() {
        return glsRol;
    }

    public void setGlsRol(String glsRol) {
        this.glsRol = glsRol;
    }

    public String getCodEstadoOri() {
        return codEstadoOri;
    }

    public void setCodEstadoOri(String codEstadoOri) {
        this.codEstadoOri = codEstadoOri;
    }

    public String getCodEstadoNvo() {
        return codEstadoNvo;
    }

    public void setCodEstadoNvo(String codEstadoNvo) {
        this.codEstadoNvo = codEstadoNvo;
    }

    public String getRolEmpleadoOri() {
        return rolEmpleadoOri;
    }

    public void setRolEmpleadoOri(String rolEmpleadoOri) {
        this.rolEmpleadoOri = rolEmpleadoOri;
    }

    public String getRolEmpleadoNvo() {
        return rolEmpleadoNvo;
    }

    public void setRolEmpleadoNvo(String rolEmpleadoNvo) {
        this.rolEmpleadoNvo = rolEmpleadoNvo;
    }

    public String getFlgResultValid() {
        return flgResultValid;
    }

    public void setFlgResultValid(String flgResultValid) {
        this.flgResultValid = flgResultValid;
    }

    public String getGlsComentario() {
        return glsComentario;
    }

    public void setGlsComentario(String glsComentario) {
        this.glsComentario = glsComentario;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseAuditoriaDTO))
            return false;
        DesgloseAuditoriaDTO that = (DesgloseAuditoriaDTO) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codPlantilla, that.codPlantilla) && Objects.equals(fecEvento, that.fecEvento) && Objects
            .equals(numLinea, that.numLinea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, fecEvento, numLinea);
    }

    @Override
    public String toString() {
        return "DesgloseAuditoriaDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", fecEvento=" + fecEvento + ", numLinea=" + numLinea + ", codEvento='" + codEvento + '\'' + ", rolEmpleadoAccion='"
            + rolEmpleadoAccion + '\'' + ", glsRol='" + glsRol + '\'' + ", codEstadoOri='" + codEstadoOri + '\'' + ", codEstadoNvo='" + codEstadoNvo + '\'' + ", rolEmpleadoOri='"
            + rolEmpleadoOri + '\'' + ", rolEmpleadoNvo='" + rolEmpleadoNvo + '\'' + ", flgResultValid='" + flgResultValid + '\'' + ", glsComentario='" + glsComentario + '\''
            + '}';
    }
}
