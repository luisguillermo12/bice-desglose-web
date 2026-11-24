import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPeriodoDesgConsol, PeriodoDesgConsol } from 'app/shared/model/periodo-desg-consol.model';
import { PeriodoDesgConsolService } from './periodo-desg-consol.service';
import { PeriodoDesgConsolComponent } from './periodo-desg-consol.component';
import { PeriodoDesgConsolDetailComponent } from './periodo-desg-consol-detail.component';
import { PeriodoDesgConsolUpdateComponent } from './periodo-desg-consol-update.component';
import { PeriodoDesgConsolDeletePopupComponent } from './periodo-desg-consol-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class PeriodoDesgConsolResolve implements Resolve<IPeriodoDesgConsol> {
    constructor(private service: PeriodoDesgConsolService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((periodoDesgConsol: HttpResponse<PeriodoDesgConsol>) => periodoDesgConsol.body));
        }
        return of(new PeriodoDesgConsol());
    }
}

export const periodoDesgConsolRoute: Routes = [
    {
        path: 'periodo-desg-consol',
        component: PeriodoDesgConsolComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.periodoDesgConsol.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo-desg-consol/:id/view',
        component: PeriodoDesgConsolDetailComponent,
        resolve: {
            periodoDesgConsol: PeriodoDesgConsolResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgConsol.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo-desg-consol/new',
        component: PeriodoDesgConsolUpdateComponent,
        resolve: {
            periodoDesgConsol: PeriodoDesgConsolResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgConsol.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo-desg-consol/:id/edit',
        component: PeriodoDesgConsolUpdateComponent,
        resolve: {
            periodoDesgConsol: PeriodoDesgConsolResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgConsol.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const periodoDesgConsolPopupRoute: Routes = [
    {
        path: 'periodo-desg-consol/:id/delete',
        component: PeriodoDesgConsolDeletePopupComponent,
        resolve: {
            periodoDesgConsol: PeriodoDesgConsolResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgConsol.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
