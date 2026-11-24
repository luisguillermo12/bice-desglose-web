import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITurbineRole, TurbineRole } from 'app/shared/model/turbine-role.model';
import { TurbineRoleService } from './turbine-role.service';
import { TurbineRoleComponent } from './turbine-role.component';
import { TurbineRoleDetailComponent } from './turbine-role-detail.component';
import { TurbineRoleUpdateComponent } from './turbine-role-update.component';
import { TurbineRoleDeletePopupComponent } from './turbine-role-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TurbineRoleResolve implements Resolve<ITurbineRole> {
    constructor(private service: TurbineRoleService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((turbineRole: HttpResponse<TurbineRole>) => turbineRole.body));
        }
        return of(new TurbineRole());
    }
}

export const turbineRoleRoute: Routes = [
    {
        path: 'turbine-role',
        component: TurbineRoleComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'roleId,asc',
            pageTitle: 'desgloseApp.turbineRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-role/:id/view',
        component: TurbineRoleDetailComponent,
        resolve: {
            turbineRole: TurbineRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-role/new',
        component: TurbineRoleUpdateComponent,
        resolve: {
            turbineRole: TurbineRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-role/:id/edit',
        component: TurbineRoleUpdateComponent,
        resolve: {
            turbineRole: TurbineRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRole.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const turbineRolePopupRoute: Routes = [
    {
        path: 'turbine-role/:id/delete',
        component: TurbineRoleDeletePopupComponent,
        resolve: {
            turbineRole: TurbineRoleResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineRole.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
