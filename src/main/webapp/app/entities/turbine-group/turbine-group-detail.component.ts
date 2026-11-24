import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbineGroup } from 'app/shared/model/turbine-group.model';

@Component({
    selector: 'jhi-turbine-group-detail',
    templateUrl: './turbine-group-detail.component.html'
})
export class TurbineGroupDetailComponent implements OnInit {
    turbineGroup: ITurbineGroup;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineGroup }) => {
            this.turbineGroup = turbineGroup;
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
