import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { ITurbineGroup } from 'app/shared/model/turbine-group.model';
import { TurbineGroupService } from './turbine-group.service';
import { isNullOrUndefined } from 'util';
import { URL_SEGMENT_NEW_ELEMENT } from 'app/app.constants';

@Component({
    selector: 'jhi-turbine-group-update',
    templateUrl: './turbine-group-update.component.html'
})
export class TurbineGroupUpdateComponent implements OnInit {
    turbineGroup: ITurbineGroup;
    isSaving: boolean;
    currentRouteLastFragment: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private turbineGroupService: TurbineGroupService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ turbineGroup }) => {
            if (!isNullOrUndefined(this.activatedRoute.snapshot.url) && this.activatedRoute.snapshot.url.length > 0) {
                this.currentRouteLastFragment = this.activatedRoute.snapshot.url[this.activatedRoute.snapshot.url.length - 1].path;
            }
            this.turbineGroup = turbineGroup;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (URL_SEGMENT_NEW_ELEMENT !== this.currentRouteLastFragment) {
            this.subscribeToSaveResponse(this.turbineGroupService.update(this.turbineGroup));
        } else {
            this.subscribeToSaveResponse(this.turbineGroupService.create(this.turbineGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITurbineGroup>>) {
        result.subscribe((res: HttpResponse<ITurbineGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
