package cl.tutorial.desglose.conexiones.cifdesglose.domain;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa la tabla \"BASCLI\" que contiene información basica de los clientes. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"BASCLI\" que contiene información basica de los clientes. Pertenece al esquema CIF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(CommonClientePK.class)
@Entity
@Table(name = "BASCLI")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClienteBasico implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Número del cliente.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Número del cliente.", required = true)
    @Column(name = "num_cliente", nullable = false)
    private Long numCliente;

    /**
     * Código de modificación del registro.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Código de modificación del registro.", required = true)
    @Column(name = "cod_reg_log", nullable = false)
    private Integer codRegLog;

    /**
     * Rol empleado asociado.
     */
    @ApiModelProperty(value = "Rol empleado asociado.")
    @Column(name = "num_rol_empleado")
    private String numRolEmpleado;

    @Column(name = "cod_tipo_juridi")
    private Integer codTipoJuridi;

    /**
     * Sucursal del cliente.
     */
    @ApiModelProperty(value = "Sucursal del cliente.")
    @Column(name = "cod_sucursal")
    private Integer codSucursal;

    @Column(name = "cod_cliente")
    private String codCliente;

    @Column(name = "cod_tipo_cliente")
    private Integer codTipoCliente;

    @Column(name = "cod_sector_econom")
    private Integer codSectorEconom;

    @Column(name = "fec_cliente")
    private LocalDate fecCliente;

    @Column(name = "num_cor_domlegal")
    private Integer numCorDomlegal;

    /**
     * Código de actividad asociada al cliente.
     */
    @ApiModelProperty(value = "Código de actividad asociada al cliente.")
    @Column(name = "cod_actividad")
    private Integer codActividad;

    @Column(name = "cod_sector")
    private Integer codSector;

    @Column(name = "cod_catdeu")
    private Integer codCatdeu;

    @Column(name = "cod_rela_bice")
    private Integer codRelaBice;

    @Column(name = "cod_grup_rela")
    private Integer codGrupRela;

    @Column(name = "cod_esta_deud")
    private Integer codEstaDeud;

    @Column(name = "num_carpeta")
    private Integer numCarpeta;

    @Column(name = "num_casi_inte")
    private Integer numCasiInte;

    @Column(name = "gls_sino_sbif")
    private String glsSinoSbif;

    @Column(name = "fec_decl_jurada")
    private LocalDate fecDeclJurada;

    @Column(name = "fec_verif_domlegal")
    private LocalDate fecVerifDomlegal;

    /**
     * Código de vigencia del registro.
     */
    @NotNull
    @ApiModelProperty(value = "Código de vigencia del registro.", required = true)
    @Column(name = "cod_reg_status", nullable = false)
    private Integer codRegStatus;

    @Column(name = "flg_firma_cobprejud")
    private String flgFirmaCobprejud;

    @Column(name = "fec_firma_cobprejud")
    private LocalDate fecFirmaCobprejud;

    @Column(name = "num_tax_id")
    private String numTaxId;

    @Column(name = "cod_sii_exeimp")
    private String codSiiExeimp;

    @Column(name = "flg_sii_contprimeracat")
    private String flgSiiContprimeracat;

    @Column(name = "cod_tipo_alerta")
    private String codTipoAlerta;

    @Column(name = "fec_alerta")
    private LocalDate fecAlerta;

    @Column(name = "flg_mandato_inv")
    private String flgMandatoInv;

    /**
     * Giro del cliente según SII.
     */
    @ApiModelProperty(value = "Giro del cliente según SII.")
    @Column(name = "cod_girosii")
    private Integer codGirosii;

    /**
     * Código alerta de fraude.
     */
    @ApiModelProperty(value = "Código alerta de fraude.")
    @Column(name = "cod_alerta_frd")
    private String codAlertaFrd;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getNumCliente() {
        return numCliente;
    }

    public ClienteBasico numCliente(Long numCliente) {
        this.numCliente = numCliente;
        return this;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public ClienteBasico numRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
        return this;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public Integer getCodTipoJuridi() {
        return codTipoJuridi;
    }

    public ClienteBasico codTipoJuridi(Integer codTipoJuridi) {
        this.codTipoJuridi = codTipoJuridi;
        return this;
    }

    public void setCodTipoJuridi(Integer codTipoJuridi) {
        this.codTipoJuridi = codTipoJuridi;
    }

    public Integer getCodSucursal() {
        return codSucursal;
    }

    public ClienteBasico codSucursal(Integer codSucursal) {
        this.codSucursal = codSucursal;
        return this;
    }

    public void setCodSucursal(Integer codSucursal) {
        this.codSucursal = codSucursal;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public ClienteBasico codCliente(String codCliente) {
        this.codCliente = codCliente;
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoCliente() {
        return codTipoCliente;
    }

    public ClienteBasico codTipoCliente(Integer codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
        return this;
    }

    public void setCodTipoCliente(Integer codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }

    public Integer getCodSectorEconom() {
        return codSectorEconom;
    }

    public ClienteBasico codSectorEconom(Integer codSectorEconom) {
        this.codSectorEconom = codSectorEconom;
        return this;
    }

    public void setCodSectorEconom(Integer codSectorEconom) {
        this.codSectorEconom = codSectorEconom;
    }

    public LocalDate getFecCliente() {
        return fecCliente;
    }

    public ClienteBasico fecCliente(LocalDate fecCliente) {
        this.fecCliente = fecCliente;
        return this;
    }

    public void setFecCliente(LocalDate fecCliente) {
        this.fecCliente = fecCliente;
    }

    public Integer getNumCorDomlegal() {
        return numCorDomlegal;
    }

    public ClienteBasico numCorDomlegal(Integer numCorDomlegal) {
        this.numCorDomlegal = numCorDomlegal;
        return this;
    }

    public void setNumCorDomlegal(Integer numCorDomlegal) {
        this.numCorDomlegal = numCorDomlegal;
    }

    public Integer getCodActividad() {
        return codActividad;
    }

    public ClienteBasico codActividad(Integer codActividad) {
        this.codActividad = codActividad;
        return this;
    }

    public void setCodActividad(Integer codActividad) {
        this.codActividad = codActividad;
    }

    public Integer getCodSector() {
        return codSector;
    }

    public ClienteBasico codSector(Integer codSector) {
        this.codSector = codSector;
        return this;
    }

    public void setCodSector(Integer codSector) {
        this.codSector = codSector;
    }

    public Integer getCodCatdeu() {
        return codCatdeu;
    }

    public ClienteBasico codCatdeu(Integer codCatdeu) {
        this.codCatdeu = codCatdeu;
        return this;
    }

    public void setCodCatdeu(Integer codCatdeu) {
        this.codCatdeu = codCatdeu;
    }

    public Integer getCodRelaBice() {
        return codRelaBice;
    }

    public ClienteBasico codRelaBice(Integer codRelaBice) {
        this.codRelaBice = codRelaBice;
        return this;
    }

    public void setCodRelaBice(Integer codRelaBice) {
        this.codRelaBice = codRelaBice;
    }

    public Integer getCodGrupRela() {
        return codGrupRela;
    }

    public ClienteBasico codGrupRela(Integer codGrupRela) {
        this.codGrupRela = codGrupRela;
        return this;
    }

    public void setCodGrupRela(Integer codGrupRela) {
        this.codGrupRela = codGrupRela;
    }

    public Integer getCodEstaDeud() {
        return codEstaDeud;
    }

    public ClienteBasico codEstaDeud(Integer codEstaDeud) {
        this.codEstaDeud = codEstaDeud;
        return this;
    }

    public void setCodEstaDeud(Integer codEstaDeud) {
        this.codEstaDeud = codEstaDeud;
    }

    public Integer getNumCarpeta() {
        return numCarpeta;
    }

    public ClienteBasico numCarpeta(Integer numCarpeta) {
        this.numCarpeta = numCarpeta;
        return this;
    }

    public void setNumCarpeta(Integer numCarpeta) {
        this.numCarpeta = numCarpeta;
    }

    public Integer getNumCasiInte() {
        return numCasiInte;
    }

    public ClienteBasico numCasiInte(Integer numCasiInte) {
        this.numCasiInte = numCasiInte;
        return this;
    }

    public void setNumCasiInte(Integer numCasiInte) {
        this.numCasiInte = numCasiInte;
    }

    public String getGlsSinoSbif() {
        return glsSinoSbif;
    }

    public ClienteBasico glsSinoSbif(String glsSinoSbif) {
        this.glsSinoSbif = glsSinoSbif;
        return this;
    }

    public void setGlsSinoSbif(String glsSinoSbif) {
        this.glsSinoSbif = glsSinoSbif;
    }

    public LocalDate getFecDeclJurada() {
        return fecDeclJurada;
    }

    public ClienteBasico fecDeclJurada(LocalDate fecDeclJurada) {
        this.fecDeclJurada = fecDeclJurada;
        return this;
    }

    public void setFecDeclJurada(LocalDate fecDeclJurada) {
        this.fecDeclJurada = fecDeclJurada;
    }

    public LocalDate getFecVerifDomlegal() {
        return fecVerifDomlegal;
    }

    public ClienteBasico fecVerifDomlegal(LocalDate fecVerifDomlegal) {
        this.fecVerifDomlegal = fecVerifDomlegal;
        return this;
    }

    public void setFecVerifDomlegal(LocalDate fecVerifDomlegal) {
        this.fecVerifDomlegal = fecVerifDomlegal;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public ClienteBasico codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public ClienteBasico codRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
        return this;
    }

    public void setCodRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
    }

    public String getFlgFirmaCobprejud() {
        return flgFirmaCobprejud;
    }

    public ClienteBasico flgFirmaCobprejud(String flgFirmaCobprejud) {
        this.flgFirmaCobprejud = flgFirmaCobprejud;
        return this;
    }

    public void setFlgFirmaCobprejud(String flgFirmaCobprejud) {
        this.flgFirmaCobprejud = flgFirmaCobprejud;
    }

    public LocalDate getFecFirmaCobprejud() {
        return fecFirmaCobprejud;
    }

    public ClienteBasico fecFirmaCobprejud(LocalDate fecFirmaCobprejud) {
        this.fecFirmaCobprejud = fecFirmaCobprejud;
        return this;
    }

    public void setFecFirmaCobprejud(LocalDate fecFirmaCobprejud) {
        this.fecFirmaCobprejud = fecFirmaCobprejud;
    }

    public String getNumTaxId() {
        return numTaxId;
    }

    public ClienteBasico numTaxId(String numTaxId) {
        this.numTaxId = numTaxId;
        return this;
    }

    public void setNumTaxId(String numTaxId) {
        this.numTaxId = numTaxId;
    }

    public String getCodSiiExeimp() {
        return codSiiExeimp;
    }

    public ClienteBasico codSiiExeimp(String codSiiExeimp) {
        this.codSiiExeimp = codSiiExeimp;
        return this;
    }

    public void setCodSiiExeimp(String codSiiExeimp) {
        this.codSiiExeimp = codSiiExeimp;
    }

    public String getFlgSiiContprimeracat() {
        return flgSiiContprimeracat;
    }

    public ClienteBasico flgSiiContprimeracat(String flgSiiContprimeracat) {
        this.flgSiiContprimeracat = flgSiiContprimeracat;
        return this;
    }

    public void setFlgSiiContprimeracat(String flgSiiContprimeracat) {
        this.flgSiiContprimeracat = flgSiiContprimeracat;
    }

    public String getCodTipoAlerta() {
        return codTipoAlerta;
    }

    public ClienteBasico codTipoAlerta(String codTipoAlerta) {
        this.codTipoAlerta = codTipoAlerta;
        return this;
    }

    public void setCodTipoAlerta(String codTipoAlerta) {
        this.codTipoAlerta = codTipoAlerta;
    }

    public LocalDate getFecAlerta() {
        return fecAlerta;
    }

    public ClienteBasico fecAlerta(LocalDate fecAlerta) {
        this.fecAlerta = fecAlerta;
        return this;
    }

    public void setFecAlerta(LocalDate fecAlerta) {
        this.fecAlerta = fecAlerta;
    }

    public String getFlgMandatoInv() {
        return flgMandatoInv;
    }

    public ClienteBasico flgMandatoInv(String flgMandatoInv) {
        this.flgMandatoInv = flgMandatoInv;
        return this;
    }

    public void setFlgMandatoInv(String flgMandatoInv) {
        this.flgMandatoInv = flgMandatoInv;
    }

    public Integer getCodGirosii() {
        return codGirosii;
    }

    public ClienteBasico codGirosii(Integer codGirosii) {
        this.codGirosii = codGirosii;
        return this;
    }

    public void setCodGirosii(Integer codGirosii) {
        this.codGirosii = codGirosii;
    }

    public String getCodAlertaFrd() {
        return codAlertaFrd;
    }

    public ClienteBasico codAlertaFrd(String codAlertaFrd) {
        this.codAlertaFrd = codAlertaFrd;
        return this;
    }

    public void setCodAlertaFrd(String codAlertaFrd) {
        this.codAlertaFrd = codAlertaFrd;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClienteBasico))
            return false;
        ClienteBasico that = (ClienteBasico) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codRegLog);
    }

    @Override
    public String toString() {
        return "ClienteBasico{" + "numCliente=" + getNumCliente() + ", numRolEmpleado='" + getNumRolEmpleado() + "'" + ", codTipoJuridi=" + getCodTipoJuridi() + ", codSucursal="
            + getCodSucursal() + ", codCliente='" + getCodCliente() + "'" + ", codTipoCliente=" + getCodTipoCliente() + ", codSectorEconom=" + getCodSectorEconom()
            + ", fecCliente='" + getFecCliente() + "'" + ", numCorDomlegal=" + getNumCorDomlegal() + ", codActividad=" + getCodActividad() + ", codSector=" + getCodSector()
            + ", codCatdeu=" + getCodCatdeu() + ", codRelaBice=" + getCodRelaBice() + ", codGrupRela=" + getCodGrupRela() + ", codEstaDeud=" + getCodEstaDeud() + ", numCarpeta="
            + getNumCarpeta() + ", numCasiInte=" + getNumCasiInte() + ", glsSinoSbif='" + getGlsSinoSbif() + "'" + ", fecDeclJurada='" + getFecDeclJurada() + "'"
            + ", fecVerifDomlegal='" + getFecVerifDomlegal() + "'" + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + ", flgFirmaCobprejud='"
            + getFlgFirmaCobprejud() + "'" + ", fecFirmaCobprejud='" + getFecFirmaCobprejud() + "'" + ", numTaxId='" + getNumTaxId() + "'" + ", codSiiExeimp='" + getCodSiiExeimp()
            + "'" + ", flgSiiContprimeracat='" + getFlgSiiContprimeracat() + "'" + ", codTipoAlerta='" + getCodTipoAlerta() + "'" + ", fecAlerta='" + getFecAlerta() + "'"
            + ", flgMandatoInv='" + getFlgMandatoInv() + "'" + ", codGirosii=" + getCodGirosii() + ", codAlertaFrd='" + getCodAlertaFrd() + "'" + "}";
    }
}
