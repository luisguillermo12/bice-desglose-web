import './vendor.ts';

import { Injector, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { LocalStorageService, Ng2Webstorage, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { DesgloseSharedModule } from 'app/shared';
import { DesgloseCoreModule } from 'app/core';
import { DesgloseAppRoutingModule } from './app-routing.module';
import { DesgloseHomeModule } from './home/home.module';
import { DesgloseAccountModule } from './account/account.module';
import { DesgloseEntityModule } from './entities/entity.module';
import * as moment from 'moment';
import { DesgloseAppBreakdownModule } from './breakdown/breakdown.module';
import { DesgloseAppQueryModule } from './query/query.module';
import { DesgloseMaintenanceModule } from './maintenance/maintenance.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent } from './layouts';
import { DesgloseAppRevisionModule } from 'app/revision';
import { LoaderInterceptor } from 'app/blocks/interceptor/loader.interceptor';
import { LoaderService } from 'app/core/load/loader.service';
import { NgxLoadingModule } from 'ngx-loading';
import { ProjectionsModule } from 'app/projections/projections.module';
import { ModalConfirmComponent } from 'app/layouts/modals/modal-confirm.component';
import { CloseSessionGuard } from 'app/shared/guards/close-session-guard';
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';
import { MomentModule } from 'angular2-moment';
@NgModule({
    imports: [
        BrowserModule,
        DesgloseAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        DesgloseSharedModule,
        DesgloseCoreModule,
        DesgloseHomeModule,
        DesgloseAccountModule,
        DesgloseEntityModule,
        DesgloseAppBreakdownModule,
        ProjectionsModule,
        DesgloseAppQueryModule,
        DesgloseMaintenanceModule,
        DesgloseAppRevisionModule,
        NgxLoadingModule.forRoot({}),
        // UserIdleModule.forRoot({ idle: USER_SESSION_IDLE_TIME, timeout: USER_SESSION_TIMEOUT_TIME, ping: USER_SESSION_PING_TIME }),
        MomentModule,
        NgIdleKeepaliveModule.forRoot()
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        ModalConfirmComponent
    ],
    entryComponents: [
        ModalConfirmComponent
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [ LocalStorageService, SessionStorageService ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [ Injector ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [ JhiEventManager ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [ Injector ]
        },
        LoaderService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: LoaderInterceptor,
            multi: true
        },
        CloseSessionGuard
    ],
    bootstrap: [ JhiMainComponent ]
})
export class DesgloseAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
