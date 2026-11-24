package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Periodo entity.
 */
public class PeriodoDTO implements Serializable {

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
    private Integer codPlantilla;

    @NotNull
    private LocalDate fecPeriodo;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer numMeses;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000")
    private BigDecimal valDeflacPrd;

    private String codAuditor;

    private String glsOtrosAudit;

    @NotNull
    private Integer codEstadoPrd;

    @NotNull
    private Integer codEstadoAudi;

    private BigDecimal mtoSbifDdVig;

    private BigDecimal mtoSbifDdMor;

    private BigDecimal mtoSbifDdVen;

    private BigDecimal mtoSbifDdCas;

    private BigDecimal mtoSbifDiVig;

    private BigDecimal mtoSbifDiVen;

    private BigDecimal mtoSbifDiCas;

    @NotNull
    private String nomUserCrea;

    @NotNull
    private LocalDate fecUserCrea;

    private String nomUserModif;

    private LocalDate fecUserModif;

    private LocalDate fecInformSbif;

    @NotNull
    private BigDecimal valTipoCambio;

    private BigDecimal mtoSbifImp90179;

    private String codEstadoPry;

    private String userEdit;

    private String userClosePry;

    private LocalDate fecUserEditPry;

    private LocalDate fecUserClosePry;

    public List<ValPeriodoDesgloseDTO> valPeriodoDesgloseDTOList;

    public List<ValPeriodoProyeccionDesgloseDTO> valPeriodoProyeccionDesgloseDTOList;

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

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public Integer getNumMeses() {
        return numMeses;
    }

    public void setNumMeses(Integer numMeses) {
        this.numMeses = numMeses;
    }

    public BigDecimal getValDeflacPrd() {
        return valDeflacPrd;
    }

    public void setValDeflacPrd(BigDecimal valDeflacPrd) {
        this.valDeflacPrd = valDeflacPrd;
    }

    public String getCodAuditor() {
        return codAuditor;
    }

    public void setCodAuditor(String codAuditor) {
        this.codAuditor = codAuditor;
    }

    public String getGlsOtrosAudit() {
        return glsOtrosAudit;
    }

    public void setGlsOtrosAudit(String glsOtrosAudit) {
        this.glsOtrosAudit = glsOtrosAudit;
    }

    public Integer getCodEstadoPrd() {
        return codEstadoPrd;
    }

    public void setCodEstadoPrd(Integer codEstadoPrd) {
        this.codEstadoPrd = codEstadoPrd;
    }

    public Integer getCodEstadoAudi() {
        return codEstadoAudi;
    }

    public void setCodEstadoAudi(Integer codEstadoAudi) {
        this.codEstadoAudi = codEstadoAudi;
    }

    public BigDecimal getMtoSbifDdVig() {
        return mtoSbifDdVig;
    }

    public void setMtoSbifDdVig(BigDecimal mtoSbifDdVig) {
        this.mtoSbifDdVig = mtoSbifDdVig;
    }

    public BigDecimal getMtoSbifDdMor() {
        return mtoSbifDdMor;
    }

    public void setMtoSbifDdMor(BigDecimal mtoSbifDdMor) {
        this.mtoSbifDdMor = mtoSbifDdMor;
    }

    public BigDecimal getMtoSbifDdVen() {
        return mtoSbifDdVen;
    }

    public void setMtoSbifDdVen(BigDecimal mtoSbifDdVen) {
        this.mtoSbifDdVen = mtoSbifDdVen;
    }

    public BigDecimal getMtoSbifDdCas() {
        return mtoSbifDdCas;
    }

    public void setMtoSbifDdCas(BigDecimal mtoSbifDdCas) {
        this.mtoSbifDdCas = mtoSbifDdCas;
    }

    public BigDecimal getMtoSbifDiVig() {
        return mtoSbifDiVig;
    }

    public void setMtoSbifDiVig(BigDecimal mtoSbifDiVig) {
        this.mtoSbifDiVig = mtoSbifDiVig;
    }

    public BigDecimal getMtoSbifDiVen() {
        return mtoSbifDiVen;
    }

    public void setMtoSbifDiVen(BigDecimal mtoSbifDiVen) {
        this.mtoSbifDiVen = mtoSbifDiVen;
    }

    public BigDecimal getMtoSbifDiCas() {
        return mtoSbifDiCas;
    }

    public void setMtoSbifDiCas(BigDecimal mtoSbifDiCas) {
        this.mtoSbifDiCas = mtoSbifDiCas;
    }

    public String getNomUserCrea() {
        return nomUserCrea;
    }

