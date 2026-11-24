package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the ClienteBasico entity.
 */
public class ClienteBasicoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long numCliente;

    private String numRolEmpleado;

    private Integer codTipoJuridi;

    private Integer codSucursal;

    private String codCliente;

    private Integer codTipoCliente;

    private Integer codSectorEconom;

    private LocalDate fecCliente;

    private Integer numCorDomlegal;

    private Integer codActividad;

    private Integer codSector;

    private Integer codCatdeu;

    private Integer codRelaBice;

    private Integer codGrupRela;

    private Integer codEstaDeud;

    private Integer numCarpeta;

    private Integer numCasiInte;

    private String glsSinoSbif;

    private LocalDate fecDeclJurada;

    private LocalDate fecVerifDomlegal;

    @NotNull
    private Integer codRegStatus;

    @NotNull
    private Integer codRegLog;

    private String flgFirmaCobprejud;

    private LocalDate fecFirmaCobprejud;

    private String numTaxId;

    private String codSiiExeimp;

    private String flgSiiContprimeracat;

    private String codTipoAlerta;

    private LocalDate fecAlerta;

    private String flgMandatoInv;

    private Integer codGirosii;

    private String codAlertaFrd;

    public Long getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(Long numCliente) {
        this.numCliente = numCliente;
    }

    public String getNumRolEmpleado() {
        return numRolEmpleado;
    }

    public void setNumRolEmpleado(String numRolEmpleado) {
        this.numRolEmpleado = numRolEmpleado;
    }

    public Integer getCodTipoJuridi() {
        return codTipoJuridi;
    }

    public void setCodTipoJuridi(Integer codTipoJuridi) {
        this.codTipoJuridi = codTipoJuridi;
    }

    public Integer getCodSucursal() {
        return codSucursal;
    }

    public void setCodSucursal(Integer codSucursal) {
        this.codSucursal = codSucursal;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Integer getCodTipoCliente() {
        return codTipoCliente;
    }

    public void setCodTipoCliente(Integer codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }

    public Integer getCodSectorEconom() {
        return codSectorEconom;
    }

    public void setCodSectorEconom(Integer codSectorEconom) {
        this.codSectorEconom = codSectorEconom;
    }

    public LocalDate getFecCliente() {
        return fecCliente;
    }

    public void setFecCliente(LocalDate fecCliente) {
        this.fecCliente = fecCliente;
    }

    public Integer getNumCorDomlegal() {
        return numCorDomlegal;
    }

    public void setNumCorDomlegal(Integer numCorDomlegal) {
        this.numCorDomlegal = numCorDomlegal;
    }

    public Integer getCodActividad() {
        return codActividad;
    }

    public void setCodActividad(Integer codActividad) {
        this.codActividad = codActividad;
    }

    public Integer getCodSector() {
        return codSector;
    }

    public void setCodSector(Integer codSector) {
        this.codSector = codSector;
    }

    public Integer getCodCatdeu() {
        return codCatdeu;
    }

    public void setCodCatdeu(Integer codCatdeu) {
        this.codCatdeu = codCatdeu;
    }

    public Integer getCodRelaBice() {
        return codRelaBice;
    }

    public void setCodRelaBice(Integer codRelaBice) {
        this.codRelaBice = codRelaBice;
    }

    public Integer getCodGrupRela() {
        return codGrupRela;
    }

    public void setCodGrupRela(Integer codGrupRela) {
        this.codGrupRela = codGrupRela;
    }

    public Integer getCodEstaDeud() {
        return codEstaDeud;
    }

    public void setCodEstaDeud(Integer codEstaDeud) {
        this.codEstaDeud = codEstaDeud;
    }

    public Integer getNumCarpeta() {
        return numCarpeta;
    }

    public void setNumCarpeta(Integer numCarpeta) {
        this.numCarpeta = numCarpeta;
    }

    public Integer getNumCasiInte() {
        return numCasiInte;
    }

    public void setNumCasiInte(Integer numCasiInte) {
        this.numCasiInte = numCasiInte;
    }

    public String getGlsSinoSbif() {
        return glsSinoSbif;
    }

    public void setGlsSinoSbif(String glsSinoSbif) {
        this.glsSinoSbif = glsSinoSbif;
    }

    public LocalDate getFecDeclJurada() {
        return fecDeclJurada;
    }

    public void setFecDeclJurada(LocalDate fecDeclJurada) {
        this.fecDeclJurada = fecDeclJurada;
    }

    public LocalDate getFecVerifDomlegal() {
        return fecVerifDomlegal;
    }

    public void setFecVerifDomlegal(LocalDate fecVerifDomlegal) {
        this.fecVerifDomlegal = fecVerifDomlegal;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public void setCodRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
    }

    public String getFlgFirmaCobprejud() {
        return flgFirmaCobprejud;
    }

    public void setFlgFirmaCobprejud(String flgFirmaCobprejud) {
        this.flgFirmaCobprejud = flgFirmaCobprejud;
    }

    public LocalDate getFecFirmaCobprejud() {
        return fecFirmaCobprejud;
    }

    public void setFecFirmaCobprejud(LocalDate fecFirmaCobprejud) {
        this.fecFirmaCobprejud = fecFirmaCobprejud;
    }

    public String getNumTaxId() {
        return numTaxId;
    }

    public void setNumTaxId(String numTaxId) {
        this.numTaxId = numTaxId;
    }

    public String getCodSiiExeimp() {
        return codSiiExeimp;
    }

    public void setCodSiiExeimp(String codSiiExeimp) {
        this.codSiiExeimp = codSiiExeimp;
    }

    public String getFlgSiiContprimeracat() {
        return flgSiiContprimeracat;
    }

    public void setFlgSiiContprimeracat(String flgSiiContprimeracat) {
        this.flgSiiContprimeracat = flgSiiContprimeracat;
    }

    public String getCodTipoAlerta() {
        return codTipoAlerta;
    }

    public void setCodTipoAlerta(String codTipoAlerta) {
        this.codTipoAlerta = codTipoAlerta;
    }

    public LocalDate getFecAlerta() {
        return fecAlerta;
    }

    public void setFecAlerta(LocalDate fecAlerta) {
        this.fecAlerta = fecAlerta;
    }

    public String getFlgMandatoInv() {
        return flgMandatoInv;
    }

    public void setFlgMandatoInv(String flgMandatoInv) {
        this.flgMandatoInv = flgMandatoInv;
    }

    public Integer getCodGirosii() {
        return codGirosii;
    }

    public void setCodGirosii(Integer codGirosii) {
        this.codGirosii = codGirosii;
    }

    public String getCodAlertaFrd() {
        return codAlertaFrd;
    }

    public void setCodAlertaFrd(String codAlertaFrd) {
        this.codAlertaFrd = codAlertaFrd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClienteBasicoDTO))
            return false;
        ClienteBasicoDTO that = (ClienteBasicoDTO) o;
        return Objects.equals(numCliente, that.numCliente) && Objects.equals(codRegLog, that.codRegLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCliente, codRegLog);
    }

    @Override
    public String toString() {
        return "ClienteBasicoDTO{" + "numCliente=" + getNumCliente() + ", numRolEmpleado='" + getNumRolEmpleado() + "'" + ", codTipoJuridi=" + getCodTipoJuridi() + ", codSucursal="
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
