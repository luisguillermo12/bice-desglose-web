import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    ClienteJuridicoComponent,
    ClienteJuridicoDetailComponent,
    ClienteJuridicoUpdateComponent,
    ClienteJuridicoDeletePopupComponent,
    ClienteJuridicoDeleteDialogComponent,
    clienteJuridicoRoute,
    clienteJuridicoPopupRoute
} from './';

const ENTITY_STATES = [...clienteJuridicoRoute, ...clienteJuridicoPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ClienteJuridicoComponent,
        ClienteJuridicoDetailComponent,
        ClienteJuridicoUpdateComponent,
        ClienteJuridicoDeleteDialogComponent,
        ClienteJuridicoDeletePopupComponent
    ],
    entryComponents: [
        ClienteJuridicoComponent,
        ClienteJuridicoUpdateComponent,
        ClienteJuridicoDeleteDialogComponent,
        ClienteJuridicoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseClienteJuridicoModule {}
