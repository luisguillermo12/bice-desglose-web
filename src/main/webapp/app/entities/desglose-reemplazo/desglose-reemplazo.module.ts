import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseReemplazoComponent,
    DesgloseReemplazoDetailComponent,
    DesgloseReemplazoUpdateComponent,
    DesgloseReemplazoDeletePopupComponent,
    DesgloseReemplazoDeleteDialogComponent,
    desgloseReemplazoRoute,
    desgloseReemplazoPopupRoute
} from './';

const ENTITY_STATES = [...desgloseReemplazoRoute, ...desgloseReemplazoPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseReemplazoComponent,
        DesgloseReemplazoDetailComponent,
        DesgloseReemplazoUpdateComponent,
        DesgloseReemplazoDeleteDialogComponent,
        DesgloseReemplazoDeletePopupComponent
    ],
    entryComponents: [
        DesgloseReemplazoComponent,
        DesgloseReemplazoUpdateComponent,
        DesgloseReemplazoDeleteDialogComponent,
        DesgloseReemplazoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseReemplazoModule {}
