import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { ReviewersComponent } from './';
import { USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

export const MAINTENANCE_ROUTE: Route = {
    path: 'reviewers',
    component: ReviewersComponent,
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
        pageTitle: 'reviewers.title'
    },
    canActivate: [ UserRouteAccessService ]
};
