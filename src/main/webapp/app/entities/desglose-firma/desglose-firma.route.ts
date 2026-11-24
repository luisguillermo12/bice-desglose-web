import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseFirma, IDesgloseFirma } from 'app/shared/model/desglose-firma.model';
import { DesgloseFirmaService } from './desglose-firma.service';
import { DesgloseFirmaComponent } from './desglose-firma.component';
import { DesgloseFirmaDetailComponent } from './desglose-firma-detail.component';
import { DesgloseFirmaUpdateComponent } from './desglose-firma-update.component';
import { DesgloseFirmaDeletePopupComponent } from './desglose-firma-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseFirmaResolve implements Resolve<IDesgloseFirma> {
    constructor(private service: DesgloseFirmaService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseFirma: HttpResponse<DesgloseFirma>) => desgloseFirma.body));
        }
        return of(new DesgloseFirma());
    }
}

export const desgloseFirmaRoute: Routes = [
    {
        path: 'desglose-firma',
        component: DesgloseFirmaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.desgloseFirma.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-firma/:id/view',
        component: DesgloseFirmaDetailComponent,
        resolve: {
            desgloseFirma: DesgloseFirmaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseFirma.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-firma/new',
        component: DesgloseFirmaUpdateComponent,
        resolve: {
            desgloseFirma: DesgloseFirmaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseFirma.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-firma/:id/edit',
        component: DesgloseFirmaUpdateComponent,
        resolve: {
            desgloseFirma: DesgloseFirmaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseFirma.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseFirmaPopupRoute: Routes = [
    {
        path: 'desglose-firma/:id/delete',
        component: DesgloseFirmaDeletePopupComponent,
        resolve: {
            desgloseFirma: DesgloseFirmaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseFirma.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
