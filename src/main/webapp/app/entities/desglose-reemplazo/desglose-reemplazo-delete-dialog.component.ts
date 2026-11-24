import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';
import { DesgloseReemplazoService } from './desglose-reemplazo.service';

@Component({
    selector: 'jhi-desglose-reemplazo-delete-dialog',
    templateUrl: './desglose-reemplazo-delete-dialog.component.html'
})
export class DesgloseReemplazoDeleteDialogComponent {
    desgloseReemplazo: IDesgloseReemplazo;

    constructor(
        private desgloseReemplazoService: DesgloseReemplazoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseReemplazoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseReemplazoListModification',
                content: 'Deleted an desgloseReemplazo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-reemplazo-delete-popup',
    template: ''
})
export class DesgloseReemplazoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseReemplazo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseReemplazoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseReemplazo = desgloseReemplazo;
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
