import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DescripcionCodigoComponent,
    DescripcionCodigoDetailComponent,
    DescripcionCodigoUpdateComponent,
    DescripcionCodigoDeletePopupComponent,
    DescripcionCodigoDeleteDialogComponent,
    descripcionCodigoRoute,
    descripcionCodigoPopupRoute
} from './';

const ENTITY_STATES = [...descripcionCodigoRoute, ...descripcionCodigoPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DescripcionCodigoComponent,
        DescripcionCodigoDetailComponent,
        DescripcionCodigoUpdateComponent,
        DescripcionCodigoDeleteDialogComponent,
        DescripcionCodigoDeletePopupComponent
    ],
    entryComponents: [
        DescripcionCodigoComponent,
        DescripcionCodigoUpdateComponent,
        DescripcionCodigoDeleteDialogComponent,
        DescripcionCodigoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDescripcionCodigoModule {}
