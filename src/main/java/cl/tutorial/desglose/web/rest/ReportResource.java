package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.ProjectionService;
import cl.tutorial.desglose.service.ReportService;
import cl.tutorial.desglose.service.ValPeriodoDesgloseService;
import cl.tutorial.desglose.service.dto.LogDTO;
import cl.tutorial.desglose.service.dto.ProjectionHistoricDataRequestDTO;
import cl.tutorial.desglose.service.dto.report.DesgloseImprimeDTO;
import cl.tutorial.desglose.service.dto.report.PdfIngressReportDTO;
import cl.tutorial.desglose.service.dto.report.PdfProyectionReportDTO;
import cl.tutorial.desglose.service.dto.report.PdfSummaryReportDTO;
import com.codahale.metrics.annotation.Timed;
import net.sf.jasperreports.engine.JRException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.CLIENTE_SOLICITA_WEB;

/**
 * REST controller for custom sql queries.
 */
@RestController
@RequestMapping("/api/report")
public class ReportResource {

    private final Logger log = LogManager.getLogger(ReportResource.class);

    private final ReportService reportService;

    private final ValPeriodoDesgloseService valPeriodoDesgloseService;

    private final ProjectionService projectionService;


    public ReportResource(ReportService reportService,ValPeriodoDesgloseService valPeriodoDesgloseService,
                          ProjectionService projectionService) {
        this.reportService = reportService;
        this.valPeriodoDesgloseService = valPeriodoDesgloseService;
        this.projectionService = projectionService;
    }

    /**
     * POST  /download/informe-ingreso : devuelve el reporte PDF Informe ingreso. PdfSummaryReportDTO
     *
     * @return the ResponseEntity with status 200 (OK) and a binary array whit the PDF file in body
     */
    @PostMapping("/download/informe-ingreso")
    @Timed
    public ResponseEntity<byte[]> getInformeIngresoPdf(@Valid @RequestBody PdfIngressReportDTO dto) throws JRException {
        // dto.setFileName(new Date().getTime() + ".pdf");
        log.info("REST request to get InformeIngresoPdf");
        byte[] pdfReport = reportService.getInformeIngresoPdf(dto);
        return getResponseEntity(pdfReport, dto.getFileName());
        // String cadena = Base64.getEncoder().encodeToString(pdfReport);
    }

    /**
     * POST  /download/informe-resumen-desglose-balance : devuelve el reporte PDF Informe resumen desglose balance.
     *
     * @return the ResponseEntity with status 200 (OK) and a binary array whit the PDF file in body
     */
    @PostMapping("/download/informe-resumen-desglose-balance")
    @Timed
    public ResponseEntity<byte[]> getInformeResumenDesgloseBalancePdf(@Valid @RequestBody PdfSummaryReportDTO dto) throws JRException {
        log.info("REST request to get InformeResumenDesgloseBalancePdf");
        // dto.setFileName(new Date().getTime() + ".pdf");
        byte[] pdfReport = reportService.getInformeResumenDesgloseBalancePdf(dto);
        return getResponseEntity(pdfReport, dto.getFileName());
    }

    /**
     *
     * @param dto
     * @return
     * @throws JRException
     */
    @PostMapping("/download/informe-ingreso-server")
    @Timed
    public ResponseEntity<byte[]> getInformeIngresoPdf(@Valid @RequestBody DesgloseImprimeDTO dto) throws JRException {
        log.info("REST request to get InformeIngresoPdf");
        // dto.setFileName(new Date().getTime() + ".pdf");
        PdfIngressReportDTO pdfIngressReportDTO = reportService.getPdfIngressReport(dto);
        byte[] pdfReport = reportService.getInformeIngresoPdf(pdfIngressReportDTO);
        // String cadena = Base64.getEncoder().encodeToString(pdfReport);
        return getResponseEntity(pdfReport, pdfIngressReportDTO.getFileName());
    }

    /**
     *
     * @param dto
     * @return
     * @throws JRException
     */
    @PostMapping("/download/informe-resumen-desglose-balance-server")
    @Timed
    public ResponseEntity<byte[]> getInformeResumenDesgloseBalancePdf(@Valid @RequestBody DesgloseImprimeDTO dto) throws JRException {
        log.info("REST request to get InformeResumenDesgloseBalancePdf");
        // dto.setFileName(new Date().getTime() + ".pdf");
        PdfSummaryReportDTO pdfSummaryReportDTO = reportService.getPdfSummaryReport(dto,CLIENTE_SOLICITA_WEB);
        byte[] pdfReport = reportService.getInformeResumenDesgloseBalancePdf(pdfSummaryReportDTO);
        return getResponseEntity(pdfReport, pdfSummaryReportDTO.getFileName());
    }

    /**/
    @PostMapping("/download/informe-proyeccion")
    @Timed
    public ResponseEntity<byte[]> getImprData(@Valid @RequestBody ProjectionHistoricDataRequestDTO historicDataRequestDTO) throws JRException {
        log.info("REST request to get Imprime Data to project a Proyecciones with value: {}", historicDataRequestDTO);
        PdfProyectionReportDTO pdfProyectionReportDTO = this.reportService.getPdfProyectionReport(historicDataRequestDTO);
        byte[] pdfReport = reportService.getInformeProyeccionesPdf(pdfProyectionReportDTO);
        return getResponseEntity(pdfReport, pdfProyectionReportDTO.getFileName());
    }

