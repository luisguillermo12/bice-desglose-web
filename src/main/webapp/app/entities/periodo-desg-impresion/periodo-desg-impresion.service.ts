import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPeriodoDesgImpresion } from 'app/shared/model/periodo-desg-impresion.model';

type EntityResponseType = HttpResponse<IPeriodoDesgImpresion>;
type EntityArrayResponseType = HttpResponse<IPeriodoDesgImpresion[]>;

@Injectable({ providedIn: 'root' })
export class PeriodoDesgImpresionService {
    private resourceUrl = SERVER_API_URL + 'api/periodo-desg-impresions';
    private  resourceUrlCifSpInsPrdImp = this.resourceUrl + '/executeCifSpInsPrdImp';
    constructor(private http: HttpClient) {}

    create(periodoDesgImpresion: IPeriodoDesgImpresion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(periodoDesgImpresion);
        return this.http
            .post<IPeriodoDesgImpresion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    CifSpInsPrdImp(periodoDesgImpresion: IPeriodoDesgImpresion[]): Observable<any> {
        periodoDesgImpresion.forEach((data,index)=> {
            const copy = this.convertDateFromClient(data);
            periodoDesgImpresion[index] = copy;
        });
        return this.http
        .post<IPeriodoDesgImpresion>(this.resourceUrlCifSpInsPrdImp, periodoDesgImpresion, { observe: 'response' });
    }
    update(periodoDesgImpresion: IPeriodoDesgImpresion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(periodoDesgImpresion);
        return this.http
            .put<IPeriodoDesgImpresion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPeriodoDesgImpresion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPeriodoDesgImpresion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(periodoDesgImpresion: IPeriodoDesgImpresion): IPeriodoDesgImpresion {
        const copy: IPeriodoDesgImpresion = Object.assign({}, periodoDesgImpresion, {
            fecPeriodo:
                periodoDesgImpresion.fecPeriodo != null && periodoDesgImpresion.fecPeriodo.isValid()
                    ? periodoDesgImpresion.fecPeriodo.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecPeriodo = res.body.fecPeriodo != null ? moment(res.body.fecPeriodo) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((periodoDesgImpresion: IPeriodoDesgImpresion) => {
            periodoDesgImpresion.fecPeriodo = periodoDesgImpresion.fecPeriodo != null ? moment(periodoDesgImpresion.fecPeriodo) : null;
        });
        return res;
    }
}
