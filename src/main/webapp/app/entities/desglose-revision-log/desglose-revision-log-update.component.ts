import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDesgloseRevisionLog } from 'app/shared/model/desglose-revision-log.model';
import { DesgloseRevisionLogService } from './desglose-revision-log.service';

@Component({
    selector: 'jhi-desglose-revision-log-update',
    templateUrl: './desglose-revision-log-update.component.html'
})
export class DesgloseRevisionLogUpdateComponent implements OnInit {
    desgloseRevisionLog: IDesgloseRevisionLog;
    isSaving: boolean;
    fecEvent: string;

    constructor(private desgloseRevisionLogService: DesgloseRevisionLogService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseRevisionLog }) => {
            this.desgloseRevisionLog = desgloseRevisionLog;
            this.fecEvent = this.desgloseRevisionLog.fecEvent != null ? this.desgloseRevisionLog.fecEvent.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.desgloseRevisionLog.fecEvent = this.fecEvent != null ? moment(this.fecEvent, DATE_TIME_FORMAT) : null;
        if (this.desgloseRevisionLog.id !== undefined) {
            this.subscribeToSaveResponse(this.desgloseRevisionLogService.update(this.desgloseRevisionLog));
        } else {
            this.subscribeToSaveResponse(this.desgloseRevisionLogService.create(this.desgloseRevisionLog));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseRevisionLog>>) {
        result.subscribe((res: HttpResponse<IDesgloseRevisionLog>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
