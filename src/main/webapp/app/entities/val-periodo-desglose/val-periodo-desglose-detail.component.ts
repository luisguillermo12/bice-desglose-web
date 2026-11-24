import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValPeriodoDesglose } from 'app/shared/model/val-periodo-desglose.model';

@Component({
    selector: 'jhi-val-periodo-desglose-detail',
    templateUrl: './val-periodo-desglose-detail.component.html'
})
export class ValPeriodoDesgloseDetailComponent implements OnInit {
    valPeriodoDesglose: IValPeriodoDesglose;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ valPeriodoDesglose }) => {
            this.valPeriodoDesglose = valPeriodoDesglose;
        });
    }

    previousState() {
        window.history.back();
    }
}
