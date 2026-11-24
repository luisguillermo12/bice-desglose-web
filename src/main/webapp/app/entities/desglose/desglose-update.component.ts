import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IDesglose } from 'app/shared/model/desglose.model';
import { DesgloseService } from './desglose.service';
import { isNullOrUndefined } from 'util';
import { URL_SEGMENT_NEW_ELEMENT } from 'app/app.constants';

@Component({
    selector: 'jhi-desglose-update',
    templateUrl: './desglose-update.component.html'
})
export class DesgloseUpdateComponent implements OnInit {
    desglose: IDesglose;
    isSaving: boolean;
    fecUserCreaDp: any;
    fecUserModifDp: any;
    fecDeflacBaseDp: any;
    fecHraOpenDp: any;
    fecDesgloDefDp: any;
    fecDesgloEnvPdfDp: any;
    fecPeriodo1RptDp: any;
    fecPeriodo2RptDp: any;
    fecPeriodo3RptDp: any;
    fecPeriodo4RptDp: any;
    fecPerConsol1RptDp: any;
    fecPerConsol2RptDp: any;
    fecUlt1RptDp: any;
    fecUlt2RptDp: any;
    fecUserModifRptDp: any;
    fecGeneracionRptDp: any;
    currentRouteLastFragment: string;

    constructor(private desgloseService: DesgloseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ desglose }) => {
            if (!isNullOrUndefined(this.activatedRoute.snapshot.url) && this.activatedRoute.snapshot.url.length > 0) {
                this.currentRouteLastFragment = this.activatedRoute.snapshot.url[this.activatedRoute.snapshot.url.length - 1].path;
            }
            this.desglose = desglose;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (URL_SEGMENT_NEW_ELEMENT !== this.currentRouteLastFragment) {
            this.subscribeToSaveResponse(this.desgloseService.update(this.desglose));
        } else {
            this.subscribeToSaveResponse(this.desgloseService.create(this.desglose));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDesglose>>) {
        result.subscribe((res: HttpResponse<IDesglose>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
