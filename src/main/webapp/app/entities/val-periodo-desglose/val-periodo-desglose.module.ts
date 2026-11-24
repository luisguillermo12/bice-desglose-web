import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    ValPeriodoDesgloseComponent,
    ValPeriodoDesgloseDetailComponent,
    ValPeriodoDesgloseUpdateComponent,
    ValPeriodoDesgloseDeletePopupComponent,
    ValPeriodoDesgloseDeleteDialogComponent,
    valPeriodoDesgloseRoute,
    valPeriodoDesglosePopupRoute
} from './';

const ENTITY_STATES = [...valPeriodoDesgloseRoute, ...valPeriodoDesglosePopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ValPeriodoDesgloseComponent,
        ValPeriodoDesgloseDetailComponent,
        ValPeriodoDesgloseUpdateComponent,
        ValPeriodoDesgloseDeleteDialogComponent,
        ValPeriodoDesgloseDeletePopupComponent
    ],
    entryComponents: [
        ValPeriodoDesgloseComponent,
        ValPeriodoDesgloseUpdateComponent,
        ValPeriodoDesgloseDeleteDialogComponent,
        ValPeriodoDesgloseDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseValPeriodoDesgloseModule {}
