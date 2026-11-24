import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRelacionTabla } from 'app/shared/model/relacion-tabla.model';
import { RelacionTablaService } from './relacion-tabla.service';

@Component({
    selector: 'jhi-relacion-tabla-delete-dialog',
    templateUrl: './relacion-tabla-delete-dialog.component.html'
})
export class RelacionTablaDeleteDialogComponent {
    relacionTabla: IRelacionTabla;

    constructor(
        private relacionTablaService: RelacionTablaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.relacionTablaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'relacionTablaListModification',
                content: 'Deleted an relacionTabla'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-relacion-tabla-delete-popup',
    template: ''
})
export class RelacionTablaDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ relacionTabla }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RelacionTablaDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.relacionTabla = relacionTabla;
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
