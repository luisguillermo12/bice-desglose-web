package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.repository.ValPeriodoDesgloseCifRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import cl.tutorial.desglose.repository.ValPeriodoDesgloseRepository;
import cl.tutorial.desglose.service.dto.*;
import cl.tutorial.desglose.service.dto.report.*;
import cl.tutorial.desglose.web.rest.errors.ImpresionNotExistException;
import io.swagger.models.properties.BinaryProperty;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRSaver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.type.BigDecimalType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Service Implementation for managing Reports.
 */
@Service
@Transactional
public class ReportService {

    private static final String ENTITY_NAME = "report";

    private static final String[] SUBREPORTS_TEMPLATE_INFORME_DESGLOSE_BALANCE_TO_COMPILE =
        { "subrpt_tbl_bal_est_res", "subrpt_tbl_gen_fondos_ind_fin", "subrpt_tbl_notas_desglose", "subrpt_tbl_emp_consol", "subrpt_tbl_observaciones", "subrpt_table_button" };

    private static final String[] SUBREPORTS_TEMPLATE_INFORME_INGRESO_TO_COMPILE = { "subrpt_tbl_informe_ingreso" };
    private static final String[] SUBREPORTS_TEMPLATE_INFORME_PROYECCION_TO_COMPILE = { "prueba", "prueba2"  };

    private static final String JASPER_JRXML_EXT                      = ".jrxml";
    private static final String JASPER_JASPER_EXT                     = ".jasper";
    private static final String TEMPLATE_INFORME_INGRESO_NAME         = "informe_de_ingreso_A4";
    private static final String TEMPLATE_INFORME_DESGLOSE_BALANCE     = "informe_resumen_desglose_balance_A4";
    private static final String TEMPLATE_INFORME_PROYECCION           = "informe_proyecciones_A";
    private static final String REPORTS_TEMPLATES_PATH                = "jasper_report/templates/";
    private static final String DATA_SOURCE_PARAMETER_NAME            = "dataSource";
    private static final String SUB_REPORT_PATH_PARAMETER_NAME        = "subReportPath";
    private static final String USER_NAME_PARAMETER_NAME              = "userName";
    private static final String LAST_MODIFICATION_DATE_PARAMETER_NAME = "lastModificationDate";
    private static final String PDF_OUTPUT_DIRECTORY                  = "ouput/pdf/";

    private final Logger log = LogManager.getLogger(ReportService.class);

    private final DesgloseService desgloseService;
    private final ClienteService  clienteService;
    private final ConceptoService conceptoService;
    private final ActividadEconomicaService actividadEconomicaService;
    private final SectorService sectorService;
    private final SubSectorService subSectorService;
    private final DescripcionCodigoService descripcionCodigoService;
    private final DesgloseImpresionService desgloseImpresionService;
    private final PeriodoDesgImpresionService periodoDesgImpresionService;
    private final ValPeriodoDesgImprService valPeriodoDesgImprService;
    private final TurbineUserService turbineUserService;
    private final ProjectionService projectionService;

    private final ValPeriodoDesgloseRepository valPeriodoDesgloseRepository;
    private final ValPeriodoDesgloseCifRepository valPeriodoDesgloseCifRepository;

    public ReportService(DesgloseService desgloseService, ClienteService clienteService, ConceptoService conceptoService,
        ActividadEconomicaService actividadEconomicaService,SectorService sectorService,SubSectorService subSectorService,
        DescripcionCodigoService descripcionCodigoService,DesgloseImpresionService desgloseImpresionService,ValPeriodoDesgloseRepository valPeriodoDesgloseRepository,
        PeriodoDesgImpresionService periodoDesgImpresionService,ProjectionService projectionService, ValPeriodoDesgImprService valPeriodoDesgImprService,
        TurbineUserService turbineUserService,ValPeriodoDesgloseCifRepository valPeriodoDesgloseCifRepository) {
        this.desgloseService = desgloseService;
        this.clienteService = clienteService;
        this.conceptoService = conceptoService;
        this.actividadEconomicaService = actividadEconomicaService;
        this.sectorService = sectorService;
        this.subSectorService = subSectorService;
        this.descripcionCodigoService = descripcionCodigoService;
        this.desgloseImpresionService= desgloseImpresionService;
        this.periodoDesgImpresionService = periodoDesgImpresionService;
        this.valPeriodoDesgImprService = valPeriodoDesgImprService;
        this.turbineUserService = turbineUserService;
        this.valPeriodoDesgloseRepository = valPeriodoDesgloseRepository;
        this.projectionService = projectionService;
        this.valPeriodoDesgloseCifRepository = valPeriodoDesgloseCifRepository;
    }

    /**
     * Genera el pdf para el informe de ingreso.
     *
     * @param dto
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    @Transactional(readOnly = true)
    public byte[] getInformeIngresoPdf(PdfIngressReportDTO dto) throws JRException {
        log.info("getInformeIngresoPdf() - INICIO");
        InputStream employeeReportStream = compileReport(SUBREPORTS_TEMPLATE_INFORME_INGRESO_TO_COMPILE, TEMPLATE_INFORME_INGRESO_NAME);
        JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
         Map<String, Object> params = saveCompiledJasperAndInitializeCommonParams(dto, jasperReport, TEMPLATE_INFORME_INGRESO_NAME);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final byte[] jasperReportPDF = this.generateJasperReportPDF(TEMPLATE_INFORME_INGRESO_NAME + JASPER_JASPER_EXT, outputStream, params, dto.getFileName());
        log.info("getInformeIngresoPdf() - FIN");

        return jasperReportPDF;
    }

    /**
     * Genera el pdf para el informe de resumen de balance del desglose.
     *
     * @param dto
     *
     * @return
     */
    @Transactional(readOnly = true)
    public byte[] getInformeResumenDesgloseBalancePdf(PdfSummaryReportDTO dto) throws JRException {
        log.info("getInformeResumenDesgloseBalancePdf() - INICIO");
        InputStream employeeReportStream = compileReport(SUBREPORTS_TEMPLATE_INFORME_DESGLOSE_BALANCE_TO_COMPILE, TEMPLATE_INFORME_DESGLOSE_BALANCE);
         JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

        Map<String, Object> params = saveCompiledJasperAndInitializeCommonParams(dto, jasperReport, TEMPLATE_INFORME_DESGLOSE_BALANCE);
        params.put(this.USER_NAME_PARAMETER_NAME, dto.getNombreUsuario());
        params.put(this.LAST_MODIFICATION_DATE_PARAMETER_NAME, dto.getFechaModificacion());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final byte[] jasperReportPDF = this.generateJasperReportPDF(TEMPLATE_INFORME_DESGLOSE_BALANCE+JASPER_JASPER_EXT, outputStream, params, dto.getFileName());
        log.info("getInformeResumenDesgloseBalancePdf() - FIN");
        return jasperReportPDF;
    }
    @Transactional(readOnly = true)
    public byte[] getInformeProyeccionesPdf(PdfProyectionReportDTO dto) throws JRException {
        log.info("Informe Proyecciones() - INICIO");
        InputStream employeeReportStream = compileReport(SUBREPORTS_TEMPLATE_INFORME_PROYECCION_TO_COMPILE, TEMPLATE_INFORME_PROYECCION);
         JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

        Map<String, Object> params = saveCompiledJasperAndInitializeCommonParams(dto, jasperReport, TEMPLATE_INFORME_PROYECCION);
       /* params.put(this.USER_NAME_PARAMETER_NAME, dto.getNombreUsuario());
        params.put(this.LAST_MODIFICATION_DATE_PARAMETER_NAME, dto.getFechaModificacion());*/

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final byte[] jasperReportPDF = this.generateJasperReportPDF(TEMPLATE_INFORME_PROYECCION+JASPER_JASPER_EXT, outputStream, params, dto.getFileName());
        log.info("Informe Proyecciones() - FIN");
        return jasperReportPDF;
    }
    public PdfProyectionReportDTO getPdfProyectionReport(ProjectionHistoricDataRequestDTO historicDataRequestDTO) {
        List<ClienteDTO> clienteDTO = clienteService.findByRut(historicDataRequestDTO.codCliente, historicDataRequestDTO.codTipoDes,
            historicDataRequestDTO.codSubtipoDes, historicDataRequestDTO.codMoneda, historicDataRequestDTO.codPlantilla,null);

        ConceptoRequestDTO conceptoRequestDTO = new ConceptoRequestDTO();
        conceptoRequestDTO.templateCode = 50000;
        List<String> conceptosTypesCodes = new LinkedList<>();
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA);
        List<String> orderByFiels = new LinkedList<>();
        orderByFiels.add(FIELD_NAME_COD_TIPO_CONCEP);
        orderByFiels.add(FIELD_NAME_NUM_SECUENCIA);
        conceptoRequestDTO.conceptTypeCodesIn = conceptosTypesCodes;
        conceptoRequestDTO.orderByFields = orderByFiels;
        conceptoRequestDTO.orderByDirection = "ASC";
        conceptoRequestDTO.flgConActivo = COD_REG_STATUS_ONE;
        conceptoRequestDTO.columnLetterGlsFormulaVal1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaVal2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaVal3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaVal4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaDef1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaDef2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaDef3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaDef4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaPrc1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaPrc2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaPrc3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaPrc4 = COLUMN_NUMBER_GLS_FORMULA__4;
        List<ConceptoDTO> conceptoDTOList = conceptoService.findByTemplateAndCodConceptsIn(conceptoRequestDTO,null).conceptoDTOList;

