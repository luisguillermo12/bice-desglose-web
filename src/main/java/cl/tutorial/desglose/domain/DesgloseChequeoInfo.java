package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.DesgloseChequeoInfoPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Representa la tabla \"DGL_CHK_INFO_WK\" que contiene logs de chequeo. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"DGL_CHK_INFO_WK\" que contiene logs de chequeo. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(DesgloseChequeoInfoPK.class)
@Entity
@Table(name = "DGL_CHK_INFO_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseChequeoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Fecha y hora del chequeo.
     */
    @ApiModelProperty(value = "Fecha y hora del chequeo.")
    @Column(name = "fec_check")
    private ZonedDateTime fecCheck;

    /**
     * Tipo de informe.
     */
    @ApiModelProperty(value = "Tipo de informe.")
    @Column(name = "cod_tipo_informe")
    private String codTipoInforme;

    /**
     * Glosa asociada al tipo de informe.
     */
    @ApiModelProperty(value = "Glosa asociada al tipo de informe.")
    @Column(name = "gls_factor_informe")
    private String glsFactorInforme;

    /**
     * Rut de cliente.
     */
    @Id
    @ApiModelProperty(value = "Rut de cliente.")
    @Column(name = "cod_cliente")
    private String codCliente;

    /**
     * Tipo de desglose.
     */
    @Id
    @ApiModelProperty(value = "Tipo de desglose.")
    @Column(name = "cod_tipo_des")
    private Integer codTipoDes;

    /**
     * Subtipo de desglose.
     */
    @Id
    @ApiModelProperty(value = "Subtipo de desglose.")
    @Column(name = "cod_subtipo_des")
    private Integer codSubtipoDes;

    /**
     * Codigo de moneda.
     */
    @Id
    @ApiModelProperty(value = "Codigo de moneda.")
    @Column(name = "cod_moneda")
    private String codMoneda;

    /**
     * Fecha para el período.
     */
    @ApiModelProperty(value = "Fecha para el período.")
    @Column(name = "fec_periodo")
    private LocalDate fecPeriodo;

    /**
     * Contador de ocurrencias.
     */
    @ApiModelProperty(value = "Contador de ocurrencias.")
    @Column(name = "num_rank")
    private Integer numRank;

    /**
     * Fecha de modificación de desglose.
     */
    @ApiModelProperty(value = "Fecha de modificación de desglose.")
    @Column(name = "fec_user_modif")
    private LocalDate fecUserModif;

    /**
     * Usuario que realiza modificación.
     */
    @ApiModelProperty(value = "Usuario que realiza modificación.")
    @Column(name = "gls_user_modif")
    private String glsUserModif;

    /**
     * Valor deflactor base. Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Valor deflactor base. Precisión 38, escala 3.")
    @Column(name = "val_deflac_base", precision = 10, scale = 2)
    private BigDecimal valDeflacBase;

    /**
     * Valor deflactor período. Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Valor deflactor período. Precisión 38, escala 3.")
    @Column(name = "val_deflac_periodo", precision = 10, scale = 2)
    private BigDecimal valDeflacPeriodo;

    /**
     * Valor de deflactacion (base dividido por período). Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Valor de deflactacion (base dividido por período). Precisión 38, escala 3.")
    @Column(name = "val_deflac_calc", precision = 10, scale = 2)
    private BigDecimal valDeflacCalc;

    /**
     * Valor de concepto en base de datos. Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Valor de concepto en base de datos. Precisión 38, escala 3.")
    @Column(name = "val_concepto_bd", precision = 10, scale = 2)
    private BigDecimal valConceptoBd;

    /**
     * Valor de concepto calculado en consulta. Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Valor de concepto calculado en consulta. Precisión 38, escala 3.")
    @Column(name = "val_concepto_online", precision = 10, scale = 2)
    private BigDecimal valConceptoOnline;

    /**
     * Detalle de fórmula.
     */
    @ApiModelProperty(value = "Detalle de fórmula.")
    @Column(name = "gls_concepto_formula")
    private String glsConceptoFormula;

    /**
     * Diferencia entre valor base de datos y valor calculado en consulta. Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Diferencia entre valor base de datos y valor calculado en consulta. Precisión 38, escala 3.")
    @Column(name = "val_dif", precision = 10, scale = 2)
    private BigDecimal valDif;

    /**
     * Monto activo. Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Monto activo. Precisión 38, escala 3.")
    @Column(name = "mto_activo", precision = 10, scale = 2)
    private BigDecimal mtoActivo;

    /**
     * Monto pasivo-patrimonio. Precisión 38, escala 3.
     */
    @ApiModelProperty(value = "Monto pasivo-patrimonio. Precisión 38, escala 3.")
    @Column(name = "mto_pasivo_patr", precision = 10, scale = 2)
    private BigDecimal mtoPasivoPatr;

    /**
     * Correlativo para concepto.
     */
    @ApiModelProperty(value = "Correlativo para concepto.")
    @Column(name = "cor_concepto")
    private Integer corConcepto;

    /**
     * Correlativo que aplica para multiregistro, de lo contrario siempre es cero.
     */
    @ApiModelProperty(value = "Correlativo que aplica para multiregistro, de lo contrario siempre es cero.")
    @Column(name = "cor_valor")
    private Integer corValor;

    /**
     * Tipo de concepto.
     */
    @ApiModelProperty(value = "Tipo de concepto.")
    @Column(name = "cod_tipo_concep")
    private String codTipoConcep;

    /**
     * Estado.
     */
    @ApiModelProperty(value = "Estado.")
    @Column(name = "cod_estado")
    private String codEstado;

    /**
     * Comentarios.
     */
    @ApiModelProperty(value = "Comentarios.")
    @Column(name = "gls_observacion")
    private String glsObservacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public ZonedDateTime getFecCheck() {
        return fecCheck;
    }

    public DesgloseChequeoInfo fecCheck(ZonedDateTime fecCheck) {
        this.fecCheck = fecCheck;
        return this;
    }

    public void setFecCheck(ZonedDateTime fecCheck) {
        this.fecCheck = fecCheck;
    }

    public String getCodTipoInforme() {
        return codTipoInforme;
    }

    public DesgloseChequeoInfo codTipoInforme(String codTipoInforme) {
        this.codTipoInforme = codTipoInforme;
        return this;
    }

    public void setCodTipoInforme(String codTipoInforme) {
        this.codTipoInforme = codTipoInforme;
    }

    public String getGlsFactorInforme() {
        return glsFactorInforme;
    }

    public DesgloseChequeoInfo glsFactorInforme(String glsFactorInforme) {
        this.glsFactorInforme = glsFactorInforme;
        return this;
    }

    public void setGlsFactorInforme(String glsFactorInforme) {
        this.glsFactorInforme = glsFactorInforme;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseChequeoInfo codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseChequeoInfo codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseChequeoInfo codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseChequeoInfo codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public DesgloseChequeoInfo fecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
        return this;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public Integer getNumRank() {
        return numRank;
    }

    public DesgloseChequeoInfo numRank(Integer numRank) {
        this.numRank = numRank;
        return this;
    }

    public void setNumRank(Integer numRank) {
        this.numRank = numRank;
    }

    public LocalDate getFecUserModif() {
        return fecUserModif;
    }

    public DesgloseChequeoInfo fecUserModif(LocalDate fecUserModif) {
        this.fecUserModif = fecUserModif;
        return this;
    }

    public void setFecUserModif(LocalDate fecUserModif) {
        this.fecUserModif = fecUserModif;
    }

    public String getGlsUserModif() {
        return glsUserModif;
    }

    public DesgloseChequeoInfo glsUserModif(String glsUserModif) {
        this.glsUserModif = glsUserModif;
        return this;
    }

    public void setGlsUserModif(String glsUserModif) {
        this.glsUserModif = glsUserModif;
    }

    public BigDecimal getValDeflacBase() {
        return valDeflacBase;
    }

    public DesgloseChequeoInfo valDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
        return this;
    }

    public void setValDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
    }

    public BigDecimal getValDeflacPeriodo() {
        return valDeflacPeriodo;
    }

    public DesgloseChequeoInfo valDeflacPeriodo(BigDecimal valDeflacPeriodo) {
        this.valDeflacPeriodo = valDeflacPeriodo;
        return this;
    }

    public void setValDeflacPeriodo(BigDecimal valDeflacPeriodo) {
        this.valDeflacPeriodo = valDeflacPeriodo;
    }

    public BigDecimal getValDeflacCalc() {
        return valDeflacCalc;
    }

    public DesgloseChequeoInfo valDeflacCalc(BigDecimal valDeflacCalc) {
        this.valDeflacCalc = valDeflacCalc;
        return this;
    }

    public void setValDeflacCalc(BigDecimal valDeflacCalc) {
        this.valDeflacCalc = valDeflacCalc;
    }

    public BigDecimal getValConceptoBd() {
        return valConceptoBd;
    }

    public DesgloseChequeoInfo valConceptoBd(BigDecimal valConceptoBd) {
        this.valConceptoBd = valConceptoBd;
        return this;
    }

    public void setValConceptoBd(BigDecimal valConceptoBd) {
        this.valConceptoBd = valConceptoBd;
    }

    public BigDecimal getValConceptoOnline() {
        return valConceptoOnline;
    }

    public DesgloseChequeoInfo valConceptoOnline(BigDecimal valConceptoOnline) {
        this.valConceptoOnline = valConceptoOnline;
        return this;
    }

    public void setValConceptoOnline(BigDecimal valConceptoOnline) {
        this.valConceptoOnline = valConceptoOnline;
    }

    public String getGlsConceptoFormula() {
        return glsConceptoFormula;
    }

    public DesgloseChequeoInfo glsConceptoFormula(String glsConceptoFormula) {
        this.glsConceptoFormula = glsConceptoFormula;
        return this;
    }

    public void setGlsConceptoFormula(String glsConceptoFormula) {
        this.glsConceptoFormula = glsConceptoFormula;
    }

    public BigDecimal getValDif() {
        return valDif;
    }

    public DesgloseChequeoInfo valDif(BigDecimal valDif) {
        this.valDif = valDif;
        return this;
    }

    public void setValDif(BigDecimal valDif) {
        this.valDif = valDif;
    }

    public BigDecimal getMtoActivo() {
        return mtoActivo;
    }

    public DesgloseChequeoInfo mtoActivo(BigDecimal mtoActivo) {
        this.mtoActivo = mtoActivo;
        return this;
    }

    public void setMtoActivo(BigDecimal mtoActivo) {
        this.mtoActivo = mtoActivo;
    }

    public BigDecimal getMtoPasivoPatr() {
        return mtoPasivoPatr;
    }

    public DesgloseChequeoInfo mtoPasivoPatr(BigDecimal mtoPasivoPatr) {
        this.mtoPasivoPatr = mtoPasivoPatr;
        return this;
    }

    public void setMtoPasivoPatr(BigDecimal mtoPasivoPatr) {
        this.mtoPasivoPatr = mtoPasivoPatr;
    }

    public Integer getCorConcepto() {
        return corConcepto;
    }

    public DesgloseChequeoInfo corConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
        return this;
    }

    public void setCorConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
    }

    public Integer getCorValor() {
        return corValor;
    }

    public DesgloseChequeoInfo corValor(Integer corValor) {
        this.corValor = corValor;
        return this;
    }

    public void setCorValor(Integer corValor) {
        this.corValor = corValor;
    }

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public DesgloseChequeoInfo codTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
        return this;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public DesgloseChequeoInfo codEstado(String codEstado) {
        this.codEstado = codEstado;
        return this;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getGlsObservacion() {
        return glsObservacion;
    }

    public DesgloseChequeoInfo glsObservacion(String glsObservacion) {
        this.glsObservacion = glsObservacion;
        return this;
    }

    public void setGlsObservacion(String glsObservacion) {
        this.glsObservacion = glsObservacion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public String toString() {
        return "DesgloseChequeoInfo{" + "fecCheck=" + fecCheck + ", codTipoInforme='" + codTipoInforme + '\'' + ", glsFactorInforme='" + glsFactorInforme + '\'' + ", codCliente='"
            + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\'' + ", fecPeriodo=" + fecPeriodo
            + ", numRank=" + numRank + ", fecUserModif=" + fecUserModif + ", glsUserModif='" + glsUserModif + '\'' + ", valDeflacBase=" + valDeflacBase + ", valDeflacPeriodo="
            + valDeflacPeriodo + ", valDeflacCalc=" + valDeflacCalc + ", valConceptoBd=" + valConceptoBd + ", valConceptoOnline=" + valConceptoOnline + ", glsConceptoFormula='"
            + glsConceptoFormula + '\'' + ", valDif=" + valDif + ", mtoActivo=" + mtoActivo + ", mtoPasivoPatr=" + mtoPasivoPatr + ", corConcepto=" + corConcepto + ", corValor="
            + corValor + ", codTipoConcep='" + codTipoConcep + '\'' + ", codEstado='" + codEstado + '\'' + ", glsObservacion='" + glsObservacion + '\'' + '}';
    }
}
