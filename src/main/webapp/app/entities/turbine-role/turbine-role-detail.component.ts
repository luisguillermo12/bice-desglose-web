import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbineRole } from 'app/shared/model/turbine-role.model';

@Component({
    selector: 'jhi-turbine-role-detail',
    templateUrl: './turbine-role-detail.component.html'
})
export class TurbineRoleDetailComponent implements OnInit {
    turbineRole: ITurbineRole;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineRole }) => {
            this.turbineRole = turbineRole;
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
