import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IDesgloseRevision } from 'app/shared/model/desglose-revision.model';
import { DesgloseRevisionService } from './desglose-revision.service';

@Component({
    selector: 'jhi-desglose-revision-update',
    templateUrl: './desglose-revision-update.component.html'
})
export class DesgloseRevisionUpdateComponent implements OnInit {
    desgloseRevision: IDesgloseRevision;
    isSaving: boolean;
    fecEnvioRevisionDp: any;
    fecEnRevisionDp: any;
    fecTerminoRevisionDp: any;

    constructor(private desgloseRevisionService: DesgloseRevisionService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseRevision }) => {
            this.desgloseRevision = desgloseRevision;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.desgloseRevision.codTipoDes !== undefined) {
            this.subscribeToSaveResponse(this.desgloseRevisionService.update(this.desgloseRevision));
        } else {
            this.subscribeToSaveResponse(this.desgloseRevisionService.create(this.desgloseRevision));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseRevision>>) {
        result.subscribe((res: HttpResponse<IDesgloseRevision>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
