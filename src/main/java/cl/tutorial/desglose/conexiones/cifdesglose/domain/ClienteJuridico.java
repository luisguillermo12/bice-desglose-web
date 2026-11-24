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
 * Representa la tabla \"CLIJUR\" que contiene información de clientes empresa y persona jurídica. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"CLIJUR\" que contiene información de clientes empresa y persona jurídica. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero "
    + "Palomino")
@IdClass(CommonClientePK.class)
@Entity
@Table(name = "CLIJUR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClienteJuridico implements Serializable {

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
     * Código de modificacion del registro.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de modificacion del registro.", required = true)
    @Column(name = "cod_reg_log", nullable = false)
    private Integer codRegLog;

    /**
     * Razon social.
     */
    @ApiModelProperty(value = "Razon social.")
    @Column(name = "gls_razon_soci")
    private String glsRazonSoci;

    /**
     * Nombre de fantasia.
     */
    @ApiModelProperty(value = "Nombre de fantasia.")
    @Column(name = "nom_fantasia")
    private String nomFantasia;

    /**
     * Grupo económico.
     */
    @ApiModelProperty(value = "Grupo económico.")
    @Column(name = "cod_grupo_econ")
    private Integer codGrupoEcon;

    /**
     * Fecha de constitución.
     */
    @ApiModelProperty(value = "Fecha de constitución.")
    @Column(name = "fec_constitucion")
    private LocalDate fecConstitucion;

    /**
     * Tipo cliente empresa.
     */
    @ApiModelProperty(value = "Tipo cliente empresa.")
    @Column(name = "cod_tipo_cliempresa")
    private String codTipoCliempresa;

    /**
     * URL de la web de la empresa.
     */
    @ApiModelProperty(value = "URL de la web de la empresa.")
    @Column(name = "gls_web")
    private String glsWeb;

    /**
     * Código de vigencia del registro.
     */
    @NotNull
    @ApiModelProperty(value = "Código de vigencia del registro.", required = true)
    @Column(name = "cod_reg_status", nullable = false)
    private Integer codRegStatus;

    /**
     * Razón social legal.
     */
    @ApiModelProperty(value = "Razón social legal.")
    @Column(name = "gls_razon_social_legal")
    private String glsRazonSocialLegal;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getNumCliente() {
        return numCliente;
    }

    public ClienteJuridico numCliente(Long numCliente) {
        this.numCliente = numCliente;
        return this;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public String getGlsRazonSoci() {
        return glsRazonSoci;
    }

    public ClienteJuridico glsRazonSoci(String glsRazonSoci) {
        this.glsRazonSoci = glsRazonSoci;
        return this;
    }

    public void setGlsRazonSoci(String glsRazonSoci) {
        this.glsRazonSoci = glsRazonSoci;
    }

    public String getNomFantasia() {
        return nomFantasia;
    }

    public ClienteJuridico nomFantasia(String nomFantasia) {
        this.nomFantasia = nomFantasia;
        return this;
    }

    public void setNomFantasia(String nomFantasia) {
        this.nomFantasia = nomFantasia;
    }

    public Integer getCodGrupoEcon() {
        return codGrupoEcon;
    }

    public ClienteJuridico codGrupoEcon(Integer codGrupoEcon) {
        this.codGrupoEcon = codGrupoEcon;
        return this;
    }

    public void setCodGrupoEcon(Integer codGrupoEcon) {
        this.codGrupoEcon = codGrupoEcon;
    }

    public LocalDate getFecConstitucion() {
        return fecConstitucion;
    }

    public ClienteJuridico fecConstitucion(LocalDate fecConstitucion) {
        this.fecConstitucion = fecConstitucion;
        return this;
    }

    public void setFecConstitucion(LocalDate fecConstitucion) {
        this.fecConstitucion = fecConstitucion;
    }

    public String getCodTipoCliempresa() {
        return codTipoCliempresa;
    }

    public ClienteJuridico codTipoCliempresa(String codTipoCliempresa) {
        this.codTipoCliempresa = codTipoCliempresa;
        return this;
    }

    public void setCodTipoCliempresa(String codTipoCliempresa) {
        this.codTipoCliempresa = codTipoCliempresa;
    }

    public String getGlsWeb() {
        return glsWeb;
    }

    public ClienteJuridico glsWeb(String glsWeb) {
        this.glsWeb = glsWeb;
        return this;
    }

    public void setGlsWeb(String glsWeb) {
        this.glsWeb = glsWeb;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public ClienteJuridico codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public ClienteJuridico codRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
        return this;
    }

    public void setCodRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
    }

    public String getGlsRazonSocialLegal() {
        return glsRazonSocialLegal;
    }

    public ClienteJuridico glsRazonSocialLegal(String glsRazonSocialLegal) {
        this.glsRazonSocialLegal = glsRazonSocialLegal;
        return this;
    }

    public void setGlsRazonSocialLegal(String glsRazonSocialLegal) {
        this.glsRazonSocialLegal = glsRazonSocialLegal;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClienteJuridico))
            return false;
        ClienteJuridico that = (ClienteJuridico) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codRegLog);
    }

    @Override
    public String toString() {
        return "ClienteJuridico{" + "numCliente=" + getNumCliente() + ", glsRazonSoci='" + getGlsRazonSoci() + "'" + ", nomFantasia='" + getNomFantasia() + "'" + ", codGrupoEcon="
            + getCodGrupoEcon() + ", fecConstitucion='" + getFecConstitucion() + "'" + ", codTipoCliempresa='" + getCodTipoCliempresa() + "'" + ", glsWeb='" + getGlsWeb() + "'"
            + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + ", glsRazonSocialLegal='" + getGlsRazonSocialLegal() + "'" + "}";
    }
}
