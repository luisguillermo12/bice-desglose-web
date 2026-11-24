import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITurbineUserGroupRole, TurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';
import { TurbineUserGroupRoleService } from './turbine-user-group-role.service';
import { TurbineUserGroupRoleComponent } from './turbine-user-group-role.component';
import { TurbineUserGroupRoleDetailComponent } from './turbine-user-group-role-detail.component';
import { TurbineUserGroupRoleUpdateComponent } from './turbine-user-group-role-update.component';
import { TurbineUserGroupRoleDeletePopupComponent } from './turbine-user-group-role-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TurbineUserGroupRoleResolve implements Resolve<ITurbineUserGroupRole> {
    constructor(private service: TurbineUserGroupRoleService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((turbineUserGroupRole: HttpResponse<TurbineUserGroupRole>) => turbineUserGroupRole.body));
        }
        return of(new TurbineUserGroupRole());
    }
}

export const turbineUserGroupRoleRoute: Routes = [
    {
        path: 'turbine-user-group-role',
        component: TurbineUserGroupRoleComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.turbineUserGroupRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-user-group-role/:id/view',
        component: TurbineUserGroupRoleDetailComponent,
        resolve: {
            turbineUserGroupRole: TurbineUserGroupRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUserGroupRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-user-group-role/new',
        component: TurbineUserGroupRoleUpdateComponent,
        resolve: {
            turbineUserGroupRole: TurbineUserGroupRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUserGroupRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-user-group-role/:id/edit',
        component: TurbineUserGroupRoleUpdateComponent,
        resolve: {
            turbineUserGroupRole: TurbineUserGroupRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUserGroupRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const turbineUserGroupRolePopupRoute: Routes = [
    {
        path: 'turbine-user-group-role/:id/delete',
        component: TurbineUserGroupRoleDeletePopupComponent,
        resolve: {
            turbineUserGroupRole: TurbineUserGroupRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUserGroupRole.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
