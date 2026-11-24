import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesglose } from 'app/shared/model/desglose.model';

@Component({
    selector: 'jhi-desglose-detail',
    templateUrl: './desglose-detail.component.html'
})
export class DesgloseDetailComponent implements OnInit {
    desglose: IDesglose;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desglose }) => {
            this.desglose = desglose;
        });
    }

    previousState() {
        window.history.back();
    }
}
