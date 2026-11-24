import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBlackListJwt } from 'app/shared/model/black-list-jwt.model';

@Component({
    selector: 'jhi-black-list-jwt-detail',
    templateUrl: './black-list-jwt-detail.component.html'
})
export class BlackListJwtDetailComponent implements OnInit {
    blackListJwt: IBlackListJwt;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ blackListJwt }) => {
            this.blackListJwt = blackListJwt;
        });
    }

    previousState() {
        window.history.back();
    }
}
