import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseRevision, IDesgloseRevision } from 'app/shared/model/desglose-revision.model';
import { DesgloseRevisionService } from './desglose-revision.service';
import { DesgloseRevisionComponent } from './desglose-revision.component';
import { DesgloseRevisionDetailComponent } from './desglose-revision-detail.component';
import { DesgloseRevisionUpdateComponent } from './desglose-revision-update.component';
import { DesgloseRevisionDeletePopupComponent } from './desglose-revision-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseRevisionResolve implements Resolve<IDesgloseRevision> {
    constructor(private service: DesgloseRevisionService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            // return this.service.find(id).pipe(map((desgloseRevision: HttpResponse<DesgloseRevision>) => desgloseRevision.body));
        }
        return of(new DesgloseRevision());
    }
}

export const desgloseRevisionRoute: Routes = [
    {
        path: 'desglose-revision',
        component: DesgloseRevisionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseRevision.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-revision/:id/view',
        component: DesgloseRevisionDetailComponent,
        resolve: {
            desgloseRevision: DesgloseRevisionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevision.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-revision/new',
        component: DesgloseRevisionUpdateComponent,
        resolve: {
            desgloseRevision: DesgloseRevisionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevision.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-revision/:id/edit',
        component: DesgloseRevisionUpdateComponent,
        resolve: {
            desgloseRevision: DesgloseRevisionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevision.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseRevisionPopupRoute: Routes = [
    {
        path: 'desglose-revision/:id/delete',
        component: DesgloseRevisionDeletePopupComponent,
        resolve: {
            desgloseRevision: DesgloseRevisionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseRevision.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
