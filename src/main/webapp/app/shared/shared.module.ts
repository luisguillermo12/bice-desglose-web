import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { DesgloseSharedCommonModule, DesgloseSharedLibsModule, HasAnyAuthorityDirective, JhiLoginModalComponent } from './';
import { DemoComponentComponent } from 'app/shared/demo-component/demo-component.component';
import { SweetAlert2Module } from '@toverux/ngx-sweetalert2';
import { CustomDateParserFormatter } from 'app/shared/util/custom-date-parser-formatter';
import { DigitOnlyDirective } from 'app/shared/directives/numbers-only.directive';
import { ThreeDigitDecimalNumberDirective } from 'app/shared/directives/three-digit-decimal-number.directive';
import { CloseSessionGuard } from 'app/shared/guards/close-session-guard';

@NgModule({
    imports: [
        DesgloseSharedLibsModule,
        DesgloseSharedCommonModule,
        SweetAlert2Module.forRoot({
            buttonsStyling: false,
            customClass: 'modal-content',
            confirmButtonClass: 'btn btn-primary',
            cancelButtonClass: 'btn'
        })
    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DigitOnlyDirective,
        ThreeDigitDecimalNumberDirective,
        DemoComponentComponent
    ],
    providers: [
        CloseSessionGuard,
        {
            provide: NgbDateAdapter,
            useClass: NgbDateMomentAdapter
        },
        {
            provide: NgbDateParserFormatter,
            useClass: CustomDateParserFormatter
        }
    ],
    entryComponents: [ JhiLoginModalComponent ],
    exports: [
        DesgloseSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DemoComponentComponent,
        DigitOnlyDirective,
        ThreeDigitDecimalNumberDirective
    ],
    schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class DesgloseSharedModule {
}
