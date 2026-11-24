import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDesgloseChequeoInfo } from 'app/shared/model/desglose-chequeo-info.model';
import { DesgloseChequeoInfoService } from './desglose-chequeo-info.service';

@Component({
    selector: 'jhi-desglose-chequeo-info-update',
    templateUrl: './desglose-chequeo-info-update.component.html'
})
export class DesgloseChequeoInfoUpdateComponent implements OnInit {
    desgloseChequeoInfo: IDesgloseChequeoInfo;
    isSaving: boolean;
    fecCheck: string;
    fecPeriodoDp: any;
    fecUserModifDp: any;

    constructor(private desgloseChequeoInfoService: DesgloseChequeoInfoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseChequeoInfo }) => {
            this.desgloseChequeoInfo = desgloseChequeoInfo;
            this.fecCheck = this.desgloseChequeoInfo.fecCheck != null ? this.desgloseChequeoInfo.fecCheck.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.desgloseChequeoInfo.fecCheck = this.fecCheck != null ? moment(this.fecCheck, DATE_TIME_FORMAT) : null;
        if (this.desgloseChequeoInfo.id !== undefined) {
            this.subscribeToSaveResponse(this.desgloseChequeoInfoService.update(this.desgloseChequeoInfo));
        } else {
            this.subscribeToSaveResponse(this.desgloseChequeoInfoService.create(this.desgloseChequeoInfo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseChequeoInfo>>) {
        result.subscribe((res: HttpResponse<IDesgloseChequeoInfo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
