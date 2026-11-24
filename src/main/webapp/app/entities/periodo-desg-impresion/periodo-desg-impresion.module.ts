import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    PeriodoDesgImpresionComponent,
    PeriodoDesgImpresionDetailComponent,
    PeriodoDesgImpresionUpdateComponent,
    PeriodoDesgImpresionDeletePopupComponent,
    PeriodoDesgImpresionDeleteDialogComponent,
    periodoDesgImpresionRoute,
    periodoDesgImpresionPopupRoute
} from './';

const ENTITY_STATES = [...periodoDesgImpresionRoute, ...periodoDesgImpresionPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PeriodoDesgImpresionComponent,
        PeriodoDesgImpresionDetailComponent,
        PeriodoDesgImpresionUpdateComponent,
        PeriodoDesgImpresionDeleteDialogComponent,
        PeriodoDesgImpresionDeletePopupComponent
    ],
    entryComponents: [
        PeriodoDesgImpresionComponent,
        PeriodoDesgImpresionUpdateComponent,
        PeriodoDesgImpresionDeleteDialogComponent,
        PeriodoDesgImpresionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesglosePeriodoDesgImpresionModule {}
