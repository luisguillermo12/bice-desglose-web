import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseChequeoInfoComponent,
    DesgloseChequeoInfoDetailComponent,
    DesgloseChequeoInfoUpdateComponent,
    DesgloseChequeoInfoDeletePopupComponent,
    DesgloseChequeoInfoDeleteDialogComponent,
    desgloseChequeoInfoRoute,
    desgloseChequeoInfoPopupRoute
} from './';

const ENTITY_STATES = [...desgloseChequeoInfoRoute, ...desgloseChequeoInfoPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseChequeoInfoComponent,
        DesgloseChequeoInfoDetailComponent,
        DesgloseChequeoInfoUpdateComponent,
        DesgloseChequeoInfoDeleteDialogComponent,
        DesgloseChequeoInfoDeletePopupComponent
    ],
    entryComponents: [
        DesgloseChequeoInfoComponent,
        DesgloseChequeoInfoUpdateComponent,
        DesgloseChequeoInfoDeleteDialogComponent,
        DesgloseChequeoInfoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseChequeoInfoModule {}
