import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseImpresion } from 'app/shared/model/desglose-impresion.model';

@Component({
    selector: 'jhi-desglose-impresion-detail',
    templateUrl: './desglose-impresion-detail.component.html'
})
export class DesgloseImpresionDetailComponent implements OnInit {
    desgloseImpresion: IDesgloseImpresion;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseImpresion }) => {
            this.desgloseImpresion = desgloseImpresion;
        });
    }

    previousState() {
        window.history.back();
    }
}
