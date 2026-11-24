package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.DescripcionCodigoPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"DESCOD\". Contiene parámetros generales. Pertenece al esquema BCT_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"DESCOD\". Contiene parámetros generales. Pertenece al esquema BCT_ADMIN. @author Antonio Marrero Palomino")
@IdClass(DescripcionCodigoPK.class)
@Entity
@Table(name = "DESCOD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DescripcionCodigo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Código de tabla del concepto.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de tabla del concepto.", required = true)
    @Column(name = "cod_tabla", nullable = false)
    private String codTabla;

    /**
     * Código genérico asociado al código de tabla.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código genérico asociado al código de tabla.", required = true)
    @Column(name = "cod_generico", nullable = false)
    private String codGenerico;

    /**
     * Descripción corta.
     */
    @Id
    @ApiModelProperty(value = "Descripción corta.")
    @Column(name = "gls_genericacorta")
    private String glsGenericacorta;

    /**
     * Descripción larga.
     */
    @ApiModelProperty(value = "Descripción larga.")
    @Column(name = "gls_genericalarga")
    private String glsGenericalarga;

    /**
     * Código de vigencia del registro.
     */
    @NotNull
    @ApiModelProperty(value = "Código de vigencia del registro.", required = true)
    @Column(name = "cod_reg_status", nullable = false)
    private Integer codRegStatus;

    /**
     * Código de modificación del registro.
     */
    @ApiModelProperty(value = "Código de modificación del registro.")
    @Column(name = "cod_reg_log")
    private Integer codRegLog;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodTabla() {
        return codTabla;
    }

    public DescripcionCodigo codTabla(String codTabla) {
        this.codTabla = codTabla;
        return this;
    }

    public void setCodTabla(String codTabla) {
        this.codTabla = codTabla;
    }

    public String getCodGenerico() {
        return codGenerico;
    }

    public DescripcionCodigo codGenerico(String codGenerico) {
        this.codGenerico = codGenerico;
        return this;
    }

    public void setCodGenerico(String codGenerico) {
        this.codGenerico = codGenerico;
    }

    public String getGlsGenericacorta() {
        return glsGenericacorta;
    }

    public DescripcionCodigo glsGenericacorta(String glsGenericacorta) {
        this.glsGenericacorta = glsGenericacorta;
        return this;
    }

    public void setGlsGenericacorta(String glsGenericacorta) {
        this.glsGenericacorta = glsGenericacorta;
    }

    public String getGlsGenericalarga() {
        return glsGenericalarga;
    }

    public DescripcionCodigo glsGenericalarga(String glsGenericalarga) {
        this.glsGenericalarga = glsGenericalarga;
        return this;
    }

    public void setGlsGenericalarga(String glsGenericalarga) {
        this.glsGenericalarga = glsGenericalarga;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public DescripcionCodigo codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public DescripcionCodigo codRegLog(Integer codRegLog) {
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
        if (!(o instanceof DescripcionCodigo))
            return false;
        DescripcionCodigo that = (DescripcionCodigo) o;
        return Objects.equals(codTabla, that.codTabla) && Objects.equals(codGenerico, that.codGenerico) && Objects.equals(codRegStatus, that.codRegStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTabla, codGenerico, codRegStatus);
    }

    @Override
    public String toString() {
        return "DescripcionCodigo{" + "codTabla='" + getCodTabla() + "'" + ", codGenerico='" + getCodGenerico() + "'" + ", glsGenericacorta='" + getGlsGenericacorta() + "'"
            + ", glsGenericalarga='" + getGlsGenericalarga() + "'" + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
