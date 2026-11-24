import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { SettingsComponent } from './settings.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

export const settingsRoute: Route = {
    path: 'settings',
    component: SettingsComponent,
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE ],
        pageTitle: 'global.menu.account.settings'
    },
    canActivate: [ UserRouteAccessService ]
};
