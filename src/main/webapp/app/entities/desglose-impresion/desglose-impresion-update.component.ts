import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IDesgloseImpresion } from 'app/shared/model/desglose-impresion.model';
import { DesgloseImpresionService } from './desglose-impresion.service';

@Component({
    selector: 'jhi-desglose-impresion-update',
    templateUrl: './desglose-impresion-update.component.html'
})
export class DesgloseImpresionUpdateComponent implements OnInit {
    desgloseImpresion: IDesgloseImpresion;
    isSaving: boolean;
    fecCreacionDp: any;
    fecDeflacBaseDp: any;

    constructor(private desgloseImpresionService: DesgloseImpresionService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseImpresion }) => {
            this.desgloseImpresion = desgloseImpresion;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.desgloseImpresion.codCliente !== undefined) {
            this.subscribeToSaveResponse(this.desgloseImpresionService.update(this.desgloseImpresion));
        } else {
            this.subscribeToSaveResponse(this.desgloseImpresionService.create(this.desgloseImpresion));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseImpresion>>) {
        result.subscribe((res: HttpResponse<IDesgloseImpresion>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
