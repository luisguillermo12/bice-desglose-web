import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';

type EntityResponseType = HttpResponse<IDesgloseAuditoria>;
type EntityArrayResponseType = HttpResponse<IDesgloseAuditoria[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseAuditoriaService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-auditorias';

    constructor(private http: HttpClient) {}

    create(desgloseAuditoria: IDesgloseAuditoria): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseAuditoria);
        return this.http
            .post<IDesgloseAuditoria>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(desgloseAuditoria: IDesgloseAuditoria): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseAuditoria);
        return this.http
            .put<IDesgloseAuditoria>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDesgloseAuditoria>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDesgloseAuditoria[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseAuditoria: IDesgloseAuditoria): IDesgloseAuditoria {
        const copy: IDesgloseAuditoria = Object.assign({}, desgloseAuditoria, {
            fecEvento:
                desgloseAuditoria.fecEvento != null && desgloseAuditoria.fecEvento.isValid()
                    ? desgloseAuditoria.fecEvento.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecEvento = res.body.fecEvento != null ? moment(res.body.fecEvento) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseAuditoria: IDesgloseAuditoria) => {
            desgloseAuditoria.fecEvento = desgloseAuditoria.fecEvento != null ? moment(desgloseAuditoria.fecEvento) : null;
        });
        return res;
    }
}
