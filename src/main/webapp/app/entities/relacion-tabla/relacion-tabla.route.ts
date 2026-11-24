import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IRelacionTabla, RelacionTabla } from 'app/shared/model/relacion-tabla.model';
import { RelacionTablaService } from './relacion-tabla.service';
import { RelacionTablaComponent } from './relacion-tabla.component';
import { RelacionTablaDetailComponent } from './relacion-tabla-detail.component';
import { RelacionTablaUpdateComponent } from './relacion-tabla-update.component';
import { RelacionTablaDeletePopupComponent } from './relacion-tabla-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class RelacionTablaResolve implements Resolve<IRelacionTabla> {
    constructor(private service: RelacionTablaService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((relacionTabla: HttpResponse<RelacionTabla>) => relacionTabla.body));
        }
        return of(new RelacionTabla());
    }
}

export const relacionTablaRoute: Routes = [
    {
        path: 'relacion-tabla',
        component: RelacionTablaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.relacionTabla.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'relacion-tabla/:id/view',
        component: RelacionTablaDetailComponent,
        resolve: {
            relacionTabla: RelacionTablaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.relacionTabla.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'relacion-tabla/new',
        component: RelacionTablaUpdateComponent,
        resolve: {
            relacionTabla: RelacionTablaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.relacionTabla.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'relacion-tabla/:id/edit',
        component: RelacionTablaUpdateComponent,
        resolve: {
            relacionTabla: RelacionTablaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.relacionTabla.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const relacionTablaPopupRoute: Routes = [
    {
        path: 'relacion-tabla/:id/delete',
        component: RelacionTablaDeletePopupComponent,
        resolve: {
            relacionTabla: RelacionTablaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.relacionTabla.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
