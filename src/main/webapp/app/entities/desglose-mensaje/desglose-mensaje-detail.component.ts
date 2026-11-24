import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';

@Component({
    selector: 'jhi-desglose-mensaje-detail',
    templateUrl: './desglose-mensaje-detail.component.html'
})
export class DesgloseMensajeDetailComponent implements OnInit {
    desgloseMensaje: IDesgloseMensaje;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseMensaje }) => {
            this.desgloseMensaje = desgloseMensaje;
        });
    }

    previousState() {
        window.history.back();
    }
}
