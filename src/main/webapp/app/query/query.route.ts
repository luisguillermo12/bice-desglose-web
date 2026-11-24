import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { QueryComponent } from './';
import { USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

export const QUERY_ROUTE: Route = {
    path: 'query/:isExternal',
    component: QueryComponent,
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR ],
        pageTitle: 'query.title'
    },
    canActivate: [ UserRouteAccessService ]
};
