import { Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { COD_AUDITOR_OTROS_9999, CODTABLA_AUDITORES, NAME_VALIDATION_PATTERN, SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES } from 'app/app.constants';
import { isNullOrUndefined } from 'util';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { Principal } from 'app/core';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { SessionStorageService } from 'ngx-webstorage';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IBreakdownAuditor } from 'app/entities/desglose/breakdown-auditor.model';

@Component({
    selector: 'jhi-breakdown-select-auditor-dialog',
    templateUrl: './breakdown-select-auditor-dialog.component.html',
    styleUrls: [ './breakdown-select-auditor-dialog.scss' ]
})
export class BreakdownSelectAuditorDialogComponent implements OnInit {

    public onAcceptEventEmitter: EventEmitter<IBreakdownAuditor> = new EventEmitter();
    public breakdownAuditor: IBreakdownAuditor;
    public isOtherAuditorNameDisabled = true;

    simpleComboBoxTypes: IDescripcionCodigo[] = [];
    currentAccount: any;
    CODTABLA_AUDITORES = CODTABLA_AUDITORES;
    NAME_VALIDATION_PATTERN = NAME_VALIDATION_PATTERN;
    sort = 'codGenerico,ASC';
    @ViewChild('fieldOtherAuditorName') fieldOtherAuditorName: ElementRef;

    constructor(public activeModal: NgbActiveModal, private jhiAlertService: JhiAlertService, private principal: Principal, private sessionStorage: SessionStorageService,
                private descripcionCodigoService: DescripcionCodigoService) {
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        const allComboBoxData = this.sessionStorage.retrieve(SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES);
        if (!isNullOrUndefined(allComboBoxData) && allComboBoxData.length > 0) {
            this.simpleComboBoxTypes = allComboBoxData;
        } else {
            const typesParam = this.CODTABLA_AUDITORES;
            this.loadIDescripcionCodigo(typesParam, this.sort);
        }
    }

    loadIDescripcionCodigo(codtabla: string, sort: string) {
        this.descripcionCodigoService.findByCodTablasIn(codtabla, sort).subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigos(res.body),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    onClickCancel() {
        this.closeModal();
    }

    triggerOnSelectItemEvent() {
        if (this.isOtherAuditorNameDisabled || (!this.isOtherAuditorNameDisabled && !isNullOrUndefined(this.breakdownAuditor.otherAuditorName) &&
            this.breakdownAuditor.otherAuditorName.trim().length > 0)) {
            this.onAcceptEventEmitter.emit(this.breakdownAuditor);
        } else {
            this.jhiAlertService.error('global.error.theFieldNameIsEmpty');
        }
    }

    onSelectAuditorChange() {
        this.isOtherAuditorNameDisabled = !(!isNullOrUndefined(this.breakdownAuditor.auditor) && this.breakdownAuditor.auditor.codGenerico === COD_AUDITOR_OTROS_9999);
        if (this.isOtherAuditorNameDisabled) {
            this.breakdownAuditor.otherAuditorName = null;
        }
    }

    private assignDescripcionCodigos(data: IDescripcionCodigo[]) {
        this.simpleComboBoxTypes = data;
    }

    private onError(errorMessage: string) {
        // this.jhiAlertService.error(errorMessage, null, null);
    }

    compareFn(item1: IDescripcionCodigo, item2: IDescripcionCodigo): boolean {
        return item1 && item2 ? item1.codGenerico === item2.codGenerico : item1 === item2;
    }

}
