import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseImpresion, IDesgloseImpresion } from 'app/shared/model/desglose-impresion.model';
import { DesgloseImpresionService } from './desglose-impresion.service';
import { DesgloseImpresionComponent } from './desglose-impresion.component';
import { DesgloseImpresionDetailComponent } from './desglose-impresion-detail.component';
import { DesgloseImpresionUpdateComponent } from './desglose-impresion-update.component';
import { DesgloseImpresionDeletePopupComponent } from './desglose-impresion-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseImpresionResolve implements Resolve<IDesgloseImpresion> {
    constructor(private service: DesgloseImpresionService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseImpresion: HttpResponse<DesgloseImpresion>) => desgloseImpresion.body));
        }
        return of(new DesgloseImpresion());
    }
}

export const desgloseImpresionRoute: Routes = [
    {
        path: 'desglose-impresion',
        component: DesgloseImpresionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-impresion/:id/view',
        component: DesgloseImpresionDetailComponent,
        resolve: {
            desgloseImpresion: DesgloseImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-impresion/new',
        component: DesgloseImpresionUpdateComponent,
        resolve: {
            desgloseImpresion: DesgloseImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-impresion/:id/edit',
        component: DesgloseImpresionUpdateComponent,
        resolve: {
            desgloseImpresion: DesgloseImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseImpresionPopupRoute: Routes = [
    {
        path: 'desglose-impresion/:id/delete',
        component: DesgloseImpresionDeletePopupComponent,
        resolve: {
            desgloseImpresion: DesgloseImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
