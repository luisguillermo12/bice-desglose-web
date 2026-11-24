import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IUniqueSessionControl, UniqueSessionControl } from 'app/shared/model/unique-session-control.model';
import { UniqueSessionControlService } from './unique-session-control.service';
import { UniqueSessionControlComponent } from './unique-session-control.component';
import { UniqueSessionControlDetailComponent } from './unique-session-control-detail.component';
import { UniqueSessionControlUpdateComponent } from './unique-session-control-update.component';
import { UniqueSessionControlDeletePopupComponent } from './unique-session-control-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class UniqueSessionControlResolve implements Resolve<IUniqueSessionControl> {
    constructor(private service: UniqueSessionControlService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((uniqueSessionControl: HttpResponse<UniqueSessionControl>) => uniqueSessionControl.body));
        }
        return of(new UniqueSessionControl());
    }
}

export const uniqueSessionControlRoute: Routes = [
    {
        path: 'unique-session-control',
        component: UniqueSessionControlComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.uniqueSessionControl.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'unique-session-control/:id/view',
        component: UniqueSessionControlDetailComponent,
        resolve: {
            uniqueSessionControl: UniqueSessionControlResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            pageTitle: 'desgloseApp.uniqueSessionControl.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'unique-session-control/new',
        component: UniqueSessionControlUpdateComponent,
        resolve: {
            uniqueSessionControl: UniqueSessionControlResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            pageTitle: 'desgloseApp.uniqueSessionControl.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'unique-session-control/:id/edit',
        component: UniqueSessionControlUpdateComponent,
        resolve: {
            uniqueSessionControl: UniqueSessionControlResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            pageTitle: 'desgloseApp.uniqueSessionControl.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const uniqueSessionControlPopupRoute: Routes = [
    {
        path: 'unique-session-control/:id/delete',
        component: UniqueSessionControlDeletePopupComponent,
        resolve: {
            uniqueSessionControl: UniqueSessionControlResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            pageTitle: 'desgloseApp.uniqueSessionControl.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
