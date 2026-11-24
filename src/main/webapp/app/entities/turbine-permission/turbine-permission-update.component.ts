import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbinePermission } from 'app/shared/model/turbine-permission.model';
import { TurbinePermissionService } from './turbine-permission.service';

@Component({
    selector: 'jhi-turbine-permission-update',
    templateUrl: './turbine-permission-update.component.html'
})
export class TurbinePermissionUpdateComponent implements OnInit {
    turbinePermission: ITurbinePermission;
    isSaving: boolean;

    constructor(
        private dataUtils: JhiDataUtils,
        private turbinePermissionService: TurbinePermissionService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ turbinePermission }) => {
            this.turbinePermission = turbinePermission;
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
        if (this.turbinePermission.permissionId !== undefined) {
            this.subscribeToSaveResponse(this.turbinePermissionService.update(this.turbinePermission));
        } else {
            this.subscribeToSaveResponse(this.turbinePermissionService.create(this.turbinePermission));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITurbinePermission>>) {
        result.subscribe((res: HttpResponse<ITurbinePermission>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
