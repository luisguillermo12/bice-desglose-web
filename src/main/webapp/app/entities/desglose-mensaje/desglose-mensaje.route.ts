import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseMensaje, IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';
import { DesgloseMensajeService } from './desglose-mensaje.service';
import { DesgloseMensajeComponent } from './desglose-mensaje.component';
import { DesgloseMensajeDetailComponent } from './desglose-mensaje-detail.component';
import { DesgloseMensajeUpdateComponent } from './desglose-mensaje-update.component';
import { DesgloseMensajeDeletePopupComponent } from './desglose-mensaje-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseMensajeResolve implements Resolve<IDesgloseMensaje> {
    constructor(private service: DesgloseMensajeService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            // return this.service.find(id).pipe(map((desgloseMensaje: HttpResponse<DesgloseMensaje>) => desgloseMensaje.body));
        }
        return of(new DesgloseMensaje());
    }
}

export const desgloseMensajeRoute: Routes = [
    {
        path: 'desglose-mensaje',
        component: DesgloseMensajeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseMensaje.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-mensaje/:id/view',
        component: DesgloseMensajeDetailComponent,
        resolve: {
            desgloseMensaje: DesgloseMensajeResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseMensaje.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-mensaje/new',
        component: DesgloseMensajeUpdateComponent,
        resolve: {
            desgloseMensaje: DesgloseMensajeResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseMensaje.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-mensaje/:id/edit',
        component: DesgloseMensajeUpdateComponent,
        resolve: {
            desgloseMensaje: DesgloseMensajeResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseMensaje.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseMensajePopupRoute: Routes = [
    {
        path: 'desglose-mensaje/:id/delete',
        component: DesgloseMensajeDeletePopupComponent,
        resolve: {
            desgloseMensaje: DesgloseMensajeResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseMensaje.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
