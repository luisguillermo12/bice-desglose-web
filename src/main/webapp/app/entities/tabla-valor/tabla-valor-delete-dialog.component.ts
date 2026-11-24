import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITablaValor } from 'app/shared/model/tabla-valor.model';
import { TablaValorService } from './tabla-valor.service';

@Component({
    selector: 'jhi-tabla-valor-delete-dialog',
    templateUrl: './tabla-valor-delete-dialog.component.html'
})
export class TablaValorDeleteDialogComponent {
    tablaValor: ITablaValor;

    constructor(private tablaValorService: TablaValorService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tablaValorService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'tablaValorListModification',
                content: 'Deleted an tablaValor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tabla-valor-delete-popup',
    template: ''
})
export class TablaValorDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tablaValor }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TablaValorDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.tablaValor = tablaValor;
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
