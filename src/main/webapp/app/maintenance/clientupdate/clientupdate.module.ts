import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DesgloseSharedModule } from '../../shared';

import { MAINTENANCE_ROUTE, ClientupdateComponent, ClientupdateformComponent } from './';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
const ENTITY_STATES = [...MAINTENANCE_ROUTE];
@NgModule({
    imports: [DesgloseSharedModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgxDatatableModule],
    declarations: [ClientupdateComponent, ClientupdateformComponent],
    entryComponents: [ClientupdateformComponent, ClientupdateformComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseAppClientUpdateModule {}
