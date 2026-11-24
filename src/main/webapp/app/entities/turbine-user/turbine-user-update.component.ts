import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbineUser } from 'app/shared/model/turbine-user.model';
import { TurbineUserService } from './turbine-user.service';

@Component({
    selector: 'jhi-turbine-user-update',
    templateUrl: './turbine-user-update.component.html'
})
export class TurbineUserUpdateComponent implements OnInit {
    turbineUser: ITurbineUser;
    isSaving: boolean;
    modifiedDp: any;
    createdDp: any;
    lastLoginDp: any;
    expireDp: any;
    expirePasswordDp: any;

    constructor(private dataUtils: JhiDataUtils, private turbineUserService: TurbineUserService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ turbineUser }) => {
            this.turbineUser = turbineUser;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.turbineUser.userId !== undefined) {
            this.subscribeToSaveResponse(this.turbineUserService.update(this.turbineUser));
        } else {
            this.subscribeToSaveResponse(this.turbineUserService.create(this.turbineUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITurbineUser>>) {
        result.subscribe((res: HttpResponse<ITurbineUser>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
