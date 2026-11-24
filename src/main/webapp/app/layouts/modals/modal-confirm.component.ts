import { Component, EventEmitter, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-modal-confirm',
    templateUrl: './modal-confirm.component.html',
    styleUrls: [ './modal-confirm.component.scss' ]
})
export class ModalConfirmComponent implements OnInit {

    public onOkEmitter: EventEmitter<any> = new EventEmitter();
    public onCancelEmitter: EventEmitter<any> = new EventEmitter();
    public translateValues: any = {};

    constructor(public activeModal: NgbActiveModal) {
    }

    ngOnInit() {
    }

    public onClickEmitCancelAction() {
        this.onCancelEmitter.emit();
    }

    public onClickEmitOkAction() {
        this.onOkEmitter.emit();
    }

    public closeModalConfirm() {
        this.activeModal.dismiss('cancel');
    }

    private onError(errorMessage: string) {
    }
}
