import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    ConceptoImpresionComponent,
    ConceptoImpresionDeleteDialogComponent,
    ConceptoImpresionDeletePopupComponent,
    ConceptoImpresionDetailComponent,
    conceptoImpresionPopupRoute,
    conceptoImpresionRoute,
    ConceptoImpresionUpdateComponent
} from './';

const ENTITY_STATES = [...conceptoImpresionRoute, ...conceptoImpresionPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ConceptoImpresionComponent,
        ConceptoImpresionDetailComponent,
        ConceptoImpresionUpdateComponent,
        ConceptoImpresionDeleteDialogComponent,
        ConceptoImpresionDeletePopupComponent
    ],
    entryComponents: [
        ConceptoImpresionComponent,
        ConceptoImpresionUpdateComponent,
        ConceptoImpresionDeleteDialogComponent,
        ConceptoImpresionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseConceptoImpresionModule {}
