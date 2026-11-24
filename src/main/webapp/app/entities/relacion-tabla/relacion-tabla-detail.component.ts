import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRelacionTabla } from 'app/shared/model/relacion-tabla.model';

@Component({
    selector: 'jhi-relacion-tabla-detail',
    templateUrl: './relacion-tabla-detail.component.html'
})
export class RelacionTablaDetailComponent implements OnInit {
    relacionTabla: IRelacionTabla;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ relacionTabla }) => {
            this.relacionTabla = relacionTabla;
        });
    }

    previousState() {
        window.history.back();
    }
}
