import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBlackListJwt } from 'app/shared/model/black-list-jwt.model';
import { BlackListJwtService } from './black-list-jwt.service';

@Component({
    selector: 'jhi-black-list-jwt-update',
    templateUrl: './black-list-jwt-update.component.html'
})
export class BlackListJwtUpdateComponent implements OnInit {
    blackListJwt: IBlackListJwt;
    isSaving: boolean;
    invalidateTime: string;

    constructor(private blackListJwtService: BlackListJwtService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ blackListJwt }) => {
            this.blackListJwt = blackListJwt;
            this.invalidateTime =
                this.blackListJwt.invalidateTime != null ? this.blackListJwt.invalidateTime.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.blackListJwt.invalidateTime = this.invalidateTime != null ? moment(this.invalidateTime, DATE_TIME_FORMAT) : null;
        if (this.blackListJwt.id !== undefined) {
            this.subscribeToSaveResponse(this.blackListJwtService.update(this.blackListJwt));
        } else {
            this.subscribeToSaveResponse(this.blackListJwtService.create(this.blackListJwt));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBlackListJwt>>) {
        result.subscribe((res: HttpResponse<IBlackListJwt>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
