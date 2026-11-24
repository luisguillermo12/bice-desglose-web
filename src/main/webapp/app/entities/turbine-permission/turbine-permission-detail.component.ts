import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbinePermission } from 'app/shared/model/turbine-permission.model';

@Component({
    selector: 'jhi-turbine-permission-detail',
    templateUrl: './turbine-permission-detail.component.html'
})
export class TurbinePermissionDetailComponent implements OnInit {
    turbinePermission: ITurbinePermission;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbinePermission }) => {
            this.turbinePermission = turbinePermission;
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
