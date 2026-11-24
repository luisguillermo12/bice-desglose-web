import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ClienteSinRut, IClienteSinRut } from 'app/shared/model/cliente-sin-rut.model';
import { ClienteSinRutService } from './cliente-sin-rut.service';
import { ClienteSinRutComponent } from './cliente-sin-rut.component';
import { ClienteSinRutDetailComponent } from './cliente-sin-rut-detail.component';
import { ClienteSinRutUpdateComponent } from './cliente-sin-rut-update.component';
import { ClienteSinRutDeletePopupComponent } from './cliente-sin-rut-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ClienteSinRutResolve implements Resolve<IClienteSinRut> {
    constructor(private service: ClienteSinRutService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const codCliente = route.params[ 'codCliente' ] ? route.params[ 'codCliente' ] : null;
        if (codCliente) {
            return this.service.find(codCliente).pipe(map((clienteSinRut: HttpResponse<ClienteSinRut>) => clienteSinRut.body));
        }
        return of(new ClienteSinRut());
    }
}

export const clienteSinRutRoute: Routes = [
    {
        path: 'cliente-sin-rut',
        component: ClienteSinRutComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codCliente,asc',
            pageTitle: 'desgloseApp.clienteSinRut.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-sin-rut/:codCliente/view',
        component: ClienteSinRutDetailComponent,
        resolve: {
            clienteSinRut: ClienteSinRutResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteSinRut.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-sin-rut/new',
        component: ClienteSinRutUpdateComponent,
        resolve: {
            clienteSinRut: ClienteSinRutResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteSinRut.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'cliente-sin-rut/:codCliente/edit',
        component: ClienteSinRutUpdateComponent,
        resolve: {
            clienteSinRut: ClienteSinRutResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteSinRut.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const clienteSinRutPopupRoute: Routes = [
    {
        path: 'cliente-sin-rut/:codCliente/delete',
        component: ClienteSinRutDeletePopupComponent,
        resolve: {
            clienteSinRut: ClienteSinRutResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.clienteSinRut.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
