import { AfterViewInit, Component, ElementRef, EventEmitter, OnInit, Renderer } from '@angular/core';
import {
    CODTABLA_TIPO_CLIENTE,
    SESSION_VARIABLE_ECONOMIC_ACTIVITIES,
    SESSION_VARIABLE_SECTORS,
    SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES,
    SESSION_VARIABLE_SUB_SECTORS
} from 'app/app.constants';
import { isNullOrUndefined } from 'util';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DesgloseService } from 'app/entities/desglose';
import { JhiAlertService } from 'ng-jhipster';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { CustomSqlService } from 'app/shared/services/custom-sql.service';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { ISector } from 'app/shared/model/sector.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { IEconomicActivity } from 'app/shared/model/economic-activity.model';
import { SessionStorageService } from 'ngx-webstorage';
import { Cliente, ClientType, ICliente } from 'app/shared/model/cliente.model';
import { ISubSector } from 'app/shared/model/sub-sector.model';
import { ClienteService } from 'app/shared/services/cliente.service';
import { Observable } from 'rxjs';
import { IRelacionTabla } from 'app/shared/model/relacion-tabla.model';
import { StateStorageService } from 'app/core';

@Component({
    selector: 'jhi-breakdown-create-client-dialog',
    templateUrl: './breakdown-create-client-dialog.component.html',
    styleUrls: [ './breakdown-create-client-dialog.scss' ]
})
export class BreakdownCreateClientDialogComponent implements OnInit, AfterViewInit {
    public withRut: boolean;
    public newClient: ICliente;
    simpleComboboxTypes: IDescripcionCodigo[] = [];
    sectorTypes: ISector[] = [];
    subSectorTypes: ISubSector[] = [];
    economicActivities: IEconomicActivity[] = [];
    CODTABLA_TIPO_CLIENTE = CODTABLA_TIPO_CLIENTE;
    sort = 'codGenerico,ASC';
    public isCompany = true;
    public sector;
    public isSaving: boolean;
    public onSaveSuccessEmitter: EventEmitter<ICliente> = new EventEmitter();
    public onCancelEmitter: EventEmitter<void> = new EventEmitter();

    constructor(public activeModal: NgbActiveModal, private desgloseService: DesgloseService, private jhiAlertService: JhiAlertService, private customSqlService: CustomSqlService,
                private descripcionCodigoService: DescripcionCodigoService, private sessionStorage: SessionStorageService, private clienteService: ClienteService,
                private elementRef: ElementRef, private renderer: Renderer) {
    }

    ngOnInit() {
        this.isSaving = false;
        const allComboboxData = this.sessionStorage.retrieve(SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES);
        const sectorsData = this.sessionStorage.retrieve(SESSION_VARIABLE_SECTORS);
        const subSectorsData = this.sessionStorage.retrieve(SESSION_VARIABLE_SUB_SECTORS);
        const economicActivitiesData = this.sessionStorage.retrieve(SESSION_VARIABLE_ECONOMIC_ACTIVITIES);
        if (!isNullOrUndefined(allComboboxData) && allComboboxData.length > 0) {
            this.simpleComboboxTypes = allComboboxData;
        } else {
            const typesParam = this.CODTABLA_TIPO_CLIENTE;
            this.loadIDescripcionCodigo(typesParam, this.sort);
        }
        this.loadSectors();
        if (!isNullOrUndefined(sectorsData) && sectorsData.length > 0) {
            this.sectorTypes = sectorsData;
        } else {
            this.loadSectors();
        }
        if (!isNullOrUndefined(subSectorsData) && subSectorsData.length > 0) {
            this.subSectorTypes = subSectorsData;
        } else {
            // this.loadSubSectors();
        }
        if (!isNullOrUndefined(economicActivitiesData) && economicActivitiesData.length > 0) {
            this.economicActivities = economicActivitiesData;
        } else {
            this.loadActividadEconomica();
        }
    }

    ngAfterViewInit() {
        // Para retomar el foco del elemento si se pierde por algún motivo pasado medio segundo.
        setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#field_clientType'), 'focus', []), 500);
    }

