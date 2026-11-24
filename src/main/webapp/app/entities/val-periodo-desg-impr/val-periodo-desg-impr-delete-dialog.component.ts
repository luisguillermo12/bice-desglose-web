import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IValPeriodoDesgImpr } from 'app/shared/model/val-periodo-desg-impr.model';
import { ValPeriodoDesgImprService } from './val-periodo-desg-impr.service';

@Component({
    selector: 'jhi-val-periodo-desg-impr-delete-dialog',
    templateUrl: './val-periodo-desg-impr-delete-dialog.component.html'
})
export class ValPeriodoDesgImprDeleteDialogComponent {
    valPeriodoDesgImpr: IValPeriodoDesgImpr;

    constructor(
        private valPeriodoDesgImprService: ValPeriodoDesgImprService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.valPeriodoDesgImprService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'valPeriodoDesgImprListModification',
                content: 'Deleted an valPeriodoDesgImpr'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-val-periodo-desg-impr-delete-popup',
    template: ''
})
export class ValPeriodoDesgImprDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ valPeriodoDesgImpr }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ValPeriodoDesgImprDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.valPeriodoDesgImpr = valPeriodoDesgImpr;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
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
