import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TurbineUserGroupRoleComponent,
    TurbineUserGroupRoleDetailComponent,
    TurbineUserGroupRoleUpdateComponent,
    TurbineUserGroupRoleDeletePopupComponent,
    TurbineUserGroupRoleDeleteDialogComponent,
    turbineUserGroupRoleRoute,
    turbineUserGroupRolePopupRoute
} from './';

const ENTITY_STATES = [...turbineUserGroupRoleRoute, ...turbineUserGroupRolePopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TurbineUserGroupRoleComponent,
        TurbineUserGroupRoleDetailComponent,
        TurbineUserGroupRoleUpdateComponent,
        TurbineUserGroupRoleDeleteDialogComponent,
        TurbineUserGroupRoleDeletePopupComponent
    ],
    entryComponents: [
        TurbineUserGroupRoleComponent,
        TurbineUserGroupRoleUpdateComponent,
        TurbineUserGroupRoleDeleteDialogComponent,
        TurbineUserGroupRoleDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTurbineUserGroupRoleModule {}
