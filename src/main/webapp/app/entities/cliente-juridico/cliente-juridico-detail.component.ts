import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClienteJuridico } from 'app/shared/model/cliente-juridico.model';

@Component({
    selector: 'jhi-cliente-juridico-detail',
    templateUrl: './cliente-juridico-detail.component.html'
})
export class ClienteJuridicoDetailComponent implements OnInit {
    clienteJuridico: IClienteJuridico;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteJuridico }) => {
            this.clienteJuridico = clienteJuridico;
        });
    }

    previousState() {
        window.history.back();
    }
}
