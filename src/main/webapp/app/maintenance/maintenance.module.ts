import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DesgloseAppChargesModule } from './charges';
import { DesgloseAppClientUpdateModule } from './clientupdate';
import { DesgloseAppGroupsModule } from './groups';
import { DesgloseAppReplacementModule } from './replacement';
import { DesgloseAppReviewersModule } from './reviewers';
import { DesgloseTemplateDesgloModule } from './template-desglo';
import { DesgloseTemplateProyectModule } from 'app/maintenance/template-proyect';
import {DesgloseAppLogsModule} from 'app/maintenance/logs';
@NgModule({
    imports: [
        CommonModule,
        DesgloseAppChargesModule,
        DesgloseAppClientUpdateModule,
        DesgloseAppGroupsModule,
        DesgloseAppReplacementModule,
        DesgloseAppReviewersModule,
        DesgloseTemplateDesgloModule,
        DesgloseTemplateProyectModule,
        DesgloseAppLogsModule
    ],
    declarations: []
})
export class DesgloseMaintenanceModule {}
