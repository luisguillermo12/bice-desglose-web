import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseChequeoLog } from 'app/shared/model/desglose-chequeo-log.model';
import { DesgloseChequeoLogService } from './desglose-chequeo-log.service';

@Component({
    selector: 'jhi-desglose-chequeo-log-delete-dialog',
    templateUrl: './desglose-chequeo-log-delete-dialog.component.html'
})
export class DesgloseChequeoLogDeleteDialogComponent {
    desgloseChequeoLog: IDesgloseChequeoLog;

    constructor(
        private desgloseChequeoLogService: DesgloseChequeoLogService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseChequeoLogService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseChequeoLogListModification',
                content: 'Deleted an desgloseChequeoLog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-chequeo-log-delete-popup',
    template: ''
})
export class DesgloseChequeoLogDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseChequeoLog }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseChequeoLogDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseChequeoLog = desgloseChequeoLog;
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
