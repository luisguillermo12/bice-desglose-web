import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';
import { DesgloseReemplazoService } from './desglose-reemplazo.service';

@Component({
    selector: 'jhi-desglose-reemplazo-update',
    templateUrl: './desglose-reemplazo-update.component.html'
})
export class DesgloseReemplazoUpdateComponent implements OnInit {
    desgloseReemplazo: IDesgloseReemplazo;
    isSaving: boolean;
    fecReemplazoDp: any;
    fecElimReemplazoDp: any;

    constructor(private desgloseReemplazoService: DesgloseReemplazoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseReemplazo }) => {
            this.desgloseReemplazo = desgloseReemplazo;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.desgloseReemplazo.id !== undefined) {
            this.subscribeToSaveResponse(this.desgloseReemplazoService.update(this.desgloseReemplazo));
        } else {
            this.subscribeToSaveResponse(this.desgloseReemplazoService.create(this.desgloseReemplazo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseReemplazo>>) {
        result.subscribe((res: HttpResponse<IDesgloseReemplazo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
