import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { EmpresaConsolidada, IEmpresaConsolidada } from 'app/shared/model/empresa-consolidada.model';
import { EmpresaConsolidadaService } from './empresa-consolidada.service';
import { EmpresaConsolidadaComponent } from './empresa-consolidada.component';
import { EmpresaConsolidadaDetailComponent } from './empresa-consolidada-detail.component';
import { EmpresaConsolidadaUpdateComponent } from './empresa-consolidada-update.component';
import { EmpresaConsolidadaDeletePopupComponent } from './empresa-consolidada-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class EmpresaConsolidadaResolve implements Resolve<IEmpresaConsolidada> {
    constructor(private service: EmpresaConsolidadaService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params[ 'id' ] ? route.params[ 'id' ] : null;
        if (id) {
            return this.service.find(id).pipe(map((empresaConsolidada: HttpResponse<EmpresaConsolidada>) => empresaConsolidada.body));
        }
        return of(new EmpresaConsolidada());
    }
}

export const empresaConsolidadaRoute: Routes = [
    {
        path: 'empresa-consolidada',
        component: EmpresaConsolidadaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'id,asc',
            pageTitle: 'desgloseApp.empresaConsolidada.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'empresa-consolidada/:id/view',
        component: EmpresaConsolidadaDetailComponent,
        resolve: {
            empresaConsolidada: EmpresaConsolidadaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.empresaConsolidada.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'empresa-consolidada/new',
        component: EmpresaConsolidadaUpdateComponent,
        resolve: {
            empresaConsolidada: EmpresaConsolidadaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.empresaConsolidada.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'empresa-consolidada/:id/edit',
        component: EmpresaConsolidadaUpdateComponent,
        resolve: {
            empresaConsolidada: EmpresaConsolidadaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.empresaConsolidada.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const empresaConsolidadaPopupRoute: Routes = [
    {
        path: 'empresa-consolidada/:id/delete',
        component: EmpresaConsolidadaDeletePopupComponent,
        resolve: {
            empresaConsolidada: EmpresaConsolidadaResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.empresaConsolidada.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
