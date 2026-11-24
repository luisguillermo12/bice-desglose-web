import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ITablaServMailDest } from 'app/shared/model/tabla-serv-mail-dest.model';
import { TablaServMailDestService } from './tabla-serv-mail-dest.service';

@Component({
    selector: 'jhi-tabla-serv-mail-dest-update',
    templateUrl: './tabla-serv-mail-dest-update.component.html'
})
export class TablaServMailDestUpdateComponent implements OnInit {
    tablaServMailDest: ITablaServMailDest;
    isSaving: boolean;

    constructor(private tablaServMailDestService: TablaServMailDestService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tablaServMailDest }) => {
            this.tablaServMailDest = tablaServMailDest;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.tablaServMailDest.numSolicitud !== undefined) {
            this.subscribeToSaveResponse(this.tablaServMailDestService.update(this.tablaServMailDest));
        } else {
            this.subscribeToSaveResponse(this.tablaServMailDestService.create(this.tablaServMailDest));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITablaServMailDest>>) {
        result.subscribe((res: HttpResponse<ITablaServMailDest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
