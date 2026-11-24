import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    SfStfOperatorComponent,
    SfStfOperatorDetailComponent,
    SfStfOperatorUpdateComponent,
    SfStfOperatorDeletePopupComponent,
    SfStfOperatorDeleteDialogComponent,
    sfStfOperatorRoute,
    sfStfOperatorPopupRoute
} from './';

const ENTITY_STATES = [...sfStfOperatorRoute, ...sfStfOperatorPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SfStfOperatorComponent,
        SfStfOperatorDetailComponent,
        SfStfOperatorUpdateComponent,
        SfStfOperatorDeleteDialogComponent,
        SfStfOperatorDeletePopupComponent
    ],
    entryComponents: [
        SfStfOperatorComponent,
        SfStfOperatorUpdateComponent,
        SfStfOperatorDeleteDialogComponent,
        SfStfOperatorDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseSfStfOperatorModule {}
