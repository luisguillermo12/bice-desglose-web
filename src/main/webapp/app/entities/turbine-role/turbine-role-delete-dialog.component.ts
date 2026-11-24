import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITurbineRole } from 'app/shared/model/turbine-role.model';
import { TurbineRoleService } from './turbine-role.service';

@Component({
    selector: 'jhi-turbine-role-delete-dialog',
    templateUrl: './turbine-role-delete-dialog.component.html'
})
export class TurbineRoleDeleteDialogComponent {
    turbineRole: ITurbineRole;

    constructor(
        private turbineRoleService: TurbineRoleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.turbineRoleService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'turbineRoleListModification',
                content: 'Deleted an turbineRole'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-turbine-role-delete-popup',
    template: ''
})
export class TurbineRoleDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineRole }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TurbineRoleDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.turbineRole = turbineRole;
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
