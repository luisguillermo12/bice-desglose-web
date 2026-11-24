import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    EmpresaConsolidadaComponent,
    EmpresaConsolidadaDetailComponent,
    EmpresaConsolidadaUpdateComponent,
    EmpresaConsolidadaDeletePopupComponent,
    EmpresaConsolidadaDeleteDialogComponent,
    empresaConsolidadaRoute,
    empresaConsolidadaPopupRoute
} from './';

const ENTITY_STATES = [...empresaConsolidadaRoute, ...empresaConsolidadaPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EmpresaConsolidadaComponent,
        EmpresaConsolidadaDetailComponent,
        EmpresaConsolidadaUpdateComponent,
        EmpresaConsolidadaDeleteDialogComponent,
        EmpresaConsolidadaDeletePopupComponent
    ],
    entryComponents: [
        EmpresaConsolidadaComponent,
        EmpresaConsolidadaUpdateComponent,
        EmpresaConsolidadaDeleteDialogComponent,
        EmpresaConsolidadaDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseEmpresaConsolidadaModule {}
