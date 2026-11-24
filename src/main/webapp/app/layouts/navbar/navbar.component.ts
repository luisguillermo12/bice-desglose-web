import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';

import {
    DEBUG_INFO_ENABLED,
    EVENT_NAME_AUTHENTICATION_SUCCESS,
    SESSION_REEMPLAZO_AUTENTICATE,
    SESSION_REEMPLAZO_MENSAJE,
    SESSION_REEMPLAZO_PARAMS, SESSION_VARIABLE_CLOSE, SESSION_VARIABLE_EN_DESGLOSE,
    USER_ROLE_PLAT_DESGLOSE,
    USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR,
    USER_ROLE_PLAT_DESGLOSE_EJECUTOR,
    USER_ROLE_PLAT_DESGLOSE_JEFE,
    USER_ROLE_PLAT_DESGLOSE_REVISOR, USER_SESSION_AUTO_LOGOUT,
    USER_SESSION_IDLE_TIME,
    USER_SESSION_PING_TIME,
    USER_SESSION_TIMEOUT_TIME,
    VERSION
} from 'app/app.constants';
import { Account, JhiLanguageHelper, LoginModalService, LoginService, Principal } from 'app/core';
import { ProfileService } from '../profiles/profile.service';
import { SessionStorageService } from 'ngx-webstorage';
import { Sweetalert2Component } from 'app/shared';
import { isNullOrUndefined } from 'util';
import {  Observable, Subscription } from 'rxjs';
import { Keepalive } from '@ng-idle/keepalive';
import { DEFAULT_INTERRUPTSOURCES, Idle, LocalStorage } from '@ng-idle/core';
import { ModalConfirmComponent } from 'app/layouts/modals/modal-confirm.component';
import { LoaderService } from 'app/core/load/loader.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ReportService } from 'app/shared/services/report.service';
import { DomSanitizer } from '@angular/platform-browser';
import { IDesgloseFirma } from 'app/shared/model/desglose-firma.model';
import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';
import { DesgloseFirmaService } from 'app/entities/desglose-firma';
import { DesgloseGrupoService } from 'app/entities/desglose-grupo';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: [ 'navbar.scss' ]
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    hideExtraMenus: boolean;
    ALL_ROLES = [ USER_ROLE_PLAT_DESGLOSE, USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR ];
    ROLE_JEFE_ROLE_REVISOR_ROLE_ADMIN = [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ];
    ROLE_PLAT_DESGLOSE_ADMINISTRADOR = [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR ];
    USER_ROLE_PLAT_DESGLOSE_JEFE = USER_ROLE_PLAT_DESGLOSE_JEFE;
    USER_ROLE_PLAT_DESGLOSE_REVISOR = USER_ROLE_PLAT_DESGLOSE_REVISOR;
    USER_ROLE_PLAT_DESGLOSE_EJECUTOR = USER_ROLE_PLAT_DESGLOSE_EJECUTOR;
    USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR = USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR;
    DEBUG_INFO_ENABLED = DEBUG_INFO_ENABLED;
    USER_SESSION_AUTO_LOGOUT = USER_SESSION_AUTO_LOGOUT;
    isReplacement = false;
    private subscriptionIdle: Subscription;
    private subscriptionIdleWarning: Subscription;
    private ngbModalRefConfirmComponent: NgbModalRef;
    private cancelLogoutTimeout = false;
    lastPing ?: Date = null;
    isActivo = false;
    isClosing = false;
    fileUrl;
    role: string;
    account: Account;
    associatedReviewer: IDesgloseFirma[] = [];
    associatedReviewerOne: IDesgloseGrupo;
    reviewerName = '';
    hasReviewerName = false;
    constructor(private localStorage:LocalStorage,private sanitizer: DomSanitizer,private reportService: ReportService,private loaderService: LoaderService,private loginService: LoginService,
                private sweetalert2Component: Sweetalert2Component, private languageService: JhiLanguageService, private modalService: NgbModal,
                private languageHelper: JhiLanguageHelper, private principal: Principal, private loginModalService: LoginModalService, private profileService: ProfileService,
                private router: Router, private sessionStorage: SessionStorageService, private idle: Idle, private keepalive: Keepalive, private eventManager: JhiEventManager,
                public activeModal: NgbActiveModal, private desgloseFirmaService: DesgloseFirmaService, private desgloseGrupoService: DesgloseGrupoService) {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        this.hideExtraMenus = true;
    }

    ngOnInit() {
        this.registerAuthenticationSuccess();
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });

        this.profileService.getProfileInfo().then(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
        if (this.isAuthenticated()) {
            this.initUserIdleComponent();
        }
        this.privilegiosUsuario();

    }

    private privilegiosUsuario() {
        this.reviewerName= '';
        if(isNullOrUndefined(this.isReplacement)) {
            this.isReplacement = false;
        }
        this.principal.identity().then(account => {
            if (!isNullOrUndefined(account)) {
                this.account = account;
                this.getUserAssociatedReviewer();
                if (this.account.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_EJECUTOR).length > 0) {
                    this.role = 'EJECUTOR';

                }
                if (this.account.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_REVISOR).length > 0) {
                    this.role = 'REVISOR';
                }
                if (this.account.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_JEFE).length > 0) {
                    this.role = 'JEFE';
                }

            }
        });
    }
    openFullscreen() {
        const elem:any =document.documentElement;
        if (elem.webkitRequestFullscreen) { /* Chrome, Safari & Opera */
            elem.webkitRequestFullscreen();
        } else if (elem.mozRequestFullScreen) { /* Firefox */
            elem.mozRequestFullScreen();
        } else if (elem.msRequestFullscreen) { /* IE/Edge */
            elem.msRequestFullscreen();
        }
    }
    getUserAssociatedReviewer() {
        if (!isNullOrUndefined(this.account) && !this.hasReviewerName) {
            if (!isNullOrUndefined(this.account.authorities)) {
                this.principal.hasAuthority(USER_ROLE_PLAT_DESGLOSE_REVISOR).then(result => {
                    if (result) {
                        this.desgloseFirmaService.empleadoAsociadoFirma(this.account.stfOperatorCode).subscribe(
                            (rpt: HttpResponse<IDesgloseFirma[]>) => {
                                this.associatedReviewer = rpt.body;
                                this.hasReviewerName = !isNullOrUndefined(this.associatedReviewer) && isNullOrUndefined(this.associatedReviewer.length > 0);
                                if (this.hasReviewerName) {
                                    this.associatedReviewer.forEach(el => {
                                        this.reviewerName = ' ' + el.numRolEmpleado;
                                    });
                                }
                            },
                        );
                    } else {
                        this.principal.hasAuthority(USER_ROLE_PLAT_DESGLOSE_EJECUTOR).then(res => {
                            if (res) {
                                this.desgloseGrupoService.findRoleEjecutor(this.account.stfOperatorCode).subscribe(
                                    (rpt: HttpResponse<IDesgloseGrupo>) => {
                                        this.associatedReviewerOne = rpt.body;
                                        this.hasReviewerName = !isNullOrUndefined(this.associatedReviewerOne) && !isNullOrUndefined(this.associatedReviewerOne.glsNumRolEmpleado);
                                        if (this.hasReviewerName) {
                                            this.reviewerName = ' ' + this.associatedReviewerOne.glsNumRolEmpleado.toUpperCase();
                                        }
                                    },
                                );

                            }
                        });
                    }
                });
                // associatedReviewer += ' ' + this.translateService.instant('footer.footerUserRole');
            }
        }
    }
    private onInactiveTimeStart(count) {
        this.isActivo = false;
        try {
            if (this.isAuthenticated()) {
                if (!this.cancelLogoutTimeout) {
                    if (isNullOrUndefined(this.ngbModalRefConfirmComponent)) {
                        this.ngbModalRefConfirmComponent = this.modalService.open(ModalConfirmComponent as Component, {
                            windowClass: 'inactive-time-modal',
                            // size: 'lg',
                            backdrop: 'static'
                        });
                        this.ngbModalRefConfirmComponent.componentInstance.translateValues = { countdown: count };

                        this.ngbModalRefConfirmComponent.componentInstance.onOkEmitter.subscribe(() => {
                            this.cancelLogoutTimeout = true;
                            this.isActivo = true;
                            this.ngbModalRefConfirmComponent.close();
                            this.reset(false, false,true);
                            this.initUserIdleComponent();
                        });
                        this.ngbModalRefConfirmComponent.componentInstance.onCancelEmitter.subscribe(() => {
                            this.cancelLogoutTimeout = true;
                            this.ngbModalRefConfirmComponent.close();
                            this.router.navigate([ '' ]);
                            this.reset(true, true,true);
                        });
                        this.ngbModalRefConfirmComponent.result.then(
                            () => {
                                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                                this.ngbModalRefConfirmComponent = null;
                            },
                            () => {
                                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                                this.ngbModalRefConfirmComponent = null;
                            }
                        );
                    } else {
                        if (!isNullOrUndefined(this.ngbModalRefConfirmComponent)) {
                            let componentInstance = null;
                            try {
                                if (!isNullOrUndefined(this.ngbModalRefConfirmComponent.componentInstance)) {
                                    componentInstance = this.ngbModalRefConfirmComponent.componentInstance;
                                    componentInstance.translateValues = { countdown: count };
                                }
                            } catch (e) {
                                if (DEBUG_INFO_ENABLED) {
                                    console.log(e);
                                }
                            }
                        }
                    }
                    // this.sweetalert2Component.mensajeWarning('info.title', 'info.sessionWillBeClosed', { countdown: count });
                } else {
                    console.log('aqui cancelLogoutTimeout -> ' + this.cancelLogoutTimeout);
                    // this.reset(false, false);
                }
            }
        } catch (e) {
            if (DEBUG_INFO_ENABLED) {
                console.log(e);
            }
        }
    }

    private initUserIdleComponent() {
        // idle.setIdle(5); // sets an idle timeout of 5 seconds, for testing purposes.
        this.idle.setIdle(USER_SESSION_IDLE_TIME);
        // idle.setTimeout(5); // sets a timeout period of 5 seconds. after 10 seconds of inactivity, the user will be considered timed out.
        this.idle.setTimeout(USER_SESSION_TIMEOUT_TIME);
        // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
        // let DEFAULT_INTERRUPT_SOURCES = [ new DocumentInterruptSource(Constants.USER_SESSION_DEFAULT_INTERRUPT_SOURCES), new StorageInterruptSource() ];
        this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
        // idle.onIdleEnd.subscribe(() => console.log('No longer idle.'));
       this.subscriptionIdle = this.idle.onTimeout.subscribe(() => {
           if(!this.isActivo) {
               this.router.navigate([ '' ]);
               this.reset(true, true,false);
           }
        });
        this.subscriptionIdleWarning = this.idle.onTimeoutWarning.subscribe(countdown => {
            this.onInactiveTimeStart(countdown);
        });
        // sets the ping interval to 15 seconds
        this.keepalive.interval(USER_SESSION_PING_TIME);
        this.keepalive.onPing.subscribe (() => {
            this.lastPing = new Date ();
            console.log(this.lastPing);
            this.loginService.udpdateLogin(this.account.login);

        });
        this.idle.watch();
    }
    @HostListener('window:beforeunload', ['$event'])
    canDeactivate(event) {
        if (this.isAuthenticated()) {
            if(this.router.url==='/breakdown') {
                return false;
            }
        }
        return  true;
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavBar() {
        this.isNavbarCollapsed = true;
    }

    showWarningMessage(collapse?: boolean) {
        if (!isNullOrUndefined(collapse) && collapse) {
            this.collapseNavBar();
        }
        this.sweetalert2Component.mensajeWarning('error.blankTitle', this.sessionStorage.retrieve(SESSION_REEMPLAZO_MENSAJE),
            { errorDetail: this.sessionStorage.retrieve(SESSION_REEMPLAZO_PARAMS) });
    }

    isAuthenticated() {
        this.isReplacement =  Boolean(JSON.parse(this.localStorage.getItem(SESSION_REEMPLAZO_AUTENTICATE)));
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavBar();
        if(this.router.url==='/breakdown') {
            this.sessionStorage.store(SESSION_VARIABLE_CLOSE, 1);
            setTimeout(() => {
                this.router.navigate([ '' ]);
                this.reset(false, false, false);
            }, 2000);
        } else {
            this.loginService.logout();
        }
    }
    toggleNavBar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }
    reset(showMessage: boolean, logout: boolean, isRapid:boolean) {
        this.idle.stop();
        this.activeModal.dismiss(true);
        this.subscriptionIdle.unsubscribe();
        this.subscriptionIdleWarning.unsubscribe();
        this.cancelLogoutTimeout = false;
        if (logout) {
            if (this.ngbModalRefConfirmComponent) {
                this.ngbModalRefConfirmComponent.close();
            }
            this.logout();
        }
    }
    public getUserFullName(): string {
        let fullNameAndRole = '';
        if (!isNullOrUndefined(this.account)) {
            fullNameAndRole = this.account.firstName + ' ' + this.account.lastName;
        }
        return fullNameAndRole;
    }
    registerAuthenticationSuccess() {
        this.eventManager.subscribe(EVENT_NAME_AUTHENTICATION_SUCCESS, message => {
            this.principal.identity().then(account => {
                this.initUserIdleComponent();
                this.privilegiosUsuario();
                this.isReplacement =  Boolean(JSON.parse(this.localStorage.getItem(SESSION_REEMPLAZO_AUTENTICATE)));
            });
        });
    }
}
