import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConceptoImpresion } from 'app/shared/model/concepto-impresion.model';
import { ConceptoImpresionService } from './concepto-impresion.service';

@Component({
    selector: 'jhi-concepto-impresion-delete-dialog',
    templateUrl: './concepto-impresion-delete-dialog.component.html'
})
export class ConceptoImpresionDeleteDialogComponent {
    conceptoImpresion: IConceptoImpresion;

    constructor(private conceptoImpresionService: ConceptoImpresionService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(codCliente: string, codTipoDes: number, codSubtipoDes: number, codMoneda: string, codTipoConcep: string, corConcepto: string) {
        this.conceptoImpresionService.delete(codCliente, codTipoDes, codSubtipoDes, codMoneda, codTipoConcep, corConcepto)
        .subscribe(response => {
            this.eventManager.broadcast({
                name: 'conceptoImpresionListModification',
                content: 'Deleted an conceptoImpresion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-concepto-impresion-delete-popup',
    template: ''
})
export class ConceptoImpresionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ conceptoImpresion }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConceptoImpresionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.conceptoImpresion = conceptoImpresion;
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
