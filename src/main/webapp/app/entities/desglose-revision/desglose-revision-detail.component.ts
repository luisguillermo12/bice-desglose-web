import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseRevision } from 'app/shared/model/desglose-revision.model';

@Component({
    selector: 'jhi-desglose-revision-detail',
    templateUrl: './desglose-revision-detail.component.html'
})
export class DesgloseRevisionDetailComponent implements OnInit {
    desgloseRevision: IDesgloseRevision;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseRevision }) => {
            this.desgloseRevision = desgloseRevision;
        });
    }

    previousState() {
        window.history.back();
    }
}
