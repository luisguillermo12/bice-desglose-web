import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from '../../shared';

import { MAINTENANCE_ROUTE, ChargesComponent } from './';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';

@NgModule({
    imports: [DesgloseSharedModule, NgxDatatableModule, RouterModule.forRoot([MAINTENANCE_ROUTE], { useHash: true })],
    declarations: [ChargesComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseAppChargesModule {}
