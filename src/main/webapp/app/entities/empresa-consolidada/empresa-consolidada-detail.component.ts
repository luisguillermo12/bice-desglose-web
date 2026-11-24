import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmpresaConsolidada } from 'app/shared/model/empresa-consolidada.model';

@Component({
    selector: 'jhi-empresa-consolidada-detail',
    templateUrl: './empresa-consolidada-detail.component.html'
})
export class EmpresaConsolidadaDetailComponent implements OnInit {
    empresaConsolidada: IEmpresaConsolidada;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ empresaConsolidada }) => {
            this.empresaConsolidada = empresaConsolidada;
        });
    }

    previousState() {
        window.history.back();
    }
}
