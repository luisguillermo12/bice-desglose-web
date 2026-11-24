import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';
import { TurbineUserGroupRoleService } from './turbine-user-group-role.service';
import { ITurbineUser } from 'app/shared/model/turbine-user.model';
import { TurbineUserService } from 'app/entities/turbine-user';
import { ITurbineGroup } from 'app/shared/model/turbine-group.model';
import { TurbineGroupService } from 'app/entities/turbine-group';
import { ITurbineRole } from 'app/shared/model/turbine-role.model';
import { TurbineRoleService } from 'app/entities/turbine-role';
import { URL_SEGMENT_NEW_ELEMENT } from 'app/app.constants';
import { isNullOrUndefined } from 'util';

@Component({
    selector: 'jhi-turbine-user-group-role-update',
    templateUrl: './turbine-user-group-role-update.component.html'
})
export class TurbineUserGroupRoleUpdateComponent implements OnInit {
    turbineUserGroupRole: ITurbineUserGroupRole;
    isSaving: boolean;
    users: ITurbineUser[];
    groups: ITurbineGroup[];
    roles: ITurbineRole[];
    currentRouteLastFragment: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private turbineUserGroupRoleService: TurbineUserGroupRoleService,
        private turbineUserService: TurbineUserService,
        private turbineGroupService: TurbineGroupService,
        private turbineRoleService: TurbineRoleService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ turbineUserGroupRole }) => {
            if (!isNullOrUndefined(this.activatedRoute.snapshot.url) && this.activatedRoute.snapshot.url.length > 0) {
                this.currentRouteLastFragment = this.activatedRoute.snapshot.url[this.activatedRoute.snapshot.url.length - 1].path;
            }
            this.turbineUserGroupRole = turbineUserGroupRole;
        });
        this.turbineUserService.query({ filter: 'turbineusergrouprole-is-null' }).subscribe(
            (res: HttpResponse<ITurbineUser[]>) => {
                if (!this.turbineUserGroupRole.userId) {
                    this.users = res.body;
                } else {
                    this.turbineUserService.find(this.turbineUserGroupRole.userId).subscribe(
                        (subRes: HttpResponse<ITurbineUser>) => {
                            this.users = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.turbineGroupService.query({ filter: 'turbineusergrouprole-is-null' }).subscribe(
            (res: HttpResponse<ITurbineGroup[]>) => {
                if (!this.turbineUserGroupRole.groupId) {
                    this.groups = res.body;
                } else {
                    this.turbineGroupService.find(this.turbineUserGroupRole.groupId).subscribe(
                        (subRes: HttpResponse<ITurbineGroup>) => {
                            this.groups = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.turbineRoleService.query({ filter: 'turbineusergrouprole-is-null' }).subscribe(
            (res: HttpResponse<ITurbineRole[]>) => {
                if (!this.turbineUserGroupRole.roleId) {
                    this.roles = res.body;
                } else {
                    this.turbineRoleService.find(this.turbineUserGroupRole.roleId).subscribe(
                        (subRes: HttpResponse<ITurbineRole>) => {
                            this.roles = [subRes.body].concat(res.body);
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
            this.subscribeToSaveResponse(this.turbineUserGroupRoleService.update(this.turbineUserGroupRole));
        } else {
            this.subscribeToSaveResponse(this.turbineUserGroupRoleService.create(this.turbineUserGroupRole));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITurbineUserGroupRole>>) {
        result.subscribe(
            (res: HttpResponse<ITurbineUserGroupRole>) => this.onSaveSuccess(),
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

    trackTurbineUserById(index: number, item: ITurbineUser) {
        return item.userId;
    }

    trackTurbineGroupById(index: number, item: ITurbineGroup) {
        return item.groupId;
    }

    trackTurbineRoleById(index: number, item: ITurbineRole) {
        return item.roleId;
    }
}
