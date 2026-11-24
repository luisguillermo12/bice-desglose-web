import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConceptoImpresion, IConceptoImpresion } from 'app/shared/model/concepto-impresion.model';
import { ConceptoImpresionService } from './concepto-impresion.service';
import { ConceptoImpresionComponent } from './concepto-impresion.component';
import { ConceptoImpresionDetailComponent } from './concepto-impresion-detail.component';
import { ConceptoImpresionUpdateComponent } from './concepto-impresion-update.component';
import { ConceptoImpresionDeletePopupComponent } from './concepto-impresion-delete-dialog.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class ConceptoImpresionResolve implements Resolve<IConceptoImpresion> {
    constructor(private service: ConceptoImpresionService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const codCliente = route.params[ 'codCliente' ] ? route.params[ 'codCliente' ] : null;
        const codTipoDes = route.params[ 'codTipoDes' ] ? route.params[ 'codTipoDes' ] : null;
        const codSubtipoDes = route.params[ 'codSubtipoDes' ] ? route.params[ 'codSubtipoDes' ] : null;
        const codMoneda = route.params[ 'codMoneda' ] ? route.params[ 'codMoneda' ] : null;
        const codTipoConcep = route.params[ 'codTipoConcep' ] ? route.params[ 'codTipoConcep' ] : null;
        const corConcepto = route.params[ 'corConcepto' ] ? route.params[ 'corConcepto' ] : null;
        if (codCliente && codTipoDes && codSubtipoDes && codMoneda && codTipoConcep && corConcepto) {
            return this.service
            .find(codCliente, codTipoDes, codSubtipoDes, codMoneda, codTipoConcep, corConcepto)
            .pipe(map((conceptoImpresion: HttpResponse<ConceptoImpresion>) => conceptoImpresion.body));
        }
        return of(new ConceptoImpresion());
    }
}

export const conceptoImpresionRoute: Routes = [
    {
        path: 'concepto-impresion',
        component: ConceptoImpresionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            defaultSort: 'codCliente,asc',
            pageTitle: 'desgloseApp.conceptoImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'concepto-impresion/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codTipoConcep/:corConcepto/view',
        component: ConceptoImpresionDetailComponent,
        resolve: {
            conceptoImpresion: ConceptoImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.conceptoImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'concepto-impresion/new',
        component: ConceptoImpresionUpdateComponent,
        resolve: {
            conceptoImpresion: ConceptoImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.conceptoImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'concepto-impresion/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codTipoConcep/:corConcepto/edit',
        component: ConceptoImpresionUpdateComponent,
        resolve: {
            conceptoImpresion: ConceptoImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.conceptoImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];

export const conceptoImpresionPopupRoute: Routes = [
    {
        path: 'concepto-impresion/:codCliente/:codTipoDes/:codSubtipoDes/:codMoneda/:codTipoConcep/:corConcepto/delete',
        component: ConceptoImpresionDeletePopupComponent,
        resolve: {
            conceptoImpresion: ConceptoImpresionResolve
        },
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE ],
            pageTitle: 'desgloseApp.conceptoImpresion.home.title'
        },
        canActivate: [ UserRouteAccessService ],
        outlet: 'popup'
    }
];
