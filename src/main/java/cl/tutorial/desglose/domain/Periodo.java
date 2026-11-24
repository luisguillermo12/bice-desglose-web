package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.PeriodoPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa la tabla \"PERIOD_WK\" que contiene información para períodos del desglose. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"PERIOD_WK\" que contiene información para períodos del desglose. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(PeriodoPK.class)
@Entity
@Table(name = "PERIOD_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Periodo implements Serializable {

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
     * Fecha para el período.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Fecha para el período.", required = true)
    @Column(name = "fec_periodo", nullable = false)
    private LocalDate fecPeriodo;

    /**
     * Cantidad de meses asociadas al período. Valor entre 1 y 12, ambos inclusive.
     */
    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @ApiModelProperty(value = "Cantidad de meses asociadas al período. Valor entre 1 y 12, ambos inclusive.", required = true)
    @Column(name = "num_meses", nullable = false)
    private Integer numMeses;

    /**
     * Valor deflator para el período. Precisión 6, escala 3. Valor entre 1 y 1000, sin incluir ambos.
     */
    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000")
    @ApiModelProperty(value = "Valor deflator para el período. Precisión 6, escala 3. Valor entre 1 y 1000, sin incluir ambos.", required = true)
    @Column(name = "val_deflac_prd", precision = 10, scale = 2, nullable = false)
    private BigDecimal valDeflacPrd;

    /**
     * Código empresa auditora.
     */
    @ApiModelProperty(value = "Código empresa auditora.")
    @Column(name = "cod_auditor")
    private String codAuditor;

    /**
     * Descripción de otra auditora.
     */
    @ApiModelProperty(value = "Descripción de otra auditora.")
    @Column(name = "gls_otros_audit")
    private String glsOtrosAudit;

    /**
     * Estado del período. Valores permitidos (0, 1, 2).
     */
    @NotNull
    @ApiModelProperty(value = "Estado del período. Valores permitidos (0, 1, 2).", required = true)
    @Column(name = "cod_estado_prd", nullable = false)
    private Integer codEstadoPrd;

    /**
     * Estado de la auditoría. Valores permitidos (0, 1, 2).
     */
    @NotNull
    @ApiModelProperty(value = "Estado de la auditoría. Valores permitidos (0, 1, 2).", required = true)
    @Column(name = "cod_estado_audi", nullable = false)
    private Integer codEstadoAudi;

    /**
     * Valor deuda directa vigente. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda directa vigente. Precisión 17, escala 2.")
    @Column(name = "mto_sbif_dd_vig", precision = 10, scale = 2)
    private BigDecimal mtoSbifDdVig;

    /**
     * Valor deuda directa morosa. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda directa morosa. Precisión 17, escala 2.")
    @Column(name = "mto_sbif_dd_mor", precision = 10, scale = 2)
    private BigDecimal mtoSbifDdMor;

    /**
     * Valor deuda directa vencida. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda directa vencida. Precisión 17, escala 2.")
    @Column(name = "mto_sbif_dd_ven", precision = 10, scale = 2)
    private BigDecimal mtoSbifDdVen;

    /**
     * Valor deuda directa castigada. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda directa castigada. Precisión 17, escala 2.")
    @Column(name = "mto_sbif_dd_cas", precision = 10, scale = 2)
    private BigDecimal mtoSbifDdCas;

    /**
     * Valor deuda indirecta vigente. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda indirecta vigente. Precisión 17, escala 2.")
    @Column(name = "mto_sbif_di_vig", precision = 10, scale = 2)
    private BigDecimal mtoSbifDiVig;

    /**
     * Valor deuda indirecta vencida. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda indirecta vencida. Precisión 17, escala 2.")
    @Column(name = "mto_sbif_di_ven", precision = 10, scale = 2)
    private BigDecimal mtoSbifDiVen;

    /**
     * Valor deuda indirecta castigada. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda indirecta castigada. Precisión 17, escala 2.")
    @Column(name = "mto_sbif_di_cas", precision = 10, scale = 2)
    private BigDecimal mtoSbifDiCas;

    /**
     * Login usuario que crea período.
     */
    @NotNull
    @ApiModelProperty(value = "Login usuario que crea período.", required = true)
    @Column(name = "nom_user_crea", nullable = false)
    private String nomUserCrea;

    /**
     * Fecha de creación del período.
     */
    @NotNull
    @ApiModelProperty(value = "Fecha de creación del período.", required = true)
    @Column(name = "fec_user_crea", nullable = false)
    private LocalDate fecUserCrea;

    /**
     * Login usuario que modifica período.
     */
    @ApiModelProperty(value = "Login usuario que modifica período.")
    @Column(name = "nom_user_modif")
    private String nomUserModif;

    /**
     * Fecha de modificación del período.
     */
    @ApiModelProperty(value = "Fecha de modificación del período.")
    @Column(name = "fec_user_modif")
    private LocalDate fecUserModif;

    /**
     * Fecha de actualización deudas SBIF.
     */
    @ApiModelProperty(value = "Fecha de actualización deudas SBIF.")
    @Column(name = "fec_inform_sbif")
    private LocalDate fecInformSbif;

    /**
     * Valor tipo de cambio. Precisión 12, escala 6.
     */
    @NotNull
    @ApiModelProperty(value = "Valor tipo de cambio. Precisión 12, escala 6.", required = true)
    @Column(name = "val_tipo_cambio", precision = 10, scale = 2, nullable = false)
    private BigDecimal valTipoCambio;

    /**
     * Valor deuda impaga entre 90 y 180 días. Precisión 17, escala 2.
     */
    @ApiModelProperty(value = "Valor deuda impaga entre 90 y 180 días. Precisión 17, escala 2.")
    @Column(name = "MTO_SBIF_IMP_90_179", precision = 10, scale = 2)
    private BigDecimal mtoSbifImp90179;

    /**
     * Estado del Periodo proyectado
     */
    @ApiModelProperty(value = "Estado del Periodo proyectado", required = false)
    @Column(name = "COD_ESTADO_PRY", nullable = true)
    private String codEstadoPry;

    /**
     * usuario que edita la proyeccion
     */
    @ApiModelProperty(value = "usuario que edita la proyeccion", required = false)
    @Column(name = "USER_EDIT", nullable = true)
    private String userEdit;

    /**
     * usuario que cierra la proyeccion
     */
    @ApiModelProperty(value = "usuario que cierra la proyeccion", required = false)
    @Column(name = "USER_CLOSE_PRY", nullable = true)
    private String userClosePry;

    /**
     * Fecha que el usuario edita la proyeccion
     */
    @ApiModelProperty(value = "Fecha que el usuario edita la proyeccion")
    @Column(name = "FEC_USER_EDIT_PRY")
    private LocalDate fecUserEditPry;

    /**
     * Fecha que el usuario cierra la proyeccion
     */
    @ApiModelProperty(value = "Fecha que el usuario cierra la proyeccion")
    @Column(name = "FEC_USER_CLOSE_PRY")
    private LocalDate fecUserClosePry;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public Periodo codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public Periodo codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public Periodo codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public Periodo codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public LocalDate getFecPeriodo() {
        return fecPeriodo;
    }

    public Periodo fecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
        return this;
    }

    public void setFecPeriodo(LocalDate fecPeriodo) {
        this.fecPeriodo = fecPeriodo;
    }

    public Integer getNumMeses() {
        return numMeses;
    }

    public Periodo numMeses(Integer numMeses) {
        this.numMeses = numMeses;
        return this;
    }

    public void setNumMeses(Integer numMeses) {
        this.numMeses = numMeses;
    }

    public BigDecimal getValDeflacPrd() {
        return valDeflacPrd;
    }

    public Periodo valDeflacPrd(BigDecimal valDeflacPrd) {
        this.valDeflacPrd = valDeflacPrd;
        return this;
    }

    public void setValDeflacPrd(BigDecimal valDeflacPrd) {
        this.valDeflacPrd = valDeflacPrd;
    }

    public String getCodAuditor() {
        return codAuditor;
    }

    public Periodo codAuditor(String codAuditor) {
        this.codAuditor = codAuditor;
        return this;
    }

    public void setCodAuditor(String codAuditor) {
        this.codAuditor = codAuditor;
    }

    public String getGlsOtrosAudit() {
        return glsOtrosAudit;
    }

    public Periodo glsOtrosAudit(String glsOtrosAudit) {
        this.glsOtrosAudit = glsOtrosAudit;
        return this;
    }

    public void setGlsOtrosAudit(String glsOtrosAudit) {
        this.glsOtrosAudit = glsOtrosAudit;
    }

    public Integer getCodEstadoPrd() {
        return codEstadoPrd;
    }

    public Periodo codEstadoPrd(Integer codEstadoPrd) {
        this.codEstadoPrd = codEstadoPrd;
        return this;
    }

    public void setCodEstadoPrd(Integer codEstadoPrd) {
        this.codEstadoPrd = codEstadoPrd;
    }

    public Integer getCodEstadoAudi() {
        return codEstadoAudi;
    }

    public Periodo codEstadoAudi(Integer codEstadoAudi) {
        this.codEstadoAudi = codEstadoAudi;
        return this;
    }

    public void setCodEstadoAudi(Integer codEstadoAudi) {
        this.codEstadoAudi = codEstadoAudi;
    }

    public BigDecimal getMtoSbifDdVig() {
        return mtoSbifDdVig;
    }

    public Periodo mtoSbifDdVig(BigDecimal mtoSbifDdVig) {
        this.mtoSbifDdVig = mtoSbifDdVig;
        return this;
    }

    public void setMtoSbifDdVig(BigDecimal mtoSbifDdVig) {
        this.mtoSbifDdVig = mtoSbifDdVig;
    }

    public BigDecimal getMtoSbifDdMor() {
        return mtoSbifDdMor;
    }

    public Periodo mtoSbifDdMor(BigDecimal mtoSbifDdMor) {
        this.mtoSbifDdMor = mtoSbifDdMor;
        return this;
    }

    public void setMtoSbifDdMor(BigDecimal mtoSbifDdMor) {
        this.mtoSbifDdMor = mtoSbifDdMor;
    }

    public BigDecimal getMtoSbifDdVen() {
        return mtoSbifDdVen;
    }

    public Periodo mtoSbifDdVen(BigDecimal mtoSbifDdVen) {
        this.mtoSbifDdVen = mtoSbifDdVen;
        return this;
    }

    public void setMtoSbifDdVen(BigDecimal mtoSbifDdVen) {
        this.mtoSbifDdVen = mtoSbifDdVen;
    }

    public BigDecimal getMtoSbifDdCas() {
        return mtoSbifDdCas;
    }

    public Periodo mtoSbifDdCas(BigDecimal mtoSbifDdCas) {
        this.mtoSbifDdCas = mtoSbifDdCas;
        return this;
    }

    public void setMtoSbifDdCas(BigDecimal mtoSbifDdCas) {
        this.mtoSbifDdCas = mtoSbifDdCas;
    }

    public BigDecimal getMtoSbifDiVig() {
        return mtoSbifDiVig;
    }

    public Periodo mtoSbifDiVig(BigDecimal mtoSbifDiVig) {
        this.mtoSbifDiVig = mtoSbifDiVig;
        return this;
    }

    public void setMtoSbifDiVig(BigDecimal mtoSbifDiVig) {
        this.mtoSbifDiVig = mtoSbifDiVig;
    }

    public BigDecimal getMtoSbifDiVen() {
        return mtoSbifDiVen;
    }

    public Periodo mtoSbifDiVen(BigDecimal mtoSbifDiVen) {
        this.mtoSbifDiVen = mtoSbifDiVen;
        return this;
    }

    public void setMtoSbifDiVen(BigDecimal mtoSbifDiVen) {
        this.mtoSbifDiVen = mtoSbifDiVen;
    }

    public BigDecimal getMtoSbifDiCas() {
        return mtoSbifDiCas;
    }

    public Periodo mtoSbifDiCas(BigDecimal mtoSbifDiCas) {
        this.mtoSbifDiCas = mtoSbifDiCas;
        return this;
    }

    public void setMtoSbifDiCas(BigDecimal mtoSbifDiCas) {
        this.mtoSbifDiCas = mtoSbifDiCas;
    }

    public String getNomUserCrea() {
        return nomUserCrea;
    }

    public Periodo nomUserCrea(String nomUserCrea) {
        this.nomUserCrea = nomUserCrea;
        return this;
    }

    public void setNomUserCrea(String nomUserCrea) {
        this.nomUserCrea = nomUserCrea;
    }

    public LocalDate getFecUserCrea() {
        return fecUserCrea;
    }

    public Periodo fecUserCrea(LocalDate fecUserCrea) {
        this.fecUserCrea = fecUserCrea;
        return this;
    }

    public void setFecUserCrea(LocalDate fecUserCrea) {
        this.fecUserCrea = fecUserCrea;
    }

    public String getNomUserModif() {
        return nomUserModif;
    }

    public Periodo nomUserModif(String nomUserModif) {
        this.nomUserModif = nomUserModif;
        return this;
    }

    public void setNomUserModif(String nomUserModif) {
        this.nomUserModif = nomUserModif;
    }

    public LocalDate getFecUserModif() {
        return fecUserModif;
    }

    public Periodo fecUserModif(LocalDate fecUserModif) {
        this.fecUserModif = fecUserModif;
        return this;
    }

    public void setFecUserModif(LocalDate fecUserModif) {
        this.fecUserModif = fecUserModif;
    }

    public LocalDate getFecInformSbif() {
        return fecInformSbif;
    }

    public Periodo fecInformSbif(LocalDate fecInformSbif) {
        this.fecInformSbif = fecInformSbif;
        return this;
    }

    public void setFecInformSbif(LocalDate fecInformSbif) {
        this.fecInformSbif = fecInformSbif;
    }

    public BigDecimal getValTipoCambio() {
        return valTipoCambio;
    }

    public Periodo valTipoCambio(BigDecimal valTipoCambio) {
        this.valTipoCambio = valTipoCambio;
        return this;
    }

    public void setValTipoCambio(BigDecimal valTipoCambio) {
        this.valTipoCambio = valTipoCambio;
    }

    public BigDecimal getMtoSbifImp90179() {
        return mtoSbifImp90179;
    }

    public Periodo mtoSbifImp90179(BigDecimal mtoSbifImp90179) {
        this.mtoSbifImp90179 = mtoSbifImp90179;
        return this;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Periodo))
            return false;
        Periodo periodo = (Periodo) o;
        return codCliente.equals(periodo.codCliente) && codTipoDes.equals(periodo.codTipoDes) && codSubtipoDes.equals(periodo.codSubtipoDes) && codMoneda.equals(periodo.codMoneda)
            && codPlantilla.equals(periodo.codPlantilla) && fecPeriodo.equals(periodo.fecPeriodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla, fecPeriodo);
    }

    @Override
    public String toString() {
        return "Periodo{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes + ", codMoneda='" + codMoneda + '\''
            + ", codPlantilla=" + codPlantilla + ", fecPeriodo=" + fecPeriodo + ", numMeses=" + numMeses + ", valDeflacPrd=" + valDeflacPrd + ", codAuditor='" + codAuditor + '\''
            + ", glsOtrosAudit='" + glsOtrosAudit + '\'' + ", codEstadoPrd=" + codEstadoPrd + ", codEstadoAudi=" + codEstadoAudi + ", mtoSbifDdVig=" + mtoSbifDdVig
            + ", mtoSbifDdMor=" + mtoSbifDdMor + ", mtoSbifDdVen=" + mtoSbifDdVen + ", mtoSbifDdCas=" + mtoSbifDdCas + ", mtoSbifDiVig=" + mtoSbifDiVig + ", mtoSbifDiVen="
            + mtoSbifDiVen + ", mtoSbifDiCas=" + mtoSbifDiCas + ", nomUserCrea='" + nomUserCrea + '\'' + ", fecUserCrea=" + fecUserCrea + ", nomUserModif='" + nomUserModif + '\''
            + ", fecUserModif=" + fecUserModif + ", fecInformSbif=" + fecInformSbif + ", valTipoCambio=" + valTipoCambio + ", mtoSbifImp90179=" + mtoSbifImp90179
            + ", codEstadoPry='" + codEstadoPry + '\'' + ", userEdit='" + userEdit + '\'' + ", userClosePry='" + userClosePry + '\'' + ", fecUserEditPry=" + fecUserEditPry
            + ", fecUserClosePry=" + fecUserClosePry + '}';
    }
}
