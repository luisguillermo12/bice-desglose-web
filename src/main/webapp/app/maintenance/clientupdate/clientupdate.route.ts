import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { ClientupdateComponent } from './clientupdate.component';
import { ClientupdateformComponent } from 'app/maintenance/clientupdate/clientupdateform.component';
import { USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';

export const MAINTENANCE_ROUTE: Routes = [
    {
        path: 'clientupdate',
        component: ClientupdateComponent,
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            pageTitle: 'clientupdate.title'
        },
        canActivate: [ UserRouteAccessService ]
    },
    {
        path: 'clientupdate/:id/edit',
        component: ClientupdateformComponent,
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ],
            pageTitle: 'clientupdate.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];
