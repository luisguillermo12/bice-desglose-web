import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIdentificacionCliente } from 'app/shared/model/identificacion-cliente.model';
import { IdentificacionClienteService } from './identificacion-cliente.service';

@Component({
    selector: 'jhi-identificacion-cliente-delete-dialog',
    templateUrl: './identificacion-cliente-delete-dialog.component.html'
})
export class IdentificacionClienteDeleteDialogComponent {
    identificacionCliente: IIdentificacionCliente;

    constructor(
        private identificacionClienteService: IdentificacionClienteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.identificacionClienteService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'identificacionClienteListModification',
                content: 'Deleted an identificacionCliente'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-identificacion-cliente-delete-popup',
    template: ''
})
export class IdentificacionClienteDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ identificacionCliente }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(IdentificacionClienteDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.identificacionCliente = identificacionCliente;
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
