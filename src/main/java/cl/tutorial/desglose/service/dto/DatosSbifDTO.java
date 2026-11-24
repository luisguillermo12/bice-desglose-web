package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for DatosSbifDTO.
 */
public class DatosSbifDTO implements Serializable{
    private  static final long serialVersionUID = 1L;

    private Boolean hayDeudaSbif;
    private LocalDate fecInforme;
    private BigDecimal mtoSbif_DVig;
    private BigDecimal mtoSbif_DMor;
    private BigDecimal mtoSbif_DVen;
    private BigDecimal mtoSbif_DCas;
    private BigDecimal mtoSbif_IVig;
    private BigDecimal mtoSbif_IVen;
    private BigDecimal mtoSbif_ICas;
    private BigDecimal mtoSbif_Imp90_179;

    public DatosSbifDTO() {

    }

    public DatosSbifDTO(Boolean hayDeudaSbif, LocalDate fecInforme, BigDecimal mtoSbif_DVig, BigDecimal mtoSbif_DMor, BigDecimal mtoSbif_DVen, BigDecimal mtoSbif_DCas, BigDecimal mtoSbif_IVig, BigDecimal mtoSbif_IVen, BigDecimal mtoSbif_ICas, BigDecimal mtoSbif_Imp90_179) {
        this.hayDeudaSbif = hayDeudaSbif;
        this.fecInforme = fecInforme;
        this.mtoSbif_DVig = mtoSbif_DVig;
        this.mtoSbif_DMor = mtoSbif_DMor;
        this.mtoSbif_DVen = mtoSbif_DVen;
        this.mtoSbif_DCas = mtoSbif_DCas;
        this.mtoSbif_IVig = mtoSbif_IVig;
        this.mtoSbif_IVen = mtoSbif_IVen;
        this.mtoSbif_ICas = mtoSbif_ICas;
        this.mtoSbif_Imp90_179 = mtoSbif_Imp90_179;
    }

    public Boolean getHayDeudaSbif() {
        return hayDeudaSbif;
    }

    public void setHayDeudaSbif(Boolean hayDeudaSbif) {
        this.hayDeudaSbif = hayDeudaSbif;
    }

    public LocalDate getFecInforme() {
        return fecInforme;
    }

    public void setFecInforme(LocalDate fecInforme) {
        this.fecInforme = fecInforme;
    }

    public BigDecimal getMtoSbif_DVig() {
        return mtoSbif_DVig;
    }

    public void setMtoSbif_DVig(BigDecimal mtoSbif_DVig) {
        this.mtoSbif_DVig = mtoSbif_DVig;
    }

    public BigDecimal getMtoSbif_DMor() {
        return mtoSbif_DMor;
    }

    public void setMtoSbif_DMor(BigDecimal mtoSbif_DMor) {
        this.mtoSbif_DMor = mtoSbif_DMor;
    }

    public BigDecimal getMtoSbif_DVen() {
        return mtoSbif_DVen;
    }

    public void setMtoSbif_DVen(BigDecimal mtoSbif_DVen) {
        this.mtoSbif_DVen = mtoSbif_DVen;
    }

    public BigDecimal getMtoSbif_DCas() {
        return mtoSbif_DCas;
    }

    public void setMtoSbif_DCas(BigDecimal mtoSbif_DCas) {
        this.mtoSbif_DCas = mtoSbif_DCas;
    }

    public BigDecimal getMtoSbif_IVig() {
        return mtoSbif_IVig;
    }

    public void setMtoSbif_IVig(BigDecimal mtoSbif_IVig) {
        this.mtoSbif_IVig = mtoSbif_IVig;
    }

    public BigDecimal getMtoSbif_IVen() {
        return mtoSbif_IVen;
    }

    public void setMtoSbif_IVen(BigDecimal mtoSbif_IVen) {
        this.mtoSbif_IVen = mtoSbif_IVen;
    }

    public BigDecimal getMtoSbif_ICas() {
        return mtoSbif_ICas;
    }

    public void setMtoSbif_ICas(BigDecimal mtoSbif_ICas) {
        this.mtoSbif_ICas = mtoSbif_ICas;
    }

    public BigDecimal getMtoSbif_Imp90_179() {
        return mtoSbif_Imp90_179;
    }

    public void setMtoSbif_Imp90_179(BigDecimal mtoSbif_Imp90_179) {
        this.mtoSbif_Imp90_179 = mtoSbif_Imp90_179;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatosSbifDTO)) return false;
        DatosSbifDTO that = (DatosSbifDTO) o;
        return Objects.equals(hayDeudaSbif, that.hayDeudaSbif) &&
            Objects.equals(fecInforme, that.fecInforme) &&
            Objects.equals(mtoSbif_DVig, that.mtoSbif_DVig) &&
            Objects.equals(mtoSbif_DMor, that.mtoSbif_DMor) &&
            Objects.equals(mtoSbif_DVen, that.mtoSbif_DVen) &&
            Objects.equals(mtoSbif_DCas, that.mtoSbif_DCas) &&
            Objects.equals(mtoSbif_IVig, that.mtoSbif_IVig) &&
            Objects.equals(mtoSbif_IVen, that.mtoSbif_IVen) &&
            Objects.equals(mtoSbif_ICas, that.mtoSbif_ICas) &&
            Objects.equals(mtoSbif_Imp90_179, that.mtoSbif_Imp90_179);
    }

    @Override
    public int hashCode() {

        return Objects.hash(hayDeudaSbif, fecInforme, mtoSbif_DVig, mtoSbif_DMor, mtoSbif_DVen, mtoSbif_DCas, mtoSbif_IVig, mtoSbif_IVen, mtoSbif_ICas, mtoSbif_Imp90_179);
    }

    @Override
    public String toString() {
        return "DatosSbifDTO{" +
            "hayDeudaSbif=" + hayDeudaSbif +
            ", fecInforme=" + fecInforme +
            ", mtoSbif_DVig=" + mtoSbif_DVig +
            ", mtoSbif_DMor=" + mtoSbif_DMor +
            ", mtoSbif_DVen=" + mtoSbif_DVen +
            ", mtoSbif_DCas=" + mtoSbif_DCas +
            ", mtoSbif_IVig=" + mtoSbif_IVig +
            ", mtoSbif_IVen=" + mtoSbif_IVen +
            ", mtoSbif_ICas=" + mtoSbif_ICas +
            ", mtoSbif_Imp90_179=" + mtoSbif_Imp90_179 +
            '}';
    }
}
