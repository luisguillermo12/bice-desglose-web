import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISfStfOperator } from 'app/shared/model/sf-stf-operator.model';
import { SfStfOperatorService } from './sf-stf-operator.service';

@Component({
    selector: 'jhi-sf-stf-operator-delete-dialog',
    templateUrl: './sf-stf-operator-delete-dialog.component.html'
})
export class SfStfOperatorDeleteDialogComponent {
    sfStfOperator: ISfStfOperator;

    constructor(
        private sfStfOperatorService: SfStfOperatorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sfStfOperatorService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sfStfOperatorListModification',
                content: 'Deleted an sfStfOperator'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sf-stf-operator-delete-popup',
    template: ''
})
export class SfStfOperatorDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sfStfOperator }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SfStfOperatorDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sfStfOperator = sfStfOperator;
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
