import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseImpresion } from 'app/shared/model/desglose-impresion.model';

type EntityResponseType = HttpResponse<IDesgloseImpresion>;
type EntityArrayResponseType = HttpResponse<IDesgloseImpresion[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseImpresionService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-impresions';
    private resourceUrlCifSpInsDes = SERVER_API_URL + 'api/desglose-impresions-executeCifSpInsDes';
    constructor(private http: HttpClient) {}

    create(desgloseImpresion: IDesgloseImpresion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseImpresion);
        return this.http
            .post<IDesgloseImpresion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    CifSpInsDes(desgloseImpresion: IDesgloseImpresion): Observable<HttpResponse<number>> {
        const copy = this.convertDateFromClient(desgloseImpresion);
        return this.http
        .post<number>(this.resourceUrlCifSpInsDes, copy, { observe: 'response' });
    }
    update(desgloseImpresion: IDesgloseImpresion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseImpresion);
        return this.http
            .put<IDesgloseImpresion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDesgloseImpresion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDesgloseImpresion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseImpresion: IDesgloseImpresion): IDesgloseImpresion {
        const copy: IDesgloseImpresion = Object.assign({}, desgloseImpresion, {
            fecCreacion:
                desgloseImpresion.fecCreacion != null && desgloseImpresion.fecCreacion.isValid()
                    ? desgloseImpresion.fecCreacion.format(DATE_FORMAT)
                    : null,
            fecDeflacBase:
                desgloseImpresion.fecDeflacBase != null && desgloseImpresion.fecDeflacBase.isValid()
                    ? desgloseImpresion.fecDeflacBase.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecCreacion = res.body.fecCreacion != null ? moment(res.body.fecCreacion) : null;
        res.body.fecDeflacBase = res.body.fecDeflacBase != null ? moment(res.body.fecDeflacBase) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseImpresion: IDesgloseImpresion) => {
            desgloseImpresion.fecCreacion = desgloseImpresion.fecCreacion != null ? moment(desgloseImpresion.fecCreacion) : null;
            desgloseImpresion.fecDeflacBase = desgloseImpresion.fecDeflacBase != null ? moment(desgloseImpresion.fecDeflacBase) : null;
        });
        return res;
    }
}
