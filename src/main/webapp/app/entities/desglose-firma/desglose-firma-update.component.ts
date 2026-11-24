import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IDesgloseFirma } from 'app/shared/model/desglose-firma.model';
import { DesgloseFirmaService } from './desglose-firma.service';

@Component({
    selector: 'jhi-desglose-firma-update',
    templateUrl: './desglose-firma-update.component.html'
})
export class DesgloseFirmaUpdateComponent implements OnInit {
    desgloseFirma: IDesgloseFirma;
    isSaving: boolean;

    constructor(private desgloseFirmaService: DesgloseFirmaService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desgloseFirma }) => {
            this.desgloseFirma = desgloseFirma;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.desgloseFirma.codCargo !== undefined) {
            this.subscribeToSaveResponse(this.desgloseFirmaService.update(this.desgloseFirma));
        } else {
            this.subscribeToSaveResponse(this.desgloseFirmaService.create(this.desgloseFirma));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesgloseFirma>>) {
        result.subscribe((res: HttpResponse<IDesgloseFirma>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
