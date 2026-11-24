import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesglose } from 'app/shared/model/desglose.model';
import { DesgloseService } from './desglose.service';

@Component({
    selector: 'jhi-desglose-delete-dialog',
    templateUrl: './desglose-delete-dialog.component.html'
})
export class DesgloseDeleteDialogComponent {
    desglose: IDesglose;

    constructor(private desgloseService: DesgloseService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(codCliente: string, codTipoDes: string, codSubtipoDes: string, codMoneda: string, codPlantilla: string) {
        this.desgloseService.delete(codCliente, codTipoDes, codSubtipoDes, codMoneda, codPlantilla).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseListModification',
                content: 'Deleted an desglose'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-delete-popup',
    template: ''
})
export class DesgloseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desglose }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.desglose = desglose;
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
