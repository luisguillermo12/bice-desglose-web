import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IClienteJuridico } from 'app/shared/model/cliente-juridico.model';
import { ClienteJuridicoService } from './cliente-juridico.service';

@Component({
    selector: 'jhi-cliente-juridico-update',
    templateUrl: './cliente-juridico-update.component.html'
})
export class ClienteJuridicoUpdateComponent implements OnInit {
    clienteJuridico: IClienteJuridico;
    isSaving: boolean;
    fecConstitucionDp: any;

    constructor(private clienteJuridicoService: ClienteJuridicoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ clienteJuridico }) => {
            this.clienteJuridico = clienteJuridico;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.clienteJuridico.id !== undefined) {
            this.subscribeToSaveResponse(this.clienteJuridicoService.update(this.clienteJuridico));
        } else {
            this.subscribeToSaveResponse(this.clienteJuridicoService.create(this.clienteJuridico));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IClienteJuridico>>) {
        result.subscribe((res: HttpResponse<IClienteJuridico>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
