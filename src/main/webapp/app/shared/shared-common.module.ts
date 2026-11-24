import { NgModule } from '@angular/core';

import { DesgloseSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, Sweetalert2Component } from './';

@NgModule({
    imports: [DesgloseSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, Sweetalert2Component],
    exports: [DesgloseSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, Sweetalert2Component]
})
export class DesgloseSharedCommonModule {}
