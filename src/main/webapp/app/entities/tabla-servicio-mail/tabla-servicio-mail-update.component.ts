import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { ITablaServicioMail } from 'app/shared/model/tabla-servicio-mail.model';
import { TablaServicioMailService } from './tabla-servicio-mail.service';

@Component({
    selector: 'jhi-tabla-servicio-mail-update',
    templateUrl: './tabla-servicio-mail-update.component.html'
})
export class TablaServicioMailUpdateComponent implements OnInit {
    tablaServicioMail: ITablaServicioMail;
    isSaving: boolean;
    fecSolicitudDp: any;
    fecEnvMailDp: any;

    constructor(private tablaServicioMailService: TablaServicioMailService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tablaServicioMail }) => {
            this.tablaServicioMail = tablaServicioMail;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.tablaServicioMail.glsMensaje !== undefined) {
            this.subscribeToSaveResponse(this.tablaServicioMailService.update(this.tablaServicioMail));
        } else {
            this.subscribeToSaveResponse(this.tablaServicioMailService.create(this.tablaServicioMail));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITablaServicioMail>>) {
        result.subscribe((res: HttpResponse<ITablaServicioMail>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
