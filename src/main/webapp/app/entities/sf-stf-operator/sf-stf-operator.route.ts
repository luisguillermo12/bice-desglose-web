import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SfStfOperator } from 'app/shared/model/sf-stf-operator.model';
import { SfStfOperatorService } from './sf-stf-operator.service';
import { SfStfOperatorComponent } from './sf-stf-operator.component';
import { SfStfOperatorDetailComponent } from './sf-stf-operator-detail.component';
import { SfStfOperatorUpdateComponent } from './sf-stf-operator-update.component';
import { SfStfOperatorDeletePopupComponent } from './sf-stf-operator-delete-dialog.component';
import { ISfStfOperator } from 'app/shared/model/sf-stf-operator.model';

@Injectable({ providedIn: 'root' })
export class SfStfOperatorResolve implements Resolve<ISfStfOperator> {
    constructor(private service: SfStfOperatorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sfStfOperator: HttpResponse<SfStfOperator>) => sfStfOperator.body));
        }
        return of(new SfStfOperator());
    }
}

export const sfStfOperatorRoute: Routes = [
    {
        path: 'sf-stf-operator',
        component: SfStfOperatorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.sfStfOperator.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sf-stf-operator/:id/view',
        component: SfStfOperatorDetailComponent,
        resolve: {
            sfStfOperator: SfStfOperatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.sfStfOperator.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sf-stf-operator/new',
        component: SfStfOperatorUpdateComponent,
        resolve: {
            sfStfOperator: SfStfOperatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.sfStfOperator.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sf-stf-operator/:id/edit',
        component: SfStfOperatorUpdateComponent,
        resolve: {
            sfStfOperator: SfStfOperatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.sfStfOperator.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sfStfOperatorPopupRoute: Routes = [
    {
        path: 'sf-stf-operator/:id/delete',
        component: SfStfOperatorDeletePopupComponent,
        resolve: {
            sfStfOperator: SfStfOperatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.sfStfOperator.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
