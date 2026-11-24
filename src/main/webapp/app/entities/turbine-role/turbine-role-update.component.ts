import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbineRole } from 'app/shared/model/turbine-role.model';
import { TurbineRoleService } from './turbine-role.service';

@Component({
    selector: 'jhi-turbine-role-update',
    templateUrl: './turbine-role-update.component.html'
})
export class TurbineRoleUpdateComponent implements OnInit {
    turbineRole: ITurbineRole;
    isSaving: boolean;

    constructor(private dataUtils: JhiDataUtils, private turbineRoleService: TurbineRoleService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ turbineRole }) => {
            this.turbineRole = turbineRole;
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
        if (this.turbineRole.roleId !== undefined) {
            this.subscribeToSaveResponse(this.turbineRoleService.update(this.turbineRole));
        } else {
            this.subscribeToSaveResponse(this.turbineRoleService.create(this.turbineRole));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITurbineRole>>) {
        result.subscribe((res: HttpResponse<ITurbineRole>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
