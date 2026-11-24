import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    ValPeriodoDesgImprComponent,
    ValPeriodoDesgImprDetailComponent,
    ValPeriodoDesgImprUpdateComponent,
    ValPeriodoDesgImprDeletePopupComponent,
    ValPeriodoDesgImprDeleteDialogComponent,
    valPeriodoDesgImprRoute,
    valPeriodoDesgImprPopupRoute
} from './';

const ENTITY_STATES = [...valPeriodoDesgImprRoute, ...valPeriodoDesgImprPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ValPeriodoDesgImprComponent,
        ValPeriodoDesgImprDetailComponent,
        ValPeriodoDesgImprUpdateComponent,
        ValPeriodoDesgImprDeleteDialogComponent,
        ValPeriodoDesgImprDeletePopupComponent
    ],
    entryComponents: [
        ValPeriodoDesgImprComponent,
        ValPeriodoDesgImprUpdateComponent,
        ValPeriodoDesgImprDeleteDialogComponent,
        ValPeriodoDesgImprDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseValPeriodoDesgImprModule {}
