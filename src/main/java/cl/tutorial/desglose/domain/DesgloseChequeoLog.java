package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseChequeoLogPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Representa la tabla \"DGL_CHK_LOG_WK\" que contiene información de auditoría de desgloses. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"DGL_CHK_LOG_WK\" que contiene información de auditoría de desgloses. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(DesgloseChequeoLogPK.class)
@Entity
@Table(name = "DGL_CHK_LOG_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseChequeoLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifica tipo de proceso en LOG.
     */
    @ApiModelProperty(value = "Identifica tipo de proceso en LOG.")
    @Column(name = "cod_tipo_proceso")
    private String codTipoProceso;

    /**
     * Rut de cliente.
     */
    @Id
    @ApiModelProperty(value = "Rut de cliente.")
    @Column(name = "cod_cliente")
    private String codCliente;

    /**
     * Tipo de desglose.
     */
    @Id
    @ApiModelProperty(value = "Tipo de desglose.")
    @Column(name = "cod_tipo_des")
    private Integer codTipoDes;

    /**
     * Subtipo de desglose.
     */
    @Id
    @ApiModelProperty(value = "Subtipo de desglose.")
    @Column(name = "cod_subtipo_des")
    private Integer codSubtipoDes;

    /**
     * Codigo de moneda.
     */
    @Id
    @ApiModelProperty(value = "Codigo de moneda.")
    @Column(name = "cod_moneda")
    private String codMoneda;

    /**
     * Fecha y hora de LOG.
     */
    @ApiModelProperty(value = "Fecha y hora de LOG.")
    @Column(name = "fec_log")
    private ZonedDateTime fecLog;

    /**
     * Descripcion del LOG.
     */
    @ApiModelProperty(value = "Descripcion del LOG.")
    @Column(name = "gls_log")
    private String glsLog;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodTipoProceso() {
        return codTipoProceso;
    }

    public DesgloseChequeoLog codTipoProceso(String codTipoProceso) {
        this.codTipoProceso = codTipoProceso;
        return this;
    }

    public void setCodTipoProceso(String codTipoProceso) {
        this.codTipoProceso = codTipoProceso;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseChequeoLog codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseChequeoLog codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseChequeoLog codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseChequeoLog codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public ZonedDateTime getFecLog() {
        return fecLog;
    }

    public DesgloseChequeoLog fecLog(ZonedDateTime fecLog) {
        this.fecLog = fecLog;
        return this;
    }

    public void setFecLog(ZonedDateTime fecLog) {
        this.fecLog = fecLog;
    }

    public String getGlsLog() {
        return glsLog;
    }

    public DesgloseChequeoLog glsLog(String glsLog) {
        this.glsLog = glsLog;
        return this;
    }

    public void setGlsLog(String glsLog) {
        this.glsLog = glsLog;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public String toString() {
        return "DesgloseChequeoLog{" + "codTipoProceso='" + codTipoProceso + '\'' + ", codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes="
            + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + ", fecLog=" + fecLog + ", glsLog='" + glsLog + '\'' + '}';
    }
}
