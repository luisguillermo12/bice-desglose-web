import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ClienteNatural, IClienteNatural } from 'app/shared/model/cliente-natural.model';
import { ClienteNaturalService } from './cliente-natural.service';
import { ClienteNaturalComponent } from './cliente-natural.component';
import { ClienteNaturalDetailComponent } from './cliente-natural-detail.component';
import { ClienteNaturalUpdateComponent } from './cliente-natural-update.component';
import { ClienteNaturalDeletePopupComponent } from './cliente-natural-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ClienteNaturalResolve implements Resolve<IClienteNatural> {
    constructor(private service: ClienteNaturalService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((clienteNatural: HttpResponse<ClienteNatural>) => clienteNatural.body));
        }
        return of(new ClienteNatural());
    }
}

export const clienteNaturalRoute: Routes = [
    {
        path: 'cliente-natural',
        component: ClienteNaturalComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.clienteNatural.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-natural/:id/view',
        component: ClienteNaturalDetailComponent,
        resolve: {
            clienteNatural: ClienteNaturalResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteNatural.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-natural/new',
        component: ClienteNaturalUpdateComponent,
        resolve: {
            clienteNatural: ClienteNaturalResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteNatural.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-natural/:id/edit',
        component: ClienteNaturalUpdateComponent,
        resolve: {
            clienteNatural: ClienteNaturalResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteNatural.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const clienteNaturalPopupRoute: Routes = [
    {
        path: 'cliente-natural/:id/delete',
        component: ClienteNaturalDeletePopupComponent,
        resolve: {
            clienteNatural: ClienteNaturalResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteNatural.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
