import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodo } from 'app/shared/model/periodo.model';
import { PeriodoService } from './periodo.service';

@Component({
    selector: 'jhi-periodo-delete-dialog',
    templateUrl: './periodo-delete-dialog.component.html'
})
export class PeriodoDeleteDialogComponent {
    periodo: IPeriodo;

    constructor(private periodoService: PeriodoService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(codCliente: string, codTipoDes: number, codSubtipoDes: number, codMoneda: string) {
        this.periodoService.delete(codCliente, '', '', codMoneda, null, '').subscribe(response => {
            this.eventManager.broadcast({
                name: 'periodoListModification',
                content: 'Deleted an periodo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-periodo-delete-popup',
    template: ''
})
export class PeriodoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ periodo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PeriodoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.periodo = periodo;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
