import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    RelacionTablaComponent,
    RelacionTablaDetailComponent,
    RelacionTablaUpdateComponent,
    RelacionTablaDeletePopupComponent,
    RelacionTablaDeleteDialogComponent,
    relacionTablaRoute,
    relacionTablaPopupRoute
} from './';

const ENTITY_STATES = [...relacionTablaRoute, ...relacionTablaPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RelacionTablaComponent,
        RelacionTablaDetailComponent,
        RelacionTablaUpdateComponent,
        RelacionTablaDeleteDialogComponent,
        RelacionTablaDeletePopupComponent
    ],
    entryComponents: [
        RelacionTablaComponent,
        RelacionTablaUpdateComponent,
        RelacionTablaDeleteDialogComponent,
        RelacionTablaDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseRelacionTablaModule {}
