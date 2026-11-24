import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TurbineGroupComponent,
    TurbineGroupDetailComponent,
    TurbineGroupUpdateComponent,
    TurbineGroupDeletePopupComponent,
    TurbineGroupDeleteDialogComponent,
    turbineGroupRoute,
    turbineGroupPopupRoute
} from './';

const ENTITY_STATES = [...turbineGroupRoute, ...turbineGroupPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TurbineGroupComponent,
        TurbineGroupDetailComponent,
        TurbineGroupUpdateComponent,
        TurbineGroupDeleteDialogComponent,
        TurbineGroupDeletePopupComponent
    ],
    entryComponents: [
        TurbineGroupComponent,
        TurbineGroupUpdateComponent,
        TurbineGroupDeleteDialogComponent,
        TurbineGroupDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTurbineGroupModule {}
