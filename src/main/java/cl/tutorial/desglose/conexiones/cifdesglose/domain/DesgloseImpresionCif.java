package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseImpresionPK;
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
 * Representa la tabla \"DESIMP_WK\" que contiene detalle del desglose generado para la impresión. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"DESIMP_WK\" que contiene detalle del desglose generado para la impresión. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero "
        + "Palomino")
@IdClass(DesgloseImpresionPK.class)
@Entity
@Table(name = "DESIMP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseImpresionCif implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Rut de cliente.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Rut de cliente.", required = true)
    @Column(name = "cod_cliente", nullable = false)
    private String codCliente;

    /**
     * Tipo de desglose.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de desglose.", required = true)
    @Column(name = "cod_tipo_des", nullable = false)
    private Integer codTipoDes;

    /**
     * Subtipo de desglose.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Subtipo de desglose.", required = true)
    @Column(name = "cod_subtipo_des", nullable = false)
    private Integer codSubtipoDes;

    /**
     * Código de moneda.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de moneda.", required = true)
    @Column(name = "cod_moneda", nullable = false)
    private String codMoneda;

    /**
     * Código de moneda de impresión.
     */
    @NotNull
    @ApiModelProperty(value = "Código de moneda de impresión.", required = true)
    @Column(name = "cod_moneda_imp", nullable = false)
    private String codMonedaImp;

    /**
     * Valor deflactor base. Precisión 6, escala 3.
     */
    @NotNull
    @ApiModelProperty(value = "Valor deflactor base. Precisión 6, escala 3.", required = true)
    @Column(name = "val_deflac_base", precision = 10, scale = 2, nullable = false)
    private BigDecimal valDeflacBase;

    /**
     * Fecha de creación del desglose.
     */
    @NotNull
    @ApiModelProperty(value = "Fecha de creación del desglose.", required = true)
    @Column(name = "fec_creacion", nullable = false)
    private LocalDate fecCreacion;

    /**
     * Fecha asociada al deflactor base.
     */
    @NotNull
    @ApiModelProperty(value = "Fecha asociada al deflactor base.", required = true)
    @Column(name = "fec_deflac_base", nullable = false)
    private LocalDate fecDeflacBase;

    /**
     * Correlativo de impresión.
     */
    @NotNull
    @ApiModelProperty(value = "Correlativo de impresión.", required = true)
    @Column(name = "cor_impreso", nullable = false)
    private Integer corImpreso;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseImpresionCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseImpresionCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseImpresionCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseImpresionCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getCodMonedaImp() {
        return codMonedaImp;
    }

    public DesgloseImpresionCif codMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
        return this;
    }

    public void setCodMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
    }

    public BigDecimal getValDeflacBase() {
        return valDeflacBase;
    }

    public DesgloseImpresionCif valDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
        return this;
    }

    public void setValDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
    }

    public LocalDate getFecCreacion() {
        return fecCreacion;
    }

    public DesgloseImpresionCif fecCreacion(LocalDate fecCreacion) {
        this.fecCreacion = fecCreacion;
        return this;
    }

    public void setFecCreacion(LocalDate fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public LocalDate getFecDeflacBase() {
        return fecDeflacBase;
    }

    public DesgloseImpresionCif fecDeflacBase(LocalDate fecDeflacBase) {
        this.fecDeflacBase = fecDeflacBase;
        return this;
    }

    public void setFecDeflacBase(LocalDate fecDeflacBase) {
        this.fecDeflacBase = fecDeflacBase;
    }

    public Integer getCorImpreso() {
        return corImpreso;
    }

    public DesgloseImpresionCif corImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
        return this;
    }

    public void setCorImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseImpresionCif))
            return false;
        DesgloseImpresionCif that = (DesgloseImpresionCif) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codMonedaImp, that.codMonedaImp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codMonedaImp);
    }

    @Override
    public String toString() {
        return "DesgloseImpresion{" + "codCliente='" + getCodCliente() + "'" + ", codTipoDes=" + getCodTipoDes() + ", codSubtipoDes=" + getCodSubtipoDes() + ", codMoneda='"
            + getCodMoneda() + "'" + ", codMonedaImp='" + getCodMonedaImp() + "'" + ", valDeflacBase=" + getValDeflacBase() + ", fecCreacion='" + getFecCreacion() + "'"
            + ", fecDeflacBase='" + getFecDeflacBase() + "'" + ", corImpreso=" + getCorImpreso() + "}";
    }
}
