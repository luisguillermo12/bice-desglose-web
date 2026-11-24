import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { DescripcionCodigoService } from './descripcion-codigo.service';

@Component({
    selector: 'jhi-descripcion-codigo-delete-dialog',
    templateUrl: './descripcion-codigo-delete-dialog.component.html'
})
export class DescripcionCodigoDeleteDialogComponent {
    descripcionCodigo: IDescripcionCodigo;

    constructor(
        private descripcionCodigoService: DescripcionCodigoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(codTabla: string, codGenerico: string, codRegStatus: number,glsGenericacorta: string,glsGenericalarga:string ) {
        this.descripcionCodigoService.delete(codTabla, codGenerico, codRegStatus,glsGenericacorta,glsGenericalarga).subscribe(response => {
            this.eventManager.broadcast({
                name: 'descripcionCodigoListModification',
                content: 'Deleted an descripcionCodigo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-descripcion-codigo-delete-popup',
    template: ''
})
export class DescripcionCodigoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ descripcionCodigo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DescripcionCodigoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.descripcionCodigo = descripcionCodigo;
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
