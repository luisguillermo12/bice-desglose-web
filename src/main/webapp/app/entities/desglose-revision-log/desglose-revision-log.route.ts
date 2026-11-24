import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseRevisionLog, IDesgloseRevisionLog } from 'app/shared/model/desglose-revision-log.model';
import { DesgloseRevisionLogService } from './desglose-revision-log.service';
import { DesgloseRevisionLogComponent } from './desglose-revision-log.component';
import { DesgloseRevisionLogDetailComponent } from './desglose-revision-log-detail.component';
import { DesgloseRevisionLogUpdateComponent } from './desglose-revision-log-update.component';
import { DesgloseRevisionLogDeletePopupComponent } from './desglose-revision-log-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseRevisionLogResolve implements Resolve<IDesgloseRevisionLog> {
    constructor(private service: DesgloseRevisionLogService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseRevisionLog: HttpResponse<DesgloseRevisionLog>) => desgloseRevisionLog.body));
        }
        return of(new DesgloseRevisionLog());
    }
}

export const desgloseRevisionLogRoute: Routes = [
    {
        path: 'desglose-revision-log',
        component: DesgloseRevisionLogComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseRevisionLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-revision-log/:id/view',
        component: DesgloseRevisionLogDetailComponent,
        resolve: {
            desgloseRevisionLog: DesgloseRevisionLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevisionLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-revision-log/new',
        component: DesgloseRevisionLogUpdateComponent,
        resolve: {
            desgloseRevisionLog: DesgloseRevisionLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevisionLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-revision-log/:id/edit',
        component: DesgloseRevisionLogUpdateComponent,
        resolve: {
            desgloseRevisionLog: DesgloseRevisionLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevisionLog.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseRevisionLogPopupRoute: Routes = [
    {
        path: 'desglose-revision-log/:id/delete',
        component: DesgloseRevisionLogDeletePopupComponent,
        resolve: {
            desgloseRevisionLog: DesgloseRevisionLogResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevisionLog.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
