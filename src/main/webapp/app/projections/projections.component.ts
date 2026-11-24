import {AfterViewInit, Component, ElementRef, OnInit, Renderer, Renderer2, ViewChild} from '@angular/core';
import {NgbAccordionConfig, NgbActiveModal, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ITurbineUser} from 'app/shared/model/turbine-user.model';
import {Account, Principal} from 'app/core';
import {
    COD_TIPO_CONCEP_BALANCE,
    COD_TIPO_CONCEP_EST_RESULT,
    COD_TIPO_CONCEP_GEN_FONDOS,
    COD_TIPO_CONCEP_IND_FINAN,
    COD_TIPO_CONCEP_PROY_DM,
    COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL,
    COD_TIPO_CONCEP_PROY_DM_EST_RESULT,
    COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA,
    CODTABLA_SUBTIPO_DESGLOSE,
    CODTABLA_TIPO_DESGLOSE,
    CODTABLA_TIPO_MONEDAS,
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE,
    CODTABLA_TIPO_PLANTILLAS_PROYECCION,
    CODTABLA_UNIDAD_MONETARIA,
    COLUMN_NUMBER_CCC,
    COLUMN_NUMBER_COD_CONCEPT,
    COLUMN_NUMBER_COD_TIPO_CONCEP,
    COLUMN_NUMBER_COD_TIPO_REGIS,
    COLUMN_NUMBER_FLG_IS_TOTAL,
    COLUMN_NUMBER_GLS_FORMULA_PRC,
    COLUMN_NUMBER_GLS_FORMULA_VAL,
    CSS_CLASS_HEADER_GROUP_RENDERER,
    CSS_CLASS_HEREDO,
    CSS_CLASS_ZERO,
    ESTADO_BORRADOR_EN_PROCESO,
    ESTADO_DEFINITIVO,
    ESTADO_EN_EDICION,
    FIELD_NAME_COD_TIPO_CONCEP,
    FIELD_NAME_NUM_SECUENCIA,
    HANDS_ON_TABLE_CUSTOM_CULTURE,
    HANDS_ON_TABLE_LICENSE_KEY,
    HANDS_ON_TABLE_NUMERIC_PATTERN_3,
    ICON_CARD_CLOSE,
    ICON_CARD_OPEN,
    INFINITY,
    INFINITY_STR,
    MAX_PROJECTION_YEARS,
    MULTI_REGISTRY,
    P_ANT, P_SIG,
    SESSION_VARIABLE_USUARIO,
    USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR,
    USER_ROLE_PLAT_DESGLOSE_EJECUTOR,
    USER_ROLE_PLAT_DESGLOSE_JEFE,
    USER_ROLE_PLAT_DESGLOSE_REVISOR,
    VALUE_IS_ACTIVE_CONCEPT_TRUE
} from 'app/app.constants';

