import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseFirma } from 'app/shared/model/desglose-firma.model';

@Component({
    selector: 'jhi-desglose-firma-detail',
    templateUrl: './desglose-firma-detail.component.html'
})
export class DesgloseFirmaDetailComponent implements OnInit {
    desgloseFirma: IDesgloseFirma;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseFirma }) => {
            this.desgloseFirma = desgloseFirma;
        });
    }

    previousState() {
        window.history.back();
    }
}
