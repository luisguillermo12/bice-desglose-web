import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITurbineGroup } from 'app/shared/model/turbine-group.model';
import { TurbineGroupService } from './turbine-group.service';

@Component({
    selector: 'jhi-turbine-group-delete-dialog',
    templateUrl: './turbine-group-delete-dialog.component.html'
})
export class TurbineGroupDeleteDialogComponent {
    turbineGroup: ITurbineGroup;

    constructor(
        private turbineGroupService: TurbineGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.turbineGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'turbineGroupListModification',
                content: 'Deleted an turbineGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-turbine-group-delete-popup',
    template: ''
})
export class TurbineGroupDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TurbineGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.turbineGroup = turbineGroup;
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
