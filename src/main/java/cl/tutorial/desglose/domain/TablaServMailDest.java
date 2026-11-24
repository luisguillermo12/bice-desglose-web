package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.TablaServicioMailDestPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"TBL_SRV_MAIL_DEST\". Almacena los destinatarios de las solicitudes de envío de correo. Pertenece al esquema BCT_ADMIN.
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TBL_SRV_MAIL_DEST\". Almacena los destinatarios de las solicitudes de envío de correo. Pertenece al esquema BCT_ADMIN. @author "
    + "Antonio Marrero Palomino")
@IdClass(TablaServicioMailDestPK.class)
@Entity
@Table(name = "TBL_SRV_MAIL_DEST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TablaServMailDest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Codigo sistema.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Codigo sistema.", required = true)
    @Column(name = "cod_sistema", nullable = false)
    private String codSistema;

    /**
     * Número solicitud envío.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Número solicitud envío.", required = true)
    @Column(name = "num_solicitud", nullable = false)
    private Long numSolicitud;

    /**
     * Nombre destinatario.
     */
    @NotNull
    @ApiModelProperty(value = "Nombre destinatario.", required = true)
    @Column(name = "gls_to_nombre", nullable = false)
    private String glsToNombre;

    /**
     * Mail destinatario.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Mail destinatario.", required = true)
    @Column(name = "gls_to_mail", nullable = false)
    private String glsToMail;

    /**
     * Tipo destinatario TO, CC, CCO.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo destinatario TO, CC, CCO.", required = true)
    @Column(name = "cod_tipo_dest", nullable = false)
    private String codTipoDest;

    public String getCodSistema() {
        return codSistema;
    }

    public TablaServMailDest codSistema(String codSistema) {
        this.codSistema = codSistema;
        return this;
    }

    public void setCodSistema(String codSistema) {
        this.codSistema = codSistema;
    }

    public Long getNumSolicitud() {
        return numSolicitud;
    }

    public TablaServMailDest numSolicitud(Long numSolicitud) {
        this.numSolicitud = numSolicitud;
        return this;
    }

    public void setNumSolicitud(Long numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    public String getGlsToNombre() {
        return glsToNombre;
    }

    public TablaServMailDest glsToNombre(String glsToNombre) {
        this.glsToNombre = glsToNombre;
        return this;
    }

    public void setGlsToNombre(String glsToNombre) {
        this.glsToNombre = glsToNombre;
    }

    public String getGlsToMail() {
        return glsToMail;
    }

    public TablaServMailDest glsToMail(String glsToMail) {
        this.glsToMail = glsToMail;
        return this;
    }

    public void setGlsToMail(String glsToMail) {
        this.glsToMail = glsToMail;
    }

    public String getCodTipoDest() {
        return codTipoDest;
    }

    public TablaServMailDest codTipoDest(String codTipoDest) {
        this.codTipoDest = codTipoDest;
        return this;
    }

    public void setCodTipoDest(String codTipoDest) {
        this.codTipoDest = codTipoDest;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TablaServMailDest))
            return false;
        TablaServMailDest that = (TablaServMailDest) o;
        return Objects.equals(getCodSistema(), that.getCodSistema()) && Objects.equals(getNumSolicitud(), that.getNumSolicitud()) && Objects
            .equals(getGlsToNombre(), that.getGlsToNombre()) && Objects.equals(getGlsToMail(), that.getGlsToMail()) && Objects.equals(getCodTipoDest(), that.getCodTipoDest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodSistema(), getNumSolicitud(), getGlsToNombre(), getGlsToMail(), getCodTipoDest());
    }

    @Override
    public String toString() {
        return "TablaServMailDest{" + "codSistema='" + codSistema + '\'' + ", numSolicitud=" + numSolicitud + ", glsToNombre='" + glsToNombre + '\'' + ", glsToMail='" + glsToMail
            + '\'' + ", codTipoDest='" + codTipoDest + '\'' + '}';
    }
}
