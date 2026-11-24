import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISfStfOperator } from 'app/shared/model/sf-stf-operator.model';

@Component({
    selector: 'jhi-sf-stf-operator-detail',
    templateUrl: './sf-stf-operator-detail.component.html'
})
export class SfStfOperatorDetailComponent implements OnInit {
    sfStfOperator: ISfStfOperator;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sfStfOperator }) => {
            this.sfStfOperator = sfStfOperator;
        });
    }

    previousState() {
        window.history.back();
    }
}
