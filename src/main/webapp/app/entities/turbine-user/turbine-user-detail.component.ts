import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbineUser } from 'app/shared/model/turbine-user.model';

@Component({
    selector: 'jhi-turbine-user-detail',
    templateUrl: './turbine-user-detail.component.html'
})
export class TurbineUserDetailComponent implements OnInit {
    turbineUser: ITurbineUser;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineUser }) => {
            this.turbineUser = turbineUser;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
