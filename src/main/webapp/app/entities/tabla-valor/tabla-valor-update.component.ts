import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { ITablaValor } from 'app/shared/model/tabla-valor.model';
import { TablaValorService } from './tabla-valor.service';

@Component({
    selector: 'jhi-tabla-valor-update',
    templateUrl: './tabla-valor-update.component.html'
})
export class TablaValorUpdateComponent implements OnInit {
    tablaValor: ITablaValor;
    isSaving: boolean;
    fecValidezDp: any;

    constructor(private tablaValorService: TablaValorService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tablaValor }) => {
            this.tablaValor = tablaValor;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.tablaValor.id !== undefined) {
            this.subscribeToSaveResponse(this.tablaValorService.update(this.tablaValor));
        } else {
            this.subscribeToSaveResponse(this.tablaValorService.create(this.tablaValor));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITablaValor>>) {
        result.subscribe((res: HttpResponse<ITablaValor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
