import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DescripcionCodigo, IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { DescripcionCodigoService } from './descripcion-codigo.service';
import { DescripcionCodigoComponent } from './descripcion-codigo.component';
import { DescripcionCodigoDetailComponent } from './descripcion-codigo-detail.component';
import { DescripcionCodigoUpdateComponent } from './descripcion-codigo-update.component';
import { DescripcionCodigoDeletePopupComponent } from './descripcion-codigo-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DescripcionCodigoResolve implements Resolve<IDescripcionCodigo> {
    constructor(private service: DescripcionCodigoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const codTabla = route.params[ 'codTabla' ] ? route.params[ 'codTabla' ] : null;
        const codGenerico = route.params[ 'codGenerico' ] ? route.params[ 'codGenerico' ] : null;
        const codRegStatus = route.params[ 'codRegStatus' ] ? route.params[ 'codRegStatus' ] : null;
        const glsGenericacorta = route.params[ 'glsGenericacorta' ] ? route.params[ 'glsGenericacorta' ] : null;
        const glsGenericalarga = route.params[ 'glsGenericalarga' ] ? route.params[ 'glsGenericalarga' ] : null;
        if (codTabla && codGenerico && codRegStatus) {
            return this.service
            .find(codTabla, codGenerico, codRegStatus,glsGenericacorta,glsGenericalarga)
            .pipe(map((descripcionCodigo: HttpResponse<DescripcionCodigo>) => descripcionCodigo.body));
        }
        return of(new DescripcionCodigo());
    }
}

export const descripcionCodigoRoute: Routes = [
    {
        path: 'descripcion-codigo',
        component: DescripcionCodigoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codTabla,asc',
            pageTitle: 'desgloseApp.descripcionCodigo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'descripcion-codigo/:codTabla/:codGenerico/:codRegStatus/:glsGenericacorta/:glsGenericalarga/view',
        component: DescripcionCodigoDetailComponent,
        resolve: {
            descripcionCodigo: DescripcionCodigoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.descripcionCodigo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'descripcion-codigo/new',
        component: DescripcionCodigoUpdateComponent,
        resolve: {
            descripcionCodigo: DescripcionCodigoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.descripcionCodigo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'descripcion-codigo/:codTabla/:codGenerico/:codRegStatus/:glsGenericacorta/:glsGenericalarga/edit',
        component: DescripcionCodigoUpdateComponent,
        resolve: {
            descripcionCodigo: DescripcionCodigoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.descripcionCodigo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const descripcionCodigoPopupRoute: Routes = [
    {
        path: 'descripcion-codigo/:codTabla/:codGenerico/:codRegStatus/:glsGenericacorta/:glsGenericalarga/delete',
        component: DescripcionCodigoDeletePopupComponent,
        resolve: {
            descripcionCodigo: DescripcionCodigoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.descripcionCodigo.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
