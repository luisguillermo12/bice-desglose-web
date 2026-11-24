import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Desglose, IDesglose } from 'app/shared/model/desglose.model';
import { DesgloseService } from './desglose.service';
import { DesgloseComponent } from './desglose.component';
import { DesgloseDetailComponent } from './desglose-detail.component';
import { DesgloseUpdateComponent } from './desglose-update.component';
import { DesgloseDeletePopupComponent } from './desglose-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseResolve implements Resolve<IDesglose> {
    constructor(private service: DesgloseService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const codCliente = route.params[ 'codCliente' ] ? route.params[ 'codCliente' ] : null;
        const codTipoDes = route.params[ 'codTipoDes' ] ? route.params[ 'codTipoDes' ] : null;
        const codSubtipoDes = route.params[ 'codSubtipoDes' ] ? route.params[ 'codSubtipoDes' ] : null;
        const codMoneda = route.params[ 'codMoneda' ] ? route.params[ 'codMoneda' ] : null;
        const codPlantilla = route.params[ 'codPlantilla' ] ? route.params[ 'codPlantilla' ] : null;
        if (codCliente && codTipoDes && codSubtipoDes && codMoneda) {
            return this.service
            .find(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla)
            .pipe(map((desglose: HttpResponse<Desglose>) => desglose.body));
        }
        return of(new Desglose());
    }
}

export const desgloseRoute: Routes = [
    {
        path: 'desglose',
        component: DesgloseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codCliente,asc',
            pageTitle: 'desgloseApp.desglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codPlantilla/view',
        component: DesgloseDetailComponent,
        resolve: {
            desglose: DesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose/new',
        component: DesgloseUpdateComponent,
        resolve: {
            desglose: DesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codPlantilla/edit',
        component: DesgloseUpdateComponent,
        resolve: {
            desglose: DesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desglose.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desglosePopupRoute: Routes = [
    {
        path: 'desglose/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codPlantilla/delete',
        component: DesgloseDeletePopupComponent,
        resolve: {
            desglose: DesgloseResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desglose.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
