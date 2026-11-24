import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { TemplateDesgloComponent } from './';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

export const MAINTENANCE_ROUTE: Route = {
    path: 'template-desglo',
    component: TemplateDesgloComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
        defaultSort: 'codTipoConcep,corConcepto,codConcepto,asc',
        pageTitle: 'templateDesglo.title'
    },
    canActivate: [ UserRouteAccessService ]
};
