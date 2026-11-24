import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPeriodo, Periodo } from 'app/shared/model/periodo.model';
import { PeriodoService } from './periodo.service';
import { PeriodoComponent } from './periodo.component';
import { PeriodoDetailComponent } from './periodo-detail.component';
import { PeriodoUpdateComponent } from './periodo-update.component';
import { PeriodoDeletePopupComponent } from './periodo-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class PeriodoResolve implements Resolve<IPeriodo> {
    constructor(private service: PeriodoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const codCliente = route.params[ 'codCliente' ] ? route.params[ 'codCliente' ] : null;
        const codTipoDes = route.params[ 'codTipoDes' ] ? route.params[ 'codTipoDes' ] : null;
        const codSubtipoDes = route.params[ 'codSubtipoDes' ] ? route.params[ 'codSubtipoDes' ] : null;
        const codMoneda = route.params[ 'codMoneda' ] ? route.params[ 'codMoneda' ] : null;
        if (codCliente && codTipoDes && codSubtipoDes && codMoneda) {
            return this.service
            .find(codCliente, codTipoDes, codSubtipoDes, codMoneda)
            .pipe(map((periodo: HttpResponse<Periodo>) => periodo.body));
        }
        return of(new Periodo());
    }
}

export const periodoRoute: Routes = [
    {
        path: 'periodo',
        component: PeriodoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codCliente,asc',
            pageTitle: 'desgloseApp.periodo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/view',
        component: PeriodoDetailComponent,
        resolve: {
            periodo: PeriodoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo/new',
        component: PeriodoUpdateComponent,
        resolve: {
            periodo: PeriodoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/edit',
        component: PeriodoUpdateComponent,
        resolve: {
            periodo: PeriodoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const periodoPopupRoute: Routes = [
    {
        path: 'periodo/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/delete',
        component: PeriodoDeletePopupComponent,
        resolve: {
            periodo: PeriodoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodo.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
