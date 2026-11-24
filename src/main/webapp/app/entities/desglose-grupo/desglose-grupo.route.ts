import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DesgloseGrupo, IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';
import { DesgloseGrupoService } from './desglose-grupo.service';
import { DesgloseGrupoComponent } from './desglose-grupo.component';
import { DesgloseGrupoDetailComponent } from './desglose-grupo-detail.component';
import { DesgloseGrupoUpdateComponent } from './desglose-grupo-update.component';
import { DesgloseGrupoDeletePopupComponent } from './desglose-grupo-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DesgloseGrupoResolve implements Resolve<IDesgloseGrupo> {
    constructor(private service: DesgloseGrupoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((desgloseGrupo: HttpResponse<DesgloseGrupo>) => desgloseGrupo.body));
        }
        return of(new DesgloseGrupo());
    }
}

export const desgloseGrupoRoute: Routes = [
    {
        path: 'desglose-grupo',
        component: DesgloseGrupoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codCargo,asc',
            pageTitle: 'desgloseApp.desgloseGrupo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-grupo/:id/view',
        component: DesgloseGrupoDetailComponent,
        resolve: {
            desgloseGrupo: DesgloseGrupoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseGrupo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-grupo/new',
        component: DesgloseGrupoUpdateComponent,
        resolve: {
            desgloseGrupo: DesgloseGrupoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseGrupo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'desglose-grupo/:id/edit',
        component: DesgloseGrupoUpdateComponent,
        resolve: {
            desgloseGrupo: DesgloseGrupoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseGrupo.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const desgloseGrupoPopupRoute: Routes = [
    {
        path: 'desglose-grupo/:id/delete',
        component: DesgloseGrupoDeletePopupComponent,
        resolve: {
            desgloseGrupo: DesgloseGrupoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.desgloseGrupo.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
