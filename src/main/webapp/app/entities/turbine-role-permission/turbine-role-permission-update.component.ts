import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITurbineRolePermission } from 'app/shared/model/turbine-role-permission.model';
import { TurbineRolePermissionService } from './turbine-role-permission.service';
import { ITurbineRole } from 'app/shared/model/turbine-role.model';
import { TurbineRoleService } from 'app/entities/turbine-role';
import { ITurbinePermission } from 'app/shared/model/turbine-permission.model';
import { TurbinePermissionService } from 'app/entities/turbine-permission';
import { URL_SEGMENT_NEW_ELEMENT } from 'app/app.constants';
import { isNullOrUndefined } from 'util';

@Component({
    selector: 'jhi-turbine-role-permission-update',
    templateUrl: './turbine-role-permission-update.component.html'
})
export class TurbineRolePermissionUpdateComponent implements OnInit {
    turbineRolePermission: ITurbineRolePermission;
    isSaving: boolean;
    roles: ITurbineRole[];
    permissions: ITurbinePermission[];
    currentRouteLastFragment: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private turbineRolePermissionService: TurbineRolePermissionService,
        private turbineRoleService: TurbineRoleService,
        private turbinePermissionService: TurbinePermissionService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ turbineRolePermission }) => {
            if (!isNullOrUndefined(this.activatedRoute.snapshot.url) && this.activatedRoute.snapshot.url.length > 0) {
                this.currentRouteLastFragment = this.activatedRoute.snapshot.url[this.activatedRoute.snapshot.url.length - 1].path;
            }
            this.turbineRolePermission = turbineRolePermission;
        });
        this.turbineRoleService.query({ filter: 'turbinerolepermission-is-null' }).subscribe(
            (res: HttpResponse<ITurbineRole[]>) => {
                if (!this.turbineRolePermission.roleId) {
                    this.roles = res.body;
                } else {
                    this.turbineRoleService.find(this.turbineRolePermission.roleId).subscribe(
                        (subRes: HttpResponse<ITurbineRole>) => {
                            this.roles = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.turbinePermissionService.query({ filter: 'turbinerolepermission-is-null' }).subscribe(
            (res: HttpResponse<ITurbinePermission[]>) => {
                if (!this.turbineRolePermission.permissionId) {
                    this.permissions = res.body;
                } else {
                    this.turbinePermissionService.find(this.turbineRolePermission.permissionId).subscribe(
                        (subRes: HttpResponse<ITurbinePermission>) => {
                            this.permissions = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (URL_SEGMENT_NEW_ELEMENT !== this.currentRouteLastFragment) {
            this.subscribeToSaveResponse(this.turbineRolePermissionService.update(this.turbineRolePermission));
        } else {
            this.subscribeToSaveResponse(this.turbineRolePermissionService.create(this.turbineRolePermission));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITurbineRolePermission>>) {
        result.subscribe(
            (res: HttpResponse<ITurbineRolePermission>) => this.onSaveSuccess(),
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

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackTurbineRoleById(index: number, item: ITurbineRole) {
        return item.roleId;
    }

    trackTurbinePermissionById(index: number, item: ITurbinePermission) {
        return item.permissionId;
    }
}
