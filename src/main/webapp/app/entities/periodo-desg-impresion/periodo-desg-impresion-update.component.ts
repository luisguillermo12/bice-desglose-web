import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPeriodoDesgImpresion } from 'app/shared/model/periodo-desg-impresion.model';
import { PeriodoDesgImpresionService } from './periodo-desg-impresion.service';

@Component({
    selector: 'jhi-periodo-desg-impresion-update',
    templateUrl: './periodo-desg-impresion-update.component.html'
})
export class PeriodoDesgImpresionUpdateComponent implements OnInit {
    periodoDesgImpresion: IPeriodoDesgImpresion;
    isSaving: boolean;
    fecPeriodoDp: any;

    constructor(private periodoDesgImpresionService: PeriodoDesgImpresionService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ periodoDesgImpresion }) => {
            this.periodoDesgImpresion = periodoDesgImpresion;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.periodoDesgImpresion.codCliente !== undefined) {
            this.subscribeToSaveResponse(this.periodoDesgImpresionService.update(this.periodoDesgImpresion));
        } else {
            this.subscribeToSaveResponse(this.periodoDesgImpresionService.create(this.periodoDesgImpresion));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodoDesgImpresion>>) {
        result.subscribe(
            (res: HttpResponse<IPeriodoDesgImpresion>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
