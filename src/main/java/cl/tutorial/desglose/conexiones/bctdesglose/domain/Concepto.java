package cl.tutorial.desglose.conexiones.bctdesglose.domain;

import cl.tutorial.desglose.conexiones.bctdesglose.domain.pk.ConceptoPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"CONCEP\" que contiene concepto de desglose. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"CONCEP\" que contiene concepto de desglose. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(ConceptoPK.class)
@Entity
@Table(name = "CONCEP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Concepto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Tipo de concepto. Posibles permitidos ('Balance','EstResult','GenFondos','IndFinan','DeudaSbif').
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de concepto. Posibles permitidos ('Balance','EstResult','GenFondos','IndFinan','DeudaSbif').", required = true)
    @Column(name = "cod_tipo_concep", nullable = false)
    private String codTipoConcep;

    /**
     * Correlativo de concepto.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo de concepto.", required = true)
    @Column(name = "cor_concepto", nullable = false)
    private Integer corConcepto;

    /**
     * Código del tipo de planilla. Por defecto 0 que es tipo banco.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código del tipo de planilla. Por defecto 0 que es tipo banco.", required = true)
    @Column(name = "cod_plantilla", nullable = false)
    private Integer codTipoPlantilla;

    /**
     * Código de cuenta.
     */
    @NotNull
    @ApiModelProperty(value = "Código de cuenta.", required = true)
    @Column(name = "cod_concepto", nullable = false)
    private String codConcepto;

    /**
     * Decripción de concepto o cuenta.
     */
    @NotNull
    @ApiModelProperty(value = "Decripción de concepto o cuenta.", required = true)
    @Column(name = "gls_concepto", nullable = false)
    private String glsConcepto;

    /**
     * Secuencia.
     */
    @NotNull
    @ApiModelProperty(value = "Secuencia.", required = true)
    @Column(name = "num_secuencia", nullable = false)
    private Integer numSecuencia;

    /**
     * Nivel de identación.
     */
    @ApiModelProperty(value = "Nivel de identación.")
    @Column(name = "num_lvl_indent")
    private Integer numLvlIndent;

    /**
     * Tipo registro. Posibles permitidos (TITULO, NORMAL, MULTIREGISTRO).
     */
    @ApiModelProperty(value = "Tipo registro. Posibles permitidos (TITULO, NORMAL, MULTIREGISTRO).")
    @Column(name = "cod_tipo_regis")
    private String codTipoRegis;

    /**
     * Fórmula de evaluación de concepto.
     */
    @ApiModelProperty(value = "Fórmula de evaluación de concepto.")
    @Column(name = "gls_formula_val")
    private String glsFormulaVal;

    /**
     * Fórmula de evaluación de concepto para el sistema web.
     */
    @ApiModelProperty(value = "Fórmula de evaluación de concepto para el sistema web.")
    @Column(name = "gls_formula_val_web")
    private String glsFormulaValWeb;

    /**
     * Fórmula de evaluación totales porcentuales.
     */
    @ApiModelProperty(value = "Fórmula de evaluación totales porcentuales.")
    @Column(name = "gls_formula_prc")
    private String glsFormulaPrc;

    /**
     * Fórmula de evaluación totales porcentuales para el sistema web.
     */
    @ApiModelProperty(value = "Fórmula de evaluación totales porcentuales para el sistema web.")
    @Column(name = "gls_formula_prc_web")
    private String glsFormulaPrcWeb;

    /**
     * Fórmuala conceptual de deflactación.
     */
    @ApiModelProperty(value = "Fórmuala conceptual de deflactación.")
    @Column(name = "gls_formula_def")
    private String glsFormulaDef;

    /**
     * Fórmuala conceptual de deflactación para el sistema web.
     */
    @ApiModelProperty(value = "Fórmuala conceptual de deflactación para el sistema web.")
    @Column(name = "gls_formula_def_web")
    private String glsFormulaDefWeb;

    /**
     * Señala si concepto es de total o no.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si concepto es de total o no.", required = true)
    @Column(name = "flg_is_total", nullable = false)
    private Integer flgIsTotal;

    /**
     * Señala si concepto en desglose puede ser modificado por el usuario.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si concepto en desglose puede ser modificado por el usuario.", required = true)
    @Column(name = "flg_user_modif", nullable = false)
    private Integer flgUserModif;

    /**
     * Señala si concepto en desglose está activo o no.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si concepto en desglose está activo o no.", required = true)
    @Column(name = "flg_con_activo", nullable = false)
    private Integer flgConActivo;

    /**
     * Cantidad de decimales.
     */
    @ApiModelProperty(value = "Cantidad de decimales.")
    @Column(name = "num_decimales")
    private Integer numDecimales;

    /**
     * Señala si concepto se ajusta o no (redondeo).
     */
    @NotNull
    @ApiModelProperty(value = "Señala si concepto se ajusta o no (redondeo).", required = true)
    @Column(name = "flg_hace_ajust", nullable = false)
    private Integer flgHaceAjust;

    /**
     * Señala si se muestra con negritas o no.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si se muestra con negritas o no.", required = true)
    @Column(name = "flg_is_bold", nullable = false)
    private Integer flgIsBold;

    /**
     * Señala si se muestra en mayúscula o no.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si se muestra en mayúscula o no.", required = true)
    @Column(name = "flg_is_ucase", nullable = false)
    private Integer flgIsUcase;

    /**
     * Señala que es adjunto y hereda formato de concepto padre.
     */
    @NotNull
    @ApiModelProperty(value = "Señala que es adjunto y hereda formato de concepto padre.", required = true)
    @Column(name = "flg_is_enclosed", nullable = false)
    private Integer flgIsEnclosed;

    /**
     * Señala si concepto se imprime en reporte o no.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si concepto se imprime en reporte o no.", required = true)
    @Column(name = "flg_is_printed", nullable = false)
    private Integer flgIsPrinted;

    /**
     * Señala cantidad de decimales que se imprimen en reporte.
     */
    @NotNull
    @ApiModelProperty(value = "Señala cantidad de decimales que se imprimen en reporte.", required = true)
    @Column(name = "num_dec_print", nullable = false)
    private Integer numDecPrint;

    /**
     * Señala si concepto puede ser negativo o no.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si concepto puede ser negativo o no.", required = true)
    @Column(name = "flg_nonegativ", nullable = false)
    private Integer flgNonegativ;

    /**
     * Señala si imprime espacio después de concepto.
     */
    @NotNull
    @ApiModelProperty(value = "Señala si imprime espacio después de concepto.", required = true)
    @Column(name = "flg_afterspace", nullable = false)
    private Integer flgAfterspace;

    /**
     * Expresión de validación para el valor del concepto.
     */
    @ApiModelProperty(value = "Expresión de validación para el valor del concepto.")
    @Column(name = "exp_validacion_val")
    private String expValidacionVal;

    /**
     * Expresión de validación para el valor de la fórmula del porciento del concepto.
     */
    @ApiModelProperty(value = "Expresión de validación para el valor de la fórmula del porciento del concepto.")
    @Column(name = "exp_validacion_prc")
    private String expValidacionPrc;

    /**
     * Expresión de validación para el valor de la fórmula del deflactor del concepto.
     */
    @ApiModelProperty(value = "Expresión de validación para el valor de la fórmula del deflactor del concepto.")
    @Column(name = "exp_validacion_def")
    private String expValidacionDef;

    /**
     * Mensaje de validación en caso de error en la validación del valor del concepto.
     */
    @ApiModelProperty(value = "Mensaje de validación en caso de error en la validación del valor del concepto.")
    @Column(name = "msg_err_validacion_val")
    private String msgErrValidacionVal;

    /**
     * Mensaje de validación en caso de error en la validación del valor de la fórmula del porciento del concepto.
     */
    @ApiModelProperty(value = "Mensaje de validación en caso de error en la validación del valor de la fórmula del porciento del concepto.")
    @Column(name = "msg_err_validacion_prc")
    private String msgErrValidacionPrc;

    /**
     * Mensaje de validación en caso de error en la validación del valor de la fórmula del deflactor del concepto.
     */
    @ApiModelProperty(value = "Mensaje de validación en caso de error en la validación del valor de la fórmula del deflactor del concepto.")
    @Column(name = "msg_err_validacion_def")
    private String msgErrValidacionDef;

    /**
     * Unidad para los datos del modelo de proyecciones.
     */
    @ApiModelProperty(value = "Unidad para los datos del modelo de proyecciones.")
    @Column(name = "unidad")
    private String unidad;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getCodTipoConcep() {
        return codTipoConcep;
    }

    public Concepto codTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
        return this;
    }

    public void setCodTipoConcep(String codTipoConcep) {
        this.codTipoConcep = codTipoConcep;
    }

    public Integer getCorConcepto() {
        return corConcepto;
    }

    public Concepto corConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
        return this;
    }

    public void setCorConcepto(Integer corConcepto) {
        this.corConcepto = corConcepto;
    }

    public String getCodConcepto() {
        return codConcepto;
    }

    public Concepto codConcepto(String codConcepto) {
        this.codConcepto = codConcepto;
        return this;
    }

    public Integer getCodTipoPlantilla() {
        return codTipoPlantilla;
    }

    public void setCodTipoPlantilla(Integer codTipoPlantilla) {
        this.codTipoPlantilla = codTipoPlantilla;
    }

    public Concepto codTipoPlantilla(Integer codTipoPlantilla) {
        this.codTipoPlantilla = codTipoPlantilla;
        return this;
    }

    public void setCodConcepto(String codConcepto) {
        this.codConcepto = codConcepto;
    }

    public String getGlsConcepto() {
        return glsConcepto;
    }

    public Concepto glsConcepto(String glsConcepto) {
        this.glsConcepto = glsConcepto;
        return this;
    }

    public void setGlsConcepto(String glsConcepto) {
        this.glsConcepto = glsConcepto;
    }

    public Integer getNumSecuencia() {
        return numSecuencia;
    }

    public Concepto numSecuencia(Integer numSecuencia) {
        this.numSecuencia = numSecuencia;
        return this;
    }

    public void setNumSecuencia(Integer numSecuencia) {
        this.numSecuencia = numSecuencia;
    }

    public Integer getNumLvlIndent() {
        return numLvlIndent;
    }

    public Concepto numLvlIndent(Integer numLvlIndent) {
        this.numLvlIndent = numLvlIndent;
        return this;
    }

    public void setNumLvlIndent(Integer numLvlIndent) {
        this.numLvlIndent = numLvlIndent;
    }

    public String getCodTipoRegis() {
        return codTipoRegis;
    }

    public Concepto codTipoRegis(String codTipoRegis) {
        this.codTipoRegis = codTipoRegis;
        return this;
    }

    public void setCodTipoRegis(String codTipoRegis) {
        this.codTipoRegis = codTipoRegis;
    }

    public String getGlsFormulaVal() {
        return glsFormulaVal;
    }

    public Concepto glsFormulaVal(String glsFormulaVal) {
        this.glsFormulaVal = glsFormulaVal;
        return this;
    }

    public void setGlsFormulaVal(String glsFormulaVal) {
        this.glsFormulaVal = glsFormulaVal;
    }

    public String getGlsFormulaPrc() {
        return glsFormulaPrc;
    }

    public Concepto glsFormulaPrc(String glsFormulaPrc) {
        this.glsFormulaPrc = glsFormulaPrc;
        return this;
    }

    public void setGlsFormulaPrc(String glsFormulaPrc) {
        this.glsFormulaPrc = glsFormulaPrc;
    }

    public String getGlsFormulaDef() {
        return glsFormulaDef;
    }

    public Concepto glsFormulaDef(String glsFormulaDef) {
        this.glsFormulaDef = glsFormulaDef;
        return this;
    }

    public void setGlsFormulaDef(String glsFormulaDef) {
        this.glsFormulaDef = glsFormulaDef;
    }

    public void setFlgIsTotal(Integer flgIsTotal) {
        this.flgIsTotal = flgIsTotal;
    }

    public void setFlgUserModif(Integer flgUserModif) {
        this.flgUserModif = flgUserModif;
    }

    public void setFlgConActivo(Integer flgConActivo) {
        this.flgConActivo = flgConActivo;
    }

    public void setNumDecimales(Integer numDecimales) {
        this.numDecimales = numDecimales;
    }

    public void setFlgHaceAjust(Integer flgHaceAjust) {
        this.flgHaceAjust = flgHaceAjust;
    }

    public void setFlgIsBold(Integer flgIsBold) {
        this.flgIsBold = flgIsBold;
    }

    public void setFlgIsUcase(Integer flgIsUcase) {
        this.flgIsUcase = flgIsUcase;
    }

    public void setFlgIsEnclosed(Integer flgIsEnclosed) {
        this.flgIsEnclosed = flgIsEnclosed;
    }

    public void setFlgIsPrinted(Integer flgIsPrinted) {
        this.flgIsPrinted = flgIsPrinted;
    }

    public void setNumDecPrint(Integer numDecPrint) {
        this.numDecPrint = numDecPrint;
    }

    public void setFlgNonegativ(Integer flgNonegativ) {
        this.flgNonegativ = flgNonegativ;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getFlgIsTotal() {
        return flgIsTotal;
    }

    public Integer getFlgUserModif() {
        return flgUserModif;
    }

    public Integer getFlgConActivo() {
        return flgConActivo;
    }

    public Integer getNumDecimales() {
        return numDecimales;
    }

    public Integer getFlgHaceAjust() {
        return flgHaceAjust;
    }

    public Integer getFlgIsBold() {
        return flgIsBold;
    }

    public Integer getFlgIsUcase() {
        return flgIsUcase;
    }

    public Integer getFlgIsEnclosed() {
        return flgIsEnclosed;
    }

    public Integer getFlgIsPrinted() {
        return flgIsPrinted;
    }

    public Integer getNumDecPrint() {
        return numDecPrint;
    }

    public Integer getFlgNonegativ() {
        return flgNonegativ;
    }

    public Integer getFlgAfterspace() {
        return flgAfterspace;
    }

    public void setFlgAfterspace(Integer flgAfterspace) {
        this.flgAfterspace = flgAfterspace;
    }

    public String getExpValidacionVal() {
        return expValidacionVal;
    }

    public void setExpValidacionVal(String expValidacionVal) {
        this.expValidacionVal = expValidacionVal;
    }

    public String getExpValidacionPrc() {
        return expValidacionPrc;
    }

    public void setExpValidacionPrc(String expValidacionPrc) {
        this.expValidacionPrc = expValidacionPrc;
    }

    public String getExpValidacionDef() {
        return expValidacionDef;
    }

    public void setExpValidacionDef(String expValidacionDef) {
        this.expValidacionDef = expValidacionDef;
    }

    public String getMsgErrValidacionVal() {
        return msgErrValidacionVal;
    }

    public void setMsgErrValidacionVal(String msgErrValidacionVal) {
        this.msgErrValidacionVal = msgErrValidacionVal;
    }

    public String getMsgErrValidacionPrc() {
        return msgErrValidacionPrc;
    }

    public void setMsgErrValidacionPrc(String msgErrValidacionPrc) {
        this.msgErrValidacionPrc = msgErrValidacionPrc;
    }

    public String getMsgErrValidacionDef() {
        return msgErrValidacionDef;
    }

    public void setMsgErrValidacionDef(String msgErrValidacionDef) {
        this.msgErrValidacionDef = msgErrValidacionDef;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getGlsFormulaValWeb() {
        return glsFormulaValWeb;
    }

    public void setGlsFormulaValWeb(String glsFormulaValWeb) {
        this.glsFormulaValWeb = glsFormulaValWeb;
    }

    public String getGlsFormulaPrcWeb() {
        return glsFormulaPrcWeb;
    }

    public void setGlsFormulaPrcWeb(String glsFormulaPrcWeb) {
        this.glsFormulaPrcWeb = glsFormulaPrcWeb;
    }

    public String getGlsFormulaDefWeb() {
        return glsFormulaDefWeb;
    }

    public void setGlsFormulaDefWeb(String glsFormulaDefWeb) {
        this.glsFormulaDefWeb = glsFormulaDefWeb;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Concepto))
            return false;
        Concepto concepto = (Concepto) o;
        return Objects.equals(codTipoConcep, concepto.codTipoConcep) && Objects.equals(corConcepto, concepto.corConcepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTipoConcep, corConcepto);
    }

    @Override
    public String toString() {
        return "Concepto{" + "codTipoConcep='" + codTipoConcep + '\'' + ", corConcepto=" + corConcepto + ", codTipoPlantilla=" + codTipoPlantilla + ", codConcepto='" + codConcepto
            + '\'' + ", glsConcepto='" + glsConcepto + '\'' + ", numSecuencia=" + numSecuencia + ", numLvlIndent=" + numLvlIndent + ", codTipoRegis='" + codTipoRegis + '\''
            + ", glsFormulaVal='" + glsFormulaVal + '\'' + ", glsFormulaValWeb='" + glsFormulaValWeb + '\'' + ", glsFormulaPrc='" + glsFormulaPrc + '\'' + ", glsFormulaPrcWeb='"
            + glsFormulaPrcWeb + '\'' + ", glsFormulaDef='" + glsFormulaDef + '\'' + ", glsFormulaDefWeb='" + glsFormulaDefWeb + '\'' + ", flgIsTotal=" + flgIsTotal
            + ", flgUserModif=" + flgUserModif + ", flgConActivo=" + flgConActivo + ", numDecimales=" + numDecimales + ", flgHaceAjust=" + flgHaceAjust + ", flgIsBold=" + flgIsBold
            + ", flgIsUcase=" + flgIsUcase + ", flgIsEnclosed=" + flgIsEnclosed + ", flgIsPrinted=" + flgIsPrinted + ", numDecPrint=" + numDecPrint + ", flgNonegativ="
            + flgNonegativ + ", flgAfterspace=" + flgAfterspace + ", expValidacionVal='" + expValidacionVal + '\'' + ", expValidacionPrc='" + expValidacionPrc + '\''
            + ", expValidacionDef='" + expValidacionDef + '\'' + ", msgErrValidacionVal='" + msgErrValidacionVal + '\'' + ", msgErrValidacionPrc='" + msgErrValidacionPrc + '\''
            + ", msgErrValidacionDef='" + msgErrValidacionDef + '\'' + ", unidad='" + unidad + '\'' + '}';
    }

}
