package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.TablaValorPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa la tabla \"TABVAL\". Contiene la definición de valores de conceptos definidos en DESCOD. Pertenece al esquema BCT_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"TABVAL\". Contiene la definición de valores de conceptos definidos en DESCOD. Pertenece al esquema BCT_ADMIN. @author Antonio Marrero "
        + "Palomino")
@IdClass(TablaValorPK.class)
@Entity
@Table(name = "TABVAL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TablaValor implements Serializable {

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
     * Fecha de validez.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha de validez.", required = true)
    @Column(name = "fec_validez", nullable = false)
    private LocalDate fecValidez;

    /**
     * Valor asociado al concepto.
     */
    @ApiModelProperty(value = "Valor asociado al concepto.")
    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    /**
     * Tipo del valor.
     */
    @ApiModelProperty(value = "Tipo del valor.")
    @Column(name = "cod_tipovalor")
    private Integer codTipovalor;

    /**
     * Código de vigencia del registro.
     */
    @Id
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

    public TablaValor codTabla(String codTabla) {
        this.codTabla = codTabla;
        return this;
    }

    public void setCodTabla(String codTabla) {
        this.codTabla = codTabla;
    }

    public String getCodGenerico() {
        return codGenerico;
    }

    public TablaValor codGenerico(String codGenerico) {
        this.codGenerico = codGenerico;
        return this;
    }

    public void setCodGenerico(String codGenerico) {
        this.codGenerico = codGenerico;
    }

    public LocalDate getFecValidez() {
        return fecValidez;
    }

    public TablaValor fecValidez(LocalDate fecValidez) {
        this.fecValidez = fecValidez;
        return this;
    }

    public void setFecValidez(LocalDate fecValidez) {
        this.fecValidez = fecValidez;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TablaValor valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getCodTipovalor() {
        return codTipovalor;
    }

    public TablaValor codTipovalor(Integer codTipovalor) {
        this.codTipovalor = codTipovalor;
        return this;
    }

    public void setCodTipovalor(Integer codTipovalor) {
        this.codTipovalor = codTipovalor;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public TablaValor codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public TablaValor codRegLog(Integer codRegLog) {
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
        if (!(o instanceof TablaValor))
            return false;
        TablaValor that = (TablaValor) o;
        return Objects.equals(codTabla, that.codTabla) && Objects.equals(codGenerico, that.codGenerico) && Objects.equals(fecValidez, that.fecValidez) && Objects
            .equals(codRegStatus, that.codRegStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTabla, codGenerico, fecValidez, codRegStatus);
    }

    @Override
    public String toString() {
        return "TablaValor{" + "codTabla='" + getCodTabla() + "'" + ", codGenerico='" + getCodGenerico() + "'" + ", fecValidez='" + getFecValidez() + "'" + ", valor=" + getValor()
            + ", codTipovalor=" + getCodTipovalor() + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
