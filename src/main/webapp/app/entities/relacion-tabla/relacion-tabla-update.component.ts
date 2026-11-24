import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRelacionTabla } from 'app/shared/model/relacion-tabla.model';
import { RelacionTablaService } from './relacion-tabla.service';

@Component({
    selector: 'jhi-relacion-tabla-update',
    templateUrl: './relacion-tabla-update.component.html'
})
export class RelacionTablaUpdateComponent implements OnInit {
    relacionTabla: IRelacionTabla;
    isSaving: boolean;

    constructor(private relacionTablaService: RelacionTablaService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ relacionTabla }) => {
            this.relacionTabla = relacionTabla;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.relacionTabla.id !== undefined) {
            this.subscribeToSaveResponse(this.relacionTablaService.update(this.relacionTabla));
        } else {
            this.subscribeToSaveResponse(this.relacionTablaService.create(this.relacionTabla));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRelacionTabla>>) {
        result.subscribe((res: HttpResponse<IRelacionTabla>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
