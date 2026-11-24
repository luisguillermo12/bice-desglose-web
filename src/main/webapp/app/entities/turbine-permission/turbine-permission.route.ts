import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITurbinePermission, TurbinePermission } from 'app/shared/model/turbine-permission.model';
import { TurbinePermissionService } from './turbine-permission.service';
import { TurbinePermissionComponent } from './turbine-permission.component';
import { TurbinePermissionDetailComponent } from './turbine-permission-detail.component';
import { TurbinePermissionUpdateComponent } from './turbine-permission-update.component';
import { TurbinePermissionDeletePopupComponent } from './turbine-permission-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TurbinePermissionResolve implements Resolve<ITurbinePermission> {
    constructor(private service: TurbinePermissionService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((turbinePermission: HttpResponse<TurbinePermission>) => turbinePermission.body));
        }
        return of(new TurbinePermission());
    }
}

export const turbinePermissionRoute: Routes = [
    {
        path: 'turbine-permission',
        component: TurbinePermissionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.turbinePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-permission/:id/view',
        component: TurbinePermissionDetailComponent,
        resolve: {
            turbinePermission: TurbinePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbinePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-permission/new',
        component: TurbinePermissionUpdateComponent,
        resolve: {
            turbinePermission: TurbinePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbinePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-permission/:id/edit',
        component: TurbinePermissionUpdateComponent,
        resolve: {
            turbinePermission: TurbinePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbinePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const turbinePermissionPopupRoute: Routes = [
    {
        path: 'turbine-permission/:id/delete',
        component: TurbinePermissionDeletePopupComponent,
        resolve: {
            turbinePermission: TurbinePermissionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbinePermission.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
