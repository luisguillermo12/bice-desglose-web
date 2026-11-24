import { AfterViewInit, Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, LoginService, Principal } from 'app/core';
import { Router } from '@angular/router';
import { EVENT_NAME_AUTHENTICATION_SUCCESS } from 'app/app.constants';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [ 'home.scss' ]
})
export class HomeComponent implements OnInit, AfterViewInit {
    account: Account;
    modalRef: NgbModalRef;

    data: any[] = [];
    selectedCellValue: any;

    constructor(private principal: Principal, private loginModalService: LoginModalService, private eventManager: JhiEventManager, private loginService: LoginService,
                private router: Router) {
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        // this.data = handsontableData.getFinanceData();
    }

    ngAfterViewInit(): void {
        this.hideEvaluationMessage();
    }

    public unregisterAuthentication() {
        this.eventManager.subscribe('logoutSuccess', message => {
            this.router.navigate([ '' ]);
        });
    }

    /**
     * Ocultar/eliminar información de licencia del componente.
     */
    private hideEvaluationMessage() {
        /*const divElement: HTMLElement = document.getElementById('hot-display-license-info');
        if (!isNullOrUndefined(divElement)) {
            divElement.remove();
        }*/
        // divElement.hidden = true;
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe(EVENT_NAME_AUTHENTICATION_SUCCESS, message => {
            this.principal.identity().then(account => {
                this.account = account;
                this.hideEvaluationMessage();
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.loginService.logout();
        this.router.navigate([ 'dispatch' ]);
    }

    /*afterSelectionMethod(preventScrolling: any, r: number, c: number, r2: number, c2: number, selectionLayerLevel: number) {
        const spanContainer: HTMLSpanElement = document.getElementById('selectedCellValueContainerId');
        if (r === r2 && c === c2) {
            console.log('preventScrolling.getDataAtCell(' + r + ', ' + c + ') ->' + preventScrolling.getDataAtCell(r, c));
            const dataAtCell = preventScrolling.getSourceDataAtCell(r, c);
            console.log('preventScrolling.getSourceDataAtCell(' + r + ', ' + c + ') ->' + dataAtCell);
            const span: HTMLSpanElement = document.getElementById('selectedCellValueId');
            span.textContent = dataAtCell;
            spanContainer.hidden = false;
        } else {
            console.log('Selección múltiple...');
            spanContainer.hidden = true;
        }
    }*/
}
