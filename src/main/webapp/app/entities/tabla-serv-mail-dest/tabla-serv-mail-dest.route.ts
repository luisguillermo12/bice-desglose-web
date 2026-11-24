import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITablaServMailDest, TablaServMailDest } from 'app/shared/model/tabla-serv-mail-dest.model';
import { TablaServMailDestService } from './tabla-serv-mail-dest.service';
import { TablaServMailDestComponent } from './tabla-serv-mail-dest.component';
import { TablaServMailDestDetailComponent } from './tabla-serv-mail-dest-detail.component';
import { TablaServMailDestUpdateComponent } from './tabla-serv-mail-dest-update.component';
import { TablaServMailDestDeletePopupComponent } from './tabla-serv-mail-dest-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TablaServMailDestResolve implements Resolve<ITablaServMailDest> {
    constructor(private service: TablaServMailDestService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((tablaServMailDest: HttpResponse<TablaServMailDest>) => tablaServMailDest.body));
        }
        return of(new TablaServMailDest());
    }
}

export const tablaServMailDestRoute: Routes = [
    {
        path: 'tabla-serv-mail-dest',
        component: TablaServMailDestComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.tablaServMailDest.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-serv-mail-dest/:id/view',
        component: TablaServMailDestDetailComponent,
        resolve: {
            tablaServMailDest: TablaServMailDestResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServMailDest.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-serv-mail-dest/new',
        component: TablaServMailDestUpdateComponent,
        resolve: {
            tablaServMailDest: TablaServMailDestResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServMailDest.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-serv-mail-dest/:id/edit',
        component: TablaServMailDestUpdateComponent,
        resolve: {
            tablaServMailDest: TablaServMailDestResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServMailDest.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const tablaServMailDestPopupRoute: Routes = [
    {
        path: 'tabla-serv-mail-dest/:id/delete',
        component: TablaServMailDestDeletePopupComponent,
        resolve: {
            tablaServMailDest: TablaServMailDestResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServMailDest.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
