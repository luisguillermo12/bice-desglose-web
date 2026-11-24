import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';

@Component({
    selector: 'jhi-desglose-auditoria-detail',
    templateUrl: './desglose-auditoria-detail.component.html'
})
export class DesgloseAuditoriaDetailComponent implements OnInit {
    desgloseAuditoria: IDesgloseAuditoria;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ desgloseAuditoria }) => {
            this.desgloseAuditoria = desgloseAuditoria;
        });
    }

    previousState() {
        window.history.back();
    }
}
