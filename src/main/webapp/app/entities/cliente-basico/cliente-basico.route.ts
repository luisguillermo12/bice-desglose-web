import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ClienteBasico, IClienteBasico } from 'app/shared/model/cliente-basico.model';
import { ClienteBasicoService } from './cliente-basico.service';
import { ClienteBasicoComponent } from './cliente-basico.component';
import { ClienteBasicoDetailComponent } from './cliente-basico-detail.component';
import { ClienteBasicoUpdateComponent } from './cliente-basico-update.component';
import { ClienteBasicoDeletePopupComponent } from './cliente-basico-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ClienteBasicoResolve implements Resolve<IClienteBasico> {
    constructor(private service: ClienteBasicoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((clienteBasico: HttpResponse<ClienteBasico>) => clienteBasico.body));
        }
        return of(new ClienteBasico());
    }
}

export const clienteBasicoRoute: Routes = [
    {
        path: 'cliente-basico',
        component: ClienteBasicoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.clienteBasico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-basico/:id/view',
        component: ClienteBasicoDetailComponent,
        resolve: {
            clienteBasico: ClienteBasicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteBasico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-basico/new',
        component: ClienteBasicoUpdateComponent,
        resolve: {
            clienteBasico: ClienteBasicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteBasico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-basico/:id/edit',
        component: ClienteBasicoUpdateComponent,
        resolve: {
            clienteBasico: ClienteBasicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteBasico.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const clienteBasicoPopupRoute: Routes = [
    {
        path: 'cliente-basico/:id/delete',
        component: ClienteBasicoDeletePopupComponent,
        resolve: {
            clienteBasico: ClienteBasicoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteBasico.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
