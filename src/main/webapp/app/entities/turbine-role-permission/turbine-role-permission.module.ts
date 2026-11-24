import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TurbineRolePermissionComponent,
    TurbineRolePermissionDetailComponent,
    TurbineRolePermissionUpdateComponent,
    TurbineRolePermissionDeletePopupComponent,
    TurbineRolePermissionDeleteDialogComponent,
    turbineRolePermissionRoute,
    turbineRolePermissionPopupRoute
} from './';

const ENTITY_STATES = [...turbineRolePermissionRoute, ...turbineRolePermissionPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TurbineRolePermissionComponent,
        TurbineRolePermissionDetailComponent,
        TurbineRolePermissionUpdateComponent,
        TurbineRolePermissionDeleteDialogComponent,
        TurbineRolePermissionDeletePopupComponent
    ],
    entryComponents: [
        TurbineRolePermissionComponent,
        TurbineRolePermissionUpdateComponent,
        TurbineRolePermissionDeleteDialogComponent,
        TurbineRolePermissionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTurbineRolePermissionModule {}
