import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TurbinePermissionComponent,
    TurbinePermissionDetailComponent,
    TurbinePermissionUpdateComponent,
    TurbinePermissionDeletePopupComponent,
    TurbinePermissionDeleteDialogComponent,
    turbinePermissionRoute,
    turbinePermissionPopupRoute
} from './';

const ENTITY_STATES = [...turbinePermissionRoute, ...turbinePermissionPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TurbinePermissionComponent,
        TurbinePermissionDetailComponent,
        TurbinePermissionUpdateComponent,
        TurbinePermissionDeleteDialogComponent,
        TurbinePermissionDeletePopupComponent
    ],
    entryComponents: [
        TurbinePermissionComponent,
        TurbinePermissionUpdateComponent,
        TurbinePermissionDeleteDialogComponent,
        TurbinePermissionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTurbinePermissionModule {}
