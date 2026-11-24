import { Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-breakdown-selection-print-dialog',
    templateUrl: './breakdown-selection-print-dialog.component.html',
    styleUrls: [ './breakdown-selection-print-dialog.scss' ]
})
export class BreakdownSelectionPrintDialogComponent implements OnInit {

    result: number;
    public saveSelectionPrintDialogEmit: EventEmitter<any> = new EventEmitter();
    constructor(public activeModal: NgbActiveModal, private jhiAlertService: JhiAlertService) {}

    ngOnInit() {
        this.onBreakdownSubTypeChange();
        this.result = 0;
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    onClickCancel() {
        this.closeModal();
    }
    onClickSave() {
        this.saveSelectionPrintDialogEmit.emit(this.result);
        this.closeModal();
    }
    private onError(errorMessage: string) {
    }

    onBreakdownSubTypeChange() {}
}
