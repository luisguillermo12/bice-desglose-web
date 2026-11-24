import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDesglose } from 'app/shared/model/desglose.model';
import { DesgloseService } from 'app/entities/desglose';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DATE_FORMAT_PERIOD_ES_UI_HEADER, DATE_TIME_FORMAT_ES_UI, ITEMS_PER_PAGE, Sweetalert2Component } from 'app/shared';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import {
    BREAKDOWN_QUERY_UI_ORIGIN_REVIEW_PAGE,
    COD_GENERICO_DESGLOSE,
    CODTABLA_ESTADO_DESGLOSE,
    CODTABLA_SUBTIPO_DESGLOSE,
    CODTABLA_TIPO_DESGLOSE,
    CODTABLA_TIPO_MONEDAS,
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE,
    COLUMN_SIZES_SEARCH_BREAKDOWN_TABLE,
    ESTADO_BORRADOR_TERMINADO,
    ESTADO_BORRADOR_VALIDADO,
    ESTADO_CORREGIDO,
    ESTADO_DEFINITIVO,
    NAME_VALIDATION_PATTERN,
    RUT_VALIDATION_PATTERN,
    SESSION_VARIABLE_BREAKDOWN_STATES,
    SESSION_VARIABLE_BUSQUEDA_DESGLO,
    SESSION_VARIABLE_SECTORS,
    SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES,
    SESSION_VARIABLE_USUARIO,
    USER_ROLE_PLAT_DESGLOSE_EJECUTOR,
    USER_ROLE_PLAT_DESGLOSE_JEFE,
    USER_ROLE_PLAT_DESGLOSE_REVISOR
} from 'app/app.constants';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { CustomSqlService } from 'app/shared/services/custom-sql.service';
import { ISector } from 'app/shared/model/sector.model';
import { isNullOrUndefined } from 'util';
import { SessionStorageService } from 'ngx-webstorage';
import { ValidationUtils } from 'app/shared/util/validation-utils';
import { NgForm } from '@angular/forms';
import { IBreakdownFilterResponseDTO } from 'app/entities/desglose/filter-breakdown-response.model';
import { BreakdownFilterRequestDTO, IBreakdownFilterRequestDTO } from 'app/entities/desglose/filter-breakdown-request.model';
import { Subscription } from 'rxjs';
import * as moment from 'moment';
import { Moment } from 'moment';
import { IProjectionBreakdownPeriod, ProjectionBreakdownPeriod } from 'app/projections/projection-breakdown-period.model';
import { ChargesService } from 'app/maintenance/charges';
import { ITurbineUsuarios, TurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { ITurbineUser } from 'app/shared/model/turbine-user.model';
import { SfStfOperator } from 'app/shared/model/sf-stf-operator.model';
import { SfStfOperatorService } from 'app/entities/sf-stf-operator';
import { FilterDescripcionCodigoPipe } from 'app/shared/pipes/filter-descripcion-codigo.pipe';
import * as jQuery from 'jquery';
@Component({
    selector: 'jhi-search-breakdown-dialog',
    templateUrl: './search-breakdown-dialog.component.html',
    styleUrls: [ './search-breakdown-dialog.scss' ]
})
export class SearchBreakdownDialogComponent implements OnInit, AfterViewInit {
    usuario: ITurbineUser;
    operatorCode: string;
    usuarios: ITurbineUsuarios[];
    ejecutores: ITurbineUsuarios[] = [];
    breakdownFilterRequestDTO: IBreakdownFilterRequestDTO = new BreakdownFilterRequestDTO();
    public onSelectItem: EventEmitter<IBreakdownFilterRequestDTO> = new EventEmitter();
    public onSelectPeriodItem: EventEmitter<IProjectionBreakdownPeriod> = new EventEmitter();
    sectorTypes: ISector[] = [];
    DATE_TIME_FORMAT_ES_UI = DATE_TIME_FORMAT_ES_UI;
    DATE_FORMAT_PERIOD_ES_UI_HEADER = DATE_FORMAT_PERIOD_ES_UI_HEADER;
    ESTADO_CORREGIDO = ESTADO_CORREGIDO;
    ESTADO_DEFINITIVO = ESTADO_DEFINITIVO;
    ESTADO_BORRADOR_VALIDADO = ESTADO_BORRADOR_VALIDADO;
    ESTADO_BORRADOR_TERMINADO = ESTADO_BORRADOR_TERMINADO;
    simpleComboBoxTypes: IDescripcionCodigo[] = [];
    statesComboBoxTypes: IDescripcionCodigo[] = [];
    currentAccount: any;
    desgloses: IBreakdownFilterResponseDTO[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    rol: number;
    role: string;
    CODTABLA_TIPO_MONEDAS = CODTABLA_TIPO_MONEDAS;
    CODTABLA_SUBTIPO_DESGLOSE = CODTABLA_SUBTIPO_DESGLOSE;
    CODTABLA_TIPO_DESGLOSE = CODTABLA_TIPO_DESGLOSE;
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE = CODTABLA_TIPO_PLANTILLAS_DESGLOSE;
    sort = 'codGenerico,ASC';
    RUT_VALIDATION_PATTERN = RUT_VALIDATION_PATTERN;
    NAME_VALIDATION_PATTERN = NAME_VALIDATION_PATTERN;
    CODTABLA_ESTADO_DESGLOSE = CODTABLA_ESTADO_DESGLOSE;
    COD_GENERICO_DESGLOSE = COD_GENERICO_DESGLOSE;
    @Input() isExternal? = false;
    @Input() fechasRevision? = true;
    @Input() userModificacion? = true;
    @Input() otrasFechas? = false;
    @Input() tipos? = false;
    @Input() moneda? = false;
    @Input() sector? = false;
    @Input() revision? = false;
    @Input() hiddenColumnaExtra? = true;
    @Input() click? = false;
    @Input() ejecutor? = false;
    @Input() estadoDesgloseRevision? = false;
    @Input() estadoDesgloseConsulta? = false;
    @Input() tiposSubtipoDesglose? = false;
    @ViewChild('fieldRutVerificationNumber') fieldRutVerificationNumber: ElementRef;
    @ViewChild('fieldName') fieldName: ElementRef;

    disabledOtros = true;
    clienteSubscription: Subscription;
    public isProjectionSearch = false;
    COLUMN_SIZES = COLUMN_SIZES_SEARCH_BREAKDOWN_TABLE;

    numberRows = 10;
    constructor(public activeModal: NgbActiveModal, private desgloseService: DesgloseService, private parseLinks: JhiParseLinks, private jhiAlertService: JhiAlertService,
                private principal: Principal, private activatedRoute: ActivatedRoute, private router: Router, private descripcionCodigoService: DescripcionCodigoService,
                private customSqlService: CustomSqlService, private sessionStorage: SessionStorageService, private validationUtils: ValidationUtils,
                private sweetAlert2: Sweetalert2Component, private chargesService: ChargesService, private sfStfOperatorService: SfStfOperatorService,
                private filterDescripcionCodigoPipe: FilterDescripcionCodigoPipe) {

        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = 0;
            this.previousPage = 0;
            this.reverse = false;
            this.predicate = 'id';
        });
    }

    ngOnInit() {
        jQuery('.alerts').hide();
        if (this.revision) {
            this.breakdownFilterRequestDTO.codEstado = this.ESTADO_BORRADOR_TERMINADO;
        }
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
        this.usuario = this.sessionStorage.retrieve(SESSION_VARIABLE_USUARIO);
        if(!isNullOrUndefined(this.usuario) && !isNullOrUndefined(this.usuario.loginName)) {
            this.sfStfOperatorService.findByLoginName(this.usuario.loginName).subscribe(
                (data: HttpResponse<SfStfOperator>) => {
                    this.operatorCode = data.body.operatorCode.toString(10);
                    this.chargesService.finUsuariosAllTotal().subscribe(
                        (res: HttpResponse<ITurbineUsuarios[]>) => {
                            this.usuarios = res.body;
                            this.ejecutores = res.body;
                            if (this.revision === true) {
                                this.ejecutores = [];
                                if (this.role === USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === USER_ROLE_PLAT_DESGLOSE_JEFE) {
                                    this.chargesService.finUsuariosAllRevision(this.operatorCode).subscribe(
                                        (resp: HttpResponse<ITurbineUsuarios[]>) => {
                                            this.ejecutores = resp.body;
                                        },
                                        (resp: HttpErrorResponse) => this.onError(resp.message)
                                    );
                                } else {
                                    const user: ITurbineUsuarios = new TurbineUsuarios(this.usuario.loginName, this.usuario.firstName + ' ' + this.usuario.lastName,
                                        this.operatorCode, this.usuario.userId.toString());
                                    this.ejecutores.push(user);
                                    this.breakdownFilterRequestDTO.nomAnalista = user.operatorCode;
                                }
                            }
                        },
                        (res: HttpErrorResponse) => this.onError(res.message)
                    );
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        }
        const allComboBoxData = this.sessionStorage.retrieve(SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES);
        const sectorsData = this.sessionStorage.retrieve(SESSION_VARIABLE_SECTORS);
        const statesData = this.sessionStorage.retrieve(SESSION_VARIABLE_BREAKDOWN_STATES);
        if (!isNullOrUndefined(allComboBoxData) && allComboBoxData.length > 0) {
            this.simpleComboBoxTypes = allComboBoxData;
        } else {
            const typesParam = this.CODTABLA_TIPO_PLANTILLAS_DESGLOSE + ',' + this.CODTABLA_SUBTIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_MONEDAS;
            this.loadIDescripcionCodigo(typesParam, this.sort);
        }
        // this.loadSectors();
        if (!isNullOrUndefined(sectorsData) && sectorsData.length > 0) {
            this.sectorTypes = sectorsData;
        } else {
            this.loadSectors();
        }
        if (!isNullOrUndefined(statesData) && statesData.length > 0) {
            this.statesComboBoxTypes = statesData;
        } else {
            this.findBreakdownStatuses();
        }
    }

    ngAfterViewInit() {
        document.getElementById('field_rut').focus();
    }
    /**
     * @deprecated Use findBreakdownStatuses() instead.
     */
    loadIDescripcionCodigoStates(codtabla: string, codGenerico: string, sort: string) {
        this.descripcionCodigoService.findAllByCodTablaInAndCodGenericoStartsWithIgnoreCase(codtabla, codGenerico, sort).subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigosStates(res.body),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    findBreakdownStatuses() {
        this.descripcionCodigoService.findBreakdownStatuses().subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigosStates(res.body),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadIDescripcionCodigo(codtabla: string, sort: string) {
        this.descripcionCodigoService.findByCodTablasIn(codtabla, sort).subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigos(res.body),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadSectors() {
        this.customSqlService.getAllSectors().subscribe(
            (res: HttpResponse<ISector[]>) => this.assignSectors(res.body),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    triggerOnSelectItemEvent(selectedItem: IBreakdownFilterResponseDTO) {
        if (this.isExternal) {
            this.sessionStorage.store(SESSION_VARIABLE_BUSQUEDA_DESGLO, selectedItem);
            /*  this.router.navigate(['/breakdown',
                  {
                          hasRut:selectedItem.hasRut,
                          codCliente:selectedItem.codCliente,
                          nomCliente:selectedItem.nomCliente,
                          codActividad:selectedItem.codActividad,
                          codPlantilla:selectedItem.codPlantilla,
                          codTipoDes:selectedItem.codTipoDes,
                          codSubTipoDes:selectedItem.codSubTipoDes,
                          codMoneda: selectedItem.codMoneda,
                          codUnidadMon:selectedItem.codUnidadMon,
                          nombreAnalista:selectedItem.nombreAnalista,
                          userAnalista:selectedItem.userAnalista
                  }]);*/
            this.router.navigate([ '/breakdown' ]);
        } else {
            this.onSelectItem.emit(selectedItem);
        }
    }

    triggerOnMarcarItemEvent(selectedItem: IBreakdownFilterResponseDTO) {
        this.clienteSubscription = this.sweetAlert2
        .solicitarAccion('breakdown.search.mensaje', 'breakdown.search.marcarDesglose', 'breakdown.search.aceptar', 'breakdown.search.cancelar')
        .subscribe(res => {
            if (res) {
                this.desgloseService.find(selectedItem.codCliente, selectedItem.codTipoDes, selectedItem.codSubTipoDes, selectedItem.codMoneda,
                    selectedItem.codPlantillaDesglose).subscribe(
                    (data: HttpResponse<IDesglose>) => {
                        const desglose: IDesglose = data.body;
                        desglose.codEstado = ESTADO_DEFINITIVO;
                        desglose.rolEmpleadoDefEnvPdf = this.operatorCode;
                        desglose.fecDesgloEnvPdf = moment(new Date(), 'DD-MM-YYYY HH:mm:ss');
                        desglose.numRolChgEstado = this.operatorCode;
                        this.desgloseService.update(desglose).subscribe(
                            (rpt: HttpResponse<IDesglose>) => {
                                selectedItem.rolEmpleadoDefEnvPdf = this.operatorCode;
                                this.sweetAlert2.mensajeSuccess('breakdown.search.mensaje', 'breakdown.search.exito');
                            },
                            (rpt: HttpErrorResponse) => this.onError(rpt.message)
                        );
                    },
                    (data: HttpErrorResponse) => this.onError(data.message)
                );
            }
            this.clienteSubscription.unsubscribe();
        });
    }

    onClickSearchBreakdown() {
        if (this.breakdownFilterRequestDTO) {
            if (this.revision) {
                this.breakdownFilterRequestDTO.tipoFiltro = BREAKDOWN_QUERY_UI_ORIGIN_REVIEW_PAGE;
            }
            this.breakdownFilterRequestDTO.loadPeriodsDates = this.isProjectionSearch;
            this.desgloseService.breakdownFilter(this.breakdownFilterRequestDTO).subscribe(
                (res: HttpResponse<IBreakdownFilterResponseDTO[]>) => this.paginateDesgloses(res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    onClickCancel() {
        this.closeModal();
    }

    trackId(index: number, item: IDesglose) {
        return item.codCliente;
    }

    onClickLoadBreakdownData() {
    }

    searchRutOnBlur(clientDataForm: NgForm) {
        if (clientDataForm.valid && !isNullOrUndefined(this.breakdownFilterRequestDTO.rut) && this.breakdownFilterRequestDTO.rut.trim()) {
            this.breakdownFilterRequestDTO.rutVerificationNumber = this.validationUtils.getRutVerificationNumber(
                parseInt(this.breakdownFilterRequestDTO.rut, 10)
            );
        } else {
            this.breakdownFilterRequestDTO.rutVerificationNumber = null;
        }
    }
    searchRutEnter(clientDataForm: NgForm) {
        if (clientDataForm.valid && !isNullOrUndefined(this.breakdownFilterRequestDTO.rut) && this.breakdownFilterRequestDTO.rut.trim()) {
            this.breakdownFilterRequestDTO.rutVerificationNumber = this.validationUtils.getRutVerificationNumber(
                parseInt(this.breakdownFilterRequestDTO.rut, 10)
            );
        } else {
            this.breakdownFilterRequestDTO.rutVerificationNumber = null;
        }
        this.fieldName.nativeElement.focus();
    }

    private paginateDesgloses(data: IBreakdownFilterResponseDTO[]) {
        this.desgloses = data;
        this.totalItems = !isNullOrUndefined(data) ? data.length : 0;
        if (this.totalItems === 0) {
            this.jhiAlertService.warning('warning.noResultDataWithFilter', null, null);
        }
    }

    private assignDescripcionCodigos(data: IDescripcionCodigo[]) {
        this.simpleComboBoxTypes = data;
    }

    private assignDescripcionCodigosStates(data: IDescripcionCodigo[]) {
        this.statesComboBoxTypes = data;
        this.sessionStorage.store(SESSION_VARIABLE_BREAKDOWN_STATES, this.statesComboBoxTypes);
    }

    private assignSectors(data: ISector[]) {
        this.sectorTypes = data;
        this.sessionStorage.store(SESSION_VARIABLE_SECTORS, this.sectorTypes);
    }

    private onError(errorMessage: string) {
        this.desgloses = [];
        jQuery('.alerts').show();
        // this.jhiAlertService.error(errorMessage, null, null);
    }

    onClickSelectBreakdownPeriod(row: IBreakdownFilterResponseDTO, item: Moment) {
        this.onSelectPeriodItem.emit(new ProjectionBreakdownPeriod(row, item));
    }

    eventoSeleccion(event: any) {
        const codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboBoxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        const descripcionCodigo = this.validationUtils.getDescripcionCodigoByCodGenerico(codes, this.breakdownFilterRequestDTO.subTipoDesglose.toString());
        const otros = 'OTROS';
        if (descripcionCodigo.glsGenericacorta.includes(otros)) {
            this.disabledOtros = false;
        } else {
            this.disabledOtros = true;
        }
    }
}
