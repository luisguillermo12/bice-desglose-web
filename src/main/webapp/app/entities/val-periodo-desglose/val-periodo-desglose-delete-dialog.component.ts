import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IValPeriodoDesglose } from 'app/shared/model/val-periodo-desglose.model';
import { ValPeriodoDesgloseService } from './val-periodo-desglose.service';

@Component({
    selector: 'jhi-val-periodo-desglose-delete-dialog',
    templateUrl: './val-periodo-desglose-delete-dialog.component.html'
})
export class ValPeriodoDesgloseDeleteDialogComponent {
    valPeriodoDesglose: IValPeriodoDesglose;

    constructor(
        private valPeriodoDesgloseService: ValPeriodoDesgloseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.valPeriodoDesgloseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'valPeriodoDesgloseListModification',
                content: 'Deleted an valPeriodoDesglose'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-val-periodo-desglose-delete-popup',
    template: ''
})
export class ValPeriodoDesgloseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ valPeriodoDesglose }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ValPeriodoDesgloseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.valPeriodoDesglose = valPeriodoDesglose;
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
