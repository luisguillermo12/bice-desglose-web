import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    TablaServMailDestComponent,
    TablaServMailDestDetailComponent,
    TablaServMailDestUpdateComponent,
    TablaServMailDestDeletePopupComponent,
    TablaServMailDestDeleteDialogComponent,
    tablaServMailDestRoute,
    tablaServMailDestPopupRoute
} from './';

const ENTITY_STATES = [...tablaServMailDestRoute, ...tablaServMailDestPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TablaServMailDestComponent,
        TablaServMailDestDetailComponent,
        TablaServMailDestUpdateComponent,
        TablaServMailDestDeleteDialogComponent,
        TablaServMailDestDeletePopupComponent
    ],
    entryComponents: [
        TablaServMailDestComponent,
        TablaServMailDestUpdateComponent,
        TablaServMailDestDeleteDialogComponent,
        TablaServMailDestDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTablaServMailDestModule {}
