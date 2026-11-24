package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.ConceptoImpresionPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"CONIMP_WK\" de conceptos que se generan para la impresión del desglose. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"CONIMP\" de conceptos que se generan para la impresión del desglose. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(ConceptoImpresionPK.class)
@Entity
@Table(name = "CONIMP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConceptoImpresionCif implements Serializable {

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
     * Código del tipo de planilla. Por defecto 0 que es tipo banco.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código del tipo de planilla. Por defecto 0 que es tipo banco.", required = true)
    @Column(name = "cod_plantilla", nullable = false)
    private Integer codPlantilla;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getCodCliente() {
        return codCliente;
    }

    public ConceptoImpresionCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public ConceptoImpresionCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public ConceptoImpresionCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public ConceptoImpresionCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public ConceptoImpresionCif codTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
        return this;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public Integer getCorConcepto() {
        return corConcepto;
    }

    public ConceptoImpresionCif corConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
        return this;
    }

    public void setCorConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConceptoImpresionCif))
            return false;
        ConceptoImpresionCif that = (ConceptoImpresionCif) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda) && Objects.equals(codTipoConcep, that.codTipoConcep) && Objects.equals(corConcepto, that.corConcepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codTipoConcep, corConcepto);
    }

    @Override
    public String toString() {
        return "ConceptoImpresionCif{" +
            "codCliente='" + codCliente + '\'' +
            ", codTipoDes=" + codTipoDes +
            ", codSubtipoDes=" + codSubtipoDes +
            ", codMoneda='" + codMoneda + '\'' +
            ", codTipoConcep='" + codTipoConcep + '\'' +
            ", corConcepto=" + corConcepto +
            ", codPlantilla=" + codPlantilla +
            '}';
    }
}
