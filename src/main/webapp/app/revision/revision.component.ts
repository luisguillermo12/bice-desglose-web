import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
    selector: 'jhi-revision',
    templateUrl: './revision.component.html',
    styleUrls: [ 'revision.scss' ]
})
export class RevisionComponent implements OnInit {
    isExternal: boolean;

    constructor(private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.activatedRoute.params.subscribe((data: Params) => {
            this.isExternal = data[ 'isExternal' ];
        });

    }
}
