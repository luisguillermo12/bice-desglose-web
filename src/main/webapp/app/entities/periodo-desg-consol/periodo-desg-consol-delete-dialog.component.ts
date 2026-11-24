import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodoDesgConsol } from 'app/shared/model/periodo-desg-consol.model';
import { PeriodoDesgConsolService } from './periodo-desg-consol.service';

@Component({
    selector: 'jhi-periodo-desg-consol-delete-dialog',
    templateUrl: './periodo-desg-consol-delete-dialog.component.html'
})
export class PeriodoDesgConsolDeleteDialogComponent {
    periodoDesgConsol: IPeriodoDesgConsol;

    constructor(
        private periodoDesgConsolService: PeriodoDesgConsolService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.periodoDesgConsolService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'periodoDesgConsolListModification',
                content: 'Deleted an periodoDesgConsol'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-periodo-desg-consol-delete-popup',
    template: ''
})
export class PeriodoDesgConsolDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ periodoDesgConsol }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PeriodoDesgConsolDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.periodoDesgConsol = periodoDesgConsol;
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
