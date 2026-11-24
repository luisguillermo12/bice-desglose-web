import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConceptoImpresion } from 'app/shared/model/concepto-impresion.model';

@Component({
    selector: 'jhi-concepto-impresion-detail',
    templateUrl: './concepto-impresion-detail.component.html'
})
export class ConceptoImpresionDetailComponent implements OnInit {
    conceptoImpresion: IConceptoImpresion;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ conceptoImpresion }) => {
            this.conceptoImpresion = conceptoImpresion;
        });
    }

    previousState() {
        window.history.back();
    }
}
