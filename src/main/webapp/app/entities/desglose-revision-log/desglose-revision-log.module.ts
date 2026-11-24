import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseRevisionLogComponent,
    DesgloseRevisionLogDetailComponent,
    DesgloseRevisionLogUpdateComponent,
    DesgloseRevisionLogDeletePopupComponent,
    DesgloseRevisionLogDeleteDialogComponent,
    desgloseRevisionLogRoute,
    desgloseRevisionLogPopupRoute
} from './';

const ENTITY_STATES = [...desgloseRevisionLogRoute, ...desgloseRevisionLogPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseRevisionLogComponent,
        DesgloseRevisionLogDetailComponent,
        DesgloseRevisionLogUpdateComponent,
        DesgloseRevisionLogDeleteDialogComponent,
        DesgloseRevisionLogDeletePopupComponent
    ],
    entryComponents: [
        DesgloseRevisionLogComponent,
        DesgloseRevisionLogUpdateComponent,
        DesgloseRevisionLogDeleteDialogComponent,
        DesgloseRevisionLogDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseRevisionLogModule {}
