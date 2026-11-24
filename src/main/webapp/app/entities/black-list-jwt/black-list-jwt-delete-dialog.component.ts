import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBlackListJwt } from 'app/shared/model/black-list-jwt.model';
import { BlackListJwtService } from './black-list-jwt.service';

@Component({
    selector: 'jhi-black-list-jwt-delete-dialog',
    templateUrl: './black-list-jwt-delete-dialog.component.html'
})
export class BlackListJwtDeleteDialogComponent {
    blackListJwt: IBlackListJwt;

    constructor(
        private blackListJwtService: BlackListJwtService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.blackListJwtService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'blackListJwtListModification',
                content: 'Deleted an blackListJwt'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-black-list-jwt-delete-popup',
    template: ''
})
export class BlackListJwtDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ blackListJwt }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BlackListJwtDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.blackListJwt = blackListJwt;
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
