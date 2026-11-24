import { Component, EventEmitter, OnInit } from '@angular/core';
import { ICliente } from 'app/shared/model/cliente.model';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { CODTABLA_TIPO_MONEDAS, SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES } from 'app/app.constants';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DesgloseService } from 'app/entities/desglose';
import { JhiAlertService } from 'ng-jhipster';
import { CustomSqlService } from 'app/shared/services/custom-sql.service';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { ClienteService } from 'app/shared/services/cliente.service';
import { isNullOrUndefined } from 'util';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SessionStorageService } from 'ngx-webstorage';
import { Desglose, IDesglose } from 'app/shared/model/desglose.model';

@Component({
    selector: 'jhi-breakdown-print-config-dialog',
    templateUrl: './breakdown-print-config-dialog.component.html',
    styleUrls: [ './breakdown-print-config-dialog.scss' ]
})
export class BreakdownPrintConfigDialogComponent implements OnInit {
    breakdown: IDesglose = new Desglose();
    simpleComboboxTypes: IDescripcionCodigo[] = [];
    CODTABLA_TIPO_MONEDAS = CODTABLA_TIPO_MONEDAS;
    public onSaveSuccessEmitter: EventEmitter<ICliente> = new EventEmitter();
    sort = 'codGenerico,ASC';
    initialDisabled = true;

    constructor(public activeModal: NgbActiveModal, private desgloseService: DesgloseService, private jhiAlertService: JhiAlertService, private customSqlService: CustomSqlService,
                private descripcionCodigoService: DescripcionCodigoService, private sessionStorage: SessionStorageService, private clienteService: ClienteService) {
    }

    ngOnInit() {
        const allComboboxData = this.sessionStorage.retrieve(SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES);
        if (!isNullOrUndefined(allComboboxData) && allComboboxData.length > 0) {
            this.simpleComboboxTypes = allComboboxData;
        } else {
            const typesParam = this.CODTABLA_TIPO_MONEDAS;
            this.loadIDescripcionCodigo(typesParam, this.sort);
        }
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
    }

    onClickSaveClient() {
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>) {
        result.subscribe((res: HttpResponse<ICliente>) => this.onSaveSuccess(res), (res: HttpErrorResponse) => this.onSaveError(res));
    }

    private onSaveSuccess(res: HttpResponse<ICliente>) {
        // this.onSaveSuccessEmitter.emit(this.newClient);
        this.closeModal();
    }

    private onSaveError(res: HttpErrorResponse) {
        // El interceptor de errores muestra en caso de algún error, solo hay que poner la etiqueta en el HTML de la página.
    }

    previousState() {
        window.history.back();
    }

    onClickCancel() {
        this.closeModal();
    }

    onClientTypeChange(value: any) {
    }

    private assignDescripcionCodigos(data: IDescripcionCodigo[], headers: HttpHeaders) {
        this.simpleComboboxTypes = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

}
