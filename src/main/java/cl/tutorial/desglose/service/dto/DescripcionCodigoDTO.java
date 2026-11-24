package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DescripcionCodigo entity.
 */
public class DescripcionCodigoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codTabla;

    @NotNull
    private String codGenerico;

    private String glsGenericacorta;

    private String glsGenericalarga;

    @NotNull
    private Integer codRegStatus;

    private Integer codRegLog;

    public String getCodTabla() {
        return codTabla;
    }

    public void setCodTabla(String codTabla) {
        this.codTabla = codTabla;
    }

    public String getCodGenerico() {
        return codGenerico;
    }

    public void setCodGenerico(String codGenerico) {
        this.codGenerico = codGenerico;
    }

    public String getGlsGenericacorta() {
        return glsGenericacorta;
    }

    public void setGlsGenericacorta(String glsGenericacorta) {
        this.glsGenericacorta = glsGenericacorta;
    }

    public String getGlsGenericalarga() {
        return glsGenericalarga;
    }

    public void setGlsGenericalarga(String glsGenericalarga) {
        this.glsGenericalarga = glsGenericalarga;
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
        if (!(o instanceof DescripcionCodigoDTO))
            return false;
        DescripcionCodigoDTO that = (DescripcionCodigoDTO) o;
        return Objects.equals(codTabla, that.codTabla) && Objects.equals(codGenerico, that.codGenerico) && Objects.equals(codRegStatus, that.codRegStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTabla, codGenerico, codRegStatus);
    }

    @Override
    public String toString() {
        return "DescripcionCodigoDTO{" + "codTabla='" + getCodTabla() + "'" + ", codGenerico='" + getCodGenerico() + "'" + ", glsGenericacorta='" + getGlsGenericacorta() + "'"
            + ", glsGenericalarga='" + getGlsGenericalarga() + "'" + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
