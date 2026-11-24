import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IUniqueSessionControl } from 'app/shared/model/unique-session-control.model';
import { UniqueSessionControlService } from './unique-session-control.service';

@Component({
    selector: 'jhi-unique-session-control-update',
    templateUrl: './unique-session-control-update.component.html'
})
export class UniqueSessionControlUpdateComponent implements OnInit {
    uniqueSessionControl: IUniqueSessionControl;
    isSaving: boolean;
    sessionCheckTime: string;
    sessionEndTime: string;

    constructor(private uniqueSessionControlService: UniqueSessionControlService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ uniqueSessionControl }) => {
            this.uniqueSessionControl = uniqueSessionControl;
            this.sessionCheckTime =
                this.uniqueSessionControl.sessionCheckTime != null
                    ? this.uniqueSessionControl.sessionCheckTime.format(DATE_TIME_FORMAT)
                    : null;
            this.sessionEndTime =
                this.uniqueSessionControl.sessionEndTime != null ? this.uniqueSessionControl.sessionEndTime.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.uniqueSessionControl.sessionCheckTime = this.sessionCheckTime != null ? moment(this.sessionCheckTime, DATE_TIME_FORMAT) : null;
        this.uniqueSessionControl.sessionEndTime = this.sessionEndTime != null ? moment(this.sessionEndTime, DATE_TIME_FORMAT) : null;
        if (this.uniqueSessionControl.id !== undefined) {
            this.subscribeToSaveResponse(this.uniqueSessionControlService.update(this.uniqueSessionControl));
        } else {
            this.subscribeToSaveResponse(this.uniqueSessionControlService.create(this.uniqueSessionControl));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUniqueSessionControl>>) {
        result.subscribe(
            (res: HttpResponse<IUniqueSessionControl>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
