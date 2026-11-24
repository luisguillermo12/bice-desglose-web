import { Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import * as jQuery from 'jquery';
@Component({
    selector: 'jhi-proyections-selection-print-dialog',
    templateUrl: './proyections-selection-print-dialog.component.html',
    styleUrls: [ './proyections-selection-print-dialog.scss' ]
})
export class ProyectionsSelectionPrintDialogComponent implements OnInit {
    result: number;
    periodo:number;
    @ViewChild('console') console: ElementRef;
    public saveSelectionPrintDialogEmit: EventEmitter<any> = new EventEmitter();
    constructor(public activeModal: NgbActiveModal, private jhiAlertService: JhiAlertService) {}

    ngOnInit() {
        this.result = 0;
        jQuery('#console').html('Calculando Periodo: ' + (this.periodo));
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }
    onClickCancel() {
        this.closeModal();
    }
    private onError(errorMessage: string) {
    }

}
