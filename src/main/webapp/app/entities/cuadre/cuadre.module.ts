import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    CuadreComponent,
    CuadreDeleteDialogComponent,
    CuadreDeletePopupComponent,
    CuadreDetailComponent,
    cuadrePopupRoute,
    cuadreRoute,
    CuadreUpdateComponent
} from './';

const ENTITY_STATES = [...cuadreRoute, ...cuadrePopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CuadreComponent, CuadreDetailComponent, CuadreUpdateComponent, CuadreDeleteDialogComponent, CuadreDeletePopupComponent],
    entryComponents: [CuadreComponent, CuadreUpdateComponent, CuadreDeleteDialogComponent, CuadreDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseCuadreModule {}
