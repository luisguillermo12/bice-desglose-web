package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.PeriodoDesgConsolPK;
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
 * Representa la tabla \"PRCCNS_WK\" que contiene los períodos de un desglose consolidado. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"PRCCNS_WK\" que contiene los períodos de un desglose consolidado. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(PeriodoDesgConsolPK.class)
@Entity
@Table(name = "PRCCNS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PeriodoDesgConsolCif implements Serializable {

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
     * Correlativo para diferenciar una empresa de otra dentro del consolidado.
     */
    @NotNull
    @ApiModelProperty(value = "Correlativo para diferenciar una empresa de otra dentro del consolidado.", required = true)
    @Column(name = "cor_empresa", nullable = false)
    private Integer corEmpresa;

    /**
     * Fecha para el período.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha para el período.", required = true)
    @Column(name = "fec_periodo", nullable = false)
    private LocalDate fecPeriodo;

    /**
     * Valor porcentual asociado a la empresa dentro del consolidado. Precisión 5, escala 4.
     */
    @NotNull
    @ApiModelProperty(value = "Valor porcentual asociado a la empresa dentro del consolidado. Precisión 5, escala 4.", required = true)
    @Column(name = "val_prc_consol", precision = 10, scale = 2, nullable = false)
    private BigDecimal valPrcConsol;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public PeriodoDesgConsolCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public PeriodoDesgConsolCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public PeriodoDesgConsolCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public PeriodoDesgConsolCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public Integer getCorEmpresa() {
        return corEmpresa;
    }

    public PeriodoDesgConsolCif corEmpresa(Integer corEmpresa) {
        this.corEmpresa = corEmpresa;
        return this;
    }

    public void setCorEmpresa(Integer corEmpresa) {
        this.corEmpresa = corEmpresa;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public PeriodoDesgConsolCif fecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
        return this;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public BigDecimal getValPrcConsol() {
        return valPrcConsol;
    }

    public PeriodoDesgConsolCif valPrcConsol(BigDecimal valPrcConsol) {
        this.valPrcConsol = valPrcConsol;
        return this;
    }

    public void setValPrcConsol(BigDecimal valPrcConsol) {
        this.valPrcConsol = valPrcConsol;
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
        if (!(o instanceof PeriodoDesgConsolCif))
            return false;
        PeriodoDesgConsolCif that = (PeriodoDesgConsolCif) o;
        return codCliente.equals(that.codCliente) && codTipoDes.equals(that.codTipoDes) && codSubtipoDes.equals(that.codSubtipoDes) && codMoneda.equals(that.codMoneda)
            && codPlantilla.equals(that.codPlantilla) && fecPeriodo.equals(that.fecPeriodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, fecPeriodo);
    }

    @Override
    public String toString() {
        return "PeriodoDesgConsol{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", corEmpresa=" + corEmpresa + ", fecPeriodo=" + fecPeriodo + ", valPrcConsol=" + valPrcConsol + '}';
    }
}
