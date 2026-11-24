import { AfterViewInit, Component, ElementRef, Renderer } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { SessionStorageService } from 'ngx-webstorage';
import {DesgloseRevisionService} from 'app/entities/desglose-revision';
import {
    EVENT_NAME_AUTHENTICATION_SUCCESS,
    SESSION_REEMPLAZO_AUTENTICATE,
    SESSION_REEMPLAZO_MENSAJE,
    SESSION_REEMPLAZO_PARAMS,
    SESSION_VARIABLE_USUARIO,
    SESSION_VARIABLE_USUARIO_NAME
} from 'app/app.constants';
import { TurbineUserService } from 'app/entities/turbine-user';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ITurbineUser } from 'app/shared/model/turbine-user.model';
import { Sweetalert2Component } from 'app/shared';
import { isNullOrUndefined } from 'util';
import { Principal } from 'app/core';
import { LocalStorage } from '@ng-idle/core';

@Component({
    selector: 'jhi-login-modal',
    templateUrl: './login.component.html',
    styleUrls: [ './login.component.scss' ]
})
export class JhiLoginModalComponent implements AfterViewInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;
    loginErrorMessage = 'login.messages.error.authentication';
    messageParams: any = {};
    private ngbModalRefConfirmComponent: NgbModalRef;
    private cancelLogoutTimeout = false;

    constructor(private eventManager: JhiEventManager, private loginService: LoginService, private stateStorageService: StateStorageService, private elementRef: ElementRef,
                private renderer: Renderer, private router: Router, public activeModal: NgbActiveModal,private localStorage:LocalStorage, private sessionStorage: SessionStorageService,
                private turbineUserService: TurbineUserService, private jhiAlertService: JhiAlertService, private sweetAlert2: Sweetalert2Component, private principal: Principal , private desgloseRevisionService: DesgloseRevisionService) {

        this.credentials = {};
        this.rememberMe = true;
    }

    ngAfterViewInit() {
        setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []), 0);
    }

    cancel() {
        this.credentials = {
            username: null,
            password: null,
            rememberMe: true
        };
        this.authenticationError = false;
        this.activeModal.dismiss('cancel');
    }

    login() {
        this.loginService.login({
            username: this.username,
            password: this.password,
            rememberMe: this.rememberMe
        })
        .then(() => {
            this.authenticationError = false;
            this.activeModal.dismiss('login success');
            if (this.router.url === '/register' || /^\/activate\//.test(this.router.url) || /^\/reset\//.test(this.router.url)) {
                this.router.navigate([ '' ]);
            }

            this.eventManager.broadcast({
                name: EVENT_NAME_AUTHENTICATION_SUCCESS,
                content: 'Sending Authentication Success'
            });
            // previousState was set in the authExpiredInterceptor before being redirected to login modal.
            // since login is succesful, go to stored previousState and clear previousState
            const redirect = this.stateStorageService.getUrl();
            if (redirect) {
                this.stateStorageService.storeUrl(null);
                this.router.navigate([ redirect ]);
            }
            this.loadUser(this.username);
            this.indicadorNotificaciones();
        })
        .catch(e => {
            if (!isNullOrUndefined(e) && !isNullOrUndefined(e.error) && !isNullOrUndefined(e.error.message)) {
                this.loginErrorMessage = e.error.message;
                if (!isNullOrUndefined(e.error.params)) {
                    this.messageParams = JSON.parse(e.error.params);
                }
            } else {
                this.loginErrorMessage = 'login.messages.error.authentication';
            }
            this.authenticationError = true;
        });
    }
    indicadorNotificaciones() {
        this.desgloseRevisionService.indicadorNotificaciones().subscribe(
            (data: HttpResponse<number>) => {
             if (data.body > 0) {
                    this.sweetAlert2.mensajeWarning('login.notification.title', 'login.notification.textnotification',{desglosespen:data.body});
                }
            },
        );
    }

    register() {
        this.activeModal.dismiss('to state register');
        this.router.navigate([ '/register' ]);
    }

    requestResetPassword() {
        this.activeModal.dismiss('to state requestReset');
        this.router.navigate([ '/reset', 'request' ]);
    }

    private loadUser(loginName: string) {
        this.turbineUserService.findByLoginNameSfStf(loginName).subscribe(
            (data: HttpResponse<ITurbineUser>) => {
                this.sessionStorage.store(SESSION_VARIABLE_USUARIO, data.body);
                this.localStorage.setItem(SESSION_REEMPLAZO_AUTENTICATE, 'false');
                this.localStorage.setItem(SESSION_VARIABLE_USUARIO_NAME, data.body.loginName);
                // this.initUserIdleComponent();
            },
            (data: HttpErrorResponse) => {
                this.authenticationError = false;
                this.sweetAlert2.mensajeWarning('error.blankTitle', data.error.message, { errorDetail: data.error.params });
                this.localStorage.setItem(SESSION_REEMPLAZO_AUTENTICATE, 'true');
                this.sessionStorage.store(SESSION_REEMPLAZO_MENSAJE, data.error.message);
                this.sessionStorage.store(SESSION_REEMPLAZO_PARAMS, data.error.params);
            }
        );
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }
}
