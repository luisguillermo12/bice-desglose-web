import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TablaServicioMailComponent,
    TablaServicioMailDetailComponent,
    TablaServicioMailUpdateComponent,
    TablaServicioMailDeletePopupComponent,
    TablaServicioMailDeleteDialogComponent,
    tablaServicioMailRoute,
    tablaServicioMailPopupRoute
} from './';

const ENTITY_STATES = [...tablaServicioMailRoute, ...tablaServicioMailPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TablaServicioMailComponent,
        TablaServicioMailDetailComponent,
        TablaServicioMailUpdateComponent,
        TablaServicioMailDeleteDialogComponent,
        TablaServicioMailDeletePopupComponent
    ],
    entryComponents: [
        TablaServicioMailComponent,
        TablaServicioMailUpdateComponent,
        TablaServicioMailDeleteDialogComponent,
        TablaServicioMailDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTablaServicioMailModule {}
