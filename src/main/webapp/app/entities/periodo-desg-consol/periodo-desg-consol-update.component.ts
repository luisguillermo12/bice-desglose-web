import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPeriodoDesgConsol } from 'app/shared/model/periodo-desg-consol.model';
import { PeriodoDesgConsolService } from './periodo-desg-consol.service';

@Component({
    selector: 'jhi-periodo-desg-consol-update',
    templateUrl: './periodo-desg-consol-update.component.html'
})
export class PeriodoDesgConsolUpdateComponent implements OnInit {
    periodoDesgConsol: IPeriodoDesgConsol;
    isSaving: boolean;
    fecPeriodoDp: any;

    constructor(private periodoDesgConsolService: PeriodoDesgConsolService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ periodoDesgConsol }) => {
            this.periodoDesgConsol = periodoDesgConsol;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.periodoDesgConsol.id !== undefined) {
            this.subscribeToSaveResponse(this.periodoDesgConsolService.update(this.periodoDesgConsol));
        } else {
            this.subscribeToSaveResponse(this.periodoDesgConsolService.create(this.periodoDesgConsol));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodoDesgConsol>>) {
        result.subscribe((res: HttpResponse<IPeriodoDesgConsol>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
