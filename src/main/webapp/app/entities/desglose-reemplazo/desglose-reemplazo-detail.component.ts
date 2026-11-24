import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';

@Component({
    selector: 'jhi-desglose-reemplazo-detail',
    templateUrl: './desglose-reemplazo-detail.component.html'
})
export class DesgloseReemplazoDetailComponent implements OnInit {
    desgloseReemplazo: IDesgloseReemplazo;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseReemplazo }) => {
            this.desgloseReemplazo = desgloseReemplazo;
        });
    }

    previousState() {
        window.history.back();
    }
}
