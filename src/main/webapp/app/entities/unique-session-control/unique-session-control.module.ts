import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    UniqueSessionControlComponent,
    UniqueSessionControlDeleteDialogComponent,
    UniqueSessionControlDeletePopupComponent,
    UniqueSessionControlDetailComponent,
    uniqueSessionControlPopupRoute,
    uniqueSessionControlRoute,
    UniqueSessionControlUpdateComponent
} from './';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

const ENTITY_STATES = [ ...uniqueSessionControlRoute, ...uniqueSessionControlPopupRoute ];

@NgModule({
    imports: [
        DesgloseSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        NgxDatatableModule
    ],
    declarations: [
        UniqueSessionControlComponent,
        UniqueSessionControlDetailComponent,
        UniqueSessionControlUpdateComponent,
        UniqueSessionControlDeleteDialogComponent,
        UniqueSessionControlDeletePopupComponent
    ],
    entryComponents: [
        UniqueSessionControlComponent,
        UniqueSessionControlUpdateComponent,
        UniqueSessionControlDeleteDialogComponent,
        UniqueSessionControlDeletePopupComponent
    ],
    schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class DesgloseUniqueSessionControlModule {
}
