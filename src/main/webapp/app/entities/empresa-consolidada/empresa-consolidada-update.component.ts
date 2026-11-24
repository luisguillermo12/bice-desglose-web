import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IEmpresaConsolidada } from 'app/shared/model/empresa-consolidada.model';
import { EmpresaConsolidadaService } from './empresa-consolidada.service';

@Component({
    selector: 'jhi-empresa-consolidada-update',
    templateUrl: './empresa-consolidada-update.component.html'
})
export class EmpresaConsolidadaUpdateComponent implements OnInit {
    empresaConsolidada: IEmpresaConsolidada;
    isSaving: boolean;

    constructor(private empresaConsolidadaService: EmpresaConsolidadaService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ empresaConsolidada }) => {
            this.empresaConsolidada = empresaConsolidada;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.empresaConsolidada.id !== undefined) {
            this.subscribeToSaveResponse(this.empresaConsolidadaService.update(this.empresaConsolidada));
        } else {
            this.subscribeToSaveResponse(this.empresaConsolidadaService.create(this.empresaConsolidada));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresaConsolidada>>) {
        result.subscribe((res: HttpResponse<IEmpresaConsolidada>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
