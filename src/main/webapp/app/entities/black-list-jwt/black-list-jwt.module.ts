import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DesgloseSharedModule } from 'app/shared';
import {
    BlackListJwtComponent,
    BlackListJwtDetailComponent,
    BlackListJwtUpdateComponent,
    BlackListJwtDeletePopupComponent,
    BlackListJwtDeleteDialogComponent,
    blackListJwtRoute,
    blackListJwtPopupRoute
} from './';

const ENTITY_STATES = [...blackListJwtRoute, ...blackListJwtPopupRoute];

@NgModule({
    imports: [DesgloseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BlackListJwtComponent,
        BlackListJwtDetailComponent,
        BlackListJwtUpdateComponent,
        BlackListJwtDeleteDialogComponent,
        BlackListJwtDeletePopupComponent
    ],
    entryComponents: [
        BlackListJwtComponent,
        BlackListJwtUpdateComponent,
        BlackListJwtDeleteDialogComponent,
        BlackListJwtDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseBlackListJwtModule {}
