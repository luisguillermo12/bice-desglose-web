import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClienteSinRut } from 'app/shared/model/cliente-sin-rut.model';
import { ClienteSinRutService } from './cliente-sin-rut.service';

@Component({
    selector: 'jhi-cliente-sin-rut-delete-dialog',
    templateUrl: './cliente-sin-rut-delete-dialog.component.html'
})
export class ClienteSinRutDeleteDialogComponent {
    clienteSinRut: IClienteSinRut;

    constructor(
        private clienteSinRutService: ClienteSinRutService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(codCliente: string) {
        this.clienteSinRutService.delete(codCliente).subscribe(response => {
            this.eventManager.broadcast({
                name: 'clienteSinRutListModification',
                content: 'Deleted an clienteSinRut'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cliente-sin-rut-delete-popup',
    template: ''
})
export class ClienteSinRutDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteSinRut }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ClienteSinRutDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.clienteSinRut = clienteSinRut;
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
