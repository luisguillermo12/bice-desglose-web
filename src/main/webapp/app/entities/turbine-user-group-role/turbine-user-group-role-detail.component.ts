import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';

@Component({
    selector: 'jhi-turbine-user-group-role-detail',
    templateUrl: './turbine-user-group-role-detail.component.html'
})
export class TurbineUserGroupRoleDetailComponent implements OnInit {
    turbineUserGroupRole: ITurbineUserGroupRole;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ turbineUserGroupRole }) => {
            this.turbineUserGroupRole = turbineUserGroupRole;
        });
    }

    previousState() {
        window.history.back();
    }
}
