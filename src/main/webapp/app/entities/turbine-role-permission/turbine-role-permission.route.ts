import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITurbineRolePermission, TurbineRolePermission } from 'app/shared/model/turbine-role-permission.model';
import { TurbineRolePermissionService } from './turbine-role-permission.service';
import { TurbineRolePermissionComponent } from './turbine-role-permission.component';
import { TurbineRolePermissionDetailComponent } from './turbine-role-permission-detail.component';
import { TurbineRolePermissionUpdateComponent } from './turbine-role-permission-update.component';
import { TurbineRolePermissionDeletePopupComponent } from './turbine-role-permission-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TurbineRolePermissionResolve implements Resolve<ITurbineRolePermission> {
    constructor(private service: TurbineRolePermissionService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service
            .find(id)
            .pipe(map((turbineRolePermission: HttpResponse<TurbineRolePermission>) => turbineRolePermission.body));
        }
        return of(new TurbineRolePermission());
    }
}

export const turbineRolePermissionRoute: Routes = [
    {
        path: 'turbine-role-permission',
        component: TurbineRolePermissionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.turbineRolePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-role-permission/:id/view',
        component: TurbineRolePermissionDetailComponent,
        resolve: {
            turbineRolePermission: TurbineRolePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRolePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-role-permission/new',
        component: TurbineRolePermissionUpdateComponent,
        resolve: {
            turbineRolePermission: TurbineRolePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRolePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-role-permission/:id/edit',
        component: TurbineRolePermissionUpdateComponent,
        resolve: {
            turbineRolePermission: TurbineRolePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRolePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const turbineRolePermissionPopupRoute: Routes = [
    {
        path: 'turbine-role-permission/:id/delete',
        component: TurbineRolePermissionDeletePopupComponent,
        resolve: {
            turbineRolePermission: TurbineRolePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRolePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
