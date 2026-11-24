import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUniqueSessionControl } from 'app/shared/model/unique-session-control.model';
import { UniqueSessionControlService } from './unique-session-control.service';

@Component({
    selector: 'jhi-unique-session-control-delete-dialog',
    templateUrl: './unique-session-control-delete-dialog.component.html'
})
export class UniqueSessionControlDeleteDialogComponent {
    uniqueSessionControl: IUniqueSessionControl;

    constructor(
        private uniqueSessionControlService: UniqueSessionControlService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.uniqueSessionControlService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'uniqueSessionControlListModification',
                content: 'Deleted an uniqueSessionControl'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-unique-session-control-delete-popup',
    template: ''
})
export class UniqueSessionControlDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ uniqueSessionControl }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UniqueSessionControlDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.uniqueSessionControl = uniqueSessionControl;
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
