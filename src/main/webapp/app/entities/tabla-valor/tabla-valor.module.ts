import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TablaValorComponent,
    TablaValorDetailComponent,
    TablaValorUpdateComponent,
    TablaValorDeletePopupComponent,
    TablaValorDeleteDialogComponent,
    tablaValorRoute,
    tablaValorPopupRoute
} from './';

const ENTITY_STATES = [...tablaValorRoute, ...tablaValorPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TablaValorComponent,
        TablaValorDetailComponent,
        TablaValorUpdateComponent,
        TablaValorDeleteDialogComponent,
        TablaValorDeletePopupComponent
    ],
    entryComponents: [TablaValorComponent, TablaValorUpdateComponent, TablaValorDeleteDialogComponent, TablaValorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTablaValorModule {}
