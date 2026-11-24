import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITablaServicioMail } from 'app/shared/model/tabla-servicio-mail.model';
import { TablaServicioMailService } from './tabla-servicio-mail.service';

@Component({
    selector: 'jhi-tabla-servicio-mail-delete-dialog',
    templateUrl: './tabla-servicio-mail-delete-dialog.component.html'
})
export class TablaServicioMailDeleteDialogComponent {
    tablaServicioMail: ITablaServicioMail;

    constructor(
        private tablaServicioMailService: TablaServicioMailService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tablaServicioMailService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'tablaServicioMailListModification',
                content: 'Deleted an tablaServicioMail'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tabla-servicio-mail-delete-popup',
    template: ''
})
export class TablaServicioMailDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tablaServicioMail }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TablaServicioMailDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.tablaServicioMail = tablaServicioMail;
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
