import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    PeriodoComponent,
    PeriodoDeleteDialogComponent,
    PeriodoDeletePopupComponent,
    PeriodoDetailComponent,
    periodoPopupRoute,
    periodoRoute,
    PeriodoUpdateComponent
} from './';

const ENTITY_STATES = [...periodoRoute, ...periodoPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PeriodoComponent,
        PeriodoDetailComponent,
        PeriodoUpdateComponent,
        PeriodoDeleteDialogComponent,
        PeriodoDeletePopupComponent
    ],
    entryComponents: [PeriodoComponent, PeriodoUpdateComponent, PeriodoDeleteDialogComponent, PeriodoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesglosePeriodoModule {}
