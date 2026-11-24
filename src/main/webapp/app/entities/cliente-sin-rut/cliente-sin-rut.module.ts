import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    ClienteSinRutComponent,
    ClienteSinRutDeleteDialogComponent,
    ClienteSinRutDeletePopupComponent,
    ClienteSinRutDetailComponent,
    clienteSinRutPopupRoute,
    clienteSinRutRoute,
    ClienteSinRutUpdateComponent
} from './';

const ENTITY_STATES = [...clienteSinRutRoute, ...clienteSinRutPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ClienteSinRutComponent,
        ClienteSinRutDetailComponent,
        ClienteSinRutUpdateComponent,
        ClienteSinRutDeleteDialogComponent,
        ClienteSinRutDeletePopupComponent
    ],
    entryComponents: [
        ClienteSinRutComponent,
        ClienteSinRutUpdateComponent,
        ClienteSinRutDeleteDialogComponent,
        ClienteSinRutDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseClienteSinRutModule {}