    public void setNomUserCrea(String nomUserCrea) {
        this.nomUserCrea = nomUserCrea;
    }

    public LocalDate getFecUserCrea() {
        return fecUserCrea;
    }

    public void setFecUserCrea(LocalDate fecUserCrea) {
        this.fecUserCrea = fecUserCrea;
    }

    public String getNomUserModif() {
        return nomUserModif;
    }

    public void setNomUserModif(String nomUserModif) {
        this.nomUserModif = nomUserModif;
    }

    public LocalDate getFecUserModif() {
        return fecUserModif;
    }

    public void setFecUserModif(LocalDate fecUserModif) {
        this.fecUserModif = fecUserModif;
    }

    public LocalDate getFecInformSbif() {
        return fecInformSbif;
    }

    public void setFecInformSbif(LocalDate fecInformSbif) {
        this.fecInformSbif = fecInformSbif;
    }

    public BigDecimal getValTipoCambio() {
        return valTipoCambio;
    }

    public void setValTipoCambio(BigDecimal valTipoCambio) {
        this.valTipoCambio = valTipoCambio;
    }

    public BigDecimal getMtoSbifImp90179() {
        return mtoSbifImp90179;
    }

    public void setMtoSbifImp90179(BigDecimal mtoSbifImp90179) {
        this.mtoSbifImp90179 = mtoSbifImp90179;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getCodEstadoPry() {
        return codEstadoPry;
    }

    public void setCodEstadoPry(String codEstadoPry) {
        this.codEstadoPry = codEstadoPry;
    }

    public String getUserEdit() {
        return userEdit;
    }

    public void setUserEdit(String userEdit) {
        this.userEdit = userEdit;
    }

    public String getUserClosePry() {
        return userClosePry;
    }

    public void setUserClosePry(String userClosePry) {
        this.userClosePry = userClosePry;
    }

    public LocalDate getFecUserEditPry() {
        return fecUserEditPry;
    }

    public void setFecUserEditPry(LocalDate fecUserEditPry) {
        this.fecUserEditPry = fecUserEditPry;
    }

    public LocalDate getFecUserClosePry() {
        return fecUserClosePry;
    }

    public void setFecUserClosePry(LocalDate fecUserClosePry) {
        this.fecUserClosePry = fecUserClosePry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PeriodoDTO))
            return false;
        PeriodoDTO that = (PeriodoDTO) o;
        return codCliente.equals(that.codCliente) && codTipoDes.equals(that.codTipoDes) && codSubtipoDes.equals(that.codSubtipoDes) && codMoneda.equals(that.codMoneda)
            && codPlantilla.equals(that.codPlantilla) && fecPeriodo.equals(that.fecPeriodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, fecPeriodo);
    }

    @Override
    public String toString() {
        return "PeriodoDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", fecPeriodo=" + fecPeriodo + ", numMeses=" + numMeses + ", valDeflacPrd=" + valDeflacPrd + ", codAuditor='" + codAuditor + '\''
            + ", glsOtrosAudit='" + glsOtrosAudit + '\'' + ", codEstadoPrd=" + codEstadoPrd + ", codEstadoAudi=" + codEstadoAudi + ", mtoSbifDdVig=" + mtoSbifDdVig
            + ", mtoSbifDdMor=" + mtoSbifDdMor + ", mtoSbifDdVen=" + mtoSbifDdVen + ", mtoSbifDdCas=" + mtoSbifDdCas + ", mtoSbifDiVig=" + mtoSbifDiVig + ", mtoSbifDiVen="
            + mtoSbifDiVen + ", mtoSbifDiCas=" + mtoSbifDiCas + ", nomUserCrea='" + nomUserCrea + '\'' + ", fecUserCrea=" + fecUserCrea + ", nomUserModif='" + nomUserModif + '\''
            + ", fecUserModif=" + fecUserModif + ", fecInformSbif=" + fecInformSbif + ", valTipoCambio=" + valTipoCambio + ", mtoSbifImp90179=" + mtoSbifImp90179
            + ", codEstadoPry='" + codEstadoPry + '\'' + ", userEdit='" + userEdit + '\'' + ", userClosePry='" + userClosePry + '\'' + ", fecUserEditPry=" + fecUserEditPry
            + ", fecUserClosePry=" + fecUserClosePry + ", valPeriodoDesgloseDTOList=" + valPeriodoDesgloseDTOList + ", valPeriodoProyeccionDesgloseDTOList="
            + valPeriodoProyeccionDesgloseDTOList + '}';
    }
}
