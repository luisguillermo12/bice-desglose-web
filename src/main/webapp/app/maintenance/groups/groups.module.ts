import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DesgloseSharedModule } from '../../shared';
import { MAINTENANCE_ROUTE, GroupsComponent } from './';
import { TreeModule } from 'angular-tree-component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
@NgModule({
    imports: [DesgloseSharedModule, NgxDatatableModule, RouterModule.forRoot([MAINTENANCE_ROUTE], { useHash: true }), TreeModule.forRoot()],
    declarations: [GroupsComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseAppGroupsModule {}
