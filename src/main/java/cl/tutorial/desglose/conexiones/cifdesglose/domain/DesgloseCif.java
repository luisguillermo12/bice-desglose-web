package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.domain.pk.NewCommonPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Representa la tabla \"DESGLO_WK\" que contiene desglose individuales o consolidado para las distintas monedas. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"DESGLO_WK\" que contiene desglose individuales o consolidado para las distintas monedas. Pertenece al esquema CIF_ADMIN. @author Antonio "
        + "Marrero Palomino")
@IdClass(NewCommonPK.class)
@Entity
@Table(name = "DESGLO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DesgloseCif implements Serializable {

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
     * Código del tipo de planilla. Por defecto 0 que es tipo banco.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código del tipo de planilla. Por defecto 0 que es tipo banco.", required = true)
    @Column(name = "cod_plantilla", nullable = false)
    private Integer codPlantilla;

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
     * Unidad monetaria.
     */
    @NotNull
    @ApiModelProperty(value = "Unidad monetaria.", required = true)
    @Column(name = "cod_unid_moneda", nullable = false)
    private String codUnidMoneda;

    /**
     * Descripción para Subtipo de desglose.
     */
    @ApiModelProperty(value = "Descripción para Subtipo de desglose.")
    @Column(name = "gls_subtipo_des")
    private String glsSubtipoDes;

    /**
     * Valor deflactor base. Precisión 6, escala 3.
     */
    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000")
    @ApiModelProperty(value = "Valor deflactor base. Precisión 6, escala 3.", required = true)
    @Column(name = "val_deflac_base", precision = 10, scale = 2, nullable = false)
    private BigDecimal valDeflacBase;

    /**
     * Valor IVA. Precisión 4, escala 3.
     */
    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @ApiModelProperty(value = "Valor IVA. Precisión 4, escala 3.", required = true)
    @Column(name = "val_iva", precision = 10, scale = 2, nullable = false)
    private BigDecimal valIva;

    /**
     * Login de último usuario que guarda desglose.
     */
    @NotNull
    @ApiModelProperty(value = "Login de último usuario que guarda desglose.", required = true)
    @Column(name = "nom_user_analis", nullable = false)
    private String nomUserAnalis;

    /**
     * Observación para el Desglose ingresada en configuración de desglose.
     */
    @ApiModelProperty(value = "Observación para el Desglose ingresada en configuración de desglose.")
    @Column(name = "gls_observacion")
    private String glsObservacion;

    /**
     * Login de usuario que crea desglose.
     */
    @NotNull
    @ApiModelProperty(value = "Login de usuario que crea desglose.", required = true)
    @Column(name = "nom_user_crea", nullable = false)
    private String nomUserCrea;

    /**
     * Fecha de creación de desglose.
     */
    @NotNull
    @ApiModelProperty(value = "Fecha de creación de desglose.", required = true)
    @Column(name = "fec_user_crea", nullable = false)
    private ZonedDateTime fecUserCrea;

    /**
     * Login de usuario que modifica desglose.
     */
    @ApiModelProperty(value = "Login de usuario que modifica desglose.")
    @Column(name = "nom_user_modif")
    private String nomUserModif;

    /**
     * Fecha de modificación de desglose.
     */
    @ApiModelProperty(value = "Fecha de modificación de desglose.")
    @Column(name = "fec_user_modif")
    private ZonedDateTime fecUserModif;

    /**
     * Fecha asociada al deflactor base.
     */
    @NotNull
    @ApiModelProperty(value = "Fecha asociada al deflactor base.", required = true)
    @Column(name = "fec_deflac_base", nullable = false)
    private LocalDate fecDeflacBase;

    /**
     * Login de usuario que tiene abierto o cargado el desglose.
     */
    @ApiModelProperty(value = "Login de usuario que tiene abierto o cargado el desglose.")
    @Column(name = "nom_user_open")
    private String nomUserOpen;

    /**
     * Flag que indica si el desglose es temporal o no.
     */
    @NotNull
    @ApiModelProperty(value = "Flag que indica si el desglose es temporal o no.", required = true)
    @Column(name = "flg_is_temporal", nullable = false)
    private Boolean flgIsTemporal;

    /**
     * Código de moneda de impresión.
     */
    @NotNull
    @ApiModelProperty(value = "Código de moneda de impresión.", required = true)
    @Column(name = "cod_moneda_imp", nullable = false)
    private String codMonedaImp;

    /**
     * Fecha y hora desde que está abierto o cargado el desglose.
     */
    @ApiModelProperty(value = "Fecha y hora desde que está abierto o cargado el desglose.")
    @Column(name = "fec_hra_open")
    private LocalDateTime fecHraOpen;

    /**
     * Nombre del usuario que tiene abierto o cargado el desglose.
     */
    @ApiModelProperty(value = "Nombre del usuario que tiene abierto o cargado el desglose.")
    @Column(name = "nom_user")
    private String nomUser;

    /**
     * Flag que señala resultado de validación. OK o NOK.
     */
    @ApiModelProperty(value = "Flag que señala resultado de validación. OK o NOK.")
    @Column(name = "flg_validacion")
    private String flgValidacion;

    /**
     * Flag que señala condicion de revision. AREV o ENREV.
     */
    @ApiModelProperty(value = "Flag que señala condicion de revision. AREV o ENREV.")
    @Column(name = "flg_revision")
    private String flgRevision;

    /**
     * Comentario producto de la validación.
     */
    @ApiModelProperty(value = "Comentario producto de la validación.")
    @Column(name = "gls_coment_valid")
    private String glsComentValid;

    /**
     * Estado del desglose.
     */
    @ApiModelProperty(value = "Estado del desglose.")
    @Column(name = "cod_estado")
    private String codEstado;

    /**
     * Rol empleado que lleva desglose a estado definitivo.
     */
    @ApiModelProperty(value = "Rol empleado que lleva desglose a estado definitivo.")
    @Column(name = "rol_empleado_des_def")
    private String rolEmpleadoDesDef;

    /**
     * Nombre de revisor que lleva desglose a estado definitivo.
     */
    @ApiModelProperty(value = "Nombre de revisor que lleva desglose a estado definitivo.")
    @Column(name = "nom_revisor_def")
    private String nomRevisorDef;

    /**
     * Fecha en que desglose se lleva a estado definitivo.
     */
    @ApiModelProperty(value = "Fecha en que desglose se lleva a estado definitivo.")
    @Column(name = "fec_desglo_def")
    private ZonedDateTime fecDesgloDef;

    /**
     * Rol empleado de revisor que revisó y modificó desglose.
     */
    @ApiModelProperty(value = "Rol empleado de revisor que revisó y modificó desglose.")
    @Column(name = "rol_empleado_rev_mod")
    private String rolEmpleadoRevMod;

    /**
     * Rol empleado que genera PDF para desglose.
     */
    @ApiModelProperty(value = "Rol empleado que genera PDF para desglose.")
    @Column(name = "rol_empleado_def_env_pdf")
    private String rolEmpleadoDefEnvPdf;

    /**
     * Fecha de envío del PDF.
     */
    @ApiModelProperty(value = "Fecha de envío del PDF.")
    @Column(name = "fec_desglo_env_pdf")
    private LocalDate fecDesgloEnvPdf;

    /**
     * Código moneda impresión (se usa como parámetro).
     */
    @ApiModelProperty(value = "Código moneda impresión (se usa como parámetro).")
    @Column(name = "cod_moneda_rpt")
    private Integer codMonedaRpt;

    /**
     * Correlativo de impresión (se usa como parámetro).
     */
    @ApiModelProperty(value = "Correlativo de impresión (se usa como parámetro).")
    @Column(name = "cor_impresion_rpt")
    private Integer corImpresionRpt;

    /**
     * Flag que señala si es ejecutivo de factoring o no (se usa como parámetro).
     */
    @ApiModelProperty(value = "Flag que señala si es ejecutivo de factoring o no (se usa como parámetro).")
    @Column(name = "flg_eje_factoring_rpt")
    private String flgEjeFactoringRpt;

    /**
     * Fecha período 1 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha período 1 (se usa como parámetro).")
    @Column(name = "fec_periodo1_rpt")
    private LocalDate fecPeriodo1Rpt;

    /**
     * Fecha período 2 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha período 2 (se usa como parámetro).")
    @Column(name = "fec_periodo2_rpt")
    private LocalDate fecPeriodo2Rpt;

    /**
     * Fecha período 3 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha período 3 (se usa como parámetro).")
    @Column(name = "fec_periodo3_rpt")
    private LocalDate fecPeriodo3Rpt;

    /**
     * Fecha período 4 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha período 4 (se usa como parámetro).")
    @Column(name = "fec_periodo4_rpt")
    private LocalDate fecPeriodo4Rpt;

    /**
     * Fecha período Consolidado 1 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha período Consolidado 1 (se usa como parámetro).")
    @Column(name = "fec_per_consol1_rpt")
    private LocalDate fecPerConsol1Rpt;

    /**
     * Fecha período consolidado 2 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha período consolidado 2 (se usa como parámetro).")
    @Column(name = "fec_per_consol2_rpt")
    private LocalDate fecPerConsol2Rpt;

    /**
     * Fecha último período 1 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha último período 1 (se usa como parámetro).")
    @Column(name = "fec_ult1_rpt")
    private LocalDate fecUlt1Rpt;

    /**
     * Fecha último período 2 (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha último período 2 (se usa como parámetro).")
    @Column(name = "fec_ult2_rpt")
    private LocalDate fecUlt2Rpt;

    /**
     * Fecha de aprobación desglose (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha de aprobación desglose (se usa como parámetro).")
    @Column(name = "fec_user_modif_rpt")
    private LocalDate fecUserModifRpt;

    /**
     * Nombre aprobador de desglose (se usa como parámetro).
     */
    @ApiModelProperty(value = "Nombre aprobador de desglose (se usa como parámetro).")
    @Column(name = "nom_user_modif_rpt")
    private String nomUserModifRpt;

    /**
     * Número de plantilla de reporte (se usa como parámetro).
     */
    @ApiModelProperty(value = "Número de plantilla de reporte (se usa como parámetro).")
    @Column(name = "num_reporte_rpt")
    private Integer numReporteRpt;

    /**
     * Fecha generación del reporte (se usa como parámetro).
     */
    @ApiModelProperty(value = "Fecha generación del reporte (se usa como parámetro).")
    @Column(name = "fec_generacion_rpt")
    private LocalDate fecGeneracionRpt;

    /**
     * Contador que señala las veces en que desglose es enviado a revisión.
     */
    @ApiModelProperty(value = "Contador que señala las veces en que desglose es enviado a revisión.")
    @Column(name = "num_devol_ejecutor")
    private Integer numDevolEjecutor;

    /**
     * Rol empleado que produce cambio de estado a desglose.
     */
    @ApiModelProperty(value = "Rol empleado que produce cambio de estado a desglose.")
    @Column(name = "num_rol_chg_estado")
    private String numRolChgEstado;

    /**
     * Flag que señala si falta segunda firma o no.
     */
    @ApiModelProperty(value = "Flag que señala si falta segunda firma o no.")
    @Column(name = "flg_req_2firma")
    private String flgReq2firma;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodCliente() {
        return codCliente;
    }

    public DesgloseCif codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoDes() {
        return codTipoDes;
    }

    public DesgloseCif codTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
        return this;
    }

    public void setCodTipoDes(Integer codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public Integer getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public DesgloseCif codSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
        return this;
    }

    public void setCodSubtipoDes(Integer codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public DesgloseCif codMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
        return this;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getCodUnidMoneda() {
        return codUnidMoneda;
    }

    public DesgloseCif codUnidMoneda(String codUnidMoneda) {
        this.codUnidMoneda = codUnidMoneda;
        return this;
    }

    public void setCodUnidMoneda(String codUnidMoneda) {
        this.codUnidMoneda = codUnidMoneda;
    }

    public String getGlsSubtipoDes() {
        return glsSubtipoDes;
    }

    public DesgloseCif glsSubtipoDes(String glsSubtipoDes) {
        this.glsSubtipoDes = glsSubtipoDes;
        return this;
    }

    public void setGlsSubtipoDes(String glsSubtipoDes) {
        this.glsSubtipoDes = glsSubtipoDes;
    }

    public BigDecimal getValDeflacBase() {
        return valDeflacBase;
    }

    public DesgloseCif valDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
        return this;
    }

    public void setValDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
    }

    public BigDecimal getValIva() {
        return valIva;
    }

    public DesgloseCif valIva(BigDecimal valIva) {
        this.valIva = valIva;
        return this;
    }

    public void setValIva(BigDecimal valIva) {
        this.valIva = valIva;
    }

    public String getNomUserAnalis() {
        return nomUserAnalis;
    }

    public DesgloseCif nomUserAnalis(String nomUserAnalis) {
        this.nomUserAnalis = nomUserAnalis;
        return this;
    }

    public void setNomUserAnalis(String nomUserAnalis) {
        this.nomUserAnalis = nomUserAnalis;
    }

    public String getGlsObservacion() {
        return glsObservacion;
    }

    public DesgloseCif glsObservacion(String glsObservacion) {
        this.glsObservacion = glsObservacion;
        return this;
    }

    public void setGlsObservacion(String glsObservacion) {
        this.glsObservacion = glsObservacion;
    }

    public String getNomUserCrea() {
        return nomUserCrea;
    }

    public DesgloseCif nomUserCrea(String nomUserCrea) {
        this.nomUserCrea = nomUserCrea;
        return this;
    }

    public void setNomUserCrea(String nomUserCrea) {
        this.nomUserCrea = nomUserCrea;
    }

    public ZonedDateTime getFecUserCrea() {
        return fecUserCrea;
    }

    public DesgloseCif fecUserCrea(ZonedDateTime fecUserCrea) {
        this.fecUserCrea = fecUserCrea;
        return this;
    }

    public void setFecUserCrea(ZonedDateTime fecUserCrea) {
        this.fecUserCrea = fecUserCrea;
    }

    public String getNomUserModif() {
        return nomUserModif;
    }

    public DesgloseCif nomUserModif(String nomUserModif) {
        this.nomUserModif = nomUserModif;
        return this;
    }

    public void setNomUserModif(String nomUserModif) {
        this.nomUserModif = nomUserModif;
    }

    public ZonedDateTime getFecUserModif() {
        return fecUserModif;
    }

    public DesgloseCif fecUserModif(ZonedDateTime fecUserModif) {
        this.fecUserModif = fecUserModif;
        return this;
    }

    public void setFecUserModif(ZonedDateTime fecUserModif) {
        this.fecUserModif = fecUserModif;
    }

    public LocalDate getFecDeflacBase() {
        return fecDeflacBase;
    }

    public DesgloseCif fecDeflacBase(LocalDate fecDeflacBase) {
        this.fecDeflacBase = fecDeflacBase;
        return this;
    }

    public void setFecDeflacBase(LocalDate fecDeflacBase) {
        this.fecDeflacBase = fecDeflacBase;
    }

    public String getNomUserOpen() {
        return nomUserOpen;
    }

    public DesgloseCif nomUserOpen(String nomUserOpen) {
        this.nomUserOpen = nomUserOpen;
        return this;
    }

    public void setNomUserOpen(String nomUserOpen) {
        this.nomUserOpen = nomUserOpen;
    }

    public Boolean isFlgIsTemporal() {
        return flgIsTemporal;
    }

    public DesgloseCif flgIsTemporal(Boolean flgIsTemporal) {
        this.flgIsTemporal = flgIsTemporal;
        return this;
    }

    public void setFlgIsTemporal(Boolean flgIsTemporal) {
        this.flgIsTemporal = flgIsTemporal;
    }

    public String getCodMonedaImp() {
        return codMonedaImp;
    }

    public DesgloseCif codMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
        return this;
    }

    public void setCodMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
    }

    public LocalDateTime getFecHraOpen() {
        return fecHraOpen;
    }

    public DesgloseCif fecHraOpen(LocalDateTime fecHraOpen) {
        this.fecHraOpen = fecHraOpen;
        return this;
    }

    public void setFecHraOpen(LocalDateTime fecHraOpen) {
        this.fecHraOpen = fecHraOpen;
    }

    public String getNomUser() {
        return nomUser;
    }

    public DesgloseCif nomUser(String nomUser) {
        this.nomUser = nomUser;
        return this;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getFlgValidacion() {
        return flgValidacion;
    }

    public DesgloseCif flgValidacion(String flgValidacion) {
        this.flgValidacion = flgValidacion;
        return this;
    }

    public void setFlgValidacion(String flgValidacion) {
        this.flgValidacion = flgValidacion;
    }

    public String getFlgRevision() {
        return flgRevision;
    }

    public DesgloseCif flgRevision(String flgRevision) {
        this.flgRevision = flgRevision;
        return this;
    }

    public void setFlgRevision(String flgRevision) {
        this.flgRevision = flgRevision;
    }

    public String getGlsComentValid() {
        return glsComentValid;
    }

    public DesgloseCif glsComentValid(String glsComentValid) {
        this.glsComentValid = glsComentValid;
        return this;
    }

    public void setGlsComentValid(String glsComentValid) {
        this.glsComentValid = glsComentValid;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public DesgloseCif codEstado(String codEstado) {
        this.codEstado = codEstado;
        return this;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getRolEmpleadoDesDef() {
        return rolEmpleadoDesDef;
    }

    public DesgloseCif rolEmpleadoDesDef(String rolEmpleadoDesDef) {
        this.rolEmpleadoDesDef = rolEmpleadoDesDef;
        return this;
    }

    public void setRolEmpleadoDesDef(String rolEmpleadoDesDef) {
        this.rolEmpleadoDesDef = rolEmpleadoDesDef;
    }

    public String getNomRevisorDef() {
        return nomRevisorDef;
    }

    public DesgloseCif nomRevisorDef(String nomRevisorDef) {
        this.nomRevisorDef = nomRevisorDef;
        return this;
    }

    public void setNomRevisorDef(String nomRevisorDef) {
        this.nomRevisorDef = nomRevisorDef;
    }

    public ZonedDateTime getFecDesgloDef() {
        return fecDesgloDef;
    }

    public DesgloseCif fecDesgloDef(ZonedDateTime fecDesgloDef) {
        this.fecDesgloDef = fecDesgloDef;
        return this;
    }

    public void setFecDesgloDef(ZonedDateTime fecDesgloDef) {
        this.fecDesgloDef = fecDesgloDef;
    }

    public String getRolEmpleadoRevMod() {
        return rolEmpleadoRevMod;
    }

    public DesgloseCif rolEmpleadoRevMod(String rolEmpleadoRevMod) {
        this.rolEmpleadoRevMod = rolEmpleadoRevMod;
        return this;
    }

    public void setRolEmpleadoRevMod(String rolEmpleadoRevMod) {
        this.rolEmpleadoRevMod = rolEmpleadoRevMod;
    }

    public String getRolEmpleadoDefEnvPdf() {
        return rolEmpleadoDefEnvPdf;
    }

    public DesgloseCif rolEmpleadoDefEnvPdf(String rolEmpleadoDefEnvPdf) {
        this.rolEmpleadoDefEnvPdf = rolEmpleadoDefEnvPdf;
        return this;
    }

    public void setRolEmpleadoDefEnvPdf(String rolEmpleadoDefEnvPdf) {
        this.rolEmpleadoDefEnvPdf = rolEmpleadoDefEnvPdf;
    }

    public LocalDate getFecDesgloEnvPdf() {
        return fecDesgloEnvPdf;
    }

    public DesgloseCif fecDesgloEnvPdf(LocalDate fecDesgloEnvPdf) {
        this.fecDesgloEnvPdf = fecDesgloEnvPdf;
        return this;
    }

    public void setFecDesgloEnvPdf(LocalDate fecDesgloEnvPdf) {
        this.fecDesgloEnvPdf = fecDesgloEnvPdf;
    }

    public Integer getCodMonedaRpt() {
        return codMonedaRpt;
    }

    public DesgloseCif codMonedaRpt(Integer codMonedaRpt) {
        this.codMonedaRpt = codMonedaRpt;
        return this;
    }

    public void setCodMonedaRpt(Integer codMonedaRpt) {
        this.codMonedaRpt = codMonedaRpt;
    }

    public Integer getCorImpresionRpt() {
        return corImpresionRpt;
    }

    public DesgloseCif corImpresionRpt(Integer corImpresionRpt) {
        this.corImpresionRpt = corImpresionRpt;
        return this;
    }

    public void setCorImpresionRpt(Integer corImpresionRpt) {
        this.corImpresionRpt = corImpresionRpt;
    }

    public String getFlgEjeFactoringRpt() {
        return flgEjeFactoringRpt;
    }

    public DesgloseCif flgEjeFactoringRpt(String flgEjeFactoringRpt) {
        this.flgEjeFactoringRpt = flgEjeFactoringRpt;
        return this;
    }

    public void setFlgEjeFactoringRpt(String flgEjeFactoringRpt) {
        this.flgEjeFactoringRpt = flgEjeFactoringRpt;
    }

    public LocalDate getFecPeriodo1Rpt() {
        return fecPeriodo1Rpt;
    }

    public DesgloseCif fecPeriodo1Rpt(LocalDate fecPeriodo1Rpt) {
        this.fecPeriodo1Rpt = fecPeriodo1Rpt;
        return this;
    }

    public void setFecPeriodo1Rpt(LocalDate fecPeriodo1Rpt) {
        this.fecPeriodo1Rpt = fecPeriodo1Rpt;
    }

    public LocalDate getFecPeriodo2Rpt() {
        return fecPeriodo2Rpt;
    }

    public DesgloseCif fecPeriodo2Rpt(LocalDate fecPeriodo2Rpt) {
        this.fecPeriodo2Rpt = fecPeriodo2Rpt;
        return this;
    }

    public void setFecPeriodo2Rpt(LocalDate fecPeriodo2Rpt) {
        this.fecPeriodo2Rpt = fecPeriodo2Rpt;
    }

    public LocalDate getFecPeriodo3Rpt() {
        return fecPeriodo3Rpt;
    }

    public DesgloseCif fecPeriodo3Rpt(LocalDate fecPeriodo3Rpt) {
        this.fecPeriodo3Rpt = fecPeriodo3Rpt;
        return this;
    }

    public void setFecPeriodo3Rpt(LocalDate fecPeriodo3Rpt) {
        this.fecPeriodo3Rpt = fecPeriodo3Rpt;
    }

    public LocalDate getFecPeriodo4Rpt() {
        return fecPeriodo4Rpt;
    }

    public DesgloseCif fecPeriodo4Rpt(LocalDate fecPeriodo4Rpt) {
        this.fecPeriodo4Rpt = fecPeriodo4Rpt;
        return this;
    }

    public void setFecPeriodo4Rpt(LocalDate fecPeriodo4Rpt) {
        this.fecPeriodo4Rpt = fecPeriodo4Rpt;
    }

    public LocalDate getFecPerConsol1Rpt() {
        return fecPerConsol1Rpt;
    }

    public DesgloseCif fecPerConsol1Rpt(LocalDate fecPerConsol1Rpt) {
        this.fecPerConsol1Rpt = fecPerConsol1Rpt;
        return this;
    }

    public void setFecPerConsol1Rpt(LocalDate fecPerConsol1Rpt) {
        this.fecPerConsol1Rpt = fecPerConsol1Rpt;
    }

    public LocalDate getFecPerConsol2Rpt() {
        return fecPerConsol2Rpt;
    }

    public DesgloseCif fecPerConsol2Rpt(LocalDate fecPerConsol2Rpt) {
        this.fecPerConsol2Rpt = fecPerConsol2Rpt;
        return this;
    }

    public void setFecPerConsol2Rpt(LocalDate fecPerConsol2Rpt) {
        this.fecPerConsol2Rpt = fecPerConsol2Rpt;
    }

    public LocalDate getFecUlt1Rpt() {
        return fecUlt1Rpt;
    }

    public DesgloseCif fecUlt1Rpt(LocalDate fecUlt1Rpt) {
        this.fecUlt1Rpt = fecUlt1Rpt;
        return this;
    }

    public void setFecUlt1Rpt(LocalDate fecUlt1Rpt) {
        this.fecUlt1Rpt = fecUlt1Rpt;
    }

    public LocalDate getFecUlt2Rpt() {
        return fecUlt2Rpt;
    }

    public DesgloseCif fecUlt2Rpt(LocalDate fecUlt2Rpt) {
        this.fecUlt2Rpt = fecUlt2Rpt;
        return this;
    }

    public void setFecUlt2Rpt(LocalDate fecUlt2Rpt) {
        this.fecUlt2Rpt = fecUlt2Rpt;
    }

    public LocalDate getFecUserModifRpt() {
        return fecUserModifRpt;
    }

    public DesgloseCif fecUserModifRpt(LocalDate fecUserModifRpt) {
        this.fecUserModifRpt = fecUserModifRpt;
        return this;
    }

    public void setFecUserModifRpt(LocalDate fecUserModifRpt) {
        this.fecUserModifRpt = fecUserModifRpt;
    }

    public String getNomUserModifRpt() {
        return nomUserModifRpt;
    }

    public DesgloseCif nomUserModifRpt(String nomUserModifRpt) {
        this.nomUserModifRpt = nomUserModifRpt;
        return this;
    }

    public void setNomUserModifRpt(String nomUserModifRpt) {
        this.nomUserModifRpt = nomUserModifRpt;
    }

    public Integer getNumReporteRpt() {
        return numReporteRpt;
    }

    public DesgloseCif numReporteRpt(Integer numReporteRpt) {
        this.numReporteRpt = numReporteRpt;
        return this;
    }

    public void setNumReporteRpt(Integer numReporteRpt) {
        this.numReporteRpt = numReporteRpt;
    }

    public LocalDate getFecGeneracionRpt() {
        return fecGeneracionRpt;
    }

    public DesgloseCif fecGeneracionRpt(LocalDate fecGeneracionRpt) {
        this.fecGeneracionRpt = fecGeneracionRpt;
        return this;
    }

    public void setFecGeneracionRpt(LocalDate fecGeneracionRpt) {
        this.fecGeneracionRpt = fecGeneracionRpt;
    }

    public Integer getNumDevolEjecutor() {
        return numDevolEjecutor;
    }

    public DesgloseCif numDevolEjecutor(Integer numDevolEjecutor) {
        this.numDevolEjecutor = numDevolEjecutor;
        return this;
    }

    public void setNumDevolEjecutor(Integer numDevolEjecutor) {
        this.numDevolEjecutor = numDevolEjecutor;
    }

    public String getNumRolChgEstado() {
        return numRolChgEstado;
    }

    public DesgloseCif numRolChgEstado(String numRolChgEstado) {
        this.numRolChgEstado = numRolChgEstado;
        return this;
    }

    public void setNumRolChgEstado(String numRolChgEstado) {
        this.numRolChgEstado = numRolChgEstado;
    }

    public String getFlgReq2firma() {
        return flgReq2firma;
    }

    public DesgloseCif flgReq2firma(String flgReq2firma) {
        this.flgReq2firma = flgReq2firma;
        return this;
    }

    public void setFlgReq2firma(String flgReq2firma) {
        this.flgReq2firma = flgReq2firma;
    }

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public DesgloseCif codPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseCif))
            return false;
        DesgloseCif desglose = (DesgloseCif) o;
        return Objects.equals(codCliente, desglose.codCliente) && Objects.equals(codTipoDes, desglose.codTipoDes) && Objects.equals(codPlantilla, desglose.codPlantilla) && Objects
            .equals(codSubtipoDes, desglose.codSubtipoDes) && Objects.equals(codMoneda, desglose.codMoneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codPlantilla, codSubtipoDes, codMoneda);
    }

    @Override
    public String toString() {
        return "Desglose{" + "codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codPlantilla=" + codPlantilla + ", codSubtipoDes=" + codSubtipoDes
            + ", codMoneda='" + codMoneda + '\'' + ", codUnidMoneda='" + codUnidMoneda + '\'' + ", glsSubtipoDes='" + glsSubtipoDes + '\'' + ", valDeflacBase=" + valDeflacBase
            + ", valIva=" + valIva + ", nomUserAnalis='" + nomUserAnalis + '\'' + ", glsObservacion='" + glsObservacion + '\'' + ", nomUserCrea='" + nomUserCrea + '\''
            + ", fecUserCrea=" + fecUserCrea + ", nomUserModif='" + nomUserModif + '\'' + ", fecUserModif=" + fecUserModif + ", fecDeflacBase=" + fecDeflacBase + ", nomUserOpen='"
            + nomUserOpen + '\'' + ", flgIsTemporal=" + flgIsTemporal + ", codMonedaImp='" + codMonedaImp + '\'' + ", fecHraOpen=" + fecHraOpen + ", nomUser='" + nomUser + '\''
            + ", flgValidacion='" + flgValidacion + '\'' + ", flgRevision='" + flgRevision + '\'' + ", glsComentValid='" + glsComentValid + '\'' + ", codEstado='" + codEstado
            + '\'' + ", rolEmpleadoDesDef='" + rolEmpleadoDesDef + '\'' + ", nomRevisorDef='" + nomRevisorDef + '\'' + ", fecDesgloDef=" + fecDesgloDef + ", rolEmpleadoRevMod='"
            + rolEmpleadoRevMod + '\'' + ", rolEmpleadoDefEnvPdf='" + rolEmpleadoDefEnvPdf + '\'' + ", fecDesgloEnvPdf=" + fecDesgloEnvPdf + ", codMonedaRpt=" + codMonedaRpt
            + ", corImpresionRpt=" + corImpresionRpt + ", flgEjeFactoringRpt='" + flgEjeFactoringRpt + '\'' + ", fecPeriodo1Rpt=" + fecPeriodo1Rpt + ", fecPeriodo2Rpt="
            + fecPeriodo2Rpt + ", fecPeriodo3Rpt=" + fecPeriodo3Rpt + ", fecPeriodo4Rpt=" + fecPeriodo4Rpt + ", fecPerConsol1Rpt=" + fecPerConsol1Rpt + ", fecPerConsol2Rpt="
            + fecPerConsol2Rpt + ", fecUlt1Rpt=" + fecUlt1Rpt + ", fecUlt2Rpt=" + fecUlt2Rpt + ", fecUserModifRpt=" + fecUserModifRpt + ", nomUserModifRpt='" + nomUserModifRpt
            + '\'' + ", numReporteRpt=" + numReporteRpt + ", fecGeneracionRpt=" + fecGeneracionRpt + ", numDevolEjecutor=" + numDevolEjecutor + ", numRolChgEstado='"
            + numRolChgEstado + '\'' + ", flgReq2firma='" + flgReq2firma + '\'' + '}';
    }

}
