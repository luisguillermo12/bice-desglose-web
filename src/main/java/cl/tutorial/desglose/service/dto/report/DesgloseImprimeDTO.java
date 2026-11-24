package cl.tutorial.desglose.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class DesgloseImprimeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String       codCliente;
    private Integer      codTipoPlantilla;
    private Integer      codTipoDes;
    private Integer      codSubtipoDes;
    private String       codMoneda;
    private Integer      activePeriodsIndexes[];
    private Boolean      impresionRapida;
    private String       codMonedaImpresion;
    private BigDecimal   deflactorBase;
    private BigDecimal[] deflactores;
    private BigDecimal[] tiposCambio;
    private LocalDate    fecDeflacBase;


    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoPlantilla() {
        return codTipoPlantilla;
    }

    public void setCodTipoPlantilla(Integer codTipoPlantilla) {
        this.codTipoPlantilla = codTipoPlantilla;
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

    public Integer[] getActivePeriodsIndexes() {
        return activePeriodsIndexes;
    }

    public void setActivePeriodsIndexes(Integer[] activePeriodsIndexes) {
        this.activePeriodsIndexes = activePeriodsIndexes;
    }

    public Boolean getImpresionRapida() {
        return impresionRapida;
    }

    public void setImpresionRapida(Boolean impresionRapida) {
        this.impresionRapida = impresionRapida;
    }

    public String getCodMonedaImpresion() {
        return codMonedaImpresion;
    }

    public void setCodMonedaImpresion(String codMonedaImpresion) {
        this.codMonedaImpresion = codMonedaImpresion;
    }

    public BigDecimal getDeflactorBase() {
        return deflactorBase;
    }

    public void setDeflactorBase(BigDecimal deflactorBase) {
        this.deflactorBase = deflactorBase;
    }

    public BigDecimal[] getDeflactores() {
        return deflactores;
    }

    public void setDeflactores(BigDecimal[] deflactores) {
        this.deflactores = deflactores;
    }

    public BigDecimal[] getTiposCambio() {
        return tiposCambio;
    }

    public void setTiposCambio(BigDecimal[] tiposCambio) {
        this.tiposCambio = tiposCambio;
    }

    public LocalDate getFecDeflacBase() {
        return fecDeflacBase;
    }

    public void setFecDeflacBase(LocalDate fecDeflacBase) {
        this.fecDeflacBase = fecDeflacBase;
    }

    @Override
    public String toString() {
        return "DesgloseImprimeDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoPlantilla=" + codTipoPlantilla + ", codTipoDes=" + codTipoDes + ", codSubtipoDes="
            + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + ", activePeriodsIndexes=" + Arrays.toString(activePeriodsIndexes) + ", impresionRapida=" + impresionRapida
            + ", codMonedaImpresion='" + codMonedaImpresion + '\'' + ", deflactorBase=" + deflactorBase + ", deflactores=" + Arrays.toString(deflactores) + ", tiposCambio="
            + Arrays.toString(tiposCambio) + ", fecDeflacBase=" + fecDeflacBase + '}';
    }
}
