package cl.tutorial.desglose.service.dto;

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
 * A DTO for the Desglose entity.
 */
public class DesgloseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String codCliente;

    @NotNull
    private String codTipoDes;

    @NotNull
    private String codSubtipoDes;

    @NotNull
    private String codMoneda;

    @NotNull
    private String codPlantilla;

    @NotNull
    private String codUnidMoneda;

    private String glsSubtipoDes;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000")
    private BigDecimal valDeflacBase;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private BigDecimal valIva;

    @NotNull
    private String nomUserAnalis;

    private String glsObservacion;

    @NotNull
    private String nomUserCrea;

    @NotNull
    private ZonedDateTime fecUserCrea;

    private String nomUserModif;

    private ZonedDateTime fecUserModif;

    @NotNull
    private LocalDate fecDeflacBase;

    private String nomUserOpen;

    @NotNull
    private Boolean flgIsTemporal;

    @NotNull
    private String codMonedaImp;

    private LocalDateTime fecHraOpen;

    private String nomUser;

    private String flgValidacion;

    private String flgRevision;

    private String glsComentValid;

    private String codEstado;

    private String glsEstado;

    private String rolEmpleadoDesDef;

    private String nomRevisorDef;

    private ZonedDateTime fecDesgloDef;

    private String rolEmpleadoRevMod;

    private String rolEmpleadoDefEnvPdf;

    private LocalDate fecDesgloEnvPdf;

    private Integer codMonedaRpt;

    private Integer corImpresionRpt;

    private String flgEjeFactoringRpt;

    private LocalDate fecPeriodo1Rpt;

    private LocalDate fecPeriodo2Rpt;

    private LocalDate fecPeriodo3Rpt;

    private LocalDate fecPeriodo4Rpt;

    private LocalDate fecPerConsol1Rpt;

    private LocalDate fecPerConsol2Rpt;

    private LocalDate fecUlt1Rpt;

    private LocalDate fecUlt2Rpt;

    private LocalDate fecUserModifRpt;

    private String nomUserModifRpt;

    private Integer numReporteRpt;

    private LocalDate fecGeneracionRpt;

    private Integer numDevolEjecutor;

    private Long numRolChgEstado;

    private String flgReq2firma;

    private Integer codPlantillaProyeccion;

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getCodTipoDes() {
        return codTipoDes;
    }

    public void setCodTipoDes(String codTipoDes) {
        this.codTipoDes = codTipoDes;
    }

    public String getCodSubtipoDes() {
        return codSubtipoDes;
    }

    public void setCodSubtipoDes(String codSubtipoDes) {
        this.codSubtipoDes = codSubtipoDes;
    }

    public String getCodMoneda() {
        return codMoneda;
    }

    public void setCodMoneda(String codMoneda) {
        this.codMoneda = codMoneda;
    }

    public String getCodUnidMoneda() {
        return codUnidMoneda;
    }

    public void setCodUnidMoneda(String codUnidMoneda) {
        this.codUnidMoneda = codUnidMoneda;
    }

    public String getGlsSubtipoDes() {
        return glsSubtipoDes;
    }

    public void setGlsSubtipoDes(String glsSubtipoDes) {
        this.glsSubtipoDes = glsSubtipoDes;
    }

    public BigDecimal getValDeflacBase() {
        return valDeflacBase;
    }

    public void setValDeflacBase(BigDecimal valDeflacBase) {
        this.valDeflacBase = valDeflacBase;
    }

    public BigDecimal getValIva() {
        return valIva;
    }

    public void setValIva(BigDecimal valIva) {
        this.valIva = valIva;
    }

    public String getNomUserAnalis() {
        return nomUserAnalis;
    }

    public void setNomUserAnalis(String nomUserAnalis) {
        this.nomUserAnalis = nomUserAnalis;
    }

    public String getGlsObservacion() {
        return glsObservacion;
    }

    public void setGlsObservacion(String glsObservacion) {
        this.glsObservacion = glsObservacion;
    }

    public String getNomUserCrea() {
        return nomUserCrea;
    }

    public void setNomUserCrea(String nomUserCrea) {
        this.nomUserCrea = nomUserCrea;
    }

    public ZonedDateTime getFecUserCrea() {
        return fecUserCrea;
    }

    public void setFecUserCrea(ZonedDateTime fecUserCrea) {
        this.fecUserCrea = fecUserCrea;
    }

    public String getNomUserModif() {
        return nomUserModif;
    }

    public void setNomUserModif(String nomUserModif) {
        this.nomUserModif = nomUserModif;
    }

    public ZonedDateTime getFecUserModif() {
        return fecUserModif;
    }

    public void setFecUserModif(ZonedDateTime fecUserModif) {
        this.fecUserModif = fecUserModif;
    }

    public LocalDate getFecDeflacBase() {
        return fecDeflacBase;
    }

    public void setFecDeflacBase(LocalDate fecDeflacBase) {
        this.fecDeflacBase = fecDeflacBase;
    }

    public String getNomUserOpen() {
        return nomUserOpen;
    }

    public void setNomUserOpen(String nomUserOpen) {
        this.nomUserOpen = nomUserOpen;
    }

    public Boolean isFlgIsTemporal() {
        return flgIsTemporal;
    }

    public void setFlgIsTemporal(Boolean flgIsTemporal) {
        this.flgIsTemporal = flgIsTemporal;
    }

    public String getCodMonedaImp() {
        return codMonedaImp;
    }

    public void setCodMonedaImp(String codMonedaImp) {
        this.codMonedaImp = codMonedaImp;
    }

    public LocalDateTime getFecHraOpen() {
        return fecHraOpen;
    }

    public void setFecHraOpen(LocalDateTime fecHraOpen) {
        this.fecHraOpen = fecHraOpen;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getFlgValidacion() {
        return flgValidacion;
    }

    public void setFlgValidacion(String flgValidacion) {
        this.flgValidacion = flgValidacion;
    }

    public String getFlgRevision() {
        return flgRevision;
    }

    public void setFlgRevision(String flgRevision) {
        this.flgRevision = flgRevision;
    }

    public String getGlsComentValid() {
        return glsComentValid;
    }

    public void setGlsComentValid(String glsComentValid) {
        this.glsComentValid = glsComentValid;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getRolEmpleadoDesDef() {
        return rolEmpleadoDesDef;
    }

    public void setRolEmpleadoDesDef(String rolEmpleadoDesDef) {
        this.rolEmpleadoDesDef = rolEmpleadoDesDef;
    }

    public String getNomRevisorDef() {
        return nomRevisorDef;
    }

    public void setNomRevisorDef(String nomRevisorDef) {
        this.nomRevisorDef = nomRevisorDef;
    }

    public ZonedDateTime getFecDesgloDef() {
        return fecDesgloDef;
    }

    public void setFecDesgloDef(ZonedDateTime fecDesgloDef) {
        this.fecDesgloDef = fecDesgloDef;
    }

    public String getRolEmpleadoRevMod() {
        return rolEmpleadoRevMod;
    }

    public void setRolEmpleadoRevMod(String rolEmpleadoRevMod) {
        this.rolEmpleadoRevMod = rolEmpleadoRevMod;
    }

    public String getRolEmpleadoDefEnvPdf() {
        return rolEmpleadoDefEnvPdf;
    }

    public void setRolEmpleadoDefEnvPdf(String rolEmpleadoDefEnvPdf) {
        this.rolEmpleadoDefEnvPdf = rolEmpleadoDefEnvPdf;
    }

    public LocalDate getFecDesgloEnvPdf() {
        return fecDesgloEnvPdf;
    }

    public void setFecDesgloEnvPdf(LocalDate fecDesgloEnvPdf) {
        this.fecDesgloEnvPdf = fecDesgloEnvPdf;
    }

    public Integer getCodMonedaRpt() {
        return codMonedaRpt;
    }

    public void setCodMonedaRpt(Integer codMonedaRpt) {
        this.codMonedaRpt = codMonedaRpt;
    }

    public Integer getCorImpresionRpt() {
        return corImpresionRpt;
    }

    public void setCorImpresionRpt(Integer corImpresionRpt) {
        this.corImpresionRpt = corImpresionRpt;
    }

    public String getFlgEjeFactoringRpt() {
        return flgEjeFactoringRpt;
    }

    public void setFlgEjeFactoringRpt(String flgEjeFactoringRpt) {
        this.flgEjeFactoringRpt = flgEjeFactoringRpt;
    }

    public LocalDate getFecPeriodo1Rpt() {
        return fecPeriodo1Rpt;
    }

    public void setFecPeriodo1Rpt(LocalDate fecPeriodo1Rpt) {
        this.fecPeriodo1Rpt = fecPeriodo1Rpt;
    }

    public LocalDate getFecPeriodo2Rpt() {
        return fecPeriodo2Rpt;
    }

    public void setFecPeriodo2Rpt(LocalDate fecPeriodo2Rpt) {
        this.fecPeriodo2Rpt = fecPeriodo2Rpt;
    }

    public LocalDate getFecPeriodo3Rpt() {
        return fecPeriodo3Rpt;
    }

    public void setFecPeriodo3Rpt(LocalDate fecPeriodo3Rpt) {
        this.fecPeriodo3Rpt = fecPeriodo3Rpt;
    }

    public LocalDate getFecPeriodo4Rpt() {
        return fecPeriodo4Rpt;
    }

    public void setFecPeriodo4Rpt(LocalDate fecPeriodo4Rpt) {
        this.fecPeriodo4Rpt = fecPeriodo4Rpt;
    }

    public LocalDate getFecPerConsol1Rpt() {
        return fecPerConsol1Rpt;
    }

    public void setFecPerConsol1Rpt(LocalDate fecPerConsol1Rpt) {
        this.fecPerConsol1Rpt = fecPerConsol1Rpt;
    }

    public LocalDate getFecPerConsol2Rpt() {
        return fecPerConsol2Rpt;
    }

    public void setFecPerConsol2Rpt(LocalDate fecPerConsol2Rpt) {
        this.fecPerConsol2Rpt = fecPerConsol2Rpt;
    }

    public LocalDate getFecUlt1Rpt() {
        return fecUlt1Rpt;
    }

    public void setFecUlt1Rpt(LocalDate fecUlt1Rpt) {
        this.fecUlt1Rpt = fecUlt1Rpt;
    }

    public LocalDate getFecUlt2Rpt() {
        return fecUlt2Rpt;
    }

    public void setFecUlt2Rpt(LocalDate fecUlt2Rpt) {
        this.fecUlt2Rpt = fecUlt2Rpt;
    }

    public LocalDate getFecUserModifRpt() {
        return fecUserModifRpt;
    }

    public void setFecUserModifRpt(LocalDate fecUserModifRpt) {
        this.fecUserModifRpt = fecUserModifRpt;
    }

    public String getNomUserModifRpt() {
        return nomUserModifRpt;
    }

    public void setNomUserModifRpt(String nomUserModifRpt) {
        this.nomUserModifRpt = nomUserModifRpt;
    }

    public Integer getNumReporteRpt() {
        return numReporteRpt;
    }

    public void setNumReporteRpt(Integer numReporteRpt) {
        this.numReporteRpt = numReporteRpt;
    }

    public LocalDate getFecGeneracionRpt() {
        return fecGeneracionRpt;
    }

    public void setFecGeneracionRpt(LocalDate fecGeneracionRpt) {
        this.fecGeneracionRpt = fecGeneracionRpt;
    }

    public Integer getNumDevolEjecutor() {
        return numDevolEjecutor;
    }

    public void setNumDevolEjecutor(Integer numDevolEjecutor) {
        this.numDevolEjecutor = numDevolEjecutor;
    }

    public Long getNumRolChgEstado() {
        return numRolChgEstado;
    }

    public void setNumRolChgEstado(Long numRolChgEstado) {
        this.numRolChgEstado = numRolChgEstado;
    }

    public String getFlgReq2firma() {
        return flgReq2firma;
    }

    public void setFlgReq2firma(String flgReq2firma) {
        this.flgReq2firma = flgReq2firma;
    }

    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getGlsEstado() {
        return glsEstado;
    }

    public void setGlsEstado(String glsEstado) {
        this.glsEstado = glsEstado;
    }

    public Boolean getFlgIsTemporal() {
        return flgIsTemporal;
    }

    public Integer getCodPlantillaProyeccion() {
        return codPlantillaProyeccion;
    }

    public void setCodPlantillaProyeccion(Integer codPlantillaProyeccion) {
        this.codPlantillaProyeccion = codPlantillaProyeccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DesgloseDTO))
            return false;
        DesgloseDTO that = (DesgloseDTO) o;
        return Objects.equals(codCliente, that.codCliente) && Objects.equals(codTipoDes, that.codTipoDes) && Objects.equals(codSubtipoDes, that.codSubtipoDes) && Objects
            .equals(codMoneda, that.codMoneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCliente, codTipoDes, codSubtipoDes, codMoneda);
    }

    @Override
    public String toString() {
        return "DesgloseDTO{" + "codCliente='" + codCliente + '\'' + ", codTipoDes='" + codTipoDes + '\'' + ", codSubtipoDes='" + codSubtipoDes + '\'' + ", codMoneda='" + codMoneda
            + '\'' + ", codPlantilla='" + codPlantilla + '\'' + ", codUnidMoneda='" + codUnidMoneda + '\'' + ", glsSubtipoDes='" + glsSubtipoDes + '\'' + ", valDeflacBase="
            + valDeflacBase + ", valIva=" + valIva + ", nomUserAnalis='" + nomUserAnalis + '\'' + ", glsObservacion='" + glsObservacion + '\'' + ", nomUserCrea='" + nomUserCrea
            + '\'' + ", fecUserCrea=" + fecUserCrea + ", nomUserModif='" + nomUserModif + '\'' + ", fecUserModif=" + fecUserModif + ", fecDeflacBase=" + fecDeflacBase
            + ", nomUserOpen='" + nomUserOpen + '\'' + ", flgIsTemporal=" + flgIsTemporal + ", codMonedaImp='" + codMonedaImp + '\'' + ", fecHraOpen=" + fecHraOpen + ", nomUser='"
            + nomUser + '\'' + ", flgValidacion='" + flgValidacion + '\'' + ", flgRevision='" + flgRevision + '\'' + ", glsComentValid='" + glsComentValid + '\'' + ", codEstado='"
            + codEstado + '\'' + ", glsEstado='" + glsEstado + '\'' + ", rolEmpleadoDesDef='" + rolEmpleadoDesDef + '\'' + ", nomRevisorDef='" + nomRevisorDef + '\''
            + ", fecDesgloDef=" + fecDesgloDef + ", rolEmpleadoRevMod='" + rolEmpleadoRevMod + '\'' + ", rolEmpleadoDefEnvPdf='" + rolEmpleadoDefEnvPdf + '\''
            + ", fecDesgloEnvPdf=" + fecDesgloEnvPdf + ", codMonedaRpt=" + codMonedaRpt + ", corImpresionRpt=" + corImpresionRpt + ", flgEjeFactoringRpt='" + flgEjeFactoringRpt
            + '\'' + ", fecPeriodo1Rpt=" + fecPeriodo1Rpt + ", fecPeriodo2Rpt=" + fecPeriodo2Rpt + ", fecPeriodo3Rpt=" + fecPeriodo3Rpt + ", fecPeriodo4Rpt=" + fecPeriodo4Rpt
            + ", fecPerConsol1Rpt=" + fecPerConsol1Rpt + ", fecPerConsol2Rpt=" + fecPerConsol2Rpt + ", fecUlt1Rpt=" + fecUlt1Rpt + ", fecUlt2Rpt=" + fecUlt2Rpt
            + ", fecUserModifRpt=" + fecUserModifRpt + ", nomUserModifRpt='" + nomUserModifRpt + '\'' + ", numReporteRpt=" + numReporteRpt + ", fecGeneracionRpt="
            + fecGeneracionRpt + ", numDevolEjecutor=" + numDevolEjecutor + ", numRolChgEstado=" + numRolChgEstado + ", flgReq2firma='" + flgReq2firma + '\''
            + ", codPlantillaProyeccion=" + codPlantillaProyeccion + '}';
    }

}