    @GetMapping("/download/log-desglose/{fechaInicio}/{fechaFin}")
    @Timed
    public ResponseEntity<List<LogDTO>> getLogDesglose(@PathVariable String fechaInicio,@PathVariable  String fechaFin) throws java.io.IOException {
        log.info("inicio traigo ficheros de log de desglose");
            List<LogDTO> listaFicheros = this.listarCarpetas(fechaInicio,fechaFin);
        return   new ResponseEntity<>(listaFicheros,HttpStatus.OK);
    }

    @PostMapping("/download/log-desglose-file")
    @Timed
    public ResponseEntity<byte[]> getImprData(@RequestBody String fichero) throws JRException {
        log.info("inicio descargando fichero", fichero);
        byte[] log = readBytesFromFile("target/"+ fichero);
        return getResponseTxt(log,fichero);
    }

    public List<LogDTO> listarCarpetas(String fechaI, String fechaF) {
        try {
            log.info("METODO LISTAR CARPETA");
            ZoneId defaultZoneId = ZoneId.systemDefault();
            LocalDate dateI = null;
            LocalDate dateF = null;
            if(!fechaI.equals("null") && !fechaF.equals("null") ) {
                dateI = LocalDate.parse(fechaI);
                dateF = LocalDate.parse(fechaF);
            } else {
                dateI = LocalDate.now();
                dateF = LocalDate.now().plusDays(1);
            }
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.matches(".*(.log|.gz)");
                }
            };
            File carpeta = new File("target");
            File[] archivos = carpeta.listFiles(filter);
            Arrays.sort(archivos, Comparator.comparingLong(File::lastModified).reversed());
            List<LogDTO> archivosLog = new LinkedList<>();
            if (archivos == null || archivos.length == 0) {
                return archivosLog;
            } else {
                for (int i = 0; i < archivos.length; i++) {
                    File archivo = archivos[i];
                    Date date1 = new Date(archivo.lastModified());
                    Calendar calendardate1 = Calendar.getInstance();
                    calendardate1.setTime(date1);
                    int yearA = calendardate1.get(Calendar.YEAR);
                    int monthA = calendardate1.get(Calendar.MONTH)+1;
                    int dayA = calendardate1.get(Calendar.DAY_OF_MONTH);
                    LocalDate fechaM = LocalDate.of(yearA,monthA,dayA);
                    Date fechaFormateada = Date.from(fechaM.atStartOfDay(defaultZoneId).toInstant());
                    for (Iterator<Date> it = this.getListaEntreFechas(Date.from(dateI.atStartOfDay(defaultZoneId).toInstant()), Date.from(dateF.atStartOfDay(defaultZoneId).toInstant())).iterator(); it.hasNext(); ) {
                        Date date = it.next();
                        DecimalFormat formato = new DecimalFormat("#,###,##0");
                        if (date.compareTo(fechaFormateada) == 0) {
                            LogDTO logDTO = new LogDTO();
                            logDTO.nombre = archivo.getName();
                            logDTO.megaBytes = formato.format(Float.parseFloat( String.valueOf(archivo.length())) / 1024);
                            archivosLog.add(logDTO);
                        }
                    }
                }
            }
            return archivosLog;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }
    public List<Date> getListaEntreFechas(Date fechaInicio, Date fechaFin) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(fechaInicio);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaFin);
        List<Date> listaFechas = new ArrayList<Date>();
        while (!c1.after(c2)) {
            listaFechas.add(c1.getTime());
            c1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return listaFechas;
    }


    @GetMapping("/download/informe-desglose/{codCliente}/{codTipoDes}/{codSubtipoDes}/{codMoneda}/{codPlantilla}/{clienteSolicita}")
    @Timed
    public ResponseEntity<byte[]> getInformeResumenDesgloseBalancePdf(@PathVariable String codCliente, @PathVariable Integer codTipoDes, @PathVariable Integer codSubtipoDes,
        @PathVariable String codMoneda, @PathVariable Integer codPlantilla,@PathVariable String clienteSolicita) throws JRException {
        log.info("REST request to get informe-desglose");
        // dto.setFileName(new Date().getTime() + ".pdf");
        DesgloseImprimeDTO dto = new DesgloseImprimeDTO();
        dto.setCodCliente(codCliente);
        dto.setCodMoneda(codMoneda);
        dto.setCodMonedaImpresion(codMoneda);
        dto.setCodTipoDes(codTipoDes);
        dto.setCodSubtipoDes(codSubtipoDes);
        dto.setImpresionRapida(true);
        dto.setCodTipoPlantilla(codPlantilla);
        PdfSummaryReportDTO pdfSummaryReportDTO = reportService.getPdfSummaryReport(dto,clienteSolicita);
        byte[] pdfReport = reportService.getInformeResumenDesgloseBalancePdf(pdfSummaryReportDTO);
        return getResponseEntity(pdfReport, pdfSummaryReportDTO.getFileName());
    }
    @NotNull
    private ResponseEntity<byte[]> getResponseEntity(byte[] pdfReport, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
        return response;
    }
    @NotNull
    private ResponseEntity<byte[]> getResponseTxt(byte[] pdfReport, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/plain"));
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
        return response;
    }
    private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bytesArray;

    }
}
