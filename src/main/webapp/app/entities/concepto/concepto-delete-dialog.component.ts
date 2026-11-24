import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConcepto } from 'app/shared/model/concepto.model';
import { ConceptoService } from './concepto.service';

@Component({
    selector: 'jhi-concepto-delete-dialog',
    templateUrl: './concepto-delete-dialog.component.html'
})
export class ConceptoDeleteDialogComponent {
    concepto: IConcepto;

    constructor(private conceptoService: ConceptoService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(codTipoConcep: string, corConcepto: number, codTipoPlantilla: number) {
        this.conceptoService.delete(codTipoConcep, corConcepto, codTipoPlantilla).subscribe(response => {
            this.eventManager.broadcast({
                name: 'conceptoListModification',
                content: 'Deleted an concepto'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-concepto-delete-popup',
    template: ''
})
export class ConceptoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ concepto }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConceptoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.concepto = concepto;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
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
