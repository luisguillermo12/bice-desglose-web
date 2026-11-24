import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IUniqueSessionControl } from 'app/shared/model/unique-session-control.model';
import { Principal } from 'app/core';

import { DATE_FORMAT_PERIOD_ES_UI_HEADER, DATE_TIME_FORMAT_ES_UI, DATE_TIME_SECONDS_FORMAT_ES_UI, ITEMS_PER_PAGE, Sweetalert2Component } from 'app/shared';
import { UniqueSessionControlService } from './unique-session-control.service';
import { ICON_CARD_CLOSE, ICON_CARD_OPEN } from 'app/app.constants';
import { IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';

@Component({
    selector: 'jhi-unique-session-control',
    templateUrl: './unique-session-control.component.html'
})
export class UniqueSessionControlComponent implements OnInit, OnDestroy {
    currentAccount: any;
    uniqueSessionControls: IUniqueSessionControl[];
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

    selected: IUniqueSessionControl[] = [];
    public isCollapsedDataPanel = false;
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;
    private deleteElementsSubscription: Subscription;
    DATE_TIME_SECONDS_FORMAT_ES_UI = DATE_TIME_SECONDS_FORMAT_ES_UI;

    constructor(private uniqueSessionControlService: UniqueSessionControlService, private parseLinks: JhiParseLinks, private jhiAlertService: JhiAlertService,
                private principal: Principal, private activatedRoute: ActivatedRoute, private router: Router, private eventManager: JhiEventManager,
                private sweetalert: Sweetalert2Component) {

        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.uniqueSessionControlService.getAll().subscribe(
            (res: HttpResponse<IUniqueSessionControl[]>) => this.allUniqueSessionControls(res.body),
            (res: HttpErrorResponse) => this.onError(res)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate([ '/unique-session-control' ], {
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
            '/unique-session-control',
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
        this.registerChangeInUniqueSessionControls();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IUniqueSessionControl) {
        return item.id;
    }

    registerChangeInUniqueSessionControls() {
        this.eventSubscriber = this.eventManager.subscribe('uniqueSessionControlListModification', response => this.loadAll());
    }

    sort() {
        const result = [ this.predicate + ',' + (this.reverse ? 'asc' : 'desc') ];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateUniqueSessionControls(data: IUniqueSessionControl[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.uniqueSessionControls = data;
    }

    private allUniqueSessionControls(data: IUniqueSessionControl[]) {
        this.queryCount = this.totalItems;
        this.uniqueSessionControls = data;
    }

    private onError(data: HttpErrorResponse) {
        let errorMessage = data.message;
        if (data.error && data.error.message) {
            errorMessage = data.error.message;
        }
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect({ selected }) {
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }

    deleteSelectedItems() {
        if (this.selected.length > 0) {
            this.deleteElementsSubscription = this.sweetalert.solicitarAccion('desgloseApp.uniqueSessionControl.delete.tittle',
                'desgloseApp.uniqueSessionControl.delete.question', 'global.form.accept', 'global.form.cancel')
            .subscribe(res => {
                if (res) {
                    const selectedItemsIds: number[] = [];
                    for (const item of this.selected) {
                        selectedItemsIds.push(item.id);
                    }
                    this.uniqueSessionControlService.deleteList(selectedItemsIds).subscribe(
                        (data: HttpResponse<IDesgloseReemplazo>) => {
                            this.loadAll();
                        },
                        (data: HttpErrorResponse) => {
                            this.onError(data);
                        }
                    );
                }
                this.deleteElementsSubscription.unsubscribe();
            });
        } else {
            this.sweetalert.mensajeWarning('desgloseApp.uniqueSessionControl.delete.tittle', 'desgloseApp.uniqueSessionControl.delete.noItemSelected');
        }
    }
}
