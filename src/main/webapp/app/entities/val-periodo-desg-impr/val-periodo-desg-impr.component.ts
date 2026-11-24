import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IValPeriodoDesgImpr } from 'app/shared/model/val-periodo-desg-impr.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ValPeriodoDesgImprService } from './val-periodo-desg-impr.service';

@Component({
    selector: 'jhi-val-periodo-desg-impr',
    templateUrl: './val-periodo-desg-impr.component.html'
})
export class ValPeriodoDesgImprComponent implements OnInit, OnDestroy {
    currentAccount: any;
    valPeriodoDesgImprs: IValPeriodoDesgImpr[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private valPeriodoDesgImprService: ValPeriodoDesgImprService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.valPeriodoDesgImprService
        .query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        })
        .subscribe(
            (res: HttpResponse<IValPeriodoDesgImpr[]>) => this.paginateValPeriodoDesgImprs(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate([ '/val-periodo-desg-impr' ], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/val-periodo-desg-impr',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInValPeriodoDesgImprs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IValPeriodoDesgImpr) {
        return item.codCliente;
    }

    registerChangeInValPeriodoDesgImprs() {
        this.eventSubscriber = this.eventManager.subscribe('valPeriodoDesgImprListModification', response => this.loadAll());
    }

    sort() {
        const result = [ this.predicate + ',' + (this.reverse ? 'asc' : 'desc') ];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateValPeriodoDesgImprs(data: IValPeriodoDesgImpr[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.valPeriodoDesgImprs = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
