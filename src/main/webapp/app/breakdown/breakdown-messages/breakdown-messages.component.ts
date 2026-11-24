import { Component, EventEmitter, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';
import { DATE_FORMAT_PERIOD_ES_UI_HEADER, DATE_TIME_SECONDS_FORMAT_ES_UI } from 'app/shared';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'jhi-breakdown-messages',
    templateUrl: './breakdown-messages.component.html',
    styleUrls: [ './breakdown-messages.component.scss' ]
})
export class BreakdownMessagesComponent implements OnInit {

    mensajeSoloLectura: IDesgloseMensaje[];
    mensaje = '';
    mensajeEscritura: string;
    isHiddenEscritura = false;
    public enviarMessagesEmit: EventEmitter<any> = new EventEmitter();

    constructor(public activeModal: NgbActiveModal, private datePipe: DatePipe) {
    }

    ngOnInit() {
        this.mensajeSoloLectura.forEach((mensaje, index) => {
            this.mensaje = this.mensaje +  mensaje.glsComentarios + '\n';
        });
    }

    closeModal() {
        this.enviarMessagesEmit.emit(this.mensajeEscritura);
        this.activeModal.dismiss('cancel');
    }

    onClickAceptar() {
        this.enviarMessagesEmit.emit(this.mensajeEscritura);
        this.activeModal.dismiss('cancel');
    }

    onClickCancel() {
        this.enviarMessagesEmit.emit(this.mensajeEscritura);
        this.activeModal.dismiss('cancel');
    }
}
