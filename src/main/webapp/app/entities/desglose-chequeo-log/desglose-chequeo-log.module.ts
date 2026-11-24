import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseChequeoLogComponent,
    DesgloseChequeoLogDetailComponent,
    DesgloseChequeoLogUpdateComponent,
    DesgloseChequeoLogDeletePopupComponent,
    DesgloseChequeoLogDeleteDialogComponent,
    desgloseChequeoLogRoute,
    desgloseChequeoLogPopupRoute
} from './';

const ENTITY_STATES = [...desgloseChequeoLogRoute, ...desgloseChequeoLogPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseChequeoLogComponent,
        DesgloseChequeoLogDetailComponent,
        DesgloseChequeoLogUpdateComponent,
        DesgloseChequeoLogDeleteDialogComponent,
        DesgloseChequeoLogDeletePopupComponent
    ],
    entryComponents: [
        DesgloseChequeoLogComponent,
        DesgloseChequeoLogUpdateComponent,
        DesgloseChequeoLogDeleteDialogComponent,
        DesgloseChequeoLogDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseChequeoLogModule {}
