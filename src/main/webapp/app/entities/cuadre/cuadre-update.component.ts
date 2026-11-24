import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICuadre } from 'app/shared/model/cuadre.model';
import { CuadreService } from './cuadre.service';

@Component({
    selector: 'jhi-cuadre-update',
    templateUrl: './cuadre-update.component.html'
})
export class CuadreUpdateComponent implements OnInit {
    cuadre: ICuadre;
    isSaving: boolean;

    constructor(private cuadreService: CuadreService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cuadre }) => {
            this.cuadre = cuadre;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cuadre.codTpoConcep1 !== undefined) {
            this.subscribeToSaveResponse(this.cuadreService.update(this.cuadre));
        } else {
            this.subscribeToSaveResponse(this.cuadreService.create(this.cuadre));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICuadre>>) {
        result.subscribe((res: HttpResponse<ICuadre>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
