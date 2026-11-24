import { Component, EventEmitter, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBreakdownValidationError } from 'app/entities/desglose/breakdown-validation-error.model';
import { DATE_FORMAT_ES_UI } from 'app/shared';

@Component({
    selector: 'jhi-breakdown-errors-dialog',
    templateUrl: './breakdown-errors-dialog.component.html',
    styleUrls: [ './breakdown-errors-dialog.scss' ]
})
export class BreakdownErrorsDialogComponent implements OnInit {

    iBreakdownValidationErrorList: IBreakdownValidationError[] = [];
    DATE_FORMAT_ES_UI = DATE_FORMAT_ES_UI;
    isAnyValidationRequired = true;
    public onClickAcceptEvent: EventEmitter<boolean> = new EventEmitter();
    public questionMessage = 'breakdown.errors.questionMessageSave';
    public mostrarColumnas = true;
    constructor(public activeModal: NgbActiveModal) {
    }

    ngOnInit() {
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    triggerOnClickAcceptEvent(result: boolean) {
        this.onClickAcceptEvent.emit(result);
    }
}
