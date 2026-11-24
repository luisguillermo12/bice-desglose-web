import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClienteNatural } from 'app/shared/model/cliente-natural.model';
import { ClienteNaturalService } from './cliente-natural.service';

@Component({
    selector: 'jhi-cliente-natural-delete-dialog',
    templateUrl: './cliente-natural-delete-dialog.component.html'
})
export class ClienteNaturalDeleteDialogComponent {
    clienteNatural: IClienteNatural;

    constructor(
        private clienteNaturalService: ClienteNaturalService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.clienteNaturalService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'clienteNaturalListModification',
                content: 'Deleted an clienteNatural'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cliente-natural-delete-popup',
    template: ''
})
export class ClienteNaturalDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteNatural }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ClienteNaturalDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.clienteNatural = clienteNatural;
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
