import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';
import { ProjectionsComponent } from './';

export const PROJECTIONS_ROUTE: Routes = [
    {
        path: 'projections',
        component: ProjectionsComponent,
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_EJECUTOR,USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            pageTitle: 'desgloseApp.projections.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
];
