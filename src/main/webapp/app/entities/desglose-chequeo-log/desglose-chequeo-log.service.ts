import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseChequeoLog } from 'app/shared/model/desglose-chequeo-log.model';

type EntityResponseType = HttpResponse<IDesgloseChequeoLog>;
type EntityArrayResponseType = HttpResponse<IDesgloseChequeoLog[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseChequeoLogService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-chequeo-logs';

    constructor(private http: HttpClient) {}

    create(desgloseChequeoLog: IDesgloseChequeoLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseChequeoLog);
        return this.http
            .post<IDesgloseChequeoLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(desgloseChequeoLog: IDesgloseChequeoLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseChequeoLog);
        return this.http
            .put<IDesgloseChequeoLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDesgloseChequeoLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDesgloseChequeoLog[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseChequeoLog: IDesgloseChequeoLog): IDesgloseChequeoLog {
        const copy: IDesgloseChequeoLog = Object.assign({}, desgloseChequeoLog, {
            fecLog: desgloseChequeoLog.fecLog != null && desgloseChequeoLog.fecLog.isValid() ? desgloseChequeoLog.fecLog.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecLog = res.body.fecLog != null ? moment(res.body.fecLog) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseChequeoLog: IDesgloseChequeoLog) => {
            desgloseChequeoLog.fecLog = desgloseChequeoLog.fecLog != null ? moment(desgloseChequeoLog.fecLog) : null;
        });
        return res;
    }
}
