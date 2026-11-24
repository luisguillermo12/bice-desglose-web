import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodoDesgImpresion } from 'app/shared/model/periodo-desg-impresion.model';
import { PeriodoDesgImpresionService } from './periodo-desg-impresion.service';

@Component({
    selector: 'jhi-periodo-desg-impresion-delete-dialog',
    templateUrl: './periodo-desg-impresion-delete-dialog.component.html'
})
export class PeriodoDesgImpresionDeleteDialogComponent {
    periodoDesgImpresion: IPeriodoDesgImpresion;

    constructor(
        private periodoDesgImpresionService: PeriodoDesgImpresionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.periodoDesgImpresionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'periodoDesgImpresionListModification',
                content: 'Deleted an periodoDesgImpresion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-periodo-desg-impresion-delete-popup',
    template: ''
})
export class PeriodoDesgImpresionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ periodoDesgImpresion }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PeriodoDesgImpresionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.periodoDesgImpresion = periodoDesgImpresion;
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
