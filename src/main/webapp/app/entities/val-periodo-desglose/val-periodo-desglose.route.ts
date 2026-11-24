import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IValPeriodoDesglose, ValPeriodoDesglose } from 'app/shared/model/val-periodo-desglose.model';
import { ValPeriodoDesgloseService } from './val-periodo-desglose.service';
import { ValPeriodoDesgloseComponent } from './val-periodo-desglose.component';
import { ValPeriodoDesgloseDetailComponent } from './val-periodo-desglose-detail.component';
import { ValPeriodoDesgloseUpdateComponent } from './val-periodo-desglose-update.component';
import { ValPeriodoDesgloseDeletePopupComponent } from './val-periodo-desglose-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ValPeriodoDesgloseResolve implements Resolve<IValPeriodoDesglose> {
    constructor(private service: ValPeriodoDesgloseService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((valPeriodoDesglose: HttpResponse<ValPeriodoDesglose>) => valPeriodoDesglose.body));
        }
        return of(new ValPeriodoDesglose());
    }
}

export const valPeriodoDesgloseRoute: Routes = [
    {
        path: 'val-periodo-desglose',
        component: ValPeriodoDesgloseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.valPeriodoDesglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'val-periodo-desglose/:id/view',
        component: ValPeriodoDesgloseDetailComponent,
        resolve: {
            valPeriodoDesglose: ValPeriodoDesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'val-periodo-desglose/new',
        component: ValPeriodoDesgloseUpdateComponent,
        resolve: {
            valPeriodoDesglose: ValPeriodoDesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'val-periodo-desglose/:id/edit',
        component: ValPeriodoDesgloseUpdateComponent,
        resolve: {
            valPeriodoDesglose: ValPeriodoDesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const valPeriodoDesglosePopupRoute: Routes = [
    {
        path: 'val-periodo-desglose/:id/delete',
        component: ValPeriodoDesgloseDeletePopupComponent,
        resolve: {
            valPeriodoDesglose: ValPeriodoDesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.valPeriodoDesglose.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
