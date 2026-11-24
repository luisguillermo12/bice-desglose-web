import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseImpresionComponent,
    DesgloseImpresionDetailComponent,
    DesgloseImpresionUpdateComponent,
    DesgloseImpresionDeletePopupComponent,
    DesgloseImpresionDeleteDialogComponent,
    desgloseImpresionRoute,
    desgloseImpresionPopupRoute
} from './';

const ENTITY_STATES = [...desgloseImpresionRoute, ...desgloseImpresionPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseImpresionComponent,
        DesgloseImpresionDetailComponent,
        DesgloseImpresionUpdateComponent,
        DesgloseImpresionDeleteDialogComponent,
        DesgloseImpresionDeletePopupComponent
    ],
    entryComponents: [
        DesgloseImpresionComponent,
        DesgloseImpresionUpdateComponent,
        DesgloseImpresionDeleteDialogComponent,
        DesgloseImpresionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseImpresionModule {}
