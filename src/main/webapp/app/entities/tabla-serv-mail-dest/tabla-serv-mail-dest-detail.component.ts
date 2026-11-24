import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITablaServMailDest } from 'app/shared/model/tabla-serv-mail-dest.model';

@Component({
    selector: 'jhi-tabla-serv-mail-dest-detail',
    templateUrl: './tabla-serv-mail-dest-detail.component.html'
})
export class TablaServMailDestDetailComponent implements OnInit {
    tablaServMailDest: ITablaServMailDest;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tablaServMailDest }) => {
            this.tablaServMailDest = tablaServMailDest;
        });
    }

    previousState() {
        window.history.back();
    }
}
