import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITurbineUser } from 'app/shared/model/turbine-user.model';
import { TurbineUserService } from './turbine-user.service';

@Component({
    selector: 'jhi-turbine-user-delete-dialog',
    templateUrl: './turbine-user-delete-dialog.component.html'
})
export class TurbineUserDeleteDialogComponent {
    turbineUser: ITurbineUser;

    constructor(
        private turbineUserService: TurbineUserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.turbineUserService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'turbineUserListModification',
                content: 'Deleted an turbineUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-turbine-user-delete-popup',
    template: ''
})
export class TurbineUserDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineUser }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TurbineUserDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.turbineUser = turbineUser;
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
