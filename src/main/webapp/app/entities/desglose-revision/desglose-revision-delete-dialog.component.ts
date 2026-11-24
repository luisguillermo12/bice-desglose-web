import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseRevision } from 'app/shared/model/desglose-revision.model';
import { DesgloseRevisionService } from './desglose-revision.service';

@Component({
    selector: 'jhi-desglose-revision-delete-dialog',
    templateUrl: './desglose-revision-delete-dialog.component.html'
})
export class DesgloseRevisionDeleteDialogComponent {
    desgloseRevision: IDesgloseRevision;

    constructor(
        private desgloseRevisionService: DesgloseRevisionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseRevisionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseRevisionListModification',
                content: 'Deleted an desgloseRevision'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-revision-delete-popup',
    template: ''
})
export class DesgloseRevisionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseRevision }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseRevisionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseRevision = desgloseRevision;
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
