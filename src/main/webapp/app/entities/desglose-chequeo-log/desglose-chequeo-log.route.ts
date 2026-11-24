import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseChequeoLog, IDesgloseChequeoLog } from 'app/shared/model/desglose-chequeo-log.model';
import { DesgloseChequeoLogService } from './desglose-chequeo-log.service';
import { DesgloseChequeoLogComponent } from './desglose-chequeo-log.component';
import { DesgloseChequeoLogDetailComponent } from './desglose-chequeo-log-detail.component';
import { DesgloseChequeoLogUpdateComponent } from './desglose-chequeo-log-update.component';
import { DesgloseChequeoLogDeletePopupComponent } from './desglose-chequeo-log-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseChequeoLogResolve implements Resolve<IDesgloseChequeoLog> {
    constructor(private service: DesgloseChequeoLogService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseChequeoLog: HttpResponse<DesgloseChequeoLog>) => desgloseChequeoLog.body));
        }
        return of(new DesgloseChequeoLog());
    }
}

export const desgloseChequeoLogRoute: Routes = [
    {
        path: 'desglose-chequeo-log',
        component: DesgloseChequeoLogComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseChequeoLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-chequeo-log/:id/view',
        component: DesgloseChequeoLogDetailComponent,
        resolve: {
            desgloseChequeoLog: DesgloseChequeoLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-chequeo-log/new',
        component: DesgloseChequeoLogUpdateComponent,
        resolve: {
            desgloseChequeoLog: DesgloseChequeoLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-chequeo-log/:id/edit',
        component: DesgloseChequeoLogUpdateComponent,
        resolve: {
            desgloseChequeoLog: DesgloseChequeoLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseChequeoLogPopupRoute: Routes = [
    {
        path: 'desglose-chequeo-log/:id/delete',
        component: DesgloseChequeoLogDeletePopupComponent,
        resolve: {
            desgloseChequeoLog: DesgloseChequeoLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoLog.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
