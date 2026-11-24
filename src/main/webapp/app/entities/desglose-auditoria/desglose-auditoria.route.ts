import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseAuditoria, IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';
import { DesgloseAuditoriaService } from './desglose-auditoria.service';
import { DesgloseAuditoriaComponent } from './desglose-auditoria.component';
import { DesgloseAuditoriaDetailComponent } from './desglose-auditoria-detail.component';
import { DesgloseAuditoriaUpdateComponent } from './desglose-auditoria-update.component';
import { DesgloseAuditoriaDeletePopupComponent } from './desglose-auditoria-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseAuditoriaResolve implements Resolve<IDesgloseAuditoria> {
    constructor(private service: DesgloseAuditoriaService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseAuditoria: HttpResponse<DesgloseAuditoria>) => desgloseAuditoria.body));
        }
        return of(new DesgloseAuditoria());
    }
}

export const desgloseAuditoriaRoute: Routes = [
    {
        path: 'desglose-auditoria',
        component: DesgloseAuditoriaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseAuditoria.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-auditoria/:id/view',
        component: DesgloseAuditoriaDetailComponent,
        resolve: {
            desgloseAuditoria: DesgloseAuditoriaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseAuditoria.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-auditoria/new',
        component: DesgloseAuditoriaUpdateComponent,
        resolve: {
            desgloseAuditoria: DesgloseAuditoriaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseAuditoria.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-auditoria/:id/edit',
        component: DesgloseAuditoriaUpdateComponent,
        resolve: {
            desgloseAuditoria: DesgloseAuditoriaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseAuditoria.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseAuditoriaPopupRoute: Routes = [
    {
        path: 'desglose-auditoria/:id/delete',
        component: DesgloseAuditoriaDeletePopupComponent,
        resolve: {
            desgloseAuditoria: DesgloseAuditoriaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseAuditoria.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
