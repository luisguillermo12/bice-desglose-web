package cl.tutorial.desglose.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the /download/informe-proyecccion.
 */
public class PdfProyectionReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Datos generales del reporte
    private String fileName;

    // Datos de códigos
    private String codCliente;
    private String codTipoPlantilla;
    private String codTipoDes;
    private String codSubtipoDes;
    private String codMoneda;
    private int    yearBase;
    private int    projectionYears;


    private String  periodHistoryFecha;
    private String  period1Fecha;
    private String  period2Fecha;
    private String  period3Fecha;
    private String  period4Fecha;
    private String  period5Fecha;
    private String  period6Fecha;
    private String  period7Fecha;
    private String  period8Fecha;
    private String  period9Fecha;
    private String  period10Fecha;
    private String  period11Fecha;
    private String  period12Fecha;
    private String  period13Fecha;
    private String  period14Fecha;
    private String  period15Fecha;
    private String  period16Fecha;
    private String  period17Fecha;
    private String  period18Fecha;
    private String  period19Fecha;
    private String  period20Fecha;
    private String  reporteSeleccionado;
    private URL imgLogoBancoBice;
    private String ejecutor;
    private String ejecutorFechaModificacion;
    private String revisor;
    private String revisorFechaModificacion;
    private String fechaImpresion;

    public String getFechaImpresion() {
        return fechaImpresion;
    }

    public void setFechaImpresion(Date fechaImpresion) {
        this.fechaImpresion = String.valueOf(fechaImpresion);
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

    public URL getImgLogoBancoBice() {
        return imgLogoBancoBice;
    }

    public void setImgLogoBancoBice(URL imgLogoBancoBice) {
        this.imgLogoBancoBice = imgLogoBancoBice;
    }

    public String getReporteSeleccionado() {
        return reporteSeleccionado;
    }

    public void setReporteSeleccionado(String reporteSeleccionado) {
        this.reporteSeleccionado = reporteSeleccionado;
    }



    public String getPeriodHistoryFecha() {
        return periodHistoryFecha;
    }

    public void setPeriodHistoryFecha(String periodHistoryFecha) {
        this.periodHistoryFecha = periodHistoryFecha;
    }

    public String getPeriod1Fecha() {
        return period1Fecha;
    }

    public void setPeriod1Fecha(String period1Fecha) {
        this.period1Fecha = period1Fecha;
    }

    public String getPeriod2Fecha() {
        return period2Fecha;
    }

    public void setPeriod2Fecha(String period2Fecha) {
        this.period2Fecha = period2Fecha;
    }

    public String getPeriod3Fecha() {
        return period3Fecha;
    }

    public void setPeriod3Fecha(String period3Fecha) {
        this.period3Fecha = period3Fecha;
    }

    public String getPeriod4Fecha() {
        return period4Fecha;
    }

    public void setPeriod4Fecha(String period4Fecha) {
        this.period4Fecha = period4Fecha;
    }

    public String getPeriod5Fecha() {
        return period5Fecha;
    }

    public void setPeriod5Fecha(String period5Fecha) {
        this.period5Fecha = period5Fecha;
    }

    public String getPeriod6Fecha() {
        return period6Fecha;
    }

    public void setPeriod6Fecha(String period6Fecha) {
        this.period6Fecha = period6Fecha;
    }

    public String getPeriod7Fecha() {
        return period7Fecha;
    }

    public void setPeriod7Fecha(String period7Fecha) {
        this.period7Fecha = period7Fecha;
    }

    public String getPeriod8Fecha() {
        return period8Fecha;
    }

    public void setPeriod8Fecha(String period8Fecha) {
        this.period8Fecha = period8Fecha;
    }

    public String getPeriod9Fecha() {
        return period9Fecha;
    }

    public void setPeriod9Fecha(String period9Fecha) {
        this.period9Fecha = period9Fecha;
    }

    public String getPeriod10Fecha() {
        return period10Fecha;
    }

    public void setPeriod10Fecha(String period10Fecha) {
        this.period10Fecha = period10Fecha;
    }

    public String getPeriod11Fecha() {
        return period11Fecha;
    }

    public void setPeriod11Fecha(String period11Fecha) {
        this.period11Fecha = period11Fecha;
    }

    public String getPeriod12Fecha() {
        return period12Fecha;
    }

    public void setPeriod12Fecha(String period12Fecha) {
        this.period12Fecha = period12Fecha;
    }

    public String getPeriod13Fecha() {
        return period13Fecha;
    }

    public void setPeriod13Fecha(String period13Fecha) {
        this.period13Fecha = period13Fecha;
    }

    public String getPeriod14Fecha() {
        return period14Fecha;
    }

    public void setPeriod14Fecha(String period14Fecha) {
        this.period14Fecha = period14Fecha;
    }

    public String getPeriod15Fecha() {
        return period15Fecha;
    }

    public void setPeriod15Fecha(String period15Fecha) {
        this.period15Fecha = period15Fecha;
    }

    public String getPeriod16Fecha() {
        return period16Fecha;
    }

    public void setPeriod16Fecha(String period16Fecha) {
        this.period16Fecha = period16Fecha;
    }

    public String getPeriod17Fecha() {
        return period17Fecha;
    }

    public void setPeriod17Fecha(String period17Fecha) {
        this.period17Fecha = period17Fecha;
    }

    public String getPeriod18Fecha() {
        return period18Fecha;
    }

    public void setPeriod18Fecha(String period18Fecha) {
        this.period18Fecha = period18Fecha;
    }

    public String getPeriod19Fecha() {
        return period19Fecha;
    }

    public void setPeriod19Fecha(String period19Fecha) {
        this.period19Fecha = period19Fecha;
    }

    public String getPeriod20Fecha() {
        return period20Fecha;
    }

    public void setPeriod20Fecha(String period20Fecha) {
        this.period20Fecha = period20Fecha;
    }




    // Datos del encabezado
    private String rutCliente;
    private String nombreCliente;
    private int    year;



    /**
     * Datos de proyecciones por períodos
     */
    private List<TableValueProyeccionesDTO> datosProyecciones;
    private List<TableValueProyeccionesDTO> datosProyeccionesFlujoCaja;
    private List<TableValueProyeccionesDTO> datosProyeccionesEstadoResultado;

    public List<TableValueProyeccionesDTO> getDatosProyeccionesEstadoResultado() {
        return datosProyeccionesEstadoResultado;
    }

    public void setDatosProyeccionesEstadoResultado(List<TableValueProyeccionesDTO> datosProyeccionesEstadoResultado) {
        this.datosProyeccionesEstadoResultado = datosProyeccionesEstadoResultado;
    }

    public List<TableValueProyeccionesDTO> getDatosProyeccionesFlujoCaja() {
        return datosProyeccionesFlujoCaja;
    }

    public void setDatosProyeccionesFlujoCaja(List<TableValueProyeccionesDTO> datosProyeccionesFlujoCaja) {
        this.datosProyeccionesFlujoCaja = datosProyeccionesFlujoCaja;
    }




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

    public int getProjectionYears() {
        return projectionYears;
    }

    public void setProjectionYears(int projectionYears) {
        this.projectionYears = projectionYears;
    }

    public int getYearBase() {
        return yearBase;
    }

    public void setYearBase(int yearBase) {
        this.yearBase = yearBase;
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


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<TableValueProyeccionesDTO> getDatosProyecciones() {
        return datosProyecciones;
    }

    public void setDatosProyecciones(List<TableValueProyeccionesDTO> datosProyecciones) {
        this.datosProyecciones = datosProyecciones;
    }

    @Override
    public String toString() {
        return "PdfProyectionReportDTO{" +
            "fileName='" + fileName + '\'' +
            ", codCliente='" + codCliente + '\'' +
            ", codTipoPlantilla='" + codTipoPlantilla + '\'' +
            ", codTipoDes='" + codTipoDes + '\'' +
            ", codSubtipoDes='" + codSubtipoDes + '\'' +
            ", codMoneda='" + codMoneda + '\'' +
            ", yearBase=" + yearBase +
            ", projectionYears=" + projectionYears +
            ", rutCliente='" + rutCliente + '\'' +
            ", nombreCliente='" + nombreCliente + '\'' +
            ", year=" + year +
            ", datosProyecciones=" + datosProyecciones +
            ", tituloReporte='" + tituloReporte + '\'' +
            '}';
    }
}
