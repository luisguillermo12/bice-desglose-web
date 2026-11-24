import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDesgloseChequeoLog } from 'app/shared/model/desglose-chequeo-log.model';
import { DesgloseChequeoLogService } from './desglose-chequeo-log.service';

@Component({
    selector: 'jhi-desglose-chequeo-log-update',
    templateUrl: './desglose-chequeo-log-update.component.html'
})
export class DesgloseChequeoLogUpdateComponent implements OnInit {
    desgloseChequeoLog: IDesgloseChequeoLog;
    isSaving: boolean;
    fecLog: string;

    constructor(private desgloseChequeoLogService: DesgloseChequeoLogService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseChequeoLog }) => {
            this.desgloseChequeoLog = desgloseChequeoLog;
            this.fecLog = this.desgloseChequeoLog.fecLog != null ? this.desgloseChequeoLog.fecLog.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.desgloseChequeoLog.fecLog = this.fecLog != null ? moment(this.fecLog, DATE_TIME_FORMAT) : null;
        if (this.desgloseChequeoLog.id !== undefined) {
            this.subscribeToSaveResponse(this.desgloseChequeoLogService.update(this.desgloseChequeoLog));
        } else {
            this.subscribeToSaveResponse(this.desgloseChequeoLogService.create(this.desgloseChequeoLog));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseChequeoLog>>) {
        result.subscribe((res: HttpResponse<IDesgloseChequeoLog>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
