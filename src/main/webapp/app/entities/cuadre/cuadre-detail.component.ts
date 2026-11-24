import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICuadre } from 'app/shared/model/cuadre.model';

@Component({
    selector: 'jhi-cuadre-detail',
    templateUrl: './cuadre-detail.component.html'
})
export class CuadreDetailComponent implements OnInit {
    cuadre: ICuadre;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cuadre }) => {
            this.cuadre = cuadre;
        });
    }

    previousState() {
        window.history.back();
    }
}
