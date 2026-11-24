import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';

@Component({
    selector: 'jhi-descripcion-codigo-detail',
    templateUrl: './descripcion-codigo-detail.component.html'
})
export class DescripcionCodigoDetailComponent implements OnInit {
    descripcionCodigo: IDescripcionCodigo;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ descripcionCodigo }) => {
            this.descripcionCodigo = descripcionCodigo;
        });
    }

    previousState() {
        window.history.back();
    }
}
