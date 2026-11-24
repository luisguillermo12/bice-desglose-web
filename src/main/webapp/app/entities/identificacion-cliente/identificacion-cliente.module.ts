import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    IdentificacionClienteComponent,
    IdentificacionClienteDetailComponent,
    IdentificacionClienteUpdateComponent,
    IdentificacionClienteDeletePopupComponent,
    IdentificacionClienteDeleteDialogComponent,
    identificacionClienteRoute,
    identificacionClientePopupRoute
} from './';

const ENTITY_STATES = [...identificacionClienteRoute, ...identificacionClientePopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        IdentificacionClienteComponent,
        IdentificacionClienteDetailComponent,
        IdentificacionClienteUpdateComponent,
        IdentificacionClienteDeleteDialogComponent,
        IdentificacionClienteDeletePopupComponent
    ],
    entryComponents: [
        IdentificacionClienteComponent,
        IdentificacionClienteUpdateComponent,
        IdentificacionClienteDeleteDialogComponent,
        IdentificacionClienteDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseIdentificacionClienteModule {}
