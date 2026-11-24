import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    PeriodoDesgConsolComponent,
    PeriodoDesgConsolDetailComponent,
    PeriodoDesgConsolUpdateComponent,
    PeriodoDesgConsolDeletePopupComponent,
    PeriodoDesgConsolDeleteDialogComponent,
    periodoDesgConsolRoute,
    periodoDesgConsolPopupRoute
} from './';

const ENTITY_STATES = [...periodoDesgConsolRoute, ...periodoDesgConsolPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PeriodoDesgConsolComponent,
        PeriodoDesgConsolDetailComponent,
        PeriodoDesgConsolUpdateComponent,
        PeriodoDesgConsolDeleteDialogComponent,
        PeriodoDesgConsolDeletePopupComponent
    ],
    entryComponents: [
        PeriodoDesgConsolComponent,
        PeriodoDesgConsolUpdateComponent,
        PeriodoDesgConsolDeleteDialogComponent,
        PeriodoDesgConsolDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesglosePeriodoDesgConsolModule {}
