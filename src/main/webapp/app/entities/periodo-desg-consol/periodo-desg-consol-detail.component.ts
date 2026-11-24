import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeriodoDesgConsol } from 'app/shared/model/periodo-desg-consol.model';

@Component({
    selector: 'jhi-periodo-desg-consol-detail',
    templateUrl: './periodo-desg-consol-detail.component.html'
})
export class PeriodoDesgConsolDetailComponent implements OnInit {
    periodoDesgConsol: IPeriodoDesgConsol;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ periodoDesgConsol }) => {
            this.periodoDesgConsol = periodoDesgConsol;
        });
    }

    previousState() {
        window.history.back();
    }
}
