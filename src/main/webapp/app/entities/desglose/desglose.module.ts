import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseComponent,
    DesgloseDeleteDialogComponent,
    DesgloseDeletePopupComponent,
    DesgloseDetailComponent,
    desglosePopupRoute,
    desgloseRoute,
    DesgloseUpdateComponent
} from './';

const ENTITY_STATES = [...desgloseRoute, ...desglosePopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseComponent,
        DesgloseDetailComponent,
        DesgloseUpdateComponent,
        DesgloseDeleteDialogComponent,
        DesgloseDeletePopupComponent
    ],
    entryComponents: [DesgloseComponent, DesgloseUpdateComponent, DesgloseDeleteDialogComponent, DesgloseDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseModule {}
