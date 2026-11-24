import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConcepto } from 'app/shared/model/concepto.model';

@Component({
    selector: 'jhi-concepto-detail',
    templateUrl: './concepto-detail.component.html'
})
export class ConceptoDetailComponent implements OnInit {
    concepto: IConcepto;

    constructor(private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ concepto }) => {
            this.concepto = concepto;
        });
    }

    previousState() {
        window.history.back();
    }
}
