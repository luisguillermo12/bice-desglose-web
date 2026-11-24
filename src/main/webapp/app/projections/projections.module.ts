import { NgModule } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ProjectionsComponent } from './projections.component';
import { RouterModule } from '@angular/router';
import { PROJECTIONS_ROUTE } from 'app/projections/projections.route';
import { HotTableModule } from '@handsontable/angular';
import { SharedPipesModule } from 'app/shared/pipes/shared-pipes.module';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { DesgloseSharedModule } from 'app/shared';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from 'ngx-spinner';
import { ProyectionsSelectionPrintDialogComponent } from 'app/projections/projections-selection-print-dialog/proyections-selection-print-dialog.component';
@NgModule({
    imports: [
        DesgloseSharedModule,
        RouterModule.forRoot(PROJECTIONS_ROUTE, { useHash: true }),
        HotTableModule.forRoot(),
        SharedPipesModule,
        NgxDatatableModule,
        NgxSpinnerModule
    ],
    declarations: [
        ProjectionsComponent,
        ProyectionsSelectionPrintDialogComponent,
    ],
    entryComponents: [
        ProyectionsSelectionPrintDialogComponent
    ],
    providers: [
        NgbActiveModal,
        DatePipe
    ]
})
export class ProjectionsModule {
}
