import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IPeriodoDesgImpresion } from 'app/shared/model/periodo-desg-impresion.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { PeriodoDesgImpresionService } from './periodo-desg-impresion.service';

@Component({
    selector: 'jhi-periodo-desg-impresion',
    templateUrl: './periodo-desg-impresion.component.html'
})
export class PeriodoDesgImpresionComponent implements OnInit, OnDestroy {
    currentAccount: any;
    periodoDesgImpresions: IPeriodoDesgImpresion[];
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
        private periodoDesgImpresionService: PeriodoDesgImpresionService,
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
        this.periodoDesgImpresionService
        .query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        })
        .subscribe(
            (res: HttpResponse<IPeriodoDesgImpresion[]>) => this.paginatePeriodoDesgImpresions(res.body, res.headers),
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
        this.router.navigate([ '/periodo-desg-impresion' ], {
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
            '/periodo-desg-impresion',
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
        this.registerChangeInPeriodoDesgImpresions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPeriodoDesgImpresion) {
        return item.codCliente;
    }

    registerChangeInPeriodoDesgImpresions() {
        this.eventSubscriber = this.eventManager.subscribe('periodoDesgImpresionListModification', response => this.loadAll());
    }

    sort() {
        const result = [ this.predicate + ',' + (this.reverse ? 'asc' : 'desc') ];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginatePeriodoDesgImpresions(data: IPeriodoDesgImpresion[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.periodoDesgImpresions = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
