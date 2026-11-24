import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DesgloseSharedModule } from '../../shared';
import { MAINTENANCE_ROUTE, TemplateDesgloComponent } from './';
import { DesgloseConceptoModule } from 'app/entities/concepto/concepto.module';

@NgModule({
    imports: [RouterModule.forRoot([MAINTENANCE_ROUTE], { useHash: true }), DesgloseConceptoModule, DesgloseSharedModule],
    declarations: [TemplateDesgloComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseTemplateDesgloModule {}
