package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.EmpresaConsolidadaPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Representa la tabla \"CONSOL_WK\" que contiene empresas consolidadas para un desglose consolidado. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"CONSOL_WK\" que contiene empresas consolidadas para un desglose consolidado. Pertenece al esquema CIF_ADMIN. @author Antonio "
    + "Marrero "
    + "Palomino")
@IdClass(EmpresaConsolidadaPK.class)
@Entity
@Table(name = "CONSOL_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmpresaConsolidada implements Serializable {

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
     * Correlativo para diferencia una empresa de otra dentro del consolidado.
     */
    @NotNull
    @ApiModelProperty(value = "Correlativo para diferencia una empresa de otra dentro del consolidado.", required = true)
    @Column(name = "cor_empresa", nullable = false)
    private Integer corEmpresa;

    /**
     * Nombre de la empresa consolidada.
     */
    @NotNull
    @ApiModelProperty(value = "Nombre de la empresa consolidada.", required = true)
    @Column(name = "nom_empresa", nullable = false)
    private String nomEmpresa;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public EmpresaConsolidada codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public EmpresaConsolidada codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public EmpresaConsolidada codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public EmpresaConsolidada codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public Integer getCorEmpresa() {
        return corEmpresa;
    }

    public EmpresaConsolidada corEmpresa(Integer corEmpresa) {
        this.corEmpresa = corEmpresa;
        return this;
    }

    public void setCorEmpresa(Integer corEmpresa) {
        this.corEmpresa = corEmpresa;
    }

    public String getNomEmpresa() {
        return nomEmpresa;
    }

    public EmpresaConsolidada nomEmpresa(String nomEmpresa) {
        this.nomEmpresa = nomEmpresa;
        return this;
    }

    public void setNomEmpresa(String nomEmpresa) {
        this.nomEmpresa = nomEmpresa;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public String toString() {
        return "EmpresaConsolidada{" + "codCliente='" + getCodCliente() + "'" + ", codTipoDes=" + getCodTipoDes() + ", codSubtipoDes=" + getCodSubtipoDes() + ", codMoneda='"
            + getCodMoneda() + "'" + ", corEmpresa=" + getCorEmpresa() + ", nomEmpresa='" + getNomEmpresa() + "'" + "}";
    }
}
