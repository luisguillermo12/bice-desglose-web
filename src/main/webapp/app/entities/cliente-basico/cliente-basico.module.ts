import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    ClienteBasicoComponent,
    ClienteBasicoDetailComponent,
    ClienteBasicoUpdateComponent,
    ClienteBasicoDeletePopupComponent,
    ClienteBasicoDeleteDialogComponent,
    clienteBasicoRoute,
    clienteBasicoPopupRoute
} from './';

const ENTITY_STATES = [...clienteBasicoRoute, ...clienteBasicoPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ClienteBasicoComponent,
        ClienteBasicoDetailComponent,
        ClienteBasicoUpdateComponent,
        ClienteBasicoDeleteDialogComponent,
        ClienteBasicoDeletePopupComponent
    ],
    entryComponents: [
        ClienteBasicoComponent,
        ClienteBasicoUpdateComponent,
        ClienteBasicoDeleteDialogComponent,
        ClienteBasicoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseClienteBasicoModule {}
