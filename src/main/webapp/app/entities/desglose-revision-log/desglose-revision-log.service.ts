import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseRevisionLog } from 'app/shared/model/desglose-revision-log.model';

type EntityResponseType = HttpResponse<IDesgloseRevisionLog>;
type EntityArrayResponseType = HttpResponse<IDesgloseRevisionLog[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseRevisionLogService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-revision-logs';

    constructor(private http: HttpClient) {}

    create(desgloseRevisionLog: IDesgloseRevisionLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseRevisionLog);
        return this.http
            .post<IDesgloseRevisionLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(desgloseRevisionLog: IDesgloseRevisionLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseRevisionLog);
        return this.http
            .put<IDesgloseRevisionLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDesgloseRevisionLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDesgloseRevisionLog[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseRevisionLog: IDesgloseRevisionLog): IDesgloseRevisionLog {
        const copy: IDesgloseRevisionLog = Object.assign({}, desgloseRevisionLog, {
            fecEvent:
                desgloseRevisionLog.fecEvent != null && desgloseRevisionLog.fecEvent.isValid()
                    ? desgloseRevisionLog.fecEvent.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecEvent = res.body.fecEvent != null ? moment(res.body.fecEvent) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseRevisionLog: IDesgloseRevisionLog) => {
            desgloseRevisionLog.fecEvent = desgloseRevisionLog.fecEvent != null ? moment(desgloseRevisionLog.fecEvent) : null;
        });
        return res;
    }
}
