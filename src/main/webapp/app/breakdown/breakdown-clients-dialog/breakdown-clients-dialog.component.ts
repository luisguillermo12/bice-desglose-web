import { Component, EventEmitter, Input, OnInit } from '@angular/core';
import { ICliente } from 'app/shared/model/cliente.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ClientFilterValue } from 'app/shared/pipes/filter-clients-rut-no-rut.pipe';

@Component({
    selector: 'jhi-breakdown-clients-dialog',
    templateUrl: './breakdown-clients-dialog.component.html',
    styleUrls: ['./breakdown-clients-dialog.scss']
})
export class BreakdownClientsDialogComponent implements OnInit {
    public clients: ICliente[];
    public onSelectItem: EventEmitter<ICliente> = new EventEmitter();
    public isCollapsedBankClientsCompanyPanel = false;
    public isCollapsedBankClientsPersonPanel = false;
    public isCollapsedDataNoRutPanel = false;
    public RUT_COMPANY = ClientFilterValue.RUT_COMPANY;
    public RUT_PERSON = ClientFilterValue.RUT_PERSON;
    public NO_RUT = ClientFilterValue.NO_RUT;

    constructor(public activeModal: NgbActiveModal) {}

    ngOnInit() {}

    triggerOnSelectItemEvent(selectedItem: ICliente) {
        this.onSelectItem.emit(selectedItem);
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }
}
