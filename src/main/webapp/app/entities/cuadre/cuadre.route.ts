import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Cuadre, ICuadre } from 'app/shared/model/cuadre.model';
import { CuadreService } from './cuadre.service';
import { CuadreComponent } from './cuadre.component';
import { CuadreDetailComponent } from './cuadre-detail.component';
import { CuadreUpdateComponent } from './cuadre-update.component';
import { CuadreDeletePopupComponent } from './cuadre-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class CuadreResolve implements Resolve<ICuadre> {
    constructor(private service: CuadreService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const codTpoConcep1 = route.params[ 'codTpoConcep1' ] ? route.params[ 'codTpoConcep1' ] : null;
        const corConcepto1 = route.params[ 'corConcepto1' ] ? route.params[ 'corConcepto1' ] : null;
        const codTpoConcep2 = route.params[ 'codTpoConcep2' ] ? route.params[ 'codTpoConcep2' ] : null;
        const corConcepto2 = route.params[ 'corConcepto2' ] ? route.params[ 'corConcepto2' ] : null;
        if (codTpoConcep1 && corConcepto1 && codTpoConcep2 && corConcepto2) {
            return this.service
            .find(codTpoConcep1, corConcepto1, codTpoConcep2, corConcepto2)
            .pipe(map((cuadre: HttpResponse<Cuadre>) => cuadre.body));
        }
        return of(new Cuadre());
    }
}

export const cuadreRoute: Routes = [
    {
        path: 'cuadre',
        component: CuadreComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codTpoConcep1,asc',
            pageTitle: 'desgloseApp.cuadre.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cuadre/:codTpoConcep1/:corConcepto1/:codTpoConcep2/:corConcepto2/view',
        component: CuadreDetailComponent,
        resolve: {
            cuadre: CuadreResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.cuadre.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cuadre/new',
        component: CuadreUpdateComponent,
        resolve: {
            cuadre: CuadreResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.cuadre.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cuadre/:codTpoConcep1/:corConcepto1/:codTpoConcep2/:corConcepto2/edit',
        component: CuadreUpdateComponent,
        resolve: {
            cuadre: CuadreResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.cuadre.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const cuadrePopupRoute: Routes = [
    {
        path: 'cuadre/:codTpoConcep1/:corConcepto1/:codTpoConcep2/:corConcepto2/delete',
        component: CuadreDeletePopupComponent,
        resolve: {
            cuadre: CuadreResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.cuadre.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
