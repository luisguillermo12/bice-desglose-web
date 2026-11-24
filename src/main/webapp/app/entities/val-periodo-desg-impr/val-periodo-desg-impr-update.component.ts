import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IValPeriodoDesgImpr } from 'app/shared/model/val-periodo-desg-impr.model';
import { ValPeriodoDesgImprService } from './val-periodo-desg-impr.service';

@Component({
    selector: 'jhi-val-periodo-desg-impr-update',
    templateUrl: './val-periodo-desg-impr-update.component.html'
})
export class ValPeriodoDesgImprUpdateComponent implements OnInit {
    valPeriodoDesgImpr: IValPeriodoDesgImpr;
    isSaving: boolean;
    fecPeriodoDp: any;

    constructor(private valPeriodoDesgImprService: ValPeriodoDesgImprService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ valPeriodoDesgImpr }) => {
            this.valPeriodoDesgImpr = valPeriodoDesgImpr;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.valPeriodoDesgImpr.codCliente !== undefined) {
            this.subscribeToSaveResponse(this.valPeriodoDesgImprService.update(this.valPeriodoDesgImpr));
        } else {
            this.subscribeToSaveResponse(this.valPeriodoDesgImprService.create(this.valPeriodoDesgImpr));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IValPeriodoDesgImpr>>) {
        result.subscribe((res: HttpResponse<IValPeriodoDesgImpr>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
