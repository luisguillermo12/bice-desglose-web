import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseChequeoLog } from 'app/shared/model/desglose-chequeo-log.model';

@Component({
    selector: 'jhi-desglose-chequeo-log-detail',
    templateUrl: './desglose-chequeo-log-detail.component.html'
})
export class DesgloseChequeoLogDetailComponent implements OnInit {
    desgloseChequeoLog: IDesgloseChequeoLog;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseChequeoLog }) => {
            this.desgloseChequeoLog = desgloseChequeoLog;
        });
    }

    previousState() {
        window.history.back();
    }
}