import {ExtraDataDesglose, IExtraDataDesglose} from 'app/shared/model/extra-data-desglose.model';
import {IDescripcionCodigo} from 'app/shared/model/descripcion-codigo.model';
import {IPeriodo} from 'app/shared/model/periodo.model';
import {Concepto, IConcepto} from 'app/shared/model/concepto.model';
import {ActivatedRoute, Router} from '@angular/router';
import {DescripcionCodigoService} from 'app/entities/descripcion-codigo';
import {JhiAlertService} from 'ng-jhipster';
import {CustomSqlService} from 'app/shared/services/custom-sql.service';
import {ClienteService} from 'app/shared/services/cliente.service';
import {Sweetalert2Component} from 'app/shared';
import {TablaValorService} from 'app/entities/tabla-valor';
import {FilterDescripcionCodigoPipe} from 'app/shared/pipes/filter-descripcion-codigo.pipe';
import {TransformIvaPipe} from 'app/shared/pipes/transform-iva.pipe';
import {ValidationUtils} from 'app/shared/util/validation-utils';
import {DesgloseService} from 'app/entities/desglose';
import {ConceptoService} from 'app/entities/concepto';
import {DatePipe} from '@angular/common';
import {HotTableRegisterer} from '@handsontable/angular';
import {PeriodoService} from 'app/entities/periodo';
import {ReportService} from 'app/shared/services/report.service';
import {ValPeriodoDesgImprService} from 'app/entities/val-periodo-desg-impr';
import {PeriodoDesgImpresionService} from 'app/entities/periodo-desg-impresion';
import {DesgloseImpresionService} from 'app/entities/desglose-impresion';
import {HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {isNullOrUndefined} from 'util';
import {SessionStorageService} from 'ngx-webstorage';
import {TranslateService} from '@ngx-translate/core';
import {Moment} from 'moment';
import {ProjectionService} from 'app/entities/projections/projection.service';
import {ProjectionHistoricDataRequestDTO} from 'app/projections/projection-historic-data-request.model';
import {IBreakdownRecoverResponseDTO} from 'app/entities/desglose/recover-breakdown-response.model';
import * as numbro from 'numbro';
import * as spanishChileNumberCulture from 'numbro/languages/es-CL';
import Handsontable from 'handsontable';
import {
    ConceptRequestDTO,
    IConceptRequestDTO,
    OrderByDirection
} from 'app/entities/concepto/filter-concept-request.model';
import {IValPeriodoDesglose} from 'app/shared/model/val-periodo-desglose.model';
import {
    BreakdownFilterRequestDTO,
    IBreakdownFilterRequestDTO
} from 'app/entities/desglose/filter-breakdown-request.model';
import {LoaderService} from 'app/core/load/loader.service';
import {Desglose, IDesglose} from 'app/shared/model/desglose.model';
import {IValPeriodoProyeccionDesglose} from 'app/shared/model/val-periodo-proyeccion-desglose.model';
import {IBreakdownFilterConceptDto} from 'app/shared/model/breakdown-filter-concept-dto';
import {NgxSpinnerService} from 'ngx-spinner';
import * as jQuery from 'jquery';
import {Observable, Subject, Subscription} from 'rxjs';
// @ts-ignore
numbro.registerLanguage(spanishChileNumberCulture);
Handsontable.renderers.registerRenderer('zeroValueRenderer', function(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.NumericRenderer.apply(this, arguments);
    if (value !== INFINITY && value !== INFINITY_STR) {
        if (!isNaN(Number(value))) {
            if (Number(value) === 0) {
                td.className = CSS_CLASS_ZERO;
            } else {
                td.className = CSS_CLASS_HEREDO;
            }
        } else {
            td.className = CSS_CLASS_ZERO;
        }
    } else {
        td.className = CSS_CLASS_ZERO;
    }
});
function headerGroupTextRenderer(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
    td.className = CSS_CLASS_HEADER_GROUP_RENDERER;
}
function initColumnas(lectura: boolean) {
    return [
        { type: 'text', readOnly: true },   // 0 A - COD_TIPO_CONCEP
        { type: 'text', readOnly: true },   // 1 B - COD_TIPO_REGIS
        { type: 'text', readOnly: true },   // 2 C - FLG_IS_TOTAL
        { type: 'text', readOnly: true },   // 3 D - Código
        { type: 'text', readOnly: true },   // 4 E - Concepto cuenta contable
        {                                   // 5 F - valor fórmula
            type: 'numeric',
            readOnly: lectura,
            numericFormat: {
                pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                culture: HANDS_ON_TABLE_CUSTOM_CULTURE
            }
        },
    ];
}
let conceptos: IConcepto[];
let conceptosPR: IConcepto[];
@Component({
    selector: 'jhi-projections',
    templateUrl: './projections.component.html',
    styleUrls: ['projections.scss'],
    providers: [NgbAccordionConfig] // add the NgbAccordionConfig to the component providers
})
export class ProjectionsComponent implements OnInit, AfterViewInit {
    datosConfImpresion: any;
    private ngbModalSelectionPrint: NgbModalRef;
    usuario: ITurbineUser;
    operatorCode: number;
    role: string;
    breakdownFilterRequestDTO: IBreakdownFilterRequestDTO = new BreakdownFilterRequestDTO();
    currentAccount: Account;
    USER_ROLE_PLAT_DESGLOSE_JEFE = USER_ROLE_PLAT_DESGLOSE_JEFE;
    USER_ROLE_PLAT_DESGLOSE_REVISOR = USER_ROLE_PLAT_DESGLOSE_REVISOR;
    USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR = USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR;
    message: string;
    extraDataDesglose: IExtraDataDesglose = new ExtraDataDesglose();
    simpleComboboxTypes: IDescripcionCodigo[] = [];
    handOnTableModelDataInstanceId = 'handOnTableInstanceIdDataModel';
    handOnTableProjectionInstanceId = 'handOnTableProjectionInstanceId';
    handOnTableProjectionCalculadoraInstanceId = 'handOnTableProjectionCalculadoInstanceId';
    tabProjectionModelDataId = 'tab-projection-data-model';
    tabProjectionId = 'tab-projection';
    data: any = [[]];
    dataProyecciones: any = [[]];
    dataProyeccionesCalculo: any = [[]];
    breakdownRecoverResponseDTO: IBreakdownRecoverResponseDTO;
    public isCollapsedDataPanel = false;
    public isCollapsedClientDataPanel = false;
    public isCollapsedActionsPanel = false;
    private ngbModalRef: NgbModalRef;
    CODTABLA_TIPO_MONEDAS = CODTABLA_TIPO_MONEDAS;
    CODTABLA_SUBTIPO_DESGLOSE = CODTABLA_SUBTIPO_DESGLOSE;
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE = CODTABLA_TIPO_PLANTILLAS_DESGLOSE;
    CODTABLA_TIPO_DESGLOSE = CODTABLA_TIPO_DESGLOSE;
    CODTABLA_TIPO_PLANTILLAS_PROYECCION = CODTABLA_TIPO_PLANTILLAS_PROYECCION;
    CODTABLA_UNIDAD_MONETARIA = CODTABLA_UNIDAD_MONETARIA;
    sort = 'codGenerico,ASC';
    // Elementos de la interfaz
    @ViewChild('fieldRut') fieldRut: ElementRef;
    @ViewChild('fieldRutVerificationNumber') fieldRutVerificationNumber: ElementRef;
    @ViewChild('fieldName') fieldName: ElementRef;
    @ViewChild('fieldActivity') fieldActivity: ElementRef;
    @ViewChild('fieldBreakdownTemplateType') fieldBreakdownTemplateType: ElementRef;
    @ViewChild('clientDataDiv') clientDataDivContainer: ElementRef;
    @ViewChild('fieldBreakdownSubTypeDesc') fieldBreakdownSubTypeDesc: ElementRef;
    @ViewChild('buttonEditar') buttonEditar: ElementRef;
    @ViewChild('buttonCalcular') buttonCalcular: ElementRef;
    @ViewChild('buttonSave') buttonSave: ElementRef;
    @ViewChild('buttonCerrar') buttonCerrar: ElementRef;
    @ViewChild('buttonPrint') buttonPrint: ElementRef;
    periods: IPeriodo[] = [];
    breakdown: IDesglose = new Desglose();
    state: { state: string, stateClass: string } = { state: '', stateClass: '' };
    // Configuración de hot-table 1
    colHeaders: string[];
    colHeadersProjection: string[];
    colHeadersProjectionCalculadora: string[];
    colWidths: string[];
    columns: any[];
    hiddenRowsDM: number[] = [];
    hiddenRowsOTHER: number[] = [];
    columnsProjection: any[];
    columnsProjectionCalculadora: any[];
    options: Handsontable.GridSettings;
    COLUMN_LETTER_GLS_FORMULA_VAL_1 = 'F';
    COLUMN_LETTER_GLS_FORMULA_DEF_1 = 'G';
    COLUMN_LETTER_GLS_FORMULA_PRC_1 = 'H';
    FLG_IS_TOTAL_0 = 0;
    FLG_IS_TOTAL_1 = 1;
    private contMultiregistro = 0;
    public isDivFormulaValueHidden = false;
    selectedPeriodYear?: Moment;
    projectionYears?: number;
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;
    public currentDestroyOnHide = false;
    public currentJustify = 'justified';
    public HANDS_ON_TABLE_LICENSE_KEY = HANDS_ON_TABLE_LICENSE_KEY;
    codCliente;
    codTipoDesglose;
    codSubtipoDesglose;
    codMoneda;
    codUnidMoneda;
    codPlantilla;
    clientName = '';
    nameCliente = '';
    fecPeriodo;
    codPlantillaProyect;
    public fila = 0;
    public columna;
    public periodo = 1;
    estadoPry;
    private questionMessageSubscription: Subscription;
    constructor(public activeModal: NgbActiveModal,private loaderService: LoaderService, private router: Router, private activatedRoute: ActivatedRoute, private config: NgbAccordionConfig, private modalService: NgbModal, private descripcionCodigoService: DescripcionCodigoService,
        private jhiAlertService: JhiAlertService, private customSqlService: CustomSqlService, private sessionStorage: SessionStorageService,
        private clienteService: ClienteService, private sweetAlert2: Sweetalert2Component, private renderer: Renderer2, private tablaValorService: TablaValorService,
        private filterDescripcionCodigoPipe: FilterDescripcionCodigoPipe, private transformIvaPipe: TransformIvaPipe, private validationUtils: ValidationUtils,
        private desgloseService: DesgloseService, private translateService: TranslateService, private conceptoService: ConceptoService, private datePipe: DatePipe,
        private hotRegisterer: HotTableRegisterer, private periodoService: PeriodoService, private rutaActiva: ActivatedRoute, private reportService: ReportService,
        private valPeriodoDesgImprService: ValPeriodoDesgImprService, private periodoDesgImpresionService: PeriodoDesgImpresionService,
        private desgloseImpresionService: DesgloseImpresionService, private principal: Principal, private elementRef: ElementRef,
        private rendererOld: Renderer, private projectionService: ProjectionService,private spinner: NgxSpinnerService) {

        // customize default values of accordions used by this component tree
        config.closeOthers = false;
        // config.type = 'custom-panel-header-bg';
        this.message = 'BreakdownComponent message';
        this.columns = initColumnas(true);
        this.initTableConfigData();
    }

    ngOnInit() {
        this.controlBotones(0);
        this.usuario = this.sessionStorage.retrieve(SESSION_VARIABLE_USUARIO);
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (this.currentAccount.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_EJECUTOR).length > 0) {
                this.role = USER_ROLE_PLAT_DESGLOSE_EJECUTOR;
            }
            if (this.currentAccount.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_REVISOR).length > 0) {
                this.role = USER_ROLE_PLAT_DESGLOSE_REVISOR;
            }
            if (this.currentAccount.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_JEFE).length > 0) {
                this.role = USER_ROLE_PLAT_DESGLOSE_JEFE;
            }
        });
        const typesParam = this.CODTABLA_TIPO_PLANTILLAS_DESGLOSE + ',' + this.CODTABLA_SUBTIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_MONEDAS
            + ',' + this.CODTABLA_TIPO_PLANTILLAS_PROYECCION + ',' + this.CODTABLA_UNIDAD_MONETARIA;
        this.loadIDescripcionCodigo(typesParam, this.sort);
        const route = this.activatedRoute.snapshot;
        this.breakdownFilterRequestDTO.rut = this.codCliente;
        this.colHeadersConfig();
        const fecha = this.fecPeriodo;
        this.selectedPeriodYear = fecha;
        this.codPlantillaProyect = '50000';
        this.breakdown.codPlantillaProyeccion = this.codPlantillaProyect;
        this.breakdown.codCliente = this.codCliente;
        this.breakdown.codTipoDes = this.codTipoDesglose;
        this.breakdown.codSubtipoDes = this.codSubtipoDesglose;
        this.breakdown.codMoneda = this.codMoneda;
        this.breakdown.codPlantilla = this.codPlantilla;
        this.breakdown.codUnidMoneda = this.codUnidMoneda;
        this.breakdown.fecPeriodo1Rpt = fecha;
        this.recoverHistorico(this.codCliente, this.codTipoDesglose, this.codSubtipoDesglose, this.codMoneda, this.codPlantilla, fecha);
        this.loadProyeccionesTemplate(this.codPlantillaProyect);
        this.spinner.show();
        setTimeout(() => {
            this.spinner.hide();
          }, 5000);
    }
    initTableConfigData() {
        // Configuración de hot-table
        this.colHeadersConfig();
        this.colWidths = ['0%', '0%', '0%', '35%', '350%', '200%'];
        // columnas
        this.options = {
            allowInvalid: false,
            // enterBeginsEditing: false,
            // validator: TODO: Aplicar para hacer validaciones más específicas en las columnas
            renderAllRows: false,
            hiddenRows: {
                indicators: true// DEBUG_INFO_ENABLED,
            },
            stretchH: 'none',
            hiddenColumns: {
                // set columns that are hidden by default
                columns: [COLUMN_NUMBER_COD_TIPO_CONCEP, COLUMN_NUMBER_COD_TIPO_REGIS, COLUMN_NUMBER_FLG_IS_TOTAL],
                // show where are hidden columns
                indicators: true// DEBUG_INFO_ENABLED
            },
            autoColumnSize: false,
            autoRowSize: false,
            width: '100%',
            height: 700,
            colHeaders: true,
            rowHeaders: true,
            colWidths: this.colWidths,
            allowInsertRow: true,
            allowRemoveRow: true,
            /*fixedColumnsLeft: 6,*/
            /*viewportRowRenderingOffset: 350,
            viewportColumnRenderingOffset: 30,*/
            allowEmpty: true
        };
        // if(DEBUG_INFO_ENABLED) { // TODO: Cambiar los true y este if para dejarlos con valor DEBUG_INFO_ENABLED
        this.options.contextMenu = ['hidden_rows_show', 'hidden_rows_hide', 'hidden_columns_hide', 'hidden_columns_show'];
        // }
    }
    colHeadersConfig() {
        this.colHeaders = [
            'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
            'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
            'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
            this.translateService.instant('breakdown.hotTable.code'),
            this.translateService.instant('breakdown.hotTable.ccc'),
            this.fecPeriodo,
        ];
    }
    ngAfterViewInit(): void {
        this.hideEvaluationMessage();
    }

    /**
     * Ocultar/eliminar información de licencia del componente.
     */
    private hideEvaluationMessage() {
        const divElement: HTMLElement = document.getElementById('hot-display-license-info');
        if (!isNullOrUndefined(divElement)) {
            divElement.remove();
        }
        // divElement.hidden = true;
    }
    loadIDescripcionCodigo(codtabla: string, sort: string /*, destinationArray: number*/) {
        this.descripcionCodigoService.findByCodTablasIn(codtabla, sort).subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigos(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    private assignDescripcionCodigos(data: IDescripcionCodigo[], headers: HttpHeaders) {
        this.simpleComboboxTypes = data;
    }

    /**
     * Método que se ejecuta después de cargar la metadata de la tabla. Se formatean las filas.
     * @param hotInstance
     */
    afterLoadDataMethod(hotInstance: any) {

    }
    hotUpdateSettings(hotInstance: any, lectura: boolean) {
        hotInstance.updateSettings({
            cells: (row, col) => {
                const cellProperties: any = {};
                if (!isNullOrUndefined(hotInstance.getSourceData()[row])) {
                    if ((isNullOrUndefined(hotInstance.getSourceData()[row][COLUMN_NUMBER_COD_TIPO_REGIS]) || hotInstance.getSourceData()[row][COLUMN_NUMBER_COD_TIPO_REGIS] === '')
                        && (!isNullOrUndefined(hotInstance.getSourceData()[row][COLUMN_NUMBER_CCC]))) {
                        cellProperties.renderer = headerGroupTextRenderer;
                        cellProperties.readOnly = true;
                    }
                    if (hotInstance.getDataAtCell(row, COLUMN_NUMBER_COD_TIPO_CONCEP) !== COD_TIPO_CONCEP_PROY_DM) {
                        cellProperties.readOnly = true;
                    }
                    if (hotInstance.getDataAtCell(row, COLUMN_NUMBER_COD_TIPO_CONCEP) === COD_TIPO_CONCEP_PROY_DM) {
                        cellProperties.readOnly = lectura;
                    }
                }
                return cellProperties;
            },
        });
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onClickProject() {
        this.projectionYearsOnBlur();
    }

    onClickEditar() {
        const historicDataRequestDTO = new ProjectionHistoricDataRequestDTO(this.codCliente, this.codTipoDesglose, this.codSubtipoDesglose,
            this.codMoneda, this.codPlantilla, this.breakdown.fecPeriodo1Rpt);
        this.projectionService.editarProyeccion(historicDataRequestDTO).subscribe((datos: HttpResponse<IPeriodo>) => {
                this.loaderService.isLoading.next(true);
                const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableProjectionInstanceId);
                if (hotInstance1) {
                    setTimeout(() => {
                        this.hotUpdateSettings(hotInstance1, false);
                        this.loaderService.isLoading.next(false);
                        hotInstance1.render();
                    }, 500);
                }
                this.estadoPry = 'EN EDICION';
                this.controlBotones(1);
            },
            (datos: HttpErrorResponse) => {
                // this.onError(datos.message);
            });
    }
    async delay(ms: number) {
        await new Promise(resolve => setTimeout(()=>resolve(), ms)).then(()=>console.log(ms));
    }

    onClickCalcular() {
        this.spinner.show();
        setTimeout(() => {
            const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableProjectionInstanceId);
            for (let i = 0; i < this.projectionYears-1; i++) {
                this.delay(((i)*2500)).then(() => {
                    hotInstance1.selectCell(1, (6+i));
                    jQuery('#divTableProjection').click();
                    jQuery('#divCarga').html('Calculando Periodo: ' + (i + 1));
                    jQuery('#divTableProjection').click();
                    this.estadoPry = 'Calculando Periodo: ' + (i + 1);
                    this.calculadora(i).subscribe(res => {
                        this.periodo = res;
                    });
                    jQuery('#divTableProjection').click();
                    if (i === this.projectionYears - 2) {
                        this.spinner.hide();
                        this.controlBotones(2);
                        jQuery('#divCarga').html('Calculando Periodo: 1 ');
                    }
                });
            }
        }, 200);
    }
     calculadora(i:number): Observable<number> {
        const periodo = new Subject<number>();
            const hotInstance1 =  this.hotRegisterer.getInstance(this.handOnTableProjectionInstanceId);
            const hotInstanceCalculadora =   this.hotRegisterer.getInstance(this.handOnTableProjectionCalculadoraInstanceId);
               conceptosPR.forEach((concept, index) => {
                    if (concept.codTipoConcep !== COD_TIPO_CONCEP_PROY_DM) {
                        this.dataProyeccionesCalculo[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0]] =  hotInstance1.getData()[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i];
                    }
                    if (concept.codTipoConcep === COD_TIPO_CONCEP_PROY_DM) {
                        this.dataProyeccionesCalculo[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + 1] =  hotInstance1.getData()[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i + 1];
                        this.dataProyeccionesCalculo[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + 2] =  hotInstance1.getData()[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i + 1];
                        this.dataProyeccionesCalculo[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + 3] =  hotInstance1.getData()[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i + 2];
                    }
                    if (index === conceptosPR.length - 1) {
                        const columna: number = (COLUMN_NUMBER_GLS_FORMULA_VAL[0] + 1);
                        const columna2: number = (COLUMN_NUMBER_GLS_FORMULA_VAL[0] + 2);
                        const fechaInicio: any = new Date();
                        console.log('Inicio calculando ' + (i+1));
                        hotInstanceCalculadora.getPlugin('formulas').recalculateFull();
                        const fechaFin: any = new Date();
                        // hotInstanceCalculadora.render();
                        console.log('Fin calculando ' + (i+1) + ' --> Tiempo de calculo +' + (fechaFin - fechaInicio) / 1000);
                        for (let j = 0; j < conceptosPR.length; j++) {
                            this.fila = j;
                            if (conceptosPR[j].codTipoConcep !== COD_TIPO_CONCEP_PROY_DM) {
                                this.dataProyecciones[ j ][ columna + i ] = hotInstanceCalculadora.getData()[ j ][ columna];
                                // console.log('escribiendo fila: ' + j + ' columna: ' + columna + ' data: ' + hotInstanceCalculadora.getData()[j][columna]);
                                if(!isNullOrUndefined(conceptosPR[j].glsFormulaPrc) && conceptosPR[j].glsFormulaPrc!== '') {
                                    if(i>0) {
                                        this.dataProyecciones[ j ][ columna + i ] = hotInstanceCalculadora.getData()[ j ][ columna2 ];
                                    }
                                }
                            }
                        }
                    }
                });
            periodo.next(i);
            return  periodo.asObservable();
    }

    onClickGrabar() {
        this.loaderService.isLoading.next(true);
        setTimeout(() => {
            const hotInstance = this.hotRegisterer.getInstance(this.handOnTableProjectionInstanceId);
            if (hotInstance) {
                const visualData: any = hotInstance.getData();
                this.extraDataDesglose.canValidate = true;
                this.extraDataDesglose.conceptos = conceptosPR;
                this.extraDataDesglose.data = hotInstance.getData();
                this.extraDataDesglose.visualData = visualData;
                this.desgloseService.saveProy(this.breakdown, this.extraDataDesglose).subscribe(
                    (rpt: HttpResponse<IDesglose>) => {
                        this.onSuccessSave(rpt.body, rpt.headers);
                        this.estadoPry = 'EN PROCESO';
                        this.buttonCerrar.nativeElement.disabled = false;
                    },
                    (rpt: HttpErrorResponse) => this.onError(rpt.message)
                );
            }

        }, 200);
    }
    private onSuccessSave(body: IDesglose, headers: HttpHeaders) {
        this.controlBotones(0);
    }
    projectionYearsOnBlur() {
        if (!isNullOrUndefined(this.projectionYears)) {
            if (this.projectionYears <= MAX_PROJECTION_YEARS) {
                const year = this.selectedPeriodYear.year();
                this.colHeadersProjection = [
                    'COD_TIPO_CONCEP', // PROY_DM, BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN           // A
                    'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO                                  // B
                    'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0                         // C
                    this.translateService.instant('breakdown.hotTable.code'),                      // D
                    this.translateService.instant('breakdown.hotTable.ccc'),                       // E
                ];
                this.colHeadersProjectionCalculadora = [
                    'COD_TIPO_CONCEP', // PROY_DM, BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN           // A
                    'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO                                  // B
                    'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0                         // C
                    this.translateService.instant('breakdown.hotTable.code'),                      // D
                    this.translateService.instant('breakdown.hotTable.ccc'),                       // E
                    'F',
                    'G',
                    'H',
                    'I'
                ];
                this.columnsProjection = [
                    { type: 'text', readOnly: true },   // A - COD_TIPO_CONCEP
                    { type: 'text', readOnly: true },   // B - COD_TIPO_REGIS
                    { type: 'text', readOnly: true },   // C - FLG_IS_TOTAL
                    { type: 'text', readOnly: true },  // D - Concepto cuenta contable
                    { type: 'text', readOnly: true },  // E - Concepto cuenta contable
                ];
                this.columnsProjectionCalculadora = [
                    { type: 'text', readOnly: true },   // A - COD_TIPO_CONCEP
                    { type: 'text', readOnly: true },   // B - COD_TIPO_REGIS
                    { type: 'text', readOnly: true },   // C - FLG_IS_TOTAL
                    { type: 'text', readOnly: true },  // D - Concepto cuenta contable
                    { type: 'text', readOnly: true },   // E - Concepto cuenta contable
                    {
                        type: 'numeric',
                        numericFormat: {
                            pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                            culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                        },
                        readOnly: true,
                        renderer: 'zeroValueRenderer'
                    },
                    {
                        type: 'numeric',
                        numericFormat: {
                            pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                            culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                        },
                        readOnly: true,
                        renderer: 'zeroValueRenderer'
                    },
                    {
                        type: 'numeric',
                        numericFormat: {
                            pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                            culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                        },
                        readOnly: true,
                        renderer: 'zeroValueRenderer'
                    },
                    {
                        type: 'numeric',
                        numericFormat: {
                            pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                            culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                        },
                        readOnly: true,
                        renderer: 'zeroValueRenderer'
                    }
                ];
                for (let i = 0; i <= this.projectionYears; i++) {
                    if (i === 0) {
                        this.colHeadersProjection.push(this.translateService.instant('desgloseApp.projections.hotTable.col2Header', { YEAR: (year).toString() }));
                        // this.colHeadersProjection.push((year + i).toString() + '\n' + 'Hist.');
                        this.colWidths.push('150%');
                        this.columnsProjection.push(
                            {
                                type: 'numeric',
                                numericFormat: {
                                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                                },
                                readOnly: true,
                                renderer: 'zeroValueRenderer'
                            });
                    } else {
                        this.colWidths.push('150%');
                        this.colHeadersProjection.push(this.translateService.instant('desgloseApp.projections.hotTable.col3Header', { YEAR: (year + i).toString() }));
                        this.columnsProjection.push(
                            {
                                type: 'numeric',
                                numericFormat: {
                                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                                },
                                readOnly: false,
                                renderer: 'zeroValueRenderer'
                            });
                    }
                }
            } else {
                this.sweetAlert2.mensajeError('projections.validation.title', 'desgloseApp.projections.validation.projectionYearsBiggerThanMaxValue',
                    { projectionYearsMax: MAX_PROJECTION_YEARS });
            }
        }
        conceptosPR.forEach((concept, index) => {
            for (let i = 0; i <= 2; i++) {
                if (i > 0) {
                    const regex = P_ANT;
                    const regex1 = P_SIG;
                    if(i === 1) {
                        if (!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef !== '') {
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = concept.glsFormulaDef.toString();
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ].toString()
                            .replace(new RegExp(this.COLUMN_LETTER_GLS_FORMULA_DEF_1, 'g'), this.asignLetra(COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i));
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ].toString()
                            .replace(new RegExp(regex, 'g'), this.asignLetra((COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i) - 1));
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ].toString()
                                .replace(new RegExp(regex1, 'g'), this.asignLetra((COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i) + 2));
                        }
                    }
                    if(i === 2) {
                        if (!isNullOrUndefined(concept.glsFormulaPrc) && concept.glsFormulaPrc!== '') {
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = concept.glsFormulaPrc.toString();
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ].toString()
                            .replace(new RegExp(this.COLUMN_LETTER_GLS_FORMULA_DEF_1, 'g'), this.asignLetra(COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i));
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ].toString()
                            .replace(new RegExp(regex, 'g'), this.asignLetra((COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i - 2)));
                            this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ] = this.dataProyeccionesCalculo[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i ].toString()
                            .replace(new RegExp(regex1, 'g'), this.asignLetra((COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] + i +1)));
                        }
                    }
                    if (concept.codTipoConcep === COD_TIPO_CONCEP_PROY_DM) {
                        if (!isNullOrUndefined(concept.codTipoRegis) && concept.codTipoRegis !== '') {
                            if (isNullOrUndefined(this.dataProyecciones[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i])) {
                                this.dataProyeccionesCalculo[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i] = 0;
                            }
                        }
                    }
                }
            }
            this.asignData(false);
            if (concept.codTipoConcep === COD_TIPO_CONCEP_PROY_DM) {
                this.hiddenRowsDM.push(index);
            } else {
                this.hiddenRowsOTHER.push(index);
            }
        });
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableProjectionInstanceId);
        if(hotInstance) {
            this.hotUpdateSettings(hotInstance, true);
        }
    }

    private asignLetra(pos: number) {
        switch (pos) {
            case 5: {
                return 'F';
            }
            case 6: {
                return 'G';
            }
            case 7: {
                return 'H';
            }
            case 8: {
                return 'I';
            }
            case 9: {
                return 'J';
            }
            case 10: {
                return 'K';
            }
            case 11: {
                return 'L';
            }
            case 12: {
                return 'M';
            }
            case 13: {
                return 'N';
            }
            case 14: {
                return 'O';
            }
            case 15: {
                return 'P';
            }
            case 16: {
                return 'Q';
            }
            case 17: {
                return 'R';
            }
            case 18: {
                return 'S';
            }
            case 19: {
                return 'T';
            }
            case 20: {
                return 'U';
            }
            case 21: {
                return 'V';
            }
            case 22: {
                return 'W';
            }
            case 23: {
                return 'X';
            }
            case 24: {
                return 'Y';
            }
            case 25: {
                return 'Z';
            }
        }
    }

    asignHistorico() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableModelDataInstanceId);
        for (let i = 0; i < this.dataProyecciones.length; i++) {
            for (let j = 0; j < this.data.length; j++) {
                if (this.dataProyecciones[i][COLUMN_NUMBER_COD_TIPO_CONCEP] === this.data[j][COLUMN_NUMBER_COD_TIPO_CONCEP]) {
                    if (this.dataProyecciones[i][COLUMN_NUMBER_COD_CONCEPT] === this.data[j][COLUMN_NUMBER_COD_CONCEPT]) {
                        if (hotInstance) {
                            const data = hotInstance.getData();
                            this.dataProyecciones[i][COLUMN_NUMBER_GLS_FORMULA_VAL[0]] = !isNaN(data[j][COLUMN_NUMBER_GLS_FORMULA_VAL[0]]) ? data[j][COLUMN_NUMBER_GLS_FORMULA_VAL[0]] : 0;
                        }
                    }
                }
            }
        }
    }
    asignData(calcularYears: boolean) {
        if (calcularYears) {
            this.projectionYears = 0;

            this.breakdownRecoverResponseDTO.extraDataDTO.periodos[0].valPeriodoProyeccionDesgloseDTOList.forEach((valPeriodoProyeccionDesglose: IValPeriodoProyeccionDesglose, index) => {
                if (!isNullOrUndefined(this.breakdownRecoverResponseDTO.extraDataDTO.periodos[0].valPeriodoProyeccionDesgloseDTOList[index + 1])) {
                    if (!isNullOrUndefined(valPeriodoProyeccionDesglose.fecPeriodoPry) && !isNullOrUndefined(this.breakdownRecoverResponseDTO.extraDataDTO.periodos[0].valPeriodoProyeccionDesgloseDTOList[index + 1].fecPeriodoPry)) {
                        if (valPeriodoProyeccionDesglose.fecPeriodoPry.year() !== this.breakdownRecoverResponseDTO.extraDataDTO.periodos[0].valPeriodoProyeccionDesgloseDTOList[index + 1].fecPeriodoPry.year()) {
                            this.projectionYears = this.projectionYears + 1;
                        }
                    }
                }
            });
            this.onClickProject();
        }
        conceptosPR.forEach((concept, index) => {
            for (let i = 0; i <= this.projectionYears; i++) {
                if (i > 0) {
                    if (!isNullOrUndefined(concept.codTipoRegis) && concept.codTipoRegis !== '') {
                        this.breakdownRecoverResponseDTO.extraDataDTO.periodos[0].valPeriodoProyeccionDesgloseDTOList.forEach((valPeriodoProyeccionDesglose: IValPeriodoProyeccionDesglose) => {
                            if (!isNullOrUndefined(valPeriodoProyeccionDesglose.fecPeriodoPry)) {
                                if (valPeriodoProyeccionDesglose.fecPeriodoPry.year() === (this.selectedPeriodYear.year() + i) &&
                                    valPeriodoProyeccionDesglose.codTipoConcepPry === this.dataProyecciones[index][COLUMN_NUMBER_COD_TIPO_CONCEP] &&
                                    valPeriodoProyeccionDesglose.codConcepto === this.dataProyecciones[index][COLUMN_NUMBER_COD_CONCEPT]
                                ) {
                                    this.dataProyecciones[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i] = valPeriodoProyeccionDesglose.valConceptoPry;
                                }
                            }
                        });
                    }
                }
            }
        });
        conceptosPR.forEach((concept, index) => {
            for (let i = 0; i <= this.projectionYears; i++) {
                if (i > 0) {
                    if (concept.codTipoConcep === COD_TIPO_CONCEP_PROY_DM) {
                        if (!isNullOrUndefined(concept.codTipoRegis) && concept.codTipoRegis !== '') {
                            if (isNullOrUndefined(this.dataProyecciones[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i]) || this.dataProyecciones[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i] === '') {
                                this.dataProyecciones[index][COLUMN_NUMBER_GLS_FORMULA_VAL[0] + i] = 0;
                            }
                        }
                    }
                }
            }
        });
    }
    private recoverHistorico(codCliente: string, codTipoDesglose: number, codSubtipoDesglose: number, codMoneda: string, codPlantilla: string, fecPeriodo: Moment) {
        const conceptRequestDTO: IConceptRequestDTO = new ConceptRequestDTO(parseInt(codPlantilla, 10));
        conceptRequestDTO.conceptTypeCodesIn = [COD_TIPO_CONCEP_BALANCE, COD_TIPO_CONCEP_EST_RESULT, COD_TIPO_CONCEP_GEN_FONDOS, COD_TIPO_CONCEP_IND_FINAN];
        conceptRequestDTO.orderByDirection = OrderByDirection.ASC;
        conceptRequestDTO.orderByFields = [FIELD_NAME_COD_TIPO_CONCEP, FIELD_NAME_NUM_SECUENCIA];
        conceptRequestDTO.columnLetterGlsFormulaVal1 = this.COLUMN_LETTER_GLS_FORMULA_VAL_1;
        conceptRequestDTO.flgConActivo = VALUE_IS_ACTIVE_CONCEPT_TRUE;
        this.conceptoService.findByTemplateConceptsCodesIn(conceptRequestDTO).subscribe(
            (res: HttpResponse<IBreakdownFilterConceptDto>) => {
                const ConceptosValidate: IBreakdownFilterConceptDto = res.body;
                conceptos = ConceptosValidate.conceptoDTOList;
                if (!isNullOrUndefined(conceptos) && conceptos.length > 0) {
                    this.data = [];
                    // let lastGroup = '';
                    let i = 0;
                    conceptos.forEach((concept, index) => {
                        if (isNullOrUndefined(concept.codTipoRegis) || concept.codTipoRegis === '') { // Si es un encabezado de grupo
                            // El encabezado se agrega uno solo en todos los períodos
                            this.data.push([
                                concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                '', // G
                                this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                null, null, null, null, null, null, null, null, null, null, null, null
                            ]);
                        } else {
                            if (concept.flgIsTotal === 0) {
                                this.data.push([
                                    concept.codTipoConcep,
                                    concept.codTipoRegis,
                                    concept.flgIsTotal,
                                    concept.codConcepto,
                                    concept.glsConcepto,
                                    0
                                ]);
                            } else {
                                this.data.push([
                                    concept.codTipoConcep,
                                    concept.codTipoRegis,
                                    concept.flgIsTotal,
                                    concept.codConcepto,
                                    concept.glsConcepto,
                                    0
                                ]);
                            }
                        }
                        i = i + 1;
                    });
                } else {
                    this.data = [[]];
                }
                // Llamamos al servicio de recuperar todos los datos del desglose
                const hotInstance = this.hotRegisterer.getInstance(this.handOnTableModelDataInstanceId);
                if (hotInstance) {
                    const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
                    const historicDataRequestDTO = new ProjectionHistoricDataRequestDTO(codCliente, codTipoDesglose, codSubtipoDesglose,
                        codMoneda, codPlantilla, fecPeriodo);
                    this.projectionService.getHistoricData(historicDataRequestDTO).subscribe(
                        (resp: HttpResponse<IBreakdownRecoverResponseDTO>) => this.onSuccessRecoverBreakdown(resp.body, resp.headers),
                        (resp: HttpErrorResponse) => this.onError(resp.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    private onSuccessRecoverBreakdown(body: IBreakdownRecoverResponseDTO, headers: HttpHeaders) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableModelDataInstanceId);
        this.breakdownRecoverResponseDTO = body;
        if (this.breakdownRecoverResponseDTO && this.breakdownRecoverResponseDTO.extraDataDTO) {
            this.extraDataDesglose = this.breakdownRecoverResponseDTO.extraDataDTO;
            this.codUnidMoneda = this.breakdownRecoverResponseDTO.codUnidMoneda;
            if(this.extraDataDesglose.periodos[0].codEstadoPry === ESTADO_EN_EDICION) {
                this.estadoPry = 'EN EDICION';
            }
            if(this.extraDataDesglose.periodos[0].codEstadoPry === ESTADO_BORRADOR_EN_PROCESO) {
                this.estadoPry = 'EN PROCESO';
            }
            if(this.extraDataDesglose.periodos[0].codEstadoPry === ESTADO_DEFINITIVO) {
                this.estadoPry = 'VALIDADO';
                this.buttonCerrar.nativeElement.disabled = true;
            }
            if (!this.extraDataDesglose.monthsPeriod) {
                this.extraDataDesglose.monthsPeriod = [];
            }
            if (!this.extraDataDesglose.deflatorPeriod) {
                this.extraDataDesglose.deflatorPeriod = [];
            }
            if (!this.extraDataDesglose.deflatorPeriodStr) {
                this.extraDataDesglose.deflatorPeriodStr = [];
            }
            if (!this.extraDataDesglose.periodRating) {
                this.extraDataDesglose.periodRating = [];
            }
            if (!this.extraDataDesglose.fecPeriodo) {
                this.extraDataDesglose.fecPeriodo = [];
            }
            if (!this.extraDataDesglose.matrizConImp) {
                this.extraDataDesglose.matrizConImp = [];
            }
            if (!this.extraDataDesglose.periodos) {
                this.extraDataDesglose.periodos = [];
            } else {
                if (hotInstance) {
                    setTimeout(() => {
                        const conceptRequestDTO1: IConceptRequestDTO = new ConceptRequestDTO(this.codPlantillaProyect);
                        conceptRequestDTO1.conceptTypeCodesIn = [COD_TIPO_CONCEP_PROY_DM, COD_TIPO_CONCEP_PROY_DM_EST_RESULT, COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA, COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL];
                        conceptRequestDTO1.orderByDirection = OrderByDirection.ASC;
                        conceptRequestDTO1.orderByFields = [FIELD_NAME_COD_TIPO_CONCEP, FIELD_NAME_NUM_SECUENCIA];
                        conceptRequestDTO1.columnLetterGlsFormulaVal1 = this.COLUMN_LETTER_GLS_FORMULA_VAL_1;
                        conceptRequestDTO1.flgConActivo = VALUE_IS_ACTIVE_CONCEPT_TRUE;
                        conceptRequestDTO1.templateCodeRelacionada = this.codPlantilla;
                        this.conceptoService.findByTemplateConceptsCodesIn(conceptRequestDTO1).subscribe(
                            (res: HttpResponse<IBreakdownFilterConceptDto>) => {
                                const ConceptosValidate: IBreakdownFilterConceptDto = res.body;
                                const conceptosPRY = ConceptosValidate.conceptoDTOList;
                                if (!isNullOrUndefined(conceptosPR) && conceptosPR.length > 0) {
                                    // let lastGroup = '';
                                    let i = 0;
                                    conceptosPRY.forEach((concept, index) => {
                                        if (isNullOrUndefined(concept.codTipoRegis) || concept.codTipoRegis === '') { // Si es un encabezado de grupo
                                            // El encabezado se agrega uno solo en todos los períodos
                                            this.data.push([
                                                concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                                '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                                concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                                '', // G
                                                this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                                null, null, null, null, null, null, null, null, null, null, null, null
                                            ]);
                                        } else {
                                            if (concept.flgIsTotal === 0) {
                                                this.data.push([
                                                    concept.codTipoConcep,
                                                    concept.codTipoRegis,
                                                    concept.flgIsTotal,
                                                    concept.codConcepto,
                                                    concept.glsConcepto,
                                                    concept.glsFormulaVal
                                                ]);
                                            } else {
                                                this.data.push([
                                                    concept.codTipoConcep,
                                                    concept.codTipoRegis,
                                                    concept.flgIsTotal,
                                                    concept.codConcepto,
                                                    concept.glsConcepto,
                                                    concept.glsFormulaVal
                                                ]);
                                            }
                                        }
                                        i = i + 1;
                                    });
                                }
                                hotInstance.getPlugin('formulas').recalculateFull();
                                this.asignMultiregistro(this.extraDataDesglose);
                                if (this.breakdownRecoverResponseDTO.extraDataDTO.periodos.length <= 4) {
                                    this.extraDataDesglose.periodos.forEach((period, index) => {
                                        this.assignValPrdToTable(period.valPeriodoDesgloseDTOList, index);
                                    });
                                }
                                hotInstance.getPlugin('formulas').recalculateFull();
                                this.asignHistorico();
                                this.asignData(true);
                            },
                            (res: HttpErrorResponse) => this.onError(res.message)
                        );
                    }, 200);
                }
            }
            if (!this.extraDataDesglose.conceptos) {
                this.extraDataDesglose.conceptos = [];
            }
            if (!this.extraDataDesglose.empresaConsolidadas) {
                this.extraDataDesglose.empresaConsolidadas = [];
            }
            if (!this.extraDataDesglose.periodoDesgConsols) {
                this.extraDataDesglose.periodoDesgConsols = [];
            }
            if (!this.extraDataDesglose.empConsolPeriodoDesgConsolList) {
                this.extraDataDesglose.empConsolPeriodoDesgConsolList = [];
            }
            if (!this.extraDataDesglose.auditors) {
                this.extraDataDesglose.auditors = [];
            }
        }
    }
    private renderHot(hotInstance: any, count: number) {
        setTimeout(() => {
            setTimeout(() => {
                hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                setTimeout(() => {
                    hotInstance.selectCell(count, COLUMN_NUMBER_CCC);
                    setTimeout(() => {
                        hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                    }, 50);
                }, 50);
            }, 50);
        }, 10);
    }
    private assignValPrdToTable(valPeriodoDesgloseDTOList: IValPeriodoDesglose[], periodNumber: number) {
        if (!isNullOrUndefined(valPeriodoDesgloseDTOList) && valPeriodoDesgloseDTOList.length > 0) {
            this.recorreConceptosAsignaData(valPeriodoDesgloseDTOList, periodNumber);
            this.recorreConceptosAsignGeneracionFondosIndicadoresFinancieros(valPeriodoDesgloseDTOList, periodNumber);
        } else {
            for (let k = 0; k < conceptos.length; k++) {
                const concept = conceptos[k];
                if ((concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)) {
                    if (!isNullOrUndefined(concept.codTipoRegis)) {
                        if (!isNullOrUndefined(this.data[k])) {
                            this.data[k][COLUMN_NUMBER_GLS_FORMULA_VAL[periodNumber]] = 0;
                        }
                    }
                }
            }
        }
    }
    private asignMultiregistro(extraDataDesglose: IExtraDataDesglose) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableModelDataInstanceId);
        let number = 0;
        let cuentaMultiregistros = 0;
        let posicion = 0;
        conceptos.filter(concepto => ((concepto.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concepto.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)))
            .forEach((concept, index) => {
                if (concept.codTipoRegis === MULTI_REGISTRY) {

                    this.extraDataDesglose.periodos.forEach(period => {
                        const values = period.valPeriodoDesgloseDTOList.filter(v => v.codTipoConcep === concept.codTipoConcep && v.corConcepto === concept.corConcepto
                            && v.codPlantilla === concept.codTipoPlantilla);
                        for (let i = 0; i < values.length; i++) {
                            if (values[i].corValor > number) {
                                number = values[i].corValor;
                            }
                        }
                    });
                    if (number > 1) {
                        hotInstance.alter('insert_row', posicion, number - 1);
                    }
                    concept.corValor = number;
                    for (let i = 1; i < number; i++) {
                        cuentaMultiregistros++;
                        this.contMultiregistro++;
                        const newConcept: IConcepto = new Concepto(
                            concept.codTipoPlantilla, concept.codTipoConcep, concept.corConcepto, concept.codConcepto, '',
                            concept.numSecuencia, concept.numLvlIndent, concept.codTipoRegis, concept.glsFormulaValOriginal, concept.glsFormulaVal, concept.glsFormulaVal2,
                            concept.glsFormulaVal3, concept.glsFormulaVal4, concept.glsFormulaPrcOriginal, concept.glsFormulaPrc, concept.glsFormulaPrc2, concept.glsFormulaPrc3,
                            concept.glsFormulaPrc4, concept.glsFormulaDefOriginal, concept.glsFormulaDef, concept.glsFormulaDef2, concept.glsFormulaDef3, concept.glsFormulaDef4,
                            concept.flgIsTotal, concept.flgUserModif, concept.flgConActivo, concept.numDecimales, concept.flgHaceAjust,
                            concept.flgIsBold, concept.flgIsUcase, concept.flgIsEnclosed, concept.flgIsPrinted, concept.numDecPrint,
                            concept.flgNonegativ, concept.flgAfterspace, concept.concepto, i, concept.newValue,
                            concept.newFormula, concept.newGlosa, concept.newPorcent, concept.expValidacionVal, concept.expValidacionPrc,
                            concept.expValidacionDef, concept.msgErrValidacionVal, concept.msgErrValidacionPrc, concept.msgErrValidacionDef,
                            concept.unidad, concept.updateFormula);
                        conceptos.splice(posicion, 0, newConcept);
                        this.data[posicion] = [
                            concept.codTipoConcep,
                            concept.codTipoRegis,
                            concept.flgIsTotal,
                            concept.codConcepto,
                            concept.glsConcepto,
                            '0',
                            '0',
                            '0',
                            '0',
                            '0',
                            '0',
                            '0',
                            '0',
                            '0',
                            '0',
                            '0',
                            '0'
                        ];
                        posicion++;
                    }
                    number = 1;
                }
                posicion++;
            });
    }
    private recorreConceptosAsignaData(valPeriodoDesgloseDTOList: IValPeriodoDesglose[], periodNumber: number) {
        let k = 0;
        for (; k < conceptos.length; k++) {
            let setToNull = null;
            const concept = conceptos[k];
            if ((concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)) {
                // todo: comento para inyectar todos los valores
                if (!isNullOrUndefined(valPeriodoDesgloseDTOList)) {
                    const values = valPeriodoDesgloseDTOList.filter(v => v.codTipoConcep === concept.codTipoConcep && v.corConcepto === concept.corConcepto
                        && v.codPlantilla === concept.codTipoPlantilla);
                    if (!isNullOrUndefined(values) && values.length > 0 && !isNullOrUndefined(values[0])) {
                        if (concept.codTipoRegis !== MULTI_REGISTRY) {
                            if (concept.flgIsTotal === 0) {
                                let formula = values[0].glsFrmlConcep;
                                if (!isNullOrUndefined(formula) && !formula.toString().includes('=')) {
                                    formula = '=' + formula;
                                }
                                if (!isNullOrUndefined(formula) && formula.toString() !== '' && formula.toString().includes('=')) {
                                    switch (periodNumber) {
                                        case 0: {
                                            concept.glsFormulaVal = formula;
                                            break;
                                        }
                                        case 1: {
                                            concept.glsFormulaVal2 = formula;
                                            break;
                                        }
                                        case 2: {
                                            concept.glsFormulaVal3 = formula;
                                            break;
                                        }
                                        case 3: {
                                            concept.glsFormulaVal4 = formula;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (!isNullOrUndefined(this.data[k])) {
                                this.data[k][COLUMN_NUMBER_GLS_FORMULA_VAL[periodNumber]] = !isNullOrUndefined(values[0].valConcepto) ? values[0].valConcepto : 0;
                                this.data[k][COLUMN_NUMBER_GLS_FORMULA_PRC[periodNumber]] = !isNullOrUndefined(values[0].valPrcConcep) ? values[0].valPrcConcep : 0;
                            }
                        }
                        if (concept.codTipoRegis === MULTI_REGISTRY) {
                            for (let i = 0; i < values.length; i++) {
                                if (this.data[k - 1][COLUMN_NUMBER_COD_TIPO_REGIS] !== MULTI_REGISTRY) {
                                    if (this.data[k + i][COLUMN_NUMBER_COD_TIPO_REGIS] === MULTI_REGISTRY) {
                                        if (!concept.flgIsTotal) {
                                            let formula = values[i].glsFrmlConcep;
                                            if (!isNullOrUndefined(formula) && !formula.toString().includes('=')) {
                                                formula = '=' + formula;
                                            }
                                            if (!isNullOrUndefined(formula) && formula.toString() !== '' && formula.toString().includes('=')) {
                                                if (!isNullOrUndefined(values[i].corValor)) {
                                                    switch (periodNumber) {
                                                        case 0: {
                                                            conceptos[k + values[i].corValor - 1].glsFormulaVal = formula;
                                                            break;
                                                        }
                                                        case 1: {
                                                            conceptos[k + values[i].corValor - 1].glsFormulaVal2 = formula;
                                                            break;
                                                        }
                                                        case 2: {
                                                            conceptos[k + values[i].corValor - 1].glsFormulaVal3 = formula;
                                                            break;
                                                        }
                                                        case 3: {
                                                            conceptos[k + values[i].corValor - 1].glsFormulaVal4 = formula;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!isNullOrUndefined(values[i].corValor)) {
                                            if (!isNullOrUndefined(this.data[k + values[i].corValor - 1])) {
                                                this.data[k + values[i].corValor - 1][COLUMN_NUMBER_GLS_FORMULA_VAL[periodNumber]] = !isNullOrUndefined(values[i].valConcepto) ? values[i].valConcepto : 0;
                                                this.data[k + values[i].corValor - 1][COLUMN_NUMBER_GLS_FORMULA_PRC[periodNumber]] = !isNullOrUndefined(values[i].valPrcConcep) ? values[i].valPrcConcep : 0;
                                                this.data[k + values[i].corValor - 1][COLUMN_NUMBER_CCC] = values[i].glsConcepto;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        setToNull = true;
                    }
                } else {

                }
                if (setToNull) {
                    if (!isNullOrUndefined(concept.codTipoRegis)) {
                        if (!isNullOrUndefined(this.data[k])) {
                            this.data[k][COLUMN_NUMBER_GLS_FORMULA_VAL[periodNumber]] = 0;
                            this.data[k][COLUMN_NUMBER_GLS_FORMULA_PRC[periodNumber]] = 0;
                        }
                    }
                }
            }
        }
    }

    private recorreConceptosAsignGeneracionFondosIndicadoresFinancieros(valPeriodoDesgloseDTOList: IValPeriodoDesglose[], periodNumber: number) {
        let k = 0;
        for (; k < conceptos.length; k++) {
            let setToNull = null;
            const concept = conceptos[k];
            if ((concept.codTipoConcep === COD_TIPO_CONCEP_GEN_FONDOS) || (concept.codTipoConcep === COD_TIPO_CONCEP_IND_FINAN)) {
                if (!isNullOrUndefined(valPeriodoDesgloseDTOList)) {
                    const values = valPeriodoDesgloseDTOList.filter(v => v.codTipoConcep === concept.codTipoConcep && v.corConcepto === concept.corConcepto
                        && v.codPlantilla === concept.codTipoPlantilla);
                    if (!isNullOrUndefined(values) && values.length > 0 && !isNullOrUndefined(values[0])) {
                        this.data[k][COLUMN_NUMBER_GLS_FORMULA_VAL[periodNumber]] = !isNullOrUndefined(values[0].valConcepto) ? values[0].valConcepto : 0;
                    } else {
                        setToNull = true;
                    }
                } else {

                }
                if (setToNull) {
                    if (!isNullOrUndefined(concept.codTipoRegis)) {
                        this.data[k][COLUMN_NUMBER_GLS_FORMULA_VAL[periodNumber]] = 0;
                    }
                }
            }
        }
    }
    private loadProyeccionesTemplate(templateCode) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableProjectionInstanceId);
        const conceptRequestDTO1: IConceptRequestDTO = new ConceptRequestDTO(templateCode);
        conceptRequestDTO1.conceptTypeCodesIn = [COD_TIPO_CONCEP_PROY_DM, COD_TIPO_CONCEP_PROY_DM_EST_RESULT, COD_TIPO_CONCEP_PROY_DM_FLUJO_CAJA, COD_TIPO_CONCEP_PROY_DM_BALAN_GRAL];
        conceptRequestDTO1.orderByDirection = OrderByDirection.ASC;
        conceptRequestDTO1.orderByFields = [FIELD_NAME_COD_TIPO_CONCEP, FIELD_NAME_NUM_SECUENCIA];
        conceptRequestDTO1.columnLetterGlsFormulaVal1 = this.COLUMN_LETTER_GLS_FORMULA_VAL_1;
        conceptRequestDTO1.columnLetterGlsFormulaDef1 = this.COLUMN_LETTER_GLS_FORMULA_DEF_1;
        conceptRequestDTO1.columnLetterGlsFormulaPrc1 = this.COLUMN_LETTER_GLS_FORMULA_PRC_1;
        conceptRequestDTO1.flgConActivo = VALUE_IS_ACTIVE_CONCEPT_TRUE;
        this.conceptoService.findByTemplateConceptsCodesIn(conceptRequestDTO1).subscribe(
            (res: HttpResponse<IBreakdownFilterConceptDto>) => {
                const ConceptosValidate: IBreakdownFilterConceptDto = res.body;
                conceptosPR = ConceptosValidate.conceptoDTOList;
                if (!isNullOrUndefined(conceptosPR) && conceptosPR.length > 0) {
                    this.dataProyecciones = [];
                    this.dataProyeccionesCalculo = [];
                    // let lastGroup = '';
                    let i = 0;
                    conceptosPR.forEach((concept, index) => {
                        if (isNullOrUndefined(concept.codTipoRegis) || concept.codTipoRegis === '') { // Si es un encabezado de grupo
                            // El encabezado se agrega uno solo en todos los períodos
                            this.dataProyecciones.push([
                                concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                '', // G
                                this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                null, null, null, null, null, null, null, null, null, null, null, null
                            ]);
                            this.dataProyeccionesCalculo.push([
                                concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                '', // G
                                this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                null, null, null, null, null, null, null, null, null, null, null, null
                            ]);
                        } else {
                            if (concept.flgIsTotal === 0) {
                                this.dataProyecciones.push([
                                    concept.codTipoConcep,
                                    concept.codTipoRegis,
                                    concept.flgIsTotal,
                                    concept.codConcepto,
                                    concept.glsConcepto,
                                    concept.glsFormulaVal
                                ]);
                                this.dataProyeccionesCalculo.push([
                                    concept.codTipoConcep,
                                    concept.codTipoRegis,
                                    concept.flgIsTotal,
                                    concept.codConcepto,
                                    concept.glsConcepto,
                                    0
                                ]);
                            } else {
                                this.dataProyecciones.push([
                                    concept.codTipoConcep,
                                    concept.codTipoRegis,
                                    concept.flgIsTotal,
                                    concept.codConcepto,
                                    concept.glsConcepto,
                                    concept.glsFormulaVal
                                ]);
                                this.dataProyeccionesCalculo.push([
                                    concept.codTipoConcep,
                                    concept.codTipoRegis,
                                    concept.flgIsTotal,
                                    concept.codConcepto,
                                    concept.glsConcepto,
                                    0
                                ]);
                            }
                        }
                        i = i + 1;
                    });
                }
                if (hotInstance) {
                    this.renderHot(hotInstance, this.dataProyecciones.length - 1);
                    this.hotUpdateSettings(hotInstance, true);
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    onClickPrintTabPrint() {
        this.onClickSelectionPrint();
    }
    private controlBotones(caso: number) {
        switch (caso) {
            case 0: {
                this.buttonEditar.nativeElement.disabled = false;
                this.buttonCalcular.nativeElement.disabled = true;
                this.buttonSave.nativeElement.disabled = true;
                break;
            }
            case 1: {
                this.buttonEditar.nativeElement.disabled = true;
                this.buttonCalcular.nativeElement.disabled = false;
                this.buttonSave.nativeElement.disabled = true;
                break;
            }
            case 2: {
                this.buttonEditar.nativeElement.disabled = false;
                this.buttonCalcular.nativeElement.disabled = true;
                this.buttonSave.nativeElement.disabled = false;
                break;
            }
        }
    }
    onClickSelectionPrint() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableModelDataInstanceId);
        if (hotInstance) {
            const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
            const fecha = this.fecPeriodo;
            const historicDataRequestDTO = new ProjectionHistoricDataRequestDTO(this.codCliente, this.codTipoDesglose, this.codSubtipoDesglose,
                this.codMoneda, this.codPlantilla, fecha);
            this.projectionService.InformeProyeccion(historicDataRequestDTO).subscribe(
                (datos: HttpResponse<any>) => {
                    const file = new Blob([datos], { type: 'application/pdf' });
                    const fileURL = URL.createObjectURL(file);
                    window.open(fileURL);
                },
                (datos: HttpErrorResponse) => this.onError(datos.message)
            );
        }
    }

    onClickCerrar() {
        this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
            'breakdown.confirmation',
            'desgloseApp.projections.saveWithoutConfirmation',
            'question',
            'breakdown.yes',
            'breakdown.no'
        ).subscribe(res => {
            if (res) {
                const historicDataRequestDTO = new ProjectionHistoricDataRequestDTO(this.codCliente, this.codTipoDesglose, this.codSubtipoDesglose,
                    this.codMoneda, this.codPlantilla, this.breakdown.fecPeriodo1Rpt);
                this.projectionService.cerrarProyeccion(historicDataRequestDTO).subscribe((datos: HttpResponse<IPeriodo>) => {
                        this.estadoPry = 'VALIDADO';
                        this.buttonCerrar.nativeElement.disabled = true;
                    },
                    (datos: HttpErrorResponse) => {
                       // this.onError(datos.message);
                    });
            }
            this.questionMessageSubscription.unsubscribe();
        });
    }
    closeModal() {
        this.activeModal.dismiss('cancel');
    }
}
