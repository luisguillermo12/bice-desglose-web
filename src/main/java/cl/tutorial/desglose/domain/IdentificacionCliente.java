package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.IdentificacionClientePK;
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
 * Representa la tabla \"IDECLI\" que contiene la relación rut cliente versus número de cliente. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"IDECLI\" que contiene la relación rut cliente versus número de cliente. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(IdentificacionClientePK.class)
@Entity
@Table(name = "IDECLI")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IdentificacionCliente implements Serializable {

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
     * Tipo de identificación del cliente.
     */
    @Id
    @ApiModelProperty(value = "Tipo de identificación del cliente.")
    @Column(name = "cod_tipo_iden")
    private Integer codTipoIden;

    /**
     * Código de modificación del registro.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de modificación del registro.", required = true)
    @Column(name = "cod_reg_log", nullable = false)
    private Integer codRegLog;

    /**
     * Rut del cliente.
     */
    @ApiModelProperty(value = "Rut del cliente.")
    @Column(name = "num_iden")
    private String numIden;

    /**
     * Fecha de creación.
     */
    @ApiModelProperty(value = "Fecha de creación.")
    @Column(name = "fec_creacion")
    private LocalDate fecCreacion;

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

    public IdentificacionCliente numCliente(Long numCliente) {
        this.numCliente = numCliente;
        return this;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public Integer getCodTipoIden() {
        return codTipoIden;
    }

    public IdentificacionCliente codTipoIden(Integer codTipoIden) {
        this.codTipoIden = codTipoIden;
        return this;
    }

    public void setCodTipoIden(Integer codTipoIden) {
        this.codTipoIden = codTipoIden;
    }

    public String getNumIden() {
        return numIden;
    }

    public IdentificacionCliente numIden(String numIden) {
        this.numIden = numIden;
        return this;
    }

    public void setNumIden(String numIden) {
        this.numIden = numIden;
    }

    public LocalDate getFecCreacion() {
        return fecCreacion;
    }

    public IdentificacionCliente fecCreacion(LocalDate fecCreacion) {
        this.fecCreacion = fecCreacion;
        return this;
    }

    public void setFecCreacion(LocalDate fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public IdentificacionCliente codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public IdentificacionCliente codRegLog(Integer codRegLog) {
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
        if (!(o instanceof IdentificacionCliente))
            return false;
        IdentificacionCliente that = (IdentificacionCliente) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codTipoIden, that.codTipoIden) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codTipoIden, codRegLog);
    }

    @Override
    public String toString() {
        return "IdentificacionCliente{" + "numCliente=" + getNumCliente() + ", codTipoIden=" + getCodTipoIden() + ", numIden='" + getNumIden() + "'" + ", fecCreacion='"
            + getFecCreacion() + "'" + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
