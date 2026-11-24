import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';
import { DesgloseMensajeService } from './desglose-mensaje.service';

@Component({
    selector: 'jhi-desglose-mensaje-update',
    templateUrl: './desglose-mensaje-update.component.html'
})
export class DesgloseMensajeUpdateComponent implements OnInit {
    desgloseMensaje: IDesgloseMensaje;
    isSaving: boolean;
    fecEvent: string;

    constructor(private desgloseMensajeService: DesgloseMensajeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseMensaje }) => {
            this.desgloseMensaje = desgloseMensaje;
            this.fecEvent = this.desgloseMensaje.fecEvent != null ? this.desgloseMensaje.fecEvent.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.desgloseMensaje.fecEvent = this.fecEvent != null ? moment(this.fecEvent, DATE_TIME_FORMAT) : null;
        if (this.desgloseMensaje.codCliente !== undefined) {
            this.subscribeToSaveResponse(this.desgloseMensajeService.update(this.desgloseMensaje));
        } else {
            this.subscribeToSaveResponse(this.desgloseMensajeService.create(this.desgloseMensaje));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseMensaje>>) {
        result.subscribe((res: HttpResponse<IDesgloseMensaje>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
