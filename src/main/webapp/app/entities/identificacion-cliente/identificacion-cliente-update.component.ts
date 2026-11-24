import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IIdentificacionCliente } from 'app/shared/model/identificacion-cliente.model';
import { IdentificacionClienteService } from './identificacion-cliente.service';

@Component({
    selector: 'jhi-identificacion-cliente-update',
    templateUrl: './identificacion-cliente-update.component.html'
})
export class IdentificacionClienteUpdateComponent implements OnInit {
    identificacionCliente: IIdentificacionCliente;
    isSaving: boolean;
    fecCreacionDp: any;

    constructor(private identificacionClienteService: IdentificacionClienteService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ identificacionCliente }) => {
            this.identificacionCliente = identificacionCliente;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.identificacionCliente.id !== undefined) {
            this.subscribeToSaveResponse(this.identificacionClienteService.update(this.identificacionCliente));
        } else {
            this.subscribeToSaveResponse(this.identificacionClienteService.create(this.identificacionCliente));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IIdentificacionCliente>>) {
        result.subscribe(
            (res: HttpResponse<IIdentificacionCliente>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
