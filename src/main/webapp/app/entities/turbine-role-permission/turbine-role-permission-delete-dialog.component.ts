import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITurbineRolePermission } from 'app/shared/model/turbine-role-permission.model';
import { TurbineRolePermissionService } from './turbine-role-permission.service';

@Component({
    selector: 'jhi-turbine-role-permission-delete-dialog',
    templateUrl: './turbine-role-permission-delete-dialog.component.html'
})
export class TurbineRolePermissionDeleteDialogComponent {
    turbineRolePermission: ITurbineRolePermission;

    constructor(
        private turbineRolePermissionService: TurbineRolePermissionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.turbineRolePermissionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'turbineRolePermissionListModification',
                content: 'Deleted an turbineRolePermission'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-turbine-role-permission-delete-popup',
    template: ''
})
export class TurbineRolePermissionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineRolePermission }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TurbineRolePermissionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.turbineRolePermission = turbineRolePermission;
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
