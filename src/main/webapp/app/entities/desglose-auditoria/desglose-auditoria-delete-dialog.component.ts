import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';
import { DesgloseAuditoriaService } from './desglose-auditoria.service';

@Component({
    selector: 'jhi-desglose-auditoria-delete-dialog',
    templateUrl: './desglose-auditoria-delete-dialog.component.html'
})
export class DesgloseAuditoriaDeleteDialogComponent {
    desgloseAuditoria: IDesgloseAuditoria;

    constructor(
        private desgloseAuditoriaService: DesgloseAuditoriaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseAuditoriaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseAuditoriaListModification',
                content: 'Deleted an desgloseAuditoria'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-auditoria-delete-popup',
    template: ''
})
export class DesgloseAuditoriaDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseAuditoria }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseAuditoriaDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseAuditoria = desgloseAuditoria;
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
