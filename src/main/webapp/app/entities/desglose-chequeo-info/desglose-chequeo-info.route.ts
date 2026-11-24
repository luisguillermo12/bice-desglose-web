import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseChequeoInfo, IDesgloseChequeoInfo } from 'app/shared/model/desglose-chequeo-info.model';
import { DesgloseChequeoInfoService } from './desglose-chequeo-info.service';
import { DesgloseChequeoInfoComponent } from './desglose-chequeo-info.component';
import { DesgloseChequeoInfoDetailComponent } from './desglose-chequeo-info-detail.component';
import { DesgloseChequeoInfoUpdateComponent } from './desglose-chequeo-info-update.component';
import { DesgloseChequeoInfoDeletePopupComponent } from './desglose-chequeo-info-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseChequeoInfoResolve implements Resolve<IDesgloseChequeoInfo> {
    constructor(private service: DesgloseChequeoInfoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseChequeoInfo: HttpResponse<DesgloseChequeoInfo>) => desgloseChequeoInfo.body));
        }
        return of(new DesgloseChequeoInfo());
    }
}

export const desgloseChequeoInfoRoute: Routes = [
    {
        path: 'desglose-chequeo-info',
        component: DesgloseChequeoInfoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseChequeoInfo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-chequeo-info/:id/view',
        component: DesgloseChequeoInfoDetailComponent,
        resolve: {
            desgloseChequeoInfo: DesgloseChequeoInfoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoInfo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-chequeo-info/new',
        component: DesgloseChequeoInfoUpdateComponent,
        resolve: {
            desgloseChequeoInfo: DesgloseChequeoInfoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoInfo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-chequeo-info/:id/edit',
        component: DesgloseChequeoInfoUpdateComponent,
        resolve: {
            desgloseChequeoInfo: DesgloseChequeoInfoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoInfo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseChequeoInfoPopupRoute: Routes = [
    {
        path: 'desglose-chequeo-info/:id/delete',
        component: DesgloseChequeoInfoDeletePopupComponent,
        resolve: {
            desgloseChequeoInfo: DesgloseChequeoInfoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseChequeoInfo.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
