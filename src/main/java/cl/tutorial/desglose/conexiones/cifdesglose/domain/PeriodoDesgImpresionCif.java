package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.PeriodoDesgImpresionPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa la tabla \"PRDIMP_WK\" que contiene información para períodos del desglose generado para la impresión. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"PRDIMP_WK\" que contiene información para períodos del desglose generado para la impresión. Pertenece al esquema CIF_ADMIN. @author "
        + "Antonio"
        + " Marrero Palomino")
@IdClass(PeriodoDesgImpresionPK.class)
@Entity
@Table(name = "PRDIMP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PeriodoDesgImpresionCif implements Serializable {

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
     * Código del tipo de planilla. Por defecto 0 que es tipo banco.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código del tipo de planilla. Por defecto 0 que es tipo banco.", required = true)
    @Column(name = "cod_plantilla", nullable = false)
    private Integer codPlantilla;

    /**
     * Código de moneda de impresión.
     */
    @NotNull
    @ApiModelProperty(value = "Código de moneda de impresión.", required = true)
    @Column(name = "cod_moneda_imp", nullable = false)
    private String codMonedaImp;

    /**
     * Fecha para el período.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha para el período.", required = true)
    @Column(name = "fec_periodo", nullable = false)
    private LocalDate fecPeriodo;

    /**
     * Valor deflator para el período. Precisión 6, escala 3.
     */
    @NotNull
    @ApiModelProperty(value = "Valor deflator para el período. Precisión 6, escala 3.", required = true)
    @Column(name = "val_deflac_prd", precision = 10, scale = 2, nullable = false)
    private BigDecimal valDeflacPrd;

    /**
     * Valor tipo de cambio. Precisión 12, escala 6.
     */
    @NotNull
    @ApiModelProperty(value = "Valor tipo de cambio. Precisión 12, escala 6.", required = true)
    @Column(name = "val_tipo_cambio", precision = 10, scale = 2, nullable = false)
    private BigDecimal valTipoCambio;

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

    public PeriodoDesgImpresionCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public PeriodoDesgImpresionCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public PeriodoDesgImpresionCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public PeriodoDesgImpresionCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getCodMonedaImp() {
        return codMonedaImp;
    }

    public PeriodoDesgImpresionCif codMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
        return this;
    }

    public void setCodMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public PeriodoDesgImpresionCif fecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
        return this;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public BigDecimal getValDeflacPrd() {
        return valDeflacPrd;
    }

    public PeriodoDesgImpresionCif valDeflacPrd(BigDecimal valDeflacPrd) {
        this.valDeflacPrd = valDeflacPrd;
        return this;
    }

    public void setValDeflacPrd(BigDecimal valDeflacPrd) {
        this.valDeflacPrd = valDeflacPrd;
    }

    public BigDecimal getValTipoCambio() {
        return valTipoCambio;
    }

    public PeriodoDesgImpresionCif valTipoCambio(BigDecimal valTipoCambio) {
        this.valTipoCambio = valTipoCambio;
        return this;
    }

    public void setValTipoCambio(BigDecimal valTipoCambio) {
        this.valTipoCambio = valTipoCambio;
    }

    public Integer getCorImpreso() {
        return corImpreso;
    }

    public PeriodoDesgImpresionCif corImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
        return this;
    }

    public void setCorImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public String toString() {
        return "PeriodoDesgImpresion{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", codMonedaImp='" + codMonedaImp + '\'' + ", fecPeriodo=" + fecPeriodo + ", valDeflacPrd=" + valDeflacPrd + ", valTipoCambio="
            + valTipoCambio + ", corImpreso=" + corImpreso + '}';
    }
}
