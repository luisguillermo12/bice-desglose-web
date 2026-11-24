import { Component, OnInit } from '@angular/core';
import { Account, Principal } from 'app/core';
import { JhiEventManager } from 'ng-jhipster';
import { isNullOrUndefined } from 'util';
import { TranslateService } from '@ngx-translate/core';
import { DesgloseFirmaService } from 'app/entities/desglose-firma';
import { EVENT_NAME_AUTHENTICATION_SUCCESS, USER_ROLE_PLAT_DESGLOSE_EJECUTOR, USER_ROLE_PLAT_DESGLOSE_REVISOR, VERSION } from 'app/app.constants';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IDesgloseFirma } from 'app/shared/model/desglose-firma.model';
import { DesgloseGrupoService } from 'app/entities/desglose-grupo';
import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';
import { SessionStorageService } from 'ngx-webstorage';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html',
    styleUrls: [ './footer.component.scss' ]
})
export class FooterComponent implements OnInit {

    account: Account;
    associatedReviewer: IDesgloseFirma[] = [];
    associatedReviewerOne: IDesgloseGrupo;
    reviewerName = '';
    hasReviewerName = false;
    version: string;

    constructor(private principal: Principal, private eventManager: JhiEventManager, private translateService: TranslateService, private router: Router,
                private desgloseFirmaService: DesgloseFirmaService, private desgloseGrupoService: DesgloseGrupoService, private sessionStorage: SessionStorageService) {
        this.principal.identity().then(account => {
            this.account = account;
        });

    }

    ngOnInit() {
        this.version = VERSION ? 'v' + VERSION : '';
        this.registerAuthenticationSuccess();
        this.unregisterAuthentication();
        this.principal.identity().then(account => {
            if (!isNullOrUndefined(account)) {
                this.account = account;
                this.getUserAssociatedReviewer();
            }
        });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe(EVENT_NAME_AUTHENTICATION_SUCCESS, message => {
            this.principal.identity().then(account => {
                this.account = account;
                this.getUserAssociatedReviewer();
            });
        });
    }

    unregisterAuthentication() {
        this.eventManager.subscribe('logoutSuccess', message => {
            this.hasReviewerName = false;
            this.reviewerName = '';
            this.associatedReviewerOne = null;
            this.associatedReviewer = [];
            this.router.navigate([ '' ]);
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    public getUserFullName(): string {
        let fullNameAndRole = '';
        if (!isNullOrUndefined(this.account)) {
            fullNameAndRole = this.account.firstName + ' ' + this.account.lastName;
        }
        return fullNameAndRole;
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
                            (rpt: HttpErrorResponse) => this.onError(rpt.message)
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
                                    (rpt: HttpErrorResponse) => this.onError(rpt.message)
                                );

                            }
                        });
                    }
                });
                // associatedReviewer += ' ' + this.translateService.instant('footer.footerUserRole');
            }
        }
    }

    private onError(errorMessage: string) {
    }
}
