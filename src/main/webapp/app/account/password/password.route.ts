import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { PasswordComponent } from './password.component';
import { USER_ROLE_PLAT_DESGLOSE } from 'app/app.constants';

export const passwordRoute: Route = {
    path: 'password',
    component: PasswordComponent,
    data: {
        authorities: [ USER_ROLE_PLAT_DESGLOSE ],
        pageTitle: 'global.menu.account.password'
    },
    canActivate: [ UserRouteAccessService ]
};
