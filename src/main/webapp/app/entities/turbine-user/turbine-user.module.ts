import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TurbineUserComponent,
    TurbineUserDetailComponent,
    TurbineUserUpdateComponent,
    TurbineUserDeletePopupComponent,
    TurbineUserDeleteDialogComponent,
    turbineUserRoute,
    turbineUserPopupRoute
} from './';

const ENTITY_STATES = [...turbineUserRoute, ...turbineUserPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TurbineUserComponent,
        TurbineUserDetailComponent,
        TurbineUserUpdateComponent,
        TurbineUserDeleteDialogComponent,
        TurbineUserDeletePopupComponent
    ],
    entryComponents: [TurbineUserComponent, TurbineUserUpdateComponent, TurbineUserDeleteDialogComponent, TurbineUserDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTurbineUserModule {}
