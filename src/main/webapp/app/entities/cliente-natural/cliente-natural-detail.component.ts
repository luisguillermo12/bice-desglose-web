import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClienteNatural } from 'app/shared/model/cliente-natural.model';

@Component({
    selector: 'jhi-cliente-natural-detail',
    templateUrl: './cliente-natural-detail.component.html'
})
export class ClienteNaturalDetailComponent implements OnInit {
    clienteNatural: IClienteNatural;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteNatural }) => {
            this.clienteNatural = clienteNatural;
        });
    }

    previousState() {
        window.history.back();
    }
}
