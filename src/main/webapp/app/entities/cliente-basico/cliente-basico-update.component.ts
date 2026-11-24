import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IClienteBasico } from 'app/shared/model/cliente-basico.model';
import { ClienteBasicoService } from './cliente-basico.service';

@Component({
    selector: 'jhi-cliente-basico-update',
    templateUrl: './cliente-basico-update.component.html'
})
export class ClienteBasicoUpdateComponent implements OnInit {
    clienteBasico: IClienteBasico;
    isSaving: boolean;
    fecClienteDp: any;
    fecDeclJuradaDp: any;
    fecVerifDomlegalDp: any;
    fecFirmaCobprejudDp: any;
    fecAlertaDp: any;

    constructor(private clienteBasicoService: ClienteBasicoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ clienteBasico }) => {
            this.clienteBasico = clienteBasico;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.clienteBasico.id !== undefined) {
            this.subscribeToSaveResponse(this.clienteBasicoService.update(this.clienteBasico));
        } else {
            this.subscribeToSaveResponse(this.clienteBasicoService.create(this.clienteBasico));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IClienteBasico>>) {
        result.subscribe((res: HttpResponse<IClienteBasico>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
