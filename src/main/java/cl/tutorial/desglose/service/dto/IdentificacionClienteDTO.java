package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the IdentificacionCliente entity.
 */
public class IdentificacionClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long numCliente;

    private Integer codTipoIden;

    private String numIden;

    private LocalDate fecCreacion;

    @NotNull
    private Integer codRegStatus;

    @NotNull
    private Integer codRegLog;

    public Long getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public Integer getCodTipoIden() {
        return codTipoIden;
    }

    public void setCodTipoIden(Integer codTipoIden) {
        this.codTipoIden = codTipoIden;
    }

    public String getNumIden() {
        return numIden;
    }

    public void setNumIden(String numIden) {
        this.numIden = numIden;
    }

    public LocalDate getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(LocalDate fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
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
        if (!(o instanceof IdentificacionClienteDTO))
            return false;
        IdentificacionClienteDTO that = (IdentificacionClienteDTO) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codTipoIden, that.codTipoIden) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codTipoIden, codRegLog);
    }

    @Override
    public String toString() {
        return "IdentificacionClienteDTO{" + "numCliente=" + getNumCliente() + ", codTipoIden=" + getCodTipoIden() + ", numIden='" + getNumIden() + "'" + ", fecCreacion='"
            + getFecCreacion() + "'" + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
