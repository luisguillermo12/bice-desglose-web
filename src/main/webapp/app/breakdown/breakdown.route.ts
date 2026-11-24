import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { BreakdownComponent } from './';
import { BreakdownClientsFilterTableComponent } from 'app/breakdown/breakdown-clients-filter-table/breakdown-clients-filter-table.component';
import { USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';
import { PendingChangesGuard } from 'app/shared/guards/pending-changes-guard';

export const BREAKDOWN_ROUTE: Routes = [
    {
        path: 'breakdown',
        component: BreakdownComponent,
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR ],
            pageTitle: 'breakdown.title'
        },
        canActivate: [ UserRouteAccessService ],
        canDeactivate: [ PendingChangesGuard ]
    },
    /*    {
            path: 'breakdown/:hasRut/:codCliente/:nomCliente/:codActividad/:codPlantilla/:codTipoDes/:codSubTipoDes/:codMoneda/:codUnidadMon/:nombreAnalista/:userAnalista',
            component: BreakdownComponent,
            data: {
                authorities: ['ROLE_ADMIN','ROLE_USER','ROLE_EJECUTOR','ROLE_REVISOR','ROLE_JEFE'],
                pageTitle: 'breakdown.title'
            },
            canActivate: [UserRouteAccessService]
        },*/
    {
        path: 'breakdown-filter',
        component: BreakdownClientsFilterTableComponent,
        data: {
            authorities: [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR ],
            pageTitle: 'breakdown.title'
        },
        canActivate: [ UserRouteAccessService ]
    }
];
