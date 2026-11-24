import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITurbineUser, TurbineUser } from 'app/shared/model/turbine-user.model';
import { TurbineUserService } from './turbine-user.service';
import { TurbineUserComponent } from './turbine-user.component';
import { TurbineUserDetailComponent } from './turbine-user-detail.component';
import { TurbineUserUpdateComponent } from './turbine-user-update.component';
import { TurbineUserDeletePopupComponent } from './turbine-user-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TurbineUserResolve implements Resolve<ITurbineUser> {
    constructor(private service: TurbineUserService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((turbineUser: HttpResponse<TurbineUser>) => turbineUser.body));
        }
        return of(new TurbineUser());
    }
}

export const turbineUserRoute: Routes = [
    {
        path: 'turbine-user',
        component: TurbineUserComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.turbineUser.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-user/:id/view',
        component: TurbineUserDetailComponent,
        resolve: {
            turbineUser: TurbineUserResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUser.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-user/new',
        component: TurbineUserUpdateComponent,
        resolve: {
            turbineUser: TurbineUserResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUser.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-user/:id/edit',
        component: TurbineUserUpdateComponent,
        resolve: {
            turbineUser: TurbineUserResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUser.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const turbineUserPopupRoute: Routes = [
    {
        path: 'turbine-user/:id/delete',
        component: TurbineUserDeletePopupComponent,
        resolve: {
            turbineUser: TurbineUserResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineUser.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
