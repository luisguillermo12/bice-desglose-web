import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClienteJuridico } from 'app/shared/model/cliente-juridico.model';
import { ClienteJuridicoService } from './cliente-juridico.service';

@Component({
    selector: 'jhi-cliente-juridico-delete-dialog',
    templateUrl: './cliente-juridico-delete-dialog.component.html'
})
export class ClienteJuridicoDeleteDialogComponent {
    clienteJuridico: IClienteJuridico;

    constructor(
        private clienteJuridicoService: ClienteJuridicoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.clienteJuridicoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'clienteJuridicoListModification',
                content: 'Deleted an clienteJuridico'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cliente-juridico-delete-popup',
    template: ''
})
export class ClienteJuridicoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteJuridico }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ClienteJuridicoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.clienteJuridico = clienteJuridico;
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
