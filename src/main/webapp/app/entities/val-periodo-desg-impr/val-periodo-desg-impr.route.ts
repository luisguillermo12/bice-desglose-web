import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IValPeriodoDesgImpr, ValPeriodoDesgImpr } from 'app/shared/model/val-periodo-desg-impr.model';
import { ValPeriodoDesgImprService } from './val-periodo-desg-impr.service';
import { ValPeriodoDesgImprComponent } from './val-periodo-desg-impr.component';
import { ValPeriodoDesgImprDetailComponent } from './val-periodo-desg-impr-detail.component';
import { ValPeriodoDesgImprUpdateComponent } from './val-periodo-desg-impr-update.component';
import { ValPeriodoDesgImprDeletePopupComponent } from './val-periodo-desg-impr-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ValPeriodoDesgImprResolve implements Resolve<IValPeriodoDesgImpr> {
    constructor(private service: ValPeriodoDesgImprService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((valPeriodoDesgImpr: HttpResponse<ValPeriodoDesgImpr>) => valPeriodoDesgImpr.body));
        }
        return of(new ValPeriodoDesgImpr());
    }
}

export const valPeriodoDesgImprRoute: Routes = [
    {
        path: 'val-periodo-desg-impr',
        component: ValPeriodoDesgImprComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.valPeriodoDesgImpr.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'val-periodo-desg-impr/:id/view',
        component: ValPeriodoDesgImprDetailComponent,
        resolve: {
            valPeriodoDesgImpr: ValPeriodoDesgImprResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesgImpr.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'val-periodo-desg-impr/new',
        component: ValPeriodoDesgImprUpdateComponent,
        resolve: {
            valPeriodoDesgImpr: ValPeriodoDesgImprResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesgImpr.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'val-periodo-desg-impr/:id/edit',
        component: ValPeriodoDesgImprUpdateComponent,
        resolve: {
            valPeriodoDesgImpr: ValPeriodoDesgImprResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesgImpr.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const valPeriodoDesgImprPopupRoute: Routes = [
    {
        path: 'val-periodo-desg-impr/:id/delete',
        component: ValPeriodoDesgImprDeletePopupComponent,
        resolve: {
            valPeriodoDesgImpr: ValPeriodoDesgImprResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesgImpr.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
