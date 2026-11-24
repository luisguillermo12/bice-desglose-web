import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITurbineGroup, TurbineGroup } from 'app/shared/model/turbine-group.model';
import { TurbineGroupService } from './turbine-group.service';
import { TurbineGroupComponent } from './turbine-group.component';
import { TurbineGroupDetailComponent } from './turbine-group-detail.component';
import { TurbineGroupUpdateComponent } from './turbine-group-update.component';
import { TurbineGroupDeletePopupComponent } from './turbine-group-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TurbineGroupResolve implements Resolve<ITurbineGroup> {
    constructor(private service: TurbineGroupService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((turbineGroup: HttpResponse<TurbineGroup>) => turbineGroup.body));
        }
        return of(new TurbineGroup());
    }
}

export const turbineGroupRoute: Routes = [
    {
        path: 'turbine-group',
        component: TurbineGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.turbineGroup.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-group/:id/view',
        component: TurbineGroupDetailComponent,
        resolve: {
            turbineGroup: TurbineGroupResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineGroup.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-group/new',
        component: TurbineGroupUpdateComponent,
        resolve: {
            turbineGroup: TurbineGroupResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineGroup.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'turbine-group/:id/edit',
        component: TurbineGroupUpdateComponent,
        resolve: {
            turbineGroup: TurbineGroupResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineGroup.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const turbineGroupPopupRoute: Routes = [
    {
        path: 'turbine-group/:id/delete',
        component: TurbineGroupDeletePopupComponent,
        resolve: {
            turbineGroup: TurbineGroupResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.turbineGroup.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
