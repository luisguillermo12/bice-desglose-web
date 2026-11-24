package cl.tutorial.desglose.service.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the /download/informe-ingreso service.
 */
public class PdfIngressReportDTO implements Serializable {

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

    // Datos de los períodos.
    private String auditor1;
    private String auditor2;
    private String auditor3;
    private String auditor4;
    private String fecPerido1;
    private String fecPerido2;
    private String fecPerido3;
    private String fecPerido4;
    private String numMeses1;
    private String numMeses2;
    private String numMeses3;
    private String numMeses4;

    // Datos del balance por períodos
    private List<TableValueDTO> tableValueDTOS;

    // Este campo no se debe pedir como entrada de datos en el servicio web.
    @JsonIgnore
    private JRBeanCollectionDataSource collectionDataSource;

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

    public List<TableValueDTO> getTableValueDTOS() {
        return tableValueDTOS;
    }

    public void setTableValueDTOS(List<TableValueDTO> tableValueDTOS) {
        this.tableValueDTOS = tableValueDTOS;
    }

    public JRBeanCollectionDataSource getCollectionDataSource() {
        return collectionDataSource;
    }

    public void setCollectionDataSource(JRBeanCollectionDataSource collectionDataSource) {
        this.collectionDataSource = collectionDataSource;
    }

    @Override
    public String toString() {
        return "PdfIngressReportDTO{" + "fileName='" + fileName + '\'' + ", codCliente='" + codCliente + '\'' + ", codTipoPlantilla='" + codTipoPlantilla + '\'' + ", codTipoDes='"
            + codTipoDes + '\'' + ", codSubtipoDes='" + codSubtipoDes + '\'' + ", codMoneda='" + codMoneda + '\'' + ", rutCliente='" + rutCliente + '\'' + ", nombreCliente='"
            + nombreCliente + '\'' + ", tipoPlantillaDesglose='" + tipoPlantillaDesglose + '\'' + ", tipoDesglose='" + tipoDesglose + '\'' + ", subTipoDesglose='" + subTipoDesglose
            + '\'' + ", moneda='" + moneda + '\'' + ", sector='" + sector + '\'' + ", subSector='" + subSector + '\'' + ", actividad='" + actividad + '\'' + ", iva='" + iva + '\''
            + ", montosEn='" + montosEn + '\'' + ", actualizadoAl='" + actualizadoAl + '\'' + ", auditor1='" + auditor1 + '\'' + ", auditor2='" + auditor2 + '\'' + ", auditor3='"
            + auditor3 + '\'' + ", auditor4='" + auditor4 + '\'' + ", fecPerido1='" + fecPerido1 + '\'' + ", fecPerido2='" + fecPerido2 + '\'' + ", fecPerido3='" + fecPerido3
            + '\'' + ", fecPerido4='" + fecPerido4 + '\'' + ", numMeses1='" + numMeses1 + '\'' + ", numMeses2='" + numMeses2 + '\'' + ", numMeses3='" + numMeses3 + '\''
            + ", numMeses4='" + numMeses4 + '\'' + ", tableValueDTOS=" + tableValueDTOS + ", collectionDataSource=" + collectionDataSource + '}';
    }
}
