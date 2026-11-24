import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { SessionStorageService } from 'ngx-webstorage';
import { ITurbineUser } from 'app/shared/model/turbine-user.model';
import { SESSION_VARIABLE_USUARIO } from 'app/app.constants';

@Component({
    selector: 'jhi-query',
    templateUrl: './query.component.html',
    styleUrls: [ 'query.scss' ]
})
export class QueryComponent implements OnInit {

    isExternal: boolean;
    usuario: ITurbineUser;

    constructor(private activatedRoute: ActivatedRoute,
                private sessionStorage: SessionStorageService) {
    }

    ngOnInit() {
        this.activatedRoute.params.subscribe((data: Params) => {
            this.isExternal = data[ 'isExternal' ];
        });
        this.usuario = this.sessionStorage.retrieve(SESSION_VARIABLE_USUARIO);
    }
}
