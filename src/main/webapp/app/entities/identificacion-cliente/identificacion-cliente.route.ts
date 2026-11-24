import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IdentificacionCliente, IIdentificacionCliente } from 'app/shared/model/identificacion-cliente.model';
import { IdentificacionClienteService } from './identificacion-cliente.service';
import { IdentificacionClienteComponent } from './identificacion-cliente.component';
import { IdentificacionClienteDetailComponent } from './identificacion-cliente-detail.component';
import { IdentificacionClienteUpdateComponent } from './identificacion-cliente-update.component';
import { IdentificacionClienteDeletePopupComponent } from './identificacion-cliente-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class IdentificacionClienteResolve implements Resolve<IIdentificacionCliente> {
    constructor(private service: IdentificacionClienteService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service
            .find(id)
            .pipe(map((identificacionCliente: HttpResponse<IdentificacionCliente>) => identificacionCliente.body));
        }
        return of(new IdentificacionCliente());
    }
}

export const identificacionClienteRoute: Routes = [
    {
        path: 'identificacion-cliente',
        component: IdentificacionClienteComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.identificacionCliente.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'identificacion-cliente/:id/view',
        component: IdentificacionClienteDetailComponent,
        resolve: {
            identificacionCliente: IdentificacionClienteResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.identificacionCliente.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'identificacion-cliente/new',
        component: IdentificacionClienteUpdateComponent,
        resolve: {
            identificacionCliente: IdentificacionClienteResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.identificacionCliente.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'identificacion-cliente/:id/edit',
        component: IdentificacionClienteUpdateComponent,
        resolve: {
            identificacionCliente: IdentificacionClienteResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.identificacionCliente.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const identificacionClientePopupRoute: Routes = [
    {
        path: 'identificacion-cliente/:id/delete',
        component: IdentificacionClienteDeletePopupComponent,
        resolve: {
            identificacionCliente: IdentificacionClienteResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.identificacionCliente.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
