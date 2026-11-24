import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';
import { TurbineUserGroupRoleService } from './turbine-user-group-role.service';

@Component({
    selector: 'jhi-turbine-user-group-role-delete-dialog',
    templateUrl: './turbine-user-group-role-delete-dialog.component.html'
})
export class TurbineUserGroupRoleDeleteDialogComponent {
    turbineUserGroupRole: ITurbineUserGroupRole;

    constructor(
        private turbineUserGroupRoleService: TurbineUserGroupRoleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.turbineUserGroupRoleService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'turbineUserGroupRoleListModification',
                content: 'Deleted an turbineUserGroupRole'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-turbine-user-group-role-delete-popup',
    template: ''
})
export class TurbineUserGroupRoleDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineUserGroupRole }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TurbineUserGroupRoleDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.turbineUserGroupRole = turbineUserGroupRole;
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
