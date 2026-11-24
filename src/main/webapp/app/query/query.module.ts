import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from '../shared';

import { QUERY_ROUTE, QueryComponent } from './';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { DesgloseAppBreakdownModule } from 'app/breakdown/breakdown.module';
@NgModule({
    imports: [DesgloseSharedModule, DesgloseAppBreakdownModule, RouterModule.forRoot([QUERY_ROUTE], { useHash: true }), NgxDatatableModule],
    declarations: [QueryComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseAppQueryModule {}
