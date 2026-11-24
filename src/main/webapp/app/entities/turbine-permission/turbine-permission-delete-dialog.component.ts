import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITurbinePermission } from 'app/shared/model/turbine-permission.model';
import { TurbinePermissionService } from './turbine-permission.service';

@Component({
    selector: 'jhi-turbine-permission-delete-dialog',
    templateUrl: './turbine-permission-delete-dialog.component.html'
})
export class TurbinePermissionDeleteDialogComponent {
    turbinePermission: ITurbinePermission;

    constructor(
        private turbinePermissionService: TurbinePermissionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.turbinePermissionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'turbinePermissionListModification',
                content: 'Deleted an turbinePermission'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-turbine-permission-delete-popup',
    template: ''
})
export class TurbinePermissionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbinePermission }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TurbinePermissionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.turbinePermission = turbinePermission;
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
