import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseChequeoInfo } from 'app/shared/model/desglose-chequeo-info.model';
import { DesgloseChequeoInfoService } from './desglose-chequeo-info.service';

@Component({
    selector: 'jhi-desglose-chequeo-info-delete-dialog',
    templateUrl: './desglose-chequeo-info-delete-dialog.component.html'
})
export class DesgloseChequeoInfoDeleteDialogComponent {
    desgloseChequeoInfo: IDesgloseChequeoInfo;

    constructor(
        private desgloseChequeoInfoService: DesgloseChequeoInfoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseChequeoInfoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseChequeoInfoListModification',
                content: 'Deleted an desgloseChequeoInfo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-chequeo-info-delete-popup',
    template: ''
})
export class DesgloseChequeoInfoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseChequeoInfo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseChequeoInfoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseChequeoInfo = desgloseChequeoInfo;
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
