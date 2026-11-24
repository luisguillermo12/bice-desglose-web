import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClienteBasico } from 'app/shared/model/cliente-basico.model';

@Component({
    selector: 'jhi-cliente-basico-detail',
    templateUrl: './cliente-basico-detail.component.html'
})
export class ClienteBasicoDetailComponent implements OnInit {
    clienteBasico: IClienteBasico;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteBasico }) => {
            this.clienteBasico = clienteBasico;
        });
    }

    previousState() {
        window.history.back();
    }
}
