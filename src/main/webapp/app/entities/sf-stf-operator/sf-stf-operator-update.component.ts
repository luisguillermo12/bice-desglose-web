import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISfStfOperator } from 'app/shared/model/sf-stf-operator.model';
import { SfStfOperatorService } from './sf-stf-operator.service';

@Component({
    selector: 'jhi-sf-stf-operator-update',
    templateUrl: './sf-stf-operator-update.component.html'
})
export class SfStfOperatorUpdateComponent implements OnInit {
    sfStfOperator: ISfStfOperator;
    isSaving: boolean;

    constructor(private sfStfOperatorService: SfStfOperatorService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sfStfOperator }) => {
            this.sfStfOperator = sfStfOperator;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.sfStfOperator.operatorCode !== undefined) {
            this.subscribeToSaveResponse(this.sfStfOperatorService.update(this.sfStfOperator));
        } else {
            this.subscribeToSaveResponse(this.sfStfOperatorService.create(this.sfStfOperator));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISfStfOperator>>) {
        result.subscribe((res: HttpResponse<ISfStfOperator>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
