import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IClienteSinRut } from 'app/shared/model/cliente-sin-rut.model';
import { ClienteSinRutService } from './cliente-sin-rut.service';

@Component({
    selector: 'jhi-cliente-sin-rut-update',
    templateUrl: './cliente-sin-rut-update.component.html'
})
export class ClienteSinRutUpdateComponent implements OnInit {
    clienteSinRut: IClienteSinRut;
    isSaving: boolean;

    constructor(private clienteSinRutService: ClienteSinRutService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ clienteSinRut }) => {
            this.clienteSinRut = clienteSinRut;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.clienteSinRut.codCliente !== undefined) {
            this.subscribeToSaveResponse(this.clienteSinRutService.update(this.clienteSinRut));
        } else {
            this.subscribeToSaveResponse(this.clienteSinRutService.create(this.clienteSinRut));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IClienteSinRut>>) {
        result.subscribe((res: HttpResponse<IClienteSinRut>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
