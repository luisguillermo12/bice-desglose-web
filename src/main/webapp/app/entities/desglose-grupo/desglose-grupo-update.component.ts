import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';
import { DesgloseGrupoService } from './desglose-grupo.service';

@Component({
    selector: 'jhi-desglose-grupo-update',
    templateUrl: './desglose-grupo-update.component.html'
})
export class DesgloseGrupoUpdateComponent implements OnInit {
    desgloseGrupo: IDesgloseGrupo;
    isSaving: boolean;

    constructor(private desgloseGrupoService: DesgloseGrupoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseGrupo }) => {
            this.desgloseGrupo = desgloseGrupo;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.desgloseGrupo.codCargo !== undefined) {
            this.subscribeToSaveResponse(this.desgloseGrupoService.update(this.desgloseGrupo));
        } else {
            this.subscribeToSaveResponse(this.desgloseGrupoService.create(this.desgloseGrupo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseGrupo>>) {
        result.subscribe((res: HttpResponse<IDesgloseGrupo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
