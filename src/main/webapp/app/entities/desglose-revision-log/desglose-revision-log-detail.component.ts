import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseRevisionLog } from 'app/shared/model/desglose-revision-log.model';

@Component({
    selector: 'jhi-desglose-revision-log-detail',
    templateUrl: './desglose-revision-log-detail.component.html'
})
export class DesgloseRevisionLogDetailComponent implements OnInit {
    desgloseRevisionLog: IDesgloseRevisionLog;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseRevisionLog }) => {
            this.desgloseRevisionLog = desgloseRevisionLog;
        });
    }

    previousState() {
        window.history.back();
    }
}
