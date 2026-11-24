import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';
import { DesgloseMensajeService } from './desglose-mensaje.service';

@Component({
    selector: 'jhi-desglose-mensaje-delete-dialog',
    templateUrl: './desglose-mensaje-delete-dialog.component.html'
})
export class DesgloseMensajeDeleteDialogComponent {
    desgloseMensaje: IDesgloseMensaje;

    constructor(
        private desgloseMensajeService: DesgloseMensajeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseMensajeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseMensajeListModification',
                content: 'Deleted an desgloseMensaje'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-mensaje-delete-popup',
    template: ''
})
export class DesgloseMensajeDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseMensaje }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseMensajeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseMensaje = desgloseMensaje;
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
