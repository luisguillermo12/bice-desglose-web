import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from '../shared';

import { BREAKDOWN_ROUTE, BreakdownComponent } from './';
import { HotTableModule } from '@handsontable/angular';
import { SearchBreakdownDialogComponent } from './search-breakdown-dialog/search-breakdown-dialog.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SharedPipesModule } from 'app/shared/pipes/shared-pipes.module';
import { BreakdownClientsDialogComponent } from './breakdown-clients-dialog/breakdown-clients-dialog.component';
import { BreakdownClientsFilterTableComponent } from './breakdown-clients-filter-table/breakdown-clients-filter-table.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { BreakdownCreateClientDialogComponent } from './breakdown-create-client-dialog/breakdown-create-client-dialog.component';
import { BreakdownConsolidateCompaniesDialogComponent } from './breakdown-consolidate-companies-dialog/breakdown-consolidate-companies-dialog.component';
import { BreakdownSelectAuditorDialogComponent } from './breakdown-select-auditor-dialog/breakdown-select-auditor-dialog.component';
import { BreakdownOtherDataConfigDialogComponent } from './breakdown-other-data-config-dialog/breakdown-other-data-config-dialog.component';
import { BreakdownPrintConfigDialogComponent } from './breakdown-print-config-dialog/breakdown-print-config-dialog.component';
import { DatePipe } from '@angular/common';
import { BreakdownCloneConfigDialogComponent } from 'app/breakdown/breakdown-clone-config-dialog/breakdown-clone-config-dialog.component';
import { BreakdownGeneratePrintDialogComponent } from 'app/breakdown/breakdown-generate-print-dialog/breakdown-generate-print-dialog.component';
import { BreakdownSelectionPrintDialogComponent } from 'app/breakdown/breakdown-selection-print-dialog/breakdown-selection-print-dialog.component';
import { BreakdownErrorsDialogComponent } from './breakdown-errors-dialog/breakdown-errors-dialog.component';
import { BreakdownShippingReviewComponent } from './breakdown-shipping-review/breakdown-shipping-review.component';
import { BreakdownMessagesComponent } from './breakdown-messages/breakdown-messages.component';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { PendingChangesGuard } from 'app/shared/guards/pending-changes-guard';
import { BreakdownSearchPopupComponent } from './breakdown-search-popup/breakdown-search-popup.component';

@NgModule({
    imports: [
        BrowserModule, FormsModule,
        DesgloseSharedModule,
        RouterModule.forRoot(BREAKDOWN_ROUTE, { useHash: true }),
        HotTableModule.forRoot(),
        SharedPipesModule,
        NgxDatatableModule/*,
        NgFloatingActionMenuModule*/
    ],
    declarations: [
        BreakdownComponent,
        SearchBreakdownDialogComponent,
        BreakdownClientsDialogComponent,
        BreakdownClientsFilterTableComponent,
        BreakdownCreateClientDialogComponent,
        BreakdownConsolidateCompaniesDialogComponent,
        BreakdownSelectAuditorDialogComponent,
        BreakdownOtherDataConfigDialogComponent,
        BreakdownPrintConfigDialogComponent,
        BreakdownCloneConfigDialogComponent,
        BreakdownGeneratePrintDialogComponent,
        BreakdownSelectionPrintDialogComponent,
        BreakdownErrorsDialogComponent,
        BreakdownShippingReviewComponent,
        BreakdownMessagesComponent,
        BreakdownSearchPopupComponent
    ],
    exports: [
        SearchBreakdownDialogComponent,
        BreakdownSearchPopupComponent
    ],
    entryComponents: [
        SearchBreakdownDialogComponent,
        BreakdownClientsDialogComponent,
        BreakdownClientsFilterTableComponent,
        BreakdownCreateClientDialogComponent,
        BreakdownConsolidateCompaniesDialogComponent,
        BreakdownSelectAuditorDialogComponent,
        BreakdownOtherDataConfigDialogComponent,
        BreakdownPrintConfigDialogComponent,
        BreakdownCloneConfigDialogComponent,
        BreakdownGeneratePrintDialogComponent,
        BreakdownSelectionPrintDialogComponent,
        BreakdownErrorsDialogComponent,
        BreakdownShippingReviewComponent,
        BreakdownMessagesComponent,
        BreakdownSearchPopupComponent
    ],
    providers: [
        NgbActiveModal,
        DatePipe,
        PendingChangesGuard
    ],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA
    ]
})
export class DesgloseAppBreakdownModule {
}
