import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';
import { DesgloseGrupoService } from './desglose-grupo.service';

@Component({
    selector: 'jhi-desglose-grupo-delete-dialog',
    templateUrl: './desglose-grupo-delete-dialog.component.html'
})
export class DesgloseGrupoDeleteDialogComponent {
    desgloseGrupo: IDesgloseGrupo;

    constructor(
        private desgloseGrupoService: DesgloseGrupoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.desgloseGrupoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'desgloseGrupoListModification',
                content: 'Deleted an desgloseGrupo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desglose-grupo-delete-popup',
    template: ''
})
export class DesgloseGrupoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseGrupo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DesgloseGrupoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.desgloseGrupo = desgloseGrupo;
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
