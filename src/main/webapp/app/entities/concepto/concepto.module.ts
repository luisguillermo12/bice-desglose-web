import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DesgloseSharedModule } from 'app/shared';
import {
    ConceptoComponent,
    ConceptoDeleteDialogComponent,
    ConceptoDeletePopupComponent,
    ConceptoDetailComponent,
    conceptoPopupRoute,
    conceptoRoute,
    ConceptoUpdateComponent,
    ConceptoUpdatePopupComponent
} from './';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedPipesModule } from 'app/shared/pipes/shared-pipes.module';

const ENTITY_STATES = [ ...conceptoRoute, ...conceptoPopupRoute ];

@NgModule({
    imports: [
        FormsModule,
        ReactiveFormsModule,
        NgxDatatableModule,
        DesgloseSharedModule,
        SharedPipesModule,
        RouterModule.forChild(ENTITY_STATES) ],
    declarations: [
        ConceptoComponent,
        ConceptoDetailComponent,
        ConceptoUpdateComponent,
        ConceptoDeleteDialogComponent,
        ConceptoDeletePopupComponent,
        ConceptoUpdatePopupComponent
    ],
    exports: [
        ConceptoComponent,
        ConceptoDetailComponent,
        ConceptoUpdateComponent,
        ConceptoDeleteDialogComponent,
        ConceptoDeletePopupComponent,
        ConceptoUpdatePopupComponent
    ],
    entryComponents: [ ConceptoComponent,
        ConceptoUpdateComponent,
        ConceptoDeleteDialogComponent,
        ConceptoDeletePopupComponent,
        ConceptoUpdatePopupComponent ],
    schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class DesgloseConceptoModule {
}
