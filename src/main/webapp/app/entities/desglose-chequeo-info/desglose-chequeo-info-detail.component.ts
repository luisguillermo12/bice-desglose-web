import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseChequeoInfo } from 'app/shared/model/desglose-chequeo-info.model';

@Component({
    selector: 'jhi-desglose-chequeo-info-detail',
    templateUrl: './desglose-chequeo-info-detail.component.html'
})
export class DesgloseChequeoInfoDetailComponent implements OnInit {
    desgloseChequeoInfo: IDesgloseChequeoInfo;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseChequeoInfo }) => {
            this.desgloseChequeoInfo = desgloseChequeoInfo;
        });
    }

    previousState() {
        window.history.back();
    }
}
