import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseRevisionComponent,
    DesgloseRevisionDetailComponent,
    DesgloseRevisionUpdateComponent,
    DesgloseRevisionDeletePopupComponent,
    DesgloseRevisionDeleteDialogComponent,
    desgloseRevisionRoute,
    desgloseRevisionPopupRoute
} from './';

const ENTITY_STATES = [...desgloseRevisionRoute, ...desgloseRevisionPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseRevisionComponent,
        DesgloseRevisionDetailComponent,
        DesgloseRevisionUpdateComponent,
        DesgloseRevisionDeleteDialogComponent,
        DesgloseRevisionDeletePopupComponent
    ],
    entryComponents: [
        DesgloseRevisionComponent,
        DesgloseRevisionUpdateComponent,
        DesgloseRevisionDeleteDialogComponent,
        DesgloseRevisionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseRevisionModule {}
