import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClienteBasico } from 'app/shared/model/cliente-basico.model';
import { ClienteBasicoService } from './cliente-basico.service';

@Component({
    selector: 'jhi-cliente-basico-delete-dialog',
    templateUrl: './cliente-basico-delete-dialog.component.html'
})
export class ClienteBasicoDeleteDialogComponent {
    clienteBasico: IClienteBasico;

    constructor(
        private clienteBasicoService: ClienteBasicoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.clienteBasicoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'clienteBasicoListModification',
                content: 'Deleted an clienteBasico'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cliente-basico-delete-popup',
    template: ''
})
export class ClienteBasicoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteBasico }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ClienteBasicoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.clienteBasico = clienteBasico;
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
