import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { LogsComponent } from './';
import { USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

export const MAINTENANCE_ROUTE: Route = {
    path: 'logs',
    component: LogsComponent,
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
        pageTitle: 'replacement.title'
    },
    canActivate: [ UserRouteAccessService ]
};
