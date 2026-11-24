import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    ClienteNaturalComponent,
    ClienteNaturalDetailComponent,
    ClienteNaturalUpdateComponent,
    ClienteNaturalDeletePopupComponent,
    ClienteNaturalDeleteDialogComponent,
    clienteNaturalRoute,
    clienteNaturalPopupRoute
} from './';

const ENTITY_STATES = [...clienteNaturalRoute, ...clienteNaturalPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ClienteNaturalComponent,
        ClienteNaturalDetailComponent,
        ClienteNaturalUpdateComponent,
        ClienteNaturalDeleteDialogComponent,
        ClienteNaturalDeletePopupComponent
    ],
    entryComponents: [
        ClienteNaturalComponent,
        ClienteNaturalUpdateComponent,
        ClienteNaturalDeleteDialogComponent,
        ClienteNaturalDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseClienteNaturalModule {}
