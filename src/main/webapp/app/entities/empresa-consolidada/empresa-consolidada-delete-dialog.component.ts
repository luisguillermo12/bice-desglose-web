import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmpresaConsolidada } from 'app/shared/model/empresa-consolidada.model';
import { EmpresaConsolidadaService } from './empresa-consolidada.service';

@Component({
    selector: 'jhi-empresa-consolidada-delete-dialog',
    templateUrl: './empresa-consolidada-delete-dialog.component.html'
})
export class EmpresaConsolidadaDeleteDialogComponent {
    empresaConsolidada: IEmpresaConsolidada;

    constructor(
        private empresaConsolidadaService: EmpresaConsolidadaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.empresaConsolidadaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'empresaConsolidadaListModification',
                content: 'Deleted an empresaConsolidada'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-empresa-consolidada-delete-popup',
    template: ''
})
export class EmpresaConsolidadaDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ empresaConsolidada }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(EmpresaConsolidadaDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.empresaConsolidada = empresaConsolidada;
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
