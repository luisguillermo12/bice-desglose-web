import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValPeriodoDesgImpr } from 'app/shared/model/val-periodo-desg-impr.model';

@Component({
    selector: 'jhi-val-periodo-desg-impr-detail',
    templateUrl: './val-periodo-desg-impr-detail.component.html'
})
export class ValPeriodoDesgImprDetailComponent implements OnInit {
    valPeriodoDesgImpr: IValPeriodoDesgImpr;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ valPeriodoDesgImpr }) => {
            this.valPeriodoDesgImpr = valPeriodoDesgImpr;
        });
    }

    previousState() {
        window.history.back();
    }
}