        BreakdownRecoverResponseDTO page = projectionService.getHistoricData(historicDataRequestDTO);
        PdfProyectionReportDTO pdfProyectionReportDTO = new PdfProyectionReportDTO();
        HashSet<String> fechasProyectadas = new HashSet<String>();
        for(ValPeriodoProyeccionDesgloseDTO item:page.extraDataDTO.periodos.get(0).valPeriodoProyeccionDesgloseDTOList) {
            fechasProyectadas.add(item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT));
        }
        TreeSet myTreeSet = new TreeSet();
        myTreeSet.addAll(fechasProyectadas);
        pdfProyectionReportDTO.setFileName("informeProyeccion");
        Iterator<String> itr=myTreeSet.iterator();
        Integer contador = 0;
        while(itr.hasNext()) {
            String fecha = itr.next();
            switch (contador){
                case 0:
                    pdfProyectionReportDTO.setPeriodHistoryFecha(fecha);
                    break;
                case 1:
                    pdfProyectionReportDTO.setPeriod1Fecha(fecha);
                    break;
                case 2:
                    pdfProyectionReportDTO.setPeriod2Fecha(fecha);
                    break;
                case 3:
                    pdfProyectionReportDTO.setPeriod3Fecha(fecha);
                    break;
                case 4:
                    pdfProyectionReportDTO.setPeriod4Fecha(fecha);
                    break;
                case 5:
                    pdfProyectionReportDTO.setPeriod5Fecha(fecha);
                    break;
                case 6:
                    pdfProyectionReportDTO.setPeriod6Fecha(fecha);
                    break;
                case 7:
                    pdfProyectionReportDTO.setPeriod7Fecha(fecha);
                    break;
                case 8:
                    pdfProyectionReportDTO.setPeriod8Fecha(fecha);
                    break;
                case 9:
                    pdfProyectionReportDTO.setPeriod9Fecha(fecha);
                    break;
                case 10:
                    pdfProyectionReportDTO.setPeriod10Fecha(fecha);
                    break;
                case 11:
                    pdfProyectionReportDTO.setPeriod11Fecha(fecha);
                    break;
                case 12:
                    pdfProyectionReportDTO.setPeriod12Fecha(fecha);
                    break;
                case 13:
                    pdfProyectionReportDTO.setPeriod13Fecha(fecha);
                    break;
                case 14:
                    pdfProyectionReportDTO.setPeriod14Fecha(fecha);
                    break;
                case 15:
                    pdfProyectionReportDTO.setPeriod15Fecha(fecha);
                    break;
                case 16:
                    pdfProyectionReportDTO.setPeriod16Fecha(fecha);
                    break;
                case 17:
                    pdfProyectionReportDTO.setPeriod17Fecha(fecha);
                    break;
                case 18:
                    pdfProyectionReportDTO.setPeriod18Fecha(fecha);
                    break;
                case 19:
                    pdfProyectionReportDTO.setPeriod19Fecha(fecha);
                    break;
                case 20:
                    pdfProyectionReportDTO.setPeriod20Fecha(fecha);
                    break;
            }
            contador++;
        }
        URL imagen = this.getClass().getClassLoader().getResource("jasper_report/templates/Logo_Bice_Reporte.gif");

        pdfProyectionReportDTO.setImgLogoBancoBice(imagen);
        pdfProyectionReportDTO.setEjecutor("Pejecutor");
        pdfProyectionReportDTO.setEjecutorFechaModificacion(historicDataRequestDTO.fecPeriodo.format(Constants.FORMATTER_DATE_SHORT));
        pdfProyectionReportDTO.setRevisorFechaModificacion(historicDataRequestDTO.fecPeriodo.format(Constants.FORMATTER_DATE_SHORT));
        pdfProyectionReportDTO.setRevisor("Previsor");
        Date dateNow = new Date();
        pdfProyectionReportDTO.setFechaImpresion(dateNow);
        pdfProyectionReportDTO.setCodCliente(formatearRUT(historicDataRequestDTO.codCliente));
        pdfProyectionReportDTO.setNombreCliente(clienteDTO.get(0).getName());
        pdfProyectionReportDTO.setYearBase(historicDataRequestDTO.fecPeriodo.getYear());
        pdfProyectionReportDTO.setRutCliente(formatearRUT(historicDataRequestDTO.codCliente));
        pdfProyectionReportDTO.setProjectionYears(contador-1);
        if (pdfProyectionReportDTO.getProjectionYears()>10 ){
            pdfProyectionReportDTO.setReporteSeleccionado("prueba.jasper");
        }else{
            pdfProyectionReportDTO.setReporteSeleccionado("prueba2.jasper");
        }


        List<TableValueProyeccionesDTO> tableValueProyeccionesDTOS = new LinkedList<>();
        List<TableValueProyeccionesDTO> tableValueProyeccionesDTOSEstadoResultado = new LinkedList<>();
        List<TableValueProyeccionesDTO> tableValueProyeccionesDTOSFlujoCaja = new LinkedList<>();
        for(ConceptoDTO conceptoDTO:conceptoDTOList) {
            TableValueProyeccionesDTO table = new TableValueProyeccionesDTO();
            TableValueProyeccionesDTO table2 = new TableValueProyeccionesDTO();
            TableValueProyeccionesDTO table3 = new TableValueProyeccionesDTO();
            String zeros = "";
            if(conceptoDTO.getNumDecPrint()!=null) {
                for (int i = 0; i < Integer.valueOf(conceptoDTO.getNumDecPrint()); i++) {
                    zeros = zeros + "0";
                }
            }
            String formatoPos = "#,##0."+ zeros.trim();
            String formatoNeg = "-#,##0."+ zeros.trim();
            if(zeros.trim().isEmpty()) {
                formatoPos = "#,##0";
                formatoNeg = "-#,##0";
            }
            if (conceptoDTO.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL) && conceptoDTO.getFlgIsPrinted()==1) {
                table = generaDatosProyecciones(conceptoDTO,pdfProyectionReportDTO,page,COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL);
                table.setFormatPathern(formatoPos+";"+formatoNeg);
                tableValueProyeccionesDTOS.add(table);
            }
            if (conceptoDTO.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_EST_RESULT) && conceptoDTO.getFlgIsPrinted()==1) {
                table2 = generaDatosProyecciones(conceptoDTO,pdfProyectionReportDTO,page,COD_TIPO_CONCEP_PROY_DM_EST_RESULT);
                table2.setFormatPathern(formatoPos+";"+formatoNeg);
                tableValueProyeccionesDTOSEstadoResultado.add(table2);
            }
            if (conceptoDTO.getCodTipoConcep().equals(COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA) && conceptoDTO.getFlgIsPrinted()==1) {
                table3 = generaDatosProyecciones(conceptoDTO,pdfProyectionReportDTO,page,COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA);
                table3.setFormatPathern(formatoPos+";"+formatoNeg);
                tableValueProyeccionesDTOSFlujoCaja.add(table3);
            }
        }
        pdfProyectionReportDTO.setDatosProyecciones(tableValueProyeccionesDTOS);
        pdfProyectionReportDTO.setDatosProyeccionesEstadoResultado(tableValueProyeccionesDTOSEstadoResultado);
        pdfProyectionReportDTO.setDatosProyeccionesFlujoCaja(tableValueProyeccionesDTOSFlujoCaja);
        return  pdfProyectionReportDTO;
    }
    private TableValueProyeccionesDTO generaDatosProyecciones(ConceptoDTO conceptoDTO, PdfProyectionReportDTO pdfProyectionReportDTO,BreakdownRecoverResponseDTO page,String codTipoConcepto){
        TableValueProyeccionesDTO table = new TableValueProyeccionesDTO();
        if (conceptoDTO.getCodTipoConcep().equals(codTipoConcepto)) {
            if (conceptoDTO.getCodTipoRegis() == null || conceptoDTO.getCodTipoRegis().isEmpty()) {
               /* table.setFlgTititleO(true);
                table.setConceptName(conceptoDTO.getGlsConcepto());
                table.setFlgIsImpreso(1);
                table.setMoneda("MMCh$");*/
            } else {
                table.setFlgTititleO(false);
                table.setConceptName(conceptoDTO.getGlsConcepto());
                table.setFlgIsImpreso(1);
            }
            table.setFlgIsBold(conceptoDTO.getFlgIsBold());
            // table.setFlgIsBoldO(Boolean.parseBoolean(conceptoDTO.getFlgIsBold().toString()));
            //  table.setFlgIsEnclosedO(Boolean.parseBoolean(conceptoDTO.getFlgIsEnclosed().toString()));
            table.setFlgIsEnclosed(conceptoDTO.getFlgIsEnclosed());
            table.setFlgIsTotal(conceptoDTO.getFlgIsTotal());
            for (ValPeriodoProyeccionDesgloseDTO item : page.extraDataDTO.periodos.get(0).valPeriodoProyeccionDesgloseDTOList) {
                if (item.getCodTipoConcepPry().equals(conceptoDTO.getCodTipoConcep()) && item.getCorConceptoPry() == conceptoDTO.getCorConcepto()) {
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriodHistoryFecha())) {
                        table.setPeriodHistoryFecha(pdfProyectionReportDTO.getPeriodHistoryFecha());
                        table.setPeriodHistoryValue(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod1Fecha())) {
                        table.setPeriod1Fecha(pdfProyectionReportDTO.getPeriod1Fecha());
                        table.setPeriod1Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod2Fecha())) {
                        table.setPeriod2Fecha(pdfProyectionReportDTO.getPeriod2Fecha());
                        table.setPeriod2Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod3Fecha())) {
                        table.setPeriod3Fecha(pdfProyectionReportDTO.getPeriod3Fecha());
                        table.setPeriod3Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod4Fecha())) {
                        table.setPeriod4Fecha(pdfProyectionReportDTO.getPeriod4Fecha());
                        table.setPeriod4Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod5Fecha())) {
                        table.setPeriod5Fecha(pdfProyectionReportDTO.getPeriod5Fecha());
                        table.setPeriod5Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod6Fecha())) {
                        table.setPeriod6Fecha(pdfProyectionReportDTO.getPeriod6Fecha());
                        table.setPeriod6Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod7Fecha())) {
                        table.setPeriod7Fecha(pdfProyectionReportDTO.getPeriod7Fecha());
                        table.setPeriod7Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod8Fecha())) {
                        table.setPeriod8Fecha(pdfProyectionReportDTO.getPeriod8Fecha());
                        table.setPeriod8Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod9Fecha())) {
                        table.setPeriod9Fecha(pdfProyectionReportDTO.getPeriod9Fecha());
                        table.setPeriod9Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod10Fecha())) {
                        table.setPeriod10Fecha(pdfProyectionReportDTO.getPeriod10Fecha());
                        table.setPeriod10Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod11Fecha())) {
                        table.setPeriod11Fecha(pdfProyectionReportDTO.getPeriod11Fecha());
                        table.setPeriod11Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod12Fecha())) {
                        table.setPeriod12Fecha(pdfProyectionReportDTO.getPeriod12Fecha());
                        table.setPeriod12Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod13Fecha())) {
                        table.setPeriod13Fecha(pdfProyectionReportDTO.getPeriod13Fecha());
                        table.setPeriod13Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod14Fecha())) {
                        table.setPeriod14Fecha(pdfProyectionReportDTO.getPeriod14Fecha());
                        table.setPeriod14Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod15Fecha())) {
                        table.setPeriod15Fecha(pdfProyectionReportDTO.getPeriod15Fecha());
                        table.setPeriod15Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod16Fecha())) {
                        table.setPeriod16Fecha(pdfProyectionReportDTO.getPeriod16Fecha());
                        table.setPeriod16Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod17Fecha())) {
                        table.setPeriod17Fecha(pdfProyectionReportDTO.getPeriod17Fecha());
                        table.setPeriod17Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod18Fecha())) {
                        table.setPeriod18Fecha(pdfProyectionReportDTO.getPeriod18Fecha());
                        table.setPeriod18Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod19Fecha())) {
                        table.setPeriod19Fecha(pdfProyectionReportDTO.getPeriod19Fecha());
                        table.setPeriod19Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                    if (item.getFecPeriodoPry().format(FORMATTER_DATE_SHORT).toString().equals(pdfProyectionReportDTO.getPeriod20Fecha())) {
                        table.setPeriod20Fecha(pdfProyectionReportDTO.getPeriod20Fecha());
                        table.setPeriod20Value(item.getValConceptoPry());
                        table.setCorConcept(item.getCorConceptoPry().toString());
                    }
                }
            }
        }
        return  table;
    }
    public PdfSummaryReportDTO getPdfSummaryReport(DesgloseImprimeDTO dto, String clienteSolicita) {
        String esquema = null;
        List<DescripcionCodigoDTO> codigoDTOList = this.descripcionCodigoService.findAllByCodTabla("DGLCONFG",0,"codGenerico,ASC");
        for (DescripcionCodigoDTO item : codigoDTOList) {
            if(item!= null) {
                if (clienteSolicita.equals(CLIENTE_SOLICITA_WEB)) {
                    if (item.getCodGenerico() != null && item.getCodGenerico().equals("DGLIMPRE_WEB")) {
                        esquema = item.getGlsGenericalarga();
                    }
                }
                if (clienteSolicita.equals(CLIENTE_SOLICITA_VB)) {
                    if (item.getCodGenerico() != null && item.getCodGenerico().equals("DGLIMPRE_VB")) {
                        esquema = item.getGlsGenericalarga();
                    }
                }
                if (clienteSolicita.equals(CLIENTE_SOLICITA_CORREO)) {
                    if (item.getCodGenerico() != null && item.getCodGenerico().equals("DGLIMPRE_CORREO")) {
                        esquema = item.getGlsGenericalarga();
                    }
                }
            }
        }
        final TurbineUserDTO user = new TurbineUserDTO();
        BreakdownSearchRequestDTO breakdownSearchRequestDTO = new BreakdownSearchRequestDTO();
        breakdownSearchRequestDTO.codCliente = dto.getCodCliente();
        breakdownSearchRequestDTO.tipoDesglose = dto.getCodTipoDes();
        breakdownSearchRequestDTO.subTipoDesglose = dto.getCodSubtipoDes();
        breakdownSearchRequestDTO.moneda = dto.getCodMoneda();
        breakdownSearchRequestDTO.tipoPlantillaDesglose = dto.getCodTipoPlantilla();
        BreakdownRecoverResponseDTO responseDTO = null;
        try {
             responseDTO = desgloseService.recoverDatosImpresionBreakdown(breakdownSearchRequestDTO,esquema);
        } catch (Exception ex){
            throw new ImpresionNotExistException();
        }
        DesgloseDTO desgloseDTO = new DesgloseDTO();
        desgloseDTO.setCodCliente(dto.getCodCliente());
        desgloseDTO.setCodTipoDes(dto.getCodTipoDes().toString());
        desgloseDTO.setCodSubtipoDes(dto.getCodSubtipoDes().toString());
        desgloseDTO.setCodMoneda(dto.getCodMoneda());
        desgloseDTO.setCodPlantilla(dto.getCodTipoPlantilla().toString());
        NewCommonPK id = NewCommonPK.newInstance(desgloseDTO);
        desgloseDTO = desgloseService.findOne(id,esquema).get();

        List<DescripcionCodigoDTO> listaMonedas = descripcionCodigoService.findAllByCodTabla(CODTABLA_TIPO_MONEDAS,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaTiposDesglose = descripcionCodigoService.findAllByCodTabla(CODTABLA_TIPO_DESGLOSE,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaSubtipoDesglose = descripcionCodigoService.findAllByCodTabla(CODTABLA_SUBTIPO_DESGLOSE,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaPlantillasDesglose = descripcionCodigoService.findAllByCodTabla(CODTABLA_PLANTILLAS_DESGLOSE,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaUnidadesM = descripcionCodigoService.findAllByCodTabla(CODTABLA_UNIDAD_MONETARIA,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaRespuestasAuditor = descripcionCodigoService.findAllByCodTabla(CODTABLA_TIPO_RESPUESTA_AUDITOR,0,"codGenerico,ASC");
        List<ClienteDTO> clienteDTO = clienteService.findByRut(dto.getCodCliente(), dto.getCodTipoDes(), dto.getCodSubtipoDes(), dto.getCodMoneda(), dto.getCodTipoPlantilla(),esquema);

        ConceptoRequestDTO conceptoRequestDTO = new ConceptoRequestDTO();
        conceptoRequestDTO.templateCode = dto.getCodTipoPlantilla();
        List<String> conceptosTypesCodes = new LinkedList<>();
        conceptosTypesCodes.add(COD_TIPO_CONCEP_BALANCE);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_GEN_FONDOS);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_IND_FINAN);
        List<String> orderByFiels = new LinkedList<>();
        orderByFiels.add(FIELD_NAME_COD_TIPO_CONCEP);
        orderByFiels.add(FIELD_NAME_NUM_SECUENCIA);
        conceptoRequestDTO.conceptTypeCodesIn = conceptosTypesCodes;
        conceptoRequestDTO.orderByFields = orderByFiels;
        conceptoRequestDTO.orderByDirection = "ASC";
        conceptoRequestDTO.flgConActivo = COD_REG_STATUS_ONE;
        conceptoRequestDTO.columnLetterGlsFormulaVal1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaVal2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaVal3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaVal4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaDef1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaDef2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaDef3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaDef4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaPrc1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaPrc2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaPrc3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaPrc4 = COLUMN_NUMBER_GLS_FORMULA__4;
        List<ConceptoDTO> conceptoDTOList = conceptoService.findByTemplateAndCodConceptsIn(conceptoRequestDTO,null).conceptoDTOList;

        if(dto.getImpresionRapida()) {
            Integer integerList[] = new Integer[responseDTO.extraDataDTO.fecPeriodo.size()];
            for(int i = 0; i< responseDTO.extraDataDTO.fecPeriodo.size(); i++ ){
                integerList[i] = responseDTO.extraDataDTO.fecPeriodo.size()-(i+1);
            }
            dto.setActivePeriodsIndexes(integerList);
            dto.setDeflactorBase(clienteDTO.get(0).getBreakdownSearchResponseDTO().valDeflacBase);
            dto.setFecDeflacBase(clienteDTO.get(0).getBreakdownSearchResponseDTO().fecDeflacBase.toLocalDate());
            BigDecimal deflactores[] = new BigDecimal[integerList.length];
            for(int i = 0; i< integerList.length ;i++) {
                deflactores[i] = responseDTO.extraDataDTO.deflatorPeriod.get(integerList[i]);
            }
            dto.setDeflactores(deflactores);
            BigDecimal cambios[] = new BigDecimal[integerList.length];
            for(int i = 0; i< integerList.length ;i++) {
                cambios[i] = new BigDecimal(1);
            }
            dto.setTiposCambio(cambios);
        }
        // llenamos el reporte de resumen

        List<ActividadEconomicaDTO> actividadEconomicaDTOList = actividadEconomicaService.getAllEconomicActivities();
        List<SectorDTO> sectorDTOList = sectorService.getAllSectors();
        List<SubSectorDTO> subSectorDTOList = subSectorService.getAllSubSectors();
        String actividadEconomica = "";
        String sector = "";
        String subSector = "";
        String moneda = "";
        String monedaImpresion = "";
        String monedaImpresionCorta = "";
        String tipoDesglose = "";
        String subTipoDesglose = "";
        String tipoPlantilla = "";
        String unidadMonetaria = "";
        String iva = "";
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaMonedas) {
            if(responseDTO.codMoneda.equals(descripcionCodigoDTO.getCodGenerico())) {
                moneda = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaMonedas) {
            if(dto.getCodMonedaImpresion().equals(descripcionCodigoDTO.getCodGenerico())) {
                monedaImpresion = descripcionCodigoDTO.getGlsGenericalarga();
                monedaImpresionCorta = descripcionCodigoDTO.getGlsGenericacorta();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaUnidadesM) {
            if(responseDTO.codUnidMoneda.equals(descripcionCodigoDTO.getCodGenerico())) {
                unidadMonetaria = descripcionCodigoDTO.getGlsGenericacorta();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaTiposDesglose) {
            if(responseDTO.codTipoDes.equals(descripcionCodigoDTO.getCodGenerico())) {
                tipoDesglose = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaSubtipoDesglose) {
            if(responseDTO.codSubtipoDes.equals(descripcionCodigoDTO.getCodGenerico())) {
                subTipoDesglose = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaPlantillasDesglose) {
            if(responseDTO.codPlantilla.equals(descripcionCodigoDTO.getCodGenerico())) {
                tipoPlantilla = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (ActividadEconomicaDTO actividadEconomicaDTO :actividadEconomicaDTOList) {
            if(clienteDTO.get(0).getActivity()!=null) {
                if (clienteDTO.get(0).getActivity().equals(actividadEconomicaDTO.getCodActividad())) {
                    actividadEconomica = actividadEconomicaDTO.getGlsActividad();
                    clienteDTO.get(0).setSector(actividadEconomicaDTO.getCodSector());
                    clienteDTO.get(0).setSubSector(actividadEconomicaDTO.getCodSubsector());
                }
            }
        }
        for (SectorDTO sectorDTO:sectorDTOList) {
            if(clienteDTO.get(0).getSector()!=null) {
                if (clienteDTO.get(0).getSector().equals(sectorDTO.getCodSector())) {
                    sector = sectorDTO.getGlsSector();
                }
            }
        }
        for (SubSectorDTO subSectorDTO:subSectorDTOList) {
            if(clienteDTO.get(0).getSubSector()!=null) {
                if (clienteDTO.get(0).getSubSector().equals(subSectorDTO.getCodSubSector())) {
                    subSector = subSectorDTO.getGlsSubSector();
                }
            }
        }
        if(clienteDTO.get(0).getBreakdownSearchResponseDTO().valIva!=null) {
            iva = clienteDTO.get(0).getBreakdownSearchResponseDTO().valIva.toString();
        }

        PdfSummaryReportDTO pdfSummaryReportDTO = new PdfSummaryReportDTO();
        pdfSummaryReportDTO.setFileName("reporteResumen");
        pdfSummaryReportDTO.setRutCliente(formatearRUT(clienteDTO.get(0).getRut()));

        char[] caracteresa = null;
        if(!actividadEconomica.isEmpty()) {
            caracteresa = actividadEconomica.toLowerCase().toCharArray();
            caracteresa[0] = Character.toUpperCase(caracteresa[0]);
            for (int i = 0; i < actividadEconomica.length() - 2; i++) {
                if (caracteresa[i] == ' ' || caracteresa[i] == '.' || caracteresa[i] == ',') {
                    caracteresa[i + 1] = Character.toUpperCase(caracteresa[i + 1]);
                }
            }
        }
        URL imagen = this.getClass().getClassLoader().getResource("jasper_report/templates/Logo_Bice_Reporte.gif");

        pdfSummaryReportDTO.setImgLogoBancoBice(imagen);
        if(caracteresa!= null) {
            pdfSummaryReportDTO.setActividad(new String(caracteresa));
        } else {
            pdfSummaryReportDTO.setActividad("");
        }
        pdfSummaryReportDTO.setSector(sector);
        pdfSummaryReportDTO.setSubSector(subSector);
        pdfSummaryReportDTO.setCodCliente(dto.getCodCliente());
        pdfSummaryReportDTO.setIva(iva);
        pdfSummaryReportDTO.setTipoPlantillaDesglose(tipoPlantilla);
        pdfSummaryReportDTO.setTipoDesglose(tipoDesglose);
        pdfSummaryReportDTO.setSubTipoDesglose(subTipoDesglose);
        pdfSummaryReportDTO.setMoneda(monedaImpresion);
        pdfSummaryReportDTO.setCodMoneda(dto.getCodMoneda());
        pdfSummaryReportDTO.setCodTipoPlantilla(dto.getCodTipoPlantilla().toString());
        pdfSummaryReportDTO.setCodTipoDes(dto.getCodTipoDes().toString());
        pdfSummaryReportDTO.setCodSubtipoDes(dto.getCodSubtipoDes().toString());
        pdfSummaryReportDTO.setCodMoneda(dto.getCodMoneda());
        pdfSummaryReportDTO.setActualizadoAl(LocalDateTime.now().format(Constants.FORMATTER_DATE_SHORT));
        pdfSummaryReportDTO.setMontosEn(unidadMonetaria+monedaImpresionCorta);
        char[] caracteres = null;
        if(!clienteDTO.get(0).getName().isEmpty()) {
            caracteres = clienteDTO.get(0).getName().toLowerCase().toCharArray();
            caracteres[0] = Character.toUpperCase(caracteres[0]);
            for (int i = 0; i < clienteDTO.get(0).getName().length() - 2; i++) {
                if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',') {
                    caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
                }
            }
        }
        if(caracteres!= null) {
            pdfSummaryReportDTO.setNombreCliente(new String(caracteres));
        } else {
            pdfSummaryReportDTO.setNombreCliente("");
        }
        pdfSummaryReportDTO.setNombreUsuario(user.getFullName());
        pdfSummaryReportDTO.setBeforeLastDate(LocalDateTime.now().format(Constants.FORMATTER_DATE_SHORT));
        pdfSummaryReportDTO.setLastDate(LocalDateTime.now().format(Constants.FORMATTER_DATE_SHORT));
        pdfSummaryReportDTO.setFechaModificacion(LocalDateTime.now().format(Constants.FORMATTER_DATE_SHORT));

        if(desgloseDTO.getNomUserModif()!= null) {
            TurbineUserDTO turbineUserDTO =  this.turbineUserService.findByLoginName(desgloseDTO.getNomUserModif());
            String name = "";
            if(turbineUserDTO!=null) {
                name = turbineUserDTO.getFullName();
            } else {
                name = desgloseDTO.getNomUserModif();
            }
            char[] userModif = null;
            if(!name.isEmpty()) {
                userModif = name.toLowerCase().toCharArray();
                userModif[0] = Character.toUpperCase(userModif[0]);
                for (int i = 0; i < name.length() - 2; i++) {
                    if (userModif[i] == ' ' || userModif[i] == '.' || userModif[i] == ',') {
                        userModif[i + 1] = Character.toUpperCase(userModif[i + 1]);
                    }
                }
            }
            if(userModif!=null) {
                pdfSummaryReportDTO.setEjecutor(new String(userModif));
            } else {
                pdfSummaryReportDTO.setEjecutor("");
            }

        }
        if(desgloseDTO.getFecUserModif()!= null) {
            pdfSummaryReportDTO.setEjecutorFechaModificacion(desgloseDTO.getFecUserModif().format(Constants.FORMATTER_DATE_SHORT));
        }
        if(desgloseDTO.getNomRevisorDef()!=null) {
            char[] revisorDef = null;
            if(!desgloseDTO.getNomRevisorDef().isEmpty()) {
                revisorDef = desgloseDTO.getNomRevisorDef().toLowerCase().toCharArray();
                revisorDef[0] = Character.toUpperCase(revisorDef[0]);
                for (int i = 0; i < desgloseDTO.getNomRevisorDef().length() - 2; i++) {
                    if (revisorDef[i] == ' ' || revisorDef[i] == '.' || revisorDef[i] == ',') {
                        revisorDef[i + 1] = Character.toUpperCase(revisorDef[i + 1]);
                    }
                }
            }
            if(revisorDef!=null) {
                pdfSummaryReportDTO.setRevisor(new String(revisorDef));
            } else {
                pdfSummaryReportDTO.setRevisor("");
            }

        }
        if(desgloseDTO.getFecDesgloDef()!= null) {
            pdfSummaryReportDTO.setRevisorFechaModificacion(desgloseDTO.getFecDesgloDef().format(Constants.FORMATTER_DATE_SHORT));
        }


        // pdfSummaryReportDTO.setNombreUsuario();
        // llenamos los auditores de los periodos visibles
        for (int i = 0; i < responseDTO.extraDataDTO.auditors.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.auditors.get(i).otherAuditorName != null) {
                        switch (l) {
                            case 0:
                                pdfSummaryReportDTO.setAuditor1(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName!=null?
                                    responseDTO.extraDataDTO.auditors.get(i).otherAuditorName:"");
                                break;
                            case 1:
                                pdfSummaryReportDTO.setAuditor2(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName!=null?
                                    responseDTO.extraDataDTO.auditors.get(i).otherAuditorName:"");
                                break;
                            case 2:
                                pdfSummaryReportDTO.setAuditor3(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName!=null?
                                    responseDTO.extraDataDTO.auditors.get(i).otherAuditorName:"");
                                break;
                            case 3:
                                pdfSummaryReportDTO.setAuditor4(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName!=null?
                                    responseDTO.extraDataDTO.auditors.get(i).otherAuditorName:"");
                                break;
                        }
                    } else {
                        if (responseDTO.extraDataDTO.auditors.get(i).auditor != null) {
                            switch (l) {
                                case 0:
                                    pdfSummaryReportDTO.setAuditor1(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga()!=null?
                                        responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga():"");
                                    break;
                                case 1:
                                    pdfSummaryReportDTO.setAuditor2(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga()!=null?
                                        responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga():"");
                                    break;
                                case 2:
                                    pdfSummaryReportDTO.setAuditor3(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga()!=null?
                                        responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga():"");
                                    break;
                                case 3:
                                    pdfSummaryReportDTO.setAuditor4(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga()!=null?
                                        responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga():"");
                                    break;
                            }
                        }
                    }
                }
            }
        }

        // fechas de los periodos visibles
        for (int i = 0; i < responseDTO.extraDataDTO.fecPeriodo.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.fecPeriodo != null) {
                        switch (l) {
                            case 0:
                                pdfSummaryReportDTO.setFecPerido1(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                pdfSummaryReportDTO.setFecCompletaPerido1(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO));
                                break;
                            case 1:
                                pdfSummaryReportDTO.setFecPerido2(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                pdfSummaryReportDTO.setFecCompletaPerido2(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO));
                                break;
                            case 2:
                                pdfSummaryReportDTO.setFecPerido3(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                pdfSummaryReportDTO.setFecCompletaPerido3(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO));
                                break;
                            case 3:
                                pdfSummaryReportDTO.setFecPerido4(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                pdfSummaryReportDTO.setFecCompletaPerido4(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO));
                                break;
                        }
                    }
                }
            }
        }
        // meses de los periodos visibles

        for (int i = 0; i < responseDTO.extraDataDTO.monthsPeriod.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.monthsPeriod != null) {
                        switch (l) {
                            case 0:
                                pdfSummaryReportDTO.setNumMeses1(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                            case 1:
                                pdfSummaryReportDTO.setNumMeses2(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                            case 2:
                                pdfSummaryReportDTO.setNumMeses3(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                            case 3:
                                pdfSummaryReportDTO.setNumMeses4(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                        }
                    }
                }
            }
        }
        // tipo de respuesta del auditor
        for (int i = 0; i < responseDTO.extraDataDTO.periodRating.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.monthsPeriod != null) {
                        String tipoRespuesta = "";
                        for (DescripcionCodigoDTO descripcionCodigoDTO:listaRespuestasAuditor) {
                            if(responseDTO.extraDataDTO.periodRating.get(i)!=null && responseDTO.extraDataDTO.periodRating.get(i).toString().equals(descripcionCodigoDTO.getCodGenerico())){
                                tipoRespuesta = descripcionCodigoDTO.getGlsGenericalarga();
                            }
                        }
                        switch (l) {
                            case 0:
                                pdfSummaryReportDTO.setCalificacion1(tipoRespuesta);
                                break;
                            case 1:
                                pdfSummaryReportDTO.setCalificacion2(tipoRespuesta);
                                break;
                            case 2:
                                pdfSummaryReportDTO.setCalificacion3(tipoRespuesta);
                                break;
                            case 3:
                                pdfSummaryReportDTO.setCalificacion4(tipoRespuesta);
                                break;
                        }
                    }
                }
            }
        }
        // table values DTO
        List<TableValueDTO> tableValueDTOS = new ArrayList<>();
        for (ConceptoDTO conceptoDTO : conceptoDTOList) {
            TableValueDTO tableValueDTO = new TableValueDTO();
            tableValueDTO.setCodTipoConcepto(conceptoDTO.getCodTipoConcep()!=null?conceptoDTO.getCodTipoConcep():"");
            tableValueDTO.setConceptName(conceptoDTO.getGlsConcepto());
            tableValueDTO.setCorConcept(conceptoDTO.getCodConcepto() != null ? conceptoDTO.getCodConcepto().toString() : "");
            tableValueDTO.setCorConcept1(conceptoDTO.getCorConcepto() != null ? conceptoDTO.getCorConcepto() : null);
            tableValueDTO.setFlgIsBold(conceptoDTO.getFlgIsBold());
            tableValueDTO.setFlgIsEnclosed(conceptoDTO.getFlgIsEnclosed());
            tableValueDTO.setFlgIsImpreso(conceptoDTO.getFlgIsPrinted());
            tableValueDTO.setFlgNonegativ(conceptoDTO.getFlgNonegativ());
            tableValueDTO.setFlgIsTotal(conceptoDTO.getFlgIsTotal());
            if(conceptoDTO.getCodTipoRegis()!=null) {
                tableValueDTO.setCodTipoRegis(conceptoDTO.getCodTipoRegis());
            }
            if(conceptoDTO.getNumDecPrint()!= null) {
                tableValueDTO.setNumDecPrint(conceptoDTO.getNumDecPrint());
            } else {
                tableValueDTO.setNumDecPrint(0);
            }
            if (pdfSummaryReportDTO.getFecPerido1() != null) {
                tableValueDTO.setPeriod1Fecha(LocalDate.parse(pdfSummaryReportDTO.getFecPerido1(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
            }
            if (pdfSummaryReportDTO.getFecPerido2() != null) {
                tableValueDTO.setPeriod2Fecha(LocalDate.parse(pdfSummaryReportDTO.getFecPerido2(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
            }
            if (pdfSummaryReportDTO.getFecPerido3() != null) {
                tableValueDTO.setPeriod3Fecha(LocalDate.parse(pdfSummaryReportDTO.getFecPerido3(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
            }
            if (pdfSummaryReportDTO.getFecPerido4() != null) {
                tableValueDTO.setPeriod4Fecha(LocalDate.parse(pdfSummaryReportDTO.getFecPerido4(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
            }
            tableValueDTOS.add(tableValueDTO);
        }
        for (ConceptoDTO conceptoDTO : conceptoDTOList) {
            for (ConceptoImpresionDTO conceptoImpresionDTO:responseDTO.extraDataDTO.matrizConImp) {
                if(conceptoDTO.getCorConcepto()!=null) {
                    if (conceptoDTO.getCodTipoConcep().equals(conceptoImpresionDTO.getCodTipoConcep()) && conceptoDTO.getCorConcepto().toString().equals(conceptoImpresionDTO.getCorConcepto())) {
                        conceptoImpresionDTO.setCodConcepto(conceptoDTO.getCodConcepto());
                    }
                }
            }
        }
        // todo: en caso de multiregistro se aplica algoritmo para determinar el numero de filas
        List<TableValueDTO> clonTableValueDTOS = new LinkedList<>();
        for (TableValueDTO conceptoDTO : tableValueDTOS) {
            conceptoDTO.setCorValor(0);
            if(conceptoDTO.getCodTipoRegis()!= null && conceptoDTO.getCodTipoRegis().equals(MULTI_REGISTRY)) {
                conceptoDTO.setCorValor(1);
            }
            clonTableValueDTOS.add(conceptoDTO);
            Integer multiregistros = 1;
            if(conceptoDTO.getCodTipoConcepto()!= null && conceptoDTO.getCorConcept1()!= null) {
                if(conceptoDTO.getCodTipoRegis()!= null && conceptoDTO.getCodTipoRegis().equals(MULTI_REGISTRY)){
                    if(esquema== null || esquema.equals(ESQUEMA_DGL)) {
                        multiregistros = valPeriodoDesgloseRepository.numberMultiregistros(desgloseDTO.getCodCliente(),Integer.valueOf(desgloseDTO.getCodTipoDes()),
                            Integer.valueOf(desgloseDTO.getCodSubtipoDes()),desgloseDTO.getCodMoneda(),Integer.valueOf(desgloseDTO.getCodPlantilla()),conceptoDTO.getCorConcept1());
                    }
                    if(esquema!= null && esquema.equals(ESQUEMA_CIF)) {
                        multiregistros = valPeriodoDesgloseCifRepository.numberMultiregistros(desgloseDTO.getCodCliente(),Integer.valueOf(desgloseDTO.getCodTipoDes()),
                            Integer.valueOf(desgloseDTO.getCodSubtipoDes()),desgloseDTO.getCodMoneda(),Integer.valueOf(desgloseDTO.getCodPlantilla()),conceptoDTO.getCorConcept1());
                    }
                }
            }
            if(multiregistros>1) {
                for(int i = 1; i< multiregistros; i++) {
                    TableValueDTO tableValueDTO = new TableValueDTO();
                    tableValueDTO.setCorValor(i+1);
                    tableValueDTO.setCorConcept1(conceptoDTO.getCorConcept1());
                    tableValueDTO.setCorConcept(conceptoDTO.getCorConcept());
                    tableValueDTO.setCodTipoRegis(conceptoDTO.getCodTipoRegis());
                    tableValueDTO.setFlgIsTotal(conceptoDTO.getFlgIsTotal());
                    tableValueDTO.setFlgNonegativ(conceptoDTO.getFlgNonegativ());
                    tableValueDTO.setTotal(conceptoDTO.getTotal());
                    tableValueDTO.setHeader(conceptoDTO.getHeader());
                    tableValueDTO.setPeriod4Fecha(conceptoDTO.getPeriod4Fecha());
                    tableValueDTO.setPeriod3Fecha(conceptoDTO.getPeriod3Fecha());
                    tableValueDTO.setPeriod2Fecha(conceptoDTO.getPeriod2Fecha());
                    tableValueDTO.setPeriod1Fecha(conceptoDTO.getPeriod1Fecha());
                    tableValueDTO.setFlgIsImpreso(conceptoDTO.getFlgIsImpreso());
                    tableValueDTO.setFlgIsEnclosed(conceptoDTO.getFlgIsEnclosed());
                    tableValueDTO.setFlgIsBold(conceptoDTO.getFlgIsBold());
                    tableValueDTO.setCodTipoConcepto(conceptoDTO.getCodTipoConcepto());
                    tableValueDTO.setConceptName(conceptoDTO.getConceptName());
                    tableValueDTO.setPeriod4Value(conceptoDTO.getPeriod4Value());
                    tableValueDTO.setPeriod4Percent(conceptoDTO.getPeriod4Percent());
                    tableValueDTO.setPeriod3Value(conceptoDTO.getPeriod3Value());
                    tableValueDTO.setPeriod3Percent(conceptoDTO.getPeriod3Percent());
                    tableValueDTO.setPeriod2Value(conceptoDTO.getPeriod2Value());
                    tableValueDTO.setPeriod2Percent(conceptoDTO.getPeriod2Percent());
                    tableValueDTO.setPeriod1Value(conceptoDTO.getPeriod1Value());
                    tableValueDTO.setPeriod1Percent(conceptoDTO.getPeriod1Percent());
                    tableValueDTO.setNumDecPrint(conceptoDTO.getNumDecPrint());
                    clonTableValueDTOS.add(tableValueDTO);
                }
            }
            // aqui logica de creacion de nuevos conceptos de multiregistro
        }

        tableValueDTOS = null;
        tableValueDTOS = clonTableValueDTOS;
        int indexPeriod = 0;
        for (PeriodoDTO periodo : responseDTO.extraDataDTO.periodos) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (indexPeriod == dto.getActivePeriodsIndexes()[l]) {
                    for (ValPeriodoDesgloseDTO valPeriodoDesgloseDTO : periodo.valPeriodoDesgloseDTOList) {
                        for (TableValueDTO conceptoDTO : tableValueDTOS) {
                            String zeros = "";
                            if(conceptoDTO.getNumDecPrint()!=null) {
                                for (int i = 0; i < Integer.valueOf(conceptoDTO.getNumDecPrint()); i++) {
                                    zeros = zeros + "0";
                                }
                            }
                            DecimalFormat formato = new DecimalFormat("#0."+ zeros.trim());
                            String formatoPos = "#,##0."+ zeros.trim();
                            String formatoNeg = "-#,##0."+ zeros.trim();
                            if(zeros.trim().isEmpty()) {
                                 formato = new DecimalFormat("#0");
                                 formatoPos = "#,##0";
                                 formatoNeg = "-#,##0";
                            }
                            conceptoDTO.setFormatPathern(formatoPos + ";" + formatoNeg);
                        if (valPeriodoDesgloseDTO.getCodTipoConcep() != null) {
                                    if (valPeriodoDesgloseDTO.getCodTipoConcep()!=null && conceptoDTO.getCodTipoConcepto()!=null && valPeriodoDesgloseDTO.getCodTipoConcep().trim().equals(conceptoDTO.getCodTipoConcepto().trim()) ) {
                                        if (valPeriodoDesgloseDTO.getCorConcepto()!=null && conceptoDTO.getCorConcept1()!=null && valPeriodoDesgloseDTO.getCorConcepto().toString().equals(conceptoDTO.getCorConcept1().toString())) {
                                            if(valPeriodoDesgloseDTO.getCorValor() == conceptoDTO.getCorValor()) {
                                                conceptoDTO.setConceptName(valPeriodoDesgloseDTO.getGlsConcepto() != null ? valPeriodoDesgloseDTO.getGlsConcepto() : conceptoDTO.getConceptName());
                                                if (dto.getCodMonedaImpresion().equals(dto.getCodMoneda())) {
                                                    switch (l) {
                                                        case 0:
                                                            if (dto.getDeflactores()[0] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod1Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        BigDecimal calculo1 = dto.getDeflactorBase().divide(dto.getDeflactores()[0], 6, RoundingMode.HALF_UP);
                                                                        conceptoDTO.setPeriod1Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo1)
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod1Value(conceptoDTO.getPeriod1Value());
                                                                        conceptoDTO.setPeriod1Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod1Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        case 1:
                                                            if (dto.getDeflactores()[1] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod2Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        BigDecimal calculo2 = dto.getDeflactorBase().divide(dto.getDeflactores()[1], 6, RoundingMode.HALF_UP);
                                                                        conceptoDTO.setPeriod2Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo2)
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod2Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod2Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        case 2:
                                                            if (dto.getDeflactores()[2] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod3Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        BigDecimal calculo3 = dto.getDeflactorBase().divide(dto.getDeflactores()[2], 6, RoundingMode.HALF_UP);
                                                                        conceptoDTO.setPeriod3Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo3)
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod3Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod3Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        case 3:
                                                            if (dto.getDeflactores()[3] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod4Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        BigDecimal calculo4 = dto.getDeflactorBase().divide(dto.getDeflactores()[3], 6, RoundingMode.HALF_UP);
                                                                        conceptoDTO.setPeriod4Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo4)
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod4Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod4Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                    }
                                                } else {
                                                    switch (l) {
                                                        case 0:
                                                            if (dto.getTiposCambio()[0] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod1Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        conceptoDTO.setPeriod1Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[0])
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod1Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod1Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        case 1:
                                                            if (dto.getTiposCambio()[1] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod2Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        conceptoDTO.setPeriod2Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[1])
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod2Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod2Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        case 2:
                                                            if (dto.getTiposCambio()[2] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod3Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        conceptoDTO.setPeriod3Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[2])
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod3Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod3Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        case 3:
                                                            if (dto.getTiposCambio()[3] != null) {
                                                                if(valPeriodoDesgloseDTO.getFecPeriodo().format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(conceptoDTO.getPeriod4Fecha())) {
                                                                    if (conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) || conceptoDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT)) {
                                                                        conceptoDTO.setPeriod4Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[3])
                                                                            .setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                        conceptoDTO.setPeriod4Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                                    } else {
                                                                        conceptoDTO.setPeriod4Value(new BigDecimal(formato.format(
                                                                            valPeriodoDesgloseDTO.getValConcepto().setScale(conceptoDTO.getNumDecPrint(), RoundingMode.HALF_UP)).replace(",",".")));
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                    }
                                                }
                                            }
                                            }
                                        }
                                }
                        }
                    }
                }
            }
            indexPeriod++;
        }
        List<TableValueDTO> tableValueBalance = new ArrayList<>();
        List<TableValueDTO> tableValueEstadoResultados = new ArrayList<>();
        List<TableValueDTO> tableValueGeneracionFondos = new ArrayList<>();
        List<TableValueDTO> tableValueGeneracionFondosCopia = new ArrayList<>();
        List<TableValueDTO> tableValueIndicadoresFinancieros = new ArrayList<>();
        for (TableValueDTO tableValueDTO:tableValueDTOS) {
            if(tableValueDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_BALANCE) && tableValueDTO.getCorConcept1()!=null) {
                if(!tableValueDTO.getCorConcept().contains(".") && (tableValueDTO.getFlgIsImpreso()== 1 || tableValueDTO.getFlgIsImpreso()== 2)) {
                    if(tableValueDTO.getFlgIsImpreso()== 1 ) {
                        tableValueBalance.add(tableValueDTO);
                    } else {
                        if (tableValueDTO.getFlgIsImpreso() == 2) { // aparicion condicional
                            if ((tableValueDTO.getPeriod1Value() == null || tableValueDTO.getPeriod1Value().equals(0)) && (tableValueDTO.getPeriod2Value() == null || tableValueDTO.getPeriod2Value().equals(0)) && (tableValueDTO.getPeriod3Value() == null || tableValueDTO.getPeriod3Value().equals(0)) && (
                                tableValueDTO.getPeriod4Value() == null || tableValueDTO.getPeriod4Value().equals(0))) {

                            } else {
                                tableValueBalance.add(tableValueDTO);
                            }
                        }
                    }

                }
            }
            if(tableValueDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_EST_RESULT) && tableValueDTO.getCorConcept1()!=null) {
                if(!tableValueDTO.getCorConcept().contains(".") && (tableValueDTO.getFlgIsImpreso()== 1 || tableValueDTO.getFlgIsImpreso()== 2)) {
                    if(tableValueDTO.getFlgIsImpreso()== 1 ) {
                        tableValueEstadoResultados.add(tableValueDTO);
                    } else {
                        if (tableValueDTO.getFlgIsImpreso() == 2) {
                            if ((tableValueDTO.getPeriod1Value() == null || tableValueDTO.getPeriod1Value().equals(0)) && (tableValueDTO.getPeriod2Value() == null || tableValueDTO.getPeriod2Value().equals(0)) && (tableValueDTO.getPeriod3Value() == null || tableValueDTO.getPeriod3Value().equals(0)) && (
                                tableValueDTO.getPeriod4Value() == null || tableValueDTO.getPeriod4Value().equals(0))) {

                            } else {
                                tableValueEstadoResultados.add(tableValueDTO);
                            }
                        }
                    }
                }
            }
            if(tableValueDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_GEN_FONDOS) && tableValueDTO.getCorConcept1()!=null && (tableValueDTO.getFlgIsImpreso()== 1 || tableValueDTO.getFlgIsImpreso()== 2)) {
                if(tableValueDTO.getFlgIsImpreso()== 1 ) {
                    tableValueGeneracionFondos.add(tableValueDTO);
                } else {
                    if (tableValueDTO.getFlgIsImpreso() == 2) {
                        if ((tableValueDTO.getPeriod1Value() == null || tableValueDTO.getPeriod1Value().equals(0)) && (tableValueDTO.getPeriod2Value() == null || tableValueDTO.getPeriod2Value().equals(0)) && (tableValueDTO.getPeriod3Value() == null || tableValueDTO.getPeriod3Value().equals(0)) && (
                            tableValueDTO.getPeriod4Value() == null || tableValueDTO.getPeriod4Value().equals(0))) {

                        } else {
                            tableValueGeneracionFondos.add(tableValueDTO);
                        }
                    }
                }
            }
            if(tableValueDTO.getCodTipoConcepto().equals(COD_TIPO_CONCEP_IND_FINAN) && tableValueDTO.getCorConcept1()!=null && (tableValueDTO.getFlgIsImpreso()== 1 || tableValueDTO.getFlgIsImpreso()== 2) ) {
                if(tableValueDTO.getFlgIsImpreso()== 1 ) {
                    tableValueIndicadoresFinancieros.add(tableValueDTO);
                } else {
                    if (tableValueDTO.getFlgIsImpreso() == 2) {
                        if ((tableValueDTO.getPeriod1Value() == null || tableValueDTO.getPeriod1Value().equals(0)) && (tableValueDTO.getPeriod2Value() == null || tableValueDTO.getPeriod2Value().equals(0)) && (tableValueDTO.getPeriod3Value() == null || tableValueDTO.getPeriod3Value().equals(0)) && (
                            tableValueDTO.getPeriod4Value() == null || tableValueDTO.getPeriod4Value().equals(0))) {

                        } else {
                            tableValueIndicadoresFinancieros.add(tableValueDTO);
                        }
                    }
                }
            }
        }

        // recorremos la tabla
        for (TableValueDTO tableValueDTO:tableValueBalance) {
            tableValueDTO.setHiddeRow(false);
            if(tableValueDTO.getCorConcept1()!=null) {
                if (tableValueDTO.getPeriod1Percent()!= null && tableValueDTO.getPeriod1Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod1Percent(null);
                }
                if (tableValueDTO.getPeriod2Percent()!=null && tableValueDTO.getPeriod2Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod2Percent(null);
                }
                if (tableValueDTO.getPeriod3Percent()!=null && tableValueDTO.getPeriod3Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod3Percent(null);
                }
                if (tableValueDTO.getPeriod4Percent()!= null && tableValueDTO.getPeriod4Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod4Percent(null);
                }
            }
        }
        for (TableValueDTO tableValueDTO:tableValueEstadoResultados) {
            tableValueDTO.setHiddeRow(false);
            if(tableValueDTO.getCorConcept1()!=null) {
                if (tableValueDTO.getPeriod1Percent()!= null && tableValueDTO.getPeriod1Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod1Percent(null);
                }
                if (tableValueDTO.getPeriod2Percent()!=null && tableValueDTO.getPeriod2Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod2Percent(null);
                }
                if (tableValueDTO.getPeriod3Percent()!=null && tableValueDTO.getPeriod3Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod3Percent(null);
                }
                if (tableValueDTO.getPeriod4Percent()!= null && tableValueDTO.getPeriod4Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod4Percent(null);
                }
            }
        }
        for (TableValueDTO tableValueDTO:tableValueIndicadoresFinancieros) {
            tableValueDTO.setHiddeRow(false);
            if(tableValueDTO.getCorConcept1()!=null) {
                if (tableValueDTO.getPeriod1Percent()!= null && tableValueDTO.getPeriod1Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod1Percent(null);
                }
                if (tableValueDTO.getPeriod2Percent()!=null && tableValueDTO.getPeriod2Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod2Percent(null);
                }
                if (tableValueDTO.getPeriod3Percent()!=null && tableValueDTO.getPeriod3Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod3Percent(null);
                }
                if (tableValueDTO.getPeriod4Percent()!= null && tableValueDTO.getPeriod4Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod4Percent(null);
                }
            }
        }
        Integer correlativo = 0;
        Integer cuentaNulos = 0;
        Integer index = 0;
        for (TableValueDTO tableValueDTO:tableValueGeneracionFondos) {
            tableValueDTO.setHiddeRow(false);
            if(tableValueDTO.getConceptName().equals(ESTADO_DE_FLUJO_DE_EFECTIVO) ) {
                correlativo = Integer.valueOf(tableValueDTO.getCorConcept1() );
            }
            if(correlativo> 0) {
                if (Integer.valueOf(tableValueDTO.getCorConcept1()) > correlativo) {
                    if (tableValueDTO.getPeriod1Value() == null && tableValueDTO.getPeriod2Value() == null && tableValueDTO.getPeriod3Value() == null
                        && tableValueDTO.getPeriod4Value() == null) {
                        cuentaNulos = cuentaNulos + 1;
                    }
                    index++;
                }
            }
            if(tableValueDTO.getCorConcept1()!=null) {
                if (tableValueDTO.getPeriod1Percent()!= null && tableValueDTO.getPeriod1Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod1Percent(null);
                }
                if (tableValueDTO.getPeriod2Percent()!=null && tableValueDTO.getPeriod2Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod2Percent(null);
                }
                if (tableValueDTO.getPeriod3Percent()!=null && tableValueDTO.getPeriod3Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod3Percent(null);
                }
                if (tableValueDTO.getPeriod4Percent()!= null && tableValueDTO.getPeriod4Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod4Percent(null);
                }
            }
        }
        for (TableValueDTO tableValueDTO:tableValueGeneracionFondos) {
            if(cuentaNulos == index &&  Integer.valueOf(tableValueDTO.getCorConcept1()) >= correlativo) {
                tableValueDTO.setHiddeRow(true);
            } else {
                tableValueGeneracionFondosCopia.add(tableValueDTO);
            }
        }
        pdfSummaryReportDTO.setDatosBalance(tableValueBalance);

        pdfSummaryReportDTO.setDatosEstadoResultado(tableValueEstadoResultados);
        pdfSummaryReportDTO.setDatosIndicadorFinanciero(tableValueIndicadoresFinancieros);
        pdfSummaryReportDTO.setDatosGenFondFlujEfectivo(tableValueGeneracionFondosCopia);

        String Fecha1=null;
        String Fecha2=null;
        String FechaNota1=null;
        String FechaNota2=null;
        String FechaNotas1=null;
        String FechaNotas2=null;
        String FechaNotasLetra1=null;
        String FechaNotasLetra2=null;
        LocalDate date;

        switch (responseDTO.extraDataDTO.numPeriodosLoaded) {
            case 1:
                FechaNota1 = pdfSummaryReportDTO.getFecPerido1();
                break;
            case 2:
                FechaNota1 = pdfSummaryReportDTO.getFecPerido1();
                FechaNota2 = pdfSummaryReportDTO.getFecPerido2();
                break;
            case 3:
                FechaNota1 = pdfSummaryReportDTO.getFecPerido2();
                FechaNota2 = pdfSummaryReportDTO.getFecPerido3();
                break;
            default:
                FechaNota1 = pdfSummaryReportDTO.getFecPerido3();
                FechaNota2 = pdfSummaryReportDTO.getFecPerido4();
                break;
        }
        // aqui sacamos las ultimas 2 fechas de los periodos consolidados
        List<LocalDate> fechasConsolidados = new LinkedList<>();

        for (EmpConsolPeriodoDesgConsolDTO empConsolPeriodoDesgConsolDTO : responseDTO.extraDataDTO.empConsolPeriodoDesgConsolList) {
            int cuenta = 0;
            if(empConsolPeriodoDesgConsolDTO.fecPeriodo!= null) {
            for (LocalDate fecha : fechasConsolidados) {
                    if (fecha.format(FORMATTER_DATE_SHORT).equals(empConsolPeriodoDesgConsolDTO.fecPeriodo.format(FORMATTER_DATE_SHORT))) {
                        cuenta++;
                    }
                }
            }
            if (cuenta == 0) {
                if(empConsolPeriodoDesgConsolDTO.fecPeriodo!= null) {
                    fechasConsolidados.add(empConsolPeriodoDesgConsolDTO.fecPeriodo);
                }
            }
        }
        Collections.sort(fechasConsolidados);

        if (fechasConsolidados.size() > 0) {
            if (fechasConsolidados.size() > 1) {
                Fecha1 = fechasConsolidados.get(fechasConsolidados.size() - 2).format(FORMATTER_DATE_SHORT_LATINO_CORTO);
                Fecha2 = fechasConsolidados.get(fechasConsolidados.size() - 1).format(FORMATTER_DATE_SHORT_LATINO_CORTO);
            } else {
                Fecha2 = fechasConsolidados.get(0).format(FORMATTER_DATE_SHORT_LATINO_CORTO);
            }
        }
        if (FechaNotas1==null && Fecha1!=null && !Fecha1.isEmpty()) {
            date = LocalDate.parse(Fecha1, FORMATTER_DATE_SHORT_LATINO_CORTO);
            FechaNotas1 = date.format(FORMATTER_DATE_SHORT_LATINO);
        }
        if (FechaNota1!=null ) {
            date = LocalDate.parse(FechaNota1, FORMATTER_DATE_SHORT_LATINO_CORTO);
            FechaNotasLetra1 = date.format(FORMATTER_DATE_SHORT_MES_LETRA);
        }
        if (FechaNotas2==null && Fecha2!=null && !Fecha2.isEmpty()) {
            date = LocalDate.parse(Fecha2, FORMATTER_DATE_SHORT_LATINO_CORTO);
            FechaNotas2 = date.format(FORMATTER_DATE_SHORT_LATINO);
        }
        if (FechaNota2!=null ) {
            date = LocalDate.parse(FechaNota2, FORMATTER_DATE_SHORT_LATINO_CORTO);
            FechaNotasLetra2 = date.format(FORMATTER_DATE_SHORT_MES_LETRA);
        }
        // asignar valores de responseDTO.extraDataDTO.periodos que es una lista
        for (int i = 0; i < responseDTO.extraDataDTO.periodos.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    switch (l){
                        case 0:
                            if(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif()!= null) {
                                pdfSummaryReportDTO.setFechaPeriodoSbif1(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif().format(FORMATTER_DATE_SHORT_LATINO));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig()!= null) {
                                pdfSummaryReportDTO.setDeudaDirectaVigenteSbif1(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig()!=null) {
                                pdfSummaryReportDTO.setDeudaIndirectaVigenteSbif1(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            break;
                        case 1:
                            if(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif()!= null) {
                                pdfSummaryReportDTO.setFechaPeriodoSbif2(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif().format(FORMATTER_DATE_SHORT_LATINO));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig()!= null) {
                                pdfSummaryReportDTO.setDeudaDirectaVigenteSbif2(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig()!= null) {
                                pdfSummaryReportDTO.setDeudaIndirectaVigenteSbif2(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            break;
                        case 2:
                            if(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif()!= null) {
                                pdfSummaryReportDTO.setFechaPeriodoSbif3(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif().format(FORMATTER_DATE_SHORT_LATINO));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig()!= null) {
                                pdfSummaryReportDTO.setDeudaDirectaVigenteSbif3(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig()!= null) {
                                pdfSummaryReportDTO.setDeudaIndirectaVigenteSbif3(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            break;
                        case 3:
                            if(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif()!= null) {
                                pdfSummaryReportDTO.setFechaPeriodoSbif4(responseDTO.extraDataDTO.periodos.get(i).getFecInformSbif().format(FORMATTER_DATE_SHORT_LATINO));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig()!= null) {
                                pdfSummaryReportDTO.setDeudaDirectaVigenteSbif4(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDdVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            if(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig()!= null) {
                                pdfSummaryReportDTO.setDeudaIndirectaVigenteSbif4(responseDTO.extraDataDTO.periodos.get(i).getMtoSbifDiVig().setScale(0, RoundingMode.HALF_UP));
                            }
                            break;
                    }

                }
            }
        }
        if ((pdfSummaryReportDTO.getDeudaDirectaVigenteSbif1() == null || pdfSummaryReportDTO.getDeudaDirectaVigenteSbif1().equals(BigDecimal.ZERO)) && (
            pdfSummaryReportDTO.getDeudaDirectaVigenteSbif2() == null || pdfSummaryReportDTO.getDeudaDirectaVigenteSbif2().equals(BigDecimal.ZERO)) && (
            pdfSummaryReportDTO.getDeudaDirectaVigenteSbif3() == null || pdfSummaryReportDTO.getDeudaDirectaVigenteSbif3().equals(BigDecimal.ZERO)) && (
            pdfSummaryReportDTO.getDeudaDirectaVigenteSbif4() == null || pdfSummaryReportDTO.getDeudaDirectaVigenteSbif4().equals(BigDecimal.ZERO)) && (
            pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif1() == null || pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif1().equals(BigDecimal.ZERO)) && (
            pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif2() == null || pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif2().equals(BigDecimal.ZERO)) && (
            pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif3() == null || pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif3().equals(BigDecimal.ZERO)) && (
            pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif4() == null || pdfSummaryReportDTO.getDeudaIndirectaVigenteSbif4().equals(BigDecimal.ZERO))) {
            pdfSummaryReportDTO.setFechaPeriodoSbif4(null);
            pdfSummaryReportDTO.setFechaPeriodoSbif3(null);
            pdfSummaryReportDTO.setFechaPeriodoSbif2(null);
            pdfSummaryReportDTO.setFechaPeriodoSbif1(null);
            pdfSummaryReportDTO.setImprimirTablaButton(false);
        }else{
            pdfSummaryReportDTO.setImprimirTablaButton(true);
        }
        pdfSummaryReportDTO.setFechaNotasPeriodo1(FechaNotas1);
        pdfSummaryReportDTO.setFechaNotasPeriodo2(FechaNotas2);
        if(FechaNotasLetra1!=null && !FechaNotasLetra1.equals("")) {
            pdfSummaryReportDTO.setFechaNotasLetraPeriodo1(FechaNotasLetra1.substring(0, 1).toUpperCase() + FechaNotasLetra1.substring(1));
        }
        if(FechaNotasLetra2!=null && !FechaNotasLetra2.equals("")) {
            pdfSummaryReportDTO.setFechaNotasLetraPeriodo2(FechaNotasLetra2.substring(0, 1).toUpperCase() + FechaNotasLetra2.substring(1));
        }
        // empresas que consolidan
        if(desgloseDTO.getCodTipoDes().equals(COD_TIPO_DESG_CONSOLIDADO)) {
            int indexEmpConsol = 0;
            List<CompanyValueDTO> companyValueDTOList = new ArrayList<>();
            for (EmpConsolPeriodoDesgConsolDTO empConsolPeriodoDesgConsolDTO : responseDTO.extraDataDTO.empConsolPeriodoDesgConsolList) {
                int existe = 0;
                for (CompanyValueDTO companyValueDTO : companyValueDTOList) {
                    if (companyValueDTO.getCompanyName().equalsIgnoreCase(empConsolPeriodoDesgConsolDTO.nomEmpresa)) {
                        existe = 1;
                    }
                    if (existe == 1) {
                        if(empConsolPeriodoDesgConsolDTO.fecPeriodo!= null) {
                            if (Fecha1 != "" && Fecha1 != null && empConsolPeriodoDesgConsolDTO.fecPeriodo.format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(Fecha1)) {
                                companyValueDTO.setPeriod1Value(empConsolPeriodoDesgConsolDTO.valPrcConsol);
                                if(companyValueDTO.getPeriod1Value().equals(BigDecimal.ZERO)) {
                                    companyValueDTO.setPeriod1Value(null);
                                }
                            }
                            if (Fecha2 != "" && Fecha2 != null && empConsolPeriodoDesgConsolDTO.fecPeriodo.format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(Fecha2)) {
                                companyValueDTO.setPeriod2Value(empConsolPeriodoDesgConsolDTO.valPrcConsol);
                                if(companyValueDTO.getPeriod2Value().equals(BigDecimal.ZERO)) {
                                    companyValueDTO.setPeriod2Value(null);
                                }
                            }
                        }
                    }
                }
                if (existe == 0) {
                    CompanyValueDTO company = new CompanyValueDTO();
                    company.setCompanyName(empConsolPeriodoDesgConsolDTO.nomEmpresa);
                    if(empConsolPeriodoDesgConsolDTO.fecPeriodo!= null) {
                        if (Fecha1 != "" && Fecha1 != null && empConsolPeriodoDesgConsolDTO.fecPeriodo.format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(Fecha1)) {
                            company.setPeriod1Value(empConsolPeriodoDesgConsolDTO.valPrcConsol);
                            if(company.getPeriod1Value().equals(BigDecimal.ZERO)) {
                                company.setPeriod1Value(null);
                            }
                        }
                        if (Fecha2 != "" && Fecha2 != null && empConsolPeriodoDesgConsolDTO.fecPeriodo.format(FORMATTER_DATE_SHORT_LATINO_CORTO).equals(Fecha2)) {
                            company.setPeriod2Value(empConsolPeriodoDesgConsolDTO.valPrcConsol);
                            if(company.getPeriod2Value().equals(BigDecimal.ZERO)) {
                                company.setPeriod2Value(null);
                            }
                        }
                    }
                    companyValueDTOList.add(company);
                }
                indexEmpConsol++;
            }
            List<CompanyValueDTO> companyValueDTOListFinal = new ArrayList<>();
            for (CompanyValueDTO valueDTO:companyValueDTOList) {
                if((valueDTO.getPeriod1Value()!=null && !valueDTO.getPeriod1Value().toString().isEmpty()) ||
                    (valueDTO.getPeriod2Value()!=null && !valueDTO.getPeriod2Value().toString().isEmpty())) {
                        companyValueDTOListFinal.add(valueDTO);
                }
            }
            pdfSummaryReportDTO.setDatosEmpresasConsolidan(companyValueDTOListFinal);
        }


        // observaciones
        List<ObservationValueDTO> listaObservaciones = new LinkedList<>();
        ObservationValueDTO observationValueDTO = new ObservationValueDTO();
        observationValueDTO.setValue(clienteDTO.get(0).getBreakdownSearchResponseDTO().glsObservacion!=null?clienteDTO.get(0).getBreakdownSearchResponseDTO().glsObservacion:"");
        listaObservaciones.add(observationValueDTO);
        pdfSummaryReportDTO.setDatosObservaciones(listaObservaciones);

        // notas al desglose
        NoteValueDTO noteValueAuxiliar = null;
        List<NoteValueDTO> noteValueDTOLinkedList = new ArrayList<>();
        for (ConceptoImpresionDTO conceptoImpresionDTO : responseDTO.extraDataDTO.matrizConImp) {
            for (int i = 0; i < tableValueDTOS.size(); i++) {
                Integer ejecutaCodigo = 0;
                if(responseDTO.extraDataDTO.numPeriodosLoaded==1){
                    if((tableValueDTOS.get(i).getPeriod1Value()!=null && tableValueDTOS.get(i).getPeriod1Value().compareTo(BigDecimal.ZERO)!=0)){
                        ejecutaCodigo = 1;
                    }
                }
                if(responseDTO.extraDataDTO.numPeriodosLoaded==2){
                    if((tableValueDTOS.get(i).getPeriod1Value()!=null && tableValueDTOS.get(i).getPeriod1Value().compareTo(BigDecimal.ZERO)!=0) ||
                        (tableValueDTOS.get(i).getPeriod2Value()!=null && tableValueDTOS.get(i).getPeriod2Value().compareTo(BigDecimal.ZERO)!=0)){
                        ejecutaCodigo = 1;
                    }
                }
                if(responseDTO.extraDataDTO.numPeriodosLoaded==3){
                    if((tableValueDTOS.get(i).getPeriod2Value()!=null && tableValueDTOS.get(i).getPeriod2Value().compareTo(BigDecimal.ZERO)!=0) ||
                        (tableValueDTOS.get(i).getPeriod3Value()!=null && tableValueDTOS.get(i).getPeriod3Value().compareTo(BigDecimal.ZERO)!=0)){
                        ejecutaCodigo = 1;
                    }
                }
                if(responseDTO.extraDataDTO.numPeriodosLoaded>=4){
                    if((tableValueDTOS.get(i).getPeriod3Value()!=null && tableValueDTOS.get(i).getPeriod3Value().compareTo(BigDecimal.ZERO)!=0) ||
                        (tableValueDTOS.get(i).getPeriod4Value()!=null && tableValueDTOS.get(i).getPeriod4Value().compareTo(BigDecimal.ZERO)!=0)){
                        ejecutaCodigo = 1;
                    }
                }
                BigDecimal valorPeriodo1 = null;
                BigDecimal valorPeriodo2 = null;
                switch (responseDTO.extraDataDTO.numPeriodosLoaded){
                    case 1:
                        if(tableValueDTOS.get(i).getPeriod1Value()!= null) {
                            valorPeriodo1 = tableValueDTOS.get(i).getPeriod1Value();
                        }
                        break;
                    case 2:
                        if(tableValueDTOS.get(i).getPeriod1Value()!= null) {
                            valorPeriodo1 = tableValueDTOS.get(i).getPeriod1Value();
                        }
                        if(tableValueDTOS.get(i).getPeriod2Value()!= null) {
                            valorPeriodo2 = tableValueDTOS.get(i).getPeriod2Value();
                        }
                        break;
                    case 3:
                        if(tableValueDTOS.get(i).getPeriod2Value()!= null) {
                            valorPeriodo1 = tableValueDTOS.get(i).getPeriod2Value();
                        }
                        if(tableValueDTOS.get(i).getPeriod3Value()!= null) {
                            valorPeriodo2 = tableValueDTOS.get(i).getPeriod3Value();
                        }
                        break;
                    default:
                        if(tableValueDTOS.get(i).getPeriod3Value()!= null) {
                            valorPeriodo1 = tableValueDTOS.get(i).getPeriod3Value();
                        }
                        if(tableValueDTOS.get(i).getPeriod4Value()!= null) {
                            valorPeriodo2 = tableValueDTOS.get(i).getPeriod4Value();
                        }
                        break;
                }
                if (tableValueDTOS.get(i).getCorConcept1() != null && conceptoImpresionDTO.getCodTipoConcep().equals(tableValueDTOS.get(i).getCodTipoConcepto())
                    && (tableValueDTOS.get(i).getFlgIsImpreso() == 1 || tableValueDTOS.get(i).getFlgIsImpreso() == 2 )) {
                            if((tableValueDTOS.get(i).getCorConcept().startsWith(conceptoImpresionDTO.getCodConcepto()+".") )) {
                                if(ejecutaCodigo==1) {
                                    NoteValueDTO noteValue = new NoteValueDTO();
                                    noteValue.setConceptName(tableValueDTOS.get(i).getConceptName());
                                    noteValue.setCorConcept(null);
                                    noteValue.setPeriod1Value(valorPeriodo1);
                                    noteValue.setPeriod2Value(valorPeriodo2);
                                    noteValue.setIsHeader(true);
                                    noteValue.setIsTotal(false);
                                    noteValue.setTipoConcepto(tableValueDTOS.get(i).getCodTipoConcepto());
                                    noteValueDTOLinkedList.add(noteValue);
                                }
                                if(tableValueDTOS.get(i + 1)!=null) {
                                    if (!tableValueDTOS.get(i + 1).getCorConcept().contains(".")) {
                                        if(noteValueAuxiliar!=null){
                                            noteValueDTOLinkedList.add(noteValueAuxiliar);
                                            noteValueAuxiliar = null;
                                        }
                                    }
                                }
                            }
                    if (tableValueDTOS.get(i).getCorConcept1().toString().equals(conceptoImpresionDTO.getCorConcepto())) {
                        if(ejecutaCodigo==1) {
                            NoteValueDTO noteValue = new NoteValueDTO();
                            noteValue.setConceptName(tableValueDTOS.get(i).getConceptName());
                            noteValue.setCorConcept("(" + tableValueDTOS.get(i).getCorConcept() + ")");
                            noteValue.setPeriod1Value(null);
                            noteValue.setPeriod2Value(null);
                            noteValue.setIsHeader(false);
                            noteValue.setIsTotal(true);
                            noteValue.setTipoConcepto(tableValueDTOS.get(i).getCodTipoConcepto());

                            noteValueAuxiliar = new NoteValueDTO();
                            noteValueAuxiliar.setConceptName(null);
                            noteValueAuxiliar.setCorConcept(null);
                            noteValueAuxiliar.setPeriod1Value(valorPeriodo1);
                            noteValueAuxiliar.setPeriod2Value(valorPeriodo2);
                            noteValueAuxiliar.setIsHeader(false);
                            noteValueAuxiliar.setIsTotal(true);
                            noteValueAuxiliar.setTipoConcepto(tableValueDTOS.get(i).getCodTipoConcepto());

                            noteValueDTOLinkedList.add(noteValue);
                        }
                    }
                }
            }
        }

        pdfSummaryReportDTO.setDatosNotasDesglose(noteValueDTOLinkedList);

        Integer corImpreso = this.grabarImpresion(dto,responseDTO,tableValueDTOS,esquema);
        String TituloReporte = "";
        if(dto.getCodSubtipoDes()!=1 && dto.getCodSubtipoDes()!=2) {
            TituloReporte = this.desgloseService
                .getGlosaImpresion(dto.getCodCliente(), dto.getCodTipoPlantilla(), dto.getCodTipoDes(), dto.getCodSubtipoDes(), dto.getCodMoneda(), dto.getCodMonedaImpresion(),
                    corImpreso);
        }

        pdfSummaryReportDTO.setTituloReporte(TituloReporte);
        return pdfSummaryReportDTO;
    }
    private  String formatearRUT(String rut) {

        int cont = 0;
        String format;
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        format = "-" + rut.substring(rut.length() - 1);
        for (int i = rut.length() - 2; i >= 0; i--) {
            format = rut.substring(i, i + 1) + format;
            cont++;
            if (cont == 3 && i != 0) {
                format = "." + format;
                cont = 0;
            }
        }
        return format;
    }
    public PdfIngressReportDTO getPdfIngressReport(DesgloseImprimeDTO dto) {
        BreakdownSearchRequestDTO breakdownSearchRequestDTO = new BreakdownSearchRequestDTO();
        breakdownSearchRequestDTO.codCliente = dto.getCodCliente();
        breakdownSearchRequestDTO.tipoDesglose = dto.getCodTipoDes();
        breakdownSearchRequestDTO.subTipoDesglose = dto.getCodSubtipoDes();
        breakdownSearchRequestDTO.moneda = dto.getCodMoneda();
        breakdownSearchRequestDTO.tipoPlantillaDesglose = dto.getCodTipoPlantilla();
        BreakdownRecoverResponseDTO responseDTO = desgloseService.recoverBreakdown(breakdownSearchRequestDTO);

        List<DescripcionCodigoDTO> listaMonedas = descripcionCodigoService.findAllByCodTabla(CODTABLA_TIPO_MONEDAS,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaTiposDesglose = descripcionCodigoService.findAllByCodTabla(CODTABLA_TIPO_DESGLOSE,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaSubtipoDesglose = descripcionCodigoService.findAllByCodTabla(CODTABLA_SUBTIPO_DESGLOSE,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaPlantillasDesglose = descripcionCodigoService.findAllByCodTabla(CODTABLA_PLANTILLAS_DESGLOSE,0,"codGenerico,ASC");
        List<DescripcionCodigoDTO> listaUnidadesM = descripcionCodigoService.findAllByCodTabla(CODTABLA_UNIDAD_MONETARIA,0,"codGenerico,ASC");
        List<ClienteDTO> clienteDTO = clienteService.findByRut(dto.getCodCliente(), dto.getCodTipoDes(), dto.getCodSubtipoDes(), dto.getCodMoneda(), dto.getCodTipoPlantilla(),null);

        ConceptoRequestDTO conceptoRequestDTO = new ConceptoRequestDTO();
        conceptoRequestDTO.templateCode = dto.getCodTipoPlantilla();
        List<String> conceptosTypesCodes = new LinkedList<>();
        conceptosTypesCodes.add(COD_TIPO_CONCEP_BALANCE);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_EST_RESULT);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_GEN_FONDOS);
        conceptosTypesCodes.add(COD_TIPO_CONCEP_IND_FINAN);
        List<String> orderByFiels = new LinkedList<>();
        orderByFiels.add(FIELD_NAME_COD_TIPO_CONCEP);
        orderByFiels.add(FIELD_NAME_NUM_SECUENCIA);
        conceptoRequestDTO.conceptTypeCodesIn = conceptosTypesCodes;
        conceptoRequestDTO.orderByFields = orderByFiels;
        conceptoRequestDTO.orderByDirection = "ASC";
        conceptoRequestDTO.flgConActivo = COD_REG_STATUS_ONE;
        conceptoRequestDTO.columnLetterGlsFormulaVal1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaVal2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaVal3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaVal4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaDef1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaDef2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaDef3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaDef4 = COLUMN_NUMBER_GLS_FORMULA__4;
        conceptoRequestDTO.columnLetterGlsFormulaPrc1 = COLUMN_NUMBER_GLS_FORMULA__1;
        conceptoRequestDTO.columnLetterGlsFormulaPrc2 = COLUMN_NUMBER_GLS_FORMULA__2;
        conceptoRequestDTO.columnLetterGlsFormulaPrc3 = COLUMN_NUMBER_GLS_FORMULA__3;
        conceptoRequestDTO.columnLetterGlsFormulaPrc4 = COLUMN_NUMBER_GLS_FORMULA__4;
        List<ConceptoDTO> conceptoDTOList = conceptoService.findByTemplateAndCodConceptsIn(conceptoRequestDTO,null).conceptoDTOList;


        // se llena el reporte de ingreso

        List<ActividadEconomicaDTO> actividadEconomicaDTOList = actividadEconomicaService.getAllEconomicActivities();
        List<SectorDTO> sectorDTOList = sectorService.getAllSectors();
        List<SubSectorDTO> subSectorDTOList = subSectorService.getAllSubSectors();
        String actividadEconomica = "";
        String sector = "";
        String subSector = "";
        String moneda = "";
        String tipoDesglose = "";
        String subTipoDesglose = "";
        String tipoPlantilla = "";
        String unidadMonetaria = "";
        String iva = "";
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaMonedas) {
            if(responseDTO.codMoneda.equals(descripcionCodigoDTO.getCodGenerico())) {
                moneda = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaUnidadesM) {
            if(responseDTO.codUnidMoneda.equals(descripcionCodigoDTO.getCodGenerico())) {
                unidadMonetaria = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaTiposDesglose) {
            if(responseDTO.codTipoDes.equals(descripcionCodigoDTO.getCodGenerico())) {
                tipoDesglose = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaSubtipoDesglose) {
            if(responseDTO.codSubtipoDes.equals(descripcionCodigoDTO.getCodGenerico())) {
                subTipoDesglose = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (DescripcionCodigoDTO descripcionCodigoDTO:listaPlantillasDesglose) {
            if(responseDTO.codPlantilla.equals(descripcionCodigoDTO.getCodGenerico())) {
                tipoPlantilla = descripcionCodigoDTO.getGlsGenericalarga();
            }
        }
        for (ActividadEconomicaDTO actividadEconomicaDTO :actividadEconomicaDTOList) {
            if(clienteDTO.get(0).getActivity().equals(actividadEconomicaDTO.getCodActividad())) {
                actividadEconomica = actividadEconomicaDTO.getGlsActividad();
                clienteDTO.get(0).setSector(actividadEconomicaDTO.getCodSector());
                clienteDTO.get(0).setSubSector(actividadEconomicaDTO.getCodSubsector());
            }
        }
        for (SectorDTO sectorDTO:sectorDTOList) {
            if(clienteDTO.get(0).getSector().equals(sectorDTO.getCodSector())) {
                sector = sectorDTO.getGlsSector();
            }
        }
        for (SubSectorDTO subSectorDTO:subSectorDTOList) {
            if(clienteDTO.get(0).getSubSector().equals(subSectorDTO.getCodSubSector())) {
                subSector = subSectorDTO.getGlsSubSector();
            }
        }
        iva = clienteDTO.get(0).getBreakdownSearchResponseDTO().valIva.toString();

        PdfIngressReportDTO pdfIngressReportDTO = new PdfIngressReportDTO();
        pdfIngressReportDTO.setFileName("reporteIngreso");
        pdfIngressReportDTO.setCodCliente(responseDTO.codCliente);
        pdfIngressReportDTO.setCodTipoPlantilla(responseDTO.codPlantilla);
        pdfIngressReportDTO.setCodTipoDes(responseDTO.codTipoDes);
        pdfIngressReportDTO.setCodSubtipoDes(responseDTO.codSubtipoDes);
        pdfIngressReportDTO.setMoneda(dto.getCodMoneda());
        pdfIngressReportDTO.setRutCliente(formatearRUT(clienteDTO.get(0).getRut()));
        pdfIngressReportDTO.setNombreCliente(clienteDTO.get(0).getName());
        pdfIngressReportDTO.setTipoPlantillaDesglose(tipoPlantilla);
        pdfIngressReportDTO.setTipoDesglose(tipoDesglose);
        pdfIngressReportDTO.setSubTipoDesglose(subTipoDesglose);
        pdfIngressReportDTO.setMoneda(moneda);
        pdfIngressReportDTO.setActividad(actividadEconomica);
        pdfIngressReportDTO.setSector(sector);
        pdfIngressReportDTO.setSubSector(subSector);
        pdfIngressReportDTO.setIva(iva);
        pdfIngressReportDTO.setMontosEn(unidadMonetaria);
        pdfIngressReportDTO.setActualizadoAl(LocalDateTime.now().format(Constants.FORMATTER_DATE_SHORT));

        // auditores de los periodos visibles

        for (int i = 0; i < responseDTO.extraDataDTO.auditors.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.auditors.get(i).otherAuditorName != null) {
                        switch (l) {
                            case 0:
                                pdfIngressReportDTO.setAuditor1(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName);
                                break;
                            case 1:
                                pdfIngressReportDTO.setAuditor2(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName);
                                break;
                            case 2:
                                pdfIngressReportDTO.setAuditor3(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName);
                                break;
                            case 3:
                                pdfIngressReportDTO.setAuditor4(responseDTO.extraDataDTO.auditors.get(i).otherAuditorName);
                                break;
                        }
                    } else {
                        if (responseDTO.extraDataDTO.auditors.get(i).auditor != null) {
                            switch (l) {
                                case 0:
                                    pdfIngressReportDTO.setAuditor1(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga());
                                    break;
                                case 1:
                                    pdfIngressReportDTO.setAuditor2(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga());
                                    break;
                                case 2:
                                    pdfIngressReportDTO.setAuditor3(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga());
                                    break;
                                case 3:
                                    pdfIngressReportDTO.setAuditor4(responseDTO.extraDataDTO.auditors.get(i).auditor.getGlsGenericalarga());
                                    break;
                            }
                        }
                    }
                }
            }
        }

        // fechas de los periodos visibles
        for (int i = 0; i < responseDTO.extraDataDTO.fecPeriodo.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.fecPeriodo != null) {
                        switch (l) {
                            case 0:
                                pdfIngressReportDTO.setFecPerido1(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                break;
                            case 1:
                                pdfIngressReportDTO.setFecPerido2(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                break;
                            case 2:
                                pdfIngressReportDTO.setFecPerido3(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                break;
                            case 3:
                                pdfIngressReportDTO.setFecPerido4(responseDTO.extraDataDTO.fecPeriodo.get(i).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                                break;
                        }
                    }
                }
            }
        }

        // meses de los periodos visibles

        for (int i = 0; i < responseDTO.extraDataDTO.monthsPeriod.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.monthsPeriod != null) {
                        switch (l) {
                            case 0:
                                pdfIngressReportDTO.setNumMeses1(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                            case 1:
                                pdfIngressReportDTO.setNumMeses2(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                            case 2:
                                pdfIngressReportDTO.setNumMeses3(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                            case 3:
                                pdfIngressReportDTO.setNumMeses4(responseDTO.extraDataDTO.monthsPeriod.get(i).toString());
                                break;
                        }
                    }
                }
            }
        }
        // table values DTO
        List<TableValueDTO> tableValueDTOS = new ArrayList<>();
        for (ConceptoDTO conceptoDTO : conceptoDTOList) {
            if(conceptoDTO.getFlgIsPrinted()==1) {
                TableValueDTO tableValueDTO = new TableValueDTO();
                tableValueDTO.setCodTipoConcepto(conceptoDTO.getCodTipoConcep() != null ? conceptoDTO.getCodTipoConcep() : "");
                tableValueDTO.setConceptName(conceptoDTO.getGlsConcepto());
                tableValueDTO.setCorConcept(conceptoDTO.getCodConcepto() != null ? conceptoDTO.getCodConcepto().toString() : "");
                tableValueDTO.setCorConcept1(conceptoDTO.getCorConcepto() != null ? conceptoDTO.getCorConcepto() : null);
                tableValueDTO.setFlgIsBold(conceptoDTO.getFlgIsBold());
                tableValueDTO.setFlgIsEnclosed(conceptoDTO.getFlgIsEnclosed());
                tableValueDTO.setFlgIsImpreso(conceptoDTO.getFlgIsPrinted());
                if (pdfIngressReportDTO.getFecPerido1() != null) {
                    tableValueDTO.setPeriod1Fecha(LocalDate.parse(pdfIngressReportDTO.getFecPerido1(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                }
                if (pdfIngressReportDTO.getFecPerido2() != null) {
                    tableValueDTO.setPeriod2Fecha(LocalDate.parse(pdfIngressReportDTO.getFecPerido2(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                }
                if (pdfIngressReportDTO.getFecPerido3() != null) {
                    tableValueDTO.setPeriod3Fecha(LocalDate.parse(pdfIngressReportDTO.getFecPerido3(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                }
                if (pdfIngressReportDTO.getFecPerido4() != null) {
                    tableValueDTO.setPeriod4Fecha(LocalDate.parse(pdfIngressReportDTO.getFecPerido4(),FORMATTER_DATE_SHORT_LATINO_CORTO).format(FORMATTER_DATE_SHORT_LATINO_CORTO));
                }
                tableValueDTOS.add(tableValueDTO);
            }
        }
        int indexPeriod = 0;
        for (PeriodoDTO periodo : responseDTO.extraDataDTO.periodos) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (indexPeriod == dto.getActivePeriodsIndexes()[l]) {
                    for (ValPeriodoDesgloseDTO valPeriodoDesgloseDTO : periodo.valPeriodoDesgloseDTOList) {
                        if (valPeriodoDesgloseDTO.getCodTipoConcep() != null) {
                                for (TableValueDTO conceptoDTO : tableValueDTOS) {
                                    String zeros = "";
                                    if(conceptoDTO.getNumDecPrint()!=null) {
                                        for (int i = 0; i < Integer.valueOf(conceptoDTO.getNumDecPrint()); i++) {
                                            zeros = zeros + "0";
                                        }
                                    }
                                    DecimalFormat formato = new DecimalFormat("#0."+ zeros.trim());
                                    if(zeros.trim().isEmpty()) {
                                        formato = new DecimalFormat("#0");
                                    }
                                    if (valPeriodoDesgloseDTO.getCodTipoConcep()!=null && conceptoDTO.getCodTipoConcepto()!=null && valPeriodoDesgloseDTO.getCodTipoConcep().trim().equals(conceptoDTO.getCodTipoConcepto().trim()) ) {
                                        if (valPeriodoDesgloseDTO.getCorConcepto() != null && conceptoDTO.getCorConcept1() != null && valPeriodoDesgloseDTO.getCorConcepto().toString().equals(conceptoDTO.getCorConcept1().toString())) {
                                            conceptoDTO.setConceptName(valPeriodoDesgloseDTO.getGlsConcepto() != null ? valPeriodoDesgloseDTO.getGlsConcepto() : conceptoDTO.getConceptName());
                                            if (dto.getCodMonedaImpresion().equals(dto.getCodMoneda())) {
                                                switch (l) {
                                                    case 0:
                                                        if (dto.getDeflactores()[0] != null) {
                                                            BigDecimal calculo1 = dto.getDeflactorBase().divide(dto.getDeflactores()[0], 3, RoundingMode.HALF_UP);
                                                            conceptoDTO.setPeriod1Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo1).setScale(3,
                                                                RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod1Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                    case 1:
                                                        if (dto.getDeflactores()[1] != null) {
                                                            BigDecimal calculo2 = dto.getDeflactorBase().divide(dto.getDeflactores()[1], 3, RoundingMode.HALF_UP);
                                                            conceptoDTO.setPeriod2Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo2).setScale(3,
                                                                RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod2Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                    case 2:
                                                        if (dto.getDeflactores()[2] != null) {
                                                            BigDecimal calculo3 = dto.getDeflactorBase().divide(dto.getDeflactores()[2], 3, RoundingMode.HALF_UP);
                                                            conceptoDTO.setPeriod3Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo3).setScale(3,
                                                                RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod3Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                    case 3:
                                                        if (dto.getDeflactores()[3] != null) {
                                                            BigDecimal calculo4 = dto.getDeflactorBase().divide(dto.getDeflactores()[3], 3, RoundingMode.HALF_UP);
                                                            conceptoDTO.setPeriod4Value(new BigDecimal(formato.format(valPeriodoDesgloseDTO.getValConcepto().multiply(calculo4).setScale(3,
                                                                RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod4Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                }
                                            } else {
                                                switch (l) {
                                                    case 0:
                                                        if (dto.getTiposCambio()[0] != null) {
                                                            conceptoDTO.setPeriod1Value(new BigDecimal(formato.format(
                                                                valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[0]).setScale(3, RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod1Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                    case 1:
                                                        if (dto.getTiposCambio()[1] != null) {
                                                            conceptoDTO.setPeriod2Value(new BigDecimal(formato.format(
                                                                valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[1]).setScale(3, RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod2Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                    case 2:
                                                        if (dto.getTiposCambio()[2] != null) {
                                                            conceptoDTO.setPeriod3Value(new BigDecimal(formato.format(
                                                                valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[2]).setScale(3, RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod3Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                    case 3:
                                                        if (dto.getTiposCambio()[3] != null) {
                                                            conceptoDTO.setPeriod4Value(new BigDecimal(formato.format(
                                                                valPeriodoDesgloseDTO.getValConcepto().multiply(dto.getTiposCambio()[3]).setScale(3, RoundingMode.HALF_UP)).replace(",",".")));
                                                            conceptoDTO.setPeriod4Percent(valPeriodoDesgloseDTO.getValPrcConcep());
                                                        }
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
            indexPeriod++;
        }

        // recorremos la tabla
        for (TableValueDTO tableValueDTO:tableValueDTOS) {
            if(tableValueDTO.getCorConcept1()!=null) {
                if (tableValueDTO.getFlgIsImpreso() == 2) { // aparicion condicional
                    if ((tableValueDTO.getPeriod1Value() == null || tableValueDTO.getPeriod1Value().equals(0)) && (tableValueDTO.getPeriod2Value() == null || tableValueDTO.getPeriod2Value().equals(0)) && (tableValueDTO.getPeriod3Value() == null || tableValueDTO.getPeriod3Value().equals(0)) && (
                        tableValueDTO.getPeriod4Value() == null || tableValueDTO.getPeriod4Value().equals(0))) {
                        tableValueDTOS.remove(tableValueDTO);
                    }
                }
                if(tableValueDTO.getFlgIsImpreso() == 0){
                    tableValueDTOS.remove(tableValueDTO);
                }

                if (tableValueDTO.getPeriod1Percent()!= null && tableValueDTO.getPeriod1Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod1Percent(null);
                }
                if (tableValueDTO.getPeriod2Percent()!=null && tableValueDTO.getPeriod2Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod2Percent(null);
                }
                if (tableValueDTO.getPeriod3Percent()!=null && tableValueDTO.getPeriod3Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod3Percent(null);
                }
                if (tableValueDTO.getPeriod4Percent()!= null && tableValueDTO.getPeriod4Percent().compareTo(BigDecimal.ZERO) == 0) {
                    tableValueDTO.setPeriod4Percent(null);
                }
            }
        }

        pdfIngressReportDTO.setTableValueDTOS(tableValueDTOS);
        this.grabarImpresion(dto,responseDTO,tableValueDTOS,null);
        return pdfIngressReportDTO;
    }
    private Integer grabarImpresion(DesgloseImprimeDTO dto, BreakdownRecoverResponseDTO responseDTO, List<TableValueDTO> tableValueDTOS,String esquema){
        // realizamos la grabacion de la impresion. antes de devolver el reporte
        DesgloseImpresionDTO desgloseImpresionDTO = new DesgloseImpresionDTO();
        desgloseImpresionDTO.setCodCliente(dto.getCodCliente());
        desgloseImpresionDTO.setCodMoneda(dto.getCodMoneda());
        desgloseImpresionDTO.setCodPlantillaDes(dto.getCodTipoPlantilla());
        desgloseImpresionDTO.setCodTipoDes(dto.getCodTipoDes());
        desgloseImpresionDTO.setCodSubtipoDes(dto.getCodSubtipoDes());
        desgloseImpresionDTO.setCodSubtipoDes(dto.getCodSubtipoDes());
        desgloseImpresionDTO.setFecDeflacBase(dto.getFecDeflacBase());
        desgloseImpresionDTO.setValDeflacBase(dto.getDeflactorBase());
        desgloseImpresionDTO.setCodMonedaImp(dto.getCodMonedaImpresion());
        Integer corImpreso = desgloseImpresionService.executeCifSpInsDes(desgloseImpresionDTO,esquema);
        // cargamos los periodos de impresion

        for (int i = 0; i < responseDTO.extraDataDTO.deflatorPeriod.size(); i++) {
            for (int l = 0; l < dto.getActivePeriodsIndexes().length; l++) {
                if (i == dto.getActivePeriodsIndexes()[l]) {
                    if (responseDTO.extraDataDTO.deflatorPeriod != null) {
                        PeriodoDesgImpresionDTO periodoDesgImpresionDTO = new PeriodoDesgImpresionDTO();
                        periodoDesgImpresionDTO.setCodCliente(dto.getCodCliente());
                        periodoDesgImpresionDTO.setCodPlantilla(dto.getCodTipoPlantilla());
                        periodoDesgImpresionDTO.setCodMoneda(dto.getCodMoneda());
                        periodoDesgImpresionDTO.setCodMonedaImp(dto.getCodMonedaImpresion());
                        periodoDesgImpresionDTO.setCodTipoDes(dto.getCodTipoDes());
                        periodoDesgImpresionDTO.setCodSubtipoDes(dto.getCodSubtipoDes());
                        periodoDesgImpresionDTO.setFecPeriodo(responseDTO.extraDataDTO.fecPeriodo.get(i));
                        /*periodoDesgImpresionDTO.setValDeflacPrd(responseDTO.extraDataDTO.deflatorPeriod.get(i));*/
                        periodoDesgImpresionDTO.setValDeflacPrd(dto.getDeflactores()[l]);
                        periodoDesgImpresionDTO.setCorImpreso(corImpreso);
                        periodoDesgImpresionDTO.setValTipoCambio(dto.getTiposCambio()[l]);
                        periodoDesgImpresionService.executeCifSpInsPrdImp(periodoDesgImpresionDTO,esquema);
                    }
                }
            }
        }
        // cargamos los valores de impresion
        for (TableValueDTO tableValueDTO:tableValueDTOS) {
            for(int i = 0;i< 4;i ++){
                if(tableValueDTO.getCorConcept1()!=null) {
                    ValPeriodoDesgImprDTO valPeriodoDesgImprDTO = new ValPeriodoDesgImprDTO();
                    valPeriodoDesgImprDTO.setCodCliente(dto.getCodCliente());
                    valPeriodoDesgImprDTO.setCodTipoDes(dto.getCodTipoDes());
                    valPeriodoDesgImprDTO.setCodMoneda(dto.getCodMoneda());
                    valPeriodoDesgImprDTO.setCodSubtipoDes(dto.getCodSubtipoDes());
                    valPeriodoDesgImprDTO.setCod_tipo_plantilla_des(dto.getCodTipoPlantilla());
                    valPeriodoDesgImprDTO.setCodMonedaImp(dto.getCodMonedaImpresion());
                    valPeriodoDesgImprDTO.setCorImpreso(corImpreso);
                    valPeriodoDesgImprDTO.setCorValor(0);
                    valPeriodoDesgImprDTO.setCorConcepto(tableValueDTO.getCorConcept1());
                    valPeriodoDesgImprDTO.setCodTipoConcep(tableValueDTO.getCodTipoConcepto());
                    switch (i) {
                        case 0:
                            if(tableValueDTO.getPeriod1Fecha()!=null && tableValueDTO.getPeriod1Value()!=null) {
                                valPeriodoDesgImprDTO.setValConcepto(tableValueDTO.getPeriod1Value());
                                valPeriodoDesgImprDTO.setFecPeriodo(LocalDate.parse(tableValueDTO.getPeriod1Fecha(),FORMATTER_DATE_SHORT_LATINO_CORTO));
                                valPeriodoDesgImprService.executeCifSpInsValImp(valPeriodoDesgImprDTO,esquema);
                            }
                            break;
                        case 1:
                            if(tableValueDTO.getPeriod2Fecha()!=null && tableValueDTO.getPeriod2Value()!=null) {
                                valPeriodoDesgImprDTO.setValConcepto(tableValueDTO.getPeriod2Value());
                                valPeriodoDesgImprDTO.setFecPeriodo(LocalDate.parse(tableValueDTO.getPeriod2Fecha(),FORMATTER_DATE_SHORT_LATINO_CORTO));
                                valPeriodoDesgImprService.executeCifSpInsValImp(valPeriodoDesgImprDTO,esquema);
                            }
                            break;
                        case 2:
                            if(tableValueDTO.getPeriod3Fecha()!=null && tableValueDTO.getPeriod3Value()!=null) {
                                valPeriodoDesgImprDTO.setValConcepto(tableValueDTO.getPeriod3Value());
                                valPeriodoDesgImprDTO.setFecPeriodo(LocalDate.parse(tableValueDTO.getPeriod3Fecha(),FORMATTER_DATE_SHORT_LATINO_CORTO));
                                valPeriodoDesgImprService.executeCifSpInsValImp(valPeriodoDesgImprDTO,esquema);
                            }
                            break;
                        case 3:
                            if(tableValueDTO.getPeriod4Fecha()!=null && tableValueDTO.getPeriod4Value()!=null) {
                                valPeriodoDesgImprDTO.setValConcepto(tableValueDTO.getPeriod4Value());
                                valPeriodoDesgImprDTO.setFecPeriodo(LocalDate.parse(tableValueDTO.getPeriod4Fecha(),FORMATTER_DATE_SHORT_LATINO_CORTO));
                                valPeriodoDesgImprService.executeCifSpInsValImp(valPeriodoDesgImprDTO,esquema);
                            }
                            break;
                    }
                }
            }
        }
        return  corImpreso;
    }
    private byte[] generateJasperReportPDF(String  name, ByteArrayOutputStream outputStream, Map<String, Object> params, String fileName) throws JRException {
        try {
            if (fileName != null) {
                fileName = fileName.replaceAll("/", "_");
            }
            String location = PDF_OUTPUT_DIRECTORY + fileName + "-" + new Date().getTime() + ".pdf";
            JasperPrint jasperPrint = JasperFillManager.fillReport(name, params, new JREmptyDataSource());
            //JasperExportManager.exportReportToPdfFile(jasperPrint, Destino);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            log.error("Error Generacion de Reporte " + ex.getMessage());
            return null;
        }
    }

    /**
     * Método para compilat el reporte jasper.
     *
     * @param subreportsTemplateInformeDesgloseBalanceToCompile
     * @param templateInformeDesgloseBalance
     *
     * @return
     *
     * @throws JRException
     */
    private InputStream compileReport(String[] subreportsTemplateInformeDesgloseBalanceToCompile, String templateInformeDesgloseBalance) throws JRException {
        // Compiling the report
        ClassLoader classLoader = getClass().getClassLoader();
        for (String subReportPath : subreportsTemplateInformeDesgloseBalanceToCompile) {
            InputStream subReport = classLoader.getResourceAsStream(REPORTS_TEMPLATES_PATH + subReportPath + JASPER_JRXML_EXT);
            final JasperReport compiledReport = JasperCompileManager.compileReport(subReport);
            JRSaver.saveObject(compiledReport, subReportPath + JASPER_JASPER_EXT); // To avoid compiling it every time, we can save it to a file
        }
        return classLoader.getResourceAsStream(REPORTS_TEMPLATES_PATH + templateInformeDesgloseBalance + JASPER_JRXML_EXT);
    }

    /**
     * Guarda el jasper compilado e inicializa los parámetros comunes para el reporte.
     *
     * @param dto
     * @param jasperReport
     * @param fileNamePart
     *
     * @return
     *
     * @throws JRException
     */
    @NotNull
    private Map<String, Object> saveCompiledJasperAndInitializeCommonParams(Object dto, JasperReport jasperReport, String fileNamePart) throws JRException {
        JRSaver.saveObject(jasperReport, fileNamePart + JASPER_JASPER_EXT); // To avoid compiling it every time, we can save it to a file

        Map<String, Object> params = new HashMap<>();
        /* Convert List to JRBeanCollectionDataSource. No es necesario, porque se puede crear dentro del mismo diseño del reporte*/
        // dto.setCollectionDataSource(new JRBeanCollectionDataSource(dto.getTableValueDTOS()));
        params.put(this.DATA_SOURCE_PARAMETER_NAME, dto);
        params.put(this.SUB_REPORT_PATH_PARAMETER_NAME, REPORTS_TEMPLATES_PATH);
        params.put(JRParameter.REPORT_LOCALE, Locale.ITALY);
        return params;
    }
}
