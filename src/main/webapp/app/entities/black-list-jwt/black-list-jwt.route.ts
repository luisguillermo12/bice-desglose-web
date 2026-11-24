import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BlackListJwt } from 'app/shared/model/black-list-jwt.model';
import { BlackListJwtService } from './black-list-jwt.service';
import { BlackListJwtComponent } from './black-list-jwt.component';
import { BlackListJwtDetailComponent } from './black-list-jwt-detail.component';
import { BlackListJwtUpdateComponent } from './black-list-jwt-update.component';
import { BlackListJwtDeletePopupComponent } from './black-list-jwt-delete-dialog.component';
import { IBlackListJwt } from 'app/shared/model/black-list-jwt.model';

@Injectable({ providedIn: 'root' })
export class BlackListJwtResolve implements Resolve<IBlackListJwt> {
    constructor(private service: BlackListJwtService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((blackListJwt: HttpResponse<BlackListJwt>) => blackListJwt.body));
        }
        return of(new BlackListJwt());
    }
}

export const blackListJwtRoute: Routes = [
    {
        path: 'black-list-jwt',
        component: BlackListJwtComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.blackListJwt.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'black-list-jwt/:id/view',
        component: BlackListJwtDetailComponent,
        resolve: {
            blackListJwt: BlackListJwtResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.blackListJwt.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'black-list-jwt/new',
        component: BlackListJwtUpdateComponent,
        resolve: {
            blackListJwt: BlackListJwtResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.blackListJwt.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'black-list-jwt/:id/edit',
        component: BlackListJwtUpdateComponent,
        resolve: {
            blackListJwt: BlackListJwtResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.blackListJwt.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const blackListJwtPopupRoute: Routes = [
    {
        path: 'black-list-jwt/:id/delete',
        component: BlackListJwtDeletePopupComponent,
        resolve: {
            blackListJwt: BlackListJwtResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'desgloseApp.blackListJwt.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
