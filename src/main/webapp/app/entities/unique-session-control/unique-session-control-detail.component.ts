import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUniqueSessionControl } from 'app/shared/model/unique-session-control.model';

@Component({
    selector: 'jhi-unique-session-control-detail',
    templateUrl: './unique-session-control-detail.component.html'
})
export class UniqueSessionControlDetailComponent implements OnInit {
    uniqueSessionControl: IUniqueSessionControl;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ uniqueSessionControl }) => {
            this.uniqueSessionControl = uniqueSessionControl;
        });
    }

    previousState() {
        window.history.back();
    }
}
