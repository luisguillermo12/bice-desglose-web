import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from '../../shared';
import { SharedPipesModule } from 'app/shared/pipes/shared-pipes.module';
import { MAINTENANCE_ROUTE, LogsComponent } from './';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
@NgModule({
    imports: [
        DesgloseSharedModule,
        RouterModule.forRoot([MAINTENANCE_ROUTE], { useHash: true }),
        FormsModule,
        ReactiveFormsModule,
        NgxDatatableModule,
        SharedPipesModule
    ],
    declarations: [LogsComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseAppLogsModule {}
