import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseFirma } from 'app/shared/model/desglose-firma.model';
import { DesgloseFirmaService } from './desglose-firma.service';

@Component({
    selector: 'jhi-desglose-firma-delete-dialog',
    templateUrl: './desglose-firma-delete-dialog.component.html'
})
export class DesgloseFirmaDeleteDialogComponent {
    desgloseFirma: IDesgloseFirma;

    constructor(
        private desgloseFirmaService: DesgloseFirmaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseFirmaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseFirmaListModification',
                content: 'Deleted an desgloseFirma'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-firma-delete-popup',
    template: ''
})
export class DesgloseFirmaDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseFirma }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseFirmaDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseFirma = desgloseFirma;
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
