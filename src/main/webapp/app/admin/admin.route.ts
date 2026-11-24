import { Routes } from '@angular/router';

import { auditsRoute, configurationRoute, docsRoute, healthRoute, logsRoute, metricsRoute, userMgmtRoute } from './';

import { UserRouteAccessService } from 'app/core';
import { USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR } from 'app/app.constants';

const ADMIN_ROUTES = [ auditsRoute, configurationRoute, docsRoute, healthRoute, logsRoute, ...userMgmtRoute, metricsRoute ];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR ]
        },
        canActivate: [ UserRouteAccessService ],
        children: ADMIN_ROUTES
    }
];
