package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.ValPeriodoProyeccionDesglosePK;
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
 *
 * @author Carlos Eduardo Rodriguez Perez
 */
@ApiModel(
    description = "Representa la tabla \"VALPRD_PRY_WK\" que contiene detalle de valores asociada a las distintas cuentas y períodos de cada desglose. Pertenece al esquema "
        + "CIF_ADMIN. "
        + "@carlos eduardo rodriguez perez")
@IdClass(ValPeriodoProyeccionDesglosePK.class)
@Entity
@Table(name = "VALPRD_PRY_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ValPeriodoProyeccionDesglose implements Serializable {

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
    @ApiModelProperty(value = "Tipo de concepto de proyeccion", required = true)
    @Column(name = "cod_tipo_concep_pry", nullable = false)
    private String codTipoConcepPry;

    /**
     * Correlativo para concepto.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo para concepto.", required = true)
    @Column(name = "cor_concepto_pry", nullable = false)
    private Integer corConceptoPry;

    /**
     * Código del tipo de planilla. Por defecto 0 que es tipo banco.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código del tipo de planilla de proyeccion", required = true)
    @Column(name = "cod_plantilla_pry", nullable = false)
    private Integer codPlantillaPry;

    /**
     * Fecha para el período.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha para el período proyectado", required = true)
    @Column(name = "fec_periodo_pry", nullable = false)
    private LocalDate fecPeriodoPry;

    /**
     * Valor asociado al concepto o cuenta. Precisión 17, escala 3.
     */
    @ApiModelProperty(value = "Valor asociado al concepto o cuenta. Precisión 17, escala 3.")
    @Column(name = "val_concepto_pry", precision = 17, scale = 3)
    private BigDecimal valConceptoPry;

    /**
     * Fórmula asociada a la celda, solo cuando se ingresa valor expresado en fórmula.
     */
    @ApiModelProperty(value = "Fórmula asociada a la celda, solo cuando se ingresa valor expresado en fórmula.")
    @Column(name = "gls_frml_concep_pry")
    private String glsFrmlConcepPry;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public ValPeriodoProyeccionDesglose codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public ValPeriodoProyeccionDesglose codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public ValPeriodoProyeccionDesglose codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public ValPeriodoProyeccionDesglose codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public ValPeriodoProyeccionDesglose fecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
        return this;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }


    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Integer getCodPlantillaPry() {
        return codPlantillaPry;
    }

    public void setCodPlantillaPry(Integer codPlantillaPry) {
        this.codPlantillaPry = codPlantillaPry;
    }

    public LocalDate getFecPeriodoPry() {
        return fecPeriodoPry;
    }

    public void setFecPeriodoPry(LocalDate fecPeriodoPry) {
        this.fecPeriodoPry = fecPeriodoPry;
    }

    public String getCodTipoConcepPry() {
        return codTipoConcepPry;
    }

    public void setCodTipoConcepPry(String codTipoConcepPry) {
        this.codTipoConcepPry = codTipoConcepPry;
    }

    public Integer getCorConceptoPry() {
        return corConceptoPry;
    }

    public void setCorConceptoPry(Integer corConceptoPry) {
        this.corConceptoPry = corConceptoPry;
    }

    public BigDecimal getValConceptoPry() {
        return valConceptoPry;
    }

    public void setValConceptoPry(BigDecimal valConceptoPry) {
        this.valConceptoPry = valConceptoPry;
    }

    public String getGlsFrmlConcepPry() {
        return glsFrmlConcepPry;
    }

    public void setGlsFrmlConcepPry(String glsFrmlConcepPry) {
        this.glsFrmlConcepPry = glsFrmlConcepPry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ValPeriodoProyeccionDesglose))
            return false;
        ValPeriodoProyeccionDesglose that = (ValPeriodoProyeccionDesglose) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codPlantilla, that.codPlantilla) && Objects.equals(fecPeriodo, that.fecPeriodo) && Objects
            .equals(codTipoConcepPry, that.codTipoConcepPry) && Objects.equals(corConceptoPry, that.corConceptoPry) && Objects.equals(codPlantillaPry, that.codPlantillaPry)
            && Objects.equals(fecPeriodoPry, that.fecPeriodoPry) && Objects.equals(valConceptoPry, that.valConceptoPry) && Objects.equals(glsFrmlConcepPry, that.glsFrmlConcepPry);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, fecPeriodo, codTipoConcepPry, corConceptoPry, codPlantillaPry, fecPeriodoPry, valConceptoPry,
                glsFrmlConcepPry);
    }

    @Override
    public String toString() {
        return "ValPeriodoProyeccionDesglose{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='"
            + codMoneda + '\'' + ", codPlantilla=" + codPlantilla + ", fecPeriodo=" + fecPeriodo + ", codTipoConcepPry='" + codTipoConcepPry + '\'' + ", corConceptoPry="
            + corConceptoPry + ", codPlantillaPry=" + codPlantillaPry + ", fecPeriodoPry=" + fecPeriodoPry + ", valConceptoPry=" + valConceptoPry + ", glsFrmlConcepPry='"
            + glsFrmlConcepPry + '\'' + '}';
    }

}
