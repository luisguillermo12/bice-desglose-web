package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa la tabla \"CLINAT\" que contiene información de clientes persona natural. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"CLINAT\" que contiene información de clientes persona natural. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(CommonClientePK.class)
@Entity
@Table(name = "CLINAT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClienteNatural implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Número del cliente.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Número del cliente.", required = true)
    @Column(name = "num_cliente", nullable = false)
    private Long numCliente;

    /**
     * Código de modificación del registro.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de modificación del registro.", required = true)
    @Column(name = "cod_reg_log", nullable = false)
    private Integer codRegLog;

    /**
     * Si es empleado o no.
     */
    @ApiModelProperty(value = "Si es empleado o no.")
    @Column(name = "flg_empleado")
    private String flgEmpleado;

    /**
     * Apellido paterno.
     */
    @ApiModelProperty(value = "Apellido paterno.")
    @Column(name = "nom_paterno")
    private String nomPaterno;

    /**
     * Apellido materno.
     */
    @ApiModelProperty(value = "Apellido materno.")
    @Column(name = "nom_materno")
    private String nomMaterno;

    /**
     * Nombres.
     */
    @ApiModelProperty(value = "Nombres.")
    @Column(name = "nom_nombres")
    private String nomNombres;

    /**
     * Pseudonimo.
     */
    @ApiModelProperty(value = "Pseudonimo.")
    @Column(name = "nom_nickname")
    private String nomNickname;

    /**
     * Nacionalidad del cliente.
     */
    @ApiModelProperty(value = "Nacionalidad del cliente.")
    @Column(name = "cod_nacion")
    private String codNacion;

    /**
     * Residencia.
     */
    @ApiModelProperty(value = "Residencia.")
    @Column(name = "cod_residencia")
    private String codResidencia;

    /**
     * Profesión del cliente.
     */
    @ApiModelProperty(value = "Profesión del cliente.")
    @Column(name = "cod_profesion")
    private Integer codProfesion;

    /**
     * Fecha de nacimiento.
     */
    @ApiModelProperty(value = "Fecha de nacimiento.")
    @Column(name = "fec_nacim")
    private LocalDate fecNacim;

    /**
     * Sexo.
     */
    @ApiModelProperty(value = "Sexo.")
    @Column(name = "cod_sexo")
    private String codSexo;

    /**
     * Nivel educacional del cliente.
     */
    @ApiModelProperty(value = "Nivel educacional del cliente.")
    @Column(name = "cod_niv_educac")
    private Integer codNivEducac;

    /**
     * Estado civil del cliente.
     */
    @ApiModelProperty(value = "Estado civil del cliente.")
    @Column(name = "cod_estcivil")
    private String codEstcivil;

    /**
     * Separación de bienes.
     */
    @ApiModelProperty(value = "Separación de bienes.")
    @Column(name = "cod_sep_bienes")
    private String codSepBienes;

    /**
     * Número de cargas.
     */
    @ApiModelProperty(value = "Número de cargas.")
    @Column(name = "num_cargas")
    private Integer numCargas;

    /**
     * Antiguedad vivienda.
     */
    @ApiModelProperty(value = "Antiguedad vivienda.")
    @Column(name = "fec_antig_viv")
    private LocalDate fecAntigViv;

    /**
     * Fecha de defunción.
     */
    @ApiModelProperty(value = "Fecha de defunción.")
    @Column(name = "fec_defuncion")
    private LocalDate fecDefuncion;

    /**
     * Fecha certificado estado civil.
     */
    @ApiModelProperty(value = "Fecha certificado estado civil.")
    @Column(name = "fec_cert_ecivi")
    private LocalDate fecCertEcivi;

    /**
     * Código de vigencia del registro.
     */
    @NotNull
    @ApiModelProperty(value = "Código de vigencia del registro.", required = true)
    @Column(name = "cod_reg_status", nullable = false)
    private Integer codRegStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getNumCliente() {
        return numCliente;
    }

    public ClienteNatural numCliente(Long numCliente) {
        this.numCliente = numCliente;
        return this;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public String getFlgEmpleado() {
        return flgEmpleado;
    }

    public ClienteNatural flgEmpleado(String flgEmpleado) {
        this.flgEmpleado = flgEmpleado;
        return this;
    }

    public void setFlgEmpleado(String flgEmpleado) {
        this.flgEmpleado = flgEmpleado;
    }

    public String getNomPaterno() {
        return nomPaterno;
    }

    public ClienteNatural nomPaterno(String nomPaterno) {
        this.nomPaterno = nomPaterno;
        return this;
    }

    public void setNomPaterno(String nomPaterno) {
        this.nomPaterno = nomPaterno;
    }

    public String getNomMaterno() {
        return nomMaterno;
    }

    public ClienteNatural nomMaterno(String nomMaterno) {
        this.nomMaterno = nomMaterno;
        return this;
    }

    public void setNomMaterno(String nomMaterno) {
        this.nomMaterno = nomMaterno;
    }

    public String getNomNombres() {
        return nomNombres;
    }

    public ClienteNatural nomNombres(String nomNombres) {
        this.nomNombres = nomNombres;
        return this;
    }

    public void setNomNombres(String nomNombres) {
        this.nomNombres = nomNombres;
    }

    public String getNomNickname() {
        return nomNickname;
    }

    public ClienteNatural nomNickname(String nomNickname) {
        this.nomNickname = nomNickname;
        return this;
    }

    public void setNomNickname(String nomNickname) {
        this.nomNickname = nomNickname;
    }

    public String getCodNacion() {
        return codNacion;
    }

    public ClienteNatural codNacion(String codNacion) {
        this.codNacion = codNacion;
        return this;
    }

    public void setCodNacion(String codNacion) {
        this.codNacion = codNacion;
    }

    public String getCodResidencia() {
        return codResidencia;
    }

    public ClienteNatural codResidencia(String codResidencia) {
        this.codResidencia = codResidencia;
        return this;
    }

    public void setCodResidencia(String codResidencia) {
        this.codResidencia = codResidencia;
    }

    public Integer getCodProfesion() {
        return codProfesion;
    }

    public ClienteNatural codProfesion(Integer codProfesion) {
        this.codProfesion = codProfesion;
        return this;
    }

    public void setCodProfesion(Integer codProfesion) {
        this.codProfesion = codProfesion;
    }

    public LocalDate getFecNacim() {
        return fecNacim;
    }

    public ClienteNatural fecNacim(LocalDate fecNacim) {
        this.fecNacim = fecNacim;
        return this;
    }

    public void setFecNacim(LocalDate fecNacim) {
        this.fecNacim = fecNacim;
    }

    public String getCodSexo() {
        return codSexo;
    }

    public ClienteNatural codSexo(String codSexo) {
        this.codSexo = codSexo;
        return this;
    }

    public void setCodSexo(String codSexo) {
        this.codSexo = codSexo;
    }

    public Integer getCodNivEducac() {
        return codNivEducac;
    }

    public ClienteNatural codNivEducac(Integer codNivEducac) {
        this.codNivEducac = codNivEducac;
        return this;
    }

    public void setCodNivEducac(Integer codNivEducac) {
        this.codNivEducac = codNivEducac;
    }

    public String getCodEstcivil() {
        return codEstcivil;
    }

    public ClienteNatural codEstcivil(String codEstcivil) {
        this.codEstcivil = codEstcivil;
        return this;
    }

    public void setCodEstcivil(String codEstcivil) {
        this.codEstcivil = codEstcivil;
    }

    public String getCodSepBienes() {
        return codSepBienes;
    }

    public ClienteNatural codSepBienes(String codSepBienes) {
        this.codSepBienes = codSepBienes;
        return this;
    }

    public void setCodSepBienes(String codSepBienes) {
        this.codSepBienes = codSepBienes;
    }

    public Integer getNumCargas() {
        return numCargas;
    }

    public ClienteNatural numCargas(Integer numCargas) {
        this.numCargas = numCargas;
        return this;
    }

    public void setNumCargas(Integer numCargas) {
        this.numCargas = numCargas;
    }

    public LocalDate getFecAntigViv() {
        return fecAntigViv;
    }

    public ClienteNatural fecAntigViv(LocalDate fecAntigViv) {
        this.fecAntigViv = fecAntigViv;
        return this;
    }

    public void setFecAntigViv(LocalDate fecAntigViv) {
        this.fecAntigViv = fecAntigViv;
    }

    public LocalDate getFecDefuncion() {
        return fecDefuncion;
    }

    public ClienteNatural fecDefuncion(LocalDate fecDefuncion) {
        this.fecDefuncion = fecDefuncion;
        return this;
    }

    public void setFecDefuncion(LocalDate fecDefuncion) {
        this.fecDefuncion = fecDefuncion;
    }

    public LocalDate getFecCertEcivi() {
        return fecCertEcivi;
    }

    public ClienteNatural fecCertEcivi(LocalDate fecCertEcivi) {
        this.fecCertEcivi = fecCertEcivi;
        return this;
    }

    public void setFecCertEcivi(LocalDate fecCertEcivi) {
        this.fecCertEcivi = fecCertEcivi;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public ClienteNatural codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public ClienteNatural codRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
        return this;
    }

    public void setCodRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClienteNatural))
            return false;
        ClienteNatural that = (ClienteNatural) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codRegLog);
    }

    @Override
    public String toString() {
        return "ClienteNatural{" + "numCliente=" + getNumCliente() + ", flgEmpleado='" + getFlgEmpleado() + "'" + ", nomPaterno='" + getNomPaterno() + "'" + ", nomMaterno='"
            + getNomMaterno() + "'" + ", nomNombres='" + getNomNombres() + "'" + ", nomNickname='" + getNomNickname() + "'" + ", codNacion='" + getCodNacion() + "'"
            + ", codResidencia='" + getCodResidencia() + "'" + ", codProfesion=" + getCodProfesion() + ", fecNacim='" + getFecNacim() + "'" + ", codSexo='" + getCodSexo() + "'"
            + ", codNivEducac=" + getCodNivEducac() + ", codEstcivil='" + getCodEstcivil() + "'" + ", codSepBienes='" + getCodSepBienes() + "'" + ", numCargas=" + getNumCargas()
            + ", fecAntigViv='" + getFecAntigViv() + "'" + ", fecDefuncion='" + getFecDefuncion() + "'" + ", fecCertEcivi='" + getFecCertEcivi() + "'" + ", codRegStatus="
            + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
