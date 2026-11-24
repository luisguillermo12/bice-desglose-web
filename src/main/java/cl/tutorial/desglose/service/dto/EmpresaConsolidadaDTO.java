package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the EmpresaConsolidada entity.
 */
public class EmpresaConsolidadaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCliente;

    @NotNull
    private Integer codTipoDes;

    @NotNull
    private Integer codSubtipoDes;

    @NotNull
    private String codMoneda;

    @NotNull
    private Integer corEmpresa;

    @NotNull
    private String nomEmpresa;

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public Integer getCorEmpresa() {
        return corEmpresa;
    }

    public void setCorEmpresa(Integer corEmpresa) {
        this.corEmpresa = corEmpresa;
    }

    public String getNomEmpresa() {
        return nomEmpresa;
    }

    public void setNomEmpresa(String nomEmpresa) {
        this.nomEmpresa = nomEmpresa;
    }

    @Override
    public String toString() {
        return "EmpresaConsolidadaDTO{" + "codCliente='" + getCodCliente() + "'" + ", codTipoDes=" + getCodTipoDes() + ", codSubtipoDes=" + getCodSubtipoDes() + ", codMoneda='"
            + getCodMoneda() + "'" + ", corEmpresa=" + getCorEmpresa() + ", nomEmpresa='" + getNomEmpresa() + "'" + "}";
    }
}
