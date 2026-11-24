import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITablaServicioMail, TablaServicioMail } from 'app/shared/model/tabla-servicio-mail.model';
import { TablaServicioMailService } from './tabla-servicio-mail.service';
import { TablaServicioMailComponent } from './tabla-servicio-mail.component';
import { TablaServicioMailDetailComponent } from './tabla-servicio-mail-detail.component';
import { TablaServicioMailUpdateComponent } from './tabla-servicio-mail-update.component';
import { TablaServicioMailDeletePopupComponent } from './tabla-servicio-mail-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class TablaServicioMailResolve implements Resolve<ITablaServicioMail> {
    constructor(private service: TablaServicioMailService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((tablaServicioMail: HttpResponse<TablaServicioMail>) => tablaServicioMail.body));
        }
        return of(new TablaServicioMail());
    }
}

export const tablaServicioMailRoute: Routes = [
    {
        path: 'tabla-servicio-mail',
        component: TablaServicioMailComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.tablaServicioMail.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-servicio-mail/:id/view',
        component: TablaServicioMailDetailComponent,
        resolve: {
            tablaServicioMail: TablaServicioMailResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServicioMail.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-servicio-mail/new',
        component: TablaServicioMailUpdateComponent,
        resolve: {
            tablaServicioMail: TablaServicioMailResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServicioMail.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'tabla-servicio-mail/:id/edit',
        component: TablaServicioMailUpdateComponent,
        resolve: {
            tablaServicioMail: TablaServicioMailResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServicioMail.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const tablaServicioMailPopupRoute: Routes = [
    {
        path: 'tabla-servicio-mail/:id/delete',
        component: TablaServicioMailDeletePopupComponent,
        resolve: {
            tablaServicioMail: TablaServicioMailResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.tablaServicioMail.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
