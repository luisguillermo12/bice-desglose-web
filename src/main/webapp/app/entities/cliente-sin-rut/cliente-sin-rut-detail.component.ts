import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClienteSinRut } from 'app/shared/model/cliente-sin-rut.model';

@Component({
    selector: 'jhi-cliente-sin-rut-detail',
    templateUrl: './cliente-sin-rut-detail.component.html'
})
export class ClienteSinRutDetailComponent implements OnInit {
    clienteSinRut: IClienteSinRut;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clienteSinRut }) => {
            this.clienteSinRut = clienteSinRut;
        });
    }

    previousState() {
        window.history.back();
    }
}
