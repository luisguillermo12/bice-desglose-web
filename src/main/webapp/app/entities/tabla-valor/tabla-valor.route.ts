import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITablaValor, TablaValor } from 'app/shared/model/tabla-valor.model';
import { TablaValorService } from './tabla-valor.service';
import { TablaValorComponent } from './tabla-valor.component';
import { TablaValorDetailComponent } from './tabla-valor-detail.component';
import { TablaValorUpdateComponent } from './tabla-valor-update.component';
import { TablaValorDeletePopupComponent } from './tabla-valor-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TablaValorResolve implements Resolve<ITablaValor> {
    constructor(private service: TablaValorService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((tablaValor: HttpResponse<TablaValor>) => tablaValor.body));
        }
        return of(new TablaValor());
    }
}

export const tablaValorRoute: Routes = [
    {
        path: 'tabla-valor',
        component: TablaValorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.tablaValor.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-valor/:id/view',
        component: TablaValorDetailComponent,
        resolve: {
            tablaValor: TablaValorResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaValor.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-valor/new',
        component: TablaValorUpdateComponent,
        resolve: {
            tablaValor: TablaValorResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaValor.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-valor/:id/edit',
        component: TablaValorUpdateComponent,
        resolve: {
            tablaValor: TablaValorResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaValor.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const tablaValorPopupRoute: Routes = [
    {
        path: 'tabla-valor/:id/delete',
        component: TablaValorDeletePopupComponent,
        resolve: {
            tablaValor: TablaValorResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaValor.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
