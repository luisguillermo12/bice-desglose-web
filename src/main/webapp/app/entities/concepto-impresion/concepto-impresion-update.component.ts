import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IConceptoImpresion } from 'app/shared/model/concepto-impresion.model';
import { ConceptoImpresionService } from './concepto-impresion.service';

@Component({
    selector: 'jhi-concepto-impresion-update',
    templateUrl: './concepto-impresion-update.component.html'
})
export class ConceptoImpresionUpdateComponent implements OnInit {
    conceptoImpresion: IConceptoImpresion;
    isSaving: boolean;

    constructor(private conceptoImpresionService: ConceptoImpresionService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ conceptoImpresion }) => {
            this.conceptoImpresion = conceptoImpresion;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (
            this.conceptoImpresion.codCliente !== undefined &&
            this.conceptoImpresion.codTipoDes !== undefined &&
            this.conceptoImpresion.codSubtipoDes !== undefined &&
            this.conceptoImpresion.codMoneda !== undefined &&
            this.conceptoImpresion.codTipoConcep !== undefined &&
            this.conceptoImpresion.corConcepto !== undefined
        ) {
            this.subscribeToSaveResponse(this.conceptoImpresionService.update(this.conceptoImpresion));
        } else {
            this.subscribeToSaveResponse(this.conceptoImpresionService.create(this.conceptoImpresion));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IConceptoImpresion>>) {
        result.subscribe((res: HttpResponse<IConceptoImpresion>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
