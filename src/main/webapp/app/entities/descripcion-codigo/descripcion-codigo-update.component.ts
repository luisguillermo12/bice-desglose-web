import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { DescripcionCodigoService } from './descripcion-codigo.service';
import { URL_SEGMENT_NEW_ELEMENT } from 'app/app.constants';
import { isNullOrUndefined } from 'util';

@Component({
    selector: 'jhi-descripcion-codigo-update',
    templateUrl: './descripcion-codigo-update.component.html'
})
export class DescripcionCodigoUpdateComponent implements OnInit {
    descripcionCodigo: IDescripcionCodigo;
    isSaving: boolean;
    currentRouteLastFragment: string;

    constructor(private descripcionCodigoService: DescripcionCodigoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ descripcionCodigo }) => {
            if (!isNullOrUndefined(this.activatedRoute.snapshot.url) && this.activatedRoute.snapshot.url.length > 0) {
                this.currentRouteLastFragment = this.activatedRoute.snapshot.url[this.activatedRoute.snapshot.url.length - 1].path;
            }
            this.descripcionCodigo = descripcionCodigo;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (URL_SEGMENT_NEW_ELEMENT !== this.currentRouteLastFragment) {
            this.subscribeToSaveResponse(this.descripcionCodigoService.update(this.descripcionCodigo));
        } else {
            this.subscribeToSaveResponse(this.descripcionCodigoService.create(this.descripcionCodigo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDescripcionCodigo>>) {
        result.subscribe((res: HttpResponse<IDescripcionCodigo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
