package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.ValPeriodoDesgImprPK;
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
 * Representa la tabla \"VALIMP_WK\" que contiene detalle de valores asociada a las distintas cuentas y períodos de cada desglose generado para la impresión. Pertenece al esquema
 * CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"VALIMP_WK\" que contiene detalle de valores asociada a las distintas cuentas y períodos de cada desglose generado para la "
    + "impresión. "
    + "Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(ValPeriodoDesgImprPK.class)
@Entity
@Table(name = "VALIMP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ValPeriodoDesgImprCif implements Serializable {

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
    //    @Id
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
     * Tipo de concepto.
     */
    //    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de concepto.", required = true)
    @Column(name = "cod_tipo_concep", nullable = false)
    private String codTipoConcep;

    /**
     * Correlativo para concepto.
     */
    //    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo para concepto.", required = true)
    @Column(name = "cor_concepto", nullable = false)
    private Integer corConcepto;

    /**
     * Correlativo que aplica para multiregistro, de lo contrario siempre es cero.
     */
    //    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo que aplica para multiregistro, de lo contrario siempre es cero.", required = true)
    @Column(name = "cor_valor", nullable = false)
    private Integer corValor;

    /**
     * Correlativo de impresión.
     */
    //    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo de impresión.", required = true)
    @Column(name = "cor_impreso", nullable = false)
    private Integer corImpreso;

    /**
     * Correlativo de cod tipo de plantilla desglose.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo de tipo plantilla desglose.", required = true)
    @Column(name = "cod_plantilla", nullable = false)
    private Integer codPlantilla;

    /**
     * Valor asociado al concepto o cuenta. Precisión 17, escala 3.
     */
    @NotNull
    @ApiModelProperty(value = "Valor asociado al concepto o cuenta. Precisión 17, escala 3.", required = true)
    @Column(name = "val_concepto", precision = 10, scale = 2, nullable = false)
    private BigDecimal valConcepto;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public ValPeriodoDesgImprCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public ValPeriodoDesgImprCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public ValPeriodoDesgImprCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public ValPeriodoDesgImprCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getCodMonedaImp() {
        return codMonedaImp;
    }

    public ValPeriodoDesgImprCif codMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
        return this;
    }

    public void setCodMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public ValPeriodoDesgImprCif fecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
        return this;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public ValPeriodoDesgImprCif codTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
        return this;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public Integer getCorConcepto() {
        return corConcepto;
    }

    public ValPeriodoDesgImprCif corConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
        return this;
    }

    public void setCorConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
    }

    public Integer getCorValor() {
        return corValor;
    }

    public ValPeriodoDesgImprCif corValor(Integer corValor) {
        this.corValor = corValor;
        return this;
    }

    public void setCorValor(Integer corValor) {
        this.corValor = corValor;
    }

    public BigDecimal getValConcepto() {
        return valConcepto;
    }

    public ValPeriodoDesgImprCif valConcepto(BigDecimal valConcepto) {
        this.valConcepto = valConcepto;
        return this;
    }

    public void setValConcepto(BigDecimal valConcepto) {
        this.valConcepto = valConcepto;
    }

    public Integer getCorImpreso() {
        return corImpreso;
    }

    public ValPeriodoDesgImprCif corImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
        return this;
    }

    public void setCorImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ValPeriodoDesgImprCif))
            return false;
        ValPeriodoDesgImprCif that = (ValPeriodoDesgImprCif) o;
        return codCliente.equals(that.codCliente) && codTipoDes.equals(that.codTipoDes) && codSubtipoDes.equals(that.codSubtipoDes) && codMoneda.equals(that.codMoneda)
            && codMonedaImp.equals(that.codMonedaImp) && fecPeriodo.equals(that.fecPeriodo) && codTipoConcep.equals(that.codTipoConcep) && corConcepto.equals(that.corConcepto)
            && corValor.equals(that.corValor) && corImpreso.equals(that.corImpreso) && codPlantilla.equals(that.codPlantilla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codMonedaImp, fecPeriodo, codTipoConcep, corConcepto, corValor, corImpreso, codPlantilla);
    }

    @Override
    public String toString() {
        return "ValPeriodoDesgImpr{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codMonedaImp='" + codMonedaImp + '\'' + ", fecPeriodo=" + fecPeriodo + ", codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + ", corValor="
            + corValor + ", valConcepto=" + valConcepto + ", corImpreso=" + corImpreso + ", codPlantilla=" + codPlantilla + '}';
    }
}
