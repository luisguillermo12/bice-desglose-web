import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITablaServicioMail } from 'app/shared/model/tabla-servicio-mail.model';

@Component({
    selector: 'jhi-tabla-servicio-mail-detail',
    templateUrl: './tabla-servicio-mail-detail.component.html'
})
export class TablaServicioMailDetailComponent implements OnInit {
    tablaServicioMail: ITablaServicioMail;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tablaServicioMail }) => {
            this.tablaServicioMail = tablaServicioMail;
        });
    }

    previousState() {
        window.history.back();
    }
}
