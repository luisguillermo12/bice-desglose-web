import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITablaValor } from 'app/shared/model/tabla-valor.model';

@Component({
    selector: 'jhi-tabla-valor-detail',
    templateUrl: './tabla-valor-detail.component.html'
})
export class TablaValorDetailComponent implements OnInit {
    tablaValor: ITablaValor;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tablaValor }) => {
            this.tablaValor = tablaValor;
        });
    }

    previousState() {
        window.history.back();
    }
}
