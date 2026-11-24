import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IClienteNatural } from 'app/shared/model/cliente-natural.model';
import { ClienteNaturalService } from './cliente-natural.service';

@Component({
    selector: 'jhi-cliente-natural-update',
    templateUrl: './cliente-natural-update.component.html'
})
export class ClienteNaturalUpdateComponent implements OnInit {
    clienteNatural: IClienteNatural;
    isSaving: boolean;
    fecNacimDp: any;
    fecAntigVivDp: any;
    fecDefuncionDp: any;
    fecCertEciviDp: any;

    constructor(private clienteNaturalService: ClienteNaturalService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ clienteNatural }) => {
            this.clienteNatural = clienteNatural;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.clienteNatural.id !== undefined) {
            this.subscribeToSaveResponse(this.clienteNaturalService.update(this.clienteNatural));
        } else {
            this.subscribeToSaveResponse(this.clienteNaturalService.create(this.clienteNatural));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IClienteNatural>>) {
        result.subscribe((res: HttpResponse<IClienteNatural>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
