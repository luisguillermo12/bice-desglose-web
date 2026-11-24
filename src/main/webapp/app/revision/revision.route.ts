import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { RevisionComponent } from './';
import { USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

export const REVISION_ROUTE: Route = {
    path: 'revision/:isExternal',
    component: RevisionComponent,
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR ],
        pageTitle: 'revision.title'
    },
    canActivate: [ UserRouteAccessService ]
};
