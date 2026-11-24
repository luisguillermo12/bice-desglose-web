import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPeriodoDesgImpresion, PeriodoDesgImpresion } from 'app/shared/model/periodo-desg-impresion.model';
import { PeriodoDesgImpresionService } from './periodo-desg-impresion.service';
import { PeriodoDesgImpresionComponent } from './periodo-desg-impresion.component';
import { PeriodoDesgImpresionDetailComponent } from './periodo-desg-impresion-detail.component';
import { PeriodoDesgImpresionUpdateComponent } from './periodo-desg-impresion-update.component';
import { PeriodoDesgImpresionDeletePopupComponent } from './periodo-desg-impresion-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class PeriodoDesgImpresionResolve implements Resolve<IPeriodoDesgImpresion> {
    constructor(private service: PeriodoDesgImpresionService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((periodoDesgImpresion: HttpResponse<PeriodoDesgImpresion>) => periodoDesgImpresion.body));
        }
        return of(new PeriodoDesgImpresion());
    }
}

export const periodoDesgImpresionRoute: Routes = [
    {
        path: 'periodo-desg-impresion',
        component: PeriodoDesgImpresionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.periodoDesgImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo-desg-impresion/:id/view',
        component: PeriodoDesgImpresionDetailComponent,
        resolve: {
            periodoDesgImpresion: PeriodoDesgImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo-desg-impresion/new',
        component: PeriodoDesgImpresionUpdateComponent,
        resolve: {
            periodoDesgImpresion: PeriodoDesgImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'periodo-desg-impresion/:id/edit',
        component: PeriodoDesgImpresionUpdateComponent,
        resolve: {
            periodoDesgImpresion: PeriodoDesgImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const periodoDesgImpresionPopupRoute: Routes = [
    {
        path: 'periodo-desg-impresion/:id/delete',
        component: PeriodoDesgImpresionDeletePopupComponent,
        resolve: {
            periodoDesgImpresion: PeriodoDesgImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.periodoDesgImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
