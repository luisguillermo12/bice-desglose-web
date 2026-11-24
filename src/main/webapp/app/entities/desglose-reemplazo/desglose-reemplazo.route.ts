import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseReemplazo, IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';
import { DesgloseReemplazoService } from './desglose-reemplazo.service';
import { DesgloseReemplazoComponent } from './desglose-reemplazo.component';
import { DesgloseReemplazoDetailComponent } from './desglose-reemplazo-detail.component';
import { DesgloseReemplazoUpdateComponent } from './desglose-reemplazo-update.component';
import { DesgloseReemplazoDeletePopupComponent } from './desglose-reemplazo-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseReemplazoResolve implements Resolve<IDesgloseReemplazo> {
    constructor(private service: DesgloseReemplazoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseReemplazo: HttpResponse<DesgloseReemplazo>) => desgloseReemplazo.body));
        }
        return of(new DesgloseReemplazo());
    }
}

export const desgloseReemplazoRoute: Routes = [
    {
        path: 'desglose-reemplazo',
        component: DesgloseReemplazoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseReemplazo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-reemplazo/:id/view',
        component: DesgloseReemplazoDetailComponent,
        resolve: {
            desgloseReemplazo: DesgloseReemplazoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseReemplazo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-reemplazo/new',
        component: DesgloseReemplazoUpdateComponent,
        resolve: {
            desgloseReemplazo: DesgloseReemplazoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseReemplazo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-reemplazo/:id/edit',
        component: DesgloseReemplazoUpdateComponent,
        resolve: {
            desgloseReemplazo: DesgloseReemplazoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseReemplazo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseReemplazoPopupRoute: Routes = [
    {
        path: 'desglose-reemplazo/:id/delete',
        component: DesgloseReemplazoDeletePopupComponent,
        resolve: {
            desgloseReemplazo: DesgloseReemplazoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseReemplazo.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
