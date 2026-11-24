import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IValPeriodoDesglose } from 'app/shared/model/val-periodo-desglose.model';
import { ValPeriodoDesgloseService } from './val-periodo-desglose.service';

@Component({
    selector: 'jhi-val-periodo-desglose-update',
    templateUrl: './val-periodo-desglose-update.component.html'
})
export class ValPeriodoDesgloseUpdateComponent implements OnInit {
    valPeriodoDesglose: IValPeriodoDesglose;
    isSaving: boolean;
    fecPeriodoDp: any;

    constructor(private valPeriodoDesgloseService: ValPeriodoDesgloseService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ valPeriodoDesglose }) => {
            this.valPeriodoDesglose = valPeriodoDesglose;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.valPeriodoDesglose.id !== undefined) {
            this.subscribeToSaveResponse(this.valPeriodoDesgloseService.update(this.valPeriodoDesglose));
        } else {
            this.subscribeToSaveResponse(this.valPeriodoDesgloseService.create(this.valPeriodoDesglose));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IValPeriodoDesglose>>) {
        result.subscribe((res: HttpResponse<IValPeriodoDesglose>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
