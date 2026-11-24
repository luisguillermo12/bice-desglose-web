package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.TablaServicioMailPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa la tabla \"TBL_SRV_MAIL\". Almacena solicitudes de envío de correo. Pertenece al esquema BCT_ADMIN.
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TBL_SRV_MAIL\". Almacena solicitudes de envío de correo. Pertenece al esquema BCT_ADMIN. @author Antonio Marrero Palomino")
@IdClass(TablaServicioMailPK.class)
@Entity
@Table(name = "TBL_SRV_MAIL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TablaServicioMail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Código sistema.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código sistema.", required = true)
    @Column(name = "cod_sistema", nullable = false)
    private String codSistema;

    /**
     * Número solicitud envio.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Número solicitud envio.", required = true)
    @Column(name = "num_solicitud", nullable = false)
    private Long numSolicitud;

    /**
     * Fecha solicitud.
     */
    @ApiModelProperty(value = "Fecha solicitud.")
    @Column(name = "fec_solicitud")
    private LocalDate fecSolicitud;

    /**
     * Número trx sistema solicitante.
     */
    @ApiModelProperty(value = "Número trx sistema solicitante.")
    @Column(name = "num_trx_sistema")
    private Long numTrxSistema;

    /**
     * Nombre remitente.
     */
    @ApiModelProperty(value = "Nombre remitente.")
    @Column(name = "gls_from_nombre")
    private String glsFromNombre;

    /**
     * Mail remitente.
     */
    @ApiModelProperty(value = "Mail remitente.")
    @Column(name = "gls_from_mail")
    private String glsFromMail;

    /**
     * Asunto mail.
     */
    @ApiModelProperty(value = "Asunto mail.")
    @Column(name = "gls_asunto")
    private String glsAsunto;

    /**
     * Detalle mensaje.
     */
    @ApiModelProperty(value = "Detalle mensaje.")
    @Column(name = "gls_mensaje")
    private Long glsMensaje;

    /**
     * Fecha envío mail.
     */
    @ApiModelProperty(value = "Fecha envío mail.")
    @Column(name = "fec_env_mail")
    private LocalDate fecEnvMail;

    /**
     * Número intento.
     */
    @ApiModelProperty(value = "Número intento.")
    @Column(name = "num_intento")
    private Long numIntento;

    /**
     * Número máximo reintento.
     */
    @ApiModelProperty(value = "Número máximo reintento.")
    @Column(name = "num_max_reintento")
    private Long numMaxReintento;

    /**
     * Estado envío correo: PTE (Pendiente), PRO (En proceso), REI (Reintentar), OK (Enviado), NOK (No enviado).
     */
    @ApiModelProperty(value = "Estado envío correo: PTE (Pendiente), PRO (En proceso), REI (Reintentar), OK (Enviado), NOK (No enviado).")
    @Column(name = "cod_estado")
    private String codEstado;

    /**
     * Detalle respuesta envío.
     */
    @ApiModelProperty(value = "Detalle respuesta envío.")
    @Column(name = "gls_det_respuesta")
    private String glsDetRespuesta;

    /**
     * Tipo del contenido del correo.
     */
    @ApiModelProperty(value = "Tipo del contenido del correo.")
    @Column(name = "gls_content_type")
    private String glsContentType;

    public String getCodSistema() {
        return codSistema;
    }

    public TablaServicioMail codSistema(String codSistema) {
        this.codSistema = codSistema;
        return this;
    }

    public void setCodSistema(String codSistema) {
        this.codSistema = codSistema;
    }

    public Long getNumSolicitud() {
        return numSolicitud;
    }

    public TablaServicioMail numSolicitud(Long numSolicitud) {
        this.numSolicitud = numSolicitud;
        return this;
    }

    public void setNumSolicitud(Long numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    public LocalDate getFecSolicitud() {
        return fecSolicitud;
    }

    public TablaServicioMail fecSolicitud(LocalDate fecSolicitud) {
        this.fecSolicitud = fecSolicitud;
        return this;
    }

    public void setFecSolicitud(LocalDate fecSolicitud) {
        this.fecSolicitud = fecSolicitud;
    }

    public Long getNumTrxSistema() {
        return numTrxSistema;
    }

    public TablaServicioMail numTrxSistema(Long numTrxSistema) {
        this.numTrxSistema = numTrxSistema;
        return this;
    }

    public void setNumTrxSistema(Long numTrxSistema) {
        this.numTrxSistema = numTrxSistema;
    }

    public String getGlsFromNombre() {
        return glsFromNombre;
    }

    public TablaServicioMail glsFromNombre(String glsFromNombre) {
        this.glsFromNombre = glsFromNombre;
        return this;
    }

    public void setGlsFromNombre(String glsFromNombre) {
        this.glsFromNombre = glsFromNombre;
    }

    public String getGlsFromMail() {
        return glsFromMail;
    }

    public TablaServicioMail glsFromMail(String glsFromMail) {
        this.glsFromMail = glsFromMail;
        return this;
    }

    public void setGlsFromMail(String glsFromMail) {
        this.glsFromMail = glsFromMail;
    }

    public String getGlsAsunto() {
        return glsAsunto;
    }

    public TablaServicioMail glsAsunto(String glsAsunto) {
        this.glsAsunto = glsAsunto;
        return this;
    }

    public void setGlsAsunto(String glsAsunto) {
        this.glsAsunto = glsAsunto;
    }

    public Long getGlsMensaje() {
        return glsMensaje;
    }

    public TablaServicioMail glsMensaje(Long glsMensaje) {
        this.glsMensaje = glsMensaje;
        return this;
    }

    public void setGlsMensaje(Long glsMensaje) {
        this.glsMensaje = glsMensaje;
    }

    public LocalDate getFecEnvMail() {
        return fecEnvMail;
    }

    public TablaServicioMail fecEnvMail(LocalDate fecEnvMail) {
        this.fecEnvMail = fecEnvMail;
        return this;
    }

    public void setFecEnvMail(LocalDate fecEnvMail) {
        this.fecEnvMail = fecEnvMail;
    }

    public Long getNumIntento() {
        return numIntento;
    }

    public TablaServicioMail numIntento(Long numIntento) {
        this.numIntento = numIntento;
        return this;
    }

    public void setNumIntento(Long numIntento) {
        this.numIntento = numIntento;
    }

    public Long getNumMaxReintento() {
        return numMaxReintento;
    }

    public TablaServicioMail numMaxReintento(Long numMaxReintento) {
        this.numMaxReintento = numMaxReintento;
        return this;
    }

    public void setNumMaxReintento(Long numMaxReintento) {
        this.numMaxReintento = numMaxReintento;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public TablaServicioMail codEstado(String codEstado) {
        this.codEstado = codEstado;
        return this;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getGlsDetRespuesta() {
        return glsDetRespuesta;
    }

    public TablaServicioMail glsDetRespuesta(String glsDetRespuesta) {
        this.glsDetRespuesta = glsDetRespuesta;
        return this;
    }

    public void setGlsDetRespuesta(String glsDetRespuesta) {
        this.glsDetRespuesta = glsDetRespuesta;
    }

    public String getGlsContentType() {
        return glsContentType;
    }

    public TablaServicioMail glsContentType(String glsContentType) {
        this.glsContentType = glsContentType;
        return this;
    }

    public void setGlsContentType(String glsContentType) {
        this.glsContentType = glsContentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TablaServicioMail))
            return false;
        TablaServicioMail that = (TablaServicioMail) o;
        return Objects.equals(getCodSistema(), that.getCodSistema()) && Objects.equals(getNumSolicitud(), that.getNumSolicitud()) && Objects
            .equals(getFecSolicitud(), that.getFecSolicitud()) && Objects.equals(getNumTrxSistema(), that.getNumTrxSistema()) && Objects
            .equals(getGlsFromNombre(), that.getGlsFromNombre()) && Objects.equals(getGlsFromMail(), that.getGlsFromMail()) && Objects.equals(getGlsAsunto(), that.getGlsAsunto())
            && Objects.equals(getGlsMensaje(), that.getGlsMensaje()) && Objects.equals(getFecEnvMail(), that.getFecEnvMail()) && Objects
            .equals(getNumIntento(), that.getNumIntento()) && Objects.equals(getNumMaxReintento(), that.getNumMaxReintento()) && Objects.equals(getCodEstado(), that.getCodEstado())
            && Objects.equals(getGlsDetRespuesta(), that.getGlsDetRespuesta()) && Objects.equals(getGlsContentType(), that.getGlsContentType());
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(getCodSistema(), getNumSolicitud(), getFecSolicitud(), getNumTrxSistema(), getGlsFromNombre(), getGlsFromMail(), getGlsAsunto(), getGlsMensaje(), getFecEnvMail(),
                getNumIntento(), getNumMaxReintento(), getCodEstado(), getGlsDetRespuesta(), getGlsContentType());
    }

    @Override
    public String toString() {
        return "TablaServicioMail{" + "codSistema='" + codSistema + '\'' + ", numSolicitud=" + numSolicitud + ", fecSolicitud=" + fecSolicitud + ", numTrxSistema=" + numTrxSistema
            + ", glsFromNombre='" + glsFromNombre + '\'' + ", glsFromMail='" + glsFromMail + '\'' + ", glsAsunto='" + glsAsunto + '\'' + ", glsMensaje=" + glsMensaje
            + ", fecEnvMail=" + fecEnvMail + ", numIntento=" + numIntento + ", numMaxReintento=" + numMaxReintento + ", codEstado='" + codEstado + '\'' + ", glsDetRespuesta='"
            + glsDetRespuesta + '\'' + ", glsContentType='" + glsContentType + '\'' + '}';
    }
}
