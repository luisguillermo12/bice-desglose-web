package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ValPeriodoProyeccionDesglosePK extends NewCommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Fecha para el período.
     */
    private LocalDate fecPeriodo;

    /**
     * Tipo de concepto.
     */
    private String codTipoConcepPry;

    /**
     * Correlativo para concepto.
     */
    private Integer corConceptoPry;

    /**
     * correlativo para fecha de periodo proyectado
     */
    private LocalDate fecPeriodoPry;

    /**
     * correlativo para codigo de plantilla proyectado
     */
    private Integer codPlantillaPry;

    public static ValPeriodoProyeccionDesglosePK newInstance(PeriodoPK periodoPK) {
        ValPeriodoProyeccionDesglosePK pk = new ValPeriodoProyeccionDesglosePK();
        pk.setCodCliente(periodoPK.codCliente);
        pk.setCodTipoDes(periodoPK.codTipoDes);
        pk.setCodSubtipoDes(periodoPK.codSubtipoDes);
        pk.setCodMoneda(periodoPK.codMoneda);
        pk.setFecPeriodo(periodoPK.fecPeriodo);
        return pk;
    }



    public LocalDate getFecPeriodoPry() {
        return fecPeriodoPry;
    }

    public void setFecPeriodoPry(LocalDate fecPeriodoPry) {
        this.fecPeriodoPry = fecPeriodoPry;
    }

    public Integer getCodPlantillaPry() {
        return codPlantillaPry;
    }

    public void setCodPlantillaPry(Integer codPlantillaPry) {
        this.codPlantillaPry = codPlantillaPry;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ValPeriodoProyeccionDesglosePK))
            return false;
        if (!super.equals(o))
            return false;
        ValPeriodoProyeccionDesglosePK that = (ValPeriodoProyeccionDesglosePK) o;
        return Objects.equals(fecPeriodo, that.fecPeriodo) && Objects.equals(codTipoConcepPry, that.codTipoConcepPry) && Objects.equals(corConceptoPry, that.corConceptoPry)
            && Objects.equals(fecPeriodoPry, that.fecPeriodoPry) && Objects.equals(codPlantillaPry, that.codPlantillaPry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fecPeriodo, codTipoConcepPry, corConceptoPry, fecPeriodoPry, codPlantillaPry);
    }

    @Override
    public String toString() {
        return "ValPeriodoProyeccionDesglosePK{" + "fecPeriodo=" + fecPeriodo + ", codTipoConcepPry='" + codTipoConcepPry + '\'' + ", corConceptoPry=" + corConceptoPry
            + ", fecPeriodoPry=" + fecPeriodoPry + ", codPlantillaPry=" + codPlantillaPry + '}';
    }

}