    loadActividadEconomica() {
        this.customSqlService
        .getAllEconomicActivities()
        .subscribe(
            (res: HttpResponse<IEconomicActivity[]>) => this.assignEconomicActivities(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadSectors() {
        this.customSqlService
        .getAllSectors()
        .subscribe(
            (res: HttpResponse<ISector[]>) => this.assignSectors(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadSubSectors() {
        this.customSqlService
        .getAllSubSectors()
        .subscribe(
            (res: HttpResponse<ISubSector[]>) => this.assignSubSectors(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    // Carga los SubSectores por codigo de Sector
    onSubSectorChange(code) {
        this.customSqlService.getSubSectorsBySector(this.sector)
        .subscribe(
            (res: HttpResponse<ISubSector[]>) => this.assignSubSectors(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    // Carga las actividades economicas por codigo de Sector
    onActividadEconomicaChange(code) {
        this.customSqlService.getEconomicActivitiesBySector(this.sector)
        .subscribe(
            (res: HttpResponse<IEconomicActivity[]>) => this.assignEconomicActivities(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadIDescripcionCodigo(codtabla: string, sort: string) {
        this.descripcionCodigoService
        .findByCodTablasIn(codtabla, sort)
        .subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigos(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
        this.loadActividadEconomica();
        this.subSectorTypes = [];
        this.sessionStorage.store(SESSION_VARIABLE_SUB_SECTORS, this.subSectorTypes);
    }

    onClickSaveClient() {
        this.isSaving = true;
        if (this.newClient.activity === '-1') {
            // Si no tiene actividad económica coloco todos en 0
            this.newClient.activity = '0';
            this.newClient.sector = '0';
            this.newClient.subSector = '0';
        }
        let validated = true;
        if (this.newClient.clientType === ClientType.PERSONA_NATURAL) {
            // Si es persona
            this.newClient.isCompany = false;
            if (isNullOrUndefined(this.newClient.name) || this.newClient.name.trim().length <= 0 || isNullOrUndefined(this.newClient.fatherLastName) ||
                this.newClient.fatherLastName.trim().length <= 0 || isNullOrUndefined(this.newClient.motherLastName) || this.newClient.motherLastName.trim().length <= 0 ||
                this.newClient.activity === '-1') {

                validated = false;
                this.onError('breakdown.newClient.lackInformationValidationMessage');
            }
        } else if (this.newClient.clientType === ClientType.EMPRESA) {
            // Si es empresa
            this.newClient.isCompany = true;
            if (isNullOrUndefined(this.newClient.name) || this.newClient.name.trim().length <= 0 || this.newClient.activity === '-1') {
                validated = false;
                this.onError('breakdown.newClient.lackInformationValidationMessage');
            }
        } else {
            validated = false;
            this.onError('breakdown.newClient.invalidClientTypeValidationMessage');
        }
        if (validated) {
            this.subscribeToSaveResponse(this.clienteService.create(this.newClient));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>) {
        result.subscribe((res: HttpResponse<ICliente>) => this.onSaveSuccess(res), (res: HttpErrorResponse) => this.onSaveError(res));
    }

    private onSaveSuccess(res: HttpResponse<ICliente>) {
        this.isSaving = false;
        this.newClient = res.body;
        this.onSaveSuccessEmitter.emit(this.newClient);
        this.closeModal();
    }

    private onSaveError(res: HttpErrorResponse) {
        this.isSaving = false;
        // El interceptor de errores muestra en caso de algún error, solo hay que poner la etiqueta en el HTML de la página.
    }

    previousState() {
        window.history.back();
    }

    onClickCancel() {
        this.onCancelEmitter.emit();
        this.closeModal();
    }

    onClientTypeChange(value: any) {
        this.isCompany = this.newClient.clientType === ClientType.EMPRESA;
        if (!this.isCompany) {
            this.newClient.sector = '-1';
            this.newClient.subSector = '-1';
            this.newClient.activity = '-1';
        } else {
            this.newClient.fatherLastName = null;
            this.newClient.motherLastName = null;
        }
    }

    private assignDescripcionCodigos(data: IDescripcionCodigo[], headers: HttpHeaders) {
        this.simpleComboboxTypes = data;
    }

    private assignSectors(data: ISector[], headers: HttpHeaders) {
        this.sectorTypes = data;
        this.sessionStorage.store(SESSION_VARIABLE_SECTORS, this.sectorTypes);
    }

    private assignSubSectors(data: ISubSector[], headers: HttpHeaders) {
        this.subSectorTypes = data;
        if (!isNullOrUndefined(data) && !isNullOrUndefined(data[ 0 ])) {
            this.newClient.subSector = data[0].codSubSector;
            this.sessionStorage.store(SESSION_VARIABLE_SUB_SECTORS, this.subSectorTypes);
        }
    }

    private assignEconomicActivities(data: IEconomicActivity[], headers: HttpHeaders) {
        this.economicActivities = data;
        if (!isNullOrUndefined(data) && !isNullOrUndefined(data[ 0 ])) {
            this.newClient.activity = data[0].codActividad;
            this.sessionStorage.store(SESSION_VARIABLE_ECONOMIC_ACTIVITIES, this.economicActivities);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
