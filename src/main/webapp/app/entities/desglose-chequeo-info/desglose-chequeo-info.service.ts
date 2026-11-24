import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseChequeoInfo } from 'app/shared/model/desglose-chequeo-info.model';

type EntityResponseType = HttpResponse<IDesgloseChequeoInfo>;
type EntityArrayResponseType = HttpResponse<IDesgloseChequeoInfo[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseChequeoInfoService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-chequeo-infos';

    constructor(private http: HttpClient) {}

    create(desgloseChequeoInfo: IDesgloseChequeoInfo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseChequeoInfo);
        return this.http
            .post<IDesgloseChequeoInfo>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(desgloseChequeoInfo: IDesgloseChequeoInfo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseChequeoInfo);
        return this.http
            .put<IDesgloseChequeoInfo>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDesgloseChequeoInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDesgloseChequeoInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseChequeoInfo: IDesgloseChequeoInfo): IDesgloseChequeoInfo {
        const copy: IDesgloseChequeoInfo = Object.assign({}, desgloseChequeoInfo, {
            fecCheck:
                desgloseChequeoInfo.fecCheck != null && desgloseChequeoInfo.fecCheck.isValid()
                    ? desgloseChequeoInfo.fecCheck.toJSON()
                    : null,
            fecPeriodo:
                desgloseChequeoInfo.fecPeriodo != null && desgloseChequeoInfo.fecPeriodo.isValid()
                    ? desgloseChequeoInfo.fecPeriodo.format(DATE_FORMAT)
                    : null,
            fecUserModif:
                desgloseChequeoInfo.fecUserModif != null && desgloseChequeoInfo.fecUserModif.isValid()
                    ? desgloseChequeoInfo.fecUserModif.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecCheck = res.body.fecCheck != null ? moment(res.body.fecCheck) : null;
        res.body.fecPeriodo = res.body.fecPeriodo != null ? moment(res.body.fecPeriodo) : null;
        res.body.fecUserModif = res.body.fecUserModif != null ? moment(res.body.fecUserModif) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseChequeoInfo: IDesgloseChequeoInfo) => {
            desgloseChequeoInfo.fecCheck = desgloseChequeoInfo.fecCheck != null ? moment(desgloseChequeoInfo.fecCheck) : null;
            desgloseChequeoInfo.fecPeriodo = desgloseChequeoInfo.fecPeriodo != null ? moment(desgloseChequeoInfo.fecPeriodo) : null;
            desgloseChequeoInfo.fecUserModif = desgloseChequeoInfo.fecUserModif != null ? moment(desgloseChequeoInfo.fecUserModif) : null;
        });
        return res;
    }
}
