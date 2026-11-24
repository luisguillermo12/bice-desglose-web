package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the ClienteJuridico entity.
 */
public class ClienteJuridicoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long numCliente;

    private String glsRazonSoci;

    private String nomFantasia;

    private Integer codGrupoEcon;

    private LocalDate fecConstitucion;

    private String codTipoCliempresa;

    private String glsWeb;

    @NotNull
    private Integer codRegStatus;

    @NotNull
    private Integer codRegLog;

    private String glsRazonSocialLegal;

    public Long getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public String getGlsRazonSoci() {
        return glsRazonSoci;
    }

    public void setGlsRazonSoci(String glsRazonSoci) {
        this.glsRazonSoci = glsRazonSoci;
    }

    public String getNomFantasia() {
        return nomFantasia;
    }

    public void setNomFantasia(String nomFantasia) {
        this.nomFantasia = nomFantasia;
    }

    public Integer getCodGrupoEcon() {
        return codGrupoEcon;
    }

    public void setCodGrupoEcon(Integer codGrupoEcon) {
        this.codGrupoEcon = codGrupoEcon;
    }

    public LocalDate getFecConstitucion() {
        return fecConstitucion;
    }

    public void setFecConstitucion(LocalDate fecConstitucion) {
        this.fecConstitucion = fecConstitucion;
    }

    public String getCodTipoCliempresa() {
        return codTipoCliempresa;
    }

    public void setCodTipoCliempresa(String codTipoCliempresa) {
        this.codTipoCliempresa = codTipoCliempresa;
    }

    public String getGlsWeb() {
        return glsWeb;
    }

    public void setGlsWeb(String glsWeb) {
        this.glsWeb = glsWeb;
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

    public String getGlsRazonSocialLegal() {
        return glsRazonSocialLegal;
    }

    public void setGlsRazonSocialLegal(String glsRazonSocialLegal) {
        this.glsRazonSocialLegal = glsRazonSocialLegal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClienteJuridicoDTO))
            return false;
        ClienteJuridicoDTO that = (ClienteJuridicoDTO) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codRegLog);
    }

    @Override
    public String toString() {
        return "ClienteJuridicoDTO{" + "numCliente=" + getNumCliente() + ", glsRazonSoci='" + getGlsRazonSoci() + "'" + ", nomFantasia='" + getNomFantasia() + "'"
            + ", codGrupoEcon=" + getCodGrupoEcon() + ", fecConstitucion='" + getFecConstitucion() + "'" + ", codTipoCliempresa='" + getCodTipoCliempresa() + "'" + ", glsWeb='"
            + getGlsWeb() + "'" + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + ", glsRazonSocialLegal='" + getGlsRazonSocialLegal() + "'" + "}";
    }
}
