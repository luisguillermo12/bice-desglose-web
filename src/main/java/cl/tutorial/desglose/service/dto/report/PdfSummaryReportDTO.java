package cl.tutorial.desglose.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

/**
 * A DTO for the /download/informe-resumen-desglose-balance service.
 */
public class PdfSummaryReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Datos generales del reporte
    private String fileName;

    // Datos de códigos
    private String codCliente;
    private String codTipoPlantilla;
    private String codTipoDes;
    private String codSubtipoDes;
    private String codMoneda;

    // Datos del encabezado
    private String rutCliente;
    private String nombreCliente;
    private String tipoPlantillaDesglose;
    private String tipoDesglose;
    private String subTipoDesglose;
    private String moneda;
    private String sector;
    private String subSector;
    private String actividad;
    private String iva;
    private String montosEn;
    private String actualizadoAl;


    private boolean ImprimirTablaButton;

    // Datos de los períodos.
    private String auditor1;
    private String auditor2;
    private String auditor3;
    private String auditor4;
    private String calificacion1;
    private String calificacion2;
    private String calificacion3;
    private String calificacion4;
    private String fecPerido1;
    private String fecPerido2;
    private String fecPerido3;
    private String fecPerido4;
    private String fecCompletaPerido1;
    private String fecCompletaPerido2;
    private String fecCompletaPerido3;
    private String fecCompletaPerido4;
    private String numMeses1;
    private String numMeses2;
    private String numMeses3;
    private String numMeses4;
    private String fechaNotasPeriodo1;
    private String fechaNotasPeriodo2;
    private String fechaNotasLetraPeriodo1;
    private String fechaNotasLetraPeriodo2;

    /**
     * Datos de la Sbif por períodos
     */
    private String fechaPeriodoSbif1;
    private String fechaPeriodoSbif2;
    private String fechaPeriodoSbif3;
    private String fechaPeriodoSbif4;
    private BigDecimal deudaDirectaVigenteSbif1;
    private BigDecimal deudaDirectaVigenteSbif2;
    private BigDecimal deudaDirectaVigenteSbif3;
    private BigDecimal deudaDirectaVigenteSbif4;
    private BigDecimal deudaIndirectaVigenteSbif1;
    private BigDecimal deudaIndirectaVigenteSbif2;
    private BigDecimal deudaIndirectaVigenteSbif3;
    private BigDecimal deudaIndirectaVigenteSbif4;
    private URL imgLogoBancoBice;


    public URL getImgLogoBancoBice() {
        return imgLogoBancoBice;
    }

    public void setImgLogoBancoBice(URL imgLogoBancoBice) {
        this.imgLogoBancoBice = imgLogoBancoBice;
    }

    /**
     * Datos del balance por períodos
     */
    private List<TableValueDTO> datosBalance;

    /**
     * Datos de estados de resultado por períodos
     */
    private List<TableValueDTO> datosEstadoResultado;

    /**
     * Datos de generaciónd e fondos y flujo de efectivo
     */
    private List<TableValueDTO> datosGenFondFlujEfectivo;

    /**
     * Datos de indicadores financieros
     */
    private List<TableValueDTO> datosIndicadorFinanciero;

    /**
     * Datos de notas del desglose
     */
    private List<NoteValueDTO> datosNotasDesglose;

    /**
     * Datos de las empresas que consolidan
     */
    private List<CompanyValueDTO> datosEmpresasConsolidan;

    /**
     * Datos de las observaciones. Lista de elementos String
     */
    private List<ObservationValueDTO> datosObservaciones;

    // Datos del footer
    // todo: esto no se esta utilizando en el reporte
    private String nombreUsuario;
    private String fechaModificacion;
    private String lastDate;
    private String beforeLastDate;

    // todo: esto si se utiliza.
    private String ejecutor;
    private String ejecutorFechaModificacion;
    private String revisor;
    private String revisorFechaModificacion;

    private String tituloReporte;

    public String getTituloReporte() {
        return tituloReporte;
    }

    public void setTituloReporte(String tituloReporte) {
        this.tituloReporte = tituloReporte;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getCodTipoPlantilla() {
        return codTipoPlantilla;
    }

    public void setCodTipoPlantilla(String codTipoPlantilla) {
        this.codTipoPlantilla = codTipoPlantilla;
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

    public String getRutCliente() {
        return rutCliente;
    }

    public void setRutCliente(String rutCliente) {
        this.rutCliente = rutCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoPlantillaDesglose() {
        return tipoPlantillaDesglose;
    }

    public void setTipoPlantillaDesglose(String tipoPlantillaDesglose) {
        this.tipoPlantillaDesglose = tipoPlantillaDesglose;
    }

    public String getTipoDesglose() {
        return tipoDesglose;
    }

    public void setTipoDesglose(String tipoDesglose) {
        this.tipoDesglose = tipoDesglose;
    }

    public String getSubTipoDesglose() {
        return subTipoDesglose;
    }

    public void setSubTipoDesglose(String subTipoDesglose) {
        this.subTipoDesglose = subTipoDesglose;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSubSector() {
        return subSector;
    }

    public void setSubSector(String subSector) {
        this.subSector = subSector;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getMontosEn() {
        return montosEn;
    }

    public void setMontosEn(String montosEn) {
        this.montosEn = montosEn;
    }

    public String getActualizadoAl() {
        return actualizadoAl;
    }

    public void setActualizadoAl(String actualizadoAl) {
        this.actualizadoAl = actualizadoAl;
    }

    public String getAuditor1() {
        return auditor1;
    }

    public void setAuditor1(String auditor1) {
        this.auditor1 = auditor1;
    }

    public String getAuditor2() {
        return auditor2;
    }

    public void setAuditor2(String auditor2) {
        this.auditor2 = auditor2;
    }

    public String getAuditor3() {
        return auditor3;
    }

    public void setAuditor3(String auditor3) {
        this.auditor3 = auditor3;
    }

    public String getAuditor4() {
        return auditor4;
    }

    public void setAuditor4(String auditor4) {
        this.auditor4 = auditor4;
    }

    public String getCalificacion1() {
        return calificacion1;
    }

    public void setCalificacion1(String calificacion1) {
        this.calificacion1 = calificacion1;
    }

    public String getCalificacion2() {
        return calificacion2;
    }

    public void setCalificacion2(String calificacion2) {
        this.calificacion2 = calificacion2;
    }

    public String getCalificacion3() {
        return calificacion3;
    }

    public void setCalificacion3(String calificacion3) {
        this.calificacion3 = calificacion3;
    }

    public String getCalificacion4() {
        return calificacion4;
    }

    public void setCalificacion4(String calificacion4) {
        this.calificacion4 = calificacion4;
    }

    public String getFecPerido1() {
        return fecPerido1;
    }

    public void setFecPerido1(String fecPerido1) {
        this.fecPerido1 = fecPerido1;
    }

    public String getFecPerido2() {
        return fecPerido2;
    }

    public void setFecPerido2(String fecPerido2) {
        this.fecPerido2 = fecPerido2;
    }

    public String getFecPerido3() {
        return fecPerido3;
    }

    public void setFecPerido3(String fecPerido3) {
        this.fecPerido3 = fecPerido3;
    }

    public String getFecPerido4() {
        return fecPerido4;
    }

    public void setFecPerido4(String fecPerido4) {
        this.fecPerido4 = fecPerido4;
    }

    public String getNumMeses1() {
        return numMeses1;
    }

    public void setNumMeses1(String numMeses1) {
        this.numMeses1 = numMeses1;
    }

    public String getNumMeses2() {
        return numMeses2;
    }

    public void setNumMeses2(String numMeses2) {
        this.numMeses2 = numMeses2;
    }

    public String getNumMeses3() {
        return numMeses3;
    }

    public void setNumMeses3(String numMeses3) {
        this.numMeses3 = numMeses3;
    }

    public String getNumMeses4() {
        return numMeses4;
    }

    public void setNumMeses4(String numMeses4) {
        this.numMeses4 = numMeses4;
    }

    public String getFechaNotasPeriodo1() {
        return fechaNotasPeriodo1;
    }

    public void setFechaNotasPeriodo1(String fechaNotasPeriodo1) {
        this.fechaNotasPeriodo1 = fechaNotasPeriodo1;
    }

    public String getFechaNotasPeriodo2() {
        return fechaNotasPeriodo2;
    }

    public void setFechaNotasPeriodo2(String fechaNotasPeriodo2) {
        this.fechaNotasPeriodo2 = fechaNotasPeriodo2;
    }

    public String getFechaNotasLetraPeriodo1() {
        return fechaNotasLetraPeriodo1;
    }

    public boolean isImprimirTablaButton() {
        return ImprimirTablaButton;
    }

    public void setImprimirTablaButton(boolean imprimirTablaButton) {
        ImprimirTablaButton = imprimirTablaButton;
    }


    public void setFechaNotasLetraPeriodo1(String fechaNotasLetraPeriodo1) {
        this.fechaNotasLetraPeriodo1 = fechaNotasLetraPeriodo1;
    }

    public String getFechaNotasLetraPeriodo2() {
        return fechaNotasLetraPeriodo2;
    }

    public void setFechaNotasLetraPeriodo2(String fechaNotasLetraPeriodo2) {
        this.fechaNotasLetraPeriodo2 = fechaNotasLetraPeriodo2;
    }

    public String getFechaPeriodoSbif1() {
        return fechaPeriodoSbif1;
    }

    public void setFechaPeriodoSbif1(String fechaPeriodoSbif1) {
        this.fechaPeriodoSbif1 = fechaPeriodoSbif1;
    }

    public String getFechaPeriodoSbif2() {
        return fechaPeriodoSbif2;
    }

    public void setFechaPeriodoSbif2(String fechaPeriodoSbif2) {
        this.fechaPeriodoSbif2 = fechaPeriodoSbif2;
    }

    public String getFechaPeriodoSbif3() {
        return fechaPeriodoSbif3;
    }

    public void setFechaPeriodoSbif3(String fechaPeriodoSbif3) {
        this.fechaPeriodoSbif3 = fechaPeriodoSbif3;
    }

    public String getFechaPeriodoSbif4() {
        return fechaPeriodoSbif4;
    }

    public void setFechaPeriodoSbif4(String fechaPeriodoSbif4) {
        this.fechaPeriodoSbif4 = fechaPeriodoSbif4;
    }

    public BigDecimal getDeudaDirectaVigenteSbif1() {
        return deudaDirectaVigenteSbif1;
    }

    public void setDeudaDirectaVigenteSbif1(BigDecimal deudaDirectaVigenteSbif1) {
        this.deudaDirectaVigenteSbif1 = deudaDirectaVigenteSbif1;
    }

    public BigDecimal getDeudaDirectaVigenteSbif2() {
        return deudaDirectaVigenteSbif2;
    }

    public void setDeudaDirectaVigenteSbif2(BigDecimal deudaDirectaVigenteSbif2) {
        this.deudaDirectaVigenteSbif2 = deudaDirectaVigenteSbif2;
    }

    public BigDecimal getDeudaDirectaVigenteSbif3() {
        return deudaDirectaVigenteSbif3;
    }

    public void setDeudaDirectaVigenteSbif3(BigDecimal deudaDirectaVigenteSbif3) {
        this.deudaDirectaVigenteSbif3 = deudaDirectaVigenteSbif3;
    }

    public BigDecimal getDeudaDirectaVigenteSbif4() {
        return deudaDirectaVigenteSbif4;
    }

    public void setDeudaDirectaVigenteSbif4(BigDecimal deudaDirectaVigenteSbif4) {
        this.deudaDirectaVigenteSbif4 = deudaDirectaVigenteSbif4;
    }

    public BigDecimal getDeudaIndirectaVigenteSbif1() {
        return deudaIndirectaVigenteSbif1;
    }

    public void setDeudaIndirectaVigenteSbif1(BigDecimal deudaIndirectaVigenteSbif1) {
        this.deudaIndirectaVigenteSbif1 = deudaIndirectaVigenteSbif1;
    }

    public BigDecimal getDeudaIndirectaVigenteSbif2() {
        return deudaIndirectaVigenteSbif2;
    }

    public void setDeudaIndirectaVigenteSbif2(BigDecimal deudaIndirectaVigenteSbif2) {
        this.deudaIndirectaVigenteSbif2 = deudaIndirectaVigenteSbif2;
    }

    public BigDecimal getDeudaIndirectaVigenteSbif3() {
        return deudaIndirectaVigenteSbif3;
    }

    public void setDeudaIndirectaVigenteSbif3(BigDecimal deudaIndirectaVigenteSbif3) {
        this.deudaIndirectaVigenteSbif3 = deudaIndirectaVigenteSbif3;
    }

    public BigDecimal getDeudaIndirectaVigenteSbif4() {
        return deudaIndirectaVigenteSbif4;
    }

    public void setDeudaIndirectaVigenteSbif4(BigDecimal deudaIndirectaVigenteSbif4) {
        this.deudaIndirectaVigenteSbif4 = deudaIndirectaVigenteSbif4;
    }

    public List<TableValueDTO> getDatosBalance() {
        return datosBalance;
    }

    public void setDatosBalance(List<TableValueDTO> datosBalance) {
        this.datosBalance = datosBalance;
    }

    public List<TableValueDTO> getDatosEstadoResultado() {
        return datosEstadoResultado;
    }

    public void setDatosEstadoResultado(List<TableValueDTO> datosEstadoResultado) {
        this.datosEstadoResultado = datosEstadoResultado;
    }

    public List<TableValueDTO> getDatosGenFondFlujEfectivo() {
        return datosGenFondFlujEfectivo;
    }

    public void setDatosGenFondFlujEfectivo(List<TableValueDTO> datosGenFondFlujEfectivo) {
        this.datosGenFondFlujEfectivo = datosGenFondFlujEfectivo;
    }

    public List<TableValueDTO> getDatosIndicadorFinanciero() {
        return datosIndicadorFinanciero;
    }

    public void setDatosIndicadorFinanciero(List<TableValueDTO> datosIndicadorFinanciero) {
        this.datosIndicadorFinanciero = datosIndicadorFinanciero;
    }

    public List<NoteValueDTO> getDatosNotasDesglose() {
        return datosNotasDesglose;
    }

    public void setDatosNotasDesglose(List<NoteValueDTO> datosNotasDesglose) {
        this.datosNotasDesglose = datosNotasDesglose;
    }

    public List<CompanyValueDTO> getDatosEmpresasConsolidan() {
        return datosEmpresasConsolidan;
    }

    public void setDatosEmpresasConsolidan(List<CompanyValueDTO> datosEmpresasConsolidan) {
        this.datosEmpresasConsolidan = datosEmpresasConsolidan;
    }

    public List<ObservationValueDTO> getDatosObservaciones() {
        return datosObservaciones;
    }

    public void setDatosObservaciones(List<ObservationValueDTO> datosObservaciones) {
        this.datosObservaciones = datosObservaciones;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getBeforeLastDate() {
        return beforeLastDate;
    }

    public void setBeforeLastDate(String beforeLastDate) {
        this.beforeLastDate = beforeLastDate;
    }

    public String getEjecutor() {
        return ejecutor;
    }

    public void setEjecutor(String ejecutor) {
        this.ejecutor = ejecutor;
    }

    public String getEjecutorFechaModificacion() {
        return ejecutorFechaModificacion;
    }

    public void setEjecutorFechaModificacion(String ejecutorFechaModificacion) {
        this.ejecutorFechaModificacion = ejecutorFechaModificacion;
    }

    public String getRevisor() {
        return revisor;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
    }

    public String getRevisorFechaModificacion() {
        return revisorFechaModificacion;
    }

    public void setRevisorFechaModificacion(String revisorFechaModificacion) {
        this.revisorFechaModificacion = revisorFechaModificacion;
    }

    public String getFecCompletaPerido1() {
        return fecCompletaPerido1;
    }

    public void setFecCompletaPerido1(String fecCompletaPerido1) {
        this.fecCompletaPerido1 = fecCompletaPerido1;
    }

    public String getFecCompletaPerido2() {
        return fecCompletaPerido2;
    }

    public void setFecCompletaPerido2(String fecCompletaPerido2) {
        this.fecCompletaPerido2 = fecCompletaPerido2;
    }

    public String getFecCompletaPerido3() {
        return fecCompletaPerido3;
    }

    public void setFecCompletaPerido3(String fecCompletaPerido3) {
        this.fecCompletaPerido3 = fecCompletaPerido3;
    }

    public String getFecCompletaPerido4() {
        return fecCompletaPerido4;
    }

    public void setFecCompletaPerido4(String fecCompletaPerido4) {
        this.fecCompletaPerido4 = fecCompletaPerido4;
    }
}
