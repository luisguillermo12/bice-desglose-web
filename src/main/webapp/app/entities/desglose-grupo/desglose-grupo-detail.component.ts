import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';

@Component({
    selector: 'jhi-desglose-grupo-detail',
    templateUrl: './desglose-grupo-detail.component.html'
})
export class DesgloseGrupoDetailComponent implements OnInit {
    desgloseGrupo: IDesgloseGrupo;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseGrupo }) => {
            this.desgloseGrupo = desgloseGrupo;
        });
    }

    previousState() {
        window.history.back();
    }
}
