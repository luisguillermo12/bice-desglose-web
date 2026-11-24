import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIdentificacionCliente } from 'app/shared/model/identificacion-cliente.model';

@Component({
    selector: 'jhi-identificacion-cliente-detail',
    templateUrl: './identificacion-cliente-detail.component.html'
})
export class IdentificacionClienteDetailComponent implements OnInit {
    identificacionCliente: IIdentificacionCliente;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ identificacionCliente }) => {
            this.identificacionCliente = identificacionCliente;
        });
    }

    previousState() {
        window.history.back();
    }
}
