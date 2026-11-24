import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TurbineRoleComponent,
    TurbineRoleDeleteDialogComponent,
    TurbineRoleDeletePopupComponent,
    TurbineRoleDetailComponent,
    turbineRolePopupRoute,
    turbineRoleRoute,
    TurbineRoleUpdateComponent
} from './';

const ENTITY_STATES = [...turbineRoleRoute, ...turbineRolePopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TurbineRoleComponent,
        TurbineRoleDetailComponent,
        TurbineRoleUpdateComponent,
        TurbineRoleDeleteDialogComponent,
        TurbineRoleDeletePopupComponent
    ],
    entryComponents: [TurbineRoleComponent, TurbineRoleUpdateComponent, TurbineRoleDeleteDialogComponent, TurbineRoleDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTurbineRoleModule {}
