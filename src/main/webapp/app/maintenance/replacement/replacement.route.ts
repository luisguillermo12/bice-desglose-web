import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { ReplacementComponent } from './';
import { USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

export const MAINTENANCE_ROUTE: Route = {
    path: 'replacement',
    component: ReplacementComponent,
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
        pageTitle: 'replacement.title'
    },
    canActivate: [ UserRouteAccessService ]
};
