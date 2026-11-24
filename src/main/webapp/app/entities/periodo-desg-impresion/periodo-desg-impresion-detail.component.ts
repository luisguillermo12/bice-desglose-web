import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeriodoDesgImpresion } from 'app/shared/model/periodo-desg-impresion.model';

@Component({
    selector: 'jhi-periodo-desg-impresion-detail',
    templateUrl: './periodo-desg-impresion-detail.component.html'
})
export class PeriodoDesgImpresionDetailComponent implements OnInit {
    periodoDesgImpresion: IPeriodoDesgImpresion;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ periodoDesgImpresion }) => {
            this.periodoDesgImpresion = periodoDesgImpresion;
        });
    }

    previousState() {
        window.history.back();
    }
}
