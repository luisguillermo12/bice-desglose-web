import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ClienteJuridico, IClienteJuridico } from 'app/shared/model/cliente-juridico.model';
import { ClienteJuridicoService } from './cliente-juridico.service';
import { ClienteJuridicoComponent } from './cliente-juridico.component';
import { ClienteJuridicoDetailComponent } from './cliente-juridico-detail.component';
import { ClienteJuridicoUpdateComponent } from './cliente-juridico-update.component';
import { ClienteJuridicoDeletePopupComponent } from './cliente-juridico-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ClienteJuridicoResolve implements Resolve<IClienteJuridico> {
    constructor(private service: ClienteJuridicoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((clienteJuridico: HttpResponse<ClienteJuridico>) => clienteJuridico.body));
        }
        return of(new ClienteJuridico());
    }
}

export const clienteJuridicoRoute: Routes = [
    {
        path: 'cliente-juridico',
        component: ClienteJuridicoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.clienteJuridico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-juridico/:id/view',
        component: ClienteJuridicoDetailComponent,
        resolve: {
            clienteJuridico: ClienteJuridicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteJuridico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-juridico/new',
        component: ClienteJuridicoUpdateComponent,
        resolve: {
            clienteJuridico: ClienteJuridicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteJuridico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-juridico/:id/edit',
        component: ClienteJuridicoUpdateComponent,
        resolve: {
            clienteJuridico: ClienteJuridicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteJuridico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const clienteJuridicoPopupRoute: Routes = [
    {
        path: 'cliente-juridico/:id/delete',
        component: ClienteJuridicoDeletePopupComponent,
        resolve: {
            clienteJuridico: ClienteJuridicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteJuridico.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
