package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.ValPeriodoDesglosePK;
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
 * Representa la tabla \"VALPRD_WK\" que contiene detalle de valores asociada a las distintas cuentas y períodos de cada desglose. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"VALPRD_WK\" que contiene detalle de valores asociada a las distintas cuentas y períodos de cada desglose. Pertenece al esquema "
        + "CIF_ADMIN. "
        + "@author Antonio Marrero Palomino")
@IdClass(ValPeriodoDesglosePK.class)
@Entity
@Table(name = "VALPRD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ValPeriodoDesgloseCif implements Serializable {

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
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de concepto.", required = true)
    @Column(name = "cod_tipo_concep", nullable = false)
    private String codTipoConcep;

    /**
     * Correlativo para concepto.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo para concepto.", required = true)
    @Column(name = "cor_concepto", nullable = false)
    private Integer corConcepto;

    /**
     * Correlativo que aplica para multiregistro, de lo contrario siempre es cero.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo que aplica para multiregistro, de lo contrario siempre es cero.", required = true)
    @Column(name = "cor_valor", nullable = false)
    private Integer corValor;

    /**
     * Valor asociado al concepto o cuenta. Precisión 17, escala 3.
     */
    @ApiModelProperty(value = "Valor asociado al concepto o cuenta. Precisión 17, escala 3.")
    @Column(name = "val_concepto", precision = 17, scale = 3)
    private BigDecimal valConcepto;

    /**
     * Fórmula asociada a la celda, solo cuando se ingresa valor expresado en fórmula.
     */
    @ApiModelProperty(value = "Fórmula asociada a la celda, solo cuando se ingresa valor expresado en fórmula.")
    @Column(name = "gls_frml_concep")
    private String glsFrmlConcep;

    /**
     * Descripción del concepto que aplica para los multiregistros.
     */
    @ApiModelProperty(value = "Descripción del concepto que aplica para los multiregistros.")
    @Column(name = "gls_concepto")
    private String glsConcepto;

    /**
     * Valor porcentual asociado a la cuenta. Precisión 4, escala 3.
     */
    @ApiModelProperty(value = "Valor porcentual asociado a la cuenta. Precisión 4, escala 3.")
    @Column(name = "val_prc_concep", precision = 4, scale = 3)
    private BigDecimal valPrcConcep;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public ValPeriodoDesgloseCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public ValPeriodoDesgloseCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public ValPeriodoDesgloseCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public ValPeriodoDesgloseCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public ValPeriodoDesgloseCif fecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
        return this;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public ValPeriodoDesgloseCif codTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
        return this;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public Integer getCorConcepto() {
        return corConcepto;
    }

    public ValPeriodoDesgloseCif corConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
        return this;
    }

    public void setCorConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
    }

    public Integer getCorValor() {
        return corValor;
    }

    public ValPeriodoDesgloseCif corValor(Integer corValor) {
        this.corValor = corValor;
        return this;
    }

    public void setCorValor(Integer corValor) {
        this.corValor = corValor;
    }

    public BigDecimal getValConcepto() {
        return valConcepto;
    }

    public ValPeriodoDesgloseCif valConcepto(BigDecimal valConcepto) {
        this.valConcepto = valConcepto;
        return this;
    }

    public void setValConcepto(BigDecimal valConcepto) {
        this.valConcepto = valConcepto;
    }

    public String getGlsFrmlConcep() {
        return glsFrmlConcep;
    }

    public ValPeriodoDesgloseCif glsFrmlConcep(String glsFrmlConcep) {
        this.glsFrmlConcep = glsFrmlConcep;
        return this;
    }

    public void setGlsFrmlConcep(String glsFrmlConcep) {
        this.glsFrmlConcep = glsFrmlConcep;
    }

    public String getGlsConcepto() {
        return glsConcepto;
    }

    public ValPeriodoDesgloseCif glsConcepto(String glsConcepto) {
        this.glsConcepto = glsConcepto;
        return this;
    }

    public void setGlsConcepto(String glsConcepto) {
        this.glsConcepto = glsConcepto;
    }

    public BigDecimal getValPrcConcep() {
        return valPrcConcep;
    }

    public ValPeriodoDesgloseCif valPrcConcep(BigDecimal valPrcConcep) {
        this.valPrcConcep = valPrcConcep;
        return this;
    }

    public void setValPrcConcep(BigDecimal valPrcConcep) {
        this.valPrcConcep = valPrcConcep;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ValPeriodoDesgloseCif))
            return false;
        ValPeriodoDesgloseCif that = (ValPeriodoDesgloseCif) o;
        return codCliente.equals(that.codCliente) && codTipoDes.equals(that.codTipoDes) && codSubtipoDes.equals(that.codSubtipoDes) && codMoneda.equals(that.codMoneda)
            && codPlantilla.equals(that.codPlantilla) && fecPeriodo.equals(that.fecPeriodo) && codTipoConcep.equals(that.codTipoConcep) && corConcepto.equals(that.corConcepto)
            && corValor.equals(that.corValor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, fecPeriodo, codTipoConcep, corConcepto, corValor);
    }

    @Override
    public String toString() {
        return "ValPeriodoDesglose{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", fecPeriodo=" + fecPeriodo + ", codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + ", corValor="
            + corValor + ", valConcepto=" + valConcepto + ", glsFrmlConcep='" + glsFrmlConcep + '\'' + ", glsConcepto='" + glsConcepto + '\'' + ", valPrcConcep=" + valPrcConcep
            + '}';
    }
}
