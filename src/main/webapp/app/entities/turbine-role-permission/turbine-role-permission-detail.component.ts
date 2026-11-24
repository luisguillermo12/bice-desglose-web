import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITurbineRolePermission } from 'app/shared/model/turbine-role-permission.model';

@Component({
    selector: 'jhi-turbine-role-permission-detail',
    templateUrl: './turbine-role-permission-detail.component.html'
})
export class TurbineRolePermissionDetailComponent implements OnInit {
    turbineRolePermission: ITurbineRolePermission;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineRolePermission }) => {
            this.turbineRolePermission = turbineRolePermission;
        });
    }

    previousState() {
        window.history.back();
    }
}
