import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    DesgloseFirmaComponent,
    DesgloseFirmaDetailComponent,
    DesgloseFirmaUpdateComponent,
    DesgloseFirmaDeletePopupComponent,
    DesgloseFirmaDeleteDialogComponent,
    desgloseFirmaRoute,
    desgloseFirmaPopupRoute
} from './';

const ENTITY_STATES = [...desgloseFirmaRoute, ...desgloseFirmaPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesgloseFirmaComponent,
        DesgloseFirmaDetailComponent,
        DesgloseFirmaUpdateComponent,
        DesgloseFirmaDeleteDialogComponent,
        DesgloseFirmaDeletePopupComponent
    ],
    entryComponents: [
        DesgloseFirmaComponent,
        DesgloseFirmaUpdateComponent,
        DesgloseFirmaDeleteDialogComponent,
        DesgloseFirmaDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseDesgloseFirmaModule {}
