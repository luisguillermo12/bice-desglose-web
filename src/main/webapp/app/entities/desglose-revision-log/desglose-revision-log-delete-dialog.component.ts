import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseRevisionLog } from 'app/shared/model/desglose-revision-log.model';
import { DesgloseRevisionLogService } from './desglose-revision-log.service';

@Component({
    selector: 'jhi-desglose-revision-log-delete-dialog',
    templateUrl: './desglose-revision-log-delete-dialog.component.html'
})
export class DesgloseRevisionLogDeleteDialogComponent {
    desgloseRevisionLog: IDesgloseRevisionLog;

    constructor(
        private desgloseRevisionLogService: DesgloseRevisionLogService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseRevisionLogService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseRevisionLogListModification',
                content: 'Deleted an desgloseRevisionLog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-revision-log-delete-popup',
    template: ''
})
export class DesgloseRevisionLogDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseRevisionLog }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseRevisionLogDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseRevisionLog = desgloseRevisionLog;
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
