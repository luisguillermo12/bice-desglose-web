import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Concepto, IConcepto } from 'app/shared/model/concepto.model';
import { ConceptoService } from './concepto.service';
import { ConceptoComponent } from './concepto.component';
import { ConceptoDetailComponent } from './concepto-detail.component';
import { ConceptoUpdateComponent, ConceptoUpdatePopupComponent } from './concepto-update.component';
import { ConceptoDeletePopupComponent } from './concepto-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ConceptoResolve implements Resolve<IConcepto> {
    constructor(private service: ConceptoService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const codTipoConcep = route.params[ 'codTipoConcep' ] ? route.params[ 'codTipoConcep' ] : null;
        const corConcepto = route.params[ 'corConcepto' ] ? route.params[ 'corConcepto' ] : null;
        const codTipoPlantilla = route.params[ 'codTipoPlantilla' ] ? route.params[ 'codTipoPlantilla' ] : null;
        const codConcepto = route.params[ 'codConcepto' ] ? route.params[ 'codConcepto' ] : null;
        if (codTipoConcep && corConcepto && codTipoPlantilla) {
            return this.service.find(codTipoConcep, corConcepto, codTipoPlantilla).pipe(map((concepto: HttpResponse<Concepto>) => concepto.body));
        }
        const concept: Concepto = new Concepto();
        concept.codTipoPlantilla = codTipoPlantilla;
        concept.codTipoConcep = codTipoConcep;
        concept.codConcepto = codConcepto;
        return of(concept);
    }
}

export const conceptoRoute: Routes = [
    {
        path: 'concepto',
        component: ConceptoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codTipoConcep,corConcepto,codConcepto,asc',
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'concepto/:codTipoConcep/:corConcepto/:codTipoPlantilla/view',
        component: ConceptoDetailComponent,
        resolve: {
            concepto: ConceptoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'concepto/new',
        component: ConceptoUpdateComponent,
        resolve: {
            concepto: ConceptoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ],
    },
    {
        path: 'concepto/:codTipoConcep/:corConcepto/:codTipoPlantilla/edit',
        component: ConceptoUpdateComponent,
        resolve: {
            concepto: ConceptoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const conceptoPopupRoute: Routes = [
    {
        path: 'concepto/:codTipoConcep/:corConcepto/:codTipoPlantilla/delete',
        component: ConceptoDeletePopupComponent,
        resolve: {
            concepto: ConceptoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    },
    {
        path: 'concepto/new',
        component: ConceptoUpdatePopupComponent,
        resolve: {
            concepto: ConceptoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    },
    {
        path: 'concepto/:codTipoConcep/:corConcepto/:codTipoPlantilla/edit',
        component: ConceptoUpdatePopupComponent,
        resolve: {
            concepto: ConceptoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    },
    {
        path: 'concepto/:codTipoConcep/:codTipoPlantilla/:codConcepto/new',
        component: ConceptoUpdatePopupComponent,
        resolve: {
            concepto: ConceptoResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.concepto.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
