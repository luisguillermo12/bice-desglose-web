import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseAuditoriaComponent,
    DesgloseAuditoriaDetailComponent,
    DesgloseAuditoriaUpdateComponent,
    DesgloseAuditoriaDeletePopupComponent,
    DesgloseAuditoriaDeleteDialogComponent,
    desgloseAuditoriaRoute,
    desgloseAuditoriaPopupRoute
} from './';

const ENTITY_STATES = [...desgloseAuditoriaRoute, ...desgloseAuditoriaPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseAuditoriaComponent,
        DesgloseAuditoriaDetailComponent,
        DesgloseAuditoriaUpdateComponent,
        DesgloseAuditoriaDeleteDialogComponent,
        DesgloseAuditoriaDeletePopupComponent
    ],
    entryComponents: [
        DesgloseAuditoriaComponent,
        DesgloseAuditoriaUpdateComponent,
        DesgloseAuditoriaDeleteDialogComponent,
        DesgloseAuditoriaDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseAuditoriaModule {}
