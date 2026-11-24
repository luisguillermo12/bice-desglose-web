package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the DesgloseImpresion entity.
 */
public class DesgloseImpresionDTO implements Serializable {

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
    private String codMonedaImp;

    @NotNull
    private BigDecimal valDeflacBase;

    @NotNull
    private LocalDate fecCreacion;

    @NotNull
    private LocalDate fecDeflacBase;

    @NotNull
    private Integer codPlantillaDes;

    @NotNull
    private Integer corImpreso;

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

    public String getCodMonedaImp() {
        return codMonedaImp;
    }

    public void setCodMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
    }

    public BigDecimal getValDeflacBase() {
        return valDeflacBase;
    }

    public void setValDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
    }

    public LocalDate getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(LocalDate fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public LocalDate getFecDeflacBase() {
        return fecDeflacBase;
    }

    public void setFecDeflacBase(LocalDate fecDeflacBase) {
        this.fecDeflacBase = fecDeflacBase;
    }

    public Integer getCorImpreso() {
        return corImpreso;
    }

    public void setCorImpreso(Integer corImpreso) {
        this.corImpreso = corImpreso;
    }

    public Integer getCodPlantillaDes() {
        return codPlantillaDes;
    }

    public void setCodPlantillaDes(Integer codPlantillaDes) {
        this.codPlantillaDes = codPlantillaDes;
    }

    @Override
    public String toString() {
        return "DesgloseImpresionDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codMonedaImp='" + codMonedaImp + '\'' + ", valDeflacBase=" + valDeflacBase + ", fecCreacion=" + fecCreacion + ", fecDeflacBase=" + fecDeflacBase
            + ", codPlantillaDes=" + codPlantillaDes + ", corImpreso=" + corImpreso + '}';
    }
}
