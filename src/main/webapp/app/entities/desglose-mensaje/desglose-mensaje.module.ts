import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseMensajeComponent,
    DesgloseMensajeDetailComponent,
    DesgloseMensajeUpdateComponent,
    DesgloseMensajeDeletePopupComponent,
    DesgloseMensajeDeleteDialogComponent,
    desgloseMensajeRoute,
    desgloseMensajePopupRoute
} from './';

const ENTITY_STATES = [...desgloseMensajeRoute, ...desgloseMensajePopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseMensajeComponent,
        DesgloseMensajeDetailComponent,
        DesgloseMensajeUpdateComponent,
        DesgloseMensajeDeleteDialogComponent,
        DesgloseMensajeDeletePopupComponent
    ],
    entryComponents: [
        DesgloseMensajeComponent,
        DesgloseMensajeUpdateComponent,
        DesgloseMensajeDeleteDialogComponent,
        DesgloseMensajeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseMensajeModule {}
