import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICuadre } from 'app/shared/model/cuadre.model';
import { CuadreService } from './cuadre.service';

@Component({
    selector: 'jhi-cuadre-delete-dialog',
    templateUrl: './cuadre-delete-dialog.component.html'
})
export class CuadreDeleteDialogComponent {
    cuadre: ICuadre;

    constructor(private cuadreService: CuadreService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(codTpoConcep1: string, corConcepto1: number, codTpoConcep2: string, corConcepto2: number) {
        this.cuadreService.delete(codTpoConcep1, corConcepto1, codTpoConcep2, corConcepto2).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cuadreListModification',
                content: 'Deleted an cuadre'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cuadre-delete-popup',
    template: ''
})
export class CuadreDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cuadre }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CuadreDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.cuadre = cuadre;
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
