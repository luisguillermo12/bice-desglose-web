import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IPeriodo } from 'app/shared/model/periodo.model';
import { PeriodoService } from './periodo.service';
import { isNullOrUndefined } from 'util';
import { URL_SEGMENT_NEW_ELEMENT } from 'app/app.constants';

@Component({
    selector: 'jhi-periodo-update',
    templateUrl: './periodo-update.component.html'
})
export class PeriodoUpdateComponent implements OnInit {
    periodo: IPeriodo;
    isSaving: boolean;
    fecPeriodoDp: any;
    fecUserCreaDp: any;
    fecUserModifDp: any;
    fecInformSbifDp: any;
    currentRouteLastFragment: string;

    constructor(private periodoService: PeriodoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ periodo }) => {
            if (!isNullOrUndefined(this.activatedRoute.snapshot.url) && this.activatedRoute.snapshot.url.length > 0) {
                this.currentRouteLastFragment = this.activatedRoute.snapshot.url[this.activatedRoute.snapshot.url.length - 1].path;
            }
            this.periodo = periodo;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (URL_SEGMENT_NEW_ELEMENT !== this.currentRouteLastFragment) {
            this.subscribeToSaveResponse(this.periodoService.update(this.periodo));
        } else {
            this.subscribeToSaveResponse(this.periodoService.create(this.periodo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodo>>) {
        result.subscribe((res: HttpResponse<IPeriodo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
