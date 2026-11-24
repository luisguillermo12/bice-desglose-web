import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';
import { DesgloseAuditoriaService } from './desglose-auditoria.service';

@Component({
    selector: 'jhi-desglose-auditoria-update',
    templateUrl: './desglose-auditoria-update.component.html'
})
export class DesgloseAuditoriaUpdateComponent implements OnInit {
    desgloseAuditoria: IDesgloseAuditoria;
    isSaving: boolean;
    fecEventoDp: any;

    constructor(private desgloseAuditoriaService: DesgloseAuditoriaService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseAuditoria }) => {
            this.desgloseAuditoria = desgloseAuditoria;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.desgloseAuditoria.codCliente !== undefined) {
            this.subscribeToSaveResponse(this.desgloseAuditoriaService.update(this.desgloseAuditoria));
        } else {
            this.subscribeToSaveResponse(this.desgloseAuditoriaService.create(this.desgloseAuditoria));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseAuditoria>>) {
        result.subscribe((res: HttpResponse<IDesgloseAuditoria>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
