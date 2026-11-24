package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the ClienteNatural entity.
 */
public class ClienteNaturalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long numCliente;

    private String flgEmpleado;

    private String nomPaterno;

    private String nomMaterno;

    private String nomNombres;

    private String nomNickname;

    private String codNacion;

    private String codResidencia;

    private Integer codProfesion;

    private LocalDate fecNacim;

    private String codSexo;

    private Integer codNivEducac;

    private String codEstcivil;

    private String codSepBienes;

    private Integer numCargas;

    private LocalDate fecAntigViv;

    private LocalDate fecDefuncion;

    private LocalDate fecCertEcivi;

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

    public String getFlgEmpleado() {
        return flgEmpleado;
    }

    public void setFlgEmpleado(String flgEmpleado) {
        this.flgEmpleado = flgEmpleado;
    }

    public String getNomPaterno() {
        return nomPaterno;
    }

    public void setNomPaterno(String nomPaterno) {
        this.nomPaterno = nomPaterno;
    }

    public String getNomMaterno() {
        return nomMaterno;
    }

    public void setNomMaterno(String nomMaterno) {
        this.nomMaterno = nomMaterno;
    }

    public String getNomNombres() {
        return nomNombres;
    }

    public void setNomNombres(String nomNombres) {
        this.nomNombres = nomNombres;
    }

    public String getNomNickname() {
        return nomNickname;
    }

    public void setNomNickname(String nomNickname) {
        this.nomNickname = nomNickname;
    }

    public String getCodNacion() {
        return codNacion;
    }

    public void setCodNacion(String codNacion) {
        this.codNacion = codNacion;
    }

    public String getCodResidencia() {
        return codResidencia;
    }

    public void setCodResidencia(String codResidencia) {
        this.codResidencia = codResidencia;
    }

    public Integer getCodProfesion() {
        return codProfesion;
    }

    public void setCodProfesion(Integer codProfesion) {
        this.codProfesion = codProfesion;
    }

    public LocalDate getFecNacim() {
        return fecNacim;
    }

    public void setFecNacim(LocalDate fecNacim) {
        this.fecNacim = fecNacim;
    }

    public String getCodSexo() {
        return codSexo;
    }

    public void setCodSexo(String codSexo) {
        this.codSexo = codSexo;
    }

    public Integer getCodNivEducac() {
        return codNivEducac;
    }

    public void setCodNivEducac(Integer codNivEducac) {
        this.codNivEducac = codNivEducac;
    }

    public String getCodEstcivil() {
        return codEstcivil;
    }

    public void setCodEstcivil(String codEstcivil) {
        this.codEstcivil = codEstcivil;
    }

    public String getCodSepBienes() {
        return codSepBienes;
    }

    public void setCodSepBienes(String codSepBienes) {
        this.codSepBienes = codSepBienes;
    }

    public Integer getNumCargas() {
        return numCargas;
    }

    public void setNumCargas(Integer numCargas) {
        this.numCargas = numCargas;
    }

    public LocalDate getFecAntigViv() {
        return fecAntigViv;
    }

    public void setFecAntigViv(LocalDate fecAntigViv) {
        this.fecAntigViv = fecAntigViv;
    }

    public LocalDate getFecDefuncion() {
        return fecDefuncion;
    }

    public void setFecDefuncion(LocalDate fecDefuncion) {
        this.fecDefuncion = fecDefuncion;
    }

    public LocalDate getFecCertEcivi() {
        return fecCertEcivi;
    }

    public void setFecCertEcivi(LocalDate fecCertEcivi) {
        this.fecCertEcivi = fecCertEcivi;
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
        if (!(o instanceof ClienteNaturalDTO))
            return false;
        ClienteNaturalDTO that = (ClienteNaturalDTO) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codRegLog);
    }

    @Override
    public String toString() {
        return "ClienteNaturalDTO{" + "numCliente=" + getNumCliente() + ", flgEmpleado='" + getFlgEmpleado() + "'" + ", nomPaterno='" + getNomPaterno() + "'" + ", nomMaterno='"
            + getNomMaterno() + "'" + ", nomNombres='" + getNomNombres() + "'" + ", nomNickname='" + getNomNickname() + "'" + ", codNacion='" + getCodNacion() + "'"
            + ", codResidencia='" + getCodResidencia() + "'" + ", codProfesion=" + getCodProfesion() + ", fecNacim='" + getFecNacim() + "'" + ", codSexo='" + getCodSexo() + "'"
            + ", codNivEducac=" + getCodNivEducac() + ", codEstcivil='" + getCodEstcivil() + "'" + ", codSepBienes='" + getCodSepBienes() + "'" + ", numCargas=" + getNumCargas()
            + ", fecAntigViv='" + getFecAntigViv() + "'" + ", fecDefuncion='" + getFecDefuncion() + "'" + ", fecCertEcivi='" + getFecCertEcivi() + "'" + ", codRegStatus="
            + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
