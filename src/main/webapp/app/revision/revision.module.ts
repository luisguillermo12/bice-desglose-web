import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from '../shared';

import { REVISION_ROUTE, RevisionComponent } from './';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { DesgloseAppBreakdownModule } from 'app/breakdown/breakdown.module';
@NgModule({
    imports: [
        NgxDatatableModule,
        DesgloseAppBreakdownModule,
        DesgloseSharedModule,
        RouterModule.forRoot([REVISION_ROUTE], { useHash: true })
    ],
    declarations: [RevisionComponent],
    entryComponents: [],
    exports: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseAppRevisionModule {}
