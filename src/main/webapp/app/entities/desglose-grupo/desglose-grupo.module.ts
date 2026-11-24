import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseGrupoComponent,
    DesgloseGrupoDetailComponent,
    DesgloseGrupoUpdateComponent,
    DesgloseGrupoDeletePopupComponent,
    DesgloseGrupoDeleteDialogComponent,
    desgloseGrupoRoute,
    desgloseGrupoPopupRoute
} from './';

const ENTITY_STATES = [...desgloseGrupoRoute, ...desgloseGrupoPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseGrupoComponent,
        DesgloseGrupoDetailComponent,
        DesgloseGrupoUpdateComponent,
        DesgloseGrupoDeleteDialogComponent,
        DesgloseGrupoDeletePopupComponent
    ],
    entryComponents: [
        DesgloseGrupoComponent,
        DesgloseGrupoUpdateComponent,
        DesgloseGrupoDeleteDialogComponent,
        DesgloseGrupoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseGrupoModule {}
