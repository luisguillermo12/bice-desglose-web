import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseImpresion } from 'app/shared/model/desglose-impresion.model';
import { DesgloseImpresionService } from './desglose-impresion.service';

@Component({
    selector: 'jhi-desglose-impresion-delete-dialog',
    templateUrl: './desglose-impresion-delete-dialog.component.html'
})
export class DesgloseImpresionDeleteDialogComponent {
    desgloseImpresion: IDesgloseImpresion;

    constructor(
        private desgloseImpresionService: DesgloseImpresionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseImpresionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseImpresionListModification',
                content: 'Deleted an desgloseImpresion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-impresion-delete-popup',
    template: ''
})
export class DesgloseImpresionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseImpresion }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseImpresionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseImpresion = desgloseImpresion;
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
